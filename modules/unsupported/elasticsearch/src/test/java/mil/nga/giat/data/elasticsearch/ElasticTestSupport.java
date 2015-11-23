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

import static org.elasticsearch.node.NodeBuilder.*;
import mil.nga.giat.data.elasticsearch.ElasticAttribute;
import mil.nga.giat.data.elasticsearch.ElasticDataStore;
import mil.nga.giat.data.elasticsearch.ElasticDataStoreFactory;
import mil.nga.giat.data.elasticsearch.ElasticFeatureSource;
import mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

public abstract class ElasticTestSupport {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ElasticTestSupport.class);

    private static final String PROPERTIES_FILE = "elasticsearch.properties";

    private static final String TEST_FILE = "wifiAccessPoint.json";

    private static final String ACTIVE_MAPPINGS_FILE = "active_mappings.json";

    private static final String INACTIVE_MAPPINGS_FILE = "inactive_mappings.json";

    private static final Pattern STATUS_PATTERN = Pattern.compile(".*\"status_s\"\\s*:\\s*\"(.*?)\".*");

    private static final Pattern ID_PATTERN = Pattern.compile(".*\"id\"\\s*:\\s*\"(\\d+)\".*");

    protected static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    protected static int numShards = 5;
    
    protected String layerName = "active";

    protected int SOURCE_SRID = 4326;

    protected static String indexName;

    protected static String clusterName;
    
    protected static String dataPath;
    
    protected static int port;
    
    protected static boolean scrollEnabled;
    
    protected static long scrollSize;

    protected static int activeNumShards;
    
    protected ElasticFeatureSource featureSource;

    protected static ElasticDataStore dataStore;

    private List<ElasticAttribute> attributes;
    
    private static Node node;
    
    @BeforeClass
    public static synchronized void suiteSetup() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE);
        properties.load(inputStream);
        indexName = properties.getProperty("index_name");
        clusterName = properties.getProperty("cluster_name");
        scrollEnabled = Boolean.valueOf(properties.getProperty("scroll_enabled"));
        scrollSize = Long.valueOf(properties.getProperty("scroll_size"));

        if (node == null || node.isClosed()) {
            connect();
        }
        
        Map<String,Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        dataStore = (ElasticDataStore) factory.createDataStore(params);        
    }
    
    @AfterClass
    public static synchronized void suiteTearDown() throws Exception {
        dataStore.dispose();
        //node.close();
    }

    private static void connect() throws Exception {
        Path baseDir = Paths.get("target/elasticsearch");
        baseDir.toFile().mkdirs();
        dataPath = Files.createTempDirectory(baseDir, null).toAbsolutePath().toString();
        
        LOGGER.info("Creating local test Elasticsearch cluster (path.home=" + dataPath + ")");
        Settings build = ImmutableSettings.builder()
                .put("path.data", dataPath)
                .put("http.enabled", false)
                .build();
        node = nodeBuilder()
                .settings(build)
                .local(true)
                .clusterName(clusterName)
                .node();
        Client client = node.client();

        // create index and add mappings
        Settings indexSettings = ImmutableSettings.settingsBuilder()
                .put("number_of_shards", numShards)
                .build();
        CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate(indexName);
        builder.setSettings(indexSettings);
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
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(TEST_FILE);
        try (Scanner scanner = new Scanner(inputStream)) {
            scanner.useDelimiter("\\n");
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

                    client.prepareIndex(indexName, layerName)
                    .setSource(line)
                    .setId(id)
                    .execute().actionGet();
                }
            }
        }
        LOGGER.info("Done setting up Elasticsearch");
    }

    protected static Map<String,Serializable> createConnectionParams() {
        Map<String,Serializable> params = new HashMap<>();
        params.put(ElasticDataStoreFactory.INDEX_NAME.key, indexName);
        params.put(ElasticDataStoreFactory.CLUSTERNAME.key, clusterName);
        params.put(ElasticDataStoreFactory.DATA_PATH.key, dataPath);
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
        ElasticLayerConfiguration config = new ElasticLayerConfiguration(layerName);
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
