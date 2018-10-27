/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data;

import java.util.Arrays;
import java.util.Set;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.Hints;

/**
 * @author Jody Garnett
 */
public class FeatureLockFactoryFinder extends FactoryFinder
{
    /** The service registry for this manager. Will be initialized only when first needed. */
    private static FactoryRegistry registry;

    /** Do not allows any instantiation of this class. */
    private FeatureLockFactoryFinder() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    @SuppressWarnings("deprecation")
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(CommonFactoryFinder.class);
        if (registry == null) {
            registry =
                    new FactoryCreator(
                            Arrays.asList(
                                    new Class<?>[] {
                                            FeatureLockFactory.class
                                    }));
        }
        return registry;
    }

    /**
     * Returns the first implementation of {@link FeatureLockFactory}. If no implementation matches, a new
     * one is created if possible or an exception is thrown otherwise.
     *
     * @return The first style factory available
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *     {@link FeatureLockFactory} interface.
     * @see Hints#STYLE_FACTORY
     */
    public static FeatureLockFactory getFeatureLockFactory() throws FactoryRegistryException {
        return getFeatureLockFactory(null);
    }

    /**
     * Looks up a certain factory using two methods:
     *
     * <ul>
     *   <li>First and un-synchronized lookup in the hints, should the user have provided the
     *       preferred factroy
     *   <li>A standard SPI registry scan, which has to be fully synchronized
     *
     * @param category
     * @param hints
     * @param key
     * @return
     */
    private static <T> T lookup(Class<T> category, Hints hints, Hints.Key key) {
        // nulls?
        if (hints == null || key == null) {
            return null;
        }

        // see if the user expressed a preference in the hints
        final Object hint = hints.get(key);
        if (hint != null) {
            if (category.isInstance(hint)) {
                return category.cast(hint);
            }
        }

        // otherwise do the lousy slow system scan
        synchronized (CommonFactoryFinder.class) {
            return getServiceRegistry().getFactory(category, null, hints, key);
        }
    }

    /**
     * Returns the first implementation of {@link FeatureLockFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return The first feature lock factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *     {@link FeatureLockFactory} interface.
     * @see Hints#FEATURE_LOCK_FACTORY
     * @deprecated FeautreLockFactory is no longer needed; please create a FeatureLock directly
     */
    public static FeatureLockFactory getFeatureLockFactory(Hints hints) {
        hints = mergeSystemHints(hints);
        return (FeatureLockFactory)
                lookup(FeatureLockFactory.class, hints, Hints.FEATURE_LOCK_FACTORY);
    }

    /**
     * Returns a set of all available implementations for the {@link FeatureLockFactory} interface.
     *
     * @param hints An optional map of hints, or {@code null} if none.
     * @return Set<FeatureLockFactory> of available style factory implementations.
     * @deprecated FeatureLockFactory is no longer needed
     */
    public static synchronized Set<FeatureLockFactory> getFeatureLockFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<FeatureLockFactory>(
                getServiceRegistry().getFactories(FeatureLockFactory.class, null, hints));
    }
}

