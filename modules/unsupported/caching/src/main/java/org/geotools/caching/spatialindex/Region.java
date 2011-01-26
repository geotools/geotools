// Spatial Index Library
//
// Copyright (C) 2002  Navel Ltd.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation;
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Contact information:
//  Mailing address:
//    Marios Hadjieleftheriou
//    University of California, Riverside
//    Department of Computer Science
//    Surge Building, Room 310
//    Riverside, CA 92521
//
//  Email:
//    marioh@cs.ucr.edu
package org.geotools.caching.spatialindex;

import java.io.Serializable;


public class Region implements Shape, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2627615073834425697L;
    private double[] m_pLow = null;
    private double[] m_pHigh = null;
    private volatile int hashCode = 0;

    public Region() {
    }

    public Region(final double[] pLow, final double[] pHigh) {
        if (pLow.length != pHigh.length) {
            throw new IllegalArgumentException(
                "Region: arguments have different number of dimensions.");
        }

        m_pLow = new double[pLow.length];
        System.arraycopy(pLow, 0, m_pLow, 0, pLow.length);

        m_pHigh = new double[pHigh.length];
        System.arraycopy(pHigh, 0, m_pHigh, 0, pHigh.length);
    }

    public Region(final Point low, final Point high) {
        if (low.m_pCoords.length != high.m_pCoords.length) {
            throw new IllegalArgumentException(
                "Region: arguments have different number of dimensions.");
        }

        m_pLow = new double[low.m_pCoords.length];
        System.arraycopy(low.m_pCoords, 0, m_pLow, 0, low.m_pCoords.length);
        m_pHigh = new double[high.m_pCoords.length];
        System.arraycopy(high.m_pCoords, 0, m_pHigh, 0, high.m_pCoords.length);
    }

    public Region(final Region r) {
        m_pLow = new double[r.m_pLow.length];
        System.arraycopy(r.m_pLow, 0, m_pLow, 0, r.m_pLow.length);
        m_pHigh = new double[r.m_pHigh.length];
        System.arraycopy(r.m_pHigh, 0, m_pHigh, 0, r.m_pHigh.length);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Region) {
            Region r = (Region) o;
            if (r.m_pLow.length != m_pLow.length) {
                return false;
            }
            for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
                if ((m_pLow[cIndex] != r.m_pLow[cIndex]) || (m_pHigh[cIndex] != r.m_pHigh[cIndex])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //
    // Cloneable interface
    //
    public Object clone() {
        return new Region(m_pLow, m_pHigh);
    }

    //
    // IShape interface
    //
    public boolean intersects(final Shape s) {
        if (s instanceof Region) {
            return intersects((Region) s);
        }

        if (s instanceof Point) {
            return contains((Point) s);
        }

        throw new IllegalStateException("intersects: Not implemented yet!");
    }

    public boolean contains(final Shape s) {
        if (s instanceof Region) {
            return contains((Region) s);
        }

        if (s instanceof Point) {
            return contains((Point) s);
        }

        throw new IllegalStateException("contains: Not implemented yet!");
    }

    public boolean touches(final Shape s) {
        if (s instanceof Region) {
            return touches((Region) s);
        }

        if (s instanceof Point) {
            return touches((Point) s);
        }

        throw new IllegalStateException("touches: Not implemented yet!");
    }

    public double[] getCenter() {
        double[] pCoords = new double[m_pLow.length];

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            pCoords[cIndex] = (m_pLow[cIndex] + m_pHigh[cIndex]) / 2.0;
        }

        return pCoords;
    }

    public int getDimension() {
        return m_pLow.length;
    }

    public Region getMBR() {
        return new Region(m_pLow, m_pHigh);
    }

    public double getArea() {
        double area = 1.0;

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            area *= (m_pHigh[cIndex] - m_pLow[cIndex]);
        }

        return area;
    }

    public double getMinimumDistance(final Shape s) {
        if (s instanceof Region) {
            return getMinimumDistance((Region) s);
        }

        if (s instanceof Point) {
            return getMinimumDistance((Point) s);
        }

        throw new IllegalStateException("getMinimumDistance: Not implemented yet!");
    }

    public boolean intersects(final Region r) {
        if (m_pLow.length != r.m_pLow.length) {
            throw new IllegalArgumentException(
                "intersects: Shape has the wrong number of dimensions.");
        }

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if ((m_pLow[cIndex] > r.m_pHigh[cIndex]) || (m_pHigh[cIndex] < r.m_pLow[cIndex])) {
                return false;
            }
        }

        return true;
    }

    public boolean contains(final Region r) {
        if (m_pLow.length != r.m_pLow.length) {
            throw new IllegalArgumentException(
                "contains: Shape has the wrong number of dimensions.");
        }

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if ((m_pLow[cIndex] > r.m_pLow[cIndex]) || (m_pHigh[cIndex] < r.m_pHigh[cIndex])) {
                return false;
            }
        }

        return true;
    }

    public boolean touches(final Region r) {
        if (m_pLow.length != r.m_pLow.length) {
            throw new IllegalArgumentException("touches: Shape has the wrong number of dimensions.");
        }

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if (((m_pLow[cIndex] > (r.m_pLow[cIndex] - SpatialIndex.EPSILON))
                    && (m_pLow[cIndex] < (r.m_pLow[cIndex] + SpatialIndex.EPSILON)))
                    || ((m_pHigh[cIndex] > (r.m_pHigh[cIndex] - SpatialIndex.EPSILON))
                    && (m_pHigh[cIndex] < (r.m_pHigh[cIndex] + SpatialIndex.EPSILON)))) {
                return true;
            }
        }

        return false;
    }

    public double getMinimumDistance(final Region r) {
        if (m_pLow.length != r.m_pLow.length) {
            throw new IllegalArgumentException(
                "getMinimumDistance: Shape has the wrong number of dimensions.");
        }

        double ret = 0.0;

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            double x = 0.0;

            if (r.m_pHigh[cIndex] < m_pLow[cIndex]) {
                x = Math.abs(r.m_pHigh[cIndex] - m_pLow[cIndex]);
            } else if (m_pHigh[cIndex] < r.m_pLow[cIndex]) {
                x = Math.abs(r.m_pLow[cIndex] - m_pHigh[cIndex]);
            }

            ret += (x * x);
        }

        return Math.sqrt(ret);
    }

    public boolean contains(final Point p) {
        if (m_pLow.length != p.m_pCoords.length) {
            throw new IllegalArgumentException(
                "contains: Shape has the wrong number of dimensions.");
        }

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if ((m_pLow[cIndex] > p.m_pCoords[cIndex]) || (m_pHigh[cIndex] < p.m_pCoords[cIndex])) {
                return false;
            }
        }

        return true;
    }

    public boolean touches(final Point p) {
        if (m_pLow.length != p.m_pCoords.length) {
            throw new IllegalArgumentException("touches: Shape has the wrong number of dimensions.");
        }

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if (((m_pLow[cIndex] > (p.m_pCoords[cIndex] - SpatialIndex.EPSILON))
                    && (m_pLow[cIndex] < (p.m_pCoords[cIndex] + SpatialIndex.EPSILON)))
                    || ((m_pHigh[cIndex] > (p.m_pCoords[cIndex] - SpatialIndex.EPSILON))
                    && (m_pHigh[cIndex] < (p.m_pCoords[cIndex] + SpatialIndex.EPSILON)))) {
                return true;
            }
        }

        return false;
    }

    public double getMinimumDistance(final Point p) {
        if (m_pLow.length != p.m_pCoords.length) {
            throw new IllegalArgumentException(
                "getMinimumDistance: Shape has the wrong number of dimensions.");
        }

        double ret = 0.0;

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if (p.m_pCoords[cIndex] < m_pLow[cIndex]) {
                ret += Math.pow(m_pLow[cIndex] - p.m_pCoords[cIndex], 2.0);
            } else if (p.m_pCoords[cIndex] > m_pHigh[cIndex]) {
                ret += Math.pow(p.m_pCoords[cIndex] - m_pHigh[cIndex], 2.0);
            }
        }

        return Math.sqrt(ret);
    }

    public double getIntersectingArea(final Region r) {
        if (m_pLow.length != r.m_pLow.length) {
            throw new IllegalArgumentException(
                "getIntersectingArea: Shape has the wrong number of dimensions.");
        }

        int cIndex;

        // check for intersection.
        // marioh: avoid function call since this is called billions of times.
        for (cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            if ((m_pLow[cIndex] > r.m_pHigh[cIndex]) || (m_pHigh[cIndex] < r.m_pLow[cIndex])) {
                return 0.0;
            }
        }

        double ret = 1.0;
        double f1;
        double f2;

        for (cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            f1 = Math.max(m_pLow[cIndex], r.m_pLow[cIndex]);
            f2 = Math.min(m_pHigh[cIndex], r.m_pHigh[cIndex]);
            ret *= (f2 - f1);
        }

        return ret;
    }

    public Region combinedRegion(final Region r) {
        if (m_pLow.length != r.m_pLow.length) {
            throw new IllegalArgumentException(
                "combinedRegion: Shape has the wrong number of dimensions.");
        }

        double[] mn = new double[m_pLow.length];
        double[] mx = new double[m_pLow.length];

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            mn[cIndex] = Math.min(m_pLow[cIndex], r.m_pLow[cIndex]);
            mx[cIndex] = Math.max(m_pHigh[cIndex], r.m_pHigh[cIndex]);
        }

        return new Region(mn, mx);
    }

    public static Region combinedRegion(Region[] pRegions) {
        double[] mn = new double[pRegions[0].m_pLow.length];
        double[] mx = new double[pRegions[0].m_pLow.length];

        for (int cDim = 0; cDim < pRegions[0].m_pLow.length; cDim++) {
            mn[cDim] = Double.POSITIVE_INFINITY;
            mx[cDim] = Double.NEGATIVE_INFINITY;

            for (int cIndex = 0; cIndex < pRegions.length; cIndex++) {
                mn[cDim] = Math.min(mn[cDim], pRegions[cIndex].m_pLow[cDim]);
                mx[cDim] = Math.max(mx[cDim], pRegions[cIndex].m_pHigh[cDim]);
            }
        }

        return new Region(mn, mx);
    }

    // Modifies the first argument to include the second.
    public static void combinedRegion(Region pToModify, final Region pConst) {
        if (pToModify.m_pLow.length != pConst.m_pLow.length) {
            throw new IllegalArgumentException(
                "combineRegion: Shape has the wrong number of dimensions.");
        }

        for (int cIndex = 0; cIndex < pToModify.m_pLow.length; cIndex++) {
            pToModify.m_pLow[cIndex] = Math.min(pToModify.m_pLow[cIndex], pConst.m_pLow[cIndex]);
            pToModify.m_pHigh[cIndex] = Math.max(pToModify.m_pHigh[cIndex], pConst.m_pHigh[cIndex]);
        }
    }

    // Returns the margin of a region. It is calcuated as the sum of  2^(d-1) * width in each dimension.
    public double getMargin() {
        double mul = Math.pow(2.0, ((double) m_pLow.length) - 1.0);
        double margin = 0.0;

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
            margin += ((m_pHigh[cIndex] - m_pLow[cIndex]) * mul);
        }

        return margin;
    }

    public double getLow(int index) throws IndexOutOfBoundsException {
        if (index >= m_pLow.length) {
            throw new IndexOutOfBoundsException("" + index);
        }

        return m_pLow[index];
    }

    public double getHigh(int index) throws IndexOutOfBoundsException {
        if (index >= m_pLow.length) {
            throw new IndexOutOfBoundsException("" + index);
        }

        return m_pHigh[index];
    }

    public String toString() {
        String s = "";

        for (int cIndex = 0; cIndex < m_pLow.length; cIndex++)
            s += (m_pLow[cIndex] + " ");

        s += ": ";

        for (int cIndex = 0; cIndex < m_pHigh.length; cIndex++)
            s += (m_pHigh[cIndex] + " ");

        return s;
    }

    public int hashCode() {
        if (hashCode == 0) {
            int hash = 17;
            int c;
            long l;

            for (int cIndex = 0; cIndex < m_pLow.length; cIndex++) {
                l = Double.doubleToLongBits(m_pLow[cIndex]);
                c = (int) (l ^ (l >>> 32));
                hash = (37 * hash) + c;
                l = Double.doubleToLongBits(m_pHigh[cIndex]);
                c = (int) (l ^ (l >>> 32));
                hash = (37 * hash) + c;
            }

            hashCode = hash;
        }

        return hashCode;
    }
}
