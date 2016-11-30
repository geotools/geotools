/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.unit.TimeValue;
import org.geotools.util.logging.Logging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mil.nga.giat.data.elasticsearch.ElasticMappings.Mapping;

public class RestElasticClient implements ElasticClient {

    private final static Logger LOGGER = Logging.getLogger(RestElasticClient.class);

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private RestClient client;

    private ObjectMapper mapper;

    public RestElasticClient(RestClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(DATE_FORMAT);
    }

    @Override
    public List<String> getTypes(String indexName) throws IOException {
        final Response response = client.performRequest("GET", "/" + indexName + "/_mapping");
        try (final InputStream inputStream = response.getEntity().getContent()) {
            final Map<String,ElasticMappings> values;
            values = mapper.readValue(inputStream, new TypeReference<Map<String, ElasticMappings>>() {});
            final Map<String, Mapping> mappings = values.get(indexName).getMappings();
            return mappings.keySet().stream().map(key -> (String) key).collect(Collectors.toList());
        }
    }

    @Override
    public Map<String, Object> getMapping(String indexName, String type) throws IOException {
        final Response response = client.performRequest("GET", "/" + indexName + "/_mapping/" + type);
        try (final InputStream inputStream = response.getEntity().getContent()) {
            final Map<String,ElasticMappings> values;
            values = mapper.readValue(inputStream, new TypeReference<Map<String, ElasticMappings>>() {});
            final Map<String, Mapping> mappings = values.get(indexName).getMappings();
            return mappings.get(type).getProperties();
        }
    }

    @Override
    public ElasticResponse search(String searchIndices, String type, ElasticRequest request) throws IOException {
        final String path = "/" + searchIndices + "/" + type + "/_search";

        final Map<String,Object> requestBody = new HashMap<>();

        if (request.getSize() != null) {
            requestBody.put("size",  request.getSize());
        }

        if (request.getFrom() != null) {
            requestBody.put("from", request.getFrom());
        }

        if (request.getScroll() != null) {
            requestBody.put("scroll", TimeValue.timeValueSeconds(request.getScroll()).toString());
        }

        final List<String> sourceIncludes = request.getSourceIncludes();
        if (sourceIncludes.size() == 1) {
            requestBody.put("_source", sourceIncludes.get(0));
        } else if (!sourceIncludes.isEmpty()) {
            requestBody.put("_source", sourceIncludes);
        }

        if (!request.getFields().isEmpty()) {
            requestBody.put("stored_fields", request.getFields());
        }

        if (!request.getSorts().isEmpty()) {
            requestBody.put("sort", request.getSorts());
        }

        if (request.getQuery() != null) {
            // TODO: Convert QueryBuilder directly to map or use string directly to avoid unnecessary conversions
            final Map<String,Object> query = mapper.readValue(request.getQuery().toString(), new TypeReference<Map<String, Object>>() {});
            makeCompatQuery2(query);
            requestBody.put("query", query);
        }

        if (request.getAggregations() != null) {
            requestBody.put("aggregations", request.getAggregations());
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Elasticsearch request:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody));
        }

        return parseResponse(performRequest("POST", path, requestBody));
    }

    private Response performRequest(String method, String path, Map<String,Object> requestBody) throws IOException {
        final byte[] data = mapper.writeValueAsBytes(requestBody);
        final HttpEntity entity = new ByteArrayEntity(data);
        final Response response = client.performRequest(
                method,
                path,
                Collections.<String, String>emptyMap(),
                entity);

        if (response.getStatusLine().getStatusCode() >= 400) {
            throw new IOException("Error executing request: " + response.getStatusLine().getReasonPhrase());
        }
        return response;
    }

    private ElasticResponse parseResponse(final Response response) throws IOException {
        try (final InputStream inputStream = response.getEntity().getContent()) {
            return mapper.readValue(inputStream, ElasticResponse.class);
        }
    }

    @Override
    public ElasticResponse scroll(String scrollId, Integer scrollTime) throws IOException {
        final String path = "/_search/scroll";

        final Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("scroll_id", scrollId);
        requestBody.put("scroll", TimeValue.timeValueSeconds(scrollTime).toString());

        return parseResponse(performRequest("POST", path, requestBody));
    }

    @Override
    public void clearScroll(Set<String> scrollIds) throws IOException {
        final String path = "/_search/scroll";
        if (!scrollIds.isEmpty()) {
            final Map<String,Object> requestBody = new HashMap<>();
            requestBody.put("scroll_id", scrollIds);

            performRequest("DELETE", path, requestBody);
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private void makeCompatQuery2(final Map<String,Object> data) {
        // validation_method and ignore_unmapped not supported in 2.x
        for (final String parent : new String[] {"geo_polygon", "geo_distance", "geo_bounding_box"}) {
            removeMapping(parent, "validation_method", data, null);
            removeMapping(parent, "ignore_unmapped", data, null);
            removeMapping(parent, "boost", data, null);
            removeMapping(parent, "type", data, null);
        }
    }

    public static void removeMapping(String parent, String key, Map<String,Object> data, String currentParent) {
        Iterator<Entry<String, Object>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            if (Objects.equals(currentParent, parent) && entry.getKey().equals(key)) {
                it.remove();
            } else if (entry.getValue() instanceof Map) {
                removeMapping(parent, key, (Map<String,Object>) entry.getValue(), entry.getKey());
            } else if (entry.getValue() instanceof List) {
                ((List) entry.getValue()).stream()
                .filter(item -> item instanceof Map)
                .forEach(item -> removeMapping(parent, key, (Map) item, currentParent));
            }
        }
    }

}
