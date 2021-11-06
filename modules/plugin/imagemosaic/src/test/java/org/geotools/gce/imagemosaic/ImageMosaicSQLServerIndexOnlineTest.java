/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.Query;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.catalog.sqlserver.SQLServerDatastoreWrapper;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.OnlineTestCase;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/** Testing using a SQLServer database for storing the index for the ImageMosaic */
public class ImageMosaicSQLServerIndexOnlineTest extends OnlineTestCase {

    static final String tempFolderNoEpsg = "rgbNoEpsg";

    static final String tempFolderName1 = "waterTempPG";

    static final String tempFolderName2 = "waterTempPG2";

    static final String tempFolderName3 = "waterTempPG3";

    static final String tempFolderName4 = "waterTempPGCD";

    static final String tempFolderNameWrap = "waterTempPGWrap";

    static final String VERY_LONG_NAME_SQLSERVER =
            "very_very_long_name_with_number_of_chars_greater_than_128_to_test_the_sqlserver_wrapper_even_if_has_some_more_characters_compared_with_oracle_and_postgres";

    /**
     * Simple Class for better testing raster manager
     *
     * @author Marco Volpini, GeoSolutions SAS
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
        // create sample properties file for sqlserver datastore
        final Properties props = new Properties();
        props.setProperty("SPI", "org.geotools.data.sqlserver.SQLServerDataStoreFactory");
        props.setProperty("host", "localhost");
        props.setProperty("port", "1433");
        props.setProperty("user", "geoserver");
        props.setProperty("passwd", "geoserver");
        props.setProperty("database", "mock");
        props.setProperty("schema", "dbo");
        props.setProperty("Loose bbox", "true");
        props.setProperty("Estimated extends", "false");
        props.setProperty("validate connections", "true");
        props.setProperty("Connection timeout", "10");
        props.setProperty("preparedStatements", "false");
        props.setProperty("Geometry metadata table", "GEOMETRY_METADATA");
        return props;
    }

    /* (non-Javadoc)
     * @see org.geotools.test.OnlineTestCase#getFixtureId()
     */
    @Override
    protected String getFixtureId() {
        return "sqlserver_datastore";
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

                out.write(key_.replace(" ", "\\ ") + "=" + value.replace(" ", "\\ ") + "\n");
            }
            out.flush();
        }
    }

    /** Complex test for SQLServer indexing on db. */
    @Test
    public void testSQLServerIndexing() throws Exception {
        ImageMosaicReader reader = null;
        try {
            // insertMetadataTable(tempFolderName1);
            final File workDir = new File(TestData.file(this, "."), tempFolderName1);
            FileUtils.deleteDirectory(workDir);
            assertTrue(workDir.mkdir());
            FileUtils.copyFile(
                    TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
            TestData.unzipFile(this, tempFolderName1 + "/watertemp.zip");
            final URL timeElevURL = TestData.url(this, tempFolderName1);

            setupDataStoreProperties(tempFolderName1);

            // now start the test
            final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
            assertNotNull(format);
            reader = TestUtils.getReader(timeElevURL, format);
            assertNotNull(reader);

            final String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals(13, metadataNames.length);

            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
            assertNotNull(timeMetadata);
            assertEquals(2, timeMetadata.split(",").length);
            assertEquals(
                    timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            assertEquals(
                    timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));

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
            final List<Date> timeValues = new ArrayList<>();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            Date date = sdf.parse("2008-10-31T00:00:00.000Z");
            timeValues.add(date);
            time.setValue(timeValues);

            final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
            bkg.setValue(new double[] {-9999.0});

            final ParameterValue<Boolean> direct =
                    ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
            direct.setValue(false);

            final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(Arrays.asList(100.0));

            // Test the output coverage
            assertNotNull(
                    reader.read(new GeneralParameterValue[] {gg, time, bkg, elevation, direct}));
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
        } finally {
            if (reader != null) reader.dispose();
        }
    }

    /** Complex test for SQLServer indexing on db. */
    @Test
    public void testSQLServerIndexingNoEpsgCode() throws Exception {
        ImageMosaicReader reader = null;
        try {
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
            reader = TestUtils.getReader(noEpsgURL, format);
            // used to blow up
            assertNotNull(reader);
        } finally {
            if (reader != null) reader.dispose();
        }
    }

    @Test
    public void testSQLServerCreateAndDrop() throws Exception {
        ImageMosaicReader reader = null;
        try {
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
            reader = TestUtils.getReader(timeElevURL, format);
            assertNotNull(reader);
            reader.delete(true);
            boolean dropSuccessfull = false;
            try {
                dropTables(new String[] {tempFolderName4});
                dropSuccessfull = true;
            } catch (SQLException E) {
                // The tables have been already deleted with the database drop performed
                // by the delete operation.
                assertFalse(dropSuccessfull);
            }
        } finally {
            reader.dispose();
        }
    }

    /** Complex test for SQLServer indexing on db. */
    @Test
    public void testSortingAndLimiting() throws Exception {
        ImageMosaicReader reader = null;
        try {
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
            reader = TestUtils.getReader(timeElevURL, format);
            assertNotNull(reader);

            final String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals(13, metadataNames.length);

            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));

            // dispose and create new reader
            reader.dispose();
            reader = new MyImageMosaicReader(timeElevURL);
            final RasterManager rasterManager =
                    reader.getRasterManager(reader.getGridCoverageNames()[0]);

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
                final SortBy[] clauses = {
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
            final Collection<GranuleDescriptor> features = new ArrayList<>();
            rasterManager.getGranuleDescriptors(query, (granule, o) -> features.add(granule));
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
            final SortBy[] clauses = {
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
            rasterManager.getGranuleDescriptors(query, (granule1, o) -> features.add(granule1));
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
        } finally {
            if (reader != null) reader.dispose();
        }
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
        try (Connection connection =
                        DriverManager.getConnection(
                                "jdbc:sqlserver://"
                                        + fixture.getProperty("host")
                                        + ":"
                                        + fixture.getProperty("port")
                                        + ";databaseName="
                                        + (database != null
                                                ? database
                                                : fixture.getProperty("database")),
                                fixture.getProperty("user"),
                                fixture.getProperty("passwd"));
                Statement st = connection.createStatement()) {
            for (String table : tables) {
                StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ");
                String schema = fixture.getProperty("schema");
                if (schema != null) sb.append(schema).append(".");
                sb.append(table);
                st.execute(sb.toString());
            }
        }
    }

    /** Complex test for SQLServer store wrapping. */
    @Test
    public void testSQLServerWrapping() throws Exception {
        ImageMosaicReader reader = null;
        try {
            final File workDir = new File(TestData.file(this, "."), tempFolderNameWrap);
            assertTrue(workDir.mkdir());
            FileUtils.copyFile(
                    TestData.file(this, "watertemplongnamessqlserver.zip"),
                    new File(workDir, "watertemplongnamessqlserver.zip"));
            TestData.unzipFile(this, tempFolderNameWrap + "/watertemplongnamessqlserver.zip");
            final URL dataUrl = TestData.url(this, tempFolderNameWrap);

            setupDataStoreProperties(tempFolderNameWrap);

            // now start the test
            final AbstractGridFormat format = TestUtils.getFormat(dataUrl, null);
            assertNotNull(format);
            reader = TestUtils.getReader(dataUrl, format, null);
            assertNotNull(reader);

            final String[] metadataNames = reader.getMetadataNames();
            String[] coverageNames = reader.getGridCoverageNames();
            String coverageName = coverageNames[0];
            assertEquals(VERY_LONG_NAME_SQLSERVER, coverageName);
            List<DimensionDescriptor> descriptors = reader.getDimensionDescriptors(coverageName);
            for (DimensionDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                if (name.equalsIgnoreCase("time")) {
                    assertTrue(descriptor.getStartAttribute().length() > 64);
                    break;
                }
            }

            assertNotNull(metadataNames);
            assertEquals(13, metadataNames.length);

            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
            assertNotNull(timeMetadata);
            assertEquals(2, timeMetadata.split(",").length);
            assertEquals(
                    timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            assertEquals(
                    timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));

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
            final List<Date> timeValues = new ArrayList<>();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            Date date = sdf.parse("2008-10-31T00:00:00.000Z");
            timeValues.add(date);
            time.setValue(timeValues);

            final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
            bkg.setValue(new double[] {-9999.0});

            final ParameterValue<Boolean> direct =
                    ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
            direct.setValue(false);

            final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(Arrays.asList(100.0));

            // Test the output coverage
            assertNotNull(
                    reader.read(new GeneralParameterValue[] {gg, time, bkg, elevation, direct}));
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
        } finally {
            if (reader != null) reader.dispose();
        }
    }

    @Override
    protected void tearDownInternal() throws Exception {

        // delete tables
        dropTables(
                new String[] {
                    tempFolderNoEpsg,
                    tempFolderName1,
                    tempFolderName2,
                    tempFolderName3,
                    VERY_LONG_NAME_SQLSERVER.substring(0, 113),
                    SQLServerDatastoreWrapper.DEFAULT_METADATA_TABLE
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
                            VERY_LONG_NAME_SQLSERVER.substring(0, 113),
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
