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
package org.geotools.geometry.iso.index.quadtree;

/**
 * Provides a test for whether an interval is so small it should be considered
 * as zero for the purposes of inserting it into a binary tree. The reason this
 * check is necessary is that round-off error can cause the algorithm used to
 * subdivide an interval to fail, by computing a midpoint value which does not
 * lie strictly between the endpoints.
 * 
 *
 * @source $URL$
 * @version 1.7.2
 */
public class IntervalSize {

	/**
	 * This value is chosen to be a few powers of 2 less than the number of bits
	 * available in the double representation (i.e. 53). This should allow
	 * enough extra precision for simple computations to be correct, at least
	 * for comparison purposes.
	 */
	public static final int MIN_BINARY_EXPONENT = -50;

	/**
	 * Computes whether the interval [min, max] is effectively zero width. I.e.
	 * the width of the interval is so much less than the location of the
	 * interval that the midpoint of the interval cannot be represented
	 * precisely.
	 */
	public static boolean isZeroWidth(double min, double max) {
		double width = max - min;
		if (width == 0.0)
			return true;

		double maxAbs = Math.max(Math.abs(min), Math.abs(max));
		double scaledInterval = width / maxAbs;
		int level = DoubleBits.exponent(scaledInterval);
		return level <= MIN_BINARY_EXPONENT;
	}
}
