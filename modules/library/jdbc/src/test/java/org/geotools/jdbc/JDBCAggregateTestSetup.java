/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.SQLException;

public abstract class JDBCAggregateTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCAggregateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        super.setUpData();
        // kill all the data
        try {
            dropAggregateTable();
        } catch (SQLException e) {
        }

        // create all the data
        createAggregateTable();
    }

    protected abstract void createAggregateTable() throws Exception;

    protected abstract void dropAggregateTable() throws Exception;
}
