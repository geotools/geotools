/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation;

import java.util.Random;

import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;

import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.ReferencingFactoryFinder;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Base class for transform test cases. This class is not a test in itself;
 * only subclasses will be.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class TransformTestBase {
    /**
     * Small values for comparaisons of floating point numbers after transformations.
     */
    private static final double EPS = 1E-6;

    /**
     * The default datum factory.
     */
    protected DatumFactory datumFactory;

    /**
     * The default coordinate reference system factory.
     */
    protected CRSFactory crsFactory;

    /**
     * The default math transform factory.
     */
    protected MathTransformFactory mtFactory;

    /**
     * The default transformations factory.
     */
    protected CoordinateOperationFactory opFactory;

    /**
     * Random numbers generator.
     */
    protected static final Random random = new Random(-3531834320875149028L);

    /**
     * Setup the factories using the hints provided by {@link #getHintsForTesting}.
     */
    @Before
    public final void setUpFactories() {
        Hints hints  = getHintsForTesting();
        datumFactory = ReferencingFactoryFinder.getDatumFactory(hints);
        crsFactory   = ReferencingFactoryFinder.getCRSFactory(hints);
        mtFactory    = ReferencingFactoryFinder.getMathTransformFactory(hints);
        opFactory    = ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
    }

    /**
     * Returns the hints to be used by {@link #setUp} in order to fetch the factories.
     * The default implementation returns {@code null}. Subclasses can override.
     */
    protected Hints getHintsForTesting() {
        return null;
    }

    /**
     * Returns <code>true</code> if the specified number is real
     * (neither NaN or infinite).
     */
    public static boolean isReal(final double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    /**
     * Verify that the specified transform implements {@link MathTransform1D}
     * or {@link MathTransform2D} as needed.
     *
     * @param transform The transform to test.
     */
    public static void assertInterfaced(final MathTransform transform) {
        int dim = transform.getSourceDimensions();
        if (transform.getTargetDimensions() != dim) {
            dim = 0;
        }
        assertTrue("MathTransform1D", (dim==1) == (transform instanceof MathTransform1D));
        assertTrue("MathTransform2D", (dim==2) == (transform instanceof MathTransform2D));
    }

    /**
     * Transforms a two-dimensional point and compare the result with the expected value.
     *
     * @param transform The transform to test.
     * @param  x The x value to transform.
     * @param  y The y value to transform.
     * @param ex The expected x value.
     * @param ey The expected y value.
     */
    public static void assertTransformEquals2_2(final MathTransform transform,
                                                final double  x, final double  y,
                                                final double ex, final double ey)
            throws TransformException
    {
        final DirectPosition2D source = new DirectPosition2D(x,y);
        final DirectPosition2D target = new DirectPosition2D();
        assertSame(target, transform.transform(source, target));
        final String message = "Expected ("+ex+", "+ey+"), "+
                               "transformed=("+target.x+", "+target.y+")";
        assertEquals(message, ex, target.x, EPS);
        assertEquals(message, ey, target.y, EPS);
    }

    /**
     * Transforms a three-dimensional point and compare the result with the expected value.
     *
     * @param transform The transform to test.
     * @param  x The x value to transform.
     * @param  y The y value to transform.
     * @param  z The z value to transform.
     * @param ex The expected x value.
     * @param ey The expected y value.
     * @param ez The expected z value.
     */
    public static void assertTransformEquals3_3(final MathTransform transform,
                                                final double  x, final double  y, final double  z,
                                                final double ex, final double ey, final double ez)
            throws TransformException
    {
        final GeneralDirectPosition source = new GeneralDirectPosition(x,y,z);
        final GeneralDirectPosition target = new GeneralDirectPosition(3);
        assertSame(target, transform.transform(source, target));
        final String message = "Expected ("+ex+", "+ey+", "+ez+"), "+
              "transformed=("+target.ordinates[0]+", "+target.ordinates[1]+", "+target.ordinates[2]+")";
        assertEquals(message, ex, target.ordinates[0], EPS);
        assertEquals(message, ey, target.ordinates[1], EPS);
        assertEquals(message, ez, target.ordinates[2], 1E-2); // Greater tolerance level for Z.
    }

    /**
     * Transforms a two-dimensional point and compare the result with the expected
     * three-dimensional value.
     *
     * @param transform The transform to test.
     * @param  x The x value to transform.
     * @param  y The y value to transform.
     * @param ex The expected x value.
     * @param ey The expected y value.
     * @param ez The expected z value.
     */
    public static void assertTransformEquals2_3(final MathTransform transform,
                                                final double  x, final double  y,
                                                final double ex, final double ey, final double ez)
            throws TransformException
    {
        final GeneralDirectPosition source = new GeneralDirectPosition(x,y);
        final GeneralDirectPosition target = new GeneralDirectPosition(3);
        assertSame(target, transform.transform(source, target));
        final String message = "Expected ("+ex+", "+ey+", "+ez+"), "+
              "transformed=("+target.ordinates[0]+", "+target.ordinates[1]+", "+target.ordinates[2]+")";
        assertEquals(message, ex, target.ordinates[0], EPS);
        assertEquals(message, ey, target.ordinates[1], EPS);
        assertEquals(message, ez, target.ordinates[2], 1E-2); // Greater tolerance level for Z.
    }

    /**
     * Transforms a three-dimensional point and compare the result with the expected
     * two-dimensional value.
     *
     * @param transform The transform to test.
     * @param  x The x value to transform.
     * @param  y The y value to transform.
     * @param  z The z value to transform.
     * @param ex The expected x value.
     * @param ey The expected y value.
     */
    public static void assertTransformEquals3_2(final MathTransform transform,
                                                final double  x, final double  y, final double  z,
                                                final double ex, final double ey)
            throws TransformException
    {
        final GeneralDirectPosition source = new GeneralDirectPosition(x,y,z);
        final GeneralDirectPosition target = new GeneralDirectPosition(2);
        assertSame(target, transform.transform(source, target));
        final String message = "Expected ("+ex+", "+ey+"), "+
              "transformed=("+target.ordinates[0]+", "+target.ordinates[1]+")";
        assertEquals(message, ex, target.ordinates[0], EPS);
        assertEquals(message, ey, target.ordinates[1], EPS);
    }

    /**
     * Transforms a three-dimensional point and compare the result with the expected
     * one-dimensional value.
     *
     * @param transform The transform to test.
     * @param  x The x value to transform.
     * @param  y The y value to transform.
     * @param  z The z value to transform.
     * @param ez The expected z value.
     */
    public static void assertTransformEquals3_1(final MathTransform transform,
                                                final double  x, final double  y, final double  z,
                                                                                  final double ez)
            throws TransformException
    {
        final GeneralDirectPosition source = new GeneralDirectPosition(x,y,z);
        final GeneralDirectPosition target = new GeneralDirectPosition(1);
        assertSame(target, transform.transform(source, target));
        final String message = "Expected ("+ez+"), "+
              "transformed=("+target.ordinates[0]+")";
        assertEquals(message, ez, target.ordinates[0], 1E-2); // Greater tolerance level for Z.
    }

    /**
     * Compare two arrays of points.
     *
     * @param name      The name of the comparaison to be performed.
     * @param expected  The expected array of points.
     * @param actual    The actual array of points.
     * @param delta     The maximal difference tolerated in comparaisons for each dimension.
     *                  This array length must be equal to coordinate dimension (usually 1, 2 or 3).
     */
    public static void assertPointsEqual(final String   name,
                                         final double[] expected,
                                         final double[] actual,
                                         final double[] delta)
    {
        final int dimension = delta.length;
        final int stop = Math.min(expected.length, actual.length)/dimension * dimension;
        assertEquals("Array length for expected points", stop, expected.length);
        assertEquals("Array length for actual points",   stop,   actual.length);
        final StringBuilder buffer = new StringBuilder(name);
        buffer.append(": point[");
        final int start = buffer.length();
        for (int i=0; i<stop; i++) {
            buffer.setLength(start);
            buffer.append(i / dimension).append(", dimension ")
                  .append(i % dimension).append(" of ")
                  .append(    dimension).append(']');
            if (isReal(expected[i])) {
                // The "two steps" method in ConcatenatedTransformTest sometime produces
                // random NaN numbers. This "two steps" is used only for comparaison purpose;
                // the "real" (tested) method work better.
                assertEquals(buffer.toString(), expected[i], actual[i], delta[i % dimension]);
            }
        }
    }

    /**
     * Quick self test, in part to give this test suite a test
     * and also to test the internal method.
     */
    @Test
    public void testAssertPointsEqual(){
        String name = "self test";
        double a[]     = {10,   10  };
        double b[]     = {10.1, 10.1};
        double delta[] = { 0.2,  0.2};
        assertPointsEqual(name, a, b, delta);
    }
}
