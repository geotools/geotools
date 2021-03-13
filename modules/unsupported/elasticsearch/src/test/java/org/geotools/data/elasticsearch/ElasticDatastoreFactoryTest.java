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
package org.geotools.data.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.geotools.data.DataStore;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ElasticDatastoreFactoryTest {

    private ElasticDataStoreFactory dataStoreFactory;

    private RestClientBuilder clientBuilder;

    private ArgumentCaptor<HttpHost[]> hostsCaptor;

    private ArgumentCaptor<RestClientBuilder.HttpClientConfigCallback> configCallbackCaptor;

    private ArgumentCaptor<CredentialsProvider> credentialsProviderCaptor;

    private HttpAsyncClientBuilder httpClientBuilder;

    private ArgumentCaptor<ThreadFactory> threadFactoryCaptor;

    private ArgumentCaptor<RestClientBuilder.RequestConfigCallback> requestConfigCallbackCaptor;

    private RequestConfig.Builder requestConfigBuilder;

    private Map<String, Serializable> params;

    @Before
    public void setUp() throws IOException {
        dataStoreFactory = Mockito.spy(new ElasticDataStoreFactory());
        clientBuilder = mock(RestClientBuilder.class);
        hostsCaptor = ArgumentCaptor.forClass(HttpHost[].class);
        Mockito.doReturn(clientBuilder)
                .when(dataStoreFactory)
                .createClientBuilder(hostsCaptor.capture());
        @SuppressWarnings("PMD.CloseResource")
        final RestClient restClient = mock(RestClient.class);
        when(clientBuilder.build()).thenReturn(restClient);
        final DataStore dataStore = mock(DataStore.class);
        Mockito.doReturn(dataStore)
                .when(dataStoreFactory)
                .createDataStore(any(RestClient.class), any(), anyMap());
        configCallbackCaptor =
                ArgumentCaptor.forClass(RestClientBuilder.HttpClientConfigCallback.class);
        when(clientBuilder.setHttpClientConfigCallback(configCallbackCaptor.capture()))
                .thenReturn(clientBuilder);
        httpClientBuilder = mock(HttpAsyncClientBuilder.class);
        credentialsProviderCaptor = ArgumentCaptor.forClass(CredentialsProvider.class);
        when(httpClientBuilder.setDefaultCredentialsProvider(credentialsProviderCaptor.capture()))
                .thenReturn(httpClientBuilder);
        threadFactoryCaptor = ArgumentCaptor.forClass(ThreadFactory.class);
        when(httpClientBuilder.setThreadFactory(threadFactoryCaptor.capture()))
                .thenReturn(httpClientBuilder);
        requestConfigCallbackCaptor =
                ArgumentCaptor.forClass(RestClientBuilder.RequestConfigCallback.class);
        when(clientBuilder.setRequestConfigCallback(requestConfigCallbackCaptor.capture()))
                .thenReturn(clientBuilder);
        requestConfigBuilder = mock(RequestConfig.Builder.class);
        when(requestConfigBuilder.setAuthenticationEnabled(true)).thenReturn(requestConfigBuilder);

        params = getParams("localhost", 9200, "admin", "proxy");
        ElasticDataStoreFactory.httpThreads.set(1);
    }

    private Map<String, Serializable> getParams(
            String hosts, int port, String user, String proxyUser) {
        final Map<String, Serializable> params = new HashMap<>();
        params.put(ElasticDataStoreFactory.HOSTNAME.key, hosts);
        params.put(ElasticDataStoreFactory.HOSTPORT.key, port);
        params.put(ElasticDataStoreFactory.INDEX_NAME.key, "test");
        if (user != null) {
            params.put(ElasticDataStoreFactory.USER.key, user);
            params.put(ElasticDataStoreFactory.PASSWD.key, user);
        }
        if (proxyUser != null) {
            params.put(ElasticDataStoreFactory.PROXY_USER.key, proxyUser);
            params.put(ElasticDataStoreFactory.PROXY_PASSWD.key, proxyUser);
        }
        return params;
    }

    @Test
    public void testBuildClient() throws IOException {
        assertNotNull(dataStoreFactory.createDataStore(params));
        verify(clientBuilder, times(2)).build();
        assertEquals(1, hostsCaptor.getValue().length);
        assertEquals("localhost", hostsCaptor.getValue()[0].getHostName());
        assertEquals(9200, hostsCaptor.getValue()[0].getPort());

        assertEquals(2, configCallbackCaptor.getAllValues().size());
        configCallbackCaptor.getAllValues().get(0).customizeHttpClient(httpClientBuilder);
        Credentials credentials =
                credentialsProviderCaptor
                        .getValue()
                        .getCredentials(new AuthScope("localhost", 9200));
        assertNotNull(credentials);
        assertEquals("admin", credentials.getUserPrincipal().getName());
        assertEquals("admin", credentials.getPassword());
        configCallbackCaptor.getAllValues().get(1).customizeHttpClient(httpClientBuilder);
        Credentials proxyCredentials =
                credentialsProviderCaptor
                        .getValue()
                        .getCredentials(new AuthScope("localhost", 9200));
        assertNotNull(proxyCredentials);
        assertEquals("proxy", proxyCredentials.getUserPrincipal().getName());
        assertEquals("proxy", proxyCredentials.getPassword());

        assertEquals(2, threadFactoryCaptor.getAllValues().size());
        ThreadFactory threadFactory = threadFactoryCaptor.getAllValues().get(0);
        Thread thread = threadFactory.newThread(mock(Runnable.class));
        assertEquals("esrest-asynchttp-ADMIN-1", thread.getName());
        ThreadFactory proxyThreadFactory = threadFactoryCaptor.getAllValues().get(1);
        Thread proxyThread = proxyThreadFactory.newThread(mock(Runnable.class));
        assertEquals("esrest-asynchttp-PROXY_USER-2", proxyThread.getName());

        assertNotNull(
                requestConfigCallbackCaptor
                        .getValue()
                        .customizeRequestConfig(requestConfigBuilder));
    }

    @Test
    public void testBuildClientWithoutProxy() throws IOException {
        params = getParams("localhost", 9200, "admin", null);
        assertNotNull(dataStoreFactory.createDataStore(params));
        verify(clientBuilder, times(1)).build();
        assertEquals(1, hostsCaptor.getValue().length);
        assertEquals("localhost", hostsCaptor.getValue()[0].getHostName());
        assertEquals(9200, hostsCaptor.getValue()[0].getPort());

        assertEquals(1, configCallbackCaptor.getAllValues().size());
        configCallbackCaptor.getAllValues().get(0).customizeHttpClient(httpClientBuilder);
        Credentials credentials =
                credentialsProviderCaptor
                        .getValue()
                        .getCredentials(new AuthScope("localhost", 9200));
        assertNotNull(credentials);
        assertEquals("admin", credentials.getUserPrincipal().getName());
        assertEquals("admin", credentials.getPassword());

        assertEquals(1, threadFactoryCaptor.getAllValues().size());
        ThreadFactory threadFactory = threadFactoryCaptor.getAllValues().get(0);
        Thread thread = threadFactory.newThread(mock(Runnable.class));
        assertEquals("esrest-asynchttp-ADMIN-1", thread.getName());

        assertNotNull(
                requestConfigCallbackCaptor
                        .getValue()
                        .customizeRequestConfig(requestConfigBuilder));
    }

    @Test
    public void testBuildClientWithoutAuth() throws IOException {
        params = getParams("localhost", 9200, null, null);
        assertNotNull(dataStoreFactory.createDataStore(params));
        verify(clientBuilder, times(1)).build();
        assertEquals(1, hostsCaptor.getValue().length);
        assertEquals("localhost", hostsCaptor.getValue()[0].getHostName());
        assertEquals(9200, hostsCaptor.getValue()[0].getPort());

        assertEquals(1, configCallbackCaptor.getAllValues().size());
        configCallbackCaptor.getAllValues().get(0).customizeHttpClient(httpClientBuilder);
        assertEquals(0, credentialsProviderCaptor.getAllValues().size());

        assertEquals(1, threadFactoryCaptor.getAllValues().size());
        ThreadFactory threadFactory = threadFactoryCaptor.getAllValues().get(0);
        Thread thread = threadFactory.newThread(mock(Runnable.class));
        assertEquals("esrest-asynchttp-ADMIN-1", thread.getName());
    }

    @Test
    public void testBuildClientWithMultipleHosts() throws IOException {
        params = getParams("localhost1,localhost2", 9201, "admin", "proxy");
        assertNotNull(dataStoreFactory.createDataStore(params));
        verify(clientBuilder, times(2)).build();
        assertEquals(2, hostsCaptor.getValue().length);
        assertEquals("localhost1", hostsCaptor.getValue()[0].getHostName());
        assertEquals(9201, hostsCaptor.getValue()[0].getPort());
        assertEquals("localhost2", hostsCaptor.getValue()[1].getHostName());
        assertEquals(9201, hostsCaptor.getValue()[1].getPort());
        configCallbackCaptor.getValue().customizeHttpClient(httpClientBuilder);
        assertNotNull(
                credentialsProviderCaptor
                        .getValue()
                        .getCredentials(new AuthScope("localhost1", 9201)));
        assertNotNull(
                credentialsProviderCaptor
                        .getValue()
                        .getCredentials(new AuthScope("localhost2", 9201)));
    }

    @Test
    public void testCreateClientbuilder() {
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        HttpHost[] hosts = {new HttpHost("localhost", 9200)};
        assertNotNull(factory.createClientBuilder(hosts));
    }

    @Test
    public void testBuildClientWithSslRejectUnauthorizedDisabled() throws IOException {
        params.put(ElasticDataStoreFactory.SSL_REJECT_UNAUTHORIZED.key, false);
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertTrue(configCallbackCaptor.getAllValues().size() > 0);
        configCallbackCaptor.getAllValues().get(0).customizeHttpClient(httpClientBuilder);
        verify(httpClientBuilder, times(1)).setSSLContext(any());
    }

    @Test(expected = UncheckedIOException.class)
    @Ignore
    public void testBuildClientWithSslRejectUnauthorizedDisabledAndInvalidSSLContext()
            throws IOException {
        params.put(ElasticDataStoreFactory.SSL_REJECT_UNAUTHORIZED.key, false);
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertTrue(configCallbackCaptor.getAllValues().size() > 0);
        when(httpClientBuilder.setSSLContext(any())).thenThrow(KeyStoreException.class);
        configCallbackCaptor.getAllValues().get(0).customizeHttpClient(httpClientBuilder);
    }

    @Test(expected = IOException.class)
    public void testBuildClientWithInvalidHost() throws IOException {
        params = getParams(":", 9200, null, null);
        dataStoreFactory.createDataStore(params);
    }
}
