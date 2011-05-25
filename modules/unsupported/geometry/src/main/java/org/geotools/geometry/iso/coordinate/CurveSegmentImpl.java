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

import java.io.Serializable;

import org.geotools.geometry.iso.primitive.CurveBoundaryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.util.DoubleOperation;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveInterpolation;
import org.opengis.geometry.primitive.CurveSegment;

/**
 * 
 * CurveSegment defines a homogeneous segment of a Curve. Each CurveSegment
 * shall be in, at most, one Curve.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 * @source $URL$
 */
public abstract class CurveSegmentImpl implements CurveSegment, Serializable {

	/**
	 * 
	 * The startParam indicates the parameter for the startPoint
	 * 
	 * GenericCurve::startParam() : Distance
	 * 
	 * GenericCurve:
	 * 
	 * {parameterization(startParam()) = startPoint()};
	 * 
	 * {parameterization(endParam()) = endPoint()};
	 * 
	 * {length() = endParam() - startParam()}
	 * 
	 * The start and end parameter of a Curve are always 0 and the arc length of
	 * the curve respectively. For CurveSegments within a Curve, the start and
	 * end parameters of the CurveSegment are equal to those of the Curve where
	 * this segment begins and ends in the Segmentation association, so that the
	 * startParam of any segment (except the first) is equal to the endParam of
	 * the previous segment. If a GenericCurve is used for other purposes, there
	 * shall be a restriction that the two parameters must differ by the arc
	 * length of the GenericCurve.
	 */
	protected double startParam;

	/**
	 * The endParam indicates the parameter for the endPoint
	 * 
	 * GenericCurve::endParam() : Distance
	 * 
	 * GenericCurve:
	 * 
	 * {parameterization(startParam()) = startPoint()};
	 * 
	 * {parameterization(endParam()) = endPoint()};
	 * 
	 * {length() = endParam() - startParam()}
	 * 
	 * The start and end parameter of a Curve are always 0 and the arc length of
	 * the curve respectively. For CurveSegments within a Curve, the start and
	 * end parameters of the CurveSegment are equal to those of the Curve where
	 * this segment begins and ends in the Segmentation association, so that the
	 * startParam of any segment (except the first) is equal to the endParam of
	 * the previous segment. If a GenericCurve is used for other purposes, there
	 * shall be a restriction that the two parameters must differ by the arc
	 * length of the GenericCurve.
	 * 
	 */
	protected double endParam;

	/**
	 * The attribute "interpolation" specifies the curve interpolation mechanism
	 * used for this segment. This mechanism uses the control points and control
	 * parameters to determine the position of this CurveSegment.
	 * 
	 * CurveSegment::interpolation : CurveInterpolation = "linear"
	 * 
	 */
	protected CurveInterpolation interpolation = null;

	/**
	 * Segmentation association from specification of Curve The association
	 * "segmentation" lists the components (CurveSegments) of Curve, each of
	 * which defines the direct position of points along a portion of the curve.
	 * The order of the CurveSegments is the order in which they are used to
	 * trace the Curve.
	 * 
	 * Curve::segment [1..n] : Sequence <CurveSegment>
	 * 
	 * CurveSegment::curve [0,1] : Reference <Curve>
	 * 
	 * For a particular parameter interval, the Curve and CurveSegment agree.
	 * CurveSegment: {curve.startParam() <= self.startParam()};
	 * {curve.endParam() >= self.endParam()}; {self.startParam() <
	 * self.endParam()}; {s : Distance (startParam() <= s <= endParam()) implies
	 * (curve.parameterization(s) = self.parameterization(s))};
	 * 
	 * NOTE In this standard, curve segments do not appear except in the context
	 * of a curve, and therefore the cardinality of the curve role in this
	 * association could be 1 which would preclude the use of curve segments
	 * except in this manner. While this would not affect this Standard, leaving
	 * the cardinality as 0..1 allows other standards based on this one to use
	 * curve segments in a more open-ended manner.
	 */
	private CurveImpl curve;

	/**
	 * @param startPar
	 */
	public CurveSegmentImpl(double startPar) {
		this.curve = null;
		this.startParam = startPar;
		this.endParam = -1;
	}

	/**
	 * @param other
	 */
	public CurveSegmentImpl(CurveSegmentImpl other) {
		this.curve = other.curve;
		this.startParam = other.startParam;
		this.endParam = other.endParam;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.CurveSegment#getBoundary()
	 */
	public CurveBoundaryImpl getBoundary() {
		return new CurveBoundaryImpl(this.getCurve().getCoordinateReferenceSystem(), 
				new PointImpl(this.getStartPoint()), 
				new PointImpl(this.getEndPoint()) );
	}

	public CurveInterpolation getInterpolation() {
		return this.interpolation;
	}

	/**
	 * Sets the type of interpolation
	 * 
	 * @param interpolation
	 */
	protected void setInterpolation(CurveInterpolation interpolation) {
		this.interpolation = interpolation;
	}

	/**
	 * Sets the Curve the Curve Segment belongs to
	 * 
	 * @param curve
	 */
	public void setCurve(CurveImpl curve) {
		if (curve == null)
			throw new IllegalArgumentException("Curve not passed"); //$NON-NLS-1$
		this.curve = curve;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.primitive.CurveSegment#getCurve()
	 * @version Implementation OK
	 */
	public CurveImpl getCurve() {
		if (this.curve == null)
			throw new IllegalArgumentException("Parent Curve not set."); //$NON-NLS-1$
		return this.curve;
	}

	/**
	 * The operation "samplePoint" returns an ordered array of point values
	 * (PointArray) that lie on the CurveSegment. In most cases, these will be
	 * related to control points used in the construction of the segment.
	 * 
	 * CurveSegment::samplePoint() : PointArray
	 * 
	 * NOTE The controlPoints of a curve segment are use to control its shape,
	 * and are not always on the curve segment itself. For example in a spline
	 * curve, the curve segment is given as a weighted vector sum of the
	 * controlPoints. Each weight function will have a maximum within the
	 * constructive parameter interval, which will roughly correspond to the
	 * point on the curve where it passes closest that the corresponding
	 * controlPoint. These points, the values of the curve at the maxima of the
	 * weight functions, will be the sample points for the curve segment.
	 * 
	 * @return
	 */
	// public abstract PointArray samplePoint();
	/**
	 * The operation "boundary" on CurveSegment operates with the same semantics
	 * as that on Curve except that the end points of a CurveSegment are not
	 * necessarily existing Points and thus the boundary may contain transient
	 * Points.
	 * 
	 * CurveSegment::boundary() : CurveBoundary
	 * 
	 * NOTE The above CurveBoundary will almost always be two distinct
	 * positions, but, like Curves, CurveSegments can be cycles in themselves.
	 * The most likely scenario is that all of the points used will be
	 * transients (constructed to support the return value), except for the
	 * startPoint and endPoint of the aggregated Curve. These two positions, in
	 * the case where the Curve is involved in a Complex, will be represented as
	 * Points in the same Complex.
	 * 
	 * @return
	 */
	// public abstract CurveBoundary getBoundary();
	/**
	 * The reverse of a CurveSegment simply reverses the orientation of the
	 * parameterizations of the segment. CurveSegment::reverse() : CurveSegment
	 * 
	 * @return DirectPositionImpl
	 */
	// public abstract CurveSegment reverse();
	public abstract DirectPosition getStartPoint();

	public abstract DirectPosition getEndPoint();

	/**
	 * @return start position
	 */
	public abstract Position getStartPosition();

	/**
	 * @return end position
	 */
	public abstract Position getEndPosition();

	/**
	 * GenericCurve: {parameterization(startParam()) = startPoint()};
	 * {parameterization(endParam()) = endPoint()}; {length() = endParam() -
	 * startParam()} returns the length => {length() = this.endParam() -
	 * this.startParam()} Returns the complete length of the CurveSegment
	 * 
	 * @return double
	 * @version Implementation OK
	 */
	public double length() {
		return DoubleOperation.subtract(this.getEndParam(), this.getStartParam());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#getStartParam()
	 * @version Implementation OK
	 */
	public double getStartParam() {
		return this.startParam;
	}

	/**
	 * @param Value
	 */
	public void setStartParam(double Value) {
		this.startParam = Value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#getEndParam()
	 * @version Implementation OK
	 */
	public double getEndParam() {
		return this.endParam;
	}

	/**
	 * Set the parameter for the endPoint
	 * 
	 * @param Value
	 */
	public void setEndParam(double Value) {
		this.endParam = Value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#getStartConstructiveParam()
	 */
	public double getStartConstructiveParam() {
		// OK
		return DoubleOperation.div(this.getStartParam(), this.curve.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#getEndConstructiveParam() @
	 */
	public double getEndConstructiveParam() {
		// OK
		return DoubleOperation.div(this.getEndParam(), this.curve.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#length(org.opengis.geometry.coordinate.Position,
	 *      org.opengis.geometry.coordinate.Position)
	 * @version Not verified whether this methods works correctly and
	 *          appropriately
	 */
	public double length(Position point0, Position point1) {
		
		// this method seems to be broken currently because getParamForPoint
		// has problems
		throw new UnsupportedOperationException("not implemented yet.");
		
//		/* Default-Values */
//		if (point0 == null && point1 == null)
//			return this.length();
//		if (point0 == null)
//			point0 = new PositionImpl((DirectPosition) this.getStartPoint().clone());
//			//point0 = this.getCurve().getFeatGeometryFactory()
//				//.getGeometryFactoryImpl().createPosition(this.getStartPoint());
//		if (point1 == null)
//			point1 = new PositionImpl((DirectPosition) this.getEndPoint().clone());
//			//point1 = this.getCurve().getFeatGeometryFactory()
//					//.getGeometryFactoryImpl().createPosition(this.getEndPoint());
//		/* Get all Params for closest points to startposition point1 */
//		ParamForPoint obj0 = this.getParamForPoint(point0.getPosition());
//		/* Get all Params for closest points to endposition point2 */
//		ParamForPoint obj1 = this.getParamForPoint(point1.getPosition());
//
//		/*
//		 * Compare the distances between each found startParam and endParam and
//		 * choose the smallest one
//		 */
//		double minLength = Double.MAX_VALUE;
//
//		minLength = Math.min(Math.abs(obj0.getDistance() - obj1.getDistance()),
//				minLength);
//		// for( Object param0 : obj0) {
//		// for( Object param1 : obj1) {
//		// minLength = Math.min(Math.abs((Double)param0 - (Double)param1),
//		// minLength);
//		// }
//		// }
//		return minLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.GenericCurve#length(double,
	 *      double)
	 */
	public double length(double par1, double par2) {
		// OK
		double parDif = Math.abs(DoubleOperation.subtract(par1, par2));
		return DoubleOperation.mult(parDif, this.getCurve().length());
	}

	/**
	 * Added to Class requirements of CurveSegments Returns the envelope of the
	 * CurveSegment
	 * 
	 * @return Envelope of the CurveSegment
	 */
	public abstract Envelope getEnvelope();

	/**
	 * @param distance
	 */
	public abstract void split(double distance);

	/**
	 * The method "isSimple" returns TRUE if this Object has no interior point
	 * of self-intersection or self-tangency. In mathematical formalisms, this
	 * means that every point in the interior of the object must have a metric
	 * neighborhood whose intersection with the object is isomorphic to an
	 * n-sphere, where n is the dimension of this Object. Since most coordinate
	 * geometries are represented, either directly or indirectly by functions
	 * from regions in Euclidean space of their topological dimension, the
	 * easiest test for simplicity to require that a function exist that is
	 * one-to-one and bicontinuous (continuous in both directions). Such a
	 * function is a topological isomorphism. This test does not work for
	 * "closed" objects (that is, objects for which the isCycle method returns
	 * TRUE). While Complexes contain only simple Objects, non-simple Objects
	 * are often used in "spaghetti" data sets.
	 * 
	 * @return a <code>boolean</code> value
	 */
	// this method is not part of the specification
	// public abstract boolean isSimple();
	// this method is not part of the specification

}
