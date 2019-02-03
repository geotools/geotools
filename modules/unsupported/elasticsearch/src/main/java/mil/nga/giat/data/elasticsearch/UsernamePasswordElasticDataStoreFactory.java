/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base32;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.geotools.data.DataStore;
import org.geotools.util.logging.Logging;

/**
 * Data store factory that creates {@linkplain ElasticDataStore} instances.
 *
 */
public class UsernamePasswordElasticDataStoreFactory extends ElasticDataStoreFactory {
    
    /** Charset for converting bytes to String */
    private static final Charset UTF8 = Charset.forName("utf-8");

    /** The logger for this class */
    protected final static Logger LOGGER = Logging.getLogger(UsernamePasswordElasticDataStoreFactory.class);

    /** The admin user parameter */
    public static final Param ADMIN_USER = new Param("admin_user", String.class, "Admin User", false);

    /** The admin password parameter */
    public static final Param ADMIN_PASSWD = new Param("admin_passwd", String.class, "Admin Password (encrypted)", false);

    /** The proxy user parameter */
    public static final Param PROXY_USER = new Param("proxy_user", String.class, "Proxy User", false);

    /** The proxy password parameter */
    public static final Param PROXY_PASSWD = new Param("proxy_passwd", String.class, "Proxy Password (encrypted)", false);

    /** The display name string in the list of datastores */
    protected static final String DISPLAY_NAME = "Elasticsearch";

    /** The display name description of this datastore */
    protected static final String DESCRIPTION = "Elasticsearch Index with Username and Password Connection";

    /** All the parameters for this data store */
    protected static final Param[] PARAMS = {
        HOSTNAME, 
        HOSTPORT, 
        INDEX_NAME, 
        DEFAULT_MAX_FEATURES, 
        ADMIN_USER,
        ADMIN_PASSWD, 
        PROXY_USER, 
        PROXY_PASSWD, 
        SOURCE_FILTERING_ENABLED, 
        SCROLL_ENABLED, 
        SCROLL_SIZE,
        SCROLL_TIME_SECONDS, 
        ARRAY_ENCODING, 
        GRID_SIZE, 
        GRID_THRESHOLD 
    };

    /** All the RestClients used by this factory */
    private static final Map<String, RestClient> clients = new HashMap<>();

    /** Counter of HTTP threads we generate */
    protected static final AtomicInteger httpThreads = new AtomicInteger(1);

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
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        return createDataStore(getAdminClient(params), getProxyClient(params), params);
    }

    /**
     * Get the Admin Client.
     *
     * @param params Map of String to Serializable
     * @return RestClient
     * @throws IOException when the client can't be created
     */
    private RestClient getAdminClient(Map<String, Serializable> params) throws IOException {

        final String type = "ADMIN";
        final String key = key(type, params);

        final String host = getValue(HOSTNAME, params);
        final Integer port = getValue(HOSTPORT, params);
        final String user = getValue(ADMIN_USER, params);
        final String password = getValue(ADMIN_PASSWD, params);

        return getClient(type, key, host, port, user, password, true);
    }

    /**
     * Get the Proxy Client.
     *
     * @param params Map of String to Serializable
     * @return RestClient
     * @throws IOException when the client can't be created
     */
    private RestClient getProxyClient(Map<String, Serializable> params) throws IOException {

        final String type = "PROXY";
        final String key = key(type, params);

        final String host = getValue(HOSTNAME, params);
        final Integer port = getValue(HOSTPORT, params);
        final String user = getValue(PROXY_USER, params);
        final String password = getValue(PROXY_PASSWD, params);

        return getClient(type, key, host, port, user, password, true);
    }

    /**
     * Get the Client.
     * @param type String
     * @param key  String
     * @param host  String
     * @param port Integer
     * @param user  String
     * @param password  String
     * @param retry boolean
     * 
     * @return RestClient
     * @throws IOException when the client can't be created
     */
    @SuppressWarnings("resource")
    private RestClient getClient(String type, String key, String host, Integer port, String user, String password, boolean retry)
            throws IOException {

        RestClient client = null;
        if (clients.containsKey(key)) {
            client = clients.get(key);
        }

        if (client == null) {
            synchronized (clients) {
                if (clients.containsKey(key)) {
                    client = clients.get(key);
                }

                if (client == null) {
                    client = buildClient(type, host, port, user, password);
                    clients.put(key, client);
                }
            }
        }
        try {
            final StatusLine sl = client.performRequest("GET", "/", Collections.emptyMap()).getStatusLine();
            if (sl.getStatusCode() >= 400) {
                throw new IOException(String.format("Connection Test %d: %s", sl.getStatusCode(), sl.getReasonPhrase()));
            }
        } catch (Exception e) {
            LOGGER.info(String.format("Removing %s:%s connection due to: %s", type, user, e.getMessage()));
            clients.remove(key);
            try {
                client.close();
            } catch (Exception e2) {
                LOGGER.warning(String.format("Failed to close %s:%s connection due to: %s", type, user, e2.getMessage()));
            }
            if (retry) {
                return getClient(type, key, host, port, user, password, false);
            }
            throw new IOException(String.format("Multiple connection attempts failed for %s RestClient to %s @ %s:%d ", 
                    type, user, host, port));
        }

        return client;
    }

    /**
     * Build a RestClient.
     * 
     * @param type String descriptor of this client purpose ADMIN|PROXY
     * @param searchHost String csv of hosts
     * @param hostPort Integer
     * @param user String
     * @param password String
     * @return RestClient
     * @throws IOException when the client can't be created
     */
    private RestClient buildClient(String type, String searchHost, Integer hostPort, String user, String password) {
        final String[] nodes = searchHost.split(",");
        final AuthScope[] auths = new AuthScope[nodes.length];
        final HttpHost[] hosts = new HttpHost[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            final String node = nodes[i];
            auths[i] = new AuthScope(node, hostPort);
            hosts[i] = new HttpHost(node, hostPort, "https");
        }

        final RestClientBuilder builder = createClientBuilder(hosts);

        builder.setRequestConfigCallback((b) -> {
            LOGGER.fine(String.format("Calling %s setRequestConfigCallback", type));
            return b.setAuthenticationEnabled(true);
        });

        builder.setHttpClientConfigCallback((httpClientBuilder) -> {
            LOGGER.fine(String.format("Calling %s customizeHttpClient", type));
            httpClientBuilder.setThreadFactory((run) -> {
                final Thread thread = new Thread(run);
                thread.setDaemon(true);
                thread.setName(String.format("esrest-asynchttp-%s-%d", type, httpThreads.getAndIncrement()));
                return thread;
            });
            httpClientBuilder.useSystemProperties();
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            final Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials(user, password);
            for (AuthScope scope : auths) {
                credentialsProvider.setCredentials(scope, credentials);
            }

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });

        LOGGER.info(String.format("Building a %s RestClient for %s @ %s:%d", type, user, searchHost, hostPort));
        return builder.build();
    }

    protected RestClientBuilder createClientBuilder(HttpHost[] hosts) {
        return RestClient.builder(hosts);
    }

    /**
     * Get the digest key of the type and host, port and users.
     * 
     * @param type String
     * @param params Map of String to Serializable
     * @return String
     * @throws IOException when needed params can't be read.
     */
    private static String key(String type, Map<String, Serializable> params) throws IOException {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Could not get MD5 Digest!", e);
        }

        md.update(type.getBytes(UTF8));
        String host = getValue(HOSTNAME, params);
        md.update(host.getBytes(UTF8));
        Integer port = getValue(HOSTPORT, params);
        md.update(port.byteValue());
        String admin = getValue(ADMIN_USER, params);
        md.update(admin.getBytes(UTF8));
        String proxy = getValue(PROXY_USER, params);
        md.update(proxy.getBytes(UTF8));

        return new Base32().encodeAsString(md.digest());
    }

    protected static Map<String, RestClient> getClients() {
        return clients;
    }
}