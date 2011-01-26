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
package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;


public class MySQLDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {
    public MySQLDataStoreAPITestSetup() {
        super(new MySQLTestSetup());
    }

    protected int getInitialPrimaryKeyValue() {
        return 1;
    }

    protected void createRoadTable() throws Exception {
        run("CREATE TABLE road(fid int AUTO_INCREMENT PRIMARY KEY, id int, "
            + "geom LINESTRING, name varchar(255) ) ENGINE=InnoDB;");
        run("INSERT INTO road (id,geom,name) VALUES (0,"
            + "GeomFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',4326)," + "'r1')");
        run("INSERT INTO road (id,geom,name) VALUES ( 1,"
            + "GeomFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',4326)," + "'r2')");
        run("INSERT INTO road (id,geom,name) VALUES ( 2,"
            + "GeomFromText('LINESTRING(3 2, 4 2, 5 3)',4326)," + "'r3')");
    }

    protected void createRiverTable() throws Exception {
        run("CREATE TABLE river(fid int AUTO_INCREMENT PRIMARY KEY, id int, "
            + "geom MULTILINESTRING, river varchar(255) , flow double ) ENGINE=InnoDB;");

        run("INSERT INTO river (id,geom,river, flow)  VALUES ( 0,"
            + "GeomFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',4326),"
            + "'rv1', 4.5)");
        run("INSERT INTO river (id,geom,river, flow) VALUES ( 1,"
            + "GeomFromText('MULTILINESTRING((4 6, 4 8, 6 10))',4326)," + "'rv2', 3.0)");
    }

    protected void createLakeTable() throws Exception {
        run("CREATE TABLE lake(fid int AUTO_INCREMENT PRIMARY KEY, id int, "
            + "geom POLYGON, name varchar(255) ) ENGINE=InnoDB;");

        run("INSERT INTO lake (id,geom,name) VALUES ( 0,"
            + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326)," + "'muddy')");
    }

    protected void dropRoadTable() throws Exception {
        run("DROP TABLE road");
    }

    protected void dropRiverTable() throws Exception {
        run("DROP TABLE river");
    }

    protected void dropLakeTable() throws Exception {
        run("DROP TABLE lake");
    }

    protected void dropBuildingTable() throws Exception {
        run("DROP TABLE building");
    }
}
