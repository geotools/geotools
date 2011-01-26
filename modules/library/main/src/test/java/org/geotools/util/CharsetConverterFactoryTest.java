/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.nio.charset.Charset;
import java.util.Set;

import junit.framework.TestCase;

public class CharsetConverterFactoryTest extends TestCase {

    CharsetConverterFactory factory;
    
    protected void setUp() throws Exception {
        factory = new CharsetConverterFactory();
    }
    
    public void testLookupStringToCharset() {
        Set<ConverterFactory> s = Converters.getConverterFactories(String.class,Charset.class);
        for ( ConverterFactory cf : s ) {
            if ( cf instanceof CharsetConverterFactory ) {
                return;
            }
        }
        
        fail( "CharsetConverterFactory not found" );
    }
    
    public void testLookupCharsetToString() {
        Set<ConverterFactory> s = Converters.getConverterFactories(Charset.class,String.class);
        for ( ConverterFactory cf : s ) {
            if ( cf instanceof CharsetConverterFactory ) {
                return;
            }
        }
        
        fail( "CharsetConverterFactory not found" );
    }
    
    public void testStringToCharset() throws Exception {
        Converter c = factory.createConverter( String.class, Charset.class, null );
        assertNotNull( c );
        
        Charset charset = c.convert( "UTF-8", Charset.class );
        assertNotNull( charset );
        assertEquals( "UTF-8", charset.name() );
        
        assertNull( c.convert( "FOO", Charset.class ) );
    }
    
    public void testCharsetToString() throws Exception {
        Converter c = factory.createConverter( Charset.class, String.class, null );
        assertNotNull( c );
        
        String charset = c.convert( Charset.forName( "UTF-8"), String.class );
        assertEquals( "UTF-8", charset );
    }
}
