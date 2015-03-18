/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import mil.nga.giat.data.elasticsearch.ElasticAttribute.ElasticGeometryType;

import org.elasticsearch.action.admin.cluster.state.ClusterStateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import java.io.IOException;
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

    private final static Logger LOGGER = Logger.getLogger(ElasticDataStoreFactory.class.getName());

    private final String indexName;

    private final String searchIndices;

    private final Node node;

    private final Client client;

    private final ImmutableList<Name> cachedTypeNames;

    // Attributes present in Elasticsearch schema
    private List<ElasticAttribute> elasticAttributes = new ArrayList<>();

    // Attributes configurations of the store entries
    private Map<String, ElasticLayerConfiguration> elasticConfigurations = new ConcurrentHashMap<>();

    public ElasticDataStore(String searchHost, Integer hostPort, 
            String indexName, String searchIndices, String clusterName,
            boolean localNode, boolean storeData) {

        LOGGER.fine("initializing data store " + searchHost + ":" + hostPort + "/" + indexName);

        this.indexName = indexName;
        
        if (searchIndices != null) {
            this.searchIndices = searchIndices;
        } else {
            this.searchIndices = indexName;
        }

        if (localNode) {
            final NodeBuilder nodeBuilder;
            nodeBuilder = nodeBuilder().data(storeData)
                    .local(false)
                    .client(false)
                    .clusterName(clusterName);
            this.node = nodeBuilder.build();
            node.start();
            this.client = node.client();
        } else {
            this.node = null;
            final TransportAddress address;
            address = new InetSocketTransportAddress(searchHost, hostPort);
            Builder settings = ImmutableSettings.settingsBuilder()
                    .put("cluster.name", clusterName);
            this.client = new TransportClient(settings);
            ((TransportClient) client).addTransportAddress(address);
        }
        LOGGER.fine("client connection established");

        final ClusterStateRequest clusterStateRequest;
        clusterStateRequest = Requests.clusterStateRequest()
                .routingTable(true)
                .nodes(true)
                .indices(indexName);

        final ClusterState state;
        state = client.admin()
                .cluster()
                .state(clusterStateRequest)
                .actionGet().getState();
        LOGGER.fine("obtained cluster state");

        IndexMetaData metadata = state.metaData().index(indexName);
        if (metadata != null) {
            final ImmutableOpenMap<String, MappingMetaData> mappings;
            mappings = state.metaData().index(indexName).mappings();
            final Iterator<String> elasticTypes = mappings.keysIt();
            final Vector<Name> names = new Vector<Name>();
            while (elasticTypes.hasNext()) {
                names.add(new NameImpl(elasticTypes.next()));
            }
            cachedTypeNames = ImmutableList.copyOf(names);
        } else {
            cachedTypeNames = ImmutableList.copyOf(new ArrayList<Name>());
        }
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return this.cachedTypeNames;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new ElasticFeatureSource(entry, Query.ALL);
    }

    @Override
    public ContentFeatureSource getFeatureSource(Name typeName, Transaction tx)
            throws IOException {

        if (typeName.getNamespaceURI() != null) {
            // shouldn't have a namespace but remove it if present
            typeName = new NameImpl(typeName.getLocalPart());
        }
        ContentFeatureSource featureSource = super.getFeatureSource(typeName, tx);
        return featureSource;
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction tx) throws IOException {
        return super.getFeatureReader(query, tx);
    }

    public void addConfiguration(String layerName) throws IOException {
        if (elasticConfigurations.get(layerName) == null) {
            final ElasticLayerConfiguration configuration;
            configuration = new ElasticLayerConfiguration();
            configuration.setAttributes(getElasticAttributes(layerName));
            elasticConfigurations.put(layerName, configuration);
        }
    }

    public List<ElasticAttribute> getElasticAttributes(String layerName) throws IOException {
        if (elasticConfigurations.get(layerName) == null) {
            elasticAttributes = new ArrayList<ElasticAttribute>();

            final ClusterStateRequest clusterStateRequest;
            clusterStateRequest = Requests.clusterStateRequest()
                    .routingTable(true)
                    .nodes(true)
                    .indices(indexName);

            final ClusterState state;
            state = client.admin().cluster()
                    .state(clusterStateRequest).actionGet().getState();
            final MappingMetaData metadata;
            metadata = state.metaData().index(indexName)
                    .mapping(layerName);

            final byte[] mappingSource = metadata.source().uncompressed();
            final XContentParser parser;
            parser = XContentFactory.xContent(mappingSource)
                    .createParser(mappingSource);

            Map<String, Object> mapping = parser.map();
            if (mapping.size() == 1 && mapping.containsKey(layerName)) {
                // the type name is the root value, reduce it
                mapping = (Map<String, Object>) mapping.get(layerName);
            }

            add("_id", "string", mapping);
            add("_index", "string", mapping);
            add("_type", "string", mapping);
            add("_score", "float", mapping);
            add("_relative_score", "float", mapping);

            walk(mapping, "", false);

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
        } else {
            elasticAttributes = elasticConfigurations.get(layerName).getAttributes();
        }
        return elasticAttributes;
    }

    private void walk(Map<String,Object> map, String propertyKey, boolean startType) {
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
                walk((Map) value, newPropertyKey, startType);
            } else if (key.equals("type")) {
                add(propertyKey, (String) value, map);
            } else if (key.equals("_timestamp")) {
                add("_timestamp", "date", map);
            }
        }
    }

    private void add(String propertyKey, String propertyType, Map<String,Object> map) {
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
                binding = String.class;
                final String index = (String) map.get("index");
                final boolean analyzed = index == null || index.equals("analyzed");
                elasticAttribute.setAnalyzed(analyzed);
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
                elasticAttributes.add(elasticAttribute);
            }
        }
    }

    @Override
    public void dispose() {
        this.client.close();
        if (this.node != null) {
            this.node.stop();
            this.node.close();
        }
        super.dispose();
        LOGGER.fine("disposed");
    }

    public String getIndexName() {
        return indexName;
    }

    public String getSearchIndices() {
        return searchIndices;
    }

    public Client getClient() {
        return client;
    }

    /**
     * Gets the attributes configuration for the types in this datastore
     */
    public Map<String, ElasticLayerConfiguration> getElasticConfigurations() {
        return elasticConfigurations;
    }

    /**
     * Add the type configuration to this datastore
     */
    public void setElasticConfigurations(ElasticLayerConfiguration configuration) {
        entries.remove(new NameImpl(namespaceURI, configuration.getLayerName()));
        this.elasticConfigurations.put(configuration.getLayerName(), configuration);
    }

}
