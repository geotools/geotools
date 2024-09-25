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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

public class ExpressionExtractorTest {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testLiteral() {
        final String exp = "I'm a plain string";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(1, result.size());
        assertEquals(ff.literal("I'm a plain string"), result.get(0));
    }

    @Test
    public void testLiteralEscapes() {
        final String exp = "I'm a plain string \\\\ \\${bla bla\\}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(1, result.size());
        assertEquals(ff.literal("I'm a plain string \\ ${bla bla}"), result.get(0));
    }

    @Test
    public void testSimpleExpressions() {
        final String exp = "I'm a ${name} expression of type ${type}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(4, result.size());
        assertEquals(ff.literal("I'm a "), result.get(0));
        assertEquals(ff.property("name"), result.get(1));
        assertEquals(ff.literal(" expression of type "), result.get(2));
        assertEquals(ff.property("type"), result.get(3));
    }

    @Test
    public void testDoubleOpen() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla ${bla}");
            fail("Double cql exp open, should have failed");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testOpenLingering() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla");
            fail("Double cql exp open, should have failed");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testDoubleClose() {
        try {
            ExpressionExtractor.splitCqlExpressions("I'm a plain string ${bla}}");
            fail("Double cql exp open, should have failed");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testEscapesInside() {
        final String exp = "${strLength('\\\\\\${\\}')}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(1, result.size());
        assertEquals(ff.function("strLength", ff.literal("\\${}")), result.get(0));
    }

    @Test
    public void testTwoExpressions() {
        final String exp = "${name}${age}";
        List<Expression> result = ExpressionExtractor.splitCqlExpressions(exp);
        assertEquals(2, result.size());
        assertEquals(ff.property("name"), result.get(0));
        assertEquals(ff.property("age"), result.get(1));
    }

    @Test
    public void testCatenateOne() {
        Expression l = ff.literal("http://test?param=");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l));
        assertEquals(l, cat);
    }

    @Test
    public void testCatenateTwo() {
        Literal l = ff.literal("http://test?param=");
        PropertyName pn = ff.property("intAttribute");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l, pn));
        assertTrue(cat instanceof Function);
        Function f = (Function) cat;
        assertEquals("Concatenate", f.getName());
        assertEquals(l, f.getParameters().get(0));
        assertEquals(pn, f.getParameters().get(1));
    }

    @Test
    public void testCatenateThree() {
        Literal l1 = ff.literal("http://test?param=");
        PropertyName pn = ff.property("intAttribute");
        Literal l2 = ff.literal("&param2=foo");
        Expression cat = ExpressionExtractor.catenateExpressions(Arrays.asList(l1, pn, l2));
        assertTrue(cat instanceof Function);
        Function f = (Function) cat;
        assertEquals("Concatenate", f.getName());
        assertEquals(l1, f.getParameters().get(0));
        assertEquals(pn, f.getParameters().get(1));
        assertEquals(l2, f.getParameters().get(2));
    }

    @Test
    public void testCacheResults() throws Exception {
        final String exp = "I'm a ${name} expression of type ${type}";
        Expression ex1 = ExpressionExtractor.extractCqlExpressions(exp);
        Expression ex2 = ExpressionExtractor.extractCqlExpressions(exp);
        assertSame(ex1, ex2);
        ExpressionExtractor.cleanExpressionCache();
        Expression ex3 = ExpressionExtractor.extractCqlExpressions(exp);
        assertNotSame(ex1, ex3);
    }
}
