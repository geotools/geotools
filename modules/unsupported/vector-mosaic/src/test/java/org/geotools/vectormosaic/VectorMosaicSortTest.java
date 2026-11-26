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
import org.geotools.data.DefaultRepository;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
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

    private static DataStore mosaicStore;

    private VectorMosaicFeatureSource source;

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
        mosaicStore = new VectorMosaicStoreFactory().createDataStore(params);
    }

    @AfterClass
    public static void disposeStores() {
        mosaicStore.dispose();
    }

    @Before
    public void getSource() throws IOException {
        source = (VectorMosaicFeatureSource) mosaicStore.getFeatureSource(TYPE_NAME);
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
