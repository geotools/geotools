/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.awt.Color;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.layer.LineMBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/** Individual parsing tests. */
public class MBObjectParserTest {

    private MBObjectParser parser = new MBObjectParser(this.getClass());

    @Test
    public void optional() throws ParseException {
        String jsonStr = "{'name1': 'fred', 'name2' : 1, 'name3' : null, 'name4': { 'age': 1 }}";
        JSONObject object = MapboxTestUtils.object(jsonStr);

        // expected
        assertEquals("fred", parser.optional(String.class, object, "name1", null));

        // fallback
        assertNull(parser.optional(String.class, object, "name0", null));
        assertEquals("sample", parser.optional(String.class, object, "name0", "sample"));
        assertNull(parser.optional(String.class, object, "name3", null));
        assertEquals("sample", parser.optional(String.class, object, "name3", "sample"));

        // validation
        try {
            assertEquals("1", parser.optional(String.class, object, "name2", null));
        } catch (MBFormatException expected) {
            assertTrue(expected.toString().contains("name2"));
        }
        try {
            assertNull(parser.optional(String.class, object, "name4", null));
            fail("Optional name4 lookup was object, rather than string");
        } catch (MBFormatException expected) {
            assertTrue(expected.toString().contains("name4"));
        }
        try {
            assertEquals("sample", parser.optional(String.class, object, "name4", "sample"));
            fail("Optional name4 lookup was object, rather than string");
        } catch (MBFormatException expected) {
            assertTrue(expected.toString().contains("name4"));
        }
    }

    @Test
    public void testNumberFromFunction() throws ParseException {
        String jsonStr =
                "{'expr': {'type':'exponential', 'base': 1.9, 'property':'temperature', "
                        + "'stops':[[0,12],[30,24],[70,48]]}}";
        JSONObject object = MapboxTestUtils.object(jsonStr);
        Expression numericExpression = parser.number(object, "expr");
        // only care that a function was returned. other unit tests should be responsible for
        // checking
        // the actual parse results
        assertTrue(
                "Parse result must be a function",
                Function.class.isAssignableFrom(numericExpression.getClass()));
    }

    @Test
    public void testPercentageFromFunction() throws ParseException {
        String jsonStr =
                "{'expr': {'type':'exponential', 'base': 1.9, 'property':'temperature', "
                        + "'stops':[[0,12],[30,24],[70,48]]}}";
        JSONObject object = MapboxTestUtils.object(jsonStr);
        Expression numericExpression = parser.percentage(object, "expr");
        // only care that a function was returned. other unit tests should be responsible for
        // checking
        // the actual parse results
        assertTrue(
                "Parse result must be a function",
                Function.class.isAssignableFrom(numericExpression.getClass()));
    }

    @Test
    public void testColorFromFunction() throws ParseException {
        JSONObject object =
                MapboxTestUtils.object("{'expr': {'property':'color','type':'identity'}}");
        Expression expression = parser.color(object, "expr", Color.black);
        assertTrue(
                "Parse result must be a function",
                Function.class.isAssignableFrom(expression.getClass()));
    }

    @Test
    public void testStringFromFunction() throws ParseException {
        JSONObject object =
                MapboxTestUtils.object("{'expr': {'base': 1, 'stops': [[0, '{name-en}']]}}");
        Expression expression = parser.string(object, "expr", "name");
        assertTrue(
                "Parse result must be a function",
                Function.class.isAssignableFrom(expression.getClass()));
    }

    @Test
    public void testBoolFromFunction() throws ParseException {
        JSONObject object =
                MapboxTestUtils.object(
                        "{'expr': { 'base': 1, 'stops': [ [ 0, true ], [ 10, true ], [ 22, true ] ] }}");
        Expression expression = parser.bool(object, "expr", true);
        assertTrue(
                "Parse result must be a function",
                Function.class.isAssignableFrom(expression.getClass()));
    }

    /** Test parsing from a Mapbox enumeration property to a GeoTools expression. */
    @Test
    public void testEnumToExpression() throws ParseException {
        // Test for text-pitch-alignment
        JSONObject object =
                MapboxTestUtils.object("{'text-pitch-alignment': 'map'}"); // viewport, auto
        Expression expression =
                parser.enumToExpression(
                        object,
                        "text-pitch-alignment",
                        SymbolMBLayer.Alignment.class,
                        SymbolMBLayer.Alignment.AUTO);
        assertEquals("Enum correctly parsed", "map", expression.evaluate(null, String.class));

        // Test for line-cap
        object = MapboxTestUtils.object("{'line-cap': 'round'}");
        expression =
                parser.enumToExpression(
                        object, "line-cap", LineMBLayer.LineCap.class, LineMBLayer.LineCap.BUTT);
        assertEquals("round", expression.evaluate(null, String.class));

        // Test for line-join
        object = MapboxTestUtils.object("{'line-join': 'bevel'}");
        expression =
                parser.enumToExpression(
                        object,
                        "line-join",
                        LineMBLayer.LineJoin.class,
                        LineMBLayer.LineJoin.MITER);
        assertEquals("bevel", expression.evaluate(null, String.class));
    }

    /**
     * Test parsing from a Mapbox enumeration property to a GeoTools expression, when the JSON
     * property value is invalid for the enumeration (should fallback to default).
     */
    @Test
    public void testInvalidEnumProperty() throws ParseException {
        // Test for text-pitch-alignment
        JSONObject object = MapboxTestUtils.object("{'text-pitch-alignment': 'bad_value'}");
        Expression expression =
                parser.enumToExpression(
                        object,
                        "text-pitch-alignment",
                        SymbolMBLayer.Alignment.class,
                        SymbolMBLayer.Alignment.AUTO);
        assertEquals("auto", expression.evaluate(null, String.class));

        // Test for line-cap
        object = MapboxTestUtils.object("{'line-cap': 'bad_value'}");
        expression =
                parser.enumToExpression(
                        object, "line-cap", LineMBLayer.LineCap.class, LineMBLayer.LineCap.BUTT);
        assertEquals("butt", expression.evaluate(null, String.class));

        // Test for line-join
        object = MapboxTestUtils.object("{'line-join': 'bad_value'}");
        expression =
                parser.enumToExpression(
                        object,
                        "line-join",
                        LineMBLayer.LineJoin.class,
                        LineMBLayer.LineJoin.MITER);
        assertEquals("mitre", expression.evaluate(null, String.class));
    }
}
