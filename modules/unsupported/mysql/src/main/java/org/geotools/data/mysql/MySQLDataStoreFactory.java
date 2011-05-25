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
package org.geotools.data.mysql;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.jdbc.datasource.ManageableDataSource;


/**
 * Creates a MySQLDataStoreFactory based on the correct params.
 *
 * <p>
 * This factory should be registered in the META-INF/ folder, under services/
 * in the DataStoreFactorySpi file.
 * </p>
 *
 * @author Andrea Aime, University of Modena and Reggio Emilia
 *
 * @source $URL$
 */
public class MySQLDataStoreFactory extends AbstractDataStoreFactory {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MySQLDataStoreFactory.class.getName());

    /** Creates MySQL JDBC driver class. */
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    /** Param, package visibiity for JUnit tests */
    public static final Param DBTYPE = new Param("dbtype", String.class, "must be 'mysql'", true, "mysql");

    /** Param, package visibiity for JUnit tests */
    public static final Param HOST = new Param("host", String.class, "mysql host machine", true,
            "localhost");

    /** Param, package visibiity for JUnit tests */
    public static final Param PORT = new Param("port", Integer.class, "mysql connection port", false, "3306");

    /** Param, package visibiity for JUnit tests */
    public static final Param DATABASE = new Param("database", String.class, "msyql database");

    /** Param, package visibiity for JUnit tests */
    public static final Param USER = new Param("user", String.class, "user name to login as", false);

    /** Param, package visibiity for JUnit tests */
    public static final Param PASSWD = new Param("passwd", String.class, "password used to login", false);
    
    public static final Param MAXCONN = new Param("max connections", Integer.class,
            "maximum number of open connections", false, new Integer(10));
    
    public static final Param MINCONN = new Param("min connections", Integer.class,
            "minimum number of pooled connection", false, new Integer(4));
    
    public static final Param VALIDATECONN = new Param("validate connections", Boolean.class,
            "check connection is alive before using it", false, Boolean.FALSE);

    /** Param, package visibiity for JUnit tests */
    public static final Param NAMESPACE = new Param("namespace", String.class, "namespace prefix used",
            false);
    
    public static final Param WKBENABLED = new Param("wkb enabled", Boolean.class,
            "set to true if Well Known Binary should be used to read MySQL"
            + "data (experimental)", false, new Boolean(true));

    /** Array with all of the params */
    static final Param[] arrayParameters = { DBTYPE, HOST, PORT, DATABASE, USER, PASSWD,  NAMESPACE, WKBENABLED };

    /**
     * Creates a new instance of MysqlDataStoreFactory
     */
    public MySQLDataStoreFactory() {
    }

    /**
     * Checks to see if all the mysql params are there.
     *
     * <p>
     * Should have:
     * </p>
     *
     * <ul>
     * <li>
     * dbtype: equal to mysql
     * </li>
     * <li>
     * host
     * </li>
     * <li>
     * user
     * </li>
     * <li>
     * passwd
     * </li>
     * <li>
     * database
     * </li>
     * </ul>
     *
     *
     * @param params Set of parameters needed for a mysql data store.
     *
     * @return <code>true</code> if dbtype equals mysql, and contains keys
     *         for host, user, passwd, and database.
     */
    public boolean canProcess(Map params) {
        if (!super.canProcess(params)) {
            return false;
        }

        return ((String) params.get("dbtype")).equalsIgnoreCase("mysql");
    }

    /**
     * Construct a mysql data store using the params.
     *
     * @param params The full set of information needed to construct a live
     *        data source.  Should have  dbtype equal to mysql, as well as
     *        host, user, passwd, database, and table.
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
        // lookup will throw nice exceptions back to the client code
        String host = (String) HOST.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
        Integer maxConn = (Integer) MAXCONN.lookUp(params);
        Integer minConn = (Integer) MINCONN.lookUp(params);
        Boolean validateConn = (Boolean) VALIDATECONN.lookUp(params);
        int port = ((Integer) PORT.lookUp(params)).intValue();
        String database = (String) DATABASE.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        Boolean wkbEnabled = (Boolean) WKBENABLED.lookUp(params);

        if (!canProcess(params)) {
            // Do this as a last sanity check.
            LOGGER.warning("Can not process : " + params);
            throw new IOException("The parameteres map isn't correct!!");
        }

        boolean validate = (validateConn != null) && validateConn.booleanValue();
        int maxActive = (maxConn != null) ? maxConn.intValue() : 10;
        int maxIdle = (minConn != null) ? minConn.intValue() : 4;
        DataSource ds = getDefaultDataSource(host, user, passwd, port, database, maxActive,
                maxIdle, validate);

        MySQLDataStore store;
        if (namespace != null) {
            store = new MySQLDataStore(ds, null, namespace);
        } else {
            store =new MySQLDataStore(ds);
        }
        if(wkbEnabled != null)
            store.setWKBEnabled(wkbEnabled.booleanValue());
        return store;
    }

    public static ManageableDataSource getDefaultDataSource(String host, String user,
        String passwd, int port, String database, int maxActive, int minIdle, boolean validate)
        throws DataSourceException {
        // this one will have to wait for another iteration over the code
//        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useCursorFetch=true&defaultFetchSize=100";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        String driver = "com.mysql.jdbc.Driver";

        return DataSourceUtil.buildDefaultDataSource(url, driver, user, passwd, maxActive, minIdle,
            validate ? "select version()" : null, false, 0);
    }

    /**
     * Mysql cannot create a new database.
     *
     * @param params
     *
     *
     * @throws IOException See UnsupportedOperationException
     * @throws UnsupportedOperationException Cannot create new database
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException("MySQL cannot create a new Database");
    }

    /**
     * @return "MySQL"
     */
    public String getDisplayName() {
        return "MySQL";
    }

    /**
     * Describe the nature of the datasource constructed by this factory.
     *
     * @return A human readable description that is suitable for inclusion in a
     *         list of available datasources.  Currently uses the string "MySQL Database"
     */
    public String getDescription() {
        return "MySQL Database";
    }

    //    /**
    //     *
    //     */
    //    public DataSourceMetadataEnity createMetadata( Map params ) throws IOException {
    //        String host = (String) HOST.lookUp(params);
    //        String user = (String) USER.lookUp(params);
    //        String port = (String) PORT.lookUp(params);
    //        String database = (String) DATABASE.lookUp(params);
    //        return new DataSourceMetadataEnity( host+"port", database, "MySQL connection to "+host+" as "+user );
    //    }

    /**
     * Test to see if this datastore is available, if it has all the
     * appropriate libraries to construct a datastore.  This datastore just
     * returns true for now.  This method is used for gui apps, so as to not
     * advertise data store capabilities they don't actually have.
     *
     * @return <tt>true</tt> if and only if this factory is available to create
     *         DataStores.
     */
    public boolean isAvailable() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException cnfe) {
            LOGGER.warning("MySQL data sources are not available: " + cnfe.getMessage());

            return false;
        }

        return true;
    }

    /**
     * Describe parameters.
     *
     *
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] {
            DBTYPE, HOST, PORT, DATABASE, USER, PASSWD, MAXCONN, MINCONN, VALIDATECONN, NAMESPACE
        };
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
