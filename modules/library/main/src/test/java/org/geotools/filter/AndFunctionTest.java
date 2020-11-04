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
package org.geotools.filter;

import junit.framework.TestCase;
import org.junit.Test;
import org.opengis.filter.expression.Function;

/**
 * Unit test for AndFunction
 *
 * @author Erwan Bocher, CNRS, 2020
 */
public class AndFunctionTest extends TestCase {

    @Test
    public void testAndFunction() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function equalsTo = ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function andFunction = ff.function("and", equalsTo, equalsTo);
        assertFalse((Boolean) andFunction.evaluate(new Object()));
    }
}
