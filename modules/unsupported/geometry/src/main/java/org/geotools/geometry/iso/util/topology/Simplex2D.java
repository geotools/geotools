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
import java.util.ArrayList;

import org.geotools.geometry.iso.util.algorithm2D.AlgoPoint2D;


/**
 * @author roehrig
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public abstract class Simplex2D extends BRepFace2D {
	
	protected BRepNode2D[] node;
	
	protected Object[] side;
	
	public abstract int n();
	
	public abstract byte side(int s);
	
	protected Simplex2D(BRepNode2D[] p) {
		int n = n();
		node = new BRepNode2D[n];
		side = new Object[n];
		for (int i = 0; i<n; ++i) {
			node[i] = p[i];
		}
	}

	public BRepNode2D[] getNodes() {
		return node;
	}
		
	public BRepNode2D getNode(int s) {
		return this.node[s];
	}
	
	public Object[] getSides() {
		return this.side;
	}
	
	public Object getSide(int s) {
		return this.side[s];
	}
	
	public BRepEdge2D getEdge(int s) {
		return (isEdgeSide(s)) ? (BRepEdge2D)side[s] : null;
	}
	 
	public Simplex2D getSimplex(int s) {
		return (isSimplexSide(s)) ? (Simplex2D)side[s] : null;
	}

	public boolean isEdgeSide(int i) {
		return side[i] instanceof BRepEdge2D;
	}

	public boolean isSimplexSide(int i) {
		return side[i] instanceof BRepFace2D;
	}
	 
	public boolean hasNode(BRepNode2D v0) {
		for (int i = 0; i < n(); ++i) {
			if (v0 == node[i]) return true;
		}
		return false;
	}
	
	public boolean hasEdge(BRepEdge2D e) {
		for (int i = 0; i < n(); ++i) {
			if (side[i]==e)	return true;
		}
		return false;
	}
	
	public boolean hasFace(BRepFace2D f) {
		for (int i = 0, n=n(); i < n; ++i) {
			if (this.isEdgeSide(i)) {
				if (this.getNeighborFace((BRepEdge2D)side[i])==f) return true;				
			} else {
				if (side[i]==f) return true;
			}
		}
		return false;
	}
	
	public int getSide(Point2D p0, Point2D p1) {
		byte flag = 0;
		for (int i = 0; i < n(); ++i) {
			if (p0.equals(node[i])) {
				flag |= (1 << i);
				break;
			}
		}
		for (int i = 0; i < n(); ++i) {
			if (p1.equals(node[i])) {
				flag |= (1 << i);
				break;
			}
		}
		for (int i = 0; i < n(); ++i)
			if (flag == side(i))
				return i;
		throw new IllegalArgumentException("error on get_side(GenVertex *v0, GenVertex *v1)");
	}
	
	public int getSide(BRepEdge2D e) {
		return getSide(e.getP1(), e.getP2());
	}
	
	public int getSide(BRepFace2D f) {
		if ((f==this) || (f==null)) {
			throw new IllegalArgumentException("GenSimplex2D f");
		}
		for (int i = 0; i < n(); ++i) {
			if (this.isSimplexSide(i)) {
				if (side[i]==f)	return i;
			} else {
				BRepEdge2D e = (BRepEdge2D)side[i];
				if ((e != null) && getNeighborFace(e) == f)
					return i;
			}
		}
		return -1;
	}

	public BRepFace2D getNeighborFace(BRepEdge2D e) {
		return (e.getSurfaceLeft()==this) ? e.getSurfaceRight() : e.getSurfaceLeft();
	}

	public BRepNode2D getNextPoint(BRepNode2D v0, BRepNode2D v1) {
		// returns the next vertex after v0 and v1
		for (int i = 0; i < n(); ++i) {
			if (node[i] == v0) {
				if (node[(i + 1) % n()] == v1)
					return node[(i + 2) % n()];
				else if (node[(i + n() - 1) % n()] == v1)
					return node[(i + n() - 2) % n()];
				else
					return null;
			}
		}
		return null;
	}
	
	public ArrayList getAllSimpliciesOnSimplex() {
		ArrayList result = new ArrayList();
		for (int i = 0; i < n(); ++i) {
			Simplex2D simplex = getSimplex(i);
			if (simplex!=null) result.add(simplex);
		}
		return result;
	}
	
	public ArrayList getAllEdgesOnFace() {
		ArrayList result = new ArrayList();
		for (int i = 0; i < n(); ++i) {
			BRepEdge2D e = getEdge(i);
			if (e!=null) result.add(e);
		}
		return result;
	}
	
	public ArrayList getAllPointsOnFace() {
		ArrayList result = new ArrayList();
		for (int i = 0; i < n(); ++i)
			result.add(node[i]);
		return result;
	}
	
	public int getOrientation(int s0, int s1) {
		return (s0 < s1 || (s0 == n() && s1 == 0)) ? 1 : -1;
	}
	
	public int getOrientation(Simplex2D f0, Simplex2D f1) {
		// get the orientation of the neighbors f0 and f1 w.r.t the
		// orientation
		// of this Simplex2D. 1 for same, -1 for opposite and 0 for error
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
		if (this.node[s0] == f.node[s1]) {
			return -1; // opposite orientation
		} else {
			return 1; // same orientation
		}
	}
	
	public Point2D getCentroid() {
		Point2D result = new Point2D.Double(this.node[0].getX(),this.node[0].getY());
		int n = this.n();
		for (int i = 1; i<n; ++i) {
			AlgoPoint2D.createAdd(result,node[i]);
		}
		return AlgoPoint2D.scale(result,1./n);		
	}
	
	protected void linkSimplex(int s, Simplex2D f) {
		side[s]=f;
	}
	
	protected void linkEdge(int s, BRepEdge2D e) {
		side[s]=e;
	}
	
	protected void linkEdge(BRepEdge2D e) {
		Point2D p0 = e.getP1();
		Point2D p1 = e.getP2();
		linkEdge(getSide(p0, p1), e);
	}
	
	protected void unlinkEdge(int s, BRepEdge2D e) {
		if (getEdge(s) != e) {
			throw new IllegalArgumentException("error on unlink_edge");
		}
		resetSide(s);
	}
	
	protected void unlinkEdge(BRepEdge2D e) {
		for (int i = 0; i < n(); ++i)
			if (side[i]==e) {
				resetSide(i);
			}
	}
	
	protected void swapSide(BRepEdge2D e, Simplex2D f) {
		for (int i = 0; i < n(); ++i) {
			if (side[i]==e) {
				side[i]=f;
				return;
			}
		}
	}

	private void resetSide(int s) {
		this.side[s] = null;
	}

}
