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
 *    
 */
package org.geotools.mbstyle.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.geotools.filter.text.ecql.ECQL;
import org.junit.Test;
import org.opengis.filter.expression.Function;

/**
 * Test the {@link ExponentialFunction}, {@link ZoomLevelFunction} and {@link ColorFunction}.
 */
public class MBFunctionFactoryTest {

    @Test
    public void colorFunction() throws Exception {
        Function expr = (Function) ECQL.toExpression("color('#ff0000')");
        assertEquals("hex", Color.red, expr.evaluate(null, Color.class));
        assertEquals("hex", "#FF0000", expr.evaluate(null, String.class));

        expr = (Function) ECQL.toExpression("color('red')");
        assertEquals("css", Color.red, expr.evaluate(null, Color.class));
        assertEquals("css", "#FF0000", expr.evaluate(null, String.class));

        expr = (Function) ECQL.toExpression("color('rgb(255,0,0)')");
        assertEquals("rgb", Color.red, expr.evaluate(null, Color.class));
        assertEquals("rgb", "#FF0000", expr.evaluate(null, String.class));
    }
    
    @Test
    public void expontentialFunctionNumeric() throws Exception {
        //
        // base 1.0 works as a simple interpolate
        //
        Function expr = (Function) ECQL.toExpression("Exponential( 0, 1.0, 0,0, 10,100)");
        assertEquals(  0, (int) expr.evaluate(null,Integer.class));
        
        expr = (Function) ECQL.toExpression("Exponential( 5, 1.0, 0,0, 10,100)");
        assertEquals( 50, (int) expr.evaluate(null,Integer.class));
        
        expr = (Function) ECQL.toExpression("Exponential(10, 1.0, 0,0, 10,100)");
        assertEquals(100, (int) expr.evaluate(null,Integer.class));
        
        //
        // base 0.7 increases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 0.7, 0,0, 10,100)");
        assertEquals(  0, (int) expr.evaluate(null,Integer.class));
        
        expr = (Function) ECQL.toExpression("Exponential( 5, 0.7, 0,0, 10,100)");
        assertTrue( 50 < (int) expr.evaluate(null,Integer.class));
        
        expr = (Function) ECQL.toExpression("Exponential(10, 0.7, 0,0, 10,100)");
        assertEquals(100, (int) expr.evaluate(null,Integer.class));

        //
        // base 1.4 decreases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 1.4, 0,0, 10,100)");
        assertEquals(  0, (int) expr.evaluate(null,Integer.class));
        
        expr = (Function) ECQL.toExpression("Exponential( 5, 1.4, 0,0, 10,100)");
        assertTrue( 50 > (int) expr.evaluate(null,Integer.class));
        
        expr = (Function) ECQL.toExpression("Exponential(10, 1.5, 0,0, 10,100)");
        assertEquals(100, (int) expr.evaluate(null,Integer.class));
    }
    
    @Test
    public void expontentialFunctionColor() throws Exception {
        //
        // base 1.0 works as a simple interpolate
        //
        Function expr = (Function) ECQL.toExpression("Exponential( 0, 1.0, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.BLACK, expr.evaluate(null,Color.class));
        
        expr = (Function) ECQL.toExpression("Exponential( 5, 1.0, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.GRAY, expr.evaluate(null,Color.class));
        
        expr = (Function) ECQL.toExpression("Exponential(10, 1.0, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.WHITE, expr.evaluate(null,Color.class));
        
        //
        // base 0.7 increases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 0.7, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.BLACK, expr.evaluate(null,Color.class));
        
        expr = (Function) ECQL.toExpression("Exponential( 5, 0.7, 0,'#000000', 10,'#ffffff')");
        assertTrue( Color.GRAY.getRed() < expr.evaluate(null,Color.class).getRed() );
        
        expr = (Function) ECQL.toExpression("Exponential(10, 0.7, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.WHITE, expr.evaluate(null,Color.class));
        
        //
        // base 1.4 decreases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 1.4, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.BLACK, expr.evaluate(null,Color.class));
        
        expr = (Function) ECQL.toExpression("Exponential( 5, 1.4, 0,'#000000', 10,'#ffffff')");
        assertTrue( Color.GRAY.getRed() > expr.evaluate(null,Color.class).getRed() );
        
        expr = (Function) ECQL.toExpression("Exponential(10, 1.4, 0,'#000000', 10,'#ffffff')");
        assertEquals(  Color.WHITE, expr.evaluate(null,Color.class));
    }
}
