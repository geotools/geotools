package org.geotools.mbstyle.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    
    /**
     * Verify that a linear color zoom function can be parsed correctly.
     */
    @Test
    public void testParseLinearColorFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function1");
        Optional<JSONObject> o = safeTraverse(layer, JSONObject.class, "paint", "line-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getType());
        assertEquals(EnumSet.of(MBFunction.FunctionCategory.ZOOM), fn.category());
        assertEquals(1, fn.getBase().intValue());
        assertNull(fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(2, fn.getStops().size());
    }

    /**
     * Verify that an exponential (color) zoom function can be parsed correctly.
     */
    @Test
    public void testParseExpColorFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function2");
        Optional<JSONObject> o = safeTraverse(layer, JSONObject.class, "paint", "line-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getType());
        assertEquals(EnumSet.of(MBFunction.FunctionCategory.ZOOM), fn.category());
        assertEquals(1.75, fn.getBase().doubleValue(), .00001);
        assertNull(fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(3, fn.getStops().size());
    }

    /**
     * Verify that an exponential (number) zoom function can be parsed correctly.
     */
    @Test
    public void testParseExpRadiusFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function3");
        Optional<JSONObject> o = safeTraverse(layer, JSONObject.class, "paint", "circle-radius");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getType());
        assertEquals(EnumSet.of(MBFunction.FunctionCategory.ZOOM), fn.category());
        assertEquals(1.5, fn.getBase().doubleValue(), .00001);
        assertNull(fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(3, fn.getStops().size());
    }

    // ---- PROPERTY AND ZOOM FUNCTIONS ---------------------------------------------------------
    
    /**
     * Verify that a linear zoom-and-property function can be parsed correctly.
     */
    @Test
    public void testParsePropertyZoomFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function4");
        Optional<JSONObject> o = safeTraverse(layer, JSONObject.class, "paint", "circle-radius");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getType());
        assertEquals(
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY, MBFunction.FunctionCategory.ZOOM),
                fn.category());
        assertEquals("rating", fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(4, fn.getStops().size());
    }
    
    // ---- PROPERTY FUNCTIONS ---------------------------------------------------------
    

    /**
     * Verify that a linear property function can be parsed correctly.
     */
    @Test
    public void testParsePropertyFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function5");
        Optional<JSONObject> o = safeTraverse(layer, JSONObject.class, "paint", "circle-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, fn.getType());
        assertEquals(EnumSet.of(MBFunction.FunctionCategory.PROPERTY), fn.category());
        assertEquals("temperature", fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(2, fn.getStops().size());
    }
    
    /**
     * Verify that a categorical property functions can be parsed correctly.
     */
    @Test
    public void testParseCategoricalFunction() throws IOException, ParseException {
        JSONObject layer = testLayersById.get("function6");
        Optional<JSONObject> o = safeTraverse(layer, JSONObject.class, "paint", "circle-color");
        JSONObject j = o.get();
        MBFunction fn = new MBFunction(j);
        assertEquals(MBFunction.FunctionType.CATEGORICAL, fn.getType());
        assertEquals(
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                fn.category());
        assertEquals("color", fn.getProperty());
        assertNotNull(fn.getStops());
        assertEquals(5, fn.getStops().size());
    }

    /**
     * Traverse a nested map using the array of strings, and cast the result to the provided class, or return {@link Optional#empty()}.
     */
    private <T> Optional<T> safeTraverse(Map<String, Object> map, Class<T> clazz,
            String... strings) {
        if (strings == null || strings.length == 0) {
            return Optional.empty();
        }

        Object curObj = map;
        for (String s : Arrays.asList(strings)) {
            if (curObj instanceof Map) {
                Map m = (Map) curObj;
                if (m.containsKey(s)) {
                    curObj = m.get(s);
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(clazz.cast(curObj));
    }
}
