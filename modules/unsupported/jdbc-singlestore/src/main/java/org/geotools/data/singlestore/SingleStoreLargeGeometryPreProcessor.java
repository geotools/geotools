/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

public class SingleStoreLargeGeometryPreProcessor extends DuplicatingFilterVisitor {

    // Envelopes cannot touch the poles or the dateline in SingleStore.
    private static final double EPS = 1e-1; // epsilon for world extent (can't touch pole and dateline)
    private static final Envelope WORLD_ENVELOPE = new Envelope(-180 + EPS, 180 - EPS, -90 + EPS, 90 - EPS);
    /**
     * Densification factor for geodesic geometries. SingleStore treats segments between vertices as orthodromes, which
     * results in missing data when displaying maps in WGS84 (the shortest part is a curve crossing the map request). We
     * need to densify the geometry to avoid this.
     */
    private static final double GEODESIC_DENSIFICATION =
            Double.parseDouble(System.getProperty("org.geotools.singlestore.densification", "1"));

    private static final double MAX_GEOMETRY_SPAN = 180;
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    @Override
    public Object visit(BBOX filter, Object extraData) {
        FilterFactory ff = getFactory(extraData);
        Expression propertyName = visit(filter.getExpression1(), extraData);
        Expression box = filter.getExpression2();
        if (box instanceof Literal && ((Literal) box).getValue() instanceof Envelope) {
            Envelope envelope = (Envelope) ((Literal) box).getValue();
            if (!WORLD_ENVELOPE.contains(envelope)) {
                envelope = envelope.intersection(WORLD_ENVELOPE);
            }

            if (envelope.getWidth() > MAX_GEOMETRY_SPAN) {
                // If the envelope spans more than 180 degrees, we need to split it into two parts
                // to avoid issues with SingleStore's handling of large geometries.
                double mid = (envelope.getMinX() + envelope.getMaxX()) / 2;
                Envelope leftPart = new Envelope(envelope.getMinX(), mid, envelope.getMinY(), envelope.getMaxY());
                Envelope rightPart = new Envelope(mid, envelope.getMaxX(), envelope.getMinY(), envelope.getMaxY());

                Polygon leftPolygon = densifyEnvelope(leftPart, GEOMETRY_FACTORY);
                Polygon rightPolygon = densifyEnvelope(rightPart, GEOMETRY_FACTORY);

                return ff.or(
                        ff.intersects(propertyName, ff.literal(leftPolygon)),
                        ff.intersects(propertyName, ff.literal(rightPolygon)));
            } else {
                if (envelope.getWidth() > GEODESIC_DENSIFICATION || envelope.getHeight() > GEODESIC_DENSIFICATION) {
                    Polygon polygon = densifyEnvelope(envelope, GEOMETRY_FACTORY);
                    return ff.intersects(propertyName, ff.literal(polygon));
                }
            }
        }

        // the intersection is "sane", no need to densify or split
        box = visit(box, extraData);
        return ff.bbox(propertyName, box, filter.getMatchAction());
    }

    /**
     * Optimized densification of an envelope to create a polygon, so that the horizontal edges are densified but the
     * vertical edges are not (no need, vertical orthodromes do not become curves).
     */
    static Polygon densifyEnvelope(Envelope env, GeometryFactory geomFactory) {
        List<Coordinate> coordinates = new ArrayList<>();

        double minX = env.getMinX(); // lon min
        double maxX = env.getMaxX(); // lon max
        double minY = env.getMinY(); // lat min
        double maxY = env.getMaxY(); // lat max

        // Densify bottom edge (from minX to maxX at minY)
        for (double x = minX; x < maxX; x += GEODESIC_DENSIFICATION) {
            coordinates.add(new Coordinate(x, minY));
        }
        coordinates.add(new Coordinate(maxX, minY)); // Ensure closure of edge

        // Right edge (maxX from minY to maxY): no densification
        coordinates.add(new Coordinate(maxX, maxY));

        // Densify top edge (from maxX to minX at maxY) in reverse
        for (double x = maxX; x > minX; x -= GEODESIC_DENSIFICATION) {
            coordinates.add(new Coordinate(x, maxY));
        }
        coordinates.add(new Coordinate(minX, maxY)); // Ensure closure of edge

        // Left edge (minX from maxY to minY): no densification
        coordinates.add(new Coordinate(minX, minY)); // Closing the polygon

        // Create LinearRing and Polygon
        LinearRing shell = geomFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));
        return geomFactory.createPolygon(shell, null);
    }
}
