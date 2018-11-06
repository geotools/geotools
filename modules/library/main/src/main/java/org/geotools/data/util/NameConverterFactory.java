/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.feature.NameImpl;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.opengis.feature.type.Name;

/**
 * ConverterFactory for handling Name conversions.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class NameConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        if (target.equals(String.class) && source.equals(Name.class)) {
            return new Converter() {

                public Object convert(Object source, Class target) throws Exception {
                    Name name = (Name) source;
                    return name.getURI();
                }
            };

        } else if (target.equals(Name.class) && source.equals(String.class)) {
            return new Converter() {

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    String str = (String) source;
                    String[] split = str.split(":");
                    if (split.length == 2) {
                        return (T) new NameImpl(split[0], split[1]);
                    } else if (split.length == 1) {
                        return (T) new NameImpl(str);
                    }

                    return null;
                }
            };
        }

        return null;
    }
}
