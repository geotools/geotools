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

import static org.geotools.mbstyle.parse.MBStyleTestUtils.categories;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.util.*;
import org.geotools.mbstyle.MapboxTestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class FunctionParseTest {

    Map<String, JSONObject> testLayersById = new HashMap<>();

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("functionParseTest.json");
        JSONArray layers = (JSONArray) jsonObject.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
    }

    // ---- ZOOM FUNCTIONS ---------------------------------------------------------

    /** Verify that a linear color zoom function can be parsed correctly. */
    @Test
    public void testParseLinearColorFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function1");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint", "line-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);

        // The function type is not specified in the JSON, so getType should return null.
        assertNull(fn.getType());
        // But the default fallback function type for color returns is 'exponential':
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getTypeWithDefault(Color.class));

        assertEquals(EnumSet.of(MBFunction.FunctionCategory.ZOOM), fn.category());
        assertEquals(1, fn.getBase().intValue());
        assertNull(fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(2, fn.getStops().size());
    }

    /** Verify that an exponential (color) zoom function can be parsed correctly. */
    @Test
    public void testParseExpColorFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function2");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint", "line-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);

        // The function type is not specified in the JSON, so getType should return null.
        assertNull(fn.getType());
        // But the default fallback function type for color returns is 'exponential':
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getTypeWithDefault(Color.class));

        assertEquals(EnumSet.of(MBFunction.FunctionCategory.ZOOM), fn.category());
        assertEquals(1.75, fn.getBase().doubleValue(), .00001);
        assertNull(fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(3, fn.getStops().size());
    }

    /** Verify that an exponential (number) zoom function can be parsed correctly. */
    @Test
    public void testParseExpRadiusFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function3");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint", "circle-radius");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);

        // The function type is not specified in the JSON, so getType should return null.
        assertNull(fn.getType());
        // But the default fallback function type for number returns is 'exponential':
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getTypeWithDefault(Number.class));

        assertEquals(EnumSet.of(MBFunction.FunctionCategory.ZOOM), fn.category());
        assertEquals(1.5, fn.getBase().doubleValue(), .00001);
        assertNull(fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(3, fn.getStops().size());
    }

    // ---- PROPERTY AND ZOOM FUNCTIONS ---------------------------------------------------------

    /** Verify that a linear zoom-and-property function can be parsed correctly. */
    @Test
    public void testParsePropertyZoomFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function4");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint", "circle-radius");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);

        // The function type is not specified in the JSON, so getType should return null.
        assertNull(fn.getType());
        // But the default fallback function type for number returns is 'exponential':
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getTypeWithDefault(Number.class));

        assertEquals(
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY, MBFunction.FunctionCategory.ZOOM),
                fn.category());
        assertEquals("rating", fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(4, fn.getStops().size());
    }

    // ---- PROPERTY FUNCTIONS ---------------------------------------------------------

    /** Verify that a linear property function can be parsed correctly. */
    @Test
    public void testParsePropertyFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function5");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint", "circle-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);

        // The function type is not specified in the JSON, so getType should return null.
        assertNull(fn.getType());
        // But the default fallback function type for color returns is 'exponential':
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getTypeWithDefault(Color.class));

        assertEquals(EnumSet.of(MBFunction.FunctionCategory.PROPERTY), fn.category());
        assertEquals("temperature", fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(2, fn.getStops().size());
    }

    /** Verify that a categorical property functions can be parsed correctly. */
    @Test
    public void testParseCategoricalFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function6");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint", "circle-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.CATEGORICAL, fn.getType());
        assertEquals(EnumSet.of(MBFunction.FunctionCategory.PROPERTY), fn.category());
        assertEquals("color", fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(5, fn.getStops().size());
    }

    /** Verify that an interval function for colours can be parsed. */
    @Test
    public void testParseIntervalFunctionColour() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("functionIntervalColour");
        JSONObject j = traverse(layer, JSONObject.class, "paint", "circle-color").get();
        MBFunction fn = new MBFunction(j);
        assertThat(fn, hasProperty("type", is(MBFunction.FunctionType.INTERVAL)));
        assertThat(fn, categories(containsInAnyOrder(MBFunction.FunctionCategory.PROPERTY)));
        assertThat(fn, hasProperty("property", equalTo("temperature")));
        assertThat(
                fn,
                hasProperty(
                        "stops",
                        contains(
                                contains(-273.15, "#4488FF"),
                                contains(10.0, "#00FF00"),
                                contains(30.0, "#FF8800"))));
    }
    /** Verify that an interval function for number can be parsed. */
    @Test
    public void testParseIntervalFunctionNumber() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("functionIntervalNumeric");
        JSONObject j = traverse(layer, JSONObject.class, "paint", "circle-radius").get();
        MBFunction fn = new MBFunction(j);
        assertThat(fn, hasProperty("type", is(MBFunction.FunctionType.INTERVAL)));
        assertThat(fn, categories(containsInAnyOrder(MBFunction.FunctionCategory.PROPERTY)));
        assertThat(fn, hasProperty("property", equalTo("rating")));
        assertThat(
                fn,
                hasProperty(
                        "stops",
                        contains(
                                contains(0L, 0L),
                                contains(2L, 5L),
                                contains(5L, 10L),
                                contains(10L, 20L))));
    }
    /**
     * Traverse a nested map using the array of strings, and cast the result to the provided class,
     * or return {@link Optional#empty()}.
     *
     * @param clazz expected type
     * @param path used to access map
     * @return result at the provided path, or {@link Optional#empty()}.
     */
    private <T> Optional<T> traverse(JSONObject map, Class<T> clazz, String... path) {
        if (path == null || path.length == 0) {
            return Optional.empty();
        }

        Object value = map;
        for (String key : path) {
            if (value instanceof JSONObject) {
                JSONObject m = (JSONObject) value;
                if (m.containsKey(key)) {
                    value = m.get(key);
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(clazz.cast(value));
    }
}
