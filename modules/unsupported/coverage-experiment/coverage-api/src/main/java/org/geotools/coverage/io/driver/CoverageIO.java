/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.driver;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.driver.Driver.DriverOperation;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;

/**
 * A class containing static convenience methods for locating <code>CoverageAccess</code>s and
 * specific <code>CoverageSource</code>s, and performing simple encoding and decoding.
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 * @todo add caching mechanism
 * @todo add method for removing and creating connections to access
 * @todo fix javadocs
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/driver/CoverageIO.java $
 */
public class CoverageIO{
    /** The {@link Logger}. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverage.io");

    /**
     * The service registry for this manager. Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Private constructor, this class cannot be instantiated nor subclassed.
     */
    private CoverageIO() {
    }

    /**
     * Test to see if this factory is suitable for processing the data pointed to by the params map.
     * 
     * <p>
     * If this datasource requires a number of parameters then this method should check that they
     * are all present and that they are all valid. If the datasource is a file reading data source
     * then the extensions or mime types of any files specified should be checked. For example, a
     * Shapefile datasource should check that the url param ends with shp, such tests should be case
     * insensitive.
     * </p>
     * 
     * @param params
     *                The full set of information needed to construct a live data source.
     * 
     * @return boolean true if and only if this factory can process the resource indicated by the
     *         param set and all the required params are present.
     */
    public static boolean canConnect(java.util.Map<String, Serializable> params) {
        for (Driver driver : getAvailableDrivers()) {

            boolean canProcess = false;
            try {
                canProcess = driver.canProcess(DriverOperation.CONNECT,params);
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Error asking " + driver.getTitle()
                        + " if it can process request", t);
                // Protect against DataStores that don't carefully code
                // canProcess
                continue;
            }
            if (canProcess) {
                boolean isAvailable = false;
                try {
                    isAvailable = driver.isAvailable();
                } catch (Throwable t) {
                    LOGGER.log(Level.WARNING, "Error when checking if " + driver.getTitle()
                            + " is available" , t);
                    // Protect against Drivers that don't carefully code
                    // isAvailable
                    continue;
                }
                if (isAvailable) {
                    try {
                        if( driver.canProcess(DriverOperation.CONNECT,params))
                        	return true;
                    } catch (Exception couldNotConnect) {
                        LOGGER.log(Level.WARNING, driver.getTitle()
                                + " could not connect", couldNotConnect);
                    }
                } 
            }
        }
        return false;
    }
    
    

    public static CoverageAccess connect(Map<String, Serializable> params,Hints hints, final ProgressListener listener)throws IOException{
        for (Driver driver : getAvailableDrivers()) {

            boolean canProcess = false;
            try {
                canProcess = driver.canProcess(DriverOperation.CONNECT,params);
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Error asking " + driver.getTitle()
                        + " if it can process request", t);
                // Protect against DataStores that don't carefully code
                // canProcess
                continue;
            }
            if (canProcess) {
                boolean isAvailable = false;
                try {
                    isAvailable = driver.isAvailable();
                } catch (Throwable t) {
                    LOGGER.log(Level.WARNING, "Error when checking if " + driver.getTitle()
                            + " is available" , t);
                    // Protect against Drivers that don't carefully code
                    // isAvailable
                    continue;
                }
                if (isAvailable) {
                    try {
                        return driver.process(DriverOperation.CONNECT,params, hints, listener);
                    } catch (IOException couldNotConnect) {
                        LOGGER.log(Level.WARNING, driver.getTitle()
                                + " could not connect", couldNotConnect);
                    }
                } 
            }
        }
        return null;
    }
    public static CoverageAccess connect(Map<String, Serializable> params)
            throws IOException {
    	return CoverageIO.connect(params,null,null);
    }

    /**
     * Finds all available implementations of {@link Driver} which have registered using the
     * services mechanism, and that have the appropriate libraries on the class-path.
     * 
     * @return An unmodifiable {@link Set} of all discovered drivers which have registered
     *         factories, and whose available method returns true.
     */
    public static synchronized Set<Driver> getAvailableDrivers() {
        // get all Driver implementations
        scanForPlugins();
        final Iterator<Driver> it = getServiceRegistry().getServiceProviders(Driver.class, true);
        final Set<Driver> drivers = new HashSet<Driver>();
        while (it.hasNext()) {
            final Driver spi = (Driver) it.next();
            if (spi.isAvailable())
                drivers.add(spi);
        }
        return Collections.unmodifiableSet(drivers);
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(CoverageIO.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { Driver.class }));
        }
        return registry;
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the
     * application class path can theoretically change, or additional plug-ins may become available.
     * Rather than re-scanning the class-path on every invocation of the API, the class path is
     * scanned automatically only on the first invocation. Clients can call this method to prompt a
     * re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public static synchronized void scanForPlugins() {
        getServiceRegistry().scanForPlugins();
    }

    /**
     * Returns an array with all available {@link Driver} implementations.
     * 
     * <p>
     * It can be used together basic information about all the available {@link GridCoverage}
     * plugins. Note that this method finds all the implemented plugins but returns only the
     * available one.
     * 
     * <p>
     * A plugin could be implemented but not available due to missing dependencies.
     * 
     * 
     * @return an array with all available {@link Driver} implementations.
     */
    public static Driver[] getAvailableDriversArray() {
        final Set<? extends Driver> drivers = CoverageIO.getAvailableDrivers();
        final List<Driver> driverSet = new ArrayList<Driver>(drivers.size());
        for (Iterator<? extends Driver> iter = drivers.iterator(); iter.hasNext();) {
            final Driver element = (Driver) iter.next();
            if (element.isAvailable())
                driverSet.add(element);
        }
        return (Driver[]) driverSet.toArray(new Driver[driverSet.size()]);
    }

    /**
     * Returns all the {@link Driver}s that can read the supplied {@link URL} url.
     * 
     * @param url
     *                is the object to search a {@link Driver} that is able to read
     * @return an unmodifiable {@link Set} comprising all the {@link Driver} that can read the
     *         {@link URL} url.
     */
    public static Set<Driver> findDrivers(URL url) {
        final Set<? extends Driver> availaibleDrivers = CoverageIO.getAvailableDrivers();
        final Set<Driver> drivers = new HashSet<Driver>();
        final Iterator<? extends Driver> it = availaibleDrivers.iterator();
        while (it.hasNext()) {
            // get the factory
            final Driver spi = (Driver) it.next();
            // check if we can accept it
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("url", url);
            if (spi.isAvailable() && spi.canProcess(DriverOperation.CONNECT,params))
                drivers.add(spi);

        }

        return Collections.unmodifiableSet(drivers);
    }

    /**
     * Returns a {@link Driver} that is able to read a certain object. If no {@link Driver} is able
     * to read such an {@link Object} we return an null object.
     * 
     * @param url
     *                the object to check for acceptance.
     * @return a {@link Driver} that has stated to accept this {@link URL} o or <code>null</code>
     *         in no plugins was able to accept it.
     */
    public static Driver findDriver(URL url) {
        final Set<Driver> drivers = findDrivers(url);
        final Iterator<Driver> it = drivers.iterator();
        if (it.hasNext())
            return (Driver) it.next();
        return null;
    }

}
