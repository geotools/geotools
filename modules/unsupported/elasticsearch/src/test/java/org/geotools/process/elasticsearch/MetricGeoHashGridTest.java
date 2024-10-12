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
package org.geotools.process.elasticsearch;

import static org.junit.Assert.assertEquals;

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
        List<String> params = new ArrayList<>();
        params.add(metricKey);
        params.add(valueKey);
        geohashGrid.setParams(params);
        assertEquals(metricKey, geohashGrid.getMetricKey());
        assertEquals(valueKey, geohashGrid.getValueKey());
    }

    @Test
    public void testSetParams_justMetric() {
        String metricKey = "mymetric";
        List<String> params = new ArrayList<>();
        params.add(metricKey);
        geohashGrid.setParams(params);
        assertEquals(metricKey, geohashGrid.getMetricKey());
        assertEquals(GeoHashGrid.VALUE_KEY, geohashGrid.getValueKey());
    }

    @Test
    public void testComputeCellValue() {
        int value = 5;
        Map<String, Object> metricBucket =
                TestUtil.createMetricBucket(1, MetricGeoHashGrid.DEFAULT_METRIC_KEY, GeoHashGrid.VALUE_KEY, value);
        Number rasterValue = geohashGrid.computeCellValue(metricBucket);
        assertEquals(value, rasterValue);
    }
}
