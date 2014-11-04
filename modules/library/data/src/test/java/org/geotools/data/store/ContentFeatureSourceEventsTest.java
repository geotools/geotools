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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;

/**
 * Tests events in autocommit and with transactions
 * 
 * @author Niels Charlier, Scitus Development
 */
public class ContentFeatureSourceEventsTest {

    /**
     * Mock feature type name.
     */
    private static final Name TYPENAME = new NameImpl("Mock");

    /**
     * Mock feature type.
     */
    private static final SimpleFeatureType TYPE = buildType();

    /**
     * The list of features on which paging is tested.
     */
    @SuppressWarnings("serial")
    private static final List<SimpleFeature> FEATURES = new ArrayList<SimpleFeature>() {
        {
            add(buildFeature("mock.3"));
            add(buildFeature("mock.1"));
            add(buildFeature("mock.2"));
        }
    };
    
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
            return (FeatureEvent) events.get(i);
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

    /**
     * Build the test type.
     */
    private static SimpleFeatureType buildType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(TYPENAME);
        builder.add("geom", LineString.class);
        return builder.buildFeatureType();
    }

    /**
     * Build a test feature with the specified id.
     */
    private static SimpleFeature buildFeature(String id) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(TYPE);
        builder.add(new Envelope(0, 1, 0, 1));
        return builder.buildFeature(id);
    }

    /**
     * {@link ContentDataStore} for the test features.
     * 
     */
    private static class MockContentDataStore extends ContentDataStore {

        /**
         * @see org.geotools.data.store.ContentDataStore#createTypeNames()
         */
        @SuppressWarnings("serial")
        @Override
        protected List<Name> createTypeNames() throws IOException {
            return new ArrayList<Name>() {
                {
                    add(TYPENAME);
                }
            };
        }

        /**
         * @see org.geotools.data.store.ContentDataStore#createFeatureSource(org.geotools.data.store.ContentEntry)
         */
        @Override
        protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
            return new MockContentFeatureStore(entry, null);
        }

    }

    /**
     * {@link ContentFeatureSource} that returns the test features.
     */
    @SuppressWarnings("unchecked")
    private static class MockContentFeatureStore extends ContentFeatureStore {

        public MockContentFeatureStore(ContentEntry entry, Query query) {
            super(entry, query);
        }

        /**
         * Not implemented.
         */
        @Override
        protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
            throw new UnsupportedOperationException();            
        }

        /**
         * Not implemented.
         */
        @Override
        protected int getCountInternal(Query query) throws IOException {
            throw new UnsupportedOperationException();
        }

        /**
         * @see org.geotools.data.store.ContentFeatureSource#getReaderInternal(org.geotools.data.Query)
         */
        @Override
        protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
                throws IOException {
            return new MockSimpleFeatureReader();
        }

        /**
         * @see org.geotools.data.store.ContentFeatureSource#buildFeatureType()
         */
        @Override
        protected SimpleFeatureType buildFeatureType() throws IOException {
            return TYPE;
        }

        @Override
        protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query,
                int flags) throws IOException {
            return new MockSimpleFeatureWriter();
        }

    }

    /**
     * Decorate the list of test features as a {@link SimpleFeatureReader}.
     */
    private static class MockSimpleFeatureReader implements SimpleFeatureReader {

        /**
         * Index of the next test feature to be returned.
         */
        private int index = 0;

        /**
         * @see org.geotools.data.FeatureReader#getFeatureType()
         */
        @Override
        public SimpleFeatureType getFeatureType() {
            return TYPE;
        }

        /**
         * @see org.geotools.data.FeatureReader#next()
         */
        @Override
        public SimpleFeature next() throws IOException, IllegalArgumentException,
                NoSuchElementException {
            return FEATURES.get(index++);
        }

        /**
         * @see org.geotools.data.FeatureReader#hasNext()
         */
        @Override
        public boolean hasNext() throws IOException {
            return index < FEATURES.size();
        }

        /**
         * @see org.geotools.data.FeatureReader#close()
         */
        @Override
        public void close() throws IOException {
            // ignored
        }

    }
    
    /**
     * Decorate the list of test features as a {@link SimpleFeatureReader}.
     */
    private static class MockSimpleFeatureWriter implements SimpleFeatureWriter {

        /**
         * Index of the next test feature to be returned.
         */
        private int index = 0;
        
        SimpleFeature newFeature;

        @Override
        public SimpleFeatureType getFeatureType() {
            return TYPE;
        }

        @Override
        public SimpleFeature next() throws IOException {
            if (index >= FEATURES.size()) {
                newFeature = buildFeature("mock." + (++index));
                return newFeature;
            }
            return FEATURES.get(index++);
        }

        @Override
        public void remove() throws IOException {
            if (index > 0 && index <= FEATURES.size()){
                SimpleFeature feature = FEATURES.remove(index-1);
            }
        }

        @Override
        public void write() throws IOException {
            if (index > FEATURES.size()) {
               FEATURES.add(newFeature);
            } 
        }

        @Override
        public boolean hasNext() throws IOException {
            return index < FEATURES.size();
        }

        @Override
        public void close() throws IOException {
            // ignored            
        }
        
    }

}
