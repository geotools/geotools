/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class DecimatorTest {

    GeometryFactory gf = new GeometryFactory();
    LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();

    private MathTransform identity = new AffineTransform2D(new AffineTransform());

    /** http://jira.codehaus.org/browse/GEOT-1923 */
    @Test
    public void testDecimateRing() {
        // a long rectangle made of 5 coordinates
        LinearRing g =
                gf.createLinearRing(csf.create(new double[] {0, 0, 0, 10, 2, 10, 2, 0, 0, 0}));
        assertTrue(g.isValid());

        Decimator d = new Decimator(4, 4);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(4, g.getCoordinateSequence().size());
    }

    /** http://jira.codehaus.org/browse/GEOT-2937 */
    @Test
    public void testDecimatePseudoRing() {
        // a long rectangle made of 3 coordinates
        LineString g = gf.createLineString(csf.create(new double[] {0, 0, 0, 10, 0, 0}));
        assertTrue(g.isValid());

        Decimator d = new Decimator(4, 4);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(3, g.getCoordinateSequence().size());
    }

    @Test
    public void testDecimateOpenTriangle() throws Exception {
        LineString g = gf.createLineString(csf.create(new double[] {0, 0, 0, 2, 2, 2, 0, 0}));
        assertTrue(g.isValid());

        Decimator d = new Decimator(3, 3);
        d.decimateTransformGeneralize(g, new AffineTransform2D(new AffineTransform()));
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(4, g.getCoordinateSequence().size());
    }

    /** http://jira.codehaus.org/browse/GEOT-1923 */
    @Test
    public void testDecimateRingEnvelope() {
        // acute triangle
        LinearRing g =
                gf.createLinearRing(csf.create(new double[] {0, 0, 0, 10, 2, 10, 2, 0, 0, 0}));
        assertTrue(g.isValid());

        Decimator d = new Decimator(20, 20);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(4, g.getCoordinateSequence().size());
    }

    @Test
    public void testNoDecimation() {
        // acute triangle
        LinearRing g =
                gf.createLinearRing(csf.create(new double[] {0, 0, 0, 10, 2, 10, 2, 0, 0, 0}));
        LinearRing original = (LinearRing) g.copy();
        assertTrue(g.isValid());

        Decimator d = new Decimator(-1, -1);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertTrue(original.equalsExact(g));
    }

    @Test
    public void testDistance() throws Exception {
        LineString ls =
                gf.createLineString(csf.create(new double[] {0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5}));

        Decimator d = new Decimator(identity, new Rectangle(0, 0, 5, 5), 0.8);
        d.decimateTransformGeneralize((Geometry) ls.copy(), identity);
        assertEquals(6, ls.getNumPoints());

        d = new Decimator(identity, new Rectangle(0, 0, 5, 5), 1);
        d.decimateTransformGeneralize(ls, identity);
        assertEquals(4, ls.getNumPoints());

        d = new Decimator(identity, new Rectangle(0, 0, 5, 5), 6);
        d.decimateTransformGeneralize(ls, identity);
        assertEquals(2, ls.getNumPoints());
    }

    @Test
    public void testDecimate3DPoint() throws Exception {
        Point p = gf.createPoint(csf.create(new double[] {0, 1, 2}, 3));

        Decimator d = new Decimator(identity, new Rectangle(0, 0, 5, 5), 0.8);
        d.decimateTransformGeneralize(p, identity);
        assertEquals(1, p.getNumPoints());
        assertEquals(2, p.getCoordinateSequence().getDimension());
    }

    @Test
    public void testDecimate3DLine() throws Exception {
        LineString ls =
                gf.createLineString(
                        csf.create(new double[] {0, 0, 1, 1, 2, 1, 3, 3, 4, 4, 5, 5}, 3));
        assertEquals(4, ls.getNumPoints());

        Decimator d = new Decimator(identity, new Rectangle(0, 0, 5, 5), 0.8);
        d.decimateTransformGeneralize(ls, identity);
        assertEquals(4, ls.getNumPoints());
        assertEquals(2, ls.getCoordinateSequence().getDimension());
    }

    @Test
    public void testDecimationSpansInfinite() throws Exception {
        MathTransform mt =
                new AbstractMathTransform() {

                    @Override
                    public void transform(
                            double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
                            throws TransformException {
                        if (srcPts[0] == -0.5 || srcPts[1] == 0.5) {
                            dstPts[0] = Double.NaN;
                            dstPts[1] = Double.NaN;
                            dstPts[2] = Double.NaN;
                            dstPts[3] = Double.NaN;
                        } else {
                            dstPts[0] = srcPts[0] * 10;
                            dstPts[1] = srcPts[1] * 10;
                            dstPts[2] = srcPts[2] * 10;
                            dstPts[3] = srcPts[3] * 10;
                        }
                    }

                    @Override
                    public int getTargetDimensions() {
                        return 2;
                    }

                    @Override
                    public int getSourceDimensions() {
                        return 2;
                    }
                };
        double[] dx = Decimator.computeGeneralizationDistances(mt, new Rectangle(10, 10), 1);
        assertEquals(10, dx[0], 0d);
        assertEquals(10, dx[1], 0d);
    }

    @Test
    public void testDecimateCollection() throws Exception {
        WKTReader2 reader = new WKTReader2();
        MultiLineString origin =
                (MultiLineString)
                        reader.read("MULTICURVE((0 0, 5 5),CIRCULARSTRING(4 0, 4 4, 8 4))");
        Decimator d = new Decimator(0.1, 0.1);
        MultiLineString simplified =
                (MultiLineString) d.decimateTransformGeneralize(origin, identity);
        assertEquals(origin.getGeometryN(0), simplified.getGeometryN(0));
        assertNotEquals(origin.getGeometryN(1), simplified.getGeometryN(1));
        assertEquals("CircularString", origin.getGeometryN(1).getGeometryType());
        assertEquals("LineString", simplified.getGeometryN(1).getGeometryType());
    }
}
