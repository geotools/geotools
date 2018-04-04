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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MBTypeTest extends AbstractMBExpressionTest {

    @Override
    protected String getTestResourceName() {
        return "expressionParseTypesTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBTypes.class;
    }

    /**
     * Verify that the "literal" MBTypes expression can be parsed correctly.
     */
    @Test
    public void testParseLiteralExpression() {

        final JSONObject j = getObjectByLayerId("literalExpression", "paint");

        // test for literal JSONArray
        Object array = getExpressionEvaluation(j, "circle-radius");
        assertTrue(array instanceof JSONArray);
        JSONArray jarray = (JSONArray)array;
        assertEquals(4, jarray.size());
        assertEquals("string", (jarray.get(1)));

        // test for literal JSONObject
        Object obj = getExpressionEvaluation(j, "circle-color");
        assertTrue(obj instanceof JSONObject);
        JSONObject jobj = (JSONObject) obj;
        assertEquals(2, jobj.size());
        assertEquals(jobj.get("object"), "test");

    }
}