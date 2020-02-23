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
package org.geotools.mbstyle.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.expression.MBColor;
import org.geotools.mbstyle.expression.MBExpression;
import org.geotools.mbstyle.expression.MBString;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.TextSymbolizer;
import org.geotools.xml.styling.SLDTransformer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

public class MBExpressionParseTest {
    Map<String, JSONObject> testLayersById = new HashMap<>();
    MBObjectParser parse;
    FilterFactory2 ff;
    JSONObject mbstyle;

    @Before
    public void setUp() throws IOException, ParseException {
        mbstyle = MapboxTestUtils.parseTestStyle("expressionParseTest.json");
        JSONArray layers = (JSONArray) mbstyle.get("layers");
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
    public void testParseExpressions() {

        JSONObject layer = testLayersById.get("testExpressions");
        Optional<JSONObject> l = traverse(layer, JSONObject.class, "layout");
        JSONObject layout = l.get();
        Optional<JSONObject> p = traverse(layer, JSONObject.class, "paint");
        JSONObject paint = p.get();
        Optional<JSONArray> f = traverse(layer, JSONArray.class, "filter");
        JSONArray filter = f.get();

        // Testing parsing expression out of layer JSONObject
        Function upper = ff.function("strToUpperCase", ff.literal("test"));
        assertEquals(upper, parse.string(layout, "text-field", "fallback"));

        // Testing parsing expression out of filter JSONArray
        Function cat =
                ff.function("Concatenate", ff.literal("P"), ff.literal("o"), ff.literal("int"));
        assertEquals(cat, parse.string(filter, 2));

        // Testing parsing expression out of paint JSONObject
        Function down = ff.function("strToLowerCase", ff.literal("RED"));
        assertEquals(down, parse.color(paint, "text-color", Color.RED));

        // Testing exception thrown for unknown expression
        try {
            parse.string(layout, "text-size", "fallback");
            fail("expected format exception due to 'test' being an invalid MBExpression");
        } catch (MBFormatException expected) {
        }
    }
    // ---- STRING EXPRESSIONS ---------------------------------------------------------

    /** Verify that a upcase string expression can be parsed correctly. */
    @Test
    public void testParseUpcaseExpression() {

        JSONObject layer = testLayersById.get("upcaseExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression upcase = MBExpression.transformExpression(arr);
        Object up = upcase.evaluate(upcase);
        assertEquals(ff.literal("UPCASING STRING"), ff.literal(up));
    }

    /** Verify that a downcase string expression can be parsed correctly. */
    @Test
    public void testParseDowncaseExpression() {

        JSONObject layer = testLayersById.get("downcaseExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression downcase = MBExpression.transformExpression(arr);
        Object down = downcase.evaluate(downcase);
        assertEquals(ff.literal("downcasing string"), ff.literal(down));
    }

    /** Verify that a concat string expression can be parsed correctly. */
    @Test
    public void testParseConcatExpression() {

        JSONObject layer = testLayersById.get("concatExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression concat = MBExpression.transformExpression(arr);
        Object cat = concat.evaluate(concat);
        assertEquals(ff.literal("cat"), ff.literal(cat));
    }

    @Test
    public void testParseAllStringExpressions() {

        JSONObject layer = testLayersById.get("allStringExpressions");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression all = MBExpression.transformExpression(arr);
        Object a = all.evaluate(all);
        assertEquals(ff.literal("Cat"), ff.literal(a));
    }

    @Test
    public void testParseUpcaseConcat() {

        JSONObject layer = testLayersById.get("upcaseConcat");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-field").getClass());
        JSONArray arr = (JSONArray) j.get("text-field");
        assertEquals(MBString.class, MBExpression.create(arr).getClass());
        Expression all = MBExpression.transformExpression(arr);
        Object a = all.evaluate(all);
        assertEquals(ff.literal("CAT"), ff.literal(a));
    }

    // ---- COLOR EXPRESSIONS ---------------------------------------------------------
    @Test
    public void testParseRgb() {

        JSONObject layer = testLayersById.get("rgbExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-color").getClass());
        JSONArray arr = (JSONArray) j.get("text-color");
        assertEquals(MBColor.class, MBExpression.create(arr).getClass());
        Expression rgb = MBExpression.transformExpression(arr);
        Object c = rgb.evaluate(rgb);
        assertEquals(new Color(0, 111, 222), c);
    }

    @Test
    public void testParseRgba() {

        JSONObject layer = testLayersById.get("toRgbaExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-color").getClass());
        JSONArray arr = (JSONArray) j.get("text-color");
        assertEquals(MBColor.class, MBExpression.create(arr).getClass());
        try {
            MBExpression.transformExpression(arr);
            fail("expected exception due to \"to-rgba\" function being unsupported");
        } catch (UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testParseToRgba() {

        JSONObject layer = testLayersById.get("rgbaExpression");
        Optional<JSONObject> o = traverse(layer, JSONObject.class, "paint");
        JSONObject j = o.get();
        assertEquals(JSONArray.class, j.get("text-color").getClass());
        JSONArray arr = (JSONArray) j.get("text-color");
        assertEquals(MBColor.class, MBExpression.create(arr).getClass());
        try {
            MBExpression.transformExpression(arr);
            fail("expected exception due to \"rgba\" function being unsupported");
        } catch (UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testRgbSldTransformation() {
        MBStyle rgbTest = MBStyle.create(mbstyle);
        SymbolMBLayer rgbLayer = (SymbolMBLayer) rgbTest.layer("rgbExpression");
        List<FeatureTypeStyle> rgbFeatures = rgbLayer.transformInternal(rgbTest);
        Color sldColor =
                (((TextSymbolizer) rgbFeatures.get(0).rules().get(0).symbolizers().get(0))
                        .getFill()
                        .getColor()
                        .evaluate(null, Color.class));
        assertEquals(new Color(0, 111, 222), sldColor);
        try {
            String xml = new SLDTransformer().transform(rgbFeatures.get(0));
            assertTrue(
                    xml.contains(
                            "<sld:Fill><sld:CssParameter name=\"fill\"><ogc:Function name=\"torgb\">"
                                    + "<ogc:Function name=\"round_2\"><ogc:Literal>0</ogc:Literal></ogc:Function>"
                                    + "<ogc:Function name=\"round_2\"><ogc:Literal>111</ogc:Literal></ogc:Function><ogc:Function name=\"round_2\">"
                                    + "<ogc:Literal>222</ogc:Literal></ogc:Function></ogc:Function></sld:CssParameter></sld:Fill>"));
        } catch (Exception e) {
        }
    }

    // ---- DECISION EXPRESSIONS ---------------------------------------------------------

    // ---- FEATUREDATA EXPRESSIONS ---------------------------------------------------------

    // ---- HEATMAP EXPRESSIONS ---------------------------------------------------------

    // ---- LOOKUP EXPRESSIONS ---------------------------------------------------------

    // ---- MATH EXPRESSIONS ---------------------------------------------------------

    // ---- RAMPS SCALES CURVES EXPRESSIONS
    // ---------------------------------------------------------

    // ---- TYPE EXPRESSIONS ---------------------------------------------------------

    // ---- VARIABLE BINDING EXPRESSIONS ---------------------------------------------------------

    // ---- ZOOM EXPRESSIONS ---------------------------------------------------------

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
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(clazz.cast(value));
    }
}
