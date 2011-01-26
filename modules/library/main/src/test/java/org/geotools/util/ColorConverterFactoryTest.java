/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.awt.Color;

import org.geotools.filter.ConstantExpression;

import junit.framework.TestCase;

public class ColorConverterFactoryTest extends TestCase {

    ColorConverterFactory factory;
    
    protected void setUp() throws Exception {
        factory = new ColorConverterFactory();
    }
 
    public void testVsConstantExpression() throws Exception {
        ConstantExpression expr = ConstantExpression.color(Color.RED);
        String expected = expr.evaluate(null, String.class );
        
        Converter converter = factory.createConverter( Color.class, String.class, null );
        String actual = converter.convert( Color.RED, String.class );
        
        assertEquals( expected, actual );
    }
    
    public void testFromString() throws Exception {
        assertEquals( Color.RED, convert( "#FF0000" ) );
    }
    
    public void testFromInteger() throws Exception {
        assertEquals( Color.RED, convert(0xFF0000) );
        assertEquals( "no alpha", new Color( 0,0,255,255), convert(0x000000FF) );
        
        assertEquals( "255 alpha", new Color( 0,0,255,255), convert(0xFF0000FF) );
        
        assertEquals( "1 alpha", new Color( 0,0,255,1), convert(0x010000FF) );
    }
    
    Color convert( Object value ) throws Exception {
        Converter converter = factory.createConverter( value.getClass(), Color.class, null );
        return (Color) converter.convert( value, Color.class );
    }
    
    public void testRegisteredWithConverters(){
        Color color = Converters.convert("#189E77", Color.class );
        Color expected = new Color(24,158,119);
        assertNotNull( "converter not registered", color );
        assertEquals( expected, color );
    }
}
