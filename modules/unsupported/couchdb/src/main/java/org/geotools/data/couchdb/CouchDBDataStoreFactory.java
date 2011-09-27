/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBDataStoreFactory extends AbstractDataStoreFactory {
    
    public static final Param SERVER_URL = new Param("serverURL", String.class, "Server URL", true);
    public static final Param DB_NAME = new Param("dbName", String.class, "Database Name", true);

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        CouchDBDataStore dataStore = new CouchDBDataStore();
        dataStore.setCouchURL((String)SERVER_URL.lookUp(params));
        dataStore.setDatabaseName((String)DB_NAME.lookUp(params));
        try {
            dataStore.init();
        } catch (Exception ex) {
            throw new IOException("Error initializing datastore",ex);
        }
        return dataStore;
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        // @todo - this could be possible
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getDisplayName() {
        return "CouchDB Datastore";
    }

    public String getDescription() {
        return "Datastore backed by CouchDB";
    }

    public Param[] getParametersInfo() {
        return new Param[] {
            SERVER_URL,
            DB_NAME
        };
    }
}
