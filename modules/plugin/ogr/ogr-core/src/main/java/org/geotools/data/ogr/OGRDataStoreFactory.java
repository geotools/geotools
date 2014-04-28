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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.logging.Logging;

/**
 * Implementation of the DataStore service provider interface for OGR.
 * 
 * @author Andrea Aime, GeoSolution
 * 
 * 
 *
 * @source $URL$
 * @version $Id$
 */
@SuppressWarnings("rawtypes")
public abstract class OGRDataStoreFactory implements DataStoreFactorySpi {

    protected static Logger LOGGER = Logging.getLogger("org.geotools.data.ogr");

    public static final Param OGR_NAME = new Param("DatasourceName", String.class,
            "Name of the file, or data source to try and open", true);

    public static final Param OGR_DRIVER_NAME = new Param(
            "DriverName",
            String.class,
            "Name of the OGR driver to be used. Required to create a new data source, optional when opening an existing one",
            false);

    public static final Param NAMESPACEP = new Param("namespace", URI.class,
            "uri to a the namespace", false); // not required

    /**
     * Caches opened data stores. TODO: is this beneficial or problematic? It's a static cache, so
     * opening a lot of datastore (thousands, hundreds of thousands...) may become a memory problem.
     * Plus OGR is not designed to be thread safe.
     */
    private Map liveStores = new HashMap();

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
        DataStore ds = null;
        if (!liveStores.containsKey(params)) {

            URL url = null;
            try {
                ds = createNewDataStore(params);
                liveStores.put(params, ds);
            } catch (MalformedURLException mue) {
                throw new DataSourceException("Unable to attatch datastore to " + url, mue);
            }
        } else
            ds = (DataStore) liveStores.get(params);
        return ds;
    }

    /**
     * Not implemented yet.
     * 
     * @param params
     * 
     * @throws IOException
     * 
     * @throws IOException DOCUMENT ME!
     * @throws UnsupportedOperationException
     */
    public DataStore createNewDataStore(Map params) throws IOException {

        DataStore ds = null;

        String ogrName = (String) OGR_NAME.lookUp(params);
        String ogrDriver = (String) OGR_DRIVER_NAME.lookUp(params);
        URI namespace = (URI) NAMESPACEP.lookUp(params);
        ds = new OGRDataStore(ogrName, ogrDriver, namespace, createOGR());

        return ds;
    }

    public String getDisplayName() {
        return "OGR";
    }

    /**
     * Describes the type of data the datastore returned by this factory works with.
     * 
     * @return String a human readable description of the type of restore supported by this
     *         datastore.
     */
    public String getDescription() {
        return "Uses OGR as a data source";
    }

    /**
     * Test to see if this datastore is available, if it has all the appropriate libraries to
     * construct a datastore.
     * 
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     * 
     * @task REVISIT: I'm just adding this method to compile, maintainer should revisit to check for
     *       any libraries that may be necessary for datastore creations.
     */
    public final boolean isAvailable() {
        return isAvailable(true);
    }

    /**
     * Performs the available test specifying how to handle errors. 
     * <p>
     * Specifying true for <tt>handleError</tt> will cause any exceptions to be caught and logged, 
     * and return false
     * </p>
     */
    public final boolean isAvailable(boolean handleError) {
        try {
            return doIsAvailable();
        } catch (Throwable t) {
            if (handleError) {
                LOGGER.log(Level.FINE, "Error initializing GDAL/OGR library", t);
                return false;
            }
            else {
                throw new RuntimeException(t);
            }
        }
    }

    /**
     * Performs the actual test to see if the OGR library and this datastore is available.
     * <p>
     * Implemetnations of this method should not attempt to handle any fatal exceptions.
     * </p>
     */
    protected abstract boolean doIsAvailable() throws Throwable;

    /**
     * Describe parameters.
     * 
     * 
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] { OGR_NAME, OGR_DRIVER_NAME, NAMESPACEP };
    }

    /**
     * Assume we can process an ogrName if the ogrName exists and can be opened, or if the specified
     * driver does exist.
     * 
     * @param ogrName
     * @param driverName
     * @return
     */
    public boolean canProcess(String ogrName, String driverName) {
        OGR ogr = createOGR();
        Object dataset = ogr.OpenShared(ogrName, 0);

        if (dataset != null) {
            //OGRReleaseDataSource(dataset);
            ogr.DataSourceRelease(dataset);
            return true;
        }

        if (driverName != null) {
            try {
                Object driver = ogr.GetDriverByName(driverName);
        
                if (driver != null) {
                    return true;
                }
            }
            catch(Exception e) {
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
