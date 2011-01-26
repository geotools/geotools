/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.jdbc.datasource.ManageableDataSource;

/**
 * Creates an OracleDataStore based on the correct params.
 * <p>
 * This factory should be registered in the META-INF/ folder, under services/
 * in the DataStoreFactorySpi file.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @source $URL$
 */
public class OracleDataStoreFactory implements DataStoreFactorySpi {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle");     
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String JDBC_PATH = "jdbc:oracle:thin:@";

    /**
     * Creates a new instance of OracleDataStoreFactory
     */
    public OracleDataStoreFactory() {

    }

    /**
     * Determines whether DataStore created by this factory can process the
     * parameters.
     * <p>
     * Required Parameters are:
     * </p>
     * <ul>
     * <li>
     * <code>dbtype</code> - must equal "oracle"
     * </li>
     * <li>
     * <code>host</code>
     * </li>
     * <li>
     * <code>port</code>
     * </li>
     * <li>
     * <code>user</code>
     * </li>
     * <li>
     * <code>passwd</code>
     * </li>
     * <li>
     * <code>instance</code>
     * </li>     
     * </ul>
     * 
     * <p>
     * There are no defaults since each parameter must be explicitly defined by the user, or
     * another DataSourceFactorySpi should be used. This behaviour is defined in the
     * DataStoreFactorySpi contract.
     * </p>
     *
     * @param params The parameter to check.
     *
     * @return True if all the required parameters are supplied.
     */
    public boolean canProcess(Map params) {
        if (params != null) {
            if (params.get("dbtype") == null || !params.get("dbtype").toString().equalsIgnoreCase("oracle")) {
                return false; //short circuit test
            }
            Param arrayParameters[] = getParametersInfo();
            for (int i = 0; i < arrayParameters.length; i++) {
            	Param param = arrayParameters[i];
            	Object value;
            	if( !params.containsKey( param.key ) ){
            		if( param.required ){
            			return false; // missing required key!
            		}
            		else {
            			continue;
            		}
            	}
				try {
					value = param.lookUp( params );
				} catch (IOException e) {
					LOGGER.warning( param.key+":"+e );
					// could not upconvert/parse to expected type!
					// even if this parameter is not required
					// we are going to refuse to process
					// these params
					return false; 
				}
				if( value == null ){					
					if (param.required) {
                        return (false);
                    }
                }
				else {
					if ( !param.type.isInstance( value )){
						return false; // value was not of the required type
					}
				}
            }
        } else {
            return (false);
        }
        if (!(((String) params.get("dbtype")).equalsIgnoreCase("oracle"))) {
            return (false);
        } else {
            return (true);
        }
    }
    /**
     * Construct a postgis data store using the params.
     *
     * @param params The full set of information needed to construct a live
     *        data source.  Should have  dbtype equal to postgis, as well as
     *        host, user, passwd, database, and table.
     *
     * @return The created DataSource, this may be null if the required
     *         resource was not found or if insufficent parameters were given.
     *         Note that canProcess() should have returned false if the
     *         problem is to do with insuficent parameters.
     *
     * @throws DataSourceException Thrown if there were any problems creating
     *         or connecting the datasource.
     */
    public DataStore createDataStore(Map params) throws IOException {
        /* There are no defaults here. Calling canProcess verifies that
         * all these variables exist.
         */
        String host = (String) HOST.lookUp( params );
        Integer port = (Integer) PORT.lookUp( params );
        String instance = (String) INSTANCE.lookUp( params );
        String user = (String) USER.lookUp( params );
        String passwd = (String) PASSWD.lookUp( params );
        String schema = (String) SCHEMA.lookUp( params ); // checks uppercase
        String namespace = (String) NAMESPACE.lookUp( params );
        String dbtype = (String) DBTYPE.lookUp( params );
        Integer maxConn = (Integer) MAXCONN.lookUp(params);
        Integer minConn = (Integer) MINCONN.lookUp(params);
        Boolean validateConn = (Boolean) VALIDATECONN.lookUp(params);
        
        if( !"oracle".equals( dbtype )){
            throw new IOException( "Parameter 'dbtype' must be oracle");
        }
        
        if (!canProcess(params)) {
            throw new IOException("Cannot connect using provided parameters");
        }
        
        
        boolean validate = validateConn != null && validateConn.booleanValue();
        int maxActive = maxConn != null ? maxConn.intValue() : 10;
        int maxIdle = minConn != null ? minConn.intValue() : 4;
        DataSource source = getDefaultDataSource(host, user, passwd, port.intValue(), instance, maxActive, maxIdle, validate);
        OracleDataStore dataStore = new OracleDataStore(source, namespace, schema, new HashMap());

        return dataStore;
    }
    
    public static ManageableDataSource getDefaultDataSource(String host, String user, String passwd, int port, String instance, int maxActive, int minIdle, boolean validate) throws DataSourceException {
        String dbUrl = null;
        if( instance.startsWith("(") )
            dbUrl = JDBC_PATH + instance;
        else if( instance.startsWith("/") )
            dbUrl = JDBC_PATH + "//" + host + ":" + port + instance;
        else
            dbUrl = JDBC_PATH + host + ":" + port + ":" + instance;
        
        return DataSourceUtil.buildDefaultDataSource(dbUrl, JDBC_DRIVER, user, passwd, maxActive, minIdle, validate ? "select sysdate from dual" : null, false, 0);
    }
    
    /**
     * Oracle cannot create a new database.
     * @param params
     * @throws UnsupportedOperationException Cannot create new database
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException("Oracle cannot create a new Database");
    }

    public String getDisplayName() {
        return "Oracle";
    }
    /**
     * Describe the nature of the datastore constructed by this factory.
     *
     * @return A human readable description that is suitable for inclusion in a
     *         list of available datasources.
     */
    public String getDescription() {
        return "Oracle Spatial Database";
    }
//	public DataSourceMetadataEnity createMetadata( Map params ) throws IOException {
//	    String host = (String) HOST.lookUp( params );
//        String port = (String) PORT.lookUp( params );
//        String instance = (String) INSTANCE.lookUp( params );
//        String user = (String) USER.lookUp( params );
//        String schema = (String) SCHEMA.lookUp( params ); // checks uppercase        
//        return new DataSourceMetadataEnity( host+":"+port, instance, "Connect to oracle using schema '"+schema+"' as  "+user );
//	}
    /**
     * Returns whether the OracleDataStoreFactory would actually be able to
     * generate a DataStore.  Depends on whether the appropriate libraries
     * are on the classpath.  For now just checks for the presence of the
     * JDBC driver, should probably check for SDOAPI as well.
     * 
     * @return True if the classes to make an oracle connection are present.
     * @task Figure out a class to check the SDOAPI for, and check it.
     */
    public boolean isAvailable() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            return false;
        } 
        return true;
        //check for sdoapi too?
    }

    static final Param DBTYPE = new Param("dbtype", String.class, "This must be 'oracle'.", true,"oracle");
    static final Param HOST = new Param("host", String.class, "The host name of the server.", true);
    static final Param PORT = new Param("port", Integer.class, "The port oracle is running on. " +
            "(Default is 1521)", true, "1521");    
    static final Param USER = new Param("user", String.class, "The user name to log in with.", true);
    static final Param PASSWD = new Param("passwd", String.class, "The password.", true);
    public static final Param MAXCONN = new Param("max connections", Integer.class,
            "maximum number of open connections", false, new Integer(10));
    public static final Param MINCONN = new Param("min connections", Integer.class,
            "minimum number of pooled connection", false, new Integer(4));
    public static final Param VALIDATECONN = new Param("validate connections", Boolean .class,
            "check connection is alive before using it", false, Boolean.FALSE);
    static final Param INSTANCE = new Param("instance", String.class, "The name of the Oracle instance to connect to.", true);
    
    /** Apparently Schema must be uppercase */
    static final Param SCHEMA = new Param("schema", String.class, "The schema name to narrow down the exposed tables (must be upper case).", false){
        public Object lookUp(Map map) throws IOException {
            if (!map.containsKey(key)) {
                if (required) {
                    throw new IOException("Parameter " + key + " is required:"
                        + description);
                } else {
                    return null;
                }
            }
            Object value = map.get(key);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                String text = (String) value;
                if( text == null ){
                    return null;
                }
                else if( text.equals( text.toUpperCase() ) ){
                    return text;
                }
                else {
                    throw new IOException("Schema must be supplied in uppercase" );
                }                
            }
            else {
                throw new IOException( "String required for parameter " + key + ": not "
                    + value.getClass().getName());
            }
        }
    };
    static final Param NAMESPACE = new Param("namespace", String.class, "The namespace to give the DataStore", false);
    /**
     * Describe parameters.
     * 
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[]{
            DBTYPE,            
            HOST,
            PORT,
            USER,
            PASSWD,
            INSTANCE,
            MAXCONN, 
            MINCONN, 
            VALIDATECONN, 
            SCHEMA,
            NAMESPACE
        };                
    }
 
    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
