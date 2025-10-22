/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.hc.core5.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.geotools.api.data.DataStore;
import org.junit.Test;

public class ElasticDataStoreFinderIT extends ElasticTestSupport {

    @Test
    public void testFactoryDefaults() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        dataStore = (ElasticDataStore) factory.createDataStore(params);

        ElasticDataStoreFactory fac = new ElasticDataStoreFactory();
        assertEquals(fac.getDisplayName(), ElasticDataStoreFactory.DISPLAY_NAME);
        assertEquals(fac.getDescription(), ElasticDataStoreFactory.DESCRIPTION);
        assertArrayEquals(fac.getParametersInfo(), ElasticDataStoreFactory.PARAMS);
        assertNull(fac.getImplementationHints());
        assertNull(fac.createNewDataStore(null));
    }

    @Test
    public void testFactory() throws IOException {
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        assertTrue(factory.isAvailable());

        Map<String, Serializable> map = new HashMap<>();
        map.put(ElasticDataStoreFactory.HOSTNAME.key, "localhost");
        map.put(ElasticDataStoreFactory.HOSTPORT.key, port);
        map.put(ElasticDataStoreFactory.INDEX_NAME.key, "sample");

        DataStore store = factory.createDataStore(map);

        assertNotNull(store);
        assertTrue(store instanceof ElasticDataStore);
    }

    @Test
    public void testFactoryWithMissingRequired() throws IOException {
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        assertTrue(factory.isAvailable());

        assertFalse(factory.canProcess(ImmutableMap.of(
                ElasticDataStoreFactory.HOSTNAME.key, "localhost", ElasticDataStoreFactory.HOSTPORT.key, port)));
        assertFalse(factory.canProcess(ImmutableMap.of(
                ElasticDataStoreFactory.HOSTNAME.key, "localhost", ElasticDataStoreFactory.INDEX_NAME.key, "test")));
        assertFalse(factory.canProcess(ImmutableMap.of(ElasticDataStoreFactory.HOSTNAME.key, "localhost")));
        assertFalse(factory.canProcess(ImmutableMap.of(
                ElasticDataStoreFactory.HOSTPORT.key, port, ElasticDataStoreFactory.INDEX_NAME.key, "test")));
        assertFalse(factory.canProcess(ImmutableMap.of(ElasticDataStoreFactory.HOSTPORT.key, port)));
        assertFalse(factory.canProcess(ImmutableMap.of(ElasticDataStoreFactory.INDEX_NAME.key, "test")));
    }

    @Test
    public void testCreateRestClient() throws IOException {
        assertEquals(ImmutableList.of(new HttpHost("http", "localhost", port)), getHosts("localhost"));
        assertEquals(
                ImmutableList.of(new HttpHost("http", "localhost.localdomain", port)),
                getHosts("localhost.localdomain"));

        assertEquals(ImmutableList.of(new HttpHost("http", "localhost", 9201)), getHosts("localhost:9201"));
        assertEquals(
                ImmutableList.of(new HttpHost("http", "localhost.localdomain", 9201)),
                getHosts("localhost.localdomain:9201"));

        assertEquals(ImmutableList.of(new HttpHost("http", "localhost", port)), getHosts("http://localhost"));
        assertEquals(ImmutableList.of(new HttpHost("http", "localhost", 9200)), getHosts("http://localhost:9200"));
        assertEquals(ImmutableList.of(new HttpHost("http", "localhost", 9201)), getHosts("http://localhost:9201"));

        assertEquals(ImmutableList.of(new HttpHost("https", "localhost", port)), getHosts("https://localhost"));
        assertEquals(ImmutableList.of(new HttpHost("https", "localhost", 9200)), getHosts("https://localhost:9200"));
        assertEquals(ImmutableList.of(new HttpHost("https", "localhost", 9201)), getHosts("https://localhost:9201"));

        assertEquals(
                ImmutableList.of(
                        new HttpHost("http", "somehost.somedomain", port),
                        new HttpHost("http", "anotherhost.somedomain", port)),
                getHosts("somehost.somedomain:" + port + ",anotherhost.somedomain:" + port));
        assertEquals(
                ImmutableList.of(
                        new HttpHost("https", "somehost.somedomain", port),
                        new HttpHost("https", "anotherhost.somedomain", port)),
                getHosts("https://somehost.somedomain:" + port + ",https://anotherhost.somedomain:" + port));
        assertEquals(
                ImmutableList.of(
                        new HttpHost("https", "somehost.somedomain", port),
                        new HttpHost("https", "anotherhost.somedomain", port)),
                getHosts("https://somehost.somedomain:" + port + ", https://anotherhost.somedomain:" + port));
        assertEquals(
                ImmutableList.of(
                        new HttpHost("https", "somehost.somedomain", port),
                        new HttpHost("http", "anotherhost.somedomain", port)),
                getHosts("https://somehost.somedomain:" + port + ",anotherhost.somedomain:" + port));
    }

    private List<HttpHost> getHosts(String hosts) throws IOException {
        return getRestClient(hosts).getNodes().stream().map(Node::getHost).collect(Collectors.toList());
    }

    private RestClient getRestClient(String hosts) throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        params.put(ElasticDataStoreFactory.HOSTNAME.key, hosts);
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        return factory.createRestClient(params);
    }
}
