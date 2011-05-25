/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import java.util.Arrays;
import java.util.Set;

import org.geotools.factory.Hints;
import org.geotools.factory.FactoryFinder;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.resources.LazySet;


/**
 * Defines static methods used to access the application's default
 * {@linkplain GridCoverageFactory factory} implementation.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class CoverageFactoryFinder extends FactoryFinder {
    /**
     * The service registry for this manager.
     * Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Do not allows any instantiation of this class.
     */
    private CoverageFactoryFinder() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first
     * time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(CoverageFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {
                    GridCoverageFactory.class}));
        }
        return registry;
    }

    /**
     * Returns the first implementation of {@link GridCoverageFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first grid coverage factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link GridCoverageFactory} interface.
     *
     * @see Hints#DEFAULT_COORDINATE_REFERENCE_SYSTEM
     * @see Hints#TILE_ENCODING
     */
    public static synchronized GridCoverageFactory getGridCoverageFactory(Hints hints)
            throws FactoryRegistryException
    {
        hints = mergeSystemHints(hints);
        return getServiceRegistry().getServiceProvider(GridCoverageFactory.class, null, hints, null);
    }

    /**
     * Returns a set of all available implementations for the {@link GridCoverageFactory} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available grid coverage factory implementations.
     *
     * @since 2.4
     */
    public static synchronized Set<GridCoverageFactory> getGridCoverageFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet<GridCoverageFactory>(getServiceRegistry().getServiceProviders(
                GridCoverageFactory.class, null, hints));
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is
     * needed because the application class path can theoretically change, or
     * additional plug-ins may become available. Rather than re-scanning the
     * classpath on every invocation of the API, the class path is scanned
     * automatically only on the first invocation. Clients can call this
     * method to prompt a re-scan. Thus this method need only be invoked by
     * sophisticated applications which dynamically make new plug-ins
     * available at runtime.
     */
    public static synchronized void scanForPlugins() {
        if (registry != null) {
            registry.scanForPlugins();
        }
    }
}
