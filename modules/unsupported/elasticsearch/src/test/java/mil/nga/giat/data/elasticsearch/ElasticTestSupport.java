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
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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

public class ElasticTestSupport {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ElasticTestSupport.class);

    private static final String TEST_FILE = "wifiAccessPoint.json";

    private static final String LEGACY_ACTIVE_MAPPINGS_FILE = "active_mappings_legacy.json";

    private static final String NG_ACTIVE_MAPPINGS_FILE = "active_mappings_ng.json";

    private static final String ACTIVE_MAPPINGS_FILE = "active_mappings.json";

    private static final int numShards = 1;

    private static final int numReplicas = 0;

    protected static final String TYPE_NAME = "active";

    private static final boolean SCROLL_ENABLED = false;

    private static final long SCROLL_SIZE = 20;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectReader mapReader = mapper.readerWithView(Map.class).forType(HashMap.class);

    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    protected static final int PORT = 9200;

    protected int SOURCE_SRID = 4326;

    protected String indexName;

    protected int activeNumShards;

    protected ElasticFeatureSource featureSource;

    protected static ElasticDataStore dataStore;

    protected ElasticLayerConfiguration config;

    protected List<ElasticAttribute> attributes;

    protected RestElasticClient client;

    @Before
    public void beforeTest() throws Exception {
        indexName = "gt_integ_test_" + System.nanoTime();
        client = new RestElasticClient(RestClient.builder(new HttpHost("localhost", PORT, "http")).build());
        Map<String,Serializable> params = createConnectionParams();
        ElasticDataStoreFactory factory = new ElasticDataStoreFactory();
        dataStore = (ElasticDataStore) factory.createDataStore(params);
        createIndices();
    }

    @After
    public void afterTest() throws Exception {
        client.performRequest("DELETE", "/" + indexName, null);
        dataStore.dispose();
        client.close();
    }

    protected void createIndices() throws IOException {
        // create index and add mappings
        Map<String,Object> settings = new HashMap<>();
        settings.put("settings", ImmutableMap.of("number_of_shards", numShards, "number_of_replicas", numReplicas));
        Map<String,Object> mappings = new HashMap<>();
        settings.put("mappings", mappings);
        final String filename;
        if (client.getVersion() < 5) {
            filename = LEGACY_ACTIVE_MAPPINGS_FILE;
        } else if (client.getVersion() > 6.1) {
            filename = NG_ACTIVE_MAPPINGS_FILE;
        } else {
            filename = ACTIVE_MAPPINGS_FILE;
        }
        try (Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream(filename))) {
            s.useDelimiter("\\A");
            Map<String,Object> source = mapReader.readValue(s.next());
            mappings.put(TYPE_NAME, source);
        }
        client.performRequest("PUT", "/" + indexName, settings);

        // add alias
        Map<String,Object> aliases = ImmutableMap.of("actions", ImmutableList.of(ImmutableMap.of("index", indexName, "alias", indexName + "_alias")));
        client.performRequest("PUT", "/_alias", aliases);
    }

    private void indexDocuments(String status) throws IOException {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(TEST_FILE); Scanner scanner = new Scanner(inputStream)) {
            scanner.useDelimiter(System.lineSeparator());
            final StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                final String line = scanner.next();
                if (!line.startsWith("#")) {
                    builder.append(line);
                }
            }
            final Map<String,Object> content = mapReader.readValue(builder.toString());
            @SuppressWarnings("unchecked")
            final List<Map<String,Object>> features = (List<Map<String,Object>>) content.get("features");
            for (final Map<String,Object> featureSource : features) {
                if (featureSource.containsKey("status_s") && featureSource.get("status_s").equals(status)) {
                    final String id = featureSource.containsKey("id") ? (String) featureSource.get("id") : null;
                    client.performRequest("POST", "/" + indexName + "/" + TYPE_NAME + "/" + id, featureSource);
                }
            }

            client.performRequest("POST", "/" + indexName + "/_refresh", null);
        }
    }

    protected Map<String,Serializable> createConnectionParams() {
        Map<String,Serializable> params = new HashMap<>();
        params.put(ElasticDataStoreFactory.HOSTPORT.key, PORT);
        params.put(ElasticDataStoreFactory.INDEX_NAME.key, indexName);
        params.put(ElasticDataStoreFactory.SCROLL_ENABLED.key, SCROLL_ENABLED);
        params.put(ElasticDataStoreFactory.SCROLL_SIZE.key, SCROLL_SIZE);
        return params;
    }

    protected void init() throws Exception {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        init("active");
    }

    protected void init(String layerName) throws Exception {
        init(layerName, "geo");
    }

    protected void init(String status, String geometryField) throws Exception {
        indexDocuments(status);
        attributes = dataStore.getElasticAttributes(new NameImpl(TYPE_NAME));
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
