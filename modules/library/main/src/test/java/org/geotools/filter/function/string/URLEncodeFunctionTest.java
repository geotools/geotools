/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function.string;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The URLEncodeFunction UnitTest
 *
 * @author Billy Newman
 */
public class URLEncodeFunctionTest {

    /** Filter factory */
    FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /** Test of getArgCount method, of class FilterFunction_strURLEncode. */
    @Test
    public void testArgCount() {
        URLEncodeFunction f = new URLEncodeFunction();
        assertEquals(-1, f.getFunctionName().getArgumentCount());
    }

    /** Test of getName method, of class FilterFunction_strURLEncode. */
    @Test
    public void testName() {
        URLEncodeFunction f = new URLEncodeFunction();
        assertEquals("strURLEncode", f.getName());
    }

    /** Test of evaluate method, of class URLEncodeFunction. */
    @Test
    public void testURLEncodeWithDefault() throws Exception {
        URLEncodeFunction f = new URLEncodeFunction();

        List<Expression> params = Arrays.asList(filterFactory.literal("Value With Spaces"));
        f.setParameters(params);

        assertEquals("Value%20With%20Spaces", f.evaluate(null));
    }

    /** Test of evaluate method, of class URLEncodeFunction. */
    @Test
    public void testURLEncode() throws Exception {
        URLEncodeFunction f = new URLEncodeFunction();

        List<Expression> params =
                Arrays.asList(
                        filterFactory.literal("Value With Spaces"), filterFactory.literal(false));
        f.setParameters(params);

        assertEquals("Value%20With%20Spaces", f.evaluate(null));
    }

    /** Test of evaluate method, of class URLEncodeFunction. */
    @Test
    public void testFormURLEncode() throws Exception {
        URLEncodeFunction f = new URLEncodeFunction();

        List<Expression> params =
                Arrays.asList(
                        filterFactory.literal("Value With Spaces"), filterFactory.literal(true));
        f.setParameters(params);

        assertEquals("Value+With+Spaces", f.evaluate(null));
    }
}
