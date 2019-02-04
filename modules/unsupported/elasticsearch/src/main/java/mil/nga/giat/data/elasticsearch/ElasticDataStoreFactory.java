/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

import mil.nga.giat.data.elasticsearch.ElasticDataStore.ArrayEncoding;
import org.geotools.data.Parameter;
import org.geotools.util.logging.Logging;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data store factory that creates {@linkplain ElasticDataStore} instances.
 *
 */
@SuppressWarnings("WeakerAccess")
public class ElasticDataStoreFactory implements DataStoreFactorySpi {

    /** The logger for this class */
    private final static Logger LOGGER = Logging.getLogger(ElasticDataStoreFactory.class);

    /**
     * System property name for flag indicating whether to force use of authenticated GeoServer user when submitting requests.
     */
    private final static String FORCE_RUNAS_PROPERTY = "org.geoserver.elasticsearch.xpack.force-runas";

    /** Counter of HTTP threads we generate */
    static final AtomicInteger httpThreads = new AtomicInteger(1);

    public static final String DISPLAY_NAME = "Elasticsearch";

    public static final String DESCRIPTION = "Elasticsearch Index";

    /** Cluster hostnames. **/
    public static final Param HOSTNAME = new Param("elasticsearch_host", String.class,
            "Host(s) with optional HTTP scheme and port.", true, "localhost");

    /** Cluster client port. **/
    public static final Param HOSTPORT = new Param("elasticsearch_port", Integer.class, 
            "Default HTTP port. Ignored if the host includes the port.", true, 9200);

    /** Index name. **/
    public static final Param INDEX_NAME = new Param("index_name", String.class,
            "Index defining type (supports wildcard)", true);

    /** Username. */
    public static final Param USER = new Param("user", String.class,
            "Elasticsearch user", isForceRunas());

    /** Password. */
    public static final Param PASSWD = new Param("passwd", String.class,
            "Elasticsearch password",  isForceRunas(), null,
            Collections.singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));

    public static final Param RUNAS_GEOSERVER_USER = new Param("runas_geoserver_user", Boolean.class,
            "Flag indicating whether to submit document search requests on behalf of the authenticated GeoServer user", false, isForceRunas());

    /** Username. */
    public static final Param PROXY_USER = new Param("proxy_user", String.class,
            "Elasticsearch user for document search requests. If not provided the default user is used for all requests.", isForceRunas());

    /** Password. */
    public static final Param PROXY_PASSWD = new Param("proxy_passwd", String.class,
            "Elasticsearch proxy user password.", isForceRunas(),null,
            Collections.singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));

    public static final Param SSL_REJECT_UNAUTHORIZED = new Param("ssl_reject_unauthorized", Boolean.class,
            "Whether to validate the server certificate during the SSL handshake for https connections", false, true);

    public static final Param SOURCE_FILTERING_ENABLED = new Param("source_filtering_enabled", Boolean.class,
            "Enable source field filtering", false, false);

    public static final Param SCROLL_ENABLED = new Param("scroll_enabled", Boolean.class,
            "Use scan search type instead of dfs_query_then_fetch", false, false);

    public static final Param SCROLL_SIZE = new Param("scroll_size", Long.class,
            "Scroll size (ignored if scroll_enabled=false)", false, 20);

    public static final Param SCROLL_TIME_SECONDS = new Param("scroll_time", Integer.class,
            "Time to keep the scroll open in seconds (ignored if scroll_enabled=false)", false, 120);

    public static final Param DEFAULT_MAX_FEATURES = new Param("default_max_features", Integer.class,
            "Default max features", false, 100);

    public static final Param ARRAY_ENCODING = new Param("array_encoding", String.class,
            "Array encoding strategy. Allowed values are \"JSON\" (keep arrays) "
            + " and \"CSV\" (URL encode and join array elements).", false, "JSON");

    public static final Param GRID_SIZE = new Param("grid_size", Long.class,
            "Hint for Geohash grid size (nrow*ncol)", false, 10000L);

    public static final Param GRID_THRESHOLD = new Param("grid_threshold",  Double.class, 
            "Geohash grid aggregation precision will be the minimum necessary to satisfy actual_grid_size/grid_size>grid_threshold", false, 0.05);

    public static final Param[] PARAMS = {
            HOSTNAME,
            HOSTPORT,
            INDEX_NAME,
            USER,
            PASSWD,
            RUNAS_GEOSERVER_USER,
            PROXY_USER,
            PROXY_PASSWD,
            SSL_REJECT_UNAUTHORIZED,
            SOURCE_FILTERING_ENABLED,
            SCROLL_ENABLED,
            SCROLL_SIZE,
            SCROLL_TIME_SECONDS,
            DEFAULT_MAX_FEATURES,
            ARRAY_ENCODING,
            GRID_SIZE,
            GRID_THRESHOLD
    };

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
        final String user = getValue(USER, params);
        final String passwd = getValue(PASSWD, params);
        final String proxyUser = getValue(PROXY_USER, params);
        final String proxyPasswd = getValue(PROXY_PASSWD, params);

        final RestClient client = createRestClient(params, user, passwd);
        final RestClient proxyClient = proxyUser != null ? createRestClient(params, proxyUser, proxyPasswd) : null;
        return createDataStore(client, proxyClient, params);
    }

    public DataStore createDataStore(RestClient client, RestClient proxyClient, Map<String, Serializable> params) throws IOException {
        final String indexName = (String) INDEX_NAME.lookUp(params);
        final String arrayEncoding = getValue(ARRAY_ENCODING, params);
        final boolean runAsGeoServerUser = getValue(RUNAS_GEOSERVER_USER, params);
        if (isForceRunas() && !runAsGeoServerUser) {
            throw new IllegalArgumentException(RUNAS_GEOSERVER_USER.key + " is disabled but " + FORCE_RUNAS_PROPERTY + " is set. "
                    + "Enable " + RUNAS_GEOSERVER_USER.key + " or unset " + FORCE_RUNAS_PROPERTY + " in the system environment.");
        }

        final ElasticDataStore dataStore = new ElasticDataStore(client, proxyClient, indexName, runAsGeoServerUser);
        dataStore.setDefaultMaxFeatures(getValue(DEFAULT_MAX_FEATURES, params));
        dataStore.setSourceFilteringEnabled(getValue(SOURCE_FILTERING_ENABLED, params));
        dataStore.setScrollEnabled(getValue(SCROLL_ENABLED, params));
        dataStore.setScrollSize(((Number)getValue(SCROLL_SIZE, params)).longValue());
        dataStore.setScrollTime(getValue(SCROLL_TIME_SECONDS, params));
        dataStore.setArrayEncoding(ArrayEncoding.valueOf(arrayEncoding.toUpperCase()));
        dataStore.setGridSize((Long) GRID_SIZE.lookUp(params));
        dataStore.setGridThreshold((Double) GRID_THRESHOLD.lookUp(params));
        return dataStore;
    }

    public RestClient createRestClient(Map<String, Serializable> params) throws IOException {
        return createRestClient(params, null, null);
    }

    private RestClient createRestClient(Map<String, Serializable> params, String user, String password) throws IOException {
        final String hostName = getValue(HOSTNAME, params);
        final String[] hosts = hostName.split(",");
        final Integer defaultPort = getValue(HOSTPORT, params);
        final Boolean sslRejectUnauthorized = getValue(SSL_REJECT_UNAUTHORIZED, params);
        final String adminUser = getValue(USER, params);
        final String type = user == null || adminUser == null || user.equals(adminUser) ? "ADMIN" : "PROXY_USER";

        final Pattern pattern = Pattern.compile("(?<scheme>https?)?(://)?(?<host>[^:]+):?(?<port>\\d+)?");
        final HttpHost[] httpHosts = new HttpHost[hosts.length];
        final AuthScope[] auths = new AuthScope[hosts.length];
        for (int index=0; index < hosts.length; index++) {
            final Matcher matcher = pattern.matcher(hosts[index].trim());
            if (matcher.find()) {
                final String scheme = matcher.group("scheme") != null ? matcher.group("scheme") : "http";
                final String host = matcher.group("host");
                final Integer port = matcher.group("port") != null ? Integer.valueOf(matcher.group("port")) : defaultPort;
                httpHosts[index] = new HttpHost(host, port, scheme);
                auths[index] = new AuthScope(host, port);
            } else {
                throw new IOException("Unable to parse host");
            }
        }

        final RestClientBuilder builder = createClientBuilder(httpHosts);

        if (user != null) {
            builder.setRequestConfigCallback((b) -> {
                LOGGER.finest(String.format("Calling %s setRequestConfigCallback", type));
                return b.setAuthenticationEnabled(true);
            });
        }

        builder.setHttpClientConfigCallback((httpClientBuilder) -> {
            LOGGER.finest(String.format("Calling %s customizeHttpClient", type));

            httpClientBuilder.setThreadFactory((run) -> {
                final Thread thread = new Thread(run);
                thread.setDaemon(true);
                thread.setName(String.format("esrest-asynchttp-%s-%d", type, httpThreads.getAndIncrement()));
                return thread;
            });

            httpClientBuilder.useSystemProperties();

            if (!sslRejectUnauthorized) {
                httpClientBuilder.setSSLHostnameVerifier((host,session) -> true);
                try {
                    httpClientBuilder.setSSLContext(SSLContextBuilder.create().loadTrustMaterial((chain,authType) ->true).build());
                } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                    throw new UncheckedIOException(new IOException("Unable to create SSLContext", e));
                }
            }

            if (user != null) {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                final Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials(user, password);
                for (AuthScope scope : auths) {
                    credentialsProvider.setCredentials(scope, credentials);
                }

                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }

            return httpClientBuilder;
        });

        LOGGER.fine(String.format("Building a %s RestClient for %s @ %s:%d", type, user, hostName, defaultPort));
        return builder.build();
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) {
        return null;
    }

    public RestClientBuilder createClientBuilder(HttpHost[] hosts) {
        return RestClient.builder(hosts);
    }

    private static boolean isForceRunas() {
        return System.getProperty(FORCE_RUNAS_PROPERTY) != null;
    }

    @SuppressWarnings({"unchecked" })
    static <T> T getValue(Param param, Map<String, Serializable> params) throws IOException {
        final Object value;
        if (param.lookUp(params) != null) {
            value = param.lookUp(params);
        } else {
            value = param.sample;
        }
        return (T) value;
    }
}