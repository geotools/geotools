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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class RestElasticClientTest {

    private RestClient mockRestClient;

    private RestClient mockProxyRestClient;

    private Response mockResponse;

    private HttpEntity mockEntity;

    private StatusLine mockStatusLine;

    private RestElasticClient client;

    private RestElasticClient proxyClient;

    private Authentication mockAuth;

    private SecurityContext mockStx;

    @Before
    public void setup() throws UnsupportedOperationException, IOException {
        mockRestClient = mock(RestClient.class);
        mockProxyRestClient = mock(RestClient.class);
        mockResponse = mock(Response.class);
        mockEntity = mock(HttpEntity.class);
        mockStatusLine = mock(StatusLine.class);
        mockAuth = mock(Authentication.class);
        mockStx = mock(SecurityContext.class);

        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(200);

        final Response mockDefaultResponse = mock(Response.class);
        final StatusLine mockDefaultStatusLine = mock(StatusLine.class);
        when(mockDefaultResponse.getStatusLine()).thenReturn(mockDefaultStatusLine);
        when(mockDefaultStatusLine.getStatusCode()).thenReturn(500);
        when(mockRestClient.performRequest(any())).thenReturn(mockDefaultResponse);

        when(mockStx.getAuthentication()).thenReturn(mockAuth);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(mockAuth.getName()).thenReturn("runAsTest");
        SecurityContextHolder.setContext(mockStx);

        client = new RestElasticClient(mockRestClient);
        proxyClient = new RestElasticClient(mockRestClient, mockProxyRestClient, true, (Integer)
                ElasticDataStoreFactory.RESPONSE_BUFFER_LIMIT.sample);

        InputStream inputStream = new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void testVersion() throws IOException {
        mockVersion("6.7.8");
        final double version = client.getVersion();
        assertEquals(6.7, version, 1e-9);
    }

    @Test
    public void testVersionWithInvalidFormat() throws IOException {
        mockVersion("6");
        final double version = client.getVersion();
        assertEquals(RestElasticClient.DEFAULT_VERSION, version, 1e-9);
    }

    @Test
    public void testVersionWithError() throws IOException {
        String content = "{}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        final RequestMatcher matcher = new RequestMatcher("GET", "/", null, null);
        when(mockRestClient.performRequest(argThat(matcher))).thenThrow(IOException.class);

        final double version = client.getVersion();
        assertEquals(RestElasticClient.DEFAULT_VERSION, version, 1e-9);
    }

    @Test
    public void testGetTypes() throws IOException {
        String content =
                "{\"status_s\": {\"mappings\": " + "{\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);

        List<String> names = client.getTypes("status_s");
        assertEquals(1, names.size());
    }

    @Test
    public void testLegacyGetTypes() throws IOException {
        String content = "{\"status_s\": {\"mappings\": "
                + "{\"active\": {\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        mockVersion("6.0.0");
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);

        List<String> names = client.getTypes("status_s");
        assertEquals(1, names.size());
        assertEquals("active", names.get(0));
    }

    @Test
    public void testGetMapping() throws IOException {
        String content =
                "{\"status_s\": {\"mappings\":" + "{\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);

        Map<String, Map<String, String>> expected = ImmutableMap.of("status_s", ImmutableMap.of("type", "keyword"));
        assertEquals(expected, client.getMapping("status_s", "active"));
    }

    @Test
    public void testLegacyGetMapping() throws IOException {
        String content = "{\"status_s\": {\"mappings\": {\"active\": "
                + "{\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        mockVersion("6.0.0");
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping/active".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);

        Map<String, Map<String, String>> expected = ImmutableMap.of("status_s", ImmutableMap.of("type", "keyword"));
        assertEquals(expected, client.getMapping("status_s", "active"));
    }

    @Test
    public void testGetMappingWithMissingIndexAndNoAlias() throws IOException {
        String content =
                "{\"status_s3\": {\"mappings\": " + "{\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);
        when(mockRestClient.performRequest(new Request("GET", "/_alias/status_s")))
                .thenThrow(IOException.class);

        Map<String, Map<String, String>> expected = ImmutableMap.of("status_s", ImmutableMap.of("type", "keyword"));
        assertEquals(expected, client.getMapping("status_s", "active"));
    }

    @Test
    public void testGetMappingMissing() throws IOException {
        String content = "{}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);
        when(mockRestClient.performRequest(new Request("GET", "/_alias/status_s")))
                .thenThrow(IOException.class);

        assertNull(client.getMapping("status_s", "active"));
    }

    @Test
    public void testGetMappingWithExtra() throws IOException {
        String content =
                "{\"status_s\": {\"mappings\":" + "{\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest((argThat((req) -> "/status_s/_mapping".equals(req.getEndpoint())))))
                .thenReturn(mockResponse);
        Map<String, Map<String, String>> expected = ImmutableMap.of("status_s", ImmutableMap.of("type", "keyword"));
        assertEquals(expected, client.getMapping("status_s", "active"));
    }

    @Test
    public void testGetMappingNotFound() throws IOException {
        ResponseException mockException = mock(ResponseException.class);
        when(mockException.getResponse()).thenReturn(mockResponse);
        when(mockStatusLine.getStatusCode()).thenReturn(404);

        when(mockRestClient.performRequest(any(Request.class))).thenThrow(mockException);
        assertNull(client.getMapping("status_s", "active"));
    }

    @Test(expected = ResponseException.class)
    public void testGetMappingWithError() throws IOException {
        ResponseException mockException = mock(ResponseException.class);
        when(mockException.getResponse()).thenReturn(mockResponse);
        when(mockStatusLine.getStatusCode()).thenReturn(400);

        when(mockRestClient.performRequest(any(Request.class))).thenThrow(mockException);
        client.getMapping("status_s", "active");
    }

    @Test
    public void testSearchSize() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search", "{\"size\":10}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setSize(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testLegacySearch() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("/status_s/active/_search", "{\"size\":10}");
        mockVersion("6.0.0");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setSize(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSizeWithProxyClient() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("POST", "/status_s/_search", "{\"size\":10}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setSize(10);
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchFrom() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search", "{\"from\":10}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setFrom(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchFromWithProxyClient() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("POST", "/status_s/_search", "{\"from\":10}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setFrom(10);
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchScroll() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search?scroll=10s", "{}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setScroll(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchScrollWithProxyClient() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("POST", "/status_s/_search?scroll=10s", "{}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setScroll(10);
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchSourceFiltering() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search", "{\"_source\":\"obj1\"}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("obj1");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSourceFilteringWithProxyClient() throws IOException {
        final RequestMatcher matcher =
                new RequestMatcher("POST", "/status_s/_search", "{\"_source\":\"obj1\"}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("obj1");
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchSourceFiltering2() throws IOException {
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search", "{\"_source\":[\"obj1\",\"obj2\"]}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("obj1");
        request.addSourceInclude("obj2");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSourceFilteringWithProxyClient2() throws IOException {
        final RequestMatcher matcher =
                new RequestMatcher("POST", "/status_s/_search", "{\"_source\":[\"obj1\",\"obj2\"]}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("obj1");
        request.addSourceInclude("obj2");
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchStoredFields() throws IOException {
        mockVersion("2.4.4");

        final RequestMatcher matcher2 = new RequestMatcher("/status_s/active/_search", "{\"fields\":[\"obj1\"]}");
        doReturn(mockResponse).when(mockRestClient).performRequest(argThat(matcher2));

        ElasticRequest request = new ElasticRequest();
        request.addField("obj1");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchStoredFieldsWithProxyClient() throws IOException {
        mockVersion("2.4.4");

        final RequestMatcher matcher2 =
                new RequestMatcher("POST", "/status_s/active/_search", "{\"fields\":[\"obj1\"]}", "runAsTest");
        doReturn(mockResponse).when(mockProxyRestClient).performRequest(argThat(matcher2));

        ElasticRequest request = new ElasticRequest();
        request.addField("obj1");
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchSort() throws IOException {
        final RequestMatcher matcher =
                new RequestMatcher("/status_s/_search", "{\"sort\":[{\"obj1\":{\"order\":\"asc\"}}]}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSort("obj1", "asc");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSortWithProxyClient() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "POST", "/status_s/_search", "{\"sort\":[{\"obj1\":{\"order\":\"asc\"}}]}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSort("obj1", "asc");
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchSort2() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "/status_s/_search", "{\"sort\":[{\"obj1\":{\"order\":\"asc\"}},{\"obj2\":{\"order\":\"desc\"}}]}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSort("obj1", "asc");
        request.addSort("obj2", "desc");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSortWithProxyClient2() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "POST",
                "/status_s/_search",
                "{\"sort\":[{\"obj1\":{\"order\":\"asc\"}},{\"obj2\":{\"order\":\"desc\"}}]}",
                "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSort("obj1", "asc");
        request.addSort("obj2", "desc");
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testSearchResponse() throws IOException {
        String content =
                "{\"hits\": {\"total\": 10, \"max_score\": 0.8, \"hits\": [{\"_index\": \"index_name\"}, {}]}}";
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search", "{}");
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        final ElasticResponse response = client.search("status_s", "active", new ElasticRequest());
        assertEquals(Long.valueOf(10), response.getResults().getTotal());
        assertEquals("index_name", response.getHits().get(0).getIndex());
        assertEquals(0.8f, response.getResults().getMaxScore(), 1e-9);
    }

    @Test
    public void testSearchResponseWithProxyClient() throws IOException {
        String content =
                "{\"hits\": {\"total\": 10, \"max_score\": 0.8, \"hits\": [{\"_index\": \"index_name\"}, {}]}}";
        final RequestMatcher matcher = new RequestMatcher("POST", "/status_s/_search", "{}", "runAsTest");
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        final ElasticResponse response = proxyClient.search("status_s", "active", new ElasticRequest());
        assertEquals(Long.valueOf(10), response.getResults().getTotal());
        assertEquals("index_name", response.getHits().get(0).getIndex());
        assertEquals(0.8f, response.getResults().getMaxScore(), 1e-9);
    }

    @Test
    public void testQuery() throws IOException {
        final Map<String, Object> query = ImmutableMap.of("term", ImmutableMap.of("obj1", "value1"));
        final String data = new ObjectMapper().writeValueAsString(ImmutableMap.of("query", query));
        final RequestMatcher matcher = new RequestMatcher("/status_s/_search", data);
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setQuery(query);
        client.search("status_s", "active", request);
    }

    @Test
    public void testLegacyQuery() throws IOException {
        final Map<String, Object> query = ImmutableMap.of("term", ImmutableMap.of("obj1", "value1"));
        final String data = new ObjectMapper().writeValueAsString(ImmutableMap.of("query", query));
        final RequestMatcher matcher = new RequestMatcher("/status_s/active/_search", data);
        mockVersion("6.0.0");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setQuery(query);
        client.search("status_s", "active", request);
    }

    @Test
    public void testQueryWithProxyClient() throws IOException {
        final Map<String, Object> query = ImmutableMap.of("term", ImmutableMap.of("obj1", "value1"));
        final String data = new ObjectMapper().writeValueAsString(ImmutableMap.of("query", query));
        final RequestMatcher matcher = new RequestMatcher("POST", "/status_s/_search", data, "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setQuery(query);
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testLegacyQueryWithProxyClient() throws IOException {
        final Map<String, Object> query = ImmutableMap.of("term", ImmutableMap.of("obj1", "value1"));
        final String data = new ObjectMapper().writeValueAsString(ImmutableMap.of("query", query));
        final RequestMatcher matcher = new RequestMatcher("POST", "/status_s/active/_search", data, "runAsTest");
        mockVersion("6.0.0");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setQuery(query);
        proxyClient.search("status_s", "active", request);
    }

    @Test
    public void testAggregation() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "/status_s/_search",
                "{\"aggregations\":{\"ageohash_grid_agg\":{\"geohash_grid\": {\"field\":\"a_field\",\"precision\":1}}}}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setAggregations(ImmutableMap.of(
                "ageohash_grid_agg",
                ImmutableMap.of("geohash_grid", ImmutableMap.of("field", "a_field", "precision", 1))));
        client.search("status_s", "active", request);
    }

    @Test
    public void testLegacyAggregation() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "/status_s/active/_search",
                "{\"aggregations\":{\"ageohash_grid_agg\":{\"geohash_grid\": {\"field\":\"a_field\",\"precision\":1}}}}");
        mockVersion("6.0.0");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setAggregations(ImmutableMap.of(
                "ageohash_grid_agg",
                ImmutableMap.of("geohash_grid", ImmutableMap.of("field", "a_field", "precision", 1))));
        client.search("status_s", "active", request);
    }

    @Test
    public void testAggregationWithProxyClient() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "POST",
                "/status_s/_search",
                "{\"aggregations\":{\"ageohash_grid_agg\":{\"geohash_grid\": {\"field\":\"a_field\",\"precision\":1}}}}",
                "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setAggregations(ImmutableMap.of(
                "ageohash_grid_agg",
                ImmutableMap.of("geohash_grid", ImmutableMap.of("field", "a_field", "precision", 1))));
        proxyClient.search("status_s", "active", request);
    }

    @Test(expected = IOException.class)
    public void testBadStatus() throws IOException {
        when(mockStatusLine.getStatusCode()).thenReturn(404);
        when(mockRestClient.performRequest(any(Request.class))).thenReturn(mockResponse);

        client.search("status_s", "active", new ElasticRequest());
    }

    @Test
    public void testNextScroll() throws IOException {
        final RequestMatcher matcher =
                new RequestMatcher("/_search/scroll", "{\"scroll_id\":\"id1\",\"scroll\":\"10s\"}");
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        client.scroll("id1", 10);
    }

    @Test
    public void testNextScrollWithProxyClient() throws IOException {
        final RequestMatcher matcher = new RequestMatcher(
                "POST", "/_search/scroll", "{\"scroll_id\":\"id1\",\"scroll\":\"10s\"}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        proxyClient.scroll("id1", 10);
    }

    @Test
    public void testClearScroll() throws IOException {
        final RequestMatcher matcher =
                new RequestMatcher("DELETE", "/_search/scroll", "{\"scroll_id\":[\"id1\"]}", null);
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        client.clearScroll(ImmutableSet.of("id1"));
    }

    @Test
    public void testClearScrollWithProxyClient() throws IOException {
        final RequestMatcher matcher =
                new RequestMatcher("DELETE", "/_search/scroll", "{\"scroll_id\":[\"id1\"]}", "runAsTest");
        when(mockProxyRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);

        proxyClient.clearScroll(ImmutableSet.of("id1"));
    }

    @Test
    public void testClose() throws IOException {
        client.close();
        verify(mockRestClient).close();
    }

    @Test
    public void testCloseWithProxyClient() throws IOException {
        proxyClient.close();
        verify(mockRestClient).close();
        verify(mockProxyRestClient).close();
    }

    @Test
    public void testRemoveMapping() {
        Map<String, Object> data = createMap("key", "value");
        RestElasticClient.removeMapping(null, "key", data, null);
        assertTrue(data.isEmpty());

        data = createMap("key", "value", "parent", ImmutableMap.of("key", "value"));
        RestElasticClient.removeMapping(null, "key", data, null);
        assertEquals(data, ImmutableMap.of("parent", ImmutableMap.of("key", "value")));

        data = ImmutableMap.of("key", "value", "parent", createMap("key", 10));
        RestElasticClient.removeMapping("parent", "key", data, null);
        assertEquals(data, ImmutableMap.of("key", "value", "parent", new HashMap<>()));

        data = new HashMap<>();
        RestElasticClient.removeMapping(null, "key", data, null);
        assertTrue(data.isEmpty());

        data = ImmutableMap.of("parent", new HashMap<>());
        RestElasticClient.removeMapping("parent", "key", data, null);
        assertEquals(data, ImmutableMap.of("parent", new HashMap<>()));

        data = createMap(
                "key",
                ImmutableList.of(
                        ImmutableMap.of("key", "value"),
                        ImmutableMap.of("key", "value", "parent", createMap("key", "value", "key2", "value2"))));
        RestElasticClient.removeMapping("parent", "key", data, null);
        assertEquals(
                data,
                createMap(
                        "key",
                        ImmutableList.of(
                                ImmutableMap.of("key", "value"),
                                ImmutableMap.of("key", "value", "parent", createMap("key2", "value2")))));
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingAuth() throws IOException {
        when(mockStx.getAuthentication()).thenReturn(null);
        proxyClient.search("status_s", "active", new ElasticRequest());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnauthenticated() throws IOException {
        when(mockAuth.isAuthenticated()).thenReturn(false);
        proxyClient.search("status_s", "active", new ElasticRequest());
    }

    private void mockVersion(String version) throws IOException {
        final Response mockResponse = mock(Response.class);
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final StatusLine mockStatusLine = mock(StatusLine.class);

        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(200);

        String content = "{\"version\":{\"number\":\"" + version + "\"}}";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        when(mockEntity.getContent()).thenReturn(inputStream);
        final RequestMatcher matcher = new RequestMatcher("GET", "/", null, null);
        when(mockRestClient.performRequest(argThat(matcher))).thenReturn(mockResponse);
    }

    private Map<String, Object> createMap(Object... params) {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < params.length - 1; i += 2) {
            data.put((String) params[i], params[i + 1]);
        }
        return data;
    }

    static class RunAsHeaderMatcher implements ArgumentMatcher<BasicHeader> {

        private static final String RUN_AS_HEADER_FIELD = "es-security-runas-user";

        private final String runAsUser;

        RunAsHeaderMatcher(String runAsUser) {
            this.runAsUser = runAsUser;
        }

        @Override
        public boolean matches(BasicHeader header) {
            return RUN_AS_HEADER_FIELD.equals(header.getName()) && runAsUser.equals(header.getValue());
        }
    }

    static class JsonByteArrayEntityMatcher implements ArgumentMatcher<ByteArrayEntity> {

        private Map<String, Object> data;

        JsonByteArrayEntityMatcher(byte[] data) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            this.data = mapper.readValue(data, new TypeReference<>() {});
        }

        @Override
        public boolean matches(ByteArrayEntity argument) {
            ByteArrayInputStream inputStream = (ByteArrayInputStream) argument.getContent();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> data = mapper.readValue(inputStream, new TypeReference<>() {});
                return this.data.equals(data);
            } catch (JacksonException e) {
                return false;
            }
        }
    }

    static class RequestMatcher implements ArgumentMatcher<Request> {

        private final String method;

        private final String endpoint;

        private JsonByteArrayEntityMatcher entityMatcher;

        private RunAsHeaderMatcher headerMatcher;

        RequestMatcher(String endpoint, String data) throws IOException {
            this("POST", endpoint, data, null);
        }

        RequestMatcher(String method, String endpoint, String data, String user) throws IOException {
            this.method = method;
            this.endpoint = endpoint;
            this.entityMatcher =
                    data != null ? new JsonByteArrayEntityMatcher(data.getBytes(StandardCharsets.UTF_8)) : null;
            this.headerMatcher = user != null ? new RunAsHeaderMatcher(user) : null;
        }

        @Override
        public boolean matches(Request argument) {
            if (argument == null) {
                return false;
            }
            final List<Header> headers = argument.getOptions().getHeaders();
            if (!this.method.equals(argument.getMethod())) {
                return false;
            }
            if (!this.endpoint.equals(argument.getEndpoint())) {
                return false;
            }
            if (this.entityMatcher == null && argument.getEntity() != null) {
                return false;
            }
            if (this.entityMatcher != null && !this.entityMatcher.matches((ByteArrayEntity) argument.getEntity())) {
                return false;
            }
            if (this.headerMatcher != null && headers.isEmpty()) {
                return false;
            }
            if (this.headerMatcher == null && !headers.isEmpty()) {
                return false;
            }
            return this.headerMatcher == null || headerMatcher.matches((BasicHeader) headers.get(0));
        }
    }
}
