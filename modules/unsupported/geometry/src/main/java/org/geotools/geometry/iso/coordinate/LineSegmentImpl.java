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
package org.geotools.geometry.iso.coordinate;

import org.geotools.geometry.iso.util.DoubleOperation;
import org.geotools.geometry.iso.util.algorithmND.AlgoPointND;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * Thus its interpolation attribute shall be "linear". The default
 * GenericCurve::parameterization = c(s) is: (L : Distance) = endParam 
 * startParam c(s) =
 * ControlPoint[1]+((s-startParam)/L)*(ControlPoint[2]-ControlPoint[1]) Any
 * other point in the controlPoint array must fall on this line. The control
 * points of a LineSegment shall all lie on the straight line between its start
 * point and end point. Between these two points, other positions may be
 * interpolated linearly. NOTE The linear interpolation, given using a
 * constructive parameter t, 0 ? t ? 1.0, where c(o) = c.startPoint() and
 * c(1)=c.endPoint(), is: c(t) = c(0)(1-t) + c(1)t
 * 
 * @author Jackson Roehrig & Sanjay Jena
 * 
 *
 *
 * @source $URL$
 */
public class LineSegmentImpl extends LineStringImpl implements LineSegment {

	/**
	 * The constructor of a LineSegment takes two positions and creates the
	 * appropriate line segment joining them. Constructors are class scoped.
	 * 
	 * LineSegment::LineSegment(point[2] : Position) : LineSegment
	 * 
	 * @param pointArray
	 * @param startPar
	 * 
	 */
	public LineSegmentImpl(PointArrayImpl pointArray, double startPar) {
		super(pointArray, startPar);
		if (pointArray.length() != 2) {
			throw new IllegalArgumentException(
					"A line segement must have exact two positions"); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * A LineSegment consists of two distinct DirectPositions (the startPoint
	 * and endPoint) joined by a straight line. Thus its interpolation attribute
	 * is "linear". The default GenericCurve::parameterization = c(s) is:
	 * 
	 * (L : Distance) = endParam - startParam
	 * 
	 * c(s) = ControlPoint[1]+((s-startParam)L)(ControlPoint[2]-ControlPoint[1])
	 * 
	 * Any other point in the controlPoint array must fall on this line. The
	 * control points of a LineSegment lie all on the straight line between its
	 * start point and end point. Between these two points, other positions may
	 * be interpolated linearly.
	 * 
	 * NOTE The linear interpolation, given using a constructive parameter t, 0<=t<=1.0,
	 * where c(o) = c.startPoint() and c(1)=c. endPoint(), is: c(t) = c(0)(1-t) +
	 * c(1)t
	 * 
	 * The constructor of a LineSegment takes two positions and creates the
	 * appropriate line segment joining them. Constructors are class scoped.
	 * LineSegment::LineSegment(point[2] : Position) : LineSegment
	 * 
	 * @param other
	 */
	public LineSegmentImpl(LineSegmentImpl other) {
		super(other);
	}


    /**
     * This functionality taken from CoordinateFactory where 
     * I am not quite clear as to the purpose (Jody).
     * 
     * @param crs
     * @param to
     * @param from
     * @param startParam
     */
	public LineSegmentImpl( CoordinateReferenceSystem crs, double[] from, double[] to, double startParam ) {
        this( new PointArrayImpl( new DirectPositionImpl( crs, from ), new DirectPositionImpl( crs, to )), startParam );        
    }

    public LineSegmentImpl(DirectPosition one, DirectPosition two, double startParam) {
    	this( new PointArrayImpl( one, two), startParam );    			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.coordinate.CurveSegmentImpl#isSimple()
	 * @version Implementation OK
	 */
	public boolean isSimple() {
		// a lineSegment is always simple since it is a straight line between two points
		return true;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#forParam(double)
	 * @version Shall this method override the LineString method? correctness
	 *          not verified.
	 */
	public DirectPosition forParamInSegment(double dist) {
		// Test ok - n dimensional valid
		// If the distance parameter is not within the parameter interval, return NULL
		if (dist < this.startParam || dist > this.endParam)
			return null;
		
		double par = DoubleOperation.div(DoubleOperation.subtract(dist, this.startParam), this.length());
		return LineSegmentImpl.linearInterpolate(this.getStartPoint(), this.getEndPoint(), par);
	}
	


	
	/**
	 * Executes a linear Interpolation between two positions and returns the
	 * DirectPosition at the distance 'par' on this straight line
	 * 
	 * This method is NON-ROBUST, e.g. it might suffer by errors caused by the Floating-point arithmetic
	 * 
	 * @param p0 -
	 *            start position of linear interpolation
	 * @param p1 -
	 *            end position of linear interpolation
	 * @param par -
	 *            distance on the straight line, for which to search
	 *            DirectPosition (from p0 to p1)
	 * @return Position on the straight line at parameter 'par'
	 */
	public static DirectPositionImpl linearInterpolate(DirectPosition p0,
			DirectPosition p1, double par) {
		// Test ok

		// 0.0 <= factor <= 1.0
		// par = 0 => result = dp0
		// par = 1 => result = dp1
		double[] coord = AlgoPointND.evaluate(p0.getCoordinates(), p1.getCoordinates(), par);
        return new DirectPositionImpl( p0.getCoordinateReferenceSystem(), coord );
	}
	
	/**
	 * The operation "tangent" shall return the tangent vector along this
	 * GenericCurve at the passed parameter value. This vector approximates the
	 * derivative of the parameterization of the curve. The tangent shall be a
	 * unit vector (have length 1.0), which is consistent with the
	 * parameterization by arc length.
	 * 
	 * This function will build the tangent vector at the position defined by
	 * the distance. The vector will have his origin at the specified position,
	 * and his vectorPosition in direction of the curve If the position, which
	 * is defined by the distance, lays on a control point, this control point
	 * will be the originPosition of the Vector
	 * 
	 * @param distance
	 *            a <code>double</code> value
	 * @return a <code>Vector</code> value
	 */
	public double[] getTangentInSegment(double distance) {
		// Works for n dimensional coordinates
		
		// Calculate the normative vector from origin
		double[] startpoint = this.getStartPoint().getCoordinates();
		double[] endpoint = this.getEndPoint().getCoordinates();
		double[] newEndPoint = AlgoPointND.subtract(startpoint, endpoint);
		newEndPoint = AlgoPointND.normalize(newEndPoint);

		// TODO this part depends from forParam() - implement this method!
		// Add the coordinate at distance to the normative vector
		double[] posAtDistance = this.forParamInSegment(distance).getCoordinates();
		newEndPoint = AlgoPointND.add(newEndPoint, posAtDistance);
		return newEndPoint;
	}
	
	
	// public double[] getTangent(double distance) {
	// Vector2D vec = null;
	// /* If distance-param lays on endPoint.. */
	// if (this.endParam == distance) {
	// /* Create vector between startPoint and endpoint of segment */
	// vec = new Vector2D(this.startPoint(), this.getEndPoint());
	// /* .. double the length of the vector.. */
	// vec = vec.scale(2.0);
	// /* .. and create a new vector which has the endPoint of the segment as
	// his startPosition,
	// * and the endPoint of the double-scaled vecor as his endPosition */
	// vec = new Vector2D(this.getEndPoint(), vec.getVectorPos());
	// /* If distance-param lays between controlPoints (or at startPoint of
	// LineString) */
	// } else {
	// /* Create vector between distance-Position and endpoint of segment */
	// vec = new Vector2D(this.param(distance), this.getEndPoint());
	// }
	//		
	// /* Return normalized vector */
	// vec = vec.getNormal();
	// return vec;
	// }

	// /**
	// * @param ls
	// * @return boolean
	// */
	// public boolean isParallel(LineSegmentImpl ls){
	// return
	// LineSegmentImpl.isParallel((DirectPositionImpl)this.getStartPoint(),
	// (DirectPositionImpl)this.getEndPoint(),
	// (DirectPositionImpl)ls.getStartPoint(),
	// (DirectPositionImpl)ls.getEndPoint());
	// }

	
	// /**
	// * @param p0
	// * @param p1
	// * @param q0
	// * @param q1
	// * @return boolean
	// */
	// public static boolean isParallel(DirectPositionImpl p0,
	// DirectPositionImpl p1, DirectPositionImpl q0, DirectPositionImpl q1) {
	// return
	// Math.abs(((Double)DirectPositionImpl.cross(p1.subtract(p0),q1.subtract(q0))))
	// <= DirectPositionImpl.EPSILON;
	// }

	// // This method returns:
	// // 1) four parameters if the segments are overlaped, the first two
	// parameters
	// // corresponding to the intersection points on this segment and the last
	// // two on the other segment. If both first parameters are Double.NaN,
	// // this segment contains the other segment. If both last parameters are
	// // Double.NaN, this segment is within the other one.
	// // 2) two parameters if the interior or the boundary of this segment
	// intersects
	// // the interior or the boundary of the other segment. If the boundaries
	// // intersect, the parameters are rounded to exactly startParam or
	// endParam
	// public static double[] constrIntersection(DirectPositionImpl p0,
	// DirectPositionImpl p1, DirectPositionImpl q0, DirectPositionImpl q1){
	// // the range of the returned values is from 0.0 to 1.0
	// // Overlaping:
	// // (p0,p1) *------------->* *------------->* *------->* *------------->*
	// *------------->*
	// // (q0,q1) *------------->* *------>* *------------->* *------------->*
	// *------------->*
	// // result: (0.0,1.0,0.0,1.0) (0.2,0.8,NaN,NaN) (NaN,NaN,0.2,0.8)
	// (0.2,NaN,NaN,0.8) (NaN,0,8,0.2,NaN)
	// //
	// // (p0,p1) *----->* *------->* *----->* *------->*
	// // (q0,q1) *------>* *----->* *------>* *----->*
	// // result: (1.0,NaN,NaN,0.0) (NaN,0.0,1.0,NaN) (1.0,NaN,NaN,0.0)
	// (NaN,0.0,1.0,NaN)
	// // Seg 0: \ /
	// // \/ result: (0.5,0.5)
	// // /\
	// // Seg1 / \
	// //
	// // |p1
	// // | q0_________q1 result: (0.5,NaN)
	// // |
	// // |p0
	// //
	// // q0 _________q1
	// // |p1
	// // | result: (NaN,0.5)
	// // |
	// // |p0
	// //
	// double eps = DirectPositionImpl.EPSILON;
	// int i;
	// if ( LineSegmentImpl.isParallel(p0, p1, q0, q1) ) {//the segments are
	// parallel
	// double[] result = new double[4];
	// result[0] = Double.NaN;
	// result[1] = Double.NaN;
	// result[2] = Double.NaN;
	// result[3] = Double.NaN;
	// // return nothing if they are not on the same line or outside the line
	// result[0] = LineSegmentImpl.constrParamForPoint(p0, p1, q0);
	// result[1] = LineSegmentImpl.constrParamForPoint(p0, p1, q1);
	// result[2] = LineSegmentImpl.constrParamForPoint(q0, q1, p0);
	// result[3] = LineSegmentImpl.constrParamForPoint(q0, q1, p1);
	// for ( i = 0 ; i <= 3; ++i) {
	// if ( result[i] < 0.0 || result[i] > 1.0 ) result[i] = Double.NaN;
	// }
	// return result;
	// } else {
	// //Dim result[1] As double
	// //result = intersectionConstrParam(p0, p1, q0, q1)
	// // return nothing if the the four points are on the same line (det <=
	// eps)
	// double[] result = new double[2];
	// result[0] = Double.NaN;
	// result[1] = Double.NaN;
	// DirectPositionImpl ep = p1.subtract(p0);
	// DirectPositionImpl eq = q1.subtract(q0);
	// DirectPositionImpl pq = q0.subtract(p0);
	// double det = (Double)DirectPositionImpl.cross(ep,eq); // not 0, because
	// LineSegment.isParallel() = false
	// double rp = (Double)DirectPositionImpl.cross(pq,eq) / det;
	// double rq = (Double)DirectPositionImpl.cross(pq,ep) / det;
	// if ( Math.abs(rp) < eps ) rp = 0.0;
	// if ( Math.abs(rp - 1.0) < eps ) rp = 1.0;
	// if ( Math.abs(rq) < eps ) rq = 0.0;
	// if ( Math.abs(rq - 1.0) < eps ) rq = 1.0;
	// if ( rp < 0.0 || rp > 1.0 ) rp = Double.NaN;
	// if ( rq < 0.0 || rq > 1.0 ) rq = Double.NaN;
	// result[0] = rp;
	// result[1] = rq;
	// return result;
	// }
	// }


	// public static double constrParamForPoint(DirectPositionImpl p0 ,
	// DirectPositionImpl p1 , DirectPositionImpl dp) {
	// // return the construction parametric coordinate (0.0 <= result <= 1.0)
	// of
	// // dp on the line (p0,p1)
	// // if p0.equals(p1) then: if dp.equals(p0) return 0.0, else Double.NaN
	// // if dp is close to p0 then result = 0.0
	// // if dp is close to p1 then result = 1.0
	// // if the line (p0,p1) does not contain dp then return Double.NaN
	// double eps = DirectPositionImpl.EPSILON;
	// double result = Double.NaN;
	// // return nothing if dp is not on the same line as (p0,p1): colinear
	// if (LineSegmentImpl.isParallel(p0, p1, p0, dp)) return Double.NaN;
	// if (p0.equals(p1)) {
	// if (p0.equals(dp))
	// return 0.0;
	// else
	// return Double.NaN;
	// }
	// // choose the most apropriated axis
	// double coord[];
	// coord = p0.getCoordinates();
	// double p0x = coord[0];
	// double p0y = coord[1];
	// coord = p1.getCoordinates();
	// double p1x = coord[0];
	// double p1y = coord[1];
	// coord = dp.getCoordinates();
	// double dpx = coord[0];
	// double dpy = coord[1];
	// if (Math.abs(p0x - p1x) > Math.abs(p0y - p1y))
	// result = (dpx - p0x) / (p1x - p0x);
	// else
	// result = (dpy - p0y) / (p1y - p0y);
	// if (Math.abs(result) <= eps) result = 0.0;
	// if (Math.abs(1.0 - result) <= eps) result = 1.0;
	// if (result < 0.0 || result > 1.0) return Double.NaN;
	// return result;
	// }



	// public int intersectionToRight(DirectPositionImpl dp) {
	// // returns 1 if a horizontal line including dp intersects this
	// // LineSegment
	// double x = dp.getX();
	// double y = dp.getY();
	// DirectPositionImpl dp0 = this.getStartPoint();
	// DirectPositionImpl dp1 = this.getEndPoint();
	//
	// double x0 = Math.min(dp0.getX(), dp1.getX());
	// double x1 = Math.max(dp0.getX(), dp1.getX());
	// double y0 = Math.min(dp0.getY(), dp1.getY());
	// double y1 = Math.max(dp0.getY(), dp1.getY());
	//
	// if (y0 == y1)
	// return 0;
	// if ((y < y0) || (y >= y1))
	// return 0;
	// if ((x0 == x1) && (x < x0))
	// return 0;
	// if (x0 != x1) {
	// double xa = x1;
	// double ya = y1;
	// double xb = x0;
	// double yb = y0;
	// if (((x1 > x0) && (y1 > y0)) || ((x1 < x0) && (y1 > y0))) {
	// xa = x0;
	// ya = y0;
	// xb = x1;
	// yb = y1;
	// }
	// if (((y - ya) * (xb - xa)) > ((x - xa) * (yb - ya)))
	// return 0;
	// }
	// return 1;
	// }

	// /**
	// * Checks weather the LineSegment intersects with another LineSegment
	// *
	// * @param other
	// * LineSegment
	// * @return TRUE, if both the LineSegments have points in common
	// (Intersection);
	// * FALSE, if they are dijoint
	// */
	// public boolean intersects(LineSegmentImpl seg) {
	// return this.intersects(seg.getStartPoint(), seg.getEndPoint());
	// }
	//
	// public boolean intersects(DirectPositionImpl q0, DirectPositionImpl q1) {
	// // double[] intsc = LineSegment.constrIntersection(
	// // (DirectPosition) this.getStartPoint(),
	// // (DirectPosition) this.getEndPoint(),
	// // (DirectPosition) q0, (DirectPosition) q1);
	// // if (intsc.length == 2) {
	// // return !Double.isNaN(intsc[0]) && !Double.isNaN(intsc[1]);
	// // } else if (intsc.length == 4) {
	// // return !Double.isNaN(intsc[0]) || !Double.isNaN(intsc[1])
	// // || !Double.isNaN(intsc[2]) || !Double.isNaN(intsc[3]);
	// // } else {
	// // return !Double.isNaN(intsc[0]) && !Double.isNaN(intsc[1]);
	// // }
	// /* Create Line2D.Double objects from LineSegments */
	// Line2D l1 = new Line2D.Double(this.getStartPoint().getX(),
	// this.getStartPoint().getY(), this.getEndPoint().getX(),
	// this.getEndPoint().getY());
	// Line2D l2 = new Line2D.Double(q0.getX(), q0.getY(), q1.getX(),
	// q1.getY());
	// return l1.intersectsLine(l2);
	// }
	//
	// public boolean rightSide(DirectPositionImpl dp){
	// double eps;
	// DirectPositionImpl p0 =
	// (DirectPositionImpl)dp.subtract(this.getStartPoint());
	// DirectPositionImpl p1 =
	// (DirectPositionImpl)this.getEndPoint().subtract(this.getStartPoint());
	// return ((Double)DirectPositionImpl.cross(p0,p1)) >
	// DirectPositionImpl.EPSILON;
	// }
	//    
	// public double[] getCoordinates() {
	// double[] c0 = this.getStartPoint().getCoordinates();
	// double[] c1 = this.getEndPoint().getCoordinates();
	// return new double[] {c0[0],c0[1],c1[0],c1[1]};
	// }



	
}
