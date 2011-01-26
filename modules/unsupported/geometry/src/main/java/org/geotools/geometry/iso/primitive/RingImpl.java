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
 * 
 * A Ring is used to represent a single connected component of a
 * SurfaceBoundary. It consists of a number of references to OrientableCurves
 * connected in a cycle (an object whose boundary is empty). A Ring is
 * structurally similar to a CompositeCurve in that the endPoint of each
 * OrientableCurve in the sequence is the startPoint of the next OrientableCurve
 * in the Sequence. Since the sequence is circular, there is no exception to
 * this rule. Each ring, like all boundaries is a cycle and each ring is simple.
 * 
 * Ring: {isSimple() = TRUE}
 * 
 * Even though each Ring is simple, the boundary need not be simple. The easiest
 * case of this is where one of the interior rings of a surface is tangent to
 * its exterior ring. Implementations may enforce stronger restrictions on the
 * interaction of boundary elements.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 * @source $URL$
 */
public class RingImpl extends RingImplUnsafe implements Ring {

	private SurfaceBoundaryImpl surfaceBoundary;

	/**
	 * Creates a Ring
	 * @param generator
	 */
	public RingImpl(List<OrientableCurve> generator) {
		super(generator);
		this.checkConsistency(generator);
	}

	/**
	 * Check consistency of the given curve list:
	 * - Continuity
	 * - Simplicity
	 * - Closeness
	 */
	private void checkConsistency(List<OrientableCurve> aGenerator) {
		CurveImpl oc0 = (CurveImpl) aGenerator.get(0).getPrimitive();
		CurveImpl oc1 = (CurveImpl) aGenerator.get(aGenerator.size() - 1)
				.getPrimitive();

		// Check Closeness
		if (!oc0.getStartPoint().equals(oc1.getEndPoint()))
			throw new IllegalArgumentException("Start point of first element has to be at the same position as end point of last element"); //$NON-NLS-1$

		// Check Continuity and merge all curves into a new curve
		CurveImpl newCurve = oc0;
		for (int i=1; i<aGenerator.size(); i++) {
			CurveImpl nextCurve = (CurveImpl) aGenerator.get(i);
			DirectPosition startPoint = nextCurve.getStartPoint();
			DirectPosition endPoint = newCurve.getEndPoint();
			if (!endPoint.equals(startPoint))
				throw new IllegalArgumentException("The curve segments are not continuous"); //$NON-NLS-1$
			newCurve = newCurve.merge(nextCurve);
		}

		// Check Simplicity
		IsSimpleOp isSimple = new IsSimpleOp();
		if (! isSimple.isSimple(newCurve))
			throw new IllegalArgumentException("The curve segments are not simple, but intersect"); //$NON-NLS-1$

	}
}
