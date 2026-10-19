/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.awt.Color;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

/** Tests the "interpolate" and "step" expression forms (flat arrays, as produced by MapBox/protomaps styles). */
public class MBRampsScalesCurvesTest {

    private final JSONParser parser = new JSONParser();
    private SimpleFeatureType type;

    @Before
    public void setUp() throws Exception {
        type = DataUtilities.createType("ramps.test", "value:double");
    }

    private SimpleFeature feature(double value) {
        return SimpleFeatureBuilder.build(type, new Object[] {value}, "ramps.1");
    }

    private Object evaluate(String json, double value, Class<?> target) throws Exception {
        return transform(json).evaluate(feature(value), target);
    }

    private Expression transform(String json) throws Exception {
        return MBExpression.transformExpression((JSONArray) parser.parse(json));
    }

    @Test
    public void testLinearInterpolate() throws Exception {
        String json = "[\"interpolate\",[\"linear\"],[\"get\",\"value\"],0,0,10,100]";
        assertEquals(50.0, (Double) evaluate(json, 5.0, Double.class), 1e-9);
        // below the first stop clamps to the first output, above the last clamps to the last
        assertEquals(0.0, (Double) evaluate(json, -5.0, Double.class), 1e-9);
        assertEquals(100.0, (Double) evaluate(json, 20.0, Double.class), 1e-9);
    }

    @Test
    public void testExponentialInterpolate() throws Exception {
        // mapbox-gl-js exponentialInterpolation: t = (base^progress - 1) / (base^difference - 1),
        // output = lower + t * (upper - lower). For base 2, stops 0->0 and 10->100, input 5:
        // t = (2^5 - 1) / (2^10 - 1) = 31 / 1023, output = 100 * 31 / 1023 = 3.0303...
        String json = "[\"interpolate\",[\"exponential\",2],[\"get\",\"value\"],0,0,10,100]";
        assertEquals(100.0 * 31 / 1023, (Double) evaluate(json, 5.0, Double.class), 1e-9);
    }

    @Test
    public void testExponentialBaseOne() throws Exception {
        String json = "[\"interpolate\",[\"exponential\",1],[\"get\",\"value\"],0,0,10,100]";
        assertEquals(50.0, (Double) evaluate(json, 5.0, Double.class), 1e-9);
    }

    @Test
    public void testExpressionStopOutput() throws Exception {
        // the upper output is value * 2 = 10 at input 5, so halfway between 0 and 10
        String json = "[\"interpolate\",[\"linear\"],[\"get\",\"value\"],0,0,10,[\"*\",[\"get\",\"value\"],2]]";
        assertEquals(5.0, (Double) evaluate(json, 5.0, Double.class), 1e-9);
    }

    @Test
    public void testCubicBezierRejected() {
        String json = "[\"interpolate\",[\"cubic-bezier\",0,0,1,1],[\"get\",\"value\"],0,0,10,100]";
        MBFormatException e = assertThrows(MBFormatException.class, () -> transform(json));
        assertThat(e.getMessage(), containsString("cubic-bezier"));
    }

    @Test
    public void testInterpolateMissingStopOutput() {
        String json = "[\"interpolate\",[\"linear\"],[\"get\",\"value\"],0,0,10]";
        MBFormatException e = assertThrows(MBFormatException.class, () -> transform(json));
        assertThat(e.getMessage(), containsString("\"interpolate\" requires"));
    }

    @Test
    public void testColorInterpolate() throws Exception {
        String json = "[\"interpolate\",[\"linear\"],[\"get\",\"value\"],0,\"#000000\",10,\"#ffffff\"]";
        Color mid = (Color) evaluate(json, 5.0, Color.class);
        assertEquals(new Color(128, 128, 128), mid);
    }

    @Test
    public void testStep() throws Exception {
        // output_0 below first threshold, then stepped values
        String json = "[\"step\",[\"get\",\"value\"],-1,10,1,20,2]";
        assertEquals(Integer.valueOf(-1), evaluate(json, 5.0, Integer.class));
        assertEquals(Integer.valueOf(1), evaluate(json, 15.0, Integer.class));
        assertEquals(Integer.valueOf(2), evaluate(json, 25.0, Integer.class));
    }

    @Test
    public void testStepAtThreshold() throws Exception {
        // the stop applies from its threshold included, as in MapLibre
        String json = "[\"step\",[\"get\",\"value\"],-1,10,1,20,2]";
        assertEquals(Integer.valueOf(1), evaluate(json, 10.0, Integer.class));
    }

    @Test
    public void testStepMissingStopOutput() {
        String json = "[\"step\",[\"get\",\"value\"],-1,10,1,20]";
        MBFormatException e = assertThrows(MBFormatException.class, () -> transform(json));
        assertThat(e.getMessage(), containsString("\"step\" requires"));
    }

    @Test
    public void testStepWithStringOutputs() throws Exception {
        // non-color string outputs (e.g. icon names) must not be misdetected as colors
        String json = "[\"step\",[\"get\",\"value\"],\"small\",10,\"big\"]";
        assertEquals("small", evaluate(json, 5.0, String.class));
        assertEquals("big", evaluate(json, 15.0, String.class));
    }
}
