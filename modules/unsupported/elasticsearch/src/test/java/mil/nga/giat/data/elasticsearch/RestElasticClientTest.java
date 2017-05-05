/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.entity.ByteArrayEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class RestElasticClientTest {

    private RestClient mockRestClient;

    private Response mockResponse;

    private HttpEntity mockEntity;

    private StatusLine mockStatusLine;

    private RestElasticClient client;

    @Before
    public void setup() throws UnsupportedOperationException, IOException {
        mockRestClient = mock(RestClient.class);
        mockResponse = mock(Response.class);
        mockEntity = mock(HttpEntity.class);
        mockStatusLine = mock(StatusLine.class);
        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(200);
        client = new RestElasticClient(mockRestClient);

        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
        when(mockEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void testGetTypes() throws IOException {
        byte[] data = "{\"status_s\": {\"mappings\": {\"active\": {\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}}".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest("GET", "/status_s/_mapping")).thenReturn(mockResponse);

        List<String> names = client.getTypes("status_s");
        assertEquals(1, names.size());
        assertEquals("active", names.get(0));
    }

    @Test
    public void testGetMapping() throws IOException {
        byte[] data = "{\"status_s\": {\"mappings\": {\"active\": {\"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}}".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest("GET", "/status_s/_mapping/active")).thenReturn(mockResponse);

        Map<String, Map<String, String>> expected = ImmutableMap.of("status_s", ImmutableMap.of("type","keyword"));
        assertEquals(expected, client.getMapping("status_s", "active"));
    }

    @Test
    public void testGetMappingWithExtra() throws IOException {
        byte[] data = "{\"status_s\": {\"mappings\": {\"active\": {\"extra\":\"value\", \"properties\": {\"status_s\": {\"type\": \"keyword\"}}}}}}".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest("GET", "/status_s/_mapping/active")).thenReturn(mockResponse);

        Map<String, Map<String, String>> expected = ImmutableMap.of("status_s", ImmutableMap.of("type","keyword"));
        assertEquals(expected, client.getMapping("status_s", "active"));
    }

    @Test
    public void testSearchSize() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"size\":10}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setSize(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchFrom() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"from\":10}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setFrom(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchScroll() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search?scroll=10s"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setScroll(10);
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSourceFiltering() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"_source\":\"obj1\"}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("obj1");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSourceFiltering2() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"_source\":[\"obj1\",\"obj2\"]}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("obj1");
        request.addSourceInclude("obj2");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchStoredFields() throws IOException {
        final Response mockResponse2 = mock(Response.class);
        final HttpEntity mockEntity2 = mock(HttpEntity.class);
        when(mockResponse2.getEntity()).thenReturn(mockEntity2);
        when(mockResponse2.getStatusLine()).thenReturn(mockStatusLine);
        byte[] data = "{\"version\": {\"number\": \"2.4.4\"}}".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(data);
        when(mockEntity2.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest(eq("GET"), eq("/"), anyMap(), any(HttpEntity.class))).thenReturn(mockResponse2);
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"fields\":[\"obj1\"]}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addField("obj1");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSort() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"sort\":[{\"obj1\":{\"order\":\"asc\"}}]}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSort("obj1", "asc");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchSort2() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"sort\":[{\"obj1\":{\"order\":\"asc\"}},{\"obj2\":{\"order\":\"desc\"}}]}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.addSort("obj1", "asc");
        request.addSort("obj2", "desc");
        client.search("status_s", "active", request);
    }

    @Test
    public void testSearchResponse() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("{\"hits\": {\"total\": 10, \"max_score\": 0.8, \"hits\": [{\"_index\": \"index_name\"}, {}]}}".getBytes());
        when(mockEntity.getContent()).thenReturn(inputStream);
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), any(HttpEntity.class))).thenReturn(mockResponse);
        client.search("status_s", "active", new ElasticRequest());
    }

    @Test
    public void testQuery() throws IOException {
        final Map<String,Object> query = ImmutableMap.of("term", ImmutableMap.of("obj1", "value1"));
        final byte[] data = new ObjectMapper().writeValueAsBytes(ImmutableMap.of("query", query));
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher(data);
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setQuery(query);
        client.search("status_s", "active", request);
    }

    @Test
    public void testAggregation() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"aggregations\":{\"ageohash_grid_agg\":{\"geohash_grid\": {\"field\":\"a_field\",\"precision\":1}}}}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        ElasticRequest request = new ElasticRequest();
        request.setAggregations(ImmutableMap.of("ageohash_grid_agg", ImmutableMap.of("geohash_grid", ImmutableMap.of("field","a_field","precision",1))));
        client.search("status_s", "active", request);
    }

    @Test(expected=IOException.class)
    public void testBadStatus() throws IOException {
        when(mockStatusLine.getStatusCode()).thenReturn(404);
        when(mockRestClient.performRequest(eq("POST"), eq("/status_s/active/_search"), anyMap(), any(HttpEntity.class))).thenReturn(mockResponse);

        client.search("status_s", "active", new ElasticRequest());
    }

    @Test
    public void testNextScroll() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"scroll_id\":\"id1\",\"scroll\":\"10s\"}".getBytes());
        when(mockRestClient.performRequest(eq("POST"), eq("/_search/scroll"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        client.scroll("id1", 10);
    }

    @Test
    public void testClearScroll() throws IOException {
        ArgumentMatcher<ByteArrayEntity> matcher = new JsonByteArrayEntityMatcher("{\"scroll_id\":[\"id1\"]}".getBytes());
        when(mockRestClient.performRequest(eq("DELETE"), eq("/_search/scroll"), anyMap(), argThat(matcher))).thenReturn(mockResponse);

        client.clearScroll(ImmutableSet.of("id1"));
    }

    @Test
    public void testClose() throws IOException {
        client.close();
        verify(mockRestClient).close();
    }

    @Test
    public void testRemoveMapping() {
        Map<String,Object> data = createMap("key", "value");
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

        data = createMap("key", ImmutableList.of(ImmutableMap.of("key", "value"), ImmutableMap.of("key","value","parent", createMap("key","value","key2","value2"))));
        RestElasticClient.removeMapping("parent", "key", data, null);
        assertEquals(data, createMap("key", ImmutableList.of(ImmutableMap.of("key", "value"), ImmutableMap.of("key","value","parent", createMap("key2","value2")))));
    }

    private Map<String,Object> createMap(Object... params) {
        Map<String,Object> data = new HashMap<>();
        for (int i=0; i<params.length-1; i+=2) {
            data.put((String) params[i], params[i+1]);
        }
        return data;
    }

    public class JsonByteArrayEntityMatcher implements ArgumentMatcher<ByteArrayEntity> {

        private Map<String,Object> data;

        public JsonByteArrayEntityMatcher(byte[] data) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            this.data = mapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        }

        @Override
        public boolean matches(ByteArrayEntity argument) {
            ByteArrayInputStream inputStream = (ByteArrayInputStream) ((ByteArrayEntity) argument).getContent();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String,Object> data = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
                return this.data.equals(data);
            } catch (IOException e) {
                return false;
            }
        }

    }

}
