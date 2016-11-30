/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import com.google.common.collect.ImmutableSet;

@ThreadLeakScope(ThreadLeakScope.Scope.NONE)
public class TransportClientTest extends ESIntegTestCase {

    private String indexName;

    private String typeName;

    private Client client;

    private TransportElasticClient elasticClient;

    private Client mockClient;

    private SearchRequestBuilder mockBuilder;

    private ListenableActionFuture mockSearchResponseFuture;

    private TransportElasticClient mockElasticClient;

    @Before
    public void beforeTest() throws InterruptedException, ExecutionException {
        indexName = "an_index";
        typeName = "a_type";
        client = ESIntegTestCase.internalCluster().transportClient();
        client.prepareIndex(indexName, typeName, "1").setSource("{\"key\":\"value\"}").execute().actionGet();
        elasticClient = new TransportElasticClient(client);

        mockClient = mock(Client.class);
        mockBuilder = mock(SearchRequestBuilder.class);
        when(mockClient.prepareSearch(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.setTypes(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.setSearchType(any(SearchType.class))).thenReturn(mockBuilder);
        mockSearchResponseFuture = mock(ListenableActionFuture.class);
        when(mockSearchResponseFuture.get()).thenReturn(mock(SearchResponse.class));
        when(mockBuilder.execute()).thenReturn(mockSearchResponseFuture);

        mockElasticClient = new TransportElasticClient(mockClient);
    }

    @Test
    public void testGetTypes() throws IOException {
        assertTrue(elasticClient.getTypes(indexName).contains(typeName));
        assertTrue(elasticClient.getTypes("not_an_index").isEmpty());
    }

    @Test
    public void testGetMapping() throws IOException {
        assertNotNull(elasticClient.getMapping(indexName, typeName));
        assertNull(elasticClient.getMapping("not_an_index", typeName));
        assertNull(elasticClient.getMapping(indexName, "not_a_type"));
    }

    @Test
    public void testEmptySearch() {
        mockElasticClient.search(indexName, typeName, new ElasticRequest());
        verify(mockBuilder, never()).setSize(anyInt());
        verify(mockBuilder, never()).setFrom(anyInt());
        verify(mockBuilder, never()).setScroll(any(TimeValue.class));
        verify(mockBuilder, never()).setFetchSource(anyString(), any());
        verify(mockBuilder, never()).setFetchSource(any(String[].class), any());
        verify(mockBuilder, never()).storedFields(anyString());
        verify(mockBuilder, never()).addSort(anyString(), any(SortOrder.class));
        verify(mockBuilder, never()).setQuery(any(QueryBuilder.class));
    }

    @Test
    public void testFullSearch() {
        int size = 10;
        int from = 5;
        int scroll = 30;
        String sourceInclude = "include_field";
        String field = "field_name";
        String sortKey = "sort_field";
        String sortOrder = "desc";
        QueryBuilder query = QueryBuilders.matchAllQuery();

        ElasticRequest request = new ElasticRequest();
        request.setSize(size);
        request.setFrom(from);
        request.addSourceInclude(sourceInclude);
        request.setScroll(scroll);
        request.addField(field);
        request.addSort(sortKey, sortOrder);
        request.setQuery(query);

        mockElasticClient.search(indexName, typeName, request);
        verify(mockBuilder).setSize(10);
        verify(mockBuilder).setFrom(from);
        verify(mockBuilder).setScroll(TimeValue.timeValueSeconds(scroll));
        verify(mockBuilder).addStoredField(field);
        verify(mockBuilder).setFetchSource(sourceInclude, null);
        verify(mockBuilder).addSort(sortKey, SortOrder.DESC);
        verify(mockBuilder).setQuery(query);
    }

    @Test
    public void testSearchWithMultipleSourceIncludes() {
        ElasticRequest request = new ElasticRequest();
        request.addSourceInclude("test1");
        request.addSourceInclude("test2");

        mockElasticClient.search(indexName, typeName, request);
        verify(mockBuilder).setFetchSource(any(String[].class), any());
    }

    @Test(expected=RuntimeException.class)
    public void testSearchException() throws InterruptedException, ExecutionException {
        when(mockSearchResponseFuture.get()).thenThrow(IOException.class);
        mockElasticClient.search(indexName, typeName, new ElasticRequest());
    }

    @Test
    public void testScroll() throws InterruptedException, ExecutionException {
        String scrollId = "scroll_id";
        int scrollTime = 60;
        SearchScrollRequestBuilder mockBuilder = mock(SearchScrollRequestBuilder.class);
        when(mockClient.prepareSearchScroll(scrollId)).thenReturn(mockBuilder);
        when(mockBuilder.execute()).thenReturn(mockSearchResponseFuture);
        mockElasticClient.scroll(scrollId, null);
        verify(mockBuilder, never()).setScroll(any(TimeValue.class));
        mockElasticClient.scroll(scrollId, scrollTime);
        verify(mockBuilder).setScroll(TimeValue.timeValueSeconds(scrollTime));
    }

    @Test(expected=RuntimeException.class)
    public void testScrollException() {
        SearchScrollRequestBuilder mockBuilder = mock(SearchScrollRequestBuilder.class);
        when(mockClient.prepareSearchScroll("test")).thenReturn(mockBuilder);
        when(mockBuilder.execute()).thenThrow(IOException.class);
        mockElasticClient.scroll("test", null);
    }

    @Test
    public void testClearScroll() throws InterruptedException, ExecutionException {
        ClearScrollRequestBuilder mockBuilder = mock(ClearScrollRequestBuilder.class);
        when(mockClient.prepareClearScroll()).thenReturn(mockBuilder);
        when(mockBuilder.execute()).thenReturn(mockSearchResponseFuture);
        when(mockSearchResponseFuture.get()).thenReturn(mock(ClearScrollResponse.class));
        mockElasticClient.clearScroll(new HashSet<>());
        verify(mockBuilder, never()).execute();
        mockElasticClient.clearScroll(ImmutableSet.of("id1"));
        verify(mockBuilder).execute();
        mockElasticClient.clearScroll(ImmutableSet.of("id1","id2"));
        verify(mockBuilder, times(2)).execute();
    }

    @Test
    public void testClose() {
        mockElasticClient.close();
        verify(mockClient).close();
    }

}
