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

import org.geotools.geometry.iso.util.Assert;
import org.geotools.geometry.iso.util.algorithm2D.CGAlgorithms;

/**
 * Models the end of an edge incident on a node. EdgeEnds have a direction
 * determined by the direction of the ray from the initial point to the next
 * point. EdgeEnds are comparable under the ordering "a has a greater angle with
 * the x-axis than b". This ordering is used to sort EdgeEnds around a node.
 *
 * @source $URL$
 */
public class EdgeEnd implements Comparable {
	protected Edge edge; // the parent edge of this edge end

	protected Label label;

	// the node this edge end originates at
	private Node node;

	// points of initial line segment
	private Coordinate p0, p1;

	// the direction vector for this edge from its starting point
	private double dx, dy;
	
	private int quadrant;

	protected EdgeEnd(Edge edge) {
		this.edge = edge;
	}

	public EdgeEnd(Edge edge, Coordinate p0, Coordinate p1) {
		this(edge, p0, p1, null);
	}

	public EdgeEnd(Edge edge, Coordinate p0, Coordinate p1, Label label) {
		this(edge);
		init(p0, p1);
		this.label = label;
	}

	protected void init(Coordinate p0, Coordinate p1) {
		this.p0 = p0;
		this.p1 = p1;
		dx = p1.x - p0.x;
		dy = p1.y - p0.y;
		quadrant = Quadrant.quadrant(dx, dy);
		Assert.isTrue(!(dx == 0 && dy == 0),
				"EdgeEnd with identical endpoints found");
	}

	public Edge getEdge() {
		return edge;
	}

	public Label getLabel() {
		return label;
	}

	public Coordinate getCoordinate() {
		return p0;
	}

	public Coordinate getDirectedCoordinate() {
		return p1;
	}

	public int getQuadrant() {
		return quadrant;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return node;
	}

	public int compareTo(Object obj) {
		EdgeEnd e = (EdgeEnd) obj;
		return compareDirection(e);
	}

	/**
	 * Implements the total order relation:
	 * <p>
	 * a has a greater angle with the positive x-axis than b
	 * <p>
	 * Using the obvious algorithm of simply computing the angle is not robust,
	 * since the angle calculation is obviously susceptible to roundoff. A
	 * robust algorithm is: - first compare the quadrant. If the quadrants are
	 * different, it it trivial to determine which vector is "greater". - if the
	 * vectors lie in the same quadrant, the computeOrientation function can be
	 * used to decide the relative orientation of the vectors.
	 */
	public int compareDirection(EdgeEnd e) {
		if (dx == e.dx && dy == e.dy)
			return 0;
		// if the rays are in different quadrants, determining the ordering is
		// trivial
		if (quadrant > e.quadrant)
			return 1;
		if (quadrant < e.quadrant)
			return -1;
		// vectors are in the same quadrant - check relative orientation of
		// direction vectors
		// this is > e if it is CCW of e
		return CGAlgorithms.computeOrientation(e.p0, e.p1, p1);
	}

	public void computeLabel() {
		// subclasses should override this if they are using labels
	}

	public void print(PrintStream out) {
		double angle = Math.atan2(dy, dx);
		String className = getClass().getName();
		int lastDotPos = className.lastIndexOf('.');
		String name = className.substring(lastDotPos + 1);
		out.print("  " + name + ": " + p0 + " - " + p1 + " " + quadrant + ":"
				+ angle + "   " + label);
	}

	public String toString() {
		return "DE(" + this.p0 + ", " + this.p1 + ")";
	}

}
