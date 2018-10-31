/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverage.grid.io.footprint;

import static org.geotools.util.Utilities.toUnmodifiableSet;

import java.util.Arrays;
import java.util.Set;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;

/**
 * Enable programs to find all available FootprintLoader implementations.
 *
 * <p>In order to be located by this finder modules must provide an implementation of the {@link
 * FootprintLoaderSpi} interface.
 *
 * <p>In addition to implementing this interface, this service file should be defined:
 *
 * <p><code>META-INF/services/org.geotools.coverage.grid.io.footprint.FootprintLoaderSpi</code> *
 *
 * <p>Example:<br>
 * <code>org.geotools.coverage.grid.io.footprint.WKBLoaderSPI</code>
 *
 * @author Daniele Romagnoli, GeoSolutions
 */
public final class FootprintLoaderFinder {
    /** The service registry for this manager. Will be initialized only when first needed. */
    private static FactoryRegistry registry;

    /** Do not allows any instantiation of this class. */
    private FootprintLoaderFinder() {
        // singleton
    }

    /**
     * Finds all available implementations of {@link FootprintLoaderSpi} which have registered using
     * the services mechanism.
     *
     * @return An unmodifiable {@link Set} of all discovered modules which have registered factories
     */
    public static synchronized Set<FootprintLoaderSpi> getAvailableLoaders() {
        // get all FootprintLoaderSpi implementations
        scanForPlugins();
        return getServiceRegistry()
                .getFactories(FootprintLoaderSpi.class, true)
                .collect(toUnmodifiableSet());
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(FootprintLoaderFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {FootprintLoaderSpi.class}));
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
