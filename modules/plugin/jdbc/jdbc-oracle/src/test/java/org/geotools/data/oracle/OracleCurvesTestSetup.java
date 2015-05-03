/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCDelegatingTestSetup;

public class OracleCurvesTestSetup extends JDBCDelegatingTestSetup {

    protected OracleCurvesTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void setUpData() throws Exception {
        dropCurveTable();
        createCurvesTable();
    }

    private void createCurvesTable() throws Exception {
        String sql = "CREATE TABLE CURVES ("
                + "id INT, name VARCHAR(64), geometry MDSYS.SDO_GEOMETRY, PRIMARY KEY(id))";
        run(sql);

        sql = "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) "
                + "VALUES ('curves','geometry',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 32632)";
        run(sql);

        sql = "CREATE INDEX CURVES_GEOMETRY_IDX ON CURVES(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                + " PARAMETERS ('SDO_INDX_DIMS=2')";
        run(sql);

        // adding data
        sql = "INSERT INTO CURVES VALUES (0, 'Arc segment', "
                + "sdo_geometry (2002, 32632, null, sdo_elem_info_array (1,2,2), sdo_ordinate_array (10,15, 15,20, 20,15)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (1, 'Arc string', "
                + "sdo_geometry (2002, 32632, null, sdo_elem_info_array (1,2,2), sdo_ordinate_array (10,35, 15,40, 20,35, 25,30, 30,35)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (2, 'Closed arc string', "
                + "sdo_geometry (2002, 32632, null, sdo_elem_info_array (1,2,2), sdo_ordinate_array (15,65, 10,68, 15,70, 20,68, 15,65)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (3, 'Compound line string', "
                + "sdo_geometry (2002, 32632, null, sdo_elem_info_array (1,4,3, 1,2,1, 3,2,2, 7,2,1), sdo_ordinate_array (10,45, 20,45, 23,48, 20,51, 10,51)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (4, 'Closed mixed line', "
                + "sdo_geometry (2002, 32632, null, sdo_elem_info_array (1,4,2, 1,2,1, 7,2,2), sdo_ordinate_array (10,78, 10,75, 20,75, 20,78, 15,80, 10,78)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (5, 'Circle', "
                + "sdo_geometry (2003, 32632, null, sdo_elem_info_array (1,1003,4), sdo_ordinate_array (15,145, 10,150, 20,150)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (6, 'Compound polygon', "
                + "sdo_geometry (2003, 32632, null, sdo_elem_info_array(1,1005,2, 1,2,1, 5,2,2), sdo_ordinate_array(6,10, 10,1, 14,10, 10,14, 6,10)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (7, 'Compound polygon with hole', sdo_geometry(2003, 32632, null,"
                + "  sdo_elem_info_array(1,1005,2, 1,2,1, 13,2,2, 19,2003,4),"
                + "  sdo_ordinate_array(20,30, 11,30, 7,22, 7,15, 11,10, 21,10, 27,30, 25,27, 20,30, 10,17, 15,22, 20,17)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (8, 'Multipolygon with curves', sdo_geometry(2007, 32632, null,"
                + " sdo_elem_info_array(1,1005,2, 1,2,1, 5,2,2,  11,2005,2, 11,2,1, 15,2,2, 21,1005,2, 21,2,1, 25,2,2),"
                + " sdo_ordinate_array(6,10, 10,1, 14,10, 10,14,  6,10, 13,10, 10,2,  7,10, 10,13, 13,10, 106,110, 110,101, 114,110, 110,114,106,110)))";
        run(sql);

        sql = "INSERT INTO CURVES VALUES (9, 'Compound polygon 2', "
                + "sdo_geometry (2003, 32632, null, sdo_elem_info_array(1,1003,2), sdo_ordinate_array(15, 145, 20, 150, 15, 155, 10, 150, 15, 145)))";
        run(sql);
    }

    private void dropCurveTable() throws Exception {
        runSafe("DROP TABLE CURVES PURGE");
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'CURVES'");
    }

}
