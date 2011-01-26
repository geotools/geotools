/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.media.jai.Warp;
import javax.media.jai.WarpAffine;
import javax.media.jai.WarpQuadratic;
import javax.media.jai.WarpPolynomial;

import org.opengis.referencing.operation.TransformException;
import org.geotools.resources.Classes;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link WarpTransform2D} and {@link WarpAdapter} classes.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class WarpTransformTest {
    /**
     * Width and height of a pseudo-image source image.
     */
    private static final int WIDTH=1000, HEIGHT=2000;

    /**
     * Transforms in place a point. This is used for testing
     * affine, quadratic and cubic warp from know formulas.
     */
    private static interface Formula {
        String message();
        void transform(Point point);
    }

    /**
     * Constructs a warp and tests the transformations. Coefficients will be tested later
     * (by the caller).
     */
    private static WarpPolynomial executeTest(final Formula formula, final int degree, final float EPS)
            throws TransformException
    {
        /*
         * Creates a set of points and transform them according the formula supplied in argument.
         */
        final Random   random = new Random(-854734760285695284L);
        final Point[] sources = new Point[100];
        final Point[]    dest = new Point[sources.length];
        for (int i=0; i<dest.length; i++) {
            Point p;
            sources[i] = p = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
            dest   [i] = p = new Point(p);
            formula.transform(p);
        }
        /*
         * Gets the transform. We specify a bounding box which contains all points.
         */
        final Point ext = new Point(WIDTH,HEIGHT);
        formula.transform(ext);
        final WarpTransform2D transform = new WarpTransform2D(
                new Rectangle(0, 0, WIDTH, HEIGHT), sources, 0,
                new Rectangle(0, 0, ext.x, ext.y),  dest,    0,
                sources.length, degree);
        final WarpTransform2D inverse = (WarpTransform2D) transform.inverse();
        assertNotNull("WKT formatting test", transform.toString());
        /*
         * Checks Warp properties.
         */
        final Warp warp = transform.getWarp();
        assertTrue("Expected a polynomial warp but got " + Classes.getShortClassName(warp),
                   warp instanceof WarpPolynomial);
        final WarpPolynomial poly = (WarpPolynomial) warp;
        /*
         * Compares transformations to the expected points.
         */
        for (int i=0; i<sources.length; i++) {
            final String  message  = formula.message() + " Point #" + i;
            final Point   source   = sources[i];
            final Point   expected = dest   [i];
            final Point2D computed = new Point2D.Double(source.x, source.y);
            assertSame  (message, computed, transform.transform(computed, computed));
            assertEquals(message, expected.x, computed.getX(), EPS*expected.x);
            assertEquals(message, expected.y, computed.getY(), EPS*expected.y);
            //
            // Try using transform(float[], ...)
            //
            if (true) {
                final float[] array = new float[] {source.x, source.y};
                transform.transform(array, 0, array, 0, 1);
                assertEquals(message, expected.x, array[0], EPS*expected.x);
                assertEquals(message, expected.y, array[1], EPS*expected.y);
            }
            //
            // Try using transform(double[], ...)
            //
            if (true) {
                final double[] array = new double[] {source.x, source.y};
                transform.transform(array, 0, array, 0, 1);
                assertEquals(message, expected.x, array[0], EPS*expected.x);
                assertEquals(message, expected.y, array[1], EPS*expected.y);
            }
            //
            // Tests inverse transform
            //
            if (degree == 1) {
                computed.setLocation(expected.x, expected.y);
                assertSame  (message, computed, inverse.transform(computed, computed));
                assertEquals(message, source.x, computed.getX(), EPS*expected.x);
                assertEquals(message, source.y, computed.getY(), EPS*expected.y);
            }
        }
        return poly;
    }

    /**
     * Tests an affine warp.
     */
    @Test
    public void testAffine() throws TransformException {
        final int[] scalesX = {1,2,3,4,5,6,  2,7,3,1,8};
        final int[] scalesY = {1,2,3,4,5,6,  6,2,5,9,1};
        for (int i=0; i<scalesX.length; i++) {
            final int scaleX = scalesX[i];
            final int scaleY = scalesY[i];
            final WarpPolynomial warp = executeTest(new Formula() {
                public String message() {
                    return "WarpAffine[" + scaleX + ',' + scaleY + ']';
                }
                public void transform(final Point point) {
                    point.x *= scaleX;
                    point.y *= scaleY;
                }
            }, 1, 1E-5f);
            assertTrue("Expected an affine warp but got " + Classes.getShortClassName(warp),
                       warp instanceof WarpAffine);
        }
    }

    /**
     * Tests a quadratic warp.
     */
    @Test
    public void testQuadratic() throws TransformException {
        final int[] scalesX = {1,2,3,4,5,6,  2,7,3,1,8};
        final int[] scalesY = {1,2,3,4,5,6,  6,2,5,9,1};
        for (int i=0; i<scalesX.length; i++) {
            final int scaleX = scalesX[i];
            final int scaleY = scalesY[i];
            final WarpPolynomial warp = executeTest(new Formula() {
                public String message() {
                    return "WarpQuadratic[" + scaleX + ',' + scaleY + ']';
                }
                public void transform(final Point point) {
                    point.x *= scaleX*point.x;
                    point.y *= scaleY;
                }
            }, 2, 1E-2f);
            assertTrue("Expected a quatratic warp but got " + Classes.getShortClassName(warp),
                       warp instanceof WarpQuadratic);
        }
    }

    /**
     * Tests the {@link WarpAdapter} class using an affine transform.
     */
    @Test
    public void testAdapter() {
        final AffineTransform atr = AffineTransform.getScaleInstance(0.25, 0.5);
        atr.translate(4, 2);
        final AffineTransform2D transform = new AffineTransform2D(atr);
        final WarpAffine        warp      = new WarpAffine       (atr);
        final WarpAdapter       adapter   = new WarpAdapter("test", transform);
        final Random            random    = new Random(-854734760285695284L);
        for (int i=0; i<200; i++) {
            Point2D source   = new Point2D.Double(random.nextDouble()*100, random.nextDouble()*100);
            Point2D expected = warp   .mapDestPoint(source);
            Point2D computed = adapter.mapDestPoint(source);
            assertEquals("X", expected.getX(), computed.getX(), 1E-5);
            assertEquals("Y", expected.getY(), computed.getY(), 1E-5);

            // Try inverse transform.
            expected = warp   .mapSourcePoint(source);
            computed = adapter.mapSourcePoint(source);
            assertEquals("X", expected.getX(), computed.getX(), 1E-5);
            assertEquals("Y", expected.getY(), computed.getY(), 1E-5);

            // Try warpPoint
            final float[] exp = warp   .warpPoint((int)source.getX(), (int)source.getY(), null);
            final float[] com = adapter.warpPoint((int)source.getX(), (int)source.getY(), null);
            assertEquals("X", exp[0], com[0], 1E-5);
            assertEquals("Y", exp[1], com[1], 1E-5);
        }
    }
}
