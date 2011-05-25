/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.awt.Color;
import java.util.ArrayList;

import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Interpolate function.
 *
 * @author Michael Bedward
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/test/java/org/geotools/filter/function/InterpolateFunctionTest.java $
 */
public class InterpolateFunctionTest extends SEFunctionTestBase {

    private static final double TOL = 1.0e-6d;
    private final Double[] data   = { 10.0, 20.0, 40.0, 80.0 };
    private final Double[] values = {  1.0,  2.0,  3.0,  4.0 };
    private final Color[] colors = { Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE };

    @Before
    public void setup() {
        parameters = new ArrayList<Expression>();
    }


    @Test
    public void testFindInterpolateFunction() throws Exception {
        System.out.println("   testFindInterpolateFunction");

        Literal fallback = ff2.literal("NOT_FOUND");
        setupParameters(data, values);
        Function fn = finder.findFunction("interpolate", parameters, fallback);
        Object result = fn.evaluate(feature(0));

        assertFalse("Could not locate 'interpolate' function", result.equals(fallback.getValue()));
    }

    @Test
    public void testLinearNumericInterpolation() throws Exception {
        System.out.println("   testLinearNumericInterpolation");

        setupParameters(data, values);
        parameters.add(ff2.literal(InterpolateFunction.METHOD_NUMERIC));
        parameters.add(ff2.literal(InterpolateFunction.MODE_LINEAR));

        Function fn = finder.findFunction("interpolate", parameters);

        // test mid-points
        Double result;
        double expected;
        for (int i = 1; i < data.length; i++) {
            double testValue = (data[i] + data[i-1]) / 2.0;
            result = fn.evaluate(feature(Double.valueOf(testValue)), Double.class);
            expected = (values[i] + values[i-1]) / 2.0;
            assertEquals(expected, result, TOL);
        }

        // test boundaries
        for (int i = 0; i < data.length; i++) {
            result = fn.evaluate(feature(Double.valueOf(data[i])), Double.class);
            expected = values[i];
            assertEquals(expected, result, TOL);
        }

        // test outside range of interpolation points
        result = fn.evaluate(feature(Double.valueOf(data[0] - 10)), Double.class);
        assertEquals(values[0], result, TOL);

        result = fn.evaluate(feature(Double.valueOf(data[data.length - 1] + 10)), Double.class);
        assertEquals(values[values.length - 1], result, TOL);
    }

    @Test
    public void testLinearColorInterpolation() throws Exception {
        System.out.println("   testLinearColorInterpolation");

        setupParameters(data, colors);
        parameters.add(ff2.literal(InterpolateFunction.METHOD_COLOR));
        parameters.add(ff2.literal(InterpolateFunction.MODE_LINEAR));

        Function fn = finder.findFunction("interpolate", parameters);
        Color result = null;

        // at mid-points
        for (int i = 1; i < data.length; i++) {
            double testValue = (data[i] + data[i-1]) / 2.0;
            result = fn.evaluate(feature(testValue), Color.class);
            Color expected = new Color(
                    (int) Math.round((colors[i].getRed() + colors[i-1].getRed()) / 2.0),
                    (int) Math.round((colors[i].getGreen() + colors[i-1].getGreen()) / 2.0),
                    (int) Math.round((colors[i].getBlue() + colors[i-1].getBlue()) / 2.0) );
            assertEquals(expected, result);
        }

        // at interpolation points
        for (int i = 0; i < data.length; i++) {
            result = fn.evaluate(feature(data[i]), Color.class);
            assertEquals(colors[i], result);
        }

        // outside range of interpolation points
        result = fn.evaluate(feature(Double.valueOf(data[0] - 10)), Color.class);
        assertEquals(colors[0], result);

        result = fn.evaluate(feature(Double.valueOf(data[data.length - 1] + 10)), Color.class);
        assertEquals(colors[colors.length - 1], result);
    }

    @Test
    public void testCosineNumericInterpolation() throws Exception {
        System.out.println("   testCosineNumericInterpolation");

        setupParameters(data, values);
        parameters.add(ff2.literal(InterpolateFunction.METHOD_NUMERIC));
        parameters.add(ff2.literal(InterpolateFunction.MODE_COSINE));

        Function fn = finder.findFunction("interpolate", parameters);

        // test points within segments away from mid-points
        double t = 0.1;
        Double result;
        double expected;
        for (int i = 1; i < data.length; i++) {
            double testValue = data[i-1] + t * (data[i] - data[i-1]);
            result = fn.evaluate(feature(Double.valueOf(testValue)), Double.class);

            expected = values[i-1] + (values[i] - values[i-1]) * (1.0 - Math.cos(t * Math.PI)) * 0.5;
            assertEquals(expected, result, TOL);
        }

        // test boundaries
        for (int i = 0; i < data.length; i++) {
            result = fn.evaluate(feature(Double.valueOf(data[i])), Double.class);
            expected = values[i];
            assertEquals(expected, result, TOL);
        }

        // test outside range of interpolation points
        result = fn.evaluate(feature(Double.valueOf(data[0] - 10)), Double.class);
        assertEquals(values[0], result, TOL);

        result = fn.evaluate(feature(Double.valueOf(data[data.length - 1] + 10)), Double.class);
        assertEquals(values[values.length - 1], result, TOL);
    }

    @Test
    public void testCubicNumericInterpolation() throws Exception {
        System.out.println("   testCubicNumericInterpolation");

        setupParameters(data, values);
        parameters.add(ff2.literal(InterpolateFunction.METHOD_NUMERIC));
        parameters.add(ff2.literal(InterpolateFunction.MODE_CUBIC));

        Function fn = finder.findFunction("interpolate", parameters);

        // test points within segments away from mid-points
        double t = 0.1;
        Double result;
        double expected;
        for (int i = 2; i < data.length - 2; i++) {
            double testValue = data[i-1] + t * (data[i] - data[i-1]);
            result = fn.evaluate(feature(Double.valueOf(testValue)), Double.class);

            expected = cubic(testValue,
                    new double[]{data[i-2], data[i-1], data[i], data[i+1]},
                    new double[]{values[i-2], values[i-1], values[i], values[i+1]});
            assertEquals(expected, result, TOL);
        }

        // test outside range of interpolation points
        result = fn.evaluate(feature(Double.valueOf(data[0] - 10)), Double.class);
        assertEquals(values[0], result, TOL);

        result = fn.evaluate(feature(Double.valueOf(data[data.length - 1] + 10)), Double.class);
        assertEquals(values[values.length - 1], result, TOL);
    }

    @Test
    public void testAsRasterData() throws Exception {
        System.out.println("   testRasterData");

        setupParameters(data, colors);
        parameters.set(0, ff2.literal("RasterData"));
        parameters.add(ff2.literal(InterpolateFunction.METHOD_COLOR));
        parameters.add(ff2.literal(InterpolateFunction.MODE_LINEAR));

        Function fn = finder.findFunction("interpolate", parameters);
        Color result = null;

        // at mid-points
        for (int i = 1; i < data.length; i++) {
            double rasterValue = (data[i] + data[i-1]) / 2.0;
            result = fn.evaluate(rasterValue, Color.class);
            Color expected = new Color(
                    (int) Math.round((colors[i].getRed() + colors[i-1].getRed()) / 2.0),
                    (int) Math.round((colors[i].getGreen() + colors[i-1].getGreen()) / 2.0),
                    (int) Math.round((colors[i].getBlue() + colors[i-1].getBlue()) / 2.0) );
            assertEquals(expected, result);
        }

        // at interpolation points
        for (int i = 0; i < data.length; i++) {
            result = fn.evaluate(data[i], Color.class);
            assertEquals(colors[i], result);
        }

        // outside range of interpolation points
        result = fn.evaluate(Double.valueOf(data[0] - 10), Color.class);
        assertEquals(colors[0], result);

        result = fn.evaluate(Double.valueOf(data[data.length - 1] + 10), Color.class);
        assertEquals(colors[colors.length - 1], result);
    }

    @Test
    public void testForOutOfRangeColorValues() {
        System.out.println("   out of range color values");

        parameters = new ArrayList<Expression>();
        parameters.add(ff2.literal("RasterData"));

        // Create interpolation points that will lead to a cubic
        // curve going out of range: the unclamped curve will dip
        // below 0 betwee points 1 and 2 and go above 255 between
        // points 3 and 4
        double[] x = {   0, 1, 2,   3,   4,   5 };
        int[] reds = { 128, 0, 0, 255, 255, 128 };

        for (int i = 0; i < x.length; i++) {
            parameters.add(ff2.literal(x[i]));
            String color = String.format("#%02x0000", reds[i]);
            parameters.add(ff2.literal(color));
        }

        parameters.add(ff2.literal(InterpolateFunction.METHOD_COLOR));
        parameters.add(ff2.literal(InterpolateFunction.MODE_CUBIC));

        Function fn = finder.findFunction("interpolate", parameters);

        // check between points 1 and 2
        Color result = fn.evaluate(Double.valueOf(1.5), Color.class);
        assertEquals(0, result.getRed());

        // check between points 3 and 4
        result = fn.evaluate(Double.valueOf(3.5), Color.class);
        assertEquals(255, result.getRed());
    }

    @Test
    public void testNoMethodParameter() throws Exception {
        System.out.println("   testNoMethodParameter");

        setupParameters(data, values);
        parameters.add(ff2.literal(InterpolateFunction.MODE_LINEAR));

        Function fn = finder.findFunction("interpolate", parameters);

        // test mid-points
        Double result;
        double expected;
        for (int i = 1; i < data.length; i++) {
            double testValue = (data[i] + data[i-1]) / 2.0;
            result = fn.evaluate(feature(Double.valueOf(testValue)), Double.class);
            expected = (values[i] + values[i-1]) / 2.0;
            assertEquals(expected, result, TOL);
        }
    }

    @Test
    public void testNoModeParameter() throws Exception {
        System.out.println("   testNoModeParameter");

        setupParameters(data, values);
        parameters.add(ff2.literal(InterpolateFunction.METHOD_NUMERIC));

        Function fn = finder.findFunction("interpolate", parameters);

        // test mid-points
        Double result;
        double expected;
        for (int i = 1; i < data.length; i++) {
            double testValue = (data[i] + data[i-1]) / 2.0;
            result = fn.evaluate(feature(Double.valueOf(testValue)), Double.class);
            expected = (values[i] + values[i-1]) / 2.0;
            assertEquals(expected, result, TOL);
        }
    }

    @Test
    public void testColorValuesNumericMethodMismatch() throws Exception {
        System.out.println("   testColorValuesNumericMethodMismatch");

        /*
         * Set interpolation points but let the function default to
         * linear / numeric
         */
        setupParameters(data, colors);

        Function fn = finder.findFunction("interpolate", parameters);
        boolean gotEx = false;
        try {
            fn.evaluate(feature(data[1]), Color.class);
        } catch (IllegalArgumentException ex) {
            gotEx = true;
        }

        assertTrue(gotEx);
    }

    @Test
    public void testNumericValuesColorMethodMismatch() throws Exception {
        System.out.println("   testNumericValuesColorMethodMismatch");

        setupParameters(data, values);
        parameters.add(ff2.literal(InterpolateFunction.METHOD_COLOR));

        Function fn = finder.findFunction("interpolate", parameters);
        boolean gotEx = false;
        try {
            fn.evaluate(feature(data[1]), Double.class);
        } catch (IllegalArgumentException ex) {
            gotEx = true;
        }

        assertTrue(gotEx);
    }

    /**
     * Set up parameters for the Interpolate function with a set of
     * input data and output values
     */
    private void setupParameters(Object[] data, Object[] values) {

        if (data.length != values.length) {
            throw new IllegalArgumentException("data and values arrays should be the same length");
        }

        parameters = new ArrayList<Expression>();
        parameters.add(ff2.property("value"));

        for (int i = 0; i < data.length; i++) {
            parameters.add(ff2.literal(data[i]));
            parameters.add(ff2.literal(values[i]));
        }
    }

    private static double cubic(double x, double[] xi, double[] yi) {
        double span01 = xi[1] - xi[0];
        double span12 = xi[2] - xi[1];
        double span23 = xi[3] - xi[2];

        double t = (x - xi[1]) / span12;
        double t2 = t*t;
        double t3 = t2 * t;

        double m1 = 0.5 * ((yi[2] - yi[1]) / span12 + (yi[1] - yi[0]) / span01);
        double m2 = 0.5 * ((yi[3] - yi[2]) / span23 + (yi[2] - yi[1]) / span12);

        double y = (2*t3 - 3*t2 + 1) * yi[1] +
                   (t3 - 2*t2 + t) * span12 * m1 +
                   (-2*t3 + 3*t2) * yi[2] +
                   (t3 - t2) * span12 * m2;

        return y;
    }
}
