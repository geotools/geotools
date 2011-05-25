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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.geotools.data.jdbc.ConnectionPool;
import org.geotools.data.jdbc.ConnectionPoolManager;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.DataSourceUtil;


/**
 * Provides javax.sql.DataSource wrapper around an OracleConnection object.
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author $Author: seangeo $
 *
 * @source $URL$
 * @version $Id$
 * @deprecated Use {@link DataSource}, {@link DataSourceUtil} and {@link DataSourceFinder} instead
 */
public class OracleConnectionFactory {
    /** The prefix of an Oracle JDBC url */
    private static final String JDBC_PATH = "jdbc:oracle:thin:@";

    /** The prefix of an Oracle OCI JDBC url */
    private static final String OCI_PATH = "jdbc:oracle:oci:@";

    /** Map that contains Connection Pool Data Sources */
    private static Map dataSources = new HashMap();

    /** The url to the DB */
    private String dbUrl;

    /** The username to login with */
    private String username = "";

    /** The password to login with */
    private String passwd = "";

    /**
     * BDT ADDED 2004/08/02 This constructor sets up a connection factor for
     * OCI connection. The alias refers to values defined by Oracle Net
     * Configuration assitant and stored in
     * $ORACLE_HOME/network/admin/tnsnames.ora OCI connection require you to
     * install the oracle client software. The classes12.jar MUST be taken
     * from $ORACLE_HOME/jdbc/lib Creates a new OracleConnection object that
     * wraps a oracle.jdbc.driver.OracleConnection.
     *
     * @param alias The host to connect to.
     */
    public OracleConnectionFactory(String alias) {
        dbUrl = OCI_PATH + alias;
    }

    /**
     * Creates a new OracleConnection object that wraps a
     * oracle.jdbc.driver.OracleConnection.
     *
     * @param host The host to connect to.
     * @param port The port number on the host
     * @param instance The instance name on the host
     */
    public OracleConnectionFactory(String host, String port, String instance) {
    	if( instance.startsWith("(") )
        dbUrl = JDBC_PATH + instance;
    	else if( instance.startsWith("/") )
        dbUrl = JDBC_PATH + "//" + host + ":" + port + instance;
    	else
        dbUrl = JDBC_PATH + host + ":" + port + ":" + instance;
    }

    /**
     * Creates the real OracleConnection. Logs in to the Oracle Database and
     * creates the OracleConnection object.
     *
     * @param user The user name.
     * @param pass The password
     *
     * @return The real OracleConnection object.
     *
     * @throws SQLException If an error occurs connecting to the DB.
     */
    public ConnectionPool getConnectionPool(String user, String pass)
        throws SQLException {
        String poolKey = dbUrl + user + pass;
        OracleConnectionPoolDataSource poolDataSource = (OracleConnectionPoolDataSource) dataSources
            .get(poolKey);

        if (poolDataSource == null) {
            poolDataSource = new OracleConnectionPoolDataSource();

            poolDataSource.setURL(dbUrl);
            poolDataSource.setUser(user);
            poolDataSource.setPassword(pass);

            dataSources.put(poolKey, poolDataSource);
        }

        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        ConnectionPool connectionPool = manager.getConnectionPool(poolDataSource);

        return connectionPool;
    }

    /**
     * Creates the real OracleConnection.  Logs into the database using the
     * credentials provided by setLogin
     *
     * @return The oracle connection to the data base.
     *
     * @throws SQLException If an error occurs connecting to the DB.
     */
    public ConnectionPool getConnectionPool() throws SQLException {
        return getConnectionPool(username, passwd);
    }

    /**
     * Sets the login credentials.
     *
     * @param user The username
     * @param pass The password
     */
    public void setLogin(String user, String pass) {
        this.username = user;

        this.passwd = pass;
    }
}
