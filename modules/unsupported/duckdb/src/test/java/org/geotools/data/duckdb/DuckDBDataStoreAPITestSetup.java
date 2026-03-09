/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

/** JDBC DataStore API online test setup for DuckDB. */
public class DuckDBDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    public DuckDBDataStoreAPITestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();
        dropRecreatedTable();
        dropDataTypesTable();
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected int getInitialPrimaryKeyValue() {
        return 0;
    }

    @Override
    protected void createRoadTable() throws Exception {
        run("CREATE TABLE \"road\" (\"fid\" INTEGER PRIMARY KEY, \"id\" INTEGER, \"geom\" GEOMETRY, \"name\" VARCHAR)");
        run("INSERT INTO \"road\" VALUES (0, 0, "
                + ((DuckDBTestSetup) delegate).geometryLiteral("LINESTRING(1 1, 2 2, 4 2, 5 1)")
                + ", 'r1')");
        run("INSERT INTO \"road\" VALUES (1, 1, "
                + ((DuckDBTestSetup) delegate).geometryLiteral("LINESTRING(3 0, 3 2, 3 3, 3 4)")
                + ", 'r2')");
        run("INSERT INTO \"road\" VALUES (2, 2, "
                + ((DuckDBTestSetup) delegate).geometryLiteral("LINESTRING(3 2, 4 2, 5 3)")
                + ", 'r3')");
    }

    @Override
    protected void createRiverTable() throws Exception {
        run("CREATE TABLE \"river\" ("
                + "\"fid\" INTEGER PRIMARY KEY, "
                + "\"id\" INTEGER, "
                + "\"geom\" GEOMETRY, "
                + "\"river\" VARCHAR, "
                + "\"flow\" DOUBLE)");
        run("INSERT INTO \"river\" VALUES (0, 0, "
                + ((DuckDBTestSetup) delegate)
                        .geometryLiteral("MULTILINESTRING((5 5, 7 4),(7 5, 9 7, 13 7),(7 5, 9 3, 11 3))")
                + ", 'rv1', 4.5)");
        run("INSERT INTO \"river\" VALUES (1, 1, "
                + ((DuckDBTestSetup) delegate).geometryLiteral("MULTILINESTRING((4 6, 4 8, 6 10))")
                + ", 'rv2', 3.0)");
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\" (\"fid\" INTEGER PRIMARY KEY, \"id\" INTEGER, \"geom\" GEOMETRY, \"name\" VARCHAR)");
        run("INSERT INTO \"lake\" VALUES (0, 0, "
                + ((DuckDBTestSetup) delegate).geometryLiteral("POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))")
                + ", 'muddy')");
        run("COMMENT ON COLUMN \"lake\".\"name\" IS 'This is a text column'");
    }

    @Override
    protected void dropRoadTable() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"road\"");
        runSafe("DROP TABLE IF EXISTS \"road\"");
    }

    @Override
    protected void dropRiverTable() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"river\"");
        runSafe("DROP TABLE IF EXISTS \"river\"");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"lake\"");
        runSafe("DROP TABLE IF EXISTS \"lake\"");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        runSafe("DROP VIEW IF EXISTS \"building\"");
        runSafe("DROP TABLE IF EXISTS \"building\"");
    }

    protected void dropRecreatedTable() {
        runSafe("DROP VIEW IF EXISTS \"recreated\"");
        runSafe("DROP TABLE IF EXISTS \"recreated\"");
    }

    protected void dropDataTypesTable() {
        runSafe("DROP VIEW IF EXISTS \"datatypes\"");
        runSafe("DROP TABLE IF EXISTS \"datatypes\"");
    }
}
