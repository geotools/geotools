/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

public class CurvedGeometryTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    @Test
    public void testCircularStringEmpty() {
        CircularString cs = new CircularString(new double[0], GEOMETRY_FACTORY, Double.MAX_VALUE);
        assertTrue(cs.isEmpty());
        assertEquals("CIRCULARSTRING EMPTY", cs.toCurvedText());
        assertEquals(0, cs.getNumArcs());
        assertEquals(0, cs.getNumPoints());
    }

    @Test
    public void testCircularStringSingleCurve() {
        double[] controlPoints = new Circle(10).samplePoints(Math.PI / 2, Math.PI / 4, 0);

        CircularString cs = new CircularString(controlPoints, GEOMETRY_FACTORY, Double.MAX_VALUE);

        // check envelope
        Envelope env = cs.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(0, 10, 0, 10), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT + 1, cs.getNumPoints());

        // check cloning
        CircularString cloned = (CircularString) cs.copy();
        assertEquals(cs, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI / 2, cs.getLength(), 1e-1);
        // use a tighter tolerance
        assertEquals(10 * Math.PI / 2, cs.linearize(1e-6).getLength(), 1e-6);

        // topological operation check
        assertTrue(cs.intersects(JTS.toGeometry(new Envelope(4, 8, 4, 8))));

        // verify a close-by, mis-aligned (angle wise) arc with the minimum segmentation
        // (cannot do 9.999 or the control points will cause the intersection)
        double[] controlPoints2 = new Circle(9.9).samplePoints(Math.PI * 3 / 5, Math.PI / 4, Math.PI / 10);
        CircularString cs2 = new CircularString(controlPoints2, GEOMETRY_FACTORY, Double.MAX_VALUE);
        // they should not intersect
        assertFalse(cs.intersects(cs2));

        // check curved WKT generation
        String wkt = cs.toCurvedText();
        assertEquals(
                "CIRCULARSTRING (6.123233995736766E-16 10.0, 7.0710678118654755 7.071067811865475, 10.0 0.0)", wkt);

        // check reversing
        CircularString reversed = cs.reverse();
        assertEquals(reversed.controlPoints[0], cs.controlPoints[4], 0d);
        assertEquals(reversed.controlPoints[1], cs.controlPoints[5], 0d);
        assertEquals(reversed.controlPoints[2], cs.controlPoints[2], 0d);
        assertEquals(reversed.controlPoints[3], cs.controlPoints[3], 0d);
        assertEquals(reversed.controlPoints[4], cs.controlPoints[0], 0d);
        assertEquals(reversed.controlPoints[5], cs.controlPoints[1], 0d);
    }

    @Test
    public void testCircularStringWings() {
        double[] sp1 = new Circle(10).samplePoints(Math.PI / 2, Math.PI / 4, 0);
        double[] sp2 = new Circle(10, 20, 0).samplePoints(Math.PI, Math.PI * 3 / 4, Math.PI / 2);
        GrowableOrdinateArray data = new GrowableOrdinateArray();
        data.addAll(sp1);
        data.add(sp2[2], sp2[3]);
        data.add(sp2[4], sp2[5]);

        CircularString cs = new CircularString(data.getData(), GEOMETRY_FACTORY, Double.MAX_VALUE);
        // System.out.println(cs.toText());
        Envelope env = cs.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(0, 20, 0, 10), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 2 + 1, cs.getNumPoints());

        // check cloning
        CircularString cloned = (CircularString) cs.copy();
        assertEquals(cs, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI, cs.getLength(), 1e-1);
        // use a tighter tolerance
        assertEquals(10 * Math.PI, cs.linearize(1e-6).getLength(), 1e-6);

        // topological operation check
        assertTrue(cs.intersects(JTS.toGeometry(new Envelope(4, 8, 4, 8))));

        // verify a close-by, mis-aligned (angle wise) arc with the minimum segmentation
        // (cannot do 9.999 or the control points will cause the intersection)
        double[] controlPoints2 = new Circle(9.9).samplePoints(Math.PI * 3 / 5, Math.PI / 4, Math.PI / 10);
        CircularString cs2 = new CircularString(controlPoints2, GEOMETRY_FACTORY, Double.MAX_VALUE);
        // they should not intersect
        assertFalse(cs.intersects(cs2));

        // check curved WKT generation
        String wkt = cs.toCurvedText();
        assertEquals(
                "CIRCULARSTRING (6.123233995736766E-16 10.0, 7.0710678118654755 7.071067811865475, 10.0 0.0, 12.928932188134524 7.0710678118654755, 20.0 10.0)",
                wkt);

        // check reversing
        CircularString reversed = cs.reverse();
        double[] controlPoints = cs.controlPoints;
        double[] controlPointsReverse = reversed.controlPoints;
        assertEquals(controlPointsReverse[0], controlPoints[8], 0d);
        assertEquals(controlPointsReverse[1], controlPoints[9], 0d);
        assertEquals(controlPointsReverse[2], controlPoints[6], 0d);
        assertEquals(controlPointsReverse[3], controlPoints[7], 0d);
        assertEquals(controlPointsReverse[4], controlPoints[4], 0d);
        assertEquals(controlPointsReverse[5], controlPoints[5], 0d);
        assertEquals(controlPointsReverse[6], controlPoints[2], 0d);
        assertEquals(controlPointsReverse[7], controlPoints[3], 0d);
        assertEquals(controlPointsReverse[8], controlPoints[0], 0d);
        assertEquals(controlPointsReverse[9], controlPoints[1], 0d);
    }

    @Test
    public void testSimpleCircle() {
        Circle circle = new Circle(10);
        double[] circleControlPoints = circle.samplePoints(0, Math.PI / 2, Math.PI, Math.PI * 3 / 2, 0);

        CircularRing cr = new CircularRing(circleControlPoints, GEOMETRY_FACTORY, Double.MAX_VALUE);
        Envelope env = cr.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, -10, 10), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 4 + 1, cr.getNumPoints());
        CoordinateSequence coordinates = cr.linearize().getCoordinateSequence();
        circle.assertTolerance(coordinates, 0.1);

        // check cloning
        CircularRing cloned = (CircularRing) cr.copy();
        assertEquals(cr, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI * 2, cr.getLength(), 1e-1);

        // topological operation check
        assertTrue(cr.intersects(JTS.toGeometry(new Envelope(4, 8, 4, 8))));

        // verify a close-by, mis-aligned (angle wise) arc with the minimum segmentation
        // (cannot do 9.999 or the control points will cause the intersection)
        double[] controlPoints2 = new Circle(9.9).samplePoints(Math.PI * 3 / 5, Math.PI / 4, Math.PI / 10);
        CircularString cs2 = new CircularString(controlPoints2, GEOMETRY_FACTORY, Double.MAX_VALUE);
        // they should not intersect
        assertFalse(cr.intersects(cs2));

        // check curved WKT generation
        String wkt = cr.toCurvedText();
        assertEquals(
                "CIRCULARSTRING (10.0 0.0, 6.123233995736766E-16 10.0, -10.0 1.2246467991473533E-15, -1.8369701987210296E-15 -10.0, 10.0 0.0)",
                wkt);

        // check reversing
        CircularRing reversed = cr.reverse();
        // System.out.println(cr.toCurvedText());
        // System.out.println(reversed.toCurvedText());
        double[] controlPoints = cr.delegate.controlPoints;
        double[] controlPointsReverse = reversed.delegate.controlPoints;
        assertEquals(controlPointsReverse[0], controlPoints[8], 0d);
        assertEquals(controlPointsReverse[1], controlPoints[9], 0d);
        assertEquals(controlPointsReverse[2], controlPoints[6], 0d);
        assertEquals(controlPointsReverse[3], controlPoints[7], 0d);
        assertEquals(controlPointsReverse[4], controlPoints[4], 0d);
        assertEquals(controlPointsReverse[5], controlPoints[5], 0d);
        assertEquals(controlPointsReverse[6], controlPoints[2], 0d);
        assertEquals(controlPointsReverse[7], controlPoints[3], 0d);
        assertEquals(controlPointsReverse[8], controlPoints[0], 0d);
        assertEquals(controlPointsReverse[9], controlPoints[1], 0d);
    }

    @Test
    public void testCompoundCurve() {
        double[] halfCircle = {10, 10, 0, 20, -10, 10};
        CircularString cs = new CircularString(halfCircle, GEOMETRY_FACTORY, Double.MAX_VALUE);
        LineString ls = new LineString(
                new CoordinateArraySequence(new Coordinate[] {
                    new Coordinate(-10, 10), new Coordinate(-10, 0), new Coordinate(10, 0), new Coordinate(10, 10)
                }),
                GEOMETRY_FACTORY);
        CompoundCurve curve = new CompoundCurve(Arrays.asList(cs, ls), GEOMETRY_FACTORY, Double.MAX_VALUE);

        assertFalse("Check that this should not be a rectangle.", curve.isRectangle());

        // envelope check
        Envelope env = curve.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, 0, 20), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 2 + 4, curve.getNumPoints());

        // check cloning
        CompoundCurve cloned = (CompoundCurve) curve.copy();
        assertEquals(curve, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI + 40, curve.getLength(), 1e-1);

        // topological operation check
        assertTrue(curve.intersects(JTS.toGeometry(new Envelope(0, 10, 12, 15))));
        assertTrue(curve.intersects(JTS.toGeometry(new Envelope(8, 12, -2, 2))));

        // check curved WKT generation
        String wkt = curve.toCurvedText();
        assertEquals(
                "COMPOUNDCURVE (CIRCULARSTRING (10.0 10.0, 0.0 20.0, -10.0 10.0), (-10.0 10.0, -10.0 0.0, 10.0 0.0, 10.0 10.0))",
                wkt);
    }

    @Test
    public void testCompoundRing() {
        double[] halfCircle = {10, 10, 0, 20, -10, 10};
        CircularString cs = new CircularString(halfCircle, GEOMETRY_FACTORY, Double.MAX_VALUE);
        LineString ls = new LineString(
                new CoordinateArraySequence(new Coordinate[] {
                    new Coordinate(-10, 10), new Coordinate(-10, 0), new Coordinate(10, 0), new Coordinate(10, 10)
                }),
                GEOMETRY_FACTORY);
        CompoundRing ring = new CompoundRing(Arrays.asList(cs, ls), GEOMETRY_FACTORY, Double.MAX_VALUE);

        // envelope check
        Envelope env = ring.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, 0, 20), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 2 + 4, ring.getNumPoints());

        // check cloning
        CompoundRing cloned = (CompoundRing) ring.copy();
        assertEquals(ring, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI + 40, ring.getLength(), 1e-1);

        // topological operation check
        assertTrue(ring.intersects(JTS.toGeometry(new Envelope(0, 10, 12, 15))));
        assertTrue(ring.intersects(JTS.toGeometry(new Envelope(8, 12, -2, 2))));

        // check curved WKT generation
        String wkt = ring.toCurvedText();
        assertEquals(
                "COMPOUNDCURVE (CIRCULARSTRING (10.0 10.0, 0.0 20.0, -10.0 10.0), (-10.0 10.0, -10.0 0.0, 10.0 0.0, 10.0 10.0))",
                wkt);
    }

    @Test
    public void testCurvePolygons() {
        CurvePolygon curved = buildCurvePolygon();

        // envelope check
        Envelope env = curved.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, -10, 10), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 4 + 1 + 5, curved.getNumPoints());

        // check cloning
        CurvePolygon cloned = (CurvePolygon) curved.copy();
        assertEquals(curved, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(2 * 10 * Math.PI + 8, curved.getLength(), 1e-1);

        // topological operation check
        assertTrue(curved.intersects(JTS.toGeometry(new Envelope(0, 10, 5, 15))));
        assertTrue(curved.intersects(JTS.toGeometry(new Envelope(8, 12, -2, 2))));

        // check curved WKT generation
        String wkt = curved.toCurvedText();
        assertEquals(
                "CURVEPOLYGON (CIRCULARSTRING (-10.0 0.0, 0.0 10.0, 10.0 0.0, 0.0 -10.0, -10.0 0.0), (-1.0 -1.0, -1.0 1.0, 1.0 1.0, 1.0 -1.0, -1.0 -1.0))",
                wkt);
    }

    @Test
    public void testMultiSurface() {
        CurvePolygon p1 = buildCurvePolygon();

        LinearRing shell = new LinearRing(
                new CoordinateArraySequence(new Coordinate[] {
                    new Coordinate(20, 20),
                    new Coordinate(24, 20),
                    new Coordinate(24, 24),
                    new Coordinate(20, 24),
                    new Coordinate(20, 20)
                }),
                GEOMETRY_FACTORY);
        LinearRing hole = new LinearRing(
                new CoordinateArraySequence(new Coordinate[] {
                    new Coordinate(22, 22),
                    new Coordinate(23, 22),
                    new Coordinate(23, 23),
                    new Coordinate(23, 22),
                    new Coordinate(22, 22)
                }),
                GEOMETRY_FACTORY);
        Polygon p2 = new Polygon(shell, new LinearRing[] {hole}, GEOMETRY_FACTORY);

        MultiSurface ms = new MultiSurface(new Polygon[] {p1, p2}, GEOMETRY_FACTORY, Double.MAX_VALUE);

        // envelope check
        Envelope env = ms.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 24, -10, 24), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 4 + 1 + 5 + 5 + 5, ms.getNumPoints());

        // check cloning
        MultiSurface cloned = (MultiSurface) ms.copy();
        assertEquals(ms, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(2 * 10 * Math.PI + 8 + (16 + 4), ms.getLength(), 1e-1);

        // topological operation check
        assertTrue(ms.intersects(JTS.toGeometry(new Envelope(0, 10, 5, 15))));
        assertTrue(ms.intersects(JTS.toGeometry(new Envelope(8, 12, -2, 2))));

        // check curved WKT generation
        String wkt = ms.toCurvedText();
        assertEquals(
                "MULTISURFACE (CURVEPOLYGON (CIRCULARSTRING (-10.0 0.0, 0.0 10.0, 10.0 0.0, 0.0 -10.0, -10.0 0.0), "
                        + "(-1.0 -1.0, -1.0 1.0, 1.0 1.0, 1.0 -1.0, -1.0 -1.0)), ((20.0 20.0, 24.0 20.0, 24.0 24.0, 20.0 24.0, 20.0 20.0), "
                        + "(22.0 22.0, 23.0 22.0, 23.0 23.0, 23.0 22.0, 22.0 22.0)))",
                wkt);
    }

    @Test
    public void testNormalize() {
        double[] circleControlPoints = {0, 0, 0, 10, 0, 0};
        CircularRing circle = new CircularRing(circleControlPoints, GEOMETRY_FACTORY, 1e-6);

        CircularRing normalized = circle.normalizeRing();
        assertEquals(2, normalized.getNumArcs());
        CircularArc a1 = normalized.getArcN(0);
        assertArrayEquals(new double[] {0, 0, 5, 5, 0, 10}, a1.getControlPoints(), 1e-6);
        CircularArc a2 = normalized.getArcN(1);
        assertArrayEquals(new double[] {0, 10, -5, 5, 0, 0}, a2.getControlPoints(), 1e-6);
    }

    private CurvePolygon buildCurvePolygon() {
        double[] circleControlPoints = {-10, 0, 0, 10, 10, 0, 0, -10, -10, 0};
        CircularRing shell = new CircularRing(circleControlPoints, GEOMETRY_FACTORY, Double.MAX_VALUE);

        LinearRing hole = new LinearRing(
                new CoordinateArraySequence(new Coordinate[] {
                    new Coordinate(-1, -1),
                    new Coordinate(-1, 1),
                    new Coordinate(1, 1),
                    new Coordinate(1, -1),
                    new Coordinate(-1, -1)
                }),
                GEOMETRY_FACTORY);
        CurvePolygon curved = new CurvePolygon(shell, new LinearRing[] {hole}, GEOMETRY_FACTORY, Double.MAX_VALUE);
        return curved;
    }

    private void assertEnvelopeEquals(Envelope envelope, Envelope env) {
        assertEquals(envelope.getMinX(), env.getMinX(), Circle.EPS);
        assertEquals(envelope.getMinY(), env.getMinY(), Circle.EPS);
        assertEquals(envelope.getMinX(), env.getMinX(), Circle.EPS);
        assertEquals(envelope.getMaxY(), env.getMaxY(), Circle.EPS);
    }
}
