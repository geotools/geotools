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
 * Indicates an invalid or inconsistent topological situation encountered during
 * processing
 *
 * @source $URL$
 */
public class TopologyException extends RuntimeException {

	private static String msgWithCoord(String msg, Coordinate pt) {
		if (pt != null)
			return msg + " [ " + pt + " ]";
		return msg;
	}

	private Coordinate pt = null;

	public TopologyException(String msg) {
		super(msg);
	}

	public TopologyException(String msg, Coordinate pt) {
		super(msgWithCoord(msg, pt));
		this.pt = new Coordinate(pt);
	}

	public Coordinate getCoordinate() {
		return pt;
	}

}
