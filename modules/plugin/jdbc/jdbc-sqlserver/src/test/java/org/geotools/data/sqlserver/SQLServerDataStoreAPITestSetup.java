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
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

public class SQLServerDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    public SQLServerDataStoreAPITestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createRoadTable() throws Exception {
        run("CREATE TABLE road(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
            + "geom geometry, name varchar(255) )");
        run("INSERT INTO road (id,geom,name) VALUES (0,"
            + "geometry::STGeomFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',4326)," + "'r1')");
        run("INSERT INTO road (id,geom,name) VALUES ( 1,"
            + "geometry::STGeomFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',4326)," + "'r2')");
        run("INSERT INTO road (id,geom,name) VALUES ( 2,"
            + "geometry::STGeomFromText('LINESTRING(3 2, 4 2, 5 3)',4326)," + "'r3')");
    }

    @Override
    protected void createRiverTable() throws Exception {
        run("CREATE TABLE river(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
            + "geom geometry, river varchar(255) , flow float )");

        run("INSERT INTO river (id,geom,river, flow)  VALUES ( 0,"
            + "geometry::STGeomFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',4326),"
            + "'rv1', 4.5)");
        run("INSERT INTO river (id,geom,river, flow) VALUES ( 1,"
            + "geometry::STGeomFromText('MULTILINESTRING((4 6, 4 8, 6 10))',4326)," + "'rv2', 3.0)");
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE lake(fid int IDENTITY(0,1) PRIMARY KEY, id int, "
            + "geom geometry, name varchar(255) )");

        run("INSERT INTO lake (id,geom,name) VALUES ( 0,"
            + "geometry::STGeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326)," + "'muddy')");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        run("DROP TABLE road");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        run("DROP TABLE river");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        run("DROP TABLE lake");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        run("DROP TABLE building");
    }
}
