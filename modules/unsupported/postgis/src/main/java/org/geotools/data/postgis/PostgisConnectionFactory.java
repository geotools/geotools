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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.jdbc.ConnectionPool;
import org.geotools.data.jdbc.ConnectionPoolManager;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.DataSourceUtil;

/**
 * Shell for JDBC transactions of all types. This creates connections for the
 * postgis datasource to make its  transactions.  To create a
 * PostgisDataSource, create a  PostgisConnectionFactory, then call
 * getConnection(), and pass that connection to the PostgisDataSource
 * constructor.
 *
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 *
 * @source $URL$
 * @version $Id$
 * @deprecated Use {@link DataSource}, {@link DataSourceUtil} and {@link DataSourceFinder} instead
 *  *
 * @task REVISIT: connection pooling, implementing java.sql.Datasource.  I
 *       removed the implementing because that class should be provided by the
 *       vendor, not a hack on top of DriverManager.  The only problem is that
 *       we can't use the postgres one directly, because it doesn't allow you
 *       to set the charset, which some of our users need.
 */
public class PostgisConnectionFactory {
    /** Standard logging instance */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.defaultcore");

    /** Creates PostGIS-specific JDBC driver class. */
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    /** Creates PostGIS-specific JDBC driver path. */
    private static final String DRIVER_PATH = "jdbc:postgresql";

    /** The full path used to connect to the database */
    private String connPath;

       /** The host to connect to */
    private String host;

   /** The database to connect to.*/
    private String dbName;

   /** The port to connect on.*/
    private int port = -1;

    /** The user to connect with */
    private String user = "test";

    /** The password of the user */
    private String password = "test";
    
    /** Map that contains Connection Pool Data Sources */
    private static Map dataSources = new HashMap();

    /**
     * Constructor with all internal database driver classes, driver paths and
     * database types.
     *
     * @param host The name or ip address of the computer where the postgis
     *        database is installed.
     * @param port The port to connect on; 5432 is generally the postgres
     *        default.
     * @param dbName The name of the pgsql database that holds the tables of
     *        the feature types that will be used by PostgisDataSource.
     */
    public PostgisConnectionFactory(String host, String port, String dbName) {
	this.host = host;
	this.dbName = dbName;
	if (port != null) {
	    try {
		this.port = Integer.parseInt(port);
	    } catch (NumberFormatException nfe) {
		throw new IllegalArgumentException("could not parse port " + 
						   "to an int value");
	    }
	}
	connPath = DRIVER_PATH + "://" + host + ":" + port + "/"
            + dbName;
    }

     /**
     * Constructor with all internal database driver classes, driver paths and
     * database types.
     *
     * @param host The name or ip address of the computer where the postgis
     *        database is installed.
     * @param port The port to connect on; 5432 is generally the postgres
     *        default.
     * @param dbName The name of the pgsql database that holds the tables of
     *        the feature types that will be used by PostgisDataSource.
     */
    public PostgisConnectionFactory(String host, int port, String dbName) {
	connPath = DRIVER_PATH + "://" + host + ":" + port + "/"
            + dbName;
	this.host = host;
	this.dbName = dbName;
	this.port = port;
    }

    /**
     * Constructor with all internal database driver classes, driver paths and
     * database types.
     *
     * @param connPath The driver class; should be passed from the
     *        database-specific subclass.
     */
    //public PostgisConnectionFactory(String connPath) {
    //  connPath = DRIVER_PATH + "://" + connPath;
    //}

    /**
     * Creates a database connection method to initialize a given database for
     * feature extraction.
     *
     * @param user the name of the user connect to connect to the pgsql db.
     * @param password the password for the user.
     */
    public void setLogin(String user, String password) {
        this.user = user;
        this.password = password;
    }

    

    /**
     * Creates a database connection method to initialize a given database for
     * feature extraction using the set username and password.
     *
     * @return the sql Connection object to the database.
     *
     * @throws SQLException if the postgres driver could not be found
     */
    public Connection getConnection() throws SQLException {
        return getConnection(user, password);
    }

    /**
     * Creates a database connection method to initialize a given database for
     * feature extraction with the user and password params.
     *
     * @param user the name of the user connect to connect to the pgsql db.
     * @param password the password for the user.
     *
     * @return the sql Connection object to the database.
     *
     * @throws SQLException if the postgis sql driver could not be found
     */
    public Connection getConnection(String user, String password)
        throws SQLException {
        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);

        return getConnection(props);
    }

      public ConnectionPool getConnectionPool(String user, String pass)
        throws SQLException {
        org.postgresql.jdbc2.optional.ConnectionPool poolDataSource = null;
	String dbUrl = connPath;
        String poolKey = dbUrl + user + pass;
	LOGGER.fine("looking up pool key " + poolKey);
	Object poolDS = dataSources.get(poolKey);
	poolDataSource = (org.postgresql.jdbc2.optional.ConnectionPool)poolDS;
	LOGGER.fine("pool is " + poolDataSource);
        if (poolDataSource == null) {
	    poolDataSource = new org.postgresql.jdbc2.optional.ConnectionPool();
	    //source.setDataSourceName("Geotools Postgis");
	    poolDataSource.setServerName(host);
	    poolDataSource.setDatabaseName(dbName);
	    poolDataSource.setPortNumber(port);
	    poolDataSource.setUser(user);
	    poolDataSource.setPassword(pass);
	    //source.setMaxConnections(10);
	    //the source looks like this defaults to false, but we have
	    //assumed true (as that's how it was before pooling)
	    poolDataSource.setDefaultAutoCommit(true);
        dataSources.put(poolKey, poolDataSource);
            
        }

        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        ConnectionPool connectionPool = manager.getConnectionPool(poolDataSource);
        
        return connectionPool;
    }

    public ConnectionPool getConnectionPool() throws SQLException {
        return getConnectionPool(user, password);
    }
    public void free(ConnectionPool connectionPool){
        if (connectionPool == null) return;
    	if(!connectionPool.isClosed() ){
            connectionPool.close();
        }        
        ConnectionPoolManager.getInstance().free( connectionPool );
    }
    /**
     * Creates a database connection method to initialize a given database for
     * feature extraction with the given Properties.
     *
     * @param props Should contain at a minimum the user and password.
     *        Additional properties, such as charSet, can also be added.
     *
     * @return the sql Connection object to the database.
     *
     * @throws SQLException if the postgis sql driver could not be found
     */
    public Connection getConnection(Properties props) throws SQLException {
        // makes a new feature type bean to deal with incoming
        Connection dbConnection = null;

        // Instantiate the driver classes
        try {
            Class.forName(DRIVER_CLASS);
            LOGGER.finest("getting connection at " + connPath + "with props: "
                + props);
            dbConnection = DriverManager.getConnection(connPath, props);
        } catch (ClassNotFoundException cnfe) {
            throw new SQLException("Postgis driver was not found.");
        }

        return dbConnection;
    }
}
