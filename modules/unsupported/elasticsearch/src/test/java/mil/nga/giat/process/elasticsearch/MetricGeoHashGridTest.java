/**
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

public class MetricGeoHashGridTest {

    private MetricGeoHashGrid geohashGrid;

    @Before
    public void setup() {
        this.geohashGrid = new MetricGeoHashGrid();
    }

    @Test
    public void testSetParams_defaults() {
        geohashGrid.setParams(null);
        assertEquals(MetricGeoHashGrid.DEFAULT_METRIC_KEY, geohashGrid.getMetricKey());
        assertEquals(GeoHashGrid.VALUE_KEY, geohashGrid.getValueKey());
    }

    @Test
    public void testSetParams() {
        String metricKey = "mymetric";
        String valueKey = "myvalue";
        List<String> params = new ArrayList<String>();
        params.add(metricKey);
        params.add(valueKey);
        geohashGrid.setParams(params);
        assertEquals(metricKey, geohashGrid.getMetricKey());
        assertEquals(valueKey, geohashGrid.getValueKey());
    }

    @Test
    public void testSetParams_justMetric() {
        String metricKey = "mymetric";
        List<String> params = new ArrayList<String>();
        params.add(metricKey);
        geohashGrid.setParams(params);
        assertEquals(metricKey, geohashGrid.getMetricKey());
        assertEquals(GeoHashGrid.VALUE_KEY, geohashGrid.getValueKey());
    }

    @Test
    public void testComputeCellValue() {
        int value = 5;
        Map<String,Object> metricBucket = TestUtil.createMetricBucket(1, MetricGeoHashGrid.DEFAULT_METRIC_KEY, GeoHashGrid.VALUE_KEY, value);
        Number rasterValue = geohashGrid.computeCellValue(metricBucket);
        assertEquals(value, rasterValue);
    }

}
