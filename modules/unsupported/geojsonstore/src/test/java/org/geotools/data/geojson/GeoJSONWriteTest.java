/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
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
import org.geotools.test.TestData;
import org.geotools.util.URLs;
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
public class GeoJSONWriteTest {
    File tmp;

    File file;

    URL url;

    @Before
    public void createTemporaryLocations() throws IOException {
        // Setting the system-wide default at startup time
        System.setProperty("org.geotools.referencing.forceXY", "true");

        tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {
            System.err.println("Removing tempfile " + tmp);
            tmp.delete();
        }
        boolean created = tmp.mkdirs();
        if (!created) {
            System.err.println("Could not create " + tmp);
            System.exit(1);
        }
        file = new File(tmp, "locations.json");

        URL resource = TestData.getResource(GeoJSONWriteTest.class, "locations.json");
        url = resource;
        if (url == null) throw new RuntimeException("Input datafile not found");
        System.out.println("copying " + resource.toExternalForm() + " to " + file);
        Files.copy(resource.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        url = URLs.fileToUrl(file);
    }

    private String checkFileContents(File modified) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(modified.toPath(), baos);
        String contents = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return contents;
    }

    @After
    public void removeTemporaryLocations() throws IOException {
        File list[] = tmp.listFiles();
        for (int i = 0; i < list.length; i++) {
            list[i].delete();
        }
        tmp.delete();
    }

    @Test
    public void featureStoreExample() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", url);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("locations");

        assertTrue("Modification not supported", (featureSource instanceof SimpleFeatureStore));
    }

    @Test
    public void transactionExample() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", url);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t1 = new DefaultTransaction("transaction 1");
        Transaction t2 = new DefaultTransaction("transactoin 2");

        SimpleFeatureType type = store.getSchema("locations");
        SimpleFeatureStore auto = (SimpleFeatureStore) store.getFeatureSource("locations");
        SimpleFeatureStore featureStore1 = (SimpleFeatureStore) store.getFeatureSource("locations");
        SimpleFeatureStore featureStore2 = (SimpleFeatureStore) store.getFeatureSource("locations");

        featureStore1.setTransaction(t1);
        featureStore2.setTransaction(t2);

        // Before we edit everything should be the same
        assertEquals("featureStore1 before", 9, featureStore1.getFeatures().size());
        assertEquals("featureStore2 before", 9, featureStore2.getFeatures().size());
        SimpleFeature first = DataUtilities.first(featureStore1.getFeatures());
        System.out.println(first);
        // select feature to remove
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        // Filter filter1 = ff.id(Collections.singleton(ff.featureId(first.getID())));
        Filter filter1 = ff.equal(ff.property("CITY"), ff.literal("Trento"), false);

        featureStore1.removeFeatures(filter1); // removes "Trento" from fs1

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
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(-122.681944, 45.52));
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {45.52, -122.681944, "Portland", 800, 2014, point},
                        "feature-10");
        System.out.println(feature);
        SimpleFeatureCollection collection = DataUtilities.collection(feature);

        featureStore2.addFeatures(collection);
        // Tests after adding the feature
        assertEquals(
                "auto after featureStore1 removes Trento and featureStore2 adds Portland",
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

        t1.close();
        t2.close();
        store.dispose(); // clear out any listeners
    }

    @Test
    public void removeAllExample() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", url);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t = new DefaultTransaction("locations");
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    store.getFeatureWriter("locations", Filter.INCLUDE, t);

            try {
                while (writer.hasNext()) {
                    writer.next();
                    writer.remove(); // marking contents for removal
                }
            } finally {
                writer.close();
            }

            // Test the contents have been removed
            SimpleFeatureStore featureStore =
                    (SimpleFeatureStore) store.getFeatureSource("locations");
            assertEquals("featureStore should be empty", 0, featureStore.getFeatures().size());
            // Make sure the file is empty
            assertEquals("file should have no content", "", checkFileContents(file));
            t.commit();
        } catch (Throwable eek) {
            t.rollback();
        } finally {
            t.close();
            store.dispose();
        }
    }

    @Test
    public void replaceAll() throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", url);
        System.out.println("fetching store from " + url);
        DataStore store = DataStoreFinder.getDataStore(params);

        final SimpleFeatureType type = store.getSchema("locations");
        assertNotNull(type);
        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        SimpleFeature f;
        DefaultFeatureCollection collection = new DefaultFeatureCollection();

        // 45.52, -122.681944, Portland, 800, 2014
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point portland = gf.createPoint(new Coordinate(45.52, -122.681944));

        f =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {45.52, -122.681944, "Portland", 800, 2014, portland},
                        "locations.1");
        collection.add(f);

        writer = store.getFeatureWriter("locations", Transaction.AUTO_COMMIT);
        try {
            // remove all features
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
            // copy new features in
            SimpleFeatureIterator iterator = collection.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                SimpleFeature newFeature = writer.next(); // new blank feature
                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        } finally {
            writer.close();
        }

        // Test everything was replaced by the one feature we added
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        assertEquals(
                "featureStore should only have the one feature we created",
                1,
                featureStore.getFeatures().size());
        final String newline = System.lineSeparator();
        String contents =
                "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[45.52,-122.6819]},\"properties\":{\"LAT\":45.52,\"LON\":-122.681944,\"CITY\":\"Portland\",\"NUMBER\":800,\"YEAR\":2014},\"id\":\"locations.0\"}]}";
        assertEquals(
                "Ensure the file has only the one feature we created",
                contents.trim(),
                checkFileContents(file).trim());
    }

    @Test
    public void copyContent() throws Exception {
        File directory = tmp;
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", url);
        DataStore store = DataStoreFinder.getDataStore(params);
        SimpleFeatureType featureType = store.getSchema("locations");

        File file2 = new File(directory, "duplicate.json");
        Map<String, Serializable> params2 = new HashMap<String, Serializable>();
        params2.put("url", URLs.fileToUrl(file2));

        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        duplicate.createSchema(featureType);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        SimpleFeature feature, newFeature;

        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT);

        writer = duplicate.getFeatureWriterAppend("duplicate", Transaction.AUTO_COMMIT);
        try {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        } finally {
            reader.close();
            writer.close();
        }

        // test the new store is the same as the original
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        assertEquals(9, featureStore.getFeatures().size());

        SimpleFeatureStore featureStored =
                (SimpleFeatureStore) duplicate.getFeatureSource("duplicate");
        assertEquals(9, featureStored.getFeatures().size());

        SimpleFeatureIterator original = featureStore.getFeatures().features();
        SimpleFeatureIterator dups = featureStored.getFeatures().features();

        try {
            while (original.hasNext() && dups.hasNext()) {
                SimpleFeature o = original.next();
                SimpleFeature d = dups.next();
                for (int i = 0; i < o.getAttributeCount(); i++) {
                    assertTrue(DataUtilities.attributesEqual(o.getAttribute(i), d.getAttribute(i)));
                }
            }

        } finally {
            original.close();
            dups.close();
        }
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
