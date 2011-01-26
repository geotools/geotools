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
package org.geotools.geometry.iso.operations;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class IsSimpleOperationTest extends TestCase {

	private GeometryBuilder builder = null;
	private CoordinateReferenceSystem crs;

	public void testMain() {
		
		this.builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		this.crs = DefaultGeographicCRS.WGS84;
		
		// Test Curves
		this._testCurves();
		
		// Test Surfaces
		this._testSurfaces();
	}
	
	private void _testCurves() {
		
		// (c1)
		// Curve is not simple, cause it has self-intersections
		CurveImpl curve = this.createCurveA(this.builder);
		assertTrue(!curve.isSimple());

		// (c2)
		// Curve is simple, cause it has no self-intersections
		curve = this.createCurveB(this.builder);
		assertTrue(curve.isSimple());
		
		// (c3)
		// Closed Intersecting Curve - not simple
		curve = this.createCurveClosedIntersection();
		assertTrue(!curve.isSimple());

		// (c4)
		// Closed Curve - simple
		curve = this.createCurveClosed();
		assertTrue(curve.isSimple());

		// (c5)
		// Curve which touches itself in a vertex (vertex-vertex intersection) - not simple
		curve = this.createCurveTouchesInVertex();
		assertTrue(!curve.isSimple());

		// (c6)
		// Curve which touches itself in an edge (vertex-edge intersection) - not simple
		curve = this.createCurveTouchesInEdge();
		assertTrue(!curve.isSimple());

	}
	
	private void _testSurfaces() {

		// Surface with hole that does not touch the surface shell - is simple
		SurfaceImpl surface1 = this.createSurfaceAHoleNotTouchesShell(this.builder);
		assertTrue(surface1.isSimple());

		// Surface with hole that touches the surface shell - is NOT simple
		SurfaceImpl surface2 = this.createSurfaceAHoleTouchesShell(this.builder);
		assertTrue(!surface2.isSimple());
	}



	private CurveImpl createCurveA(GeometryBuilder builder) {

		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		// Self-Intersecting Curve
		// CURVE(30 20, 10 50, 100 120, 100 70, 10 140)
		ArrayList<Position> line1 = new ArrayList<Position>();
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30, 20})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{10, 50})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 120})));
		ArrayList<Position> line2 = new ArrayList<Position>();
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 120})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 70})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{10, 140})));

		/* Setting up Array of these LineStrings */
		ArrayList<CurveSegment> tLineList1 = new ArrayList<CurveSegment>();
		tLineList1.add(tCoordFactory.createLineString(line1));
		tLineList1.add(tCoordFactory.createLineString(line2)); 

		/* Build Curve */
		return tPrimFactory.createCurve(tLineList1);
		
	}

	private CurveImpl createCurveB(GeometryBuilder builder) {

		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		// Non-Self-Intersecting Curve
		// CURVE(30 20, 10 50, 100 70, 100 120, 10 140)
		ArrayList<Position> line1 = new ArrayList<Position>();
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30, 20})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{10, 50})));
		line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 70})));
		ArrayList<Position> line2 = new ArrayList<Position>();
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 70})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 120})));
		line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{10, 140})));

		/* Setting up Array of these LineStrings */
		ArrayList<CurveSegment> tLineList1 = new ArrayList<CurveSegment>();
		tLineList1.add(tCoordFactory.createLineString(line1));
		tLineList1.add(tCoordFactory.createLineString(line2)); 

		/* Build Curve */
		return tPrimFactory.createCurve(tLineList1);
		
	}
	
	private SurfaceImpl createSurfaceFromWKT(CoordinateReferenceSystem crs, String aWKTsurface) {
		SurfaceImpl rSurface = null;
		WKTReader wktReader = new WKTReader(crs);
		try {
			rSurface = (SurfaceImpl) wktReader.read(aWKTsurface);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rSurface;
	}
	
	private CurveImpl createCurveFromWKT(String aWKTcurve) {
		CurveImpl rCurve = null;
		WKTReader wktReader = new WKTReader(this.crs);
		try {
			rCurve = (CurveImpl) wktReader.read(aWKTcurve);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rCurve;
	}
	
	private CurveImpl createCurveClosedIntersection() {
		String wktCurve1 = "CURVE(150.0 100.0, 160.0 140.0, 180.0 100.0, 170.0 120.0, 150.0 100.0)";
		return this.createCurveFromWKT(wktCurve1);
	}
	
	private CurveImpl createCurveClosed() {
		String wktCurve1 = "CURVE(20.0 10.0, 40.0 10.0, 50.0 40.0, 30.0 50.0, 10.0 30.0, 20.0 10.0)";
		return this.createCurveFromWKT(wktCurve1);
	}
	

	private CurveImpl createCurveTouchesInEdge() {
		String wktCurve1 = "CURVE(150.0 100.0, 200.0 100.0, 180.0 130.0, 180.0 100.0)";
		return this.createCurveFromWKT(wktCurve1);
	}

	private CurveImpl createCurveTouchesInVertex() {
		String wktCurve1 = "CURVE(150.0 100.0, 180.0 100.0, 200.0 100.0, 180.0 130.0, 180.0 100.0)";
		return this.createCurveFromWKT(wktCurve1);
	}

	private SurfaceImpl createSurfaceAHoleNotTouchesShell(GeometryBuilder builder) {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceAHoleTouchesShell(GeometryBuilder builder) {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (30 140, 60 140, 60 130, 40 120, 30 140))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}
	

}
