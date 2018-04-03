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

import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MBTypeTest {
    Map<String, JSONObject> testLayersById = new HashMap<>();
    MBObjectParser parse;
    FilterFactory2 ff;
    JSONObject mbstyle;

    @Before
    public void setUp() throws IOException, ParseException {
        mbstyle = MapboxTestUtils.parseTestStyle("expressionParseTypesTest.json");
        JSONArray layers = (JSONArray) mbstyle.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
        parse = new MBObjectParser(MBExpression.class);
        ff = parse.getFilterFactory();
    }

    /**
     * Verify that the "literal" MBTypes expression can be parsed correctly.
     */
    @Test
    public void testParseLiteralExpression() {

        JSONObject layer = testLayersById.get("literalExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint");
        JSONObject j = o.get();

        // test for literal JSONArray
        assertEquals(JSONArray.class, j.get("circle-radius").getClass());
        JSONArray arr0 = (JSONArray) j.get("circle-radius");
        assertEquals(MBTypes.class, MBExpression.create(arr0).getClass());
        Expression litArr = MBExpression.transformExpression(arr0);
        Object array = litArr.evaluate(litArr);
        assertTrue(array instanceof JSONArray);
        JSONArray jarray = (JSONArray)array;
        assertEquals(4, jarray.size());
        assertEquals("string", (jarray.get(1)));

        // test for literal JSONObject
        assertEquals(JSONArray.class, j.get("circle-color").getClass());
        JSONArray arr1 = (JSONArray) j.get("circle-color");
        assertEquals(MBTypes.class, MBExpression.create(arr1).getClass());
        Expression litObj = MBExpression.transformExpression(arr1);
        Object obj = litObj.evaluate(litObj);
        assertTrue(obj instanceof JSONObject);
        JSONObject jobj = (JSONObject) obj;
        assertEquals(2, jobj.size());
        assertEquals(jobj.get("object"), "test");

    }

    private <T> Optional<T> traverse(JSONObject map, Class<T> clazz,
                                     String... path) {
        if (path == null || path.length == 0) {
            return Optional.empty();
        }

        Object value = map;
        for (String key : path) {
            if (value instanceof JSONObject) {
                JSONObject m = (JSONObject) value;
                if (m.containsKey(key)) {
                    value = m.get(key);
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(clazz.cast(value));
    }
}