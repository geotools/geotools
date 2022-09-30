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
package org.h2gis.geotools;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {
    public H2GISDataStoreAPITestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void createRoadTable() throws Exception {
        run(
                "CREATE TABLE \"road\"(\"fid\" int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, "
                        + "\"geom\" GEOMETRY(LINESTRING, 4326), \"name\" varchar )");
        run("Create Spatial Index spIdxRoad on \"road\"(\"geom\")");
        run(
                "INSERT INTO \"road\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('LINESTRING(1 1, 2 2, 4 2, 5 1)',4326),"
                        + "'r1')");
        run(
                "INSERT INTO \"road\" (\"id\",\"geom\",\"name\") VALUES ( 1,"
                        + "ST_GeomFromText('LINESTRING(3 0, 3 2, 3 3, 3 4)',4326),"
                        + "'r2')");
        run(
                "INSERT INTO \"road\" (\"id\",\"geom\",\"name\") VALUES ( 2,"
                        + "ST_GeomFromText('LINESTRING(3 2, 4 2, 5 3)',4326),"
                        + "'r3')");
    }

    @Override
    protected void createRiverTable() throws Exception {
        run(
                "CREATE TABLE \"river\"(\"fid\" int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, "
                        + "\"geom\" GEOMETRY(MULTILINESTRING, 4326), \"river\" varchar , \"flow\" double )");
        run("Create Spatial Index spIdxRiver on \"river\"(\"geom\")");
        run(
                "INSERT INTO \"river\" (\"id\",\"geom\",\"river\", \"flow\")  VALUES ( 0,"
                        + "ST_GeomFromText('MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))',4326),"
                        + "'rv1', 4.5)");
        run(
                "INSERT INTO \"river\" (\"id\",\"geom\",\"river\", \"flow\") VALUES ( 1,"
                        + "ST_GeomFromText('MULTILINESTRING((4 6, 4 8, 6 10))',4326),"
                        + "'rv2', 3.0)");
    }

    @Override
    protected void createLakeTable() throws Exception {
        run(
                "CREATE TABLE \"lake\"(\"fid\" int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, "
                        + "\"geom\" GEOMETRY(POLYGON, 4326), \"name\" varchar )");
        run("Create Spatial Index spIdxLake on \"lake\"(\"geom\")");
        run(
                "INSERT INTO \"lake\" (\"id\",\"geom\",\"name\") VALUES ( 0,"
                        + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                        + "'muddy')");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        runSafe("DROP TABLE \"road\"");
        runSafe("CALL DropGeometryColumn(NULL, 'road', 'geom')");
        // runSafe("DELETE FROM geometry_columns where f_table_name = 'road'");

        runSafe("DROP TABLE \"road_HATBOX\"");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        runSafe("DROP TABLE \"river\"");
        runSafe("CALL DropGeometryColumn(NULL, 'river', 'geom')");
        // runSafe("DELETE FROM geometry_columns where f_table_name = 'river'");
        runSafe("DROP TABLE \"river_HATBOX\"");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP TABLE \"lake\"");
        runSafe("CALL DropGeometryColumn(NULL, 'lake', 'geom')");
        // runSafe("DELETE FROM geometry_columns where f_table_name = 'lake'");
        runSafe("DROP TABLE \"lake_HATBOX\"");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        runSafe("DROP TABLE \"building\"");
        runSafe("CALL DropGeometryColumn(NULL, 'building', 'geom')");
        // runSafe("DELETE FROM geometry_columns where f_table_name = 'building'");
        runSafe("DROP TABLE \"building_HATBOX\"");
    }
}
