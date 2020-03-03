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
package org.geotools.data.wfs.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.FilteringFeatureWriter;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.internal.ExceptionDetails;
import org.geotools.data.wfs.internal.WFSException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.MultiLineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.geometry.BoundingBox;

/**
 * Abstract Integration Test.
 *
 * @author Jesse Eichar, Refractions Research
 * @author Niels Charlier
 */
public abstract class AbstractIntegrationTest {

    /** The logger for the filter module. */
    private final Logger LOGGER = Logging.getLogger(getClass());

    protected DataStore data;

    protected static class TestDataType {
        public String typeName;
        public SimpleFeatureType featureType;
        public String stringAttribute;
        public SimpleFeature newFeature;
        public int numberOfFeatures;

        public TestDataType() {}

        SimpleFeature[] features;
        Filter feat1Filter;
        Filter feat2Filter;
        Filter feat12Filter;
        ReferencedEnvelope bounds;
        ReferencedEnvelope feat12Bounds;
    }

    protected TestDataType first;
    protected TestDataType second;

    public AbstractIntegrationTest() {}

    /**
     * Creates and populates a new instance of the datastore. The datastore must not have a roads or
     * rivers type.
     *
     * <p>Something like the following should suffice:
     *
     * <pre>
     * <code>
     *   DataStore data = ....
     *   data.createSchema(roadType);
     *   data.createSchema(riverType);
     *
     *   SimpleFeatureStore roads;
     *   roads = ((SimpleFeatureStore) data.getFeatureSource(getRoadTypeName()));
     *
     *   roads.addFeatures(DataUtilities.collection(roadFeatures));
     *
     *   SimpleFeatureStore rivers = ((SimpleFeatureStore) data.getFeatureSource(riverType
     *           .getTypeName()));
     *
     *   rivers.addFeatures(DataUtilities.collection(riverFeatures));
     *
     *   return data;
     * </code>
     * </pre>
     */
    public abstract DataStore createDataStore() throws Exception;

    /**
     * This method must remove the roads and rivers types from the datastore. It must also close all
     * connections to the datastore if it has connections and get rid of any temporary files.
     */
    public abstract DataStore tearDownDataStore(DataStore data) throws Exception;

    protected abstract TestDataType createFirstType() throws Exception;

    protected abstract TestDataType createSecondType() throws Exception;

    private void completeTestDataType(TestDataType test) throws IOException {
        FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory2(null);

        SimpleFeatureSource source = data.getFeatureSource(test.typeName);
        test.features = grabArray(source.getFeatures());

        test.feat1Filter = ff.id(ff.featureId(test.features[0].getID()));
        test.feat2Filter = ff.id(ff.featureId(test.features[1].getID()));
        test.feat12Filter =
                ff.id(
                        ff.featureId(test.features[0].getID()),
                        ff.featureId(test.features[1].getID()));

        test.bounds =
                new ReferencedEnvelope(
                        test.features[0].getFeatureType().getCoordinateReferenceSystem());
        for (int i = 0; i < test.numberOfFeatures; i++) {
            test.bounds.expandToInclude(new ReferencedEnvelope(test.features[i].getBounds()));
        }

        test.feat12Bounds =
                new ReferencedEnvelope(
                        test.features[0].getFeatureType().getCoordinateReferenceSystem());
        test.feat12Bounds.expandToInclude(new ReferencedEnvelope(test.features[0].getBounds()));
        test.feat12Bounds.expandToInclude(new ReferencedEnvelope(test.features[1].getBounds()));
    }

    @Before
    public void setUp() throws Exception {
        first = createFirstType();
        second = createSecondType();

        try {
            data = createDataStore();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception while making schema", e);
            throw e;
        }

        completeTestDataType(first);
        completeTestDataType(second);
    }

    @After
    public void tearDown() throws Exception {
        tearDownDataStore(data);
        data = null;
    }

    @Test
    public void testFeatureEvents() throws Exception {
        SimpleFeatureStore store1 = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
        SimpleFeatureStore store2 = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
        try (DefaultTransaction transaction = new DefaultTransaction()) {
            store1.setTransaction(transaction);
            class Listener implements FeatureListener {

                List<FeatureEvent> events = new ArrayList<FeatureEvent>();

                public void changed(FeatureEvent featureEvent) {
                    this.events.add(featureEvent);
                }

                FeatureEvent getEvent(int i) {
                    return (FeatureEvent) events.get(i);
                }
            }

            Listener listener1 = new Listener();
            Listener listener2 = new Listener();

            store1.addFeatureListener(listener1);
            store2.addFeatureListener(listener2);

            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

            // test that only the listener listening with the current transaction gets the event.
            final SimpleFeature feature = first.features[0];
            Id fidFilter = ff.id(feature.getIdentifier());

            store1.removeFeatures(fidFilter);

            assertEquals(1, listener1.events.size());
            assertEquals(0, listener2.events.size());

            FeatureEvent event = listener1.getEvent(0);
            assertEquals(feature.getBounds(), event.getBounds());
            assertEquals(FeatureEvent.Type.REMOVED, event.getType());

            // test that commit only sends events to listener2.
            listener1.events.clear();
            listener2.events.clear();

            store1.getTransaction().commit();

            assertEquals(0, listener1.events.size());
            assertEquals(3, listener2.events.size());

            event = listener2.getEvent(0);
            assertEquals(feature.getBounds(), event.getBounds());
            assertEquals(FeatureEvent.Type.REMOVED, event.getType());

            // test add same as modify
            listener1.events.clear();
            listener2.events.clear();

            store1.addFeatures(DataUtilities.collection(feature));

            assertEquals(1, listener1.events.size());
            event = listener1.getEvent(0);
            assertEquals(feature.getBounds(), event.getBounds());
            assertEquals(FeatureEvent.Type.ADDED, event.getType());
            assertEquals(0, listener2.events.size());

            // test that rollback only sends events to listener1.
            listener1.events.clear();
            listener2.events.clear();

            store1.getTransaction().rollback();

            assertEquals(1, listener1.events.size());
            event = listener1.getEvent(0);
            assertNull(event.getBounds());
            assertEquals(FeatureEvent.Type.CHANGED, event.getType());

            assertEquals(0, listener2.events.size());
        }
    }

    @Test
    public void testFixture() throws Exception {
        SimpleFeatureType type =
                DataUtilities.createType(
                        "namespace.typename", "name:String,id:0,geom:MultiLineString");
        assertEquals("namespace", "namespace", type.getName().getNamespaceURI());
        assertEquals("typename", "typename", type.getTypeName());
        assertEquals("attributes", 3, type.getAttributeCount());

        AttributeDescriptor[] a =
                type.getAttributeDescriptors()
                        .toArray(new AttributeDescriptor[type.getAttributeCount()]);
        assertEquals("a1", "name", a[0].getLocalName());
        assertEquals("a1", String.class, a[0].getType().getBinding());

        assertEquals("a2", "id", a[1].getLocalName());
        assertEquals("a2", Integer.class, a[1].getType().getBinding());

        assertEquals("a3", "geom", a[2].getLocalName());
        assertEquals("a3", MultiLineString.class, a[2].getType().getBinding());
    }

    @Test
    public void testGetFeatureTypes() {
        String[] names;

        try {
            names = data.getTypeNames();
            assertTrue(contains(names, first.typeName));
            assertTrue(contains(names, second.typeName));
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail("Fail with an IOException trying to getTypeNames()");
        }
    }

    boolean contains(Object[] array, Object expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(expected)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks that a given SimpleFeatureCollection contains the specified feature
     *
     * @param fc the feature collection we're going to search
     * @param f the feature we're looking for
     * @return true if the feature is in the feature collection, false otherwise
     */
    boolean containsFeatureCollection(SimpleFeatureCollection fc, SimpleFeature f) {
        if ((fc == null) || fc.isEmpty()) {
            return false;
        }

        return containsFeature(fc.toArray(), f);
    }

    /**
     * Checks that a given Feature array contains the specified feature
     *
     * @param array must be an array of features
     * @param expected the expected feature we're looking for
     * @return true if the feature is found, false otherwise
     */
    boolean containsFeature(Object[] array, Object expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (isFeatureEqual((SimpleFeature) array[i], (SimpleFeature) expected)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This function is stolen from DefaultFeature equals method. We want to check for equality
     * except for featureId which we expect to be different.
     *
     * @param feature1 the Feature to test against.
     * @param feature2 the Feature to test for equality.
     * @return <code>true</code> if the object is equal, <code>false</code> otherwise.
     */
    public boolean isFeatureEqual(SimpleFeature feature1, SimpleFeature feature2) {
        if (feature2 == null) {
            return false;
        }

        if (feature2 == feature1) {
            return true;
        }

        if (!feature2.getFeatureType().equals(feature1.getFeatureType())) {
            return false;
        }

        for (int i = 0, ii = feature1.getAttributeCount(); i < ii; i++) {
            Object otherAtt = feature2.getAttribute(i);

            if (feature1.getAttribute(i) == null) {
                if (otherAtt != null) {
                    return false;
                }
            } else {
                if (!feature1.getAttribute(i).equals(otherAtt)) {
                    return false;
                }
            }
        }

        return true;
    }

    /** Like contain but based on match rather than equals */
    boolean containsLax(SimpleFeature[] array, SimpleFeature expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        // SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < array.length; i++) {
            if (match(array[i], expected)) {
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

            if ((av == null) && (ev != null)) {
                return false;
            } else if ((ev == null) && (av != null)) {
                return false;
            } else if (!av.equals(ev)) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testGetSchema() throws IOException {
        SimpleFeatureType firstSchema = data.getSchema(first.typeName);
        SimpleFeatureType secondSchema = data.getSchema(second.typeName);

        assertNotNull(firstSchema);
        assertNotNull(secondSchema);

        assertEquals(first.typeName, firstSchema.getTypeName());
        assertEquals(second.typeName, secondSchema.getTypeName());

        assertSchemaEqual(first.featureType, firstSchema);
        assertSchemaEqual(second.featureType, secondSchema);
    }

    void assertSchemaEqual(SimpleFeatureType expected, SimpleFeatureType actual) {
        assertEquals(second.featureType.getAttributeCount(), actual.getAttributeCount());
        for (int i = 0; i < first.featureType.getAttributeCount(); i++) {
            assertEquals(expected.getDescriptor(i), actual.getDescriptor(i));
        }
    }

    void assertCovers(String msg, SimpleFeatureCollection c1, SimpleFeatureCollection c2) {
        if (c1 == c2) {
            return;
        }

        assertNotNull(msg, c1);
        assertNotNull(msg, c2);
        assertEquals(msg + " size", c1.size(), c2.size());

        SimpleFeature f;

        for (SimpleFeatureIterator i = c1.features(); i.hasNext(); ) {
            f = i.next();
            assertTrue(msg + " " + f.getID(), containsFeatureCollection(c2, f)); // c2.contains(f));
        }
    }

    @Test
    public void testGetFeatureReader() throws Exception {
        Query query = new Query(first.typeName);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertCovered(first.features, reader);
        assertEquals(false, reader.hasNext());
    }

    @Test
    public void testGetFeatureReaderMutability() throws IOException {
        Query query = new Query(first.typeName);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (reader.hasNext()) {
            feature = (SimpleFeature) reader.next();
            feature.setAttribute(first.stringAttribute, null);
        }

        reader.close();

        reader = data.getFeatureReader(query, Transaction.AUTO_COMMIT);

        while (reader.hasNext()) {
            feature = (SimpleFeature) reader.next();
            assertNotNull(feature.getAttribute(first.stringAttribute));
        }

        reader.close();

        try {
            reader.next();
            fail("next should fail with an IOException or NoSuchElementException");
        } catch (IOException expected) {
        } catch (NoSuchElementException expected) {
        }
    }

    @Test
    public void testGetFeatureReaderConcurency() throws Exception {
        Query query = new Query(first.typeName);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader1 =
                data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader2 =
                data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        query = new Query(second.typeName);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader3 =
                data.getFeatureReader(query, Transaction.AUTO_COMMIT);

        while (reader1.hasNext() || reader2.hasNext() || reader3.hasNext()) {
            assertTrue(containsFeature(first.features, reader1.next()));
            assertTrue(containsFeature(first.features, reader2.next()));

            if (reader3.hasNext()) {
                assertTrue(containsFeature(second.features, reader3.next()));
            }
        }

        try {
            reader1.next();
            fail("next should fail with an IOException or NoSuchElementException");
        } catch (IOException expected) {
        } catch (NoSuchElementException expected) {
        }

        try {
            reader2.next();
            fail("next should fail with an IOException or NoSuchElementException");
        } catch (IOException expected) {
        } catch (NoSuchElementException expected) {
        }

        try {
            reader3.next();
            fail("next should fail with an IOException or NoSuchElementException");
        } catch (IOException expected) {
        } catch (NoSuchElementException expected) {
        }

        reader1.close();
        reader2.close();
        reader3.close();
    }

    @Test
    public void testGetFeatureReaderFilterAutoCommit() throws Exception {
        SimpleFeatureType type = data.getSchema(first.typeName);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        Query query = new Query(first.typeName);
        reader = data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertFalse(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(first.features.length, count(reader));

        reader =
                data.getFeatureReader(
                        new Query(first.typeName, Filter.EXCLUDE), Transaction.AUTO_COMMIT);

        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader =
                data.getFeatureReader(
                        new Query(first.typeName, first.feat1Filter), Transaction.AUTO_COMMIT);

        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));
    }

    @Test
    public void testGetFeatureReaderFilterTransaction() throws Exception {
        try (Transaction t = new DefaultTransaction()) {
            SimpleFeatureType type = data.getSchema(first.typeName);
            FeatureReader<SimpleFeatureType, SimpleFeature> reader;

            reader = data.getFeatureReader(new Query(first.typeName, Filter.EXCLUDE), t);

            assertEquals(type, reader.getFeatureType());
            assertEquals(0, count(reader));

            reader = data.getFeatureReader(new Query(first.typeName), t);
            assertTrue(reader instanceof DiffFeatureReader);
            assertEquals(type, reader.getFeatureType());
            assertEquals(first.features.length, count(reader));

            reader = data.getFeatureReader(new Query(first.typeName, first.feat1Filter), t);

            // assertTrue(reader instanceof DiffFeatureReader);//Currently wrapped by a filtering
            // feature reader
            assertEquals(type, reader.getFeatureType());
            assertEquals(1, count(reader));

            SimpleFeatureStore store = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
            store.setTransaction(t);
            store.removeFeatures(first.feat1Filter);

            reader = data.getFeatureReader(new Query(first.typeName, Filter.EXCLUDE), t);
            assertEquals(0, count(reader));

            reader = data.getFeatureReader(new Query(first.typeName), t);
            assertEquals(first.features.length - 1, count(reader));

            reader = data.getFeatureReader(new Query(first.typeName, first.feat1Filter), t);
            assertEquals(0, count(reader));

            t.rollback();
            reader = data.getFeatureReader(new Query(first.typeName, Filter.EXCLUDE), t);
            assertEquals(0, count(reader));

            reader = data.getFeatureReader(new Query(first.typeName), t);
            assertEquals(first.features.length, count(reader));

            reader = data.getFeatureReader(new Query(first.typeName, first.feat1Filter), t);
            assertEquals(1, count(reader));
        }
    }

    void assertCovered(
            SimpleFeature[] features, FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws Exception {
        int count = 0;

        try {
            while (reader.hasNext()) {
                assertTrue(containsFeature(features, reader.next()));
                count++;
            }
        } finally {
            // This is not a good idea, since later tests try and run a reader.hasNext() !!
            // reader.close();
        }

        assertEquals(features.length, count);
    }

    /**
     * Ensure that FeatureReader<SimpleFeatureType, SimpleFeature> reader contains extactly the
     * contents of array.
     */
    boolean covers(SimpleFeatureCollection features, SimpleFeature[] array) throws Exception {

        SimpleFeature feature;
        int count = 0;
        SimpleFeatureIterator i = features.features();
        try {
            while (i.hasNext()) {
                feature = (SimpleFeature) i.next();
                if (!containsFeature(array, feature)) {
                    return false;
                }
                count++;
            }
        } finally {
            i.close();
        }
        return count == array.length;
    }

    /**
     * Ensure that FeatureReader<SimpleFeatureType, SimpleFeature> reader contains extactly the
     * contents of array.
     */
    boolean covers(FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
            throws Exception {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                assertNotNull("feature", feature);
                if (!containsFeature(array, feature)) {
                    fail("feature " + feature.getID() + " not listed");
                    return false;
                }
                count++;
            }
        } finally {
            reader.close();
        }
        assertEquals("covers", count, array.length);
        return true;
    }

    boolean covers(SimpleFeatureIterator reader, SimpleFeature[] array)
            throws NoSuchElementException, IOException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                assertNotNull("feature", feature);
                if (!containsFeature(array, feature)) {
                    fail("feature " + feature.getID() + " not listed");
                    return false;
                }
                count++;
            }
        } finally {
            reader.close();
        }
        assertEquals("covers", count, array.length);
        return true;
    }

    boolean coversLax(FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
            throws Exception {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }
        return count == array.length;
    }

    boolean coversLax(SimpleFeatureIterator reader, SimpleFeature[] array) throws Exception {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }
        return count == array.length;
    }

    void dump(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws Exception {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();
                LOGGER.fine(count + " feature:" + feature);
                count++;
            }
        } finally {
            reader.close();
        }
    }

    void dump(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            LOGGER.fine(i + " feature:" + array[i]);
        }
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, Filter, Transaction)
     */
    @Test
    public void testGetFeatureWriter() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);
        assertEquals(first.features.length, count(writer));

        try {
            writer.hasNext();
            fail("Should not be able to use a closed writer");
        } catch (IOException expected) {
        }

        try {
            writer.next();
            fail("Should not be able to use a closed writer");
        } catch (IOException expected) {
        }
    }

    @Test
    public void testGetFeatureWriterRemove() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals(first.features[0].getID())) {
                writer.remove();
            }
        }

        writer = data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);
        assertEquals(first.features.length - 1, count(writer));
    }

    @Test
    public void testGetFeaturesWriterAdd() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = (SimpleFeature) writer.next();
        }

        assertFalse(writer.hasNext());
        feature = (SimpleFeature) writer.next();
        feature.setAttributes(first.newFeature.getAttributes());
        writer.write();
        assertFalse(writer.hasNext());

        writer = data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);
        assertEquals(first.features.length + 1, count(writer));
    }

    @Test
    public void testGetFeaturesWriterModify() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);

        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals(first.features[0].getID())) {
                feature.setAttribute(first.stringAttribute, "changed");
                writer.write();
            }
        }

        feature = null;

        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                data.getFeatureReader(
                        new Query(first.typeName, first.feat1Filter), Transaction.AUTO_COMMIT);

        if (reader.hasNext()) {
            feature = reader.next();
        }

        assertEquals("changed", feature.getAttribute(first.stringAttribute));
    }

    @Test
    public void testGetFeatureWriterTypeNameTransaction() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter(first.typeName, Transaction.AUTO_COMMIT);
        assertEquals(first.features.length, count(writer));
        writer.close();
    }

    @Test
    public void testGetFeatureWriterAppendTypeNameTransaction() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriterAppend(first.typeName, Transaction.AUTO_COMMIT);
        assertEquals(0, count(writer));
        writer.close();
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, boolean, Transaction)
     */
    @Test
    public void testGetFeatureWriterFilter() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter(first.typeName, Filter.EXCLUDE, Transaction.AUTO_COMMIT);
        assertFalse(writer.hasNext());

        assertEquals(0, count(writer));

        writer = data.getFeatureWriter(first.typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
        assertFalse(writer instanceof FilteringFeatureWriter);
        assertEquals(first.features.length, count(writer));

        writer = data.getFeatureWriter(first.typeName, first.feat1Filter, Transaction.AUTO_COMMIT);

        assertEquals(1, count(writer));
    }

    /** Test two transactions one removing feature, and one adding a feature. */
    @Test
    public void testGetFeatureWriterTransaction() throws Exception {
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 =
                    data.getFeatureWriter(first.typeName, first.feat1Filter, t1);
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 =
                    data.getFeatureWriterAppend(first.typeName, t2);

            FeatureReader<SimpleFeatureType, SimpleFeature> reader;
            SimpleFeature feature;
            SimpleFeature[] ORIGIONAL = first.features;
            SimpleFeature[] REMOVE = new SimpleFeature[ORIGIONAL.length - 1];
            SimpleFeature[] ADD = new SimpleFeature[ORIGIONAL.length + 1];
            SimpleFeature[] FINAL = new SimpleFeature[ORIGIONAL.length];
            int i;
            int index;
            index = 0;

            for (i = 0; i < ORIGIONAL.length; i++) {
                feature = ORIGIONAL[i];

                if (!feature.getID().equals(first.features[0].getID())) {
                    REMOVE[index++] = feature;
                }
            }

            for (i = 0; i < ORIGIONAL.length; i++) {
                ADD[i] = ORIGIONAL[i];
            }

            ADD[i] = first.newFeature;

            for (i = 0; i < REMOVE.length; i++) {
                FINAL[i] = REMOVE[i];
            }

            FINAL[i] = first.newFeature;

            // start of with ORIGINAL
            final Query allRoadsQuery = new Query(first.typeName);
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            assertTrue(covers(reader, ORIGIONAL));

            // writer 1 removes road.rd1 on t1
            // -------------------------------
            // - tests transaction independence from DataStore
            while (writer1.hasNext()) {
                feature = (SimpleFeature) writer1.next();
                assertEquals(first.features[0].getID(), feature.getID());
                writer1.remove();
            }

            // still have ORIGIONAL and t1 has REMOVE
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);

            assertTrue(covers(reader, ORIGIONAL));

            reader = data.getFeatureReader(allRoadsQuery, t1);
            assertTrue(covers(reader, REMOVE));

            // close writer1
            // --------------
            // ensure that modification is left up to transaction commmit
            writer1.close();

            // We still have ORIGIONAL and t1 has REMOVE
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            assertTrue(covers(reader, ORIGIONAL));
            reader = data.getFeatureReader(allRoadsQuery, t1);
            assertTrue(covers(reader, REMOVE));

            // writer 2 adds road.rd4 on t2
            // ----------------------------
            // - tests transaction independence from each other
            feature = (SimpleFeature) writer2.next();
            feature.setAttributes(first.newFeature.getAttributes());
            writer2.write();

            // We still have ORIGIONAL and t2 has ADD
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            assertTrue(covers(reader, ORIGIONAL));
            reader = data.getFeatureReader(allRoadsQuery, t2);
            assertTrue(coversLax(reader, ADD));

            // close writer2
            // -------------
            // ensure that modification is left up to transaction commmit
            writer2.close();

            // Still have ORIGIONAL and t2 has ADD
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            assertTrue(covers(reader, ORIGIONAL));
            reader = data.getFeatureReader(allRoadsQuery, t2);
            assertTrue(coversLax(reader, ADD));

            // commit t1
            // ---------
            // -ensure that delayed writing of transactions takes place
            //
            t1.commit();

            // We now have REMOVE, as does t1 (which has not additional diffs)
            // t2 will have FINAL
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            assertTrue(covers(reader, REMOVE));
            reader = data.getFeatureReader(allRoadsQuery, t1);
            assertTrue(covers(reader, REMOVE));
            reader = data.getFeatureReader(allRoadsQuery, t2);
            assertTrue(coversLax(reader, FINAL));

            // commit t2
            // ---------
            // -ensure that everyone is FINAL at the end of the day
            t2.commit();

            // We now have Number( remove one and add one)
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
            assertTrue(coversLax(reader, FINAL));
            reader = data.getFeatureReader(allRoadsQuery, t1);
            assertTrue(coversLax(reader, FINAL));
            reader = data.getFeatureReader(allRoadsQuery, t2);
            assertTrue(coversLax(reader, FINAL));
        }
    }

    public void testGetFeatureSource(TestDataType test) throws Exception {
        SimpleFeatureSource source = data.getFeatureSource(test.typeName);

        assertSchemaEqual(test.featureType, source.getSchema());

        assertEquals(data, source.getDataStore());
        if (source.getCount(Query.ALL) >= 0) {
            assertEquals(test.numberOfFeatures, source.getCount(Query.ALL));
        }

        assertEquals(test.typeName, source.getSchema().getTypeName());
        assertSame(data, source.getDataStore());

        SimpleFeatureCollection collection = source.getFeatures();
        assertEquals(test.numberOfFeatures, collection.size());
        assertTrue(source.getBounds().covers(test.bounds));
        assertTrue(covers(collection.features(), test.features));

        SimpleFeatureCollection expected = DataUtilities.collection(test.features);
        assertCovers(test.typeName, expected, collection);
        assertTrue(collection.getBounds().covers(test.bounds));

        SimpleFeatureCollection some = source.getFeatures(test.feat12Filter);
        assertEquals(2, some.size());
        assertEquals(test.feat12Bounds, some.getBounds());
        assertEquals(some.getSchema(), source.getSchema());

        Query query =
                new Query(
                        test.typeName,
                        test.feat12Filter,
                        new String[] {
                            test.stringAttribute,
                            test.featureType.getGeometryDescriptor().getName().getLocalPart()
                        });

        SimpleFeatureCollection half = source.getFeatures(query);
        assertEquals(2, half.size());
        assertEquals(2, half.getSchema().getAttributeCount());

        SimpleFeatureIterator reader = half.features();
        SimpleFeatureType type = half.getSchema();
        reader.close();

        SimpleFeatureType actual = half.getSchema();

        assertEquals(type.getTypeName(), actual.getTypeName());
        assertEquals(type.getName().getNamespaceURI(), actual.getName().getNamespaceURI());
        assertEquals(type.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < type.getAttributeCount(); i++) {
            assertEquals(type.getDescriptor(i), actual.getDescriptor(i));
        }

        assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
        assertEquals(type, actual);

        BoundingBox b = half.getBounds();
        assertEquals(test.feat12Bounds, b);
    }

    @Test
    public void testGetFeatureSource() throws Exception {
        testGetFeatureSource(first);
        testGetFeatureSource(second);
    }

    //
    // Feature Store Testing
    //
    @Test
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
        Name name = first.featureType.getDescriptor(first.stringAttribute).getName();
        road.modifyFeatures(name, "changed", first.feat1Filter);

        SimpleFeatureCollection results = road.getFeatures(first.feat1Filter);
        assertEquals("changed", results.features().next().getAttribute(first.stringAttribute));
    }

    @Test
    public void testGetFeatureStoreModifyFeatures2() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
        Name name = first.featureType.getDescriptor(first.stringAttribute).getName();
        road.modifyFeatures(
                new Name[] {name},
                new Object[] {
                    "changed",
                },
                first.feat1Filter);

        SimpleFeatureCollection results = road.getFeatures(first.feat1Filter);
        assertEquals("changed", results.features().next().getAttribute(first.stringAttribute));
    }

    @Test
    public void testGetFeatureStoreRemoveFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(first.typeName);

        road.removeFeatures(first.feat1Filter);
        assertEquals(0, road.getFeatures(first.feat1Filter).size());
        assertEquals(first.features.length - 1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreAddFeatures() throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                DataUtilities.reader(
                        new SimpleFeature[] {
                            first.newFeature,
                        });
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(first.typeName);

        road.addFeatures(DataUtilities.collection(reader));
        assertEquals(first.features.length + 1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreSetFeatures() throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader =
                DataUtilities.reader(
                        new SimpleFeature[] {
                            first.newFeature,
                        });
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(first.typeName);

        assertEquals(3, road.getFeatures().size());
        road.setFeatures(reader);
        assertEquals(
                1,
                count(data.getFeatureReader(new Query(first.typeName), Transaction.AUTO_COMMIT)));
        assertEquals(1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreTransactionSupport() throws Exception {
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {

            SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
            SimpleFeatureStore road1 = (SimpleFeatureStore) data.getFeatureSource(first.typeName);
            SimpleFeatureStore road2 = (SimpleFeatureStore) data.getFeatureSource(first.typeName);

            road1.setTransaction(t1);
            road2.setTransaction(t2);

            SimpleFeature feature;
            SimpleFeature[] ORIGINAL = first.features;
            SimpleFeature[] REMOVE = new SimpleFeature[ORIGINAL.length - 1];
            SimpleFeature[] ADD = new SimpleFeature[ORIGINAL.length + 1];
            SimpleFeature[] FINAL = new SimpleFeature[ORIGINAL.length];
            int i;
            int index;
            index = 0;

            for (i = 0; i < ORIGINAL.length; i++) {
                feature = ORIGINAL[i];

                if (!feature.getID().equals(first.features[0].getID())) {
                    REMOVE[index++] = feature;
                }
            }

            for (i = 0; i < ORIGINAL.length; i++) {
                ADD[i] = ORIGINAL[i];
            }

            ADD[i] = first.newFeature;

            for (i = 0; i < REMOVE.length; i++) {
                FINAL[i] = REMOVE[i];
            }

            FINAL[i] = first.newFeature;

            // start of with ORIGINAL
            assertTrue(covers(road.getFeatures().features(), ORIGINAL));

            // road1 removes road.rd1 on t1
            // -------------------------------
            // - tests transaction independence from DataStore
            road1.removeFeatures(first.feat1Filter);

            // still have ORIGIONAL and t1 has REMOVE
            assertTrue(covers(road.getFeatures().features(), ORIGINAL));
            assertTrue(covers(road1.getFeatures().features(), REMOVE));

            // road2 adds road.rd4 on t2
            // ----------------------------
            // - tests transaction independence from each other
            SimpleFeatureCollection collection =
                    DataUtilities.collection(
                            new SimpleFeature[] {
                                first.newFeature,
                            });
            road2.addFeatures(collection);

            // We still have ORIGIONAL, t1 has REMOVE, and t2 has ADD
            assertTrue(covers(road.getFeatures().features(), ORIGINAL));
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

    //
    // FeatureLocking Testing
    //

    /*
     * Test for void lockFeatures()
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", 3600);
        FeatureLocking<SimpleFeatureType, SimpleFeature> road;
        SimpleFeatureSource source = data.getFeatureSource(first.typeName);
        if (!(source instanceof FeatureLocking)) {
            LOGGER.info("testLockFeature ignored, store does not support locking");
            return;
        }
        road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        road.setFeatureLock(lock);

        assertFalse(isLocked(first.typeName, first.features[0].getID()));
        road.lockFeatures();
        assertTrue(isLocked(first.typeName, first.features[0].getID()));
    }

    @Test
    public void testUnLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", 360000);
        SimpleFeatureSource source = data.getFeatureSource(first.typeName);
        if (!(source instanceof FeatureLocking)) {
            LOGGER.info("testUnLockFeatures ignored, store does not support locking");
            return;
        }
        @SuppressWarnings("unchecked")
        FeatureLocking<SimpleFeatureType, SimpleFeature> road =
                (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        road.setFeatureLock(lock);
        road.lockFeatures();

        try {
            road.unLockFeatures();
            fail("unlock should fail due on AUTO_COMMIT");
        } catch (IOException expected) {
        }

        try (Transaction t = new DefaultTransaction(); ) {
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

    @SuppressWarnings("unchecked")
    @Test
    public void testLockFeatureInteraction() throws IOException {
        FeatureLock lockA = new FeatureLock("LockA", 3600);
        FeatureLock lockB = new FeatureLock("LockB", 3600);
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {
            FeatureLocking<SimpleFeatureType, SimpleFeature> road1;
            FeatureLocking<SimpleFeatureType, SimpleFeature> road2;

            {
                SimpleFeatureSource source = data.getFeatureSource(first.typeName);
                if (!(source instanceof FeatureLocking)) {
                    LOGGER.info(
                            "testLockFeatureInteraction ignored, store does not support locking");
                    return;
                }

                road1 = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;

                source = data.getFeatureSource(first.typeName);
                if (!(source instanceof FeatureLocking)) {
                    LOGGER.info(
                            "testLockFeatureInteraction ignored, store does not support locking");
                    return;
                }
                road2 = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
            }
            road1.setTransaction(t1);
            road2.setTransaction(t2);
            road1.setFeatureLock(lockA);
            road2.setFeatureLock(lockB);

            assertFalse(isLocked(first.typeName, first.features[0].getID()));
            assertFalse(isLocked(first.typeName, first.features[1].getID()));
            assertFalse(isLocked(first.typeName, first.features[2].getID()));

            road1.lockFeatures(first.feat1Filter);
            assertTrue(isLocked(first.typeName, first.features[0].getID()));
            assertFalse(isLocked(first.typeName, first.features[1].getID()));
            assertFalse(isLocked(first.typeName, first.features[2].getID()));

            road2.lockFeatures(first.feat2Filter);
            assertTrue(isLocked(first.typeName, first.features[0].getID()));
            assertTrue(isLocked(first.typeName, first.features[1].getID()));
            assertFalse(isLocked(first.typeName, first.features[2].getID()));

            try {
                road1.unLockFeatures(first.feat1Filter);
                fail("need authorization");
            } catch (IOException expected) {
            }

            t1.addAuthorization(lockA.getAuthorization());

            try {
                road1.unLockFeatures(first.feat2Filter);
                fail("need correct authorization");
            } catch (IOException expected) {
            }

            road1.unLockFeatures(first.feat1Filter);
            assertFalse(isLocked(first.typeName, first.features[0].getID()));
            assertTrue(isLocked(first.typeName, first.features[1].getID()));
            assertFalse(isLocked(first.typeName, first.features[2].getID()));

            t2.addAuthorization(lockB.getAuthorization());
            road2.unLockFeatures(first.feat2Filter);
            assertFalse(isLocked(first.typeName, first.features[0].getID()));
            assertFalse(isLocked(first.typeName, first.features[1].getID()));
            assertFalse(isLocked(first.typeName, first.features[2].getID()));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetFeatureLockingExpire() throws Exception {
        FeatureLock lock = new FeatureLock("Timed", 100);
        FeatureLocking<SimpleFeatureType, SimpleFeature> road;
        {
            SimpleFeatureSource source = data.getFeatureSource(first.typeName);
            if (!(source instanceof FeatureLocking)) {
                LOGGER.info("testLockFeatureInteraction ignored, store does not support locking");
                return;
            }
            road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        }
        road.setFeatureLock(lock);
        String fid = first.features[0].getID();
        assertFalse(isLocked(first.typeName, fid));
        road.lockFeatures(first.feat1Filter);
        assertTrue(fid + " not locked", isLocked(first.typeName, fid));
        Thread.sleep(150);
        assertFalse(isLocked(first.typeName, fid));
    }

    @Test
    public void testException() throws IOException {
        SimpleFeatureStore rivers = (SimpleFeatureStore) data.getFeatureSource("sf_rivers");
        SimpleFeatureCollection coll = rivers.getFeatures();
        try {
            coll.features();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof WFSException);
            assertTrue(e.getMessage().contains("MyErrorMessage"));
            assertTrue(e.getMessage().contains("MyExceptionCode"));

            // Make sure the values from the exception are also available in structured form
            WFSException wfsEx = (WFSException) e.getCause();
            List<ExceptionDetails> exceptionData = wfsEx.getExceptionDetails();
            assertEquals(1, exceptionData.size());
            assertEquals("MyExceptionCode", exceptionData.get(0).getCode());
            assertEquals("typeName", exceptionData.get(0).getLocator());
            assertEquals(2, exceptionData.get(0).getTexts().size());
            assertEquals("MyErrorMessage", exceptionData.get(0).getTexts().get(0));
            assertEquals("AdditionalErrorMessage", exceptionData.get(0).getTexts().get(1));
            return;
        }
        assertTrue(false);
    }

    /**
     * Counts the number of Features returned by the specified reader.
     *
     * <p>This method will close the reader.
     */
    protected static int count(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        if (reader == null) {
            return -1;
        }
        int count = 0;
        try {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } catch (NoSuchElementException e) {
            // bad dog!
            throw new DataSourceException("hasNext() lied to me at:" + count, e);
        } catch (Exception e) {
            throw new DataSourceException("next() could not understand feature at:" + count, e);
        } finally {
            reader.close();
        }
        return count;
    }

    /** Counts the number of Features in the specified writer. This method will close the writer. */
    protected static int count(FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
            throws NoSuchElementException, IOException {
        int count = 0;

        try {
            while (writer.hasNext()) {
                writer.next();
                count++;
            }
        } finally {
            writer.close();
        }

        return count;
    }

    protected static SimpleFeature[] grabArray(SimpleFeatureCollection features) {
        SimpleFeature array[] = new SimpleFeature[features.size()];
        array = (SimpleFeature[]) features.toArray(array);
        assertNotNull(array);

        return array;
    }
}
