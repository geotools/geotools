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
package org.geotools.geometry.iso.util.algorithmND;

import java.util.Iterator;
import java.util.List;
import org.geotools.geometry.iso.aggregate.MultiCurveImpl;
import org.geotools.geometry.iso.complex.CompositeCurveImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Computes the centroid of a linear geometry.
 * <h2>Algorithm</h2>
 * Compute the average of the midpoints of all line segments weighted by the
 * segment length.
 *
 *
 * @source $URL$
 */
public class CentroidLine {
	
	//private FeatGeomFactoryImpl factory = null;
	private CoordinateReferenceSystem crs = null;
	
	DirectPositionImpl centSum = null;
	
	private double totalLength = 0.0;

	/**
	 * Creates a new Centroid operation
	 * 
	 * @param crs
	 */
	public CentroidLine(CoordinateReferenceSystem crs) {
		this.crs = crs;
		this.centSum = new DirectPositionImpl(crs); //this.factory.getGeometryFactoryImpl().createDirectPosition();
	}

	/**
	 * Adds the linestring(s) defined by a Geometry to the centroid total. If
	 * the geometry is not linear it does not contribute to the centroid
	 * 
	 * @param geom
	 *            the geometry to add
	 */
	public void add(Geometry geom) {
		if (geom instanceof CurveImpl) {
			this.addCurve((CurveImpl) geom);
		} else if (geom instanceof RingImpl) {
			this.addCurveIter(((RingImplUnsafe)geom).getGenerators().iterator());
		} else if (geom instanceof MultiCurveImpl) {
			this.addCurveIter(((MultiCurveImpl)geom).getElements().iterator());
		} else if (geom instanceof CompositeCurveImpl) {
			this.addCurveIter(((CompositeCurveImpl)geom).getGenerators().iterator());
		}
	}
	
	private void addCurveIter(Iterator<OrientableCurve> curveIter) {
		while (curveIter.hasNext()) {
			this.addCurve((CurveImpl) curveIter.next());
		}
	}
	
	private void addCurve(CurveImpl curve) {
		this.addPointSequence(curve.asDirectPositions());
	}

	/**
	 * Adds the length defined by an array of coordinates.
	 * 
	 * @param pts
	 *            an array of {@link Coordinate}s
	 */
	public void addPointSequence(List<DirectPosition> pts) {
		
		DirectPositionImpl dpAct = new DirectPositionImpl( pts.get(0) );
		DirectPositionImpl directPositionNext;

		for (int i = 0; i < pts.size()-1; i++) {
			directPositionNext = new DirectPositionImpl( pts.get(i+1) );
			
			double segmentLen = dpAct.distance(directPositionNext);
			this.totalLength += segmentLen;
			
			DirectPositionImpl tempMid = dpAct.clone();
			tempMid.add( directPositionNext);
			tempMid.divideBy(2);
			tempMid.scale(segmentLen);
			this.centSum.add(tempMid);
			
			dpAct = directPositionNext;
		}
	}
	
	/**
	 * Returns the centroid for the added curves
	 * 
	 * @return Centroid position
	 */
	public DirectPositionImpl getCentroid() {
		this.centSum.divideBy(this.totalLength);
		return this.centSum;
	}

}
