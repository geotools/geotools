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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    public TeradataDataStoreAPITestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }


    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, \"geom\" st_geometry, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'lake', 'geom', 2, 1619, 'POLYGON')");
//        run("CREATE INDEX LAKE_GEOM_INDEX ON \"lake\" USING GIST (\"geom\") ");

        // advance the sequence to 1 to compensate for hand insertions
//        run("SELECT nextval(pg_get_serial_sequence('lake','fid'))");

        run("INSERT INTO \"lake\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy');");
    }


    protected void createRiverTable() throws Exception {
        run("CREATE TABLE \"river\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, "
                + "\"geom\" ST_GEOMETRY, \"river\" varchar(200) , \"flow\" real )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'river', 'geom', 2, 1619, 'MULTILINESTRING')");
//        run("CREATE INDEX RIVER_GEOM_INDEX ON \"river\" USING GIST (\"geom\") ");

        // advance the sequence to 1 to compensate for hand insertions
//        run("SELECT nextval(pg_get_serial_sequence('river','fid'))");

        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\")  VALUES (0, 0,"
                + "'MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',"
                + "'rv1', 4.5)");
        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\") VALUES (1, 1,"
                + "'MULTILINESTRING((4 6, 4 8, 6 10))',"
                + "'rv2', 3.0)");
    }


    protected void createRoadTable() throws Exception {
        // create table and spatial index
        run("CREATE TABLE \"road\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, "
                + "\"geom\" ST_GEOMETRY, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + fixture.getProperty("schema") + "', 'road', 'geom', 2, 1619, 'LINESTRING')");
//        run("CREATE INDEX ROAD_GEOM_INDEX ON \"road\" USING GIST (\"geom\") ");

        // advance the sequence to 2 to compensate for hand insertions
//        run("SELECT nextval(pg_get_serial_sequence('road','fid'))");
//        run("SELECT nextval(pg_get_serial_sequence('road','fid'))");

        // insertions
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "'LINESTRING(1 1, 2 2, 4 2, 5 1)',"
                + "'r1')");
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (1, 1,"
                + "'LINESTRING(3 0, 3 2, 3 3, 3 4)',"
                + "'r2')");
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (2, 2,"
                + "'LINESTRING(3 2, 4 2, 5 3)'," + "'r3')");
    }


    protected void dropBuildingTable() throws Exception {
        runSafe("DELETE FROM  SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'building'");
        runSafe("DROP TABLE \"building\"");
    }


    protected void dropLakeTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lake'");
        runSafe("DROP TABLE \"lake\"");
    }


    protected void dropRiverTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'river'");
        runSafe("DROP TABLE \"river\"");
    }


    protected void dropRoadTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'road'");
        runSafe("DROP TABLE \"road\"");
    }

}
