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

import static org.geotools.geometry.jts.ReferencedEnvelope.EVERYTHING;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;

public class GeohashUtilTest {

    @Test
    public void testComputePrecision() {
        assertEquals(
                1,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 32, 0.9));
        assertEquals(
                2,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 1024, 0.9));
        assertEquals(
                3,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 32768, 0.9));

        assertEquals(
                2,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 1000, 0.9));
        assertEquals(
                3,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 1500, 0.9));

        assertEquals(
                1,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(
                                EVERYTHING.getMinX(),
                                EVERYTHING.getMaxX(),
                                EVERYTHING.getMinY(),
                                EVERYTHING.getMaxY(),
                                null),
                        32,
                        0.9));

        assertEquals(
                1,
                GeohashUtil.computePrecision(new ReferencedEnvelope(-1, 1, -1, 1, null), 0, 0.9));
        assertEquals(
                6,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), (long) 1e9, 0.9));
        assertEquals(
                6,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 1, 1e9));
        assertEquals(
                1,
                GeohashUtil.computePrecision(
                        new ReferencedEnvelope(-180, 180, -90, 90, null), 1, -1e9));
    }

    @Test
    public void updatePrecisionLower() {
        final Map<String, Object> geohashGridAgg =
                new HashMap<>(ImmutableMap.of("field", "name", "precision", 4));
        final Map<String, Map<String, Map<String, Object>>> aggregations =
                ImmutableMap.of("first", ImmutableMap.of("geohash_grid", geohashGridAgg));
        final Map<String, Object> expected =
                ImmutableMap.of(
                        "first",
                        ImmutableMap.of(
                                "geohash_grid", ImmutableMap.of("field", "name", "precision", 2)));
        GeohashUtil.updateGridAggregationPrecision(aggregations, 2);
        assertEquals(expected, aggregations);
    }

    @Test
    public void updatePrecisionHigher() {
        final Map<String, Object> geohashGridAgg =
                new HashMap<>(ImmutableMap.of("field", "name", "precision", 1));
        final Map<String, Map<String, Map<String, Object>>> aggregations =
                ImmutableMap.of("first", ImmutableMap.of("geohash_grid", geohashGridAgg));
        final Map<String, Object> expected =
                ImmutableMap.of(
                        "first",
                        ImmutableMap.of(
                                "geohash_grid", ImmutableMap.of("field", "name", "precision", 1)));
        GeohashUtil.updateGridAggregationPrecision(aggregations, 2);
        assertEquals(expected, aggregations);
    }
}
