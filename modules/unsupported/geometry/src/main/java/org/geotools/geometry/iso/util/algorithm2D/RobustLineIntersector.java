/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Envelope;
import org.geotools.geometry.iso.util.Assert;

/**
 * A robust version of {@link LineIntersector}.
 *
 * @see RobustDeterminant
 */
public class RobustLineIntersector extends LineIntersector {

    public RobustLineIntersector() {}

    public void computeIntersection(Coordinate p, Coordinate p1, Coordinate p2) {
        isProper = false;
        // do between check first, since it is faster than the orientation test
        if (Envelope.intersects(p1, p2, p)) {
            if ((CGAlgorithms.orientationIndex(p1, p2, p) == 0)
                    && (CGAlgorithms.orientationIndex(p2, p1, p) == 0)) {
                isProper = true;
                if (p.equals(p1) || p.equals(p2)) {
                    isProper = false;
                }
                result = DO_INTERSECT;
                return;
            }
        }
        result = DONT_INTERSECT;
    }

    protected int computeIntersect(Coordinate p1, Coordinate p2, Coordinate q1, Coordinate q2) {
        isProper = false;

        // first try a fast test to see if the envelopes of the lines intersect
        if (!Envelope.intersects(p1, p2, q1, q2)) return DONT_INTERSECT;

        // for each endpoint, compute which side of the other segment it lies
        // if both endpoints lie on the same side of the other segment,
        // the segments do not intersect
        int Pq1 = CGAlgorithms.orientationIndex(p1, p2, q1);
        int Pq2 = CGAlgorithms.orientationIndex(p1, p2, q2);

        if ((Pq1 > 0 && Pq2 > 0) || (Pq1 < 0 && Pq2 < 0)) {
            return DONT_INTERSECT;
        }

        int Qp1 = CGAlgorithms.orientationIndex(q1, q2, p1);
        int Qp2 = CGAlgorithms.orientationIndex(q1, q2, p2);

        if ((Qp1 > 0 && Qp2 > 0) || (Qp1 < 0 && Qp2 < 0)) {
            return DONT_INTERSECT;
        }

        boolean collinear = Pq1 == 0 && Pq2 == 0 && Qp1 == 0 && Qp2 == 0;
        if (collinear) {
            return computeCollinearIntersection(p1, p2, q1, q2);
        }
        /**
         * Check if the intersection is an endpoint. If it is, copy the endpoint as the intersection
         * point. Copying the point rather than computing it ensures the point has the exact value,
         * which is important for robustness. It is sufficient to simply check for an endpoint which
         * is on the other line, since at this point we know that the inputLines must intersect.
         */
        if (Pq1 == 0 || Pq2 == 0 || Qp1 == 0 || Qp2 == 0) {
            isProper = false;
            if (Pq1 == 0) {
                intPt[0] = new Coordinate(q1);
            }
            if (Pq2 == 0) {
                intPt[0] = new Coordinate(q2);
            }
            if (Qp1 == 0) {
                intPt[0] = new Coordinate(p1);
            }
            if (Qp2 == 0) {
                intPt[0] = new Coordinate(p2);
            }
        } else {
            isProper = true;
            intPt[0] = intersection(p1, p2, q1, q2);
        }
        return DO_INTERSECT;
    }

    /*
     * private boolean intersectsEnvelope(Coordinate p1, Coordinate p2,
     * Coordinate q) { if (((q.x >= Math.min(p1.x, p2.x)) && (q.x <=
     * Math.max(p1.x, p2.x))) && ((q.y >= Math.min(p1.y, p2.y)) && (q.y <=
     * Math.max(p1.y, p2.y)))) { return true; } return false; }
     */

    private int computeCollinearIntersection(
            Coordinate p1, Coordinate p2, Coordinate q1, Coordinate q2) {
        boolean p1q1p2 = Envelope.intersects(p1, p2, q1);
        boolean p1q2p2 = Envelope.intersects(p1, p2, q2);
        boolean q1p1q2 = Envelope.intersects(q1, q2, p1);
        boolean q1p2q2 = Envelope.intersects(q1, q2, p2);

        if (p1q1p2 && p1q2p2) {
            intPt[0] = q1;
            intPt[1] = q2;
            return COLLINEAR;
        }
        if (q1p1q2 && q1p2q2) {
            intPt[0] = p1;
            intPt[1] = p2;
            return COLLINEAR;
        }
        if (p1q1p2 && q1p1q2) {
            intPt[0] = q1;
            intPt[1] = p1;
            return q1.equals(p1) && !p1q2p2 && !q1p2q2 ? DO_INTERSECT : COLLINEAR;
        }
        if (p1q1p2 && q1p2q2) {
            intPt[0] = q1;
            intPt[1] = p2;
            return q1.equals(p2) && !p1q2p2 && !q1p1q2 ? DO_INTERSECT : COLLINEAR;
        }
        if (p1q2p2 && q1p1q2) {
            intPt[0] = q2;
            intPt[1] = p1;
            return q2.equals(p1) && !p1q1p2 && !q1p2q2 ? DO_INTERSECT : COLLINEAR;
        }
        if (p1q2p2 && q1p2q2) {
            intPt[0] = q2;
            intPt[1] = p2;
            return q2.equals(p2) && !p1q1p2 && !q1p1q2 ? DO_INTERSECT : COLLINEAR;
        }
        return DONT_INTERSECT;
    }

    /**
     * This method computes the actual value of the intersection point. To obtain the maximum
     * precision from the intersection calculation, the coordinates are normalized by subtracting
     * the minimum ordinate values (in absolute value). This has the effect of removing common
     * significant digits from the calculation to maintain more bits of precision.
     */
    private Coordinate intersection(Coordinate p1, Coordinate p2, Coordinate q1, Coordinate q2) {
        Coordinate n1 = new Coordinate(p1);
        Coordinate n2 = new Coordinate(p2);
        Coordinate n3 = new Coordinate(q1);
        Coordinate n4 = new Coordinate(q2);
        Coordinate normPt = new Coordinate();
        normalizeToEnvCentre(n1, n2, n3, n4, normPt);

        Coordinate intPt = null;
        try {
            intPt = HCoordinate.intersection(n1, n2, n3, n4);
        } catch (NotRepresentableException e) {
            Assert.shouldNeverReachHere("Coordinate for intersection is not calculable");
            // EnvelopeMidpointIntersector emInt = new
            // EnvelopeMidpointIntersector(n1, n2, n3, n4);
            // intPt = emInt.getIntersection();
        }

        intPt.x += normPt.x;
        intPt.y += normPt.y;

        /**
         * MD - May 4 2005 - This is still a problem. Here is a failure case:
         *
         * <p>LINESTRING (2089426.5233462777 1180182.3877339689, 2085646.6891757075
         * 1195618.7333999649) LINESTRING (1889281.8148903656 1997547.0560044837, 2259977.3672235999
         * 483675.17050843034) int point = (2097408.2633752143,1144595.8008114607)
         */
        // if (!isInSegmentEnvelopes(intPt)) {
        // System.out.println("Intersection outside segment envelopes: " + intPt);
        // }
        /*
         * // disabled until a better solution is found if (!
         * isInSegmentEnvelopes(intPt)) { // System.out.println("first value
         * outside segment envelopes: " + intPt);
         *
         * IteratedBisectionIntersector ibi = new
         * IteratedBisectionIntersector(p1, p2, q1, q2); intPt =
         * ibi.getIntersection(); } if (! isInSegmentEnvelopes(intPt)) {
         * // System.out.println("ERROR - outside segment envelopes: " + intPt);
         *
         * IteratedBisectionIntersector ibi = new
         * IteratedBisectionIntersector(p1, p2, q1, q2); Coordinate testPt =
         * ibi.getIntersection(); }
         */

        if (precisionModel != null) {
            precisionModel.makePrecise(intPt);
        }

        return intPt;
    }

    /**
     * Normalize the supplied coordinates to so that the midpoint of their intersection envelope
     * lies at the origin.
     */
    private void normalizeToEnvCentre(
            Coordinate n00, Coordinate n01, Coordinate n10, Coordinate n11, Coordinate normPt) {
        double minX0 = n00.x < n01.x ? n00.x : n01.x;
        double minY0 = n00.y < n01.y ? n00.y : n01.y;
        double maxX0 = n00.x > n01.x ? n00.x : n01.x;
        double maxY0 = n00.y > n01.y ? n00.y : n01.y;

        double minX1 = n10.x < n11.x ? n10.x : n11.x;
        double minY1 = n10.y < n11.y ? n10.y : n11.y;
        double maxX1 = n10.x > n11.x ? n10.x : n11.x;
        double maxY1 = n10.y > n11.y ? n10.y : n11.y;

        double intMinX = minX0 > minX1 ? minX0 : minX1;
        double intMaxX = maxX0 < maxX1 ? maxX0 : maxX1;
        double intMinY = minY0 > minY1 ? minY0 : minY1;
        double intMaxY = maxY0 < maxY1 ? maxY0 : maxY1;

        double intMidX = (intMinX + intMaxX) / 2.0;
        double intMidY = (intMinY + intMaxY) / 2.0;
        normPt.x = intMidX;
        normPt.y = intMidY;

        /*
         * // equilavalent code using more modular but slower method Envelope
         * env0 = new Envelope(n00, n01); Envelope env1 = new Envelope(n10,
         * n11); Envelope intEnv = env0.intersection(env1); Coordinate intMidPt =
         * intEnv.centre();
         *
         * normPt.x = intMidPt.x; normPt.y = intMidPt.y;
         */

        n00.x -= normPt.x;
        n00.y -= normPt.y;
        n01.x -= normPt.x;
        n01.y -= normPt.y;
        n10.x -= normPt.x;
        n10.y -= normPt.y;
        n11.x -= normPt.x;
        n11.y -= normPt.y;
    }
}
