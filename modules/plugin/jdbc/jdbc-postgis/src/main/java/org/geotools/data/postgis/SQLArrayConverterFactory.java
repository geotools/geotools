/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;
import static java.lang.reflect.Array.set;

import java.sql.Array;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;

/** ConverterFactory for handling {@link org.postgresql.jdbc.PgArray} conversions */
public class SQLArrayConverterFactory implements ConverterFactory {

    public static final SQLArrayToJavaConverter ARRAY_TO_JAVA_CONVERTER =
            new SQLArrayToJavaConverter();

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (target.isArray() && Array.class.isAssignableFrom(source)) {
            return ARRAY_TO_JAVA_CONVERTER;
        }
        return null;
    }

    static class SQLArrayToJavaConverter implements Converter {

        public Object convert(Object source, Class target) throws Exception {
            Array sqlArray = (Array) source;
            Object array = sqlArray.getArray();
            int length = getLength(array);
            Class componentType = target.getComponentType();
            Object result = newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                Object original = get(array, i);
                if (original == null) {
                    set(result, i, null);
                } else {
                    Object converted = Converters.convert(original, componentType);
                    if (converted == null) {
                        throw new RuntimeException(
                                "Failed to convert " + original + " to " + componentType);
                    }
                    set(result, i, converted);
                }
            }

            return result;
        }
    }
}
