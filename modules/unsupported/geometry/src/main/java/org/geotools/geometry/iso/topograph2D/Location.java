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
 * Constants representing the location of a point relative to a geometry. They
 * can also be thought of as the row or column index of a DE-9IM matrix. For a
 * description of the DE-9IM, see the <A
 * HREF="http://www.opengis.org/techno/specs.htm">OpenGIS Simple Features
 * Specification for SQL</A> .
 * 
 *
 * @source $URL$
 */
public class Location {
	/**
	 * DE-9IM row index of the interior of the first geometry and column index
	 * of the interior of the second geometry. Location value for the interior
	 * of a geometry.
	 */
	public final static int INTERIOR = 0;

	/**
	 * DE-9IM row index of the boundary of the first geometry and column index
	 * of the boundary of the second geometry. Location value for the boundary
	 * of a geometry.
	 */
	public final static int BOUNDARY = 1;

	/**
	 * DE-9IM row index of the exterior of the first geometry and column index
	 * of the exterior of the second geometry. Location value for the exterior
	 * of a geometry.
	 */
	public final static int EXTERIOR = 2;

	/**
	 * Used for uninitialized location values.
	 */
	public final static int NONE = -1;

	/**
	 * Converts the location value to a location symbol, for example,
	 * <code>EXTERIOR => 'e'</code> .
	 * 
	 * @param locationValue
	 *            either EXTERIOR, BOUNDARY, INTERIOR or NONE
	 * @return either 'e', 'b', 'i' or '-'
	 */
	public static char toLocationSymbol(int locationValue) {
		switch (locationValue) {
		case EXTERIOR:
			return 'e';
		case BOUNDARY:
			return 'b';
		case INTERIOR:
			return 'i';
		case NONE:
			return '-';
		}
		throw new IllegalArgumentException("Unknown location value: "
				+ locationValue);
	}
}
