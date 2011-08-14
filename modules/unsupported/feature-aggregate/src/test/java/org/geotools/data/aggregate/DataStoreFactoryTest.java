package org.geotools.data.aggregate;

import static org.geotools.data.aggregate.AggregatingDataStoreFactory.REPOSITORY_PARAM;
import static org.geotools.data.aggregate.AggregatingDataStoreFactory.STORES_PARAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.junit.Test;

public class DataStoreFactoryTest extends AbstractAggregatingStoreTest {

    @Test
    public void testConnectMinimalParams() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);

        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(0, store.getTypeNames().length);
        assertTrue(store.isTolerant());
    }

    @Test
    public void testOneStore() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        params.put(STORES_PARAM.key, Arrays.asList("gt:store3"));

        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(1, store.getTypeNames().length);
        assertTrue(store.isTolerant());
    }

}
