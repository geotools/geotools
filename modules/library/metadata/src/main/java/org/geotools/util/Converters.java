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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Convenience class for converting an object from one type to an object of another.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 */
public final class Converters {

    private static final Logger LOGGER = Logging.getLogger(Converters.class);

    /** Cached list of converter factories */
    static volatile ConverterFactory[] factories;

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(Converters.class);
        if (registry == null) {
            registry =
                    new FactoryCreator(
                            Arrays.asList(
                                    new Class<?>[] {
                                        ConverterFactory.class,
                                    }));
        }
        return registry;
    }
    //    /**
    //     * Used to combine provided hints with global GeoTools defaults.
    //     *
    //     */
    //    private static Hints addDefaultHints(final Hints hints) {
    //        return GeoTools.addDefaultHints(hints);
    //    }

    /**
     * Returns a set of all available implementations for the {@link ConverterFactory} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set of available ConverterFactory implementations.
     */
    public static synchronized Set<ConverterFactory> getConverterFactories(Hints hints) {
        hints = GeoTools.addDefaultHints(hints);
        return new LazySet<ConverterFactory>(
                getServiceRegistry().getFactories(ConverterFactory.class, null, hints));
    }

    /**
     * Returns a set of all available {@link ConverterFactory}'s which can handle convert from the
     * source to destination class.
     *
     * <p>This method essentially returns all factories in which the following returns non null.
     *
     * <pre>
     * factory.createConverter( source, target );
     * </pre>
     *
     * @since 2.5
     */
    public static Set<ConverterFactory> getConverterFactories(Class<?> source, Class<?> target) {
        HashSet<ConverterFactory> factories = new HashSet<ConverterFactory>();
        for (ConverterFactory factory : factories()) {
            if (factory.createConverter(source, target, null) != null) {
                factories.add(factory);
            }
        }
        return factories;
    }

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

        for (ConverterFactory factory : factories()) {
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

    /**
     * Processed the {@link ConverterFactory} extension point.
     *
     * @return A collection of converter factories.
     * @since 2.4
     */
    static ConverterFactory[] factories() {
        if (factories == null) {
            Collection<ConverterFactory> factoryCollection =
                    getConverterFactories(GeoTools.getDefaultHints());
            factories = factoryCollection.toArray(new ConverterFactory[factoryCollection.size()]);
        }
        return factories;
    }
}
