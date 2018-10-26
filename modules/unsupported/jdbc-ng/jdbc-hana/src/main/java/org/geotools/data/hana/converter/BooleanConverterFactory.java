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
package org.geotools.data.hana.converter;

import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converter factory for converting Short objects to Boolean objects.
 *
 * <p>In HANA, ResultSet#getObject will return a Short object in case of boolean fields.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class BooleanConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (Short.class.equals(source) && Boolean.class.equals(target)) {
            return new Converter() {
                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source == null) {
                        throw new IllegalArgumentException("source must not be null");
                    }
                    Short s = (Short) source;
                    if (s.shortValue() == 0) {
                        return target.cast(new Boolean(false));
                    } else if (s.shortValue() == 1) {
                        return target.cast(new Boolean(true));
                    } else {
                        return null;
                    }
                }
            };
        }
        return null;
    }
}
