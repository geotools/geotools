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
import java.nio.charset.UnsupportedCharsetException;

import org.geotools.factory.Hints;

/**
 * Converter for going from a String to a {@link Charset} and vice versa.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 *
 * @source $URL$
 */
public class CharsetConverterFactory implements ConverterFactory {

    public Converter createConverter(Class<?> source, Class<?> target,
            Hints hints) {
        
        if ( CharSequence.class.isAssignableFrom( source ) && 
                Charset.class.isAssignableFrom( target ) ) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    try {
                        return(T) Charset.forName( (String) source );
                    }
                    catch( UnsupportedCharsetException e ) {
                        //TODO: log this
                        return null;
                    }
                }
            };
        }
        if ( Charset.class.isAssignableFrom( source ) && 
                CharSequence.class.isAssignableFrom( target ) ) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    return (T) ((Charset)source).toString();
                }
                
            };
        }
                
        return null;
    }

}
