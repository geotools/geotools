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

import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public class BRepNode2D extends Point2D.Double {
	
	/**
	 * @param x
	 * @param y
	 */
	public BRepNode2D(double x, double y) {
		super(x,y);
		edges = null;
		value = null;
	}

	protected LinkedList edges;
	
	public Object value;

	protected void insertEdge(BRepEdge2D edge) {
		if (edges==null) {
			edges = new LinkedList();
		}
		edges.add(edge);
	}
}
