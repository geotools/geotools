/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.logging.Logging;

/**
 * Implementation of the DataStore service provider interface for OGR.
 *
 * @author Andrea Aime, GeoSolution
 * @version $Id$
 */
@SuppressWarnings("rawtypes")
public abstract class OGRDataStoreFactory implements DataStoreFactorySpi {

    protected static Logger LOGGER = Logging.getLogger(OGRDataStoreFactory.class);

    public static final Param OGR_NAME =
            new Param(
                    "DatasourceName",
                    String.class,
                    "Name of the file, or data source to try and open",
                    true);

    public static final Param OGR_DRIVER_NAME =
            new Param(
                    "DriverName",
                    String.class,
                    "Name of the OGR driver to be used. Required to create a new data source, optional when opening an existing one",
                    false,
                    null);

    public static final Param NAMESPACEP =
            new Param("namespace", URI.class, "uri to a the namespace", false); // not required

    protected static final Integer DEFAULT_MAXCONN = Integer.valueOf(20);

    /** Maximum number of connections in the connection pool */
    public static final Param MAXCONN =
            new Param(
                    "max connections",
                    Integer.class,
                    "maximum number of pooled data source connections",
                    false,
                    DEFAULT_MAXCONN);

    protected static final Integer DEFAULT_MINCONN = Integer.valueOf(1);

    /** Minimum number of connections in the connection pool */
    public static final Param MINCONN =
            new Param(
                    "min connections",
                    Integer.class,
                    "minimum number of pooled data source connection connection",
                    false,
                    DEFAULT_MINCONN);

    protected static final int DEFAULT_MAXWAIT = 20;

    /** Maximum amount of time the pool will wait when trying to grab a new connection * */
    public static final Param MAXWAIT =
            new Param(
                    "Connection timeout",
                    Integer.class,
                    "number of seconds the pool will wait before timing out attempting to get a new data source (default, 20 seconds)",
                    false,
                    DEFAULT_MAXWAIT);

    protected static final int DEFAULT_EVICTABLE_TIME = 300;

    /** Min time for a connection to be idle in order to be evicted * */
    public static final Param MIN_EVICTABLE_TIME =
            new Param(
                    "Max data source idle time",
                    Integer.class,
                    "number of seconds a data source needs to stay idle for the evictor to consider closing it",
                    false,
                    DEFAULT_EVICTABLE_TIME);

    public static final int DEFAULT_EVICTOR_TESTS_PER_RUN = 3;

    /** Number of connections checked during a single evictor run * */
    public static final Param EVICTOR_TESTS_PER_RUN =
            new Param(
                    "Evictor tests per run",
                    Integer.class,
                    "number of data source checked by the idle connection evictor for each of its runs (defaults to 3)",
                    false,
                    DEFAULT_EVICTOR_TESTS_PER_RUN);

    public static final boolean DEFAULT_PRIME_DATASOURCE = false;

    /** Whether to try to initialize a datasource with a full data read before using it* */
    public static final Param PRIME_DATASOURCE =
            new Param(
                    "Prime DataSources",
                    Boolean.class,
                    "Performs a full data read on data source creation, in some formats this generates a in memory cache, or a spatial index (check the OGR documentation for details)",
                    false,
                    DEFAULT_PRIME_DATASOURCE);

    static Boolean AVAILABLE = null;

    protected abstract OGR createOGR();

    public boolean canProcess(Map params) {
        boolean accept = false;
        String ogrName = null;
        String ogrDriver = null;
        try {
            ogrName = (String) OGR_NAME.lookUp(params);
        } catch (IOException ioe) {
            // yes, I am eating this
        }
        try {
            ogrDriver = (String) OGR_DRIVER_NAME.lookUp(params);
        } catch (IOException ioe) {
            // yes, I am eating this
        }

        accept = canProcess(ogrName, ogrDriver);
        return accept;
    }

    public DataStore createDataStore(Map params) throws IOException {
        return createNewDataStore(params);
    }

    /** Not implemented yet. */
    public DataStore createNewDataStore(Map params) throws IOException {

        DataStore ds;

        String ogrName = (String) OGR_NAME.lookUp(params);
        String ogrDriver = (String) OGR_DRIVER_NAME.lookUp(params);
        URI namespace = (URI) NAMESPACEP.lookUp(params);
        OGR ogr = createOGR();
        ds =
                new OGRDataStore(
                        ogrName,
                        ogrDriver,
                        namespace,
                        ogr,
                        new OGRDataSourcePool(ogr, ogrName, ogrDriver, params));

        return ds;
    }

    public String getDisplayName() {
        return "OGR";
    }

    /**
     * Describes the type of data the datastore returned by this factory works with.
     *
     * @return String a human readable description of the type of restore supported by this
     *     datastore.
     */
    public String getDescription() {
        return "Uses OGR as a data source";
    }

    /**
     * Test to see if this datastore is available, if it has all the appropriate libraries to
     * construct a datastore.
     *
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     * @task REVISIT: I'm just adding this method to compile, maintainer should revisit to check for
     *     any libraries that may be necessary for datastore creations.
     */
    public final boolean isAvailable() {
        return isAvailable(true);
    }

    /**
     * Performs the available test specifying how to handle errors.
     *
     * <p>Specifying true for <tt>handleError</tt> will cause any exceptions to be caught and
     * logged, and return false
     */
    public final boolean isAvailable(boolean handleError) {
        if (AVAILABLE == null) {
            try {
                AVAILABLE = doIsAvailable();
            } catch (Throwable t) {
                if (handleError) {
                    LOGGER.log(Level.WARNING, "Error initializing GDAL/OGR library", t);
                    return false;
                } else {
                    throw new RuntimeException(t);
                }
            }
        }

        return AVAILABLE;
    }

    /**
     * Performs the actual test to see if the OGR library and this datastore is available.
     *
     * <p>Implementations of this method should not attempt to handle any fatal exceptions.
     */
    protected abstract boolean doIsAvailable() throws Throwable;

    /**
     * Describe parameters.
     *
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] {
            OGR_NAME,
            OGR_DRIVER_NAME,
            NAMESPACEP,
            MAXCONN,
            MINCONN,
            MAXWAIT,
            MIN_EVICTABLE_TIME,
            EVICTOR_TESTS_PER_RUN,
            PRIME_DATASOURCE
        };
    }

    /**
     * Assume we can process an ogrName if the ogrName exists and can be opened, or if the specified
     * driver does exist.
     */
    public boolean canProcess(String ogrName, String driverName) {
        OGR ogr = createOGR();
        if (ogrName == null) {
            return false;
        }
        Object dataset = ogr.OpenShared(ogrName, 0);

        if (dataset != null) {
            // OGRReleaseDataSource(dataset);
            ogr.DataSourceRelease(dataset);
            return true;
        }

        if (driverName != null) {
            try {
                Object driver = ogr.GetDriverByName(driverName);

                if (driver != null) {
                    return true;
                }
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Error loading driver", e);
            }
        }

        return false;
    }

    public Set<String> getAvailableDrivers() {
        OGR ogr = createOGR();

        int count = ogr.GetDriverCount();
        Set<String> result = new HashSet<String>();
        for (int i = 0; i < count; i++) {
            Object driver = ogr.GetDriver(i);
            String name = ogr.DriverGetName(driver);
            result.add(name);
        }

        return result;
    }

    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
