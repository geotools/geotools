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
import java.util.EnumSet;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.mbstyle.parse.MBFunction;
import org.geotools.mbstyle.parse.MBFunction.FunctionType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Try out property and zoom function.
 * 
 * See example https://www.mapbox.com/help/gl-dds-ref/ online.
 */
public class MBFunctionTest {

    public static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /**
     * Parse JSONObject using ' rather than " for faster test case writing.
     * @param json
     * @return parsed JSONArray
     * @throws ParseException
     */
    private JSONObject object(String json) throws ParseException{
        JSONParser parser = new JSONParser();
        String text = json.replace('\'', '\"');
        Object object = parser.parse(text);
        return (JSONObject) object;
    }
    @Test
    public void identity() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326,color:java.awt.Color");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)|#FF0000");

        JSONObject json = object("{'property':'color','type':'identity'}");
        MBFunction function = new MBFunction(json);
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertTrue("identity", function.getType() == FunctionType.IDENITY );
        
        Expression fn = function.color();
        Color result = fn.evaluate(feature, Color.class);
        assertEquals(Color.RED, result);
    }
    
    @Test
    public void propertyColorStops() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        JSONObject json = object("{'property':'temperature','stops':[[0,'blue'],[100,'red']]}");
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
        assertEquals(new Integer(0), stop1.evaluate(null, Integer.class));

        Literal color1 = (Literal) fn.getParameters().get(2);
        assertEquals(Color.BLUE, color1.evaluate(null, Color.class));

        Literal stop2 = (Literal) fn.getParameters().get(3);
        assertEquals(new Integer(100), stop2.evaluate(null, Integer.class));

        Literal color2 = (Literal) fn.getParameters().get(4);
        assertEquals(Color.RED, color2.evaluate(null, Color.class));

        // We are taking care to check that the function method has been supplied
        Literal method = (Literal) fn.getParameters().get(5);
        assertEquals("color", method.evaluate(null, String.class));

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(128, 0, 128), result);
    }

    @Test
    public void propertyColorStopsBase() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        JSONObject json = object("{'property':'temperature','stops':[[0,'blue'],[100,'red']],'base':1.1}");
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
        assertEquals(new Double(1.1), base.evaluate(null, Double.class));

        Literal stop1 = (Literal) fn.getParameters().get(2);
        assertEquals(new Integer(0), stop1.evaluate(null, Integer.class));

        Literal color1 = (Literal) fn.getParameters().get(3);
        assertEquals(Color.BLUE, color1.evaluate(null, Color.class));

        Literal stop2 = (Literal) fn.getParameters().get(4);
        assertEquals(new Integer(100), stop2.evaluate(null, Integer.class));

        Literal color2 = (Literal) fn.getParameters().get(5);
        assertEquals(Color.RED, color2.evaluate(null, Color.class));

        Color result = fn.evaluate(feature, Color.class);
        
        // When the base is greater than 1, more growth should be at the end of the range.
        // So the interpolation on the halfway input value should be less than the halfway output value, which would be (128, 0 , 128).        
        assertEquals(0, result.getGreen());
        assertTrue(result.getBlue() > 128);
        assertTrue(result.getRed() < 128);
        
    }
    
    @Test
    public void propertyValueStops() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        JSONObject json = object("{'property':'temperature','stops':[[0,5],[100,10]]}");

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
    
    /**
     * Tests that a MapBox zoom function (outputting a color) correctly creates a GeoTools interpolation function. 
     * @throws Exception
     */
    @Test
    public void zoomColorStopsTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");
        
        EnvFunction.setGlobalValue("wms_scale_denominator", "2132.729584");

        JSONObject json = object("{'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}");
        MBFunction function = new MBFunction(json);

        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        Function fn = (Function) function.color();
        assertNotNull(fn);        
        assertEquals("Interpolate", fn.getName());
        
        Expression zoomLevel = fn.getParameters().get(0);
        Number n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 18
        assertEquals(18.0, n.doubleValue(), .000001);        

        Literal stop1 = (Literal) fn.getParameters().get(1);
        assertEquals(new Integer(0), stop1.evaluate(null, Integer.class));

        Literal color1 = (Literal) fn.getParameters().get(2);
        assertEquals(Color.BLUE, color1.evaluate(null, Color.class));

        Literal stop2 = (Literal) fn.getParameters().get(3);
        assertEquals(new Integer(6), stop2.evaluate(null, Integer.class));

        Literal color2 = (Literal) fn.getParameters().get(4);
        assertEquals(Color.RED, color2.evaluate(null, Color.class));
        
        Literal stop3 = (Literal) fn.getParameters().get(5);
        assertEquals(new Integer(12), stop3.evaluate(null, Integer.class));

        Literal color3 = (Literal) fn.getParameters().get(6);
        assertEquals(Color.GREEN, color3.evaluate(null, Color.class));

        // We are taking care to check that the function method has been supplied
        Literal method = (Literal) fn.getParameters().get(7);
        assertEquals("color", method.evaluate(null, String.class));

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(0, 255, 0), result);
    }
    
    /**
     * Tests that a MapBox linear zoom function (outputting a color) correctly interpolates color values
     * for zoom levels between stops.
     * 
     * @throws Exception
     */
    @Test
    public void zoomColorStopsInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");
        
        EnvFunction.setGlobalValue("wms_scale_denominator", "69885282.993862");

        JSONObject json = object("{'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}");
        
        MBFunction function = new MBFunction(json);
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
                
        Function fn = (Function) function.color();
        Expression zoomLevel = fn.getParameters().get(0);
        Number n = zoomLevel.evaluate(null, Number.class);   
        // This should be zoom level 3
        assertEquals(3.0, n.doubleValue(), .000001);        

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(127, 0, 128), result);
        
        
        EnvFunction.setGlobalValue("wms_scale_denominator", "8735660.374233");
        n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 6
        assertEquals(6.0, n.doubleValue(), .000001);                
        result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(255, 0, 0), result);
        
        
        EnvFunction.setGlobalValue("wms_scale_denominator", "1091957.546779");
        n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 9
        assertEquals(9.0, n.doubleValue(), .000001);                
        result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(127, 128, 0), result);
        
        
        EnvFunction.setGlobalValue("wms_scale_denominator", "136494.693347");
        n = zoomLevel.evaluate(null, Number.class);
        // This should be zoom level 12
        assertEquals(12.0, n.doubleValue(), .000001);                
        result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(0, 255, 0), result);        
    }
    
    /**
     * Tests that a MapBox exponential zoom function (outputting a color) correctly interpolates color values
     * for zoom levels between stops, when the exponential base is > 1.
     * 
     * @throws Exception
     */
    @Test
    public void zoomColorStopsExponentialInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");
        
        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "1091957.546779";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(1091957.546779, ff.function("env", ff.literal("wms_scale_denominator"))
                .evaluate(null, Number.class).doubleValue(), .00001);
        verifyEnvironmentZoomLevel(9);

        // Create a Mapbox Function
        String jsonStr = "{'base': 1.9, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}";
        JSONObject json = object(jsonStr);        
        MBFunction function = new MBFunction(json);
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.9, function.getBase().doubleValue(), .00001);
        
        Function fn = (Function) function.color();
        //System.out.println("cql: "+ECQL.toCQL( fn ));
        Color result = fn.evaluate(feature, Color.class);
        //System.out.println("The interpolated color is: " + result);
        
        Expression zoomLevel = fn.getParameters().get(0);        
        Number n = zoomLevel.evaluate(null, Number.class);   
        //System.out.println("(the function's interpolate value was " + n + ")");
                
        assertTrue("Color has no red, but should be interpolated mix of red and green", result.getRed() > 0);
        assertTrue("Color has full green, but should be interpolated mix of red and green", result.getGreen() < 255);
        
        // We are interpolating at the halfway point, so the linear interpolation would have been (128, 128, 0).        
        assertTrue("Exponential interpolation base is > 1, so the interpolated green value should lag behind the linear interpolation.", result.getGreen() < 128);
        assertTrue("Exponential interpolation base is > 1, so the interpolated red value should lag behind the linear interpolation.", result.getRed() > 128);   

    }
    
    /**
     * Tests that a MapBox exponential zoom function (outputting a color) correctly interpolates color values
     * for zoom levels between stops, when the exponential base is < 1.
     * 
     * @throws Exception
     */
    @Test
    public void zoomColorStopsExponentialInterpolationTestBaseLessThan1() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");
        
        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "1091957.546779";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(1091957.546779, ff.function("env", ff.literal("wms_scale_denominator"))
                .evaluate(null, Number.class).doubleValue(), .00001);
        verifyEnvironmentZoomLevel(9);

        // Create a Mapbox Function
        String jsonStr = "{'base': 0.1, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}";
        JSONObject json = object(jsonStr);        
        MBFunction function = new MBFunction(json);
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(0.1, function.getBase().doubleValue(), .00001);

        Function fn = (Function) function.color();
        Color result = fn.evaluate(feature, Color.class);
        
        Expression zoomLevel = fn.getParameters().get(0);        
        Number n = zoomLevel.evaluate(null, Number.class);
                
        assertTrue("Color has no red, but should be interpolated mix of red and green", result.getRed() > 0);
        assertTrue("Color has full green, but should be interpolated mix of red and green", result.getGreen() < 255);
        
        // We are interpolating at the halfway point, so the linear interpolation would have been (128, 128, 0).        
        assertTrue("Exponential interpolation base is < 1, so the interpolated green value should lead the linear interpolation.", result.getGreen() > 128);
        assertTrue("Exponential interpolation base is < 1, so the interpolated red value should lead the linear interpolation.", result.getRed() < 128);   

    }
    
    /**
     * Tests that a MapBox exponential zoom function (outputting a color) correctly interpolates color values
     * for zoom levels between stops, when the exponential base is == 1 (linear).
     * 
     * @throws Exception
     */
    @Test
    public void zoomColorStopsLinearInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");
        
        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "1091957.546779";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(1091957.546779, ff.function("env", ff.literal("wms_scale_denominator"))
                .evaluate(null, Number.class).doubleValue(), .00001);
        verifyEnvironmentZoomLevel(9);

        // Create a Mapbox Function
        String jsonStr = "{'base': 1.0, 'stops':[[0,'blue'],[6,'red'],[12, 'lime']]}";
        JSONObject json = object(jsonStr);        
        MBFunction function = new MBFunction(json);
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.0, function.getBase().doubleValue(), .00001);
        
        Function fn = (Function) function.color();
        Color result = fn.evaluate(feature, Color.class);
        
        Expression zoomLevel = fn.getParameters().get(0);        
        Number n = zoomLevel.evaluate(null, Number.class);
                
        assertTrue("Color has no red, but should be interpolated mix of red and green", result.getRed() > 0);
        assertTrue("Color has full green, but should be interpolated mix of red and green", result.getGreen() < 255);
        
        // We are interpolating at the halfway point, so the linear interpolation would have been (128, 128, 0).        
        assertEquals("Exponential interpolation base is = 1, so the interpolated green value should equal the linear interpolation.", 128, result.getGreen());
        assertEquals("Exponential interpolation base is = 1, so the interpolated red value should equal the linear interpolation.", 127, result.getRed());   

    }
    
    /**
     * Tests that a MapBox exponential zoom function (outputting a numeric value) correctly interpolates values
     * for zoom levels between stops.
     * 
     * @throws Exception
     */
    @Test
    public void zoomStopsNumericInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");
        
        // Set scale denominator to the equivalent of zoomLevel 9
        String scaleDenomForZoom9 = "1091957.546779";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenomForZoom9);

        // Verify environment has expected scale denominator (and zoom level)
        assertEquals(1091957.546779, ff.function("env", ff.literal("wms_scale_denominator"))
                .evaluate(null, Number.class).doubleValue(), .00001);
        verifyEnvironmentZoomLevel(9);

        // -------- Test interpolation for base > 1
        // Create a Mapbox Function for base > 1
        String jsonStr = "{'base': 1.9, 'stops':[[0,12],[6,24],[12,48]]}";
        MBFunction function = new MBFunction(object(jsonStr));
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.9, function.getBase().doubleValue(), .00001);
        
        Function fn = (Function) function.numeric();
        Number result = fn.evaluate(feature, Number.class);       
           
        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);    
        assertTrue("Interpolated value should be below midpoint (for base > 1)", result.doubleValue() < 36);
        
        // -------- Test interpolation for base < 1
        jsonStr = "{'base': 0.1, 'stops':[[0,12],[6,24],[12,48]]}";
        function = new MBFunction(object(jsonStr));
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(0.1, function.getBase().doubleValue(), .00001);
        
        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);       
        
        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);    
        assertTrue("Interpolated value should be above midpoint (for base < 1)", result.doubleValue() > 36);
        
        // -------- Test interpolation for base = 1        
        jsonStr = "{'base': 1.0, 'stops':[[0,12],[6,24],[12,48]]}";
        function = new MBFunction(object(jsonStr));
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a zoom function", EnumSet.of(MBFunction.FunctionCategory.ZOOM).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.0, function.getBase().doubleValue(), .00001);
        
        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);       
        
        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);    
        assertEquals("Interpolated value should = midpoint (for base = 1)", result.doubleValue(), 36.0, .0001);
    }
    
    
    /**
     * Tests that a MapBox exponential property function (outputting a numeric value) correctly interpolates values
     * for zoom levels between stops.
     * 
     * @throws Exception
     */
    @Test
    public void propertyStopsNumericInterpolationTest() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        // -------- Test interpolation for base > 1
        // Create a Mapbox Function for base > 1
        String jsonStr = "{'base': 1.9, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]]}";
        MBFunction function = new MBFunction(object(jsonStr));
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a property function", EnumSet.of(MBFunction.FunctionCategory.PROPERTY).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.9, function.getBase().doubleValue(), .00001);
        
        Function fn = (Function) function.numeric();
        Number result = fn.evaluate(feature, Number.class);       
           
        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);    
        assertTrue("Interpolated value should be below midpoint (for base > 1)", result.doubleValue() < 36);
        
        // -------- Test interpolation for base < 1
        jsonStr = "{'base': 0.1, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]]}";
        function = new MBFunction(object(jsonStr));
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a property function", EnumSet.of(MBFunction.FunctionCategory.PROPERTY).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(0.1, function.getBase().doubleValue(), .00001);
        
        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);       
        
        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);    
        assertTrue("Interpolated value should be above midpoint (for base < 1)", result.doubleValue() > 36);
        
        // -------- Test interpolation for base = 1        
        jsonStr = "{'base': 1.0, 'property':'temperature', 'stops':[[0,12],[30,24],[70,48]]}";
        function = new MBFunction(object(jsonStr));
        
        // Assert it is an exponential function with the correct base
        assertTrue("Is a property function", EnumSet.of(MBFunction.FunctionCategory.PROPERTY).equals(function.category()));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());
        assertEquals(1.0, function.getBase().doubleValue(), .00001);
        
        fn = (Function) function.numeric();
        result = fn.evaluate(feature, Number.class);       
        
        assertTrue("Interpolated value should be above lower stop", result.doubleValue() > 24);    
        assertEquals("Interpolated value should = midpoint (for base = 1)", result.doubleValue(), 36.0, .0001);
    }
    
    public void verifyEnvironmentZoomLevel(int zoomLevel) {
        Number envZoomLevel = ff.function("zoomLevel",
                ff.function("env", ff.literal("wms_scale_denominator")), ff.literal("EPSG:3857"))
                .evaluate(null, Number.class);
        assertEquals("Zoom level is " + zoomLevel, zoomLevel, envZoomLevel.doubleValue(), .00001);
    }
    
}
