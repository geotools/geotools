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

    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }
    
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, \"geom\" st_geometry, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '"
                + fixture.getProperty("schema") + "', 'lake', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");

        // add the spatial index
        run("CREATE MULTISET TABLE \"lake_geom_idx\""
                + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");
//        run("CREATE TRIGGER \"lake_geom_mi\" AFTER INSERT ON lake"
//                + "  REFERENCING NEW TABLE AS nt" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
//                + "  (" + "    INSERT INTO \"lake_geom_idx\"" + "    SELECT" + "    id,"
//                + "    sysspatial.tessellate_index ("
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(),"
//                + "      -180, -90, 180, 90, 100, 100, 1, 0.01, 0)" + "    FROM nt;" + "  ) "
//                + "END");
//        run("CREATE TRIGGER \"lake_geom_md\" AFTER DELETE ON \"lake\""
//                + "  REFERENCING OLD TABLE AS ot" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
//                + "  (" + "    DELETE FROM \"lake_geom_idx\" WHERE ID IN (SELECT ID from ot);"
//                + "  )" + "END");

        // advance the sequence to 1 to compensate for hand insertions
        // run("SELECT nextval(pg_get_serial_sequence('lake','fid'))");

        run("INSERT INTO \"lake\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy');");
    }

    protected void createRiverTable() throws Exception {
        run("CREATE TABLE \"river\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, "
                + "\"geom\" ST_GEOMETRY, \"river\" varchar(200) , \"flow\" real )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '"
                + fixture.getProperty("schema") + "', 'river', 'geom', 2, " + getDelegate().getSrid4326() + ", 'MULTILINESTRING')");

        run("CREATE MULTISET TABLE \"river_geom_idx\""
                + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");
//        run("CREATE TRIGGER \"river_geom_mi\" AFTER INSERT ON river"
//                + "  REFERENCING NEW TABLE AS nt" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
//                + "  (" + "    INSERT INTO \"river_geom_idx\"" + "    SELECT" + "    id,"
//                + "    sysspatial.tessellate_index ("
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(),"
//                + "      -180, -90, 180, 90, 100, 100, 1, 0.01, 0)" + "    FROM nt;" + "  ) "
//                + "END");
//        run("CREATE TRIGGER \"river_geom_md\" AFTER DELETE ON \"river\""
//                + "  REFERENCING OLD TABLE AS ot" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
//                + "  (" + "    DELETE FROM \"river_geom_idx\" WHERE ID IN (SELECT ID from ot);"
//                + "  )" + "END");
        // advance the sequence to 1 to compensate for hand insertions
        // run("SELECT nextval(pg_get_serial_sequence('river','fid'))");

        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\")  VALUES (0, 0,"
                + "'MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',"
                + "'rv1', 4.5)");
        run("INSERT INTO \"river\" (\"fid\", \"id\",\"geom\",\"river\", \"flow\") VALUES (1, 1,"
                + "'MULTILINESTRING((4 6, 4 8, 6 10))'," + "'rv2', 3.0)");
    }

    protected void createRoadTable() throws Exception {
        // create table and spatial index
        run("CREATE TABLE \"road\"(\"fid\" PRIMARY KEY not null generated always as identity (start with 0) integer, \"id\" int, "
                + "\"geom\" ST_GEOMETRY, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '"
                + fixture.getProperty("schema") + "', 'road', 'geom', 2, " + getDelegate().getSrid4326() + ", 'LINESTRING')");

        run("CREATE MULTISET TABLE \"road_geom_idx\""
                + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");
//        run("CREATE TRIGGER \"road_geom_mi\" AFTER INSERT ON road"
//                + "  REFERENCING NEW TABLE AS nt" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
//                + "  (" + "    INSERT INTO \"road_geom_idx\"" + "    SELECT" + "    id,"
//                + "    sysspatial.tessellate_index ("
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(),"
//                + "      \"geom\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(),"
//                + "      -180, -90, 180, 90, 100, 100, 1, 0.01, 0)" + "    FROM nt;" + "  ) "
//                + "END");
//        run("CREATE TRIGGER \"road_geom_md\" AFTER DELETE ON \"road\""
//                + "  REFERENCING OLD TABLE AS ot" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
//                + "  (" + "    DELETE FROM \"road_geom_idx\" WHERE ID IN (SELECT ID from ot);"
//                + "  )" + "END");
        // advance the sequence to 2 to compensate for hand insertions
        // run("SELECT nextval(pg_get_serial_sequence('road','fid'))");
        // run("SELECT nextval(pg_get_serial_sequence('road','fid'))");

        // insertions
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (0, 0,"
                + "'LINESTRING(1 1, 2 2, 4 2, 5 1)'," + "'r1')");
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (1, 1,"
                + "'LINESTRING(3 0, 3 2, 3 3, 3 4)'," + "'r2')");
        run("INSERT INTO \"road\" (\"fid\", \"id\",\"geom\",\"name\") VALUES (2, 2,"
                + "'LINESTRING(3 2, 4 2, 5 3)'," + "'r3')");
    }

    protected void dropBuildingTable() throws Exception {
        runSafe("DELETE FROM  SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'building'");
        runSafe("DROP TRIGGER \"building_the_geom_mi\"");
        runSafe("DROP TRIGGER \"building_the_geom_mu\"");
        runSafe("DROP TRIGGER \"building_the_geom_md\"");
        runSafe("DROP TABLE \"building_the_geom_idx\"");
        runSafe("DROP TABLE \"building\"");
    }

    protected void dropLakeTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lake'");
        runSafe("DROP TRIGGER \"lake_geom_mi\"");
        runSafe("DROP TRIGGER \"lake_geom_mu\"");
        runSafe("DROP TRIGGER \"lake_geom_md\"");
        runSafe("DROP TABLE \"lake\"");
        runSafe("DROP TABLE \"lake_geom_idx\"");
    }

    protected void dropRiverTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'river'");
        runSafe("DROP TRIGGER \"river_geom_mi\"");
        runSafe("DROP TRIGGER \"river_geom_mu\"");
        runSafe("DROP TRIGGER \"river_geom_md\"");
        runSafe("DROP TABLE \"river\"");
        runSafe("DROP TABLE \"river_geom_idx\"");
    }

    protected void dropRoadTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'road'");
        runSafe("DROP TRIGGER \"road_geom_mi\"");
        runSafe("DROP TRIGGER \"road_geom_mu\"");
        runSafe("DROP TRIGGER \"road_geom_md\"");
        runSafe("DROP TABLE \"road\"");
        runSafe("DROP TABLE \"road_geom_idx\"");
    }

}
