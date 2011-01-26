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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

public class SpatiaLiteDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    protected SpatiaLiteDataStoreAPITestSetup() {
        super(new SpatiaLiteTestSetup());
    }
    
    @Override
    protected int getInitialPrimaryKeyValue() {
        return 0;
    }

    @Override
    protected void createLakeTable() throws Exception {
        run( "CREATE TABLE lake (fid INTEGER PRIMARY KEY, id INTEGER)");
        run( "SELECT AddGeometryColumn('lake','geom',4326,'POLYGON',2)");
        run( "ALTER TABLE lake add name VARCHAR" );
        run( "INSERT INTO lake VALUES (0, 0," +
            "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        run( "DROP TABLE lake");
        run( "DELETE FROM geometry_columns WHERE f_table_name = 'lake'");
    }
    
    @Override
    protected void createRiverTable() throws Exception {
        run( "CREATE TABLE river (fid INTEGER PRIMARY KEY, id INTEGER)");
        run( "SELECT AddGeometryColumn('river','geom',4326,'MULTILINESTRING',2)");
        run( "ALTER TABLE river add river VARCHAR" );
        run( "ALTER TABLE river add flow FLOAT" );
        
        run("INSERT INTO river VALUES (0, 0,"
                + "GeomFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',4326),"
                + "'rv1', 4.5)");
        run("INSERT INTO river VALUES (1, 1,"
                + "GeomFromText('MULTILINESTRING((4 6, 4 8, 6 10))',4326),"
                + "'rv2', 3.0)");
    }
    
    @Override
    protected void dropRiverTable() throws Exception {
        run( "DROP TABLE river");
        run( "DELETE FROM geometry_columns WHERE f_table_name = 'river'");
    }
    
    @Override
    protected void createRoadTable() throws Exception {
        run( "CREATE TABLE road (fid INTEGER PRIMARY KEY, id INTEGER)");
        run( "SELECT AddGeometryColumn('road','geom',4326,'LINESTRING',2)");
        run( "ALTER TABLE road add name VARCHAR" );
        
        run("INSERT INTO road VALUES (0, 0,"
                + "GeomFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',4326),"
                + "'r1')");
        run("INSERT INTO road VALUES (1, 1,"
                + "GeomFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',4326),"
                + "'r2')");
        run("INSERT INTO road VALUES (2, 2,"
                + "GeomFromText('LINESTRING(3 2, 4 2, 5 3)',4326)," + "'r3')");
        
    }
    
    @Override
    protected void dropRoadTable() throws Exception {
        run( "DROP TABLE road");
        run( "DELETE FROM geometry_columns WHERE f_table_name = 'road'");
    }
  

    @Override
    protected void dropBuildingTable() throws Exception {
        run( "DROP TABLE building");
        run( "DELETE FROM geometry_columns WHERE f_table_name = 'building'");
    }
}
