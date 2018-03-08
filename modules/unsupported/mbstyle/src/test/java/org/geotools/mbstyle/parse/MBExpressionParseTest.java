package org.geotools.mbstyle.parse;

import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.expression.MBExpression;
import org.geotools.mbstyle.expression.MBString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class MBExpressionParseTest {
    Map<String, JSONObject> testLayersById = new HashMap<>();
    MBObjectParser parse;
    FilterFactory2 ff;

    @Before
    public void setUp() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("expressionParseTest.json");
        JSONArray layers = (JSONArray) jsonObject.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
        parse = new MBObjectParser(MBExpression.class);
        ff = parse.getFilterFactory();
    }

    // ---- MAPBOX EXPRESSIONS BASE ---------------------------------------------------------
    /**
     * Verify that an expression can be parsed correctly from Layout, Paint, and Filter properties.
     */
    @Test
    public void testParseExpressions() throws IOException, ParseException {

        JSONObject layer = testLayersById.get("testExpressions");
        Optional<JSONObject> l = traverse(layer, JSONObject.class, "layout");
        JSONObject layout = l.get();
        Optional<JSONObject> p = traverse(layer, JSONObject.class, "paint");
        JSONObject paint = p.get();
        Optional<JSONArray> f = traverse(layer, JSONArray.class, "filter");
        JSONArray filter = f.get();

        // Testing parsing expression out of layer JSONObject
        assertEquals("TEST", parse.string(layout, "text-field", "fallback").toString());

        // Testing parsing expression out of filter JSONArray
        assertEquals("Point", parse.value(filter, 2).toString());

        // Testing parsing expression out of paint JSONObject
        assertEquals("red", parse.color(paint, "text-color", Color.RED).toString());


        // Testing exception thrown for unknown expression
        try {
            parse.string(layout, "text-size", "fallback");
            fail("expected format exception due to 'test' being an invalid MBExpression");
        }
        catch (MBFormatException expected){
        }

    }
    // ---- STRING EXPRESSIONS ---------------------------------------------------------

    /**
     * Verify that a upcase string expression can be parsed correctly.
     */
    @Test
    public void testParseUpcaseExpression() throws IOException, ParseException {

        JSONObject layer = testLayersById.get("upcaseExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression upcase = MBExpression.transformExpression(arr);
        assertEquals(ff.literal("UPCASING STRING"), upcase);
    }

    /**
     * Verify that a downcase string expression can be parsed correctly.
     */
    @Test
    public void testParseDowncaseExpression() throws IOException, ParseException {

        JSONObject layer = testLayersById.get("downcaseExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression downcase = MBExpression.transformExpression(arr);
        assertEquals(ff.literal("downcasing string"), downcase);
    }

    /**
     * Verify that a concat string expression can be parsed correctly.
     */
    @Test
    public void testParseConcatExpression() throws IOException, ParseException {

        JSONObject layer = testLayersById.get("concatExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression concat = MBExpression.transformExpression(arr);
        assertEquals(ff.literal("cat"), concat);
    }

    // ---- COLOR EXPRESSIONS ---------------------------------------------------------


    // ---- DECISION EXPRESSIONS ---------------------------------------------------------


    // ---- FEATUREDATA EXPRESSIONS ---------------------------------------------------------


    // ---- HEATMAP EXPRESSIONS ---------------------------------------------------------


    // ---- LOOKUP EXPRESSIONS ---------------------------------------------------------


    // ---- MATH EXPRESSIONS ---------------------------------------------------------


    // ---- RAMPS SCALES CURVES EXPRESSIONS ---------------------------------------------------------


    // ---- TYPE EXPRESSIONS ---------------------------------------------------------


    // ---- VARIABLE BINDING EXPRESSIONS ---------------------------------------------------------


    // ---- ZOOM EXPRESSIONS ---------------------------------------------------------

    /**
     * Traverse a nested map using the array of strings, and cast the result to the provided class, or return {@link Optional#empty()}.
     * @param map
     * @param clazz expected type
     * @param path used to access map
     * @return result at the provided path, or {@link Optional#empty()}.
     */
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
