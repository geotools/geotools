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
package org.geotools.data.jdbc.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * An abstract wrapper created to ease the setup of a
 * {@link ManageableDataSource}, you just have to subclass and create a close
 * method
 * 
 * @author Andrea Aime - TOPP
 * 
 *
 * @source $URL$
 */
public abstract class AbstractManageableDataSource implements ManageableDataSource {

    protected DataSource wrapped;

    public AbstractManageableDataSource(DataSource wrapped) {
        this.wrapped = wrapped;
    }

    public Connection getConnection() throws SQLException {
        return wrapped.getConnection();
    }

    public Connection getConnection(String username, String password)
            throws SQLException {
        return wrapped.getConnection(username, password);
    }

    public int getLoginTimeout() throws SQLException {
        return wrapped.getLoginTimeout();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return wrapped.getLogWriter();
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        wrapped.setLoginTimeout(seconds);
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        wrapped.setLogWriter(out);
    }
    
    public boolean isWrapperFor(Class c) throws SQLException {
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		throw new SQLException("This implementation cannot unwrap anything");
	}
}
