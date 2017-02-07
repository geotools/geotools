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
package org.geotools.mbstyle;

import static org.junit.Assert.*;

import java.awt.Color;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Try out property and zoom function.
 */
public class MBFunctionTest {

    public static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void propertyColorStops() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");

        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser
                .parse("{\"property\":\"temperature\",\"stops\":[[0,\"blue\"],[100,\"red\"]] }");

        MBFunction function = new MBFunction(json);

        assertEquals("temperature", function.getProperty());
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = function.color();
        assertNotNull(fn);
        assertEquals("Interpolate", fn.getName());
        PropertyName propertyName = (PropertyName) fn.getParameters().get(0);
        assertEquals("temperature", propertyName.getPropertyName());

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

        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        Color result = fn.evaluate(feature, Color.class);
        assertEquals(new Color(128, 0, 128), result);
    }

    @Test
    public void propertyValueStops() throws Exception {
        SimpleFeatureType SAMPLE = DataUtilities.createType("SAMPLE",
                "id:\"\",temperature:0.0,location=4326");

        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser
                .parse("{\"property\":\"temperature\",\"stops\":[[0,5],[100,10]] }");

        MBFunction function = new MBFunction(json);

        assertEquals("temperature", function.getProperty());
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = function.expression();
        assertNotNull(fn);
        assertEquals("Interpolate", fn.getName());
        PropertyName propertyName = (PropertyName) fn.getParameters().get(0);
        assertEquals("temperature", propertyName.getPropertyName());
        SimpleFeature feature = DataUtilities.createFeature(SAMPLE, "measure1=A|50.0|POINT(0,0)");

        Double result = fn.evaluate(feature, Double.class);
        assertEquals( 7.5, result, 0.0);
    }
}
