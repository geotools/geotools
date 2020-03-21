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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.After;
import org.junit.Before;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class ElasticTestSupport {

    private static final String IMAGE = "docker.elastic.co/elasticsearch/elasticsearch-oss";

    private static final String VERSION_PROPERTY_NAME = "elastic.test.version";

    private static ElasticsearchContainer elasticsearch;

    static {
        elasticsearch =
                new ElasticsearchContainer(IMAGE + ":" + System.getProperty(VERSION_PROPERTY_NAME));
        elasticsearch.start();
    }

    private static final String TEST_FILE = "wifiAccessPoint.json";

    private static final String LEGACY_ACTIVE_MAPPINGS_FILE = "active_mappings_legacy.json";

    private static final String NG_ACTIVE_MAPPINGS_FILE = "active_mappings_ng.json";

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

    String host;

    int port;

    String indexName;

    ElasticDataStore dataStore;

    ElasticFeatureSource featureSource;

    ElasticLayerConfiguration config;

    ElasticClient client;

    @Before
    public void beforeTest() throws Exception {
        host = elasticsearch.getContainerIpAddress();
        port = elasticsearch.getFirstMappedPort();
        indexName = "gt_integ_test_" + System.nanoTime();
        client =
                new RestElasticClient(RestClient.builder(new HttpHost(host, port, "http")).build());
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

    private void createIndices() throws IOException {
        // create index and add mappings
        Map<String, Object> settings = new HashMap<>();
        settings.put(
                "settings",
                ImmutableMap.of("number_of_shards", numShards, "number_of_replicas", numReplicas));
        final String filename;
        if (client.getVersion() < 5) {
            filename = LEGACY_ACTIVE_MAPPINGS_FILE;
        } else if (client.getVersion() > 6.1) {
            filename = NG_ACTIVE_MAPPINGS_FILE;
        } else {
            filename = ACTIVE_MAPPINGS_FILE;
        }
        final InputStream resource = ClassLoader.getSystemResourceAsStream(filename);
        if (resource != null) {
            try (Scanner s = new Scanner(resource)) {
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
        performRequest("PUT", "/" + indexName, settings);

        // add alias
        Map<String, Object> aliases =
                ImmutableMap.of(
                        "actions",
                        ImmutableList.of(
                                ImmutableMap.of(
                                        "index", indexName, "alias", indexName + "_alias")));
        performRequest("PUT", "/_alias", aliases);
    }

    private void indexDocuments(String status) throws IOException {
        final InputStream inputStream = ClassLoader.getSystemResourceAsStream(TEST_FILE);
        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream)) {
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
                final List<Map<String, Object>> features =
                        (List<Map<String, Object>>) content.get("features");
                for (final Map<String, Object> featureSource : features) {
                    if (featureSource.containsKey("status_s")
                            && featureSource.get("status_s").equals(status)) {
                        final String id =
                                featureSource.containsKey("id")
                                        ? (String) featureSource.get("id")
                                        : null;
                        final String typeName = client.getVersion() < 7 ? TYPE_NAME : "_doc";
                        performRequest(
                                "POST", "/" + indexName + "/" + typeName + "/" + id, featureSource);
                    }
                }

                performRequest("POST", "/" + indexName + "/_refresh", null);
            } finally {
                inputStream.close();
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

    void init() throws Exception {
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

    private void performRequest(String method, String endpoint, Map<String, Object> body)
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
