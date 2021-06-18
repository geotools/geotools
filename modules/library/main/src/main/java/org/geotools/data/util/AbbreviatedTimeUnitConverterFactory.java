/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.util.concurrent.TimeUnit;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converter Factory converting abbreviated timeUnit Strings (s, m, h, d) to {@link TimeUnit} enum.
 */
public class AbbreviatedTimeUnitConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (String.class.isAssignableFrom(source)) {
            if (TimeUnit.class.isAssignableFrom(target)) {

                return new Converter() {
                    @Override
                    public <T> T convert(Object source, Class<T> target) {
                        String timeUnitString = ((String) source).toUpperCase();
                        switch (timeUnitString) {
                            case "MS":
                                return target.cast(TimeUnit.MILLISECONDS);
                            case "S":
                                return target.cast(TimeUnit.SECONDS);
                            case "M":
                                return target.cast(TimeUnit.MINUTES);
                            case "H":
                                return target.cast(TimeUnit.HOURS);
                            case "D":
                                return target.cast(TimeUnit.DAYS);
                            default:
                                return null;
                        }
                    }
                };
            }
        }
        return null;
    }
}
