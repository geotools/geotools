/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

public class SingleStoreDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {
    public SingleStoreDataStoreAPITestSetup() {
        super(new SingleStoreTestSetup());
    }

    @Override
    protected void createRoadTable() throws Exception {
        run("CREATE ROWSTORE TABLE road(fid int PRIMARY KEY, id int, " + "geom GEOGRAPHY, name varchar(255) );");
        run("INSERT INTO road (fid,id,geom,name) VALUES ( 0,0," + "'LINESTRING(1 1, 2 2, 4 2, 5 1)'," + "'r1');");
        run("INSERT INTO road (fid,id,geom,name) VALUES ( 1,1," + "'LINESTRING(3 0, 3 2, 3 3, 3 4)'," + "'r2');");
        run("INSERT INTO road (fid,id,geom,name) VALUES ( 2,2," + "'LINESTRING(3 2, 4 2, 5 3)'," + "'r3');");
    }

    @Override
    protected void createRiverTable() throws Exception {
        // No Multiline string
        run("CREATE ROWSTORE TABLE river(fid int PRIMARY KEY, id int, "
                + "geom GEOGRAPHY, river varchar(255) , flow double );");

        run("INSERT INTO river (fid,id,geom,river, flow)  VALUES ( 0, 0,"
                + "'LINESTRING(5 5, 7 4,7 5, 9 7, 13 7,7 5, 9 3, 11 3)',"
                + "'rv1', 4.5);");
        run("INSERT INTO river (fid,id,geom,river, flow) VALUES ( 1, 1,"
                + "'LINESTRING(4 6, 4 8, 6 10)',"
                + "'rv2', 3.0);");
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE ROWSTORE TABLE lake(fid int PRIMARY KEY, id int, " + "geom GEOGRAPHY, name varchar(255) );");

        run("INSERT INTO lake (fid,id,geom,name) VALUES ( 0, 0,"
                + "'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"
                + "'muddy');");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        run("DROP TABLE IF EXISTS road");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        run("DROP TABLE IF EXISTS river");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        run("DROP TABLE IF EXISTS lake");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        run("DROP TABLE IF EXISTS building");
    }
}
