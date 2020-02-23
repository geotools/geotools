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

import java.io.IOException;
import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.sort.SortBy;

/**
 * Test the paging behaviour of {@link ContentFeatureSource}. To establish inter-page consistency,
 * if no sorting order is specified in the query, the feature source should impose natural sorting.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class ContentFeatureSourcePagingTest extends AbstractContentTest {

    DataStore store = new MockContentDataStore();

    /** Test that the default query returns all features unsorted. */
    @Test
    public void defaultFeatures() throws IOException {
        Query query = new Query();
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(3, fs.getCount(query));
        Assert.assertEquals("mock.3", features[0].getID());
        Assert.assertEquals("mock.1", features[1].getID());
        Assert.assertEquals("mock.2", features[2].getID());
    }

    /** Test natural sorting. */
    @Test
    public void naturalSortedFeatures() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(3, fs.getCount(query));
        Assert.assertEquals("mock.1", features[0].getID());
        Assert.assertEquals("mock.2", features[1].getID());
        Assert.assertEquals("mock.3", features[2].getID());
    }

    /** Test reverse sorting. */
    @Test
    public void reverseSortedFeatures() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(3, fs.getCount(query));
        Assert.assertEquals("mock.3", features[0].getID());
        Assert.assertEquals("mock.2", features[1].getID());
        Assert.assertEquals("mock.1", features[2].getID());
    }

    /** Test the first page of one feature per page. */
    @Test
    public void oneFeatureFirstPage() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(1);
        query.setStartIndex(0);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.1", features[0].getID());
    }

    /** Test the second page of one feature per page. */
    @Test
    public void oneFeatureSecondPage() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(1);
        query.setStartIndex(1);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.2", features[0].getID());
    }

    /** Test the third page of one feature per page. */
    @Test
    public void oneFeatureThirdPage() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(1);
        query.setStartIndex(2);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.3", features[0].getID());
    }

    /** Test the first page of one feature per page with natural sorting. */
    @Test
    public void naturalSortedOneFeatureFirstPage() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        query.setMaxFeatures(1);
        query.setStartIndex(0);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.1", features[0].getID());
    }

    /** Test the second page of one feature per page with natural sorting. */
    @Test
    public void naturalSortedOneFeatureSecondPage() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        query.setMaxFeatures(1);
        query.setStartIndex(1);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.2", features[0].getID());
    }

    /** Test the third page of one feature per page with natural sorting. */
    @Test
    public void naturalSortedOneFeatureThirdPage() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        query.setMaxFeatures(1);
        query.setStartIndex(2);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.3", features[0].getID());
    }

    /** Test the first page of one feature per page with reverse sorting. */
    @Test
    public void reverseSortedOneFeatureFirstPage() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        query.setMaxFeatures(1);
        query.setStartIndex(0);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.3", features[0].getID());
    }

    /** Test the second page of one feature per page with reverse sorting. */
    @Test
    public void reverseSortedOneFeatureSecondPage() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        query.setMaxFeatures(1);
        query.setStartIndex(1);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.2", features[0].getID());
    }

    /** Test the third page of one feature per page with reverse sorting. */
    @Test
    public void reverseSortedOneFeatureThirdPage() throws IOException {
        Query query = new Query();
        query.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        query.setMaxFeatures(1);
        query.setStartIndex(2);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.1", features[0].getID());
    }

    /** Test the first page of two features per page. */
    @Test
    public void twoFeaturesFirstPage() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(2);
        query.setStartIndex(0);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(2, fs.getCount(query));
        Assert.assertEquals("mock.1", features[0].getID());
        Assert.assertEquals("mock.2", features[1].getID());
    }

    /** Test the page of two features per page that should contain the last two features. */
    @Test
    public void twoFeaturesLastPage() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(2);
        query.setStartIndex(1);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(2, fs.getCount(query));
        Assert.assertEquals("mock.2", features[0].getID());
        Assert.assertEquals("mock.3", features[1].getID());
    }

    /**
     * Test a page of two features that only contains one because startindex is too close to the
     * end.
     */
    @Test
    public void twoFeaturesReturnOne() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(2);
        query.setStartIndex(2);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(1, fs.getCount(query));
        Assert.assertEquals("mock.3", features[0].getID());
    }

    /** Test a single page with three features. */
    @Test
    public void threeFeatures() throws IOException {
        Query query = new Query();
        query.setMaxFeatures(3);
        query.setStartIndex(0);
        SimpleFeatureSource fs = store.getFeatureSource(TYPENAME);
        SimpleFeature[] features = (SimpleFeature[]) fs.getFeatures(query).toArray();
        Assert.assertEquals(3, fs.getCount(query));
        Assert.assertEquals("mock.1", features[0].getID());
        Assert.assertEquals("mock.2", features[1].getID());
        Assert.assertEquals("mock.3", features[2].getID());
    }
}
