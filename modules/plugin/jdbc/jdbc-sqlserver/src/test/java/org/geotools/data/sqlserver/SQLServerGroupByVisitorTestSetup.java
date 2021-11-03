/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;

public class SQLServerGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    protected SQLServerGroupByVisitorTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        // last_update_date is an extra column used to test datedifference translation to SQL
        // against date fields, as opposed to timestamp fields
        run(
                "CREATE TABLE buildings_group_by_tests (id int PRIMARY KEY, building_id varchar(255), "
                        + "building_type varchar(255), energy_type varchar(255), "
                        + "energy_consumption float, last_update datetime, last_update_date date)");
        run(
                "INSERT INTO buildings_group_by_tests VALUES "
                        + "(1, 'SCHOOL_A', 'SCHOOL', 'FLOWING_WATER', 50.0, CAST('2016-06-03 12:00:00' AS DATETIME), CAST('2016-06-03' AS DATE)),"
                        + "(2, 'SCHOOL_A', 'SCHOOL', 'NUCLEAR', 10.0, CAST('2016-06-03 16:00:00' AS DATETIME), CAST('2016-06-03' AS DATE)),"
                        + "(3, 'SCHOOL_A', 'SCHOOL', 'WIND', 20.0, CAST('2016-06-03 20:00:00' AS DATETIME), CAST('2016-06-03' AS DATE)),"
                        + "(4, 'SCHOOL_B', 'SCHOOL', 'SOLAR', 30.0, CAST('2016-06-05 12:00:00' AS DATETIME), CAST('2016-06-05' AS DATE)),"
                        + "(5, 'SCHOOL_B', 'SCHOOL', 'FUEL', 60.0, CAST('2016-06-06 12:00:00' AS DATETIME), CAST('2016-06-06' AS DATE)),"
                        + "(6, 'SCHOOL_B', 'SCHOOL', 'NUCLEAR', 10.0, CAST('2016-06-06 14:00:00' AS DATETIME), CAST('2016-06-06' AS DATE)),"
                        + "(7, 'FABRIC_A', 'FABRIC', 'FLOWING_WATER', 500.0, CAST('2016-06-07 12:00:00' AS DATETIME), CAST('2016-06-07' AS DATE)),"
                        + "(8, 'FABRIC_A', 'FABRIC', 'NUCLEAR', 150.0, CAST('2016-06-07 18:00:00' AS DATETIME), CAST('2016-06-07' AS DATE)),"
                        + "(9, 'FABRIC_B', 'FABRIC', 'WIND', 20.0, CAST('2016-06-07 20:00:00' AS DATETIME), CAST('2016-06-07' AS DATE)),"
                        + "(10, 'FABRIC_B', 'FABRIC', 'SOLAR', 30.0, CAST('2016-06-15 12:00:00' AS DATETIME), CAST('2016-06-15' AS DATE)),"
                        + "(11, 'HOUSE_A', 'HOUSE', 'FUEL', 6.0, CAST('2016-06-15 19:00:00' AS DATETIME), CAST('2016-06-15' AS DATE)),"
                        + "(12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', 4.0, CAST('2016-06-15 20:00:00' AS DATETIME), CAST('2016-06-15' AS DATE));");
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        runSafe("DROP TABLE buildings_group_by_tests");
    }

    @Override
    protected void createFt1GroupByTable() throws Exception {
        run(
                "CREATE TABLE ft1_group_by(" //
                        + "id int primary key, " //
                        + "geometry geometry, " //
                        + "intProperty int," //
                        + "doubleProperty float, " //
                        + "stringProperty varchar(255))");
        run(
                "INSERT INTO ft1_group_by VALUES(0, geometry::STGeomFromText('POINT(0 0)', 4326), 0, 0.0, 'aa')");
        run(
                "INSERT INTO ft1_group_by VALUES(1, geometry::STGeomFromText('POINT(0 0)', 4326), 1, 1.0, 'ba')");
        run(
                "INSERT INTO ft1_group_by VALUES(2, geometry::STGeomFromText('POINT(0 0)', 4326), 2, 2.0, 'ca')");
        run(
                "INSERT INTO ft1_group_by VALUES(3, geometry::STGeomFromText('POINT(1 1)', 4326), 10, 10.0, 'ab')");
        run(
                "INSERT INTO ft1_group_by VALUES(4, geometry::STGeomFromText('POINT(1 1)', 4326), 11, 11.0, 'bb')");
        run(
                "INSERT INTO ft1_group_by VALUES(5, geometry::STGeomFromText('POINT(1 1)', 4326), 12, 12.0, 'cb')");
        run(
                "INSERT INTO ft1_group_by VALUES(6, geometry::STGeomFromText('POINT(2 2)', 4326), 20, 20.0, 'ac')");
        run(
                "INSERT INTO ft1_group_by VALUES(7, geometry::STGeomFromText('POINT(2 2)', 4326), 21, 21.0, 'bc')");
        run(
                "INSERT INTO ft1_group_by VALUES(8, geometry::STGeomFromText('POINT(2 2)', 4326), 22, 22.0, 'cc')");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        runSafe("DROP TABLE ft1_group_by");
    }
}
