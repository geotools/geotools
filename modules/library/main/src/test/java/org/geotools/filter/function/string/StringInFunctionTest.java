/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;

public class StringInFunctionTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void test() throws Exception {
        StringInFunction f = new StringInFunction();

        List<Expression> params =
                Arrays.asList(
                        ff.literal("foo"),
                        ff.literal(true),
                        ff.literal("foo"),
                        ff.literal("bar"),
                        ff.literal("baz"));
        f.setParameters(params);

        Assert.assertEquals(Boolean.TRUE, f.evaluate(null));

        params =
                Arrays.asList(
                        ff.literal("foo"),
                        ff.literal(true),
                        ff.literal("FOO"),
                        ff.literal("bar"),
                        ff.literal("baz"));
        f.setParameters(params);
        Assert.assertEquals(Boolean.FALSE, f.evaluate(null));
    }

    @Test
    public void testTooFewArguments() throws Exception {
        StringInFunction f = new StringInFunction();

        List<Expression> params = Arrays.asList(ff.literal("foo"), ff.literal(true));
        f.setParameters(params);

        try {
            f.evaluate(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
