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
import org.geotools.api.filter.expression.Function;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Test;

public class LinFunctionTest {
    @Test
    public void testMatchIntegers() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "lin", ff.literal(9), ff.literal(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testMatchStrings() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function("lin", ff.literal("b"), ff.literal(Arrays.asList("a", "b", "c", "d")));
        assertTrue((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testDoesNotMatchIntegers() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function(
                        "lin",
                        ff.literal(10),
                        ff.literal(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        assertFalse((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testDoesNotMatchStrings() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func =
                ff.function("lin", ff.literal("g"), ff.literal(Arrays.asList("a", "b", "c", "d")));
        assertFalse((Boolean) func.evaluate(new Object()));
    }

    @Test
    public void testDoesNotMatchIfNotList() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func = ff.function("lin", ff.literal("a"), ff.literal("g"));
        assertFalse((Boolean) func.evaluate(new Object()));
    }
}
