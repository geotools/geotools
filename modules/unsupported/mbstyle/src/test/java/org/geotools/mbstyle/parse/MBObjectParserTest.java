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
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Individual parsing tests.
 */
public class MBObjectParserTest {

    private MBObjectParser parser = new MBObjectParser(this.getClass());

    @Test
    public void testNumberFromFunction() throws ParseException {
        String jsonStr = "{'expr': {'type':'exponential', 'base': 1.9, 'property':'temperature', "
            + "'stops':[[0,12],[30,24],[70,48]]}}";
        JSONObject object = MapboxTestUtils.object(jsonStr);
        Expression numericExpression = parser.number(object, "expr");
        //only care that a function was returned. other unit tests should be responsible for checking
        //the actual parse results
        assertTrue("Parse result must be a function",
            Function.class.isAssignableFrom(numericExpression.getClass()));
    }

    @Test
    public void testPercentageFromFunction() throws ParseException {
        String jsonStr = "{'expr': {'type':'exponential', 'base': 1.9, 'property':'temperature', "
            + "'stops':[[0,12],[30,24],[70,48]]}}";
        JSONObject object = MapboxTestUtils.object(jsonStr);
        Expression numericExpression = parser.percentage(object, "expr");
        //only care that a function was returned. other unit tests should be responsible for checking
        //the actual parse results
        assertTrue("Parse result must be a function",
            Function.class.isAssignableFrom(numericExpression.getClass()));
    }

    @Test
    public void testColorFromFunction() throws ParseException {
        JSONObject object = MapboxTestUtils.object("{'expr': {'property':'color','type':'identity'}}");
        Expression expression = parser.color(object, "expr", Color.black);
        assertTrue("Parse result must be a function",
            Function.class.isAssignableFrom(expression.getClass()));
    }

    @Test
    public void testStringFromFunction() throws ParseException {
        JSONObject object = MapboxTestUtils.object("{'expr': {'base': 1, 'stops': [[0, '{name-en}']]}}");
        Expression expression = parser.string(object, "expr", "name");
        assertTrue("Parse result must be a function",
            Function.class.isAssignableFrom(expression.getClass()));
    }

    @Test
    public void testBoolFromFunction() throws ParseException {
        JSONObject object = MapboxTestUtils.object("{'expr': { 'base': 1, 'stops': [ [ 0, true ], [ 10, true ], [ 22, true ] ] }}");
        Expression expression = parser.bool(object, "expr", true);
        assertTrue("Parse result must be a function",
            Function.class.isAssignableFrom(expression.getClass()));
    }

}