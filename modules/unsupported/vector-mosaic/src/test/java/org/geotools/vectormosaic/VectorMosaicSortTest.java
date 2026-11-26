/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.data.DefaultRepository;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Sorting on a mosaic whose index has a day attribute. Granules G2 and G4 share day 1, G1 has day 2, G3 has day 3. Each
 * granule has three features with a value attribute.
 */
public class VectorMosaicSortTest {

    private static final String TYPE_NAME = "Sorted_mosaic";
    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();
    private static final SortBy DAY_ASC = FF.sort("day", SortOrder.ASCENDING);
    private static final SortBy DAY_DESC = FF.sort("day", SortOrder.DESCENDING);
    private static final SortBy VALUE_ASC = FF.sort("value", SortOrder.ASCENDING);
    private static final SortBy ORBIT_ASC = FF.sort("orbit", SortOrder.ASCENDING);
    private static final SortBy ORBIT_DESC = FF.sort("orbit", SortOrder.DESCENDING);

    private static DataStore mosaicStore;
    private static SortingPropertyDataStore sortingIndex;
    private static SortingPropertyDataStore sortingGranules;
    private static DataStore sortingStore;
    private static DataStore objectStore;

    private VectorMosaicFeatureSource source;
    private GranuleTracker tracker;

    @BeforeClass
    public static void createStore() throws IOException {
        DefaultRepository repository = new DefaultRepository();
        repository.register(
                "delegate", new PropertyDataStore(new File("src/test/resources/org.geotools.vectormosaic.sort")));
        Map<String, Object> params = new HashMap<>();
        params.put(VectorMosaicStoreFactory.DELEGATE_STORE_NAME.getName(), "delegate");
        params.put(VectorMosaicStoreFactory.REPOSITORY_PARAM.getName(), repository);
        params.put(
                VectorMosaicStoreFactory.PREFERRED_DATASTORE_SPI.getName(), PropertyDataStoreFactory.class.getName());
        VectorMosaicStoreFactory factory = new VectorMosaicStoreFactory();
        mosaicStore = factory.createDataStore(params);

        // The index and the granules sort the natural order natively, and the granules come from the repository by
        // name
        DefaultRepository sortingRepository = new DefaultRepository();
        sortingIndex =
                new SortingPropertyDataStore(new File("src/test/resources/org.geotools.vectormosaic.sortnative"));
        sortingGranules =
                new SortingPropertyDataStore(new File("src/test/resources/org.geotools.vectormosaic.sort/granules"));
        sortingRepository.register("delegate", sortingIndex);
        sortingRepository.register("granules", sortingGranules);
        params.put(VectorMosaicStoreFactory.REPOSITORY_PARAM.getName(), sortingRepository);
        sortingStore = factory.createDataStore(params);

        // the granules have an attribute that is not serializable
        DefaultRepository objectRepository = new DefaultRepository();
        objectRepository.register(
                "delegate", new PropertyDataStore(new File("src/test/resources/org.geotools.vectormosaic.sortobject")));
        params.put(VectorMosaicStoreFactory.REPOSITORY_PARAM.getName(), objectRepository);
        objectStore = factory.createDataStore(params);
    }

    @AfterClass
    public static void disposeStores() {
        mosaicStore.dispose();
        sortingStore.dispose();
        sortingIndex.dispose();
        sortingGranules.dispose();
        objectStore.dispose();
    }

    @Before
    public void trackGranules() throws IOException {
        useStore(mosaicStore);
    }

    private void useStore(DataStore store) throws IOException {
        source = (VectorMosaicFeatureSource) store.getFeatureSource(TYPE_NAME);
        tracker = new GranuleTracker();
        source.finder.granuleTracker = tracker;
        // the first schema request reads a sample of the index, keep it out of the recorded sorts
        source.getSchema();
        sortingIndex.sorts.clear();
        sortingGranules.sorts.clear();
    }

    /** Granules with the same day come out in index order, and reading stops at the granule holding the limit. */
    @Test
    public void testSortIndexAttribute() throws IOException {
        Query query = sortQuery(DAY_ASC);
        query.setMaxFeatures(4);

        assertEquals(List.of("G2.1", "G2.2", "G2.3", "G4.1"), readIds(query));
        assertEquals(2, tracker.getCount());
    }

    /** The limit ends with the last feature of G2, so the reader never opens G4. */
    @Test
    public void testSortIndexAttributeLimitAtGranuleEnd() throws IOException {
        Query query = sortQuery(DAY_ASC);
        query.setMaxFeatures(3);

        assertEquals(List.of("G2.1", "G2.2", "G2.3"), readIds(query));
        assertEquals(1, tracker.getCount());
    }

    @Test
    public void testSortIndexAttributeDescending() throws IOException {
        List<String> expected =
                List.of("G3.1", "G3.2", "G3.3", "G1.1", "G1.2", "G1.3", "G2.1", "G2.2", "G2.3", "G4.1", "G4.2", "G4.3");
        assertEquals(expected, readIds(sortQuery(DAY_DESC)));
    }

    /** The reader sorts the granules that share a day together, on value. */
    @Test
    public void testSortGroup() throws IOException {
        List<String> expected =
                List.of("G4.3", "G2.3", "G2.1", "G2.2", "G4.2", "G4.1", "G1.2", "G1.1", "G1.3", "G3.2", "G3.3", "G3.1");
        assertEquals(expected, readIds(sortQuery(DAY_ASC, VALUE_ASC)));
    }

    /**
     * Each orbit is in one granule, so each group is a granule alone. The granule store sorts it on value, and the
     * reader stops inside the first granule.
     */
    @Test
    public void testSortGroupOneGranule() throws IOException {
        useStore(sortingStore);
        Query query = sortQuery(ORBIT_ASC, VALUE_ASC);
        query.setMaxFeatures(2);

        assertEquals(List.of("G1.2", "G1.1"), readIds(query));
        assertEquals(1, tracker.getCount());
        assertEquals(List.of(List.of(VALUE_ASC)), sortingGranules.sorts);
    }

    /**
     * G2 and G4 share day 1, so their stores get no sort. G1 and G3 are alone on their day, so their stores sort them.
     */
    @Test
    public void testSortGroupSharedDay() throws IOException {
        useStore(sortingStore);

        List<String> expected =
                List.of("G4.3", "G2.3", "G2.1", "G2.2", "G4.2", "G4.1", "G1.2", "G1.1", "G1.3", "G3.2", "G3.3", "G3.1");
        assertEquals(expected, readIds(sortQuery(DAY_ASC, VALUE_ASC)));
        List<SortBy> value = List.of(VALUE_ASC);
        assertEquals(List.of(List.of(), List.of(), value, value), sortingGranules.sorts);
    }

    /** Day 1 is in G2 and G4, which both hold value 8. The orbit, an index attribute, decides their order. */
    @Test
    public void testSortGroupTrailingIndexAttribute() throws IOException {
        Query query = sortQuery(DAY_ASC, VALUE_ASC, ORBIT_DESC);
        query.setMaxFeatures(5);

        assertEquals(List.of("G4.3", "G2.3", "G2.1", "G4.2", "G2.2"), readIds(query));
    }

    /** The reader opens G1 to find the end of the day 1 group. It never opens G3. */
    @Test
    public void testSortGroupLimit() throws IOException {
        Query query = sortQuery(DAY_ASC, VALUE_ASC);
        query.setMaxFeatures(3);

        assertEquals(List.of("G4.3", "G2.3", "G2.1"), readIds(query));
        assertEquals(3, tracker.getCount());
    }

    @Test
    public void testSortGroupPaging() throws IOException {
        Query query = sortQuery(DAY_ASC, VALUE_ASC);
        query.setStartIndex(5);
        query.setMaxFeatures(3);

        assertEquals(List.of("G4.1", "G1.2", "G1.1"), readIds(query));
    }

    /**
     * The group sort loads only the selected and sort attributes, so an unselected attribute need not be serializable.
     */
    @Test
    public void testSortGroupUnselectedAttribute() throws IOException {
        useStore(objectStore);
        Query query = sortQuery(DAY_ASC, VALUE_ASC);
        query.setPropertyNames("value");

        assertEquals(List.of("H1.2", "H2.1", "H1.1"), readIds(query));
    }

    /** The granule store cannot sort a granule with an attribute that is not serializable. The group sort does it. */
    @Test
    public void testSortGroupOneGranuleNotSortable() throws IOException {
        useStore(objectStore);
        Query query = sortQuery(ORBIT_ASC, VALUE_ASC);
        query.setPropertyNames("value");

        assertEquals(List.of("H1.2", "H1.1", "H2.1"), readIds(query));
    }

    /** The store rejects a sort on a geometry before it opens any granule. */
    @Test
    public void testSortGroupNotComparable() throws IOException {
        Query query = sortQuery(DAY_ASC, FF.sort("geom", SortOrder.ASCENDING));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> source.getReader(query));
        assertEquals(
                "Cannot sort Sorted_mosaic on [day, geom]: the sort attributes must be comparable and "
                        + "the loaded attributes serializable",
                e.getMessage());
        assertEquals(0, tracker.getCount());
    }

    /** The reader loads the sort attributes to sort, but does not return them. */
    @Test
    public void testSortAttributesNotSelected() throws IOException {
        Query query = sortQuery(DAY_ASC, VALUE_ASC);
        query.setPropertyNames("value");
        query.setMaxFeatures(3);

        List<Object> values = new ArrayList<>();
        try (SimpleFeatureIterator it = source.getFeatures(query).features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                assertEquals(1, feature.getAttributeCount());
                values.add(feature.getAttribute("value"));
            }
        }
        assertEquals(List.of(0, 2, 4), values);
    }

    /** Paging without a sort uses the natural order: index order, then the order of each granule. */
    @Test
    public void testNaturalOrderPaging() throws IOException {
        Query query = new Query(TYPE_NAME);
        query.setStartIndex(4);
        query.setMaxFeatures(3);

        assertEquals(List.of("G2.2", "G2.3", "G3.1"), readIds(query));
        assertEquals(3, tracker.getCount());
    }

    /** The count stops at startIndex + maxFeatures, before ContentFeatureSource subtracts the offset. */
    @Test
    public void testCountPage() throws IOException {
        Query query = new Query(TYPE_NAME);
        query.setStartIndex(5);
        query.setMaxFeatures(3);

        assertEquals(3, source.getCount(query));
    }

    /** Closing the reader again after the end of the data closes the granule provider once. */
    @Test
    public void testCloseTwice() throws IOException {
        GranuleSourceProvider provider = mock(GranuleSourceProvider.class);
        when(provider.getQuery()).thenReturn(new Query(TYPE_NAME));
        try (VectorMosaicFeatureReader reader = new VectorMosaicFeatureReader(provider, source)) {
            assertFalse(reader.hasNext());
        }
        verify(provider, times(1)).close();
    }

    /** The offset and the limit both fall inside the first granule. */
    @Test
    public void testNaturalOrderPagingInsideGranule() throws IOException {
        Query query = new Query(TYPE_NAME);
        query.setStartIndex(1);
        query.setMaxFeatures(1);

        assertEquals(List.of("G1.2"), readIds(query));
    }

    /** The mosaic sorts the whole result on the feature identifiers, descending, so it opens every granule. */
    @Test
    public void testReverseOrder() throws IOException {
        Query query = sortQuery(SortBy.REVERSE_ORDER);
        query.setMaxFeatures(4);

        assertEquals(List.of("G4.3", "G4.2", "G4.1", "G3.3"), readIds(query));
        assertEquals(4, tracker.getCount());
    }

    /** A sort on a granule attribute reads every granule, and the limit applies to the sorted result. */
    @Test
    public void testSortGranuleAttribute() throws IOException {
        Query query = sortQuery(VALUE_ASC);
        query.setMaxFeatures(2);

        assertEquals(List.of("G4.3", "G1.2"), readIds(query));
        assertEquals(4, tracker.getCount());
    }

    /** The reader loads the sort attribute to sort the whole result, but does not return it. */
    @Test
    public void testSortGranuleAttributeNotSelected() throws IOException {
        Query query = sortQuery(VALUE_ASC);
        query.setPropertyNames("day");
        query.setMaxFeatures(2);

        List<Object> days = new ArrayList<>();
        try (SimpleFeatureIterator it = source.getFeatures(query).features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                assertEquals(1, feature.getAttributeCount());
                days.add(feature.getAttribute("day"));
            }
        }
        assertEquals(List.of(1, 2), days);
    }

    /** The filter matches only G3, so the granule store sorts it. */
    @Test
    public void testSortGranuleAttributeOneGranule() throws IOException {
        useStore(sortingStore);
        Query query = sortQuery(VALUE_ASC);
        query.setFilter(FF.equals(FF.property("orbit"), FF.literal(3)));

        assertEquals(List.of("G3.2", "G3.3", "G3.1"), readIds(query));
        assertEquals(List.of(List.of(VALUE_ASC)), sortingGranules.sorts);
    }

    /** Paging sends the natural order to the index and to each granule. */
    @Test
    public void testNaturalOrderPagingNative() throws IOException {
        useStore(sortingStore);
        Query query = new Query(TYPE_NAME);
        query.setStartIndex(4);
        query.setMaxFeatures(3);

        assertEquals(List.of("G2.2", "G2.3", "G3.1"), readIds(query));
        assertEquals(3, tracker.getCount());
        List<SortBy> natural = List.of(SortBy.NATURAL_ORDER);
        assertEquals(List.of(natural), sortingIndex.sorts);
        assertEquals(List.of(natural, natural, natural), sortingGranules.sorts);
    }

    private static Query sortQuery(SortBy... sortBy) {
        Query query = new Query(TYPE_NAME);
        query.setSortBy(sortBy);
        return query;
    }

    private List<String> readIds(Query query) throws IOException {
        List<String> ids = new ArrayList<>();
        try (SimpleFeatureIterator it = source.getFeatures(query).features()) {
            while (it.hasNext()) {
                ids.add(it.next().getID());
            }
        }
        return ids;
    }
}
