/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentState;
import org.geotools.feature.SchemaException;
import org.junit.Before;
import org.junit.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class ElasticAggregationReaderTest {

    private ContentState state;

    private List<ElasticHit> hits;

    private Map<String, ElasticAggregation> aggregations;

    private ElasticFeatureReader reader;

    private SimpleFeature feature;

    private ObjectMapper mapper;

    @Before
    public void setup() throws SchemaException {
        SimpleFeatureType featureType = DataUtilities.createType("test", "name:String,_aggregation:java.util.HashMap");
        state = new ContentState(null);
        state.setFeatureType(featureType);
        hits = new ArrayList<>();
        aggregations = new LinkedHashMap<>();
        mapper = new ObjectMapper();
    }

    @Test
    public void testNoAggregations() {
        assertFalse((new ElasticFeatureReader(state, hits, aggregations, 0)).hasNext());
    }

    @Test
    public void testBuckets() throws IOException {
        ElasticAggregation aggregation = new ElasticAggregation();
        aggregation.setBuckets(new ArrayList<>());
        aggregations.put("test", aggregation);
        assertFalse((new ElasticFeatureReader(state, hits, aggregations, 0)).hasNext());

        aggregation.setBuckets(ImmutableList.of(ImmutableMap.of("key1", "value1"), ImmutableMap.of("key2", "value2")));
        reader = new ElasticFeatureReader(state, hits, aggregations, 0);
        assertTrue(reader.hasNext());
        feature = reader.next();
        assertNotNull(feature.getAttribute("_aggregation"));
        assertEquals(
                ImmutableSet.of("key1"),
                byteArrayToMap(feature.getAttribute("_aggregation")).keySet());
        assertTrue(reader.hasNext());
        feature = reader.next();
        assertNotNull(feature.getAttribute("_aggregation"));
        assertEquals(
                ImmutableSet.of("key2"),
                byteArrayToMap(feature.getAttribute("_aggregation")).keySet());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testMultipleAggregations() throws IOException {
        ElasticAggregation aggregation = new ElasticAggregation();
        aggregation.setBuckets(ImmutableList.of(ImmutableMap.of("key1", "value1")));
        aggregations.put("test", aggregation);
        aggregation = new ElasticAggregation();
        aggregation.setBuckets(ImmutableList.of(ImmutableMap.of("key2", "value2")));
        aggregations.put("test2", aggregation);

        reader = new ElasticFeatureReader(state, hits, aggregations, 0);
        assertTrue(reader.hasNext());
        feature = reader.next();
        assertNotNull(feature.getAttribute("_aggregation"));
        assertEquals(
                ImmutableSet.of("key1"),
                byteArrayToMap(feature.getAttribute("_aggregation")).keySet());
        assertFalse(reader.hasNext());
    }

    private Map<String, Object> byteArrayToMap(Object bytes) throws IOException {
        return mapper.readValue((byte[]) bytes, new TypeReference<>() {});
    }
}
