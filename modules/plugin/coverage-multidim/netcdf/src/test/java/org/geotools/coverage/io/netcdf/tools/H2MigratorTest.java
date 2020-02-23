/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.tools;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.io.Files;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.io.netcdf.NetCDFMosaicReaderTest;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

public class H2MigratorTest {

    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();

    public static ParameterValue<Boolean> NO_DEFERRED_LOADING_PARAM;

    static {
        final ParameterValue<Boolean> imageRead =
                AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        imageRead.setValue(false);
        NO_DEFERRED_LOADING_PARAM = imageRead;
    }

    public static final GeneralParameterValue[] NO_DEFERRED_LOADING_PARAMS = {
        NO_DEFERRED_LOADING_PARAM
    };

    @BeforeClass
    public static void init() {
        // make sure CRS ordering is correct
        System.setProperty("org.geotools.referencing.forceXY", "true");
        System.setProperty("user.timezone", "GMT");
        System.setProperty("org.geotools.shapefile.datetime", "true");
        CRS.reset("all");
    }

    @AfterClass
    public static void close() {
        System.clearProperty("org.geotools.referencing.forceXY");
        System.clearProperty("user.timezone");
        System.clearProperty("org.geotools.shapefile.datetime");
        CRS.reset("all");
    }

    @Before
    public void clearNetCDFDatadir() {
        System.clearProperty(NetCDFUtilities.NETCDF_DATA_DIR);
    }

    /** Test migration on a mosaic with multiple files each one having multiple coverages */
    @Test
    public void testMultiCoverageDirect() throws Exception {
        File testDir = tempFolder.newFolder("multi-coverage");
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(
                TestData.file(NetCDFMosaicReaderTest.class, "multi-coverage"), testDir);
        // setup the reader, will make it generate the H2 indexes
        testMultiCoverageMosaic(testUrl, null);

        // build the target store config file
        final File targetStoreConfiguration = new File(testDir, "target.properties");
        final String properties =
                String.format(
                        "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
                                + "driver=org.h2.Driver\n"
                                + "database=%s\n"
                                + "user=sa\n"
                                + "password=",
                        getTargetDbForProperties(testDir));
        FileUtils.writeStringToFile(targetStoreConfiguration, properties, "UTF-8");

        File logDir = new File(testDir, "logs");
        assertTrue(logDir.mkdir());

        H2MigrateConfiguration config = new H2MigrateConfiguration();
        config.setMosaicPath(testDir.getPath());
        config.setConcurrency(1);
        config.setTargetStoreConfiguration(targetStoreConfiguration.getPath());
        config.setLogDirectory(logDir.getPath());
        final H2Migrator migrator = new H2Migrator(config);
        migrator.migrate();

        // perform the basic migration checks
        assertMultiCoverageMigration(testDir, logDir, config);

        // reload the mosaic past migration, is it working?
        testMultiCoverageMosaic(testUrl, null);
    }

    /** Returns a path usable for the property file (even on Windows) */
    private String getTargetDbForProperties(File testDir) {
        String path = new File(testDir, "target").getPath();
        return path.replace("\\", "\\\\");
    }

    @Test
    public void testMultiCoverageRepository() throws Exception {
        testMultiCoverageRepository(
                new String[] {"air_temperature", "sea_surface_temperature"}, false);
    }

    @Test
    public void testMultiCoverageRepositoryStoreName() throws Exception {
        testMultiCoverageRepository(
                new String[] {"air_temperature", "sea_surface_temperature"}, true);
    }

    @Test
    public void testMultiCoverageRepositoryGuessCoverages() throws Exception {
        testMultiCoverageRepository(null, false);
    }

    /** Test migration on a mosaic with multiple files each one having multiple coverages */
    public void testMultiCoverageRepository(String[] coverageNames, boolean setIndexStoreName)
            throws Exception {
        // copy the data over
        File testDir = tempFolder.newFolder("multi-coverage");
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(
                TestData.file(NetCDFMosaicReaderTest.class, "multi-coverage"), testDir);

        // create an H2 store to hold the mosaic slices
        final String mosaicDatabasePath = new File(testDir, "customDB").getAbsolutePath();
        Map sourceParams = new HashMap<>();
        sourceParams.put("database", mosaicDatabasePath);
        sourceParams.put("MVCC", "true");
        final JDBCDataStore customStore = new H2DataStoreFactory().createDataStore(sourceParams);
        JDBCDataStore indexStore = null;
        try {
            DefaultRepository repository = new DefaultRepository();
            repository.register("mosaicSlices", customStore);

            // change the configuration to refer to a store by name (mimics OpenSearch mosaic
            // setups)
            final File mosaicStoreConfiguration = new File(testDir, "datastore.properties");
            FileUtils.writeStringToFile(
                    mosaicStoreConfiguration, "StoreName=mosaicSlices", "UTF-8");

            // setup the reader, will make it generate the H2 indexes
            Hints hints = new Hints(Hints.REPOSITORY, repository);
            testMultiCoverageMosaic(testUrl, hints);

            // build the sourcestore config file
            final File sourceStoreConfiguration = new File(testDir, "source.properties");
            final String sourceProperties =
                    String.format(
                            "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
                                    + "driver=org.h2.Driver\n"
                                    + "database=%s\n",
                            mosaicDatabasePath.replace("\\", "\\\\"));
            FileUtils.writeStringToFile(sourceStoreConfiguration, sourceProperties, "UTF-8");

            // build the target store config file
            final File targetStoreConfiguration = new File(testDir, "target.properties");
            final String targetProperties =
                    String.format(
                            "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
                                    + "driver=org.h2.Driver\n"
                                    + "database=%s\n"
                                    + "user=sa\n"
                                    + "password=",
                            getTargetDbForProperties(testDir));
            FileUtils.writeStringToFile(targetStoreConfiguration, targetProperties, "UTF-8");

            File logDir = new File(testDir, "logs");
            assertTrue(logDir.mkdir());

            H2MigrateConfiguration config = new H2MigrateConfiguration();
            config.setMosaicPath(testDir.getPath());
            config.setConcurrency(1);
            config.setTargetStoreConfiguration(targetStoreConfiguration.getPath());
            config.setSourceStoreConfiguration(sourceStoreConfiguration.getPath());
            if (setIndexStoreName) {
                config.setIndexStoreName("netcdfIndex");
            }
            if (coverageNames != null) {
                config.setCoverageNames(coverageNames);
            }
            config.setLogDirectory(logDir.getPath());

            final H2Migrator migrator = new H2Migrator(config);
            migrator.migrate();

            // perform the basic migration checks
            assertMultiCoverageMigration(testDir, logDir, config);

            File netcdfStore = new File(testDir, H2Migrator.NETCDF_DATASTORE_PROPERTIES);
            assertTrue(netcdfStore.exists());

            if (setIndexStoreName) {
                // check the netcdf_datastore contents
                Properties indexProperties = new Properties();
                try (InputStream is = new FileInputStream(netcdfStore)) {
                    indexProperties.load(is);
                    assertEquals("netcdfIndex", indexProperties.getProperty(Utils.Prop.STORE_NAME));
                }

                final String indexDatabasePath = new File(testDir, "target").getAbsolutePath();
                sourceParams = new HashMap<>();
                sourceParams.put("database", indexDatabasePath);
                sourceParams.put("MVCC", "true");

                indexStore = new H2DataStoreFactory().createDataStore(sourceParams);
                repository.register("netcdfIndex", indexStore);
            }

            // reload the mosaic past migration, is it working?
            testMultiCoverageMosaic(testUrl, hints);
        } finally {
            try {
                if (indexStore != null) {
                    indexStore.dispose();
                }
            } finally {
                customStore.dispose();
            }
        }
    }

    public void assertMultiCoverageMigration(
            File testDir, File logDir, H2MigrateConfiguration config) throws Exception {
        // check the migrated data
        final DataStore store =
                H2MigrateConfiguration.getDataStore(config.getTargetStoreConfiguration());
        try {
            assertEquals(2, store.getTypeNames().length);
            assertThat(
                    store.getTypeNames(),
                    Matchers.arrayContainingInAnyOrder(
                            "air_temperature", "sea_surface_temperature"));

            // check the records for the various files are there
            UniqueVisitor u1 = new UniqueVisitor("location");
            final SimpleFeatureCollection airTemperature =
                    store.getFeatureSource("air_temperature").getFeatures();
            assertEquals(2, airTemperature.size());
            airTemperature.accepts(u1, null);
            assertThat(
                    (Set<String>) u1.getUnique(),
                    Matchers.containsInAnyOrder(
                            endsWith("multi-coverage-1.nc"), endsWith("multi-coverage-2.nc")));

            UniqueVisitor u2 = new UniqueVisitor("location");
            final SimpleFeatureCollection seaSurfaceTemperature =
                    store.getFeatureSource("sea_surface_temperature").getFeatures();
            assertEquals(2, seaSurfaceTemperature.size());
            seaSurfaceTemperature.accepts(u2, null);
            assertThat(
                    (Set<String>) u2.getUnique(),
                    Matchers.containsInAnyOrder(
                            endsWith("multi-coverage-1.nc"), endsWith("multi-coverage-2.nc")));

        } finally {
            store.dispose();
        }

        // check the log files contents
        File netcdfLog = new File(logDir, "migrated.txt");
        assertTrue(netcdfLog.exists());
        final List<String> netcdfList = Files.readLines(netcdfLog, Charset.forName("UTF-8"));
        assertEquals(2, netcdfList.size());
        assertThat(
                netcdfList,
                Matchers.containsInAnyOrder(
                        endsWith("multi-coverage-1.nc"), endsWith("multi-coverage-2.nc")));
        File h2Log = new File(logDir, "h2.txt");
        assertTrue(h2Log.exists());
        final List<String> h2List = Files.readLines(h2Log, Charset.forName("UTF-8"));
        assertEquals(10, h2List.size());
        assertThat(
                h2List,
                Matchers.everyItem(allOf(containsString("multi-coverage-"), endsWith(".db"))));
        // for extra measure, check and remove the H2 dbs, they should not be needed anymore
        for (String h2File : h2List) {
            final File file = new File(h2File);
            if (file.exists()) { // the log files linger a bit and then they get removed
                assertTrue("Could not remove: " + file, file.delete());
            }
        }
        assertIndexerUpdated(testDir);

        // check each mosaic property file has the aux datastore property too
        assertCoveragePropertiesUpdate(
                testDir, new String[] {"air_temperature", "sea_surface_temperature"});
    }

    public void assertIndexerUpdated(File testDir) throws JAXBException {
        // check the indexer has been modified and the aux datastore file is there
        File auxDataStoreFile = new File(testDir, H2Migrator.NETCDF_DATASTORE_PROPERTIES);
        assertTrue(auxDataStoreFile.exists());
        File indexerFile = new File(testDir, "indexer.xml");
        assertTrue(indexerFile.exists());
        final Indexer indexer = Utils.unmarshal(indexerFile);
        final Optional<String> auxDataStore =
                indexer.getParameters()
                        .getParameter()
                        .stream()
                        .filter(p -> Utils.Prop.AUXILIARY_DATASTORE_FILE.equals(p.getName()))
                        .map(p -> p.getValue())
                        .findFirst();
        assertTrue(auxDataStore.isPresent());
        assertEquals(H2Migrator.NETCDF_DATASTORE_PROPERTIES, auxDataStore.get());
    }

    public void testMultiCoverageMosaic(URL testUrl, Hints hints) throws Exception {
        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicReader(testUrl, hints);
            assertNotNull(reader);
            checkMultiCoverage(reader, "air_temperature", -85, 26, "2017-02-06T00:00:00.000", 295);
            checkMultiCoverage(
                    reader, "sea_surface_temperature", -85, 26, "2017-02-06T00:00:00.000", 296);
            checkMultiCoverage(reader, "air_temperature", -85, 26, "2017-02-06T12:00:00.000", 296);
            checkMultiCoverage(
                    reader, "sea_surface_temperature", -85, 26, "2017-02-06T12:00:00.000", 295);

        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Check that reading a single data value from an ImageMosaic of multi-coverage NetCDF files
     * yields the expected value.
     */
    private void checkMultiCoverage(
            ImageMosaicReader reader,
            String coverageName,
            double longitude,
            double latitude,
            String timestamp,
            double expected)
            throws Exception {
        ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        time.setValue(Arrays.asList(new Date[] {parseTimeStamp(timestamp)}));
        GeneralParameterValue[] params =
                new GeneralParameterValue[] {NO_DEFERRED_LOADING_PARAM, time};
        GridCoverage2D coverage = reader.read(coverageName, params);
        assertNotNull(coverage);
        // delta is zero because an exact match is expected
        assertEquals(
                expected,
                coverage.evaluate(new Point2D.Double(longitude, latitude), (double[]) null)[0],
                0);
    }

    private Date parseTimeStamp(String timeStamp) throws ParseException {
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatD.setTimeZone(TimeZone.getTimeZone("Zulu"));
        return formatD.parse(timeStamp);
    }

    /** Test migration on a mosaic with multiple files each one having one coverage */
    @Test
    public void testMultiCoverageSplitNames() throws Exception {
        File testDir = tempFolder.newFolder("gome");
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(TestData.file(NetCDFMosaicReaderTest.class, "gome"), testDir);

        // the test data already contained a custom store for indexes, get rid of it and its
        // associated configuration
        assertTrue(new File(testDir, "netcdf_datastore.properties").delete());
        final File indexerFile = new File(testDir, "indexer.xml");
        final Indexer indexer = Utils.unmarshal(indexerFile);
        final List<ParametersType.Parameter> parameters = indexer.getParameters().getParameter();
        final List<ParametersType.Parameter> filteredParameters =
                parameters
                        .stream()
                        .filter(p -> !p.getName().equals(Utils.Prop.AUXILIARY_DATASTORE_FILE))
                        .collect(Collectors.toList());
        parameters.addAll(filteredParameters);
        Utils.marshal(indexer, indexerFile);

        // setup the reader, will make it generate the H2 indexes
        genericMosaicLoadAndRead(testUrl);

        // build the target store config file
        final File targetStoreConfiguration = new File(testDir, "target.properties");
        final String properties =
                String.format(
                        "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
                                + "driver=org.h2.Driver\n"
                                + "database=%s\n"
                                + "user=sa\n"
                                + "password=",
                        getTargetDbForProperties(testDir));
        FileUtils.writeStringToFile(targetStoreConfiguration, properties, "UTF-8");

        File logDir = new File(testDir, "logs");
        assertTrue(logDir.mkdir());

        H2MigrateConfiguration config = new H2MigrateConfiguration();
        config.setMosaicPath(testDir.getPath());
        config.setConcurrency(1);
        config.setTargetStoreConfiguration(targetStoreConfiguration.getPath());
        config.setLogDirectory(logDir.getPath());
        final H2Migrator migrator = new H2Migrator(config);
        migrator.migrate();

        // perform the basic migration checks
        // check the migrated data
        final DataStore store =
                H2MigrateConfiguration.getDataStore(config.getTargetStoreConfiguration());
        try {
            assertEquals(2, store.getTypeNames().length);
            assertThat(store.getTypeNames(), Matchers.arrayContainingInAnyOrder("BrO", "NO2"));

            // check the records for the various files are there
            final SimpleFeatureCollection bro = store.getFeatureSource("BrO").getFeatures();
            assertEquals(2, bro.size());
            UniqueVisitor u1 = new UniqueVisitor("location");
            bro.accepts(u1, null);
            assertThat(
                    (Set<String>) u1.getUnique(),
                    Matchers.containsInAnyOrder(
                            endsWith("20130101.BrO.DUMMY.nc"), endsWith("20130108.BrO.DUMMY.nc")));

            final SimpleFeatureCollection no2 = store.getFeatureSource("NO2").getFeatures();
            assertEquals(2, no2.size());
            UniqueVisitor u2 = new UniqueVisitor("location");
            no2.accepts(u2, null);
            assertThat(
                    (Set<String>) u2.getUnique(),
                    Matchers.containsInAnyOrder(
                            endsWith("20130101.NO2.DUMMY.nc"), endsWith("20130108.NO2.DUMMY.nc")));

        } finally {
            store.dispose();
        }

        // check the log files contents
        File netcdfLog = new File(logDir, "migrated.txt");
        assertTrue(netcdfLog.exists());
        final List<String> netcdfList = Files.readLines(netcdfLog, Charset.forName("UTF-8"));
        assertEquals(4, netcdfList.size());
        assertThat(
                netcdfList,
                Matchers.containsInAnyOrder(
                        endsWith("20130101.BrO.DUMMY.nc"),
                        endsWith("20130101.NO2.DUMMY.nc"),
                        endsWith("20130108.BrO.DUMMY.nc"),
                        endsWith("20130108.NO2.DUMMY.nc")));
        File h2Log = new File(logDir, "h2.txt");
        assertTrue(h2Log.exists());
        final List<String> h2List = Files.readLines(h2Log, Charset.forName("UTF-8"));
        assertEquals(20, h2List.size());
        assertThat(h2List, Matchers.everyItem(allOf(endsWith(".db"))));
        // for extra measure, check and remove the H2 dbs, they should not be needed anymore
        for (String h2File : h2List) {
            final File file = new File(h2File);
            if (file.exists()) { // the log files linger a bit and then they get removed
                assertTrue("Could not remove: " + file, file.delete());
            }
        }

        // check the indexer has been modified and the aux datastore file is there
        assertIndexerUpdated(testDir);

        // check each mosaic property file has the aux datastore property too
        assertCoveragePropertiesUpdate(testDir, new String[] {"BrO", "NO2"});

        // reload the mosaic past migration, is it working?
        genericMosaicLoadAndRead(testUrl);
    }

    public void assertCoveragePropertiesUpdate(File testDir, String[] coverageNames)
            throws IOException {
        for (String coverage : coverageNames) {
            File coverageConfigFile = new File(testDir, coverage + ".properties");
            Properties properties1 = new Properties();
            try (FileInputStream fis = new FileInputStream(coverageConfigFile)) {
                properties1.load(fis);
            }

            assertEquals(
                    H2Migrator.NETCDF_DATASTORE_PROPERTIES,
                    properties1.getProperty(Utils.Prop.AUXILIARY_DATASTORE_FILE));
        }
    }

    public void genericMosaicLoadAndRead(URL testUrl) throws IOException {

        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicReader(testUrl, null);
            assertNotNull(reader);
            for (String name : reader.getGridCoverageNames()) {
                GridCoverage2D coverage = reader.read(name, NO_DEFERRED_LOADING_PARAMS);
                assertNotNull(coverage);
                coverage.dispose(true);
            }
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }
}
