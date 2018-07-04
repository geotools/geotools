/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.csv2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
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
import org.geotools.tutorial.csv.CSVTest;
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

    File file;

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
        file = new File(tmp, "locations.csv");
        URL resource = CSVTest.class.getResource("locations.csv");
        Files.copy(resource.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void fileContents(String test, File modified) throws IOException {
        System.out.println(test + " contents start");
        Files.copy(modified.toPath(), System.out);
        System.out.println(test + " contents end");
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
        System.out.println("featureStoreExample start\n");
        // featureStoreExample start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("locations");
        if (!(featureSource instanceof SimpleFeatureStore)) {
            throw new IllegalStateException("Modification not supported");
        }
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // featureStoreExample end
        System.out.println("\nfeatureStoreExample end\n");
    }

    @Test
    public void transactionExample() throws Exception {
        System.out.println("transactionExample start\n");
        // transactionExample start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t1 = new DefaultTransaction("transaction 1");
        Transaction t2 = new DefaultTransaction("transactoin 2");

        SimpleFeatureType type = store.getSchema("locations");
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        SimpleFeatureStore featureStore1 = (SimpleFeatureStore) store.getFeatureSource("locations");
        SimpleFeatureStore featureStore2 = (SimpleFeatureStore) store.getFeatureSource("locations");

        featureStore1.setTransaction(t1);
        featureStore2.setTransaction(t2);

        System.out.println("Step 1");
        System.out.println("------");
        System.out.println(
                "start     auto-commit: " + DataUtilities.fidSet(featureStore.getFeatures()));
        System.out.println(
                "start              t1: " + DataUtilities.fidSet(featureStore1.getFeatures()));
        System.out.println(
                "start              t2: " + DataUtilities.fidSet(featureStore2.getFeatures()));

        // select feature to remove
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter filter1 = ff.id(Collections.singleton(ff.featureId("fid1")));
        featureStore1.removeFeatures(filter1); // road1 removes fid1 on t1

        System.out.println();
        System.out.println("Step 2 transaction 1 removes feature 'fid1'");
        System.out.println("------");
        System.out.println(
                "t1 remove auto-commit: " + DataUtilities.fidSet(featureStore.getFeatures()));
        System.out.println(
                "t1 remove          t1: " + DataUtilities.fidSet(featureStore1.getFeatures()));
        System.out.println(
                "t1 remove          t2: " + DataUtilities.fidSet(featureStore2.getFeatures()));

        // new feature to add!
        // 42.3601° N, 71.0589° W Boston 1300 2017
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point boston = gf.createPoint(new Coordinate(-71.0589, 42.3601));
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        type, new Object[] {boston, "Boston", 1300, 2017}, "locations.1");
        SimpleFeatureCollection collection = DataUtilities.collection(feature);
        featureStore2.addFeatures(collection);

        System.out.println();
        System.out.println("Step 3 transaction 2 adds a new feature '" + feature.getID() + "'");
        System.out.println("------");
        System.out.println(
                "t2 add    auto-commit: " + DataUtilities.fidSet(featureStore.getFeatures()));
        System.out.println(
                "t2 add             t1: " + DataUtilities.fidSet(featureStore1.getFeatures()));
        System.out.println(
                "t1 add             t2: " + DataUtilities.fidSet(featureStore2.getFeatures()));

        // commit transaction one
        t1.commit();

        System.out.println();
        System.out.println("Step 4 transaction 1 commits the removal of feature 'fid1'");
        System.out.println("------");
        System.out.println(
                "t1 commit auto-commit: " + DataUtilities.fidSet(featureStore.getFeatures()));
        System.out.println(
                "t1 commit          t1: " + DataUtilities.fidSet(featureStore1.getFeatures()));
        System.out.println(
                "t1 commit          t2: " + DataUtilities.fidSet(featureStore2.getFeatures()));

        // commit transaction two
        t2.commit();

        System.out.println();
        System.out.println(
                "Step 5 transaction 2 commits the addition of '" + feature.getID() + "'");
        System.out.println("------");
        System.out.println(
                "t2 commit auto-commit: " + DataUtilities.fidSet(featureStore.getFeatures()));
        System.out.println(
                "t2 commit          t1: " + DataUtilities.fidSet(featureStore1.getFeatures()));
        System.out.println(
                "t2 commit          t2: " + DataUtilities.fidSet(featureStore2.getFeatures()));

        t1.close();
        t2.close();
        store.dispose(); // clear out any listeners
        // transactionExample end
        System.out.println("\ntransactionExample end\n");

        fileContents("transactionExample", file);
    }

    @Test
    public void removeAllExample() throws Exception {
        System.out.println("removeAllExample start\n");
        // removeAllExample start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t = new DefaultTransaction("locations");
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    store.getFeatureWriter("locations", Filter.INCLUDE, t);

            SimpleFeature feature;
            try {
                while (writer.hasNext()) {
                    feature = writer.next();
                    System.out.println("remove " + feature.getID());
                    writer.remove(); // marking contents for removal
                }
            } finally {
                writer.close();
            }
            System.out.println("commit " + t); // now the contents are removed
            t.commit();
        } catch (Throwable eek) {
            t.rollback();
        } finally {
            t.close();
            store.dispose();
        }
        // removeAllExample end
        System.out.println("\nremoveAllExample end\n");

        fileContents("removeAllExample", file);
    }

    @Test
    public void replaceAll() throws Exception {
        System.out.println("replaceAll start\n");
        // replaceAll start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        final SimpleFeatureType type = store.getSchema("locations");
        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        SimpleFeature f;
        DefaultFeatureCollection collection = new DefaultFeatureCollection();

        // 42.3601° N, 71.0589° W Boston 1300 2017
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point boston = gf.createPoint(new Coordinate(-71.0589, 42.3601));
        SimpleFeature bf =
                SimpleFeatureBuilder.build(
                        type, new Object[] {boston, "Boston", 1300, 2017}, "locations.1");
        collection.add(bf);

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
        // replaceAll end
        System.out.println("\nreplaceAll end\n");

        fileContents("replaceAll", file);
    }

    @Test
    public void appendContent() throws Exception {
        System.out.println("appendContent start\n");
        File directory = tmp;
        // appendContent start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        SimpleFeatureType featureType = store.getSchema("locations");

        File file2 = new File(directory, "duplicate.rst");
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put("file", file2);

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        duplicate.createSchema(featureType);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        SimpleFeature feature, newFeature;

        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT);

        writer = duplicate.getFeatureWriterAppend("duplicate", Transaction.AUTO_COMMIT);
        // writer = duplicate.getFeatureWriter("duplicate", Transaction.AUTO_COMMIT);
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

        // appendContent end
        System.out.println("\nappendContent end\n");

        fileContents("appendContent", file2);
    }
}
