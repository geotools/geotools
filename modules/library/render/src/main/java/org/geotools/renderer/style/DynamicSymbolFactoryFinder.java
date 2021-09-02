/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.Hints;

/**
 * Searches for all available {@link ExternalGraphicFactory} and {@link MarkFactory}
 * implementations.
 *
 * <p>In addition to implementing this interface dynamic symbol handlers should have a services
 * file:
 *
 * <ul>
 *   <li><code>META-INF/services/org.geotools.renderer.style.MarkFactory</code> if the are {@link
 *       MarkFactory} instances
 *   <li><code>META-INF/services/org.geotools.renderer.style.ExternalGraphicFactory</code> if the
 *       are {@link ExternalGraphicFactory} instances
 * </ul>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>Example:<br>
 * <code>org.geotools.data.jdbc.DBCPDataSourceFactory</code>
 */
public final class DynamicSymbolFactoryFinder {
    /** The logger for the filter module. */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DynamicSymbolFactoryFinder.class);

    /**
     * Rendering hint key to store a {@link Comparator}<{@link MarkFactory}> instance to set the
     * {@link MarkFactory} execution order.
     */
    public static final Hints.Key MARK_FACTORY_ORDER = new Hints.Key(Comparator.class);
    /**
     * Rendering hint key to store a {@link Predicate}<{@link MarkFactory}> instance to set the
     * allowed {@link MarkFactory} instances to be evaluated.
     */
    public static final Hints.Key MARK_FACTORY_FILTER = new Hints.Key(Predicate.class);

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    // Singleton pattern
    private DynamicSymbolFactoryFinder() {}

    /**
     * Finds all implementations of {@link MarkFactory} which have registered using the services
     * mechanism.
     *
     * @return An iterator over all discovered datastores which have registered factories, and whose
     *     available method returns true.
     */
    public static synchronized Iterator<MarkFactory> getMarkFactories() {
        return getServiceRegistry().getFactories(MarkFactory.class, null, null).iterator();
    }

    /**
     * Finds all implementations of {@link MarkFactory} which have registered using the services
     * mechanism. Returns a filtered and ordered iterator based on the provided Hints input
     * parameter.
     *
     * @param hints An optional map of hints for factory configuration, or {@code null} if none.
     *     Allowed Hints are: {@link DynamicSymbolFactoryFinder.MARK_FACTORY_ORDER}, {@link
     *     DynamicSymbolFactoryFinder.MARK_FACTORY_FILTER}
     * @return A filtered and ordered iterator over all discovered datastores which have registered
     *     factories, and whose available method returns true.
     */
    @SuppressWarnings("unchecked")
    public static synchronized Iterator<MarkFactory> getMarkFactories(Hints hints) {
        Comparator<MarkFactory> sort =
                hints != null ? (Comparator<MarkFactory>) hints.get(MARK_FACTORY_ORDER) : null;
        Predicate<MarkFactory> filter =
                hints != null ? (Predicate<MarkFactory>) hints.get(MARK_FACTORY_FILTER) : null;
        Stream<MarkFactory> factories =
                getServiceRegistry().getFactories(MarkFactory.class, filter, hints);

        return sort != null ? factories.sorted(sort).iterator() : factories.iterator();
    }

    /**
     * Finds all implementations of {@link ExternalGraphicFactory} which have registered using the
     * services mechanism.
     *
     * @return An iterator over all registered ExternalGraphicFactory
     */
    public static synchronized Iterator<ExternalGraphicFactory> getExternalGraphicFactories() {
        return getExternalGraphicFactories(null);
    }

    /**
     * Finds all implementations of {@link ExternalGraphicFactory} which have registered using the
     * services mechanism.
     *
     * @param hints An optional map of hints for factory configfuration, or {@code null} if none.
     * @return An iterator over all registered ExternalGraphicFactory
     */
    public static synchronized Iterator<ExternalGraphicFactory> getExternalGraphicFactories(
            Hints hints) {
        return getServiceRegistry()
                .getFactories(ExternalGraphicFactory.class, null, hints)
                .iterator();
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(DynamicSymbolFactoryFinder.class);
        if (registry == null) {
            registry =
                    new FactoryCreator(
                            Arrays.asList(
                                    new Class<?>[] {
                                        MarkFactory.class, ExternalGraphicFactory.class
                                    }));
        }
        return registry;
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the
     * application class path can theoretically change, or additional plug-ins may become available.
     * Rather than re-scanning the classpath on every invocation of the API, the class path is
     * scanned automatically only on the first invocation. Clients can call this method to prompt a
     * re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public static synchronized void scanForPlugins() {
        getServiceRegistry().scanForPlugins();
    }
}
