/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.factory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The list of registered {@linkplain FactoryIteratorProvider factory iterator providers}.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 * @todo Consider removing {@link FactoryRegistry#globalConfiguration} and use listeners instead.
 */
final class FactoryIteratorProviders {
    /**
     * The system-wide configuration. This is the instance configured by the public static methods
     * provided in this class.
     */
    private static final FactoryIteratorProviders GLOBAL = new FactoryIteratorProviders();

    /** Incremented every time a modification is performed. */
    private final AtomicInteger modifications = new AtomicInteger();

    /**
     * Alternative scanning methods used by {@link FactoryRegistry#scanForPlugins(Collection,Class)}
     * in addition of the default lookup mechanism. Will be created only when first needed.
     */
    private final Set<FactoryIteratorProvider> iteratorProviders =
            Collections.synchronizedSet(new LinkedHashSet<>());

    /** Creates an initially empty set of factories. */
    FactoryIteratorProviders() {}

    /**
     * Synchronizes the content of the {@link #iteratorProviders} map with the {@linkplain #GLOBAL
     * global} one. New providers are returned for later {@linkplain FactoryRegistry#register
     * registration}. Note that this method is typically invoked in a different thread than {@link
     * FactoryIteratorProviders} public static method calls.
     *
     * @return The new iterators providers {@linkplain #addFactoryIteratorProvider added} since the
     *     last time this method was invoked, or {@code null} if none.
     */
    final FactoryIteratorProvider[] synchronizeIteratorProviders() {
        if (modifications.get() == GLOBAL.modifications.get()) {
            return null;
        }
        synchronized (GLOBAL) {
            modifications.set(GLOBAL.modifications.get());
            /*
             * If 'removeFactoryIteratorProvider(...)' has been invoked since the last time
             * this method was run, then synchronize 'iteratorProviders' accordingly. Current
             * implementation do not unregister the factories that were created by those iterators.
             */
            if (!iteratorProviders.isEmpty()) {
                iteratorProviders.retainAll(GLOBAL.iteratorProviders);
            }

            /*
             * If 'addFactoryIteratorProvider(...)' has been invoked since the last time
             * this method was run, then synchronize 'iteratorProviders' accordingly. We
             * keep trace of new providers in order to allow 'FactoryRegistry' to use them
             * for a immediate scanning.
             */
            FactoryIteratorProvider[] newProviders =
                    GLOBAL.iteratorProviders
                            .stream()
                            .filter(this.iteratorProviders::add)
                            .toArray(FactoryIteratorProvider[]::new);

            return newProviders.length == 0 ? null : newProviders;
        }
    }

    /**
     * Adds an alternative way to search for factory implementations. {@link FactoryRegistry} has a
     * default mechanism bundled in it, which uses the content of all {@code META-INF/services}
     * directories found on the classpath. This {@code addFactoryIteratorProvider} method allows to
     * specify additional discovery algorithms. It may be useful in the context of some frameworks
     * that use the <cite>constructor injection</cite> pattern, like the <a
     * href="http://www.springframework.org/">Spring framework</a>.
     */
    public static void addFactoryIteratorProvider(FactoryIteratorProvider provider) {
        GLOBAL.add(provider);
    }

    private void add(FactoryIteratorProvider provider) {
        if (iteratorProviders.add(provider)) {
            modifications.incrementAndGet();
        }
    }

    private void remove(FactoryIteratorProvider provider) {
        if (iteratorProviders.remove(provider)) {
            modifications.incrementAndGet();
        }
    }

    /**
     * Removes a provider that was previously {@linkplain #addFactoryIteratorProvider added}. Note
     * that factories already obtained from the specified provider will not be {@linkplain
     * FactoryRegistry#deregisterFactory deregistered} by this method.
     */
    public static void removeFactoryIteratorProvider(FactoryIteratorProvider provider) {
        GLOBAL.remove(provider);
    }

    /**
     * Returns all iterator providers. This method do not returns any live collection since the
     * array will be used outside the synchronized block.
     */
    static FactoryIteratorProvider[] getIteratorProviders() {
        return GLOBAL.iteratorProviders.toArray(
                new FactoryIteratorProvider[GLOBAL.iteratorProviders.size()]);
    }
}
