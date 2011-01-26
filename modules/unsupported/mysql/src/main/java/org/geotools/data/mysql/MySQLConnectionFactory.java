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

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;


/**
 * Creates ConnectionPool objects for a certain MySQL database instance.
 * @author Gary Sheppard garysheppard@psu.edu
 * @source $URL$
 * @deprecated Use {@link DataSource}, {@link DataSourceUtil} and {@link DataSourceFinder} instead
 */
public class MySQLConnectionFactory {
    /** Standard logging instance */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.mysql");

    /** Creates Mysql-specific JDBC driver class. */
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String MYSQL_URL_PREFIX = "jdbc:mysql://";
    private static Map _dataSources = new HashMap();
    private String _dbURL;
    private String _username = "";
    private String _password = "";

    /**
     * Creates a new MySQLConnectionFactory object from a MySQL database URL.  This
     * is normally of the following format:<br/>
     * <br/>
     * jdbc:mysql://<host>:<port>/<instance>
     * @param url the MySQL database URL
     */
    public MySQLConnectionFactory(String url) {
        _dbURL = url;
    }

    /**
     * Creates a new MySQLConnectionFactory object from a host name, port number,
     * and instance name.
     * @param host the MySQL database host
     * @param port the port number for the MySQL database
     * @param instance the MySQL database instance name
     */
    public MySQLConnectionFactory(String host, int port, String instance) {
        this(MYSQL_URL_PREFIX + host + ":" + String.valueOf(port) + "/" + instance);
    }

    /**
     * Creates a new MySQLConnectionFactory object from a host name and an instance
     * name, using the normal MySQL port number of 3306.
     * @param host the MySQL database host
     * @param instance the MySQL database instance name
     */
    public MySQLConnectionFactory(String host, String instance) {
        this(host, 3306, instance);
    }

    /**
     * Creates and returns a MySQL ConnectionPool, or gets an existing ConnectionPool
     * if one exists, based upon the username and password parameters passed to this
     * method.  This is shorthand for the following two calls:<br>
     * <br>
     * connPool.setLogin(username, password);<br>
     * connPool.getConnectionPool();<br>
     * @param username the MySQL username
     * @param password the password corresponding to <code>username</code>
     * @return a MySQL ConnectionPool object
     * @throws SQLException if an error occurs connecting to the MySQL database
     */
    public ConnectionPool getConnectionPool(String username, String password)
        throws SQLException {
        setLogin(username, password);

        return getConnectionPool();
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
            LOGGER.finest("getting connection at " + _dbURL + "with props: " + props);
            dbConnection = DriverManager.getConnection(_dbURL, props);
        } catch (ClassNotFoundException cnfe) {
            throw new SQLException("Postgis driver was not found.");
        }

        return dbConnection;
    }

    /**
     * Creates and returns a MySQL ConnectionPool, or gets an existing ConnectionPool
     * if one exists, based upon the username and password set in this MySQLConnectionFactory
     * object.  Please call setLogin before calling this method, or use getConnectionPool(String, String)
     * instead.
     * @return a MySQL ConnectionPool object
     * @throws SQLException if an error occurs connecting to the DB
     */
    public ConnectionPool getConnectionPool() throws SQLException {
        String poolKey = _dbURL + _username + _password;
        MysqlConnectionPoolDataSource poolDataSource = (MysqlConnectionPoolDataSource) _dataSources
            .get(poolKey);

        if (poolDataSource == null) {
            poolDataSource = new MysqlConnectionPoolDataSource();

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
     * Sets the MySQL database login credentials.
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
}
