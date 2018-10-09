/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Find the coordinate dimension of a geometry instance.
 *
 * <p>JTS coordinate sequences are often three-dimensional, but their coordinates don't have a third
 * ordinate (i.e. it is NaN). This class checks if all coordinate sequences in a geometry claiming
 * to be three-dimensional really have a third ordinate.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaDimensionFinder {

    private HanaDimensionFinder() {}

    /**
     * Finds the coordinate dimension of a geometry.
     *
     * @param g A geometry. Must not be null.
     * @return Returns 3 iff all coordinate sequences in the geometry are three-dimensional and all
     *     their coordinates have a third ordinate different from NaN. Returns 2 otherwise.
     */
    public static int findDimension(Geometry g) {
        return allPointsHave3Coordinates(g) ? 3 : 2;
    }

    private static boolean allPointsHave3Coordinates(Geometry g) {
        if (g.isEmpty()) {
            return true;
        }
        if (g instanceof Point) {
            return allPointsHave3Coordinates((Point) g);
        } else if (g instanceof LineString) {
            return allPointsHave3Coordinates((LineString) g);
        } else if (g instanceof Polygon) {
            return allPointsHave3Coordinates((Polygon) g);
        } else if (g instanceof GeometryCollection) {
            return allPointsHave3Coordinates((GeometryCollection) g);
        }
        throw new AssertionError();
    }

    private static boolean allPointsHave3Coordinates(Point p) {
        Coordinate c = p.getCoordinate();
        return !Double.isNaN(c.getZ());
    }

    private static boolean allPointsHave3Coordinates(LineString ls) {
        return allPointsHave3Coordinates(ls.getCoordinateSequence());
    }

    private static boolean allPointsHave3Coordinates(Polygon pg) {
        if (!allPointsHave3Coordinates(pg.getExteriorRing())) {
            return false;
        }
        int numHoles = pg.getNumInteriorRing();
        for (int i = 0; i < numHoles; ++i) {
            if (!allPointsHave3Coordinates(pg.getInteriorRingN(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean allPointsHave3Coordinates(GeometryCollection gc) {
        int numGeometries = gc.getNumGeometries();
        for (int i = 0; i < numGeometries; ++i) {
            if (!allPointsHave3Coordinates(gc.getGeometryN(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean allPointsHave3Coordinates(CoordinateSequence cs) {
        if (cs.getDimension() < 3) {
            return false;
        }
        int size = cs.size();
        for (int i = 0; i < size; ++i) {
            if (Double.isNaN(cs.getOrdinate(i, 2))) {
                return false;
            }
        }
        return true;
    }
}
