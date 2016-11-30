/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Before;
import org.junit.Test;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import static org.mockito.Mockito.*;

@ThreadLeakScope(ThreadLeakScope.Scope.NONE)
public class TransportElasticResponseTest extends ESIntegTestCase {

    private String indexName;

    private String typeName;

    private Client client;

    private SearchResponse mockSearchResponse;

    private SearchHits mockSearchHits;

    private SearchHit[] hits;

    private TransportElasticResponse response;

    @Before
    public void setup() {
        indexName = "an_index";
        typeName = "a_type";
        client = ESIntegTestCase.internalCluster().transportClient();
        client.prepareIndex(indexName, typeName, "1").setSource("{\"key\":\"value\"}").execute().actionGet();

        mockSearchResponse = mock(SearchResponse.class);
        mockSearchHits = mock(SearchHits.class);
        hits = new SearchHit[] { mock(SearchHit.class) };
        when(mockSearchResponse.getHits()).thenReturn(mockSearchHits);
        when(mockSearchHits.getHits()).thenReturn(hits);

        response = new TransportElasticResponse(mockSearchResponse);
    }

    @Test
    public void testFields() {
        when(mockSearchResponse.getScrollId()).thenReturn("scroll_id");
        when(mockSearchHits.getTotalHits()).thenReturn(50l);
        when(mockSearchHits.getMaxScore()).thenReturn(0.8f);
        assertEquals(50, response.getTotalNumHits());
        assertEquals(0.8, response.getMaxScore(), 1e-6);
        assertEquals(hits.length, response.getNumHits());
        assertEquals("scroll_id", response.getScrollId());
    }

    @Test
    public void testSearchResults() {
        when(hits[0].getId()).thenReturn("doc_id");
        when(hits[0].getIndex()).thenReturn("index_name");
        when(hits[0].getType()).thenReturn("type_name");
        when(hits[0].getScore()).thenReturn(0.8f);
        when(hits[0].getSource()).thenReturn(ImmutableMap.of("key","value"));
        SearchHitField mockField = mock(SearchHitField.class);
        when(mockField.getValues()).thenReturn(ImmutableList.of("value1","value2"));
        when(hits[0].field("a_field")).thenReturn(mockField);

        assertEquals(1, response.getResults().getHits().size());
        ElasticHit hit = response.getResults().getHits().get(0);
        assertEquals("doc_id", hit.getId());
        assertEquals("index_name", hit.getIndex());
        assertEquals("type_name", hit.getType());
        assertEquals(0.8, hit.getScore(), 1e-6);
        assertEquals(ImmutableMap.of("key","value"), hit.getSource());
        assertEquals(ImmutableList.of("value1","value2"), hit.field("a_field"));
        assertNull(hit.field("not_a_field"));
    }

}
