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

    private static final String TABLE = "buildings_group_by_tests";

    public HanaGroupByVisitorTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"id", "INT PRIMARY KEY"},
                {"building_id", "VARCHAR(255)"},
                {"building_type", "VARCHAR(255)"},
                {"energy_type", "VARCHAR(255)"},
                {"energy_consumption", "DECIMAL(5, 1)"},
                {"last_update", "TIMESTAMP"}
            };
            htu.createTestTable(TABLE, cols);

            htu.insertIntoTestTable(
                    TABLE, 1, "SCHOOL_A", "SCHOOL", "FLOWING_WATER", 50.0, "2016-06-03 12:00:00");
            htu.insertIntoTestTable(
                    TABLE, 2, "SCHOOL_A", "SCHOOL", "NUCLEAR", 10.0, "2016-06-03 16:00:00");
            htu.insertIntoTestTable(
                    TABLE, 3, "SCHOOL_A", "SCHOOL", "WIND", 20.0, "2016-06-03 20:00:00");
            htu.insertIntoTestTable(
                    TABLE, 4, "SCHOOL_B", "SCHOOL", "SOLAR", 30.0, "2016-06-05 12:00:00");
            htu.insertIntoTestTable(
                    TABLE, 5, "SCHOOL_B", "SCHOOL", "FUEL", 60.0, "2016-06-06 12:00:00");
            htu.insertIntoTestTable(
                    TABLE, 6, "SCHOOL_B", "SCHOOL", "NUCLEAR", 10.0, "2016-06-06 14:00:00");
            htu.insertIntoTestTable(
                    TABLE, 7, "FABRIC_A", "FABRIC", "FLOWING_WATER", 500.0, "2016-06-07 12:00:00");
            htu.insertIntoTestTable(
                    TABLE, 8, "FABRIC_A", "FABRIC", "NUCLEAR", 150.0, "2016-06-07 18:00:00");
            htu.insertIntoTestTable(
                    TABLE, 9, "FABRIC_B", "FABRIC", "WIND", 20.0, "2016-06-07 20:00:00");
            htu.insertIntoTestTable(
                    TABLE, 10, "FABRIC_B", "FABRIC", "SOLAR", 30.0, "2016-06-15 12:00:00");
            htu.insertIntoTestTable(
                    TABLE, 11, "HOUSE_A", "HOUSE", "FUEL", 6.0, "2016-06-15 19:00:00");
            htu.insertIntoTestTable(
                    TABLE, 12, "HOUSE_B", "HOUSE", "NUCLEAR", 4.0, "2016-06-15 20:00:00");
        }
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
