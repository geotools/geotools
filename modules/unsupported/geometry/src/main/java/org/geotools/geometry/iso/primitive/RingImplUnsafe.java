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
package org.geotools.geometry.iso.primitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.complex.CompositeCurveImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.io.GeometryToString;
import org.geotools.geometry.iso.operation.IsSimpleOp;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


/**
 * This Ring implementation does not do a consistency
 * check.  This Ring should only be used when the user knows the Ring is valid and doesn't
 * want to spend the expensive processing time to validate it upon creation.  Otherwise,
 * RingImpl should be used which will validate when it is created.
 *
 * @author Graham Davis
 *
 *
 * @source $URL$
 */
public class RingImplUnsafe extends CompositeCurveImpl implements Ring {

	private SurfaceBoundaryImpl surfaceBoundary;
	
	/**
	 * Creates a Ring
	 * @param generator
	 */
	public RingImplUnsafe(List<OrientableCurve> generator) {
		super(generator);
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.CompositeCurveImpl#clone()
	 */
	public RingImplUnsafe clone() throws CloneNotSupportedException {
		// Test OK
		Iterator<Primitive> elementIter = this.getElements().iterator();
		List<OrientableCurve> newElements = new ArrayList<OrientableCurve>();
		while (elementIter.hasNext()) {
			newElements.add((Curve) elementIter.next().clone());
		}
		return new RingImplUnsafe(newElements);
	}

	
	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.CompositeCurveImpl#getBoundary()
	 */
	public CurveBoundary getBoundary() {
		// A Ring does not have a Boundary since it´s start and end points are equal.
		return null;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.CompositeCurveImpl#createBoundary()
	 */
	public Set<Complex> createBoundary() {
		// overwrites the boundary definition of CompositeCurve
		// returns null, cause a Ring does not have a boundary
		return null;
	}

	/**
	 * @return Returns the surfaceBoundary.
	 */
	public SurfaceBoundaryImpl getSurfaceBoundary() {
		return surfaceBoundary;
	}

	/**
	 * @param surfaceBoundary
	 *            The surfaceBoundary to set.
	 */
	public void setSurfaceBoundary(SurfaceBoundaryImpl surfaceBoundary) {
		this.surfaceBoundary = surfaceBoundary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.complex.CompositeCurveImpl#isSimple()
	 */
	public boolean isSimple() {
		// Implementation ok
		// A Ring is always simple
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.complex.CompositeCurveImpl#isCycle()
	 */
	public boolean isCycle() {
		// Implementation ok
		// A Ring is always a cycle
		return true;
	}

	/**
	 * @return
	 */
	public List<DirectPosition> asDirectPositions() {

		List<DirectPosition> rList = new ArrayList<DirectPosition>();

		// Iterate all Curves
		for (int i = 0; i < this.elements.size(); i++) {

			CurveImpl tCurve = (CurveImpl) this.elements.get(i);
			Iterator<CurveSegment> tCurveSegmentIter = tCurve.getSegments()
					.iterator();
			CurveSegment tSegment = null;

			// Iterate all CurveSegments (= LineStrings)
			while (tCurveSegmentIter.hasNext()) {
				tSegment = tCurveSegmentIter.next();

				// TODO: This version only handles the CurveSegment type
				// LineString
				LineStringImpl tLineString = (LineStringImpl) tSegment;

				Iterator<LineSegment> tLineSegmentIter = tLineString
						.asLineSegments().iterator();
				while (tLineSegmentIter.hasNext()) {
					LineSegment tLineSegment = tLineSegmentIter.next();
					// Add new Coordinate, which is the start point of the
					// actual LineSegment
					rList.add( tLineSegment.getStartPoint());
				}
			}
			// Add new Coordinate, which is the end point of the last
			// curveSegment
			rList.add( tSegment.getEndPoint());
		}

		return rList;
	}
	
	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.#getRepresentativePoint()
	 */
	public DirectPosition getRepresentativePoint() {
		// Return the start point of this ring, since it is part of the object
		return ((CurveImpl)this.getGenerators().get(0)).getStartPoint();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.CompositeCurveImpl#toString()
	 */
	public String toString() {
		return GeometryToString.getString(this);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((surfaceBoundary == null) ? 0 : surfaceBoundary.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RingImplUnsafe other = (RingImplUnsafe) obj;
		if (surfaceBoundary == null) {
			if (other.surfaceBoundary != null)
				return false;
		} else if (!surfaceBoundary.equals(other.surfaceBoundary))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#transform(org.opengis.referencing.crs.CoordinateReferenceSystem,
	 *      org.opengis.referencing.operation.MathTransform)
	 */
	public Geometry transform(CoordinateReferenceSystem newCRS,
			MathTransform transform) throws MismatchedDimensionException, TransformException {

		// loop through each point in this Ring and transform it to the new CRS, then
		// use the new points to build a new Ring and return that.
		PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(newCRS, getPositionFactory());
		GeometryFactory geometryFactory = new GeometryFactoryImpl(newCRS, getPositionFactory());
		
		DirectPositionImpl dp1 = null;
		List<DirectPosition> currentpositions = this.asDirectPositions();
		Iterator<DirectPosition> iter = currentpositions.iterator();
		List<Position> newpositions = new ArrayList<Position>();
		while (iter.hasNext()) {
			DirectPosition thispos = (DirectPosition) iter.next();
			
			dp1 = new DirectPositionImpl(newCRS);
			dp1 = (DirectPositionImpl) transform.transform( thispos, dp1);
			newpositions.add(dp1);
		}
		
		// use the new positions list to build a new Ring and return it
		LineString lineString = geometryFactory.createLineString(newpositions);
		List curveSegmentList = Collections.singletonList(lineString);
		CurveImpl newCurve = (CurveImpl) primitiveFactory.createCurve(curveSegmentList);
		ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
		curveList.add(newCurve);
		Ring newRing = primitiveFactory.createRing(curveList);
		return newRing;
			
	}
}
