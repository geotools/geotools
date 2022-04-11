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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davidmoten.geo.GeoHash;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;

public class GeohashUtil {

    static final ObjectMapper MAPPER = new ObjectMapper();
    static final Logger LOGGER = Logging.getLogger(GeohashUtil.class);

    public static int computePrecision(Envelope envelope, long size, double threshold) {
        return computePrecision(envelope, size, threshold, 1);
    }

    private static int computePrecision(Envelope envelope, long size, double threshold, int n) {
        return computeSize(envelope, n) / size >= threshold
                ? n
                : computePrecision(envelope, size, threshold, n + 1);
    }

    private static double computeSize(Envelope envelope, int n) {
        final double area = Math.min(360 * 180, envelope.getArea());
        return area / (GeoHash.widthDegrees(n) * GeoHash.heightDegrees(n));
    }

    /**
     * Updates the precision in the aggregation to the given value, but only if it's missing or if
     * it's higher than the provided value (for safety)
     */
    public static void updateGridAggregationPrecision(
            Map<String, Map<String, Map<String, Object>>> aggregations, int precision) {
        aggregations.values().stream()
                .filter(a -> a.containsKey("geohash_grid"))
                .forEach(a -> updateAggregatePrecision(precision, a));
    }

    private static void updateAggregatePrecision(
            int precision, Map<String, Map<String, Object>> a) {
        Map<String, Object> grid = a.get("geohash_grid");
        Object foundPrecision = grid.get("precision");
        if (!(foundPrecision instanceof Number)
                || ((Number) foundPrecision).intValue() > precision) {
            LOGGER.log(
                    Level.FINE,
                    "Updating aggregation precision from " + foundPrecision + " to " + precision);
            grid.put("precision", precision);
        }
    }

    public static Map<String, Map<String, Map<String, Object>>> parseAggregation(
            String definition) {

        final TypeReference<Map<String, Map<String, Map<String, Object>>>> type =
                new TypeReference<Map<String, Map<String, Map<String, Object>>>>() {};
        try {
            return MAPPER.readValue(definition, type);
        } catch (Exception e) {
            try {
                return MAPPER.readValue(ElasticParserUtil.urlDecode(definition), type);
            } catch (Exception e2) {
                throw new FilterToElasticException("Unable to parse aggregation", e);
            }
        }
    }
}
