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
package org.geotools.data.sqlserver;

import java.io.IOException;
import java.util.Map;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStore factory for Microsoft SQL Server.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 * @source $URL$
 */
public class SQLServerDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "sqlserver");
    
    /** parameter for using integrated security, only works on windows, ignores the user and password parameters, the current windows user account is used for login*/
    public static final Param INTSEC = new Param("Integrated Security", Boolean.class, "Login as current windows user account. Works only in windows. Ignores user and password settings.", false, new Boolean(false)); 
    
    /** Metadata table providing information about primary keys **/
    public static final Param GEOMETRY_METADATA_TABLE = new Param("Geometry metadata table", String.class,
            "The optional table containing geometry metadata (geometry type and srid). Can be expressed as 'schema.name' or just 'name'", false);
    
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new SQLServerDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    public String getDescription() {
        return "Microsoft SQL Server";
    }
    
    @Override
    protected String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    protected String getValidationQuery() {
        return "select 1";
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(INTSEC.key, INTSEC);
        parameters.put(GEOMETRY_METADATA_TABLE.key, GEOMETRY_METADATA_TABLE);
    }
    
    /**
     *  Builds up the JDBC url in a jdbc:<database>://<host>:<port>;DatabaseName=<dbname>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String url = super.getJDBCUrl(params);
        String db = (String) DATABASE.lookUp(params);
        Boolean intsec = (Boolean) INTSEC.lookUp(params);
        if (db != null) {
            url = url.substring(0, url.lastIndexOf("/")) + (db != null ? ";DatabaseName="+db : "");
        }

        if (intsec != null && intsec.booleanValue()) {
        	url = url + ";integratedSecurity=true";
        }
        
        return url;
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        SQLServerDialect dialect = (SQLServerDialect) dataStore.getSQLDialect();
    	
    	// check the geometry metadata table
        String metadataTable = (String) GEOMETRY_METADATA_TABLE.lookUp(params);
        dialect.setGeometryMetadataTable(metadataTable);
        
        return dataStore;
    }
    
}
