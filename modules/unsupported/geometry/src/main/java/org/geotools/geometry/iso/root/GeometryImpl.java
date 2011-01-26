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
package org.geotools.geometry.iso.root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.UnsupportedDimensionException;
import org.geotools.geometry.iso.aggregate.MultiCurveImpl;
import org.geotools.geometry.iso.aggregate.MultiPointImpl;
import org.geotools.geometry.iso.aggregate.MultiPrimitiveImpl;
import org.geotools.geometry.iso.aggregate.MultiSurfaceImpl;
import org.geotools.geometry.iso.complex.ComplexImpl;
import org.geotools.geometry.iso.complex.CompositeCurveImpl;
import org.geotools.geometry.iso.complex.CompositeSurfaceImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.operation.overlay.OverlayOp;
import org.geotools.geometry.iso.operation.relate.RelateOp;
import org.geotools.geometry.iso.primitive.CurveBoundaryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.IntersectionMatrix;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.geometry.iso.util.algorithm2D.CGAlgorithms;
import org.geotools.geometry.iso.util.algorithm2D.CentroidArea2D;
import org.geotools.geometry.iso.util.algorithm2D.ConvexHull;
import org.geotools.geometry.iso.util.algorithmND.CentroidLine;
import org.geotools.geometry.iso.util.algorithmND.CentroidPoint;
import org.geotools.referencing.CRS;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Ring;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 * GeometryImpl is the root class of the geometric object taxonomy and supports
 * methods common to all geographically referenced geometric objects.
 * GeometryImpl instances are sets of direct positions in a particular
 * coordinate reference system. A GeometryImpl can be regarded as an infinite
 * set of points that satisfies the set operation interfaces for a set of direct
 * positions, TransfiniteSet&lt;DirectPosition&gt;. Since an infinite collection
 * class cannot be implemented directly, a boolean test for inclusion is
 * provided by this class.
 * 
 * NOTE As a type, GeometryImpl does not have a well-defined default state or
 * value representation as a data type. Instantiated subclasses of GeometryImpl
 * will.
 * 
 * 
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/01-101.pdf">Abstract
 *          Specification V5</A>
 * @author Jackson Roehrig & Sanjay Jena
 */

public abstract class GeometryImpl implements Geometry, Serializable  {

	private boolean mutable = true;

	// TODO: Remove this and use positionFactory.getCoordinateReferenceSystem()
	protected final CoordinateReferenceSystem crs;
	
	// TODO: Remove this and use positionFactory.getPrecision()	
	protected final Precision percision;
	
	//protected final PrimitiveFactory primitiveFactory; // for making stuff like curve, point 
	//protected final GeometryFactory geometryFactory; // geometry for Line etc...
	private transient PositionFactory positionFactory; // for position and point array
	//protected final ComplexFactory complexFactory; // surface and friends

		
	public GeometryImpl(CoordinateReferenceSystem crs, Precision pm ){
		this.crs = crs;
		this.percision = pm;
		this.positionFactory = new PositionFactoryImpl(crs, pm);
	}

	public GeometryImpl(CoordinateReferenceSystem coordinateReferenceSystem) {
		this( coordinateReferenceSystem, new PrecisionModel() );
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract GeometryImpl clone() throws CloneNotSupportedException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getBoundary()
	 */
	public abstract Boundary getBoundary();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getDimension(org.opengis.geometry.coordinate.DirectPosition)
	 */
	public abstract int getDimension(DirectPosition point);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getEnvelope()
	 */
	public abstract Envelope getEnvelope();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getRepresentativePoint()
	 */
	public abstract DirectPosition getRepresentativePoint();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#isMutable()
	 */
	public boolean isMutable() {
		// TODO semantic JR, SJ
		// TODO implementation
		// TODO test
		// TODO documentation
		return this.mutable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#toImmutable()
	 */
	public Geometry toImmutable() {
		// TODO semantic JR, SJ
		// TODO implementation
		// TODO test
		// TODO documentation
		if (this.mutable) {
			try {
				GeometryImpl g = this.clone();
				g.mutable = false;
				return g;
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getCoordinateReferenceSystem()
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return crs;
	}
    public Precision getPrecision() {
        return percision;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getCoordinateDimension()
	 */
	public int getCoordinateDimension() {
		return crs.getCoordinateSystem().getDimension();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#transform(org.opengis.referencing.crs.CoordinateReferenceSystem)
	 */
	public Geometry transform(CoordinateReferenceSystem newCRS)
			throws TransformException {
		// create the appropriate math transform and do the transform
		MathTransform transform = null;
		try {
			transform = CRS.findMathTransform(this.getCoordinateReferenceSystem(), newCRS);
		} catch (FactoryException e) {
			Assert.isTrue(false, "Could not find math transform for given CRS objects.");
			//e.printStackTrace();
		}
		return transform(newCRS, transform);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#transform(org.opengis.referencing.crs.CoordinateReferenceSystem,
	 *      org.opengis.referencing.operation.MathTransform)
	 */
	public Geometry transform(CoordinateReferenceSystem newCRS,
			MathTransform transform) throws MismatchedDimensionException, TransformException {

			// this should be handled in each of the individual geometry classes (ie:
			// PointImpl, CurveImpl, etc), if not it will fall through to here
			throw new UnsupportedOperationException("Transform not implemented for this geometry type yet.");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getDistance(org.opengis.geometry.coordinate.root.Geometry)
	 * @deprecated  use distance()
	 */
	public final double getDistance(Geometry geometry) {
		return this.distance(geometry);
	}
	
	/**
     * Computes the distance between this and another geometry.  We have
     * to implement the logic of dealing with multiprimtive geometries separately.
	 * 
	 * gdavis - This method should really be broken out into 1 of 2 things:
	 * 		1.  an operator class that figures out the geometry type 
	 * 			and does the operation based on that, or
	 * 		2.  a double dispatch command pattern system that returns a command to perform
	 * 			based on the geometry type and the command of "distance".
	 * 		Currently this implementation works for our needs, but we should 
	 * 		consider re-designing it with one of the above approaches for better
	 * 		scalability.
	 * @see org.opengis.geometry.coordinate.root.Geometry#distance(org.opengis.geometry.coordinate.root.Geometry)
	 */
	public final double distance(Geometry geometry) {
		// first determine if this or the given geom is a multiprimitive, if so, break it
		// down and loop through each of its geoms and determine the mindistance from
		// those.
        if (geometry instanceof MultiPrimitive) {
            double minDistance = Double.POSITIVE_INFINITY;
            MultiPrimitive gc1 = (MultiPrimitive) geometry;
            Iterator<? extends Geometry> iter = gc1.getElements().iterator();
            while (iter.hasNext()) {
            	GeometryImpl prim = (GeometryImpl) iter.next();
                double d = prim.distance(this);
                if (d < minDistance) {
                    minDistance = d;
                    // can't get smaller than 0, so exit now if that is the case
                    if (minDistance == 0) return 0;
                }
            }
            return minDistance;
        }
        else if (this instanceof MultiPrimitive) {
            double minDistance = Double.POSITIVE_INFINITY;
            MultiPrimitive gc1 = (MultiPrimitive) this;
            Iterator<? extends Geometry> iter = gc1.getElements().iterator();
            while (iter.hasNext()) {
            	GeometryImpl prim = (GeometryImpl) iter.next();
                double d = prim.distance(geometry);
                if (d < minDistance) {
                    minDistance = d;
                    // can't get smaller than 0, so exit now if that is the case
                    if (minDistance == 0) return 0;
                }
            }
            return minDistance;
        }
        else {
        	// first check if the geometries intersect (if so the min distance is 0)
        	if (this.intersects(geometry)) {
        		return 0;
        	}
        	
        	// ensure both this and the given geometry are either points or 
        	// linesegments so we can use the CGAlrogrithm calculations.
        	List<LineSegment> lines1 = null;
        	List<LineSegment> lines2 = null;
        	PointImpl point1 = null;
        	PointImpl point2 = null;
        	
        	// convert this geom
        	if (this instanceof PointImpl) {
        		point1 = (PointImpl) this;
        	}
        	else if (this instanceof CurveImpl) {
        		lines1 = ((CurveImpl)this).asLineSegments();
        	}
        	else if (this instanceof RingImplUnsafe) {
        		lines1 = ((RingImplUnsafe)this).asLineString().asLineSegments();
        	}
        	else if (this instanceof RingImpl) {
        		lines1 = ((RingImpl)this).asLineString().asLineSegments();
        	}
        	else if (this instanceof SurfaceImpl) {
        		lines1 = ((RingImplUnsafe)((SurfaceImpl)this).getBoundary().getExterior()).asLineString().asLineSegments();
        	}
        	
        	// convert given geom
        	if (geometry instanceof PointImpl) {
        		point2 = (PointImpl) geometry;
        	}
        	else if (geometry instanceof CurveImpl) {
        		lines2 = ((CurveImpl)geometry).asLineSegments();
        	}
        	else if (geometry instanceof RingImplUnsafe) {
        		lines2 = ((RingImplUnsafe)geometry).asLineString().asLineSegments();
        	}
        	else if (geometry instanceof RingImpl) {
        		lines2 = ((RingImpl)geometry).asLineString().asLineSegments();
        	}
        	else if (geometry instanceof SurfaceImpl) {
        		lines2 = ((RingImplUnsafe)((SurfaceImpl)geometry).getBoundary().getExterior()).asLineString().asLineSegments();
        	}
        	
        	// now determine which algorithm to use for finding the shortest
        	// distance between the two geometries
        	if (point1 != null && point2 != null) {
        		// use directposition.distance()
        		return point1.getPosition().distance(point2.getPosition());
        	}
        	else if (lines1 != null) {
        		if (point2 != null) {
        			// loop through each linesegment and check for the min distance
        			double minDistance = Double.POSITIVE_INFINITY;
        			for (int i=0; i<lines1.size(); i++) {
        				Coordinate c1 = new Coordinate(point2.getRepresentativePoint().getCoordinates());
        				Coordinate cA = new Coordinate(lines1.get(i).getStartPoint().getCoordinates());
        				Coordinate cB = new Coordinate(lines1.get(i).getEndPoint().getCoordinates());
        				double d = CGAlgorithms.distancePointLine(c1, cA, cB);
        				if ( d < minDistance) {
        					minDistance = d;
        					if (minDistance == 0) return 0;
        				}
        			}
        			return minDistance;
        		}
        		else if (lines2 != null) {
        			// loop through each set of linesegments and check for the 
        			// min distance
        			double minDistance = Double.POSITIVE_INFINITY;
        			for (int i=0; i<lines1.size(); i++) {
        				for (int y=0; y<lines2.size(); y++) {
	        				Coordinate A = new Coordinate(lines1.get(i).getStartPoint().getCoordinates());
	        				Coordinate B = new Coordinate(lines1.get(i).getEndPoint().getCoordinates());
	        				Coordinate C = new Coordinate(lines2.get(y).getStartPoint().getCoordinates());
	        				Coordinate D = new Coordinate(lines2.get(y).getEndPoint().getCoordinates());
	        				double d = CGAlgorithms.distanceLineLine(A, B, C, D);
	        				if ( d < minDistance) {
	        					minDistance = d;
	        					if (minDistance == 0) return 0;
	        				}
        				}
        			}
        			return minDistance;
        		}
        		
        	}
        	else if (lines2 != null) {
        		if (point1 != null) {
        			// loop through each linesegment and check for the min distance
        			double minDistance = Double.POSITIVE_INFINITY;
        			for (int i=0; i<lines2.size(); i++) {
        				Coordinate c1 = new Coordinate(point1.getRepresentativePoint().getCoordinates());
        				Coordinate cA = new Coordinate(lines2.get(i).getStartPoint().getCoordinates());
        				Coordinate cB = new Coordinate(lines2.get(i).getEndPoint().getCoordinates());
        				double d = CGAlgorithms.distancePointLine(c1, cA, cB);
        				if ( d < minDistance) {
        					minDistance = d;
        					if (minDistance == 0) return 0;
        				}
        			}
        			return minDistance;
        		}
        	}
       	
        }
        
		// if it is some other type, show an error
		// TODO: implement code for any missing types
		Assert.isTrue(false);
		return Double.NaN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getBuffer(double)
	 */
	public Geometry getBuffer(double distance) {
		// TODO semantic JR, SJ
		// TODO implementation
		// TODO test
		// TODO documentation
		Assert.isTrue(false);
		return null;
	}


	/**
	 * Return a Primitive which represents the envelope of this Geometry instance
	 * (non-Javadoc)
	 * 
	 * @return primitive representing the envelope of this Geometry
	 * @see org.opengis.geometry.coordinate.root.Geometry#getMbRegion()
	 */
	public Geometry getMbRegion() {
		PrimitiveFactoryImpl primitiveFactory = new PrimitiveFactoryImpl(crs, getPositionFactory());
		return primitiveFactory.createPrimitive( this.getEnvelope() );
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getCentroid()
	 */
	public DirectPosition getCentroid() {
	
		// Point: the point itself
		// MultiPoint: the average of the contained points
		if (this instanceof PointImpl ||
			this instanceof MultiPointImpl) {
			CentroidPoint cp = new CentroidPoint(this.crs);
			cp.add(this);
			return cp.getCentroid();
		} else
			
		// CurveBoundary: the average of start and end point
		if (this instanceof CurveBoundaryImpl) {
			CentroidPoint cp = new CentroidPoint(this.crs);
			cp.add(((CurveBoundaryImpl)this).getStartPoint());
			cp.add(((CurveBoundaryImpl)this).getEndPoint());
			return cp.getCentroid();
			
		} else
		// Curve: the average of the weighted line segments
		// MultiCurve: the average of the weighted line segments of all contained curves
		// Ring: the average of the weighted line segments of the contained curves
		if (this instanceof CurveImpl ||
			this instanceof MultiCurveImpl ||
			this instanceof RingImpl) {
			CentroidLine cl = new CentroidLine(this.crs);
			cl.add(this);
			return cl.getCentroid();
		} else
			
		// SurfaceBoundary: the average of the weighted line segments of all curves of the exterior and interior rings
		if (this instanceof SurfaceBoundaryImpl) {
				CentroidLine cl = new CentroidLine(this.crs);
				cl.add(((SurfaceBoundaryImpl)this).getExterior());
				Iterator<Ring> interiors = ((SurfaceBoundaryImpl)this).getInteriors().iterator();
				while (interiors.hasNext()) {
					cl.add((GeometryImpl) interiors.next());
				}
				return cl.getCentroid();
					
		} else
			
		// Surface: the average of the surface (considers holes)
		// MultiSurface: the average of all contained surfaces (considers holes)
		if (this instanceof SurfaceImpl ||
			this instanceof MultiSurfaceImpl) {
			CentroidArea2D ca = new CentroidArea2D(this.crs);
			ca.add(this);
			return ca.getCentroid();
					
		}
		
		// Missing: CompositePoint, CompositeCurve, CompositeSurface

		// - MultiPrimitive
		// The ISO 19107 specs state that the centroid of a colleciton of primitives
		// should only take into consideration the primitives with the largest
		// dimension (ie: if there are points, lines and polygons, it only considers
		// the polygons).
		if (this instanceof MultiPrimitiveImpl) {
			// First figure out what type of primtives should be considered in this
			// multiprimitive
			int maxD = this.getDimension(null);
			
			// get the centroid point of each element in this multiprimitive that matches
			// the maxD dimension and return the average of the centroid points
			CentroidPoint cp = new CentroidPoint(this.crs);
			Set<? extends Primitive> elems = ((MultiPrimitiveImpl)this).getElements();
			Iterator<? extends Primitive> iter = elems.iterator();
			while (iter.hasNext()) {
				Geometry prim = iter.next();
				if (prim.getDimension(null) == maxD) {
					cp.add(new PointImpl(prim.getCentroid()));
				}
			}
			
			// return the average of the centroid points
			return cp.getCentroid();
			
		}
	
		Assert.isTrue(false, "The centroid operation is not defined for this geometry object");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getConvexHull()
	 */
	public Geometry getConvexHull() {
		ConvexHull ch = new ConvexHull(this);
		return ch.getConvexHull();		
	}
	
	
	// ***************************************************************************
	// ***************************************************************************
	// ******  RELATIONAL BOOLEAN OPERATORS
	// ***************************************************************************
	// ***************************************************************************
	
	/**
	 * Verifies a boolean relation between two geometry objects
	 * 
	 * @version <A HREF="http://www.opengis.org/docs/01-101.pdf">Abstract Specification V5</A>, page 126 (Clementini Operators)
	 * 
	 * @param geom1
	 * @param geom2
	 * @param intersectionPatternMatrix
	 * 
	 * @return TRUE if the Intersection Pattern Matrix describes the topological relation between the two input geomtries correctly, FALSE if not. 
	 * @throws UnsupportedDimensionException
	 */
	public static boolean cRelate(Geometry g1, Geometry g2, String intersectionPatternMatrix) throws UnsupportedDimensionException {
		GeometryImpl geom1 = GeometryImpl.castToGeometryImpl(g1);
		GeometryImpl geom2 = GeometryImpl.castToGeometryImpl(g2);
		IntersectionMatrix tIM = RelateOp.relate((GeometryImpl) geom1, (GeometryImpl) geom2);
		return tIM.matches(intersectionPatternMatrix);
	}
	
	/**
	 * Verifies a boolean relation between two geometry objects
	 * 
	 * @version <A HREF="http://www.opengis.org/docs/01-101.pdf">Abstract Specification V5</A>, page 126 (Clementini Operators)
	 * 
	 * @param aOther
	 * @param intersectionPatternMatrix
	 * 
	 * @return TRUE if the Intersection Pattern Matrix describes the topological relation between the two input geomtries correctly, FALSE if not. 
	 * @throws UnsupportedDimensionException
	 */
	public boolean relate(Geometry aOther, String intersectionPatternMatrix)
			throws UnsupportedDimensionException {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(aOther);
		IntersectionMatrix tIM = RelateOp.relate(this, geom);
		return tIM.matches(intersectionPatternMatrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#contains(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public boolean contains(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);
		// a.Contains(b) = b.within(a)
		return geom.within(this);
	}
	
	/**
	 * This operator tests, whether an object is spatially within this Geometry object
	 *  
	 * @param pointSet Another Object
	 * 
	 * @return TRUE, if the other object is spatially within this object
	 */
	public boolean within(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);
		
		// Return false, if the envelopes doesn´t intersect
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
			return false;

		IntersectionMatrix tIM = null;
		try {
			tIM = RelateOp.relate(this, geom);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return false;
		}
		
		boolean rValue = false;
		rValue = tIM.matches("T*F**F***");
		
//		if (this instanceof PrimitiveImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Primitive / Primitive
//				rValue = tIM.matches("TFF******");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Primitive / Complex
//				rValue = tIM.matches("T*F******");
//			} else {
//				Assert.isTrue(false);
//			}
//		} else
//		if (this instanceof ComplexImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Complex / Primitive
//				rValue = tIM.matches("T***F****");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Complex / Complex
//				rValue = tIM.matches("T*F**F***");
//			} else {
//				Assert.isTrue(false);
//			}
//		}
		
		return rValue;	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#contains(org.opengis.geometry.coordinate.DirectPosition)
	 */
	public boolean contains(DirectPosition position) {

		// Return false, if the point doesn´t lie in the envelope of this object
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(position))
			return false;
		
		GeometryImpl point = new PointImpl( position );
		return point.within(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#intersects(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public boolean intersects(TransfiniteSet pointSet) {
		// Intersects = !Disjoint
		return !this.disjoint(pointSet);
	}
	
	/**
	 * This operator tests, whether an object is spatially disjoint with this Geometry object
	 * 
	 * @param pointSet The other object
	 * 
	 * @return TRUE, if the other object is disjoint with this object
	 */
	public boolean disjoint(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);

		// Return true, if the envelopes doesn´t intersect
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
			return true;
		

//		String intersectionPatternMatrix = "";
//		if (this instanceof PrimitiveImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Primitive / Primitive
//				// Empty: I/I
//				// B/I, I/B, B/B may intersect
//				intersectionPatternMatrix = "F********";
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Primitive / Complex
//				// Empty: I/I, I/B
//				// B/I, B/B may intersect
//				intersectionPatternMatrix = "FF*******";
//			} else {
//				Assert.isTrue(false);
//			}
//		} else
//		if (this instanceof ComplexImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Complex / Primitive
//				// Empty: I/I, B/I
//				// I/B, B/B may intersect
//				intersectionPatternMatrix = "F**F*****";
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Complex / Complex
//				// Empty: I/I, B/I, I/B, B/B
//				intersectionPatternMatrix = "FF*FF****";
//			} else {
//				Assert.isTrue(false);
//			}
//		}

		String intersectionPatternMatrix = "FF*FF****";

		try {
			IntersectionMatrix tIM = RelateOp.relate(this, geom);
			boolean rValue = tIM.matches(intersectionPatternMatrix);
			return rValue;
			
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#equals(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public boolean equals(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);

		// Return false, if the envelopes doesn´t intersect
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
			return false;
		
		IntersectionMatrix tIM = null;
		try {
			tIM = RelateOp.relate(this, geom);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return false;
		}
		
		boolean rValue = false;
		
		// No distinction between primitive and complex (explanation see thesis)
		rValue = tIM.matches("T*F**FFF*");
		
//		if (this instanceof PrimitiveImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Primitive / Primitive
//				rValue = tIM.matches("T*F**FFF*");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Primitive / Complex
//				//rValue = tIM.matches("TTFF*TFF*");
//				rValue = tIM.matches("T*F**FFF*");
//			} else {
//						Assert.isTrue(false);
//			}
//		} else
//		if (this instanceof ComplexImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Complex / Primitive
//				//rValue = tIM.matches("TFFT*FFT*");
//				rValue = tIM.matches("T*F**FFF*");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Complex / Complex
//				rValue = tIM.matches("T*F**FFF*");
//			} else {
//						Assert.isTrue(false);
//			}
//		}
		
		return rValue;	

		
	}

	/**
	 * This operator tests, whether an object touches this object in an edge or point
	 * 
	 * @param pointSet The other object
	 * 
	 * @return TRUE, if the other object touches this object
	 */
	public boolean touches(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);

		// Return false, if the envelopes doesn´t intersect
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
			return false;

		IntersectionMatrix tIM = null;
		try {
			tIM = RelateOp.relate(this, geom);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return false;
		}
		
		boolean rValue = false;
		rValue = tIM.matches("F***T****")
	  	  || tIM.matches("FT*******")
	  	  || tIM.matches("F**T*****");
		
//		if (this instanceof PrimitiveImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Primitive / Primitive
//				rValue = tIM.matches("FT*******")
//					  || tIM.matches("F**T*****");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Primitive / Complex
//				rValue = tIM.matches("F***T****")
//					  || tIM.matches("FT*******")
//					  || tIM.matches("F**T*****");
//			} else {
//				Assert.isTrue(false);
//			}
//		} else
//		if (this instanceof ComplexImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Complex / Primitive
//				rValue = tIM.matches("F***T****")
//				  	  || tIM.matches("FT*******")
//				  	  || tIM.matches("F**T*****");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Complex / Complex
//				rValue = tIM.matches("F***T****")
//			  	  	  || tIM.matches("FT*******")
//			  	  	  || tIM.matches("F**T*****");
//			} else {
//				Assert.isTrue(false);
//			}
//		}
		
		return rValue;	
	}
	
	/**
	 * This operator tests, whether an object overlaps with this object.
	 * That is that a part of the object lies within this object and another part lies without this object,
	 * e.g. the other object intersects with the interior, boundary and exterior of this object
	 * 
	 * @param pointSet The other object
	 * 
	 * @return TRUE, if the other object overlaps with this object
	 */
	public boolean overlaps(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);
		
		int d1 = geom.getDimension(null);
		int d2 = this.getDimension(null);

		// Overlaps only for Point/Point, Curve/Curve, Surface/Surface
		if (d1 != d2) {
			return false;
		}
		// Return false, if the envelopes doesn´t intersect
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
			return false;
		
		IntersectionMatrix tIM = null;
		try {
			tIM = RelateOp.relate(this, geom);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return false;
		}
		
		boolean rValue = false;
		if (d1 == 1)
			rValue = tIM.matches("1*T***T**");
		else
			rValue = tIM.matches("T*T***T**");

		
//		if (this instanceof PrimitiveImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Primitive / Primitive
//				if (geom.getDimension(null) == 1)
//					rValue = tIM.matches("1*T***T**");
//				else
//					rValue = tIM.matches("T*T***T**");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Primitive / Complex
//				if (geom.getDimension(null) == 1)
//					rValue = tIM.matches("1*T***T**");
//				else
//					rValue = tIM.matches("T*T***T**");
//			} else {
//				Assert.isTrue(false);
//			}
//		} else
//		if (this instanceof ComplexImpl) {
//			if (geom instanceof PrimitiveImpl) {
//				// Complex / Primitive
//				if (geom.getDimension(null) == 1)
//					rValue = tIM.matches("1*T***T**");
//				else
//					rValue = tIM.matches("T*T***T**");
//			} else
//			if (geom instanceof ComplexImpl) {
//				// Complex / Complex
//				if (geom.getDimension(null) == 1)
//					rValue = tIM.matches("1*T***T**");
//				else
//					rValue = tIM.matches("T*T***T**");
//			} else {
//				Assert.isTrue(false);
//			}
//		}
		
		return rValue;	
	}
	
	public boolean crosses(TransfiniteSet pointSet) {
		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);
		
		int d1 = geom.getDimension(null);
		int d2 = this.getDimension(null);

		// Crosses only for Point/Curve, Curve/Curve, Point/Surface, Curve/Surface
		if ((d1 == 2 && d2 == 2) || (d1 == 0) && (d2 == 0)) {
			return false;
		}

		// Return false, if the envelopes doesn´t intersect
		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
			return false;
		
		IntersectionMatrix tIM = null;
		try {
			tIM = RelateOp.relate(this, geom);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return false;
		}
		
		// No distinction between primitive and complex (explanation see thesis)
		boolean rValue = false;
		
		if (d1 == 1 && d2 == 1)
			rValue = tIM.matches("0********");
		else
			rValue = tIM.matches("T*T******");
		
		return rValue;
		
	}
	
	
//	public boolean covers(TransfiniteSet pointSet) {
//		// TO-DO test
//		// TO-DO documentation
//		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);
//
//		// Return false, if the envelopes doesn´t intersect
//		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
//			return false;
//		
//		try {
//			IntersectionMatrix tIM = RelateOp.relate(this, geom);
//			return tIM.isCovers();
//		} catch (UnsupportedDimensionException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
//	public boolean coveredBy(TransfiniteSet pointSet) {
//		// TO-DO test
//		// TO-DO documentation
//		GeometryImpl geom = GeometryImpl.castToGeometryImpl(pointSet);
//
//		// Return false, if the envelopes doesn´t intersect
//		if (!((EnvelopeImpl)this.getEnvelope()).intersects(geom.getEnvelope()))
//			return false;
//		
//		try {
//			IntersectionMatrix tIM = RelateOp.relate(geom, this);
//			return tIM.isCovers();
//		} catch (UnsupportedDimensionException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	
	// ***************************************************************************
	// ***************************************************************************
	// ******  SET OPERATIONS
	// ***************************************************************************
	// ***************************************************************************
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#union(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public TransfiniteSet union(TransfiniteSet pointSet) {
		GeometryImpl otherGeom = GeometryImpl.castToGeometryImpl(pointSet);
		// Return the result geometry of the Union operation between the input
		// geometries
		try {
			return OverlayOp.overlayOp(this, otherGeom, OverlayOp.UNION);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#intersection(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public TransfiniteSet intersection(TransfiniteSet pointSet) {
		// Return the result geometry of the Intersection operation between the
		// input geometries
		GeometryImpl otherGeom = GeometryImpl.castToGeometryImpl(pointSet);
		try {
			return OverlayOp.overlayOp(this, otherGeom, OverlayOp.INTERSECTION);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#difference(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public TransfiniteSet difference(TransfiniteSet pointSet) {
		// Return the result geometry of the Difference operation between the
		// input geometries
		GeometryImpl otherGeom = GeometryImpl.castToGeometryImpl(pointSet);
		try {
			return OverlayOp.overlayOp(this, otherGeom, OverlayOp.DIFFERENCE);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.TransfiniteSet#symmetricDifference(org.opengis.geometry.coordinate.TransfiniteSet)
	 */
	public TransfiniteSet symmetricDifference(TransfiniteSet pointSet) {
		// Return the result geometry of the Symmetric Difference operation
		// between the input geometries
		GeometryImpl otherGeom = GeometryImpl.castToGeometryImpl(pointSet);
		try {
			return OverlayOp
					.overlayOp(this, otherGeom, OverlayOp.SYMDIFFERENCE);
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#getClosure()
	 */
	public Complex getClosure() {		
		if (this instanceof ComplexImpl) {
			// Return this Complex instance, because complexes already contain their boundary
			// CompositePoint, CompositeCurve, CompositeSurface, Ring, CurveBoundary, SurfaceBoundary
			return (Complex) this;
		} else
		if (this instanceof CurveImpl) {
			List<OrientableCurve> cl = new ArrayList<OrientableCurve>();
			cl.add((OrientableCurve) this);
			return new CompositeCurveImpl(cl);
		} else
		if (this instanceof SurfaceImpl) {
			List<OrientableSurface> cs = new ArrayList<OrientableSurface>();
			cs.add( (OrientableSurface) this);
			return new CompositeSurfaceImpl(cs);
		} else
		if (this instanceof MultiPrimitiveImpl) {
			// TODO
			return null;
		} else

		
		Assert.isTrue(false, "The closure operation is not implemented for this geometry object");
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.#isCycle()
	 */
	public boolean isCycle() {
		// The object is a cycle, if the boundary is empty: isCycle() = boundary().isEmpty()
		return (this.getBoundary() == null);
	}
	
	
	/**
	 * Use this function to cast Geometry instances to a GeometryImpl instance.
	 * In that way we can control the illegal injection of other instances at a central point.
	 * 
	 * @param g Geometry instance
	 * @return Instance of Geometry Impl
	 */
	protected static GeometryImpl castToGeometryImpl(Geometry g) {
		if (g instanceof GeometryImpl) {
			return (GeometryImpl)g;
		} else {
			throw new IllegalArgumentException("Illegal Geometry instance.");
		}
	}

	/**
	 * Use this function to cast TransfiniteSet instances to a GeometryImpl instance.
	 * In that way we can control the illegal injection of other instances at a central point.
	 * 
	 * @param tf
	 * @return
	 */
	protected static GeometryImpl castToGeometryImpl(TransfiniteSet tf) {
		if (tf instanceof GeometryImpl) {
			return (GeometryImpl)tf;
		} else {
			throw new IllegalArgumentException("TransfiniteSet instance not supported.");
		}
	}

	protected PositionFactory getPositionFactory() {
		if( positionFactory == null ){
			// we must of been transfered over a wire?
			positionFactory = new PositionFactoryImpl(crs, percision );
		}
		return positionFactory;
	}
	
	

}
