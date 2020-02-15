/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * Utility methods for curved geometries
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CurvedGeometries {

    /** Returns true if the geometry is curved, or contains elements that are curved */
    public static boolean isCurved(Geometry geometry) {
        if (geometry instanceof CurvedGeometry<?>) {
            return true;
        }

        final AtomicBoolean curveFound = new AtomicBoolean(false);
        geometry.apply(
                new GeometryComponentFilter() {

                    @Override
                    public void filter(Geometry geom) {
                        if (geom instanceof CurvedGeometry<?>) {
                            curveFound.set(true);
                        }
                    }
                });

        return curveFound.get();
    }

    /** Checks if the specified geometry is a circle */
    public static boolean isCircle(Geometry geom) {
        if (geom.isEmpty()) {
            return false;
        }
        if (!(geom instanceof CircularRing) && !(geom instanceof CompoundRing)) {
            return false;
        }
        if (geom instanceof CircularRing) {
            // check that all arcs have the same center and radius
            CircularRing curved = (CircularRing) geom;
            CircularArc first = curved.getArcN(0);
            double radius = first.getRadius();
            if (radius == Double.POSITIVE_INFINITY) {
                return false;
            }
            Coordinate center = first.getCenter();
            final int numArcs = curved.getNumArcs();
            for (int i = 1; i < numArcs; i++) {
                CircularArc curr = curved.getArcN(i);
                if (!CircularArc.equals(curr.getRadius(), radius)) {
                    return false;
                }
                Coordinate cc = curr.getCenter();
                if (!CircularArc.equals(cc.x, center.x) || !CircularArc.equals(cc.y, center.y)) {
                    return false;
                }
            }

            return true;
        } else {
            // check that all arcs in the components have the same center and radius, and that
            // all components are curved
            CompoundRing curved = (CompoundRing) geom;
            List<LineString> components = curved.getComponents();
            double radius = Double.NaN;
            Coordinate center = null;
            for (LineString component : components) {
                if (!(component instanceof SingleCurvedGeometry<?>)) {
                    return false;
                }
                SingleCurvedGeometry<?> curvedComponent = (SingleCurvedGeometry<?>) component;
                final int numArcs = curvedComponent.getNumArcs();
                for (int i = 0; i < numArcs; i++) {
                    CircularArc curr = curvedComponent.getArcN(i);
                    if (center == null) {
                        radius = curr.getRadius();
                        if (radius == Double.POSITIVE_INFINITY) {
                            return false;
                        }
                        center = curr.getCenter();
                    } else {
                        if (!CircularArc.equals(curr.getRadius(), radius)) {
                            return false;
                        }
                        Coordinate cc = curr.getCenter();
                        if (!CircularArc.equals(cc.x, center.x)
                                || !CircularArc.equals(cc.y, center.y)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /** Builds a circular arc out of the specified coordinate sequence */
    public static CircularArc getArc(CoordinateSequence cs, int startCoordinate) {
        if (cs.size() < (startCoordinate + 3)) {
            throw new IllegalArgumentException(
                    "The coordinate sequence has "
                            + cs.size()
                            + " points, cannot extract a circular arc starting from coordinate "
                            + startCoordinate);
        } else if (startCoordinate < 0) {
            throw new IllegalArgumentException(
                    "Start coordinate must be 0 or positive, not: " + startCoordinate);
        }

        return new CircularArc(
                cs.getOrdinate(0, 0),
                cs.getOrdinate(0, 1), //
                cs.getOrdinate(1, 0),
                cs.getOrdinate(1, 1), //
                cs.getOrdinate(2, 0),
                cs.getOrdinate(2, 1));
    }

    /** Returns the circle containing this arc */
    public static CircularRing toCircle(
            CircularArc arc, GeometryFactory geometryFactory, double tolerance) {
        double radius = arc.getRadius();
        Coordinate center = arc.getCenter();

        double[] rcp = new double[10];
        rcp[0] = center.x + radius;
        rcp[1] = center.y;
        rcp[2] = center.x;
        rcp[3] = center.y + radius;
        rcp[4] = center.x - radius;
        rcp[5] = center.y;
        rcp[6] = center.x;
        rcp[7] = center.y - radius;
        rcp[8] = center.x + radius;
        rcp[9] = center.y;

        return new CircularRing(rcp, geometryFactory, tolerance);
    }

    /**
     * Extracts a {@link CurvedGeometryFactory} from the provided geometry, either by just returning
     * the one that is held by the geometry, if consistent with its tolerance, or by creating a new
     * one
     */
    public static CurvedGeometryFactory getFactory(CurvedGeometry<?> curved) {
        GeometryFactory factory = ((Geometry) curved).getFactory();
        if (factory instanceof CurvedGeometryFactory) {
            CurvedGeometryFactory cf = (CurvedGeometryFactory) factory;
            if (cf.getTolerance() == curved.getTolerance()) {
                return cf;
            }
        }
        return new CurvedGeometryFactory(factory, curved.getTolerance());
    }
}
