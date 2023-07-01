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
package org.geotools.filter.function.math;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.FactoryRegistryException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;

public class FilterFunction_Test {

    private Literal literal_1 = null;

    private Literal literal_m1;

    private Literal literal_2;

    private Literal literal_m2;

    private Literal literal_pi;

    private Literal literal_05pi;

    private Literal literal_null;

    private FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory2(null);

        literal_1 = ff.literal("1");
        literal_m1 = ff.literal("-1");
        literal_2 = ff.literal("2");
        literal_m2 = ff.literal("-2");
        literal_pi = ff.literal(String.valueOf(Math.PI));
        literal_05pi = ff.literal(String.valueOf(0.5 * Math.PI));
        literal_null = ff.literal(null);
        Assert.assertEquals(
                "Literal Expression 0.0",
                Double.valueOf(1.0),
                literal_1.evaluate(null, Double.class));
        Assert.assertEquals(
                "Literal Expression pi",
                Double.valueOf(Math.PI),
                literal_pi.evaluate(null, Double.class));
        Assert.assertEquals(
                "Literal Expression 05pi",
                Double.valueOf(0.5 * Math.PI),
                literal_05pi.evaluate(null, Double.class));
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testsin() {
        try {
            FilterFunction_sin sin = (FilterFunction_sin) ff.function("sin", Expression.NIL);
            Assert.assertEquals("Name is, ", "sin", sin.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, sin.getFunctionName().getArgumentCount());

            Function sinFunction = ff.function("sin", literal_1);
            double good0 = Math.sin(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "sin of (1.0):",
                        Double.isNaN(((Double) sinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sin of (1.0):",
                        Math.sin(1.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sinFunction = ff.function("sin", literal_m1);
            double good1 = Math.sin(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "sin of (-1.0):",
                        Double.isNaN(((Double) sinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sin of (-1.0):",
                        Math.sin(-1.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            sinFunction = ff.function("sin", literal_2);
            double good2 = Math.sin(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "sin of (2.0):",
                        Double.isNaN(((Double) sinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sin of (2.0):",
                        Math.sin(2.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sinFunction = ff.function("sin", literal_m2);
            double good3 = Math.sin(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "sin of (-2.0):",
                        Double.isNaN(((Double) sinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sin of (-2.0):",
                        Math.sin(-2.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sinFunction = ff.function("sin", literal_pi);
            double good4 = Math.sin(3.141592653589793);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "sin of (3.141592653589793):",
                        Double.isNaN(((Double) sinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sin of (3.141592653589793):",
                        Math.sin(3.141592653589793),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sinFunction = ff.function("sin", literal_05pi);
            double good5 = Math.sin(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "sin of (1.5707963267948966):",
                        Double.isNaN(((Double) sinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sin of (1.5707963267948966):",
                        Math.sin(1.5707963267948966),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sinFunction = ff.function("sin", literal_null);
            Assert.assertNull(sinFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testcos() {
        try {

            FilterFunction_cos cos =
                    (FilterFunction_cos)
                            ff.function("cos", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "cos", cos.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, cos.getFunctionName().getArgumentCount());

            Function cosFunction = ff.function("cos", literal_1);
            double good0 = Math.cos(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "cos of (1.0):",
                        Double.isNaN(((Double) cosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "cos of (1.0):",
                        Math.cos(1.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            cosFunction = ff.function("cos", literal_m1);
            double good1 = Math.cos(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "cos of (-1.0):",
                        Double.isNaN(((Double) cosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "cos of (-1.0):",
                        Math.cos(-1.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            cosFunction = ff.function("cos", literal_2);
            double good2 = Math.cos(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "cos of (2.0):",
                        Double.isNaN(((Double) cosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "cos of (2.0):",
                        Math.cos(2.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            cosFunction = ff.function("cos", literal_m2);
            double good3 = Math.cos(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "cos of (-2.0):",
                        Double.isNaN(((Double) cosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "cos of (-2.0):",
                        Math.cos(-2.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            cosFunction = ff.function("cos", literal_pi);
            double good4 = Math.cos(3.141592653589793);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "cos of (3.141592653589793):",
                        Double.isNaN(((Double) cosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "cos of (3.141592653589793):",
                        Math.cos(3.141592653589793),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            cosFunction = ff.function("cos", literal_05pi);
            double good5 = Math.cos(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "cos of (1.5707963267948966):",
                        Double.isNaN(((Double) cosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "cos of (1.5707963267948966):",
                        Math.cos(1.5707963267948966),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            cosFunction = ff.function("cos", literal_null);
            Assert.assertNull(cosFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testtan() {
        try {

            FilterFunction_tan tan =
                    (FilterFunction_tan)
                            ff.function("tan", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "tan", tan.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, tan.getFunctionName().getArgumentCount());

            Function tanFunction = ff.function("tan", literal_1);
            double good0 = Math.tan(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "tan of (1.0):",
                        Double.isNaN(((Double) tanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "tan of (1.0):",
                        Math.tan(1.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            tanFunction = ff.function("tan", literal_m1);
            double good1 = Math.tan(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "tan of (-1.0):",
                        Double.isNaN(((Double) tanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "tan of (-1.0):",
                        Math.tan(-1.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            tanFunction = ff.function("tan", literal_2);
            double good2 = Math.tan(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "tan of (2.0):",
                        Double.isNaN(((Double) tanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "tan of (2.0):",
                        Math.tan(2.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            tanFunction = ff.function("tan", literal_m2);
            double good3 = Math.tan(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "tan of (-2.0):",
                        Double.isNaN(((Double) tanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "tan of (-2.0):",
                        Math.tan(-2.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            tanFunction = ff.function("tan", literal_pi);
            double good4 = Math.tan(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "tan of (3.141592653589793):",
                        Double.isNaN(((Double) tanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "tan of (3.141592653589793):",
                        Math.tan(3.141592653589793),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            tanFunction = ff.function("tan", literal_05pi);
            double good5 = Math.tan(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "tan of (1.5707963267948966):",
                        Double.isNaN(((Double) tanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "tan of (1.5707963267948966):",
                        Math.tan(1.5707963267948966),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            tanFunction = ff.function("tan", literal_null);
            Assert.assertNull(tanFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testatan2() {
        try {
            FilterFunction_atan2 atan2 =
                    (FilterFunction_atan2)
                            ff.function(
                                    "atan2",
                                    org.geotools.api.filter.expression.Expression.NIL,
                                    org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "atan2", atan2.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, atan2.getFunctionName().getArgumentCount());

            Function atan2Function = ff.function("atan2", literal_1, literal_m1);
            double good0 = Math.atan2(1.0, -1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "atan2 of (1.0,-1.0):",
                        Double.isNaN(((Double) atan2Function.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan2 of (1.0,-1.0):",
                        Math.atan2(1.0, -1.0),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atan2Function = ff.function("atan2", literal_m1, literal_2);
            double good1 = Math.atan2(-1.0, 2.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "atan2 of (-1.0,2.0):",
                        Double.isNaN(((Double) atan2Function.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan2 of (-1.0,2.0):",
                        Math.atan2(-1.0, 2.0),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atan2Function = ff.function("atan2", literal_2, literal_m2);
            double good2 = Math.atan2(2.0, -2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "atan2 of (2.0,-2.0):",
                        Double.isNaN(((Double) atan2Function.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan2 of (2.0,-2.0):",
                        Math.atan2(2.0, -2.0),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atan2Function = ff.function("atan2", literal_m2, literal_pi);
            double good3 = Math.atan2(-2.0, 3.141592653589793);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "atan2 of (-2.0,3.141592653589793):",
                        Double.isNaN(((Double) atan2Function.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan2 of (-2.0,3.141592653589793):",
                        Math.atan2(-2.0, 3.141592653589793),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atan2Function = ff.function("atan2", literal_pi, literal_05pi);
            double good4 = Math.atan2(3.141592653589793, 1.5707963267948966);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "atan2 of (3.141592653589793,1.5707963267948966):",
                        Double.isNaN(((Double) atan2Function.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan2 of (3.141592653589793,1.5707963267948966):",
                        Math.atan2(3.141592653589793, 1.5707963267948966),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atan2Function = ff.function("atan2", literal_05pi, literal_1);
            double good5 = Math.atan2(1.5707963267948966, 1.0);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "atan2 of (1.5707963267948966,1.0):",
                        Double.isNaN(((Double) atan2Function.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan2 of (1.5707963267948966,1.0):",
                        Math.atan2(1.5707963267948966, 1.0),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atan2Function = ff.function("atan2", literal_null, literal_1);
            Assert.assertNull(atan2Function.evaluate(null));

            atan2Function = ff.function("atan2", literal_1, literal_null);
            Assert.assertNull(atan2Function.evaluate(null));

            atan2Function = ff.function("atan2", literal_null, literal_null);
            Assert.assertNull(atan2Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testsqrt() {
        try {
            FilterFunction_sqrt sqrt =
                    (FilterFunction_sqrt)
                            ff.function("sqrt", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "sqrt", sqrt.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, sqrt.getFunctionName().getArgumentCount());

            Function sqrtFunction = ff.function("sqrt", literal_1);
            double good0 = Math.sqrt(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "sqrt of (1.0):",
                        Double.isNaN(((Double) sqrtFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sqrt of (1.0):",
                        Math.sqrt(1.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sqrtFunction = ff.function("sqrt", literal_m1);
            double good1 = Math.sqrt(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "sqrt of (-1.0):",
                        Double.isNaN(((Double) sqrtFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sqrt of (-1.0):",
                        Math.sqrt(-1.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sqrtFunction = ff.function("sqrt", literal_2);
            double good2 = Math.sqrt(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "sqrt of (2.0):",
                        Double.isNaN(((Double) sqrtFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sqrt of (2.0):",
                        Math.sqrt(2.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sqrtFunction = ff.function("sqrt", literal_m2);
            double good3 = Math.sqrt(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "sqrt of (-2.0):",
                        Double.isNaN(((Double) sqrtFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sqrt of (-2.0):",
                        Math.sqrt(-2.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sqrtFunction = ff.function("sqrt", literal_pi);
            double good4 = Math.sqrt(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "sqrt of (3.141592653589793):",
                        Double.isNaN(((Double) sqrtFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sqrt of (3.141592653589793):",
                        Math.sqrt(3.141592653589793),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sqrtFunction = ff.function("sqrt", literal_05pi);
            double good5 = Math.sqrt(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "sqrt of (1.5707963267948966):",
                        Double.isNaN(((Double) sqrtFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "sqrt of (1.5707963267948966):",
                        Math.sqrt(1.5707963267948966),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            sqrtFunction = ff.function("sqrt", literal_null);
            Assert.assertNull(sqrtFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testpow() {
        try {
            FilterFunction_pow pow =
                    (FilterFunction_pow)
                            ff.function(
                                    "pow",
                                    org.geotools.api.filter.expression.Expression.NIL,
                                    org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "pow", pow.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, pow.getFunctionName().getArgumentCount());

            Function powFunction = ff.function("pow", literal_1, literal_m1);
            double good0 = Math.pow(1.0, -1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "pow of (1.0,-1.0):",
                        Double.isNaN(((Double) powFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "pow of (1.0,-1.0):",
                        Math.pow(1.0, -1.0),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            powFunction = ff.function("pow", literal_m1, literal_2);
            double good1 = Math.pow(-1.0, 2.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "pow of (-1.0,2.0):",
                        Double.isNaN(((Double) powFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "pow of (-1.0,2.0):",
                        Math.pow(-1.0, 2.0),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            powFunction = ff.function("pow", literal_2, literal_m2);
            double good2 = Math.pow(2.0, -2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "pow of (2.0,-2.0):",
                        Double.isNaN(((Double) powFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "pow of (2.0,-2.0):",
                        Math.pow(2.0, -2.0),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            powFunction = ff.function("pow", literal_m2, literal_pi);
            double good3 = Math.pow(-2.0, 3.141592653589793);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "pow of (-2.0,3.141592653589793):",
                        Double.isNaN(((Double) powFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "pow of (-2.0,3.141592653589793):",
                        Math.pow(-2.0, 3.141592653589793),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            powFunction = ff.function("pow", literal_pi, literal_05pi);
            double good4 = Math.pow(3.141592653589793, 1.5707963267948966);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "pow of (3.141592653589793,1.5707963267948966):",
                        Double.isNaN(((Double) powFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "pow of (3.141592653589793,1.5707963267948966):",
                        Math.pow(3.141592653589793, 1.5707963267948966),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            powFunction = ff.function("pow", literal_05pi, literal_1);
            double good5 = Math.pow(1.5707963267948966, 1.0);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "pow of (1.5707963267948966,1.0):",
                        Double.isNaN(((Double) powFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "pow of (1.5707963267948966,1.0):",
                        Math.pow(1.5707963267948966, 1.0),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            powFunction = ff.function("pow", literal_null, literal_1);
            Assert.assertNull(powFunction.evaluate(null));

            powFunction = ff.function("pow", literal_1, literal_null);
            Assert.assertNull(powFunction.evaluate(null));

            powFunction = ff.function("pow", literal_null, literal_null);
            Assert.assertNull(powFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmin_4() {
        try {
            FilterFunction_min_4 min_4 =
                    (FilterFunction_min_4)
                            ff.function("min_4", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "min_4", min_4.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, min_4.getFunctionName().getArgumentCount());

            Function min_4Function = ff.function("min_4", literal_1, literal_m1);
            Assert.assertEquals(
                    "min of (1.0,-1.0):",
                    (long) Math.min(1.0, -1.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(),
                    0.00001);

            min_4Function = ff.function("min_4", literal_m1, literal_2);
            Assert.assertEquals(
                    "min of (-1.0,2.0):",
                    (long) Math.min(-1.0, 2.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(),
                    0.00001);

            min_4Function = ff.function("min_4", literal_2, literal_m2);
            Assert.assertEquals(
                    "min of (2.0,-2.0):",
                    (long) Math.min(2.0, -2.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(),
                    0.00001);

            min_4Function = ff.function("min_4", literal_m2, literal_pi);
            Assert.assertEquals(
                    "min of (-2.0,3.141592653589793):",
                    (long) Math.min(-2.0, 3.141592653589793),
                    ((Integer) min_4Function.evaluate(null)).intValue(),
                    0.00001);

            min_4Function = ff.function("min_4", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "min of (3.141592653589793,1.5707963267948966):",
                    (long) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Integer) min_4Function.evaluate(null)).intValue(),
                    0.00001);

            min_4Function = ff.function("min_4", literal_05pi, literal_1);
            Assert.assertEquals(
                    "min of (1.5707963267948966,1.0):",
                    (long) Math.min(1.5707963267948966, 1.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(),
                    0.00001);

            min_4Function = ff.function("min_4", literal_null, literal_1);
            Assert.assertNull(min_4Function.evaluate(null));

            min_4Function = ff.function("min_4", literal_1, literal_null);
            Assert.assertNull(min_4Function.evaluate(null));

            min_4Function = ff.function("min_4", literal_null, literal_null);
            Assert.assertNull(min_4Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmin_2() {
        try {
            FilterFunction_min_2 min_2 =
                    (FilterFunction_min_2)
                            ff.function("min_2", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "min_2", min_2.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, min_2.getFunctionName().getArgumentCount());

            Function min_2Function = ff.function("min_2", literal_1, literal_m1);
            Assert.assertEquals(
                    "min of (1.0,-1.0):",
                    (long) Math.min(1.0, -1.0),
                    ((Long) min_2Function.evaluate(null)).longValue(),
                    0.00001);

            min_2Function = ff.function("min_2", literal_m1, literal_2);
            Assert.assertEquals(
                    "min of (-1.0,2.0):",
                    (long) Math.min(-1.0, 2.0),
                    ((Long) min_2Function.evaluate(null)).longValue(),
                    0.00001);

            min_2Function = ff.function("min_2", literal_2, literal_m2);
            Assert.assertEquals(
                    "min of (2.0,-2.0):",
                    (long) Math.min(2.0, -2.0),
                    ((Long) min_2Function.evaluate(null)).longValue(),
                    0.00001);

            min_2Function = ff.function("min_2", literal_m2, literal_pi);
            Assert.assertEquals(
                    "min of (-2.0,3.141592653589793):",
                    (long) Math.min(-2.0, 3.141592653589793),
                    ((Long) min_2Function.evaluate(null)).longValue(),
                    0.00001);

            min_2Function = ff.function("min_2", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "min of (3.141592653589793,1.5707963267948966):",
                    (long) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Long) min_2Function.evaluate(null)).longValue(),
                    0.00001);

            min_2Function = ff.function("min_2", literal_05pi, literal_1);
            Assert.assertEquals(
                    "min of (1.5707963267948966,1.0):",
                    (long) Math.min(1.5707963267948966, 1.0),
                    ((Long) min_2Function.evaluate(null)).longValue(),
                    0.00001);

            min_2Function = ff.function("min_2", literal_null, literal_1);
            Assert.assertNull(min_2Function.evaluate(null));

            min_2Function = ff.function("min_2", literal_1, literal_null);
            Assert.assertNull(min_2Function.evaluate(null));

            min_2Function = ff.function("min_2", literal_null, literal_null);
            Assert.assertNull(min_2Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmin_3() {
        try {
            FilterFunction_min_3 min_3 =
                    (FilterFunction_min_3)
                            ff.function("min_3", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "min_3", min_3.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, min_3.getFunctionName().getArgumentCount());

            Function min_3Function = ff.function("min_3", literal_1, literal_m1);
            Assert.assertEquals(
                    "min of (1.0,-1.0):",
                    (float) Math.min(1.0, -1.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(),
                    0.00001);

            min_3Function = ff.function("min_3", literal_m1, literal_2);
            Assert.assertEquals(
                    "min of (-1.0,2.0):",
                    (float) Math.min(-1.0, 2.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(),
                    0.00001);

            min_3Function = ff.function("min_3", literal_2, literal_m2);
            Assert.assertEquals(
                    "min of (2.0,-2.0):",
                    (float) Math.min(2.0, -2.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(),
                    0.00001);

            min_3Function = ff.function("min_3", literal_m2, literal_pi);
            Assert.assertEquals(
                    "min of (-2.0,3.141592653589793):",
                    (float) Math.min(-2.0, 3.141592653589793),
                    ((Float) min_3Function.evaluate(null)).floatValue(),
                    0.00001);

            min_3Function = ff.function("min_3", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "min of (3.141592653589793,1.5707963267948966):",
                    (float) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Float) min_3Function.evaluate(null)).floatValue(),
                    0.00001);

            min_3Function = ff.function("min_3", literal_05pi, literal_1);
            Assert.assertEquals(
                    "min of (1.5707963267948966,1.0):",
                    (float) Math.min(1.5707963267948966, 1.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(),
                    0.00001);

            min_3Function = ff.function("min_3", literal_null, literal_1);
            Assert.assertNull(min_3Function.evaluate(null));

            min_3Function = ff.function("min_3", literal_1, literal_null);
            Assert.assertNull(min_3Function.evaluate(null));

            min_3Function = ff.function("min_3", literal_null, literal_null);
            Assert.assertNull(min_3Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmin() {
        try {
            FilterFunction_min min =
                    (FilterFunction_min)
                            ff.function("min", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "min", min.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, min.getFunctionName().getArgumentCount());

            Function minFunction = ff.function("min", literal_1, literal_m1);
            Assert.assertEquals(
                    "min of (1.0,-1.0):",
                    Math.min(1.0, -1.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(),
                    0.00001);

            minFunction = ff.function("min", literal_m1, literal_2);
            Assert.assertEquals(
                    "min of (-1.0,2.0):",
                    Math.min(-1.0, 2.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(),
                    0.00001);

            minFunction = ff.function("min", literal_2, literal_m2);
            Assert.assertEquals(
                    "min of (2.0,-2.0):",
                    Math.min(2.0, -2.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(),
                    0.00001);

            minFunction = ff.function("min", literal_m2, literal_pi);
            Assert.assertEquals(
                    "min of (-2.0,3.141592653589793):",
                    Math.min(-2.0, 3.141592653589793),
                    ((Double) minFunction.evaluate(null)).doubleValue(),
                    0.00001);

            minFunction = ff.function("min", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "min of (3.141592653589793,1.5707963267948966):",
                    Math.min(3.141592653589793, 1.5707963267948966),
                    ((Double) minFunction.evaluate(null)).doubleValue(),
                    0.00001);

            minFunction = ff.function("min", literal_05pi, literal_1);
            Assert.assertEquals(
                    "min of (1.5707963267948966,1.0):",
                    Math.min(1.5707963267948966, 1.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(),
                    0.00001);

            minFunction = ff.function("min", literal_null, literal_1);
            Assert.assertNull(minFunction.evaluate(null));

            minFunction = ff.function("min", literal_1, literal_null);
            Assert.assertNull(minFunction.evaluate(null));

            minFunction = ff.function("min", literal_null, literal_null);
            Assert.assertNull(minFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmax_4() {
        try {
            FilterFunction_max_4 max_4 =
                    (FilterFunction_max_4)
                            ff.function("max_4", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "max_4", max_4.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, max_4.getFunctionName().getArgumentCount());

            Function max_4Function = ff.function("max_4", literal_1, literal_m1);
            Assert.assertEquals(
                    "max_4 of (1.0,-1.0):",
                    (int) Math.max(1.0, -1.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(),
                    0.00001);

            max_4Function = ff.function("max_4", literal_m1, literal_2);
            Assert.assertEquals(
                    "max_4 of (-1.0,2.0):",
                    (int) Math.max(-1.0, 2.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(),
                    0.00001);

            max_4Function = ff.function("max_4", literal_2, literal_m2);
            Assert.assertEquals(
                    "max_4 of (2.0,-2.0):",
                    (int) Math.max(2.0, -2.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(),
                    0.00001);

            max_4Function = ff.function("max_4", literal_m2, literal_pi);
            Assert.assertEquals(
                    "max_4 of (-2.0,3.141592653589793):",
                    (int) Math.max(-2.0, 3.141592653589793),
                    ((Integer) max_4Function.evaluate(null)).intValue(),
                    0.00001);

            max_4Function = ff.function("max_4", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "max_4 of (3.141592653589793,1.5707963267948966):",
                    (int) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Integer) max_4Function.evaluate(null)).intValue(),
                    0.00001);

            max_4Function = ff.function("max_4", literal_05pi, literal_1);
            Assert.assertEquals(
                    "max_4 of (1.5707963267948966,1.0):",
                    (int) Math.max(1.5707963267948966, 1.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(),
                    0.00001);

            max_4Function = ff.function("max_4", literal_null, literal_1);
            Assert.assertNull(max_4Function.evaluate(null));

            max_4Function = ff.function("max_4", literal_1, literal_null);
            Assert.assertNull(max_4Function.evaluate(null));

            max_4Function = ff.function("max_4", literal_null, literal_null);
            Assert.assertNull(max_4Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmax_2() {
        try {
            FilterFunction_max_2 max_2 =
                    (FilterFunction_max_2)
                            ff.function("max_2", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "max_2", max_2.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, max_2.getFunctionName().getArgumentCount());

            Function max_2Function = ff.function("max_2", literal_1, literal_m1);
            Assert.assertEquals(
                    "max_2 of (1.0,-1.0):",
                    (long) Math.max(1.0, -1.0),
                    ((Long) max_2Function.evaluate(null)).longValue(),
                    0.00001);

            max_2Function = ff.function("max_2", literal_m1, literal_2);
            Assert.assertEquals(
                    "max_2 of (-1.0,2.0):",
                    (long) Math.max(-1.0, 2.0),
                    ((Long) max_2Function.evaluate(null)).longValue(),
                    0.00001);

            max_2Function = ff.function("max_2", literal_2, literal_m2);
            Assert.assertEquals(
                    "max_2 of (2.0,-2.0):",
                    (long) Math.max(2.0, -2.0),
                    ((Long) max_2Function.evaluate(null)).longValue(),
                    0.00001);

            max_2Function = ff.function("max_2", literal_m2, literal_pi);
            Assert.assertEquals(
                    "max_2 of (-2.0,3.141592653589793):",
                    (long) Math.max(-2.0, 3.141592653589793),
                    ((Long) max_2Function.evaluate(null)).longValue(),
                    0.00001);

            max_2Function = ff.function("max_2", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "max_2 of (3.141592653589793,1.5707963267948966):",
                    (long) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Long) max_2Function.evaluate(null)).longValue(),
                    0.00001);

            max_2Function = ff.function("max_2", literal_05pi, literal_1);
            Assert.assertEquals(
                    "max_2 of (1.5707963267948966,1.0):",
                    (long) Math.max(1.5707963267948966, 1.0),
                    ((Long) max_2Function.evaluate(null)).longValue(),
                    0.00001);

            max_2Function = ff.function("max_2", literal_null, literal_1);
            Assert.assertNull(max_2Function.evaluate(null));

            max_2Function = ff.function("max_2", literal_1, literal_null);
            Assert.assertNull(max_2Function.evaluate(null));

            max_2Function = ff.function("max_2", literal_null, literal_null);
            Assert.assertNull(max_2Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmax_3() {
        try {
            FilterFunction_max_3 max_3 =
                    (FilterFunction_max_3)
                            ff.function("max_3", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "max_3", max_3.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, max_3.getFunctionName().getArgumentCount());

            Function max_3Function = ff.function("max_3", literal_1, literal_m1);
            Assert.assertEquals(
                    "max_3 of (1.0,-1.0):",
                    (float) Math.max(1.0, -1.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(),
                    0.00001);

            max_3Function = ff.function("max_3", literal_m1, literal_2);
            Assert.assertEquals(
                    "max_3 of (-1.0,2.0):",
                    (float) Math.max(-1.0, 2.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(),
                    0.00001);

            max_3Function = ff.function("max_3", literal_2, literal_m2);
            Assert.assertEquals(
                    "max_3 of (2.0,-2.0):",
                    (float) Math.max(2.0, -2.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(),
                    0.00001);

            max_3Function = ff.function("max_3", literal_m2, literal_pi);
            Assert.assertEquals(
                    "max_3 of (-2.0,3.141592653589793):",
                    (float) Math.max(-2.0, 3.141592653589793),
                    ((Float) max_3Function.evaluate(null)).floatValue(),
                    0.00001);

            max_3Function = ff.function("max_3", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "max_3 of (3.141592653589793,1.5707963267948966):",
                    (float) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Float) max_3Function.evaluate(null)).floatValue(),
                    0.00001);

            max_3Function = ff.function("max_3", literal_05pi, literal_1);
            Assert.assertEquals(
                    "max_3 of (1.5707963267948966,1.0):",
                    (float) Math.max(1.5707963267948966, 1.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(),
                    0.00001);

            max_3Function = ff.function("max_3", literal_null, literal_1);
            Assert.assertNull(max_3Function.evaluate(null));

            max_3Function = ff.function("max_3", literal_1, literal_null);
            Assert.assertNull(max_3Function.evaluate(null));

            max_3Function = ff.function("max_3", literal_null, literal_null);
            Assert.assertNull(max_3Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testmax() {
        try {
            FilterFunction_max max =
                    (FilterFunction_max)
                            ff.function("max", new org.geotools.api.filter.expression.Expression[2]);
            Assert.assertEquals("Name is, ", "max", max.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, max.getFunctionName().getArgumentCount());

            Function maxFunction = ff.function("max", literal_1, literal_m1);
            Assert.assertEquals(
                    "max of (1.0,-1.0):",
                    Math.max(1.0, -1.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(),
                    0.00001);

            maxFunction = ff.function("max", literal_m1, literal_2);
            Assert.assertEquals(
                    "max of (-1.0,2.0):",
                    Math.max(-1.0, 2.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(),
                    0.00001);

            maxFunction = ff.function("max", literal_2, literal_m2);
            Assert.assertEquals(
                    "max of (2.0,-2.0):",
                    Math.max(2.0, -2.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(),
                    0.00001);

            maxFunction = ff.function("max", literal_m2, literal_pi);
            Assert.assertEquals(
                    "max of (-2.0,3.141592653589793):",
                    Math.max(-2.0, 3.141592653589793),
                    ((Double) maxFunction.evaluate(null)).doubleValue(),
                    0.00001);

            maxFunction = ff.function("max", literal_pi, literal_05pi);
            Assert.assertEquals(
                    "max of (3.141592653589793,1.5707963267948966):",
                    Math.max(3.141592653589793, 1.5707963267948966),
                    ((Double) maxFunction.evaluate(null)).doubleValue(),
                    0.00001);

            maxFunction = ff.function("max", literal_05pi, literal_1);
            Assert.assertEquals(
                    "max of (1.5707963267948966,1.0):",
                    Math.max(1.5707963267948966, 1.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(),
                    0.00001);

            maxFunction = ff.function("max", literal_null, literal_1);
            Assert.assertNull(maxFunction.evaluate(null));

            maxFunction = ff.function("max", literal_1, literal_null);
            Assert.assertNull(maxFunction.evaluate(null));

            maxFunction = ff.function("max", literal_null, literal_null);
            Assert.assertNull(maxFunction.evaluate(null));
        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testabs() {
        try {
            FilterFunction_abs abs =
                    (FilterFunction_abs)
                            ff.function("abs", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "abs", abs.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, abs.getFunctionName().getArgumentCount());

            Function absFunction = ff.function("abs", literal_1);
            Assert.assertEquals(
                    "abs of (1.0):",
                    (int) Math.abs(1.0),
                    ((Integer) absFunction.evaluate(null)).intValue(),
                    0.00001);

            absFunction = ff.function("abs", literal_m1);
            Assert.assertEquals(
                    "abs of (-1.0):",
                    (int) Math.abs(-1.0),
                    ((Integer) absFunction.evaluate(null)).intValue(),
                    0.00001);

            absFunction = ff.function("abs", literal_2);
            Assert.assertEquals(
                    "abs of (2.0):",
                    (int) Math.abs(2.0),
                    ((Integer) absFunction.evaluate(null)).intValue(),
                    0.00001);

            absFunction = ff.function("abs", literal_m2);
            Assert.assertEquals(
                    "abs of (-2.0):",
                    (int) Math.abs(-2.0),
                    ((Integer) absFunction.evaluate(null)).intValue(),
                    0.00001);

            absFunction = ff.function("abs", literal_pi);
            Assert.assertEquals(
                    "abs of (3.141592653589793):",
                    (int) Math.abs(3.141592653589793),
                    ((Integer) absFunction.evaluate(null)).intValue(),
                    0.00001);

            absFunction = ff.function("abs", literal_05pi);
            Assert.assertEquals(
                    "abs of (1.5707963267948966):",
                    (int) Math.abs(1.5707963267948966),
                    ((Integer) absFunction.evaluate(null)).intValue(),
                    0.00001);

            absFunction = ff.function("abs", literal_null);
            Assert.assertNull(absFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testabs_4() {
        try {
            FilterFunction_abs_4 abs_4 =
                    (FilterFunction_abs_4)
                            ff.function("abs_4", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "abs_4", abs_4.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, abs_4.getFunctionName().getArgumentCount());

            Function abs_4Function = ff.function("abs_4", literal_1);
            Assert.assertEquals(
                    "abs_4 of (1.0):",
                    Math.abs(1.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(),
                    0.00001);

            abs_4Function = ff.function("abs_4", literal_m1);
            Assert.assertEquals(
                    "abs_4 of (-1.0):",
                    Math.abs(-1.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(),
                    0.00001);

            abs_4Function = ff.function("abs_4", literal_2);
            Assert.assertEquals(
                    "abs_4 of (2.0):",
                    Math.abs(2.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(),
                    0.00001);

            abs_4Function = ff.function("abs_4", literal_m2);
            Assert.assertEquals(
                    "abs_4 of (-2.0):",
                    Math.abs(-2.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(),
                    0.00001);

            abs_4Function = ff.function("abs_4", literal_pi);
            Assert.assertEquals(
                    "abs_4 of (3.141592653589793):",
                    Math.abs(3.141592653589793),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(),
                    0.00001);

            abs_4Function = ff.function("abs_4", literal_05pi);
            Assert.assertEquals(
                    "abs_4 of (1.5707963267948966):",
                    Math.abs(1.5707963267948966),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(),
                    0.00001);

            abs_4Function = ff.function("abs_4", literal_null);
            Assert.assertNull(abs_4Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testabs_3() {
        try {
            FilterFunction_abs_3 abs_3 =
                    (FilterFunction_abs_3)
                            ff.function("abs_3", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "abs_3", abs_3.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, abs_3.getFunctionName().getArgumentCount());

            Function abs_3Function = ff.function("abs_3", literal_1);
            Assert.assertEquals(
                    "abs_3 of (1.0):",
                    (float) Math.abs(1.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(),
                    0.00001);

            abs_3Function = ff.function("abs_3", literal_m1);
            Assert.assertEquals(
                    "abs_3 of (-1.0):",
                    (float) Math.abs(-1.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(),
                    0.00001);

            abs_3Function = ff.function("abs_3", literal_2);
            Assert.assertEquals(
                    "abs_3 of (2.0):",
                    (float) Math.abs(2.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(),
                    0.00001);

            abs_3Function = ff.function("abs_3", literal_m2);
            Assert.assertEquals(
                    "abs_3 of (-2.0):",
                    (float) Math.abs(-2.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(),
                    0.00001);

            abs_3Function = ff.function("abs_3", literal_pi);
            Assert.assertEquals(
                    "abs_3 of (3.141592653589793):",
                    (float) Math.abs(3.141592653589793),
                    ((Float) abs_3Function.evaluate(null)).floatValue(),
                    0.00001);

            abs_3Function = ff.function("abs_3", literal_05pi);
            Assert.assertEquals(
                    "abs_3 of (1.5707963267948966):",
                    (float) Math.abs(1.5707963267948966),
                    ((Float) abs_3Function.evaluate(null)).floatValue(),
                    0.00001);

            abs_3Function = ff.function("abs_3", literal_null);
            Assert.assertNull(abs_3Function.evaluate(null));
        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testabs_2() {
        try {
            FilterFunction_abs_2 abs_2 =
                    (FilterFunction_abs_2)
                            ff.function("abs_2", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "abs_2", abs_2.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, abs_2.getFunctionName().getArgumentCount());

            Function abs_2Function = ff.function("abs_2", literal_1);
            Assert.assertEquals(
                    "abs_2 of (1.0):",
                    (long) Math.abs(1.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(),
                    0.00001);

            abs_2Function = ff.function("abs_2", literal_m1);
            Assert.assertEquals(
                    "abs_2 of (-1.0):",
                    (long) Math.abs(-1.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(),
                    0.00001);

            abs_2Function = ff.function("abs_2", literal_2);
            Assert.assertEquals(
                    "abs_2 of (2.0):",
                    (long) Math.abs(2.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(),
                    0.00001);

            abs_2Function = ff.function("abs_2", literal_m2);
            Assert.assertEquals(
                    "abs_2 of (-2.0):",
                    (long) Math.abs(-2.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(),
                    0.00001);

            abs_2Function = ff.function("abs_2", literal_pi);
            Assert.assertEquals(
                    "abs_2 of (3.141592653589793):",
                    (long) Math.abs(3.141592653589793),
                    ((Long) abs_2Function.evaluate(null)).longValue(),
                    0.00001);

            abs_2Function = ff.function("abs_2", literal_05pi);
            Assert.assertEquals(
                    "abs_2 of (1.5707963267948966):",
                    (long) Math.abs(1.5707963267948966),
                    ((Long) abs_2Function.evaluate(null)).longValue(),
                    0.00001);

            abs_2Function = ff.function("abs_2", literal_null);
            Assert.assertNull(abs_2Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testIEEEremainder() {
        try {
            FilterFunction_IEEEremainder IEEEremainder =
                    (FilterFunction_IEEEremainder)
                            ff.function(
                                    "IEEEremainder",
                                    org.geotools.api.filter.expression.Expression.NIL,
                                    org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "IEEEremainder", IEEEremainder.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 2, IEEEremainder.getFunctionName().getArgumentCount());

            Function IEEEremainderFunction = ff.function("IEEEremainder", literal_1, literal_m1);
            double good0 = Math.IEEEremainder(1.0, -1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "IEEEremainder of (1.0,-1.0):",
                        Double.isNaN(
                                ((Double) IEEEremainderFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "IEEEremainder of (1.0,-1.0):",
                        Math.IEEEremainder(1.0, -1.0),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            IEEEremainderFunction = ff.function("IEEEremainder", literal_m1, literal_2);
            double good1 = Math.IEEEremainder(-1.0, 2.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "IEEEremainder of (-1.0,2.0):",
                        Double.isNaN(
                                ((Double) IEEEremainderFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "IEEEremainder of (-1.0,2.0):",
                        Math.IEEEremainder(-1.0, 2.0),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            IEEEremainderFunction = ff.function("IEEEremainder", literal_2, literal_m2);
            double good2 = Math.IEEEremainder(2.0, -2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "IEEEremainder of (2.0,-2.0):",
                        Double.isNaN(
                                ((Double) IEEEremainderFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "IEEEremainder of (2.0,-2.0):",
                        Math.IEEEremainder(2.0, -2.0),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            IEEEremainderFunction = ff.function("IEEEremainder", literal_m2, literal_pi);
            double good3 = Math.IEEEremainder(-2.0, 3.141592653589793);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "IEEEremainder of (-2.0,3.141592653589793):",
                        Double.isNaN(
                                ((Double) IEEEremainderFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "IEEEremainder of (-2.0,3.141592653589793):",
                        Math.IEEEremainder(-2.0, 3.141592653589793),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            IEEEremainderFunction = ff.function("IEEEremainder", literal_pi, literal_05pi);
            double good4 = Math.IEEEremainder(3.141592653589793, 1.5707963267948966);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "IEEEremainder of (3.141592653589793,1.5707963267948966):",
                        Double.isNaN(
                                ((Double) IEEEremainderFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "IEEEremainder of (3.141592653589793,1.5707963267948966):",
                        Math.IEEEremainder(3.141592653589793, 1.5707963267948966),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            IEEEremainderFunction = ff.function("IEEEremainder", literal_05pi, literal_1);
            double good5 = Math.IEEEremainder(1.5707963267948966, 1.0);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "IEEEremainder of (1.5707963267948966,1.0):",
                        Double.isNaN(
                                ((Double) IEEEremainderFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "IEEEremainder of (1.5707963267948966,1.0):",
                        Math.IEEEremainder(1.5707963267948966, 1.0),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            IEEEremainderFunction = ff.function("IEEEremainder", literal_null, literal_1);
            Assert.assertNull(IEEEremainderFunction.evaluate(null));

            IEEEremainderFunction = ff.function("IEEEremainder", literal_1, literal_null);
            Assert.assertNull(IEEEremainderFunction.evaluate(null));

            IEEEremainderFunction = ff.function("IEEEremainder", literal_null, literal_null);
            Assert.assertNull(IEEEremainderFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testacos() {
        try {
            FilterFunction_acos acos =
                    (FilterFunction_acos)
                            ff.function("acos", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "acos", acos.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, acos.getFunctionName().getArgumentCount());

            Function acosFunction = ff.function("acos", literal_1);
            double good0 = Math.acos(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "acos of (1.0):",
                        Double.isNaN(((Double) acosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "acos of (1.0):",
                        Math.acos(1.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            acosFunction = ff.function("acos", literal_m1);
            double good1 = Math.acos(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "acos of (-1.0):",
                        Double.isNaN(((Double) acosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "acos of (-1.0):",
                        Math.acos(-1.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            acosFunction = ff.function("acos", literal_2);
            double good2 = Math.acos(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "acos of (2.0):",
                        Double.isNaN(((Double) acosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "acos of (2.0):",
                        Math.acos(2.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            acosFunction = ff.function("acos", literal_m2);
            double good3 = Math.acos(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "acos of (-2.0):",
                        Double.isNaN(((Double) acosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "acos of (-2.0):",
                        Math.acos(-2.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            acosFunction = ff.function("acos", literal_pi);
            double good4 = Math.acos(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "acos of (3.141592653589793):",
                        Double.isNaN(((Double) acosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "acos of (3.141592653589793):",
                        Math.acos(3.141592653589793),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            acosFunction = ff.function("acos", literal_05pi);
            double good5 = Math.acos(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "acos of (1.5707963267948966):",
                        Double.isNaN(((Double) acosFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "acos of (1.5707963267948966):",
                        Math.acos(1.5707963267948966),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            acosFunction = ff.function("acos", literal_null);
            Assert.assertNull(acosFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testasin() {
        try {
            FilterFunction_asin asin =
                    (FilterFunction_asin)
                            ff.function("asin", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "asin", asin.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, asin.getFunctionName().getArgumentCount());

            Function asinFunction = ff.function("asin", literal_1);
            double good0 = Math.asin(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "asin of (1.0):",
                        Double.isNaN(((Double) asinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "asin of (1.0):",
                        Math.asin(1.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            asinFunction = ff.function("asin", literal_m1);
            double good1 = Math.asin(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "asin of (-1.0):",
                        Double.isNaN(((Double) asinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "asin of (-1.0):",
                        Math.asin(-1.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            asinFunction = ff.function("asin", literal_2);
            double good2 = Math.asin(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "asin of (2.0):",
                        Double.isNaN(((Double) asinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "asin of (2.0):",
                        Math.asin(2.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            asinFunction = ff.function("asin", literal_m2);
            double good3 = Math.asin(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "asin of (-2.0):",
                        Double.isNaN(((Double) asinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "asin of (-2.0):",
                        Math.asin(-2.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            asinFunction = ff.function("asin", literal_pi);
            double good4 = Math.asin(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "asin of (3.141592653589793):",
                        Double.isNaN(((Double) asinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "asin of (3.141592653589793):",
                        Math.asin(3.141592653589793),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            asinFunction = ff.function("asin", literal_05pi);
            double good5 = Math.asin(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "asin of (1.5707963267948966):",
                        Double.isNaN(((Double) asinFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "asin of (1.5707963267948966):",
                        Math.asin(1.5707963267948966),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            asinFunction = ff.function("asin", literal_null);
            Assert.assertNull(asinFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testatan() {
        try {
            FilterFunction_atan atan =
                    (FilterFunction_atan)
                            ff.function("atan", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "atan", atan.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, atan.getFunctionName().getArgumentCount());

            Function atanFunction = ff.function("atan", literal_1);
            double good0 = Math.atan(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "atan of (1.0):",
                        Double.isNaN(((Double) atanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan of (1.0):",
                        Math.atan(1.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atanFunction = ff.function("atan", literal_m1);
            double good1 = Math.atan(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "atan of (-1.0):",
                        Double.isNaN(((Double) atanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan of (-1.0):",
                        Math.atan(-1.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atanFunction = ff.function("atan", literal_2);
            double good2 = Math.atan(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "atan of (2.0):",
                        Double.isNaN(((Double) atanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan of (2.0):",
                        Math.atan(2.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atanFunction = ff.function("atan", literal_m2);
            double good3 = Math.atan(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "atan of (-2.0):",
                        Double.isNaN(((Double) atanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan of (-2.0):",
                        Math.atan(-2.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atanFunction = ff.function("atan", literal_pi);
            double good4 = Math.atan(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "atan of (3.141592653589793):",
                        Double.isNaN(((Double) atanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan of (3.141592653589793):",
                        Math.atan(3.141592653589793),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atanFunction = ff.function("atan", literal_05pi);
            double good5 = Math.atan(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "atan of (1.5707963267948966):",
                        Double.isNaN(((Double) atanFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "atan of (1.5707963267948966):",
                        Math.atan(1.5707963267948966),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            atanFunction = ff.function("atan", literal_null);
            Assert.assertNull(atanFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testceil() {
        try {
            FilterFunction_ceil ceil =
                    (FilterFunction_ceil)
                            ff.function("ceil", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "ceil", ceil.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, ceil.getFunctionName().getArgumentCount());

            Function ceilFunction = ff.function("ceil", literal_1);
            double good0 = Math.ceil(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "ceil of (1.0):",
                        Double.isNaN(((Double) ceilFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "ceil of (1.0):",
                        Math.ceil(1.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            ceilFunction = ff.function("ceil", literal_m1);
            double good1 = Math.ceil(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "ceil of (-1.0):",
                        Double.isNaN(((Double) ceilFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "ceil of (-1.0):",
                        Math.ceil(-1.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            ceilFunction = ff.function("ceil", literal_2);
            double good2 = Math.ceil(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "ceil of (2.0):",
                        Double.isNaN(((Double) ceilFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "ceil of (2.0):",
                        Math.ceil(2.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            ceilFunction = ff.function("ceil", literal_m2);
            double good3 = Math.ceil(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "ceil of (-2.0):",
                        Double.isNaN(((Double) ceilFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "ceil of (-2.0):",
                        Math.ceil(-2.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            ceilFunction = ff.function("ceil", literal_pi);
            double good4 = Math.ceil(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "ceil of (3.141592653589793):",
                        Double.isNaN(((Double) ceilFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "ceil of (3.141592653589793):",
                        Math.ceil(3.141592653589793),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            ceilFunction = ff.function("ceil", literal_05pi);
            double good5 = Math.ceil(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "ceil of (1.5707963267948966):",
                        Double.isNaN(((Double) ceilFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "ceil of (1.5707963267948966):",
                        Math.ceil(1.5707963267948966),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            ceilFunction = ff.function("ceil", literal_null);
            Assert.assertNull(ceilFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testexp() {
        try {
            FilterFunction_exp exp =
                    (FilterFunction_exp)
                            ff.function("exp", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "exp", exp.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, exp.getFunctionName().getArgumentCount());

            Function expFunction = ff.function("exp", literal_1);
            double good0 = Math.exp(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "exp of (1.0):",
                        Double.isNaN(((Double) expFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "exp of (1.0):",
                        Math.exp(1.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            expFunction = ff.function("exp", literal_m1);
            double good1 = Math.exp(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "exp of (-1.0):",
                        Double.isNaN(((Double) expFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "exp of (-1.0):",
                        Math.exp(-1.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            expFunction = ff.function("exp", literal_2);
            double good2 = Math.exp(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "exp of (2.0):",
                        Double.isNaN(((Double) expFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "exp of (2.0):",
                        Math.exp(2.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            expFunction = ff.function("exp", literal_m2);
            double good3 = Math.exp(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "exp of (-2.0):",
                        Double.isNaN(((Double) expFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "exp of (-2.0):",
                        Math.exp(-2.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            expFunction = ff.function("exp", literal_pi);
            double good4 = Math.exp(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "exp of (3.141592653589793):",
                        Double.isNaN(((Double) expFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "exp of (3.141592653589793):",
                        Math.exp(3.141592653589793),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            expFunction = ff.function("exp", literal_05pi);
            double good5 = Math.exp(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "exp of (1.5707963267948966):",
                        Double.isNaN(((Double) expFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "exp of (1.5707963267948966):",
                        Math.exp(1.5707963267948966),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            expFunction = ff.function("exp", literal_null);
            Assert.assertNull(expFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testfloor() {
        try {
            FilterFunction_floor floor =
                    (FilterFunction_floor)
                            ff.function("floor", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "floor", floor.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, floor.getFunctionName().getArgumentCount());

            Function floorFunction = ff.function("floor", literal_1);
            double good0 = Math.floor(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "floor of (1.0):",
                        Double.isNaN(((Double) floorFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "floor of (1.0):",
                        Math.floor(1.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            floorFunction = ff.function("floor", literal_m1);
            double good1 = Math.floor(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "floor of (-1.0):",
                        Double.isNaN(((Double) floorFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "floor of (-1.0):",
                        Math.floor(-1.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            floorFunction = ff.function("floor", literal_2);
            double good2 = Math.floor(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "floor of (2.0):",
                        Double.isNaN(((Double) floorFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "floor of (2.0):",
                        Math.floor(2.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            floorFunction = ff.function("floor", literal_m2);
            double good3 = Math.floor(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "floor of (-2.0):",
                        Double.isNaN(((Double) floorFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "floor of (-2.0):",
                        Math.floor(-2.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            floorFunction = ff.function("floor", literal_pi);
            double good4 = Math.floor(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "floor of (3.141592653589793):",
                        Double.isNaN(((Double) floorFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "floor of (3.141592653589793):",
                        Math.floor(3.141592653589793),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            floorFunction = ff.function("floor", literal_05pi);
            double good5 = Math.floor(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "floor of (1.5707963267948966):",
                        Double.isNaN(((Double) floorFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "floor of (1.5707963267948966):",
                        Math.floor(1.5707963267948966),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            floorFunction = ff.function("floor", literal_null);
            Assert.assertNull(floorFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testlog() {
        try {
            FilterFunction_log log =
                    (FilterFunction_log)
                            ff.function("log", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "log", log.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, log.getFunctionName().getArgumentCount());

            Function logFunction = ff.function("log", literal_1);
            double good0 = Math.log(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "log of (1.0):",
                        Double.isNaN(((Double) logFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "log of (1.0):",
                        Math.log(1.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            logFunction = ff.function("log", literal_m1);
            double good1 = Math.log(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "log of (-1.0):",
                        Double.isNaN(((Double) logFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "log of (-1.0):",
                        Math.log(-1.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            logFunction = ff.function("log", literal_2);
            double good2 = Math.log(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "log of (2.0):",
                        Double.isNaN(((Double) logFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "log of (2.0):",
                        Math.log(2.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            logFunction = ff.function("log", literal_m2);
            double good3 = Math.log(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "log of (-2.0):",
                        Double.isNaN(((Double) logFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "log of (-2.0):",
                        Math.log(-2.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            logFunction = ff.function("log", literal_pi);
            double good4 = Math.log(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "log of (3.141592653589793):",
                        Double.isNaN(((Double) logFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "log of (3.141592653589793):",
                        Math.log(3.141592653589793),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            logFunction = ff.function("log", literal_05pi);
            double good5 = Math.log(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "log of (1.5707963267948966):",
                        Double.isNaN(((Double) logFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "log of (1.5707963267948966):",
                        Math.log(1.5707963267948966),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            logFunction = ff.function("log", literal_null);
            Assert.assertNull(logFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testrandom() {
        try {

            FilterFunction_random randomFunction =
                    (FilterFunction_random)
                            ff.function("random", new org.geotools.api.filter.expression.Expression[0]);
            Assert.assertEquals("Name is, ", "random", randomFunction.getName());
            Assert.assertEquals(
                    "Number of arguments, ",
                    0,
                    randomFunction.getFunctionName().getArgumentCount());
        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testrint() {
        try {
            FilterFunction_rint rint =
                    (FilterFunction_rint)
                            ff.function("rint", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "rint", rint.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, rint.getFunctionName().getArgumentCount());

            Function rintFunction = ff.function("rint", literal_1);
            double good0 = Math.rint(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "rint of (1.0):",
                        Double.isNaN(((Double) rintFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "rint of (1.0):",
                        Math.rint(1.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            rintFunction = ff.function("rint", literal_m1);
            double good1 = Math.rint(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "rint of (-1.0):",
                        Double.isNaN(((Double) rintFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "rint of (-1.0):",
                        Math.rint(-1.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            rintFunction = ff.function("rint", literal_2);
            double good2 = Math.rint(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "rint of (2.0):",
                        Double.isNaN(((Double) rintFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "rint of (2.0):",
                        Math.rint(2.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            rintFunction = ff.function("rint", literal_m2);
            double good3 = Math.rint(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "rint of (-2.0):",
                        Double.isNaN(((Double) rintFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "rint of (-2.0):",
                        Math.rint(-2.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            rintFunction = ff.function("rint", literal_pi);
            double good4 = Math.rint(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "rint of (3.141592653589793):",
                        Double.isNaN(((Double) rintFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "rint of (3.141592653589793):",
                        Math.rint(3.141592653589793),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            rintFunction = ff.function("rint", literal_05pi);
            double good5 = Math.rint(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "rint of (1.5707963267948966):",
                        Double.isNaN(((Double) rintFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "rint of (1.5707963267948966):",
                        Math.rint(1.5707963267948966),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            rintFunction = ff.function("rint", literal_null);
            Assert.assertNull(rintFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testround() {
        try {
            FilterFunction_round round =
                    (FilterFunction_round)
                            ff.function("round", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "round", round.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, round.getFunctionName().getArgumentCount());

            Function roundFunction = ff.function("round", literal_1);
            Assert.assertEquals(
                    "round of (1.0):",
                    (int) Math.round(1.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(),
                    0.00001);

            roundFunction = ff.function("round", literal_m1);
            Assert.assertEquals(
                    "round of (-1.0):",
                    (int) Math.round(-1.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(),
                    0.00001);

            roundFunction = ff.function("round", literal_2);
            Assert.assertEquals(
                    "round of (2.0):",
                    (int) Math.round(2.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(),
                    0.00001);

            roundFunction = ff.function("round", literal_m2);
            Assert.assertEquals(
                    "round of (-2.0):",
                    (int) Math.round(-2.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(),
                    0.00001);

            roundFunction = ff.function("round", literal_pi);
            Assert.assertEquals(
                    "round of (3.141592653589793):",
                    (int) Math.round(3.141592653589793),
                    ((Integer) roundFunction.evaluate(null)).intValue(),
                    0.00001);

            roundFunction = ff.function("round", literal_05pi);
            Assert.assertEquals(
                    "round of (1.5707963267948966):",
                    (int) Math.round(1.5707963267948966),
                    ((Integer) roundFunction.evaluate(null)).intValue(),
                    0.00001);

            roundFunction = ff.function("round", literal_null);
            Assert.assertNull(roundFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testround_2() {
        try {
            FilterFunction_round_2 round_2 =
                    (FilterFunction_round_2)
                            ff.function("round_2", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "round_2", round_2.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, round_2.getFunctionName().getArgumentCount());

            Function round_2Function = ff.function("round_2", literal_1);
            Assert.assertEquals(
                    "round_2 of (1.0):",
                    Math.round(1.0),
                    ((Long) round_2Function.evaluate(null)).longValue(),
                    0.00001);

            round_2Function = ff.function("round_2", literal_m1);
            Assert.assertEquals(
                    "round_2 of (-1.0):",
                    Math.round(-1.0),
                    ((Long) round_2Function.evaluate(null)).longValue(),
                    0.00001);

            round_2Function = ff.function("round_2", literal_2);
            Assert.assertEquals(
                    "round_2 of (2.0):",
                    Math.round(2.0),
                    ((Long) round_2Function.evaluate(null)).longValue(),
                    0.00001);

            round_2Function = ff.function("round_2", literal_m2);
            Assert.assertEquals(
                    "round_2 of (-2.0):",
                    Math.round(-2.0),
                    ((Long) round_2Function.evaluate(null)).longValue(),
                    0.00001);

            round_2Function = ff.function("round_2", literal_pi);
            Assert.assertEquals(
                    "round_2 of (3.141592653589793):",
                    Math.round(3.141592653589793),
                    ((Long) round_2Function.evaluate(null)).longValue(),
                    0.00001);

            round_2Function = ff.function("round_2", literal_05pi);
            Assert.assertEquals(
                    "round_2 of (1.5707963267948966):",
                    Math.round(1.5707963267948966),
                    ((Long) round_2Function.evaluate(null)).longValue(),
                    0.00001);

            round_2Function = ff.function("round_2", literal_null);
            Assert.assertNull(round_2Function.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testtoDegrees() {
        try {
            FilterFunction_toDegrees toDegrees =
                    (FilterFunction_toDegrees)
                            ff.function("toDegrees", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "toDegrees", toDegrees.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, toDegrees.getFunctionName().getArgumentCount());

            Function toDegreesFunction = ff.function("toDegrees", literal_1);
            double good0 = Math.toDegrees(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "toDegrees of (1.0):",
                        Double.isNaN(((Double) toDegreesFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toDegrees of (1.0):",
                        Math.toDegrees(1.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toDegreesFunction = ff.function("toDegrees", literal_m1);
            double good1 = Math.toDegrees(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "toDegrees of (-1.0):",
                        Double.isNaN(((Double) toDegreesFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toDegrees of (-1.0):",
                        Math.toDegrees(-1.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toDegreesFunction = ff.function("toDegrees", literal_2);
            double good2 = Math.toDegrees(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "toDegrees of (2.0):",
                        Double.isNaN(((Double) toDegreesFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toDegrees of (2.0):",
                        Math.toDegrees(2.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toDegreesFunction = ff.function("toDegrees", literal_m2);
            double good3 = Math.toDegrees(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "toDegrees of (-2.0):",
                        Double.isNaN(((Double) toDegreesFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toDegrees of (-2.0):",
                        Math.toDegrees(-2.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toDegreesFunction = ff.function("toDegrees", literal_pi);
            double good4 = Math.toDegrees(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "toDegrees of (3.141592653589793):",
                        Double.isNaN(((Double) toDegreesFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toDegrees of (3.141592653589793):",
                        Math.toDegrees(3.141592653589793),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toDegreesFunction = ff.function("toDegrees", literal_05pi);
            double good5 = Math.toDegrees(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "toDegrees of (1.5707963267948966):",
                        Double.isNaN(((Double) toDegreesFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toDegrees of (1.5707963267948966):",
                        Math.toDegrees(1.5707963267948966),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toDegreesFunction = ff.function("toDegrees", literal_null);
            Assert.assertNull(toDegreesFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testtoRadians() {
        try {
            FilterFunction_toRadians toRadians =
                    (FilterFunction_toRadians)
                            ff.function("toRadians", org.geotools.api.filter.expression.Expression.NIL);
            Assert.assertEquals("Name is, ", "toRadians", toRadians.getName());
            Assert.assertEquals(
                    "Number of arguments, ", 1, toRadians.getFunctionName().getArgumentCount());

            Function toRadiansFunction = ff.function("toRadians", literal_1);
            double good0 = Math.toRadians(1.0);
            if (Double.isNaN(good0)) {
                Assert.assertTrue(
                        "toRadians of (1.0):",
                        Double.isNaN(((Double) toRadiansFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toRadians of (1.0):",
                        Math.toRadians(1.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toRadiansFunction = ff.function("toRadians", literal_m1);
            double good1 = Math.toRadians(-1.0);
            if (Double.isNaN(good1)) {
                Assert.assertTrue(
                        "toRadians of (-1.0):",
                        Double.isNaN(((Double) toRadiansFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toRadians of (-1.0):",
                        Math.toRadians(-1.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toRadiansFunction = ff.function("toRadians", literal_2);
            double good2 = Math.toRadians(2.0);
            if (Double.isNaN(good2)) {
                Assert.assertTrue(
                        "toRadians of (2.0):",
                        Double.isNaN(((Double) toRadiansFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toRadians of (2.0):",
                        Math.toRadians(2.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toRadiansFunction = ff.function("toRadians", literal_m2);
            double good3 = Math.toRadians(-2.0);
            if (Double.isNaN(good3)) {
                Assert.assertTrue(
                        "toRadians of (-2.0):",
                        Double.isNaN(((Double) toRadiansFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toRadians of (-2.0):",
                        Math.toRadians(-2.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toRadiansFunction = ff.function("toRadians", literal_pi);
            double good4 = Math.toRadians(Math.PI);
            if (Double.isNaN(good4)) {
                Assert.assertTrue(
                        "toRadians of (3.141592653589793):",
                        Double.isNaN(((Double) toRadiansFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toRadians of (3.141592653589793):",
                        Math.toRadians(3.141592653589793),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toRadiansFunction = ff.function("toRadians", literal_05pi);
            double good5 = Math.toRadians(1.5707963267948966);
            if (Double.isNaN(good5)) {
                Assert.assertTrue(
                        "toRadians of (1.5707963267948966):",
                        Double.isNaN(((Double) toRadiansFunction.evaluate(null)).doubleValue()));
            } else {
                Assert.assertEquals(
                        "toRadians of (1.5707963267948966):",
                        Math.toRadians(1.5707963267948966),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }

            toRadiansFunction = ff.function("toRadians", literal_null);
            Assert.assertNull(toRadiansFunction.evaluate(null));

        } catch (FactoryRegistryException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testToLowerCase() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function f = ff.function("strToLowerCase", ff.literal("UPCASE"));
        Assert.assertEquals("upcase", f.evaluate(null));
    }

    @Test
    public void testToUpperCase() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function f = ff.function("strToUpperCase", ff.literal("lowcase"));
        Assert.assertEquals("LOWCASE", f.evaluate(null));
    }
}
