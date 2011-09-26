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

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bridj.Pointer;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.ogr.bridj.GdalInit;

/**
 * Implementation of the DataStore service provider interface for OGR.
 * 
 * @author Andrea Aime, GeoSolution
 * 
 * 
 * @source $URL$
 * @version $Id$
 */
@SuppressWarnings("rawtypes")
public class OGRDataStoreFactory implements DataStoreFactorySpi {
    public static final Param OGR_NAME = new Param("OGRName", String.class,
            "Name of the file, or data source to try and open", true);

    public static final Param OGR_DRIVER_NAME = new Param(
            "OGRDriverName",
            String.class,
            "Name of the OGR driver to be used. Required to create a new data source, optional when opening an existing one",
            false);

    public static final Param NAMESPACEP = new Param("namespace", URI.class,
            "uri to a the namespace", false); // not required

    static {
        GdalInit.init();

        // perform OGR format registration once
        if (OGRGetDriverCount() == 0) {
            OGRRegisterAll();
        }
    }

    /**
     * Caches opened data stores. TODO: is this beneficial or problematic? It's a static cache, so
     * opening a lot of datastore (thousands, hundreds of thousands...) may become a memory problem.
     * Plus OGR is not designed to be thread safe.
     */
    private Map liveStores = new HashMap();

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
        ds = new OGRDataStore(ogrName, ogrDriver, namespace);

        return ds;
    }

    public String getDisplayName() {
        return "Shapefile";
    }

    /**
     * Describes the type of data the datastore returned by this factory works with.
     * 
     * @return String a human readable description of the type of restore supported by this
     *         datastore.
     */
    public String getDescription() {
        return "OGR data store for shapefiles";
    }

    /**
     * Test to see if this datastore is available, if it has all the appropriate libraries to
     * construct a datastore. This datastore just returns true for now.
     * 
     * @return <tt>true</tt> if and only if this factory is available to create DataStores.
     * 
     * @task REVISIT: I'm just adding this method to compile, maintainer should revisit to check for
     *       any libraries that may be necessary for datastore creations.
     */
    public boolean isAvailable() {
        try {
            return OGRGetDriverCount() > 0;
        } catch (Throwable t) {
            return false;
        }
    }

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
        Pointer dataset = OGROpenShared(pointerToCString(ogrName), 0, null);
        if (dataset != null) {
            OGRReleaseDataSource(dataset);
            return true;
        }

        Pointer driver = OGRGetDriverByName(pointerToCString(driverName));
        if (driver != null) {
            return true;
        }

        return false;

    }

    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }

}
