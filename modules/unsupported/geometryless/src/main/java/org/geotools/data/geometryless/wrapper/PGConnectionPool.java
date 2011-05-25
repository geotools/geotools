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
 package org.geotools.data.geometryless.wrapper;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

//import javax.sql.ConnectionPoolDataSource;

/**
 * <p>
 * Wraps a Postgres ConnectionPoolDataSource implementation to support the setURL()
 * method
 * </p>
 * <p>
 * The jdbc3 8.0x driver includes org.postgresql.jdbc2.optional.ConnectionPool,
 * which wrappers org.postgresql.ds.PGConnectionPool, so you could use that as
 * the extension class for 8.0x driver, ie instead of
 * org.postgresql.jdbc2.optional.ConnectionPool, but it does't really matter,
 * however that's why this class has the name it has
 * </p>
 * <p>
 * in info.xml, you need to set <parameter name="driver" value=
 * "org.geotools.data.geometryless.wrapper.PGConnectionPool"/>
 * </p>
 * 
 * @author Rob Atkinson rob@socialchange.net.NOSPAM.au
 * @author Peter Barrs@socialchange.net.NOSPAM.au
 *
 * @source $URL$
 */
public class PGConnectionPool extends org.postgresql.jdbc2.optional.ConnectionPool { 
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -1157879368880510850L;

	/** Standard logging instance */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

	//private ConnectionPoolDataSource _nativePool;

	/** Creates configuration-driven JDBC driver class. */

	private String _dbURL;

	//private String _username = "";

	//private String _password = "";

	/**
	 * Sets the JDBC database login credentials.
	 * 
	 * @param complete
	 *            JDBC connectionURL
	 */
	public void setURL(String dbURL) throws SQLException {

		_dbURL = dbURL;
		try {
			// _nativePool.getClass().getMethod("setURL", new Class [] {
			// String.class } ).invoke( _nativePool, new Object [] { _dbURL } );
			Properties props = parseURL(_dbURL, new Properties());

			LOGGER.finer("setting ServerName = " + props.getProperty("ServerName"));
			setServerName(props.getProperty("ServerName"));
			
			if (props.getProperty("PortNumber") != null || !props.getProperty("PortNumber").equals("")) {
				LOGGER.finer("setting ServerName = " + props.getProperty("PortNumber"));
				setPortNumber(Integer.parseInt(props.getProperty("PortNumber")));
			}
			
			LOGGER.finer("setting DatabaseName = " + props.getProperty("DatabaseName"));
			setDatabaseName(props.getProperty("DatabaseName"));

		} catch (Exception e) {
			LOGGER.severe("Failed to instantiate connection pool using " + _dbURL);
			throw new SQLException(
					"Failed to instantiate connection pool using " + _dbURL
							+ "(" + e + ")");
		}
	}

	/**
	 * Parse the driver URL and extract the properties.
	 * 
	 * @param url
	 *            The URL to parse.
	 * @param info
	 *            Any existing properties already loaded in a Properties object.
	 * @return The URL properties as a <code>Properties</code> object.
	 */
	private static Properties parseURL(String url, Properties info) {
		Properties props = new Properties();

		// Take local copy of existing properties
		for (Enumeration e = info.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = info.getProperty(key);

			if (value != null) {
				props.setProperty(key.toUpperCase(), value);
			}
		}

		StringBuffer token = new StringBuffer(128);
		int pos = 0;

		pos = nextToken(url, pos, token);
		// LOGGER.fine(token.toString());

		if (!token.toString().equalsIgnoreCase("jdbc")) {
			LOGGER.severe("expected token jdbc in urlprefix " + url);
			return null; // jdbc: missing
		}

		pos = nextToken(url, pos, token); // Skip jtds
		// LOGGER.fine(token.toString());

		if (!token.toString().equalsIgnoreCase("postgresql")) {
			LOGGER.severe("expected token postgresql in urlprefix " + url);			
			return null; // postgresql: missing
		}

		pos = nextToken(url, pos, token); // Null token between : and //
		if (token.length() > 0) {
			LOGGER.severe("There should be no token between : and // in urlprefix " + url);			
			return null; // There should not be one!
		}

		pos = nextToken(url, pos, token); // Get server name
		// LOGGER.fine(token.toString());

		String host = token.toString();
		if (host == null || host.length() == 0) {
			LOGGER.severe("Missing server (host) name in urlprefix " + url);			
			return null; // Server name missing
		}
		props.setProperty("ServerName", host);
		
		if (url.charAt(pos - 1) == ':' && pos < url.length()) {
			pos = nextToken(url, pos, token); // Get port number
			// LOGGER.fine(token.toString());			

			try {
				int port = Integer.parseInt(token.toString());
				props.setProperty("PortNumber", Integer
						.toString(port));
			} catch (NumberFormatException e) {
				LOGGER.severe("Bad port number in urlprefix " + url);			
				return null; // Bad port number
			}
		}

		if (url.charAt(pos - 1) == '/' && pos < url.length()) {
			pos = nextToken(url, pos, token); // Get database name
			// LOGGER.fine(token.toString());			
			props.setProperty("DatabaseName", token.toString());
		}

		//
		// Process any additional properties in URL
		//
		
		// but we ignore anyway - pb
/*		
		while (url.charAt(pos - 1) == ';' && pos < url.length()) {
			pos = nextToken(url, pos, token);
			String tmp = token.toString();
			int index = tmp.indexOf('=');

			if (index > 0 && index < tmp.length() - 1) {
				props.setProperty(tmp.substring(0, index).toUpperCase(), tmp
						.substring(index + 1));
			} else {
				props.setProperty(tmp.toUpperCase(), "");
			}
		}
*/			
		return props;
	}

	/**
	 * Extract the next lexical token from the URL.
	 * 
	 * @param url
	 *            The URL being parsed
	 * @param pos
	 *            The current position in the URL string.
	 * @param token
	 *            The buffer containing the extracted token.
	 * @return The updated position as an <code>int</code>.
	 */
	private static int nextToken(String url, int pos, StringBuffer token) {
		token.setLength(0);

		while (pos < url.length()) {
			char ch = url.charAt(pos++);

			if (ch == ':' || ch == ';') {
				break;
			}

			if (ch == '/') {
				if (pos < url.length() && url.charAt(pos) == '/') {
					pos++;
				}

				break;
			}

			token.append(ch);
		}

		return pos;
	}

}
