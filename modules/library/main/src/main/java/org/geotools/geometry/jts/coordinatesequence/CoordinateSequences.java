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
 *
 *    Created on 31-dic-2004
 */
package org.geotools.geometry.jts.coordinatesequence;

import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.CurvedGeometry;
import org.locationtech.jts.algorithm.RobustDeterminant;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * Utility functions for coordinate sequences (extends the same named JTS class)
 *
 * @author Andrea Aime - OpenGeo
 * @author Martin Davis - OpenGeo
 */
public class CoordinateSequences extends org.locationtech.jts.geom.CoordinateSequences {

    /**
     * Computes whether a ring defined by an array of {@link Coordinate}s is oriented
     * counter-clockwise.
     *
     * <ul>
     *   <li>The list of points is assumed to have the first and last points equal.
     *   <li>This will handle coordinate lists which contain repeated points.
     * </ul>
     *
     * This algorithm is <b>only</b> guaranteed to work with valid rings. If the ring is invalid
     * (e.g. self-crosses or touches), the computed result may not be correct.
     *
     * @param ring an array of Coordinates forming a ring
     * @return true if the ring is oriented counter-clockwise.
     */
    public static boolean isCCW(CoordinateSequence ring) {
        // # of points without closing endpoint
        int nPts = ring.size() - 1;

        // find highest point
        double hiy = ring.getOrdinate(0, 1);
        int hiIndex = 0;
        for (int i = 1; i <= nPts; i++) {
            if (ring.getOrdinate(i, 1) > hiy) {
                hiy = ring.getOrdinate(i, 1);
                hiIndex = i;
            }
        }

        // find distinct point before highest point
        int iPrev = hiIndex;
        do {
            iPrev = iPrev - 1;
            if (iPrev < 0) iPrev = nPts;
        } while (equals2D(ring, iPrev, hiIndex) && iPrev != hiIndex);

        // find distinct point after highest point
        int iNext = hiIndex;
        do {
            iNext = (iNext + 1) % nPts;
        } while (equals2D(ring, iNext, hiIndex) && iNext != hiIndex);

        /**
         * This check catches cases where the ring contains an A-B-A configuration of points. This
         * can happen if the ring does not contain 3 distinct points (including the case where the
         * input array has fewer than 4 elements), or it contains coincident line segments.
         */
        if (equals2D(ring, iPrev, hiIndex)
                || equals2D(ring, iNext, hiIndex)
                || equals2D(ring, iPrev, iNext)) return false;

        int disc = computeOrientation(ring, iPrev, hiIndex, iNext);

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
            isCCW = (ring.getOrdinate(iPrev, 0) > ring.getOrdinate(iNext, 0));
        } else {
            // if area is positive, points are ordered CCW
            isCCW = (disc > 0);
        }
        return isCCW;
    }

    private static boolean equals2D(CoordinateSequence cs, int i, int j) {
        return cs.getOrdinate(i, 0) == cs.getOrdinate(j, 0)
                && cs.getOrdinate(i, 1) == cs.getOrdinate(j, 1);
    }

    public static int computeOrientation(CoordinateSequence cs, int p1, int p2, int q) {
        // travelling along p1->p2, turn counter clockwise to get to q return 1,
        // travelling along p1->p2, turn clockwise to get to q return -1,
        // p1, p2 and q are colinear return 0.
        double p1x = cs.getOrdinate(p1, 0);
        double p1y = cs.getOrdinate(p1, 1);
        double p2x = cs.getOrdinate(p2, 0);
        double p2y = cs.getOrdinate(p2, 1);
        double qx = cs.getOrdinate(q, 0);
        double qy = cs.getOrdinate(q, 1);
        double dx1 = p2x - p1x;
        double dy1 = p2y - p1y;
        double dx2 = qx - p2x;
        double dy2 = qy - p2y;
        return RobustDeterminant.signOfDet2x2(dx1, dy1, dx2, dy2);
    }

    /**
     * Gets the dimension of the coordinates in a {@link Geometry}, by reading it from a component
     * {@link CoordinateSequence}. This will be usually either 2 or 3.
     *
     * @param g a Geometry
     * @return the dimension of the coordinates in the Geometry
     */
    public static int coordinateDimension(Geometry g) {
        // common fast cases
        if (g instanceof CurvedGeometry<?>) {
            return ((CurvedGeometry<?>) g).getCoordinatesDimension();
        }
        if (g instanceof Point) return coordinateDimension(((Point) g).getCoordinateSequence());
        if (g instanceof LineString)
            return coordinateDimension(((LineString) g).getCoordinateSequence());
        if (g instanceof Polygon)
            return coordinateDimension(((Polygon) g).getExteriorRing().getCoordinateSequence());

        // dig down to find a CS
        CoordinateSequence cs = CoordinateSequenceFinder.find(g);
        return coordinateDimension(cs);
    }

    /**
     * Gets the effective dimension of a CoordinateSequence. This is a workaround for the issue that
     * CoordinateArraySequence does not keep an accurate dimension - it always reports dim=3, even
     * if there is no Z ordinate (ie they are NaN). This method checks for that case and reports
     * dim=2. Only the first coordinate is checked.
     *
     * <p>There is one small hole: if a CoordinateArraySequence is empty, the dimension will be
     * reported as 3.
     *
     * @param seq a CoordinateSequence
     * @return the effective dimension of the coordinate sequence
     */
    public static int coordinateDimension(CoordinateSequence seq) {
        if (seq == null) return 3;

        int dim = seq.getDimension();
        if (dim != 3) return dim;

        // hack to handle issue that CoordinateArraySequence always reports
        // dimension = 3
        // check if a Z value is NaN - if so, assume dim is 2
        if (seq instanceof CoordinateArraySequence) {
            if (seq.size() > 0) {
                if (Double.isNaN(seq.getOrdinate(0, CoordinateSequence.Y))) return 1;
                if (Double.isNaN(seq.getOrdinate(0, CoordinateSequence.Z))) return 2;
            }
        }
        return 3;
    }

    /**
     * Returns true if the two geometries are equal in N dimensions (normal geometry equality is
     * only 2D)
     */
    public static boolean equalsND(Geometry g1, Geometry g2) {
        // if not even in 2d, they are not equal
        if (!g1.equals(g2)) {
            return false;
        }
        int dim1 = coordinateDimension(g1);
        int dim2 = coordinateDimension(g2);
        if (dim1 != dim2) {
            return false;
        }
        if (dim1 == 2) {
            return true;
        }

        // ok, 2d equal, it means they have the same list of geometries and coordinate sequences, in
        // the same order
        List<CoordinateSequence> sequences1 = CoordinateSequenceCollector.find(g1);
        List<CoordinateSequence> sequences2 = CoordinateSequenceCollector.find(g2);
        if (sequences1.size() != sequences2.size()) {
            return false;
        }
        CoordinateSequenceComparator comparator = new CoordinateSequenceComparator();
        for (int i = 0; i < sequences1.size(); i++) {
            CoordinateSequence cs1 = sequences1.get(i);
            CoordinateSequence cs2 = sequences2.get(i);
            if (comparator.compare(cs1, cs2) != 0) {
                return false;
            }
        }

        return true;
    }

    private static class CoordinateSequenceFinder implements CoordinateSequenceFilter {

        public static CoordinateSequence find(Geometry g) {
            CoordinateSequenceFinder finder = new CoordinateSequenceFinder();
            g.apply(finder);
            return finder.getSeq();
        }

        private CoordinateSequence firstSeqFound = null;

        /**
         * Gets the coordinate sequence found (if any).
         *
         * @return the sequence found, or null if no sequence could be found
         */
        public CoordinateSequence getSeq() {
            return firstSeqFound;
        }

        public void filter(CoordinateSequence seq, int i) {
            if (firstSeqFound == null) firstSeqFound = seq;
        }

        public boolean isDone() {
            return firstSeqFound != null;
        }

        public boolean isGeometryChanged() {
            return false;
        }
    }

    private static class CoordinateSequenceCollector implements CoordinateSequenceFilter {

        public static List<CoordinateSequence> find(Geometry g) {
            CoordinateSequenceCollector finder = new CoordinateSequenceCollector();
            g.apply(finder);
            return finder.getSequences();
        }

        private List<CoordinateSequence> sequences = new ArrayList<>();

        public List<CoordinateSequence> getSequences() {
            return sequences;
        }

        public void filter(CoordinateSequence seq, int i) {
            sequences.add(seq);
        }

        public boolean isDone() {
            return false;
        }

        public boolean isGeometryChanged() {
            return false;
        }
    }
}
