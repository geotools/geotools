/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class NestedAggGeoHashGridTest {

    private static final int[] AGG_RESULTS = {1, 2, 3, 4, 5};

    private NestedAggGeoHashGrid geohashGrid;

    @Before
    public void setup() {
        this.geohashGrid = new NestedAggGeoHashGrid();
    }

    @Test
    public void testSetParams_defaults() {
        geohashGrid.setParams(null);
        assertEquals(NestedAggGeoHashGrid.DEFAULT_AGG_KEY, geohashGrid.getNestedAggKey());
        assertEquals(NestedAggGeoHashGrid.DEFAULT_METRIC_KEY, geohashGrid.getMetricKey());
        assertEquals(GeoHashGrid.VALUE_KEY, geohashGrid.getValueKey());
        assertEquals(NestedAggGeoHashGrid.SELECT_LARGEST, geohashGrid.getSelectionStrategy());
        assertEquals(NestedAggGeoHashGrid.RASTER_FROM_VALUE, geohashGrid.getRasterStrategy());
        assertNull(geohashGrid.getTermsMap());
    }

    @Test
    public void testSetParams() {
        String aggKey = "myagg";
        String metricKey = "mymetric";
        String valueKey = "myvalue";
        List<String> params = new ArrayList<>();
        params.add(aggKey);
        params.add(metricKey);
        params.add(valueKey);
        params.add(NestedAggGeoHashGrid.SELECT_SMALLEST);
        params.add(NestedAggGeoHashGrid.RASTER_FROM_KEY);
        params.add("key1:1;key2:2");
        geohashGrid.setParams(params);
        assertEquals(aggKey, geohashGrid.getNestedAggKey());
        assertEquals(metricKey, geohashGrid.getMetricKey());
        assertEquals(valueKey, geohashGrid.getValueKey());
        assertEquals(NestedAggGeoHashGrid.SELECT_SMALLEST, geohashGrid.getSelectionStrategy());
        assertEquals(NestedAggGeoHashGrid.RASTER_FROM_KEY, geohashGrid.getRasterStrategy());
        Map<String, Integer> termsMap = geohashGrid.getTermsMap();
        assertEquals(2, termsMap.size());
        assertEquals(new Integer(1), termsMap.get("key1"));
        assertEquals(new Integer(2), termsMap.get("key2"));
    }

    @Test
    public void testSetParams_ignoreInvalidParams() {
        String aggKey = "myagg";
        String metricKey = "mymetric";
        String valueKey = "myvalue";
        List<String> params = new ArrayList<>();
        params.add(aggKey);
        params.add(metricKey);
        params.add(valueKey);
        params.add("invalid token");
        params.add("invalid token");
        geohashGrid.setParams(params);
        assertEquals(aggKey, geohashGrid.getNestedAggKey());
        assertEquals(metricKey, geohashGrid.getMetricKey());
        assertEquals(valueKey, geohashGrid.getValueKey());
        assertEquals(NestedAggGeoHashGrid.SELECT_LARGEST, geohashGrid.getSelectionStrategy());
        assertEquals(NestedAggGeoHashGrid.RASTER_FROM_VALUE, geohashGrid.getRasterStrategy());
        assertNull(geohashGrid.getTermsMap());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetParams_notEnoughParameters() {
        geohashGrid.setParams(new ArrayList<>());
    }

    @Test
    public void testComputeCellValue() {
        Number rasterValue = geohashGrid.computeCellValue(TestUtil.createAggBucket(NestedAggGeoHashGrid.DEFAULT_AGG_KEY, AGG_RESULTS));
        assertEquals(5, rasterValue);
    }

    @Test
    public void testSelectLargest() {
        Number rasterValue = geohashGrid.selectLargest(TestUtil.createBuckets(AGG_RESULTS));
        assertEquals(5, rasterValue);
    }

    @Test
    public void testSelectSmallest() {
        Number rasterValue = geohashGrid.selectSmallest(TestUtil.createBuckets(AGG_RESULTS));
        assertEquals(1, rasterValue);
    }

    @Test
    public void testBucketToRaster_rasterFromValue() {
        Number bucketValue = 5.0;
        Number rasterValue = geohashGrid.bucketToRaster("bucket_key", bucketValue);
        assertEquals(bucketValue, rasterValue);
    }

    @Test
    public void testBucketToRaster_rasterFromNumericKey() {
        List<String> params = new ArrayList<>();
        params.add("aggKey");
        params.add("metricKey");
        params.add("valueKey");
        params.add(NestedAggGeoHashGrid.SELECT_SMALLEST);
        params.add(NestedAggGeoHashGrid.RASTER_FROM_KEY);
        geohashGrid.setParams(params);
        Number rasterValue = geohashGrid.bucketToRaster("1.0", 5.0);
        assertEquals(1.0, rasterValue);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBucketToRaster_rasterFromNumericKey_keyIsString() {
        List<String> params = new ArrayList<>();
        params.add("aggKey");
        params.add("metricKey");
        params.add("valueKey");
        params.add(NestedAggGeoHashGrid.SELECT_SMALLEST);
        params.add(NestedAggGeoHashGrid.RASTER_FROM_KEY);
        geohashGrid.setParams(params);
        geohashGrid.bucketToRaster("I am not a number!", 5.0);
    }

    @Test
    public void testBucketToRaster_rasterFromStringKey() {
        List<String> params = new ArrayList<>();
        params.add("aggKey");
        params.add("metricKey");
        params.add("valueKey");
        params.add(NestedAggGeoHashGrid.SELECT_SMALLEST);
        params.add(NestedAggGeoHashGrid.RASTER_FROM_KEY);
        params.add("key1:1;key2:2");
        geohashGrid.setParams(params);
        Number rasterValue = geohashGrid.bucketToRaster("key1", 5.0);
        assertEquals(1, rasterValue);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBucketToRaster_rasterFromStringKey_keyNotInMap() {
        List<String> params = new ArrayList<>();
        params.add("aggKey");
        params.add("metricKey");
        params.add("valueKey");
        params.add(NestedAggGeoHashGrid.SELECT_SMALLEST);
        params.add(NestedAggGeoHashGrid.RASTER_FROM_KEY);
        params.add("key1:1;key2:2");
        geohashGrid.setParams(params);
        geohashGrid.bucketToRaster("key3", 5.0);
    }

}
