/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Test;
import org.opengis.filter.expression.Function;

public class FilterFunction_equalToTest {
    @Test
    public void testNoMatchActionStrings() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func = ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        assertTrue((Boolean) func.evaluate(new Object()));

        func = ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
        assertFalse((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testNoMatchActionNumbers() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func = ff.function("equalTo", ff.literal(123), ff.literal(123));
        assertTrue((Boolean) func.evaluate(new Object()));

        func = ff.function("equalTo", ff.literal(123), ff.literal(123.0));
        assertTrue((Boolean) func.evaluate(new Object()));

        func = ff.function("equalTo", ff.literal(123), ff.literal(124));
        assertFalse((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testAnyMatch() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8)),
                        ff.literal(Arrays.asList(1, 2, 3)),
                        ff.literal("ANY"));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testAnyMatchArrays() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8}),
                        ff.literal(new Integer[] {1, 2, 3}),
                        ff.literal("ANY"));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testAnyMatchPrimitiveArrays() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(new int[] {1, 2, 3, 4, 5, 6, 7, 8}),
                        ff.literal(new int[] {1, 2, 3}),
                        ff.literal("ANY"));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testAnyDoesNotMatch() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(Arrays.asList(4, 5, 6, 7, 8)),
                        ff.literal(Arrays.asList(1, 2, 3)),
                        ff.literal("ANY"));
        assertFalse((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testAllMatch() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(Arrays.asList(1, 1, 1)),
                        ff.literal(Arrays.asList(1)),
                        ff.literal("ALL"));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testAllDoesNotMatch() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(Arrays.asList(1, 1, 3)),
                        ff.literal(Arrays.asList(1)),
                        ff.literal("ALL"));
        assertFalse((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testOneMatch() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(Arrays.asList(1, 2, 3)),
                        ff.literal(Arrays.asList(1, 4, 5)),
                        ff.literal("ONE"));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testOneDoesNotMatch() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "equalTo",
                        ff.literal(Arrays.asList(1, 2, 3)),
                        ff.literal(Arrays.asList(1, 2)),
                        ff.literal("ONE"));
        assertFalse((Boolean) func.evaluate(new Object()));
    }
}
