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

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.sort.SortBy;

/**
 * Test the paging behaviour of {@link ContentFeatureSource}. To establish inter-page consistency,
 * if no sorting order is specified in the query, the feature source should impose natural sorting.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class ContentFeatureSourcePagingTest {

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

    /**
     * Test that the default query returns all features unsorted.
     */
    @Test
    public void defaultFeatures() {
        Query query = new Query();
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(3, features.size());
        Assert.assertEquals("mock.3", features.get(0).getID());
        Assert.assertEquals("mock.1", features.get(1).getID());
        Assert.assertEquals("mock.2", features.get(2).getID());
    }

    /**
     * Test natural sorting.
     */
    @Test
    public void naturalSortedFeatures() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(3, features.size());
        Assert.assertEquals("mock.1", features.get(0).getID());
        Assert.assertEquals("mock.2", features.get(1).getID());
        Assert.assertEquals("mock.3", features.get(2).getID());
    }

    /**
     * Test reverse sorting.
     */
    @Test
    public void reverseSortedFeatures() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.REVERSE_ORDER });
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(3, features.size());
        Assert.assertEquals("mock.3", features.get(0).getID());
        Assert.assertEquals("mock.2", features.get(1).getID());
        Assert.assertEquals("mock.1", features.get(2).getID());
    }

    /**
     * Test the first page of one feature per page.
     */
    @Test
    public void oneFeatureFirstPage() {
        Query query = new Query();
        query.setMaxFeatures(1);
        query.setStartIndex(0);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.1", features.get(0).getID());
    }

    /**
     * Test the second page of one feature per page.
     */
    @Test
    public void oneFeatureSecondPage() {
        Query query = new Query();
        query.setMaxFeatures(1);
        query.setStartIndex(1);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.2", features.get(0).getID());
    }

    /**
     * Test the third page of one feature per page.
     */
    @Test
    public void oneFeatureThirdPage() {
        Query query = new Query();
        query.setMaxFeatures(1);
        query.setStartIndex(2);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.3", features.get(0).getID());
    }

    /**
     * Test the first page of one feature per page with natural sorting.
     */
    @Test
    public void naturalSortedOneFeatureFirstPage() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
        query.setMaxFeatures(1);
        query.setStartIndex(0);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.1", features.get(0).getID());
    }

    /**
     * Test the second page of one feature per page with natural sorting.
     */
    @Test
    public void naturalSortedOneFeatureSecondPage() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
        query.setMaxFeatures(1);
        query.setStartIndex(1);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.2", features.get(0).getID());
    }

    /**
     * Test the third page of one feature per page with natural sorting.
     */
    @Test
    public void naturalSortedOneFeatureThirdPage() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
        query.setMaxFeatures(1);
        query.setStartIndex(2);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.3", features.get(0).getID());
    }

    /**
     * Test the first page of one feature per page with reverse sorting.
     */
    @Test
    public void reverseSortedOneFeatureFirstPage() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.REVERSE_ORDER });
        query.setMaxFeatures(1);
        query.setStartIndex(0);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.3", features.get(0).getID());
    }

    /**
     * Test the second page of one feature per page with reverse sorting.
     */
    @Test
    public void reverseSortedOneFeatureSecondPage() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.REVERSE_ORDER });
        query.setMaxFeatures(1);
        query.setStartIndex(1);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.2", features.get(0).getID());
    }

    /**
     * Test the third page of one feature per page with reverse sorting.
     */
    @Test
    public void reverseSortedOneFeatureThirdPage() {
        Query query = new Query();
        query.setSortBy(new SortBy[] { SortBy.REVERSE_ORDER });
        query.setMaxFeatures(1);
        query.setStartIndex(2);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.1", features.get(0).getID());
    }

    /**
     * Test the first page of two features per page.
     */
    @Test
    public void twoFeaturesFirstPage() {
        Query query = new Query();
        query.setMaxFeatures(2);
        query.setStartIndex(0);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(2, features.size());
        Assert.assertEquals("mock.1", features.get(0).getID());
        Assert.assertEquals("mock.2", features.get(1).getID());
    }

    /**
     * Test the page of two features per page that should contain the last two features.
     */
    @Test
    public void twoFeaturesLastPage() {
        Query query = new Query();
        query.setMaxFeatures(2);
        query.setStartIndex(1);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(2, features.size());
        Assert.assertEquals("mock.2", features.get(0).getID());
        Assert.assertEquals("mock.3", features.get(1).getID());
    }

    /**
     * Test a page of two features that only contains one because startindex is too close to the
     * end.
     */
    @Test
    public void twoFeaturesReturnOne() {
        Query query = new Query();
        query.setMaxFeatures(2);
        query.setStartIndex(2);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("mock.3", features.get(0).getID());
    }

    /**
     * Test a single page with three features.
     */
    @Test
    public void threeFeatures() {
        Query query = new Query();
        query.setMaxFeatures(3);
        query.setStartIndex(0);
        List<SimpleFeature> features = readFeatures(query);
        Assert.assertEquals(3, features.size());
        Assert.assertEquals("mock.1", features.get(0).getID());
        Assert.assertEquals("mock.2", features.get(1).getID());
        Assert.assertEquals("mock.3", features.get(2).getID());
    }

    /**
     * Read all the test features into a list, using the specified query.
     */
    private List<SimpleFeature> readFeatures(Query query) {
        List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        DataStore store = new MockContentDataStore();
        SimpleFeatureCollection collection;
        try {
            collection = store.getFeatureSource(TYPENAME).getFeatures(query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (SimpleFeatureIterator iterator = collection.features(); iterator.hasNext();) {
            features.add(iterator.next());
        }
        return features;
    }

    /**
     * Build the test type.
     */
    private static SimpleFeatureType buildType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(TYPENAME);
        return builder.buildFeatureType();
    }

    /**
     * Build a test feature with the specified id.
     */
    private static SimpleFeature buildFeature(String id) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(TYPE);
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
            return new MockContentFeatureSource(entry, null);
        }

    }

    /**
     * {@link ContentFeatureSource} that returns the test features.
     */
    @SuppressWarnings("unchecked")
    private static class MockContentFeatureSource extends ContentFeatureSource {

        public MockContentFeatureSource(ContentEntry entry, Query query) {
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

}
