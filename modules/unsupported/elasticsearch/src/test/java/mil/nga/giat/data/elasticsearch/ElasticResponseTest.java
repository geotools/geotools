/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ElasticResponseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaults() {
        ElasticResponse response = new ElasticResponse();
        assertEquals(0, response.getTotalNumHits());
        assertEquals(0, response.getNumHits());
        assertTrue(response.getHits().isEmpty());
    }

    @Test
    public void testTotalHits() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"total\":10}}", ElasticResponse.class);
        assertEquals(10, response.getTotalNumHits());
    }

    @Test
    public void testNullMaxScore() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{}}", ElasticResponse.class);
        assertEquals(0, response.getMaxScore(), 1e-9);
    }

    @Test
    public void testMaxScore() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"max_score\":0.8}}", ElasticResponse.class);
        assertEquals(0.8, response.getMaxScore(), 1e-6);
    }

    @Test
    public void testScroll() throws IOException {
        ElasticResponse response = mapper.readValue("{\"_scroll_id\":\"12345\"}", ElasticResponse.class);
        assertEquals("12345", response.getScrollId());
    }

    @Test
    public void getNumHits() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{},{},{}]}}", ElasticResponse.class);
        assertEquals(3, response.getNumHits());
    }

    @Test
    public void testHitId() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"_id\": \"5\"}]}}", ElasticResponse.class);
        assertEquals(1, response.getResults().getHits().size());
        assertEquals("5", response.getResults().getHits().get(0).getId());

    }

    @Test
    public void testHitIndex() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"_index\": \"test\"}]}}", ElasticResponse.class);
        assertEquals(1, response.getResults().getHits().size());
        assertEquals("test", response.getResults().getHits().get(0).getIndex());
    }

    @Test
    public void testHitType() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"_type\": \"test\"}]}}", ElasticResponse.class);
        assertEquals(1, response.getResults().getHits().size());
        assertEquals("test", response.getResults().getHits().get(0).getType());
    }

    @Test
    public void testHitScore() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"_score\": 0.4}]}}", ElasticResponse.class);
        assertEquals(1, response.getResults().getHits().size());
        assertEquals(0.4, response.getResults().getHits().get(0).getScore(), 1e-6);
    }

    @Test
    public void testHitFields() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"fields\": {\"tags\":[\"red\"]}}]}}", ElasticResponse.class);
        assertEquals(1, response.getResults().getHits().size());
        assertNotNull(response.getResults().getHits().get(0).field("tags"));
        assertEquals(ImmutableList.of("red"), response.getResults().getHits().get(0).field("tags"));

        response = mapper.readValue("{\"hits\":{\"hits\":[{}]}}", ElasticResponse.class);
        assertNull(response.getResults().getHits().get(0).field("tags"));
    }

    @Test
    public void testHitSource() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"_source\": {\"tags\":[\"red\"]}}]}}", ElasticResponse.class);
        assertEquals(1, response.getResults().getHits().size());
        assertNotNull(response.getResults().getHits().get(0).getSource());
        assertEquals(ImmutableList.of("red"), response.getResults().getHits().get(0).getSource().get("tags"));
    }

    @Test
    public void testAggregations() throws IOException {
        ElasticResponse response = mapper.readValue("{\"aggregations\":{\"first\":{\"buckets\": [{\"key\":\"0\",\"doc_count\":10}]}}}", ElasticResponse.class);
        assertEquals(1, response.getAggregations().size());
        assertEquals(1, response.getAggregations().size());
        assertEquals(1, response.getAggregations().values().stream().findFirst().orElse(null).getBuckets().size());
        assertEquals(ImmutableMap.of("key","0","doc_count",10), response.getAggregations().values().stream().findFirst().orElse(null).getBuckets().get(0));
    }

    @Test
    public void testMissingAggregation() throws IOException {
        ElasticResponse response = mapper.readValue("{}", ElasticResponse.class);
        assertNull(response.getAggregations());
    }

    @Test
    public void testToString() throws IOException {
        ElasticResponse response = mapper.readValue("{\"hits\":{\"hits\":[{\"_source\": {\"tags\":[\"red\"]}}]}, " + 
                "\"aggregations\":{\"first\":{\"buckets\": [{\"key\":\"0\",\"doc_count\":10}]}}}", ElasticResponse.class);
        String responseStr = response.toString();
        assertTrue(responseStr.contains("hits=1"));
        assertTrue(responseStr.contains("numBuckets=1"));
    }

}
