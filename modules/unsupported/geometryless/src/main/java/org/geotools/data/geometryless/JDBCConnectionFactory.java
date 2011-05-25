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
package org.geotools.data.geometryless;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.data.jdbc.ConnectionPool;
import org.geotools.data.jdbc.ConnectionPoolManager;

/**
 * Creates ConnectionPool objects for a certain JDBC database instance.
 * @author Rob Atkinson rob@socialchange.net.NOSPAM.au
 * @author Gary Sheppard garysheppard@psu.edu
 *
 * @source $URL$
 */
public class JDBCConnectionFactory {

    /** Standard logging instance */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

    /** Creates configuration-driven JDBC driver class. */
    private String _driver = "";
    private static Map _dataSources = new HashMap();
    private String _dbURL;
    private String _username = "";
    private String _password = "";
    /** An alternate character set to use. */
    private String charSet;

    /**
     * Creates a new JDBCConnectionFactory object from a specified database URL.  
	This is the only constructor supported since there is significant variability in implementation syntax
<br/>
     * <br/>
     * jdbc:mysql://<host>:<port>/<instance>
     * @param url the JDBC database URL
     */
    public JDBCConnectionFactory(String url, String driver) {
       _driver = driver;
        _dbURL = url;
    }


    /**
     * Creates and returns a JDBC ConnectionPool, or gets an existing ConnectionPool
     * if one exists, based upon the username and password parameters passed to this
     * method.  This is shorthand for the following two calls:<br>
     * <br>
     * connPool.setLogin(username, password);<br>
     * connPool.getConnectionPool();<br>
     * @param username the JDBC username
     * @param password the password corresponding to <code>username</code>
     * @return a JDBC ConnectionPool object
     * @throws SQLException if an error occurs connecting to the JDBC database
     */
    public ConnectionPool getConnectionPool(String username, String password) throws SQLException {
        setLogin(username, password);
        return getConnectionPool();
    }

    /**
         * Creates a database connection method to initialize a given database for
         * feature extraction with the user and password params.
         *
         * @param user the name of the user connect to connect to the db.
         * @param password the password for the user.
         *
         * @return the sql Connection object to the database.
         *
         * @throws SQLException if the configured sql driver could not be found
         */
    public Connection getConnection(String user, String password) throws SQLException {
        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);

        if (charSet != null) {
            props.put("charSet", charSet);
        }

        return getConnection(props);
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

//       dbConnection = getConnectionPool().getConnection();

        // Instantiate the driver classes
        try {
            Class.forName("com.mysql.jdbc.Driver");
           LOGGER.fine("getting connection at " + _dbURL + " using " + _driver + " with props: " + props);
            dbConnection = DriverManager.getConnection(_dbURL, props);
        } catch (ClassNotFoundException cnfe) {
            throw new SQLException("JDBC driver ("+ _driver + " was not found.");
        }

        return dbConnection;
    }

    /**
     * Creates and returns a  ConnectionPool, or gets an existing ConnectionPool
     * if one exists, based upon the username and password set in this JDBCConnectionFactory
     * object.  Please call setLogin before calling this method, or use getConnectionPool(String, String)
     * instead.
     * @return a  ConnectionPool object
     * @throws SQLException if an error occurs connecting to the DB
     */
    public ConnectionPool getConnectionPool() throws SQLException {
        String poolKey = _dbURL + _username + _password;
 

        ConnectionPoolFacade poolDataSource =
            (ConnectionPoolFacade) _dataSources.get(poolKey);
        if (poolDataSource == null) {

              poolDataSource = new  ConnectionPoolFacade( poolKey , _driver );
    

            poolDataSource.setURL(_dbURL);
            poolDataSource.setUser(_username);
            poolDataSource.setPassword(_password);

            _dataSources.put(poolKey, poolDataSource);

        } 

        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        ConnectionPool connectionPool = manager.getConnectionPool(poolDataSource);

        return connectionPool;
    }

    /**
     * Sets the JDBC database login credentials.
     * @param username the username
     * @param password the password
     */
    public void setLogin(String username, String password) {
        _username = username;
        _password = password;
    }

    public void free(ConnectionPool connectionPool) {
        if (!connectionPool.isClosed()) {
            connectionPool.close();
        }
        ConnectionPoolManager.getInstance().free(connectionPool);
    }

    /**
         * Sets a different character set for the postgis driver to use.
         *
         * @param charSet the string of a valid charset name.
         */
    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

}
