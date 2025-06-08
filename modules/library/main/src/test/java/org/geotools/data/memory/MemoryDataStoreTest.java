/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureEvent;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.FeatureLocking;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiLineString;

/**
 * API Test for FeatureSource using MemoryDataStore as a reference implementation.
 *
 * @author Jody Garnett, Refractions Research
 */
public class MemoryDataStoreTest extends DataTestCase {
    MemoryDataStore data;
    SimpleFeatureType riverType;
    SimpleFeature[] riverFeatures;
    ReferencedEnvelope riverBounds;
    Transaction defaultTransaction = new DefaultTransaction();

    /*
     * @see TestCase#setUp()
     */
    @Override
    public void init() throws Exception {
        super.init();
        data = new MemoryDataStore();
        data.addFeatures(roadFeatures);

        // Override river to use CRS
        riverType = SimpleFeatureTypeBuilder.retype(super.riverType, CRS.decode("EPSG:4326"));
        riverBounds = new ReferencedEnvelope(super.riverBounds, CRS.decode("EPSG:4326"));
        riverFeatures = new SimpleFeature[super.riverFeatures.length];
        for (int i = 0; i < riverFeatures.length; i++) {

            riverFeatures[i] = SimpleFeatureBuilder.retype(super.riverFeatures[i], riverType);
        }

        data.addFeatures(riverFeatures);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        defaultTransaction.close();
        data = null;
        super.tearDown();
    }

    @Test
    public void testEmpty() throws Exception {
        SimpleFeatureType type =
                DataUtilities.createType("namespace.typename", "name:String,id:0,geom:MultiLineString");
        MemoryDataStore memory = new MemoryDataStore(type);

        FeatureSource<SimpleFeatureType, SimpleFeature> source = memory.getFeatureSource("typename");
        Assert.assertEquals(0, source.getCount(Query.ALL));
    }

    @Test
    public void testFixture() throws Exception {
        SimpleFeatureType type =
                DataUtilities.createType("namespace.typename", "name:String,id:0,geom:MultiLineString");
        Assert.assertEquals("namespace", "namespace", type.getName().getNamespaceURI());
        Assert.assertEquals("typename", "typename", type.getTypeName());
        Assert.assertEquals("attributes", 3, type.getAttributeCount());

        Assert.assertEquals("a1", "name", type.getDescriptor(0).getLocalName());
        Assert.assertEquals("a1", String.class, type.getDescriptor(0).getType().getBinding());

        Assert.assertEquals("a2", "id", type.getDescriptor(1).getLocalName());
        Assert.assertEquals("a2", Integer.class, type.getDescriptor(1).getType().getBinding());

        Assert.assertEquals("a3", "geom", type.getDescriptor(2).getLocalName());
        Assert.assertEquals(
                "a3", MultiLineString.class, type.getDescriptor(2).getType().getBinding());
    }

    @Test
    public void testMemoryDataStore() throws Exception {
        DataStore store = new MemoryDataStore();
        assertNotNull(store);
        String[] typeNames = store.getTypeNames();
        assertNotNull(typeNames);
        Assert.assertEquals(0, typeNames.length);
    }

    /*
     * Test for void MemoryDataStore(SimpleFeatureCollection)
     */
    @Test
    public void testMemoryDataStoreFeatureCollection() throws IOException {
        DataStore store = new MemoryDataStore(DataUtilities.collection(roadFeatures));
        assertStoreHasFeatureType(store, "road");
    }

    /*
     * Test for void MemoryDataStore.addFeatures(FeatureCollection<SimpleFeatureType,SimpleFeature> collection)
     */
    @Test
    public void testMemoryDataStoreAddFeatures() throws Exception {
        MemoryDataStore store = new MemoryDataStore();
        assertNotNull(store);
        store.addFeatures(DataUtilities.collection(roadFeatures));
        assertStoreHasFeatureType(store, "road");
        SimpleFeatureSource featureSource = store.getFeatureSource("road");
        SimpleFeatureCollection features = featureSource.getFeatures();
        int size = features.size();
        Assert.assertEquals(roadFeatures.length, size);
    }
    /*
     * Test for void MemoryDataStore(FeatureReader)
     */
    @Test
    public void testMemoryDataStoreFeatureArray() throws IOException {
        DataStore store = new MemoryDataStore(roadFeatures);
        assertStoreHasFeatureType(store, "road");
    }

    /*
     * Test for void MemoryDataStore(FeatureReader)
     */
    @Test
    public void testMemoryDataStoreFeatureReader() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(roadFeatures)) {
            DataStore store = new MemoryDataStore(reader);
            assertStoreHasFeatureType(store, "road");
        }
    }

    private void assertStoreHasFeatureType(DataStore store, String featureType) throws IOException {
        assertNotNull(store);
        assertNotNull(featureType);
        SimpleFeatureType schema = store.getSchema(featureType);
        assertNotNull(schema);
    }

    @Test
    public void testGetFeatureTypes() throws IOException {
        String[] names = data.getTypeNames();
        Assert.assertEquals(2, names.length);
        assertTrue(contains(names, "road"));
        assertTrue(contains(names, "river"));
    }

    boolean contains(Object[] array, Object expected) {
        if (array == null || array.length == 0) {
            return false;
        }

        for (Object o : array) {
            if (o.equals(expected)) {
                return true;
            }
        }

        return false;
    }

    /** Like contain but based on match rather than equals */
    boolean containsLax(SimpleFeature[] array, SimpleFeature expected) {
        if (array == null || array.length == 0) {
            return false;
        }

        for (SimpleFeature simpleFeature : array) {
            if (match(simpleFeature, expected)) {
                return true;
            }
        }

        return false;
    }

    /** Compare based on attributes not getID allows comparison of Diff contents */
    boolean match(SimpleFeature expected, SimpleFeature actual) {
        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < type.getAttributeCount(); i++) {
            Object av = actual.getAttribute(i);
            Object ev = expected.getAttribute(i);

            if (av == null && ev != null) {
                return false;
            } else if (ev == null && av != null) {
                return false;
            } else if (!av.equals(ev)) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testGetSchema() throws IOException {
        assertSame(roadType, data.getSchema("road"));
        assertSame(riverType, data.getSchema("river"));
    }

    void assertCovers(String msg, SimpleFeatureCollection c1, SimpleFeatureCollection c2) {
        if (c1 == c2) {
            return;
        }

        assertNotNull(msg, c1);
        assertNotNull(msg, c2);
        Assert.assertEquals(msg + " size", c1.size(), c2.size());

        SimpleFeature f;

        try (SimpleFeatureIterator i = c1.features()) {
            while (i.hasNext()) {
                f = i.next();
                assertTrue(msg + " " + f.getID(), c2.contains(f));
            }
        }
    }

    @Test
    public void testGetFeatureReader() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureSource("road").getReader()) {
            assertCovered(roadFeatures, reader);
            assertFalse(reader.hasNext());
        }
    }

    @Test
    public void testGetFeatureReaderMutability() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureSource("road").getReader()) {
            SimpleFeature feature;

            while (reader.hasNext()) {
                feature = reader.next();
                feature.setAttribute("name", null);
            }
        }

        @SuppressWarnings("PMD.CloseResource") // need to test behavior after close
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureSource("road").getReader();
        while (reader.hasNext()) {
            SimpleFeature feature = reader.next();
            assertNotNull(feature.getAttribute("name"));
        }

        reader.close();

        try {
            reader.next();
            fail("next should fail with an IOException");
        } catch (IOException expected) {
        }
    }

    @Test
    public void testGetFeatureReaderConcurancy() throws NoSuchElementException, IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader1 =
                        data.getFeatureSource("road").getReader();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader2 =
                        data.getFeatureSource("road").getReader();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader3 =
                        data.getFeatureSource("river").getReader()) {

            while (reader1.hasNext() || reader2.hasNext() || reader3.hasNext()) {
                assertTrue(contains(roadFeatures, reader1.next()));
                assertTrue(contains(roadFeatures, reader2.next()));

                if (reader3.hasNext()) {
                    assertTrue(contains(riverFeatures, reader3.next()));
                }
            }

            try {
                reader1.next();
                fail("next should fail with an IOException");
            } catch (IOException expected) {
            }

            try {
                reader2.next();
                fail("next should fail with an IOException");
            } catch (IOException expected) {
            }

            try {
                reader3.next();
                fail("next should fail with an IOException");
            } catch (IOException expected) {
            }
        }
    }

    @Test
    public void testGetFeatureReaderFilterAutoCommit() throws NoSuchElementException, IOException {
        SimpleFeatureType type = data.getSchema("road");

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
            assertFalse(reader instanceof FilteringFeatureReader);
            Assert.assertEquals(type, reader.getFeatureType());
            Assert.assertEquals(roadFeatures.length, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", Filter.EXCLUDE), Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(type, reader.getFeatureType());
            Assert.assertEquals(0, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", rd1Filter), Transaction.AUTO_COMMIT)) {
            assertTrue(reader instanceof FilteringFeatureReader);
            Assert.assertEquals(type, reader.getFeatureType());
            Assert.assertEquals(1, count(reader));
        }
    }

    @Test
    public void testGetFeatureReaderFilterTransaction() throws NoSuchElementException, IOException {

        SimpleFeatureType type = data.getSchema("road");

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", Filter.EXCLUDE), defaultTransaction)) {
            Assert.assertEquals(type, reader.getFeatureType());
            Assert.assertEquals(0, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road"), defaultTransaction)) {
            assertTrue(reader instanceof DiffFeatureReader);
            Assert.assertEquals(type, reader.getFeatureType());
            Assert.assertEquals(roadFeatures.length, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", rd1Filter), defaultTransaction)) {

            Assert.assertEquals(type, reader.getFeatureType());
            Assert.assertEquals(1, count(reader));
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                ((ContentFeatureStore) data.getFeatureSource("road", defaultTransaction)).getWriter(Filter.INCLUDE)) {
            SimpleFeature feature;

            while (writer.hasNext()) {
                feature = writer.next();

                if (feature.getID().equals("road.rd1")) {
                    writer.remove();
                }
            }
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", Filter.EXCLUDE), defaultTransaction)) {
            Assert.assertEquals(0, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road"), defaultTransaction)) {
            Assert.assertEquals(roadFeatures.length - 1, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", rd1Filter), defaultTransaction)) {
            Assert.assertEquals(0, count(reader));
        }

        defaultTransaction.rollback();
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", Filter.EXCLUDE), defaultTransaction)) {
            Assert.assertEquals(0, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road"), defaultTransaction)) {
            Assert.assertEquals(roadFeatures.length, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", rd1Filter), defaultTransaction)) {
            Assert.assertEquals(1, count(reader));
        }
    }

    /**
     * When a data store is loaded with a reader, it would be nice if the memory data store preserved feature order, so
     * that features are always rendered the same way (rendering is different if order changes and features do overlap)
     */
    @Test
    public void testOrderPreservationRoad() throws Exception {
        assertOrderSame(roadFeatures);
    }

    @Test
    public void testOrderPreservationRiver() throws Exception {
        assertOrderSame(riverFeatures);
    }

    @Test
    public void testOrderPreservationMemFetures() throws Exception {
        SimpleFeature[] dynFeatures = new SimpleFeature[3];
        dynFeatures[0] = SimpleFeatureBuilder.build(
                roadType, new Object[] {Integer.valueOf(1), line(new int[] {1, 1, 2, 2, 4, 2, 5, 1}), "r1"}, null);
        dynFeatures[1] = SimpleFeatureBuilder.build(
                roadType, new Object[] {Integer.valueOf(2), line(new int[] {3, 0, 3, 2, 3, 3, 3, 4}), "r2"}, null);
        dynFeatures[2] = SimpleFeatureBuilder.build(
                roadType, new Object[] {Integer.valueOf(3), line(new int[] {3, 2, 4, 2, 5, 3}), "r3"}, null);
        assertOrderSame(dynFeatures);
    }

    void assertOrderSame(SimpleFeature... features) throws Exception {
        // init using readers
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(features)) {
            DataStore store1 = new MemoryDataStore(reader);
            assertReaderOrderSame(features, store1);
        }

        // init using array directly
        DataStore store2 = new MemoryDataStore(features);
        assertReaderOrderSame(features, store2);
    }

    private void assertReaderOrderSame(SimpleFeature[] features, DataStore store) throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> r1 = store.getFeatureReader(
                        new Query(features[0].getFeatureType().getTypeName()), Transaction.AUTO_COMMIT);
                FeatureReader<SimpleFeatureType, SimpleFeature> r2 = DataUtilities.reader(features)) {

            while (r1.hasNext() && r2.hasNext()) {
                SimpleFeature f1 = r1.next();
                SimpleFeature f2 = r2.next();
                Assert.assertEquals(f1, f2);
            }
            Assert.assertEquals(r1.hasNext(), r2.hasNext());
        }
    }

    void assertCovered(SimpleFeature[] features, FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws NoSuchElementException, IOException {
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                assertTrue(contains(features, reader.next()));
                count++;
            }
        }

        Assert.assertEquals(features.length, count);
    }

    /** Ensure that FeatureReader<SimpleFeatureType, SimpleFeature> reader contains extactly the contents of array. */
    boolean covers(FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeature... array)
            throws NoSuchElementException, IOException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!contains(array, feature)) {
                    return false;
                }

                count++;
            }
        }

        return count == array.length;
    }

    boolean covers(SimpleFeatureIterator reader, SimpleFeature... array) throws NoSuchElementException, IOException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!contains(array, feature)) {
                    return false;
                }

                count++;
            }
        }

        return count == array.length;
    }

    boolean coversLax(FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeature... array)
            throws NoSuchElementException, IOException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        }

        return count == array.length;
    }

    boolean coversLax(SimpleFeatureIterator reader, SimpleFeature... array) throws NoSuchElementException, IOException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        }

        return count == array.length;
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, Filter, Transaction)
     */
    @Test(expected = IOException.class)
    public void testGetFeatureWriter() throws NoSuchElementException, IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(roadFeatures.length, count(writer));

            assertFalse(writer.hasNext());
            writer.next();
            fail("Should not be able to use a closed writer");
        }
    }

    @Test
    public void testGetFeatureWriterRemove() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Transaction.AUTO_COMMIT)) {
            SimpleFeature feature;

            while (writer.hasNext()) {
                feature = writer.next();

                if (feature.getID().equals("road.rd1")) {
                    writer.remove();
                }
            }

            Assert.assertEquals(
                    roadFeatures.length - 1, data.entry("road").getMemory().size());
        }
    }

    @Test
    public void testGetFeaturesWriterAdd() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Transaction.AUTO_COMMIT)) {
            SimpleFeature feature;

            while (writer.hasNext()) {
                feature = writer.next();
            }

            assertFalse(writer.hasNext());
            feature = writer.next();
            feature.setAttributes(newRoad.getAttributes());
            writer.write();
            assertFalse(writer.hasNext());
            Assert.assertEquals(
                    roadFeatures.length + 1, data.entry("road").getMemory().size());
        }
    }

    @Test
    public void testGetFeaturesWriterModify() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Transaction.AUTO_COMMIT)) {
            SimpleFeature feature;

            while (writer.hasNext()) {
                feature = writer.next();

                if (feature.getID().equals("road.rd1")) {
                    feature.setAttribute("name", "changed");
                    writer.write();
                }
            }

            feature = data.entry("road").getMemory().get("road.rd1");
            Assert.assertEquals("changed", feature.getAttribute("name"));
        }
    }

    @Test
    public void testGetFeatureWriterTypeNameTransaction() throws NoSuchElementException, IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(roadFeatures.length, count(writer));
        }
    }

    @Test
    public void testGetFeatureWriterAppendTypeNameTransaction() throws Exception {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriterAppend("road", Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(0, count(writer));
        }
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, boolean, Transaction)
     */
    @Test
    public void testGetFeatureWriterFilter() throws NoSuchElementException, IOException {

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Filter.EXCLUDE, Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(0, count(writer));
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", Filter.INCLUDE, Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(roadFeatures.length, count(writer));
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter("road", rd1Filter, Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(1, count(writer));
        }
    }

    /** Test two transactions one removing feature, and one adding a feature. */
    @Test
    public void testGetFeatureWriterTransaction() throws Exception {

        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction();
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 = data.getFeatureWriter("road", rd1Filter, t1);
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 = data.getFeatureWriterAppend("road", t2)) {
            SimpleFeature feature;
            SimpleFeature[] ORIGIONAL = roadFeatures;
            SimpleFeature[] REMOVE = new SimpleFeature[ORIGIONAL.length - 1];
            SimpleFeature[] ADD = new SimpleFeature[ORIGIONAL.length + 1];
            SimpleFeature[] FINAL = new SimpleFeature[ORIGIONAL.length];
            int i;
            int index = 0;

            for (i = 0; i < ORIGIONAL.length; i++) {
                feature = ORIGIONAL[i];

                if (!feature.getID().equals("road.rd1")) {
                    REMOVE[index++] = feature;
                }
            }

            for (i = 0; i < ORIGIONAL.length; i++) {
                ADD[i] = ORIGIONAL[i];
            }

            ADD[i] = newRoad;

            for (i = 0; i < REMOVE.length; i++) {
                FINAL[i] = REMOVE[i];
            }

            FINAL[i] = newRoad;

            // start of with ORIGINAL
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGIONAL));
            }

            // writer 1 removes road.rd1 on t1
            // -------------------------------
            // - tests transaction independence from DataStore
            while (writer1.hasNext()) {
                feature = writer1.next();
                Assert.assertEquals("road.rd1", feature.getID());
                writer1.remove();
            }

            // still have ORIGIONAL and t1 has REMOVE
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGIONAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t1)) {
                assertTrue(covers(reader, REMOVE));
            }

            // close writer1
            // --------------
            // ensure that modification is left up to transaction commmit
            writer1.close();

            // We still have ORIGIONAL and t1 has REMOVE
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGIONAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t1)) {
                assertTrue(covers(reader, REMOVE));
            }

            // writer 2 adds road.rd4 on t2
            // ----------------------------
            // - tests transaction independence from each other
            feature = writer2.next();
            feature.setAttributes(newRoad.getAttributes());
            writer2.write();

            // We still have ORIGIONAL and t2 has ADD
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGIONAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t2)) {
                assertTrue(coversLax(reader, ADD));
            }

            // close writer2
            // -------------
            // ensure that modification is left up to transaction commmit
            writer2.close();

            // Still have ORIGIONAL and t2 has ADD
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGIONAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t2)) {
                assertTrue(coversLax(reader, ADD));
            }

            // commit t1
            // ---------
            // -ensure that delayed writing of transactions takes place
            //
            t1.commit();

            // We now have REMOVE, as does t1 (which has not additional diffs)
            // t2 will have FINAL
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, REMOVE));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t1)) {
                assertTrue(covers(reader, REMOVE));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t2)) {
                assertTrue(coversLax(reader, FINAL));
            }

            // commit t2
            // ---------
            // -ensure that everyone is FINAL at the end of the day
            t2.commit();

            // We now have Number( remove one and add one)
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), Transaction.AUTO_COMMIT)) {
                assertTrue(coversLax(reader, FINAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t1)) {
                assertTrue(coversLax(reader, FINAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    data.getFeatureReader(new Query("road"), t2)) {
                assertTrue(coversLax(reader, FINAL));
            }
        }
    }

    /** Test the transaction when multiple edits occur using a transaction and a fid filter. */
    @Test
    public void testModifyInTransactionFidFilter() throws Exception {
        GeometryFactory fac = new GeometryFactory();

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 =
                data.getFeatureWriter("road", rd1Filter, defaultTransaction)) {
            writer1.next()
                    .setDefaultGeometry(
                            fac.createLineString(new Coordinate[] {new Coordinate(0, 0), new Coordinate(0, 1)}));
            writer1.write();
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", rd1Filter), defaultTransaction)) {

            Geometry geom1 = (Geometry) reader.next().getDefaultGeometry();

            Assert.assertEquals(new Coordinate(0, 0), geom1.getCoordinates()[0]);
            Assert.assertEquals(new Coordinate(0, 1), geom1.getCoordinates()[1]);
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 =
                data.getFeatureWriter("road", rd1Filter, defaultTransaction)) {
            writer1.next()
                    .setDefaultGeometry(
                            fac.createLineString(new Coordinate[] {new Coordinate(10, 0), new Coordinate(10, 1)}));
            writer1.write();
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", rd1Filter), defaultTransaction)) {
            Geometry geom1 = (Geometry) reader.next().getDefaultGeometry();
            Assert.assertEquals(new Coordinate(10, 0), geom1.getCoordinates()[0]);
            Assert.assertEquals(new Coordinate(10, 1), geom1.getCoordinates()[1]);
        }

        SimpleFeature feature;
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriterAppend("road", defaultTransaction)) {
            feature = writer.next();
            feature.setDefaultGeometry(
                    fac.createLineString(new Coordinate[] {new Coordinate(20, 0), new Coordinate(20, 1)}));
            writer.write();
        }

        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        Id filter = filterFactory.id(Collections.singleton(filterFactory.featureId(feature.getID())));

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", filter), defaultTransaction)) {
            Geometry geom1 = (Geometry) reader.next().getDefaultGeometry();
            reader.close();
            Assert.assertEquals(new Coordinate(20, 0), geom1.getCoordinates()[0]);
            Assert.assertEquals(new Coordinate(20, 1), geom1.getCoordinates()[1]);
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 =
                data.getFeatureWriter("road", filter, defaultTransaction)) {
            writer1.next()
                    .setDefaultGeometry(
                            fac.createLineString(new Coordinate[] {new Coordinate(30, 0), new Coordinate(30, 1)}));
            writer1.write();
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(new Query("road", filter), defaultTransaction)) {
            Geometry geom1 = (Geometry) reader.next().getDefaultGeometry();
            Assert.assertEquals(new Coordinate(30, 0), geom1.getCoordinates()[0]);
            Assert.assertEquals(new Coordinate(30, 1), geom1.getCoordinates()[1]);
        }
    }

    // Feature Source Testing
    @Test
    public void testGetFeatureSourceRoad() throws IOException {
        SimpleFeatureSource road = data.getFeatureSource("road");

        assertSame(roadType, road.getSchema());
        assertSame(data, road.getDataStore());
        Assert.assertEquals(3, road.getCount(Query.ALL));
        Assert.assertEquals(new ReferencedEnvelope(1, 5, 0, 4, null), road.getBounds(Query.ALL));

        SimpleFeatureCollection all = road.getFeatures();
        Assert.assertEquals(3, all.size());
        Assert.assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection expected = DataUtilities.collection(roadFeatures);

        assertCovers("all", expected, all);
        Assert.assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection some = road.getFeatures(rd12Filter);
        Assert.assertEquals(2, some.size());
        Assert.assertEquals(rd12Bounds, some.getBounds());
        Assert.assertEquals(some.getSchema(), road.getSchema());

        Query query = new Query("road", rd12Filter, new String[] {"name", "geom"});

        SimpleFeatureCollection half = road.getFeatures(query);
        Assert.assertEquals(2, half.size());
        Assert.assertEquals(2, half.getSchema().getAttributeCount());
        try (SimpleFeatureIterator reader = half.features()) {
            SimpleFeatureType type = reader.next().getType();
            SimpleFeatureType actual = half.getSchema();

            Assert.assertEquals(type.getTypeName(), actual.getTypeName());
            Assert.assertEquals(type.getName(), actual.getName());
            Assert.assertEquals(type.getAttributeCount(), actual.getAttributeCount());
            for (int i = 0; i < type.getAttributeCount(); i++) {
                Assert.assertEquals(type.getDescriptor(i), actual.getDescriptor(i));
            }
            Assert.assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
            Assert.assertEquals(type, actual);
            Envelope b = half.getBounds();
            Assert.assertEquals(new Envelope(1, 5, 0, 4), b);
        }
    }

    @Test
    public void testGetFeatureSourceRiver() throws NoSuchElementException, IOException {
        SimpleFeatureSource river = data.getFeatureSource("river");

        assertSame(riverType, river.getSchema());
        assertSame(data, river.getDataStore());

        SimpleFeatureCollection all = river.getFeatures();
        Assert.assertEquals(2, all.size());
        Assert.assertEquals(riverBounds, all.getBounds());
        assertTrue("rivers", covers(all.features(), riverFeatures));

        SimpleFeatureCollection expected = DataUtilities.collection(riverFeatures);
        assertCovers("all", expected, all);
        Assert.assertEquals(riverBounds, all.getBounds());
    }

    /*
     * Utility method used to verify that a collection of features was correctly transformed
     */
    private void testTransformedFeatures(
            SimpleFeatureCollection sourceFeatures,
            SimpleFeatureCollection transformedFeatures,
            CoordinateReferenceSystem nativeCRS,
            CoordinateReferenceSystem forcedCRS,
            CoordinateReferenceSystem reprojectCRS)
            throws TransformException, FactoryException {

        // The expected CRS of the transformed features
        CoordinateReferenceSystem targetCRS = nativeCRS;
        GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();

        if (reprojectCRS != null) {
            targetCRS = reprojectCRS;
            // Set up geometry transform
            if (forcedCRS == null) {
                transformer.setMathTransform(CRS.findMathTransform(nativeCRS, reprojectCRS, true));
            } else {
                transformer.setMathTransform(CRS.findMathTransform(forcedCRS, reprojectCRS, true));
            }
        } else if (forcedCRS != null) {
            targetCRS = forcedCRS;
        }

        try (SimpleFeatureIterator i = sourceFeatures.features();
                SimpleFeatureIterator j = transformedFeatures.features()) {

            // Go through all the features
            while (i.hasNext() && j.hasNext()) {
                SimpleFeature sourceFeature = i.next();
                SimpleFeature transformedFeature = j.next();
                Assert.assertEquals(
                        targetCRS, transformedFeature.getFeatureType().getCoordinateReferenceSystem());

                for (int k = 0; k < sourceFeature.getAttributes().size(); k++) {
                    Object o = sourceFeature.getAttributes().get(k);

                    // Check that the geometry was transformed correctly
                    if (o instanceof Geometry) {
                        Geometry sourceGeometry = (Geometry) o;
                        Geometry transformedGeometry =
                                (Geometry) transformedFeature.getAttributes().get(k);

                        Geometry expectedGeometry = sourceGeometry.copy();
                        if (reprojectCRS != null) {
                            expectedGeometry = transformer.transform(expectedGeometry);
                        }
                        assertEquals(expectedGeometry, transformedGeometry);
                    }
                }
            }
        }
    }

    @Test
    public void testSetsEnvelopeCrsFromQuery() throws Exception {
        Query query = new Query(Query.ALL);
        query.setCoordinateSystem(DefaultEngineeringCRS.CARTESIAN_2D);
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");

        SimpleFeatureSource river = data.getFeatureSource("river");
        SimpleFeatureCollection features = river.getFeatures(query);
        SimpleFeatureCollection expectedFeatures = DataUtilities.collection(riverFeatures);
        testTransformedFeatures(expectedFeatures, features, sourceCRS, DefaultEngineeringCRS.CARTESIAN_2D, null);
    }

    @Test
    public void testReprojectFeaturesCrsFromQuery() throws Exception {
        Query query = new Query(Query.ALL);
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3005");
        query.setCoordinateSystemReproject(targetCRS);

        SimpleFeatureSource river = data.getFeatureSource("river");
        SimpleFeatureCollection features = river.getFeatures(query);
        SimpleFeatureCollection expectedFeatures = DataUtilities.collection(riverFeatures);
        testTransformedFeatures(expectedFeatures, features, sourceCRS, null, targetCRS);
    }

    @Test
    public void testSetReprojectFeaturesCrsFromQuery() throws Exception {
        Query query = new Query(Query.ALL);
        query.setCoordinateSystem(DefaultEngineeringCRS.GENERIC_2D);
        query.setCoordinateSystemReproject(DefaultEngineeringCRS.CARTESIAN_2D);
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");

        SimpleFeatureSource river = data.getFeatureSource("river");
        SimpleFeatureCollection features = river.getFeatures(query);
        SimpleFeatureCollection expectedFeatures = DataUtilities.collection(riverFeatures);
        testTransformedFeatures(
                expectedFeatures,
                features,
                sourceCRS,
                DefaultEngineeringCRS.GENERIC_2D,
                DefaultEngineeringCRS.CARTESIAN_2D);
    }

    @Test
    public void testSetsFeaturesCrsFromFeatureType() throws Exception {
        Query query = new Query(Query.ALL);
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");

        SimpleFeatureSource river = data.getFeatureSource("river");
        SimpleFeatureCollection features = river.getFeatures(query);
        SimpleFeatureCollection expectedFeatures = DataUtilities.collection(riverFeatures);
        testTransformedFeatures(expectedFeatures, features, sourceCRS, null, null);
    }

    //
    // Feature Store Testing
    //
    @Test
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
        AttributeDescriptor descriptor = roadType.getDescriptor("name");
        road.modifyFeatures(descriptor.getName(), "changed", rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        Assert.assertEquals("changed", results.features().next().getAttribute("name"));
    }

    @Test
    public void testGetFeatureStoreModifyFeatures2() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
        AttributeDescriptor descriptor = roadType.getDescriptor("name");
        road.modifyFeatures(
                new Name[] {descriptor.getName()},
                new Object[] {
                    "changed",
                },
                rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        Assert.assertEquals("changed", results.features().next().getAttribute("name"));
    }

    @Test
    public void testGetFeatureStoreRemoveFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        road.removeFeatures(rd1Filter);
        Assert.assertEquals(0, road.getFeatures(rd1Filter).size());
        Assert.assertEquals(roadFeatures.length - 1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreAddFeatures() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(newRoad)) {
            SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

            road.addFeatures(DataUtilities.collection(reader));
            Assert.assertEquals(roadFeatures.length + 1, road.getFeatures().size());
        }
    }

    @Test
    public void testGetFeatureStoreSetFeatures() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(newRoad)) {
            SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

            road.setFeatures(reader);
            Assert.assertEquals(1, road.getFeatures().size());
        }
    }

    @Test
    public void testGetFeatureStoreTransactionSupport() throws Exception {
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {
            SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
            SimpleFeatureStore road1 = (SimpleFeatureStore) data.getFeatureSource("road");
            SimpleFeatureStore road2 = (SimpleFeatureStore) data.getFeatureSource("road");

            road1.setTransaction(t1);
            road2.setTransaction(t2);

            SimpleFeature feature;
            SimpleFeature[] ORIGIONAL = roadFeatures;
            SimpleFeature[] REMOVE = new SimpleFeature[ORIGIONAL.length - 1];
            SimpleFeature[] ADD = new SimpleFeature[ORIGIONAL.length + 1];
            SimpleFeature[] FINAL = new SimpleFeature[ORIGIONAL.length];
            int i;
            int index = 0;

            for (i = 0; i < ORIGIONAL.length; i++) {
                feature = ORIGIONAL[i];

                if (!feature.getID().equals("road.rd1")) {
                    REMOVE[index++] = feature;
                }
            }

            for (i = 0; i < ORIGIONAL.length; i++) {
                ADD[i] = ORIGIONAL[i];
            }

            ADD[i] = newRoad;

            for (i = 0; i < REMOVE.length; i++) {
                FINAL[i] = REMOVE[i];
            }

            FINAL[i] = newRoad;

            // start of with ORIGINAL
            assertTrue(covers(road.getFeatures().features(), ORIGIONAL));

            // road1 removes road.rd1 on t1
            // -------------------------------
            // - tests transaction independence from DataStore
            road1.removeFeatures(rd1Filter);

            // still have ORIGIONAL and t1 has REMOVE
            assertTrue(covers(road.getFeatures().features(), ORIGIONAL));
            assertTrue(covers(road1.getFeatures().features(), REMOVE));

            // road2 adds road.rd4 on t2
            // ----------------------------
            // - tests transaction independence from each other
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(newRoad)) {
                road2.addFeatures(DataUtilities.collection(reader));
            }

            // We still have ORIGIONAL, t1 has REMOVE, and t2 has ADD
            assertTrue(covers(road.getFeatures().features(), ORIGIONAL));
            assertTrue(covers(road1.getFeatures().features(), REMOVE));
            assertTrue(coversLax(road2.getFeatures().features(), ADD));

            // commit t1
            // ---------
            // -ensure that delayed writing of transactions takes place
            //
            t1.commit();

            // We now have REMOVE, as does t1 (which has not additional diffs)
            // t2 will have FINAL
            assertTrue(covers(road.getFeatures().features(), REMOVE));
            assertTrue(covers(road1.getFeatures().features(), REMOVE));
            assertTrue(coversLax(road2.getFeatures().features(), FINAL));

            // commit t2
            // ---------
            // -ensure that everyone is FINAL at the end of the day
            t2.commit();

            // We now have Number( remove one and add one)
            assertTrue(coversLax(road.getFeatures().features(), FINAL));
            assertTrue(coversLax(road1.getFeatures().features(), FINAL));
            assertTrue(coversLax(road2.getFeatures().features(), FINAL));
        }
    }

    boolean isLocked(String typeName, String fid) {
        InProcessLockingManager lockingManager = (InProcessLockingManager) data.getLockingManager();
        return lockingManager.isLocked(typeName, fid);
    }

    @Test
    public void testFeatureEvents() throws Exception {
        SimpleFeatureStore store1 = (SimpleFeatureStore)
                data.getFeatureSource(roadFeatures[0].getFeatureType().getTypeName());
        SimpleFeatureStore store2 = (SimpleFeatureStore)
                data.getFeatureSource(roadFeatures[0].getFeatureType().getTypeName());
        store1.setTransaction(defaultTransaction);
        class Listener implements FeatureListener {
            String name;
            List<FeatureEvent> events = new ArrayList<>();

            public Listener(String name) {
                this.name = name;
            }

            @Override
            public void changed(FeatureEvent featureEvent) {
                this.events.add(featureEvent);
            }

            FeatureEvent getEvent(int i) {
                return events.get(i);
            }

            @Override
            public String toString() {
                return "Feature Listener " + name;
            }
        }
        Listener listener1 = new Listener("one");
        Listener listener2 = new Listener("two");

        store1.addFeatureListener(listener1);
        store2.addFeatureListener(listener2);
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);

        // test that only the listener listening with the current transaction gets the event.
        final SimpleFeature feature = roadFeatures[0];
        store1.removeFeatures(factory.id(Collections.singleton(factory.featureId(feature.getID()))));
        Assert.assertEquals(1, listener1.events.size());
        Assert.assertEquals(0, listener2.events.size());
        FeatureEvent event = listener1.getEvent(0);
        Assert.assertEquals(feature.getBounds(), event.getBounds());
        Assert.assertEquals(FeatureEvent.Type.REMOVED, event.getType());

        // test that commit only sends events to listener2.
        listener1.events.clear();
        listener2.events.clear();

        store1.getTransaction().commit();

        Assert.assertEquals(0, listener1.events.size());

        Assert.assertEquals(1, listener2.events.size());
        event = listener2.getEvent(0);
        Assert.assertEquals(feature.getBounds(), event.getBounds());
        Assert.assertEquals(FeatureEvent.Type.COMMIT, event.getType());

        // test add same as modify
        listener1.events.clear();
        listener2.events.clear();

        store1.addFeatures(DataUtilities.collection(feature));

        Assert.assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        Assert.assertEquals(feature.getBounds(), event.getBounds());
        Assert.assertEquals(FeatureEvent.Type.ADDED, event.getType());
        Assert.assertEquals(0, listener2.events.size());

        // test that rollback only sends events to listener1.
        listener1.events.clear();
        listener2.events.clear();

        store1.getTransaction().rollback();

        Assert.assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        Assert.assertEquals(feature.getBounds(), event.getBounds());
        Assert.assertEquals(FeatureEvent.Type.ROLLBACK, event.getType());

        Assert.assertEquals(0, listener2.events.size());

        // this is how Auto_commit is supposed to work
        listener1.events.clear();
        listener2.events.clear();
        store2.addFeatures(DataUtilities.collection(feature));

        Assert.assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        Assert.assertEquals(feature.getBounds(), event.getBounds());
        Assert.assertEquals(FeatureEvent.Type.ADDED, event.getType());
        Assert.assertEquals(1, listener2.events.size());
    }

    //
    // FeatureLocking Testing
    //
    /*
     * Test for void lockFeatures()
     */
    @Test
    public void testLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", 3600);
        SimpleFeatureLocking road = (SimpleFeatureLocking) data.getFeatureSource("road");
        road.setFeatureLock(lock);

        assertFalse(isLocked("road", "road.rd1"));
        road.lockFeatures();
        assertTrue(isLocked("road", "road.rd1"));
    }

    @Test
    public void testUnLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", 3600);

        @SuppressWarnings("unchecked")
        FeatureLocking<SimpleFeatureType, SimpleFeature> road =
                (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road.setFeatureLock(lock);
        road.lockFeatures();

        try {
            road.unLockFeatures();
            fail("unlock should fail due on AUTO_COMMIT");
        } catch (IOException expected) {
        }
        try (Transaction t = new DefaultTransaction()) {
            road.setTransaction(t);
            try {
                road.unLockFeatures();
                fail("unlock should fail due lack of authorization");
            } catch (IOException expected) {

            }
            t.addAuthorization(lock.getAuthorization());
            road.unLockFeatures();
        }
    }

    @Test
    public void testLockFeatureInteraction() throws IOException {
        FeatureLock lockA = new FeatureLock("LockA", 3600);
        FeatureLock lockB = new FeatureLock("LockB", 3600);
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {
            @SuppressWarnings("unchecked")
            FeatureLocking<SimpleFeatureType, SimpleFeature> road1 =
                    (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
            @SuppressWarnings("unchecked")
            FeatureLocking<SimpleFeatureType, SimpleFeature> road2 =
                    (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
            road1.setTransaction(t1);
            road2.setTransaction(t2);
            road1.setFeatureLock(lockA);
            road2.setFeatureLock(lockB);

            assertFalse(isLocked("road", "road.rd1"));
            assertFalse(isLocked("road", "road.rd2"));
            assertFalse(isLocked("road", "road.rd3"));

            road1.lockFeatures(rd1Filter);
            assertTrue(isLocked("road", "road.rd1"));
            assertFalse(isLocked("road", "road.rd2"));
            assertFalse(isLocked("road", "road.rd3"));

            road2.lockFeatures(rd2Filter);
            assertTrue(isLocked("road", "road.rd1"));
            assertTrue(isLocked("road", "road.rd2"));
            assertFalse(isLocked("road", "road.rd3"));

            try {
                road1.unLockFeatures(rd1Filter);
                fail("need authorization");
            } catch (IOException expected) {
            }
            t1.addAuthorization(lockA.getAuthorization());
            try {
                road1.unLockFeatures(rd2Filter);
                fail("need correct authorization");
            } catch (IOException expected) {
            }
            road1.unLockFeatures(rd1Filter);
            assertFalse(isLocked("road", "road.rd1"));
            assertTrue(isLocked("road", "road.rd2"));
            assertFalse(isLocked("road", "road.rd3"));

            t2.addAuthorization(lockB.getAuthorization());
            road2.unLockFeatures(rd2Filter);
            assertFalse(isLocked("road", "road.rd1"));
            assertFalse(isLocked("road", "road.rd2"));
            assertFalse(isLocked("road", "road.rd3"));
        }
    }

    @Test
    public void testGetFeatureLockingExpire() throws Exception {
        FeatureLock lock = new FeatureLock("Timed", 500);
        @SuppressWarnings("unchecked")
        FeatureLocking<SimpleFeatureType, SimpleFeature> road =
                (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road.setFeatureLock(lock);
        assertFalse(isLocked("road", "road.rd1"));
        road.lockFeatures(rd1Filter);
        assertTrue(isLocked("road", "road.rd1"));
        long then = System.currentTimeMillis();
        do {
            Thread.sleep(15);
        } while (then > System.currentTimeMillis() - 515);
        assertFalse(isLocked("road", "road.rd1"));
    }

    @Test
    public void testRemoveSchema() throws IOException {
        // two featureTypes should be in
        List<Name> names = data.getNames();
        assertNotNull(names);
        Assert.assertEquals(2, names.size());

        data.removeSchema("road");

        List<Name> namesAfterRemove = data.getNames();
        assertNotNull(namesAfterRemove);
        Assert.assertEquals(1, namesAfterRemove.size());
    }

    @Test
    public void testRemoveTypeThatDoesntExistsGracefulWithoutIOException() throws IOException {
        // two featureTypes should be in
        List<Name> names = data.getNames();
        assertNotNull(names);
        Assert.assertEquals(2, names.size());

        try {
            data.removeSchema("typeThatDoesntExists");
        } catch (IOException e) {
            fail("remove Schema should act gracfully if it has never been created for that type");
        }

        // still the same size and no IOException
        List<Name> namesAfterRemove = data.getNames();
        assertNotNull(namesAfterRemove);
        Assert.assertEquals(2, namesAfterRemove.size());
    }

    @Test
    public void testAddingTwoFeaturesWithSameType() throws IOException {
        MemoryDataStore mds = new MemoryDataStore();
        mds.addFeature(roadFeatures[0]);
        mds.addFeature(roadFeatures[1]);

        Assert.assertEquals(2, mds.entry("road").getMemory().size());
    }

    @Test
    public void testCallingAddFeaturesWithArrayTwiceAndExtentInitialCollection() throws IOException {
        MemoryDataStore mds = new MemoryDataStore();
        mds.addFeatures(roadFeatures);

        SimpleFeature road1 = SimpleFeatureBuilder.template(roadType, null);
        mds.addFeatures(road1);

        Assert.assertEquals(
                roadFeatures.length + 1, mds.entry("road").getMemory().size());
    }

    @Test
    public void testCallingAddFeaturesWithCollectionTwiceAndExtentInitialCollection() throws IOException {
        MemoryDataStore mds = new MemoryDataStore();
        mds.addFeatures(Arrays.asList(roadFeatures));

        SimpleFeature road1 = SimpleFeatureBuilder.template(roadType, null);

        mds.addFeatures(Collections.singletonList(road1));

        Assert.assertEquals(
                roadFeatures.length + 1, mds.entry("road").getMemory().size());
    }

    @Test
    public void testCallingAddFeaturesWithReaderTwiceAndExtentInitialCollection() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(roadFeatures)) {
            MemoryDataStore mds = new MemoryDataStore(reader);

            Assert.assertEquals(
                    roadFeatures.length,
                    mds.entry(roadType.getTypeName()).getMemory().size());

            try (FeatureReader<SimpleFeatureType, SimpleFeature> secondReader =
                    DataUtilities.reader(SimpleFeatureBuilder.template(roadType, null))) {
                mds.addFeatures(secondReader);
                Assert.assertEquals(
                        roadFeatures.length + 1, mds.entry("road").getMemory().size());
            }
        }
    }

    @Test
    public void testCallingAddFeaturesWithIteratorTwiceAndExtentInitialCollection() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(roadFeatures)) {
            MemoryDataStore mds = new MemoryDataStore(reader);

            Assert.assertEquals(
                    roadFeatures.length,
                    mds.entry(roadType.getTypeName()).getMemory().size());

            try (SimpleFeatureIterator featureIterator = DataUtilities.collection(
                            SimpleFeatureBuilder.template(roadType, null))
                    .features()) {

                mds.addFeatures(featureIterator);

                Assert.assertEquals(
                        roadFeatures.length + 1, mds.entry("road").getMemory().size());
            }
        }
    }
}
