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
package org.geotools.data.util;

import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * ConverterFactory which converts between a percentage to Float or Double
 *
 * @author Andrea Aime - GeoSolutions
 */
public class PercentageConverterFactory implements ConverterFactory {

    static final PercentageConverter INSTANCE = new PercentageConverter();

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {
        // convert to non-primitive class
        target = NumericConverterFactory.primitiveToWrapperClass(target);

        // check if source is a number or a string. We can't convert to a number from anything else.
        if (!String.class.isAssignableFrom(source)) return null;

        // check if target is one of supported
        if (Double.class.equals(target) || Float.class.equals(target)) {
            return INSTANCE;
        }

        return null;
    }

    static class PercentageConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String s = (String) source;
            if (!s.endsWith("%")) {
                return null;
            }
            String number = s.substring(0, s.length() - 1);
            if (Double.class.equals(target)) {
                double value = Double.valueOf(number);
                return target.cast(Double.valueOf(value / 100));
            } else if (Float.class.equals(target)) {
                float value = Float.valueOf(number);
                return target.cast(Float.valueOf(value / 100));
            }

            return null;
        }
    }
}
