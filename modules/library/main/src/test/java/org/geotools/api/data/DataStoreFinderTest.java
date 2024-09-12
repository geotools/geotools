/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.api.data;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import org.junit.Test;

public class DataStoreFinderTest {

    /** Should find at least the {@link org.geotools.data.DataAccessFinderTest} mock store */
    @Test
    public void testLookup() throws Exception {
        Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
        assertTrue(it.hasNext());
    }

    @Test
    public void testDynamicRegistration() throws Exception {
        // setting up the mocks
        Map<String, ?> params = Map.of("testKey", "testValue");
        DataStoreFactorySpi mockFactory = createMock(DataStoreFactorySpi.class);
        expect(mockFactory.isAvailable()).andReturn(true).anyTimes();
        expect(mockFactory.canProcess(params)).andReturn(true).anyTimes();
        DataStore mockStore = createMock(DataStore.class);
        expect(mockFactory.createDataStore(params)).andReturn(mockStore).anyTimes();
        replay(mockFactory);

        // first lookup attempt, not registered
        assertNull(DataStoreFinder.getDataStore(params));

        // register and second lookup
        DataStoreFinder.registerFactrory(mockFactory);
        assertEquals(mockStore, DataStoreFinder.getDataStore(params));

        // unregister, should stop working
        DataStoreFinder.deregisterFactrory(mockFactory);
        assertNull(DataStoreFinder.getDataStore(params));
    }
}
