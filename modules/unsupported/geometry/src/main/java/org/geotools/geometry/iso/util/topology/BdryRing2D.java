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

import java.util.LinkedList;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class BdryRing2D {

	// LinkedList<Edge2D>
	protected LinkedList edges;

	// orientation of the first edge in order be a clockwise oriented ring
	protected boolean orientation;

	/**
	 * @return Returns the boundaryEdges ArrayList<Edge2D>
	 */
	public LinkedList getEdges() {
		return edges;
	}

	/**
	 * @param maxLength
	 */
	public void split(double maxLength) { 
		if (maxLength<=0.0) return;
		LinkedList newEdges = new LinkedList();
		for (int i = 0; i<edges.size();++i) {
			BdryEdge2D edge = (BdryEdge2D)edges.get(i); 
			newEdges.addAll(edge.split(maxLength));
		}
		this.edges = newEdges;
	}


}
