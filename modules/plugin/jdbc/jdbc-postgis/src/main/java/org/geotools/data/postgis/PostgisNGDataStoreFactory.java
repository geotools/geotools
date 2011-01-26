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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Map;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

public class PostgisNGDataStoreFactory extends JDBCDataStoreFactory {
    
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "postgis");
    
    /** enables using && in bbox queries */
    public static final Param LOOSEBBOX = new Param("Loose bbox", Boolean.class, "Perform only primary filter on bbox", false, Boolean.TRUE);
    
    /** parameter that enables estimated extends instead of exact ones */ 
    public static final Param ESTIMATED_EXTENTS = new Param("Estimated extends", Boolean.class, "Use the spatial index information to quickly get an estimate of the data bounds", false, Boolean.TRUE);
    
    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 5432);
    
    /** parameter for database schema */
    public static final Param SCHEMA = new Param("schema", String.class, "Schema", false, "public");

    /**
     * Wheter a prepared statements based dialect should be used, or not
     */
    public static final Param PREPARED_STATEMENTS = new Param("preparedStatements", Boolean.class, "Use prepared statements", false, Boolean.FALSE);
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new PostGISDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }
    
    @Override
    public String getDisplayName() {
        return "PostGIS";
    }

    public String getDescription() {
        return "PostGIS Database";
    }
    
    @Override
    protected String getDriverClassName() {
        return "org.postgresql.Driver";
    }
    
    @Override
    protected boolean checkDBType(Map params) {
        if (super.checkDBType(params)) {
          //check for old factory
            try {
                Class.forName("org.geotools.data.postgis.PostgisDataStoreFactory");
                
                //old factory is around, let it handle the connection
                return false;
            } 
            catch(ClassNotFoundException e) {
                //old factory is not around, handle this connection
                return true;
            }
        }
        else {
            //check for postgisng as well
            return checkDBType(params, "postgisng");
        }
    }
    
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
        throws IOException {
        
        // setup loose bbox
        PostGISDialect dialect = (PostGISDialect) dataStore.getSQLDialect();
        Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
        dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));
        
        // check the estimated extents
        Boolean estimated = (Boolean) ESTIMATED_EXTENTS.lookUp(params);
        dialect.setEstimatedExtentsEnabled(estimated == null || Boolean.TRUE.equals(estimated));
        
        // setup the ps dialect if need be
        Boolean usePs = (Boolean) PREPARED_STATEMENTS.lookUp(params);
        if(Boolean.TRUE.equals(usePs)) {
            dataStore.setSQLDialect(new PostGISPSDialect(dataStore, dialect));
        }
        
        
        return dataStore;
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        // NOTE: when adding parameters here remember to add them to PostgisNGJNDIDataStoreFactory
        
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(PORT.key, PORT);
        parameters.put(PREPARED_STATEMENTS.key, PREPARED_STATEMENTS);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);
    }
    
    @Override
    protected String getValidationQuery() {
        return "select now()";
    }
    
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        int port = (Integer) PORT.lookUp(params);
        return "jdbc:postgresql" + "://" + host + ":" + port + "/" + db;
    }

}
