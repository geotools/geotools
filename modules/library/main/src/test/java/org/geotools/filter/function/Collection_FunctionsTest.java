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
 *
 *    Created on May 11, 2005, 9:21 PM
 */
package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.feature.DefaultFeatureCollection;
import org.junit.Test;

/**
 * Do aggregate functions actually work?
 *
 * @author Cory Horner, Refractions Research
 */
public class Collection_FunctionsTest extends FunctionTestSupport {
    @Test
    public void testInstance() {
        Function cmin = ff.function("Collection_Min", ff.literal(new DefaultFeatureCollection()));
        assertNotNull(cmin);
    }

    @Test
    public void testAverage() throws Exception {
        performNumberTest("Collection_Average", Double.valueOf(33.375));
    }

    @Test
    public void testCount() throws Exception {
        performNumberTest("Collection_Count", Integer.valueOf(8));
    }

    @Test
    public void testMin() throws Exception {
        performNumberTest("Collection_Min", Integer.valueOf(4));
    }

    @Test
    public void testMedian() throws Exception {
        performNumberTest("Collection_Median", Double.valueOf(24.5));
    }

    @Test
    public void testMax() throws Exception {
        performNumberTest("Collection_Max", Integer.valueOf(90));
    }

    @Test
    public void testSum() throws Exception {
        performNumberTest("Collection_Sum", Integer.valueOf(267));
    }

    @Test
    public void testUnique() throws Exception {
        HashSet<Integer> result = new HashSet<>(8);
        result.add(Integer.valueOf(90));
        result.add(Integer.valueOf(4));
        result.add(Integer.valueOf(8));
        result.add(Integer.valueOf(43));
        result.add(Integer.valueOf(61));
        result.add(Integer.valueOf(20));
        result.add(Integer.valueOf(29));
        result.add(Integer.valueOf(12));
        performObjectTest("Collection_Unique", result);
    }

    @Test
    public void testNearest() throws Exception {
        Function func = ff.function("Collection_Nearest", ff.property("foo"), ff.literal(9));
        Object match = func.evaluate(featureCollection);
        assertEquals("Nearest to 9 is 8", 8, match);
    }

    @Test
    public void testCountFunctionDescription() throws Exception {
        // Create instance of function to get hold of the filter capabilities
        PropertyName exp = ff.property("foo");
        Function func = ff.function("Collection_Count", exp);

        // Expecting one function parameter
        assertEquals(func.getParameters().size(), 1);

        // Test return parameter
        assertEquals(func.getFunctionName().getReturn().toString(), "count:Number");
    }

    /**
     * Tests a function class of org.geotools.filter.function.Collection_*Function
     *
     * <p>Example: performTest("Collection_Min", 4);
     */
    public void performNumberTest(String functionName, Object expectedValue) throws Exception {
        PropertyName exp = ff.property("foo");
        Function func = ff.function(functionName, exp);
        Object obj = func.evaluate(featureCollection);
        Number result = (Number) obj;
        Number expected = (Number) expectedValue;
        assertEquals(expected.doubleValue(), result.doubleValue(), 0);
    }

    public void performObjectTest(String functionName, Object expectedValue) throws Exception {
        PropertyName exp = ff.property("foo");
        Function func = ff.function(functionName, exp);
        Object result = func.evaluate(featureCollection);
        assertEquals(expectedValue, result);
    }
}
