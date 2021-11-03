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
package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;

public class MySQLGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    protected MySQLGroupByVisitorTestSetup() {
        super(new MySQLTestSetup());
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        // last_update_date is an extra column used to test datedifference translation to SQL
        // against date fields, as opposed to timestamp fields
        run(
                "CREATE TABLE buildings_group_by_tests (id int PRIMARY KEY, building_id varchar(255), "
                        + "building_type varchar(255), energy_type varchar(255), "
                        + "energy_consumption double, last_update timestamp, last_update_date date)");
        run(
                "INSERT INTO buildings_group_by_tests VALUES "
                        + "(1, 'SCHOOL_A', 'SCHOOL', 'FLOWING_WATER', 50.0, '2016-06-03 12:00:00', '2016-06-03'),"
                        + "(2, 'SCHOOL_A', 'SCHOOL', 'NUCLEAR', 10.0, '2016-06-03 16:00:00', '2016-06-03'),"
                        + "(3, 'SCHOOL_A', 'SCHOOL', 'WIND', 20.0, '2016-06-03 20:00:00', '2016-06-03'),"
                        + "(4, 'SCHOOL_B', 'SCHOOL', 'SOLAR', 30.0, '2016-06-05 12:00:00', '2016-06-05'),"
                        + "(5, 'SCHOOL_B', 'SCHOOL', 'FUEL', 60.0, '2016-06-06 12:00:00', '2016-06-06'),"
                        + "(6, 'SCHOOL_B', 'SCHOOL', 'NUCLEAR', 10.0, '2016-06-06 14:00:00', '2016-06-06'),"
                        + "(7, 'FABRIC_A', 'FABRIC', 'FLOWING_WATER', 500.0, '2016-06-07 12:00:00', '2016-06-07'),"
                        + "(8, 'FABRIC_A', 'FABRIC', 'NUCLEAR', 150.0, '2016-06-07 18:00:00', '2016-06-07'),"
                        + "(9, 'FABRIC_B', 'FABRIC', 'WIND', 20.0, '2016-06-07 20:00:00', '2016-06-07'),"
                        + "(10, 'FABRIC_B', 'FABRIC', 'SOLAR', 30.0, '2016-06-15 12:00:00', '2016-06-15'),"
                        + "(11, 'HOUSE_A', 'HOUSE', 'FUEL', 6.0, '2016-06-15 19:00:00', '2016-06-15'),"
                        + "(12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', 4.0, '2016-06-15 20:00:00', '2016-06-15');");
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
                        + "geometry POINT, " //
                        + "intProperty int," //
                        + "doubleProperty double, " //
                        + "stringProperty varchar(255))");
        run(
                "INSERT INTO ft1_group_by VALUES(0, ST_GeometryFromText('POINT(0 0)', 4326), 0, 0.0, 'aa')");
        run(
                "INSERT INTO ft1_group_by VALUES(1, ST_GeometryFromText('POINT(0 0)', 4326), 1, 1.0, 'ba')");
        run(
                "INSERT INTO ft1_group_by VALUES(2, ST_GeometryFromText('POINT(0 0)', 4326), 2, 2.0, 'ca')");
        run(
                "INSERT INTO ft1_group_by VALUES(3, ST_GeometryFromText('POINT(1 1)', 4326), 10, 10.0, 'ab')");
        run(
                "INSERT INTO ft1_group_by VALUES(4, ST_GeometryFromText('POINT(1 1)', 4326), 11, 11.0, 'bb')");
        run(
                "INSERT INTO ft1_group_by VALUES(5, ST_GeometryFromText('POINT(1 1)', 4326), 12, 12.0, 'cb')");
        run(
                "INSERT INTO ft1_group_by VALUES(6, ST_GeometryFromText('POINT(2 2)', 4326), 20, 20.0, 'ac')");
        run(
                "INSERT INTO ft1_group_by VALUES(7, ST_GeometryFromText('POINT(2 2)', 4326), 21, 21.0, 'bc')");
        run(
                "INSERT INTO ft1_group_by VALUES(8, ST_GeometryFromText('POINT(2 2)', 4326), 22, 22.0, 'cc')");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        runSafe("DROP TABLE ft1_group_by");
    }
}
