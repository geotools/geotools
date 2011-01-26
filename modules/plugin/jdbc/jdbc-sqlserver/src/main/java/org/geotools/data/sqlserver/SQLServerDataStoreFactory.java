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

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStore factory for Microsoft SQL Server.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 * @source $URL$
 */
public class SQLServerDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "sqlserver");
    
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
        // no known way to validate a connection, if you know any, please advise
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
    }
    
    /**
     *  Builds up the JDBC url in a jdbc:<database>://<host>:<port>;DatabaseName=<dbname>
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String url = super.getJDBCUrl(params);
        String db = (String) DATABASE.lookUp(params);
        if (db != null) {
            url = url.substring(0, url.lastIndexOf("/")) + (db != null ? ";DatabaseName="+db : "");
        }
        return url;
    }

}
