/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.PropertyName;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link InFunction}.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class InFunctionTest extends FunctionTestSupport {

    private static final String FUNCTION_NAME = "in";

    private SimpleFeature feature;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        feature = featureCollection.features().next();
    }

    @Test
    public void testIntPresent() {
        PropertyName exp = ff.property("foo");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal(3), ff.literal(4), ff.literal(5));
        Object result = func.evaluate(feature);
        assertEquals(true, result);
    }

    @Test
    public void testIntMissing() {
        PropertyName exp = ff.property("foo");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal(1), ff.literal(2));
        Object result = func.evaluate(feature);
        assertEquals(false, result);
    }

    @Test
    public void testDoublePresent() {
        PropertyName exp = ff.property("bar");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal(2.5), ff.literal(2.6), ff.literal(3.0));
        Object result = func.evaluate(feature);
        assertEquals(true, result);
    }

    @Test
    public void testDoubleMissing() {
        PropertyName exp = ff.property("bar");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal(2.499999), ff.literal("2.500001"), ff.literal(2));
        Object result = func.evaluate(feature);
        assertEquals(false, result);
    }

    @Test
    public void testStringPresent() {
        PropertyName exp = ff.property("group");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal("Group0"), ff.literal("Group1"));
        Object result = func.evaluate(feature);
        assertEquals(true, result);
    }

    @Test
    public void testStringMissing() {
        PropertyName exp = ff.property("group");
        Function func =
                ff.function(FUNCTION_NAME, exp, ff.literal("Group1"), ff.literal("Group2"), ff.literal("Group3"));
        Object result = func.evaluate(feature);
        assertEquals(false, result);
    }

    @Test
    public void testMixedType() {
        PropertyName exp = ff.property("group");
        Expression[] args = {exp, ff.literal(1), ff.literal(2.5), ff.literal("Group1"), ff.literal(4)};
        Function func = ff.function(FUNCTION_NAME, args);
        Object result = func.evaluate(feature);
        assertEquals(false, result);

        exp = ff.property("foo");
        args[0] = exp;
        func = ff.function(FUNCTION_NAME, args);
        result = func.evaluate(feature);
        assertEquals(true, result);

        exp = ff.property("bar");
        args[0] = exp;
        func = ff.function(FUNCTION_NAME, args);
        result = func.evaluate(feature);
        assertEquals(true, result);
    }

    @Test
    public void testLongList() {
        final int NUM_ARGS = 500;
        PropertyName exp = ff.property("group");

        Expression[] args = new Expression[NUM_ARGS + 1];
        args[0] = exp;
        for (int i = 1; i <= NUM_ARGS; i++) {
            args[i] = ff.literal("Group" + i);
        }
        Function func = ff.function(FUNCTION_NAME, args);

        Object result = func.evaluate(feature);
        assertEquals(false, result);

        feature.setAttribute("group", "Group" + NUM_ARGS / 2);

        result = func.evaluate(feature);
        assertEquals(true, result);
    }

    @Test
    public void testNullComparison() {
        PropertyName exp = ff.property("not_there");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal(1), ff.literal(null));
        Object result = func.evaluate(feature);
        assertEquals(true, result);

        func = ff.function(FUNCTION_NAME, exp, ff.literal("1"), ff.literal(2));
        result = func.evaluate(feature);
        assertEquals(false, result);
    }

    @Test
    public void testTypelessComparison() {
        PropertyName exp = ff.property("foo");
        Function func = ff.function(FUNCTION_NAME, exp, ff.literal("4"));
        Object result = func.evaluate(feature);
        assertEquals(true, result);

        func = ff.function(FUNCTION_NAME, exp, ff.literal(4.0));
        result = func.evaluate(feature);
        assertEquals(true, result);
    }
}
