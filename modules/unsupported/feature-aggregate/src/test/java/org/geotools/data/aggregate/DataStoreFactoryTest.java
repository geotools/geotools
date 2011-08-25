package org.geotools.data.aggregate;

import static org.geotools.data.aggregate.AggregatingDataStoreFactory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.NameImpl;
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
        params.put(STORES_PARAM.key, new String[] {"gt:store3"});

        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(1, store.getTypeNames().length);
        assertTrue(store.isTolerant());
    }
    
    @Test
    public void testConfigurationUrl() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        params.put(CONFIGURATION.key, DataStoreFactoryTest.class.getResource("configuration.xml"));

        checkManualConfiguration(params);
    }
    
    @Test
    public void testConfigurationXml() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(REPOSITORY_PARAM.key, repository);
        InputStream is = DataStoreFactoryTest.class.getResourceAsStream("configuration.xml");
        params.put(CONFIGURATION_XML.key, IOUtils.toString(is));

        checkManualConfiguration(params);
    }

    private void checkManualConfiguration(Map<String, Object> params) throws IOException {
        AggregatingDataStore store = (AggregatingDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(store);
        assertEquals(2, store.getTypeNames().length);
        assertTrue(store.isTolerant());
        
        AggregateTypeConfiguration bp = store.getConfiguration("BasicPolygons");
        assertNotNull(bp);
        assertEquals("BasicPolygons", bp.getName());
        assertEquals(2, bp.getStoreMap().size());
        assertEquals("BasicPolygons", bp.getStoreMap().get(new NameImpl("store1")));
        assertEquals("BasicPolygons2", bp.getStoreMap().get(new NameImpl("store2")));
        
        AggregateTypeConfiguration rs = store.getConfiguration("RoadSegments");
        assertNotNull(rs);
        assertEquals("RoadSegments", rs.getName());
        assertEquals(2, rs.getStoreMap().size());
        assertEquals("RoadSegments", rs.getStoreMap().get(new NameImpl("store1")));
        assertEquals("RoadSegments2", rs.getStoreMap().get(new NameImpl("store3")));
    }

}
