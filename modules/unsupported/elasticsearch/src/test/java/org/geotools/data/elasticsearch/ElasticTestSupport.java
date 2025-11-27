/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.temporal.Instant;
import org.geotools.api.temporal.Period;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;

public class ElasticTestSupport {

    private static final String IMAGE_PROPERTY_NAME = "elastic.test.image";

    /** The pure Apache licensed version */
    private static final String DEFAULT_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch-oss";

    private static final String VERSION_PROPERTY_NAME = "elastic.test.version";

    /** Last version provided on the OSS build */
    private static final String DEFAULT_VERSION = "7.10.2";

    private static ElasticsearchContainer elasticsearch;

    private static final String TEST_FILE = "wifiAccessPoint.json";

    private static final String LEGACY_ACTIVE_MAPPINGS_FILE = "active_mappings_legacy.json";

    private static final String NG_ACTIVE_MAPPINGS_FILE = "active_mappings_ng.json";

    private static final String V8_ACTIVE_MAPPINGS_FILE = "active_mappings_v8.json";

    private static final String ACTIVE_MAPPINGS_FILE = "active_mappings.json";

    private static final int numShards = 1;

    private static final int numReplicas = 0;

    private static final boolean SCROLL_ENABLED = false;

    private static final long SCROLL_SIZE = 20;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectReader mapReader =
            mapper.readerWithView(Map.class).forType(HashMap.class);

    static final String TYPE_NAME = "active";

    static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    static final int SOURCE_SRID = 4326;

    protected String host;

    protected int port;

    protected String indexName;

    protected ElasticDataStore dataStore;

    protected ElasticFeatureSource featureSource;

    protected ElasticLayerConfiguration config;

    protected ElasticClient client;

    protected static final Logger log = Logging.getLogger(ElasticTestSupport.class);

    @BeforeClass
    public static void startContainer() {
        String image = System.getProperty(IMAGE_PROPERTY_NAME, DEFAULT_IMAGE);
        String version = System.getProperty(VERSION_PROPERTY_NAME, DEFAULT_VERSION);
        DockerImageName dockerImageName = DockerImageName.parse(image + ":" + version);
        elasticsearch = new ElasticsearchContainer(dockerImageName);
        // Disable security for Elasticsearch 8+/9+ non-OSS images to allow HTTP connections
        // OSS images don't have X-Pack security, so skip these settings
        if (!image.contains("-oss")) {
            elasticsearch.withEnv("xpack.security.enabled", "false");
            elasticsearch.withEnv("xpack.security.http.ssl.enabled", "false");
        }

        // Workaround for cgroup v2 issue in CI environments (GitHub Actions) Elasticsearch fails to read cgroup metrics
        // on some systems with cgroup v2  (at least for the 7.15.2 docker image)
        elasticsearch.withEnv("ES_JAVA_OPTS", "-Djdk.internal.platform.CgroupSubsystemController.enabled=false");

        final String regex = ".*(\"message\":\\s?\"started[\\s?|\"].*|] started\n$)";
        WaitStrategy waitStrategy = Wait.forLogMessage(regex, 1);
        // In CI environments containers may take longer to start up due to resource constraints
        // Use longer timeout in CI (GitHub Actions sets CI=true)
        final boolean ciEnvironment = System.getenv("CI") != null;
        int timeoutSeconds = ciEnvironment ? 60 : 15;
        waitStrategy = waitStrategy.withStartupTimeout(Duration.ofSeconds(timeoutSeconds));
        elasticsearch.setWaitStrategy(waitStrategy);

        log.warning("starting " + dockerImageName);
        Stopwatch sw = Stopwatch.createStarted();
        elasticsearch.start();
        log.warning(dockerImageName + " started in " + sw.stop());
    }

    private static void enableIdFieldDataIfNeeded(ElasticClient client) throws IOException {
        // Enable _id field data access for Elasticsearch 8+ (required for natural sorting)
        // This is a dynamic cluster setting that must be set via API after startup
        if (client.getVersion() >= 8) {
            try {
                Map<String, Object> settings =
                        ImmutableMap.of("persistent", ImmutableMap.of("indices.id_field_data.enabled", true));
                ((RestElasticClient) client).performRequest("PUT", "/_cluster/settings", settings);
                log.info("Enabled indices.id_field_data for Elasticsearch " + client.getVersion());
            } catch (Exception e) {
                log.warning("Failed to enable indices.id_field_data: " + e.getMessage());
            }
        }
    }

    @AfterClass
    public static void stopContainer() {
        if (elasticsearch != null && elasticsearch.isCreated()) {
            elasticsearch.stop();
        }
    }

    @Before
    public void beforeTest() throws Exception {
        host = elasticsearch.getHost();
        port = elasticsearch.getFirstMappedPort();
        indexName = "gt_integ_test_" + System.nanoTime();
        client = new RestElasticClient(
                RestClient.builder(new HttpHost(host, port, "http")).build());
        enableIdFieldDataIfNeeded(client);
        Map<String, Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        dataStore = (ElasticDataStore) factory.createDataStore(params);
        createIndices();
    }

    @After
    public void afterTest() throws Exception {
        performRequest("DELETE", "/" + indexName, null);
        dataStore.dispose();
        client.close();
    }

    protected void createIndices(ElasticClient client, String indexName) throws IOException {
        // create index and add mappings
        Map<String, Object> settings = new HashMap<>();
        settings.put("settings", ImmutableMap.of("number_of_shards", numShards, "number_of_replicas", numReplicas));
        final String filename;
        if (client.getVersion() < 5) {
            filename = LEGACY_ACTIVE_MAPPINGS_FILE;
        } else if (client.getVersion() >= 8) {
            filename = V8_ACTIVE_MAPPINGS_FILE;
        } else if (client.getVersion() > 6.1) {
            filename = NG_ACTIVE_MAPPINGS_FILE;
        } else {
            filename = ACTIVE_MAPPINGS_FILE;
        }
        final InputStream resource = ClassLoader.getSystemResourceAsStream(filename);
        if (resource != null) {
            try (Scanner s = new Scanner(resource, StandardCharsets.UTF_8)) {
                s.useDelimiter("\\A");
                Map<String, Object> source = mapReader.readValue(s.next());
                if (client.getVersion() < 7) {
                    Map<String, Object> mappings = new HashMap<>();
                    mappings.put(TYPE_NAME, source);
                    settings.put("mappings", mappings);
                } else {
                    settings.put("mappings", source);
                }
            }
        }
        performRequest(client, "PUT", "/" + indexName, settings);

        // add alias
        Map<String, Object> aliasAction =
                ImmutableMap.of("add", ImmutableMap.of("index", indexName, "alias", indexName + "_alias"));
        Map<String, Object> aliases = ImmutableMap.of("actions", ImmutableList.of(aliasAction));
        performRequest(client, "POST", "/_aliases", aliases);
    }

    private void createIndices() throws IOException {
        createIndices(client, indexName);
    }

    private void indexDocuments(String status) throws IOException {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(TEST_FILE)) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                    scanner.useDelimiter(System.lineSeparator());
                    final StringBuilder builder = new StringBuilder();
                    while (scanner.hasNext()) {
                        final String line = scanner.next();
                        if (!line.startsWith("#")) {
                            builder.append(line);
                        }
                    }
                    final Map<String, Object> content = mapReader.readValue(builder.toString());
                    @SuppressWarnings("unchecked")
                    final List<Map<String, Object>> features = (List<Map<String, Object>>) content.get("features");
                    for (final Map<String, Object> featureSource : features) {
                        if (featureSource.containsKey("status_s")
                                && featureSource.get("status_s").equals(status)) {
                            final String id = featureSource.containsKey("id") ? (String) featureSource.get("id") : null;
                            final String typeName = client.getVersion() < 7 ? TYPE_NAME : "_doc";
                            performRequest("POST", "/" + indexName + "/" + typeName + "/" + id, featureSource);
                        }
                    }

                    performRequest("POST", "/" + indexName + "/_refresh", null);
                }
            }
        }
    }

    Map<String, Serializable> createConnectionParams() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(ElasticDataStoreFactory.HOSTNAME.key, host);
        params.put(ElasticDataStoreFactory.HOSTPORT.key, port);
        params.put(ElasticDataStoreFactory.INDEX_NAME.key, indexName);
        params.put(ElasticDataStoreFactory.SCROLL_ENABLED.key, SCROLL_ENABLED);
        params.put(ElasticDataStoreFactory.SCROLL_SIZE.key, SCROLL_SIZE);
        return params;
    }

    protected void init() throws Exception {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        init("active");
    }

    void init(String layerName) throws Exception {
        init(layerName, "geo");
    }

    void init(String status, String geometryField) throws Exception {
        indexDocuments(status);
        List<ElasticAttribute> attributes = dataStore.getElasticAttributes(new NameImpl(TYPE_NAME));
        config = new ElasticLayerConfiguration(TYPE_NAME);
        List<ElasticAttribute> layerAttributes = new ArrayList<>();
        for (ElasticAttribute attribute : attributes) {
            attribute.setUse(true);
            if (geometryField.equals(attribute.getName())) {
                ElasticAttribute copy = new ElasticAttribute(attribute);
                copy.setDefaultGeometry(true);
                layerAttributes.add(copy);
            } else {
                layerAttributes.add(attribute);
            }
        }
        config.getAttributes().clear();
        config.getAttributes().addAll(layerAttributes);
        dataStore.setLayerConfiguration(config);
        featureSource = (ElasticFeatureSource) dataStore.getFeatureSource(TYPE_NAME);
    }

    private void performRequest(String method, String endpoint, Map<String, Object> body) throws IOException {
        performRequest(client, method, endpoint, body);
    }

    protected void performRequest(ElasticClient client, String method, String endpoint, Map<String, Object> body)
            throws IOException {
        ((RestElasticClient) client).performRequest(method, endpoint, body);
    }

    private Date date(String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }

    private Instant instant(String d) throws ParseException {
        return new DefaultInstant(new DefaultPosition(date(d)));
    }

    Period period(String d1, String d2) throws ParseException {
        return new DefaultPeriod(instant(d1), instant(d2));
    }

    List<SimpleFeature> readFeatures(SimpleFeatureIterator iterator) {
        final List<SimpleFeature> features = new ArrayList<>();
        try (iterator) {
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
        }
        return features;
    }
}
