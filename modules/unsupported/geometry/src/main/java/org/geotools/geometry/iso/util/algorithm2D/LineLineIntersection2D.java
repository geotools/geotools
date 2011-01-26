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
package org.geotools.geometry.iso.util.algorithm2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public class LineLineIntersection2D {
	// the range of the returned values is from 0.0 to 1.0
	// Overlaping:
	// (p0,p1) *------------->*   *------------->*        *------->*        
	// (q0,q1) *------------->*      *------>*         *------------->*     
	// result: (0.0,1.0,0.0,1.0)  (0.2,0.8,NaN,NaN)   (NaN,NaN,0.2,0.8)   
	//
	// (p0,p1)    *------------->*          *------------->*
	// (q0,q1)       *------------->*    *------------->*
	// result:  (0.2,NaN,NaN,0.8)       (NaN,0,8,0.2,NaN)
	//
	// (p0,p1) *--------->*       *------------->*         *------->*    *------------->*
	// (q0,q1) *------------->*   *--------->*       *------------->*         *-------->*
	// result: (0.0,NaN,0.0,0.8)  (0.0,0.8,0.0,NaN)  (NaN,1.0,0.4,1.0)   (0.3,1.0,Nan,1.0)   
	//
	// (p0,p1) *----->*                     *------->*    *----->*                     *------->*  
	// (q0,q1)        *------>*      *----->*                    *------>*      *----->*            
	// result: (1.0,NaN,NaN,0.0)     (NaN,0.0,1.0,NaN)    (1.0,NaN,NaN,0.0)     (NaN,0.0,1.0,NaN)  
	//
	// Seg 0:  \  /
	//          \/  result: (0.5,0.5)
	//          /\
	// Seg1    /  \
	// 
	//   |p1   
	//   |    q0_________q1  result: (0.5,NaN)
	//   | 
	//   |p0   
	//
	//  q0 _________q1
	//
	//         |p1   
	//         |    result: (NaN,0.5)
	//         | 
	//         |p0   
	//
	/**
	 * this class interprets the intersection method
	 */
	private double itsc[];
	
	Line2D line0;
	
	Line2D line1;
	
	public LineLineIntersection2D() {
		itsc = null;
		line0=null;
		line1=null;
	}

	public LineLineIntersection2D(Line2D line0, Line2D line1) {
		setValues(line0,line1);
	}

	public void setValues(Line2D line0, Line2D line1) {
		Point2D p0 = line0.getP1();
		Point2D p1 = line0.getP2();
		Point2D q0 = line1.getP1(); 
		Point2D q1 = line1.getP2();
		this.line0 = line0;
		this.line1 = line1;
		this.itsc = intersection(p0,p1,q0,q1);
		if (itsc == null)
			return;
		int n = itsc.length;
		if (n == 4) {
			/**
			 * the segments are colinear and their interiors may overlap. The
			 * segments are equal if the four itscs are real numbers, and
			 * in this case the four itscs must be 0.0 or 1.0. The second
			 * possibility are two real numbers and two Double.NaN. In this
			 * case, the interiors of the segments intersect
			 */
			int count = 0;
			if (isNaN(itsc[0]))
				count++;
			if (isNaN(itsc[1]))
				count++;
			if (isNaN(itsc[2]))
				count++;
			if (isNaN(itsc[3]))
				count++;
			if (count==3)
				throw new IllegalArgumentException(
				"Error on LineLineIntersection: wrong overlaping itscs. Count="+count);
			if (count == 0) {
				/** all real numbers, hence they coincide and must be 0.0 or 1.0 */
				if ((itsc[0] != 0.0 && itsc[0] != 1.0)
						|| (itsc[1] != 0.0 && itsc[1] != 1.0)
						|| (itsc[2] != 0.0 && itsc[2] != 1.0)
						|| (itsc[3] != 0.0 && itsc[3] != 1.0)) {
					throw new IllegalArgumentException(
					"Error on LineLineIntersection: wrong coincident itscs");
				}
			} else if (count == 2) {
			}
		} else if (n == 2) {
			
		}
	}
	
	private boolean isNaN(double d) {
		return java.lang.Double.isNaN(d);
	}
	
	public boolean isCoincident() {
		/**
		 * the segments are coincident if they overlap and the intersection
		 * itscs are 0.0 or 1.0. This implies no NaN in the itscs
		 */
		return (itsc != null) && (itsc.length == 4) 
				&& !isNaN(itsc[0]) && !isNaN(itsc[1]) 
				&& !isNaN(itsc[2]) && !isNaN(itsc[3]);
	}
	
	public boolean overlapsInterior() {
		/**
		 * the interiors of the segments overlap if at least one of the four
		 * itsc is less then 1.0 and greater then 0.0
		 */
		return ((itsc != null) && (itsc.length == 4) 
				&& (    (!isNaN(itsc[0]) && itsc[0] > 0.0 && itsc[0] < 1.0) || 
						(!isNaN(itsc[1]) && itsc[1] > 0.0 && itsc[1] < 1.0) ||
						(!isNaN(itsc[1]) && itsc[2] > 0.0 && itsc[2] < 1.0) ||
						(!isNaN(itsc[1]) && itsc[3] > 0.0 && itsc[3] < 1.0) ));
	}
	
	public boolean intersectInteriorInteriorPoint() {
		/**
		 * the interiors of the segments intersect at a node if both
		 * itscs are real number not equal 0.0 and not equal 1.0
		 */
		if ((itsc == null) || (itsc.length != 2))
			return false;
		return ((!isNaN(itsc[0]) && itsc[0] != 0.0 && itsc[0] != 1.0) && 
				(!isNaN(itsc[1]) && itsc[1] != 0.0 && itsc[1] != 1.0));
	}
	
	public boolean isII() {
		/**
		 * the interiors of the segments intersect if they intersect at one node or
		 * if their interior overlap number and two itscs are Double.NaN
		 */
		return intersectInteriorInteriorPoint() || overlapsInterior();
	}
	
	public boolean isIB() {
		/**
		 * the interior of the line0 intersects the boundary of line1 if they
		 * have two itscs and the first itsc is a real number between 0.0 and
		 * 1.0 and the second itsc is a real number equal 0.0 or 1.0
		 * 
		 * (itsc[0]>0.0 and itsc[0]<1.0) and (itsc[1]==0.0 or itsc[1]==1.0)
		 * 
		 */
		return ((itsc != null) && (itsc.length == 2) && !isNaN(itsc[0]) && !isNaN(itsc[1])
				&& (itsc[0] > 0.0 && itsc[0] < 1.0)
				&& (itsc[1] == 0.0 || itsc[1] == 1.0));
	}
	
	public boolean isBI() {
		/**
		 * the boundary of the line0 intersects the interior of line1 if they
		 * have two itscs and the first itsc is a real number between equal 0.0 or
		 * 1.0 and the second itsc is a real number between 0.0 and 1.0
		 * 
		 * (itsc[0]==0.0 and itsc[0]==1.0) and (itsc[1]>0.0 or itsc[1]<1.0)
		 * 
		 */
		return ((itsc != null) && (itsc.length == 2) && !isNaN(itsc[0]) && !isNaN(itsc[1])
				&& (itsc[0] == 0.0 || itsc[0] == 1.0)
				&& (itsc[1] > 0.0 && itsc[1] < 1.0));
	}
	
	public boolean isBB() {
		/**
		 * the boundary of both lines intersect if they
		 * have two or four itscs and the first itsc is a real number 
		 * equal 0.0 or 1.0 and the second itsc is a real itsc not equal
		 * 0.0 or 1.0 
		 * 
		 * itsc[0]==0.0 or itsc[0]==1.0 
		 * itsc[1]!=0.0 or itsc[1]!=1.0
		 * 
		 */
		if (itsc == null)
			return false;
		if (itsc.length == 2) {
			return ((itsc[0] == 0.0 || itsc[0] == 1.0) && 
					(itsc[1] == 0.0 || itsc[1] == 1.0));
		} else if (itsc.length == 4) {
			return isCoincident();
		} else {
			throw new IllegalArgumentException(
			"Error on LineLineIntersection::IsBB()");
		}
	}
	
    public  Point2D[] intersectionPoint() { 
    	/**
		 * returns one intersection node for a 0-d intersection or
		 * two intersection points for a 1-d intersection (overlaping)
		 */
    	Point2D[] result = null;
    	if (itsc==null) return null;
    	else if ( itsc.length == 2 ) {
            if ( !isNaN(itsc[0]) ) {
            	result = new Point2D[1];
            	result[0] = AlgoLine2D.evaluate(line0,itsc[0]); 
            } else if ( !isNaN(itsc[1]) ) {
            	result = new Point2D[1];
            	result[0] = AlgoLine2D.evaluate(line0,itsc[1]); 
            } else {
            	result = null;
            }
        } else if ( itsc.length == 4 ) {
			int count = 0;
			int index[] = new int[4];
			if (!isNaN(itsc[0])) index[count++] = 0;
			if (!isNaN(itsc[1])) index[count++] = 1;
			if (!isNaN(itsc[2])) index[count++] = 2;
			if (!isNaN(itsc[3])) index[count++] = 3;
        	Line2D l0 = (index[0]<2) ? line0 : line1;
        	Line2D l1 = (index[1]<2) ? line0 : line1;
        	result = new Point2D[2];
			if (count==0) {
				result = null;
			} else if (count==2) {
				if ((itsc[index[0]]==0.0 || itsc[index[0]]==1.0) && 
					(itsc[index[1]]==0.0 || itsc[index[1]]==1.0)) {
					/** colinear touching the boundaries */
		        	result = new Point2D[1];
					result[0] = AlgoLine2D.evaluate(l0,itsc[index[0]]); 
				} else {
					/** colinear overlaping interiors */
		        	result = new Point2D[2];
					result[0] = AlgoLine2D.evaluate(l0,itsc[index[0]]); 
					result[1] = AlgoLine2D.evaluate(l1,itsc[index[1]]); 
				}
			} else if (count==3) {
				/** colinear overlaping and intersecting the boundary at one node */
	        	// (p0,p1) *--------->*       *------------->*         *------->*    *------------->*
	        	// (q0,q1) *------------->*   *--------->*       *------------->*         *-------->*
	        	// result: (0.0,NaN,0.0,0.8)  (0.0,0.8,0.0,NaN)  (NaN,1.0,0.4,1.0)   (0.3,1.0,Nan,1.0)   
	        	if (index[0]<2 && index[1]<2) {
					result[0] = AlgoLine2D.evaluate(l0,itsc[index[0]]); 
					result[1] = AlgoLine2D.evaluate(l0,itsc[index[1]]); 
	        	} else {
					result[0] = AlgoLine2D.evaluate(l1,itsc[index[0]]); 
					result[1] = AlgoLine2D.evaluate(l1,itsc[index[1]]); 	        		
	        	}

			} else if (count==4) {
				result[0] = AlgoLine2D.evaluate(l0,itsc[index[0]]); 
				result[1] = AlgoLine2D.evaluate(l0,itsc[index[1]]); 				
			}

        }
        return result;
    }

//    public  Point2D[] intersectionPoint1() { 
//        if ( itsc.length == 2 ) {
//            if ( !isNaN(itsc[0]) )  itsc[0] = itsc[0] * AlgoLine.length(line0); 
//            if ( !isNaN(itsc[1]) )  itsc[1] = itsc[1] * AlgoLine.length(line1); 
//        } else if ( itsc.length == 4 ) {
//            if ( !java.lang.Double.isNaN(itsc[0]) )  itsc[0] = itsc[0] * AlgoLine.length(line0); 
//            if ( !java.lang.Double.isNaN(itsc[1]) )  itsc[1] = itsc[1] * AlgoLine.length(line0); 
//            if ( !java.lang.Double.isNaN(itsc[2]) )  itsc[2] = itsc[2] * AlgoLine.length(line1); 
//            if ( !java.lang.Double.isNaN(itsc[3]) )  itsc[3] = itsc[3] * AlgoLine.length(line1); 
//        }
//        return null;
//    }

    //  This method returns: 
    // 1) four parameters if the segments are overlaped, the first two parameters
    //  corresponding to the intersection points on this segment and the last
    //  two on the other segment. If both first parameters are Double.NaN,
    //  this segment contains the other segment. If both last parameters are
    //  Double.NaN, this segment is within the other one. 
    // 2) two parameters if the interior or the boundary of this segment intersects 
    //  the interior or the boundary of the other segment. If the boundaries 
    //  intersect, the parameters are rounded to exactly startParam or endParam
    public static double[] intersection(Point2D p0, Point2D p1, Point2D q0, Point2D q1){ 
        // the range of the returned values is from 0.0 to 1.0
		// Overlaping:
		// (p0,p1) *------------->*   *------------->*        *------->*        *------------->*          *------------->*
		// (q0,q1) *------------->*      *------>*         *------------->*        *------------->*    *------------->*
		//   result: (0.0,1.0,0.0,1.0)  (0.2,0.8,NaN,NaN)   (NaN,NaN,0.2,0.8)    (0.2,NaN,NaN,0.8)       (NaN,0,8,0.2,NaN)
		//
		// (p0,p1) *----->*                     *------->*    *----->*                     *------->*  
		// (q0,q1)        *------>*      *----->*                    *------>*      *----->*            
		// result: (1.0,NaN,NaN,0.0)     (NaN,0.0,1.0,NaN)    (1.0,NaN,NaN,0.0)     (NaN,0.0,1.0,NaN)
		// Seg 0: \ /
		// \/ result: (0.5,0.5)
		// /\
		// Seg1 / \
		// 
		// |p1
		// | q0_________q1 result: (0.5,NaN)
		// |
		// |p0
		//
		// q0 _________q1
		// |p1
		// | result: (NaN,0.5)
		// |
		// |p0
		//
    	
        if ( AlgoLine2D.isParallel(p0, p1, q0, q1) ) {// the segments are
														// parallel
            double[] result = new double[4]; 
            result[0] = java.lang.Double.NaN;
            result[1] = java.lang.Double.NaN;
            result[2] = java.lang.Double.NaN;
            result[3] = java.lang.Double.NaN;
            // return nothing if they are not on the same line or outside the line
            result[0] = AlgoLine2D.constrParamForPoint(p0, p1, q0);
            result[1] = AlgoLine2D.constrParamForPoint(p0, p1, q1);
            result[2] = AlgoLine2D.constrParamForPoint(q0, q1, p0);
            result[3] = AlgoLine2D.constrParamForPoint(q0, q1, p1);
            for ( int i = 0 ; i <= 3; ++i) {
                if ( result[i] < 0.0 || result[i] > 1.0 )  result[i] = java.lang.Double.NaN; 
            }
            return result;
        } else {
            //Dim result[1] As double
            //result = intersectionConstrParam(p0, p1, q0, q1)
            // return nothing if the the four points are on the same line (det <= eps)
            double[] result = new double[2]; 
            result[0] = java.lang.Double.NaN;
            result[1] = java.lang.Double.NaN;
            Point2D ep = AlgoPoint2D.subtract(p1,p0); 
            Point2D eq = AlgoPoint2D.subtract(q1,q0); 
            Point2D pq = AlgoPoint2D.subtract(q0,p0); 
            double det = AlgoPoint2D.cross(ep,eq); // not 0, because AlgoLine.isParallel() = false
            double rp = AlgoPoint2D.cross(pq,eq) / det; 
            double rq = AlgoPoint2D.cross(pq,ep) / det; 
    		double eps = AlgoPoint2D.EPSILONSQ;
            if ( Math.abs(rp) < eps )  rp = 0.0; 
            if ( Math.abs(rp - 1.0) < eps )  rp = 1.0; 
            if ( Math.abs(rq) < eps )  rq = 0.0; 
            if ( Math.abs(rq - 1.0) < eps )  rq = 1.0; 
            if ( rp < 0.0 || rp > 1.0 )  rp = java.lang.Double.NaN; 
            if ( rq < 0.0 || rq > 1.0 )  rq = java.lang.Double.NaN; 
            result[0] = rp;
            result[1] = rq;
            return result;
        }
    }
}
