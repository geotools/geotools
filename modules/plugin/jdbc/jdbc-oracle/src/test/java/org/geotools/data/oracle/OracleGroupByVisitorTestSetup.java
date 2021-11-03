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

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class OracleGroupByVisitorTestSetup extends JDBCGroupByVisitorTestSetup {

    public OracleGroupByVisitorTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createBuildingsTable() throws Exception {
        run(
                "CREATE TABLE BUILDINGS_GROUP_BY_TESTS (id INT PRIMARY KEY, building_id varchar(20), "
                        + "building_type varchar(20), energy_type varchar(20), energy_consumption FLOAT, last_update timestamp, last_update_date date)");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (1, 'SCHOOL_A', 'SCHOOL', 'FLOWING_WATER', 50.0, TO_DATE('2016-06-03 12:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-03', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (2, 'SCHOOL_A', 'SCHOOL', 'NUCLEAR', 10.0, TO_DATE('2016-06-03 16:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-03', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (3, 'SCHOOL_A', 'SCHOOL', 'WIND', 20.0, TO_DATE('2016-06-03 20:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-03', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (4, 'SCHOOL_B', 'SCHOOL', 'SOLAR', 30.0, TO_DATE('2016-06-05 12:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-05', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (5, 'SCHOOL_B', 'SCHOOL', 'FUEL', 60.0, TO_DATE('2016-06-06 12:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-06', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (6, 'SCHOOL_B', 'SCHOOL', 'NUCLEAR', 10.0, TO_DATE('2016-06-06 14:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-06', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (7, 'FABRIC_A', 'FABRIC', 'FLOWING_WATER', 500.0, TO_DATE('2016-06-07 12:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-07', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (8, 'FABRIC_A', 'FABRIC', 'NUCLEAR', 150.0, TO_DATE('2016-06-07 18:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-07', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (9, 'FABRIC_B', 'FABRIC', 'WIND', 20.0, TO_DATE('2016-06-07 20:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-07', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (10, 'FABRIC_B', 'FABRIC', 'SOLAR', 30.0, TO_DATE('2016-06-15 12:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-15', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (11, 'HOUSE_A', 'HOUSE', 'FUEL', 6.0, TO_DATE('2016-06-15 19:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-15', 'yyyy-mm-dd'))");
        run(
                "INSERT INTO BUILDINGS_GROUP_BY_TESTS VALUES (12, 'HOUSE_B', 'HOUSE', 'NUCLEAR', 4.0, TO_DATE('2016-06-15 20:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2016-06-15', 'yyyy-mm-dd'))");
    }

    @Override
    protected void dropBuildingsTable() throws Exception {
        runSafe("DROP TABLE BUILDINGS_GROUP_BY_TESTS");
    }

    @Override
    protected void createFt1GroupByTable() throws Exception {
        String sql =
                "CREATE TABLE FT1_GROUP_BY ("
                        + "id INT, geometry MDSYS.SDO_GEOMETRY, intProperty INT, "
                        + "doubleProperty FLOAT, stringProperty VARCHAR(255)"
                        + ", PRIMARY KEY(id))";
        run(sql);

        sql =
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) "
                        + "VALUES ('ft1_group_by','geometry',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                        + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);

        sql =
                "CREATE INDEX FT1_GROUP_BY_GEOMETRY_IDX ON FT1_GROUP_BY(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);

        run("INSERT INTO FT1_GROUP_BY VALUES (0," + pointSql(4326, 0, 0) + ", 0, 0.0,'aa')");
        run("INSERT INTO FT1_GROUP_BY VALUES (1," + pointSql(4326, 0, 0) + ", 1, 1.0,'ba')");
        run("INSERT INTO FT1_GROUP_BY VALUES (2," + pointSql(4326, 0, 0) + ", 2, 2.0,'ca')");
        run("INSERT INTO FT1_GROUP_BY VALUES (3," + pointSql(4326, 1, 1) + ", 10, 10.0,'ab')");
        run("INSERT INTO FT1_GROUP_BY VALUES (4," + pointSql(4326, 1, 1) + ", 11, 11.0,'bb')");
        run("INSERT INTO FT1_GROUP_BY VALUES (5," + pointSql(4326, 1, 1) + ", 12, 12.0,'cb')");
        run("INSERT INTO FT1_GROUP_BY VALUES (6," + pointSql(4326, 2, 2) + ", 20, 20.0,'ac')");
        run("INSERT INTO FT1_GROUP_BY VALUES (7," + pointSql(4326, 2, 2) + ", 21, 21.0,'bc')");
        run("INSERT INTO FT1_GROUP_BY VALUES (8," + pointSql(4326, 2, 2) + ", 22, 22.0,'cc')");
    }

    @Override
    protected void dropFt1GroupByTable() throws Exception {
        ((OracleTestSetup) delegate).deleteSpatialTable("FT1_GROUP_BY");
    }

    protected String pointSql(int srid, double x, double y) {
        return ((OracleTestSetup) delegate).pointSql(srid, x, y);
    }
}
