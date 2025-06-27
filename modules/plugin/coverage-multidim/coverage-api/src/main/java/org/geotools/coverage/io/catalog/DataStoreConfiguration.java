/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.catalog;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.Repository;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;

/**
 * A simple class storing DataStore connection properties such as the FactorySPI used to create that datastore, as well
 * as the connections parameters. In the beginning, Multidim coverages were holding granules index within an H2 database
 * for each NetCDF/GRIB file.
 *
 * <p>Starting with 14.x, it is also possible to deal with a PostGIS DB to be shared across different readers/files. A
 * new attribute LOCATION is used to distinguish granules coming from specific file/reader instances.
 *
 * <p>Starting with 19.x, it is also possible to use a {@link Repository} providing an externally managed store
 * identified by name
 *
 * @author Daniele Romagnoli, GeoSolutions
 */
public class DataStoreConfiguration {

    private static final H2DataStoreFactory INTERNAL_STORE_SPI = new H2DataStoreFactory();

    /** The Datastore factory spi used to create the Datastore instance */
    private DataStoreFactorySpi datastoreSpi;

    /** The connection params */
    private Map<String, Serializable> params;

    /**
     * a boolean stating whether the granules index is stored "the classic way", which is using an internal H2 DB for
     * each file or it's a shared DB.
     */
    private boolean shared = false;

    private String storeName;

    /** Default instance is using a H2 DB for each file */
    public DataStoreConfiguration(Map<String, Serializable> datastoreParams) {
        this(INTERNAL_STORE_SPI, datastoreParams);
    }

    public DataStoreConfiguration(DataStoreFactorySpi datastoreSpi, Map<String, Serializable> datastoreParams) {
        this.datastoreSpi = datastoreSpi;
        this.params = datastoreParams;
    }

    public DataStoreConfiguration(String storeName) {
        this.storeName = storeName;
        this.shared = true;
    }

    public DataStoreFactorySpi getDatastoreSpi() {
        return datastoreSpi;
    }

    public void setDatastoreSpi(DataStoreFactorySpi datastoreSpi) {
        this.datastoreSpi = datastoreSpi;
    }

    public Map<String, Serializable> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Serializable> params) {
        this.params = params;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getStoreName() {
        return storeName;
    }

    /** Return default params for the 1 File <-> 1 H2 DB classic configuration. */
    public static Map<String, Serializable> getDefaultParams(String database, File parentLocation) {
        Utilities.ensureNonNull("database", database);
        Utilities.ensureNonNull("parentLocation", parentLocation);
        final Map<String, Serializable> params = new HashMap<>();
        final String url = URLs.fileToUrl(parentLocation).toExternalForm();
        String updatedDB;
        try {
            updatedDB = "file:" + new File(URLs.urlToFile(new URL(url)), database).getPath();
            params.put("ParentLocation", url);
            params.put("database", updatedDB);
            params.put("dbtype", "h2");
            params.put("user", "geotools");
            params.put("passwd", "geotools");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        return params;
    }
}
