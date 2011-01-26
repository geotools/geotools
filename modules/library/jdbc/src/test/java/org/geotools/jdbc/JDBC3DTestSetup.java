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

public abstract class JDBC3DTestSetup extends JDBCDelegatingTestSetup {

    protected JDBC3DTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropPoint3DTable();
        } catch (SQLException e) {
        }

        try {
            dropLine3DTable();
        } catch (SQLException e) {
        }

        try {
            dropPoly3DTable();
        } catch (SQLException e) {
        }

        //create all the data
        createPoint3DTable();
        createLine3DTable();
    }


    /**
     * Drops the "poly3D" table
     */
    protected abstract void dropPoly3DTable() throws Exception;


    /**
     * Creates a table with the following schema:
     * <p>
     * line3D( id:Integer; geom:LineString; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data (assume 3d coordinates):
     *  0 | LINESTRING( 1 1 0, 2 2 0, 4 2 1, 5 1 1);srid=4326 | "l1"
     *  1 | LINESTRING( 3 0 1, 3 2 2, 3 3 3, 3 4 5);srid=4326 | "l2"
     * </p>
     */
    protected abstract void createLine3DTable() throws Exception;
    
    /**
     * Creates a table with the following schema:
     * <p>
     * point3D( id:Integer; geom:Point; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data (assume 3d coordinates):
     *  0 | POINT(1 1 1);srid=4326 | "p1"
     *  1 | POINT(3 0 1);srid=4326 | "p2"
     * </p>
     */
    protected abstract void createPoint3DTable() throws Exception;

    /**
     * Drops the "line3D" table
     */
    protected abstract void dropLine3DTable() throws Exception;


    /**
     * Drops the "point3D" table
     */
    protected abstract void dropPoint3DTable() throws Exception;
}
