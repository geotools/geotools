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
package org.geotools.coverage.io.impl;

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

import org.geotools.coverage.io.RasterDatasetReader;
import org.geotools.coverage.io.RasterDatasetWriter;
import org.geotools.coverage.io.service.RasterService;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;

/**
 * A class containing static convenience methods for locating <code>RasterStorage</code>s and
 * specific <code>RasterDataset</code>s, and performing simple encoding and decoding.
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 * @todo add caching mechanism
 * @todo add method for removing and creating connections to access
 * @todo fix javadocs
 */
public class RasterIO{
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
    private RasterIO() {
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
    public static boolean findService(
    		final java.util.Map<String, Serializable> params,
    		final boolean read,
    		final Hints hints) {
        for (RasterService service : getAvailableServices()) {

            boolean canProcess = false;
            try {
            	if(read)
            		canProcess = service.canCreateReader(params,hints);
            	else
            		canProcess = service.canCreateWriter(params,hints);
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Error asking " + service.getTitle()
                        + " if it can process request", t);
                // Protect against DataStores that don't carefully code
                // canProcess
                continue;
            }
            if (canProcess) {
                boolean isAvailable = false;
                try {
                    isAvailable = service.isAvailable();
                } catch (Throwable t) {
                    LOGGER.log(Level.WARNING, "Error when checking if " + service.getTitle()
                            + " is available" , t);
                    // Protect against Drivers that don't carefully code
                    // isAvailable
                    continue;
                }
                return true;

            }
        }
        return false;
    }
    
    

    public static RasterDatasetReader aquireReader(
    		final Map<String, Serializable> parameters,
    		final Hints hints, 
    		final ProgressListener listener)throws IOException{
        for (RasterService service : getAvailableServices()) {
        	boolean canProcess = false;
            try {
            	canProcess = service.canCreateReader(parameters,hints);
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Error asking " + service.getTitle()
                        + " if it can process request", t);
                // Protect against DataStores that don't carefully code
                // canProcess
                continue;
            }
            if (canProcess) {
                boolean isAvailable = false;
                try {
                    isAvailable = service.isAvailable();
                } catch (Throwable t) {
                    LOGGER.log(Level.WARNING, "Error when checking if " + service.getTitle()
                            + " is available" , t);
                    // Protect against Drivers that don't carefully code
                    // isAvailable
                    continue;
                }
                return service.createReader(parameters, hints, listener);

            }
        }
        return null;
    }
    
    public static RasterDatasetWriter aquireWriter(
    		final Map<String, Serializable> parameters,
    		final Hints hints, 
    		final ProgressListener listener)throws IOException{
        for (RasterService service : getAvailableServices()) {
        	boolean canProcess = false;
            try {
            	canProcess = service.canCreateReader(parameters,hints);
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Error asking " + service.getTitle()
                        + " if it can process request", t);
                // Protect against DataStores that don't carefully code
                // canProcess
                continue;
            }
            if (canProcess) {
                boolean isAvailable = false;
                try {
                    isAvailable = service.isAvailable();
                } catch (Throwable t) {
                    LOGGER.log(Level.WARNING, "Error when checking if " + service.getTitle()
                            + " is available" , t);
                    // Protect against Drivers that don't carefully code
                    // isAvailable
                    continue;
                }
                return service.createWriter(parameters, hints, listener);

            }
        }
        return null;
    }

    /**
     * Finds all available implementations of {@link RasterService} which have registered using the
     * services mechanism, and that have the appropriate libraries on the class-path.
     * 
     * @return An unmodifiable {@link Set} of all discovered drivers which have registered
     *         factories, and whose available method returns true.
     */
    public static synchronized Set<RasterService> getAvailableServices() {
        // get all RasterService implementations
        scanForPlugins();
        final Iterator<RasterService> it = getServiceRegistry().getServiceProviders(RasterService.class, true);
        final Set<RasterService> drivers = new HashSet<RasterService>();
        while (it.hasNext()) {
            final RasterService spi = (RasterService) it.next();
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
        assert Thread.holdsLock(RasterIO.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { RasterService.class }));
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
     * Returns an array with all available {@link RasterService} implementations.
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
     * @return an array with all available {@link RasterService} implementations.
     */
    public static RasterService[] getAvailableDriversArray() {
        final Set<? extends RasterService> drivers = RasterIO.getAvailableServices();
        final List<RasterService> driverSet = new ArrayList<RasterService>(drivers.size());
        for (Iterator<? extends RasterService> iter = drivers.iterator(); iter.hasNext();) {
            final RasterService element = (RasterService) iter.next();
            if (element.isAvailable())
                driverSet.add(element);
        }
        return (RasterService[]) driverSet.toArray(new RasterService[driverSet.size()]);
    }

    /**
     * Returns all the {@link RasterService}s that can read the supplied {@link URL} url.
     * 
     * @param url
     *                is the object to search a {@link RasterService} that is able to read
     * @param read 
     * @return an unmodifiable {@link Set} comprising all the {@link RasterService} that can read the
     *         {@link URL} url.
     */
    public static Set<RasterService> findServices(
    		final URL url,
    		final boolean read) {
        final Set<? extends RasterService> availaibleDrivers = RasterIO.getAvailableServices();
        final Set<RasterService> drivers = new HashSet<RasterService>();
        final Iterator<? extends RasterService> it = availaibleDrivers.iterator();
        while (it.hasNext()) {
            // get the factory
            final RasterService spi = (RasterService) it.next();
            // check if we can accept it
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("url", url);
            
            //is it available?
            if (!spi.isAvailable())
            	continue;
            try{
	        	if(read)
	        	{
	        		if(spi.canCreateReader(params, null))
	        			drivers.add(spi);
	        	}        			
	        	else{
	        		if(spi.canCreateWriter(params, null))
	        			drivers.add(spi);        		
	        	}
            }catch (Throwable e) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
			}
            
            

        }

        return Collections.unmodifiableSet(drivers);
    }

    /**
     * Returns a {@link RasterService} that is able to read a certain object. If no {@link RasterService} is able
     * to read such an {@link Object} we return an null object.
     * 
     * @param url
     *                the object to check for acceptance.
     * @return a {@link RasterService} that has stated to accept this {@link URL} o or <code>null</code>
     *         in no plugins was able to accept it.
     */
    public static RasterService findService(
    		final URL url,
    		final boolean read) {
        final Set<RasterService> drivers = findServices(url,read);
        final Iterator<RasterService> it = drivers.iterator();
        if (it.hasNext())
            return (RasterService) it.next();
        return null;
    }

}