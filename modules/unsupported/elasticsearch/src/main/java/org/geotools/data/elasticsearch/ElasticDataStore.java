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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.elasticsearch.ElasticAttribute.ElasticGeometryType;
import org.geotools.data.elasticsearch.date.ElasticsearchDateConverter;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.type.Name;

/** A data store for an Elasticsearch index containing geo_point or geo_shape types. */
public class ElasticDataStore extends ContentDataStore {

    private static final Logger LOGGER = Logging.getLogger(ElasticDataStore.class);

    private ElasticClient client;

    private final String indexName;

    private final List<Name> baseTypeNames;

    private final Map<Name, String> docTypes;

    private Map<String, ElasticLayerConfiguration> layerConfigurations;

    private boolean sourceFilteringEnabled;

    private Integer defaultMaxFeatures;

    private Long scrollSize;

    private boolean scrollEnabled;

    private Integer scrollTime;

    private ArrayEncoding arrayEncoding;

    private Long gridSize;

    private Double gridThreshold;

    public enum ArrayEncoding {

        /** Return all arrays without encoding. */
        JSON,

        /** URL encode and join string array elements. */
        CSV
    }

    public ElasticDataStore(String searchHost, Integer hostPort, String indexName)
            throws IOException {
        this(RestClient.builder(new HttpHost(searchHost, hostPort, "http")).build(), indexName);
    }

    public ElasticDataStore(RestClient restClient, String indexName) throws IOException {
        this(restClient, null, indexName, false);
    }

    public ElasticDataStore(
            RestClient restClient,
            RestClient proxyRestClient,
            String indexName,
            boolean enableRunAs)
            throws IOException {
        LOGGER.fine("Initializing data store for " + indexName);

        this.indexName = indexName;

        try {
            checkRestClient(restClient);
            if (proxyRestClient != null) {
                checkRestClient(proxyRestClient);
            }
            client = new RestElasticClient(restClient, proxyRestClient, enableRunAs);
        } catch (Exception e) {
            throw new IOException("Unable to create REST client", e);
        }
        LOGGER.fine("Created REST client: " + client);

        final List<String> types = getClient().getTypes(indexName);
        if (!types.isEmpty()) {
            baseTypeNames = types.stream().map(NameImpl::new).collect(Collectors.toList());
        } else {
            baseTypeNames = new ArrayList<>();
        }

        layerConfigurations = new ConcurrentHashMap<>();
        docTypes = new HashMap<>();

        arrayEncoding = ArrayEncoding.JSON;
    }

    @Override
    protected List<Name> createTypeNames() {
        final List<Name> names = new ArrayList<>();
        names.addAll(baseTypeNames);
        names.addAll(docTypes.keySet());
        return names;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new ElasticFeatureSource(entry, Query.ALL);
    }

    @Override
    public ContentFeatureSource getFeatureSource(Name name, Transaction tx) throws IOException {

        final ElasticLayerConfiguration layerConfig = layerConfigurations.get(name.getLocalPart());
        if (layerConfig != null) {
            docTypes.put(name, layerConfig.getDocType());
        }
        final ContentFeatureSource featureSource = super.getFeatureSource(name, tx);
        featureSource.getEntry().getState(Transaction.AUTO_COMMIT).flush();

        return featureSource;
    }

    @Override
    public void dispose() {
        try {
            this.client.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
        super.dispose();
    }

    public List<ElasticAttribute> getElasticAttributes(Name layerName) throws IOException {
        final String localPart = layerName.getLocalPart();
        final ElasticLayerConfiguration layerConfig = layerConfigurations.get(localPart);
        final List<ElasticAttribute> elasticAttributes;
        if (layerConfig == null || layerConfig.getAttributes().isEmpty()) {
            final String docType = docTypes.getOrDefault(layerName, localPart);
            final Map<String, Object> mapping = getClient().getMapping(indexName, docType);
            elasticAttributes = new ArrayList<>();
            if (mapping != null) {
                add(elasticAttributes, "_id", "string", mapping, false);
                add(elasticAttributes, "_index", "string", mapping, false);
                add(elasticAttributes, "_type", "string", mapping, false);
                add(elasticAttributes, "_score", "float", mapping, false);
                add(elasticAttributes, "_relative_score", "float", mapping, false);
                add(elasticAttributes, "_aggregation", "binary", mapping, false);

                walk(elasticAttributes, mapping, "", false, false);
            }
        } else {
            elasticAttributes = layerConfig.getAttributes();
        }
        return elasticAttributes;
    }

    String getIndexName() {
        return indexName;
    }

    ElasticClient getClient() {
        return client;
    }

    boolean isSourceFilteringEnabled() {
        return sourceFilteringEnabled;
    }

    public void setSourceFilteringEnabled(boolean sourceFilteringEnabled) {
        this.sourceFilteringEnabled = sourceFilteringEnabled;
    }

    public Integer getDefaultMaxFeatures() {
        return defaultMaxFeatures;
    }

    public void setDefaultMaxFeatures(Integer defaultMaxFeatures) {
        this.defaultMaxFeatures = defaultMaxFeatures;
    }

    public Long getScrollSize() {
        return scrollSize;
    }

    public Boolean getScrollEnabled() {
        return scrollEnabled;
    }

    public Integer getScrollTime() {
        return scrollTime;
    }

    public void setScrollSize(Long scrollSize) {
        this.scrollSize = scrollSize;
    }

    public void setScrollEnabled(Boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }

    public void setScrollTime(Integer scrollTime) {
        this.scrollTime = scrollTime;
    }

    public ArrayEncoding getArrayEncoding() {
        return arrayEncoding;
    }

    public void setArrayEncoding(ArrayEncoding arrayEncoding) {
        this.arrayEncoding = arrayEncoding;
    }

    public Long getGridSize() {
        return gridSize;
    }

    public void setGridSize(Long gridSize) {
        this.gridSize = gridSize;
    }

    public Double getGridThreshold() {
        return gridThreshold;
    }

    public void setGridThreshold(Double gridThreshold) {
        this.gridThreshold = gridThreshold;
    }

    public Map<String, ElasticLayerConfiguration> getLayerConfigurations() {
        return layerConfigurations;
    }

    public void setLayerConfiguration(ElasticLayerConfiguration layerConfig) {
        final String layerName = layerConfig.getLayerName();
        this.layerConfigurations.put(layerName, layerConfig);
    }

    public Map<Name, String> getDocTypes() {
        return docTypes;
    }

    public String getDocType(Name typeName) {
        final String docType;
        if (docTypes.containsKey(typeName)) {
            docType = docTypes.get(typeName);
        } else {
            docType = typeName.getLocalPart();
        }
        return docType;
    }

    @SuppressWarnings("unchecked")
    private void walk(
            List<ElasticAttribute> elasticAttributes,
            Map<String, Object> map,
            String propertyKey,
            boolean startType,
            boolean nested) {

        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (!key.equals("_timestamp") && Map.class.isAssignableFrom(value.getClass())) {
                final String newPropertyKey;
                if (!startType && key.equals("properties")) {
                    newPropertyKey = propertyKey;
                } else if (propertyKey.isEmpty()) {
                    newPropertyKey = entry.getKey();
                } else {
                    newPropertyKey = propertyKey + "." + key;
                }
                startType = !startType && key.equals("properties");
                if (!nested && map.containsKey("type")) {
                    nested = map.get("type").equals("nested");
                }

                if (ElasticParserUtil.isGeoPointFeature((Map) value)) {
                    add(
                            elasticAttributes,
                            propertyKey + ".coordinates",
                            "geo_point",
                            (Map) value,
                            nested);
                } else {
                    walk(elasticAttributes, (Map) value, newPropertyKey, startType, nested);
                }
            } else if (key.equals("type") && !value.equals("nested")) {
                add(elasticAttributes, propertyKey, (String) value, map, nested);
            } else if (key.equals("_timestamp")) {
                add(elasticAttributes, "_timestamp", "date", map, nested);
            }
        }
    }

    private void add(
            List<ElasticAttribute> elasticAttributes,
            String propertyKey,
            String propertyType,
            Map<String, Object> map,
            boolean nested) {
        if (propertyKey != null) {
            final ElasticAttribute elasticAttribute = new ElasticAttribute(propertyKey);
            final Class<?> binding;
            switch (propertyType) {
                case "geo_point":
                    binding = Point.class;
                    elasticAttribute.setSrid(4326);
                    elasticAttribute.setGeometryType(ElasticGeometryType.GEO_POINT);
                    break;
                case "geo_shape":
                    binding = Geometry.class;
                    elasticAttribute.setSrid(4326);
                    elasticAttribute.setGeometryType(ElasticGeometryType.GEO_SHAPE);
                    break;
                case "string":
                case "keyword":
                case "text":
                    binding = String.class;
                    elasticAttribute.setAnalyzed(isAnalyzed(map));
                    break;
                case "integer":
                    binding = Integer.class;
                    break;
                case "long":
                    binding = Long.class;
                    break;
                case "float":
                    binding = Float.class;
                    break;
                case "double":
                    binding = Double.class;
                    break;
                case "boolean":
                    binding = Boolean.class;
                    break;
                case "date":
                    List<String> validFormats = new ArrayList<>();
                    String availableFormat = (String) map.get("format");
                    if (availableFormat == null) {
                        validFormats.add("date_optional_time");
                    } else {
                        if (!availableFormat.contains("\\|\\|")) {
                            try {
                                ElasticsearchDateConverter.forFormat(availableFormat);
                                validFormats.add(availableFormat);
                            } catch (Exception e) {
                                LOGGER.fine(
                                        "Unable to parse date format ('"
                                                + availableFormat
                                                + "') for "
                                                + propertyKey);
                            }
                        } else {
                            String[] formats = availableFormat.split("\\|\\|");
                            for (String format : formats) {
                                try {
                                    ElasticsearchDateConverter.forFormat(availableFormat);
                                    validFormats.add(format);
                                } catch (Exception e) {
                                    LOGGER.fine(
                                            "Unable to parse date format ('"
                                                    + format
                                                    + "') for "
                                                    + propertyKey);
                                }
                            }
                        }
                    }
                    if (validFormats.isEmpty()) {
                        validFormats.add("date_optional_time");
                    }
                    elasticAttribute.setValidDateFormats(validFormats);
                    binding = Date.class;
                    break;
                case "binary":
                    binding = byte[].class;
                    break;
                default:
                    binding = null;
                    break;
            }
            if (binding != null) {
                final boolean stored;
                if (map.get("store") != null) {
                    stored = (Boolean) map.get("store");
                } else {
                    stored = false;
                }
                elasticAttribute.setStored(stored);
                elasticAttribute.setType(binding);
                elasticAttribute.setNested(nested);
                elasticAttributes.add(elasticAttribute);
            }
        }
    }

    private static void checkRestClient(RestClient client) throws IOException {
        final Response response = client.performRequest(new Request("GET", "/"));
        final int status = response.getStatusLine().getStatusCode();
        if (status >= 400) {
            final String reason = response.getStatusLine().getReasonPhrase();
            throw new IOException(
                    String.format("Unexpected response from Elasticsearch: %d %s", status, reason));
        }
    }

    static boolean isAnalyzed(Map<String, Object> map) {
        boolean analyzed = false;
        Object value = map.get("type");
        if (value instanceof String && value.equals("text")) {
            analyzed = true;
        }
        return analyzed;
    }
}
