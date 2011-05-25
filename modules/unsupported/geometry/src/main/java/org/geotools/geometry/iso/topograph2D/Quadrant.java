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
package org.geotools.geometry.iso.topograph2D;


/**
 * Utility functions for working with quadrants, which are numbered as follows:
 * 
 * <pre>
 *  1 | 0
 *  --+--
 *  2 | 3
 *  &lt;pre&gt;
 * 
 *
 *
 * @source $URL$
 */
public class Quadrant {
	/**
	 * Returns the quadrant of a directed line segment (specified as x and y
	 * displacements, which cannot both be 0).
	 */
	public static int quadrant(double dx, double dy) {
		if (dx == 0.0 && dy == 0.0)
			throw new IllegalArgumentException(
					"Cannot compute the quadrant for point ( " + dx + ", " + dy
							+ " )");
		if (dx >= 0) {
			if (dy >= 0)
				return 0;
			else
				return 3;
		} else {
			if (dy >= 0)
				return 1;
			else
				return 2;
		}
	}

	/**
	 * Returns the quadrant of a directed line segment from p0 to p1.
	 */
	public static int quadrant(Coordinate p0, Coordinate p1) {
		double dx = p1.x - p0.x;
		double dy = p1.y - p0.y;
		if (dx == 0.0 && dy == 0.0)
			throw new IllegalArgumentException(
					"Cannot compute the quadrant for two identical points "
							+ p0);
		return quadrant(dx, dy);
	}

	/**
	 * Returns true if the quadrants are 1 and 3, or 2 and 4
	 */
	public static boolean isOpposite(int quad1, int quad2) {
		if (quad1 == quad2)
			return false;
		int diff = (quad1 - quad2 + 4) % 4;
		// if quadrants are not adjacent, they are opposite
		if (diff == 2)
			return true;
		return false;
	}

	/**
	 * Returns the right-hand quadrant of the halfplane defined by the two
	 * quadrants, or -1 if the quadrants are opposite, or the quadrant if they
	 * are identical.
	 */
	public static int commonHalfPlane(int quad1, int quad2) {
		// if quadrants are the same they do not determine a unique common
		// halfplane.
		// Simply return one of the two possibilities
		if (quad1 == quad2)
			return quad1;
		int diff = (quad1 - quad2 + 4) % 4;
		// if quadrants are not adjacent, they do not share a common halfplane
		if (diff == 2)
			return -1;
		//
		int min = (quad1 < quad2) ? quad1 : quad2;
		int max = (quad1 > quad2) ? quad1 : quad2;
		// for this one case, the righthand plane is NOT the minimum index;
		if (min == 0 && max == 3)
			return 3;
		// in general, the halfplane index is the minimum of the two adjacent
		// quadrants
		return min;
	}

	/**
	 * Returns whether the given quadrant lies within the given halfplane
	 * (specified by its right-hand quadrant).
	 */
	public static boolean isInHalfPlane(int quad, int halfPlane) {
		if (halfPlane == 3) {
			return quad == 3 || quad == 0;
		}
		return quad == halfPlane || quad == halfPlane + 1;
	}

	/**
	 * Returns true if the given quadrant is 0 or 1.
	 */
	public static boolean isNorthern(int quad) {
		return quad == 0 || quad == 1;
	}
}
