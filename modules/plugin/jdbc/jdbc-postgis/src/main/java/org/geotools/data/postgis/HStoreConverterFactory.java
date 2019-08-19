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
 */
package org.geotools.data.postgis;

import java.util.Map;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * ConverterFactory for handling Map of String to {@link HStore} conversion.
 *
 * @author Daniele Romagnoli - GeoSolutions SAS
 */
public class HStoreConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (HStore.class.equals(target) && Map.class.isAssignableFrom(source)) {
            return new Converter() {

                public Object convert(Object source, Class target) throws Exception {
                    return new HStore((Map<String, String>) source);
                }
            };
        }
        return null;
    }
}
