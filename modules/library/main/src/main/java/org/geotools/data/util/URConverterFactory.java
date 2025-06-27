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
package org.geotools.data.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converter factory which can convert between URL,URI, and String.
 *
 * <p>Handles the following conversions:
 *
 * <ul>
 *   <li>String -> URL
 *   <li>String -> URI
 *   <li>URL -> URI
 *   <li>URI -> URL
 * </ul>
 *
 * @author Justin Deoliveira, OpenGEO
 * @since 2.5
 */
public class URConverterFactory implements ConverterFactory {

    public static final Converter StringToURL = new Converter() {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String s = (String) source;
            try {
                return target.cast(new URL(s));
            } catch (MalformedURLException e1) {
                File f = new File(s);
                try {
                    return target.cast(f.toURI().toURL());
                } catch (MalformedURLException e2) {
                    // ignore
                }
            }

            return null;
        }
    };

    public static final Converter StringToURI = new Converter() {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String s = (String) source;
            try {
                return target.cast(new URI(s));
            } catch (URISyntaxException e1) {
                File f = new File(s);
                try {
                    return target.cast(f.toURI());
                } catch (Exception e2) {
                    // ignore
                }
            }

            return null;
        }
    };

    public static final Converter URLToURI = new Converter() {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            URL url = (URL) source;
            return target.cast(url.toURI());
        }
    };

    public static final Converter URIToURL = new Converter() {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            URI uri = (URI) source;
            return target.cast(uri.toURL());
        }
    };

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (String.class.equals(source)) {
            if (URL.class.equals(target)) {
                return StringToURL;
            }
            if (URI.class.equals(target)) {
                return StringToURI;
            }
        }

        if (URL.class.equals(source) && URI.class.equals(target)) {
            return URLToURI;
        }
        if (URI.class.equals(source) && URL.class.equals(target)) {
            return URIToURL;
        }

        return null;
    }
}
