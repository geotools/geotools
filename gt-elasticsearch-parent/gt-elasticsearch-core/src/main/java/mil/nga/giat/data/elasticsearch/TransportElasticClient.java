/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.search.sort.SortOrder;
import org.geotools.util.logging.Logging;

import com.carrotsearch.hppc.cursors.ObjectCursor;

public class TransportElasticClient implements ElasticClient {

    private final static Logger LOGGER = Logging.getLogger(TransportElasticClient.class);

    private Client client;

    public TransportElasticClient(Client client) {
        this.client = client;
    }

    @Override
    public List<String> getTypes(String indexName) {
        final ClusterStateRequest clusterStateRequest;
        clusterStateRequest = Requests.clusterStateRequest()
                .indices(indexName);

        final ClusterState state;
        state = client.admin()
                .cluster()
                .state(clusterStateRequest)
                .actionGet().getState();

        final IndexMetaData metadata = state.metaData().index(indexName);
        final List<String> types = new ArrayList<>();
        if (metadata != null) {
            final ImmutableOpenMap<String, MappingMetaData> mappings;
            mappings = state.metaData().index(indexName).getMappings();
            final Iterator<ObjectCursor<String>> elasticTypes = mappings.keys().iterator();
            while (elasticTypes.hasNext()) {
                types.add(elasticTypes.next().value);
            }
        }
        return types;
    }

    @Override
    public Map<String, Object> getMapping(String indexName, String type) throws IOException {
        final ClusterStateRequest clusterStateRequest;
        clusterStateRequest = Requests.clusterStateRequest()
                .routingTable(true)
                .nodes(true)
                .indices(indexName);

        final ClusterState state;
        state = client.admin().cluster()
                .state(clusterStateRequest).actionGet().getState();

        final Map<String, Object> mapping;        
        final IndexMetaData indexMetadata = state.metaData().index(indexName);
        if (indexMetadata != null) {
            final MappingMetaData metadata;
            metadata = state.metaData().index(indexName)
                    .mapping(type);

            if (metadata != null) {
                final byte[] mappingSource = metadata.source().uncompressed();
                final XContentParser parser;
                parser = XContentFactory.xContent(mappingSource)
                        .createParser(mappingSource);

                final Map<String,Object> rawMapping = parser.map();
                if (rawMapping.size() == 1 && rawMapping.containsKey(type)) {
                    // the type name is the root value, reduce it
                    mapping = (Map<String, Object>) rawMapping.get(type);
                } else {
                    mapping = rawMapping;
                }
            } else {
                mapping = null;
            }
        } else {
            mapping = null;
        }
        return mapping;
    }

    @Override
    public void close() {
        this.client.close();
    }

    @Override
    public ElasticResponse search(String searchIndices, String type, ElasticRequest request) {
        final SearchRequestBuilder searchRequest = client.prepareSearch(searchIndices)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        if (request.getSize() != null) {
            searchRequest.setSize(request.getSize());
        }

        if (request.getFrom() != null) {
            searchRequest.setFrom(request.getFrom());
        }

        if (request.getScroll() != null) {
            searchRequest.setScroll(TimeValue.timeValueSeconds(request.getScroll()));
        }

        final List<String> sourceIncludes = request.getSourceIncludes();
        if (sourceIncludes.size() == 1) {
            searchRequest.setFetchSource(sourceIncludes.get(0), null);
        } else if (!sourceIncludes.isEmpty()) {
            searchRequest.setFetchSource(sourceIncludes.toArray(new String[sourceIncludes.size()]), null);
        }

        request.getFields().stream().forEach(field -> ElasticCompatLoader.getCompat(null).addField(searchRequest, field));

        request.getSorts().stream().forEach(entry -> {
            final String key = entry.keySet().iterator().next();
            final String value = entry.values().iterator().next();
            searchRequest.addSort(key, SortOrder.valueOf(value.toUpperCase()));
        });

        if (request.getQuery() != null) {
            searchRequest.setQuery(request.getQuery());
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Request: " + System.lineSeparator() + searchRequest);
        }

        try {
            return new TransportElasticResponse(searchRequest.execute().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ElasticResponse scroll(String scrollId, Integer scrollTime) {
        final SearchScrollRequestBuilder scrollRequest = client.prepareSearchScroll(scrollId);
        if (scrollTime != null) {
            scrollRequest.setScroll(TimeValue.timeValueSeconds(scrollTime));
        }
        try {
            return new TransportElasticResponse(scrollRequest.execute().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearScroll(Set<String> scrollIds) {
        if (!scrollIds.isEmpty()) {
            final ClearScrollRequestBuilder clearScrollRequest = client.prepareClearScroll();
            scrollIds.stream().forEach(id -> clearScrollRequest.addScrollId(id));
            clearScrollRequest.execute().actionGet();
        }
    }

}
