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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

public class TransformTest extends TestCase {

    private CoordinateReferenceSystem crs1;
    private CoordinateReferenceSystem crs2;

    public void setUp() throws Exception {
        this.crs1 = CRS.decode("EPSG:4326", true);
        this.crs2 = CRS.decode("EPSG:3005", true);
    }

    // test that two points, curves, rings, or surfaces are equal within a tolerance
    // (equals means same CRS and same ordinates within tolerance)
    private void assertEquals(GeometryImpl geom1, GeometryImpl geom2, double epsilon) {
        if (!CRS.equalsIgnoreMetadata(
                geom1.getCoordinateReferenceSystem(), geom2.getCoordinateReferenceSystem())) {
            assertTrue("CRS of two objects do not match", false);
        }
        if (geom1.getCoordinateDimension() != geom2.getCoordinateDimension()) {
            assertTrue("Coordinate dimension of objects do not match", false);
        }

        if (geom1 instanceof PointImpl && geom2 instanceof PointImpl) {
            PointImpl point1 = (PointImpl) geom1;
            PointImpl point2 = (PointImpl) geom2;
            for (int i = 0; i < point1.getDirectPosition().getCoordinate().length; i++) {
                assertEquals(
                        point1.getDirectPosition().getOrdinate(i),
                        point2.getDirectPosition().getOrdinate(i),
                        epsilon);
            }

        } else if (geom1 instanceof CurveImpl && geom2 instanceof CurveImpl) {
            CurveImpl curve1 = (CurveImpl) geom1;
            CurveImpl curve2 = (CurveImpl) geom2;
            List<DirectPosition> list1 = curve1.asDirectPositions();
            List<DirectPosition> list2 = curve2.asDirectPositions();
            Iterator<DirectPosition> iterator1 = list1.iterator();
            Iterator<DirectPosition> iterator2 = list2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                PointImpl p1 = new PointImpl(iterator1.next());
                PointImpl p2 = new PointImpl(iterator2.next());
                assertEquals(p1, p2, epsilon);
            }
        } else if (geom1 instanceof RingImpl && geom2 instanceof RingImplUnsafe) {
            RingImplUnsafe ring1 = (RingImplUnsafe) geom1;
            RingImplUnsafe ring2 = (RingImplUnsafe) geom2;
            List<DirectPosition> list1 = ring1.asDirectPositions();
            List<DirectPosition> list2 = ring2.asDirectPositions();
            Iterator<DirectPosition> iterator1 = list1.iterator();
            Iterator<DirectPosition> iterator2 = list2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                PointImpl p1 = new PointImpl(iterator1.next());
                PointImpl p2 = new PointImpl(iterator2.next());
                assertEquals(p1, p2, epsilon);
            }
        } else if (geom1 instanceof RingImpl && geom2 instanceof RingImpl) {
            RingImpl ring1 = (RingImpl) geom1;
            RingImpl ring2 = (RingImpl) geom2;
            List<DirectPosition> list1 = ring1.asDirectPositions();
            List<DirectPosition> list2 = ring2.asDirectPositions();
            Iterator<DirectPosition> iterator1 = list1.iterator();
            Iterator<DirectPosition> iterator2 = list2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                PointImpl p1 = new PointImpl(iterator1.next());
                PointImpl p2 = new PointImpl(iterator2.next());
                assertEquals(p1, p2, epsilon);
            }
        } else if (geom1 instanceof SurfaceImpl && geom2 instanceof SurfaceImpl) {
            SurfaceImpl surface1 = (SurfaceImpl) geom1;
            SurfaceImpl surface2 = (SurfaceImpl) geom2;
            List<Ring> list1 = surface1.getBoundaryRings();
            List<Ring> list2 = surface2.getBoundaryRings();
            Iterator<Ring> iterator1 = list1.iterator();
            Iterator<Ring> iterator2 = list2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                RingImplUnsafe r1 = (RingImplUnsafe) iterator1.next();
                RingImplUnsafe r2 = (RingImplUnsafe) iterator2.next();
                assertEquals(r1, r2, epsilon);
            }
        } else {
            assertTrue("unsupported or unmatching geometries", false);
        }
    }

    public void testPoint() throws Exception {

        PositionFactory positionFactory = new PositionFactoryImpl(crs1, new PrecisionModel());
        PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(crs1, positionFactory);

        PointImpl point1 =
                (PointImpl)
                        primitiveFactory.createPoint(
                                new double[] {-123.47009555832284, 48.543261561072285});
        PointImpl point2 = (PointImpl) point1.transform(crs2);

        // create expected result
        PositionFactory expectedPosF2 = new PositionFactoryImpl(crs2, new PrecisionModel());
        PrimitiveFactory expectedPrimF2 = new PrimitiveFactoryImpl(crs2, expectedPosF2);

        PointImpl expectedPoint2 =
                (PointImpl)
                        expectedPrimF2.createPoint(
                                new double[] {1187128.000000001, 395268.0000000004});

        // System.out.println(point1);
        // System.out.println(point2);
        // System.out.println(expectedPoint2);

        // assertTrue(point2.equals(expectedPoint2));
        assertEquals(expectedPoint2, point2, 0.9);
    }

    public void testCurve() throws Exception {

        PositionFactory positionFactory = new PositionFactoryImpl(crs1, new PrecisionModel());
        PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(crs1, positionFactory);
        GeometryFactory geometryFactory = new GeometryFactoryImpl(crs1, positionFactory);

        List<Position> points = new ArrayList<Position>();
        points.add(
                primitiveFactory.createPoint(
                        new double[] {-123.47009555832284, 48.543261561072285}));
        points.add(
                primitiveFactory.createPoint(
                        new double[] {-123.46972894676578, 48.55009592117936}));
        points.add(
                primitiveFactory.createPoint(
                        new double[] {-123.45463828850829, 48.54973520267305}));
        points.add(
                primitiveFactory.createPoint(new double[] {-123.4550070827961, 48.54290089070186}));
        LineString lineString = geometryFactory.createLineString(points);
        List curveSegmentList = Collections.singletonList(lineString);

        CurveImpl curve1 = (CurveImpl) primitiveFactory.createCurve(curveSegmentList);
        CurveImpl curve2 = (CurveImpl) curve1.transform(crs2);

        // create expected result
        PositionFactory expectedPosF2 = new PositionFactoryImpl(crs2, new PrecisionModel());
        PrimitiveFactory expectedPrimF2 = new PrimitiveFactoryImpl(crs2, expectedPosF2);
        GeometryFactory ExpectedGeomF2 = new GeometryFactoryImpl(crs2, expectedPosF2);

        List<Position> expectedPoints = new ArrayList<Position>();
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187128.000000001, 395268.0000000004}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187127.9999999998, 396026.99999999825}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1188245.0000000007, 396027.0000000039}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1188245.0000000005, 395268.0000000018}));
        LineString expectedLineString = ExpectedGeomF2.createLineString(expectedPoints);
        List expectedCurveSegmentList = Collections.singletonList(expectedLineString);

        CurveImpl expectedCurve = (CurveImpl) expectedPrimF2.createCurve(expectedCurveSegmentList);

        // System.out.println(curve1);
        // System.out.println(curve2);
        // System.out.println(expectedCurve);

        // assertTrue(curve2.equals(expectedCurve));
        assertEquals(curve2, expectedCurve, 0.9);
    }

    public void testRing() throws Exception {

        PositionFactory positionFactory = new PositionFactoryImpl(crs1, new PrecisionModel());
        PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(crs1, positionFactory);
        GeometryFactory geometryFactory = new GeometryFactoryImpl(crs1, positionFactory);

        List<Position> points1 = new ArrayList<Position>();
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.47009555832284, 48.543261561072285}));
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.46972894676578, 48.55009592117936}));
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.45463828850829, 48.54973520267305}));
        points1.add(
                primitiveFactory.createPoint(new double[] {-123.4550070827961, 48.54290089070186}));
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.47009555832284, 48.543261561072285}));
        LineString lineString1 = geometryFactory.createLineString(points1);
        List curveSegmentList1 = Collections.singletonList(lineString1);

        CurveImpl curve1 = (CurveImpl) primitiveFactory.createCurve(curveSegmentList1);

        /* Build Ring from Curve */
        ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
        curveList.add(curve1);

        RingImplUnsafe ring1 = (RingImplUnsafe) primitiveFactory.createRing(curveList);
        RingImplUnsafe ring2 = (RingImplUnsafe) ring1.transform(crs2);

        // create expected result
        PositionFactory expectedPosF2 = new PositionFactoryImpl(crs2, new PrecisionModel());
        PrimitiveFactory expectedPrimF2 = new PrimitiveFactoryImpl(crs2, expectedPosF2);
        GeometryFactory ExpectedGeomF2 = new GeometryFactoryImpl(crs2, expectedPosF2);

        List<Position> expectedPoints = new ArrayList<Position>();
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187128.000000001, 395268.0000000004}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187127.9999999998, 396026.99999999825}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1188245.0000000007, 396027.0000000039}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1188245.0000000005, 395268.0000000018}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187128.000000001, 395268.0000000004}));
        LineString expectedLineString = ExpectedGeomF2.createLineString(expectedPoints);
        List expectedCurveSegmentList = Collections.singletonList(expectedLineString);

        CurveImpl expectedCurve = (CurveImpl) expectedPrimF2.createCurve(expectedCurveSegmentList);

        /* Build Ring from Curve */
        ArrayList<OrientableCurve> expectedCurveList = new ArrayList<OrientableCurve>();
        expectedCurveList.add(expectedCurve);

        RingImplUnsafe expectedRing = (RingImplUnsafe) expectedPrimF2.createRing(expectedCurveList);

        // System.out.println(ring1);
        // System.out.println(ring2);
        // System.out.println(expectedRing);

        // assertTrue(ring2.equals(expectedRing));
        assertEquals(ring2, expectedRing, 0.9);
    }

    /**
     * We need to ensure that an IdentityTransform is recognized and the result is *equals*
     * (although perhaps not *equalsExact*.
     *
     * <p>Note I am using GeometryBuilder here as I am interested in testing functionality rather
     * than factories.
     *
     * <p>Thanks to SriPuligundla for the bug report.
     */
    public void testSurfaceIdentityTransform() throws Exception {
        CoordinateReferenceSystem wsg1 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem wsg2 = CRS.decode("EPSG:4326");
        MathTransform t = CRS.findMathTransform(wsg1, wsg2);
        assertTrue("WSG84 transformed to WSG84 should be Identity", t.isIdentity());

        GeometryBuilder builder = new GeometryBuilder(wsg1);

        double array[] =
                new double[] {
                    -123.47009555832284, 48.543261561072285,
                    -123.46972894676578, 48.55009592117936,
                    -123.45463828850829, 48.54973520267305,
                    -123.4550070827961, 48.54290089070186,
                    -123.47009555832284, 48.543261561072285
                };
        PointArray points = builder.createPointArray(array);
        SurfaceBoundary boundary = builder.createSurfaceBoundary(points);
        Surface surface = builder.createSurface(boundary);
        assertNotNull(surface);

        Surface surface2 = (Surface) surface.transform(wsg2, t);
        assertNotNull(surface2);

        assertTrue("object equals", surface.equals((Object) surface2));
        assertTrue("geometry equals", surface.equals(surface2));
    }

    public void testSurface() throws Exception {
        PositionFactory positionFactory = new PositionFactoryImpl(crs1, new PrecisionModel());
        PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(crs1, positionFactory);
        GeometryFactory geometryFactory = new GeometryFactoryImpl(crs1, positionFactory);

        List<Position> points1 = new ArrayList<Position>();
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.47009555832284, 48.543261561072285}));
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.46972894676578, 48.55009592117936}));
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.45463828850829, 48.54973520267305}));
        points1.add(
                primitiveFactory.createPoint(new double[] {-123.4550070827961, 48.54290089070186}));
        points1.add(
                primitiveFactory.createPoint(
                        new double[] {-123.47009555832284, 48.543261561072285}));
        LineString lineString1 = geometryFactory.createLineString(points1);
        List curveSegmentList1 = Collections.singletonList(lineString1);

        CurveImpl curve1 = (CurveImpl) primitiveFactory.createCurve(curveSegmentList1);

        /* Build Ring from Curve */
        ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
        curveList.add(curve1);

        // Build Ring then SurfaceBoundary then Surface
        RingImpl exteriors = (RingImpl) primitiveFactory.createRing(curveList);
        List<Ring> interiors = new ArrayList<Ring>();
        SurfaceBoundary sboundary = primitiveFactory.createSurfaceBoundary(exteriors, interiors);
        Surface surface1 = primitiveFactory.createSurface(sboundary);
        Surface surface2 = (SurfaceImpl) surface1.transform(crs2);

        // create expected result
        PositionFactory expectedPosF2 = new PositionFactoryImpl(crs2, new PrecisionModel());
        PrimitiveFactory expectedPrimF2 = new PrimitiveFactoryImpl(crs2, expectedPosF2);
        GeometryFactory ExpectedGeomF2 = new GeometryFactoryImpl(crs2, expectedPosF2);

        List<Position> expectedPoints = new ArrayList<Position>();
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187128.000000001, 395268.0000000004}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187127.9999999998, 396026.99999999825}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1188245.0000000007, 396027.0000000039}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1188245.0000000005, 395268.0000000018}));
        expectedPoints.add(
                expectedPrimF2.createPoint(new double[] {1187128.000000001, 395268.0000000004}));
        LineString expectedLineString = ExpectedGeomF2.createLineString(expectedPoints);
        List expectedCurveSegmentList = Collections.singletonList(expectedLineString);

        CurveImpl expectedCurve = (CurveImpl) expectedPrimF2.createCurve(expectedCurveSegmentList);

        /* Build Ring from Curve */
        ArrayList<OrientableCurve> expectedCurveList = new ArrayList<OrientableCurve>();
        expectedCurveList.add(expectedCurve);

        // Build Ring then SurfaceBoundary then Surface
        RingImpl exteriors2 = (RingImpl) expectedPrimF2.createRing(expectedCurveList);
        List<Ring> interiors2 = new ArrayList<Ring>();
        SurfaceBoundary sboundary2 = expectedPrimF2.createSurfaceBoundary(exteriors2, interiors2);
        Surface expectedSurface = expectedPrimF2.createSurface(sboundary2);

        // System.out.println(surface1);
        // System.out.println(surface2);
        // System.out.println(expectedSurface);

        // assertTrue(surface2.equals(expectedSurface));
        assertEquals((SurfaceImpl) surface2, (SurfaceImpl) expectedSurface, 0.9);
    }
}
