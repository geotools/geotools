/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import static java.util.Map.entry;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import si.uom.SI;

/** Utility class for DistanceBufferOperations */
public class DistanceBufferUtil {

    /** Standard java logger */
    protected static Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DistanceBufferUtil.class);

    private static final Map<String, Double> UNITS_MAP =
            Map.ofEntries(
                    entry("kilometers", 1000.0),
                    entry("kilometer", 1000.0),
                    entry("km", 1000.0),
                    entry("m", 1.0),
                    entry("meter", 1.0),
                    entry("mm", 0.001),
                    entry("millimeter", 0.001),
                    entry("mi", 1609.344),
                    entry("miles", 1609.344),
                    entry("nm", 1852d),
                    entry("feet", 0.3048),
                    entry("ft", 0.3048),
                    entry("in", 0.0254));

    public static double getDistanceInNativeUnits(
            DistanceBufferOperator operator, Integer currentSRID) {
        if (currentSRID == null) {
            return operator.getDistance();
        }
        try {
            CoordinateReferenceSystem crs = CRS.getHorizontalCRS(CRS.decode("EPSG:" + currentSRID));
            double distanceMeters = getDistanceInMeters(operator);
            if (crs instanceof GeographicCRS) {
                double sizeDegree = 110574.2727;
                Coordinate center = getReferenceGeometryCentroid(operator);
                if (center != null) {
                    double cosLat = Math.cos(Math.PI * center.y / 180.0);
                    double latAdjustment = Math.sqrt(1 + cosLat * cosLat) / Math.sqrt(2.0);
                    sizeDegree *= latAdjustment;
                }

                return distanceMeters / sizeDegree;
            } else {
                @SuppressWarnings("unchecked")
                Unit<Length> unit = (Unit<Length>) crs.getCoordinateSystem().getAxis(0).getUnit();
                if (unit == null) {
                    return distanceMeters;
                } else {
                    UnitConverter converter = SI.METRE.getConverterTo(unit);
                    return converter.convert(distanceMeters);
                }
            }
        } catch (Exception e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to turn the distance of spatial "
                            + "filter into native units, using it as a pure number instead",
                    e);
            // tried, fall back on pure value
            return operator.getDistance();
        }
    }

    /** Returns the center of the reference geometry of the distance buffer operator, in case */
    private static Coordinate getReferenceGeometryCentroid(DistanceBufferOperator operator) {
        Geometry geom = operator.getExpression1().evaluate(null, Geometry.class);
        if (geom == null) {
            geom = operator.getExpression2().evaluate(null, Geometry.class);
        }
        if (geom == null) {
            return null;
        }
        return geom.getCentroid().getCoordinate();
    }

    /**
     * Converts the distance of the operator in meters, or returns the current value if there is no
     * units distance
     */
    public static double getDistanceInMeters(DistanceBufferOperator operator) {
        double distance = operator.getDistance();
        String units = operator.getDistanceUnits();
        // no units or no SRID, no party, return value as-is
        if (units == null || UNITS_MAP.get(units.toLowerCase()) == null) {
            return distance;
        }

        double factor = UNITS_MAP.get(units.toLowerCase());
        return distance * factor;
    }
}
