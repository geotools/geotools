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

/**
 * Creates an Oracle datastore based on a thick OCI client connection, instead
 * of the traditional (thin) jdbc connection.  The thin JDBC connection was designed
 * for clients requiring no more classes than just the classes12.jar (or ojdbc14.jar).
 * The OCI JDBC drivers are based on the Oracle client software and rely mostly on the
 * very fast C/C++ based OCI (Oracle Call Interface) runtime.
 * <p>
 * This leads to an easy way to speed up GeoTools when gt2 is running on the same computer
 * as the oracle install, as the OCI drivers are already there.  And I believe if a computer
 * has the OCI correctly installed and configure it can be used on remote computers, with 
 * the faster connection.  This just takes more work by the admin.  Server applications like
 * GeoServer will often be on the same computer as the databse, so it makes sense to offer
 * admins the advantage of using the OCI jdbc driver.  
 * <p>
 * Instead of the instance, host, port requirments of the normal oracle factory this driver
 * just requires the 'alias', which refers to values defined by the Oracle Net Configuration
 * assistant and stored in $ORACLE_HOME/NETWORK/ADMIN/tnsnames.ora.  We have also had luck 
 * on the same computer with just leaving 'alias' as an empty string, and it seems to have
 * a reasonable default behavior.  
 * 
 * <p>
 * This factory should be registered in the META-INF/ folder, under services/
 * in the DataStoreFactorySpi file.
 * </p>
 *
 * @author Chris Holmes, TOPP
 * @author Bernard de Terwangne, star.be
 *
 * @source $URL$
 */
public class OracleOCIDataStoreFactory implements DataStoreFactorySpi {
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle");

    /**
     * Creates a new instance of OracleOCIDataStoreFactory
     */
    public OracleOCIDataStoreFactory() {
    }

    /**
     * Determines whether DataStore created by this factory can process the
     * parameters.
     * 
     * <p>
     * Required Parameters are:
     * </p>
     * 
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
     * There are no defaults since each parameter must be explicitly defined by
     * the user, or another DataSourceFactorySpi should be used. This
     * behaviour is defined in the DataStoreFactorySpi contract.
     * </p>
     *
     * @param params The parameter to check.
     *
     * @return True if all the required parameters are supplied.
     */
    //this should probably switch to the new way of doing things, with the PARAMS, but I want to see canProcess done in 
    //a super class, since everyone does it the same way. -ch
    public boolean canProcess(Map params) {
        //J-
        return params.containsKey("dbtype")
        && params.get("dbtype").equals("oracle")
        && params.containsKey("alias")
        && params.containsKey("user")
        && params.containsKey("passwd");
        //J+
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
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException Thrown if there were any problems creating
     *         or connecting the datasource.
     */
    public DataStore createDataStore(Map params) throws IOException {

        /* There are no defaults here. Calling canProcess verifies that
         * all these variables exist.
         */
        String alias = (String) ALIAS.lookUp( params );
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
        DataSource source = getDefaultDataSource(alias, user, passwd, maxActive, maxIdle, validate);
        return new OracleDataStore(source, namespace, schema, new HashMap());
    }

    public static DataSource getDefaultDataSource(String alias, String user, String passwd, int maxActive, int minIdle, boolean validate) throws DataSourceException {
        String dbUrl = "jdbc:oracle:oci:@" + alias;
        return DataSourceUtil.buildDefaultDataSource(dbUrl, JDBC_DRIVER, user, passwd, maxActive, minIdle, validate ? "select sysdate from dual" : null, false, 0);
    }

    /**
     * Oracle cannot create a new database.
     *
     * @param params
     *
     *
     * @throws IOException DOCUMENT ME!
     * @throws UnsupportedOperationException Cannot create new database
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException(
            "Oracle cannot create a new Database");
    }

    public String getDisplayName() {
        return "Oracle (OCI)";
    }

    /**
     * Describe the nature of the datastore constructed by this factory.
     *
     * @return A human readable description that is suitable for inclusion in a
     *         list of available datasources.
     */
    public String getDescription() {
        return "Oracle Spatial w/ OCI (thick) connection";
    }

//	public DataSourceMetadataEnity createMetadata( Map params ) throws IOException {
//	    String alias = (String) ALIAS.lookUp( params );
//         String user = (String) USER.lookUp( params );
//        String schema = (String) SCHEMA.lookUp( params ); // checks uppercase        
//        return new DataSourceMetadataEnity( alias, alias, "Connect to oracle using schema '"+schema+"' as  "+user );
//	}


    /**
     * Returns whether the OracleDataStoreFactory would actually be able to
     * generate a DataStore.  Depends on whether the appropriate libraries are
     * on the classpath.  For now just checks for the presence of the JDBC
     * driver, should probably check for SDOAPI as well.
     *
     * @return True if the classes to make an oracle connection are present.
     *
     * @task Figure out a class to check the SDOAPI for, and check it.
     */
    public boolean isAvailable() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            return false;
        }

        return true;
    }

    static final Param DBTYPE = new Param("dbtype", String.class, "This must be 'oracle'.", true,"oracle");
    static final Param ALIAS = new Param("alias", String.class, "The alias to the oracle server, as defined in the tnsnames.ora file", true);
    static final Param PORT = new Param("port", String.class, "The port oracle is running on. " +
            "(Default is 1521)", true, "1521");    
    static final Param USER = new Param("user", String.class, "The user name to log in with.", true);
    static final Param PASSWD = new Param("passwd", String.class, "The password.", true);
    static final Param INSTANCE = new Param("instance", String.class, "The name of the Oracle instance to connect to.", true);
    public static final Param MAXCONN = new Param("max connections", Integer.class,
            "maximum number of open connections", false, new Integer(10));
    public static final Param MINCONN = new Param("min connections", Integer.class,
            "minimum number of pooled connection", false, new Integer(4));
    public static final Param VALIDATECONN = new Param("validate connections", Boolean .class,
            "check connection is alive before using it", false, Boolean.FALSE);
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
            ALIAS,
            USER,
            PASSWD,
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
