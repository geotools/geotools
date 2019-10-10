/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.jdbc.JDBCTestSetup;

public class OracleLongNameTestSetup extends JDBCDelegatingTestSetup {

    protected OracleLongNameTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        super.setUpData();
        // kill all the data
        runSafe("DROP SEQUENCE THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME_pkey_seq");
        runSafe("DROP TABLE THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME");
        run(
                "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME'");

        // create the table with long names again
        run(
                "CREATE TABLE THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME("
                        + "ID INT, "
                        + "GEOMETRY_ATTRIBUTE_WITH_A_VERY_LONG_NAME MDSYS.SDO_GEOMETRY, "
                        + "INT_ATTRIBUTE_WITH_A_VERY_LONG_NAME INT,"
                        + "PRIMARY KEY(ID))");
        run("CREATE SEQUENCE THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME_pkey_seq");
        run(
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) "
                        + "VALUES ('THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME','GEOMETRY_ATTRIBUTE_WITH_A_VERY_LONG_NAME',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                        + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)");
        run(
                "INSERT INTO THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME VALUES (0,"
                        + pointSql(4326, 0, 0)
                        + ", 123)");
    }

    protected String pointSql(int srid, double x, double y) {
        return "MDSYS.SDO_GEOMETRY(2001,"
                + srid
                + ",SDO_POINT_TYPE("
                + x
                + ","
                + y
                + ",NULL),NULL,NULL)";
    }
}
