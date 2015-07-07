/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.geopkg.geom.GeoPkgGeomWriter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.sqlite.SQLiteConfig;

/**
 * The GeoPackage DataStore Factory.
 * 
 * @author Justin Deoliveira
 * @author Niels Charlier
 *
 */
public class GeoPkgDataStoreFactory extends JDBCDataStoreFactory {

    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "geopkg");
    
    /** optional user parameter */
    public static final Param USER = new Param(JDBCDataStoreFactory.USER.key, JDBCDataStoreFactory.USER.type, 
            JDBCDataStoreFactory.USER.description, false, JDBCDataStoreFactory.USER.sample);
    
    /** parameter for database instance */
    public static final Param DATABASE = new Param("database", File.class, "Database", true );

    /**
     * base location to store database files
     */
    File baseDirectory = null;
        
    GeoPkgGeomWriter.Configuration writerConfig;
    
    public GeoPkgDataStoreFactory() {
        this.writerConfig = new GeoPkgGeomWriter.Configuration();
    }
    
    public GeoPkgDataStoreFactory(GeoPkgGeomWriter.Configuration writerConfig) {
        this.writerConfig = writerConfig;
    }

    /**
     * Sets the base location to store database files.
     *
     * @param baseDirectory A directory.
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    protected String getDatabaseID() {
        return "geopkg";
    }

    @Override
    public String getDescription() {
        return "GeoPackage";
    }

    @Override
    protected String getDriverClassName() {
        return "org.sqlite.JDBC";
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new GeoPkgDialect(dataStore, writerConfig);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        File db = (File) DATABASE.lookUp(params);
        if (db.getPath().startsWith("file:")) {
            db = new File(db.getPath().substring(5));
        }
        if (baseDirectory != null) {
            // check for a relative path
            if (!db.isAbsolute()) {
                db = new File(baseDirectory, db.getPath());
            }
        }
        return "jdbc:sqlite:" + db;
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        
        //remove unneccessary parameters
        parameters.remove(HOST.key);
        parameters.remove(PORT.key);
        parameters.remove(SCHEMA.key);
        
        //replace database with File database
        parameters.put(DATABASE.key, DATABASE);
        //replace user to make optional
        parameters.put(USER.key, USER);
        //replace dbtype
        parameters.put(DBTYPE.key, DBTYPE);
    }

    @Override
    public BasicDataSource createDataSource(Map params) throws IOException {
        //create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));
        
        //dataSource.setMaxActive(1);
        //dataSource.setMinIdle(1);

        //dataSource.setTestOnBorrow(true);
        //dataSource.setValidationQuery(getValidationQuery());
        addConnectionProperties(dataSource);
        
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        
        return dataSource;
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params) throws IOException {
        dataStore.setDatabaseSchema(null);
        return dataStore;
    }

    static void addConnectionProperties(BasicDataSource dataSource) {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        //config.enableSpatiaLite(true);
        
        for (Map.Entry e : config.toProperties().entrySet()) {
            dataSource.addConnectionProperty((String)e.getKey(), (String)e.getValue());
        }
    }

}
