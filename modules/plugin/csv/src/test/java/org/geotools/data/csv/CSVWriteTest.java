/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2015, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * Informal test used to document expected functionality for workshop.
 *
 * <p>This test has a setup method used to copy locations.csv to a temporary file.
 */
public class CSVWriteTest {
    File tmp;

    File statesfile;

    @Before
    public void createTemporaryLocations() throws IOException {
        tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {

            tmp.delete();
        }
        boolean created = tmp.mkdirs();
        if (!created) {

            System.exit(1);
        }
        statesfile = new File(tmp, "locations.csv");

        URL resource = TestData.getResource(CSVWriteTest.class, "locations.csv");
        Files.copy(resource.openStream(), statesfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private String getFileContents(File modified) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(modified.toPath(), baos);
        String contents = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return contents;
    }

    @After
    public void removeTemporaryLocations() throws IOException {
        File[] list = tmp.listFiles();
        if (list != null) {
            for (File file : list) {
                file.delete();
            }
        }
        tmp.delete();
    }

    // Make sure any temp files were cleaned up.
    public boolean cleanedup() {
        File[] list = tmp.listFiles((dir, name) -> name.endsWith(".csv"));
        if (list != null) {
            for (File file : list) {
                if (file.getName().equalsIgnoreCase("locations.csv")) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    @Test
    public void featureStoreExample() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", statesfile);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("locations");

        assertTrue("Modification not supported", featureSource instanceof SimpleFeatureStore);
        assertTrue("Temp files being left behind", cleanedup());
    }

    @Test
    public void transactionExample() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", statesfile);
        DataStore store = DataStoreFinder.getDataStore(params);

        try (Transaction t1 = new DefaultTransaction("transaction 1");
                Transaction t2 = new DefaultTransaction("transactoin 2"); ) {

            SimpleFeatureType type = store.getSchema("locations");
            SimpleFeatureStore auto = (SimpleFeatureStore) store.getFeatureSource("locations");
            SimpleFeatureStore featureStore1 =
                    (SimpleFeatureStore) store.getFeatureSource("locations");
            SimpleFeatureStore featureStore2 =
                    (SimpleFeatureStore) store.getFeatureSource("locations");

            featureStore1.setTransaction(t1);
            featureStore2.setTransaction(t2);

            // Before we edit everything should be the same
            assertEquals("featureStore1 before", 9, featureStore1.getFeatures().size());
            assertEquals("featureStore2 before", 9, featureStore2.getFeatures().size());

            // select feature to remove
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            Filter filter1 = ff.id(Collections.singleton(ff.featureId("locations-fid1")));
            featureStore1.removeFeatures(filter1); // road1 removes fid1 on t1

            // Tests after removal
            assertEquals("auto after featureStore1 removes fid1", 9, auto.getFeatures().size());
            assertEquals(
                    "featureStore1 after featureStore1 removes fid1",
                    8,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after featureStore1 removes fid1",
                    9,
                    featureStore2.getFeatures().size());

            // new feature to add!
            // 45.52, -122.681944, Portland, 800, 2014
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(
                            type,
                            new Object[] {45.52, -122.681944, "Portland", 800, 2014},
                            "locations-fid10");
            SimpleFeatureCollection collection = DataUtilities.collection(feature);

            featureStore2.addFeatures(collection);
            // Tests after adding the feature
            assertEquals(
                    "auto after featureStore1 removes fid1 and featureStore2 adds fid5",
                    9,
                    auto.getFeatures().size());
            assertEquals(
                    "featureStore1 after featureStore1 removes fid1 and featureStore2 adds fid5",
                    8,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after featureStore1 removes fid1 and featureStore2 adds fid5",
                    10,
                    featureStore2.getFeatures().size());

            // commit transaction one
            t1.commit();

            // Tests after first commit
            assertEquals(
                    "auto after featureStore1 commits removal of fid1 (featureStore2 has added fid5)",
                    8,
                    auto.getFeatures().size());
            assertEquals(
                    "featureStore1 after commiting removal of fid1 (featureStore2 has added fid5)",
                    8,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after featureStore1 commits removal of fid1 (featureStore2 has added fid5)",
                    9,
                    featureStore2.getFeatures().size());

            // commit transaction two
            t2.commit();

            // Tests after 2nd commit
            assertEquals(
                    "auto after featureStore2 commits addition of fid5 (fid1 previously removed)",
                    9,
                    auto.getFeatures().size());
            assertEquals(
                    "featureStore1 after featureStore2 commits addition of fid5 (fid1 previously removed)",
                    9,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after commiting addition of fid5 (fid1 previously removed)",
                    9,
                    featureStore2.getFeatures().size());

            store.dispose(); // clear out any listeners
        }
        assertTrue("Temp files being left behind", cleanedup());
    }

    @Test
    @SuppressWarnings("PMD.UseTryWithResources") // need transaction in catch for rollback
    public void removeAllExample() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", statesfile);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t = new DefaultTransaction("locations");
        try {

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    store.getFeatureWriter("locations", Filter.INCLUDE, t)) {
                while (writer.hasNext()) {
                    writer.next();
                    writer.remove(); // marking contents for removal
                }
            }

            // Test the contents have been removed
            SimpleFeatureStore featureStore =
                    (SimpleFeatureStore) store.getFeatureSource("locations");
            assertEquals("featureStore should be empty", 0, featureStore.getFeatures().size());
            // Make sure the file is empty
            assertEquals("file should have no content", "", getFileContents(statesfile));
            t.commit();
        } catch (Throwable eek) {
            t.rollback();
        } finally {
            t.close();
            store.dispose();
        }
        assertTrue("Temp files being left behind", cleanedup());
    }

    @Test
    public void replaceAll() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", statesfile);
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params.put(CSVDataStoreFactory.LATFIELDP.key, "LAT");
        params.put(CSVDataStoreFactory.LnGFIELDP.key, "LON");
        DataStore store = DataStoreFinder.getDataStore(params);

        final SimpleFeatureType type = store.getSchema("locations");
        DefaultFeatureCollection collection = new DefaultFeatureCollection();

        // 45.52, -122.681944, Portland, 800, 2014
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point portland = gf.createPoint(new Coordinate(45.52, -122.681944));
        SimpleFeature f =
                SimpleFeatureBuilder.build(
                        type, new Object[] {portland, "Portland", 800, 2014}, "locations.1");
        collection.add(f);

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                store.getFeatureWriter("locations", Transaction.AUTO_COMMIT); ) {

            // remove all features
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
            // copy new features in
            try (SimpleFeatureIterator iterator = collection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    SimpleFeature newFeature = writer.next(); // new blank feature
                    newFeature.setAttributes(feature.getAttributes());
                    writer.write();
                }
            }
        }

        // Test everything was replaced by the one feature we added
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        assertEquals(
                "featureStore should only have the one feature we created",
                1,
                featureStore.getFeatures().size());
        final String newline = System.lineSeparator();
        String contents =
                "LAT,LON,CITY,NUMBER,YEAR" + newline + "45.52,-122.681944,Portland,800,2014";
        assertEquals(
                "Ensure the file has only the one feature we created",
                contents.trim(),
                getFileContents(statesfile).trim());
        assertTrue("Temp files being left behind", cleanedup());
    }

    @Test
    public void appendContent() throws Exception {
        File directory = tmp;
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", statesfile);
        DataStore store = DataStoreFinder.getDataStore(params);
        SimpleFeatureType featureType = store.getSchema("locations");

        File file2 = new File(directory, "duplicate.rst");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        duplicate.createSchema(featureType);

        SimpleFeature feature, newFeature;
        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        store.getFeatureReader(query, Transaction.AUTO_COMMIT);
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        duplicate.getFeatureWriterAppend("duplicate", Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }
        assertTrue("Temp files being left behind", cleanedup());
        // Test that content was appended
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        assertEquals(9, featureStore.getFeatures().size());

        // Compare the new file written to with the old one
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        Files.copy(statesfile.toPath(), baos1);
        Files.copy(file2.toPath(), baos2);
        String contents1 = new String(baos1.toByteArray(), StandardCharsets.UTF_8);
        String contents2 = new String(baos2.toByteArray(), StandardCharsets.UTF_8);

        // Making sure whitespace doesn't cause problems
        // and adjust for potential windows line endings.
        assertEqualsIgnoreWhitespace(
                "Contents of both files should be the same", contents1, contents2);
    }

    @Test
    public void testAttribyteOnlyStrategyWrites() throws IOException {

        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(states);
        assertNotNull("couldn't create store", store);
        File file2 = File.createTempFile("CSVTest", ".csv");
        file2.deleteOnExit();
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);
        params2.put(
                CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.ATTRIBUTES_ONLY_STRATEGY);

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        SimpleFeatureType featureType = store.getSchema();

        duplicate.createSchema(featureType);

        SimpleFeature feature, newFeature;

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader();
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        duplicate.getFeatureWriterAppend(
                                duplicate.getTypeNames()[0], Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }
        assertTrue("Temp files being left behind", cleanedup());
        String contents = getFileContents(file2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);
            assertFalse("Geom is included", line.toLowerCase().contains("the_geom"));
            line = lineReader.readLine();
            assertNotNull(line);
            assertFalse("Geom is included", line.toLowerCase().contains("multipolygon"));
        }
        file2.delete();
    }

    @Test
    public void testSpecificStrategyWrites() throws IOException {
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", statesfile);
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params.put(CSVDataStoreFactory.LATFIELDP.key, "LAT");
        params.put(CSVDataStoreFactory.LnGFIELDP.key, "LON");
        DataStore store = DataStoreFinder.getDataStore(params);

        final SimpleFeatureType type = store.getSchema("locations");

        DefaultFeatureCollection collection = new DefaultFeatureCollection();

        // 45.52, -122.681944, Portland, 800, 2014
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point portland = gf.createPoint(new Coordinate(45.52, -122.681944));
        SimpleFeature f =
                SimpleFeatureBuilder.build(
                        type, new Object[] {portland, "Portland", 800, 2014}, "locations.1");
        collection.add(f);

        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);
        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params2.put(CSVDataStoreFactory.LATFIELDP.key, "LAT");
        params2.put(CSVDataStoreFactory.LnGFIELDP.key, "LON");
        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        SimpleFeatureType featureType = store.getSchema("locations");

        duplicate.createSchema(featureType);

        SimpleFeature feature, newFeature;

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        store.getFeatureReader(new Query("locations"), Transaction.AUTO_COMMIT);
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        duplicate.getFeatureWriterAppend(
                                duplicate.getTypeNames()[0], Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }
        assertTrue("Temp files being left behind", cleanedup());
        String contents = getFileContents(file2);
        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);
            assertTrue("No Lat", line.contains("LAT"));
            assertTrue("No Lon", line.contains("LON"));
            line = lineReader.readLine();
            // LatLonStrategy can switch the coordinate order
            assertEquals("46.066667,11.116667,Trento,140,2002", line);
        }
        file2.delete();
    }

    @Test
    public void testWriteToNewFile() throws IOException {
        File f = File.createTempFile("does-not-exist", ".csv");
        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(states);
        assertNotNull("couldn't create input store", store);
        CSVDataStoreFactory csvDataStoreFactory = new CSVDataStoreFactory();

        CSVDataStore datastore = (CSVDataStore) csvDataStoreFactory.createDataStoreFromFile(f);

        SimpleFeatureType featureType = store.getSchema();

        datastore.createSchema(featureType);
        SimpleFeatureSource source = datastore.getFeatureSource();
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;
            outStore.addFeatures(store.getFeatureSource().getFeatures());
        } else {
            fail("Can't write to new CSVDatastore");
        }

        datastore.dispose();
        datastore = (CSVDataStore) csvDataStoreFactory.createDataStoreFromFile(f);
        source = datastore.getFeatureSource();
        SimpleFeatureCollection features = source.getFeatures();
        assertEquals(
                "wrong number of records",
                store.getFeatureSource().getFeatures().size(),
                features.size());
    }

    @Test
    public void testWKTWrites() throws IOException {

        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(states);
        assertNotNull("couldn't create store", store);
        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);
        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        SimpleFeatureType featureType = store.getSchema();

        duplicate.createSchema(featureType);

        SimpleFeature feature, newFeature;

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader();
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        duplicate.getFeatureWriterAppend(
                                duplicate.getTypeNames()[0], Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }
        assertTrue("Temp files being left behind", cleanedup());
        String contents = getFileContents(file2);

        try (BufferedReader lineReader =
                new BufferedReader(new CharArrayReader(contents.toCharArray()))) {
            String line = lineReader.readLine(); // header
            assertNotNull(line);
            assertTrue("Geom is not included", line.toLowerCase().contains("the_geom_wkt"));
            line = lineReader.readLine();
            assertNotNull(line);
            assertTrue("Geom is not included", line.toLowerCase().contains("multipolygon"));
        }
        file2.delete();
    }

    public static void assertEqualsIgnoreWhitespace(
            String message, String expected, String actual) {
        expected = removeWhitespace(expected);
        actual = removeWhitespace(actual);
        assertEquals(message, expected, actual);
    }

    private static String removeWhitespace(String actual) {
        if (actual == null) return "";
        StringBuffer crush = new StringBuffer(actual);
        int ch = 0;
        while (ch < crush.length()) {
            char chracter = crush.charAt(ch);
            if (Character.isWhitespace(chracter)) {
                crush.deleteCharAt(ch);
                continue;
            }
            ch++;
        }
        return crush.toString();
    }
}
