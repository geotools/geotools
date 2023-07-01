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
package org.geotools.filter.expression;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddImplTest {

    AddImpl add;

    @Before
    public void setUp() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Expression e1 = ff.literal(1);
        Expression e2 = ff.literal(2);

        add = new AddImpl(e1, e2);
    }

    @Test
    public void testEvaluate() {
        Object result = add.evaluate(null);
        Assert.assertEquals(Double.valueOf(3), result);
    }

    @Test
    public void testEvaluateAsInteger() {
        Object result = add.evaluate(null, Integer.class);
        Assert.assertEquals(Integer.valueOf(3), result);
    }

    @Test
    public void testEvaluateAsString() {
        Object result = add.evaluate(null, String.class);
        Assert.assertEquals("3.0", result);
    }
}
