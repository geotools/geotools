/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;

/**
 * Searches for all available {@link GeoPkgExtensionFactory} implementations.
 *
 * <p>In addition to implementing this interface GeoPackage extension providers should have a
 * services file, <code>META-INF/services/org.geotools.geopkg.GeoPkgExtensionFactory</code>,
 * declaring the factory.
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>Example:<br>
 * <code>org.geotools.geopkg.GeoPkgSchemaExtension$Factory</code>
 */
final class GeoPkgExtensionFactoryFinder {
    /** The logger for the filter module. */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GeoPkgExtensionFactoryFinder.class);

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    // Singleton pattern
    private GeoPkgExtensionFactoryFinder() {}

    /**
     * Finds all implementations of {@link GeoPkgExtensionFactory} which have registered using the
     * services mechanism.
     *
     * @return An iterator over all discovered GeoPkgExtensionFactory.
     */
    public static synchronized Iterator<GeoPkgExtensionFactory> getExtensionFactories() {
        return getServiceRegistry()
                .getFactories(GeoPkgExtensionFactory.class, null, null)
                .iterator();
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(GeoPkgExtensionFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {
                GeoPkgExtensionFactory.class,
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
