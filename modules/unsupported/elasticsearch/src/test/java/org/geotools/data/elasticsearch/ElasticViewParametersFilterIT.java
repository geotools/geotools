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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.process.elasticsearch.ElasticBucketVisitor;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import tools.jackson.databind.ObjectMapper;

public class ElasticViewParametersFilterIT extends ElasticTestSupport {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testNativeTermQuery() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<>();
        Map<String, Object> query = ImmutableMap.of("term", ImmutableMap.of("security_ss", "WPA"));
        vparams.put("q", mapper.writeValueAsString(query));
        // check it works both directly and as part of an aggregation
        List<Hints.ClassKey> keys =
                Arrays.asList(Hints.VIRTUAL_TABLE_PARAMETERS, ElasticBucketVisitor.ES_AGGREGATE_BUCKET);
        for (Hints.ClassKey key : keys) {
            Hints hints = new Hints(key, vparams);
            Query q = new Query(featureSource.getSchema().getTypeName());
            q.setHints(hints);
            FilterFactory ff = dataStore.getFilterFactory();
            PropertyIsEqualTo filter = ff.equals(ff.property("speed_is"), ff.literal("300"));
            q.setFilter(filter);
            ContentFeatureCollection features = featureSource.getFeatures(q);
            assertEquals(1, features.size());
            try (SimpleFeatureIterator fsi = features.features()) {
                assertTrue(fsi.hasNext());
                assertEquals(fsi.next().getID(), "active.12");
            }
        }
    }

    @Test
    public void testEncodedNativeTermQuery() throws Exception {
        init("not-active");
        Map<String, String> vparams = new HashMap<>();
        Map<String, Object> query = ImmutableMap.of("term", ImmutableMap.of("security_ss", "WPA"));
        vparams.put("q", URLEncoder.encode(mapper.writeValueAsString(query), "UTF-8"));
        Hints hints = new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vparams);
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setHints(hints);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("speed_is"), ff.literal("300"));
        q.setFilter(filter);
        ContentFeatureCollection features = featureSource.getFeatures(q);
        assertEquals(1, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getID(), "active.12");
        }
    }

    @Test
    public void testNativeBooleanQuery() throws Exception {
        init();
        Map<String, String> vparams = new HashMap<>();
        Map<String, Object> query = ImmutableMap.of(
                "bool",
                ImmutableMap.of(
                        "must",
                        ImmutableMap.of("term", ImmutableMap.of("security_ss", "WPA")),
                        "must_not",
                        ImmutableMap.of("term", ImmutableMap.of("modem_b", true))));
        vparams.put("q", mapper.writeValueAsString(query));
        Hints hints = new Hints(Hints.VIRTUAL_TABLE_PARAMETERS, vparams);
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setHints(hints);
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property("speed_is"), ff.literal("300"));
        q.setFilter(filter);
        ContentFeatureCollection features = featureSource.getFeatures(q);
        assertEquals(2, features.size());
        try (SimpleFeatureIterator fsi = features.features()) {
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getAttribute("modem_b"), false);
            assertTrue(fsi.hasNext());
            assertEquals(fsi.next().getAttribute("modem_b"), false);
        }
    }

    @Test
    public void testNativeAggregation() throws Exception {
        init();
        Query q = new Query(featureSource.getSchema().getTypeName());
        ElasticBucketVisitor elasticBucketVisitor =
                new ElasticBucketVisitor("{\"agg\": {\"geohash_grid\": {\"field\": \"geo\", \"precision\": 3}}}", null);
        featureSource.accepts(q, elasticBucketVisitor, null);
        List<Map<String, Object>> buckets = elasticBucketVisitor.getBuckets();
        // all 11 features in the store accounted for
        assertEquals(10, buckets.size());
        assertEquals(
                11, buckets.stream().mapToInt(b -> (int) b.get("doc_count")).sum());
    }
}
