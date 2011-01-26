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
package org.geotools.referencing.operation;

import java.util.Random;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.quantity.Length;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.Matrix;

import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.operation.matrix.Matrix2;
import org.geotools.referencing.operation.matrix.Matrix3;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
import static org.geotools.referencing.cs.DefaultCartesianCS.PROJECTED;
import static org.opengis.referencing.cs.AxisDirection.*;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests some operation steps involved in coordinate operation creation.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class LinearConversionTest {
    /**
     * Tests matrix inversion and multiplication using {@link Matrix2}.
     */
    @Test
    public void testMatrix2() {
        final Matrix2 m = new Matrix2();
        assertTrue(m.isAffine());
        assertTrue(m.isIdentity());
        final Random random = new Random(8447482612423035360L);
        final GeneralMatrix identity = new GeneralMatrix(2);
        for (int i=0; i<100; i++) {
            m.setElement(0,0, 100*random.nextDouble());
            m.setElement(0,1, 100*random.nextDouble());
            m.setElement(1,0, 100*random.nextDouble());
            m.setElement(1,1, 100*random.nextDouble());
            final Matrix2 original = m.clone();
            final GeneralMatrix check = new GeneralMatrix(m);
            m.invert();
            check.invert();
            assertTrue(check.equals(m, 1E-9));
            m.multiply(original);
            assertTrue(identity.equals(m, 1E-9));
        }
    }

    /**
     * Tests axis swapping using {@link GeneralMatrix}.
     */
    @Test
    public void testAxisSwapping() {
        AxisDirection[] srcAxis = {NORTH, EAST, UP};
        AxisDirection[] dstAxis = {NORTH, EAST, UP};
        GeneralMatrix   matrix  = new GeneralMatrix(srcAxis, dstAxis);
        assertTrue(matrix.isAffine  ());
        assertTrue(matrix.isIdentity());
        dstAxis = new AxisDirection[] {WEST, UP, SOUTH};
        matrix  = new GeneralMatrix(srcAxis, dstAxis);
        assertTrue (matrix.isAffine  ());
        assertFalse(matrix.isIdentity());
        assertEquals(new GeneralMatrix(new double[][] {
            { 0,-1, 0, 0},
            { 0, 0, 1, 0},
            {-1, 0, 0, 0},
            { 0, 0, 0, 1}
        }), matrix);
        dstAxis = new AxisDirection[] {DOWN, NORTH};
        matrix  = new GeneralMatrix(srcAxis, dstAxis);
        assertFalse(matrix.isIdentity());
        assertEquals(new GeneralMatrix(new double[][] {
            {0, 0,-1, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 1}
        }), matrix);
        dstAxis = new AxisDirection[] {DOWN, DOWN};
        matrix  = new GeneralMatrix(srcAxis, dstAxis);
        assertFalse(matrix.isIdentity());
        assertEquals(new GeneralMatrix(new double[][] {
            {0, 0,-1, 0},
            {0, 0,-1, 0},
            {0, 0, 0, 1}
        }), matrix);
        dstAxis = new AxisDirection[] {DOWN, GEOCENTRIC_X};
        try {
            matrix = new GeneralMatrix(srcAxis, dstAxis);
            fail();
        } catch (IllegalArgumentException exception) {
            // This is the expected exception (axis not in source).
        }
        srcAxis = dstAxis;
        dstAxis = new AxisDirection[] {NORTH, EAST, UP, WEST};
        try {
            matrix = new GeneralMatrix(srcAxis, dstAxis);
            fail();
        } catch (IllegalArgumentException exception) {
            // This is the expected exception (colinear axis).
        }
    }

    /**
     * Tests an example similar to the one provided in the
     * {@link AbstractCS#testScaleAndSwapAxis} javadoc.
     */
    @Test
    public void testScaleAndSwapAxis() {
        final Unit<Length> cm = SI.CENTI(SI.METER);
        final Unit<Length> mm = SI.MILLI(SI.METER);
        final AbstractCS cs = new DefaultCartesianCS("Test",
              new DefaultCoordinateSystemAxis("y", SOUTH, cm),
              new DefaultCoordinateSystemAxis("x", EAST,  mm));
        Matrix matrix;
        matrix = AbstractCS.swapAndScaleAxis(DefaultCartesianCS.GENERIC_2D, cs);
        assertEquals(new GeneralMatrix(new double[][] {
            {0,  -100,    0},
            {1000,  0,    0},
            {0,     0,    1}
        }), matrix);
        matrix = AbstractCS.swapAndScaleAxis(DefaultCartesianCS.GENERIC_3D, cs);
        assertEquals(new GeneralMatrix(new double[][] {
            {0,  -100,   0,   0},
            {1000,  0,   0,   0},
            {0,     0,   0,   1}
        }), matrix);
    }

    /**
     * Test the {@link DefaultProjectedCRS#createLinearConversion} method.
     * Note: this requires a working {@link MathTransformFactory}.
     *
     * @throws FactoryException If the conversion can't be created.
     */
    @Test
    public void testCreateLinearConversion() throws FactoryException {
        final double                     EPS = 1E-12;
        final MathTransformFactory   factory = new DefaultMathTransformFactory();
        final ParameterValueGroup parameters = factory.getDefaultParameters("Mercator_1SP");
        DefaultProjectedCRS sourceCRS, targetCRS;
        MathTransform transform;
        Matrix conversion;

        parameters.parameter("semi_major").setValue(DefaultEllipsoid.WGS84.getSemiMajorAxis());
        parameters.parameter("semi_minor").setValue(DefaultEllipsoid.WGS84.getSemiMinorAxis());
        transform = factory.createParameterizedTransform(parameters);
        sourceCRS = new DefaultProjectedCRS("source", WGS84, transform, PROJECTED);

        parameters.parameter("false_easting" ).setValue(1000);
        parameters.parameter("false_northing").setValue(2000);
        transform = factory.createParameterizedTransform(parameters);
        targetCRS = new DefaultProjectedCRS("source", WGS84, transform, PROJECTED);

        conversion = ProjectionAnalyzer.createLinearConversion(sourceCRS, targetCRS, EPS);
        assertEquals(new Matrix3(
            1,  0,  1000,
            0,  1,  2000,
            0,  0,     1
        ), conversion);

        parameters.parameter("scale_factor").setValue(2);
        transform = factory.createParameterizedTransform(parameters);
        targetCRS = new DefaultProjectedCRS("source", WGS84, transform, PROJECTED);

        conversion = ProjectionAnalyzer.createLinearConversion(sourceCRS, targetCRS, EPS);
        assertEquals(new Matrix3(
            2,  0,  1000,
            0,  2,  2000,
            0,  0,     1
        ), conversion);

        parameters.parameter("semi_minor").setValue(DefaultEllipsoid.WGS84.getSemiMajorAxis());
        transform = factory.createParameterizedTransform(parameters);
        targetCRS = new DefaultProjectedCRS("source", WGS84, transform, PROJECTED);
        conversion = ProjectionAnalyzer.createLinearConversion(sourceCRS, targetCRS, EPS);
        assertNull(conversion);
    }
}
