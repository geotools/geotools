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

import com.vividsolutions.jts.algorithm.RobustDeterminant;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;

/**
 * Utility functions for coordinate sequences (extends the same named JTS class)
 * @author Andrea Aime - OpenGeo
 */
public class CoordinateSequences extends com.vividsolutions.jts.geom.CoordinateSequences {
    
    /**
     * Computes whether a ring defined by an array of {@link Coordinate}s is
     * oriented counter-clockwise.
     * <ul>
     * <li>The list of points is assumed to have the first and last points equal.
     * <li>This will handle coordinate lists which contain repeated points.
     * </ul>
     * This algorithm is <b>only</b> guaranteed to work with valid rings.
     * If the ring is invalid (e.g. self-crosses or touches),
     * the computed result may not be correct.
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
       * This check catches cases where the ring contains an A-B-A configuration of points.
       * This can happen if the ring does not contain 3 distinct points
       * (including the case where the input array has fewer than 4 elements),
       * or it contains coincident line segments.
       */
      if (equals2D(ring, iPrev, hiIndex) || equals2D(ring, iNext, hiIndex)|| equals2D(ring, iPrev, iNext))
        return false;

      int disc = computeOrientation(ring, iPrev, hiIndex, iNext);

      /**
       *  If disc is exactly 0, lines are collinear.  There are two possible cases:
       *  (1) the lines lie along the x axis in opposite directions
       *  (2) the lines lie on top of one another
       *
       *  (1) is handled by checking if next is left of prev ==> CCW
       *  (2) will never happen if the ring is valid, so don't check for it
       *  (Might want to assert this)
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
        return cs.getOrdinate(i, 0) ==  cs.getOrdinate(j, 0) &&
               cs.getOrdinate(i, 1) ==  cs.getOrdinate(j, 1); 
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

}
