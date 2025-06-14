/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.geotools.api.data.FeatureEvent.Type;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.CollectionFeatureReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public abstract class JDBCFeatureStoreOnlineTest extends JDBCTestSupport {
    JDBCFeatureStore featureStore;

    @Override
    protected void connect() throws Exception {
        super.connect();

        featureStore = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
    }

    @Test
    public void testAddFeatures() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());

        FeatureEventWatcher watcher = new FeatureEventWatcher();

        for (int i = 3; i < 6; i++) {
            b.set(aname("intProperty"), Integer.valueOf(i));
            b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
            collection.add(b.buildFeature(null));
        }
        featureStore.addFeatureListener(watcher);
        List<FeatureId> fids = featureStore.addFeatures((SimpleFeatureCollection) collection);
        assertEquals(watcher.bounds, collection.getBounds());

        assertEquals(3, fids.size());

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(6, features.size());

        FilterFactory ff = dataStore.getFilterFactory();

        for (FeatureId identifier : fids) {
            String fid = identifier.getID();
            Id filter = ff.id(Collections.singleton(identifier));

            features = featureStore.getFeatures(filter);
            assertEquals(1, features.size());

            try (SimpleFeatureIterator iterator = features.features()) {
                assertTrue(iterator.hasNext());

                SimpleFeature feature = iterator.next();
                assertEquals(fid, feature.getID());
                assertFalse(iterator.hasNext());
            }
        }
    }

    /**
     * Tests that returned keys are actually allowing the code to get back the same feature inserted (SQLServer code
     * used to rely on a key generation approach that failed this test)
     */
    @Test
    public void testMultithreadedAddFeatures() throws IOException, InterruptedException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());

        // ensure a minimum of concurrency
        final ExecutorService executor = Executors.newFixedThreadPool(8);
        CompletionService<Void> cs = new ExecutorCompletionService<>(executor);
        try {
            final int LOOPS = 32;
            for (int i = 0; i < LOOPS; i++) {
                final String theProperty = aname("intProperty");
                final Integer theValue = Integer.valueOf(i + 3);
                b.set(theProperty, theValue);
                SimpleFeature feature = b.buildFeature(null);
                cs.submit(() -> {
                    List<FeatureId> ids = featureStore.addFeatures(Arrays.asList(feature));
                    Filter filter = dataStore.getFilterFactory().id(Collections.singleton(ids.get(0)));
                    ContentFeatureCollection features = featureStore.getFeatures(filter);
                    assertEquals(1, features.size());
                    SimpleFeature found = DataUtilities.first(features);
                    assertEquals(theValue, found.getAttribute(theProperty));
                    return null;
                });
            }

            // gather the results to make sure there are no exceptions
            for (int i = 0; i < LOOPS; i++) {
                cs.take();
            }

        } finally {
            executor.shutdown();
        }
    }

    @Test
    public void testAddFeaturesUseProvidedFid() throws IOException {
        // check we advertise the ability to reuse feature ids
        assertTrue(featureStore.getQueryCapabilities().isUseProvidedFIDSupported());

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());

        String typeName = b.getFeatureType().getTypeName();
        for (int i = 3; i < 6; i++) {
            b.set(aname("intProperty"), Integer.valueOf(i));
            b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
            b.featureUserData(Hints.USE_PROVIDED_FID, Boolean.TRUE);
            collection.add(b.buildFeature(typeName + "." + i * 10));
        }
        List<FeatureId> fids = featureStore.addFeatures((SimpleFeatureCollection) collection);

        assertEquals(3, fids.size());
        assertTrue(fids.contains(SimpleFeatureBuilder.createDefaultFeatureIdentifier(typeName + ".30")));
        assertTrue(fids.contains(SimpleFeatureBuilder.createDefaultFeatureIdentifier(typeName + ".40")));
        assertTrue(fids.contains(SimpleFeatureBuilder.createDefaultFeatureIdentifier(typeName + ".50")));

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(6, features.size());

        FilterFactory ff = dataStore.getFilterFactory();

        for (FeatureId identifier : fids) {
            Id filter = ff.id(Collections.singleton(identifier));

            features = featureStore.getFeatures(filter);
            assertEquals(1, features.size());
        }
    }

    @Test
    public void testAddInTransaction() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());

        b.set(aname("intProperty"), Integer.valueOf(3));
        b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(3, 3)));
        collection.add(b.buildFeature(null));

        FeatureEventWatcher watcher = new FeatureEventWatcher();
        try (Transaction t = new DefaultTransaction()) {
            featureStore.setTransaction(t);
            featureStore.addFeatureListener(watcher);
            JDBCFeatureStore featureStore2 = (JDBCFeatureStore)
                    dataStore.getFeatureSource(featureStore.getName().getLocalPart());
            List<FeatureId> fids = featureStore.addFeatures((SimpleFeatureCollection) collection);

            assertEquals(1, fids.size());

            // check the store with the transaction sees the new features, but the other store does
            // not
            assertEquals(4, featureStore.getFeatures().size());
            assertEquals(3, featureStore2.getFeatures().size());

            // check that after the commit everybody sees 4
            t.commit();
            assertEquals(4, featureStore.getFeatures().size());
            assertEquals(4, featureStore2.getFeatures().size());
        }
    }

    @Test
    public void testExternalConnection() throws IOException, SQLException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());

        b.set(aname("intProperty"), Integer.valueOf(3));
        b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(3, 3)));
        collection.add(b.buildFeature(null));

        FeatureEventWatcher watcher = new FeatureEventWatcher();

        try (Connection conn = setup.getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            try (Transaction t = dataStore.buildTransaction(conn)) {
                featureStore.setTransaction(t);
                featureStore.addFeatureListener(watcher);
                JDBCFeatureStore featureStore2 = (JDBCFeatureStore)
                        dataStore.getFeatureSource(featureStore.getName().getLocalPart());
                List<FeatureId> fids = featureStore.addFeatures((SimpleFeatureCollection) collection);

                assertEquals(1, fids.size());

                // check the store with the transaction sees the new features, but the other store
                // does not
                assertEquals(4, featureStore.getFeatures().size());
                assertEquals(3, featureStore2.getFeatures().size());

                // check that after the commit on the transaction things have not changed,
                // the connection is externally managed
                t.commit();
                assertEquals(4, featureStore.getFeatures().size());
                assertEquals(3, featureStore2.getFeatures().size());

                // commit directly
                conn.commit();
                assertEquals(4, featureStore.getFeatures().size());
                assertEquals(4, featureStore2.getFeatures().size());

                // check that closing the transaction does not affect the connection
                assertFalse(conn.isClosed());
            }
        }
    }

    /** Check null encoding is working properly */
    @Test
    public void testAddNullAttributes() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        SimpleFeature nullFeature = b.buildFeature("testId");
        featureStore.addFeatures(Arrays.asList(nullFeature));
    }

    /** Check null encoding is working properly */
    @Test
    public void testModifyNullAttributes() throws IOException {
        String[] attributeNames = new String[featureStore.getSchema().getAttributeCount()];
        for (int i = 0; i < attributeNames.length; i++) {
            attributeNames[i] = featureStore.getSchema().getDescriptor(i).getLocalName();
        }
        Object[] nulls = new Object[attributeNames.length];
        featureStore.modifyFeatures(attributeNames, nulls, Filter.INCLUDE);
    }

    @Test
    public void testSetFeatures() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureStore.getSchema());

        for (int i = 3; i < 6; i++) {
            b.set(aname("intProperty"), Integer.valueOf(i));
            b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
            collection.add(b.buildFeature(null));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                new CollectionFeatureReader((SimpleFeatureCollection) collection, collection.getSchema())) {
            featureStore.setFeatures(reader);
        }

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(3, features.size());

        try (SimpleFeatureIterator iterator = features.features()) {
            HashSet<Integer> numbers = new HashSet<>();
            numbers.add(Integer.valueOf(3));
            numbers.add(Integer.valueOf(4));
            numbers.add(Integer.valueOf(5));

            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertTrue(numbers.contains(((Number) feature.getAttribute(aname("intProperty"))).intValue()));
                numbers.remove(feature.getAttribute(aname("intProperty")));
            }
        }
    }

    @Test
    public void testModifyFeatures() throws IOException {
        FeatureEventWatcher watcher = new FeatureEventWatcher();

        featureStore.addFeatureListener(watcher);
        featureStore.modifyFeatures(
                new Name[] {new NameImpl(aname("stringProperty"))}, new Object[] {"foo"}, Filter.INCLUDE);

        assertTrue("check that at least one event was issued", watcher.count > 0);
        assertEquals("Should be an update event", Type.CHANGED, watcher.type);
        assertEquals(Filter.INCLUDE, watcher.filter);

        SimpleFeatureCollection features = featureStore.getFeatures();
        try (SimpleFeatureIterator i = features.features()) {
            assertTrue(i.hasNext());

            while (i.hasNext()) {
                SimpleFeature feature = i.next();
                assertEquals("foo", feature.getAttribute(aname("stringProperty")));
            }
        }
    }

    @Test
    public void testModifyGeometry() throws IOException {
        // GEOT-2371
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(-10, 0));
        featureStore.modifyFeatures(new Name[] {new NameImpl(aname("geometry"))}, new Object[] {point}, Filter.INCLUDE);

        SimpleFeatureCollection features = featureStore.getFeatures();
        try (SimpleFeatureIterator i = features.features()) {
            assertTrue(i.hasNext());

            while (i.hasNext()) {
                SimpleFeature feature = i.next();
                assertTrue(point.equalsExact((Geometry) feature.getAttribute(aname("geometry"))));
            }
        }
    }

    @Test
    public void testModifyMadeUpGeometry() throws IOException {
        // GEOT-2371
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(-10, 0));

        // make up a fake attribute with the same name, something that might happen
        // in chains of retyping where attributes are rebuilt
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.binding(Point.class);

        featureStore.modifyFeatures(new NameImpl(aname("geometry")), point, Filter.INCLUDE);

        SimpleFeatureCollection features = featureStore.getFeatures();
        try (SimpleFeatureIterator i = features.features()) {
            assertTrue(i.hasNext());

            while (i.hasNext()) {
                SimpleFeature feature = i.next();
                assertTrue(point.equalsExact((Geometry) feature.getAttribute(aname("geometry"))));
            }
        }
    }

    @Test
    public void testModifyFeaturesSingleAttribute() throws IOException {
        featureStore.modifyFeatures(new NameImpl(aname("stringProperty")), "foo", Filter.INCLUDE);

        SimpleFeatureCollection features = featureStore.getFeatures();
        try (SimpleFeatureIterator i = features.features()) {
            assertTrue(i.hasNext());

            while (i.hasNext()) {
                SimpleFeature feature = i.next();
                assertEquals("foo", feature.getAttribute(aname("stringProperty")));
            }
        }
    }

    @Test(expected = Exception.class)
    public void testModifyFeaturesInvalidFilter() throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));
        featureStore.modifyFeatures(new NameImpl(aname("stringProperty")), "foo", f);
    }

    @Test
    public void testRemoveFeatures() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Filter filter = ff.equals(ff.property(aname("intProperty")), ff.literal(1));

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(3, features.size());

        featureStore.removeFeatures(filter);
        assertEquals(2, features.size());

        featureStore.removeFeatures(Filter.INCLUDE);
        assertEquals(0, features.size());
    }

    @Test(expected = Exception.class)
    public void testRemoveFeaturesWithInvalidFilter() throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));
        featureStore.removeFeatures(f);
    }
}
