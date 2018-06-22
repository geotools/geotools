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
package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCNativeFilterTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public final class OracleNativeFilterTestSetup extends JDBCNativeFilterTestSetup {

    public OracleNativeFilterTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createMeasurementsTable() throws Exception {
        // create the necessary able
        run(
                "CREATE TABLE GT_JDBC_TEST_MEASUREMENTS ("
                        + "id INTEGER PRIMARY KEY, "
                        + "code VARCHAR2(50) NOT NULL, "
                        + "type VARCHAR2(100) NOT NULL, "
                        + "value REAL NOT NULL, "
                        + "location SDO_GEOMETRY NOT NULL)");
        // insert he needed spatial metadata
        run(
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)"
                        + "   VALUES ('GT_JDBC_TEST_MEASUREMENTS', 'LOCATION',"
                        + "   SDO_DIM_ARRAY ("
                        + "      SDO_DIM_ELEMENT('X', -180.0, 180.0, 0.5),"
                        + "      SDO_DIM_ELEMENT('Y', -90.0, 90.0, 0.5)),"
                        + "      4326)");
        // create the spatial index
        run(
                "CREATE INDEX GT_JDBC_TEST_MEASUREMENTS_GEOM_INDEX ON "
                        + "GT_JDBC_TEST_MEASUREMENTS(location) INDEXTYPE IS MDSYS.SPATIAL_INDEX");
        // insert the needed records
        run(
                "INSERT ALL"
                        + "  INTO GT_JDBC_TEST_MEASUREMENTS VALUES (1, '#1', 'temperature', 15.0, SDO_GEOMETRY('POINT (1.0 2.0)', 4326))"
                        + "  INTO GT_JDBC_TEST_MEASUREMENTS VALUES (2, '#2', 'temperature', 18.5, SDO_GEOMETRY('POINT (1.0 4.0)', 4326))"
                        + "  INTO GT_JDBC_TEST_MEASUREMENTS VALUES (3, '#3', 'wind', 8.5, SDO_GEOMETRY('POINT (2.0 4.0)', 4326))"
                        + "  INTO GT_JDBC_TEST_MEASUREMENTS VALUES (4, '#4', 'wind', 4.5, SDO_GEOMETRY('POINT (2.0 2.0)', 4326))"
                        + "  INTO GT_JDBC_TEST_MEASUREMENTS VALUES (5, '#5', 'humidity', 0.7, SDO_GEOMETRY('POINT (1.0 4.0)', 4326))"
                        + "  INTO GT_JDBC_TEST_MEASUREMENTS VALUES (6, '#6', 'humidity', 0.5, SDO_GEOMETRY('POINT (5.0 4.0)', 4326))"
                        + "SELECT * FROM dual");
    }

    @Override
    protected void dropMeasurementsTable() {
        // drop the spatial index
        runSafe("DROP INDEX GT_JDBC_TEST_MEASUREMENTS_GEOM_INDEX");
        // drop the spatial metadata
        runSafe(
                "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'GT_JDBC_TEST_MEASUREMENTS'");
        // drop the table
        runSafe("DROP TABLE GT_JDBC_TEST_MEASUREMENTS");
    }
}
