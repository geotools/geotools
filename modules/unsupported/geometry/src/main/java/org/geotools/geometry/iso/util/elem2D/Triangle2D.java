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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.geotools.geometry.iso.util.algorithm2D.AlgoLine2D;
import org.geotools.geometry.iso.util.algorithm2D.AlgoPoint2D;
import org.geotools.geometry.iso.util.algorithm2D.AlgoRectangle2D;
import org.geotools.geometry.iso.util.algorithm2D.LineLineIntersection2D;


/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class Triangle2D extends Simplex2D {

	private static int SIDE[] = {
		  (1<<0) | (1<<1),		// v[0] && v[1]
		  (1<<1) | (1<<2),		// v[1] && v[2]
		  (1<<2) | (1<<0)		// v[2] && v[0]
		};

	/**
	 * @param startPoint
	 * @param p2
	 * @param endPoint
	 */
	public Triangle2D (Node2D p0, Node2D p1, Node2D p2) {
		super(new Node2D[] {p0,p1,p2});
	}

	/* (non-Javadoc)
	 * @see de.fhkoeln.tt.core.geo.GeoSimplex2D#n()
	 */
	public int n() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see de.fhkoeln.tt.core.geo.GeoSimplex2D#side(int)
	 */
	public int sideBits(int s) {
		return SIDE[s];
	}

	public double getArea(){
		Point2D p[] = this.getPoints();
		return Math.abs((0.5 * 
					(((p[0].getX() - p[1].getX()) * (p[0].getY() + p[1].getY())) + 
					 ((p[1].getX() - p[2].getX()) * (p[1].getY() + p[2].getY())) + 
					 ((p[2].getX() - p[0].getX()) * (p[2].getY() + p[0].getY())))));
	}
	
	// fill side with value
	public void setSide(int s, Edge2D e){
		this.linkEdge(s,e);
	}
	
	public void setSide(int s, Triangle2D f){
		this.linkSimplex(s,f);
	}
		
	public int getSideNr(Point2D p0, Point2D p1){
		int n = 0;
		n = this.getSide(p0,p1);
		return n;
		}

	public double getRadiusU(){
		Point2D pts[] = new Point2D[n()];
		pts = this.getPoints();
		Point2D p0 = new Point2D.Double();
		Point2D p1 = new Point2D.Double();
		Point2D p2 = new Point2D.Double();
		p0 = pts[0];
		p1 = pts[1];
		p2 = pts[2];
		Point2D pm = new Point2D.Double(((0.5 * p1.getX())+(0.5 * p0.getX())),((0.5 * p1.getY())+(0.5* p0.getY())));
		double c = Math.sqrt((pm.getX() * pm.getX())- (2 * (pm.getX() * p2.getX())) + (p2.getX() * p2.getX())+ (pm.getY() * pm.getY()) - (2 * (pm.getY() * p2.getY()))+ (p2.getY() * p2.getY()));
		
		double l = 2 * Math.sqrt((pm.getX() * pm.getX())
				- (2 * (pm.getX() * p0.getX())) + (p0.getX() * p0.getX())
				+ (pm.getY() * pm.getY()) - (2 * (pm.getY() * p0.getY()))
				+ (p0.getY() * p0.getY()));

		Point2D p0_1 = AlgoPoint2D.subtract(p0,pm);
		Point2D p1_1 = AlgoPoint2D.subtract(p1,pm);
		Point2D p2_1 = AlgoPoint2D.subtract(p2,pm);
		Point2D p0_2 = new Point2D.Double((-0.5 * l), 0);
        Point2D p1_2 = new Point2D.Double((0.5 * l), 0);
		double angle = Math.acos(((p0_1.getX()  * p0_2.getX()) / (Math.sqrt(Math.pow(p0_1.getX(), 2)+ Math.pow(p0_1.getY() , 2)) * Math.sqrt((p0_2.getX() * p0_2.getX())))));
		if (p1_1.getY() >0) {
			angle = ((2 * Math.PI) - angle);
		}
		Point2D p2_2 = new Point2D.Double((Math.cos(angle) * (p2.getX() - pm.getX()))
				- (Math.sin(angle) * (p2.getY() - pm.getY())),
				(Math.sin(angle) * (p2.getX() - pm.getX()))
						+ (Math.cos(angle) * (p2.getY() - pm.getY())));
		
		double b = (1 / (2 * p2_2.getY())) * ((c * c) - ( (l * l)/4));
		return  ((Math.sqrt((4*(b*b))+ (l*l)))/2);
	}

	public double getRadiusI(){
		Point2D p[] = this.getPoints();
		double p0x = p[0].getX();
		double p0y = p[0].getY();
		double p1x = p[1].getX();
		double p1y = p[1].getY();
		double p2x = p[2].getX();
		double p2y = p[2].getY();
		double umfang = Math.sqrt(((p0x * p0x)-(2*p0x*p1x)+(p1x * p1x)+(p0y * p0y) - (2*p0y *p1y)+ (p1y*p1y))) +
		 				Math.sqrt(((p1x * p1x)-(2*p1x*p2x)+(p2x * p2x)+(p1y * p1y) - (2*p1y *p2y)+ (p2y*p2y))) + 
		 				Math.sqrt(((p2x * p2x)-(2*p2x*p0x)+(p0x * p0x)+(p2y * p2y) - (2*p2y *p0y)+ (p0y*p0y)));
		return this.getArea() / (umfang * 0.5);
	}
	
	public double getQuality(){
		return this.getRadiusI()/this.getRadiusU();
	}
	
	public boolean containsPoints(Point2D[] p) {
		for (int i = 0, n = p.length; i<n; ++i) {
			if (containsPoint(p[i])) return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean containsPoint(Point2D p) {
		for (int i=0; i<3; ++i) {
			if (p.equals(point[i])) return false;
			if (AlgoLine2D.rightSide(this.point[i],this.point[(i+1)%3],p)) return false;
		}
		return true;
	}

	private boolean validPatch(Line2D lineA[], Line2D lineB[]) {
		int nA = lineA.length;
		int nB = lineB.length;
		final LineLineIntersection2D intsc = new LineLineIntersection2D();
		for (int iA = 0; iA<nA; ++iA) {
			for (int iB = 0; iB<nB; ++iB) {
				intsc.setValues(lineA[iA],lineB[iB]);
				if (!intsc.isCoincident() && (intsc.isBI() || intsc.isIB())) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * return true if the interiors of both simplices intersect
	 * valid only for counterclockwise simplices 
	 */
	public boolean intersects(Triangle2D other) {
		if (!AlgoRectangle2D.intersects(this.getRectangle(),other.getRectangle())) return false;
		int nA = 3;
		int nB = 3;
		Line2D lineA[] = new Line2D[nA];
		Line2D lineB[] = new Line2D[nB];
		for (int iA = 0; iA<nA; ++iA) {
			lineA[iA] = new Line2D.Double(this.point[iA],this.point[(iA+1)%nA]);
		}
		for (int iB = 0; iB<nB; ++iB) {
			lineB[iB] = new Line2D.Double(other.point[iB],other.point[(iB+1)%nB]);
		}
		if (!validPatch(lineA, lineB) || !validPatch(lineB, lineA)) return true;
		if (this.containsPoints(other.point)) return true;
		if (other.containsPoints(this.point)) return true;
		return false;
	}


	
}
