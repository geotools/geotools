/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataTestCase;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * An abstract super class for testing datastore implementations. All datastore implementations
 * should has a test case that extends this class.
 * 
 * @author Jesse Eichar, Refractions Research
 * 
 * 
 * @source $URL$
 */
public abstract class AbstractDataStoreTest extends DataTestCase {

    /** The logger for the filter module. */
    private final Logger LOGGER = Logging.getLogger(getClass());

    DataStore data;

    public AbstractDataStoreTest(String name) {
        super(name);
    }

    /**
     * Creates and populates a new instance of the datastore. The datastore must not have a roads or
     * rivers type.
     * <p>
     * Something like the following should suffice:
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
     * 
     * @throws Exception
     */
    public abstract DataStore createDataStore() throws Exception;

    /**
     * This method must remove the roads and rivers types from the datastore. It must also close all
     * connections to the datastore if it has connections and get rid of any temporary files.
     * 
     * @throws Exception
     */
    public abstract DataStore tearDownDataStore(DataStore data) throws Exception;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void dataSetUp() throws Exception {
        super.dataSetUp();
        // no UUID type in wfs
        roadType = DataUtilities.createSubType(roadType, new String[] { "id", "geom", "name" });
        newRoad = SimpleFeatureBuilder.retype(newRoad, roadType);

        boolean longitudeFirst = true;
        CoordinateReferenceSystem wgs84LonLat = CRS.decode("EPSG:4326", longitudeFirst);

        roadType = DataUtilities.createSubType(roadType, null, wgs84LonLat);
        riverType = DataUtilities.createSubType(riverType, null, wgs84LonLat);

        try {
            data = createDataStore();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "exception while making schema", e);
            throw e;
        }

        SimpleFeatureSource rivers = data.getFeatureSource(getRiverTypeName());
        SimpleFeatureSource roads = data.getFeatureSource(getRoadTypeName());

        // Now that we have seeded the contents we need to
        // set up our arrays in the same order
        //
        roadFeatures = grabArray(roads.getFeatures(), roadFeatures.length);
        riverFeatures = grabArray(rivers.getFeatures(), riverFeatures.length);

        rd1Filter = ff.id(ff.featureId(roadFeatures[0].getID()));
        rd2Filter = ff.id(ff.featureId(roadFeatures[1].getID()));

        rd12Filter = ff.id(ff.featureId(roadFeatures[0].getID()),
                ff.featureId(roadFeatures[1].getID()));

        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        riverBounds = new ReferencedEnvelope(riverBounds, crs);
        roadBounds = new ReferencedEnvelope(roadBounds, crs);
    }

    /**
     * @return the nameAttribute
     */
    protected abstract String getNameAttribute();

    /**
     * @return the roadTypeName
     */
    protected abstract String getRoadTypeName();

    /**
     * @return the riverTypeName
     */
    protected abstract String getRiverTypeName();

    SimpleFeature[] grabArray(SimpleFeatureCollection features, int size) {
        try {
            SimpleFeature array[] = new SimpleFeature[size];
            array = (SimpleFeature[]) features.toArray(array);
            assertNotNull(array);

            return array;
        } finally {
            features.purge();
        }
    }

    protected void tearDown() throws Exception {
        tearDownDataStore(data);
        data = null;
        super.tearDown();
    }

    @Test
    public void testFeatureEvents() throws Exception {
        SimpleFeatureStore store1 = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        SimpleFeatureStore store2 = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        store1.setTransaction(new DefaultTransaction());
        class Listener implements FeatureListener {
            String name;

            List<FeatureEvent> events = new ArrayList<FeatureEvent>();

            public Listener(String name) {
                this.name = name;
            }

            public void changed(FeatureEvent featureEvent) {
                this.events.add(featureEvent);
            }

            FeatureEvent getEvent(int i) {
                return (FeatureEvent) events.get(i);
            }
        }

        Listener listener1 = new Listener("one");
        Listener listener2 = new Listener("two");

        store1.addFeatureListener(listener1);
        store2.addFeatureListener(listener2);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        // test that only the listener listening with the current transaction gets the event.
        final SimpleFeature feature = roadFeatures[0];
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

    @Test
    public void testFixture() throws Exception {
        SimpleFeatureType type = DataUtilities.createType("namespace.typename",
                "name:String,id:0,geom:MultiLineString");
        assertEquals("namespace", "namespace", type.getName().getNamespaceURI());
        assertEquals("typename", "typename", type.getTypeName());
        assertEquals("attributes", 3, type.getAttributeCount());

        AttributeDescriptor[] a = type.getAttributeDescriptors().toArray(
                new AttributeDescriptor[type.getAttributeCount()]);
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
            assertEquals(2, names.length);
            assertTrue(contains(names, getRoadTypeName()));
            assertTrue(contains(names, getRiverTypeName()));
        } catch (IOException e) {
            e.printStackTrace();
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
     * @param fc
     *            the feature collection we're going to search
     * @param f
     *            the feature we're looking for
     * 
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
     * @param array
     *            must be an array of features
     * @param expected
     *            the expected feature we're looking for
     * 
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
     * @param feature1
     *            the Feature to test against.
     * @param feature2
     *            the Feature to test for equality.
     * 
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

        // if (!featureId.equals(feat.getID())) {
        // return false;
        // }
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

    /**
     * Like contain but based on match rather than equals
     */
    boolean containsLax(SimpleFeature[] array, SimpleFeature expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < array.length; i++) {
            if (match(array[i], expected)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compare based on attributes not getID allows comparison of Diff contents
     */
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
        SimpleFeatureType road = data.getSchema(getRoadTypeName());
        SimpleFeatureType river = data.getSchema(getRiverTypeName());

        assertNotNull(road);
        assertNotNull(river);

        assertEquals(getRoadTypeName(), road.getTypeName());
        assertEquals(getRiverTypeName(), river.getTypeName());

        assertEquals(roadType.getAttributeCount(), road.getAttributeCount());
        assertEquals(riverType.getAttributeCount(), river.getAttributeCount());

        for (int i = 0; i < roadType.getAttributeCount(); i++) {
            AttributeDescriptor expected = roadType.getDescriptor(i);
            AttributeDescriptor actual = road.getDescriptor(i);
            assertEquals(expected, actual);
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

        for (SimpleFeatureIterator i = c1.features(); i.hasNext();) {
            f = i.next();
            assertTrue(msg + " " + f.getID(), containsFeatureCollection(c2, f)); // c2.contains(f));
        }
    }

    @Test
    public void testGetFeatureReader() throws Exception {
        Query query = new Query(getRoadTypeName());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertCovered(roadFeatures, reader);
        assertEquals(false, reader.hasNext());
    }

    @Test
    public void testGetFeatureReaderMutability() throws IOException {
        Query query = new Query(getRoadTypeName());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader(query,
                Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (reader.hasNext()) {
            feature = (SimpleFeature) reader.next();
            feature.setAttribute(getNameAttribute(), null);
        }

        reader.close();

        reader = data.getFeatureReader(query, Transaction.AUTO_COMMIT);

        while (reader.hasNext()) {
            feature = (SimpleFeature) reader.next();
            assertNotNull(feature.getAttribute(getNameAttribute()));
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
        Query query = new Query(getRoadTypeName());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader1 = data.getFeatureReader(query,
                Transaction.AUTO_COMMIT);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader2 = data.getFeatureReader(query,
                Transaction.AUTO_COMMIT);
        query = new Query(getRiverTypeName());

        FeatureReader<SimpleFeatureType, SimpleFeature> reader3 = data.getFeatureReader(query,
                Transaction.AUTO_COMMIT);

        while (reader1.hasNext() || reader2.hasNext() || reader3.hasNext()) {
            assertTrue(containsFeature(roadFeatures, reader1.next()));
            assertTrue(containsFeature(roadFeatures, reader2.next()));

            if (reader3.hasNext()) {
                assertTrue(containsFeature(riverFeatures, reader3.next()));
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
        SimpleFeatureType type = data.getSchema(getRoadTypeName());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        Query query = new Query(getRoadTypeName());
        reader = data.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertFalse(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName(), Filter.EXCLUDE),
                Transaction.AUTO_COMMIT);

        // TODO: This assert sucks since it EXPECTS an emptyFeatureWriter...well, we got A writer...
        // and it was empty; that's good enough dammit! Even more, the very next assert fails since
        // an emptyFeatureReader looks to not contain the featureType anyways!
        // asserting that the count is 0 just below accomplishes the same thing!
        // assertTrue(reader instanceof EmptyFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName(), rd1Filter),
                Transaction.AUTO_COMMIT);

        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));
    }

    @Test
    public void testGetFeatureReaderFilterTransaction() throws Exception {
        Transaction t = new DefaultTransaction();
        SimpleFeatureType type = data.getSchema(getRoadTypeName());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = data.getFeatureReader(new Query(getRoadTypeName(), Filter.EXCLUDE), t);

        // TODO: remove this silly check!
        // assertTrue(reader instanceof EmptyFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName()), t);
        assertTrue(reader instanceof DiffFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName(), rd1Filter), t);

        // assertTrue(reader instanceof DiffFeatureReader);//Currently wrapped by a filtering
        // feature reader
        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));

        SimpleFeatureStore store = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        store.setTransaction(t);
        store.removeFeatures(rd1Filter);

        reader = data.getFeatureReader(new Query(getRoadTypeName(), Filter.EXCLUDE), t);
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName()), t);
        assertEquals(roadFeatures.length - 1, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName(), rd1Filter), t);
        assertEquals(0, count(reader));

        t.rollback();
        reader = data.getFeatureReader(new Query(getRoadTypeName(), Filter.EXCLUDE), t);
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName()), t);
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new Query(getRoadTypeName(), rd1Filter), t);
        assertEquals(1, count(reader));
    }

    void assertCovered(SimpleFeature[] features,
            FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws Exception {
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
     * 
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
                System.out.println(count + " feature:" + feature);
                count++;
            }
        } finally {
            reader.close();
        }
    }

    void dump(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(i + " feature:" + array[i]);
        }
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, Filter, Transaction)
     */
    @Test
    public void testGetFeatureWriter() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter(
                getRoadTypeName(), Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length, count(writer));

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
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter(
                getRoadTypeName(), Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                writer.remove();
            }
        }

        writer = data.getFeatureWriter(getRoadTypeName(), Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length - 1, count(writer));
    }

    @Test
    public void testGetFeaturesWriterAdd() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter(
                getRoadTypeName(), Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = (SimpleFeature) writer.next();
        }

        assertFalse(writer.hasNext());
        feature = (SimpleFeature) writer.next();
        feature.setAttributes(newRoad.getAttributes());
        writer.write();
        assertFalse(writer.hasNext());

        writer = data.getFeatureWriter(getRoadTypeName(), Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length + 1, count(writer));
    }

    @Test
    public void testGetFeaturesWriterModify() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = data.getFeatureWriter(getRoadTypeName(), Transaction.AUTO_COMMIT);

        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                feature.setAttribute(getNameAttribute(), "changed");
                writer.write();
            }
        }

        feature = null;

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader(new Query(
                getRoadTypeName(), rd1Filter), Transaction.AUTO_COMMIT);

        if (reader.hasNext()) {
            feature = reader.next();
        }

        // feature = (SimpleFeature) data.features("road").get("road.rd1");
        assertEquals("changed", feature.getAttribute(getNameAttribute()));
    }

    @Test
    public void testGetFeatureWriterTypeNameTransaction() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter(getRoadTypeName(), Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length, count(writer));
        writer.close();
    }

    @Test
    public void testGetFeatureWriterAppendTypeNameTransaction() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriterAppend(getRoadTypeName(), Transaction.AUTO_COMMIT);
        assertEquals(0, count(writer));
        writer.close();
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, boolean, Transaction)
     */
    @Test
    public void testGetFeatureWriterFilter() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter(getRoadTypeName(), Filter.EXCLUDE, Transaction.AUTO_COMMIT);
        assertFalse(writer.hasNext());

        assertEquals(0, count(writer));

        writer = data.getFeatureWriter(getRoadTypeName(), Filter.INCLUDE, Transaction.AUTO_COMMIT);
        assertFalse(writer instanceof FilteringFeatureWriter);
        assertEquals(roadFeatures.length, count(writer));

        writer = data.getFeatureWriter(getRoadTypeName(), rd1Filter, Transaction.AUTO_COMMIT);

        assertEquals(1, count(writer));
    }

    /**
     * Test two transactions one removing feature, and one adding a feature.
     */
    @Test
    public void testGetFeatureWriterTransaction() throws Exception {
        Transaction t1 = new DefaultTransaction();
        Transaction t2 = new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 = data.getFeatureWriter(
                getRoadTypeName(), rd1Filter, t1);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 = data.getFeatureWriterAppend(
                getRoadTypeName(), t2);

        SimpleFeatureType road = data.getSchema(getRoadTypeName());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        SimpleFeature feature;
        SimpleFeature[] ORIGIONAL = roadFeatures;
        SimpleFeature[] REMOVE = new SimpleFeature[ORIGIONAL.length - 1];
        SimpleFeature[] ADD = new SimpleFeature[ORIGIONAL.length + 1];
        SimpleFeature[] FINAL = new SimpleFeature[ORIGIONAL.length];
        int i;
        int index;
        index = 0;

        for (i = 0; i < ORIGIONAL.length; i++) {
            feature = ORIGIONAL[i];

            if (!feature.getID().equals(roadFeatures[0].getID())) {
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
        final Query allRoadsQuery = new Query(getRoadTypeName());
        reader = data.getFeatureReader(allRoadsQuery, Transaction.AUTO_COMMIT);
        assertTrue(covers(reader, ORIGIONAL));

        // writer 1 removes road.rd1 on t1
        // -------------------------------
        // - tests transaction independence from DataStore
        while (writer1.hasNext()) {
            feature = (SimpleFeature) writer1.next();
            assertEquals(roadFeatures[0].getID(), feature.getID());
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
        feature.setAttributes(newRoad.getAttributes());
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

    // FIXME: This test will fail for all DB datastores because of the getBounds issue...
    // should revisit later.
    //
    // Feature Source Testing
    public void atestGetFeatureSourceRoad() throws IOException {
        SimpleFeatureSource road = data.getFeatureSource(getRoadTypeName());

        assertEquals(roadType, road.getSchema());
        assertEquals(data, road.getDataStore());
        assertEquals(3, road.getCount(Query.ALL));

        // TODO: which way to get the bounds?
        assertEquals(new Envelope(1, 5, 0, 4), road.getFeatures(Query.ALL).getBounds()); // road.getBounds(Query.ALL));

        SimpleFeatureCollection all = road.getFeatures();
        assertEquals(3, all.size());
        assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection expected = DataUtilities.collection(roadFeatures);

        assertCovers("ALL", expected, all);
        assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection some = road.getFeatures(rd12Filter);
        assertEquals(2, some.size());
        assertEquals(rd12Bounds, some.getBounds());
        assertEquals(some.getSchema(), road.getSchema());

        // TODO: what should be done here? change the query to get GEOM or not?
        // We need to fetch "GEOM" since we'll need to create the bounds based
        // on the geometry later on (getting the geom from the DB is expensive and
        // so far unsupported
        Query query = new Query(getRoadTypeName(), rd12Filter, new String[] { getNameAttribute(), });

        SimpleFeatureCollection half = road.getFeatures(query);
        assertEquals(2, half.size());
        assertEquals(1, half.getSchema().getAttributeCount());

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

        assertNull(type.getGeometryDescriptor());
        assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
        assertEquals(type, actual);

        BoundingBox b = half.getBounds();
        assertEquals(new ReferencedEnvelope(1, 5, 0, 4, null), b);
    }

    @Test
    public void testGetFeatureSourceRiver() throws Exception {
        SimpleFeatureSource river = data.getFeatureSource(getRiverTypeName());

        assertEquals(getRiverTypeName(), river.getSchema().getTypeName());
        assertSame(data, river.getDataStore());

        SimpleFeatureCollection all = river.getFeatures();
        assertEquals(2, all.size());
        ReferencedEnvelope bounds = all.getBounds();
        assertEquals(riverBounds, bounds);
        assertTrue("RIVERS", covers(all.features(), riverFeatures));

        SimpleFeatureCollection expected = DataUtilities.collection(riverFeatures);
        assertCovers("ALL", expected, all);
        assertEquals(riverBounds, all.getBounds());
    }

    //
    // Feature Store Testing
    //
    @Test
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        Name name = roadType.getDescriptor(getNameAttribute()).getName();
        road.modifyFeatures(name, "changed", rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        assertEquals("changed", results.features().next().getAttribute(getNameAttribute()));
    }

    @Test
    public void testGetFeatureStoreModifyFeatures2() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        Name name = roadType.getDescriptor(getNameAttribute()).getName();
        road.modifyFeatures(new Name[] { name }, new Object[] { "changed", }, rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        assertEquals("changed", results.features().next().getAttribute(getNameAttribute()));
    }

    @Test
    public void testGetFeatureStoreRemoveFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());

        road.removeFeatures(rd1Filter);
        assertEquals(0, road.getFeatures(rd1Filter).size());
        assertEquals(roadFeatures.length - 1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreAddFeatures() throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities
                .reader(new SimpleFeature[] { newRoad, });
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());

        road.addFeatures(DataUtilities.collection(reader));
        assertEquals(roadFeatures.length + 1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreSetFeatures() throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = DataUtilities.reader(new SimpleFeature[] { newRoad, });
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());

        assertEquals(3, road.getFeatures().size());
        road.setFeatures(reader);
        assertEquals(1,
                count(data.getFeatureReader(new Query(getRoadTypeName()), Transaction.AUTO_COMMIT)));
        assertEquals(1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreTransactionSupport() throws Exception {
        Transaction t1 = new DefaultTransaction();
        Transaction t2 = new DefaultTransaction();

        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        SimpleFeatureStore road1 = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());
        SimpleFeatureStore road2 = (SimpleFeatureStore) data.getFeatureSource(getRoadTypeName());

        road1.setTransaction(t1);
        road2.setTransaction(t2);

        SimpleFeature feature;
        SimpleFeature[] ORIGINAL = roadFeatures;
        SimpleFeature[] REMOVE = new SimpleFeature[ORIGINAL.length - 1];
        SimpleFeature[] ADD = new SimpleFeature[ORIGINAL.length + 1];
        SimpleFeature[] FINAL = new SimpleFeature[ORIGINAL.length];
        int i;
        int index;
        index = 0;

        for (i = 0; i < ORIGINAL.length; i++) {
            feature = ORIGINAL[i];

            if (!feature.getID().equals(roadFeatures[0].getID())) {
                REMOVE[index++] = feature;
            }
        }

        for (i = 0; i < ORIGINAL.length; i++) {
            ADD[i] = ORIGINAL[i];
        }

        ADD[i] = newRoad;

        for (i = 0; i < REMOVE.length; i++) {
            FINAL[i] = REMOVE[i];
        }

        FINAL[i] = newRoad;

        // start of with ORIGINAL
        assertTrue(covers(road.getFeatures().features(), ORIGINAL));

        // road1 removes road.rd1 on t1
        // -------------------------------
        // - tests transaction independence from DataStore
        road1.removeFeatures(rd1Filter);

        // still have ORIGIONAL and t1 has REMOVE
        assertTrue(covers(road.getFeatures().features(), ORIGINAL));
        assertTrue(covers(road1.getFeatures().features(), REMOVE));

        // road2 adds road.rd4 on t2
        // ----------------------------
        // - tests transaction independence from each other
        SimpleFeatureCollection collection = DataUtilities
                .collection(new SimpleFeature[] { newRoad, });
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
        SimpleFeatureSource source = data.getFeatureSource(getRoadTypeName());
        if (!(source instanceof FeatureLocking)) {
            LOGGER.info("testLockFeature ignored, store does not support locking");
            return;
        }
        road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        road.setFeatureLock(lock);

        assertFalse(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
        road.lockFeatures();
        assertTrue(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
    }

    @Test
    public void testUnLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", 360000);
        SimpleFeatureSource source = data.getFeatureSource(getRoadTypeName());
        if (!(source instanceof FeatureLocking)) {
            LOGGER.info("testUnLockFeatures ignored, store does not support locking");
            return;
        }
        @SuppressWarnings("unchecked")
        FeatureLocking<SimpleFeatureType, SimpleFeature> road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        road.setFeatureLock(lock);
        road.lockFeatures();

        try {
            road.unLockFeatures();
            fail("unlock should fail due on AUTO_COMMIT");
        } catch (IOException expected) {
        }

        Transaction t = new DefaultTransaction();
        road.setTransaction(t);

        try {
            road.unLockFeatures();
            fail("unlock should fail due lack of authorization");
        } catch (IOException expected) {
        }

        t.addAuthorization(lock.getAuthorization());
        road.unLockFeatures();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLockFeatureInteraction() throws IOException {
        FeatureLock lockA = new FeatureLock("LockA", 3600);
        FeatureLock lockB = new FeatureLock("LockB", 3600);
        Transaction t1 = new DefaultTransaction();
        Transaction t2 = new DefaultTransaction();
        FeatureLocking<SimpleFeatureType, SimpleFeature> road1;
        FeatureLocking<SimpleFeatureType, SimpleFeature> road2;

        {
            SimpleFeatureSource source = data.getFeatureSource(getRoadTypeName());
            if (!(source instanceof FeatureLocking)) {
                LOGGER.info("testLockFeatureInteraction ignored, store does not support locking");
                return;
            }

            road1 = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;

            source = data.getFeatureSource(getRoadTypeName());
            if (!(source instanceof FeatureLocking)) {
                LOGGER.info("testLockFeatureInteraction ignored, store does not support locking");
                return;
            }
            road2 = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        }
        road1.setTransaction(t1);
        road2.setTransaction(t2);
        road1.setFeatureLock(lockA);
        road2.setFeatureLock(lockB);

        assertFalse(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[1].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[2].getID()));

        road1.lockFeatures(rd1Filter);
        assertTrue(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[1].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[2].getID()));

        road2.lockFeatures(rd2Filter);
        assertTrue(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
        assertTrue(isLocked(getRoadTypeName(), roadFeatures[1].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[2].getID()));

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
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
        assertTrue(isLocked(getRoadTypeName(), roadFeatures[1].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[2].getID()));

        t2.addAuthorization(lockB.getAuthorization());
        road2.unLockFeatures(rd2Filter);
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[0].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[1].getID()));
        assertFalse(isLocked(getRoadTypeName(), roadFeatures[2].getID()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetFeatureLockingExpire() throws Exception {
        FeatureLock lock = new FeatureLock("Timed", 1);
        FeatureLocking<SimpleFeatureType, SimpleFeature> road;
        {
            SimpleFeatureSource source = data.getFeatureSource(getRoadTypeName());
            if (!(source instanceof FeatureLocking)) {
                LOGGER.info("testLockFeatureInteraction ignored, store does not support locking");
                return;
            }
            road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) source;
        }
        road.setFeatureLock(lock);
        String fid = roadFeatures[0].getID();
        assertFalse(isLocked(getRoadTypeName(), fid));
        road.lockFeatures(rd1Filter);
        assertTrue(fid + " not locked", isLocked(getRoadTypeName(), fid));
        Thread.sleep(50);
        assertFalse(isLocked(getRoadTypeName(), fid));
    }

    @Test
    public void testCreateSchema() throws Exception {
        String typename = "NewType";
        SimpleFeatureType t = DataUtilities.createType(typename, "*geom:Geometry");
        data.createSchema(t);

        String[] names = data.getTypeNames();
        boolean foundNewType = false;

        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(typename)) {
                foundNewType = true;
            }
        }

        assertTrue(foundNewType);
    }
}
