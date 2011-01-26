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

public class Point implements Shape, Cloneable {
    public double[] m_pCoords = null;

    public Point(double[] pCoords) {
        m_pCoords = new double[pCoords.length];
        System.arraycopy(pCoords, 0, m_pCoords, 0, pCoords.length);
    }

    public Point(final Point pt) {
        m_pCoords = new double[pt.m_pCoords.length];
        System.arraycopy(pt.m_pCoords, 0, m_pCoords, 0, pt.m_pCoords.length);
    }

    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point pt = (Point) o;

            if (pt.m_pCoords.length != m_pCoords.length) {
                return false;
            }

            for (int cIndex = 0; cIndex < m_pCoords.length; cIndex++) {
                if ((m_pCoords[cIndex] < (pt.m_pCoords[cIndex] - SpatialIndex.EPSILON))
                        || (m_pCoords[cIndex] > (pt.m_pCoords[cIndex] + SpatialIndex.EPSILON))) {
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
        return new Point(m_pCoords);
    }

    //
    // IShape interface
    //
    public boolean intersects(final Shape s) {
        if (s instanceof Region) {
            return ((Region) s).contains(this);
        }

        return false;
    }

    public boolean contains(final Shape s) {
        return false;
    }

    public boolean touches(final Shape s) {
        if (s instanceof Point && this.equals(s)) {
            return true;
        }

        if (s instanceof Region) {
            return ((Region) s).touches(this);
        }

        return false;
    }

    public double[] getCenter() {
        double[] pCoords = new double[m_pCoords.length];
        System.arraycopy(m_pCoords, 0, pCoords, 0, m_pCoords.length);

        return pCoords;
    }

    public int getDimension() {
        return m_pCoords.length;
    }

    public Region getMBR() {
        return new Region(m_pCoords, m_pCoords);
    }

    public double getArea() {
        return 0.0f;
    }

    public double getMinimumDistance(final Shape s) {
        if (s instanceof Region) {
            return ((Region) s).getMinimumDistance(this);
        }

        if (s instanceof Point) {
            return getMinimumDistance((Point) s);
        }

        throw new IllegalStateException("getMinimumDistance: Not implemented yet!");
    }

    double getMinimumDistance(final Point p) {
        if (m_pCoords.length != p.m_pCoords.length) {
            throw new IllegalArgumentException(
                "getMinimumDistance: Shape has the wrong number of dimensions.");
        }

        double ret = 0.0;

        for (int cIndex = 0; cIndex < m_pCoords.length; cIndex++) {
            ret += Math.pow(m_pCoords[cIndex] - p.m_pCoords[cIndex], 2.0);
        }

        return Math.sqrt(ret);
    }

    public double getCoord(int index) throws IndexOutOfBoundsException {
        if (index >= m_pCoords.length) {
            throw new IndexOutOfBoundsException("" + index);
        }

        return m_pCoords[index];
    }
}
