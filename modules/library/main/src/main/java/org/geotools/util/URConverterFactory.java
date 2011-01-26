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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.geotools.factory.Hints;

/**
 * Converter factory which can convert between URL,URI, and String.
 * <p>
 * Handles the following conversions:
 * <ul>
 *   <li>String -> URL
 *   <li>String -> URI
 *   <li>URL -> URI
 *   <li>URI -> URL
 * </ul>
 * </p>
 *
 * @author Justin Deoliveira, OpenGEO
 * @since 2.5
 *
 * @source $URL$
 */
public class URConverterFactory implements ConverterFactory {

    public static final Converter StringToURL = new Converter() {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String s = (String) source;
            try {
                return (T) new URL( s );
            }
            catch( MalformedURLException e1 ) {
                File f = new File( s );
                try {
                    return (T) f.toURI().toURL();
                }
                catch( MalformedURLException e2 ) {}
            }

            return null;

        }
    };
    
    public static final Converter StringToURI = new Converter() {

        public <T> T convert(Object source, Class<T> target) throws Exception {
            String s = (String) source;
            try {
                return (T) new URI( s );
            }
            catch( URISyntaxException e1 ) {
                File f = new File( s );
                try {
                    return (T) f.toURI();
                }
                catch( Exception e2 ) {}
            }

            return null;
        }
        
    };
    
    public static final Converter URLToURI = new Converter() {

        public <T> T convert(Object source, Class<T> target) throws Exception {
            URL url = (URL) source;
            return (T) url.toURI();
        }
    };
    
    public static final Converter URIToURL = new Converter() {
      
        public <T> T convert(Object source, Class<T> target) throws Exception {
            URI uri = (URI) source;
            return (T) uri.toURL();
        }
    };
    
    public Converter createConverter(Class<?> source, Class<?> target,
            Hints hints) {
        if ( String.class.equals( source ) ) {
            if ( URL.class.equals( target ) ) {
                return StringToURL;
            }
            if ( URI.class.equals( target ) ) {
                return StringToURI;
            }
        }
        
        if ( URL.class.equals( source ) && URI.class.equals( target ) ) {
            return URLToURI;
        }
        if ( URI.class.equals( source ) && URL.class.equals( target ) ) {
            return URIToURL;
        }
        
        return null;
    }

}
