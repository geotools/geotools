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

package mil.nga.giat.data.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.test.ESIntegTestCase;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

@ThreadLeakScope(ThreadLeakScope.Scope.NONE)
public class ElasticTestSupport extends ESIntegTestCase {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ElasticTestSupport.class);

    private static final String LINE_SEPARATOR = "line.separator";

    private static final String PROPERTIES_FILE = "elasticsearch.properties";

    private static final String TEST_FILE = "wifiAccessPoint.json";

    private static final String ACTIVE_MAPPINGS_FILE = "active_mappings.json";

    private static final String INACTIVE_MAPPINGS_FILE = "inactive_mappings.json";

    private static final Pattern STATUS_PATTERN = Pattern.compile(".*\"status_s\"\\s*:\\s*\"(.*?)\".*");

    private static final Pattern ID_PATTERN = Pattern.compile(".*\"id\"\\s*:\\s*\"(\\d+)\".*");

    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    protected static final int numShards = 1;

    protected static final int numReplicas = 0;

    protected String layerName = "active";

    protected int SOURCE_SRID = 4326;

    protected String indexName;

    protected String clusterName;

    protected int port;

    protected boolean scrollEnabled;

    protected long scrollSize;

    protected Path path;

    protected int activeNumShards;

    protected ElasticFeatureSource featureSource;

    protected static ElasticDataStore dataStore;

    protected ElasticLayerConfiguration config;

    protected List<ElasticAttribute> attributes;

    protected Client client;

    protected static String dataPath;

    protected final static int HTTP_TEST_PORT = 9400;

    @BeforeClass
    public static void beforeTestSuite() throws IOException {
        Path baseDir = Paths.get("target" + File.separator + "elasticsearch");
        baseDir.toFile().mkdirs();
        dataPath = Files.createTempDirectory(baseDir, null).toAbsolutePath().toString();
    }

    @Before
    public void beforeTest() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE);
        properties.load(inputStream);
        indexName = properties.getProperty("index_name");
        clusterName = properties.getProperty("cluster_name");
        scrollEnabled = Boolean.valueOf(properties.getProperty("scroll_enabled"));
        scrollSize = Long.valueOf(properties.getProperty("scroll_size"));

        client = ESIntegTestCase.client();
        ElasticDataStore.setClient(new TransportElasticClient(client));
        Map<String,Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        dataStore = (ElasticDataStore) factory.createDataStore(params);
        createIndices();
    }

    @After
    public void afterTest() throws Exception {
        dataStore.dispose();
    }

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        return Settings.builder().put(super.nodeSettings(nodeOrdinal))
                .put("path.home", dataPath)
                .build();
    }

    protected void createIndices() throws IOException {
        final ElasticCompat compat = ElasticCompatLoader.getCompat(null);

        // create index and add mappings
        Settings indexSettings = compat.createSettings("number_of_shards", numShards, 
                "number_of_replicas", numReplicas);
        CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate(indexName);
        Settings createSettings = Settings.settingsBuilder().put("number_of_shards", 1)
				.put("number_of_replicas", 0).build();
        builder.setSettings(createSettings);
        try (Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream(ACTIVE_MAPPINGS_FILE))) {
            s.useDelimiter("\\A");
            builder.addMapping("active", s.next());
        }
        try (Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream(INACTIVE_MAPPINGS_FILE))) {
            s.useDelimiter("\\A");
            builder.addMapping("not-active", s.next());
        }
        builder.execute().actionGet();

        // index documents
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(TEST_FILE); Scanner scanner = new Scanner(inputStream)) {
            String eol = System.getProperty(LINE_SEPARATOR);
            scanner.useDelimiter(eol);
            while (scanner.hasNext()) {
                final String line = scanner.next();
                if (!line.startsWith("#")) {
                    final Matcher idMatcher = ID_PATTERN.matcher(line);
                    final String id;
                    if (idMatcher.matches()) {
                        id = idMatcher.group(1);
                    } else {
                        id = null;
                    }
                    final String layerName;
                    final Matcher statusMatcher = STATUS_PATTERN.matcher(line);
                    if (statusMatcher.matches()) {
                        layerName = statusMatcher.group(1);
                    } else {
                        layerName = null;
                    }

                    bulkRequestBuilder.add(client.prepareIndex(indexName, layerName).setSource(line).setId(id));
                }
            }
            bulkRequestBuilder.execute().actionGet();

            client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();
        }
    }

    protected Map<String,Serializable> createConnectionParams() {
        Map<String,Serializable> params = new HashMap<>();
        params.put(ElasticDataStoreFactory.INDEX_NAME.key, indexName);
        params.put(ElasticDataStoreFactory.CLUSTERNAME.key, clusterName);
        params.put(ElasticDataStoreFactory.SCROLL_ENABLED.key, scrollEnabled);
        params.put(ElasticDataStoreFactory.SCROLL_SIZE.key, scrollSize);
        return params;
    }

    protected void init() throws Exception {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        init(this.layerName);
    }

    protected void init(String layerName) throws Exception {
        init(layerName, "geo");
    }

    protected void init(String layerName, String geometryField) throws Exception {
        this.layerName = layerName;
        attributes = dataStore.getElasticAttributes(new NameImpl(this.layerName));
        config = new ElasticLayerConfiguration(layerName);
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
        featureSource = (ElasticFeatureSource) dataStore.getFeatureSource(this.layerName);
    }

    protected Date date(String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }

    protected Instant instant(String d) throws ParseException {
        return new DefaultInstant(new DefaultPosition(date(d)));
    }

    protected Period period(String d1, String d2) throws ParseException {
        return new DefaultPeriod(instant(d1), instant(d2));
    }

    protected List<SimpleFeature> readFeatures(SimpleFeatureIterator iterator) {
        final List<SimpleFeature> features = new ArrayList<>();
        try {
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
        } finally {
            iterator.close();
        }
        return features;
    }

}
