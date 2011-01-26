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
package org.geotools.geometry.iso.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PointArrayImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Merges curves at end points
 * 
 * @author Sanjay Dominik Jena
 *
 *
 * @source $URL$
 */
public class Merger {
	
	//FeatGeomFactoryImpl mFactory = null;
	CoordinateReferenceSystem crs;
	
	/*
	public Merger(FeatGeomFactoryImpl factory) {
		this.mFactory = factory;
		this.crs = factory.getCoordinateReferenceSystem();
	}
	*/
	
	public Merger(CoordinateReferenceSystem crs) {
		// TODO Auto-generated constructor stub
		this.crs = crs;
	}

	/**
	 * Merges a list of continuous curves into a new single curve.
	 * In order two neighboured curves are merged, their end and startpoint must be equal.
	 * 
	 * @param curves
	 * @return null or tosses IllegalArgumentException
	 */
	public CurveImpl merge(List<CurveImpl> curves) {

		for (int i=0; i<curves.size()-1; i++) {
			if (!curves.get(i).getEndPoint().equals(curves.get(i+1).getStartPoint())) {
				throw new IllegalArgumentException("Curves are not continuous");
			}
		}
		

		return null;
	}
	
	/**
	 * Merges a set of curves into a new single curve.
	 * This method trys all combinations of curve´s start and end points.
	 * 
	 * @param curves
	 * @return null
	 */
	public CurveImpl merge(Set<CurveImpl> curves) {
		return null;
	}
	
	/**
	 * Constructs a new Curve by merging this Curve with another Curve
	 * The two input curves will not be modified.
	 * There will be no more references to positions or lists of the input curves, all values are copied.
	 * 
	 * @param curve1
	 * @param curve2
	 * @return new curve
	 */
	public CurveImpl merge(CurveImpl curve1, CurveImpl curve2) {
		CurveImpl firstCurve = null;
		CurveImpl secondCurve = null;
		
		if (curve1.getStartPoint().equals(curve2.getEndPoint())) {
			firstCurve = curve2;
			secondCurve = curve1;
		} else
		if (curve1.getEndPoint().equals(curve2.getStartPoint())) {
				firstCurve = curve1;
				secondCurve = curve2;
		} else
			throw new IllegalArgumentException("Curves do not share a start and end point ");
		
		List<CurveImpl> curves = new ArrayList<CurveImpl>();
		curves.add(firstCurve);
		curves.add(secondCurve);
		
		return this.mergeContinuousCurves(curves);		
	}
	
	/**
	 * Merges a list of continuous curves into a new single curve.
	 * In order two neighboured curves are merged, their end and startpoint must be equal.
	 * 
	 * @param curves
	 * @return
	 */
	private CurveImpl mergeContinuousCurves(List<CurveImpl> curves) {

		List<Position> positionList = new ArrayList<Position>();
		
		int i=0;
		int j=0;
		for (i=0; i<curves.size(); i++) {
			List<DirectPosition> dPList = curves.get(i).asDirectPositions();
			for (j=0; j<dPList.size()-1; j++) {
				positionList.add( new DirectPositionImpl( dPList.get(j) ));
			}
		}		
		positionList.add(curves.get(curves.size()-1).getEndPoint());
		
		// Create List of CurveSegment´s (LineString´s)
		LineStringImpl lineString = new LineStringImpl(new PointArrayImpl(positionList), 0.0);
		// LineStringImpl lineString =
		// coordFactory.createLineString(aPositions);
		List<CurveSegment> segments = new ArrayList<CurveSegment>();
		segments.add(lineString);
		
		// Create List of OrientableCurve´s (Curve´s)
		// test OK
		if (segments == null)
			throw new NullPointerException();
		
		// A curve will be created
		// - The curve will be set as parent curves for the Curve segments
		// - Start and end params for the CurveSegments will be set
		return new CurveImpl( crs, segments);
	}
	
	

}
