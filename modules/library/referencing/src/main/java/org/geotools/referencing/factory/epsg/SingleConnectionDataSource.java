/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Fake data source working around a single user provided connection.
 * Used to allow backwards compatibility with the athority constructors
 * taking a single connection
 * @author Andrea Aime - OpenGeo
 *
 */
class SingleConnectionDataSource implements DataSource {

    Connection connection;
    
    public SingleConnectionDataSource(Connection connection) {
        this.connection = connection;
    }
    
    public Connection getConnection() throws SQLException {
        return connection;
    }

    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Not wrapping an object implementing "+ iface.getClass().getName() );
    }

}
