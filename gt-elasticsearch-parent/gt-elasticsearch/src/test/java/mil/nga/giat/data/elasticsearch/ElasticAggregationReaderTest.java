package mil.nga.giat.data.elasticsearch;

import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentState;
import org.geotools.feature.SchemaException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElasticAggregationReaderTest {

    private ContentState state;

    private List<ElasticHit> hits;

    private Map<String,ElasticAggregation> aggregations;

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

        aggregation.setBuckets(ImmutableList.of(ImmutableMap.of("key1","value1"), ImmutableMap.of("key2","value2")));
        reader = new ElasticFeatureReader(state, hits, aggregations, 0);
        assertTrue(reader.hasNext());
        feature = reader.next();
        assertNotNull(feature.getAttribute("_aggregation"));
        assertEquals(ImmutableSet.of("key1"), byteArrayToMap(feature.getAttribute("_aggregation")).keySet());
        assertTrue(reader.hasNext());
        feature = reader.next();
        assertNotNull(feature.getAttribute("_aggregation"));
        assertEquals(ImmutableSet.of("key2"), byteArrayToMap(feature.getAttribute("_aggregation")).keySet());
        assertFalse(reader.hasNext());
    }

    @Test
    public void testMultipleAggregations() throws IOException {
        ElasticAggregation aggregation = new ElasticAggregation();
        aggregation.setBuckets(ImmutableList.of(ImmutableMap.of("key1","value1")));
        aggregations.put("test", aggregation);
        aggregation = new ElasticAggregation();
        aggregation.setBuckets(ImmutableList.of(ImmutableMap.of("key2","value2")));
        aggregations.put("test2", aggregation);

        reader = new ElasticFeatureReader(state, hits, aggregations, 0);
        assertTrue(reader.hasNext());
        feature = reader.next();
        assertNotNull(feature.getAttribute("_aggregation"));
        assertEquals(ImmutableSet.of("key1"), byteArrayToMap(feature.getAttribute("_aggregation")).keySet());
        assertFalse(reader.hasNext());
    }

    private Map<String,Object> byteArrayToMap(Object bytes) throws IOException {
        final Map<String,Object> data = mapper.readValue((byte[]) bytes, new TypeReference<Map<String,Object>>() {});
        return data;
    }
}
