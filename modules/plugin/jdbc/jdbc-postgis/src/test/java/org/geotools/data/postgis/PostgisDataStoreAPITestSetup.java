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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    public PostgisDataStoreAPITestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'lake', 'geom', 2, '4326', 'POLYGON')");
        run("CREATE INDEX LAKE_GEOM_INDEX ON \"lake\" USING GIST (\"geom\") ");
        
        // advance the sequence to 1 to compensate for hand insertions
        run("SELECT nextval(pg_get_serial_sequence('lake','fid'))");

        run("INSERT INTO \"lake\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void createRiverTable() throws Exception {
        run("CREATE TABLE \"river\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"river\" varchar , \"flow\" real )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'river', 'geom', 2, '4326', 'MULTILINESTRING')");
        run("CREATE INDEX RIVER_GEOM_INDEX ON \"river\" USING GIST (\"geom\") ");
        
        // advance the sequence to 1 to compensate for hand insertions
        run("SELECT nextval(pg_get_serial_sequence('river','fid'))");

        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\")  VALUES (0, 0,"
                + "GeomFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',4326),"
                + "'rv1', 4.5)");
        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\") VALUES (1, 1,"
                + "GeomFromText('MULTILINESTRING((4 6, 4 8, 6 10))',4326),"
                + "'rv2', 3.0)");
    }

    @Override
    protected void createRoadTable() throws Exception {
        // create table and spatial index
        run("CREATE TABLE \"road\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                + "\"geom\" geometry, \"name\" varchar )");
        run("INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'road', 'geom', 2, '4326', 'LINESTRING')");
        run("CREATE INDEX ROAD_GEOM_INDEX ON \"road\" USING GIST (\"geom\") ");

        // advance the sequence to 2 to compensate for hand insertions
        run("SELECT nextval(pg_get_serial_sequence('road','fid'))");
        run("SELECT nextval(pg_get_serial_sequence('road','fid'))");

        // insertions
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "GeomFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',4326),"
                + "'r1')");
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (1, 1,"
                + "GeomFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',4326),"
                + "'r2')");
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (2, 2,"
                + "GeomFromText('LINESTRING(3 2, 4 2, 5 3)',4326)," + "'r3')");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        runSafe("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'building'");
        runSafe("DROP TABLE \"building\"");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lake'");
        runSafe("DROP TABLE \"lake\"");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'river'");
        runSafe("DROP TABLE \"river\"");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'road'");
        runSafe("DROP TABLE \"road\"");
    }

}
