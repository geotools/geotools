/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class OracleGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    public OracleGroupByVisitorTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        run(
                "CREATE TABLE BUILDINGS_GROUP_BY_TESTS (id INT PRIMARY KEY, building_id varchar(20), "
                        + "building_type varchar(20), energy_type varchar(20), energy_consumption FLOAT, last_update timestamp)");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (1, 'SCHOOL_A', 'SCHOOL', 'FLOWING_WATER', 50.0, TO_DATE('2016-06-03 12:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (2, 'SCHOOL_A', 'SCHOOL', 'NUCLEAR', 10.0, TO_DATE('2016-06-03 16:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (3, 'SCHOOL_A', 'SCHOOL', 'WIND', 20.0, TO_DATE('2016-06-03 20:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (4, 'SCHOOL_B', 'SCHOOL', 'SOLAR', 30.0, TO_DATE('2016-06-05 12:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (5, 'SCHOOL_B', 'SCHOOL', 'FUEL', 60.0, TO_DATE('2016-06-06 12:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (6, 'SCHOOL_B', 'SCHOOL', 'NUCLEAR', 10.0, TO_DATE('2016-06-06 14:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (7, 'FABRIC_A', 'FABRIC', 'FLOWING_WATER', 500.0, TO_DATE('2016-06-07 12:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (8, 'FABRIC_A', 'FABRIC', 'NUCLEAR', 150.0, TO_DATE('2016-06-07 18:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (9, 'FABRIC_B', 'FABRIC', 'WIND', 20.0, TO_DATE('2016-06-07 20:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (10, 'FABRIC_B', 'FABRIC', 'SOLAR', 30.0, TO_DATE('2016-06-15 12:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (11, 'HOUSE_A', 'HOUSE', 'FUEL', 6.0, TO_DATE('2016-06-15 19:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', 4.0, TO_DATE('2016-06-15 20:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        runSafe("DROP TABLE BUILDINGS_GROUP_BY_TESTS");
    }
}
