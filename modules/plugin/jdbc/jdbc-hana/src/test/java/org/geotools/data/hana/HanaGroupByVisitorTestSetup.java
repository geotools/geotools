/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.sql.Connection;
import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    private static final String BUILDINGS_TABLE = "buildings_group_by_tests";

    private static final String FT1_TABLE = "ft1_group_by";

    public HanaGroupByVisitorTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT PRIMARY KEY"},
                {"building_id", "VARCHAR(255)"},
                {"building_type", "VARCHAR(255)"},
                {"energy_type", "VARCHAR(255)"},
                {"energy_consumption", "DECIMAL(5, 1)"},
                {"last_update", "TIMESTAMP"},
                {"last_update_date", "DATE"}
            };
            htu.createTestTable(BUILDINGS_TABLE, cols);

            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    1,
                    "SCHOOL_A",
                    "SCHOOL",
                    "FLOWING_WATER",
                    50.0,
                    "2016-06-03 12:00:00",
                    "2016-06-03");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    2,
                    "SCHOOL_A",
                    "SCHOOL",
                    "NUCLEAR",
                    10.0,
                    "2016-06-03 16:00:00",
                    "2016-06-03");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    3,
                    "SCHOOL_A",
                    "SCHOOL",
                    "WIND",
                    20.0,
                    "2016-06-03 20:00:00",
                    "2016-06-03");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    4,
                    "SCHOOL_B",
                    "SCHOOL",
                    "SOLAR",
                    30.0,
                    "2016-06-05 12:00:00",
                    "2016-06-05");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    5,
                    "SCHOOL_B",
                    "SCHOOL",
                    "FUEL",
                    60.0,
                    "2016-06-06 12:00:00",
                    "2016-06-06");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    6,
                    "SCHOOL_B",
                    "SCHOOL",
                    "NUCLEAR",
                    10.0,
                    "2016-06-06 14:00:00",
                    "2016-06-06");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    7,
                    "FABRIC_A",
                    "FABRIC",
                    "FLOWING_WATER",
                    500.0,
                    "2016-06-07 12:00:00",
                    "2016-06-07");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    8,
                    "FABRIC_A",
                    "FABRIC",
                    "NUCLEAR",
                    150.0,
                    "2016-06-07 18:00:00",
                    "2016-06-07");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    9,
                    "FABRIC_B",
                    "FABRIC",
                    "WIND",
                    20.0,
                    "2016-06-07 20:00:00",
                    "2016-06-07");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    10,
                    "FABRIC_B",
                    "FABRIC",
                    "SOLAR",
                    30.0,
                    "2016-06-15 12:00:00",
                    "2016-06-15");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    11,
                    "HOUSE_A",
                    "HOUSE",
                    "FUEL",
                    6.0,
                    "2016-06-15 19:00:00",
                    "2016-06-15");
            htu.insertIntoTestTable(
                    BUILDINGS_TABLE,
                    12,
                    "HOUSE_B",
                    "HOUSE",
                    "NUCLEAR",
                    4.0,
                    "2016-06-15 20:00:00",
                    "2016-06-15");
        }
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.dropTestTableCascade(BUILDINGS_TABLE);
        }
    }

    @Override
    protected void createFt1GroupByTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT PRIMARY KEY"},
                {"geometry", "ST_Geometry(1000004326)"},
                {"intProperty", "INT"},
                {"doubleProperty", "DOUBLE"},
                {"stringProperty", "VARCHAR(255)"}
            };
            htu.createTestTable(FT1_TABLE, cols);

            htu.insertIntoTestTable(
                    FT1_TABLE, 0, htu.geometry("POINT(0 0)", 1000004326), 0, 0.0, "aa");
            htu.insertIntoTestTable(
                    FT1_TABLE, 1, htu.geometry("POINT(0 0)", 1000004326), 1, 1.0, "ba");
            htu.insertIntoTestTable(
                    FT1_TABLE, 2, htu.geometry("POINT(0 0)", 1000004326), 2, 2.0, "ca");
            htu.insertIntoTestTable(
                    FT1_TABLE, 3, htu.geometry("POINT(1 1)", 1000004326), 10, 10.0, "ab");
            htu.insertIntoTestTable(
                    FT1_TABLE, 4, htu.geometry("POINT(1 1)", 1000004326), 11, 11.0, "bb");
            htu.insertIntoTestTable(
                    FT1_TABLE, 5, htu.geometry("POINT(1 1)", 1000004326), 12, 12.0, "cb");
            htu.insertIntoTestTable(
                    FT1_TABLE, 6, htu.geometry("POINT(2 2)", 1000004326), 20, 20.0, "ac");
            htu.insertIntoTestTable(
                    FT1_TABLE, 7, htu.geometry("POINT(2 2)", 1000004326), 21, 21.0, "bc");
            htu.insertIntoTestTable(
                    FT1_TABLE, 8, htu.geometry("POINT(2 2)", 1000004326), 22, 22.0, "cc");
        }
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.dropTestTableCascade(FT1_TABLE);
        }
    }
}
