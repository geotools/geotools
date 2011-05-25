/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mysql;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;


/**
 * DataStoreFactory for MySQL database.
 *
 * @author David Winslow, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class MySQLDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true,"mysql");
    /** Default port number for MYSQL */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 3306);
    /** Storage engine to use when creating tables */
    public static final Param STORAGE_ENGINE = 
        new Param("storage engine", String.class, "Storage Engine", false, "MyISAM" );
    
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        //return new MySQLDialectPrepared(dataStore);
        return new MySQLDialectBasic(dataStore);
    }

    public String getDisplayName() {
        return "MySQL";
    }
    
    protected String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    public String getDescription() {
        return "MySQL Database";
    }

    @Override
    protected String getValidationQuery() {
        return "select version()";
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(PORT.key, PORT);
        parameters.put(STORAGE_ENGINE.key, STORAGE_ENGINE);
        
        parameters.remove(SCHEMA.key);
    }
    
    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        String storageEngine = (String) STORAGE_ENGINE.lookUp( params );
        if (storageEngine == null) {
            storageEngine = (String) STORAGE_ENGINE.sample;
        }
        SQLDialect dialect = dataStore.getSQLDialect();
        if (dialect instanceof MySQLDialectBasic) {
            ((MySQLDialectBasic)dialect).setStorageEngine(storageEngine);
        }
        else {
            ((MySQLDialectPrepared)dialect).setStorageEngine(storageEngine);
        }
        
        return dataStore;
    }
}
