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

import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;

public class IsNotEqualToImpltest {

    org.geotools.api.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testOperandsSameType() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal(2);

        PropertyIsNotEqualTo notEqual = filterFactory.notEqual(e1, e2, true);
        Assert.assertTrue(notEqual.evaluate(null));
    }

    @Test
    public void testOperandsDifferentType() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal("2");

        PropertyIsNotEqualTo notEqual = filterFactory.notEqual(e1, e2, true);
        Assert.assertTrue(notEqual.evaluate(null));
    }

    @Test
    public void testOperandsIntDouble() {
        Expression e1 = filterFactory.literal(1);
        Expression e2 = filterFactory.literal("1.0");

        PropertyIsNotEqualTo notEqual = filterFactory.notEqual(e1, e2, true);
        Assert.assertFalse(notEqual.evaluate(null));
    }

    @Test
    public void testCaseSensitivity() {
        Expression e1 = filterFactory.literal("foo");
        Expression e2 = filterFactory.literal("FoO");

        PropertyIsNotEqualTo caseSensitive = filterFactory.notEqual(e1, e2, true);
        Assert.assertTrue(caseSensitive.evaluate(null));

        PropertyIsNotEqualTo caseInsensitive = filterFactory.notEqual(e1, e2, false);
        Assert.assertFalse(caseInsensitive.evaluate(null));
    }
}
