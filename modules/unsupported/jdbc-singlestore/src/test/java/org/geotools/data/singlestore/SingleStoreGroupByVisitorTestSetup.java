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

import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;

public class SingleStoreGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    protected SingleStoreGroupByVisitorTestSetup() {
        super(new SingleStoreTestSetup());
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        // last_update_date is an extra column used to test datedifference translation to SQL
        // against date fields, as opposed to timestamp fields
        run("CREATE TABLE buildings_group_by_tests (id int PRIMARY KEY, building_id varchar(255), "
                + "building_type varchar(255), energy_type varchar(255), fuel_consumption double, "
                + "energy_consumption double, last_update timestamp, last_update_date date)");
        run("INSERT INTO buildings_group_by_tests VALUES "
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
                + "(12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', NULL, 4.0, '2016-06-15 20:00:00', '2016-06-15');");
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
                        + "geometry GEOGRAPHYPOINT, " //
                        + "intProperty int," //
                        + "doubleProperty double, " //
                        + "stringProperty varchar(255))");
        run("INSERT INTO ft1_group_by VALUES(0, 'POINT(0 0)', 0, 0.0, 'aa')");
        run("INSERT INTO ft1_group_by VALUES(1, 'POINT(0 0)', 1, 1.0, 'ba')");
        run("INSERT INTO ft1_group_by VALUES(2, 'POINT(0 0)', 2, 2.0, 'ca')");
        run("INSERT INTO ft1_group_by VALUES(3, 'POINT(1 1)', 10, 10.0, 'ab')");
        run("INSERT INTO ft1_group_by VALUES(4, 'POINT(1 1)', 11, 11.0, 'bb')");
        run("INSERT INTO ft1_group_by VALUES(5, 'POINT(1 1)', 12, 12.0, 'cb')");
        run("INSERT INTO ft1_group_by VALUES(6, 'POINT(2 2)', 20, 20.0, 'ac')");
        run("INSERT INTO ft1_group_by VALUES(7, 'POINT(2 2)', 21, 21.0, 'bc')");
        run("INSERT INTO ft1_group_by VALUES(8, 'POINT(2 2)', 22, 22.0, 'cc')");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        runSafe("DROP TABLE ft1_group_by");
    }
}
