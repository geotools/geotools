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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;

/**
 * Enable programs to find all available FootprintLoader implementations.
 *
 * <p>
 * In order to be located by this finder modules must provide an implementation of the {@link FootprintLoaderSpi} interface.
 * </p>
 *
 * <p>
 * In addition to implementing this interface, this service file should be defined:
 * </p>
 *
 * <p>
 * <code>META-INF/services/org.geotools.coverage.grid.io.footprint.FootprintLoaderSpi</code>
 * </p>
 * *
 * <p>
 * Example:<br/>
 * <code>org.geotools.coverage.grid.io.footprint.WKBLoaderSPI</code>
 * </p>
 * 
 * @author Daniele Romagnoli, GeoSolutions
 *
 */
public final class FootprintLoaderFinder {
    /**
     * The service registry for this manager. Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Do not allows any instantiation of this class.
     */
    private FootprintLoaderFinder() {
        // singleton
    }

    /**
     * Finds all available implementations of {@link FootprintLoaderSpi} which have registered 
     * using the services mechanism.
     *
     * @return An unmodifiable {@link Set} of all discovered modules which have registered factories
     */
    public static synchronized Set<FootprintLoaderSpi> getAvailableLoaders() {
        // get all FootprintLoaderSpi implementations
        scanForPlugins();
        final Iterator<FootprintLoaderSpi> it = getServiceRegistry().getServiceProviders(
                FootprintLoaderSpi.class, true);
        final Set<FootprintLoaderSpi> loaders = new HashSet<FootprintLoaderSpi>();
        while (it.hasNext()) {
            loaders.add(it.next());
        }
        return Collections.unmodifiableSet(loaders);
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(FootprintLoaderFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { FootprintLoaderSpi.class }));
        }
        return registry;
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed 
     * because the application class path can theoretically change, or additional plug-ins 
     * may become available. Rather than re-scanning the classpath on every invocation of 
     * the API, the class path is scanned automatically only on the first invocation. 
     * Clients can call this method to prompt a re-scan. Thus this method need only be invoked by
     * sophisticated applications which dynamically make new plug-ins available at runtime.
     */
    public static synchronized void scanForPlugins() {
        getServiceRegistry().scanForPlugins();
    }
}
