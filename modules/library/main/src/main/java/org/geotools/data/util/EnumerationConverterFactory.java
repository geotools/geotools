/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2012 TOPP - www.openplans.org.
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

import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converts between enumerations and strings
 *
 * @author Andrea Aime - OpenGeo
 */
public class EnumerationConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (String.class.equals(source) && target.isEnum() || source.isEnum() && String.class.equals(source)) {
            return new EnumConverter();
        } else {
            return null;
        }
    }

    private static class EnumConverter implements Converter {

        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source instanceof String && target.isEnum()) {
                return (T) Enum.valueOf((Class<Enum>) target, (String) source);
            } else if (source.getClass().isEnum() && String.class.equals(target)) {
                return (T) ((Enum) source).name();
            } else {
                return null;
            }
        }
    }
}
