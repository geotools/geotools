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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.aggregate.MultiCurveImpl;
import org.geotools.geometry.iso.aggregate.MultiPointImpl;
import org.geotools.geometry.iso.aggregate.MultiSurfaceImpl;
import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

public class ConvexHullTest extends TestCase {

	private GeometryBuilder builder = null;
	private CoordinateReferenceSystem crs = null;

	public void testMain() {
		
		// === 2D ===
		this.builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		this.crs = DefaultGeographicCRS.WGS84;
		
		// Test Points, MultiPoints and CompositePoints
		this._testPoints();

		// Test Curves, CurveBoundaries, MultiCurves
		this._testCurves();
		
		// Test Surfaces, SurfaceBoundaries and MultiSurfaces
		this._testSurfaces();
		
	}
	
	private void _testPoints() {
		
		// Point
		
		Geometry res = this.createPointA().getConvexHull();
		double coord[] = ((PointImpl)res).getPosition().getCoordinates();
		assertTrue(coord[0] == 30);
		assertTrue(coord[1] == 50);

		res = this.createPointB().getConvexHull();
		coord = ((PointImpl)res).getPosition().getCoordinates();
		assertTrue(coord[0] == 100);
		assertTrue(coord[1] == 120);

		// MultiPoint
		res = this.createMultiPointA().getConvexHull();
		//System.out.println(res);
		List<DirectPosition> pos = this.surfaceToPositions((Surface) res);
		assertTrue(pos.get(0).getOrdinate(0) == 70);
		assertTrue(pos.get(0).getOrdinate(1) == 10);
		assertTrue(pos.get(1).getOrdinate(0) == 30);
		assertTrue(pos.get(1).getOrdinate(1) == 50);
		assertTrue(pos.get(2).getOrdinate(0) == 100);
		assertTrue(pos.get(2).getOrdinate(1) == 120);
		assertTrue(pos.get(3).getOrdinate(0) == 170);
		assertTrue(pos.get(3).getOrdinate(1) == 10);
		assertTrue(pos.get(4).getOrdinate(0) == 70);
		assertTrue(pos.get(4).getOrdinate(1) == 10);
		
		// compare multipoint to expected linestring value
		//MULTIPOINT(130 240, 130 240, 130 240, 570 240, 570 240, 570 240, 650 240)
		//LINESTRING(130 240, 650 240)
		Set<Point> points = new HashSet<Point>();
		points.add(this.createPointFromWKT("POINT(130 240)"));
		points.add(this.createPointFromWKT("POINT(130 240)"));
		points.add(this.createPointFromWKT("POINT(130 240)"));
		points.add(this.createPointFromWKT("POINT(570 240)"));
		points.add(this.createPointFromWKT("POINT(570 240)"));
		points.add(this.createPointFromWKT("POINT(570 240)"));
		points.add(this.createPointFromWKT("POINT(650 240)"));
		MultiPointImpl mp = (MultiPointImpl) this.builder.getAggregateFactory().createMultiPoint(points);
		
		CurveImpl line = this.createCurveFromWKT("CURVE(130 240, 650 240)");
		
		CurveImpl ch = (CurveImpl) mp.getConvexHull();
		//System.out.println(ch.equals(line));
		//System.out.println(line.equals(mp.getConvexHull()));
		//System.out.println(mp.getConvexHull().equals(line));

		assertTrue(mp.getConvexHull().equals(line));
		assertEquals(mp.getConvexHull(),line);
	}
	
	private void _testCurves() {

		Geometry res = this.createCurveA().getConvexHull();
		List<DirectPosition> dpos = this.surfaceToPositions((Surface) res);
		assertTrue(dpos.get(0).getOrdinate(0) == 30);
		assertTrue(dpos.get(0).getOrdinate(1) == 20);
		assertTrue(dpos.get(1).getOrdinate(0) == 10);
		assertTrue(dpos.get(1).getOrdinate(1) == 50);
		assertTrue(dpos.get(2).getOrdinate(0) == 10);
		assertTrue(dpos.get(2).getOrdinate(1) == 140);
		assertTrue(dpos.get(3).getOrdinate(0) == 100);
		assertTrue(dpos.get(3).getOrdinate(1) == 120);
		assertTrue(dpos.get(4).getOrdinate(0) == 100);
		assertTrue(dpos.get(4).getOrdinate(1) == 70);
		assertTrue(dpos.get(5).getOrdinate(0) == 30);
		assertTrue(dpos.get(5).getOrdinate(1) == 20);
		
		Curve c = (Curve) this.createCurveA().getBoundary().getConvexHull();
		dpos = this.curveToPositions(c);
		assertTrue(dpos.size() == 2);
		assertTrue(dpos.get(0).getOrdinate(0) == 30);
		assertTrue(dpos.get(0).getOrdinate(1) == 20);
		assertTrue(dpos.get(1).getOrdinate(0) == 10);
		assertTrue(dpos.get(1).getOrdinate(1) == 140);
		
		res = this.createMultiCurveA().getConvexHull();
		//System.out.println(res);
		dpos = this.surfaceToPositions((Surface) res);
		assertTrue(dpos.get(0).getOrdinate(0) == 40);
		assertTrue(dpos.get(0).getOrdinate(1) == 10);
		assertTrue(dpos.get(1).getOrdinate(0) == 30);
		assertTrue(dpos.get(1).getOrdinate(1) == 20);
		assertTrue(dpos.get(2).getOrdinate(0) == 10);
		assertTrue(dpos.get(2).getOrdinate(1) == 50);
		assertTrue(dpos.get(3).getOrdinate(0) == 10);
		assertTrue(dpos.get(3).getOrdinate(1) == 140);
		assertTrue(dpos.get(4).getOrdinate(0) == 100);
		assertTrue(dpos.get(4).getOrdinate(1) == 120);
		assertTrue(dpos.get(5).getOrdinate(0) == 100);
		assertTrue(dpos.get(5).getOrdinate(1) == 20);
		assertTrue(dpos.get(6).getOrdinate(0) == 40);
		assertTrue(dpos.get(6).getOrdinate(1) == 10);
		
	}
	
	
	private void _testSurfaces() {
		
		Geometry res = this.createSurfaceAwithTwoHoles().getConvexHull();
		List<DirectPosition> pos = this.surfaceToPositions((Surface) res);
		assertTrue(pos.get(0).getOrdinate(0) == 70);
		assertTrue(pos.get(0).getOrdinate(1) == 30);
		assertTrue(pos.get(1).getOrdinate(0) == 30);
		assertTrue(pos.get(1).getOrdinate(1) == 50);
		assertTrue(pos.get(2).getOrdinate(0) == 10);
		assertTrue(pos.get(2).getOrdinate(1) == 90);
		assertTrue(pos.get(3).getOrdinate(0) == 30);
		assertTrue(pos.get(3).getOrdinate(1) == 140);
		assertTrue(pos.get(4).getOrdinate(0) == 100);
		assertTrue(pos.get(4).getOrdinate(1) == 150);
		assertTrue(pos.get(5).getOrdinate(0) == 150);
		assertTrue(pos.get(5).getOrdinate(1) == 120);
		assertTrue(pos.get(6).getOrdinate(0) == 150);
		assertTrue(pos.get(6).getOrdinate(1) == 70);
		assertTrue(pos.get(7).getOrdinate(0) == 120);
		assertTrue(pos.get(7).getOrdinate(1) == 40);
		assertTrue(pos.get(8).getOrdinate(0) == 70);
		assertTrue(pos.get(8).getOrdinate(1) == 30);

		res = this.createSurfaceBwithoutHole().getConvexHull();
		//System.out.println(res);
		pos = this.surfaceToPositions((Surface) res);
		assertTrue(pos.get(0).getOrdinate(0) == 100);
		assertTrue(pos.get(0).getOrdinate(1) == 10);
		assertTrue(pos.get(1).getOrdinate(0) == 70);
		assertTrue(pos.get(1).getOrdinate(1) == 50);
		assertTrue(pos.get(2).getOrdinate(0) == 90);
		assertTrue(pos.get(2).getOrdinate(1) == 100);
		assertTrue(pos.get(3).getOrdinate(0) == 160);
		assertTrue(pos.get(3).getOrdinate(1) == 140);
		assertTrue(pos.get(4).getOrdinate(0) == 200);
		assertTrue(pos.get(4).getOrdinate(1) == 90);
		assertTrue(pos.get(5).getOrdinate(0) == 170);
		assertTrue(pos.get(5).getOrdinate(1) == 20);
		assertTrue(pos.get(6).getOrdinate(0) == 100);
		assertTrue(pos.get(6).getOrdinate(1) == 10);

		res = this.createSurfaceConcaveX().getConvexHull();
		//System.out.println(res);
		pos = this.surfaceToPositions((Surface) res);
		assertTrue(pos.get(0).getOrdinate(0) == 100);
		assertTrue(pos.get(0).getOrdinate(1) == 10);
		assertTrue(pos.get(1).getOrdinate(0) == 70);
		assertTrue(pos.get(1).getOrdinate(1) == 50);
		assertTrue(pos.get(2).getOrdinate(0) == 90);
		assertTrue(pos.get(2).getOrdinate(1) == 100);
		assertTrue(pos.get(3).getOrdinate(0) == 160);
		assertTrue(pos.get(3).getOrdinate(1) == 140);
		assertTrue(pos.get(4).getOrdinate(0) == 200);
		assertTrue(pos.get(4).getOrdinate(1) == 90);
		assertTrue(pos.get(5).getOrdinate(0) == 170);
		assertTrue(pos.get(5).getOrdinate(1) == 20);
		assertTrue(pos.get(6).getOrdinate(0) == 130);
		assertTrue(pos.get(6).getOrdinate(1) == 10);
		assertTrue(pos.get(7).getOrdinate(0) == 100);
		assertTrue(pos.get(7).getOrdinate(1) == 10);

		res = this.createSurfaceConcaveX().getBoundary().getConvexHull();
		//System.out.println(res);
		pos = this.surfaceToPositions((Surface) res);
		assertTrue(pos.get(0).getOrdinate(0) == 100);
		assertTrue(pos.get(0).getOrdinate(1) == 10);
		assertTrue(pos.get(1).getOrdinate(0) == 70);
		assertTrue(pos.get(1).getOrdinate(1) == 50);
		assertTrue(pos.get(2).getOrdinate(0) == 90);
		assertTrue(pos.get(2).getOrdinate(1) == 100);
		assertTrue(pos.get(3).getOrdinate(0) == 160);
		assertTrue(pos.get(3).getOrdinate(1) == 140);
		assertTrue(pos.get(4).getOrdinate(0) == 200);
		assertTrue(pos.get(4).getOrdinate(1) == 90);
		assertTrue(pos.get(5).getOrdinate(0) == 170);
		assertTrue(pos.get(5).getOrdinate(1) == 20);
		assertTrue(pos.get(6).getOrdinate(0) == 130);
		assertTrue(pos.get(6).getOrdinate(1) == 10);
		assertTrue(pos.get(7).getOrdinate(0) == 100);
		assertTrue(pos.get(7).getOrdinate(1) == 10);

		res = this.createMultiSurfaceA().getConvexHull();
		//System.out.println(res);
		pos = this.surfaceToPositions((Surface) res);
		assertTrue(pos.get(0).getOrdinate(0) == 100);
		assertTrue(pos.get(0).getOrdinate(1) == 10);
		assertTrue(pos.get(1).getOrdinate(0) == 30);
		assertTrue(pos.get(1).getOrdinate(1) == 50);
		assertTrue(pos.get(2).getOrdinate(0) == 10);
		assertTrue(pos.get(2).getOrdinate(1) == 90);
		assertTrue(pos.get(3).getOrdinate(0) == 30);
		assertTrue(pos.get(3).getOrdinate(1) == 140);
		assertTrue(pos.get(4).getOrdinate(0) == 100);
		assertTrue(pos.get(4).getOrdinate(1) == 150);
		assertTrue(pos.get(5).getOrdinate(0) == 160);
		assertTrue(pos.get(5).getOrdinate(1) == 140);
		assertTrue(pos.get(6).getOrdinate(0) == 200);
		assertTrue(pos.get(6).getOrdinate(1) == 90);
		assertTrue(pos.get(7).getOrdinate(0) == 170);
		assertTrue(pos.get(7).getOrdinate(1) == 20);
		assertTrue(pos.get(8).getOrdinate(0) == 100);
		assertTrue(pos.get(8).getOrdinate(1) == 10);
		
	}
	
	private List<DirectPosition> surfaceToPositions(Surface s) {
		List<? extends DirectPosition> pos;
		Ring ext = ((SurfaceBoundary)s.getBoundary()).getExterior();
		pos = ((RingImplUnsafe)ext).asDirectPositions();
		return (List<DirectPosition>) pos;		
	}
	
	private List<DirectPosition> curveToPositions(Curve c) {
		List<? extends DirectPosition> pos;
		pos = ((CurveImpl)c).asDirectPositions();
		return (List<DirectPosition>) pos;		
	}

	
	private PointImpl createPointFromWKT(String aWKTpoint) {
		PointImpl rPoint = null;
		WKTReader wktReader = new WKTReader(this.crs);
		try {
			rPoint = (PointImpl) wktReader.read(aWKTpoint);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rPoint;
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
	
	private SurfaceImpl createSurfaceFromWKT(String aWKTsurface) {
		SurfaceImpl rSurface = null;
		WKTReader wktReader = new WKTReader(this.crs);
		try {
			rSurface = (SurfaceImpl) wktReader.read(aWKTsurface);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rSurface;
	}

	
	
	
	
	private PointImpl createPointA() {
		String wktPoint = "POINT(30 50)";
		return this.createPointFromWKT(wktPoint);
	}

	private PointImpl createPointB() {
		String wktPoint = "POINT(100 120)";
		return this.createPointFromWKT(wktPoint);
	}
	
	private PointImpl createPointC() {
		String wktPoint = "POINT(70 20)";
		return this.createPointFromWKT(wktPoint);
	}

	private PointImpl createPointD() {
		String wktPoint = "POINT(70 10)";
		return this.createPointFromWKT(wktPoint);
	}

	private PointImpl createPointX() {
		String wktPoint = "POINT(170 10)";
		return this.createPointFromWKT(wktPoint);
	}

	
	private MultiPointImpl createMultiPointA() {
		Set<Point> points = new HashSet<Point>();
		points.add(this.createPointA());
		points.add(this.createPointB());
		points.add(this.createPointC());
		points.add(this.createPointD());
		points.add(this.createPointX());
		return (MultiPointImpl) this.builder.getAggregateFactory().createMultiPoint(points);
	}


	
	private CurveImpl createCurveA() {
		String wktCurve1 = "CURVE(30 20, 10 50, 100 120, 100 70, 10 140)";
		return this.createCurveFromWKT(wktCurve1);
	}
	
	private CurveImpl createCurveB() {
		String wktCurve1 = "CURVE(30 20, 50 20, 80 20)";
		return this.createCurveFromWKT(wktCurve1);
	}
	
	private CurveImpl createCurveC() {
		String wktCurve1 = "CURVE(40 60, 40 30, 40 10)";
		return this.createCurveFromWKT(wktCurve1);
	}

	private CurveImpl createCurveD() {
		String wktCurve1 = "CURVE(70 20, 100 20, 100 50)";
		return this.createCurveFromWKT(wktCurve1);
	}


	private MultiCurveImpl createMultiCurveA() {
		Set<OrientableCurve> curves = new HashSet<OrientableCurve>();
		curves.add(this.createCurveA());
		curves.add(this.createCurveB());
		curves.add(this.createCurveC());
		curves.add(this.createCurveD());
		return (MultiCurveImpl) this.builder.getAggregateFactory().createMultiCurve(curves);
	}


	private SurfaceImpl createSurfaceAwithTwoHoles() {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60), (30 100, 30 120, 50 120, 50 100, 30 100))";
		return this.createSurfaceFromWKT(wktSurface1);
	}

	private SurfaceImpl createSurfaceBwithoutHole() {
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10))";
		return this.createSurfaceFromWKT(wktSurface1);
	}

	private SurfaceImpl createSurfaceConcaveX() {
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 130 50, 110 100, 160 140, 200 90, 170 20, 150 30, 130 10 ,100 10))";
		return this.createSurfaceFromWKT(wktSurface1);
	}
	
	private MultiSurfaceImpl createMultiSurfaceA() {
		Set<OrientableSurface> surfaces = new HashSet<OrientableSurface>();
		surfaces.add(this.createSurfaceAwithTwoHoles());
		surfaces.add(this.createSurfaceBwithoutHole());
		return (MultiSurfaceImpl) this.builder.getAggregateFactory().createMultiSurface(surfaces);
	}

	
	
	
	
	
}
