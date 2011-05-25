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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.geometry.iso.index.SpatialIndex;
import org.geotools.geometry.iso.index.quadtree.Quadtree;

/**
 * A EdgeList is a list of Edges. It supports locating edges that are pointwise
 * equals to a target edge.
 *
 *
 * @source $URL$
 */
public class EdgeList {
	
	private List edges = new ArrayList();

	/**
	 * An index of the edges, for fast lookup.
	 * 
	 * a Quadtree is used, because this index needs to be dynamic (e.g. allow
	 * insertions after queries). An alternative would be to use an ordered set
	 * based on the values of the edge coordinates
	 * 
	 */
	private SpatialIndex index = new Quadtree();

	public EdgeList() {
	}

	/**
	 * Insert an edge unless it is already in the list
	 */
	public void add(Edge e) {
		edges.add(e);
		index.insert(e.getEnvelope(), e);
	}

	public void addAll(Collection edgeColl) {
		for (Iterator i = edgeColl.iterator(); i.hasNext();) {
			add((Edge) i.next());
		}
	}

	public List getEdges() {
		return edges;
	}

	// <FIX> fast lookup for edges
	/**
	 * If there is an edge equal to e already in the list, return it. Otherwise
	 * return null.
	 * 
	 * @return equal edge, if there is one already in the list null otherwise
	 */
	public Edge findEqualEdge(Edge e) {
		Collection testEdges = index.query(e.getEnvelope());

		for (Iterator i = testEdges.iterator(); i.hasNext();) {
			Edge testEdge = (Edge) i.next();
			if (testEdge.equals(e))
				return testEdge;
		}
		return null;
	}

	public Iterator iterator() {
		return edges.iterator();
	}

	public Edge get(int i) {
		return (Edge) edges.get(i);
	}

	/**
	 * If the edge e is already in the list, return its index.
	 * 
	 * @return index, if e is already in the list -1 otherwise
	 */
	public int findEdgeIndex(Edge e) {
		for (int i = 0; i < edges.size(); i++) {
			if (((Edge) edges.get(i)).equals(e))
				return i;
		}
		return -1;
	}

	public void print(PrintStream out) {
		out.print("MULTILINESTRING ( ");
		for (int j = 0; j < edges.size(); j++) {
			Edge e = (Edge) edges.get(j);
			if (j > 0)
				out.print(",");
			out.print("(");
			Coordinate[] pts = e.getCoordinates();
			for (int i = 0; i < pts.length; i++) {
				if (i > 0)
					out.print(",");
				out.print(pts[i].x + " " + pts[i].y);
			}
			out.println(")");
		}
		out.print(")  ");
	}

}
