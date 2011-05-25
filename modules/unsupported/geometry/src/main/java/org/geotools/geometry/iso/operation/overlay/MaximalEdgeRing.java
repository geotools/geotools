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

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.iso.topograph2D.DirectedEdge;
import org.geotools.geometry.iso.topograph2D.DirectedEdgeStar;
import org.geotools.geometry.iso.topograph2D.EdgeRing;
import org.geotools.geometry.iso.topograph2D.Node;
import org.geotools.geometry.iso.util.algorithm2D.CGAlgorithms;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * A ring of {@link org.geotools.geometry.iso.topograph2D.Edge}s which may contain nodes of degree > 2. A
 * MaximalEdgeRing may represent two different spatial entities:
 * <ul>
 * <li>a single polygon possibly containing inversions (if the ring is oriented
 * CW)
 * <li>a single hole possibly containing exversions (if the ring is oriented
 * CCW)
 * </ul>
 * If the MaximalEdgeRing represents a polygon, the interior of the polygon is
 * strongly connected.
 * <p>
 * These are the form of rings used to define polygons under some spatial data
 * models. However, under the OGC SFS model, {@link MinimalEdgeRing} are
 * required. A MaximalEdgeRing can be converted to a list of MinimalEdgeRings
 * using the {@link #buildMinimalRings() } method.
 * 
 *
 *
 * @source $URL$
 */
public class MaximalEdgeRing extends EdgeRing {

	public MaximalEdgeRing(DirectedEdge start,
			CoordinateReferenceSystem crs, CGAlgorithms cga) {
		super(start, crs, cga);
	}

	public DirectedEdge getNext(DirectedEdge de) {
		return de.getNext();
	}

	public void setEdgeRing(DirectedEdge de, EdgeRing er) {
		de.setEdgeRing(er);
	}

	/**
	 * For all nodes in this EdgeRing, link the DirectedEdges at the node to
	 * form minimalEdgeRings
	 */
	public void linkDirectedEdgesForMinimalEdgeRings() {
		DirectedEdge de = startDe;
		do {
			Node node = de.getNode();
			((DirectedEdgeStar) node.getEdges()).linkMinimalDirectedEdges(this);
			de = de.getNext();
		} while (de != startDe);
	}

	public List buildMinimalRings() {
		List minEdgeRings = new ArrayList();
		DirectedEdge de = startDe;
		do {
			if (de.getMinEdgeRing() == null) {
				EdgeRing minEr = new MinimalEdgeRing(de,
						super.crs, cga);
				minEdgeRings.add(minEr);
			}
			de = de.getNext();
		} while (de != startDe);
		return minEdgeRings;
	}

}
