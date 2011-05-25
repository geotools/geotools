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

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Envelope;

/**
 * A Key is a unique identifier for a node in a quadtree. It contains a
 * lower-left point and a level number. The level number is the power of two for
 * the size of the node envelope
 * 
 *
 *
 * @source $URL$
 * @version 1.7.2
 */
public class Key {

	public static int computeQuadLevel(Envelope env) {
		double dx = env.getWidth();
		double dy = env.getHeight();
		double dMax = dx > dy ? dx : dy;
		int level = DoubleBits.exponent(dMax) + 1;
		return level;
	}

	// the fields which make up the key
	private Coordinate pt = new Coordinate();

	private int level = 0;

	// auxiliary data which is derived from the key for use in computation
	private Envelope env = null;

	public Key(Envelope itemEnv) {
		computeKey(itemEnv);
	}

	public Coordinate getPoint() {
		return pt;
	}

	public int getLevel() {
		return level;
	}

	public Envelope getEnvelope() {
		return env;
	}

	public Coordinate getCentre() {
		return new Coordinate((env.getMinX() + env.getMaxX()) / 2, (env
				.getMinY() + env.getMaxY()) / 2);
	}

	/**
	 * return a square envelope containing the argument envelope, whose extent
	 * is a power of two and which is based at a power of 2
	 */
	public void computeKey(Envelope itemEnv) {
		level = computeQuadLevel(itemEnv);
		env = new Envelope();
		computeKey(level, itemEnv);
		// MD - would be nice to have a non-iterative form of this algorithm
		while (!env.contains(itemEnv)) {
			level += 1;
			computeKey(level, itemEnv);
		}
	}

	private void computeKey(int level, Envelope itemEnv) {
		double quadSize = DoubleBits.powerOf2(level);
		pt.x = Math.floor(itemEnv.getMinX() / quadSize) * quadSize;
		pt.y = Math.floor(itemEnv.getMinY() / quadSize) * quadSize;
		env.init(pt.x, pt.x + quadSize, pt.y, pt.y + quadSize);
	}
}
