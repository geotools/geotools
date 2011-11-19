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
package org.geotools.jdbc;

import java.sql.SQLException;
import java.util.UUID;

public abstract class JDBCUuidTestSetup extends JDBCDelegatingTestSetup {

    protected UUID uuid1 = UUID.fromString("c563f527-507e-4b80-a30b-4cab189d4dba");
    protected UUID uuid2 = UUID.fromString("cae47178-2e84-4319-a5ba-8d4089c9d80d");

    protected JDBCUuidTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropUuidTable();
        } catch (SQLException e) {
        }

        //create all the data
        createUuidTable();
    }

    /**
     * Creates a table with the following schema:
     * <p>
     * b( id:Integer; boolProperty: Boolean)
     * </p>
     * <p>
     * The table should contain the following data.
     *  0 | false
     *  1 | true
     * </p>
     */
    protected abstract void createUuidTable() throws Exception;

    /**
     * Drops the "b" table previously created
     */
    protected abstract void dropUuidTable() throws Exception;

}
