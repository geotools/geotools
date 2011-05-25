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

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * A closeable wrapper around {@link BasicDataSource}
 * 
 * @author Administrator
 * 
 *
 *
 * @source $URL$
 */
public class DBCPDataSource extends AbstractManageableDataSource {

    public DBCPDataSource(BasicDataSource wrapped) {
        super(wrapped);

    }

    public DataSource getWrapped() {
        return wrapped;
    }

    public void close() throws SQLException {
        ((BasicDataSource) wrapped).close();
    }

    @Override
    public boolean isWrapperFor(Class c) throws SQLException {
        if (DataSource.class.isAssignableFrom(c)) {
            return true;
        }
        return false;
    }
    
    @Override
    public Object unwrap(Class c) throws SQLException {
        if (isWrapperFor(c)) {
            return getWrapped();
        }
        return null;
    }
}
