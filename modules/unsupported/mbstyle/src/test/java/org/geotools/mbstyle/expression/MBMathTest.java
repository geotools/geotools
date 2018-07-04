/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.expression;

import static org.junit.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.junit.Test;

/** Tests the various MapBox Math expressions. */
public class MBMathTest extends AbstractMBExpressionTest {

    private static final double TOLERANCE = 0.0001d;

    @Override
    protected String getTestResourceName() {
        return "expressionMBMathTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBMath.class;
    }

    private void _assertEquals(double expected, double actual) {
        assertEquals(expected, actual, TOLERANCE);
    }

    @Test
    public void testPi() throws Exception {
        final JSONObject j = getObjectByLayerId("mathPi", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.PI,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "pi", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testE() throws Exception {
        final JSONObject j = getObjectByLayerId("mathE", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.E,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "e", testFeatures[i]).toString()));
        }
    }

    @Test
    public void test_ln2() throws Exception {
        final JSONObject j = getObjectByLayerId("mathln2", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.log(2),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "naturalLog2", testFeatures[i]).toString()));
        }
    }

    @Test
    public void test_ln() throws Exception {
        final JSONObject j = getObjectByLayerId("mathln", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.log(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "naturalLog", testFeatures[i]).toString()));
        }
    }

    @Test
    public void test_log10() throws Exception {
        final JSONObject j = getObjectByLayerId("mathlog10", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.log10(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "logBase10", testFeatures[i]).toString()));
        }
    }

    @Test
    public void test_log2() throws Exception {
        final JSONObject j = getObjectByLayerId("mathlog2", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.log(intVals[i]) / Math.log(2),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "logBase2", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testSqrt() throws Exception {
        final JSONObject j = getObjectByLayerId("mathSqrt", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    2d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "sqrt4", testFeatures[i]).toString()));
            _assertEquals(
                    10d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "sqrt100", testFeatures[i]).toString()));
            _assertEquals(
                    Math.sqrt(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "sqrtWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testAdd() throws Exception {
        final JSONObject j = getObjectByLayerId("mathAdd", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    100d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "add1", testFeatures[i]).toString()));
            _assertEquals(
                    3d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "add2", testFeatures[i]).toString()));
            _assertEquals(
                    7d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "add3", testFeatures[i]).toString()));
            _assertEquals(
                    -5d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "addNegative", testFeatures[i]).toString()));
            _assertEquals(
                    54d + intVals[i],
                    Double.parseDouble(
                            getExpressionEvaluation(j, "addWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testSubtract() throws Exception {
        final JSONObject j = getObjectByLayerId("mathSubtract", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    6d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "subtract", testFeatures[i]).toString()));
            _assertEquals(
                    -2d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "subtractLargeFromSmall", testFeatures[i])
                                    .toString()));
            _assertEquals(
                    3d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "subtractNegative", testFeatures[i])
                                    .toString()));
            _assertEquals(
                    -10d + intVals[i],
                    Double.parseDouble(
                            getExpressionEvaluation(j, "subtractWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testMultiply() throws Exception {
        final JSONObject j = getObjectByLayerId("mathMultiply", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    6d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "multiply", testFeatures[i]).toString()));
            _assertEquals(
                    24d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "multiply3", testFeatures[i]).toString()));
            _assertEquals(
                    12d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "multiplyNegative", testFeatures[i])
                                    .toString()));
            _assertEquals(
                    720d * intVals[i],
                    Double.parseDouble(
                            getExpressionEvaluation(j, "multiplyWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testDivide() throws Exception {
        final JSONObject j = getObjectByLayerId("mathDivide", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    2.5d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "divide", testFeatures[i]).toString()));
            _assertEquals(
                    0.25d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "divideNegative", testFeatures[i])
                                    .toString()));
            _assertEquals(
                    0.1d * intVals[i],
                    Double.parseDouble(
                            getExpressionEvaluation(j, "divideWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testRemainder() throws Exception {
        final JSONObject j = getObjectByLayerId("mathRemainder", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            // test data is 10 remainder 4
            _assertEquals(
                    2d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "remainder", testFeatures[i]).toString()));
            // test data is -1 remainder -4
            _assertEquals(
                    -1d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "remainderNegative", testFeatures[i])
                                    .toString()));
            // test data is intVals[i] remainder 10
            _assertEquals(
                    intVals[i] % 10d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "remainderWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testMin() throws Exception {
        final JSONObject j = getObjectByLayerId("mathMin", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    4d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "min", testFeatures[i]).toString()));
            _assertEquals(
                    -20d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "minOf10", testFeatures[i]).toString()));
            _assertEquals(
                    Math.min(intVals[i], 5),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "minWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testMax() throws Exception {
        final JSONObject j = getObjectByLayerId("mathMax", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    10d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "max", testFeatures[i]).toString()));
            _assertEquals(
                    18d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "maxOf10", testFeatures[i]).toString()));
            _assertEquals(
                    Math.max(intVals[i], 5),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "maxWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testExponent() throws Exception {
        final JSONObject j = getObjectByLayerId("mathExponent", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    10000d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "exponent", testFeatures[i]).toString()));
            _assertEquals(
                    0.0001d,
                    Double.parseDouble(
                            getExpressionEvaluation(j, "exponentNegative", testFeatures[i])
                                    .toString()));
            _assertEquals(
                    Math.pow(intVals[i], 5),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "exponentWithFeature", testFeatures[i])
                                    .toString()));
        }
    }

    @Test
    public void testSin() throws Exception {
        final JSONObject j = getObjectByLayerId("mathSin", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.sin(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "sin", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testAsin() throws Exception {
        final JSONObject j = getObjectByLayerId("mathAsin", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.asin(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "asin", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testCos() throws Exception {
        final JSONObject j = getObjectByLayerId("mathCos", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.cos(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "cos", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testAcos() throws Exception {
        final JSONObject j = getObjectByLayerId("mathAcos", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.acos(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "acos", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testTan() throws Exception {
        final JSONObject j = getObjectByLayerId("mathTan", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.tan(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "tan", testFeatures[i]).toString()));
        }
    }

    @Test
    public void testAtan() throws Exception {
        final JSONObject j = getObjectByLayerId("mathAtan", "layout");
        for (int i = 0; i < intVals.length; ++i) {
            _assertEquals(
                    Math.atan(intVals[i]),
                    Double.parseDouble(
                            getExpressionEvaluation(j, "atan", testFeatures[i]).toString()));
        }
    }
}
