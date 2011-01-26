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

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.dbcp.DelegatingConnection;
import org.apache.commons.dbcp.DelegatingStatement;

/**
 * Unwraps DBCP managed connections
 * 
 * @author Andrea Aime - TOPP
 * 
 *
 * @source $URL$
 */
public class DBCPUnWrapper implements UnWrapper {

    public boolean canUnwrap(Connection conn) {
        return conn instanceof DelegatingConnection;
    }

    public Connection unwrap(Connection conn) {
        if (!canUnwrap(conn))
            throw new IllegalArgumentException("This unwrapper can only handle instances of "
                    + DelegatingConnection.class);
        Connection unwrapped = ((DelegatingConnection) conn).getInnermostDelegate();
        if (unwrapped == null)
            throw new RuntimeException("Could not unwrap connection. Is the DBCP pool configured "
                    + "to allow access to underlying connections?");
        return unwrapped;
    }

    public boolean canUnwrap(Statement st) {
        return st instanceof DelegatingStatement;
    }

    public Statement unwrap(Statement statement) {
        if(!canUnwrap(statement))
            throw new IllegalArgumentException("This unwrapper can only handle instances of "
                    + DelegatingStatement.class);
        Statement unwrapped = ((DelegatingStatement) statement).getInnermostDelegate();
        if (unwrapped == null)
            throw new RuntimeException("Could not unwrap connection. Is the DBCP pool configured "
                    + "to allow access to underlying connections?");
        return unwrapped;
    }

}
