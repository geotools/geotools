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

/** Same as JDBCCompound3DTestSetup, but using the compound CRS https://epsg.io/7415 as the CRS */
public abstract class JDBCCompound3DTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCCompound3DTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        // kill all the data
        try {
            dropPointCompound3DTable();
        } catch (SQLException e) {
        }

        try {
            dropLineCompound3DTable();
        } catch (SQLException e) {
        }

        try {
            dropPolyCompound3DTable();
        } catch (SQLException e) {
        }

        // create all the data
        createPointCompound3DTable();
        createLineCompound3DTable();
    }

    /** Drops the "polyCompound3D" table */
    protected abstract void dropPolyCompound3DTable() throws Exception;

    /**
     * Creates a table with the following schema:
     *
     * <p>line3D( id:Integer; geom:LineString; name:String )
     *
     * <p>The table should be populated with the following data (assume 3d coordinates): 0 |
     * LINESTRING( 1 1 0, 2 2 0, 4 2 1, 5 1 1);srid=7415 | "l2"
     */
    protected abstract void createLineCompound3DTable() throws Exception;

    /**
     * Creates a table with the following schema:
     *
     * <p>point3D( id:Integer; geom:Point; name:String )
     *
     * <p>The table should be populated with the following data (assume 3d coordinates): 0 | POINT(1
     * 1 1);srid=7415 | "p1" 1 | POINT(3 0 1);srid=7415 | "p2"
     */
    protected abstract void createPointCompound3DTable() throws Exception;

    /** Drops the "line3D" table */
    protected abstract void dropLineCompound3DTable() throws Exception;

    /** Drops the "point3D" table */
    protected abstract void dropPointCompound3DTable() throws Exception;
}
