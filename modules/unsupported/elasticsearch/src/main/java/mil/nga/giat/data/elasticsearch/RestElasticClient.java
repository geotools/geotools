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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.geotools.util.logging.Logging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mil.nga.giat.data.elasticsearch.ElasticMappings.Mapping;

public class RestElasticClient implements ElasticClient {

    private final static Logger LOGGER = Logging.getLogger(RestElasticClient.class);

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final static int DEFAULT_MAJOR_VERSION = 5;

    private RestClient client;

    private ObjectMapper mapper;

    private Integer majorVersion;

    public RestElasticClient(RestClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(DATE_FORMAT);
    }

    @Override
    public int getMajorVersion() {
        if (majorVersion != null) {
            return majorVersion;
        }

        final Pattern pattern = Pattern.compile("(\\d+)\\.\\d+\\.\\d+");
        try {
            final Response response = performRequest("GET", "/", null);
            try (final InputStream inputStream = response.getEntity().getContent()) {
                Map<String,Object> info = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
                @SuppressWarnings("unchecked")
                Map<String,Object> version = (Map<String,Object>) info.getOrDefault("version", Collections.EMPTY_MAP);
                final Matcher m = pattern.matcher((String) version.get("number"));
                if (!m.find()) {
                    majorVersion = DEFAULT_MAJOR_VERSION;
                } else {
                    majorVersion = Integer.valueOf(m.group(1));
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Error getting server version: " + e);
            majorVersion = DEFAULT_MAJOR_VERSION;
        }

        return majorVersion;
    }

    @Override
    public List<String> getTypes(String indexName) throws IOException {
        return getMappings(indexName, null).keySet().stream().map(key -> (String) key).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getMapping(String indexName, String type) throws IOException {
        final Map<String, Mapping> mappings = getMappings(indexName, type);
        final Map<String,Object> properties;
        if (mappings.containsKey(type)) {
            properties = mappings.get(type).getProperties();
        } else {
            properties = null;
        }
        return properties;
    }

    private Map<String, Mapping> getMappings(String indexName, String type) throws IOException {
        final Response response;
        try {
            final StringBuilder path = new StringBuilder("/").append(indexName).append("/_mapping");
            if (type != null) {
                path.append("/").append(type);
            }
            response = client.performRequest("GET", path.toString());
        } catch (ResponseException e) {
            if (e.getResponse().getStatusLine().getStatusCode() == 404) {
                return Collections.emptyMap();
            }
            throw e;
        }

        try (final InputStream inputStream = response.getEntity().getContent()) {
            final Map<String,ElasticMappings> values;
            values = mapper.readValue(inputStream, new TypeReference<Map<String, ElasticMappings>>() {});
            final Map<String, Mapping> mappings;
            if (values.containsKey(indexName)) {
                mappings = values.get(indexName).getMappings();
            } else {
                final String aliasedIndex = getIndices(indexName).stream().findFirst().orElse(null);
                if (values.containsKey(aliasedIndex)) {
                    mappings = values.get(aliasedIndex).getMappings();
                } else if (!values.isEmpty()) {
                    mappings = values.values().iterator().next().getMappings();
                } else {
                    LOGGER.severe("No types found for index/alias " + indexName);
                    mappings = Collections.emptyMap();
                }
            }
            return mappings;
        }
    }

    @Override
    public ElasticResponse search(String searchIndices, String type, ElasticRequest request) throws IOException {
        final StringBuilder pathBuilder = new StringBuilder("/" + searchIndices + "/" + type + "/_search");

        final Map<String,Object> requestBody = new HashMap<>();

        if (request.getSize() != null) {
            requestBody.put("size",  request.getSize());
        }

        if (request.getFrom() != null) {
            requestBody.put("from", request.getFrom());
        }

        if (request.getScroll() != null) {
            pathBuilder.append("?scroll=" + request.getScroll() + "s");
        }

        final List<String> sourceIncludes = request.getSourceIncludes();
        if (sourceIncludes.size() == 1) {
            requestBody.put("_source", sourceIncludes.get(0));
        } else if (!sourceIncludes.isEmpty()) {
            requestBody.put("_source", sourceIncludes);
        }

        if (!request.getFields().isEmpty()) {
            final String key = getMajorVersion() > 2 ? "stored_fields" : "fields";
            requestBody.put(key, request.getFields());
        }

        if (!request.getSorts().isEmpty()) {
            requestBody.put("sort", request.getSorts());
        }

        if (request.getQuery() != null) {
            requestBody.put("query", request.getQuery());
        }

        if (request.getAggregations() != null) {
            requestBody.put("aggregations", request.getAggregations());
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Elasticsearch request:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody));
        }
        return parseResponse(performRequest("POST", pathBuilder.toString(), requestBody));
    }

    Response performRequest(String method, String path, Map<String,Object> requestBody) throws IOException {
        final byte[] data = mapper.writeValueAsBytes(requestBody);
        final HttpEntity entity = new ByteArrayEntity(data, ContentType.APPLICATION_JSON);
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
        requestBody.put("scroll", scrollTime + "s");

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
        LOGGER.fine("Closing client: " + client);
        client.close();
    }

    @SuppressWarnings("unchecked")
    public static void removeMapping(String parent, String key, Map<String,Object> data, String currentParent) {
        Iterator<Entry<String, Object>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            if (Objects.equals(currentParent, parent) && entry.getKey().equals(key)) {
                it.remove();
            } else if (entry.getValue() instanceof Map) {
                removeMapping(parent, key, (Map<String,Object>) entry.getValue(), entry.getKey());
            } else if (entry.getValue() instanceof List) {
                ((List<Object>) entry.getValue()).stream()
                .filter(item -> item instanceof Map)
                .forEach(item -> removeMapping(parent, key, (Map<String,Object>) item, currentParent));
            }
        }
    }

    private Set<String> getIndices(String alias) {
        Set<String> indices = null;
        try {
            final Response response = performRequest("GET", "/_alias/" + alias, null);
            try (final InputStream inputStream = response.getEntity().getContent()) {
                final Map<String,Object> result = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
                indices = result.keySet();
            }
        } catch (IOException e) {
            indices = new HashSet<>();
        }
        return indices;
    }
}
