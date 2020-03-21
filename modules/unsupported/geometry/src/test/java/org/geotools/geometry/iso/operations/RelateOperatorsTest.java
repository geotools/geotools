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
import java.util.List;
import junit.framework.TestCase;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.complex.CompositePointImpl;
import org.geotools.geometry.iso.complex.CompositeSurfaceImpl;
import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class RelateOperatorsTest extends TestCase {

    private static final boolean F = false;
    private static final boolean T = true;

    private GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
    private CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

    private PointImpl pointA = this.createPointA();
    private PointImpl pointB = this.createPointB();
    private PointImpl pointC = this.createPointC();
    private PointImpl pointD = this.createPointD();

    private CurveImpl curveA = this.createCurveA();
    private CurveImpl curveB = this.createCurveB();
    private CurveImpl curveC = this.createCurveC();
    private CurveImpl curveD = this.createCurveD();
    private CurveImpl curveE = this.createCurveE();
    private CurveImpl curveF = this.createCurveF();
    private CurveImpl curveG = this.createCurveG();
    private CurveImpl curveH = this.createCurveH();
    private CurveImpl curveI = this.createCurveI();

    private SurfaceImpl surfaceAwithoutHole = this.createSurfaceAwithoutHole();
    private SurfaceImpl surfaceAwithHole = this.createSurfaceAwithHole();
    private SurfaceImpl surfaceAwithTwoHoles = this.createSurfaceAwithTwoHoles();
    private SurfaceImpl surfaceBwithoutHole = this.createSurfaceBwithoutHole();
    private SurfaceImpl surfaceBwithHole = this.createSurfaceBwithHole();
    private SurfaceImpl surfaceC = this.createSmallSurfaceCWithoutHoles();
    private SurfaceImpl surfaceD = this.createSmallSurfaceDWithoutHoles();
    private SurfaceImpl surfaceE = this.createSmallSurfaceEWithoutHoles();

    private CompositeSurfaceImpl compSurfAwithoutHole = this.createCompositeSurfaceAwithoutHole();
    private CompositeSurfaceImpl compSurfBwithoutHole = this.createCompositeSurfaceBwithoutHole();

    private CompositePointImpl compPointA = this.createCompositePointA();
    private CompositePointImpl compPointB = this.createCompositePointB();

    public void testMain() {

        // Primitive / Primitive - Tests
        this._testCurveCurve();
        this._testSurfaceSurface();
        this._testSurfaceCurve();
        this._testPointGeometry();

        // Primitive / Complex - Tests

        this._testPointCompositePoint();

        // Complex / Complex - Tests
        this._testCompositeSurfaceCompositeSurface();
        this._testCompositePointCompositePoint();
    }

    private void _testCurveCurve() {

        //		Parameters:
        //		1) shouldBeEqual,
        //		2) shouldIntersectAndNotBeDisjoint,
        //		3) shouldOverlap,
        //		4) shouldTouch,
        //		5) shouldContain,
        //		6) shouldbeWithin,
        //		7) shouldCover,
        //		8) shouldBeCoveredBy

        // (C1)
        // Intersection
        this.testAndAssertTest("C1", curveB, curveC, F, T, F, F, F, F, T);

        // (C2)
        // Disjoint
        this.testAndAssertTest("C2", curveB, curveE, F, F, F, F, F, F, F);

        // (C3)
        // Overlap
        this.testAndAssertTest("C3", curveB, curveD, F, T, T, F, F, F, F);

        // (C4)
        // Contains
        this.testAndAssertTest("C4", curveC, curveE, F, T, F, F, T, F, F);

        // (C5)
        // Within
        this.testAndAssertTest("C5", curveE, curveC, F, T, F, F, F, T, F);

        // (C6)
        // Touch
        // Boundary Intersection
        this.testAndAssertTest("C6", curveA, curveB, F, T, F, T, F, F, F);

        // (C7)
        // Equals (= within and contains)
        this.testAndAssertTest("C7", curveA, curveA, T, T, F, F, T, T, F);

        // (C8)
        // Touch
        // Interior with Boundary
        // but no Intersect
        this.testAndAssertTest("C8", curveA, curveH, F, T, F, T, F, F, F);
    }

    /** SURFACE / SURFACE TESTS */
    private void _testSurfaceSurface() {

        //		Parameters:
        //		1) shouldBeEqual,
        //		2) shouldIntersectAndNotBeDisjoint,
        //		3) shouldOverlap,
        //		4) shouldTouch,
        //		5) shouldContain,
        //		6) shouldbeWithin,
        //		7) shouldCover,
        //		8) shouldBeCoveredBy

        // (P1)
        // Overlap
        // Simple Overlap Intersection
        this.testAndAssertTest("P1", surfaceAwithoutHole, surfaceBwithoutHole, F, T, T, F, F, F, F);

        // (P2)
        // Overlap
        // Simple Overlap Intersection with holes
        this.testAndAssertTest("P2", surfaceAwithHole, surfaceBwithHole, F, T, T, F, F, F, F);

        // (P3)
        // Not touches
        // Polygon Boundary intersects Polygon Boundary: Point-Edge
        this.testAndAssertTest("P3", surfaceAwithoutHole, surfaceC, F, T, F, T, F, F, F);

        // (P4)
        // Not touches
        // Polygon Boundary intersects Boundary Polygon in Edge (Edge-Edge Touch)
        this.testAndAssertTest("P4", surfaceAwithoutHole, surfaceD, F, T, F, T, F, F, F);

        // (P5)
        // Contains
        // Polygon E within Polygon A
        // E touching A in Corner (point-point) (from inner side)
        this.testAndAssertTest("P5", surfaceAwithoutHole, surfaceE, F, T, F, F, T, F, F);

        // (P6)
        // Contains
        // Polygon C within Polygon B, without touching
        this.testAndAssertTest("P6", surfaceBwithoutHole, surfaceC, F, T, F, F, T, F, F);

        // (P7)
        // Contains
        // Polygon E within Polygon A with 2 holes, touching hole
        this.testAndAssertTest("P7", surfaceAwithTwoHoles, surfaceE, F, T, F, F, T, F, F);

        // (P8)
        // Overlap
        // Polygon D within Polygon B with 1 hole, overlapping the hole
        this.testAndAssertTest("P8", surfaceBwithHole, surfaceD, F, T, T, F, F, F, F);

        // (P9)
        // Equal (=within and contains)
        this.testAndAssertTest("P9", surfaceBwithHole, surfaceBwithHole, T, T, F, F, T, T, F);
    }

    /** SURFACE / CURVE TESTS */
    public void _testSurfaceCurve() {

        SurfaceImpl surfaceA = this.createSurfaceAwithoutHole();

        //		Parameters:
        //		1) shouldBeEqual,
        //		2) shouldIntersectAndNotBeDisjoint,
        //		3) shouldOverlap,
        //		4) shouldTouch,
        //		5) shouldContain,
        //		6) shouldbeWithin,
        //		7) shouldCross

        // (PC1)
        // Crosses Intersection
        this.testAndAssertTest("PC1", surfaceA, curveA, F, T, F, F, F, F, T);

        // (PC2)
        // Disjoint
        this.testAndAssertTest("PC2", surfaceA, curveB, F, F, F, F, F, F, F);

        // (PC3)
        // Touches
        this.testAndAssertTest("PC3", surfaceA, curveF, F, T, F, T, F, F, F);

        // (PC4)
        // Contains
        this.testAndAssertTest("PC4", surfaceA, curveG, F, T, F, F, T, F, T);

        // (PC5)
        // Touch: Interior with Boundary
        this.testAndAssertTest("PC5", surfaceA, curveI, F, T, F, T, F, F, F);
    }

    /** POINT / PRIMITIVE TESTS */
    private void _testPointGeometry() {

        // (PT1)
        // Equals Point-Point
        this.testAndAssertTest("PT1", pointA, pointA, T, T, F, F, T, T, F);

        // (PT2)
        // Disjoint Point-Point
        this.testAndAssertTest("PT2", pointA, pointB, F, F, F, F, F, F, F);

        // (PT3)
        // Disjoint Point-Curve
        this.testAndAssertTest("PT3", pointA, curveF, F, F, F, F, F, F, F);

        // (PT4)
        // Disjoint Point-Surface
        this.testAndAssertTest("PT4", pointC, surfaceAwithoutHole, F, F, F, F, F, F, F);

        // (PT5)
        // Point in Curve, within
        this.testAndAssertTest("PT5", pointC, curveF, F, T, F, F, F, T, F);

        // (PT6)
        // Point in Curve, Contains, no Touches
        this.testAndAssertTest("PT6", curveF, pointC, F, T, F, F, T, F, T);

        // (PT7)
        // Point touches Curve
        // Point intersects Curve Boundary
        this.testAndAssertTest("PT7", curveF, pointD, F, T, F, T, F, F, F);

        // (PT8)
        // Point in Surface, within
        this.testAndAssertTest("PT8", pointB, surfaceAwithoutHole, F, T, F, F, F, T, F);

        // (PT9)
        // Point in Surface, contains
        this.testAndAssertTest("PT9", surfaceAwithoutHole, pointB, F, T, F, F, T, F, T);

        // (PT10)
        // Point touches Surface
        // Point touches Surface Boundary
        this.testAndAssertTest("PT10", surfaceAwithoutHole, pointA, F, T, F, T, F, F, F);
    }

    /** POINT / COMPOSITEPOINT TESTS */
    private void _testPointCompositePoint() {

        // Disjoint
        this.testAndAssertTest("PCP1", pointA, compPointB, F, F, F, F, F, F, F);

        // Equal
        this.testAndAssertTest("PCP2", pointA, compPointA, T, T, F, F, T, T, F);
    }

    /** COMPOSITESURFACE / COMPOSITESURFACE TESTS */
    private void _testCompositeSurfaceCompositeSurface() {

        // (CSCS1) wie (P1)
        this.testAndAssertTest(
                "CSCS1", compSurfAwithoutHole, compSurfBwithoutHole, F, T, T, F, F, F, F);
    }

    /** COMPOSITEPOINT / COMPOSITEPOINT TESTS */
    private void _testCompositePointCompositePoint() {

        //		Parameters:
        //		1) shouldBeEqual,
        //		2) shouldIntersectAndNotBeDisjoint,
        //		3) shouldOverlap,
        //		4) shouldTouch,
        //		5) shouldContain,
        //		6) shouldbeWithin,
        //		7) shouldCross,

        // Disjoint
        this.testAndAssertTest("CPCP1", compPointA, compPointB, F, F, F, F, F, F, F);

        // Equal
        this.testAndAssertTest("CPCP2", compPointA, compPointA, T, T, F, F, T, T, F);
    }

    //    private void pointLinePolygonTest() {
    //        Geometry pt = parser.read("POINT(1 1)");
    //        Geometry ls = parser.read("LINESTRING(1 1, 1 2)");
    //        Geometry po = parser.read("POLYGON((1 1, 1 2, 2 2, 1 1))");
    //
    //        test(pt, ls);
    //        test(pt, po);
    //        test(ls, po);
    //    }
    //
    //    private static void test(Geometry p, Geometry ls) {
    //        System.out.println(ls.contains(p) + " " + ls.covers(p) + " " + p.within(ls) + " " +
    // p.coveredBy(ls));
    //    }

    /**
     * Tests a topologic relation between geometry objects and asserts the results according to the
     * given should-values in the parameter list.
     *
     * @param g1 First Geometry
     * @param g2 Second Geometry
     */
    private void testAndAssertTest(
            String testCaseID,
            GeometryImpl g1,
            GeometryImpl g2,
            boolean shouldBeEqual,
            boolean shouldIntersectAndNotBeDisjoint,
            boolean shouldOverlap,
            boolean shouldTouch,
            boolean shouldContain,
            boolean shouldbeWithin,
            boolean shouldCross) {

        // System.out.print("\nTestcase : (" + testCaseID + ")");
        // System.out.print("\nGeometry 1 :" + g1);
        // System.out.print("\nGeometry 2 :" + g2 + "\n");

        boolean result = false;

        // EQUALS
        result = g1.equals(g2);
        // System.out.println("Equals - result: " + result);
        assertTrue(shouldBeEqual == result);

        // INTERSECTION
        result = g1.intersects(g2);
        // System.out.println("Intersection - result: " + result);
        assertTrue(shouldIntersectAndNotBeDisjoint == result);

        // DISJOINT
        result = g1.disjoint(g2);
        // System.out.println("Disjoint - result: " + result);
        assertTrue(!shouldIntersectAndNotBeDisjoint == result);

        // OVERLAPS
        result = g1.overlaps(g2);
        // System.out.println("Overlaps - result: " + result);
        assertTrue(shouldOverlap == result);

        // TOUCHES
        result = g1.touches(g2);
        // System.out.println("Touches - result: " + result);
        assertTrue(shouldTouch == result);

        // CONTAINS
        result = g1.contains(g2);
        // System.out.println("Contains - result: " + result);
        assertTrue(shouldContain == result);

        // WITHIN
        result = g1.within(g2);
        // System.out.println("Within - result: " + result);
        assertTrue(shouldbeWithin == result);

        // CROSSES
        result = g1.crosses(g2);
        // System.out.println("Crosses - result: " + result);
        assertTrue(shouldCross == result);

        //		// COVERS
        //		result  = g1.covers(g2);
        //		//System.out.println("Covers - result: " + result);
        //		assertTrue(shouldContain == result);
        //
        //		// COVEREDBY
        //		result = g1.coveredBy(g2);
        //		//System.out.println("CoveredBy - result: " + result);
        //		assertTrue(shouldBeCoveredBy == result);

    }

    private PointImpl createPointFromWKT(String aWKTpoint) {
        PointImpl rPoint = null;
        WKTReader wktReader = new WKTReader(this.crs);
        try {
            rPoint = (PointImpl) wktReader.read(aWKTpoint);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        return rPoint;
    }

    private CurveImpl createCurveFromWKT(String aWKTcurve) {
        CurveImpl rCurve = null;
        WKTReader wktReader = new WKTReader(this.crs);
        try {
            rCurve = (CurveImpl) wktReader.read(aWKTcurve);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        return rCurve;
    }

    private SurfaceImpl createSurfaceFromWKT(String aWKTsurface) {
        SurfaceImpl rSurface = null;
        WKTReader wktReader = new WKTReader(this.crs);
        try {
            rSurface = (SurfaceImpl) wktReader.read(aWKTsurface);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        return rSurface;
    }

    private CompositePointImpl createCompositePointFromWKT(String aWKTpoint) {
        PointImpl point = this.createPointFromWKT(aWKTpoint);
        return (CompositePointImpl) this.builder.getComplexFactory().createCompositePoint(point);
    }

    private CompositeSurfaceImpl createCompositeSurfaceFromWKT(String aWKTsurface) {
        SurfaceImpl surf = this.createSurfaceFromWKT(aWKTsurface);
        List<OrientableSurface> surfList = new ArrayList<OrientableSurface>();
        surfList.add(surf);
        return (CompositeSurfaceImpl)
                this.builder.getComplexFactory().createCompositeSurface(surfList);
    }

    // ==========================================
    // ==========================================
    //    METHODS TO CREATE GEOMETRY INSTANCES
    // ==========================================
    // ==========================================

    private SurfaceImpl createSurfaceAwithoutHole() {
        String wktSurface1 =
                "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    private SurfaceImpl createSurfaceAwithHole() {
        String wktSurface1 =
                "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    private SurfaceImpl createSurfaceAwithTwoHoles() {
        String wktSurface1 =
                "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60), (30 100, 30 120, 50 120, 50 100, 30 100))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    private SurfaceImpl createSurfaceBwithoutHole() {
        // Clockwise oriented
        String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    private SurfaceImpl createSurfaceBwithHole() {
        // Clockwise oriented
        String wktSurface1 =
                "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10), (120 30, 110 50, 120 80, 170 80, 160 40, 120 30))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    // Within B
    // Touching A in Point
    private SurfaceImpl createSmallSurfaceCWithoutHoles() {
        String wktSurface1 = "SURFACE ((150 100, 160 90, 180 100, 170 120, 150 100))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    // Overlap with B
    // Touching A in LineSegment
    private SurfaceImpl createSmallSurfaceDWithoutHoles() {
        String wktSurface1 = "SURFACE ((150 120, 150 70, 180 70, 160 120, 150 120))";
        return this.createSurfaceFromWKT(wktSurface1);
    }

    // Within A, touching A in corner
    // Touching A (with 2 holes) in edge of second hole
    private SurfaceImpl createSmallSurfaceEWithoutHoles() {
        String wktSurface1 = "SURFACE ((30 140, 40 120, 60 130, 60 140, 30 140))";
        return this.createSurfaceFromWKT(wktSurface1);
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
        String wktCurve1 = "CURVE(70 20, 100 20)";
        return this.createCurveFromWKT(wktCurve1);
    }

    private CurveImpl createCurveE() {
        String wktCurve1 = "CURVE(40 40, 40 50)";
        return this.createCurveFromWKT(wktCurve1);
    }

    private CurveImpl createCurveF() {
        String wktCurve1 = "CURVE(70 10, 70 30)";
        return this.createCurveFromWKT(wktCurve1);
    }

    private CurveImpl createCurveG() {
        String wktCurve1 = "CURVE(50 70, 60 60)";
        return this.createCurveFromWKT(wktCurve1);
    }

    private CurveImpl createCurveH() {
        String wktCurve1 = "CURVE(30 0, 30 30)";
        return this.createCurveFromWKT(wktCurve1);
    }

    private CurveImpl createCurveI() {
        String wktCurve1 = "CURVE(10 50, 10 100)";
        return this.createCurveFromWKT(wktCurve1);
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

    private CompositePointImpl createCompositePointA() {
        String wktPoint = "POINT (30 50)";
        return this.createCompositePointFromWKT(wktPoint);
    }

    private CompositePointImpl createCompositePointB() {
        String wktPoint = "POINT (100 120)";
        return this.createCompositePointFromWKT(wktPoint);
    }

    private CompositeSurfaceImpl createCompositeSurfaceAwithoutHole() {
        String wktSurface =
                "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90))";
        return this.createCompositeSurfaceFromWKT(wktSurface);
    }

    private CompositeSurfaceImpl createCompositeSurfaceBwithoutHole() {
        String wktSurface = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10))";
        return this.createCompositeSurfaceFromWKT(wktSurface);
    }
}
