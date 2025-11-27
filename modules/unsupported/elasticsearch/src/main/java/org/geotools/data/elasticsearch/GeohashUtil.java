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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import si.uom.SI;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class GeohashUtil {

    static final double OGC_DEGREE_TO_METERS = 6378137.0 * 2.0 * Math.PI / 360;
    static final double DEFAULT_PIXEL_SIZE_METER = 0.00028;

    static final ObjectMapper MAPPER = new ObjectMapper();
    static final Logger LOGGER = Logging.getLogger(GeohashUtil.class);

    public static int computePrecision(Envelope envelope, long size, double threshold) {
        return computePrecision(envelope, size, threshold, 1);
    }

    private static int computePrecision(Envelope envelope, long size, double threshold, int n) {
        return computeSize(envelope, n) / size >= threshold ? n : computePrecision(envelope, size, threshold, n + 1);
    }

    private static double computeSize(Envelope envelope, int n) {
        final double area = Math.min(360 * 180, envelope.getArea());
        return area / (GeoHash.widthDegrees(n) * GeoHash.heightDegrees(n));
    }

    /**
     * Updates the precision in the aggregation to the given value, but only if it's missing or if it's higher than the
     * provided value (for safety)
     */
    public static void updateGridAggregationPrecision(
            Map<String, Map<String, Map<String, Object>>> aggregations, int precision) {
        aggregations.values().stream()
                .filter(a -> a.containsKey("geohash_grid"))
                .forEach(a -> updateAggregatePrecision(precision, a));
    }

    private static void updateAggregatePrecision(int precision, Map<String, Map<String, Object>> a) {
        Map<String, Object> grid = a.get("geohash_grid");
        Object foundPrecision = grid.get("precision");
        if (!(foundPrecision instanceof Number) || ((Number) foundPrecision).intValue() > precision) {
            LOGGER.log(Level.FINE, "Updating aggregation precision from " + foundPrecision + " to " + precision);
            grid.put("precision", precision);
        }
    }

    public static Map<String, Map<String, Map<String, Object>>> parseAggregation(String definition) {

        final TypeReference<Map<String, Map<String, Map<String, Object>>>> type = new TypeReference<>() {};
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

    public static int getPrecisionFromScale(ReferencedEnvelope envelope, int imageWidth) {
        double targetScaleDenominator = calculateOGCScale(envelope, imageWidth);
        int precision = 1;
        double sd = 0;
        for (; precision <= GeoHash.MAX_HASH_LENGTH; precision++) {
            double ghw = GeoHash.widthDegrees(precision);
            double ghh = GeoHash.heightDegrees(precision);
            // compute equivalent pixel size (as it if was a square)
            double ps = Math.sqrt(ghw * ghh);

            sd = ps * OGC_DEGREE_TO_METERS / DEFAULT_PIXEL_SIZE_METER;
            if (sd < targetScaleDenominator) break;
        }
        // stick with a lower precision, number of cells goes up by a factor of 32 for
        // each precision level (unless the match is pretty close, within 10%)
        if (precision > 1 && Math.abs(targetScaleDenominator - sd) > (targetScaleDenominator * 0.1)) precision--;

        return precision;
    }

    /** Borrowed from gt-renderer RendererUtilities to avoid adding a dependency */
    private static double calculateOGCScale(ReferencedEnvelope envelope, int imageWidth) {
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        double width = envelope.getWidth();
        double widthMeters = toMeters(width, crs);

        double dpi = 25.4 / 0.28;
        return widthMeters / (imageWidth / dpi * 0.0254);
    }

    /** Borrowed from gt-renderer RendererUtilities to avoid adding a dependency */
    private static double toMeters(double size, CoordinateReferenceSystem crs) {
        if (crs == null) {
            LOGGER.finer("toMeters: assuming the original size is in meters already, as crs is null");
            return size;
        }
        if (crs instanceof GeographicCRS) {
            return size * OGC_DEGREE_TO_METERS;
        }
        CoordinateReferenceSystem horizontal = CRS.getHorizontalCRS(crs);
        if (horizontal != null) {
            crs = horizontal;
        }
        @SuppressWarnings("unchecked")
        Unit<Length> unit = (Unit<Length>) crs.getCoordinateSystem().getAxis(0).getUnit();
        if (unit == null) {
            LOGGER.finer(
                    "toMeters: assuming the original size is in meters already, as the first crs axis unit is null. CRS is "
                            + crs);
            return size;
        }
        if (!unit.isCompatible(SI.METRE)) {
            LOGGER.warning("toMeters: could not convert unit " + unit + " to meters");
            return size;
        }
        return unit.getConverterTo(SI.METRE).convert(size);
    }

    public static Integer getPrecision(String aggregationDefinition) {
        Integer precision = null;
        final ObjectMapper mapper = new ObjectMapper();
        final TypeReference<Map<String, Map<String, Map<String, Object>>>> type = new TypeReference<>() {};
        try {
            Map<String, Map<String, Map<String, Object>>> map = mapper.readValue(aggregationDefinition, type);
            Map<String, Map<String, Object>> aggMap = map.get("agg");
            if (aggMap != null) {
                Map<String, Object> geohashMap = aggMap.get("geohash_grid");
                if (geohashMap != null) {
                    if (geohashMap.get("precision") != null) {
                        if (geohashMap.get("precision") instanceof Integer) {
                            precision = (Integer) geohashMap.get("precision");
                        }
                    }
                }
            }
        } catch (JacksonException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Failure when trying to read precision from aggregation definition: " + aggregationDefinition,
                    e);
        }
        return precision;
    }
}
