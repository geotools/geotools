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
package org.geotools.data.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import oracle.jdbc.OracleConnection;

import org.geotools.data.Transaction;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * Oracle data store factory.
 * 
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 * @author Hendrik Peilke
 *
 *
 * @source $URL$
 */
public class OracleNGDataStoreFactory extends JDBCDataStoreFactory {
    private static final String JDBC_PATH = "jdbc:oracle:thin:@";
    
    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "oracle");

    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", false, 1521);
    
    /** parameter for database host */
    public static final Param HOST = new Param("host", String.class, "Host", false, "localhost");
    
    /** parameter for database instance */
    public static final Param DATABASE = new Param("database", String.class, "Database", true);
    
    /** parameter that enables estimated extends instead of exact ones */ 
    public static final Param ESTIMATED_EXTENTS = new Param("Estimated extends", Boolean.class, "Use the spatial index information to quickly get an estimate of the data bounds", false, Boolean.TRUE);
    
    /** parameter for namespace of the datastore */
    public static final Param LOOSEBBOX = new Param("Loose bbox", Boolean.class, "Perform only primary filter on bbox", false, Boolean.TRUE);
    
    /** Metadata table providing information about primary keys **/
    public static final Param GEOMETRY_METADATA_TABLE = new Param("Geometry metadata table", String.class,
            "The optional table containing geometry metadata (geometry type and srid). Can be expressed as 'schema.name' or just 'name'", false);
    
    /** parameter for getting bbox from MDSYS.USER_SDO_GEOM_METADATA or MDSYS.ALL_SDO_GEOM_METADATA table */
    public static final Param METADATA_BBOX = new Param("Metadata bbox", Boolean.class, "Get data bounds quickly from MDSYS.USER_SDO_GEOM_METADATA or MDSYS.ALL_SDO_GEOM_METADATA table", false, Boolean.FALSE);
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new OracleDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }
    
    @Override
    public String getDisplayName() {
        return "Oracle NG";
    }

    public String getDescription() {
        return "Oracle Database";
    }
    
    @Override
    protected String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
    }
    
    @Override
    protected boolean checkDBType(Map params) {
        if (super.checkDBType(params)) {
            try {
                //check for old factory
                Class.forName("org.geotools.data.oracle.OracleDataStoreFactory");
                
                //it is around, let it handle this connection
                return false;
            } 
            catch(ClassNotFoundException e) {
                //old factory not around, pick up the connection
                return true;
            }
        }
        else {
            //check for the old id
            return checkDBType(params, "Oracle");
        }
    }
    
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
        throws IOException {
        
        // make the schema uppercase if it's not already
        if(dataStore.getDatabaseSchema() != null) {
            dataStore.setDatabaseSchema(dataStore.getDatabaseSchema().toUpperCase());
        }
        
        // setup loose bbox
        OracleDialect dialect = (OracleDialect) dataStore.getSQLDialect();
        Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
        dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));
        
        // check the estimated extents
        Boolean estimated = (Boolean) ESTIMATED_EXTENTS.lookUp(params);
        dialect.setEstimatedExtentsEnabled(estimated == null || Boolean.TRUE.equals(estimated));
        
        // check the geometry metadata table
        String metadataTable = (String) GEOMETRY_METADATA_TABLE.lookUp(params);
        dialect.setGeometryMetadataTable(metadataTable);
        
        // check the metadata bbox option
        Boolean metadateBbox = (Boolean) METADATA_BBOX.lookUp(params);
        dialect.setMetadataBboxEnabled(Boolean.TRUE.equals(metadateBbox));
        
        if (dataStore.getFetchSize() <= 0) {
            // Oracle is dead slow with the fetch size at 0, let's have a sane default
            dataStore.setFetchSize(200);
        }
        
        Connection cx = dataStore.getConnection(Transaction.AUTO_COMMIT);
        try {
            OracleConnection oracleConnection = dialect.unwrapConnection( cx );
        } catch (SQLException e) {
            throw new IOException(
                    "Unable to obtain Oracle Connection require for SDO Geometry access)."+
                    "Check connection pool implementation to unsure unwrap is available", e);
        }
        finally {
            dataStore.closeSafe(cx);
        }        
        return dataStore;
    }
    
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
    	String db = (String) DATABASE.lookUp(params);    	
      String host = (String) HOST.lookUp(params);        
      Integer port =(Integer) PORT.lookUp(params);

    	if(db.startsWith("(") || db.startsWith("ldap://"))
    		// for Oracle LDAP:
    		// ldap://[host]/[db],cn=OracleContext,dc=[oracle_ldap_context]
    		// for Oracle RAC
    		// (DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=[host])(PORT=[port, often 1521]))(LOAD_BALANCE=YES)(CONNECT_DATA=(SERVICE_NAME=[oracle_service_name])))
    		return JDBC_PATH + db;  
    	else if(db.startsWith("/") && host != null && port != null)
    		return JDBC_PATH + "//" + host + ":" + port + db;
        else if(host != null && port != null)
    		return JDBC_PATH + host + ":" + port + ":" + db;
        else
        	throw new IOException("Unable to properly compose the JDBC URL string, some parameters as host and port may be null !");
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        // NOTE: when adding parameters here remember to add them to OracleNGOCIDataStoreFactory and
        // OracleNGJNDIDataStoreFactory
        
        super.setupParameters(parameters);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);
        parameters.put(PORT.key, PORT);
        parameters.put(HOST.key, HOST);
        parameters.put(DATABASE.key, DATABASE);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(GEOMETRY_METADATA_TABLE.key, GEOMETRY_METADATA_TABLE);
        parameters.put(METADATA_BBOX.key, METADATA_BBOX);
    }
    
    @Override
    protected String getValidationQuery() {
        return "select 1 from dual";
    }
}
