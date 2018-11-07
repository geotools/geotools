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
 */
package org.geotools.imageio.netcdf.cv;

import static org.geotools.util.Utilities.toUnmodifiableSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.imageio.netcdf.cv.CoordinateHandlerSpi.CoordinateHandler;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.logging.Logging;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;

/**
 * Enable programs to find all available CoordinateHandler implementations.
 *
 * <p>In order to be located by this finder modules must provide an implementation of the {@link
 * CoordinateHandlerSpi} interface.
 *
 * <p>In addition to implementing this interface, this service file should be defined:
 *
 * <p><code>META-INF/services/org.geotools.imageio.netcdf.cv.CoordinateHandlerSpi</code> *
 *
 * <p>Example:<br>
 * <code>org.geotools.imageio.netcdf.cv.ClimatologicalTimeHandlerSPI</code>
 *
 * @author Daniele Romagnoli, GeoSolutions
 */
public final class CoordinateHandlerFinder {

    private static final Logger LOGGER = Logging.getLogger(CoordinateHandlerFinder.class);

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static FactoryRegistry registry;

    /** Do not allows any instantiation of this class. */
    private CoordinateHandlerFinder() {
        // singleton
    }

    /**
     * Finds all available implementations of {@link CoordinateHandlerSpi} which have registered
     * using the services mechanism.
     *
     * @return An unmodifiable {@link Set} of all discovered modules which have registered factories
     */
    public static synchronized Set<CoordinateHandlerSpi> getAvailableHandlers() {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Coordinate Handlers scan");
        }
        // get all CoordinateHandlerSpi implementations
        scanForPlugins();

        return getServiceRegistry()
                .getFactories(CoordinateHandlerSpi.class, true)
                .collect(toUnmodifiableSet());
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(CoordinateHandlerFinder.class);
        if (registry == null) {
            registry =
                    new FactoryCreator(Arrays.asList(new Class<?>[] {CoordinateHandlerSpi.class}));
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

    /**
     * Returns all the {@link CoordinateHandler}s that can handle the supplied {@link
     * CoordinateAxis1D} axis.
     *
     * @param axis is the axis to search a {@link CoordinateHandler} that is able to handle
     * @return an unmodifiable {@link Set} comprising all the {@link CoordinateHandler} that can
     *     handle the {@link CoordinateAxis1D} axis.
     */
    public static synchronized Set<CoordinateHandler> findHandlers(CoordinateAxis axis) {
        final Set<CoordinateHandlerSpi> availableHandlersSpi = getAvailableHandlers();
        final Set<CoordinateHandler> handlers = new HashSet<CoordinateHandler>();
        final Iterator<CoordinateHandlerSpi> it = availableHandlersSpi.iterator();
        while (it.hasNext()) {
            // get the spi
            final CoordinateHandlerSpi spi = it.next();
            // check if we can handle it
            if (spi.canHandle(axis)) {
                handlers.add(spi.createHandler());
            }
        }

        return Collections.unmodifiableSet(handlers);
    }

    /**
     * Returns a {@link CoordinateHandler} that is able to handle a certain coordinate axis.
     *
     * @param axis the object to check for acceptance.
     */
    public static synchronized CoordinateHandler findHandler(CoordinateAxis axis) {
        final Set<CoordinateHandler> formats = findHandlers(axis);
        final Iterator<CoordinateHandler> it = formats.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }
}
