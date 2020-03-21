/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.Query;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.OnlineTestCase;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/**
 * Testing using a Postgis database for storing the index for the ImageMosaic
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class ImageMosaicPostgisIndexOnlineTest extends OnlineTestCase {

    private static final Logger LOGGER = Logging.getLogger(ImageMosaicPostgisIndexOnlineTest.class);

    static final String tempFolderNoEpsg = "rgbNoEpsg";

    static final String tempFolderName1 = "waterTempPG";

    static final String tempFolderName2 = "waterTempPG2";

    static final String tempFolderName3 = "waterTempPG3";

    static final String tempFolderName4 = "waterTempPGCD";

    static final String tempFolderNameWrap = "waterTempPGWrap";

    static final String VERY_LONG_NAME =
            "very_very_long_name_with_number_of_chars_greater_than_64_to_test_the_postgis_wrapper";
    /**
     * Simple Class for better testing raster manager
     *
     * @author Simone Giannecchini, GeoSolutions SAS
     */
    private static class MyImageMosaicReader extends ImageMosaicReader {

        public MyImageMosaicReader(Object source) throws IOException {
            super(source);
        }

        public MyImageMosaicReader(Object source, Hints uHints) throws IOException {
            super(source, uHints);
        }
    }

    @Override
    protected Properties createExampleFixture() {
        // create sample properties file for postgis datastore
        final Properties props = new Properties();
        props.setProperty("SPI", "org.geotools.data.postgis.PostgisNGDataStoreFactory");
        props.setProperty("host", "localhost");
        props.setProperty("port", "5432");
        props.setProperty("user", "xxx");
        props.setProperty("passwd", "xxx");
        props.setProperty("database", "ddd");
        props.setProperty("schema", "public");
        props.setProperty("Loose bbox", "true");
        props.setProperty("Estimated extends", "false");
        props.setProperty("validate connections", "true");
        props.setProperty("Connection timeout", "10");
        props.setProperty("preparedStatements", "false");
        props.setProperty("create database params", "WITH TEMPLATE=template_postgis");
        return props;
    }

    /* (non-Javadoc)
     * @see org.geotools.test.OnlineTestCase#getFixtureId()
     */
    @Override
    protected String getFixtureId() {
        return "postgis_datastore";
    }

    // name of a table without geometry
    //
    private final String noGeomFirst = "wNoGeom";

    private final String noGeomLast = "zNotGeom";

    /** Complex test for Postgis indexing on db. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testPostgisIndexing() throws Exception {
        final File workDir = new File(TestData.file(this, "."), tempFolderName1);
        assertTrue(workDir.mkdir());
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, tempFolderName1 + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, tempFolderName1);

        setupDataStoreProperties(tempFolderName1);

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(12, metadataNames.length);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));

        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
        assertNotNull(elevationMetadata);
        assertEquals(2, elevationMetadata.split(",").length);
        assertEquals(
                Double.parseDouble(elevationMetadata.split(",")[0]),
                Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM")),
                1E-6);
        assertEquals(
                Double.parseDouble(elevationMetadata.split(",")[1]),
                Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM")),
                1E-6);

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});

        final ParameterValue<Boolean> direct = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        direct.setValue(false);

        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(Arrays.asList(100.0));

        // Test the output coverage
        assertNotNull(reader.read(new GeneralParameterValue[] {gg, time, bkg, elevation, direct}));
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");

        // Test the output coverage
        reader = TestUtils.getReader(timeElevURL, format);
        elevation.setValue(Arrays.asList(NumberRange.create(0.0, 10.0)));
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");

        reader.dispose();
    }

    /** Complex test for Postgis indexing on db. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testPostgisIndexingNoEpsgCode() throws Exception {
        final File workDir = new File(TestData.file(this, "."), tempFolderNoEpsg);
        workDir.mkdir();
        assertTrue(workDir.exists());
        FileUtils.copyFile(
                TestData.file(this, "rgb_noepsg.zip"), new File(workDir, "rgb_noepsg.zip"));
        TestData.unzipFile(this, tempFolderNoEpsg + "/rgb_noepsg.zip");
        final URL noEpsgURL = TestData.url(this, tempFolderNoEpsg);

        setupDataStoreProperties(tempFolderNoEpsg);

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(noEpsgURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(noEpsgURL, format);
        // used to blow up
        assertNotNull(reader);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testPostgisCreateAndDrop() throws Exception {
        final File workDir = new File(TestData.file(this, "."), tempFolderName4);
        workDir.mkdir();
        assertTrue(workDir.exists());
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, tempFolderName4 + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, tempFolderName4);

        setupDataStoreProperties(tempFolderName4);

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);
        reader.delete(true);
        boolean dropSuccessfull = false;
        try {
            dropTables(new String[] {tempFolderName4}, "samplecreate2");
            dropSuccessfull = true;
        } catch (SQLException E) {
            // The tables have been already deleted with the database drop performed
            // by the delete operation.
            assertFalse(dropSuccessfull);
        }
        reader.dispose();
    }

    private void setupDataStoreProperties(String folder) throws IOException, FileNotFoundException {
        // place datastore.properties file in the dir for the indexing
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), folder + "/datastore.properties"))) {
            final Set<Object> keyset = fixture.keySet();
            for (Object key : keyset) {
                final String key_ = (String) key;
                String value = fixture.getProperty(key_);
                if (key_.equalsIgnoreCase("database")) {
                    value = "samplecreate2";
                }

                out.write(key_.replace(" ", "\\ ") + "=" + value.replace(" ", "\\ ") + "\n");
            }
            out.flush();
        }
    }

    /** Complex test for Postgis indexing on db. */
    @Test
    public void testSortingAndLimiting() throws Exception {
        final File workDir = new File(TestData.file(this, "."), tempFolderName2);
        assertTrue(workDir.mkdir());
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, tempFolderName2 + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, tempFolderName2);

        setupDataStoreProperties(tempFolderName2);

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format);
        assertNotNull(reader);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(12, metadataNames.length);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));

        // dispose and create new reader
        reader.dispose();
        final MyImageMosaicReader reader1 = new MyImageMosaicReader(timeElevURL);
        final RasterManager rasterManager =
                reader1.getRasterManager(reader1.getGridCoverageNames()[0]);

        // query
        final SimpleFeatureType type = rasterManager.granuleCatalog.getType("waterTempPG2");
        Query query = null;
        if (type != null) {
            // creating query
            query = new Query(type.getTypeName());

            // sorting and limiting
            // max number of elements
            query.setMaxFeatures(1);

            // sorting
            final SortBy[] clauses =
                    new SortBy[] {
                        new SortByImpl(
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property("ingestion"),
                                SortOrder.DESCENDING),
                        new SortByImpl(
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property("elevation"),
                                SortOrder.ASCENDING),
                    };
            query.setSortBy(clauses);
        }

        // checking that we get a single feature and that feature is correct
        final Collection<GranuleDescriptor> features = new ArrayList<GranuleDescriptor>();
        rasterManager.getGranuleDescriptors(
                query,
                new GranuleCatalogVisitor() {

                    @Override
                    public void visit(GranuleDescriptor granule, SimpleFeature o) {
                        features.add(granule);
                    }
                });
        assertEquals(features.size(), 1);
        GranuleDescriptor granule = features.iterator().next();
        SimpleFeature sf = granule.getOriginator();
        assertNotNull(sf);
        Object ingestion = sf.getAttribute("ingestion");
        assertTrue(ingestion instanceof Timestamp);
        final GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        gc.setTimeInMillis(1225497600000l);
        assertEquals(0, (((Timestamp) ingestion).compareTo(gc.getTime())));
        Object elevation = sf.getAttribute("elevation");
        assertTrue(elevation instanceof Integer);
        assertEquals(((Integer) elevation).intValue(), 0);

        // Reverting order (the previous timestamp shouldn't match anymore)
        final SortBy[] clauses =
                new SortBy[] {
                    new SortByImpl(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property("ingestion"),
                            SortOrder.ASCENDING),
                    new SortByImpl(
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property("elevation"),
                            SortOrder.DESCENDING),
                };
        query.setSortBy(clauses);

        // checking that we get a single feature and that feature is correct
        features.clear();
        rasterManager.getGranuleDescriptors(
                query,
                new GranuleCatalogVisitor() {

                    @Override
                    public void visit(GranuleDescriptor granule, SimpleFeature o) {
                        features.add(granule);
                    }
                });
        assertEquals(features.size(), 1);
        granule = features.iterator().next();
        sf = granule.getOriginator();
        assertNotNull(sf);
        ingestion = sf.getAttribute("ingestion");
        assertTrue(ingestion instanceof Timestamp);
        assertNotSame(0, (((Timestamp) ingestion).compareTo(gc.getTime())));
        elevation = sf.getAttribute("elevation");
        assertTrue(elevation instanceof Integer);
        assertNotSame(((Integer) elevation).intValue(), 0);
        reader1.dispose();
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();

        // make sure CRS ordering is correct
        System.setProperty("org.geotools.referencing.forceXY", "true");
        System.setProperty("user.timezone", "GMT");
    }

    private void dropTables(String[] tables) throws Exception {
        dropTables(tables, null);
    }

    private void dropTables(String[] tables, String database) throws Exception {
        // delete tables
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        Statement st = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:postgresql://"
                                    + fixture.getProperty("host")
                                    + ":"
                                    + fixture.getProperty("port")
                                    + "/"
                                    + (database != null
                                            ? database
                                            : fixture.getProperty("database")),
                            fixture.getProperty("user"),
                            fixture.getProperty("passwd"));
            st = connection.createStatement();
            for (String table : tables) {
                st.execute("DROP TABLE IF EXISTS \"" + table + "\"");
            }
        } finally {

            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /** Complex test for Postgis store wrapping. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testPostgisWrapping() throws Exception {
        final File workDir = new File(TestData.file(this, "."), tempFolderNameWrap);
        assertTrue(workDir.mkdir());
        FileUtils.copyFile(
                TestData.file(this, "watertemplongnames.zip"),
                new File(workDir, "watertemplongnames.zip"));
        TestData.unzipFile(this, tempFolderNameWrap + "/watertemplongnames.zip");
        final URL dataUrl = TestData.url(this, tempFolderNameWrap);

        setupDataStoreProperties(tempFolderNameWrap);

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(dataUrl, null);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(dataUrl, format, null);
        assertNotNull(reader);

        final String[] metadataNames = reader.getMetadataNames();
        String[] coverageNames = reader.getGridCoverageNames();
        String coverageName = coverageNames[0];
        assertEquals(VERY_LONG_NAME, coverageName);
        List<DimensionDescriptor> descriptors = reader.getDimensionDescriptors(coverageName);
        for (DimensionDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            if (name.equalsIgnoreCase("time")) {
                assertTrue(descriptor.getStartAttribute().length() > 64);
                break;
            }
        }

        assertNotNull(metadataNames);
        assertEquals(12, metadataNames.length);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));

        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
        assertNotNull(elevationMetadata);
        assertEquals(2, elevationMetadata.split(",").length);
        assertEquals(
                Double.parseDouble(elevationMetadata.split(",")[0]),
                Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM")),
                1E-6);
        assertEquals(
                Double.parseDouble(elevationMetadata.split(",")[1]),
                Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM")),
                1E-6);

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});

        final ParameterValue<Boolean> direct = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        direct.setValue(false);

        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(Arrays.asList(100.0));

        // Test the output coverage
        assertNotNull(reader.read(new GeneralParameterValue[] {gg, time, bkg, elevation, direct}));
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");

        // Test the output coverage
        reader = TestUtils.getReader(dataUrl, format, null);
        elevation.setValue(Arrays.asList(NumberRange.create(0.0, 10.0)));
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");
        reader.dispose();
    }

    @Override
    protected void tearDownInternal() throws Exception {

        // delete tables
        dropTables(
                new String[] {
                    tempFolderNoEpsg,
                    tempFolderName1,
                    tempFolderName2,
                    noGeomLast,
                    noGeomFirst,
                    tempFolderName3,
                    VERY_LONG_NAME.substring(0, 63)
                });

        System.clearProperty("org.geotools.referencing.forceXY");

        // clean up disk
        if (!ImageMosaicReaderTest.INTERACTIVE) {
            File parent = TestData.file(this, ".");
            for (String name :
                    Arrays.asList(
                            tempFolderName1,
                            tempFolderName2,
                            tempFolderName3,
                            tempFolderName4,
                            tempFolderNameWrap,
                            tempFolderNoEpsg)) {
                File directory = new File(parent, name);
                if (directory.isDirectory() && directory.exists()) {
                    FileUtils.deleteDirectory(directory);
                }
            }
        }
        super.tearDownInternal();
    }
}
