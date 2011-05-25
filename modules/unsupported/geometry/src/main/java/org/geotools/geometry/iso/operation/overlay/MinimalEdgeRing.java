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
package org.geotools.geometry.iso.operation.overlay;

import org.geotools.geometry.iso.topograph2D.DirectedEdge;
import org.geotools.geometry.iso.topograph2D.Edge;
import org.geotools.geometry.iso.topograph2D.EdgeRing;
import org.geotools.geometry.iso.util.algorithm2D.CGAlgorithms;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * A ring of {@link Edge}s with the property that no node has degree greater
 * than 2. These are the form of rings required to represent polygons under the
 * OGC SFS spatial data model.
 * 
 *
 *
 * @source $URL$
 */
public class MinimalEdgeRing extends EdgeRing {

	public MinimalEdgeRing(DirectedEdge start,
			CoordinateReferenceSystem crs, CGAlgorithms cga) {
		super(start, crs, cga);
	}

	public DirectedEdge getNext(DirectedEdge de) {
		return de.getNextMin();
	}

	public void setEdgeRing(DirectedEdge de, EdgeRing er) {
		de.setMinEdgeRing(er);
	}

}
