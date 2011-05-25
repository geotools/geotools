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
 * A Position indicates the position of a Location relative to a graph component
 * (Node, Edge, or Area).
 *
 *
 * @source $URL$
 */
public class Position {

	/** An indicator that a Location is <i>on</i> a GraphComponent */
	public static final int ON = 0;

	/** An indicator that a Location is to the <i>left</i> of a GraphComponent */
	public static final int LEFT = 1;

	/** An indicator that a Location is to the <i>right</i> of a GraphComponent */
	public static final int RIGHT = 2;

	/**
	 * Returns LEFT if the position is RIGHT, RIGHT if the position is LEFT, or
	 * the position otherwise.
	 */
	public static final int opposite(int position) {
		if (position == LEFT)
			return RIGHT;
		if (position == RIGHT)
			return LEFT;
		return position;
	}
}
