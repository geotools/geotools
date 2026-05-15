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
import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;

public class DuckDBGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    protected DuckDBGroupByVisitorTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return ((DuckDBTestSetup) delegate).getConnection();
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        run("CREATE TABLE \"buildings_group_by_tests\" ("
                + "\"id\" INTEGER PRIMARY KEY, "
                + "\"building_id\" VARCHAR, "
                + "\"building_type\" VARCHAR, "
                + "\"energy_type\" VARCHAR, "
                + "\"fuel_consumption\" DOUBLE, "
                + "\"energy_consumption\" DOUBLE, "
                + "\"last_update\" TIMESTAMP, "
                + "\"last_update_date\" DATE)");

        run("INSERT INTO \"buildings_group_by_tests\" VALUES "
                + "(1, 'SCHOOL_A', 'SCHOOL', 'FLOWING_WATER', NULL, 50.0, '2016-06-03 12:00:00', '2016-06-03'),"
                + "(2, 'SCHOOL_A', 'SCHOOL', 'NUCLEAR', NULL, 10.0, '2016-06-03 16:00:00', '2016-06-03'),"
                + "(3, 'SCHOOL_A', 'SCHOOL', 'WIND', NULL, 20.0, '2016-06-03 20:00:00', '2016-06-03'),"
                + "(4, 'SCHOOL_B', 'SCHOOL', 'SOLAR', NULL, 30.0, '2016-06-05 12:00:00', '2016-06-05'),"
                + "(5, 'SCHOOL_B', 'SCHOOL', 'FUEL', NULL, 60.0, '2016-06-06 12:00:00', '2016-06-06'),"
                + "(6, 'SCHOOL_B', 'SCHOOL', 'NUCLEAR', NULL, 10.0, '2016-06-06 14:00:00', '2016-06-06'),"
                + "(7, 'FABRIC_A', 'FABRIC', 'FLOWING_WATER', NULL, 500.0, '2016-06-07 12:00:00', '2016-06-07'),"
                + "(8, 'FABRIC_A', 'FABRIC', 'NUCLEAR', NULL, 150.0, '2016-06-07 18:00:00', '2016-06-07'),"
                + "(9, 'FABRIC_B', 'FABRIC', 'WIND', NULL, 20.0, '2016-06-07 20:00:00', '2016-06-07'),"
                + "(10, 'FABRIC_B', 'FABRIC', 'SOLAR', NULL, 30.0, '2016-06-15 12:00:00', '2016-06-15'),"
                + "(11, 'HOUSE_A', 'HOUSE', 'FUEL', NULL, 6.0, '2016-06-15 19:00:00', '2016-06-15'),"
                + "(12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', NULL, 4.0, '2016-06-15 20:00:00', '2016-06-15')");
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        ((DuckDBTestSetup) delegate).removeTable("buildings_group_by_tests");
    }

    @Override
    protected void createFt1GroupByTable() throws Exception {
        DuckDBTestSetup setup = (DuckDBTestSetup) delegate;
        run("CREATE TABLE \"ft1_group_by\" ("
                + "\"id\" INTEGER PRIMARY KEY, "
                + "\"geometry\" GEOMETRY, "
                + "\"intProperty\" INTEGER, "
                + "\"doubleProperty\" DOUBLE, "
                + "\"stringProperty\" VARCHAR)");
        run("INSERT INTO \"ft1_group_by\" VALUES "
                + "(0, " + setup.geometryLiteral("POINT(0 0)") + ", 0, 0.0, 'aa'),"
                + "(1, " + setup.geometryLiteral("POINT(0 0)") + ", 1, 1.0, 'ba'),"
                + "(2, " + setup.geometryLiteral("POINT(0 0)") + ", 2, 2.0, 'ca'),"
                + "(3, " + setup.geometryLiteral("POINT(1 1)") + ", 10, 10.0, 'ab'),"
                + "(4, " + setup.geometryLiteral("POINT(1 1)") + ", 11, 11.0, 'bb'),"
                + "(5, " + setup.geometryLiteral("POINT(1 1)") + ", 12, 12.0, 'cb'),"
                + "(6, " + setup.geometryLiteral("POINT(2 2)") + ", 20, 20.0, 'ac'),"
                + "(7, " + setup.geometryLiteral("POINT(2 2)") + ", 21, 21.0, 'bc'),"
                + "(8, " + setup.geometryLiteral("POINT(2 2)") + ", 22, 22.0, 'cc')");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        ((DuckDBTestSetup) delegate).removeTable("ft1_group_by");
    }
}
