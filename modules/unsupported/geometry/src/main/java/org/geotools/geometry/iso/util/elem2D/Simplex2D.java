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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.geotools.geometry.iso.util.algorithm2D.AlgoPoint2D;
import org.geotools.geometry.iso.util.algorithm2D.AlgoRectangle2D;


/**
 * @author roehrig
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public abstract class Simplex2D {
	
	protected Node2D[] point;
	
	protected Object[] neighbour;
	
	public int id = -1; 
	
	public Object object = null;
	
	protected Simplex2D(Node2D[] p) {
		this.initialize();
		//assert p.length >= n();
		for (int i = 0, n=n(); i<n; ++i) {
			point[i] = p[i];
		}
	}
	protected Simplex2D(Node2D[] p, int n) {
		this.initialize(n);
		//assert p.length >= n;
		for (int i = 0; i< n; ++i) {
			point[i] = new Node2D(p[i]);
		}
	}
	// ABSTRACT
	protected abstract int n();
	
	// ABSTRACT
	public abstract int sideBits(int s);
	
	private void initialize() {
		point = new Node2D[n()];
		neighbour = new Object[n()];
	}
	private void initialize(int n) {
		point = new Node2D[n];
		neighbour = new Object[n()];
	}
	/**
	 * @return Returns the object.
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * @param object The object to set.
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
	public Node2D[] getPoints() {
		return this.point;
	}
	
		
	public Node2D getPoint(int n) {
		// assert(n<=n());
		return this.point[n];
	}
	
	public Node2D[] getPointFromSide(int side) {
		// assert(n<=n());
		return new Node2D[] {this.point[side], this.point[(side+1)%n()]};
	}
	
	public Object[] getNeighbours() {
		return this.neighbour;
	}
	
	public Rectangle2D getRectangle() {
		return AlgoRectangle2D.createRectangle(point);
	}
	
	public void setRectangle(Rectangle2D r) {
		AlgoRectangle2D.setValues(r,point);
	}
	
	public double getSizeSq() {
		double sizeSq = Double.MIN_VALUE;
		for (int i = 0, n = n(); i < n; ++i) {
			double distSq = this.point[i].distanceSq(this.point[(i+1)%n]);
			if ( distSq > sizeSq ) sizeSq = distSq;
		}
		return sizeSq;
	}

	public double getSize() {
		return Math.sqrt(getSizeSq());
	}

	/** A simplex has at least three points */
	boolean hasNeighbour(int side) {
		return (this.neighbour[side] != null);
	}
	
	boolean hasEdge(Edge2D e) {
		for (int i = 0, n = n(); i < n; ++i) {
			if (this.neighbour[i]==e) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPoint(Node2D p) {
		for (int i = 0, n=n(); i < n; ++i) {
			if (p == point[i]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasEqualPoint(Point2D p){
		for(int i = 0, n = n(); i < n; ++i){
			if (p.equals(this.point[i])) return true;
		}
		return false;
	}
	
	public int getSide(Point2D p0, Point2D p1) {
		int flag = 0;
		int n = n();
		for (int i = 0; i < n; ++i) {
			if (p0.equals(point[i])) {
				flag |= (1 << i);
				break;
			}
		}
		for (int i = 0; i < n; ++i) {
			if (p1.equals(point[i])) {
				flag |= (1 << i);
				break;
			}
		}
		for (int i = 0; i < n; ++i) {
			if (flag == sideBits(i)) {
				return i;
			}
		}
		return -1;
	}
	
	public int[] getSides(Node2D node) {
		int n = n();
		
		if (node == this.point[0]) {
			return new int[] {n-1,0};
		} else {
			for (int i = 1; i < n; ++i) {
				if (node == this.point[i]) {
					return new int[] {(i-1)%n,i};
				}
			}			
		}
		return null;
	}
	
	public int getSide(Edge2D e) {
		return getSide(e.getP1(), e.getP2());
	}
	
	public int getSide(Simplex2D f) {
		if (f != this) {
			for (int i = 0, n = n(); i < n; ++i) {
				if (this.neighbour[i]==f) return i;
				if (this.neighbour[i] instanceof Edge2D) {
					Edge2D e = (Edge2D)this.neighbour[i];
					if (e.hasSimplex(f)) return i;
				}
			}
		}
		throw new IllegalArgumentException("getSide(Simplex2D f)");
	}
	
	public Simplex2D getNeighbourSimplex(int side) {
		if (this.neighbour[side] instanceof Simplex2D) {
			return (Simplex2D)this.neighbour[side];
		} else {
			Edge2D e = (Edge2D)this.neighbour[side];
			return (e==null) ? null : e.getNeighborSimplex(this);
		}
	}
	
	public Edge2D getNeighbourEdge(int side) {
		return (this.neighbour[side] instanceof Edge2D) ? (Edge2D)this.neighbour[side] : null;
	}
	
	public Node2D getNextPoint(Point2D p0, Point2D p1) {
		// returns the next vertex after v0 and v1
		for (int i = 0, n = n(); i < n; ++i) {
			if (point[i] == p0) {
				if (point[(i + 1) % n] == p1)
					return point[(i + 2) % n];
				else if (point[(i + n - 1) % n] == p1)
					return point[(i + n - 2) % n];
				else
					return null;
			}
		}
		return null;
	}
	
	protected void linkSimplex(int side, Simplex2D s) {
		this.neighbour[side] = s;
	}
	
	protected boolean linkSimplex(Simplex2D other) {
		for (int thisSide = 0, thisN = this.n(); thisSide < thisN; ++thisSide) {
			for (int otherSide = 0, otherN = other.n(); otherSide < otherN; ++otherSide) {
				if ( (this.point[thisSide]==other.point[otherSide] && 
					  this.point[(thisSide+1)%thisN]==other.point[(otherSide+1)%otherN]) ||
					 (this.point[thisSide]==other.point[(otherSide+1)%otherN] && 
					  this.point[(thisSide+1)%thisN]==other.point[otherSide]) ) {
					this.neighbour[thisSide] = other;
					other.neighbour[otherSide] = this;
					return true;
				}
			}
		}
		return false;
	}

	protected void linkEdge(int side, Edge2D e) {
		this.neighbour[side] = e;
	}
	
	protected void linkEdge(Edge2D e) {
		linkEdge(getSide(e.getP1(), e.getP2()), e);
	}
	
	protected void unlinkEdge(int side, Edge2D e) {
		if (this.neighbour[side] != e) {
			throw new IllegalArgumentException("error on unlink_edge");
		}
		this.neighbour[side] = null;
	}
	
	protected void unlinkEdge(Edge2D e) {
		for (int i = 0, n = n(); i < n; ++i)
			if (this.neighbour[i] == e) {
				this.neighbour[i] = null;
				return;
			}
	}
	
	protected void swapSide(Edge2D e, Simplex2D f) {
		for (int i = 0; i < n(); ++i) {
			if (this.neighbour[i] == e) {
				this.neighbour[i] = f;
			}
		}
	}
	
	public ArrayList<Simplex2D> getAllSimpliciesOnSimplex() {
		int n = n();
		ArrayList<Simplex2D> result = new ArrayList<Simplex2D>(n);
		for (int i = 0; i < n; ++i) {
			Simplex2D s = this.getNeighbourSimplex(i);
			if (s != null)	result.add(s);
		}
		return result;
	}
	
	public ArrayList<Edge2D> getAllEdgesOnFace() {
		int n = n();
		ArrayList<Edge2D> result = new ArrayList<Edge2D>(n);
		for (int i = 0; i < n; ++i) {
			Edge2D e = this.getNeighbourEdge(i);
			if (e != null) result.add(e);
		}
		return result;
	}
	
	public ArrayList<Node2D> getAllPointsOnFace() {
		int n = n();
		ArrayList<Node2D> result = new ArrayList<Node2D>(n);
		for (int i = 0; i < n; ++i) {
			result.add(point[i]);
		}
		return result;
	}
	
	public int getOrientation(Node2D n0, Node2D n1) {
		for (int i=0, n=n(); i<n; ++i) {
			if (point[i]==n0 && point[(i+1)%n]==n1) {
				return 1;
			} else if (point[i]==n1 && point[(i+1)%n]==n0) {
				return -1;
			}
		}
		return 0;
	}
	
	public int getOrientation(int s0, int s1) {
		return (s0 < s1 || (s0 == n() && s1 == 0)) ? 1 : -1;
	}
	
	public int getOrientation(Simplex2D f0, Simplex2D f1) {
		// get the orientation of the neighbors f0 and f1 w.r.t the
		// orientation of this Simplex2D. 1 for same, -1 for opposite and 0 for error
		int s0 = getSide(f0);
		int s1 = getSide(f1);
		if ((s0 == -1) || (s1 == -1)) {
			throw new IllegalArgumentException("error on get_side(GenVertex *v0, GenVertex *v1)");
		}
		return getOrientation(s0, s1);
	}
	
	public int getOrientation(Simplex2D f) {
		// return 1 for same, -1 for opposite orientation and 0 for error
		int s0 = this.getSide(f);
		int s1 = f.getSide(this);
		if (s0 == -1 || s1 == -1)
			return 0;
		if (this.point[s0] == f.point[s1]) {
			return -1; // opposite orientation
		} else {
			return 1; // same orientation
		}
	}
	
	public Point2D getCentroid() {
		Point2D result = new Point2D.Double(0.0,0.0);
		int n = this.n();
		for (int i = 0; i<n; ++i) {
			AlgoPoint2D.add(result,point[i]);
		}
		return AlgoPoint2D.scale(result,1./n);		
	}
	/**
	 * @return
	 */
	public boolean hasID() {
		return id!=-1;
	}
	
}
