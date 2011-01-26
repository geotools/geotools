/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.jdbc.datasource.ManageableDataSource;


/**
 * Implements the DataStoreFactorySpi interface to create an instance of a
 * DB2DataStore.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2DataStoreFactory extends AbstractDataStoreFactory
    implements DataStoreFactorySpi {
    public static final String DRIVERNAME = "com.ibm.db2.jcc.DB2Driver";

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.db2");

    // The DB2 JDBC universal driver class.  isAvailable() uses this to
    // check whether the DB2 JDBC library is in the classpath
    private static final String DRIVER_CLASS = DRIVERNAME;
    private static boolean isAvailable = false;
    public static final Param DBTYPE = new Param("dbtype", String.class,
            "must be 'DB2'", true, "DB2");
    public static final Param HOST = new Param("host", String.class,
            "DB2 host machine", true, "localhost");
    public static final Param PORT = new Param("port", Integer.class,
            "DB2 connection port", true, new Integer(50000));
    public static final Param DATABASE = new Param("database", String.class,
            "database name", true);
    public static final Param USER = new Param("user", String.class,
            "user name to login as", false);
    public static final Param PASSWD = new Param("passwd", String.class,
            "password used to login", false);
    public static final Param TABSCHEMA = new Param("tabschema", String.class,
            "default table schema", false);
    public static final Param MAXCONN = new Param("max connections", Integer.class,
            "maximum number of open connections", false, new Integer(10));
    
    public static final Param MINCONN = new Param("min connections", Integer.class,
            "minimum number of pooled connection", false, new Integer(4));
    
    public static final Param VALIDATECONN = new Param("validate connections", Boolean .class,
            "check connection is alive before using it", false, Boolean.FALSE);
    static final Param[] DB2PARMS = {
            DBTYPE, HOST, PORT, DATABASE, USER, PASSWD, TABSCHEMA, MAXCONN, MINCONN, VALIDATECONN
        };

    /**
     * canProcess and lastParams are used to cut out processing when
     * 'canProcess' is called successively.
     */
    private boolean canProcess = false;
    private Map lastParams = null;

    /**
     * Constructs a DB2 data store using the params.
     * 
     * If the port number is zero we will try to use the JDBC type 2 driver
     * and if the port number is non-zer, we will try to use the JDBC type 4
     * driver
     *
     * @param params The full set of information needed to construct a live
     *        data source.  Should have  dbtype equal to DB2, as well as host,
     *        user, passwd, database, and table schema.
     *
     * @return The created DataSource, this may be null if the required
     *         resource was not found or if insufficent parameters were given.
     *         Note that canProcess() should have returned false if the
     *         problem is to do with insuficent parameters.
     *
     * @throws IOException See DataSourceException
     * @throws DataSourceException Thrown if there were any problems creating
     *         or connecting the datasource.
     */
    public DataStore createDataStore(Map params) throws IOException {
        if (!canProcess(params)) {
            throw new IOException("Invalid parameters");
        }
        if (!isAvailable()) {
        	throw new IOException("DB2 Driver not available");
        }

        String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
        int port = ((Integer) PORT.lookUp(params)).intValue();
        String database = (String) DATABASE.lookUp(params);
        String tabschema = (String) TABSCHEMA.lookUp(params);
        Integer maxConn = (Integer) MAXCONN.lookUp(params);
        Integer minConn = (Integer) MINCONN.lookUp(params);
        Boolean validateConn = (Boolean) VALIDATECONN.lookUp(params);
        
        boolean validate = validateConn != null && validateConn.booleanValue();
        int maxActive = maxConn != null ? maxConn.intValue() : 10;
        int maxIdle = minConn != null ? minConn.intValue() : 4;
        String url = getJDBCUrl(host, port, database);
        DataSource source = getDefaultDataSource(url, user, passwd, maxActive, maxIdle, validate);

        // If the table schema is null or blank, uset the userid for the table schema
        if (tabschema == null || tabschema.length() == 0) {
        	tabschema = user;
        }
        // if the table schema is not double-quoted, convert it to uppercase.
        // if it is double-quoted, remove the double quotes.
        if (tabschema.startsWith("\"")) {
        	tabschema = tabschema.substring(1,tabschema.length()-1);
        } else {
        	tabschema = tabschema.toUpperCase();
        }
        // trim trailing blanks - just in case
        tabschema = tabschema.trim();
        // Set the namespace and databaseSchemaName both to the table schema name
        // Set the timeout value to 100 seconds to force FeatureTypeHandler caching
        JDBCDataStoreConfig config = new JDBCDataStoreConfig(tabschema,
                tabschema, 10000);
        DB2DataStore ds;

        try {
            ds = new DB2DataStore(source, config, url);
        } catch (IOException e) {
            LOGGER.info("Create DB2Datastore failed: "
                    + e);
            throw new DataSourceException("Could not create DB2DataStore", e);
        }

        LOGGER.info("Successfully created DB2Datastore for: "
            + host + ":" + port + "/" + database);

        return ds;
    }

    public static ManageableDataSource getDefaultDataSource(String url, String user, String passwd, int maxActive, int minIdle, boolean validate) throws DataSourceException {
        return DataSourceUtil.buildDefaultDataSource(url, DRIVER_CLASS, user, passwd, maxActive, minIdle, validate ? "select current date from sysibm.sysdummy1" : null, false, 0);
    }

    /**
     * Returns the JDBC url used for connecting to a specific database
     */
    public static String getJDBCUrl(String host, int port, String database) {
    	String url = null;
    	if (port == 0) {
    		url = "jdbc:db2:" + database;
    	} else {
    		url = "jdbc:db2://" + host + ":" + port + "/" + database;
    	}
        return url;
    }

    /**
     * Creating a new DB2 database is not supported.
     *
     * @param params Doesn't much matter what this contains.
     *
     * @return DataStore But will always throw an exception
     *
     * @throws UnsupportedOperationException Cannot create new database
     */
    public DataStore createNewDataStore(Map params)
        throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
            "Creating a new DB2 database is not supported");
    }

    /**
     * Provide a String description of this data store.
     *
     * @return the data store description.
     */
    public String getDescription() {
        return "DB2 Data Store";
    }

    /**
     * Name suitable for display to end user.
     * 
     * <p>
     * A non localized display name for this data store type.
     * </p>
     *
     * @return A short name suitable for display in a user interface.
     */
    public String getDisplayName() {
        return "DB2";
    }

    /**
     * Returns the array of parameters used by DB2.
     *
     * @return Param[] Array of parameters.
     */
    public Param[] getParametersInfo() {
        return DB2PARMS;
    }

    /**
     * Check whether the parameter list passed identifies it as a request for a
     * DB2DataStore.
     * 
     * <p>
     * Most critical is the 'dbtype' parameter which must have the value 'DB2'.
     * If it is, then the remaining parameter values can be checked.
     * </p>
     *
     * @param params Key/Value parameter list containing values required to
     *        identify a request for a DB2DataStore and remaining values to
     *        identify the database to be connected to.
     *
     * @return true if dbtype equals DB2, and contains keys for host, user,
     *         passwd, and database.
     */
    public boolean canProcess(Map params) {
        String logInfo = "";

        // Hopefully we won't be called with a null parameter list.		
        if (params == null) {
            return false;
        }

        // Can't do anything if no dbtype or the dbtype is not DB2	
        String dbtype = (String) params.get("dbtype");

        if (dbtype == null) {
            return (false);
        }

        if (!(dbtype.equalsIgnoreCase("DB2"))) {
            return (false);
        }

        // If the parameters are the same as last time and it was ok last time
        // it should still be ok.
        if (this.canProcess && (this.lastParams == params)) {
            return true;
        }

        if (!super.canProcess(params)) {
            return false;
        }

        this.lastParams = params;
        this.canProcess = true;

        return true;
    }

    /**
     * Check whether the DB2 JDBC type 4 driver is found in the classpath.
     * 
     * <p>
     * If it isn't, there is a problem since the FactoryFinder found the
     * DB2DataStoreFactory but there is no driver to connect to a DB2
     * database.
     * </p>
     * 
     * <p>
     * The classpath should have db2jcc.jar and db2jcc_license_cu.jar
     * </p>
     *
     * @return true if a DB2 driver is available for the DB2DataStore to
     *         connect to a DB2 database.
     */
    public boolean isAvailable() {
        if (isAvailable) {
            return isAvailable;
        }

        try {
            Class.forName(DRIVER_CLASS);
            isAvailable = true;
        } catch (ClassNotFoundException e) {
            isAvailable = false;
        }

        LOGGER.info("DB2 driver found: " + isAvailable);

        return isAvailable;
    }
}
