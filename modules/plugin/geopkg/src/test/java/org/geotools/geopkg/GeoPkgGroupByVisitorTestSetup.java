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
package org.geotools.geopkg;

import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;

public class GeoPkgGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    protected GeoPkgGroupByVisitorTestSetup() {
        super(new GeoPkgTestSetup());
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        run(
                "CREATE TABLE buildings_group_by_tests (id int4 PRIMARY KEY, building_id text, "
                        + "building_type text, energy_type text, energy_consumption numeric, "
                        + "last_update timestamp, last_update_date date)");

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
                        + "(12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', 4.0, '2016-06-15 20:00:00', '2016-06-15')");

        run(
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) "
                        + "VALUES ('buildings_group_by_tests', 'features', 'buildings_group_by_tests', 4326)");
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("buildings_group_by_tests");
    }

    @Override
    protected void createFt1GroupByTable() throws Exception {
        run(
                "CREATE TABLE ft1_group_by(id INTEGER PRIMARY KEY, geometry BLOB, intProperty int, doubleProperty double, stringProperty varchar);");

        GeometryBuilder gb = new GeometryBuilder();
        String p0 = ((GeoPkgTestSetup) delegate).toString(gb.point(0, 0));
        String p1 = ((GeoPkgTestSetup) delegate).toString(gb.point(1, 1));
        String p2 = ((GeoPkgTestSetup) delegate).toString(gb.point(2, 2));
        run("INSERT INTO ft1_group_by VALUES(0, X'" + p0 + "', 0, 0.0, 'aa')");
        run("INSERT INTO ft1_group_by VALUES(1, X'" + p0 + "', 1, 1.0, 'ba')");
        run("INSERT INTO ft1_group_by VALUES(2, X'" + p0 + "', 2, 2.0, 'ca')");
        run("INSERT INTO ft1_group_by VALUES(3, X'" + p1 + "', 10, 10.0, 'ab')");
        run("INSERT INTO ft1_group_by VALUES(4, X'" + p1 + "', 11, 11.0, 'bb')");
        run("INSERT INTO ft1_group_by VALUES(5, X'" + p1 + "', 12, 12.0, 'cb')");
        run("INSERT INTO ft1_group_by VALUES(6, X'" + p2 + "', 20, 20.0, 'ac')");
        run("INSERT INTO ft1_group_by VALUES(7, X'" + p2 + "', 21, 21.0, 'bc')");
        run("INSERT INTO ft1_group_by VALUES(8, X'" + p2 + "', 22, 22.0, 'cc')");

        run(
                "INSERT INTO gpkg_geometry_columns VALUES ('ft1_group_by', 'geometry', 'POINT', 4326, 0, 0)");
        run(
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) "
                        + "VALUES ('ft1_group_by', 'features', 'ft1_group_by', 4326)");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        // drop old data (calling the removeTable from delegate fails, no idea why!!)
        runSafe("DROP TABLE IF EXISTS " + "ft1_group_by");
        runSafe("DELETE FROM gpkg_geometry_columns where table_name ='" + "ft1_group_by" + "'");
        runSafe("DELETE FROM gpkg_contents where table_name ='" + "ft1_group_by" + "'");
    }
}
