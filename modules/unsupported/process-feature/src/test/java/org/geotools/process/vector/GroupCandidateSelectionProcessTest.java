/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class GroupCandidateSelectionProcessTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    DataStore store;

    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null);
        store = new PropertyDataStore(file);
    }

    @Test
    public void testFeatureCollectionFilteringByMin() throws Exception {
        SimpleFeatureSource source = store.getFeatureSource("featuresToGroup");
        Query query = new Query();
        query.setFilter(Filter.INCLUDE);
        SortBy[] sorts = {
            ff.sort("group", SortOrder.ASCENDING), ff.sort("group", SortOrder.ASCENDING)
        };
        query.setSortBy(sorts);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
        int size = collection.size();
        assertEquals(12, size);
        List<String> props = Arrays.asList("group", "group2");
        PropertyName pn = ff.property("numericVal");
        FeatureCollection features =
                new GroupCandidateSelectionProcess()
                        .execute(collection, "MIN", "numericVal", props);
        try (FeatureIterator it = features.features()) {
            List<Integer> numericResults = new ArrayList<>(6);
            while (it.hasNext()) {
                numericResults.add((Integer) pn.evaluate(it.next()));
            }
            assertEquals(2, numericResults.get(0).intValue());
            assertEquals(43, numericResults.get(1).intValue());
            assertEquals(11, numericResults.get(2).intValue());
            assertEquals(65, numericResults.get(3).intValue());
            assertEquals(47, numericResults.get(4).intValue());
            assertEquals(90, numericResults.get(5).intValue());
        }
    }

    @Test
    public void testFeatureCollectionFilteringByMax() throws Exception {
        SimpleFeatureSource source = store.getFeatureSource("featuresToGroup");
        Query query = new Query();
        query.setFilter(Filter.INCLUDE);
        SortBy[] sorts = {
            ff.sort("group", SortOrder.ASCENDING), ff.sort("group", SortOrder.ASCENDING)
        };
        query.setSortBy(sorts);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
        int size = collection.size();
        assertEquals(12, size);
        List<String> props = Arrays.asList("group", "group2");
        PropertyName pn = ff.property("numericVal");
        FeatureCollection features =
                new GroupCandidateSelectionProcess()
                        .execute(collection, "MAX", "numericVal", props);
        try (FeatureIterator it = features.features()) {
            List<Integer> numericResults = new ArrayList<>(6);
            while (it.hasNext()) {
                numericResults.add((Integer) pn.evaluate(it.next()));
            }
            assertEquals(40, numericResults.get(0).intValue());
            assertEquals(50, numericResults.get(1).intValue());
            assertEquals(22, numericResults.get(2).intValue());
            assertEquals(80, numericResults.get(3).intValue());
            assertEquals(57, numericResults.get(4).intValue());
            assertEquals(91, numericResults.get(5).intValue());
        }
    }

    @Test
    public void testFeatureCollectionFilteringByMinWithNullValues() throws Exception {
        SimpleFeatureSource source = store.getFeatureSource("featuresToGroupWithNullValues");
        Query query = new Query();
        query.setFilter(Filter.INCLUDE);
        SortBy[] sorts = {
            ff.sort("group", SortOrder.ASCENDING), ff.sort("group", SortOrder.ASCENDING)
        };
        query.setSortBy(sorts);
        SimpleFeatureCollection collection = source.getFeatures(query);
        int size = collection.size();
        assertEquals(12, size);
        List<String> props = Arrays.asList("group", "group2");
        PropertyName pn = ff.property("numericVal");
        FeatureCollection features =
                new GroupCandidateSelectionProcess()
                        .execute(collection, "MIN", "numericVal", props);
        try (FeatureIterator it = features.features()) {
            List<Integer> numericResults = new ArrayList<>(6);
            while (it.hasNext()) {
                numericResults.add((Integer) pn.evaluate(it.next()));
            }
            assertEquals(40, numericResults.get(0).intValue());
            assertEquals(43, numericResults.get(1).intValue());
            assertEquals(22, numericResults.get(2).intValue());
            assertEquals(65, numericResults.get(3).intValue());
            assertEquals(47, numericResults.get(4).intValue());
            assertEquals(91, numericResults.get(5).intValue());
        }
    }

    @Test
    public void testFeatureCollectionFilteringByMinWithGroupWithAllNullValues() throws Exception {
        SimpleFeatureSource source = store.getFeatureSource("featuresToGroupWithGroupAllNull");
        Query query = new Query();
        query.setFilter(Filter.INCLUDE);
        SortBy[] sorts = {
            ff.sort("group", SortOrder.ASCENDING), ff.sort("group", SortOrder.ASCENDING)
        };
        query.setSortBy(sorts);
        SimpleFeatureCollection collection = source.getFeatures(query);
        int size = collection.size();
        assertEquals(12, size);
        List<String> props = Arrays.asList("group", "group2");
        PropertyName pn = ff.property("numericVal");
        FeatureCollection features =
                new GroupCandidateSelectionProcess()
                        .execute(collection, "MIN", "numericVal", props);
        try (FeatureIterator it = features.features()) {
            List<Integer> numericResults = new ArrayList<>(6);
            while (it.hasNext()) {
                numericResults.add((Integer) pn.evaluate(it.next()));
            }
            assertEquals(40, numericResults.get(0).intValue());
            assertEquals(43, numericResults.get(1).intValue());
            assertNull(numericResults.get(2));
            assertEquals(65, numericResults.get(3).intValue());
            assertEquals(47, numericResults.get(4).intValue());
            assertEquals(91, numericResults.get(5).intValue());
        }
    }
}
