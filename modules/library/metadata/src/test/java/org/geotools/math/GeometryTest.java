/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1998-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.math;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.vecmath.Point3d;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link Line} and {@link Plane} classes.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 */
public final class GeometryTest {
    /**
     * Tolerance factor for comparaisons.
     */
    private static final double EPS = 1E-8;

    /**
     * Tests {@link Line#setLine}.
     */
    @Test
    public void testLine() {
        final Line line = new Line();
        line.setLine(new Point2D.Double(-2, 2), new Point2D.Double(8, 22));
        assertEquals("slope", 2, line.getSlope(), EPS);
        assertEquals("x0",   -3, line.getX0(),    EPS);
        assertEquals("y0",    6, line.getY0(),    EPS);

        // Horizontal line
        line.setLine(new Point2D.Double(-2, 2), new Point2D.Double(8, 2));
        assertEquals("slope", 0, line.getSlope(), EPS);
        assertTrue  ("x0", Double.isInfinite(line.getX0()));
        assertEquals("y0",    2, line.getY0(),    EPS);

        // Vertical line
        line.setLine(new Point2D.Double(-2, 2), new Point2D.Double(-2, 22));
        assertTrue  ("slope", Double.isInfinite(line.getSlope()));
        assertEquals("x0", -2, line.getX0(), EPS);
        assertTrue  ("y0", Double.isInfinite(line.getY0()));

        // Horizontal line on the x axis
        line.setLine(new Point2D.Double(-2, 0), new Point2D.Double(8, 0));
        assertEquals("slope", 0, line.getSlope(), EPS);
        assertTrue  ("x0", Double.isInfinite(line.getX0()));
        assertEquals("y0", 0, line.getY0(), EPS);

        // Vertical line on the y axis
        line.setLine(new Point2D.Double(0, 2), new Point2D.Double(0, 22));
        assertTrue  ("slope", Double.isInfinite(line.getSlope()));
        assertEquals("x0", 0, line.getX0(), EPS);
        assertTrue  ("y0", Double.isInfinite(line.getY0()));
    }

    /**
     * Tests {@link Line#isoscelesTriangleBase}.
     */
    @Test
    public void testIsoscelesTriangleBase() {
        final Line test = new Line();
        test.setLine(new Point2D.Double(20,30), new Point2D.Double(80,95));
        assertEquals("slope", 1.083333333333333333333333, test.getSlope(), EPS);
        assertEquals("y0",    8.333333333333333333333333, test.getY0(),    EPS);

        final double distance = 40;
        final Point2D summit = new Point2D.Double(27, -9); // An arbitrary point.
        final Line2D base = test.isoscelesTriangleBase(summit, distance);
        assertEquals("distance P1", distance, base.getP1().distance(summit), EPS);
        assertEquals("distance P2", distance, base.getP2().distance(summit), EPS);

        final double x=10; // Can be any arbitrary point.
        final double y=8;
        assertEquals("nearest colinear point", base.ptLineDist(x,y),
                     test.nearestColinearPoint(new Point2D.Double(x,y)).distance(x,y), EPS);
    }

    /**
     * Tests {@link Plane#setPlane} methods.
     */
    @Test
    public void testPlaneFit() {
        final Random  rd = new Random(457821698762354L);
        final Plane plan = new Plane();
        final Point3d P1 = new Point3d(100*rd.nextDouble()+25, 100*rd.nextDouble()+25, Math.rint(100*rd.nextDouble()+40));
        final Point3d P2 = new Point3d(100*rd.nextDouble()+25, 100*rd.nextDouble()+25, Math.rint(100*rd.nextDouble()+40));
        final Point3d P3 = new Point3d(100*rd.nextDouble()+25, 100*rd.nextDouble()+25, Math.rint(100*rd.nextDouble()+40));
        plan.setPlane(P1, P2, P3);
        assertEquals("P1", P1.z, plan.z(P1.x,P1.y), EPS);
        assertEquals("P2", P2.z, plan.z(P2.x,P2.y), EPS);
        assertEquals("P3", P3.z, plan.z(P3.x,P3.y), EPS);

        final double[] x = new double[4000];
        final double[] y = new double[4000];
        final double[] z = new double[4000];
        for (int i=0; i<z.length; i++) {
            x[i] = 40 + 100*rd.nextDouble();
            y[i] = 40 + 100*rd.nextDouble();
            z[i] = plan.z(x[i], y[i]) + 10*rd.nextDouble()-5;
        }
        final Plane copy = plan.clone();
        final double eps = 1E-2; // We do expect some difference, but not much more than that.
        assertEquals("c",  copy.c,  plan.c,  eps);
        assertEquals("cx", copy.cx, plan.cx, eps);
        assertEquals("cy", copy.cy, plan.cy, eps);
    }
}
