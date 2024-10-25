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
package org.geotools.api.data;

import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Enable programs to find all available datastore implementations.
 *
 * <p>In order to be located by this finder datasources must provide an implementation of the {@link
 * DataStoreFactorySpi} interface.
 *
 * <p>In addition to implementing this interface datasouces should have a services file:<br>
 * <code>META-INF/services/org.geotools.api.data.DataStoreFactorySpi</code>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>Example:<br>
 * <code>org.geotools.data.mytype.MyTypeDataStoreFacotry</code>
 */
public final class DataStoreFinder {

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    // Singleton pattern
    private DataStoreFinder() {}

    /*
     * Implementation note: because this class and DataStoreFinder call each other, all
     * non-thread-safe methods are synchronized on a common object (DataAccessFinder.class)
     * in order to prevent potential deadlocks.
     */

    /**
     * Checks each available datasource implementation in turn and returns the first one which
     * claims to support the resource identified by the params object.
     *
     * @param params A Map object which contains a defenition of the resource to connect to. for
     *     file based resources the property 'url' should be set within this Map.
     * @return The first datasource which claims to process the required resource, returns null if
     *     none can be found.
     * @throws IOException If a suitable loader can be found, but it can not be attached to the
     *     specified resource without errors.
     */
    public static DataStore getDataStore(Map<String, ?> params) throws IOException {
        synchronized (DataAccessFinder.class) {
            Iterator<DataStoreFactorySpi> ps = getAvailableDataStores();
            DataAccess<? extends FeatureType, ? extends Feature> dataStore =
                    DataAccessFinder.getDataStore(params, ps);
            return (DataStore) dataStore;
        }
    }

    /**
     * Finds all implemtaions of DataStoreFactory which have registered using the services
     * mechanism, regardless weather it has the appropriate libraries on the classpath.
     *
     * @return An iterator over all discovered datastores which have registered factories
     */
    public static Iterator<DataStoreFactorySpi> getAllDataStores() {
        synchronized (DataAccessFinder.class) {
            return DataAccessFinder.getAllDataStores(
                    getServiceRegistry(), DataStoreFactorySpi.class);
        }
    }

    /**
     * Finds all implemtaions of DataStoreFactory which have registered using the services
     * mechanism, and that have the appropriate libraries on the classpath.
     *
     * @return An iterator over all discovered datastores which have registered factories, and whose
     *     available method returns true.
     */
    public static Iterator<DataStoreFactorySpi> getAvailableDataStores() {
        synchronized (DataAccessFinder.class) {
            FactoryRegistry serviceRegistry = getServiceRegistry();
            Set<DataStoreFactorySpi> availableDS =
                    DataAccessFinder.getAvailableDataStores(
                            serviceRegistry, DataStoreFactorySpi.class);
            return availableDS.iterator();
        }
    }

    /**
     * Returns the service registry. The registry will be created the first time this method is
     * invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(DataAccessFinder.class);
        if (registry == null) {
            registry =
                    new FactoryCreator(Arrays.asList(new Class<?>[] {DataStoreFactorySpi.class}));
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
    public static void scanForPlugins() {
        synchronized (DataAccessFinder.class) {
            getServiceRegistry().scanForPlugins();
        }
    }

    /** Resets the factory finder and prepares for a new full scan of the SPI subsystems */
    public static void reset() {
        FactoryRegistry copy = registry;
        registry = null;
        if (copy != null) {
            copy.deregisterAll();
        }
    }

    /**
     * Programmatically registers a store. Mostly useful for tests, normal store registration should
     * go through the SPI subsystem (META-INF/services files).
     */
    public static void registerFactrory(DataStoreFactorySpi factorySpi) {
        synchronized (DataAccessFinder.class) {
            getServiceRegistry().registerFactory(factorySpi);
        }
    }

    /**
     * Programmatically deregisters a store. Mostly useful for tests, normal store registration
     * should go through the SPI subsystem (META-INF/services files).
     */
    public static void deregisterFactrory(DataStoreFactorySpi factorySpi) {
        synchronized (DataAccessFinder.class) {
            getServiceRegistry().deregisterFactory(factorySpi);
        }
    }
}
