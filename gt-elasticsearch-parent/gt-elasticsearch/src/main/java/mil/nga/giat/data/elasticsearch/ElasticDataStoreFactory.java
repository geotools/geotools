/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Data store factory that creates {@linkplain ElasticDataStore} instances.
 *
 */
public class ElasticDataStoreFactory implements DataStoreFactorySpi {

    /** Cluster hostname. **/
    public static final Param HOSTNAME = new Param("elasticsearch_host", String.class, "Elasticsearch host", false, "localhost");

    /** Cluster client port. **/
    public static final Param HOSTPORT = new Param("elasticsearch_port", Integer.class, 
            "Elasticsearch port. Use HTTP (e.g. 9200) or transport (e.g. 9300) port based on the desired client type.", false, 9200);

    /** Index name. **/
    public static final Param INDEX_NAME = new Param("index_name", String.class, "Index defining type", true);

    /** Index name. **/
    public static final Param SEARCH_INDICES = new Param("search_indices", String.class, "Indices for search (default is index_name)", false);

    /** Cluster name. **/
    public static final Param CLUSTERNAME = new Param("cluster_name", String.class, "Name of cluster", false, "elasticsearch");

    public static final Param DEFAULT_MAX_FEATURES = new Param("default_max_features", Integer.class, "Default max features", false, 100);

    public static final Param SOURCE_FILTERING_ENABLED = new Param("source_filtering_enabled", Boolean.class, "Enable source field filtering", false, false);

    public static final Param SCROLL_ENABLED = new Param("scroll_enabled", Boolean.class, "Use scan search type instead of dfs_query_then_fetch", false, false);

    public static final Param SCROLL_SIZE = new Param("scroll_size", Long.class, "Scroll size (ignored if scroll_enabled=false)", false, 20);

    public static final Param SCROLL_TIME_SECONDS = new Param("scroll_time", Integer.class, "Time to keep the scroll open in seconds (ignored if scroll_enabled=false)", false, 120);

    public static final Param GRID_SIZE = new Param("grid_size", Long.class, "Hint for Geohash grid size (nrow*ncol)", false, 10000l);

    public static final Param GRID_THRESHOLD = new Param("grid_threshold",  Double.class, 
            "Geohash grid aggregation precision will be the minimum necessary to satisfy actual_grid_size/grid_size>grid_threshold", false, 0.05);

    protected static final Param[] PARAMS = {
            HOSTNAME,
            HOSTPORT,
            INDEX_NAME,
            SEARCH_INDICES,
            CLUSTERNAME,
            DEFAULT_MAX_FEATURES,
            SOURCE_FILTERING_ENABLED,
            SCROLL_ENABLED,
            SCROLL_SIZE,
            SCROLL_TIME_SECONDS,
            GRID_SIZE,
            GRID_THRESHOLD
    };

    protected static final String DISPLAY_NAME = "Elasticsearch";

    protected static final String DESCRIPTION = "Elasticsearch Index";

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public Param[] getParametersInfo() {
        return PARAMS;
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        boolean result = false;
        try {
            final String searchHost = (String) HOSTNAME.lookUp(params);
            final String indexName = (String) INDEX_NAME.lookUp(params);
            final Integer hostport = (Integer) HOSTPORT.lookUp(params);

            if (searchHost != null && hostport != null && indexName != null) {
                result = true;
            }
        } catch (IOException e) {
            // ignore
        }
        return result;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        final String searchHost = (String) getValue(HOSTNAME, params);
        final Integer hostPort = (Integer) getValue(HOSTPORT, params);
        final String indexName = (String) INDEX_NAME.lookUp(params);
        final String searchIndices = (String) SEARCH_INDICES.lookUp(params);
        final String clusterName = (String) getValue(CLUSTERNAME, params);

        final ElasticDataStore dataStore = new ElasticDataStore(searchHost, hostPort, indexName, searchIndices, clusterName);
        dataStore.setDefaultMaxFeatures((Integer) getValue(DEFAULT_MAX_FEATURES, params));
        dataStore.setSourceFilteringEnabled((Boolean) getValue(SOURCE_FILTERING_ENABLED, params));
        dataStore.setScrollEnabled((Boolean)getValue(SCROLL_ENABLED, params));
        dataStore.setScrollSize(((Number)getValue(SCROLL_SIZE, params)).longValue());
        dataStore.setScrollTime((Integer)getValue(SCROLL_TIME_SECONDS, params));
        dataStore.setGridSize((Long) GRID_SIZE.lookUp(params));
        dataStore.setGridThreshold((Double) GRID_THRESHOLD.lookUp(params));
        return dataStore;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

    private Object getValue(Param param, Map<String, Serializable> params) throws IOException {
        final Object value;
        if (param.lookUp(params) != null) {
            value = param.lookUp(params);
        } else {
            value = param.sample;
        }
        return value;
    }

}
