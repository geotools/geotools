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

import java.io.IOException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.CollectionFeatureReader;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.FeatureEvent.Type;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public abstract class JDBCFeatureStoreTest extends JDBCTestSupport {
    JDBCFeatureStore featureStore;

    protected void connect() throws Exception {
        super.connect();

        featureStore = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
    }

    public void testAddFeatures() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null,
                featureStore.getSchema());
        
        FeatureEventWatcher watcher = new FeatureEventWatcher();
        
        for (int i = 3; i < 6; i++) {
            b.set(aname("intProperty"), new Integer(i));
            b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
            collection.add(b.buildFeature(null));
        }
        featureStore.addFeatureListener( watcher );
        List<FeatureId> fids = featureStore.addFeatures(collection);
        assertEquals( watcher.bounds, collection.getBounds() );
        
        assertEquals(3, fids.size());

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(6, features.size());

        FilterFactory ff = dataStore.getFilterFactory();

        for (Iterator f = fids.iterator(); f.hasNext();) {
            FeatureId identifier = (FeatureId) f.next();
            String fid = identifier.getID();
            Id filter = ff.id(Collections.singleton(identifier));

            features = featureStore.getFeatures(filter);
            assertEquals(1, features.size());

            Iterator iterator = features.iterator();
            assertTrue(iterator.hasNext());

            SimpleFeature feature = (SimpleFeature) iterator.next();
            assertEquals(fid, feature.getID());
            assertFalse(iterator.hasNext());

            features.close(iterator);
        }
    }
    
    public void testAddFeaturesUseProvidedFid() throws IOException {
        // check we advertise the ability to reuse feature ids
        assertTrue(featureStore.getQueryCapabilities().isUseProvidedFIDSupported());
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null,
                featureStore.getSchema());
        
        String typeName = b.getFeatureType().getTypeName();
        for (int i = 3; i < 6; i++) {
            b.set(aname("intProperty"), new Integer(i));
            b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
            b.featureUserData(Hints.USE_PROVIDED_FID, Boolean.TRUE);
            collection.add(b.buildFeature(typeName + "." + (i * 10)));
        }
        List<FeatureId> fids = featureStore.addFeatures(collection);
        
        assertEquals(3, fids.size());
        assertTrue(fids.contains(SimpleFeatureBuilder.createDefaultFeatureIdentifier(typeName + ".30")));
        assertTrue(fids.contains(SimpleFeatureBuilder.createDefaultFeatureIdentifier(typeName + ".40")));
        assertTrue(fids.contains(SimpleFeatureBuilder.createDefaultFeatureIdentifier(typeName + ".50")));

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(6, features.size());

        FilterFactory ff = dataStore.getFilterFactory();

        for (Iterator f = fids.iterator(); f.hasNext();) {
            FeatureId identifier = (FeatureId) f.next();
            String fid = identifier.getID();
            Id filter = ff.id(Collections.singleton(identifier));

            features = featureStore.getFeatures(filter);
            assertEquals(1, features.size());
        }
    }
    
    public void testAddInTransaction() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null,
                featureStore.getSchema());
        
        b.set(aname("intProperty"), new Integer(3));
        b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(3, 3)));
        collection.add(b.buildFeature(null));

        FeatureEventWatcher watcher = new FeatureEventWatcher();
        Transaction t = new DefaultTransaction();
        featureStore.setTransaction(t);
        featureStore.addFeatureListener(watcher);
        JDBCFeatureStore featureStore2 = (JDBCFeatureStore) dataStore.getFeatureSource(featureStore.getName().getLocalPart()); 
        List<FeatureId> fids = featureStore.addFeatures(collection);
        
        assertEquals(1, fids.size());

        // check the store with the transaction sees the new features, but the other store does not
        assertEquals(4, featureStore.getFeatures().size());
        assertEquals(3, featureStore2.getFeatures().size());
        
        // check that after the commit everybody sees 4
        t.commit();
        assertEquals(4, featureStore.getFeatures().size());
        assertEquals(4, featureStore2.getFeatures().size());
        
        t.close();
    }
    
    public void testExternalConnection() throws IOException, SQLException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null,
                featureStore.getSchema());
        
        b.set(aname("intProperty"), new Integer(3));
        b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(3, 3)));
        collection.add(b.buildFeature(null));

        FeatureEventWatcher watcher = new FeatureEventWatcher();
        
        Connection conn = setup.getDataSource().getConnection();
        conn.setAutoCommit(false);
        Transaction t = dataStore.buildTransaction(conn);
        featureStore.setTransaction(t);
        featureStore.addFeatureListener(watcher);
        JDBCFeatureStore featureStore2 = (JDBCFeatureStore) dataStore.getFeatureSource(featureStore.getName().getLocalPart()); 
        List<FeatureId> fids = featureStore.addFeatures(collection);
        
        assertEquals(1, fids.size());

        // check the store with the transaction sees the new features, but the other store does not
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
        t.close();
        assertFalse(conn.isClosed());
        conn.close();
    }
    
    /**
     * Check null encoding is working properly
     * @throws IOException
     */
    public void testAddNullAttributes() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        SimpleFeature nullFeature = b.buildFeature("testId");
        featureStore.addFeatures(Arrays.asList(nullFeature));
    }
    
    /**
     * Check null encoding is working properly
     * @throws IOException
     */
    public void testModifyNullAttributes() throws IOException {
        String[] attributeNames = new String[featureStore.getSchema().getAttributeCount()];
        for(int i = 0; i < attributeNames.length; i++) {
            attributeNames[i] = featureStore.getSchema().getDescriptor(i).getLocalName();
        }
        Object[] nulls = new Object[attributeNames.length];
        featureStore.modifyFeatures(attributeNames, nulls, Filter.INCLUDE);
    }

    public void testSetFeatures() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null,
                featureStore.getSchema());

        for (int i = 3; i < 6; i++) {
            b.set(aname("intProperty"), new Integer(i));
            b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
            collection.add(b.buildFeature(null));
        }

         FeatureReader<SimpleFeatureType, SimpleFeature> reader = new CollectionFeatureReader(collection, collection.getSchema());
        featureStore.setFeatures(reader);

        SimpleFeatureCollection features = featureStore.getFeatures();
        assertEquals(3, features.size());

        Iterator iterator = features.iterator();
        HashSet numbers = new HashSet();
        numbers.add(new Integer(3));
        numbers.add(new Integer(4));
        numbers.add(new Integer(5));

        for (int i = 3; iterator.hasNext(); i++) {
            SimpleFeature feature = (SimpleFeature) iterator.next();
            assertTrue(numbers.contains(((Number)feature.getAttribute(aname("intProperty"))).intValue()));
            numbers.remove(feature.getAttribute(aname("intProperty")));
        }

        features.close(iterator);
    }

    public void testModifyFeatures() throws IOException {
        FeatureEventWatcher watcher = new FeatureEventWatcher();
        SimpleFeatureType t = featureStore.getSchema();
        
        featureStore.addFeatureListener( watcher );
        featureStore.modifyFeatures(new AttributeDescriptor[] { t.getDescriptor(aname("stringProperty")) },
            new Object[] { "foo" }, Filter.INCLUDE);
        
        assertTrue( "check that at least one event was issued", watcher.count > 0 );
        assertEquals( "Should be an update event", Type.CHANGED, watcher.type );
        assertEquals( Filter.INCLUDE, watcher.filter );
        
        SimpleFeatureCollection features = featureStore.getFeatures();
        Iterator<SimpleFeature> i = features.iterator();

        assertTrue(i.hasNext());

        while (i.hasNext()) {
            SimpleFeature feature = (SimpleFeature) i.next();
            assertEquals("foo", feature.getAttribute(aname("stringProperty")));
        }
        features.close(i);
    }
    
    public void testModifyGeometry() throws IOException {
        // GEOT-2371
        SimpleFeatureType t = featureStore.getSchema();
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(-10, 0));
		featureStore.modifyFeatures(new AttributeDescriptor[] { t.getDescriptor(aname("geometry")) },
            new Object[] { point }, Filter.INCLUDE);

        SimpleFeatureCollection features = featureStore.getFeatures();
        Iterator i = features.iterator();

        assertTrue(i.hasNext());

        while (i.hasNext()) {
            SimpleFeature feature = (SimpleFeature) i.next();
            assertTrue(point.equalsExact((Geometry) feature.getAttribute(aname("geometry"))));
        }

        features.close(i);
    }
    
    public void testModifyMadeUpGeometry() throws IOException {
        // GEOT-2371
        SimpleFeatureType t = featureStore.getSchema();
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(-10, 0));
        
        // make up a fake attribute with the same name, something that might happen
        // in chains of retyping where attributes are rebuilt
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.binding(Point.class);
        AttributeDescriptor madeUp = ab.buildDescriptor(aname("geometry"));
        
        featureStore.modifyFeatures(new AttributeDescriptor[] { madeUp },
            new Object[] { point }, Filter.INCLUDE);

        SimpleFeatureCollection features = featureStore.getFeatures();
        Iterator i = features.iterator();

        assertTrue(i.hasNext());

        while (i.hasNext()) {
            SimpleFeature feature = (SimpleFeature) i.next();
            assertTrue(point.equalsExact((Geometry) feature.getAttribute(aname("geometry"))));
        }

        features.close(i);
    }
    
    public void testModifyFeaturesSingleAttribute() throws IOException {
        SimpleFeatureType t = featureStore.getSchema();
        featureStore.modifyFeatures(t.getDescriptor(aname("stringProperty")), "foo" , Filter.INCLUDE);

        SimpleFeatureCollection features = featureStore.getFeatures();
        Iterator i = features.iterator();

        assertTrue(i.hasNext());

        while (i.hasNext()) {
            SimpleFeature feature = (SimpleFeature) i.next();
            assertEquals("foo", feature.getAttribute(aname("stringProperty")));
        }

        features.close(i);
    }
    
    public void testModifyFeaturesInvalidFilter() throws IOException {
        SimpleFeatureType t = featureStore.getSchema();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));
        
        try {
            featureStore.modifyFeatures(new AttributeDescriptor[] { t.getDescriptor(aname("stringProperty")) },
            new Object[] { "foo" }, f);
            fail("This should have failed with an exception reporting the invalid filter");
        } catch(Exception e) {
            //  fine
        }
    }

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
    
    public void testRemoveFeaturesWithInvalidFilter() throws IOException {
        SimpleFeatureType t = featureStore.getSchema();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));
        
        try {
            featureStore.removeFeatures(f);
            fail("This should have failed with an exception reporting the invalid filter");
        } catch(Exception e) {
            //  fine
        }
    }
}
