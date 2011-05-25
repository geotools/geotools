/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util.elem2D;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class Node2D extends Point2D.Double {

	public int id;
	
	public Object link;
	
	public Object object;

	Node2D() {
		super();
		this.id = -1;
		this.link = null;
		this.object = null;
	}

	/**
	 * @param point2D
	 */
	public Node2D(Point2D point2D) {
		super(point2D.getX(),point2D.getY());
		this.id = -1;
		this.link = null;
		this.object = null;		
	}

	/**
	 * @param d
	 * @param e
	 */
	public Node2D(double x, double y) {
		super(x,y);
		this.id = -1;
		this.link = null;
		this.object = null;		
	}
	
	/**
	 * @return Returns the object.
	 */
	public Object getObject() {
		return object;
	}
	public boolean hasID() {
		return this.id != -1;
	}
	
	public boolean hasEdge() {
		return (link instanceof Edge2D) ? link != null : false;
	}
	
	public boolean hasSimplex() {
		return (link instanceof Simplex2D) ? link != null : false;
	}
	
	public Edge2D getEdge(Node2D n) {
		if ((link instanceof Edge2D[]) && (link != null)) {
			Edge2D[] e = (Edge2D[])link;
			for (int i=0; i<e.length; ++i) {
				if ((e[i].getNode1()==n) || (e[i].getNode2()==n)) return e[i];
			}
		}
		return null;
	}
	
	public Edge2D[] getEdges() {
		return (link instanceof Edge2D[]) ? (Edge2D[])link : null;
	}
	
	/**
	 * @param tri
	 * @param node2D
	 * @return
	 */
	public Simplex2D[] getSimplices(Node2D node2D) {
		HashSet<Simplex2D> hs = getSimplices();
		Simplex2D s0 = null;
		Simplex2D s1 = null;
		for (Iterator<Simplex2D> it = hs.iterator(); it.hasNext(); ) {
			Simplex2D s = it.next();
			if ( s.hasPoint(node2D) ) {
				if (s0==null) {
					s0 = s;
				}
				else if (s1==null) {
					s1 = s;
					return new Simplex2D[] {s0,s1};
				}
			}
		}
		return (s0==null) ? null : new Simplex2D[] {s0};
	}

	public Simplex2D getSimplex() {
		return (link instanceof Simplex2D) ? (Simplex2D)link : null;
	}
	
	public HashSet<Simplex2D> getSimplices() {
		
		HashSet<Simplex2D> simplices = new HashSet<Simplex2D>();
		
		if (link instanceof Simplex2D) {
			if (link!=null) getSimplices((Simplex2D)link, simplices);
		} else {
			Edge2D edges[]= (Edge2D[])link;
			for (int i=0; i<edges.length; ++i) {
				Edge2D edge = edges[i];
				Simplex2D simplex = edge.getRightSimplex();
				if (simplex!=null) getSimplices(simplex, simplices);
				simplex = edge.getLeftSimplex();
				if (simplex!=null) getSimplices(simplex, simplices);
				
			}
		}
		
		return simplices;
	}

	private void  getSimplices(Simplex2D simplex, HashSet<Simplex2D> simplices) {
		
		if (simplex!=null) simplices.add(simplex);

		int side[] = simplex.getSides(this);
		
		Simplex2D s0 = simplex.getNeighbourSimplex(side[0]);
		if ( ( s0 != null ) && !simplices.contains(s0)) {
			getSimplices(s0, simplices);
		}

		s0 = simplex.getNeighbourSimplex(side[1]);
		if ( ( s0 != null ) && !simplices.contains(s0)) {
			getSimplices(s0, simplices);
		}
	}

	public void linkSimplex(Simplex2D s) {
		link = s;
	}

	public void linkEdge(Edge2D e) {
		if ( link instanceof Edge2D[] ) {
			int length = ((Edge2D[])link).length;
			Edge2D newLink[] = new Edge2D[length+1];
			System.arraycopy(link, 0, newLink, 0, length);
			newLink[length] = e;
			link = newLink;
		} else {
			link = new Edge2D[1];
			((Edge2D[])link)[0] = e;
		}
	}
	
	public void unlinkEdge(Edge2D e) {
		if ( !(link instanceof Simplex2D) && (link!=null) ) {
			Edge2D[] edges = (Edge2D[])link;
			Vector<Edge2D> vec = new Vector<Edge2D>(edges.length);
			for (int i=0; i<edges.length;++i) {
				if (edges[i]!=e) vec.add(edges[i]);
			}
			edges = new Edge2D[vec.size()];
			link = vec.toArray(edges);
		}
	}
	
	/**
	 * @param edges
	 */
	public void appendEdges(Edge2D[] edges) {
		if ( link instanceof Simplex2D ) {
			link = edges;
		} else {
			Vector<Edge2D> vec = new Vector<Edge2D>(20);
			Edge2D[] ee = this.getEdges();
			for (int i=0; i<ee.length; ++i) {
				Edge2D e =  ee[i]; 
				if (!vec.contains(e)) vec.add(e);
			}
			for (int i=0; i<edges.length;++i) {
				Edge2D e =  edges[i]; 
				if (!vec.contains(e)) vec.add(e);
			}
			this.link = vec.toArray(new Edge2D[vec.size()]);
		}
	}

	/**
	 * @param edge2D
	 * @return
	 */
	public boolean hasEdge(Edge2D edge) {
		Edge2D edges[] = this.getEdges();
		if (edges==null) return false;
		for (int i=0; i<edges.length; ++i ) {
			if ( edges[i] == edge ) return true;
		}
		return false;
	}

}
