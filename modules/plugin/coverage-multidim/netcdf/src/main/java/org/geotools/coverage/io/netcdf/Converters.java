/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.util.BooleanConverterFactory;
import org.geotools.data.util.NumericConverterFactory;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Convenience class for converting an object from one type to an object of another.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @since 9.0
 */
final class ConvertersHack {

    private static final Logger LOGGER = Logging.getLogger(ConvertersHack.class);

    /** Cached list of converter factories */
    static final ConverterFactory[] factories = {
        new NumericConverterFactory(),
        new BooleanConverterFactory(),
        new TemporalConverterFactoryHack()
    };

    /**
     * Converts an object of a particular type into an object of a different type.
     *
     * <p>Convenience for {@link #convert(Object, Class, Hints)}
     *
     * @param source The object to convert.
     * @param target The type of the converted value.
     * @return The converted value as an instance of target, or <code>null</code> if a converter
     *     could not be found
     * @since 2.4
     */
    public static <T> T convert(Object source, Class<T> target) {
        return convert(source, target, null);
    }

    /**
     * Converts an object of a particular type into an object of a different type.
     *
     * <p>This method uses the {@link ConverterFactory} extension point to find a converter capable
     * of performing the conversion. The first converter found is the one used. Using this class
     * there is no way to guarantee which converter will be used.
     *
     * @param source The object to convert.
     * @param target The type of the converted value.
     * @param hints Any hints for the converter factory.
     * @return The converted value as an instance of target, or <code>null</code> if a converter
     *     could not be found.
     * @since 2.4
     */
    public static <T> T convert(Object source, Class<T> target, Hints hints) {
        // can't convert null
        if (source == null) return null;

        // handle case of source being an instance of target up front
        final Class<?> sourceClass = source.getClass();
        if (sourceClass == target
                || sourceClass.equals(target)
                || target.isAssignableFrom(sourceClass)) {
            return target.cast(source);
        }

        for (ConverterFactory factory : factories) {
            Converter converter = factory.createConverter(sourceClass, target, hints);
            if (converter != null) {
                try {
                    T converted = converter.convert(source, target);
                    if (converted != null) {
                        return converted;
                    }
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINER))
                        LOGGER.log(
                                Level.FINER,
                                "Error applying the converter "
                                        + converter.getClass()
                                        + " on ("
                                        + source
                                        + ","
                                        + target
                                        + ")",
                                e);
                }
            }
        }

        // a couple of final tries
        if (String.class.equals(target)) {
            return target.cast(source.toString());
        }
        return null;
    }
}
