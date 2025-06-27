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
import static org.geotools.mbstyle.parse.MBStyleTestUtils.equalInt;
import static org.geotools.mbstyle.parse.MBStyleTestUtils.evaluatesTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.layer.LineMBLayer.LineJoin;
import org.geotools.mbstyle.parse.MBFunction.FunctionType;
import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * Try out property and zoom function.
 *
 * <p>See example https://www.mapbox.com/help/gl-dds-ref/ online.
 */
public class MBFunctionTest {

    public static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    @Test
    public void colorIdentityTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType(
                "SAMPLE", "id:\"\",temperature:0.0,location=4326,color:java.awt.Color,text:String:");
        SimpleFeature feature1 = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)|#FF0000|red");
        SimpleFeature feature2 = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)|#0000FF|rgb(0,0,255)");

        // color test
        MBFunction function = new MBFunction(MapboxTestUtils.object("{'property':'color','type':'identity'}"));
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertSame("identity", function.getType(), FunctionType.IDENTITY);

        Expression color = function.color();
        assertEquals("color", Color.RED, color.evaluate(feature1, Color.class));
        assertEquals("color", Color.BLUE, color.evaluate(feature2, Color.class));

        function = new MBFunction(MapboxTestUtils.object("{'property':'text','type':'identity'}"));
        Expression text = function.color();
        assertEquals("text", Color.RED, text.evaluate(feature1, Color.class));
        assertEquals("text", Color.BLUE, text.evaluate(feature2, Color.class));
    }

    /**
     * Assert that the color identity function correctly returns the default value when the property is not a valid
     * color.
     */
    @Test
    public void colorIdentityDefaultValueTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType(
                "SAMPLE", "id:\"\",temperature:0.0,location=4326,color:java.awt.Color,text:String:");
        MBFunction function =
                new MBFunction(MapboxTestUtils.object("{'property':'color','type':'identity', 'default':'#FFFFFF'}"));
        SimpleFeature feature =
                DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)|NOT_A_VALID_COLOR|NOT_A_VALID_COLOR");
        assertEquals("Default", Color.WHITE, function.color().evaluate(feature, Color.class));
    }

    @Test
    public void propertyColorStops() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        JSONObject json = MapboxTestUtils.object(
                "{'property':'temperature','type':'exponential','stops':[[0,'blue'],[100,'red']]}");
        MBFunction function = new MBFunction(json);

        assertEquals("temperature", function.getProperty());
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = (Function) function.color();
        assertNotNull(fn);
        assertEquals("Interpolate", fn.getName());
        Expression property = fn.getParameters().get(0);
        assertEquals("temperature", ECQL.toCQL(property));

        Literal stop1 = (Literal) fn.getParameters().get(1);
        assertEquals(Integer.valueOf(0), stop1.evaluate(null, Integer.class));

        Literal color1 = (Literal) fn.getParameters().get(2);
        assertEquals(Color.BLUE, color1.evaluate(null, Color.class));

        Literal stop2 = (Literal) fn.getParameters().get(3);
        assertEquals(Integer.valueOf(100), stop2.evaluate(null, Integer.class));

        Literal color2 = (Literal) fn.getParameters().get(4);
        assertEquals(Color.RED, color2.evaluate(null, Color.class));

        // We are taking care to check that the function method has been supplied
        Literal method = (Literal) fn.getParameters().get(5);
        assertEquals("color", method.evaluate(null, String.class));

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(128, 0, 128), result);
    }

    /**
     * Assert that the an exponential color function correctly falls back to the default value if the input property is
     * not numeric.
     */
    @Test
    public void propertyColorStopsDefaultValue() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|NOT_A_VALID_NUMBER|POINT(0,0)");

        JSONObject json = MapboxTestUtils.object(
                "{'property':'temperature','type':'exponential','stops':[[0,'blue'],[100,'red']], 'default':'#000000'}");
        MBFunction function = new MBFunction(json);

        Color result = function.color().evaluate(feature, Color.class);
        assertEquals(new Color(0, 0, 0), result);
    }

    @Test
    public void propertyColorStopsBase() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        JSONObject json = MapboxTestUtils.object(
                "{'property':'temperature','type':'exponential','stops':[[0,'blue'],[100,'red']],'base':1.1}");
        MBFunction function = new MBFunction(json);

        assertEquals("temperature", function.getProperty());
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = (Function) function.color();
        assertNotNull(fn);
        assertEquals("Exponential", fn.getName());

        Expression property = fn.getParameters().get(0);
        assertEquals("temperature", ECQL.toCQL(property));

        Literal base = (Literal) fn.getParameters().get(1);
        assertEquals(Double.valueOf(1.1), base.evaluate(null, Double.class));

        Literal stop1 = (Literal) fn.getParameters().get(2);
        assertEquals(Integer.valueOf(0), stop1.evaluate(null, Integer.class));

        Literal color1 = (Literal) fn.getParameters().get(3);
        assertEquals(Color.BLUE, color1.evaluate(null, Color.class));

        Literal stop2 = (Literal) fn.getParameters().get(4);
        assertEquals(Integer.valueOf(100), stop2.evaluate(null, Integer.class));

        Literal color2 = (Literal) fn.getParameters().get(5);
        assertEquals(Color.RED, color2.evaluate(null, Color.class));

        Color result = fn.evaluate(feature, Color.class);

        // When the base is greater than 1, more growth should be at the end of the range.
        // So the interpolation on the halfway input value should be less than the halfway output
        // value, which would be (128, 0 , 128).
        assertEquals(0, result.getGreen());
        assertTrue(result.getBlue() > 128);
        assertTrue(result.getRed() < 128);
    }

    @Test
    public void propertyValueStops() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        JSONObject json =
                MapboxTestUtils.object("{'type':'exponential', 'property':'temperature','stops':[[0,5],[100,10]]}");

        MBFunction function = new MBFunction(json);

        assertEquals("temperature", function.getProperty());
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = (Function) function.numeric();
        assertNotNull(fn);
        assertEquals("Interpolate", fn.getName());
        PropertyName propertyName = (PropertyName) fn.getParameters().get(0);
        assertEquals("temperature", propertyName.getPropertyName());

        Double result = fn.evaluate(feature, Double.class);
        assertEquals(7.5, result, 0.0);
    }

    /** Tests that a MapBox zoom function (outputting a color) correctly creates a GeoTools interpolation function. */
    @Test
    public void zoomColorStopsTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        EnvFunction.setGlobalValue("wms_scale_denominator", "1066.364792");

        JSONObject json = MapboxTestUtils.object("{'type':'exponential','stops':[[0,'blue'],[6,'red'],[12, 'lime']]}");
        MBFunction function = new MBFunction(json);

        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        Function fn = (Function) function.color();
        assertNotNull(fn);
        assertEquals("Interpolate", fn.getName());

        Expression zoomLevel = fn.getParameters().get(0);
        Number n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 17
        assertEquals(18.0, n.doubleValue(), .000001);

        Literal stop1 = (Literal) fn.getParameters().get(1);
        assertEquals(Integer.valueOf(0), stop1.evaluate(null, Integer.class));

        Literal color1 = (Literal) fn.getParameters().get(2);
        assertEquals(Color.BLUE, color1.evaluate(null, Color.class));

        Literal stop2 = (Literal) fn.getParameters().get(3);
        assertEquals(Integer.valueOf(6), stop2.evaluate(null, Integer.class));

        Literal color2 = (Literal) fn.getParameters().get(4);
        assertEquals(Color.RED, color2.evaluate(null, Color.class));

        Literal stop3 = (Literal) fn.getParameters().get(5);
        assertEquals(Integer.valueOf(12), stop3.evaluate(null, Integer.class));

        Literal color3 = (Literal) fn.getParameters().get(6);
        assertEquals(Color.GREEN, color3.evaluate(null, Color.class));

        // We are taking care to check that the function method has been supplied
        Literal method = (Literal) fn.getParameters().get(7);
        assertEquals("color", method.evaluate(null, String.class));

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(0, 255, 0), result);
    }

    /**
     * Tests that a MapBox linear zoom function (outputting a color) correctly interpolates color values for zoom levels
     * between stops.
     */
    @Test
    public void zoomColorStopsInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        EnvFunction.setGlobalValue("wms_scale_denominator", "34942641.4969\n");

        JSONObject json = MapboxTestUtils.object("{'type':'exponential', 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}");

        MBFunction function = new MBFunction(json);
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = (Function) function.color();
        Expression zoomLevel = fn.getParameters().get(0);
        Number n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 3
        assertEquals(3.0, n.doubleValue(), .000001);

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(128, 0, 127), result);

        EnvFunction.setGlobalValue("wms_scale_denominator", "4367830.18712");
        n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 6
        assertEquals(6.0, n.doubleValue(), .000001);
        result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(255, 0, 0), result);

        EnvFunction.setGlobalValue("wms_scale_denominator", "545978.773389");
        n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 9
        assertEquals(9.0, n.doubleValue(), .000001);
        result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(127, 128, 0), result);

        EnvFunction.setGlobalValue("wms_scale_denominator", "68247.3466735");
        n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 12
        assertEquals(12.0, n.doubleValue(), .000001);
        result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(0, 255, 0), result);
    }

    /**
     * Tests that a MapBox exponential zoom function (outputting a color) correctly interpolates color values for zoom
     * levels between stops, when the exponential base is > 1.
     */
    @Test
    public void zoomColorStopsExponentialInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "1091957.546779";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(
                1091957.546779,
                ff.function("env", ff.literal("wms_scale_denominator"))
                        .evaluate(null, Number.class)
                        .doubleValue(),
                .00001);
        verifyEnvironmentZoomLevel(8);

        // Create a Mapbox Function
        String jsonStr = "{'type':'exponential', 'base': 1.9, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}";
        JSONObject json = MapboxTestUtils.object(jsonStr);
        MBFunction function = new MBFunction(json);

        // Assert it is an exponential function with the correct base
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.9, function.getBase().doubleValue(), .00001);

        Function fn = (Function) function.color();
        // System.out.println("cql: "+ECQL.toCQL( fn ));
        Color result = fn.evaluate(feature, Color.class);
        // System.out.println("The interpolated color is: " + result);

        assertTrue("Color has no red, but should be interpolated mix of red and green", result.getRed() > 0);
        assertTrue("Color has full green, but should be interpolated mix of red and green", result.getGreen() < 255);

        // We are interpolating at the halfway point, so the linear interpolation would have been
        // (128, 128, 0).
        assertTrue(
                "Exponential interpolation base is > 1, so the interpolated green value should lag behind the linear interpolation.",
                result.getGreen() < 128);
        assertTrue(
                "Exponential interpolation base is > 1, so the interpolated red value should lag behind the linear interpolation.",
                result.getRed() > 128);
    }

    /**
     * Tests that a MapBox exponential zoom function (outputting a color) correctly interpolates color values for zoom
     * levels between stops, when the exponential base is &lt; 1.
     */
    @Test
    public void zoomColorStopsExponentialInterpolationTestBaseLessThan1() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "545978.773389";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(
                545978.773389,
                ff.function("env", ff.literal("wms_scale_denominator"))
                        .evaluate(null, Number.class)
                        .doubleValue(),
                .00001);
        verifyEnvironmentZoomLevel(9);

        // Create a Mapbox Function (used to have a base of 0.1, but that puts it so close
        // to the lime color that it becomes lime due to integer rounding)
        String jsonStr = "{'type':'exponential', 'base': 0.5, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}";
        JSONObject json = MapboxTestUtils.object(jsonStr);
        MBFunction function = new MBFunction(json);

        // Assert it is an exponential function with the correct base
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(0.5, function.getBase().doubleValue(), .00001);

        Function fn = (Function) function.color();
        Color result = fn.evaluate(feature, Color.class);

        assertTrue("Color has no red, but should be interpolated mix of red and green", result.getRed() > 0);
        assertTrue("Color has full green, but should be interpolated mix of red and green", result.getGreen() < 255);

        // We are interpolating at the halfway point, so the linear interpolation would have been
        // (128, 128, 0).
        assertTrue(
                "Exponential interpolation base is < 1, so the interpolated green value should lead the linear interpolation.",
                result.getGreen() > 128);
        assertTrue(
                "Exponential interpolation base is < 1, so the interpolated red value should lead the linear interpolation.",
                result.getRed() < 128);
    }

    /**
     * Tests that a MapBox exponential zoom function (outputting a color) correctly interpolates color values for zoom
     * levels between stops, when the exponential base is == 1 (linear).
     */
    @Test
    public void zoomColorStopsLinearInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "545978.7733895";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(
                545978.7733895,
                ff.function("env", ff.literal("wms_scale_denominator"))
                        .evaluate(null, Number.class)
                        .doubleValue(),
                .00001);
        verifyEnvironmentZoomLevel(9);

        // Create a Mapbox Function
        String jsonStr = "{'type':'exponential', 'base': 1.0, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}";
        JSONObject json = MapboxTestUtils.object(jsonStr);
        MBFunction function = new MBFunction(json);

        // Assert it is an exponential function with the correct base
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.0, function.getBase().doubleValue(), .00001);

        Function fn = (Function) function.color();
        Color result = fn.evaluate(feature, Color.class);

        assertTrue("Color has no red, but should be interpolated mix of red and green", result.getRed() > 0);
        assertTrue("Color has full green, but should be interpolated mix of red and green", result.getGreen() < 255);

        // We are interpolating at the halfway point, so the linear interpolation would have been
        // (128, 128, 0).
        assertEquals(
                "Exponential interpolation base is = 1, so the interpolated green value should equal the linear interpolation.",
                128,
                result.getGreen());
        assertEquals(
                "Exponential interpolation base is = 1, so the interpolated red value should equal the linear interpolation.",
                127,
                result.getRed());
    }

    /** Test an {@link MBFunction} (type = interval) that returns a color value. */
    @Test
    public void colorIntervalFunctionTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",numbervalue,location=4326");
        java.util.function.Function<Long, SimpleFeature> features =
                value -> DataUtilities.createFeature(SAMPLE, "measure1=A|" + value + "|POINT(0,0)");

        String jsonStr =
                "{'property': 'numbervalue', 'type': 'interval', 'default': '#0F0F0F', 'stops': [[-1000, '#000000'], [-30, '#00FF00'], [0, '#0000FF'], [100, '#FFFFFF']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"interval\"", MBFunction.FunctionType.INTERVAL, function.getType());

        Expression outputExpression = function.color();

        // Before the first stop is undefined (return default value)
        assertThat(outputExpression, evaluatesTo(features.apply(-10000L), Color.class, equalTo(new Color(0x0F0F0F))));

        // Test each interval
        assertThat(outputExpression, evaluatesTo(features.apply(-900L), Color.class, equalTo(Color.BLACK)));
        assertThat(outputExpression, evaluatesTo(features.apply(-20L), Color.class, equalTo(Color.GREEN)));
        assertThat(outputExpression, evaluatesTo(features.apply(10L), Color.class, equalTo(Color.BLUE)));
        assertThat(outputExpression, evaluatesTo(features.apply(500L), Color.class, equalTo(Color.WHITE)));

        // Test at stops
        assertThat(outputExpression, evaluatesTo(features.apply(-1000L), Color.class, equalTo(Color.BLACK)));
        assertThat(outputExpression, evaluatesTo(features.apply(-30L), Color.class, equalTo(Color.GREEN)));
        assertThat(outputExpression, evaluatesTo(features.apply(0L), Color.class, equalTo(Color.BLUE)));
        assertThat(outputExpression, evaluatesTo(features.apply(100L), Color.class, equalTo(Color.WHITE)));
    }

    /** Test an {@link MBFunction} (type = interval) that returns a color value. */
    @Test
    public void enumIntervalFunctionTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",numbervalue,location=4326");
        java.util.function.Function<Long, SimpleFeature> features =
                value -> DataUtilities.createFeature(SAMPLE, "measure1=A|" + value + "|POINT(0,0)");

        String jsonStr =
                "{'property': 'numbervalue', 'type': 'interval', 'default': 1, 'stops': [[-1000, 'INTERVAL'], [-30, 'CATEGORICAL'], [0, 'EXPONENTIAL'], [100, 'IDENTITY']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"interval\"", MBFunction.FunctionType.INTERVAL, function.getType());

        Expression outputExpression = function.enumeration(MBFunction.FunctionType.class);

        // Before the first stop is undefined
        assertThat(outputExpression, evaluatesTo(features.apply(-10000L), String.class, any(String.class)));

        // Test each interval
        assertThat(outputExpression, evaluatesTo(features.apply(-900L), String.class, equalTo("interval")));
        assertThat(outputExpression, evaluatesTo(features.apply(-20L), String.class, equalTo("categorical")));
        assertThat(outputExpression, evaluatesTo(features.apply(10L), String.class, equalTo("exponential")));
        assertThat(outputExpression, evaluatesTo(features.apply(500L), String.class, equalTo("identity")));

        // Test at stops
        assertThat(outputExpression, evaluatesTo(features.apply(-1000L), String.class, equalTo("interval")));
        assertThat(outputExpression, evaluatesTo(features.apply(-30L), String.class, equalTo("categorical")));
        assertThat(outputExpression, evaluatesTo(features.apply(0L), String.class, equalTo("exponential")));
        assertThat(outputExpression, evaluatesTo(features.apply(100L), String.class, equalTo("identity")));
    }

    @Test
    public void stringIntervalFunctionTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",numbervalue,location=4326");
        java.util.function.Function<Long, SimpleFeature> features =
                value -> DataUtilities.createFeature(SAMPLE, "measure1=A|" + value + "|POINT(0,0)");

        String jsonStr =
                "{'property': 'numbervalue', 'type': 'interval', 'default': 1, 'stops': [[-1000, 'foo'], [-30, 'bar'], [0, 'baz'], [100, 'quux']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"interval\"", MBFunction.FunctionType.INTERVAL, function.getType());

        Expression outputExpression = function.function(String.class);

        // Before the first stop is undefined
        assertThat(outputExpression, evaluatesTo(features.apply(-10000L), String.class, any(String.class)));

        // Test each interval
        assertThat(outputExpression, evaluatesTo(features.apply(-900L), String.class, equalTo("foo")));
        assertThat(outputExpression, evaluatesTo(features.apply(-20L), String.class, equalTo("bar")));
        assertThat(outputExpression, evaluatesTo(features.apply(10L), String.class, equalTo("baz")));
        assertThat(outputExpression, evaluatesTo(features.apply(500L), String.class, equalTo("quux")));

        // Test at stops
        assertThat(outputExpression, evaluatesTo(features.apply(-1000L), String.class, equalTo("foo")));
        assertThat(outputExpression, evaluatesTo(features.apply(-30L), String.class, equalTo("bar")));
        assertThat(outputExpression, evaluatesTo(features.apply(0L), String.class, equalTo("baz")));
        assertThat(outputExpression, evaluatesTo(features.apply(100L), String.class, equalTo("quux")));
    }

    /**
     * Tests that a MapBox exponential zoom function (outputting a numeric value) correctly interpolates values for zoom
     * levels between stops. Tests exponential base == 1 and != 1.
     */
    @Test
    public void zoomStopsNumericInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "545978.773389";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(
                545978.773389,
                ff.function("env", ff.literal("wms_scale_denominator"))
                        .evaluate(null, Number.class)
                        .doubleValue(),
                .00001);
        verifyEnvironmentZoomLevel(9);

        // -------- Test interpolation for base > 1
        String jsonStr = "{'type':'exponential', 'base': 1.9, 'stops':[[0,12],[6,24],[12,48]]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));

        // Assert it is an exponential function with the correct base
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.9, function.getBase().doubleValue(), .00001);

        Function fn = (Function) function.numeric();
        Number result = fn.evaluate(feature, Number.class);

        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);
        assertTrue("Interpolated value should be below midpoint (for base > 1)", result.doubleValue() < 36);

        // -------- Test interpolation for base < 1
        jsonStr = "{'type':'exponential', 'base': 0.1, 'stops':[[0,12],[6,24],[12,48]]}";
        function = new MBFunction(MapboxTestUtils.object(jsonStr));

        // Assert it is an exponential function with the correct base
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(0.1, function.getBase().doubleValue(), .00001);

        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);

        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);
        assertTrue("Interpolated value should be above midpoint (for base < 1)", result.doubleValue() > 36);

        // -------- Test interpolation for base = 1
        jsonStr = "{'type':'exponential', 'base': 1.0, 'stops':[[0,12],[6,24],[12,48]]}";
        function = new MBFunction(MapboxTestUtils.object(jsonStr));

        // Assert it is an exponential function with the correct base
        assertEquals("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.0, function.getBase().doubleValue(), .00001);

        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);

        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);
        assertEquals("Interpolated value should = midpoint (for base = 1)", result.doubleValue(), 36.0, .0001);
    }

    /**
     * Tests that a MapBox exponential property function (outputting a numeric value) correctly interpolates values for
     * zoom levels between stops. Tests exponential base == 1 and != 1.
     */
    @Test
    public void propertyStopsNumericInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // -------- Test interpolation for base > 1
        String jsonStr =
                "{'type':'exponential', 'base': 1.9, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));

        // Assert it is an exponential function with the correct base
        assertEquals("Is a property function", EnumSet.of(MBFunction.FunctionCategory.PROPERTY), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.9, function.getBase().doubleValue(), .00001);

        Function fn = (Function) function.numeric();
        Number result = fn.evaluate(feature, Number.class);

        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);
        assertTrue("Interpolated value should be below midpoint (for base > 1)", result.doubleValue() < 36);

        // -------- Test interpolation for base < 1
        jsonStr = "{'type':'exponential', 'base': 0.1, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]]}";
        function = new MBFunction(MapboxTestUtils.object(jsonStr));

        // Assert it is an exponential function with the correct base
        assertEquals("Is a property function", EnumSet.of(MBFunction.FunctionCategory.PROPERTY), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(0.1, function.getBase().doubleValue(), .00001);

        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);

        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);
        assertTrue("Interpolated value should be above midpoint (for base < 1)", result.doubleValue() > 36);

        // -------- Test interpolation for base = 1
        jsonStr = "{'type':'exponential', 'base': 1.0, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]]}";
        function = new MBFunction(MapboxTestUtils.object(jsonStr));

        // Assert it is an exponential function with the correct base
        assertEquals("Is a property function", EnumSet.of(MBFunction.FunctionCategory.PROPERTY), function.category());
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.0, function.getBase().doubleValue(), .00001);

        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);

        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);
        assertEquals("Interpolated value should = midpoint (for base = 1)", result.doubleValue(), 36.0, .0001);
    }

    /**
     * Assert that the numeric exponential function correctly returns the default value when the property input is not a
     * valid number.
     */
    @Test
    public void numericExponentialDefaultValueTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|NOT_A_VALID_NUMBER|POINT(0,0)");

        String jsonStr =
                "{'type':'exponential', 'base': 1.9, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]], 'default':42}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));

        Expression fn = function.numeric();
        Number result = fn.evaluate(feature, Number.class);
        assertEquals(42, result.intValue());
    }

    /** Test an MBFunction (type = Identity) that returns a numeric value for a given property. */
    @Test
    public void numericIdentityFunctionTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // Verify the function was created correctly
        String jsonStr = "{'property':'temperature', 'type':'identity', 'default':-1}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"Identity\"", MBFunction.FunctionType.IDENTITY, function.getType());

        // Verify that the output is correct (== input)
        Expression outputExpression = function.numeric();
        Number result = outputExpression.evaluate(feature, Number.class);

        assertEquals(
                "Numeric identity function output is == input property value", 50.0, result.doubleValue(), .000001);

        // Check default (for a feature missing the property)
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A||POINT(0,0)");
        result = outputExpression.evaluate(feature, Number.class);
        assertEquals(-1, result.intValue());
    }

    /** Test an {@link MBFunction} (type = categorical) that returns a numeric value. */
    @Test
    public void numericCategoricalFunctionTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",roadtype,location=4326");

        String jsonStr =
                "{'property': 'roadtype', 'type': 'categorical', 'default': -1, 'stops': [['trail', 1], ['dirtroad', 2], ['road', 3], ['highway', 4]]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"categorical\"", MBFunction.FunctionType.CATEGORICAL, function.getType());

        Expression outputExpression = function.numeric();

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|dirtroad|POINT(0,0)");
        Number result = outputExpression.evaluate(feature, Number.class);
        assertEquals(2, result.intValue());

        // Check default
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|other|POINT(0,0)");
        result = outputExpression.evaluate(feature, Number.class);
        assertEquals(-1, result.intValue());
    }

    /** Test an {@link MBFunction} (type = interval) that returns a numeric value. */
    @Test
    public void numericIntervalFunctionTest() throws Exception {

        String jsonStr =
                "{'property': 'temperature', 'default':0, 'type': 'interval','stops': [[0, 2],[100, 4],[1000, 6]]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertThat(function, categories(containsInAnyOrder(MBFunction.FunctionCategory.PROPERTY)));
        assertThat(function, hasProperty("type", is(MBFunction.FunctionType.INTERVAL)));

        Expression outputExpression = function.numeric();

        // Test each interval
        final SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature,location=4326");
        java.util.function.Function<Long, SimpleFeature> features =
                temp -> DataUtilities.createFeature(SAMPLE, "measure1=A|" + temp + "|POINT(0,0)");

        // Bellow the first stop is undefined (return default value)
        assertThat(outputExpression, evaluatesTo(features.apply(-1L), Number.class, equalInt(0)));

        assertThat(outputExpression, evaluatesTo(features.apply(0L), Number.class, equalInt(2)));
        assertThat(outputExpression, evaluatesTo(features.apply(20L), Number.class, equalInt(2)));

        assertThat(outputExpression, evaluatesTo(features.apply(100L), Number.class, equalInt(4)));
        assertThat(outputExpression, evaluatesTo(features.apply(200L), Number.class, equalInt(4)));

        assertThat(outputExpression, evaluatesTo(features.apply(1000L), Number.class, equalInt(6)));
        assertThat(outputExpression, evaluatesTo(features.apply(2000L), Number.class, equalInt(6)));
    }

    /** Test a {@link MBFunction} (type = identity) that returns a string value. */
    @Test
    public void stringIdentityFunctionTest() throws Exception {
        String jsonStr = "{'property': 'textproperty','type': 'identity', 'default':'defaultText'}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"identity\"", MBFunction.FunctionType.IDENTITY, function.getType());

        Expression outputExpression = function.function(String.class);

        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",textproperty,location=4326");

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|textvalue|POINT(0,0)");
        String output = outputExpression.evaluate(feature, String.class);
        assertEquals("textvalue", output);

        // Check default (for a feature that's missing the function's property)
        SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",location=4326");
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|POINT(0,0)");
        output = outputExpression.evaluate(feature, String.class);
        assertEquals("defaultText", output);
    }

    /** Test a {@link MBFunction} (type = identity) that returns an enum value. */
    @Test
    public void enumIdentityFunctionTest() throws Exception {
        String jsonStr = "{'property': 'linecap','type': 'identity', 'default':'round'}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"identity\"", MBFunction.FunctionType.IDENTITY, function.getType());

        Expression outputExpression = function.function(LineJoin.class);

        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",linecap,location=4326");

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|square|POINT(0,0)");
        String output = outputExpression.evaluate(feature, String.class);
        assertEquals("square", output);

        // Check default (for a feature that's missing the function's property)
        SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",location=4326");
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|POINT(0,0)");
        output = outputExpression.evaluate(feature, String.class);
        assertEquals("round", output);
    }

    /** Test a {@link MBFunction} (type = identity) that returns a boolean value. */
    @Test
    public void booleanIdentityFunctionTest() throws Exception {
        String jsonStr = "{'property': 'propName','type': 'identity', 'default':'false'}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"identity\"", MBFunction.FunctionType.IDENTITY, function.getType());

        Expression outputExpression = function.function(Boolean.class);

        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",propName,location=4326");

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|true|POINT(0,0)");
        Boolean output = outputExpression.evaluate(feature, Boolean.class);
        assertTrue(output);

        // Check default
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A||POINT(0,0)");
        output = outputExpression.evaluate(feature, Boolean.class);
        assertFalse(output);
    }

    @Test
    public void stringCategoricalFunctionTest() throws Exception {
        String jsonStr =
                "{'property': 'roadtype', 'type': 'categorical', 'default': 'defaultStr', 'stops': [['trail', 'string1'], ['dirtroad', 'string2'], ['road', 'string3'], ['highway', 'string4']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"categorical\"", MBFunction.FunctionType.CATEGORICAL, function.getType());

        Expression outputExpression = function.function(String.class);

        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",roadtype,location=4326");

        // Test for each category
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|trail|POINT(0,0)");
        String result = outputExpression.evaluate(feature, String.class);
        assertEquals("string1", result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|dirtroad|POINT(0,0)");
        result = outputExpression.evaluate(feature, String.class);
        assertEquals("string2", result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|road|POINT(0,0)");
        result = outputExpression.evaluate(feature, String.class);
        assertEquals("string3", result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|highway|POINT(0,0)");
        result = outputExpression.evaluate(feature, String.class);
        assertEquals("string4", result);

        // Check default
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|other|POINT(0,0)");
        result = outputExpression.evaluate(feature, String.class);
        assertEquals("defaultStr", result);
    }

    @Test
    public void booleanIntervalFunctionTest() throws Exception {
        String jsonStr =
                "{'property': 'temperature', 'default':'false', 'type': 'interval','stops': [[-1000, 'true'],[0, 'false'],[1000, 'true']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"interval\"", MBFunction.FunctionType.INTERVAL, function.getType());

        Expression outputExpression = function.function(Boolean.class);

        // Test each interval
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",temperature,location=4326");

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|-500|POINT(0,0)");
        Boolean result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(true, result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|0|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(false, result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|500|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(false, result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|1000|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(true, result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|9999|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(true, result);

        // Below lowest stop, the default should be returned.
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|-9999|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(false, result);
    }

    @Test
    public void booleanCategoricalFunctionTest() throws Exception {
        String jsonStr =
                "{'property': 'roadtype', 'type': 'categorical', 'default': 'false', 'stops': [['trail', 'true'], ['dirtroad', 'false'], ['road', 'true']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());
        assertEquals("Function type is \"categorical\"", MBFunction.FunctionType.CATEGORICAL, function.getType());

        Expression outputExpression = function.function(Boolean.class);

        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE", "id:\"\",roadtype,location=4326");

        // Test for each category
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|trail|POINT(0,0)");
        Boolean result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(true, result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|dirtroad|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(false, result);

        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|road|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(true, result);

        // Check default
        feature = DataUtilities.createFeature(SAMPLE, "measure1=A|other|POINT(0,0)");
        result = outputExpression.evaluate(feature, Boolean.class);
        assertEquals(false, result);
    }

    /**
     * Assert that {@link MBFunction} uses the correct default function type when the requested return class is String.
     */
    @Test
    public void stringDefaultFunctionTest() throws Exception {
        String jsonStr =
                "{'property': 'temperature',  'stops': [[0, 'string1'], [100, 'string2'], [200, 'string3'], [300, 'string4']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());

        assertEquals(
                "The default function type for String returns should be interval",
                MBFunction.FunctionType.INTERVAL,
                function.getTypeWithDefault(String.class));
    }

    /**
     * Assert that {@link MBFunction} uses the correct default function type when the requested return class is Boolean.
     */
    @Test
    public void booleanDefaultFunctionTest() throws Exception {
        String jsonStr =
                "{'property': 'temperature',  'stops': [[0, 'true'], [100, 'false'], [200, 'true'], [300, 'false']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "Function category is \"property\"",
                EnumSet.of(MBFunction.FunctionCategory.PROPERTY),
                function.category());

        assertEquals(
                "The default function type for Boolean returns should be interval",
                MBFunction.FunctionType.INTERVAL,
                function.getTypeWithDefault(Boolean.class));
    }

    /**
     * Assert that {@link MBFunction} uses the correct default function type when the requested return class is Number.
     */
    @Test
    public void numericDefaultFunctionTest() throws Exception {
        String jsonStr = "{'property': 'temperature','stops': [[0, 2],[100, 4],[1000, 6]]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "The default function type for Number returns should be exponential",
                FunctionType.EXPONENTIAL,
                function.getTypeWithDefault(Number.class));
    }

    /**
     * Assert that {@link MBFunction} uses the correct default function type when the requested return class is Enum.
     */
    @Test
    public void enumDefaultFunctionTest() throws Exception {
        String jsonStr =
                "{'property': 'temperature','stops': [[0, 'ENUM_VAL1'],[100, 'ENUM_VAL2'],[1000, 'ENUM_VAL3']]}";
        MBFunction function = new MBFunction(MapboxTestUtils.object(jsonStr));
        assertEquals(
                "The default function type for Enums returns should be interval",
                FunctionType.INTERVAL,
                function.getTypeWithDefault(Enumeration.class));
    }

    /**
     * Assert that {@link MBFunction} uses the correct default function type when the requested return class is Color.
     */
    @Test
    public void colorDefaultFunctionTest() throws Exception {
        JSONObject json = MapboxTestUtils.object("{'property':'temperature','stops':[[0,'blue'],[100,'red']]}");
        MBFunction function = new MBFunction(json);

        assertEquals(
                "The default function type for Color returns should be exponential",
                FunctionType.EXPONENTIAL,
                function.getTypeWithDefault(Color.class));
    }

    public void verifyEnvironmentZoomLevel(int zoomLevel) {
        Number envZoomLevel = ff.function(
                        "zoomLevel", ff.function("env", ff.literal("wms_scale_denominator")), ff.literal("EPSG:3857"))
                .evaluate(null, Number.class);
        assertEquals("Zoom level is " + zoomLevel, zoomLevel, envZoomLevel.doubleValue(), .00001);
    }

    /**
     * Test splitting an array function into an array of functions -- one for each dimension in the array. (Exponential
     * function).
     */
    @Test
    public void testArrayFunction() throws Exception {
        JSONObject json = MapboxTestUtils.object(
                "{'property':'temperature','type':'exponential', 'base':1.5, 'stops':[ [0,[0,5]], [100,[2,10]] ]}");
        MBFunction function = new MBFunction(json);

        SimpleFeatureType SAMPLE = DataUtilities.createType(
                "SAMPLE", "id:\"\",temperature:0.0,location=4326,color:java.awt.Color,text:String:");
        SimpleFeature feature1 = DataUtilities.createFeature(SAMPLE, "measure1=A|0|POINT(0,0)|#FF0000|red");
        SimpleFeature feature2 = DataUtilities.createFeature(SAMPLE, "measure1=A|100|POINT(0,0)|#FF0000|red");

        List<MBFunction> splitFunctions = function.splitArrayFunction();
        assertEquals(2, splitFunctions.size());

        MBFunction xFn = splitFunctions.get(0);
        MBFunction yFn = splitFunctions.get(1);

        assertEquals(0, xFn.numeric().evaluate(feature1, Integer.class).intValue());
        assertEquals(5, yFn.numeric().evaluate(feature1, Integer.class).intValue());

        assertEquals(2, xFn.numeric().evaluate(feature2, Integer.class).intValue());
        assertEquals(10, yFn.numeric().evaluate(feature2, Integer.class).intValue());
    }

    /**
     * Test splitting an array function into an array of functions -- one for each dimension in the array. (Categorical
     * function, with default).
     */
    @Test
    public void testArrayFunctionCategoricalWithDefault() throws Exception {
        JSONObject json = MapboxTestUtils.object(
                "{'property':'character','type':'categorical', 'base':1.5, 'default': [-1,-2], 'stops':[ ['a',[0,5]], ['b',[2,10]] ]}");
        MBFunction function = new MBFunction(json);

        SimpleFeatureType SAMPLE = DataUtilities.createType(
                "SAMPLE", "id:\"\",temperature:0.0,location=4326,color:java.awt.Color,character:String:");
        SimpleFeature featurea = DataUtilities.createFeature(SAMPLE, "measure1=A|0|POINT(0,0)|#FF0000|a");
        SimpleFeature featureb = DataUtilities.createFeature(SAMPLE, "measure1=A|100|POINT(0,0)|#FF0000|b");
        SimpleFeature featuredefault = DataUtilities.createFeature(SAMPLE, "measure1=A|100|POINT(0,0)|#FF0000|default");

        List<MBFunction> splitFunctions = function.splitArrayFunction();
        assertEquals(2, splitFunctions.size());

        MBFunction xFn = splitFunctions.get(0);
        MBFunction yFn = splitFunctions.get(1);

        assertEquals(0, xFn.numeric().evaluate(featurea, Integer.class).intValue());
        assertEquals(5, yFn.numeric().evaluate(featurea, Integer.class).intValue());

        assertEquals(2, xFn.numeric().evaluate(featureb, Integer.class).intValue());
        assertEquals(10, yFn.numeric().evaluate(featureb, Integer.class).intValue());

        assertEquals(-1, xFn.numeric().evaluate(featuredefault, Integer.class).intValue());
        assertEquals(-2, yFn.numeric().evaluate(featuredefault, Integer.class).intValue());
    }

    /** Test that nothing breaks when an array function has output arrays of size one. */
    @Test
    public void testArrayFunctionSize1() throws Exception {
        JSONObject json = MapboxTestUtils.object(
                "{'property':'temperature','type':'exponential', 'base':1.5, 'stops':[ [0,[0]], [100,[2]] ]}");
        MBFunction function = new MBFunction(json);

        List<MBFunction> splitFunctions = function.splitArrayFunction();
        assertEquals(1, splitFunctions.size());

        SimpleFeatureType SAMPLE = DataUtilities.createType(
                "SAMPLE", "id:\"\",temperature:0.0,location=4326,color:java.awt.Color,text:String:");
        SimpleFeature feature1 = DataUtilities.createFeature(SAMPLE, "measure1=A|0|POINT(0,0)|#FF0000|red");
        SimpleFeature feature2 = DataUtilities.createFeature(SAMPLE, "measure1=A|100|POINT(0,0)|#FF0000|red");

        assertEquals(
                0,
                splitFunctions
                        .get(0)
                        .numeric()
                        .evaluate(feature1, Integer.class)
                        .intValue());
        assertEquals(
                2,
                splitFunctions
                        .get(0)
                        .numeric()
                        .evaluate(feature2, Integer.class)
                        .intValue());
    }
}
