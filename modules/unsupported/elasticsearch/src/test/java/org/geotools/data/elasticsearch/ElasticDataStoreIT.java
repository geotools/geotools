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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.geotools.data.DataStore;
import org.geotools.data.store.ContentFeatureSource;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

public class ElasticDataStoreIT extends ElasticTestSupport {

    @Test
    public void testConstructionWithHostAndPortAndIndex() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        String host = ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.HOSTNAME, params);
        Integer port = ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.HOSTPORT, params);
        String indexName =
                ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.INDEX_NAME, params);

        DataStore dataStore = new ElasticDataStore(host, port, indexName);
        String[] typeNames = dataStore.getTypeNames();
        assertTrue(typeNames.length > 0);
    }

    @Test
    public void testConstructionWithClientAndIndex() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        String host = ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.HOSTNAME, params);
        Integer port = ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.HOSTPORT, params);
        String indexName =
                ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.INDEX_NAME, params);

        HttpHost httpHost = new HttpHost(host, port, "http");
        try (RestClient client = RestClient.builder(httpHost).build()) {

            DataStore dataStore = new ElasticDataStore(client, indexName);
            String[] typeNames = dataStore.getTypeNames();
            assertTrue(typeNames.length > 0);
        }
    }

    @Test
    public void testConstructionWithProxyClientAndIndex() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        String host = ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.HOSTNAME, params);
        Integer port = ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.HOSTPORT, params);
        String indexName =
                ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.INDEX_NAME, params);

        HttpHost httpHost = new HttpHost(host, port, "http");
        try (RestClient client = RestClient.builder(httpHost).build()) {

            DataStore dataStore =
                    new ElasticDataStore(
                            client,
                            client,
                            indexName,
                            false,
                            (Integer) ElasticDataStoreFactory.RESPONSE_BUFFER_LIMIT.sample);
            String[] typeNames = dataStore.getTypeNames();
            assertTrue(typeNames.length > 0);
        }
    }

    @Test(expected = IOException.class)
    @SuppressWarnings("PMD.CloseResource") // all mocks
    public void testConstructionWithBadClient() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        String indexName =
                ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.INDEX_NAME, params);

        RestClient mockClient = mock(RestClient.class);
        Response mockResponse = mock(Response.class);
        HttpEntity mockEntity = mock(HttpEntity.class);
        StatusLine mockStatusLine = mock(StatusLine.class);
        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(400);
        when(mockClient.performRequest(any(Request.class))).thenReturn(mockResponse);

        new ElasticDataStore(mockClient, indexName);
    }

    @Test(expected = IOException.class)
    @SuppressWarnings("PMD.CloseResource") // all mocks
    public void testConstructionWithBadProxyClient() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        String indexName =
                ElasticDataStoreFactory.getValue(ElasticDataStoreFactory.INDEX_NAME, params);

        RestClient mockClient = mock(RestClient.class);
        Response mockResponse = mock(Response.class);
        HttpEntity mockEntity = mock(HttpEntity.class);
        StatusLine mockStatusLine = mock(StatusLine.class);
        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockClient.performRequest(any(Request.class))).thenReturn(mockResponse);

        final AtomicInteger count = new AtomicInteger(0);
        when(mockStatusLine.getStatusCode())
                .thenAnswer((invocation) -> count.getAndIncrement() == 0 ? 200 : 400);
        new ElasticDataStore(
                mockClient,
                mockClient,
                indexName,
                false,
                (Integer) ElasticDataStoreFactory.RESPONSE_BUFFER_LIMIT.sample);
    }

    @Test
    public void testGetNames() throws IOException {
        Map<String, Serializable> params = createConnectionParams();

        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        DataStore dataStore = factory.createDataStore(params);
        String[] typeNames = dataStore.getTypeNames();
        assertTrue(typeNames.length > 0);
    }

    @Test
    public void testGetNamesByAlias() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        params.put(ElasticDataStoreFactory.INDEX_NAME.key, indexName + "_alias");

        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        DataStore dataStore = factory.createDataStore(params);
        String[] typeNames = dataStore.getTypeNames();
        assertTrue(typeNames.length > 0);
    }

    @Test
    public void testLayerConfigClone() {
        ElasticLayerConfiguration layerConfig = new ElasticLayerConfiguration("d");
        layerConfig.setLayerName("ln");
        layerConfig.getAttributes().add(new ElasticAttribute("a1"));

        ElasticLayerConfiguration layerConfig2 = new ElasticLayerConfiguration(layerConfig);
        assertEquals(layerConfig.getDocType(), layerConfig2.getDocType());
        assertEquals(layerConfig.getLayerName(), layerConfig2.getLayerName());
        assertEquals(layerConfig.getAttributes(), layerConfig2.getAttributes());
    }

    @Test
    public void testSchema() throws IOException {
        Map<String, Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        ElasticDataStore dataStore = (ElasticDataStore) factory.createDataStore(params);
        ContentFeatureSource featureSource =
                dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
        SimpleFeatureType schema = featureSource.getSchema();
        assertTrue(schema.getAttributeCount() > 0);
        assertNotNull(schema.getDescriptor("speed_is"));
    }

    @Test
    public void testSchemaWithValidCustomName() throws Exception {
        init();
        Map<String, Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        ElasticDataStore dataStore = (ElasticDataStore) factory.createDataStore(params);
        ElasticLayerConfiguration config2 = new ElasticLayerConfiguration(config);
        config2.setLayerName("fake");
        dataStore.setLayerConfiguration(config2);
        ContentFeatureSource featureSource = dataStore.getFeatureSource("fake");
        SimpleFeatureType schema = featureSource.getSchema();
        assertTrue(schema.getAttributeCount() > 0);
        assertNotNull(schema.getDescriptor("speed_is"));
    }

    @Test
    public void testIsAnalyzed() {
        assertFalse(ElasticDataStore.isAnalyzed(new HashMap<>()));
        assertFalse(ElasticDataStore.isAnalyzed(ImmutableMap.of("type", "keyword")));
        assertFalse(
                ElasticDataStore.isAnalyzed(
                        ImmutableMap.of("type", ImmutableMap.of("type", "keyword"))));
        assertFalse(ElasticDataStore.isAnalyzed(ImmutableMap.of("type", "not_valid")));
        assertTrue(ElasticDataStore.isAnalyzed(ImmutableMap.of("type", "text")));
    }
}
