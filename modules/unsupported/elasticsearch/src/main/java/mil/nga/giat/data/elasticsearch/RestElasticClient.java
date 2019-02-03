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

import mil.nga.giat.data.elasticsearch.ElasticMappings.Mapping;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.geotools.util.logging.Logging;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("javadoc")
public class RestElasticClient implements ElasticClient {

    private final static Logger LOGGER = Logging.getLogger(RestElasticClient.class);

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final static double DEFAULT_VERSION = 6.0;

    private RestClient adminClient;

    private RestClient proxyClient;
    
    private ObjectMapper mapper;

    private Double version;

    public RestElasticClient(RestClient client) {
        this.adminClient = client;
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(DATE_FORMAT);
    }

    public RestElasticClient(RestClient client, RestClient proxyClient) {
        this.adminClient = client;
        this.proxyClient = proxyClient;
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(DATE_FORMAT);
    }
    
    @Override
    public double getVersion() {
        if (version != null) {
            return version;
        }

        final Pattern pattern = Pattern.compile("(\\d+\\.\\d+)\\.\\d+");
        try {
            final Response response = performRequest("GET", "/", null, this.adminClient);
            try (final InputStream inputStream = response.getEntity().getContent()) {
                Map<String,Object> info = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
                @SuppressWarnings("unchecked")
                Map<String,Object> ver = (Map<String,Object>) info.getOrDefault("version", Collections.EMPTY_MAP);
                final Matcher m = pattern.matcher((String) ver.get("number"));
                if (!m.find()) {
                    version = DEFAULT_VERSION;
                } else {
                    version = Double.valueOf(m.group(1));
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Error getting server version: " + e);
            version = DEFAULT_VERSION;
        }

        return version;
    }

    @Override
    public List<String> getTypes(String indexName) throws IOException {
        return getMappings(indexName, null).keySet().stream().collect(Collectors.toList());
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
            response = this.adminClient.performRequest("GET", path.toString());
        } catch (ResponseException e) {
            if (e.getResponse().getStatusLine().getStatusCode() == 404) {
                return Collections.emptyMap();
            }
            throw e;
        }

        try (final InputStream inputStream = response.getEntity().getContent()) {
            final Map<String,ElasticMappings> values;
            values = this.mapper.readValue(inputStream, new TypeReference<Map<String, ElasticMappings>>() {});
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
            final String key = getVersion() >= 5 ? "stored_fields" : "fields";
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
            LOGGER.fine("Elasticsearch request:\n" + this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody));
        }

        return parseResponse(performRequest("POST", pathBuilder.toString(), requestBody));
    }

    Response performRequest(String method, String path, Map<String,Object> requestBody, RestClient rc) throws IOException {
        final byte[] data = this.mapper.writeValueAsBytes(requestBody);
        final HttpEntity entity = new ByteArrayEntity(data, ContentType.APPLICATION_JSON);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Method: " + method);
            LOGGER.fine("Path: " + path);
            LOGGER.fine("RequestBody: " + requestBody);
        }

        Response response = null;
        if (rc == this.proxyClient) {
            final SecurityContext ctx = SecurityContextHolder.getContext();
            final Authentication auth = ctx.getAuthentication();
            if (auth == null) {
                throw new IllegalStateException(String.format("Authentication could not be determined!"));
            }
            if (!auth.isAuthenticated()) {
                throw new IllegalStateException(String.format("User is not authenticated: %s", auth.getName()));
            }
            final Header proxy = new BasicHeader(RUN_AS, auth.getName());
            LOGGER.fine("Performing proxy request for: " + auth.getName());
            response = rc.performRequest(method, path, Collections.<String, String> emptyMap(), entity, proxy);
        } else {
            LOGGER.fine("Performing admin request.");
            response = rc.performRequest(method, path, Collections.<String, String> emptyMap(), entity);
        }
        if (response.getStatusLine().getStatusCode() >= 400) {
            throw new IOException("Error executing request: " + response.getStatusLine().getReasonPhrase());
        }
        return response;
    }

    Response performRequest(String method, String path, Map<String,Object> requestBody) throws IOException {
        return performRequest(method, path, requestBody, this.proxyClient != null ? this.proxyClient : this.adminClient);
    }
    
    private ElasticResponse parseResponse(final Response response) throws IOException {
        try (final InputStream inputStream = response.getEntity().getContent()) {
            return this.mapper.readValue(inputStream, ElasticResponse.class);
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
        LOGGER.fine("Closing client: " + this.adminClient);
        this.adminClient.close();
        if (this.proxyClient != null) {
            LOGGER.fine("Closing proxyClient: " + this.proxyClient);
            this.proxyClient.close();
        }
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
            final Response response = performRequest("GET", "/_alias/" + alias, null, this.adminClient);
            try (final InputStream inputStream = response.getEntity().getContent()) {
                final Map<String,Object> result = this.mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
                indices = result.keySet();
            }
        } catch (IOException e) {
            indices = new HashSet<>();
        }
        return indices;
    }
}
