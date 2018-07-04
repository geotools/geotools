package org.geotools.data.geojson;
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class GeoJSONDataStoreFactoryTest {

    private URL url;
    private DataStore store;

    @Before
    public void setUp() throws Exception {
        url = TestData.url(GeoJSONDataStore.class, "ne_110m_admin_1_states_provinces.geojson");
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", url);

        store = DataStoreFinder.getDataStore(params);
        assertNotNull("didn't create store", store);
    }

    @Test
    public void testExtensions() throws IOException {
        URL t1 = new URL("http://example.com/ian.json");
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", t1);

        DataStore store1 = DataStoreFinder.getDataStore(params);
        assertNotNull("didn't create store", store1);
        URL t2 = new URL("http://example.com/ian.geojson");

        params.put("url", t2);
        store1 = DataStoreFinder.getDataStore(params);
        assertNotNull("didn't create store", store1);

        URL t3 = new URL("http://example.com/ian.randomjson");
        store1 = null;
        params.put("url", t3);
        store = DataStoreFinder.getDataStore(params);
        assertNull("created bad store", store1);
        URL t4 = new URL("http://example.com/ian.random");
        store1 = null;
        params.put("url", t4);
        store = DataStoreFinder.getDataStore(params);
        assertNull("created bad store", store1);
    }

    @Test
    public void testGetTypeNames() throws IOException {

        String names[] = store.getTypeNames();
        // System.out.println("typenames: " + names.length);
        assertEquals("wrong number of types", 1, names.length);
        // System.out.println("typename[0]: " + names[0]);
        assertEquals("ne_110m_admin_1_states_provinces", names[0]);
    }

    @Test
    public void testGetSchema() throws IOException {
        String names[] = store.getTypeNames();
        SimpleFeatureType schema = store.getSchema(names[0]);
        assertNotNull("no schema found", schema);
    }

    @Test
    public void testGetFeatureReader() throws IOException {
        String names[] = store.getTypeNames();
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(new Query(names[0], Filter.INCLUDE), null);
        assertNotNull("failed to get FeatureReader", reader);
    }
}
