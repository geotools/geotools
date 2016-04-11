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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCGroupByVisitorTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    public PostgisGroupByVisitorTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        run("CREATE TABLE BUILDINGS_GROUP_BY_TESTS (id int4 PRIMARY KEY, building_id text, " +
                "building_type text, energy_type text, energy_consumption numeric);");
        run("INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES " +
                "(1, 'SCHOOL_A', 'SCHOOL', 'FLOWING_WATER', 50.0)," +
                "(2, 'SCHOOL_A', 'SCHOOL', 'NUCLEAR', 10.0)," +
                "(3, 'SCHOOL_A', 'SCHOOL', 'WIND', 20.0)," +
                "(4, 'SCHOOL_B', 'SCHOOL', 'SOLAR', 30.0)," +
                "(5, 'SCHOOL_B', 'SCHOOL', 'FUEL', 60.0)," +
                "(6, 'SCHOOL_B', 'SCHOOL', 'NUCLEAR', 10.0)," +
                "(7, 'FABRIC_A', 'FABRIC', 'FLOWING_WATER', 500.0)," +
                "(8, 'FABRIC_A', 'FABRIC', 'NUCLEAR', 150.0)," +
                "(9, 'FABRIC_B', 'FABRIC', 'WIND', 20.0)," +
                "(10, 'FABRIC_B', 'FABRIC', 'SOLAR', 30.0)," +
                "(11, 'HOUSE_A', 'HOUSE', 'FUEL', 6.0)," +
                "(12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', 4.0);");
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        runSafe("DROP TABLE BUILDINGS_GROUP_BY_TESTS");
    }
}
