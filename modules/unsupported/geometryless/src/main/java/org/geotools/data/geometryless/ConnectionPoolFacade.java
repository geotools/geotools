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

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * Creates ConnectionPool objects for a certain JDBC database instance.
 * 
 * @author Rob Atkinson rob@socialchange.net.NOSPAM.au
 * @author Gary Sheppard garysheppard@psu.edu
 * @source $URL$
 */
public class ConnectionPoolFacade implements ConnectionPoolDataSource {

	/** Standard logging instance */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

	private ConnectionPoolDataSource _nativePool;

	/** Creates configuration-driven JDBC driver class. */

	private String _dbURL;

	private String _username = "";

	private String _password = "";

	/**
	 * Creates a new ConnectionPool object from a specified database URL. This
	 * is normally of the following format: <br/><br/>jdbc:mysql:// <host>:
	 * <port>/ <instance>
	 * 
	 * @param url
	 *            the JDBC database URL
	 */
	public ConnectionPoolFacade(String poolKey, String driver)
			throws SQLException {

		try {
			_nativePool = (ConnectionPoolDataSource) (Class.forName(driver)
					.newInstance());
			// LOGGER.fine("Obtained ConnectionPoolDataSource " + _nativePool
			//		+ " from driver " + driver);

		} catch (Exception e) {
			throw new SQLException(
					"Failed to instantiate connection pool using " + driver
							+ "(" + e + ")");
		}
	}

	/**
	 * Sets the JDBC database login credentials.
	 * 
	 * @param username
	 *            the username
	 *  
	 */
	public void setUser(String username) {
		_username = username;

	}

	/**
	 * Sets the JDBC database login credentials.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public void setURL(String dbURL) throws SQLException {

		_dbURL = dbURL;

		try {

			_nativePool.getClass().getMethod("setURL",
					new Class[] { String.class }).invoke(_nativePool,
					new Object[] { _dbURL });
		} catch (Exception e) {
			
			LOGGER.severe("Failed to instantiate connection pool trying setURL method with value "
							+ _dbURL);
			LOGGER.severe("Either the info.xml urlprefix is incorrect, or you need to use an org.geotools.data.geometryless.wrapper class,");
			LOGGER.severe("or you need to wrapper your driver to provide a setURL method");			
			
			throw new SQLException(
					"Failed to instantiate connection pool using " + _dbURL
							+ "(" + e + ")");
		}

		LOGGER.fine("Instantiated connection pool using " + _dbURL);
	}

	/**
	 * Sets the JDBC database login credentials.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public void setPassword(String password) {

		_password = password;
	}

	public PooledConnection getPooledConnection() throws SQLException {

		return getPooledConnection(_username, _password);
	}

	/* these are all the methods needed for the interface... */

	public PooledConnection getPooledConnection(String user, String password)
			throws SQLException {

		return _nativePool.getPooledConnection(user, password);
	}

	public int getLoginTimeout() throws SQLException {
		return _nativePool.getLoginTimeout();
	}

	public java.io.PrintWriter getLogWriter() throws SQLException {
		return _nativePool.getLogWriter();
	}

	public void setLogWriter(java.io.PrintWriter out) throws SQLException {
		_nativePool.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		_nativePool.setLoginTimeout(seconds);
	}
}
