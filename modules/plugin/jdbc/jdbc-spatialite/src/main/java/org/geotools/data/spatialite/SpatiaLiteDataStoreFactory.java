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
package org.geotools.data.spatialite;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStoreFactory for SpatiaLite database.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL$
 */
public class SpatiaLiteDataStoreFactory extends JDBCDataStoreFactory {

  
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "spatialite");
    
    /** optional user parameter */
    public static final Param USER = new Param(JDBCDataStoreFactory.USER.key, JDBCDataStoreFactory.USER.type, 
            JDBCDataStoreFactory.USER.description, false, JDBCDataStoreFactory.USER.sample);
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new SpatiaLiteDialect( dataStore );
    }

    @Override
    protected String getDatabaseID() {
        return "spatialite";
    }
    
    @Override
    protected String getDriverClassName() {
        return "org.sqlite.JDBC";
    }
    
    public String getDescription() {
        return "SpatiaLite";
    }

    @Override
    protected String getValidationQuery() {
        return null;
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        
        //remove unneccessary parameters
        parameters.remove(HOST.key);
        parameters.remove(PORT.key);
        
        //remove user and password temporarily in order to make username optional
        parameters.remove(JDBCDataStoreFactory.USER.key);
        parameters.put(USER.key, USER);
        
        //add user 
        //add additional parameters
        parameters.put(DBTYPE.key, DBTYPE);

    }
    
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String db = (String) DATABASE.lookUp(params);
        return "jdbc:sqlite:" + db;
    }
    
    @Override
    public BasicDataSource createDataSource(Map params) throws IOException {
        //create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));
        
        dataSource.addConnectionProperty("enable_load_extension", "true");
        dataSource.addConnectionProperty("shared_cache", "yes");
        
        return dataSource;
    }
}
