/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import org.geotools.data.jdbc.datasource.UnWrapper;

/**
 * Un-wraps the {@link LifecycleConnection} instances
 *
 * @author Andrea Aime - GeoSolutions
 */
public class LifecycleConnectionUnWrapper implements UnWrapper {

    public boolean canUnwrap(Connection conn) {
        return conn instanceof LifecycleConnection;
    }

    public boolean canUnwrap(Statement st) {
        return false;
    }

    public Connection unwrap(Connection conn) {
        return ((LifecycleConnection) conn).delegate;
    }

    public Statement unwrap(Statement statement) {
        throw new UnsupportedOperationException("This un-wrapper cannot operate on statements");
    }
}
