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

/**
 * @author Jackson Roehrig & Sanjay Jena
 * 
 * Circle2D A circle, representated by a X- and Y- Coordinate and a radius r. This
 * class provides a constructor building a unique circle from 3 points.
 * 
 * This class was translated from CPP into Java The original source code can be
 * found at http://astronomy.swin.edu.au/~pbourke/geometry/circlefrom3/
 *
 * @source $URL$
 */
public class Circle2D {
    
    private double x = 0;
    private double y = 0;
    private double radius = 0;
    
    public Circle2D(double centerX, double centerY, double radius) {
    	this.x = centerX;
        this.y = centerY;
        this.radius = radius;
    }
    
    /**
     * Constructs a circle from three points
     * @param First node
     * @param Second node
     * @param Thirs node
     */
    public Circle2D(Point2D pt1, Point2D pt2, Point2D pt3) {
        
        this.radius=-1;		// error checking 

    	    	
    	if (!this.isPerpendicular(pt1, pt2, pt3) )
    	    this.CalcCircle(pt1, pt2, pt3);	
    	else if (!this.isPerpendicular(pt1, pt3, pt2) )
    	    this.CalcCircle(pt1, pt3, pt2);	
    	else if (!this.isPerpendicular(pt2, pt1, pt3) )
    	    this.CalcCircle(pt2, pt1, pt3);	
    	else if (!this.isPerpendicular(pt2, pt3, pt1) )
    	    this.CalcCircle(pt2, pt3, pt1);	
    	else if (!this.isPerpendicular(pt3, pt2, pt1) )
    	    this.CalcCircle(pt3, pt2, pt1);	
    	else if (!this.isPerpendicular(pt3, pt1, pt2) )
    	    this.CalcCircle(pt3, pt1, pt2);	
    	else { 
    	    throw new IllegalArgumentException("The three pts are perpendicular to axis\n");
    	}
        
    }
    
    //  Check the given node are perpendicular to x or y axis 
    private boolean isPerpendicular(Point2D pt1, Point2D pt2, Point2D pt3)  {
	 	double yDelta_a= pt2.getY() - pt1.getY();
	 	double xDelta_a= pt2.getX() - pt1.getX();
	 	double yDelta_b= pt3.getY() - pt2.getY();
	 	double xDelta_b= pt3.getX() - pt2.getX();
	
	 	// checking whether the line of the two pts are vertical
	 	if (Math.abs(xDelta_a) <= 0.000000001 && Math.abs(yDelta_b) <= 0.000000001){
	 		return false;
	 	}
	
	 	return ((Math.abs(yDelta_a) <= 0.0000001) || 
	 			(Math.abs(yDelta_b) <= 0.0000001) ||
	 			(Math.abs(xDelta_a) <= 0.000000001) ||
	 			(Math.abs(xDelta_b) <= 0.000000001));
	 }
    
    
    private double CalcCircle(Point2D pt1, Point2D pt2, Point2D pt3)
    {
    	double yDelta_a= pt2.getY() - pt1.getY();
    	double xDelta_a= pt2.getX() - pt1.getX();
    	double yDelta_b= pt3.getY() - pt2.getY();
    	double xDelta_b= pt3.getX() - pt2.getX();
    	
    	if (Math.abs(xDelta_a) <= 0.000000001 && Math.abs(yDelta_b) <= 0.000000001){
    		this.x= 0.5*(pt2.getX() + pt3.getX());
    		this.y= 0.5*(pt1.getY() + pt2.getY());
    		this.radius= pt1.distance(this.getCenter());
    		return this.radius;
    	}
    	
    	// IsPerpendicular() assure that xDelta(s) are not zero
    	double aSlope=yDelta_a/xDelta_a;
    	double bSlope=yDelta_b/xDelta_b;
    	
    	if (Math.abs(aSlope-bSlope) <=  0.000000001) {
    	    // checking whether the given points are colinear. 	
    	    throw new IllegalArgumentException("The three pts are colinear\n");
    	}

    	// calc center
    	this.x= (aSlope*bSlope*(pt1.getY() - pt3.getY()) + bSlope*(pt1.getX() + pt2.getX())
    		- aSlope*(pt2.getX() + pt3.getX()) )/(2* (bSlope-aSlope) );
    	this.y = -1*(this.x - (pt1.getX() + pt2.getX())/2)/aSlope +  (pt1.getY() + pt2.getY())/2;

    	this.radius= pt1.distance(this.getCenter());
    	return this.radius;
    }
    
    
    /**
     * 
     * @return
     */
    public double getX() {
        return this.x;
    }

    /**
     * 
     * @return
     */
    public double getY() {
        return this.y;
    }

    /**
     * 
     * @return
     */
    public double getRadius() {
        return this.radius;
    }
    
    
    /**
     * 
     * @return
     */
    public Point2D getCenter() {
        return new Point2D.Double(this.x, this.y);
    }
    
    
	/**
	 * @param radius The radius to set.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	public void setValues(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
    /**
     * Verifies weather the circle contains a node or not.
     * Point laying at the border of the circle are not contained by the circle.
     * @param dp
     * @return
     */
    public boolean contains(Point2D dp) {
    	double dx = this.x - dp.getX();
    	double dy = this.y - dp.getY();
        return ((dx*dx) + (dy*dy)) < (this.radius*this.radius);
    }
    
    public Rectangle2D getRectangle() {    	
    	return new Rectangle2D.Double(x-radius,y-radius,2*radius,2*radius);
    }
    
    
    public String toString() {
        return "Circle2D: X:"+this.x+" Y:"+this.y+" r:"+this.radius;
    }

}
