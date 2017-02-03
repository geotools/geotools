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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import com.sun.prism.paint.Color;

/**
 * Try out property and zoom function.
 */
public class MBFunctionTest {

    @Ignore
    public void test() throws Exception {
        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser
                .parse("{\"property\":\"temperature\",\"stops\":[[0,\"blue\"],[100,\"red\"]] }");

        MBFunction function = new MBFunction(json);

        assertEquals("temperature", function.getProperty());
        assertTrue("property", function.category().contains(MBFunction.FunctionCategory.PROPERTY));
        assertEquals(MBFunction.FunctionType.EXPONENTIAL, function.getType());

        Function fn = function.expression();
        assertNotNull(fn);
        assertEquals("Interpolate", fn.getName());
        PropertyName propertyName = (PropertyName) fn.getParameters().get(0);
        assertEquals("temperature", propertyName.getPropertyName());
        
        Literal stop1 = (Literal) fn.getParameters().get(1);
        assertEquals(new Integer(0),stop1.evaluate(null,Integer.class));
        
        Literal color1 = (Literal) fn.getParameters().get(2);
        assertEquals(Color.BLUE, color1.evaluate(null,Color.class));
        
        Literal stop2 = (Literal) fn.getParameters().get(3);
        assertEquals(new Integer(100),stop2.evaluate(null,Integer.class));
        
        Literal color2 = (Literal) fn.getParameters().get(4);
        assertEquals(Color.BLUE, color2.evaluate(Color.class));
    }

}
