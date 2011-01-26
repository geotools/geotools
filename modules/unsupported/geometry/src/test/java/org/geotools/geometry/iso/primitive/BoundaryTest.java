/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author sanjay
 *
 *
 * @source $URL$
 */
public class BoundaryTest extends TestCase {
	
	private CoordinateReferenceSystem crs;
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		this.crs = DefaultGeographicCRS.WGS84;
		
		this._testCurveBoundary1(builder);
		this._testSurfaceBoundary1(builder);
		
	}

	private void _testCurveBoundary1(GeometryBuilder builder) {

		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimitiveFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		DirectPosition dp1 = tCoordFactory.createDirectPosition(new double[] {0, 0});
		DirectPosition dp2 = tCoordFactory.createDirectPosition(new double[] {100, 100});
		
		CurveBoundary curveBoundary1 = tPrimitiveFactory.createCurveBoundary(dp1, dp2);
		
		//System.out.println(curveBoundary1);

		// RepresentativePoint()
		DirectPosition dp = curveBoundary1.getRepresentativePoint();
		assertTrue(dp.getOrdinate(0) == 0);
		assertTrue(dp.getOrdinate(1) == 0);
		
		assertTrue(curveBoundary1.isCycle() == true);
		
		// Test creating a curve boundary with the same start and end point (should result in
		//  an exception).
		PointImpl point1 = new PointImpl(dp1);
		try {
			CurveBoundaryImpl b2 = new CurveBoundaryImpl(this.crs, point1, point1);
			// fail if we get here, the above should throw an exception
			fail();
		}
		catch (IllegalArgumentException expected) {
			
		}
		
		// test clone
		PointImpl point2 = new PointImpl(dp2);
		CurveBoundaryImpl b2 = new CurveBoundaryImpl(curveBoundary1.getCoordinateReferenceSystem(), point1, point2);
		try {
			CurveBoundaryImpl expected = ((CurveBoundaryImpl) curveBoundary1).clone();
			assertTrue(b2.equals(expected));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail();
		}
		
		// test toString
		String toS = b2.toString();
		assertTrue(toS != null);
		assertTrue(toS.length() > 0);
		
		// test getEnvelope
		EnvelopeImpl env = b2.getEnvelope();
		EnvelopeImpl exp_env = new EnvelopeImpl(dp1, dp2);
		assertTrue(env.equals(exp_env));
		
		// test isSimple
		assertTrue(b2.isSimple());

		// test obj equals and hashcode
		assertTrue(b2.equals((Object) curveBoundary1));
		assertTrue(b2.equals((Object) b2));
		assertFalse(b2.equals((Object) dp1));
		assertFalse(b2.equals((Object) null));
		DirectPosition dp3 = tCoordFactory.createDirectPosition(new double[] {3, 3});
		PointImpl point3 = new PointImpl(dp3);
		assertFalse(b2.equals((Object) new CurveBoundaryImpl(this.crs, point1, point3)));
		
		assertFalse(b2.hashCode() == ((CurveBoundaryImpl) curveBoundary1).hashCode());
	}

	private void _testSurfaceBoundary1(GeometryBuilder builder) {

		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();

		/* Defining Positions for LineStrings */
		ArrayList<Position> line1 = new ArrayList<Position>();
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50, 20})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30, 30})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 50})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 70})));

		ArrayList<Position> line2 = new ArrayList<Position>();
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 70})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{40, 80})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{70, 80})));

		ArrayList<Position> line3 = new ArrayList<Position>();
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{70, 80})));
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{90, 70})));
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 60})));
		line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 40})));

		ArrayList<Position> line4 = new ArrayList<Position>();
		line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 40})));
		line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{80, 30})));
		line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50, 20})));
		
		/* Setting up Array of these LineStrings */
		ArrayList<CurveSegment> tLineList1 = new ArrayList<CurveSegment>();
		tLineList1.add(tCoordFactory.createLineString(line1));
		tLineList1.add(tCoordFactory.createLineString(line2)); 

		ArrayList<CurveSegment> tLineList2 = new ArrayList<CurveSegment>();
		tLineList2.add(tCoordFactory.createLineString(line3)); 
		tLineList2.add(tCoordFactory.createLineString(line4)); 

		/* Build Curve */
		CurveImpl curve1 = tPrimFactory.createCurve(tLineList1);
		CurveImpl curve2 = tPrimFactory.createCurve(tLineList2);

		
		/* Build Ring */
		ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
		curveList.add(curve1);
		curveList.add(curve2);
		
		Ring exteriorring1 = tPrimFactory.createRing(curveList);

		//System.out.println(exteriorring1);
		
		List<Ring> interiors = new ArrayList<Ring>();
		
		SurfaceBoundary surfaceBoundary1 = tPrimFactory.createSurfaceBoundary(exteriorring1, interiors);
		
		//System.out.println(surfaceBoundary1);

		assertTrue(surfaceBoundary1.isCycle() == true);

		
		// clone()
		SurfaceBoundary surfaceBoundary2 = null;
		try {
			surfaceBoundary2 = (SurfaceBoundary) surfaceBoundary1.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		assertTrue(surfaceBoundary1 != surfaceBoundary2);
		assertTrue(surfaceBoundary1.getExterior() != surfaceBoundary2.getExterior());
		if (surfaceBoundary1.getInteriors().size() > 0) {
			assertTrue(surfaceBoundary1.getInteriors().get(0) != surfaceBoundary2.getInteriors().get(0));
		}
		
		// RepresentativePoint()
		DirectPosition dp = surfaceBoundary1.getRepresentativePoint();
		assertTrue(dp.getOrdinate(0) == 50);
		assertTrue(dp.getOrdinate(1) == 20);

		
	}

		

	
	
}
