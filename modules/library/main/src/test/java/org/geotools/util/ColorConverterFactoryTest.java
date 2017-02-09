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

import org.geotools.factory.Hints;
import org.geotools.filter.ConstantExpression;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
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
    
    public void testFromLong() throws Exception {
//        assertEquals( Color.RED, convert(0xFF0000) );
        assertEquals( "no alpha", new Color( 0,0,255,255), convert((long) 0x000000FF) );
        
        assertEquals( "255 alpha", new Color( 0,0,255,255), convert((long) 0xFF0000FF) );
        
        assertEquals( "1 alpha", new Color( 0,0,255,1), convert((long) 0x010000FF) );
    }

    public void testFromCssName() throws Exception {
        Converter converter = factory.createConverter(String.class, Color.class,
                new Hints(Hints.COLOR_NAMES, "CSS"));
        assertEquals("aliceblue", new Color(240, 248, 255),
                converter.convert("aliceblue", Color.class));
        assertEquals("AliceBlue", new Color(240, 248, 255),
                converter.convert("AliceBlue", Color.class));
        assertEquals("gray", new Color(128, 128, 128), converter.convert("gray", Color.class));
        assertEquals("lemonchiffon", new Color(255, 250, 205),
                converter.convert("lemonchiffon", Color.class));
        assertEquals("WHITE", Color.WHITE, converter.convert("WHITE", Color.class));
        assertEquals("black", Color.BLACK, converter.convert("black", Color.class));
        assertEquals("thistle", new Color(216, 191, 216),
                converter.convert("thistle", Color.class));
        assertEquals("hex fallback", new Color(128, 128, 128),
                converter.convert("#808080", Color.class));
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
