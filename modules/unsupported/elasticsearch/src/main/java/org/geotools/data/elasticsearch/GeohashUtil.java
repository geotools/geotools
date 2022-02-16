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

import com.github.davidmoten.geo.GeoHash;
import java.util.Map;
import org.locationtech.jts.geom.Envelope;

class GeohashUtil {

    public static int computePrecision(Envelope envelope, long size, double threshold) {
        return computePrecision(envelope, size, threshold, 1);
    }

    private static int computePrecision(Envelope envelope, long size, double threshold, int n) {
        return computeSize(envelope, n) / size > threshold
                ? n
                : computePrecision(envelope, size, threshold, n + 1);
    }

    private static double computeSize(Envelope envelope, int n) {
        final double area = Math.min(360 * 180, envelope.getArea());
        return area / (GeoHash.widthDegrees(n) * GeoHash.heightDegrees(n));
    }

    public static void updateGridAggregationPrecision(
            Map<String, Map<String, Map<String, Object>>> aggregations, int precision) {
        aggregations.values().stream()
                .filter(a -> a.containsKey("geohash_grid"))
                .forEach(a -> a.get("geohash_grid").put("precision", precision));
    }
}
