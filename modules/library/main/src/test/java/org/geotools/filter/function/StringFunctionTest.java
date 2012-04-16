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
package org.geotools.filter.function;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * 
 *
 * @source $URL$
 */
public class StringFunctionTest  {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testStrReplace() {
        Literal foo = ff.literal("foo");
        Literal o = ff.literal("o");
        Literal bar = ff.literal("bar");
        
        Function f = ff.function("strReplace", new Expression[]{foo,o,bar,ff.literal(true)});
        String s = (String) f.evaluate(null,String.class);
        assertEquals( "fbarbar", s );
        
        f = ff.function("strReplace", new Expression[]{foo,o,bar,ff.literal(false)});
        s = (String) f.evaluate(null,String.class);
        assertEquals( "fbaro", s );
    }
    
    @Test
    public void testParseLong() {
        assertEquals(Long.MAX_VALUE , ff.function("parseLong", ff.literal(Long.MAX_VALUE + "")).evaluate(null));
        assertEquals(5l , ff.function("parseLong", ff.literal("5.0")).evaluate(null));
    }
    
    @Test
    public void testCapitalize() {
        assertEquals("United Kingdom", ff.function("strCapitalize", ff.literal("UNITED KINGDOM")).evaluate(null));
        assertEquals("United Kingdom", ff.function("strCapitalize", ff.literal("UnItEd kInGdOm")).evaluate(null));
    }
    
    @Test
    public void testNull() {
        assertEquals(null, ff.function("strCapitalize", ff.literal(null)).evaluate(null));
        assertEquals(null, ff.function("strToUpperCase", ff.literal(null)).evaluate(null));
        assertEquals(0, ff.function("strLength", ff.literal(null)).evaluate(null));
        assertEquals(false, ff.function("strMatches", ff.literal(null), ff.literal(null)).evaluate(null));
    }
    
    @Test
    public void testStrTrim2() throws Exception {
        assertEquals("hello", ff.function("strTrim2", ff.literal("  hello  "), ff.literal("both"), 
            ff.literal(" ")).evaluate(null));
        assertEquals("hello  ", ff.function("strTrim2", ff.literal("  hello  "), ff.literal("leading"), 
                ff.literal(" ")).evaluate(null));
        assertEquals("  hello", ff.function("strTrim2", ff.literal("  hello  "), ff.literal("trailing"), 
                ff.literal(" ")).evaluate(null));
        assertEquals("hello", ff.function("strTrim2", ff.literal("xxhelloxx"), ff.literal("both"), 
                ff.literal("x")).evaluate(null));
    }
    
    @Test
    public void testStrPosition() throws Exception {
        assertEquals("1", ff.function("strPosition", ff.literal("he"), ff.literal("hello"), 
                ff.literal("frontToBack")).evaluate(null, String.class));
        assertEquals("1", ff.function("strPosition", ff.literal("he"), ff.literal("hello"), 
                ff.literal("backToFront")).evaluate(null, String.class));
        assertEquals("0", ff.function("strPosition", ff.literal("x"), ff.literal("hello"), 
                ff.literal("backToFront")).evaluate(null, String.class));
        assertEquals("0", ff.function("strPosition", ff.literal("x"), ff.literal("hello"), 
                ff.literal("frontToBack")).evaluate(null, String.class));
        assertEquals("3", ff.function("strPosition", ff.literal("l"), ff.literal("hello"), 
                ff.literal("frontToBack")).evaluate(null, String.class));
        assertEquals("4", ff.function("strPosition", ff.literal("l"), ff.literal("hello"), 
                ff.literal("backToFront")).evaluate(null, String.class));
    }

    @Test
    public void testStrSubstring() throws Exception {
        // test bad ranges return null
        assertNull(ff.function("strSubstring", ff.literal("ABCD"), ff.literal(-1), ff.literal(2))
                .evaluate(null));
        assertNull(ff.function("strSubstring", ff.literal("ABCD"), ff.literal(2), ff.literal(5))
                .evaluate(null));
        assertNull(ff.function("strSubstring", ff.literal("ABCD"), ff.literal(3), ff.literal(2))
                .evaluate(null));
        // test empty string input
        assertEquals("", ff.function("strSubstring", ff.literal(""), ff.literal(0), ff.literal(0))
                .evaluate(null));
        // test empty string output
        assertEquals("",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(0), ff.literal(0))
                        .evaluate(null));
        assertEquals("",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(2), ff.literal(2))
                        .evaluate(null));
        assertEquals("",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(4), ff.literal(4))
                        .evaluate(null));
        // test non-empty substrings
        assertEquals("ABCD",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(0), ff.literal(4))
                        .evaluate(null));
        assertEquals("A",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(0), ff.literal(1))
                        .evaluate(null));
        assertEquals("AB",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(0), ff.literal(2))
                        .evaluate(null));
        assertEquals("BC",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(1), ff.literal(3))
                        .evaluate(null));
        assertEquals("CD",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(2), ff.literal(4))
                        .evaluate(null));
        assertEquals("D",
                ff.function("strSubstring", ff.literal("ABCD"), ff.literal(3), ff.literal(4))
                        .evaluate(null));
    }

    @Test
    public void testStrSubstringStart() throws Exception {
        // test bad index returns null
        assertNull(ff.function("strSubstringStart", ff.literal("ABCD"), ff.literal(-1)).evaluate(
                null));
        assertNull(ff.function("strSubstringStart", ff.literal("ABCD"), ff.literal(5)).evaluate(
                null));
        // test empty string input
        assertEquals("",
                ff.function("strSubstringStart", ff.literal(""), ff.literal(0)).evaluate(null));
        // test empty string output
        assertEquals("", ff.function("strSubstringStart", ff.literal("ABCD"), ff.literal(4))
                .evaluate(null));
        // test non-empty substrings
        assertEquals("ABCD", ff.function("strSubstringStart", ff.literal("ABCD"), ff.literal(0))
                .evaluate(null));
        assertEquals("CD", ff.function("strSubstringStart", ff.literal("ABCD"), ff.literal(2))
                .evaluate(null));
        assertEquals("D", ff.function("strSubstringStart", ff.literal("ABCD"), ff.literal(3))
                .evaluate(null));
    }

}
