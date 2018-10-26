/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.geotools.geometry.GeometryFactoryFinder;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * Class with static methods to help the conversion process between JTS geometries and GO-1
 * geometries.
 */
public final class JTSUtils {
    /**
     * This class has only static methods, so we make the constructor private to prevent
     * instantiation.
     */
    private JTSUtils() {}

    /**
     * Common instance of GEOMETRY_FACTORY with the default JTS precision model that can be used to
     * make new geometries.
     */
    public static final org.locationtech.jts.geom.GeometryFactory GEOMETRY_FACTORY =
            new org.locationtech.jts.geom.GeometryFactory();

    /** Creates a 19107 primitive geometry from the given JTS geometry. */
    public static Geometry jtsToGo1(
            final org.locationtech.jts.geom.Geometry jtsGeom, final CoordinateReferenceSystem crs) {

        Hints hints = new Hints(Hints.CRS, crs);
        PrimitiveFactory pf = GeometryFactoryFinder.getPrimitiveFactory(hints);
        GeometryFactory gf = GeometryFactoryFinder.getGeometryFactory(hints);

        String geomType = jtsGeom.getGeometryType();
        if (geomType.equalsIgnoreCase("Point")) {
            org.locationtech.jts.geom.Point jtsPoint = (org.locationtech.jts.geom.Point) jtsGeom;
            DirectPosition dp = pointToDirectPosition(jtsPoint, crs);
            Point result = pf.createPoint(dp);
            return result;

        } else if (geomType.equalsIgnoreCase("LineString")) {
            org.locationtech.jts.geom.LineString jtsLineString =
                    (org.locationtech.jts.geom.LineString) jtsGeom;
            int numPoints = jtsLineString.getNumPoints();
            LineString ls = gf.createLineString(new ArrayList());
            List pointList = ls.getControlPoints();
            for (int i = 0; i < numPoints; i++) {
                pointList.add(coordinateToDirectPosition(jtsLineString.getCoordinateN(i), crs));
            }
            ArrayList segments = new ArrayList();
            segments.add(ls);
            Curve result = pf.createCurve(segments);
            return result;

        } else if (geomType.equalsIgnoreCase("LinearRing")) {
            Ring result = linearRingToRing((org.locationtech.jts.geom.LinearRing) jtsGeom, crs);
            return result;

        } else if (geomType.equalsIgnoreCase("Polygon")) {
            org.locationtech.jts.geom.Polygon jtsPolygon =
                    (org.locationtech.jts.geom.Polygon) jtsGeom;
            Ring externalRing =
                    linearRingToRing(
                            (org.locationtech.jts.geom.LinearRing) jtsPolygon.getExteriorRing(),
                            crs);
            int n = jtsPolygon.getNumInteriorRing();
            ArrayList internalRings = new ArrayList();
            for (int i = 0; i < n; i++) {
                internalRings.add(
                        linearRingToRing(
                                (org.locationtech.jts.geom.LinearRing)
                                        jtsPolygon.getInteriorRingN(i),
                                crs));
            }
            SurfaceBoundary boundary = pf.createSurfaceBoundary(externalRing, internalRings);
            Polygon polygon = gf.createPolygon(boundary);
            ArrayList patches = new ArrayList();
            patches.add(polygon);
            PolyhedralSurface result = gf.createPolyhedralSurface(patches);
            return result;

        } else if (geomType.equalsIgnoreCase("GeometryCollection")
                || geomType.equalsIgnoreCase("MultiPoint")
                || geomType.equalsIgnoreCase("MultiLineString")
                || geomType.equalsIgnoreCase("MultiPolygon")) {
            org.locationtech.jts.geom.GeometryCollection jtsCollection =
                    (org.locationtech.jts.geom.GeometryCollection) jtsGeom;
            int n = jtsCollection.getNumGeometries();
            MultiPrimitive result = gf.createMultiPrimitive();
            Set elements = result.getElements();
            for (int i = 0; i < n; i++) {
                // result.getElements().add(jtsToGo1(jtsCollection.getGeometryN(i), crs));
                elements.add(jtsToGo1(jtsCollection.getGeometryN(i), crs));
            }
            return result;

        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + geomType);
        }
    }

    /**
     * Converts a DirectPosition to a JTS Coordinate. Returns a newly instantiated Coordinate
     * object.
     */
    public static org.locationtech.jts.geom.Coordinate directPositionToCoordinate(
            DirectPosition dp) {
        double x = Double.NaN, y = Double.NaN, z = Double.NaN;
        int d = dp.getDimension();
        if (d >= 1) {
            x = dp.getOrdinate(0);
            if (d >= 2) {
                y = dp.getOrdinate(1);
                if (d >= 3) {
                    z = dp.getOrdinate(2);
                }
            }
        }
        return new org.locationtech.jts.geom.Coordinate(x, y, z);
    }

    /**
     * Sets the coordinate values of an existing JTS Coordinate by extracting values from a
     * DirectPosition. If the dimension of the DirectPosition is less than three, then the unused
     * ordinates of the Coordinate are set to Double.NaN.
     */
    public static void directPositionToCoordinate(
            DirectPosition dp, org.locationtech.jts.geom.Coordinate result) {
        int d = dp.getDimension();
        if (d >= 1) {
            result.x = dp.getOrdinate(0);
            if (d >= 2) {
                result.y = dp.getOrdinate(1);
                if (d >= 3) {
                    result.z = dp.getOrdinate(3);
                } else {
                    result.z = Double.NaN;
                }
            } else {
                result.y = result.z = Double.NaN;
            }
        } else {
            // I can't imagine a DirectPosition with dimension zero, but it
            // can't hurt to have code to handle that case...
            result.x = result.y = result.z = Double.NaN;
        }
    }

    /**
     * Converts a DirectPosition to a JTS Point primitive. Returns a newly instantiated Point object
     * that was created using the default GeometryFactory instance.
     */
    public static org.locationtech.jts.geom.Point directPositionToPoint(DirectPosition dp) {
        return GEOMETRY_FACTORY.createPoint(directPositionToCoordinate(dp));
    }

    /** Converts a JTS Coordinate to a DirectPosition with the given CRS. */
    public static DirectPosition coordinateToDirectPosition(
            org.locationtech.jts.geom.Coordinate c, CoordinateReferenceSystem crs) {

        Hints hints = new Hints(Hints.CRS, crs);
        GeometryFactory gf = GeometryFactoryFinder.getGeometryFactory(hints);

        double[] vertices;
        if (crs == null) vertices = new double[3];
        else vertices = new double[crs.getCoordinateSystem().getDimension()];
        DirectPosition result = gf.createDirectPosition(vertices);
        coordinateToDirectPosition(c, result);
        return result;
    }

    /** Extracts the values of a JTS coordinate into an existing DirectPosition object. */
    public static void coordinateToDirectPosition(
            org.locationtech.jts.geom.Coordinate c, DirectPosition result) {
        // Get the CRS so we can figure out the dimension of the result.
        CoordinateReferenceSystem crs = result.getCoordinateReferenceSystem();
        int d;

        if (crs != null) {
            d = crs.getCoordinateSystem().getDimension();
        } else {
            // If the result DP has no CRS, then we just assume 2 dimensions.
            // This could result in IndexOutOfBounds exceptions if the DP has
            // fewer than 2 coordinates.
            d = 2;
        }
        final CoordinateSystem cs = crs.getCoordinateSystem();

        if (d >= 1) {
            int xIndex = GeometryUtils.getDirectedAxisIndex(cs, AxisDirection.EAST);
            result.setOrdinate(xIndex, c.x); // 0
            if (d >= 2) {
                int yIndex = GeometryUtils.getDirectedAxisIndex(cs, AxisDirection.NORTH);
                result.setOrdinate(yIndex, c.y); // 1
                if (d >= 3) {
                    int zIndex = GeometryUtils.getDirectedAxisIndex(cs, AxisDirection.UP);
                    result.setOrdinate(zIndex, c.z); // 2
                    // If d > 3, then the remaining ordinates of the DP are
                    // (so far) left with their original values.  So we init
                    // them to zero here.
                    if (d > 3) {
                        for (int i = 3; i < d; i++) {
                            result.setOrdinate(i, 0.0);
                        }
                    }
                }
            }
        }
    }

    /** Converts a JTS Point to a DirectPosition with the given CRS. */
    public static DirectPosition pointToDirectPosition(
            org.locationtech.jts.geom.Point p, CoordinateReferenceSystem crs) {
        return coordinateToDirectPosition(p.getCoordinate(), crs);
    }

    public static Ring linearRingToRing(
            org.locationtech.jts.geom.LineString jtsLinearRing, CoordinateReferenceSystem crs) {
        int numPoints = jtsLinearRing.getNumPoints();
        if (!jtsLinearRing.getCoordinateN(0).equals(jtsLinearRing.getCoordinateN(numPoints - 1))) {
            throw new IllegalArgumentException("LineString must be a ring");
        }
        Hints hints = new Hints(Hints.CRS, crs);
        PrimitiveFactory pf = GeometryFactoryFinder.getPrimitiveFactory(hints);
        GeometryFactory gf = GeometryFactoryFinder.getGeometryFactory(hints);

        LineString ls = gf.createLineString(new ArrayList());
        List pointList = ls.getControlPoints();
        for (int i = 0; i < numPoints; i++) {
            pointList.add(coordinateToDirectPosition(jtsLinearRing.getCoordinateN(i), crs));
        }
        Curve curve = pf.createCurve(new ArrayList());
        // Cast below can be removed when GeoAPI will be allowed to abandon Java 1.4 support.
        ((List) curve.getSegments()).add(ls);
        Ring result = pf.createRing(new ArrayList());
        // Cast below can be removed when GeoAPI will be allowed to abandon Java 1.4 support.
        ((List) result.getGenerators()).add(curve);
        return result;
    }

    /**
     * Computes the distance between two JTS geometries. Unfortunately, JTS's methods do not allow
     * for either parameter to be a collection. So we have to implement the logic of dealing with
     * collection geometries separately.
     */
    public static double distance(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        if (g1 instanceof org.locationtech.jts.geom.GeometryCollection) {
            double minDistance = Double.POSITIVE_INFINITY;
            org.locationtech.jts.geom.GeometryCollection gc1 =
                    (org.locationtech.jts.geom.GeometryCollection) g1;
            int n = gc1.getNumGeometries();
            for (int i = 0; i < n; i++) {
                double d = distance(gc1.getGeometryN(i), g2);
                if (d < minDistance) minDistance = d;
            }
            return minDistance;
        } else if (g2 instanceof org.locationtech.jts.geom.GeometryCollection) {
            double minDistance = Double.POSITIVE_INFINITY;
            org.locationtech.jts.geom.GeometryCollection gc2 =
                    (org.locationtech.jts.geom.GeometryCollection) g2;
            int n = gc2.getNumGeometries();
            for (int i = 0; i < n; i++) {
                // This call will result in a redundant check of
                // g1 instanceof GeometryCollection.  Maybe we oughta fix that
                // somehow.
                double d = distance(g1, gc2.getGeometryN(i));
                if (d < minDistance) minDistance = d;
            }
            return minDistance;
        } else {
            return g1.distance(g2);
        }
    }

    /**
     * Returns the union of the two geometries. In the case of primitive geometries, this simply
     * delegates to the JTS method. In the case of aggregates, creates an aggregate containing all
     * the parts of both.
     */
    public static org.locationtech.jts.geom.Geometry union(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        return null;
    }

    public static org.locationtech.jts.geom.Geometry intersection(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        return null;
    }

    public static org.locationtech.jts.geom.Geometry difference(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        return null;
    }

    public static org.locationtech.jts.geom.Geometry symmetricDifference(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        return null;
    }

    public static boolean contains(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        return false;
    }

    public static boolean equals(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        return false;
    }

    /**
     * Returns true if the two given geometries intersect. In the case of primitive geometries, this
     * simply delegates to the JTS method. In the case of Aggregates, loops over pairs of children
     * looking for intersections.
     */
    public static boolean intersects(
            org.locationtech.jts.geom.Geometry g1, org.locationtech.jts.geom.Geometry g2) {
        if (g1 instanceof org.locationtech.jts.geom.GeometryCollection) {
            org.locationtech.jts.geom.GeometryCollection gc1 =
                    (org.locationtech.jts.geom.GeometryCollection) g1;
            int n = gc1.getNumGeometries();
            for (int i = 0; i < n; i++) {
                org.locationtech.jts.geom.Geometry g = gc1.getGeometryN(i);
                if (intersects(g, g2)) return true;
            }
            return false;
        } else if (g2 instanceof org.locationtech.jts.geom.GeometryCollection) {
            org.locationtech.jts.geom.GeometryCollection gc2 =
                    (org.locationtech.jts.geom.GeometryCollection) g2;
            int n = gc2.getNumGeometries();
            for (int i = 0; i < n; i++) {
                org.locationtech.jts.geom.Geometry g = gc2.getGeometryN(i);
                if (intersects(g1, g)) return true;
            }
            return false;
        } else {
            return g1.intersects(g2);
        }
    }

    /**
     * Creates a JTS LineString from the four corners of the specified Envelope.
     *
     * @param envelope The Envelope to be converted
     * @return A JTS Geometry
     */
    public static org.locationtech.jts.geom.Geometry getEnvelopeGeometry(final Envelope envelope) {
        // PENDING(NL): Add code to check for CRS compatibility
        // Must consider possibility that this is a pixel envelope
        // rather than geo coordinate; only way to be sure is to check Units
        DirectPosition topCorner = envelope.getUpperCorner();
        DirectPosition botCorner = envelope.getLowerCorner();
        DirectPosition topLeft = new DirectPositionImpl(topCorner);
        DirectPosition botRight = new DirectPositionImpl(botCorner);

        // Again, making assumption we can ignore this LatLonAlt stuff - colin

        /*
        // If the Envelope coordinates are LatLonAlts,
        // calling setOrdinate causes Error-level logging messages,
        // including a stack trace,
        // though it still works.  But in principal we should
        // call get/setLat and get/setLon instead if we have LatLonAlts
        if (topLeft instanceof LatLonAlt && botRight instanceof LatLonAlt) {
        	((LatLonAlt) topLeft).setLon(((LatLonAlt)
        			botCorner).getLon(NonSI.DEGREE_ANGLE), NonSI.DEGREE_ANGLE);
        	((LatLonAlt) botRight).setLon(((LatLonAlt)
        			topCorner).getLon(NonSI.DEGREE_ANGLE), NonSI.DEGREE_ANGLE);
        } else {*/

        topLeft.setOrdinate(1, botCorner.getOrdinate(1));
        botRight.setOrdinate(1, topCorner.getOrdinate(1));

        // }//end of else statment associated with above LatLongAlt stuff
        // Create a JTS Envelope
        Coordinate jtsTopRight = JTSUtils.directPositionToCoordinate(topCorner);
        Coordinate jtsTopLeft = JTSUtils.directPositionToCoordinate(topLeft);
        Coordinate jtsBotLeft = JTSUtils.directPositionToCoordinate(botCorner);
        Coordinate jtsBotRight = JTSUtils.directPositionToCoordinate(botRight);

        org.locationtech.jts.geom.Geometry jtsEnv =
                GEOMETRY_FACTORY
                        .createLineString(
                                new Coordinate[] {
                                    jtsTopLeft, jtsTopRight, jtsBotRight, jtsBotLeft, jtsTopLeft
                                })
                        .getEnvelope();
        return jtsEnv;
    }
}
