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
package org.geotools.data.jdbc.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;

/**
 * Enable programs to find all available {@link DataSourceFactorySpi} implementations.
 * 
 * <p>
 * In addition to implementing this interface data souces should have a services file:<br/><code>META-INF/services/org.geotools.data.jdbc.DataSourceFactorySpi</code>
 * </p>
 * 
 * <p>
 * The file should contain a single line which gives the full name of the implementing class.
 * </p>
 * 
 * <p>
 * Example:<br/><code>org.geotools.data.jdbc.DBCPDataSourceFactory</code>
 * </p>
 * 
 *
 * @source $URL$
 */
public final class DataSourceFinder {
    /** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc");

    /**
     * The service registry for this manager. Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    // Singleton pattern
    private DataSourceFinder() {
    }

    /**
     * Checks each available datasource implementation in turn and returns the first one which
     * claims to support the resource identified by the params object.
     * 
     * @param params
     *            A Map object which contains a defenition of the resource to connect to. for file
     *            based resources the property 'url' should be set within this Map.
     * 
     * @return The first datasource which claims to process the required resource, returns null if
     *         none can be found.
     * 
     * @throws IOException
     *             If a suitable loader can be found, but it can not be attached to the specified
     *             resource without errors.
     */
    public static synchronized DataSource getDataSource(Map params) throws IOException {
        Iterator ps = getAvailableDataSources();
        DataSourceFactorySpi fac;
        while (ps.hasNext()) {
            fac = (DataSourceFactorySpi) ps.next();

            try {
                if (fac.canProcess(params)) {
                    return fac.createDataSource(params);
                }
            } catch (Throwable t) {
                /** The logger for the filter module. */
                LOGGER.log(Level.WARNING, "Could not acquire " + fac.getDescription() + ":" + t, t);
                // Protect against DataStores that don't carefully
                // code canProcess

            }
        }

        return null;
    }

    /**
     * Checks each available datasource implementation in turn and returns the first one which
     * claims to support the resource identified by the params object.
     * 
     * @param params
     *            A Map object which contains a defenition of the resource to connect to. for file
     *            based resources the property 'url' should be set within this Map.
     * 
     * @return The first datasource which claims to process the required resource, returns null if
     *         none can be found.
     * 
     * @throws IOException
     *             If a suitable loader can be found, but it can not be attached to the specified
     *             resource without errors.
     */
    public static synchronized UnWrapper getUnWrapper(Connection conn) throws IOException {
        Iterator ps = getUnWrappers();
        UnWrapper uw;
        while (ps.hasNext()) {
            uw = (UnWrapper) ps.next();

            try {
                if (uw.canUnwrap(conn)) {
                    return uw;
                }
            } catch (Throwable t) {
                /** The logger for the filter module. */
                LOGGER.log(Level.WARNING, "Could not test  " + uw
                        + " for unwrapping abilities agaist " + conn, t);
                // Protect against DataStores that don't carefully
                // code canProcess

            }
        }

        return null;
    }
    
    /**
     * Checks each available {@link UnWrapper} implementation in turn and returns the first one which
     * claims to support the resource identified by the params object.
     * 
     * @param params
     *            A Map object which contains a defenition of the resource to connect to. for file
     *            based resources the property 'url' should be set within this Map.
     * 
     * @return The first datasource which claims to process the required resource, returns null if
     *         none can be found.
     * 
     * @throws IOException
     *             If a suitable loader can be found, but it can not be attached to the specified
     *             resource without errors.
     */
    public static synchronized UnWrapper getUnWrapper(Statement st) throws IOException {
        Iterator ps = getUnWrappers();
        UnWrapper uw;
        while (ps.hasNext()) {
            uw = (UnWrapper) ps.next();

            try {
                if (uw.canUnwrap(st)) {
                    return uw;
                }
            } catch (Throwable t) {
                /** The logger for the filter module. */
                LOGGER.log(Level.WARNING, "Could not test  " + uw
                        + " for unwrapping abilities agaist " + st, t);
                // Protect against DataStores that don't carefully
                // code canProcess

            }
        }

        return null;
    }

    /**
     * Finds all implemtaions of DataStoreFactory which have registered using the services
     * mechanism, and that have the appropriate libraries on the classpath.
     * 
     * @return An iterator over all discovered datastores which have registered factories, and whose
     *         available method returns true.
     */
    public static synchronized Iterator getAvailableDataSources() {
        Set availableDS = new HashSet();
        Iterator it = getServiceRegistry().getServiceProviders(DataSourceFactorySpi.class, null, null);
        DataSourceFactorySpi dsFactory;
        while (it.hasNext()) {
            dsFactory = (DataSourceFactorySpi) it.next();

            if (dsFactory.isAvailable()) {
                availableDS.add(dsFactory);
            }
        }

        return availableDS.iterator();
    }

    /**
     * Finds all implemtaions of DataStoreFactory which have registered using the services
     * mechanism, and that have the appropriate libraries on the classpath.
     * 
     * @return An iterator over all discovered datastores which have registered factories, and whose
     *         available method returns true.
     */
    public static synchronized Iterator getUnWrappers() {
        Set availableDS = new HashSet();
        return getServiceRegistry().getServiceProviders(UnWrapper.class, null, null);
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(DataSourceFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] { DataSourceFactorySpi.class,
                    UnWrapper.class }));
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
