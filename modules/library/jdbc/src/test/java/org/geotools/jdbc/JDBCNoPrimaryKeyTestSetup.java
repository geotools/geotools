/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

public abstract class JDBCNoPrimaryKeyTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCNoPrimaryKeyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropLakeTable();
        } catch (SQLException e) {
        }

        //create all the data
        createLakeTable();
    }
    
    /**
     * Creates a table with the following schema:
     * <p>
     * lake( id:Integer; geom:Polygon; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data:
     * <pre>
     * 0 | POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6));srid=4326 | "muddy"
     * </pre>
     * </p>
     * <p>
     * For this test make sure the table has no primary key whatsoever.
     * </p>
     */
    protected abstract void createLakeTable() throws Exception;
    
    /**
     * Drops the "lake" table.
     */
    protected abstract void dropLakeTable() throws Exception;

}
