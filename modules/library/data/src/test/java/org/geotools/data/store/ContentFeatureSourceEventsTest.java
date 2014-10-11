/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;

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

        // test that both listeners get the event.
        final SimpleFeature feature = FEATURES.get(0);
        Id fidFilter = ff.id(feature.getIdentifier());

        store1.removeFeatures(fidFilter);

        assertEquals(1, listener1.events.size());
        assertEquals(1, listener2.events.size());
        
        FeatureEvent event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.REMOVED, event.getType());
        
        // test add
        listener1.events.clear();
        listener2.events.clear();        

        store1.addFeatures(DataUtilities.collection(feature));

        assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.ADDED, event.getType());
        assertEquals(1, listener2.events.size());
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

        // test that only the listener listening with the current transaction gets the event.
        final SimpleFeature feature = FEATURES.get(0);
        Id fidFilter = ff.id(feature.getIdentifier());

        store1.removeFeatures(fidFilter);

        assertEquals(1, listener1.events.size());
        assertEquals(0, listener2.events.size());

        FeatureEvent event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.REMOVED, event.getType());

        listener1.events.clear();
        listener2.events.clear();
        
        // test that commit sends events to both listeners.

        store1.getTransaction().commit();

        assertEquals(2, listener1.events.size());
        assertEquals(2, listener2.events.size());

        event = listener2.getEvent(1);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.COMMIT, event.getType());

        // test add 
        listener1.events.clear();
        listener2.events.clear();

        store1.addFeatures(DataUtilities.collection(feature));

        assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        assertEquals(feature.getBounds(), event.getBounds());
        assertEquals(FeatureEvent.Type.ADDED, event.getType());
        assertEquals(0, listener2.events.size());

        // test that rollback sends events to both listeners.
        listener1.events.clear();
        listener2.events.clear();

        store1.getTransaction().rollback();

        assertEquals(1, listener1.events.size());
        event = listener1.getEvent(0);
        assertEquals(FeatureEvent.Type.ROLLBACK, event.getType());

        assertEquals(1, listener2.events.size());
    }

    

}
