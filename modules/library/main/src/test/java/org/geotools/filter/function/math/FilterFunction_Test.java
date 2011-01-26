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

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class FilterFunction_Test extends TestCase {

    private Literal literal_1 = null;

    private Literal literal_m1;

    private Literal literal_2;

    private Literal literal_m2;

    private Literal literal_pi;

    private Literal literal_05pi;

    private FilterFactory ff;

    protected void setUp() throws Exception {
        super.setUp();
        ff = (FilterFactoryImpl) CommonFactoryFinder.getFilterFactory2(null);

        literal_1 = ff.literal(new Double(1));
        literal_m1 = ff.literal(new Double(-1));
        literal_2 = ff.literal(new Double(2));
        literal_m2 = ff.literal(new Double(-2));
        literal_pi = ff.literal(new Double(Math.PI));
        literal_05pi = ff.literal(new Double(0.5 * Math.PI));
        assertEquals("Literal Expression 0.0", new Double(1.0), literal_1
                .evaluate(null));
        assertEquals("Literal Expression pi", new Double(Math.PI), literal_pi
                .evaluate(null));
        assertEquals("Literal Expression 05pi", new Double(0.5 * Math.PI),
                literal_05pi.evaluate(null));

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testsin() {
        try {
            FilterFunction_sin sin = (FilterFunction_sin) ff.function("sin", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "sin", sin.getName());
            assertEquals("Number of arguments, ", 1, sin.getArgCount());

            Function sinFunction = ff.function("sin", literal_1);
            double good0 = Math.sin(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("sin of (1.0):", Double.isNaN(((Double) sinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sin of (1.0):", (double) Math.sin(1.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sinFunction = ff.function("sin", literal_m1);
            double good1 = Math.sin(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("sin of (-1.0):", Double.isNaN(((Double) sinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sin of (-1.0):", (double) Math.sin(-1.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            sinFunction = ff.function("sin", literal_2);
            double good2 = Math.sin(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("sin of (2.0):", Double.isNaN(((Double) sinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sin of (2.0):", (double) Math.sin(2.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sinFunction = ff.function("sin", literal_m2);
            double good3 = Math.sin(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("sin of (-2.0):", Double.isNaN(((Double) sinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sin of (-2.0):", (double) Math.sin(-2.0),
                        ((Double) sinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sinFunction = ff.function("sin",literal_pi);
            double good4 = Math.sin(3.141592653589793);
            if (Double.isNaN(good4)) {
                assertTrue("sin of (3.141592653589793):", Double
                        .isNaN(((Double) sinFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("sin of (3.141592653589793):", (double) Math
                        .sin(3.141592653589793), ((Double) sinFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            sinFunction = ff.function("sin", literal_05pi);
            double good5 = Math.sin(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("sin of (1.5707963267948966):", Double
                        .isNaN(((Double) sinFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("sin of (1.5707963267948966):", (double) Math
                        .sin(1.5707963267948966), ((Double) sinFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testcos() {
        try {

            FilterFunction_cos cos = (FilterFunction_cos) ff.function("cos", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "cos", cos.getName());
            assertEquals("Number of arguments, ", 1, cos.getArgCount());

            Function cosFunction = ff.function("cos", literal_1);
            double good0 = Math.cos(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("cos of (1.0):", Double.isNaN(((Double) cosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("cos of (1.0):", (double) Math.cos(1.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            cosFunction = ff.function("cos", literal_m1);
            double good1 = Math.cos(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("cos of (-1.0):", Double.isNaN(((Double) cosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("cos of (-1.0):", (double) Math.cos(-1.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            cosFunction = ff.function("cos", literal_2);
            double good2 = Math.cos(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("cos of (2.0):", Double.isNaN(((Double) cosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("cos of (2.0):", (double) Math.cos(2.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            cosFunction = ff.function("cos", literal_m2);
            double good3 = Math.cos(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("cos of (-2.0):", Double.isNaN(((Double) cosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("cos of (-2.0):", (double) Math.cos(-2.0),
                        ((Double) cosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            cosFunction = ff.function("cos", literal_pi);
            double good4 = Math.cos(3.141592653589793);
            if (Double.isNaN(good4)) {
                assertTrue("cos of (3.141592653589793):", Double
                        .isNaN(((Double) cosFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("cos of (3.141592653589793):", (double) Math
                        .cos(3.141592653589793), ((Double) cosFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            cosFunction = ff.function("cos", literal_05pi);
            double good5 = Math.cos(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("cos of (1.5707963267948966):", Double
                        .isNaN(((Double) cosFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("cos of (1.5707963267948966):", (double) Math
                        .cos(1.5707963267948966), ((Double) cosFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testtan() {
        try {

            FilterFunction_tan tan = (FilterFunction_tan) ff.function("tan", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "tan", tan.getName());
            assertEquals("Number of arguments, ", 1, tan.getArgCount());

            Function tanFunction = ff.function("tan", literal_1);
            double good0 = Math.tan(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("tan of (1.0):", Double.isNaN(((Double) tanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("tan of (1.0):", (double) Math.tan(1.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            tanFunction = ff.function("tan", literal_m1);
            double good1 = Math.tan(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("tan of (-1.0):", Double.isNaN(((Double) tanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("tan of (-1.0):", (double) Math.tan(-1.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            tanFunction = ff.function("tan", literal_2);
            double good2 = Math.tan(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("tan of (2.0):", Double.isNaN(((Double) tanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("tan of (2.0):", (double) Math.tan(2.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            tanFunction = ff.function("tan", literal_m2);
            double good3 = Math.tan(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("tan of (-2.0):", Double.isNaN(((Double) tanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("tan of (-2.0):", (double) Math.tan(-2.0),
                        ((Double) tanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            tanFunction = ff.function("tan", literal_pi);
            double good4 = Math.tan(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("tan of (3.141592653589793):", Double
                        .isNaN(((Double) tanFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("tan of (3.141592653589793):", (double) Math
                        .tan(3.141592653589793), ((Double) tanFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            tanFunction = ff.function("tan", literal_05pi);
            double good5 = Math.tan(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("tan of (1.5707963267948966):", Double
                        .isNaN(((Double) tanFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("tan of (1.5707963267948966):", (double) Math
                        .tan(1.5707963267948966), ((Double) tanFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testatan2() {
        try {
            FilterFunction_atan2 atan2 = (FilterFunction_atan2) ff.function("atan2", 
                    org.opengis.filter.expression.Expression.NIL,
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "atan2", atan2.getName());
            assertEquals("Number of arguments, ", 2, atan2.getArgCount());

            Function atan2Function = ff.function("atan2", literal_1, literal_m1);
            double good0 = Math.atan2(1.0, -1.0);
            if (Double.isNaN(good0)) {
                assertTrue("atan2 of (1.0,-1.0):", Double
                        .isNaN(((Double) atan2Function.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan2 of (1.0,-1.0):", (double) Math.atan2(1.0,
                        -1.0), ((Double) atan2Function.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            atan2Function = ff.function("atan2", literal_m1, literal_2);
            double good1 = Math.atan2(-1.0, 2.0);
            if (Double.isNaN(good1)) {
                assertTrue("atan2 of (-1.0,2.0):", Double
                        .isNaN(((Double) atan2Function.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan2 of (-1.0,2.0):", (double) Math.atan2(-1.0,
                        2.0), ((Double) atan2Function.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            atan2Function = ff.function("atan2", literal_2, literal_m2);
            double good2 = Math.atan2(2.0, -2.0);
            if (Double.isNaN(good2)) {
                assertTrue("atan2 of (2.0,-2.0):", Double
                        .isNaN(((Double) atan2Function.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan2 of (2.0,-2.0):", (double) Math.atan2(2.0,
                        -2.0), ((Double) atan2Function.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            atan2Function = ff.function("atan2", literal_m2, literal_pi);
            double good3 = Math.atan2(-2.0, 3.141592653589793);
            if (Double.isNaN(good3)) {
                assertTrue("atan2 of (-2.0,3.141592653589793):", Double
                        .isNaN(((Double) atan2Function.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan2 of (-2.0,3.141592653589793):",
                        (double) Math.atan2(-2.0, 3.141592653589793),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            atan2Function = ff.function("atan2", literal_pi, literal_05pi);
            double good4 = Math.atan2(3.141592653589793, 1.5707963267948966);
            if (Double.isNaN(good4)) {
                assertTrue("atan2 of (3.141592653589793,1.5707963267948966):",
                        Double.isNaN(((Double) atan2Function.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals(
                        "atan2 of (3.141592653589793,1.5707963267948966):",
                        (double) Math.atan2(3.141592653589793,
                                1.5707963267948966), ((Double) atan2Function
                                .evaluate(null)).doubleValue(), 0.00001);
            }
            
            atan2Function = ff.function("atan2", literal_05pi, literal_1);
            double good5 = Math.atan2(1.5707963267948966, 1.0);
            if (Double.isNaN(good5)) {
                assertTrue("atan2 of (1.5707963267948966,1.0):", Double
                        .isNaN(((Double) atan2Function.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan2 of (1.5707963267948966,1.0):",
                        (double) Math.atan2(1.5707963267948966, 1.0),
                        ((Double) atan2Function.evaluate(null)).doubleValue(),
                        0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testsqrt() {
        try {
            FilterFunction_sqrt sqrt = (FilterFunction_sqrt) ff.function("sqrt", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "sqrt", sqrt.getName());
            assertEquals("Number of arguments, ", 1, sqrt.getArgCount());

            Function sqrtFunction = ff.function("sqrt", literal_1);
            double good0 = Math.sqrt(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("sqrt of (1.0):", Double.isNaN(((Double) sqrtFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sqrt of (1.0):", (double) Math.sqrt(1.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sqrtFunction = ff.function("sqrt", literal_m1);
            double good1 = Math.sqrt(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("sqrt of (-1.0):", Double.isNaN(((Double) sqrtFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sqrt of (-1.0):", (double) Math.sqrt(-1.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sqrtFunction = ff.function("sqrt", literal_2);
            double good2 = Math.sqrt(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("sqrt of (2.0):", Double.isNaN(((Double) sqrtFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sqrt of (2.0):", (double) Math.sqrt(2.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sqrtFunction = ff.function("sqrt", literal_m2);
            double good3 = Math.sqrt(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("sqrt of (-2.0):", Double.isNaN(((Double) sqrtFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("sqrt of (-2.0):", (double) Math.sqrt(-2.0),
                        ((Double) sqrtFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            sqrtFunction = ff.function("sqrt", literal_pi);
            double good4 = Math.sqrt(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("sqrt of (3.141592653589793):", Double
                        .isNaN(((Double) sqrtFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("sqrt of (3.141592653589793):", (double) Math
                        .sqrt(3.141592653589793), ((Double) sqrtFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            sqrtFunction = ff.function("sqrt", literal_05pi);
            double good5 = Math.sqrt(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("sqrt of (1.5707963267948966):", Double
                        .isNaN(((Double) sqrtFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("sqrt of (1.5707963267948966):", (double) Math
                        .sqrt(1.5707963267948966), ((Double) sqrtFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testpow() {
        try {
            FilterFunction_pow pow = (FilterFunction_pow) ff.function("pow", 
                    org.opengis.filter.expression.Expression.NIL,
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "pow", pow.getName());
            assertEquals("Number of arguments, ", 2, pow.getArgCount());

            Function powFunction = ff.function("pow", literal_1, literal_m1);
            double good0 = Math.pow(1.0, -1.0);
            if (Double.isNaN(good0)) {
                assertTrue("pow of (1.0,-1.0):", Double
                        .isNaN(((Double) powFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("pow of (1.0,-1.0):", (double) Math.pow(1.0,
                        -1.0), ((Double) powFunction.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            powFunction = ff.function("pow", literal_m1, literal_2);
            double good1 = Math.pow(-1.0, 2.0);
            if (Double.isNaN(good1)) {
                assertTrue("pow of (-1.0,2.0):", Double
                        .isNaN(((Double) powFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("pow of (-1.0,2.0):", (double) Math.pow(-1.0,
                        2.0), ((Double) powFunction.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            powFunction = ff.function("pow", literal_2, literal_m2);
            double good2 = Math.pow(2.0, -2.0);
            if (Double.isNaN(good2)) {
                assertTrue("pow of (2.0,-2.0):", Double
                        .isNaN(((Double) powFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("pow of (2.0,-2.0):", (double) Math.pow(2.0,
                        -2.0), ((Double) powFunction.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            powFunction = ff.function("pow", literal_m2, literal_pi);
            double good3 = Math.pow(-2.0, 3.141592653589793);
            if (Double.isNaN(good3)) {
                assertTrue("pow of (-2.0,3.141592653589793):", Double
                        .isNaN(((Double) powFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("pow of (-2.0,3.141592653589793):",
                        (double) Math.pow(-2.0, 3.141592653589793),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            powFunction = ff.function("pow", literal_pi, literal_05pi);
            double good4 = Math.pow(3.141592653589793, 1.5707963267948966);
            if (Double.isNaN(good4)) {
                assertTrue("pow of (3.141592653589793,1.5707963267948966):",
                        Double.isNaN(((Double) powFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals(
                        "pow of (3.141592653589793,1.5707963267948966):",
                        (double) Math.pow(3.141592653589793,
                                1.5707963267948966), ((Double) powFunction
                                .evaluate(null)).doubleValue(), 0.00001);
            }
            
            powFunction = ff.function("pow", literal_05pi, literal_1);
            double good5 = Math.pow(1.5707963267948966, 1.0);
            if (Double.isNaN(good5)) {
                assertTrue("pow of (1.5707963267948966,1.0):", Double
                        .isNaN(((Double) powFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("pow of (1.5707963267948966,1.0):",
                        (double) Math.pow(1.5707963267948966, 1.0),
                        ((Double) powFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmin_4() {
        try {
            FilterFunction_min_4 min_4 = (FilterFunction_min_4) ff.function("min_4", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "min_4", min_4.getName());
            assertEquals("Number of arguments, ", 2, min_4.getArgCount());

            Function min_4Function = ff.function("min_4", literal_1, literal_m1);
            assertEquals("min of (1.0,-1.0):", (long) Math.min(1.0, -1.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(), 0.00001);
            
            min_4Function = ff.function("min_4", literal_m1, literal_2);
            assertEquals("min of (-1.0,2.0):", (long) Math.min(-1.0, 2.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(), 0.00001);
            
            min_4Function = ff.function("min_4", literal_2, literal_m2);
            assertEquals("min of (2.0,-2.0):", (long) Math.min(2.0, -2.0),
                    ((Integer) min_4Function.evaluate(null)).intValue(), 0.00001);
            
            min_4Function = ff.function("min_4", literal_m2, literal_pi);
            assertEquals("min of (-2.0,3.141592653589793):", (long) Math.min(
                    -2.0, 3.141592653589793), ((Integer) min_4Function
                    .evaluate(null)).intValue(), 0.00001);
            
            min_4Function = ff.function("min_4", literal_pi, literal_05pi);
            assertEquals("min of (3.141592653589793,1.5707963267948966):",
                    (long) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Integer) min_4Function.evaluate(null)).intValue(), 0.00001);
            
            min_4Function = ff.function("min_4", literal_05pi, literal_1);
            assertEquals("min of (1.5707963267948966,1.0):", (long) Math.min(
                    1.5707963267948966, 1.0), ((Integer) min_4Function
                    .evaluate(null)).intValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmin_2() {
        try {
            FilterFunction_min_2 min_2 = (FilterFunction_min_2) ff.function("min_2", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "min_2", min_2.getName());
            assertEquals("Number of arguments, ", 2, min_2.getArgCount());

            Function min_2Function = ff.function("min_2", literal_1, literal_m1);
            assertEquals("min of (1.0,-1.0):", (long) Math.min(1.0, -1.0),
                    ((Long) min_2Function.evaluate(null)).longValue(), 0.00001);
            
            min_2Function = ff.function("min_2", literal_m1, literal_2);
            assertEquals("min of (-1.0,2.0):", (long) Math.min(-1.0, 2.0),
                    ((Long) min_2Function.evaluate(null)).longValue(), 0.00001);
            
            min_2Function = ff.function("min_2", literal_2, literal_m2);
            assertEquals("min of (2.0,-2.0):", (long) Math.min(2.0, -2.0),
                    ((Long) min_2Function.evaluate(null)).longValue(), 0.00001);
            
            min_2Function = ff.function("min_2", literal_m2, literal_pi);
            assertEquals("min of (-2.0,3.141592653589793):", (long) Math.min(
                    -2.0, 3.141592653589793), ((Long) min_2Function
                    .evaluate(null)).longValue(), 0.00001);
            
            min_2Function = ff.function("min_2", literal_pi, literal_05pi);
            assertEquals("min of (3.141592653589793,1.5707963267948966):",
                    (long) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Long) min_2Function.evaluate(null)).longValue(), 0.00001);
            
            min_2Function = ff.function("min_2", literal_05pi, literal_1);
            assertEquals("min of (1.5707963267948966,1.0):", (long) Math.min(
                    1.5707963267948966, 1.0), ((Long) min_2Function
                    .evaluate(null)).longValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmin_3() {
        try {
            FilterFunction_min_3 min_3 = (FilterFunction_min_3) ff.function("min_3", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "min_3", min_3.getName());
            assertEquals("Number of arguments, ", 2, min_3.getArgCount());

            Function min_3Function = ff.function("min_3", literal_1, literal_m1);
            assertEquals("min of (1.0,-1.0):", (float) Math.min(1.0, -1.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(), 0.00001);
            
            min_3Function = ff.function("min_3", literal_m1, literal_2);
            assertEquals("min of (-1.0,2.0):", (float) Math.min(-1.0, 2.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(), 0.00001);
            
            min_3Function = ff.function("min_3", literal_2, literal_m2);
            assertEquals("min of (2.0,-2.0):", (float) Math.min(2.0, -2.0),
                    ((Float) min_3Function.evaluate(null)).floatValue(), 0.00001);
            
            min_3Function = ff.function("min_3", literal_m2, literal_pi);
            assertEquals("min of (-2.0,3.141592653589793):", (float) Math.min(
                    -2.0, 3.141592653589793), ((Float) min_3Function
                    .evaluate(null)).floatValue(), 0.00001);
            
            min_3Function = ff.function("min_3", literal_pi, literal_05pi);
            assertEquals("min of (3.141592653589793,1.5707963267948966):",
                    (float) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Float) min_3Function.evaluate(null)).floatValue(), 0.00001);
            
            min_3Function = ff.function("min_3", literal_05pi, literal_1);
            assertEquals("min of (1.5707963267948966,1.0):", (float) Math.min(
                    1.5707963267948966, 1.0), ((Float) min_3Function
                    .evaluate(null)).floatValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmin() {
        try {
            FilterFunction_min min = (FilterFunction_min) ff.function("min", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "min", min.getName());
            assertEquals("Number of arguments, ", 2, min.getArgCount());

            Function minFunction = ff.function("min", literal_1, literal_m1);
            assertEquals("min of (1.0,-1.0):", (double) Math.min(1.0, -1.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(), 0.00001);
            
            minFunction = ff.function("min", literal_m1, literal_2);
            assertEquals("min of (-1.0,2.0):", (double) Math.min(-1.0, 2.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(), 0.00001);
            
            minFunction = ff.function("min", literal_2, literal_m2);
            assertEquals("min of (2.0,-2.0):", (double) Math.min(2.0, -2.0),
                    ((Double) minFunction.evaluate(null)).doubleValue(), 0.00001);
            
            minFunction = ff.function("min", literal_m2, literal_pi);
            assertEquals("min of (-2.0,3.141592653589793):", (double) Math.min(
                    -2.0, 3.141592653589793), ((Double) minFunction
                    .evaluate(null)).doubleValue(), 0.00001);
            
            minFunction = ff.function("min", literal_pi, literal_05pi);
            assertEquals("min of (3.141592653589793,1.5707963267948966):",
                    (double) Math.min(3.141592653589793, 1.5707963267948966),
                    ((Double) minFunction.evaluate(null)).doubleValue(), 0.00001);
            
            minFunction = ff.function("min", literal_05pi, literal_1);
            assertEquals("min of (1.5707963267948966,1.0):", (double) Math.min(
                    1.5707963267948966, 1.0), ((Double) minFunction
                    .evaluate(null)).doubleValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmax_4() {
        try {
            FilterFunction_max_4 max_4 = (FilterFunction_max_4) ff.function("max_4", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "max_4", max_4.getName());
            assertEquals("Number of arguments, ", 2, max_4.getArgCount());

            Function max_4Function = ff.function("max_4", literal_1, literal_m1);
            assertEquals("max_4 of (1.0,-1.0):", (int) Math.max(1.0, -1.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(), 0.00001);
            
            max_4Function = ff.function("max_4", literal_m1, literal_2);
            assertEquals("max_4 of (-1.0,2.0):", (int) Math.max(-1.0, 2.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(), 0.00001);
            
            max_4Function = ff.function("max_4", literal_2, literal_m2);
            assertEquals("max_4 of (2.0,-2.0):", (int) Math.max(2.0, -2.0),
                    ((Integer) max_4Function.evaluate(null)).intValue(), 0.00001);
            
            max_4Function = ff.function("max_4", literal_m2, literal_pi);
            assertEquals("max_4 of (-2.0,3.141592653589793):", (int) Math.max(
                    -2.0, 3.141592653589793), ((Integer) max_4Function
                    .evaluate(null)).intValue(), 0.00001);
            
            max_4Function = ff.function("max_4", literal_pi, literal_05pi);
            assertEquals("max_4 of (3.141592653589793,1.5707963267948966):",
                    (int) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Integer) max_4Function.evaluate(null)).intValue(), 0.00001);
            
            max_4Function = ff.function("max_4", literal_05pi, literal_1);
            assertEquals("max_4 of (1.5707963267948966,1.0):", (int) Math.max(
                    1.5707963267948966, 1.0), ((Integer) max_4Function
                    .evaluate(null)).intValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmax_2() {
        try {
            FilterFunction_max_2 max_2 = (FilterFunction_max_2) ff.function("max_2", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "max_2", max_2.getName());
            assertEquals("Number of arguments, ", 2, max_2.getArgCount());

            Function max_2Function = ff.function("max_2", literal_1, literal_m1);
            assertEquals("max_2 of (1.0,-1.0):", (long) Math.max(1.0, -1.0),
                    ((Long) max_2Function.evaluate(null)).longValue(), 0.00001);
            
            max_2Function = ff.function("max_2", literal_m1, literal_2);
            assertEquals("max_2 of (-1.0,2.0):", (long) Math.max(-1.0, 2.0),
                    ((Long) max_2Function.evaluate(null)).longValue(), 0.00001);
            
            max_2Function = ff.function("max_2", literal_2, literal_m2);
            assertEquals("max_2 of (2.0,-2.0):", (long) Math.max(2.0, -2.0),
                    ((Long) max_2Function.evaluate(null)).longValue(), 0.00001);
            
            max_2Function = ff.function("max_2", literal_m2, literal_pi);
            assertEquals("max_2 of (-2.0,3.141592653589793):", (long) Math.max(
                    -2.0, 3.141592653589793), ((Long) max_2Function
                    .evaluate(null)).longValue(), 0.00001);
            
            max_2Function = ff.function("max_2", literal_pi, literal_05pi);
            assertEquals("max_2 of (3.141592653589793,1.5707963267948966):",
                    (long) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Long) max_2Function.evaluate(null)).longValue(), 0.00001);
            
            max_2Function = ff.function("max_2", literal_05pi, literal_1);
            assertEquals("max_2 of (1.5707963267948966,1.0):", (long) Math.max(
                    1.5707963267948966, 1.0), ((Long) max_2Function
                    .evaluate(null)).longValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmax_3() {
        try {
            FilterFunction_max_3 max_3 = (FilterFunction_max_3) ff.function("max_3", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "max_3", max_3.getName());
            assertEquals("Number of arguments, ", 2, max_3.getArgCount());

            Function max_3Function = ff.function("max_3", literal_1, literal_m1);
            assertEquals("max_3 of (1.0,-1.0):", (float) Math.max(1.0, -1.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(), 0.00001);
            
            max_3Function = ff.function("max_3", literal_m1, literal_2);
            assertEquals("max_3 of (-1.0,2.0):", (float) Math.max(-1.0, 2.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(), 0.00001);
            
            max_3Function = ff.function("max_3", literal_2, literal_m2);
            assertEquals("max_3 of (2.0,-2.0):", (float) Math.max(2.0, -2.0),
                    ((Float) max_3Function.evaluate(null)).floatValue(), 0.00001);
            
            max_3Function = ff.function("max_3", literal_m2, literal_pi);
            assertEquals("max_3 of (-2.0,3.141592653589793):", (float) Math.max(
                    -2.0, 3.141592653589793), ((Float) max_3Function
                    .evaluate(null)).floatValue(), 0.00001);
            
            max_3Function = ff.function("max_3", literal_pi, literal_05pi);
            assertEquals("max_3 of (3.141592653589793,1.5707963267948966):",
                    (float) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Float) max_3Function.evaluate(null)).floatValue(), 0.00001);
            
            max_3Function = ff.function("max_3", literal_05pi, literal_1);
            assertEquals("max_3 of (1.5707963267948966,1.0):", (float) Math.max(
                    1.5707963267948966, 1.0), ((Float) max_3Function
                    .evaluate(null)).floatValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testmax() {
        try {
            FilterFunction_max max = (FilterFunction_max) ff.function("max", 
                    new org.opengis.filter.expression.Expression[2]);
            assertEquals("Name is, ", "max", max.getName());
            assertEquals("Number of arguments, ", 2, max.getArgCount());

            Function maxFunction = ff.function("max", literal_1, literal_m1);
            assertEquals("max of (1.0,-1.0):", (double) Math.max(1.0, -1.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(), 0.00001);
            
            maxFunction = ff.function("max", literal_m1, literal_2);
            assertEquals("max of (-1.0,2.0):", (double) Math.max(-1.0, 2.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(), 0.00001);
            
            maxFunction = ff.function("max", literal_2, literal_m2);
            assertEquals("max of (2.0,-2.0):", (double) Math.max(2.0, -2.0),
                    ((Double) maxFunction.evaluate(null)).doubleValue(), 0.00001);
            
            maxFunction = ff.function("max", literal_m2, literal_pi);
            assertEquals("max of (-2.0,3.141592653589793):", (double) Math.max(
                    -2.0, 3.141592653589793), ((Double) maxFunction
                    .evaluate(null)).doubleValue(), 0.00001);
            
            maxFunction = ff.function("max", literal_pi, literal_05pi);
            assertEquals("max of (3.141592653589793,1.5707963267948966):",
                    (double) Math.max(3.141592653589793, 1.5707963267948966),
                    ((Double) maxFunction.evaluate(null)).doubleValue(), 0.00001);
            
            maxFunction = ff.function("max", literal_05pi, literal_1);
            assertEquals("max of (1.5707963267948966,1.0):", (double) Math.max(
                    1.5707963267948966, 1.0), ((Double) maxFunction
                    .evaluate(null)).doubleValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testabs() {
        try {
            FilterFunction_abs abs = (FilterFunction_abs) ff.function("abs", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "abs", abs.getName());
            assertEquals("Number of arguments, ", 1, abs.getArgCount());

            Function absFunction = ff.function("abs", literal_1);
            assertEquals("abs of (1.0):", (int) Math.abs(1.0),
                    ((Integer) absFunction.evaluate(null)).intValue(), 0.00001);
            
            absFunction = ff.function("abs", literal_m1);
            assertEquals("abs of (-1.0):", (int) Math.abs(-1.0),
                    ((Integer) absFunction.evaluate(null)).intValue(), 0.00001);
            
            absFunction = ff.function("abs", literal_2);
            assertEquals("abs of (2.0):", (int) Math.abs(2.0),
                    ((Integer) absFunction.evaluate(null)).intValue(), 0.00001);
            
            absFunction = ff.function("abs", literal_m2);
            assertEquals("abs of (-2.0):", (int) Math.abs(-2.0),
                    ((Integer) absFunction.evaluate(null)).intValue(), 0.00001);
            
            absFunction = ff.function("abs", literal_pi);
            assertEquals("abs of (3.141592653589793):", (int) Math
                    .abs(3.141592653589793), ((Integer) absFunction
                    .evaluate(null)).intValue(), 0.00001);
            
            absFunction = ff.function("abs", literal_05pi);
            assertEquals("abs of (1.5707963267948966):", (int) Math
                    .abs(1.5707963267948966), ((Integer) absFunction
                    .evaluate(null)).intValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testabs_2() {
        try {
            FilterFunction_abs_4 abs_4 = (FilterFunction_abs_4) ff.function("abs_4", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "abs_4", abs_4.getName());
            assertEquals("Number of arguments, ", 1, abs_4.getArgCount());

            Function abs_4Function = ff.function("abs_4", literal_1);
            assertEquals("abs_4 of (1.0):", (double) Math.abs(1.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(), 0.00001);
            
            abs_4Function = ff.function("abs_4", literal_m1);
            assertEquals("abs_4 of (-1.0):", (double) Math.abs(-1.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(), 0.00001);
            
            abs_4Function = ff.function("abs_4", literal_2);
            assertEquals("abs_4 of (2.0):", (double) Math.abs(2.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(), 0.00001);
            
            abs_4Function = ff.function("abs_4", literal_m2);
            assertEquals("abs_4 of (-2.0):", (double) Math.abs(-2.0),
                    ((Double) abs_4Function.evaluate(null)).doubleValue(), 0.00001);
            
            abs_4Function = ff.function("abs_4", literal_pi);
            assertEquals("abs_4 of (3.141592653589793):", (double) Math
                    .abs(3.141592653589793), ((Double) abs_4Function
                    .evaluate(null)).doubleValue(), 0.00001);
            
            abs_4Function = ff.function("abs_4", literal_05pi);
            assertEquals("abs_4 of (1.5707963267948966):", (double) Math
                    .abs(1.5707963267948966), ((Double) abs_4Function
                    .evaluate(null)).doubleValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testabs_3() {
        try {
            FilterFunction_abs_3 abs_3 = (FilterFunction_abs_3) ff.function("abs_3", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "abs_3", abs_3.getName());
            assertEquals("Number of arguments, ", 1, abs_3.getArgCount());

            Function abs_3Function = ff.function("abs_3", literal_1);
            assertEquals("abs_3 of (1.0):", (float) Math.abs(1.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(), 0.00001);
            
            abs_3Function = ff.function("abs_3", literal_m1);
            assertEquals("abs_3 of (-1.0):", (float) Math.abs(-1.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(), 0.00001);
            
            abs_3Function = ff.function("abs_3", literal_2);
            assertEquals("abs_3 of (2.0):", (float) Math.abs(2.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(), 0.00001);
            
            abs_3Function = ff.function("abs_3", literal_m2);
            assertEquals("abs_3 of (-2.0):", (float) Math.abs(-2.0),
                    ((Float) abs_3Function.evaluate(null)).floatValue(), 0.00001);
            
            abs_3Function = ff.function("abs_3", literal_pi);
            assertEquals("abs_3 of (3.141592653589793):", (float) Math
                    .abs(3.141592653589793), ((Float) abs_3Function
                    .evaluate(null)).floatValue(), 0.00001);
            
            abs_3Function = ff.function("abs_3", literal_05pi);
            assertEquals("abs_3 of (1.5707963267948966):", (float) Math
                    .abs(1.5707963267948966), ((Float) abs_3Function
                    .evaluate(null)).floatValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testabs_4() {
        try {
            FilterFunction_abs_2 abs_2 = (FilterFunction_abs_2) ff.function("abs_2", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "abs_2", abs_2.getName());
            assertEquals("Number of arguments, ", 1, abs_2.getArgCount());

            Function abs_2Function = ff.function("abs_2", literal_1);
            assertEquals("abs_2 of (1.0):", (long) Math.abs(1.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(), 0.00001);
            
            abs_2Function = ff.function("abs_2", literal_m1);
            assertEquals("abs_2 of (-1.0):", (long) Math.abs(-1.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(), 0.00001);
            
            abs_2Function = ff.function("abs_2", literal_2);
            assertEquals("abs_2 of (2.0):", (long) Math.abs(2.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(), 0.00001);
            
            abs_2Function = ff.function("abs_2", literal_m2);
            assertEquals("abs_2 of (-2.0):", (long) Math.abs(-2.0),
                    ((Long) abs_2Function.evaluate(null)).longValue(), 0.00001);
            
            abs_2Function = ff.function("abs_2", literal_pi);
            assertEquals("abs_2 of (3.141592653589793):", (long) Math
                    .abs(3.141592653589793), ((Long) abs_2Function
                    .evaluate(null)).longValue(), 0.00001);
            
            abs_2Function = ff.function("abs_2", literal_05pi);
            assertEquals("abs_2 of (1.5707963267948966):", (long) Math
                    .abs(1.5707963267948966), ((Long) abs_2Function
                    .evaluate(null)).longValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testIEEEremainder() {
        try {
            FilterFunction_IEEEremainder IEEEremainder = (FilterFunction_IEEEremainder) ff.function("IEEEremainder", 
                    org.opengis.filter.expression.Expression.NIL,
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "IEEEremainder", IEEEremainder.getName());
            assertEquals("Number of arguments, ", 2, IEEEremainder.getArgCount());

            Function IEEEremainderFunction = ff.function("IEEEremainder", literal_1, literal_m1);
            double good0 = Math.IEEEremainder(1.0, -1.0);
            if (Double.isNaN(good0)) {
                assertTrue("IEEEremainder of (1.0,-1.0):", Double
                        .isNaN(((Double) IEEEremainderFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("IEEEremainder of (1.0,-1.0):", (double) Math.IEEEremainder(1.0,
                        -1.0), ((Double) IEEEremainderFunction.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            IEEEremainderFunction = ff.function("IEEEremainder", literal_m1, literal_2);
            double good1 = Math.IEEEremainder(-1.0, 2.0);
            if (Double.isNaN(good1)) {
                assertTrue("IEEEremainder of (-1.0,2.0):", Double
                        .isNaN(((Double) IEEEremainderFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("IEEEremainder of (-1.0,2.0):", (double) Math.IEEEremainder(-1.0,
                        2.0), ((Double) IEEEremainderFunction.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            IEEEremainderFunction = ff.function("IEEEremainder", literal_2, literal_m2);
            double good2 = Math.IEEEremainder(2.0, -2.0);
            if (Double.isNaN(good2)) {
                assertTrue("IEEEremainder of (2.0,-2.0):", Double
                        .isNaN(((Double) IEEEremainderFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("IEEEremainder of (2.0,-2.0):", (double) Math.IEEEremainder(2.0,
                        -2.0), ((Double) IEEEremainderFunction.evaluate(null))
                        .doubleValue(), 0.00001);
            }
            
            IEEEremainderFunction = ff.function("IEEEremainder", literal_m2, literal_pi);
            double good3 = Math.IEEEremainder(-2.0, 3.141592653589793);
            if (Double.isNaN(good3)) {
                assertTrue("IEEEremainder of (-2.0,3.141592653589793):", Double
                        .isNaN(((Double) IEEEremainderFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("IEEEremainder of (-2.0,3.141592653589793):",
                        (double) Math.IEEEremainder(-2.0, 3.141592653589793),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            IEEEremainderFunction = ff.function("IEEEremainder", literal_pi, literal_05pi);
            double good4 = Math.IEEEremainder(3.141592653589793, 1.5707963267948966);
            if (Double.isNaN(good4)) {
                assertTrue("IEEEremainder of (3.141592653589793,1.5707963267948966):",
                        Double.isNaN(((Double) IEEEremainderFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals(
                        "IEEEremainder of (3.141592653589793,1.5707963267948966):",
                        (double) Math.IEEEremainder(3.141592653589793,
                                1.5707963267948966), ((Double) IEEEremainderFunction
                                .evaluate(null)).doubleValue(), 0.00001);
            }
            
            IEEEremainderFunction = ff.function("IEEEremainder", literal_05pi, literal_1);
            double good5 = Math.IEEEremainder(1.5707963267948966, 1.0);
            if (Double.isNaN(good5)) {
                assertTrue("IEEEremainder of (1.5707963267948966,1.0):", Double
                        .isNaN(((Double) IEEEremainderFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("IEEEremainder of (1.5707963267948966,1.0):",
                        (double) Math.IEEEremainder(1.5707963267948966, 1.0),
                        ((Double) IEEEremainderFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testacos() {
        try {
            FilterFunction_acos acos = (FilterFunction_acos) ff.function("acos", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "acos", acos.getName());
            assertEquals("Number of arguments, ", 1, acos.getArgCount());

            Function acosFunction = ff.function("acos", literal_1);
            double good0 = Math.acos(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("acos of (1.0):", Double.isNaN(((Double) acosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("acos of (1.0):", (double) Math.acos(1.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            acosFunction = ff.function("acos", literal_m1);
            double good1 = Math.acos(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("acos of (-1.0):", Double.isNaN(((Double) acosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("acos of (-1.0):", (double) Math.acos(-1.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            acosFunction = ff.function("acos", literal_2);
            double good2 = Math.acos(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("acos of (2.0):", Double.isNaN(((Double) acosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("acos of (2.0):", (double) Math.acos(2.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            acosFunction = ff.function("acos", literal_m2);
            double good3 = Math.acos(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("acos of (-2.0):", Double.isNaN(((Double) acosFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("acos of (-2.0):", (double) Math.acos(-2.0),
                        ((Double) acosFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            acosFunction = ff.function("acos", literal_pi);
            double good4 = Math.acos(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("acos of (3.141592653589793):", Double
                        .isNaN(((Double) acosFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("acos of (3.141592653589793):", (double) Math
                        .acos(3.141592653589793), ((Double) acosFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            acosFunction = ff.function("acos", literal_05pi);
            double good5 = Math.acos(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("acos of (1.5707963267948966):", Double
                        .isNaN(((Double) acosFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("acos of (1.5707963267948966):", (double) Math
                        .acos(1.5707963267948966), ((Double) acosFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testasin() {
        try {
            FilterFunction_asin asin = (FilterFunction_asin) ff.function("asin", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "asin", asin.getName());
            assertEquals("Number of arguments, ", 1, asin.getArgCount());

            Function asinFunction = ff.function("asin", literal_1);
            double good0 = Math.asin(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("asin of (1.0):", Double.isNaN(((Double) asinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("asin of (1.0):", (double) Math.asin(1.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            asinFunction = ff.function("asin", literal_m1);
            double good1 = Math.asin(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("asin of (-1.0):", Double.isNaN(((Double) asinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("asin of (-1.0):", (double) Math.asin(-1.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            asinFunction = ff.function("asin", literal_2);
            double good2 = Math.asin(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("asin of (2.0):", Double.isNaN(((Double) asinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("asin of (2.0):", (double) Math.asin(2.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            asinFunction = ff.function("asin", literal_m2);
            double good3 = Math.asin(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("asin of (-2.0):", Double.isNaN(((Double) asinFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("asin of (-2.0):", (double) Math.asin(-2.0),
                        ((Double) asinFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            asinFunction = ff.function("asin", literal_pi);
            double good4 = Math.asin(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("asin of (3.141592653589793):", Double
                        .isNaN(((Double) asinFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("asin of (3.141592653589793):", (double) Math
                        .asin(3.141592653589793), ((Double) asinFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            asinFunction = ff.function("asin", literal_05pi);
            double good5 = Math.asin(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("asin of (1.5707963267948966):", Double
                        .isNaN(((Double) asinFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("asin of (1.5707963267948966):", (double) Math
                        .asin(1.5707963267948966), ((Double) asinFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testatan() {
        try {
            FilterFunction_atan atan = (FilterFunction_atan) ff.function("atan", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "atan", atan.getName());
            assertEquals("Number of arguments, ", 1, atan.getArgCount());

            Function atanFunction = ff.function("atan", literal_1);
            double good0 = Math.atan(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("atan of (1.0):", Double.isNaN(((Double) atanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("atan of (1.0):", (double) Math.atan(1.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            atanFunction = ff.function("atan", literal_m1);
            double good1 = Math.atan(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("atan of (-1.0):", Double.isNaN(((Double) atanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("atan of (-1.0):", (double) Math.atan(-1.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            atanFunction = ff.function("atan", literal_2);
            double good2 = Math.atan(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("atan of (2.0):", Double.isNaN(((Double) atanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("atan of (2.0):", (double) Math.atan(2.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            atanFunction = ff.function("atan", literal_m2);
            double good3 = Math.atan(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("atan of (-2.0):", Double.isNaN(((Double) atanFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("atan of (-2.0):", (double) Math.atan(-2.0),
                        ((Double) atanFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            atanFunction = ff.function("atan", literal_pi);
            double good4 = Math.atan(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("atan of (3.141592653589793):", Double
                        .isNaN(((Double) atanFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan of (3.141592653589793):", (double) Math
                        .atan(3.141592653589793), ((Double) atanFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            atanFunction = ff.function("atan", literal_05pi);
            double good5 = Math.atan(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("atan of (1.5707963267948966):", Double
                        .isNaN(((Double) atanFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("atan of (1.5707963267948966):", (double) Math
                        .atan(1.5707963267948966), ((Double) atanFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testceil() {
        try {
            FilterFunction_ceil ceil = (FilterFunction_ceil) ff.function("ceil", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "ceil", ceil.getName());
            assertEquals("Number of arguments, ", 1, ceil.getArgCount());

            Function ceilFunction = ff.function("ceil", literal_1);
            double good0 = Math.ceil(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("ceil of (1.0):", Double.isNaN(((Double) ceilFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("ceil of (1.0):", (double) Math.ceil(1.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            ceilFunction = ff.function("ceil", literal_m1);
            double good1 = Math.ceil(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("ceil of (-1.0):", Double.isNaN(((Double) ceilFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("ceil of (-1.0):", (double) Math.ceil(-1.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            ceilFunction = ff.function("ceil", literal_2);
            double good2 = Math.ceil(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("ceil of (2.0):", Double.isNaN(((Double) ceilFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("ceil of (2.0):", (double) Math.ceil(2.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            ceilFunction = ff.function("ceil", literal_m2);
            double good3 = Math.ceil(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("ceil of (-2.0):", Double.isNaN(((Double) ceilFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("ceil of (-2.0):", (double) Math.ceil(-2.0),
                        ((Double) ceilFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            ceilFunction = ff.function("ceil", literal_pi);
            double good4 = Math.ceil(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("ceil of (3.141592653589793):", Double
                        .isNaN(((Double) ceilFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("ceil of (3.141592653589793):", (double) Math
                        .ceil(3.141592653589793), ((Double) ceilFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            ceilFunction = ff.function("ceil", literal_05pi);
            double good5 = Math.ceil(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("ceil of (1.5707963267948966):", Double
                        .isNaN(((Double) ceilFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("ceil of (1.5707963267948966):", (double) Math
                        .ceil(1.5707963267948966), ((Double) ceilFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testexp() {
        try {
            FilterFunction_exp exp = (FilterFunction_exp) ff.function("exp", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "exp", exp.getName());
            assertEquals("Number of arguments, ", 1, exp.getArgCount());

            Function expFunction = ff.function("exp", literal_1);
            double good0 = Math.exp(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("exp of (1.0):", Double.isNaN(((Double) expFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("exp of (1.0):", (double) Math.exp(1.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            expFunction = ff.function("exp", literal_m1);
            double good1 = Math.exp(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("exp of (-1.0):", Double.isNaN(((Double) expFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("exp of (-1.0):", (double) Math.exp(-1.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            expFunction = ff.function("exp", literal_2);
            double good2 = Math.exp(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("exp of (2.0):", Double.isNaN(((Double) expFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("exp of (2.0):", (double) Math.exp(2.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            expFunction = ff.function("exp", literal_m2);
            double good3 = Math.exp(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("exp of (-2.0):", Double.isNaN(((Double) expFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("exp of (-2.0):", (double) Math.exp(-2.0),
                        ((Double) expFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            expFunction = ff.function("exp", literal_pi);
            double good4 = Math.exp(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("exp of (3.141592653589793):", Double
                        .isNaN(((Double) expFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("exp of (3.141592653589793):", (double) Math
                        .exp(3.141592653589793), ((Double) expFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            expFunction = ff.function("exp", literal_05pi);
            double good5 = Math.exp(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("exp of (1.5707963267948966):", Double
                        .isNaN(((Double) expFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("exp of (1.5707963267948966):", (double) Math
                        .exp(1.5707963267948966), ((Double) expFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testfloor() {
        try {
            FilterFunction_floor floor = (FilterFunction_floor) ff.function("floor", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "floor", floor.getName());
            assertEquals("Number of arguments, ", 1, floor.getArgCount());

            Function floorFunction = ff.function("floor", literal_1);
            double good0 = Math.floor(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("floor of (1.0):", Double.isNaN(((Double) floorFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("floor of (1.0):", (double) Math.floor(1.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            floorFunction = ff.function("floor", literal_m1);
            double good1 = Math.floor(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("floor of (-1.0):", Double.isNaN(((Double) floorFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("floor of (-1.0):", (double) Math.floor(-1.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            floorFunction = ff.function("floor", literal_2);
            double good2 = Math.floor(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("floor of (2.0):", Double.isNaN(((Double) floorFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("floor of (2.0):", (double) Math.floor(2.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            floorFunction = ff.function("floor", literal_m2);
            double good3 = Math.floor(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("floor of (-2.0):", Double.isNaN(((Double) floorFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("floor of (-2.0):", (double) Math.floor(-2.0),
                        ((Double) floorFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            floorFunction = ff.function("floor", literal_pi);
            double good4 = Math.floor(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("floor of (3.141592653589793):", Double
                        .isNaN(((Double) floorFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("floor of (3.141592653589793):", (double) Math
                        .floor(3.141592653589793), ((Double) floorFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            floorFunction = ff.function("floor", literal_05pi);
            double good5 = Math.floor(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("floor of (1.5707963267948966):", Double
                        .isNaN(((Double) floorFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("floor of (1.5707963267948966):", (double) Math
                        .floor(1.5707963267948966), ((Double) floorFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testlog() {
        try {
            FilterFunction_log log = (FilterFunction_log) ff.function("log", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "log", log.getName());
            assertEquals("Number of arguments, ", 1, log.getArgCount());

            Function logFunction = ff.function("log", literal_1);
            double good0 = Math.log(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("log of (1.0):", Double.isNaN(((Double) logFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("log of (1.0):", (double) Math.log(1.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            logFunction = ff.function("log", literal_m1);
            double good1 = Math.log(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("log of (-1.0):", Double.isNaN(((Double) logFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("log of (-1.0):", (double) Math.log(-1.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            logFunction = ff.function("log", literal_2);
            double good2 = Math.log(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("log of (2.0):", Double.isNaN(((Double) logFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("log of (2.0):", (double) Math.log(2.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            logFunction = ff.function("log", literal_m2);
            double good3 = Math.log(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("log of (-2.0):", Double.isNaN(((Double) logFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("log of (-2.0):", (double) Math.log(-2.0),
                        ((Double) logFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            logFunction = ff.function("log", literal_pi);
            double good4 = Math.log(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("log of (3.141592653589793):", Double
                        .isNaN(((Double) logFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("log of (3.141592653589793):", (double) Math
                        .log(3.141592653589793), ((Double) logFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            logFunction = ff.function("log", literal_05pi);
            double good5 = Math.log(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("log of (1.5707963267948966):", Double
                        .isNaN(((Double) logFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("log of (1.5707963267948966):", (double) Math
                        .log(1.5707963267948966), ((Double) logFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testrandom() {
        try {

            FilterFunction_random randomFunction = (FilterFunction_random) ff
                    .function("random", new org.opengis.filter.expression.Expression[0]);
            assertEquals("Name is, ", "random", randomFunction.getName());
            assertEquals("Number of arguments, ", 0, randomFunction
                    .getArgCount());
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testrint() {
        try {
            FilterFunction_rint rint = (FilterFunction_rint) ff.function("rint", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "rint", rint.getName());
            assertEquals("Number of arguments, ", 1, rint.getArgCount());

            Function rintFunction = ff.function("rint", literal_1);
            double good0 = Math.rint(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("rint of (1.0):", Double.isNaN(((Double) rintFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("rint of (1.0):", (double) Math.rint(1.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            rintFunction = ff.function("rint", literal_m1);
            double good1 = Math.rint(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("rint of (-1.0):", Double.isNaN(((Double) rintFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("rint of (-1.0):", (double) Math.rint(-1.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            rintFunction = ff.function("rint", literal_2);
            double good2 = Math.rint(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("rint of (2.0):", Double.isNaN(((Double) rintFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("rint of (2.0):", (double) Math.rint(2.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            rintFunction = ff.function("rint", literal_m2);
            double good3 = Math.rint(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("rint of (-2.0):", Double.isNaN(((Double) rintFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("rint of (-2.0):", (double) Math.rint(-2.0),
                        ((Double) rintFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            rintFunction = ff.function("rint", literal_pi);
            double good4 = Math.rint(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("rint of (3.141592653589793):", Double
                        .isNaN(((Double) rintFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("rint of (3.141592653589793):", (double) Math
                        .rint(3.141592653589793), ((Double) rintFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            rintFunction = ff.function("rint", literal_05pi);
            double good5 = Math.rint(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("rint of (1.5707963267948966):", Double
                        .isNaN(((Double) rintFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("rint of (1.5707963267948966):", (double) Math
                        .rint(1.5707963267948966), ((Double) rintFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testround() {
        try {
            FilterFunction_round round = (FilterFunction_round) ff.function("round", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "round", round.getName());
            assertEquals("Number of arguments, ", 1, round.getArgCount());

            Function roundFunction = ff.function("round", literal_1);
            assertEquals("round of (1.0):", (int) Math.round(1.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(), 0.00001);
            
            roundFunction = ff.function("round", literal_m1);
            assertEquals("round of (-1.0):", (int) Math.round(-1.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(), 0.00001);
            
            roundFunction = ff.function("round", literal_2);
            assertEquals("round of (2.0):", (int) Math.round(2.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(), 0.00001);
            
            roundFunction = ff.function("round", literal_m2);
            assertEquals("round of (-2.0):", (int) Math.round(-2.0),
                    ((Integer) roundFunction.evaluate(null)).intValue(), 0.00001);
            
            roundFunction = ff.function("round", literal_pi);
            assertEquals("round of (3.141592653589793):", (int) Math
                    .round(3.141592653589793), ((Integer) roundFunction
                    .evaluate(null)).intValue(), 0.00001);
            
            roundFunction = ff.function("round", literal_05pi);
            assertEquals("round of (1.5707963267948966):", (int) Math
                    .round(1.5707963267948966), ((Integer) roundFunction
                    .evaluate(null)).intValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testround_2() {
        try {
            FilterFunction_round_2 round_2 = (FilterFunction_round_2) ff.function("round_2", org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "round_2", round_2.getName());
            assertEquals("Number of arguments, ", 1, round_2.getArgCount());

            Function round_2Function = ff.function("round_2", literal_1);
            assertEquals("round_2 of (1.0):", (long) Math.round(1.0),
                    ((Long) round_2Function.evaluate(null)).longValue(), 0.00001);
            
            round_2Function = ff.function("round_2", literal_m1);
            assertEquals("round_2 of (-1.0):", (long) Math.round(-1.0),
                    ((Long) round_2Function.evaluate(null)).longValue(), 0.00001);
            
            round_2Function = ff.function("round_2", literal_2);
            assertEquals("round_2 of (2.0):", (long) Math.round(2.0),
                    ((Long) round_2Function.evaluate(null)).longValue(), 0.00001);
            
            round_2Function = ff.function("round_2", literal_m2);
            assertEquals("round_2 of (-2.0):", (long) Math.round(-2.0),
                    ((Long) round_2Function.evaluate(null)).longValue(), 0.00001);
            
            round_2Function = ff.function("round_2", literal_pi);
            assertEquals("round_2 of (3.141592653589793):", (long) Math
                    .round(3.141592653589793), ((Long) round_2Function
                    .evaluate(null)).longValue(), 0.00001);
            
            round_2Function = ff.function("round_2", literal_05pi);
            assertEquals("round_2 of (1.5707963267948966):", (long) Math
                    .round(1.5707963267948966), ((Long) round_2Function
                    .evaluate(null)).longValue(), 0.00001);
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testtoDegrees() {
        try {
            FilterFunction_toDegrees toDegrees = (FilterFunction_toDegrees) ff.function("toDegrees", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "toDegrees", toDegrees.getName());
            assertEquals("Number of arguments, ", 1, toDegrees.getArgCount());

            Function toDegreesFunction = ff.function("toDegrees", literal_1);
            double good0 = Math.toDegrees(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("toDegrees of (1.0):", Double.isNaN(((Double) toDegreesFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toDegrees of (1.0):", (double) Math.toDegrees(1.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toDegreesFunction = ff.function("toDegrees", literal_m1);
            double good1 = Math.toDegrees(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("toDegrees of (-1.0):", Double.isNaN(((Double) toDegreesFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toDegrees of (-1.0):", (double) Math.toDegrees(-1.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toDegreesFunction = ff.function("toDegrees", literal_2);
            double good2 = Math.toDegrees(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("toDegrees of (2.0):", Double.isNaN(((Double) toDegreesFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toDegrees of (2.0):", (double) Math.toDegrees(2.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toDegreesFunction = ff.function("toDegrees", literal_m2);
            double good3 = Math.toDegrees(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("toDegrees of (-2.0):", Double.isNaN(((Double) toDegreesFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toDegrees of (-2.0):", (double) Math.toDegrees(-2.0),
                        ((Double) toDegreesFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toDegreesFunction = ff.function("toDegrees", literal_pi);
            double good4 = Math.toDegrees(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("toDegrees of (3.141592653589793):", Double
                        .isNaN(((Double) toDegreesFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("toDegrees of (3.141592653589793):", (double) Math
                        .toDegrees(3.141592653589793), ((Double) toDegreesFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            toDegreesFunction = ff.function("toDegrees", literal_05pi);
            double good5 = Math.toDegrees(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("toDegrees of (1.5707963267948966):", Double
                        .isNaN(((Double) toDegreesFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("toDegrees of (1.5707963267948966):", (double) Math
                        .toDegrees(1.5707963267948966), ((Double) toDegreesFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testtoRadians() {
        try {
            FilterFunction_toRadians toRadians = (FilterFunction_toRadians) ff.function("toRadians", 
                    org.opengis.filter.expression.Expression.NIL);
            assertEquals("Name is, ", "toRadians", toRadians.getName());
            assertEquals("Number of arguments, ", 1, toRadians.getArgCount());

            Function toRadiansFunction = ff.function("toRadians", literal_1);
            double good0 = Math.toRadians(1.0);
            if (Double.isNaN(good0)) {
                assertTrue("toRadians of (1.0):", Double.isNaN(((Double) toRadiansFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toRadians of (1.0):", (double) Math.toRadians(1.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toRadiansFunction = ff.function("toRadians", literal_m1);
            double good1 = Math.toRadians(-1.0);
            if (Double.isNaN(good1)) {
                assertTrue("toRadians of (-1.0):", Double.isNaN(((Double) toRadiansFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toRadians of (-1.0):", (double) Math.toRadians(-1.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toRadiansFunction = ff.function("toRadians", literal_2);
            double good2 = Math.toRadians(2.0);
            if (Double.isNaN(good2)) {
                assertTrue("toRadians of (2.0):", Double.isNaN(((Double) toRadiansFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toRadians of (2.0):", (double) Math.toRadians(2.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toRadiansFunction = ff.function("toRadians", literal_m2);
            double good3 = Math.toRadians(-2.0);
            if (Double.isNaN(good3)) {
                assertTrue("toRadians of (-2.0):", Double.isNaN(((Double) toRadiansFunction
                        .evaluate(null)).doubleValue()));
            } else {
                assertEquals("toRadians of (-2.0):", (double) Math.toRadians(-2.0),
                        ((Double) toRadiansFunction.evaluate(null)).doubleValue(),
                        0.00001);
            }
            
            toRadiansFunction = ff.function("toRadians", literal_pi);
            double good4 = Math.toRadians(Math.PI);
            if (Double.isNaN(good4)) {
                assertTrue("toRadians of (3.141592653589793):", Double
                        .isNaN(((Double) toRadiansFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("toRadians of (3.141592653589793):", (double) Math
                        .toRadians(3.141592653589793), ((Double) toRadiansFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
            
            toRadiansFunction = ff.function("toRadians", literal_05pi);
            double good5 = Math.toRadians(1.5707963267948966);
            if (Double.isNaN(good5)) {
                assertTrue("toRadians of (1.5707963267948966):", Double
                        .isNaN(((Double) toRadiansFunction.evaluate(null))
                                .doubleValue()));
            } else {
                assertEquals("toRadians of (1.5707963267948966):", (double) Math
                        .toRadians(1.5707963267948966), ((Double) toRadiansFunction
                        .evaluate(null)).doubleValue(), 0.00001);
            }
        } catch (FactoryRegistryException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testToLowerCase() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function f = ff.function("strToLowerCase", ff.literal("UPCASE"));
        assertEquals("upcase", f.evaluate(null));
    }
    
    public void testToUpperCase() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function f = ff.function("strToUpperCase", ff.literal("lowcase"));
        assertEquals("LOWCASE", f.evaluate(null));
    }
}
