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
package org.geotools.data.h2;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;


/**
 * DataStoreFacotry for H2 database.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class H2DataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "h2");
    
    /** parameter for how to handle associations */
    public static final Param ASSOCIATIONS = new Param("Associations", Boolean.class,
            "Associations", false, Boolean.FALSE);

    /** optional user parameter */
    public static final Param USER = new Param(JDBCDataStoreFactory.USER.key, JDBCDataStoreFactory.USER.type, 
            JDBCDataStoreFactory.USER.description, false, JDBCDataStoreFactory.USER.sample);

    /** optional host parameter */
    public static final Param HOST = new Param(JDBCDataStoreFactory.HOST.key, JDBCDataStoreFactory.HOST.type, 
            JDBCDataStoreFactory.HOST.description, false, JDBCDataStoreFactory.HOST.sample);

    /** optional port parameter */
    public static final Param PORT = new Param(JDBCDataStoreFactory.PORT.key, JDBCDataStoreFactory.PORT.type, 
            JDBCDataStoreFactory.PORT.description, false, 9902);

    /**
     * base location to store h2 database files
     */
    File baseDirectory = null;

    /**
     * Sets the base location to store h2 database files.
     *
     * @param baseDirectory A directory.
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * The base location to store h2 database files.
     */
    public File getBaseDirectory() {
        return baseDirectory;
    }
    
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);

        //remove host and port temporarily in order to make username optional
        parameters.remove(JDBCDataStoreFactory.HOST.key);
        parameters.remove(JDBCDataStoreFactory.PORT.key);
        
        parameters.put(HOST.key, HOST);
        parameters.put(PORT.key, PORT);

        //remove user and password temporarily in order to make username optional
        parameters.remove(JDBCDataStoreFactory.USER.key);
        parameters.remove(PASSWD.key);
        
        parameters.put(USER.key, USER);
        parameters.put(PASSWD.key, PASSWD);
        
        //add user 
        //add additional parameters
        parameters.put(ASSOCIATIONS.key, ASSOCIATIONS);
        parameters.put(DBTYPE.key, DBTYPE);
    }

    public String getDisplayName() {
        return "H2";
    }

    public String getDescription() {
        return "H2 Embedded Database";
    }

    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    protected String getDriverClassName() {
        return "org.h2.Driver";
    }

    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new H2DialectBasic(dataStore);
        //return new H2DialectPrepared(dataStore);
    }

    protected DataSource createDataSource(Map params, SQLDialect dialect) throws IOException {
        String database = (String) DATABASE.lookUp(params);
        String host = (String) HOST.lookUp(params);
        BasicDataSource dataSource = new BasicDataSource();
        
        if (host != null && !host.equals("")) {
            Integer port = (Integer) PORT.lookUp(params);
            if (port != null && !port.equals("")) {
                dataSource.setUrl("jdbc:h2:tcp://" + host + ":" + port + "/" + database);
            }
            else {
                dataSource.setUrl("jdbc:h2:tcp://" + host + "/" + database);
            }
        } else if (baseDirectory == null) {
            //use current working directory
            dataSource.setUrl("jdbc:h2:" + database);
        } else {
            //use directory specified if the patch is relative
            String location;
            if (!new File(database).isAbsolute()) {
                location = new File(baseDirectory, database).getAbsolutePath();    
            }
            else {
                location = database;
            }

            dataSource.setUrl("jdbc:h2:file:" + location);
        }
        
        String username = (String) USER.lookUp(params);
        if (username != null) {
            dataSource.setUsername(username);
        }
        String password = (String) PASSWD.lookUp(params);
        if (password != null) {
            dataSource.setPassword(password);
        }
        
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setPoolPreparedStatements(false);

        return dataSource;
    }

    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
        throws IOException {
        //check the foreign keys parameter
        Boolean foreignKeys = (Boolean) ASSOCIATIONS.lookUp(params);

        if (foreignKeys != null) {
            dataStore.setAssociations(foreignKeys.booleanValue());
        }

        return dataStore;
    }

    @Override
    protected String getValidationQuery() {
        // no need for this until we are using H2 embedded, there is no
        // network connection that can fail
        return null;
    }
   
}
