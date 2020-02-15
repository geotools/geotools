/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.elasticsearch.common.joda.Joda;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * A data store for an Elasticsearch index containing geo_point or geo_shape
 * types.
 *
 */
public class ElasticDataStore extends ContentDataStore {

    private final static Logger LOGGER = Logging.getLogger(ElasticDataStore.class);

    private static ElasticCompat compat = ElasticCompatLoader.getCompat(null);

    private static ElasticClient client;

    private final ElasticClient managedClient;

    private final String indexName;

    private final String searchIndices;

    private final List<Name> baseTypeNames;

    private final Map<Name, String> docTypes;

    private Map<String, ElasticLayerConfiguration> layerConfigurations;

    private boolean sourceFilteringEnabled;

    private Integer defaultMaxFeatures;

    private Long scrollSize;

    private boolean scrollEnabled;

    private Integer scrollTime;

    private Long gridSize;

    private Double gridThreshold;

    public ElasticDataStore(String searchHost, Integer hostPort, 
            String indexName, String searchIndices, String clusterName) throws IOException {

        LOGGER.fine("Initializing data store " + searchHost + ":" + hostPort + "/" + indexName);

        this.indexName = indexName;

        if (searchIndices != null) {
            this.searchIndices = searchIndices;
        } else {
            this.searchIndices = indexName;
        }

        if (client == null) {
            managedClient = compat.createClient(searchHost, hostPort, clusterName);
        } else {
            managedClient = null;
        }

        final List<String> types = getClient().getTypes(indexName);
        if (!types.isEmpty()) {
            baseTypeNames = types.stream().map(name -> new NameImpl(name)).collect(Collectors.toList());
        } else {
            baseTypeNames = new ArrayList<>();
        }

        layerConfigurations = new ConcurrentHashMap<>();
        docTypes = new HashMap<>();
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
    public ContentFeatureSource getFeatureSource(Name name, Transaction tx)
            throws IOException {

        final ElasticLayerConfiguration layerConfig = layerConfigurations.get(name.getLocalPart());
        if (layerConfig != null) {
            docTypes.put(name, layerConfig.getDocType());
        }
        final ContentFeatureSource featureSource = super.getFeatureSource(name, tx);
        featureSource.getEntry().getState(Transaction.AUTO_COMMIT).flush();

        return featureSource;
    }

    public List<ElasticAttribute> getElasticAttributes(Name layerName) throws IOException {
        final String localPart = layerName.getLocalPart();
        final ElasticLayerConfiguration layerConfig = layerConfigurations.get(localPart);
        final List<ElasticAttribute> elasticAttributes;
        if (layerConfig == null || layerConfig.getAttributes().isEmpty()) {
            final String docType;
            if (docTypes.containsKey(layerName)) {
                docType = docTypes.get(layerName);
            } else {
                docType = localPart;
            }

            final Map<String,Object> mapping = getClient().getMapping(indexName, docType);
            elasticAttributes = new ArrayList<ElasticAttribute>();
            if (mapping != null) {
                add(elasticAttributes, "_id", "string", mapping, false);
                add(elasticAttributes, "_index", "string", mapping, false);
                add(elasticAttributes, "_type", "string", mapping, false);
                add(elasticAttributes, "_score", "float", mapping, false);
                add(elasticAttributes, "_relative_score", "float", mapping, false);
                add(elasticAttributes, "_aggregation", "binary", mapping, false);

                walk(elasticAttributes, mapping, "", false, false);

                // add default geometry and short name and count duplicate short names
                final Map<String,Integer> counts = new HashMap<>();
                boolean foundGeometry = false;
                for (final ElasticAttribute attribute : elasticAttributes) {
                    if (!foundGeometry && Geometry.class.isAssignableFrom(attribute.getType())) {
                        attribute.setDefaultGeometry(true);
                        foundGeometry = true;
                    }
                    final String[] parts = attribute.getName().split("\\.");
                    attribute.setShortName(parts[parts.length-1]);
                    final int count;
                    if (counts.containsKey(attribute.getShortName())) {
                        count = counts.get(attribute.getShortName())+1;
                    } else {
                        count = 1;
                    }
                    counts.put(attribute.getShortName(), count);
                }
                // use full name if short name has duplicates
                for (final ElasticAttribute attribute : elasticAttributes) {
                    if (counts.get(attribute.getShortName()) > 1) {
                        attribute.setShortName(attribute.getName());
                    }
                }
            }
        } else {
            elasticAttributes = layerConfig.getAttributes();
        }
        return elasticAttributes;
    }

    @Override
    public void dispose() {
        if (managedClient != null) {
            LOGGER.fine("Closing client");
            try {
                managedClient.close();
            } catch (IOException e) {
                LOGGER.warning("Error closing client: " + e);
            }
        }
        super.dispose();
    }

    public String getIndexName() {
        return indexName;
    }

    public String getSearchIndices() {
        return searchIndices;
    }

    public ElasticClient getClient() {
        return client != null ? client : managedClient;
    }

    public boolean isSourceFilteringEnabled() {
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

    private void walk(List<ElasticAttribute> elasticAttributes, Map<String,Object> map, 
            String propertyKey, boolean startType, boolean nested) {

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
                    add(elasticAttributes, propertyKey + ".coordinates", "geo_point", (Map) value, nested);
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

    private void add(List<ElasticAttribute> elasticAttributes, String propertyKey, 
            String propertyType, Map<String,Object> map, boolean nested) {
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
                elasticAttribute.setAnalyzed(compat.isAnalyzed(map));
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
                String format = (String) map.get("format");
                if (format != null) {
                    try {
                        Joda.forPattern(format);
                    } catch (Exception e) {
                        LOGGER.fine("Unable to parse date format ('" + format + "') for " + propertyKey);
                        format = null;
                    }
                }
                if (format == null) {
                    format = "date_optional_time";
                }
                elasticAttribute.setDateFormat(format);
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

    public static void setClient(ElasticClient client) {
        ElasticDataStore.client = client;
    }

}
