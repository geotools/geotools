/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.mbstyle.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

public class MBTypeTest extends AbstractMBExpressionTest {

    @Override
    protected String getTestResourceName() {
        return "expressionParseTypesTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBTypes.class;
    }

    /** Verify that the "literal" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseLiteralExpression() {

        final JSONObject j = getObjectByLayerId("literalExpression", "paint");

        // test for literal JSONArray
        Object array = getExpressionEvaluation(j, "circle-radius");
        assertTrue(array instanceof JSONArray);
        JSONArray jarray = (JSONArray) array;
        assertEquals(4, jarray.size());
        assertEquals("string", jarray.get(1));

        // test for literal JSONObject
        Object obj = getExpressionEvaluation(j, "circle-color");
        assertTrue(obj instanceof JSONObject);
        JSONObject jobj = (JSONObject) obj;
        assertEquals(2, jobj.size());
        assertEquals(jobj.get("object"), "test");
    }

    /** Verify that the "typeof" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseTypeOfExpression() {

        final JSONObject j = getObjectByLayerId("typeOfExpression", "paint");

        Object number = getExpressionEvaluation(j, "number");
        assertEquals(Long.class.toString(), number);

        Object object = getExpressionEvaluation(j, "JSONObject");
        assertEquals(JSONObject.class.toString(), object);

        Object bool = getExpressionEvaluation(j, "boolean");
        assertEquals(Boolean.class.toString(), bool);

        Object arr = getExpressionEvaluation(j, "JSONArray");
        assertEquals(JSONArray.class.toString(), arr);

        Object string = getExpressionEvaluation(j, "string");
        assertEquals(String.class.toString(), string);
    }

    /** Verify that the "boolean" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseBooleanExpression() {

        final JSONObject j = getObjectByLayerId("booleanExpression", "paint");

        Object single = getExpressionEvaluation(j, "single");
        assertEquals(false, single);

        Object multi = getExpressionEvaluation(j, "multiple");
        assertEquals(true, multi);

        Object nested = getExpressionEvaluation(j, "nested");
        assertEquals(true, nested);

        try {
            getExpressionEvaluation(j, "no-bool");
            fail("expected exception function \"mbBoolean\" fails if not argument is a boolean");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Verify that the "number" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseNumberExpression() {

        final JSONObject j = getObjectByLayerId("numberExpression", "paint");

        Object dec = getExpressionEvaluation(j, "decimal");
        assertEquals(1.0, dec);

        Object ints = getExpressionEvaluation(j, "decimal");
        assertEquals(1.0, ints);

        Object multi = getExpressionEvaluation(j, "multiple");
        assertEquals(123L, multi);

        Object nested = getExpressionEvaluation(j, "nested");
        assertEquals(1234L, nested);

        try {
            getExpressionEvaluation(j, "no-num");
            fail("expected exception function \"mbNumber\" fails if no argument is a number");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Verify that the "object" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseObjectExpression() {

        final JSONObject j = getObjectByLayerId("objectExpression", "paint");

        Object single = getExpressionEvaluation(j, "single");
        assertEquals(JSONObject.class, single.getClass());

        Object multi = getExpressionEvaluation(j, "multiple");
        assertEquals(JSONObject.class, multi.getClass());

        Object nested = getExpressionEvaluation(j, "nested");
        assertEquals(JSONObject.class, nested.getClass());

        try {
            getExpressionEvaluation(j, "no-obj");
            fail("expected exception function \"mbObject\" fails if not argument is a boolean");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Verify that the "string" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseStringExpression() {

        final JSONObject j = getObjectByLayerId("stringExpression", "paint");

        Object single = getExpressionEvaluation(j, "single");
        assertEquals("singlestring", single);

        Object multi = getExpressionEvaluation(j, "multiple");
        assertEquals("multistring", multi);

        Object nested = getExpressionEvaluation(j, "nested");
        assertEquals("nestedstring", nested);

        try {
            getExpressionEvaluation(j, "no-string");
            fail("expected exception function \"mbObject\" fails if not argument is a boolean");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Verify that the "to-boolean" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseToBoolExpression() {

        final JSONObject j = getObjectByLayerId("toBooleanExpression", "paint");

        Object aNull = getExpressionEvaluation(j, "null");
        assertEquals(Boolean.FALSE, aNull);

        Object aZero = getExpressionEvaluation(j, "zero");
        assertEquals(Boolean.FALSE, aZero);

        Object aNumber = getExpressionEvaluation(j, "number");
        assertEquals(Boolean.TRUE, aNumber);

        Object aFalse = getExpressionEvaluation(j, "false");
        assertEquals(Boolean.FALSE, aFalse);

        Object anEmpty = getExpressionEvaluation(j, "empty-string");
        assertEquals(Boolean.FALSE, anEmpty);

        Object aTrue = getExpressionEvaluation(j, "true");
        assertEquals(Boolean.TRUE, aTrue);

        Object aString = getExpressionEvaluation(j, "string");
        assertEquals(Boolean.TRUE, aString);

        Object anEmptyString = getExpressionEvaluation(j, "empty-string");
        assertEquals(Boolean.FALSE, anEmptyString);

        Object nestedFalse = getExpressionEvaluation(j, "nestedTrue");
        assertEquals(Boolean.TRUE, nestedFalse);

        Object nestedTrue = getExpressionEvaluation(j, "nestedFalse");
        assertEquals(Boolean.FALSE, nestedTrue);
    }

    /** Verify that the "to-boolean" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseToStringExpression() {

        final JSONObject j = getObjectByLayerId("toStringExpression", "paint");

        Object aNull = getExpressionEvaluation(j, "null");
        assertEquals("null", aNull);

        Object aZero = getExpressionEvaluation(j, "zero");
        assertEquals("0", aZero);

        Object aFalse = getExpressionEvaluation(j, "false");
        assertEquals("false", aFalse);

        Object aTrue = getExpressionEvaluation(j, "true");
        assertEquals("true", aTrue);

        Object aNumber = getExpressionEvaluation(j, "number");
        assertEquals("123", aNumber);

        Object aString = getExpressionEvaluation(j, "string");
        assertEquals("string", aString);

        Object jsonObject = getExpressionEvaluation(j, "object");
        assertEquals("{\"color\":false}", jsonObject);

        Object jsonArray = getExpressionEvaluation(j, "array");
        assertEquals("[\"color\",true,123,{\"obj\":\"test\"}]", jsonArray);

        Object color = getExpressionEvaluation(j, "color");
        assertEquals("rgba(23,34,45,1.0)", color);
    }

    /** Verify that the "to-number" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseToNumberExpression() {

        final JSONObject j = getObjectByLayerId("toNumberExpression", "paint");

        Object aNull = getExpressionEvaluation(j, "null");
        assertEquals(0L, aNull);

        Object aZero = getExpressionEvaluation(j, "zero");
        assertEquals(0L, aZero);

        Object anInt = getExpressionEvaluation(j, "int-string");
        assertEquals(123.0D, anInt);

        Object aDouble = getExpressionEvaluation(j, "double-string");
        assertEquals(123.123D, aDouble);

        Object aFalse = getExpressionEvaluation(j, "false");
        assertEquals(0L, aFalse);

        Object aTrue = getExpressionEvaluation(j, "true");
        assertEquals(1L, aTrue);

        Object nested = getExpressionEvaluation(j, "nest");
        assertEquals(46.08D, nested);

        try {
            getExpressionEvaluation(j, "object");
            fail("expected exception function \"mbToColor\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }
    }

    /** Verify that the "to-number" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseToColorExpression() {

        final JSONObject j = getObjectByLayerId("toColorExpression", "paint");

        Object rgb = getExpressionEvaluation(j, "rgb");
        assertEquals(new Color(111, 222, 121), rgb);

        Object rgba = getExpressionEvaluation(j, "rgba");
        assertEquals(new Color(111, 222, 121, 128), rgba);

        Object hex = getExpressionEvaluation(j, "hex");
        assertEquals(new Color(170, 255, 17), hex);

        Object threeHex = getExpressionEvaluation(j, "three-hex");
        assertEquals(new Color(170, 255, 17), threeHex);

        Object rgbArr = getExpressionEvaluation(j, "rgb-array");
        assertEquals(new Color(111, 222, 121), rgbArr);

        Object rgbaArr = getExpressionEvaluation(j, "rgba-array");
        assertEquals(new Color(111, 222, 121, 128), rgbaArr);

        Object nested = getExpressionEvaluation(j, "nest");
        assertEquals(new Color(111, 222, 121), nested);

        try {
            getExpressionEvaluation(j, "true");
            fail("expected exception function \"mbToColor\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }

        try {
            getExpressionEvaluation(j, "false");
            fail("expected exception function \"mbToColor\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }
    }
    /** Verify that the "array" MBTypes expression can be parsed and evaluated correctly. */
    @Test
    public void testParseArrayExpression() {

        final JSONObject j = getObjectByLayerId("arrayExpression", "paint");
        JSONArray test = new JSONArray();

        @SuppressWarnings("unchecked")
        ArrayList<Object> array = (ArrayList<Object>) test;

        array.add(111L);
        array.add(222L);
        array.add(121L);

        Object arr = getExpressionEvaluation(j, "array");
        assertEquals(test, arr);

        Object nested = getExpressionEvaluation(j, "nest");
        assertEquals(test, nested);

        try {
            getExpressionEvaluation(j, "boolean");
            fail("expected exception function \"mbType\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }

        try {
            getExpressionEvaluation(j, "string");
            fail("expected exception function \"mbType\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }

        try {
            getExpressionEvaluation(j, "number");
            fail("expected exception function \"mbType\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }

        try {
            getExpressionEvaluation(j, "object");
            fail("expected exception function \"mbType\" fails if argument can't be converted to a color");
        } catch (IllegalArgumentException e) {
        }
    }
}
