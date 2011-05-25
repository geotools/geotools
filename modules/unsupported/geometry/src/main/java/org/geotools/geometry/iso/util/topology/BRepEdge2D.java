/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util.topology;

import java.awt.geom.Line2D;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public abstract class BRepEdge2D extends Line2D {

	protected BRepFace2D surfaceRight;
	
	protected BRepFace2D surfaceLeft;
	
	public Object value;

	protected BRepEdge2D(BRepFace2D surfaceRight, BRepFace2D surfaceLeft) {
		this.surfaceRight = surfaceRight;
		this.surfaceLeft = surfaceLeft;
		this.value = null;
	}
	/**
	 * @return Returns the surfaceRight.
	 */
	public BRepFace2D getSurfaceRight() {
		return surfaceRight;
	}
	/**
	 * @return Returns the surfaceLeft.
	 */
	public BRepFace2D getSurfaceLeft() {
		return surfaceLeft;
	}


}
