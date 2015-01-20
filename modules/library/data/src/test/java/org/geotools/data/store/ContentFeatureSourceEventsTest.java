/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012-2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.store;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.BatchFeatureEvent;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Tests events in autocommit and with transactions
 * 
 * @author Niels Charlier, Scitus Development
 */
public class ContentFeatureSourceEventsTest extends AbstractContentTest {


    
    private static class Listener implements FeatureListener {
        String name;

        List<FeatureEvent> events = new ArrayList<FeatureEvent>();

        public Listener(String name) {
            this.name = name;
        }

        public void changed(FeatureEvent featureEvent) {
            this.events.add(featureEvent);
        }

        FeatureEvent getEvent(int i) {
            return events.get(i);
        }
    }
    
    @Test
    public void testFeatureEventsAutoCommit() throws Exception {
        DataStore store = new MockContentDataStore();
        SimpleFeatureStore store1 = (SimpleFeatureStore) store.getFeatureSource(TYPENAME);
        SimpleFeatureStore store2 = (SimpleFeatureStore) store.getFeatureSource(TYPENAME);        
        
        Listener listener1 = new Listener("one");
        Listener listener2 = new Listener("two");
        store1.addFeatureListener(listener1);
        store2.addFeatureListener(listener2);
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        final SimpleFeature feature = FEATURES.get(0);
        Filter fidFilter = ff.id(feature.getIdentifier());
        
        // test change
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
        WKTReader reader = new WKTReader( geometryFactory );
        Polygon geom = (Polygon) reader.read("POLYGON ((0 2, 1 0, 1 1, 0 1, 0 2))");
        
        ReferencedEnvelope bounds = new ReferencedEnvelope();
        bounds.include(feature.getBounds());
        bounds.expandToInclude(geom.getEnvelopeInternal());
        
        store1.modifyFeatures(new Name[]{new NameImpl("geom")}, 
                new Object[]{geom}, fidFilter);
        
        // test that both listeners get the event.
        assertEquals(1, listener1.events.size());
        assertEquals(1, listener2.events.size());
        
        FeatureEvent event = listener1.getEvent(0);
        assertEquals(bounds, event.getBounds());
        assertEquals(FeatureEvent.Type.CHANGED, event.getType());
        assertEquals(event, listener2.getEvent(0));
        
        listener1.events.clear();
        listener2.events.clear();
        
        // test remove
        store1.removeFeatures(fidFilter);
        
        assertEquals(1, listener1.events.size());
        assertEquals(1, listener2.events.size());
        
        event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.REMOVED, event.getType());
        assertEquals(event, listener2.getEvent(0));
        
        // test add
        listener1.events.clear();
        listener2.events.clear();
        
        store1.addFeatures(DataUtilities.collection(feature));
        
        assertEquals(1, listener1.events.size());
        assertEquals(1, listener2.events.size());
        
        event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.ADDED, event.getType());
        assertEquals(event, listener2.getEvent(0));
    }
    
    @Test
    public void testFeatureEventsWithTransaction() throws Exception {
        DataStore store = new MockContentDataStore();
        SimpleFeatureStore store1 = (SimpleFeatureStore) store.getFeatureSource(TYPENAME);
        SimpleFeatureStore store2 = (SimpleFeatureStore) store.getFeatureSource(TYPENAME);
        store1.setTransaction(new DefaultTransaction());
        store2.setTransaction(new DefaultTransaction());
        
        Listener listener1 = new Listener("one");
        Listener listener2 = new Listener("two");
        store1.addFeatureListener(listener1);
        store2.addFeatureListener(listener2);
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        final SimpleFeature feature = FEATURES.get(0);
        Filter fidFilter = ff.id(feature.getIdentifier());
        
        // test change
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
        WKTReader reader = new WKTReader( geometryFactory );
        Polygon geom = (Polygon) reader.read("POLYGON ((0 2, 1 0, 1 1, 0 1, 0 2))");
        
        ReferencedEnvelope bounds = new ReferencedEnvelope();
        bounds.include(feature.getBounds());
        bounds.expandToInclude(geom.getEnvelopeInternal());
        
        store1.modifyFeatures(new Name[]{new NameImpl("geom")}, 
                new Object[]{geom}, fidFilter);
        
        // test that only the listener listening with the current transaction gets the event.
        assertEquals(1, listener1.events.size());
        assertEquals(0, listener2.events.size());
        
        FeatureEvent event = listener1.getEvent(0);
        assertEquals(bounds, event.getBounds());
        assertEquals(FeatureEvent.Type.CHANGED, event.getType());
        
        listener1.events.clear();
        listener2.events.clear();
        
        // test that rollback sends events to
        // only the listener listening with the current transaction.
        store1.getTransaction().rollback();
        
        assertEquals(1, listener1.events.size());
        assertEquals(0, listener2.events.size());
        
        event = listener1.getEvent(0);
        assertEquals(bounds, event.getBounds());
        assertEquals(FeatureEvent.Type.ROLLBACK, event.getType());
        
        listener1.events.clear();
        listener2.events.clear();
        
        //test remove
        store1.removeFeatures(fidFilter);
        
        assertEquals(1, listener1.events.size());
        assertEquals(0, listener2.events.size());
        
        event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.REMOVED, event.getType());
        
        listener1.events.clear();
        listener2.events.clear();
        
        // test that commit sends events to all listeners
        // except the listener listening with the current transaction.
        store1.getTransaction().commit();
        
        assertEquals(0, listener1.events.size());
        assertEquals(1, listener2.events.size());
        
        event = listener2.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.COMMIT, event.getType());
        
        listener1.events.clear();
        listener2.events.clear();
        
        // test add
        store1.addFeatures(DataUtilities.collection(feature));
        
        assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.ADDED, event.getType());
        assertEquals(0, listener2.events.size());
        
        listener1.events.clear();
        listener2.events.clear();
        
        // test that rollback sends events to
        // only the listener listening with the current transaction.
        store1.getTransaction().rollback();
        
        assertEquals(1, listener1.events.size());
        assertEquals(0, listener2.events.size());
        event = listener1.getEvent(0);
        assertEquals(FeatureEvent.Type.ROLLBACK, event.getType());
    }
    
    @Test
    public void testBatchFeatureEvents() throws Exception {
        DataStore store = new MockContentDataStore();
        SimpleFeatureStore store1 = (SimpleFeatureStore) store.getFeatureSource(TYPENAME);
        SimpleFeatureStore store2 = (SimpleFeatureStore) store.getFeatureSource(TYPENAME);
        store1.setTransaction(new DefaultTransaction());
        
        Listener listener1 = new Listener("one");
        Listener listener2 = new Listener("two");
        store1.addFeatureListener(listener1);
        store2.addFeatureListener(listener2);
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        final SimpleFeature feature0 = FEATURES.get(0);
        final SimpleFeature feature1 = FEATURES.get(1);
        Filter fidFilter0 = ff.id(feature0.getIdentifier());
        Filter fidFilter1 = ff.id(feature1.getIdentifier());
        
        //remove a feature
        store1.removeFeatures(fidFilter0);
        
        assertEquals(1, listener1.events.size());
        assertEquals(0, listener2.events.size());
        
        FeatureEvent event = listener1.getEvent(0);
        assertEquals(feature0.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.REMOVED, event.getType());
        
        //remove another feature
        store1.removeFeatures(fidFilter1);
        
        assertEquals(2, listener1.events.size());
        assertEquals(0, listener2.events.size());
        
        event = listener1.getEvent(1);
        assertEquals(feature1.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.REMOVED, event.getType());
        
        // commit the changes
        store1.getTransaction().commit();
        
        // test that multiple changes are contained within a single batch feature event
        assertEquals(2, listener1.events.size());
        assertEquals(1, listener2.events.size());
        
        event = listener2.getEvent(0);
        
        assertTrue(event instanceof BatchFeatureEvent);
        ReferencedEnvelope bounds = new ReferencedEnvelope();
        bounds.include(feature0.getBounds());
        bounds.include(feature1.getBounds());
        
        assertEquals(bounds, event.getBounds());
        assertEquals(FeatureEvent.Type.COMMIT, event.getType());
    }
}
