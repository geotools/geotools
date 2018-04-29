/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util.algorithm2D;

import java.util.List;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.util.CoordinateArrays;
import org.opengis.geometry.DirectPosition;

/**
 * Specifies and implements various fundamental Computational Geometric algorithms. The algorithms
 * supplied in this class are robust for double-precision floating point.
 *
 * @source $URL$
 */
public class CGAlgorithms {

    /** A value that indicates an orientation of clockwise, or a right turn. */
    public static final int CLOCKWISE = -1;

    public static final int RIGHT = CLOCKWISE;

    /** A value that indicates an orientation of counterclockwise, or a left turn. */
    public static final int COUNTERCLOCKWISE = 1;

    public static final int LEFT = COUNTERCLOCKWISE;

    /** A value that indicates an orientation of collinear, or no turn (straight). */
    public static final int COLLINEAR = 0;

    public static final int STRAIGHT = COLLINEAR;

    /**
     * Returns the index of the direction of the point <code>q</code> relative to a vector specified
     * by <code>p1-p2</code>.
     *
     * @param p1 the origin point of the vector
     * @param p2 the final point of the vector
     * @param q the point to compute the direction to
     * @return 1 if q is counter-clockwise (left) from p1-p2
     * @return -1 if q is clockwise (right) from p1-p2
     * @return 0 if q is collinear with p1-p2
     */
    public static int orientationIndex(Coordinate p1, Coordinate p2, Coordinate q) {
        // travelling along p1->p2, turn counter clockwise to get to q return 1,
        // travelling along p1->p2, turn clockwise to get to q return -1,
        // p1, p2 and q are colinear return 0.
        double dx1 = p2.x - p1.x;
        double dy1 = p2.y - p1.y;
        double dx2 = q.x - p2.x;
        double dy2 = q.y - p2.y;
        return RobustDeterminant.signOfDet2x2(dx1, dy1, dx2, dy2);
    }

    public CGAlgorithms() {}

    /**
     * Test whether a point lies inside a ring. The ring may be oriented in either direction. If the
     * point lies on the ring boundary the result of this method is unspecified.
     *
     * <p>This algorithm does not attempt to first check the point against the envelope of the ring.
     *
     * @param p point to check for ring inclusion
     * @param ring assumed to have first point identical to last point
     * @return <code>true</code> if p is inside ring
     */
    public static boolean isPointInRing(Coordinate p, Coordinate[] ring) {
        /*
         * For each segment l = (i-1, i), see if it crosses ray from test point
         * in positive x direction.
         */
        int crossings = 0; // number of segment/ray crossings
        for (int i = 1; i < ring.length; i++) {
            int i1 = i - 1;
            Coordinate p1 = ring[i];
            Coordinate p2 = ring[i1];

            if (((p1.y > p.y) && (p2.y <= p.y)) || ((p2.y > p.y) && (p1.y <= p.y))) {
                double x1 = p1.x - p.x;
                double y1 = p1.y - p.y;
                double x2 = p2.x - p.x;
                double y2 = p2.y - p.y;
                /*
                 * segment straddles x axis, so compute intersection with
                 * x-axis.
                 */
                double xInt = RobustDeterminant.signOfDet2x2(x1, y1, x2, y2) / (y2 - y1);
                // xsave = xInt;
                /*
                 * crosses ray if strictly positive intersection.
                 */
                if (xInt > 0.0) {
                    crossings++;
                }
            }
        }
        /*
         * p is inside if number of crossings is odd.
         */
        if ((crossings % 2) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test whether a point lies on the line segments defined by a list of coordinates.
     *
     * @return true true if the point is a vertex of the line or lies in the interior of a line
     *     segment in the linestring
     */
    public static boolean isOnLine(Coordinate p, Coordinate[] pt) {
        LineIntersector lineIntersector = new RobustLineIntersector();
        for (int i = 1; i < pt.length; i++) {
            Coordinate p0 = pt[i - 1];
            Coordinate p1 = pt[i];
            lineIntersector.computeIntersection(p, p0, p1);
            if (lineIntersector.hasIntersection()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Same method as public static boolean isCCW(Coordinate[] ring) based on a DirectPosition list.
     * ===================================================== Computes whether a ring defined by an
     * array of {@link Coordinate} is oriented counter-clockwise.
     *
     * <ul>
     *   <li>The list of points is assumed to have the first and last points equal.
     *   <li>This will handle coordinate lists which contain repeated points.
     * </ul>
     *
     * This algorithm is <b>only</b> guaranteed to work with valid rings. If the ring is invalid
     * (e.g. self-crosses or touches), the computed result <b>may</b> not be correct.
     *
     * @param ring a list of direct positions forming a ring
     * @return <code>true</code> if the ring is oriented counter-clockwise.
     */
    public static boolean isCCW(List<DirectPosition> ring) {
        Coordinate[] coords = CoordinateArrays.toCoordinateArray(ring);
        return isCCW(coords);
    }

    /**
     * Computes whether a ring defined by an array of {@link Coordinate} is oriented
     * counter-clockwise.
     *
     * <ul>
     *   <li>The list of points is assumed to have the first and last points equal.
     *   <li>This will handle coordinate lists which contain repeated points.
     * </ul>
     *
     * This algorithm is <b>only</b> guaranteed to work with valid rings. If the ring is invalid
     * (e.g. self-crosses or touches), the computed result <b>may</b> not be correct.
     *
     * @param ring an array of coordinates forming a ring
     * @return <code>true</code> if the ring is oriented counter-clockwise.
     */
    public static boolean isCCW(Coordinate[] ring) {
        // # of points without closing endpoint
        int nPts = ring.length - 1;

        // find highest point
        Coordinate hiPt = ring[0];
        int hiIndex = 0;
        for (int i = 1; i <= nPts; i++) {
            Coordinate p = ring[i];
            if (p.y > hiPt.y) {
                hiPt = p;
                hiIndex = i;
            }
        }

        // find distinct point before highest point
        int iPrev = hiIndex;
        do {
            iPrev = iPrev - 1;
            if (iPrev < 0) iPrev = nPts;
        } while (ring[iPrev].equals2D(hiPt) && iPrev != hiIndex);

        // find distinct point after highest point
        int iNext = hiIndex;
        do {
            iNext = (iNext + 1) % nPts;
        } while (ring[iNext].equals2D(hiPt) && iNext != hiIndex);

        Coordinate prev = ring[iPrev];
        Coordinate next = ring[iNext];

        /**
         * This check catches cases where the ring contains an A-B-A configuration of points. This
         * can happen if the ring does not contain 3 distinct points (including the case where the
         * input array has fewer than 4 elements), or it contains coincident line segments.
         */
        if (prev.equals2D(hiPt) || next.equals2D(hiPt) || prev.equals2D(next)) return false;

        int disc = computeOrientation(prev, hiPt, next);

        /**
         * If disc is exactly 0, lines are collinear. There are two possible cases: (1) the lines
         * lie along the x axis in opposite directions (2) the lines lie on top of one another
         *
         * <p>(1) is handled by checking if next is left of prev ==> CCW (2) will never happen if
         * the ring is valid, so don't check for it (Might want to assert this)
         */
        boolean isCCW = false;
        if (disc == 0) {
            // poly is CCW if prev x is right of next x
            isCCW = (prev.x > next.x);
        } else {
            // if area is positive, points are ordered CCW
            isCCW = (disc > 0);
        }
        return isCCW;
    }

    /**
     * Computes the orientation of a point q to the directed line segment p1-p2. The orientation of
     * a point relative to a directed line segment indicates which way you turn to get to q after
     * travelling from p1 to p2.
     *
     * @return 1 if q is counter-clockwise from p1-p2
     * @return -1 if q is clockwise from p1-p2
     * @return 0 if q is collinear with p1-p2
     */
    public static int computeOrientation(Coordinate p1, Coordinate p2, Coordinate q) {
        return orientationIndex(p1, p2, q);
    }

    /**
     * Computes the distance from a point p to a line segment AB
     *
     * <p>Note: NON-ROBUST!
     *
     * @param p the point to compute the distance for
     * @param A one point of the line
     * @param B another point of the line (must be different to A)
     * @return the distance from p to line segment AB
     */
    public static double distancePointLine(Coordinate p, Coordinate A, Coordinate B) {
        // if start==end, then use pt distance
        if (A.equals(B)) return p.distance(A);

        // otherwise use comp.graphics.algorithms Frequently Asked Questions
        // method
        /*
         * (1) AC dot AB r = --------- ||AB||^2 r has the following meaning: r=0
         * P = A r=1 P = B r<0 P is on the backward extension of AB r>1 P is on
         * the forward extension of AB 0<r<1 P is interior to AB
         */

        double r =
                ((p.x - A.x) * (B.x - A.x) + (p.y - A.y) * (B.y - A.y))
                        / ((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));

        if (r <= 0.0) return p.distance(A);
        if (r >= 1.0) return p.distance(B);

        /*
         * (2) (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay) s = -----------------------------
         * L^2
         *
         * Then the distance from C to P = |s|*L.
         */

        double s =
                ((A.y - p.y) * (B.x - A.x) - (A.x - p.x) * (B.y - A.y))
                        / ((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));

        return Math.abs(s) * Math.sqrt(((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y)));
    }

    /**
     * Computes the perpendicular distance from a point p to the (infinite) line containing the
     * points AB
     *
     * @param p the point to compute the distance for
     * @param A one point of the line
     * @param B another point of the line (must be different to A)
     * @return the distance from p to line AB
     */
    public static double distancePointLinePerpendicular(Coordinate p, Coordinate A, Coordinate B) {
        // use comp.graphics.algorithms Frequently Asked Questions method
        /*
         * (2) (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay) s = -----------------------------
         * L^2
         *
         * Then the distance from C to P = |s|*L.
         */

        double s =
                ((A.y - p.y) * (B.x - A.x) - (A.x - p.x) * (B.y - A.y))
                        / ((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));

        return Math.abs(s) * Math.sqrt(((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y)));
    }

    /**
     * Computes the distance from a line segment AB to a line segment CD
     *
     * <p>Note: NON-ROBUST!
     *
     * @param A a point of one line
     * @param B the second point of (must be different to A)
     * @param C one point of the line
     * @param D another point of the line (must be different to A)
     */
    public static double distanceLineLine(Coordinate A, Coordinate B, Coordinate C, Coordinate D) {
        // check for zero-length segments
        if (A.equals(B)) return distancePointLine(A, C, D);
        if (C.equals(D)) return distancePointLine(D, A, B);

        // AB and CD are line segments
        /*
         * from comp.graphics.algo
         *
         * Solving the above for r and s yields (Ay-Cy)(Dx-Cx)-(Ax-Cx)(Dy-Cy) r =
         * ----------------------------- (eqn 1) (Bx-Ax)(Dy-Cy)-(By-Ay)(Dx-Cx)
         *
         * (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay) s = ----------------------------- (eqn
         * 2) (Bx-Ax)(Dy-Cy)-(By-Ay)(Dx-Cx) Let P be the position vector of the
         * intersection point, then P=A+r(B-A) or Px=Ax+r(Bx-Ax) Py=Ay+r(By-Ay)
         * By examining the values of r & s, you can also determine some other
         * limiting conditions: If 0<=r<=1 & 0<=s<=1, intersection exists r<0
         * or r>1 or s<0 or s>1 line segments do not intersect If the
         * denominator in eqn 1 is zero, AB & CD are parallel If the numerator
         * in eqn 1 is also zero, AB & CD are collinear.
         *
         */
        double r_top = (A.y - C.y) * (D.x - C.x) - (A.x - C.x) * (D.y - C.y);
        double r_bot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);

        double s_top = (A.y - C.y) * (B.x - A.x) - (A.x - C.x) * (B.y - A.y);
        double s_bot = (B.x - A.x) * (D.y - C.y) - (B.y - A.y) * (D.x - C.x);

        if ((r_bot == 0) || (s_bot == 0)) {
            return Math.min(
                    distancePointLine(A, C, D),
                    Math.min(
                            distancePointLine(B, C, D),
                            Math.min(distancePointLine(C, A, B), distancePointLine(D, A, B))));
        }
        double s = s_top / s_bot;
        double r = r_top / r_bot;

        if ((r < 0) || (r > 1) || (s < 0) || (s > 1)) {
            // no intersection
            return Math.min(
                    distancePointLine(A, C, D),
                    Math.min(
                            distancePointLine(B, C, D),
                            Math.min(distancePointLine(C, A, B), distancePointLine(D, A, B))));
        }
        return 0.0; // intersection exists
    }

    /** Returns the signed area for a ring. The area is positive if the ring is oriented CW. */
    public static double signedArea(Coordinate[] ring) {
        if (ring.length < 3) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < ring.length - 1; i++) {
            double bx = ring[i].x;
            double by = ring[i].y;
            double cx = ring[i + 1].x;
            double cy = ring[i + 1].y;
            sum += (bx + cx) * (cy - by);
        }
        return -sum / 2.0;
    }

    /**
     * Computes the length of a linestring specified by a sequence of points.
     *
     * @param pts the points specifying the linestring
     * @return the length of the linestring
     */
    // TODO evtl wieder einkommentieren wenn von operationen benoetigt
    // public static double length(CoordinateSequence pts) {
    // if (pts.size() < 1) return 0.0;
    // double sum = 0.0;
    // for (int i = 1; i < pts.size(); i++) {
    // sum += pts.getCoordinate(i).distance(pts.getCoordinate(i - 1));
    // }
    // return sum;
    // }
}
