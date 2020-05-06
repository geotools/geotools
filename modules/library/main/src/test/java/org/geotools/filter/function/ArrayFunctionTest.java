/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertTrue;

import org.geotools.filter.FilterFactoryImpl;
import org.junit.Test;
import org.opengis.filter.expression.Function;

public class ArrayFunctionTest {
    @Test
    public void testArrayOfStrings() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func = ff.function("array", ff.literal("string1"), ff.literal("string2"));
        assertTrue(func.evaluate(new Object(), String[].class) instanceof String[]);
    }

    @Test
    public void testArrayOfIntegers() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function func = ff.function("array", ff.literal(1), ff.literal(2));
        assertTrue(func.evaluate(new Object(), Integer[].class) instanceof Integer[]);
    }
}
