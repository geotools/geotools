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


/**
 * Extension of JDBCTestSetup which is used by {@link JDBCDataStoreAPITest}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public abstract class JDBCDataStoreAPITestSetup extends JDBCDelegatingTestSetup {
   
    protected JDBCDataStoreAPITestSetup(JDBCTestSetup delegate) {
        super( delegate );
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropRoadTable();
        } catch (SQLException e) {
        }

        try {
            dropRiverTable();
        } catch (SQLException e) {
        }

        try {
            dropLakeTable();
        } catch (SQLException e) {
        }

        try {
            dropBuildingTable();
        } catch (SQLException e) {
        }

        //create all the data
        createRoadTable();
        createRiverTable();
        createLakeTable();
    }

    /**
     * Returns the value in which primary keys start at.
     */
    protected int getInitialPrimaryKeyValue() {
        return 0;
    }

    /**
     * Drops the "road" table.
     */
    protected abstract void dropRoadTable() throws Exception;

    /**
     * Drops the "river" table.
     */
    protected abstract void dropRiverTable() throws Exception;

    /**
     * Drops the "lake" table.
     */
    protected abstract void dropLakeTable() throws Exception;

    /**
     * Drops the "building" table.
     * <p>
     * This feature type / table is created by one of the tests in teh suite,
     * and not by this test harness.
     * </p>
     */
    protected abstract void dropBuildingTable() throws Exception;

    /**
     * Creates a table with the following schema:
     * <p>
     * road( id:Integer; geom:LineString; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data:
     *  0 | LINESTRING( 1 1, 2 2, 4 2, 5 1 );srid=4326 | "r1"
     *  1 | LINESTRING( 3 0, 3 2, 3 3, 3 4 );srid=4326 | "r2"
     *  2 | LINESTRING( 3 2, 4 2, 5 3 );srid=4326 | "r3"
     * </p>
     */
    protected abstract void createRoadTable() throws Exception;

    /**
     * Creates a table with the following schema:
     * <p>
     * river( id:Integer; geom:MultiLineString; river:String; flow:Double )
     * </p>
     * <p>
     * The table should be populated with the following data:
     * 0 | MULTILINESTRING((5 5, 7 4)(7 5, 9 7, 13 7)(7 5, 9 3, 11 3));srid=4326 | "rv1" | 4.5
     * 1 | MULTILINESTRING((4 6, 4 8, 6 10));srid=4326 | "rv2" | 3.0
     * </p>
     */
    protected abstract void createRiverTable() throws Exception;

    /**
     * Creates a table with the following schema:
     * <p>
     * lake( id:Integer; geom:Polygon; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data:
     * 0 | POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6));srid=4326 | "muddy"
     * </p>
     */
    protected abstract void createLakeTable() throws Exception;
}
