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
package org.geotools.data.db2;

import static org.geotools.data.db2.DB2TestUtil.SCHEMA_QUOTED;
import static org.geotools.data.db2.DB2TestUtil.SRID;

import java.sql.Connection;
import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;

public class DB2GroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    protected DB2GroupByVisitorTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        // last_update_date is an extra column used to test datedifference translation to SQL
        // against date fields, as opposed to timestamp fields
        run(
                "CREATE TABLE "
                        + SCHEMA_QUOTED
                        + ".\"buildings_group_by_tests\" (\"id\" int PRIMARY KEY not null, \"building_id\" varchar(255), "
                        + "\"building_type\" varchar(255), \"energy_type\" varchar(255), "
                        + "\"energy_consumption\" double, \"last_update\" timestamp, \"last_update_date\" date)");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"buildings_group_by_tests\" VALUES "
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
        runSafe("DROP TABLE " + SCHEMA_QUOTED + ".\"buildings_group_by_tests\"");
    }

    @Override
    protected void createFt1GroupByTable() throws Exception {
        run(
                "CREATE TABLE "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\"(" //
                        + "\"id\" int primary key not null, " //
                        + "\"geometry\" db2gse.ST_POINT, " //
                        + "\"intProperty\" int," //
                        + "\"doubleProperty\" double, " //
                        + "\"stringProperty\" varchar(255))");

        try (Connection cx = getConnection()) {
            DB2Util.executeRegister(
                    DB2TestUtil.SCHEMA, "ft1_group_by", "geometry", DB2TestUtil.SRSNAME, cx);
        }

        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(0, db2gse.st_PointFromText('POINT(0 0)', "
                        + SRID
                        + "), 0, 0.0, 'aa')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(1, db2gse.st_PointFromText('POINT(0 0)', "
                        + SRID
                        + "), 1, 1.0, 'ba')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(2, db2gse.st_PointFromText('POINT(0 0)', "
                        + SRID
                        + "), 2, 2.0, 'ca')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(3, db2gse.st_PointFromText('POINT(1 1)', "
                        + SRID
                        + "), 10, 10.0, 'ab')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(4, db2gse.st_PointFromText('POINT(1 1)', "
                        + SRID
                        + "), 11, 11.0, 'bb')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(5, db2gse.st_PointFromText('POINT(1 1)', "
                        + SRID
                        + "), 12, 12.0, 'cb')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(6, db2gse.st_PointFromText('POINT(2 2)', "
                        + SRID
                        + "), 20, 20.0, 'ac')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(7, db2gse.st_PointFromText('POINT(2 2)', "
                        + SRID
                        + "), 21, 21.0, 'bc')");
        run(
                "INSERT INTO "
                        + SCHEMA_QUOTED
                        + ".\"ft1_group_by\" VALUES(8, db2gse.st_PointFromText('POINT(2 2)', "
                        + SRID
                        + "), 22, 22.0, 'cc')");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        runSafe("DROP TABLE " + SCHEMA_QUOTED + ".\"ft1_group_by\"");
    }
}
