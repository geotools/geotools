/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class CurvedGeometryTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

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
        CircularString cloned = (CircularString) cs.clone();
        assertEquals(cs, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI / 2, cs.getLength(), 1e-1);
        // use a tighter tolerance
        assertEquals(10 * Math.PI / 2, cs.linearize(1e-6).getLength(), 1e-6);

        // topological operation check
        assertTrue(cs.intersects(JTS.toGeometry(new Envelope(4, 8, 4, 8))));

        // verify a close-by, mis-aligned (angle wise) arc with the minimum segmentation
        // (cannot do 9.999 or the control points will cause the intersection)
        double[] controlPoints2 = new Circle(9.9).samplePoints(Math.PI * 3 / 5, Math.PI / 4,
                Math.PI / 10);
        CircularString cs2 = new CircularString(controlPoints2, GEOMETRY_FACTORY, Double.MAX_VALUE);
        // they should not intersect
        assertFalse(cs.intersects(cs2));

        // check curved WKT generation
        String wkt = cs.toCurvedText();
        assertEquals(
                "CIRCULARSTRING(6.123233995736766E-16 10.0, 7.0710678118654755 7.071067811865475, 10.0 0.0)",
                wkt);

        // check reversing
        CircularString reversed = (CircularString) cs.reverse();
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
        GrowableDoubleArray data = new GrowableDoubleArray();
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
        CircularString cloned = (CircularString) cs.clone();
        assertEquals(cs, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI, cs.getLength(), 1e-1);
        // use a tighter tolerance
        assertEquals(10 * Math.PI, cs.linearize(1e-6).getLength(), 1e-6);

        // topological operation check
        assertTrue(cs.intersects(JTS.toGeometry(new Envelope(4, 8, 4, 8))));

        // verify a close-by, mis-aligned (angle wise) arc with the minimum segmentation
        // (cannot do 9.999 or the control points will cause the intersection)
        double[] controlPoints2 = new Circle(9.9).samplePoints(Math.PI * 3 / 5, Math.PI / 4,
                Math.PI / 10);
        CircularString cs2 = new CircularString(controlPoints2, GEOMETRY_FACTORY, Double.MAX_VALUE);
        // they should not intersect
        assertFalse(cs.intersects(cs2));

        // check curved WKT generation
        String wkt = cs.toCurvedText();
        assertEquals(
                "CIRCULARSTRING(6.123233995736766E-16 10.0, 7.0710678118654755 7.071067811865475, 10.0 0.0, 12.928932188134524 7.0710678118654755, 20.0 10.0)",
                wkt);

        // check reversing
        CircularString reversed = (CircularString) cs.reverse();
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
        double[] circleControlPoints = circle.samplePoints(0, Math.PI / 2, Math.PI,
                Math.PI * 3 / 2, 0);

        CircularRing cr = new CircularRing(circleControlPoints, GEOMETRY_FACTORY, Double.MAX_VALUE);
        Envelope env = cr.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, -10, 10), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 4 + 1, cr.getNumPoints());
        LiteCoordinateSequence coordinates = (LiteCoordinateSequence) cr.linearize()
                .getCoordinateSequence();
        circle.assertTolerance(coordinates.getArray(), 0.1);

        // check cloning
        CircularRing cloned = (CircularRing) cr.clone();
        assertEquals(cr, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI * 2, cr.getLength(), 1e-1);

        // topological operation check
        assertTrue(cr.intersects(JTS.toGeometry(new Envelope(4, 8, 4, 8))));

        // verify a close-by, mis-aligned (angle wise) arc with the minimum segmentation
        // (cannot do 9.999 or the control points will cause the intersection)
        double[] controlPoints2 = new Circle(9.9).samplePoints(Math.PI * 3 / 5, Math.PI / 4,
                Math.PI / 10);
        CircularString cs2 = new CircularString(controlPoints2, GEOMETRY_FACTORY, Double.MAX_VALUE);
        // they should not intersect
        assertFalse(cr.intersects(cs2));

        // check curved WKT generation
        String wkt = cr.toCurvedText();
        assertEquals(
                "CIRCULARSTRING(10.0 0.0, 6.123233995736766E-16 10.0, -10.0 1.2246467991473533E-15, -1.8369701987210296E-15 -10.0, 10.0 0.0)",
                wkt);

        // check reversing
        CircularRing reversed = (CircularRing) cr.reverse();
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
        double[] halfCircle = new double[] { 10, 10, 0, 20, -10, 10 };
        CircularString cs = new CircularString(halfCircle, GEOMETRY_FACTORY, Double.MAX_VALUE);
        LineString ls = new LineString(new LiteCoordinateSequence(new double[] { -10, 10, -10, 0,
                10, 0, 10, 10 }), GEOMETRY_FACTORY);
        CompoundCurve curve = new CompoundCurve(Arrays.asList(cs, ls), GEOMETRY_FACTORY,
                Double.MAX_VALUE);

        // envelope check
        Envelope env = curve.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, 0, 20), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 2 + 4, curve.getNumPoints());

        // check cloning
        CompoundCurve cloned = (CompoundCurve) curve.clone();
        assertEquals(curve, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI + 40, curve.getLength(), 1e-1);

        // topological operation check
        assertTrue(curve.intersects(JTS.toGeometry(new Envelope(0, 10, 12, 15))));
        assertTrue(curve.intersects(JTS.toGeometry(new Envelope(8, 12, -2, 2))));

        // check curved WKT generation
        String wkt = curve.toCurvedText();
        assertEquals(
                "COMPOUNDCURVE(CIRCULARSTRING(10.0 10.0, 0.0 20.0, -10.0 10.0), (-10.0 10.0, -10.0 0.0, 10.0 0.0, 10.0 10.0))",
                wkt);
    }

    @Test
    public void testCompoundRing() {
        double[] halfCircle = new double[] { 10, 10, 0, 20, -10, 10 };
        CircularString cs = new CircularString(halfCircle, GEOMETRY_FACTORY, Double.MAX_VALUE);
        LineString ls = new LineString(new LiteCoordinateSequence(new double[] { -10, 10, -10, 0,
                10, 0, 10, 10 }), GEOMETRY_FACTORY);
        CompoundRing ring = new CompoundRing(Arrays.asList(cs, ls), GEOMETRY_FACTORY,
                Double.MAX_VALUE);

        // envelope check
        Envelope env = ring.getEnvelopeInternal();
        assertEnvelopeEquals(new Envelope(-10, 10, 0, 20), env);

        // check linearization
        assertEquals(CircularArc.BASE_SEGMENTS_QUADRANT * 2 + 4, ring.getNumPoints());

        // check cloning
        CompoundRing cloned = (CompoundRing) ring.clone();
        assertEquals(ring, cloned);

        // check perimeter, not enough control points to have a accurate estimate
        assertEquals(10 * Math.PI + 40, ring.getLength(), 1e-1);

        // topological operation check
        assertTrue(ring.intersects(JTS.toGeometry(new Envelope(0, 10, 12, 15))));
        assertTrue(ring.intersects(JTS.toGeometry(new Envelope(8, 12, -2, 2))));

        // check curved WKT generation
        String wkt = ring.toCurvedText();
        assertEquals(
                "COMPOUNDCURVE(CIRCULARSTRING(10.0 10.0, 0.0 20.0, -10.0 10.0), (-10.0 10.0, -10.0 0.0, 10.0 0.0, 10.0 10.0))",
                wkt);
    }

    private void assertEnvelopeEquals(Envelope envelope, Envelope env) {
        assertEquals(envelope.getMinX(), env.getMinX(), Circle.EPS);
        assertEquals(envelope.getMinY(), env.getMinY(), Circle.EPS);
        assertEquals(envelope.getMinX(), env.getMinX(), Circle.EPS);
        assertEquals(envelope.getMaxY(), env.getMaxY(), Circle.EPS);
    }

}
