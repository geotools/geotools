/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.process.geometry;

import org.geotools.geometry.jts.GeometryBuilder;
import org.locationtech.jts.algorithm.construct.MaximumInscribedCircle;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Formerly, based on Vladimir Agafonkin's Algorithm https://www.mapbox.com/blog/polygon-center/
 *
 * @author Ian Turton
 * @author Casper Børgesen
 */
public class PolyLabeller {

    /** Distance tolerance used to test whether a centroid or a envelope center could be preferred */
    private static final double DISTANCE_TOLERANCE_PERC = 0.8;

    static GeometryBuilder GB = new GeometryBuilder();

    private static class LabelPosition {
        Point position;
        double distance;

        public LabelPosition(Point position, double distance) {
            this.position = position;
            this.distance = distance;
        }

        public Point getPosition() {
            return position;
        }

        public double getDistance() {
            return distance;
        }
    }

    static Point getPolylabel(Geometry geometry, Double precision) {
        if (geometry == null) {
            return null;
        }

        if (geometry.isEmpty() || geometry.getArea() <= 0.0) {
            return null;
        }

        if (geometry instanceof Polygon) {
            return getPolyLabel_((Polygon) geometry, precision).getPosition();
        } else if (geometry instanceof MultiPolygon) {
            LabelPosition widest = null;
            for (int i = 0; i < geometry.getNumGeometries(); i++) {
                Polygon p = (Polygon) geometry.getGeometryN(i);
                LabelPosition polylabel = getPolyLabel_(p, precision);
                if (widest == null || polylabel.getDistance() > widest.getDistance()) widest = polylabel;
            }
            return widest.getPosition();
        } else {
            throw new IllegalStateException("Input polygon must be a Polygon or MultiPolygon");
        }
    }

    private static LabelPosition getPolyLabel_(Polygon polygon, Double precision) {
        if (precision == null) {
            precision = getDefaultPrecision(polygon);
        }

        // Start with the maximum inscribed circle. For a rectangular geometry, it can
        // be in a random place though (multiple maximum inscribed circles available)
        Point polyLabel = MaximumInscribedCircle.getCenter(polygon, precision);

        // look at the centroid and the envelope center
        Point centroid = polygon.getCentroid();
        Point envelopeCenter = polygon.getEnvelope().getCentroid();

        // evaluate the distances
        Geometry boundary = polygon.getBoundary();
        double distancePolyLabel = polyLabel.distance(boundary);
        double distanceCentroid = centroid.distance(boundary);
        double distanceEnvelope = envelopeCenter.distance(boundary);

        // prefer the centroid if inside and the distance is competitive with the poly label one
        if (distanceCentroid > distancePolyLabel * DISTANCE_TOLERANCE_PERC && polygon.contains(centroid)) {
            return new LabelPosition(centroid, distanceCentroid);
        }
        // prefer the envelope center if inside and the distance is competitive with the poly label
        if (distanceEnvelope > distancePolyLabel * DISTANCE_TOLERANCE_PERC && polygon.contains(envelopeCenter)) {
            return new LabelPosition(envelopeCenter, distanceEnvelope);
        }

        // otherwise go for the maximum inscribed circle
        return new LabelPosition(polyLabel, distancePolyLabel);
    }

    /**
     * The default precision should be rough enough, small values produce pretty poor output. The default is half the
     * radius of the circle that has the same area as the polygon
     */
    private static double getDefaultPrecision(Geometry polygon) {
        return Math.sqrt(polygon.getArea()) / Math.PI / 2;
    }
}
