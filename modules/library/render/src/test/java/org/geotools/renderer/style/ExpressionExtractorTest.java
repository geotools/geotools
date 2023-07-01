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
package org.geotools.renderer.style;

import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionExtractorTest {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testLiteral() {
        final String exp = "I'm a plain string";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(ff.literal("I'm a plain string"), result.get(0));
    }

    @Test
    public void testLiteralEscapes() {
        final String exp = "I'm a plain string \\\\ \\${bla bla\\}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(ff.literal("I'm a plain string \\ ${bla bla}"), result.get(0));
    }

    @Test
    public void testSimpleExpressions() {
        final String exp = "I'm a ${name} expression of type ${type}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(ff.literal("I'm a "), result.get(0));
        Assert.assertEquals(ff.property("name"), result.get(1));
        Assert.assertEquals(ff.literal(" expression of type "), result.get(2));
        Assert.assertEquals(ff.property("type"), result.get(3));
    }

    @Test
    public void testDoubleOpen() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla ${bla}");
            Assert.fail("Double cql exp open, should have failed");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testOpenLingering() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla");
            Assert.fail("Double cql exp open, should have failed");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testDoubleClose() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla}}");
            Assert.fail("Double cql exp open, should have failed");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testEscapesInside() {
        final String exp = "${strLength('\\\\\\${\\}')}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(ff.function("strLength", ff.literal("\\${}")), result.get(0));
    }

    @Test
    public void testTwoExpressions() {
        final String exp = "${name}${age}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(ff.property("name"), result.get(0));
        Assert.assertEquals(ff.property("age"), result.get(1));
    }

    @Test
    public void testCatenateOne() {
        Expression l = ff.literal("http://test?param=");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l));
        Assert.assertEquals(l, cat);
    }

    @Test
    public void testCatenateTwo() {
        Literal l = ff.literal("http://test?param=");
        PropertyName pn = ff.property("intAttribute");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l, pn));
        Assert.assertTrue(cat instanceof Function);
        Function f = (Function) cat;
        Assert.assertEquals("Concatenate", f.getName());
        Assert.assertEquals(l, f.getParameters().get(0));
        Assert.assertEquals(pn, f.getParameters().get(1));
    }

    @Test
    public void testCatenateThree() {
        Literal l1 = ff.literal("http://test?param=");
        PropertyName pn = ff.property("intAttribute");
        Literal l2 = ff.literal("&param2=foo");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l1, pn, l2));
        Assert.assertTrue(cat instanceof Function);
        Function f = (Function) cat;
        Assert.assertEquals("Concatenate", f.getName());
        Assert.assertEquals(l1, f.getParameters().get(0));
        Assert.assertEquals(pn, f.getParameters().get(1));
        Assert.assertEquals(l2, f.getParameters().get(2));
    }
}
