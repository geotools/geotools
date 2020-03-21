/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.algorithm.Distance;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.GeometryFilter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;

/**
 * Builds a offset curve, that is, a line parallel to the provided geometry at a given distance. An
 * offset curve is not the same as a buffer, the line is built only on one side of the original
 * geometry, and will self intersect if the original geometry does the same.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class OffsetCurveBuilder {

    public static int QUADRANT_SEGMENTS_DEFAULT = 8;

    public static double DEFAULT_THRESHOLD_RATIO = 2;

    static double EPS = 1e-9;

    double offset;

    int quadrantSegments;

    double thresholdRatio = DEFAULT_THRESHOLD_RATIO;

    double maxSearchDistanceSquared;

    /**
     * Creates an offset builder with the given offset and the default number of quadrant segments
     * {@link QUADRANT_SEGMENTS_DEFAULT}
     *
     * @param offset The offset distance. The offset line will be on the left for positive values,
     *     on the right otherwise
     */
    public OffsetCurveBuilder(double offset) {
        this(offset, QUADRANT_SEGMENTS_DEFAULT);
    }

    /**
     * Creates an offset builder with the given offset and number of quadrant segments
     *
     * @param offset The offset distance. The offset line will be on the left for positive values,
     *     on the right otherwise
     * @param quadrantSegments The number of segments per quadrant, must be positive
     */
    public OffsetCurveBuilder(double offset, int quadrantSegments) {
        this.offset = offset;
        this.maxSearchDistanceSquared = offset * offset * thresholdRatio * thresholdRatio;
        if (quadrantSegments < 1) {
            throw new IllegalArgumentException(
                    "Invalid number of quadrantSegments, must be greater than zero: "
                            + quadrantSegments);
        }
        this.quadrantSegments = quadrantSegments;
    }

    /**
     * Builds and return the perpendicular offset geometry. Only the linestring elements in the
     * input geometries will be subject to offset, the returned geometry will be either a LineString
     * or a MultiLineString
     *
     * @param g The source geometry
     * @return The offset geometry (a single linestring for single line inputs, a multi-linestring
     *     for multi-line inputs), or null if no offset geometry could be computed (e.g. the input
     *     geometry is made of points)
     */
    public Geometry offset(Geometry g) {
        // shortcut for too short offset
        if (abs(offset) < EPS) {
            return g;
        }

        if (g == null) {
            return null;
        }

        double threshold = abs(offset) / 10;
        final List<LineString> lines = extractLineStrings(g);
        List<LineString> offsets = new ArrayList<>();
        for (LineString ls : lines) {
            LineString simplified = (LineString) DouglasPeuckerSimplifier.simplify(ls, threshold);
            if (simplified == null) {
                return null;
            }
            if (ls.isClosed() && !simplified.isClosed()) {
                CoordinateSequence source = simplified.getCoordinateSequence();
                int numCoordinates = source.size();
                LiteCoordinateSequence dest = new LiteCoordinateSequence(numCoordinates + 1, 2);
                for (int i = 0; i < numCoordinates; i++) {
                    dest.setOrdinate(i, 0, source.getOrdinate(i, 0));
                    dest.setOrdinate(i, 1, source.getOrdinate(i, 1));
                }
                dest.setOrdinate(numCoordinates, 0, source.getOrdinate(0, 0));
                dest.setOrdinate(numCoordinates, 1, source.getOrdinate(0, 1));
                simplified = simplified.getFactory().createLinearRing(dest);
            }
            LineString offsetLine = (LineString) offset(simplified);
            if (offsetLine != null) {
                offsets.add(offsetLine);
            }
        }
        if (offsets.isEmpty()) {
            return null;
        } else if (offsets.size() == 1) {
            return offsets.get(0);
        } else {
            GeometryFactory factory = offsets.get(0).getFactory();
            MultiLineString result =
                    factory.createMultiLineString(offsets.toArray(new LineString[offsets.size()]));
            return result;
        }
    }

    /** Extracts all the {@link LineString} present in the given geometry */
    private List<LineString> extractLineStrings(Geometry g) {
        // normalize order of polygons so that
        // left is equivalent to outwards
        if (g instanceof Polygon) {
            ((Polygon) g).normalize();
        } else if (g instanceof GeometryCollection) {
            g.apply(
                    new GeometryFilter() {

                        @Override
                        public void filter(Geometry geom) {
                            if (geom instanceof Polygon) {
                                ((Polygon) geom).normalize();
                            }
                        }
                    });
        }

        // then extract the lines
        final List<LineString> lines = new ArrayList<>();
        if (g instanceof LineString) {
            lines.add((LineString) g);
        } else {
            g.apply(
                    new GeometryComponentFilter() {

                        @Override
                        public void filter(Geometry geom) {
                            if (geom instanceof LineString) {
                                lines.add((LineString) geom);
                            }
                        }
                    });
        }
        return lines;
    }

    /** Offsets a single linestring */
    private LineString offset(LineString ls) {
        boolean closed = ls instanceof LinearRing;
        CoordinateSequence cs = ls.getCoordinateSequence();
        int numPoints = cs.size();
        GrowableOrdinateArray ordinates = new GrowableOrdinateArray(numPoints * 2);

        // Three subsequent coordinates, c0, c1, and c2, expressed as their ordinates
        // For the loop start, c0 and c1 are the same
        double c0x, c0y, c1x, c1y;
        if (closed) {
            c0x = cs.getOrdinate(cs.size() - 2, 0);
            c0y = cs.getOrdinate(cs.size() - 2, 1);
            c1x = cs.getOrdinate(0, 0);
            c1y = cs.getOrdinate(0, 1);
        } else {
            c0x = cs.getOrdinate(0, 0);
            c0y = cs.getOrdinate(0, 1);
            c1x = c0x;
            c1y = c0y;
        }
        double c2x = cs.getOrdinate(1, 0);
        double c2y = cs.getOrdinate(1, 1);
        // the deltas between c0 and c1, c1 and c2.
        // Note the deltas are from 1 to 0, and from 1 to 2 (1 being the center)
        double dx10 = c0x - c1x;
        double dy10 = c0y - c1y;
        double dx12 = c2x - c1x;
        double dy12 = c2y - c1y;
        // the absolute angles of segment c1-c0 and c1-c2
        double angle10 = atan2(-dy10, -dx10);
        double angle12 = atan2(dy12, dx12);
        if (closed) {
            addPoint(ordinates, c1x, c1y, dx10, dy10, dx12, dy12, angle10, angle12);
        } else {
            // displace first vertex on the perp of angle12
            appendPerpendicular(c1x, c1y, angle12, ordinates);
        }

        // loop over all points
        for (int i = 2; i < numPoints; i++) {
            // shift points (no need to work on c0, it's not used)
            c1x = c2x;
            c1y = c2y;
            c2x = cs.getOrdinate(i, 0);
            c2y = cs.getOrdinate(i, 1);
            // compute the new deltas and angles
            dx10 = -dx12;
            dy10 = -dy12;
            angle10 = angle12;
            dx12 = c2x - c1x;
            dy12 = c2y - c1y;
            angle12 = atan2(dy12, dx12);

            addPoint(ordinates, c1x, c1y, dx10, dy10, dx12, dy12, angle10, angle12);
        }

        // add the last vertex
        if (closed) {
            ordinates.close();
        } else {
            appendPerpendicular(c2x, c2y, angle12, ordinates);
        }

        ordinates = cleanupOrdinates(ordinates, closed);

        LineString result = buildLineString(ls, ordinates);
        return result;
    }

    private GrowableOrdinateArray cleanupOrdinates(
            GrowableOrdinateArray ordinates, boolean closed) {
        final int max = ordinates.size();
        if (max <= 8 && closed || (max < 8 && !closed)) {
            // not enough points for self intersection anyways
            return ordinates;
        }
        // raw access to allow the JIT perform array bounds access elimination
        double[] data = ordinates.getDataRaw();
        // check to let the the JIT know i and j access is safe
        if (max > data.length) {
            throw new ArrayIndexOutOfBoundsException(max);
        }
        // hope we don't have any issue to fix, avoid a copy
        GrowableOrdinateArray result = ordinates;
        // The coordinate objects we'll feed to the intersector
        Coordinate c1 = new Coordinate();
        Coordinate c2 = new Coordinate();
        Coordinate c3 = new Coordinate();
        Coordinate c4 = new Coordinate();
        LineIntersector intersector = new RobustLineIntersector();
        c1.x = data[0];
        c1.y = data[1];
        boolean lastCurlEliminated = false;
        for (int i = 2; i < (max - 3); i += 2) {
            c2.x = data[i];
            c2.y = data[i + 1];
            c3.x = data[i + 2];
            c3.y = data[i + 3];
            for (int j = i + 4; j < (max - 1); j += 2) {
                c4.x = data[j];
                c4.y = data[j + 1];

                if (squaredDistance(c1, c3) > maxSearchDistanceSquared
                        && squaredDistance(c1, c4) > maxSearchDistanceSquared
                        && squaredDistance(c2, c3) > maxSearchDistanceSquared
                        && squaredDistance(c2, c4) > maxSearchDistanceSquared) {
                    // we got beyond the search area, bail out
                    break;
                }

                intersector.computeIntersection(c1, c2, c3, c4);
                if (intersector.hasIntersection()) {
                    Coordinate intersection = intersector.getIntersection(0);
                    c2.x = intersection.x;
                    c2.y = intersection.y;
                    // we had to cut, prepare the result array by copying the part we already
                    // scrolled over, if any
                    if (result == ordinates) {
                        result = new GrowableOrdinateArray();
                        if (i > 3) {
                            result.copy(ordinates, 0, i - 3);
                        }
                    }
                    i = j - 2;
                    break;
                } else if (j == max - 2 && !closed) {
                    // check if we have an almost closed curl
                    double distancePointLine = Distance.pointToLinePerpendicular(c4, c1, c2);
                    if (distancePointLine < abs(offset) / 10) {
                        c2.x = c4.x;
                        c2.y = c4.y;
                        if (result == ordinates) {
                            result = new GrowableOrdinateArray();
                            if (i > 3) {
                                result.copy(ordinates, 0, i - 3);
                            }
                        }
                        i = j - 2;
                        lastCurlEliminated = true;
                        break;
                    }
                }

                // roll over to the next segment
                c3.x = c4.x;
                c3.y = c4.y;
            }

            // if we are in copy mode already, copy over the current point
            if (result != ordinates) {
                result.add(c1.x, c1.y);
            }
            // roll over to the next ordinate
            c1.x = c2.x;
            c1.y = c2.y;
        }

        // copy over the last points if we are in copy mode
        if (result != ordinates) {
            result.add(c1.x);
            result.add(c1.y);
            if (!lastCurlEliminated) {
                result.add(data[max - 2]);
                result.add(data[max - 1]);
            }
        }

        return result;
    }

    private double squaredDistance(Coordinate a, Coordinate b) {
        final double dx = a.x - b.x;
        final double dy = a.y - b.y;
        return squaredDistance(dx, dy);
    }

    private double squaredDistance(final double dx, final double dy) {
        return dx * dx + dy * dy;
    }

    private void addPoint(
            GrowableOrdinateArray ordinates,
            double c1x,
            double c1y,
            double dx10,
            double dy10,
            double dx12,
            double dy12,
            double angle10,
            double angle12) {
        // compute the angle between the two segments
        double jointAngle = computeJointAngle(dx10, dy10, dx12, dy12);
        // see if we are on a inside angle, or an outside one
        if (abs(jointAngle) > PI) {
            addBulge(ordinates, c1x, c1y, angle10, angle12);
        } else {
            // internal angle
            appendInternalJoint(c1x, c1y, angle10, angle12, dx10, dy10, dx12, dy12, ordinates);
        }
    }

    private void addBulge(
            GrowableOrdinateArray ordinates,
            double c1x,
            double c1y,
            double angle10,
            double angle12) {
        double curveAngle = reflexAngle(angle12 - angle10);
        double segmentTurns = quadrantSegments * abs(curveAngle);
        int steps = 1 + (int) (segmentTurns / PI * 2);

        for (int step = 0; step <= steps; step++) {
            appendPerpendicular(c1x, c1y, angle10 + (curveAngle * step) / steps, ordinates);
        }
    }

    private LineString buildLineString(LineString ls, GrowableOrdinateArray ordinates) {
        GeometryFactory factory = ls.getFactory();
        CoordinateSequence cs = ordinates.toCoordinateSequence(factory);
        if (ls instanceof LinearRing) {
            if (cs.size() >= 4) {
                return factory.createLinearRing(cs);
            } else {
                return null;
            }
        } else {
            return factory.createLineString(cs);
        }
    }

    private double reflexAngle(double angle) {
        if (angle > PI) {
            return angle - 2 * PI;
        } else if (angle < -PI) {
            return angle + 2 * PI;
        } else {
            return angle;
        }
    }

    private double computeJointAngle(double dx10, double dy10, double dx12, double dy12) {
        // see
        // http://stackoverflow.com/questions/21483999/using-atan2-to-find-angle-between-two-vectors
        // for an alternative way to compute the angle
        double dot = dx10 * dx12 + dy10 * dy12;
        double det = dx10 * dy12 - dy10 * dx12;
        double angle = atan2(det, dot);
        if (angle < 0) {
            angle += 2 * PI;
        }
        angle = angle % (2 * PI);
        if (offset > 0) {
            angle = 2 * PI - angle;
        }

        return angle;
    }

    /**
     * Appends to ordinates a point calculated as the perpendicular offset to the specified angle,
     * from location x,y
     */
    private void appendPerpendicular(
            double x, double y, double angle, GrowableOrdinateArray ordinates) {
        double px = x - offset * sin(angle);
        double py = y + offset * cos(angle);
        ordinates.add(px, py);
    }

    /**
     * Appends to ordinates a point calculated as being the joining point of two segments with
     * angles angle01, angle02 and starting in x,y
     */
    private void appendInternalJoint(
            double x,
            double y,
            double angle01,
            double angle12,
            double dx10,
            double dy10,
            double dx12,
            double dy12,
            GrowableOrdinateArray ordinates) {
        double sa = offset * sin(angle01);
        double ca = offset * cos(angle01);
        double h = tan(0.5 * (angle12 - angle01));
        double hsa = h * sa;
        double hca = h * ca;
        double px = x - sa - hca;
        double py = y + ca - hsa;
        // a very small angle can cause the point to spike away, control this
        // by adding some limits (yes, these are just heuristics)
        double maxAllowedDistanceSquared =
                Math.max(
                        maxSearchDistanceSquared,
                        Math.max(squaredDistance(dx10, dy10), squaredDistance(dx12, dy12)));
        if (squaredDistance(px - x, py - y) > maxAllowedDistanceSquared) {
            double angle = atan2(py - y, px - x);
            double maxAllowedDistance = Math.sqrt(maxAllowedDistanceSquared);
            px = x + maxAllowedDistance * cos(angle);
            py = y + maxAllowedDistance * sin(angle);
        }
        ordinates.add(px, py);
    }
}
