/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.geotools.data.DataStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsernamePasswordElasticDatastoreFactoryTest {

    private UsernamePasswordElasticDataStoreFactory dataStoreFactory;

    private RestClientBuilder clientBuilder;

    private RestClient restClient;

    private StatusLine statusLine;

    private ArgumentCaptor<HttpHost[]> hostsCaptor;

    private ArgumentCaptor<RestClientBuilder.HttpClientConfigCallback> configCallbackCaptor;

    private ArgumentCaptor<CredentialsProvider> credentialsProviderCaptor;

    private HttpAsyncClientBuilder httpClientBuilder;

    private ArgumentCaptor<ThreadFactory> threadFactoryCaptor;

    private ArgumentCaptor<RestClientBuilder.RequestConfigCallback> requestConfigCallbackCaptor;

    private RequestConfig.Builder requestConfigBuilder;

    private Map<String,Serializable> params;

    @Before
    public void setUp() throws IOException {
        dataStoreFactory = Mockito.spy(new UsernamePasswordElasticDataStoreFactory());
        dataStoreFactory.getClients().clear();
        clientBuilder = mock(RestClientBuilder.class);
        hostsCaptor = ArgumentCaptor.forClass(HttpHost[].class);
        Mockito.doReturn(clientBuilder).when(dataStoreFactory).createClientBuilder(hostsCaptor.capture());
        restClient = mock(RestClient.class);
        when(clientBuilder.build()).thenReturn(restClient);
        final Response response = mock(Response.class);
        when(restClient.performRequest(anyString(), anyString(), anyMap())).thenReturn(response);
        statusLine = mock(StatusLine.class);
        when(response.getStatusLine()).thenReturn(statusLine);
        final DataStore dataStore = mock(DataStore.class);
        Mockito.doReturn(dataStore).when(dataStoreFactory).createDataStore(any(RestClient.class), any(RestClient.class), anyMap());
        configCallbackCaptor = ArgumentCaptor.forClass(RestClientBuilder.HttpClientConfigCallback.class);
        when(clientBuilder.setHttpClientConfigCallback(configCallbackCaptor.capture())).thenReturn(clientBuilder);
        httpClientBuilder = mock(HttpAsyncClientBuilder.class);
        credentialsProviderCaptor = ArgumentCaptor.forClass(CredentialsProvider.class);
        when(httpClientBuilder.setDefaultCredentialsProvider(credentialsProviderCaptor.capture())).thenReturn(httpClientBuilder);
        threadFactoryCaptor = ArgumentCaptor.forClass(ThreadFactory.class);
        when(httpClientBuilder.setThreadFactory(threadFactoryCaptor.capture())).thenReturn(httpClientBuilder);
        requestConfigCallbackCaptor = ArgumentCaptor.forClass(RestClientBuilder.RequestConfigCallback.class);
        when(clientBuilder.setRequestConfigCallback(requestConfigCallbackCaptor.capture())).thenReturn(clientBuilder);
        requestConfigBuilder = mock(RequestConfig.Builder.class);
        when(requestConfigBuilder.setAuthenticationEnabled(true)).thenReturn(requestConfigBuilder);

        params = getParams("localhost", 9200, "admin", "proxy");
    }

    private Map<String, Serializable> getParams(String hosts, int port, String adminUser, String proxyUser) {
        final Map<String,Serializable> params = new HashMap<>();
        params.put(UsernamePasswordElasticDataStoreFactory.HOSTNAME.key, hosts);
        params.put(UsernamePasswordElasticDataStoreFactory.HOSTPORT.key, port);
        params.put(UsernamePasswordElasticDataStoreFactory.INDEX_NAME.key, "test");
        params.put(UsernamePasswordElasticDataStoreFactory.ADMIN_USER.key, adminUser);
        params.put(UsernamePasswordElasticDataStoreFactory.ADMIN_PASSWD.key, adminUser);
        params.put(UsernamePasswordElasticDataStoreFactory.PROXY_USER.key, proxyUser);
        params.put(UsernamePasswordElasticDataStoreFactory.PROXY_PASSWD.key, proxyUser);
        return params;
    }

    @Test
    public void testFactoryDefaults() {
        UsernamePasswordElasticDataStoreFactory fac = new UsernamePasswordElasticDataStoreFactory();

        assertTrue(fac.getDisplayName().equals(UsernamePasswordElasticDataStoreFactory.DISPLAY_NAME));
        assertTrue(fac.getDescription().equals(UsernamePasswordElasticDataStoreFactory.DESCRIPTION));
        assertTrue(fac.getParametersInfo().equals(UsernamePasswordElasticDataStoreFactory.PARAMS));
        assertTrue(fac.getImplementationHints()==null);
    }

    @Test
    public void testBuildClient() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertEquals(2, dataStoreFactory.getClients().size());
        verify(clientBuilder, times(2)).build();
        assertEquals(1, hostsCaptor.getValue().length);
        assertEquals("localhost", hostsCaptor.getValue()[0].getHostName());
        assertEquals(9200, hostsCaptor.getValue()[0].getPort());

        assertEquals(2, configCallbackCaptor.getAllValues().size());
        configCallbackCaptor.getAllValues().get(0).customizeHttpClient(httpClientBuilder);
        Credentials adminCredentials = credentialsProviderCaptor.getValue().getCredentials(new AuthScope("localhost", 9200));
        assertNotNull(adminCredentials);
        assertEquals("admin", adminCredentials.getUserPrincipal().getName());
        assertEquals("admin", adminCredentials.getPassword());
        configCallbackCaptor.getAllValues().get(1).customizeHttpClient(httpClientBuilder);
        Credentials proxyCredentials = credentialsProviderCaptor.getValue().getCredentials(new AuthScope("localhost", 9200));
        assertNotNull(proxyCredentials);
        assertEquals("proxy", proxyCredentials.getUserPrincipal().getName());
        assertEquals("proxy", proxyCredentials.getPassword());

        assertEquals(2, threadFactoryCaptor.getAllValues().size());
        ThreadFactory adminThreadFactory = threadFactoryCaptor.getAllValues().get(0);
        Thread adminThread = adminThreadFactory.newThread(mock(Runnable.class));
        assertEquals("esrest-asynchttp-ADMIN-1", adminThread.getName());
        ThreadFactory proxyThreadFactory = threadFactoryCaptor.getAllValues().get(1);
        Thread proxyThread = proxyThreadFactory.newThread(mock(Runnable.class));
        assertEquals("esrest-asynchttp-PROXY-2", proxyThread.getName());

        assertNotNull(requestConfigCallbackCaptor.getValue().customizeRequestConfig(requestConfigBuilder));
    }

    @Test
    public void testBuildClientWithMultipleHosts() throws IOException {
        params = getParams("localhost1,localhost2", 9201, "admin", "proxy");
        when(statusLine.getStatusCode()).thenReturn(200);
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertEquals(2, dataStoreFactory.getClients().size());
        verify(clientBuilder, times(2)).build();
        assertEquals(2, hostsCaptor.getValue().length);
        assertEquals("localhost1", hostsCaptor.getValue()[0].getHostName());
        assertEquals(9201, hostsCaptor.getValue()[0].getPort());
        assertEquals("localhost2", hostsCaptor.getValue()[1].getHostName());
        assertEquals(9201, hostsCaptor.getValue()[1].getPort());
        configCallbackCaptor.getValue().customizeHttpClient(httpClientBuilder);
        assertNotNull(credentialsProviderCaptor.getValue().getCredentials(new AuthScope("localhost1", 9201)));
        assertNotNull(credentialsProviderCaptor.getValue().getCredentials(new AuthScope("localhost2", 9201)));
    }

    @Test
    public void testBuildClientWithRetry() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(400).thenReturn(200);
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertEquals(2, dataStoreFactory.getClients().size());
        verify(clientBuilder, times(3)).build();
        verify(restClient, times(1)).close();
    }

    @Test
    public void testBuildClientFailure() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(400);
        Mockito.doAnswer(new Answer() {
            private int count = 0;

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                count++;
                if (count > 1) {
                    throw new IOException("close fail");
                }
                return null;
            }
        }).when(restClient).close();
        try {
            dataStoreFactory.createDataStore(params);
            fail();
        } catch (IOException e) {
            assertEquals(0, dataStoreFactory.getClients().size());
            // admin client with retry
            verify(clientBuilder, times(2)).build();
            verify(restClient, times(2)).close();
        }
    }

    @Test
    public void testGetCachedClient() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertNotNull(dataStoreFactory.createDataStore(params));
        assertEquals(2, dataStoreFactory.getClients().size());
        verify(clientBuilder, times(2)).build();
    }

    @Test
    public void testDigestKey() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        assertNotNull(dataStoreFactory.createDataStore(getParams("localhost", 9200, "admin", "proxy")));
        assertNotNull(dataStoreFactory.createDataStore(getParams("localhost", 9201, "admin", "proxy")));
        assertNotNull(dataStoreFactory.createDataStore(getParams("localhost", 9200, "admin1", "proxy")));
        assertNotNull(dataStoreFactory.createDataStore(getParams("localhost", 9200, "admin", "proxy1")));
        assertEquals(8, dataStoreFactory.getClients().size());
        verify(clientBuilder, times(8)).build();
    }

    @Test
    public void testCreateClientbuilder() {
        UsernamePasswordElasticDataStoreFactory factory = new UsernamePasswordElasticDataStoreFactory();
        HttpHost[] hosts = new HttpHost[] { new HttpHost("localhost", 9200)};
        assertNotNull(factory.createClientBuilder(hosts));
    }
}