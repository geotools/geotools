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

import junit.framework.TestCase;

import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class SetOperationsTest extends TestCase {
	
	public void testMain() {
		
		//FeatGeomFactoryImpl tGeomFactory = FeatGeomFactoryImpl.getDefault2D();
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		
		// Primitive / Primitive - Tests
		this._testCurvePolygon(crs);
		this._testPolygonPolygon(crs);
		this._testCurveCurve(crs);

		// Primitive / Complex - Tests

		// Complex / Complex - Tests

		
		// TODO Testing all combinations of surface / curve / point intersections
		
	}
	
	
	private void _testPolygonPolygon(CoordinateReferenceSystem crs) {

		SurfaceImpl surfaceAwithoutHole = this.createSurfaceAwithoutHole(crs);
		SurfaceImpl surfaceAwithHole = this.createSurfaceAwithHole(crs);
		SurfaceImpl surfaceAwithTwoHoles = this.createSurfaceAwithTwoHoles(crs);
		SurfaceImpl surfaceBwithoutHole = this.createSurfaceBwithoutHole(crs);
		SurfaceImpl surfaceBwithHole = this.createSurfaceBwithHole(crs);
		SurfaceImpl surfaceC = this.createSurfaceC(crs);
		
		SurfaceImpl s1 = null;
		SurfaceImpl s2 = null;
		
		// (S1)
		// A WITHOUT HOLES - B WITHOUT HOLES:
		// INTERSECTION SHELL-A/SHELL-B
		s1 = surfaceAwithoutHole;
		s2 = surfaceBwithoutHole;
		//System.out.println("(S1)");
		this.testAndPrintTest("S1", s1, s2);

		// (S2)
		// A WITH ONE HOLE - B WITHOUT HOLES
		// INTERSECTION SHELL-A/SHELL-B
		s1 = surfaceAwithHole;
		s2 = surfaceBwithoutHole;
		//System.out.println("(S2)");
		this.testAndPrintTest("S2", s1, s2);

		// (S3)
		// A WITHOUT HOLES - B WITH ONE HOLE
		// INTERSECTION SHELL-A/SHELL-B AND SHELL-A/HOLE-B
		s1 = surfaceAwithoutHole;
		s2 = surfaceBwithHole;
		//System.out.println("(S3)");
		this.testAndPrintTest("S3", s1, s2);
		
		// (S4)
		// A WITH ONE HOLE - B WITH ONE HOLE
		// INTERSECTION SHELL-A/SHELL-B AND SHELL-A/HOLE-B
		s1 = surfaceAwithHole;
		s2 = surfaceBwithHole;
		//System.out.println("(S4)");
		this.testAndPrintTest("S4", s1, s2);
		
		// (S5)
		// A WITH TWO HOLES (one in and one outside B) - B WITHOUT HOLES
		// INTERSECTION SHELL-A/SHELL-B
		s1 = surfaceAwithTwoHoles;
		s2 = surfaceBwithoutHole;
		//System.out.println("(S5)");
		this.testAndPrintTest("S5", s1, s2);

		// (S6)
		// A WITH TWO HOLES (one in and one outside B) - B WITH ONE HOLES
		// INTERSECTION SHELL-A/SHELL-B AND SHELL-A/HOLE-B
		s1 = surfaceAwithTwoHoles;
		s2 = surfaceBwithHole;
		//System.out.println("(S6)");
		this.testAndPrintTest("S6", s1, s2);

		// (S7)
		// B WITHOUT HOLES - C withour holes
		// DISJOINT
		s1 = surfaceBwithoutHole;
		s2 = surfaceC;
		//System.out.println("(S7)");
		this.testAndPrintTest("S7", s1, s2);

	}

	private void _testCurvePolygon(CoordinateReferenceSystem crs) {

		SurfaceImpl surfaceAwithoutHole = this.createSurfaceAwithoutHole(crs);
		SurfaceImpl surfaceAwithHole = this.createSurfaceAwithHole(crs);
		SurfaceImpl surfaceAwithTwoHoles = this.createSurfaceAwithTwoHoles(crs);
		SurfaceImpl surfaceBwithoutHole = this.createSurfaceBwithoutHole(crs);
		SurfaceImpl surfaceBwithHole = this.createSurfaceBwithHole(crs);
		SurfaceImpl surfaceC = this.createSurfaceC(crs);
		CurveImpl curveA = this.createCurveA(crs);
		CurveImpl curveF = this.createCurveF(crs);

		GeometryImpl g1 = null;
		GeometryImpl g2 = null;
		
		g1 = surfaceBwithoutHole;
		g2 = curveA;
		//System.out.println("(CS1)");
		this.testAndPrintTest("CS1", g1, g2);

		g1 = surfaceAwithoutHole;
		g2 = curveF;
		//System.out.println("(CS2)");
		this.testAndPrintTest("CS2", g1, g2);

		//GeometryImpl g = this._testIntersection(surface1, curve1);
		//System.out.println("\nIntersection Surface/Curve - Expected result geometry: CURVE(100 105.71, 100 70, 83.22 83.05)");
		//System.out.println("Intersection Surface/Curve - result geometry: " + g);

	}
	
	private void _testCurveCurve(CoordinateReferenceSystem crs) {

		CurveImpl curveA = this.createCurveA(crs);
		CurveImpl curveB = this.createCurveB(crs);
		CurveImpl curveC = this.createCurveC(crs);
		CurveImpl curveD = this.createCurveD(crs);
		CurveImpl curveE = this.createCurveE(crs);
		
		CurveImpl c1 = null;
		CurveImpl c2 = null;
		
		// (C1) - Touch
		c1 = curveA;
		c2 = curveB;
		//System.out.println("(C1)");
		this.testAndPrintTest("C1", c1, c2);

		// (C2) - Cross
		c1 = curveB;
		c2 = curveC;
		//System.out.println("(C2)");
		this.testAndPrintTest("C2", c1, c2);

		// (C3) - Overlap
		c1 = curveB;
		c2 = curveD;
		//System.out.println("(C3)");
		this.testAndPrintTest("C3", c1, c2);

		// (C4) - Equal
		c1 = curveB;
		c2 = curveB;
		//System.out.println("(C4)");
		this.testAndPrintTest("C4", c1, c2);

		
	}
	
	
	
	

	private void testAndPrintTest(String testCaseID, GeometryImpl g1, GeometryImpl g2) {
		
		//System.out.print("\nTestcase : (" + testCaseID + ")");
		//System.out.print("\nGeometry 1 :" + g1);
		//System.out.print("\nGeometry 2 :" + g2 + "\n");

		GeometryImpl g = null;
		
		// INTERSECTION
		g = this._testIntersection(g1, g2);
		//System.out.println("Intersection - result geometry: " + g);

		// UNION
		g = this._testUnion(g1, g2);
		//System.out.println("Union - result geometry: " + g);

		// DIFFERENCE S1-S2
		g = this._testDifference(g1, g2);
		//System.out.println("Difference - result geometry: " + g);

		// DIFFERENCE S2-S1
		g = this._testDifference(g2, g1);
		//System.out.println("Difference - result geometry: " + g);

		// SYMMETRIC DIFFERENCE
		g = this._testSymmetricDifference(g1, g2);
		//System.out.println("SymmetricDifference - result geometry: " + g);
		
	}

	
	
	private CurveImpl createCurveFromWKT(CoordinateReferenceSystem crs, String aWKTcurve) {
		CurveImpl rCurve = null;
		WKTReader wktReader = new WKTReader(crs);
		try {
			rCurve = (CurveImpl) wktReader.read(aWKTcurve);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rCurve;
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
	
	private GeometryImpl _testIntersection(GeometryImpl g1, GeometryImpl g2) {
		return (GeometryImpl) g1.intersection(g2);
	}
	
	private GeometryImpl _testUnion(GeometryImpl g1, GeometryImpl g2) {
		return (GeometryImpl) g1.union(g2);
	}
	
	private GeometryImpl _testDifference(GeometryImpl g1, GeometryImpl g2) {
		return (GeometryImpl) g1.difference(g2);
	}
	
	private GeometryImpl _testSymmetricDifference(GeometryImpl g1, GeometryImpl g2) {
		return (GeometryImpl) g1.symmetricDifference(g2);
	}

	
	private SurfaceImpl createSurfaceAwithoutHole(CoordinateReferenceSystem crs) {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}
	
	private SurfaceImpl createSurfaceAwithHole(CoordinateReferenceSystem crs) {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceAwithTwoHoles(CoordinateReferenceSystem crs) {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60), (30 100, 30 120, 50 120, 50 100, 30 100))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceBwithoutHole(CoordinateReferenceSystem crs) {
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceBwithHole(CoordinateReferenceSystem crs) {
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10), (120 30, 110 50, 120 80, 170 80, 160 40, 120 30))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}
	
	private SurfaceImpl createSurfaceC(CoordinateReferenceSystem crs) {
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((0 50, 50 50, 50 150, 20 140, 0 50))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}
	
	
	private CurveImpl createCurveA(CoordinateReferenceSystem crs) {
		String wktCurve1 = "CURVE(30 20, 10 50, 100 120, 100 70, 10 140)";
		return this.createCurveFromWKT(crs, wktCurve1);
	}
	
	private CurveImpl createCurveB(CoordinateReferenceSystem crs) {
		String wktCurve1 = "CURVE(30 20, 50 20, 80 20)";
		return this.createCurveFromWKT(crs, wktCurve1);
	}
	
	private CurveImpl createCurveC(CoordinateReferenceSystem crs) {
		String wktCurve1 = "CURVE(40 60, 40 30, 40 10)";
		return this.createCurveFromWKT(crs, wktCurve1);
	}

	private CurveImpl createCurveD(CoordinateReferenceSystem crs) {
		String wktCurve1 = "CURVE(70 20, 100 20)";
		return this.createCurveFromWKT(crs, wktCurve1);
	}
	
	private CurveImpl createCurveE(CoordinateReferenceSystem crs) {
		String wktCurve1 = "CURVE(40 40, 40 50)";
		return this.createCurveFromWKT(crs, wktCurve1);
	}

	private CurveImpl createCurveF(CoordinateReferenceSystem crs) {
		String wktCurve1 = "CURVE(80 200, 80 -100)";
		return this.createCurveFromWKT(crs, wktCurve1);
	}

	

}
