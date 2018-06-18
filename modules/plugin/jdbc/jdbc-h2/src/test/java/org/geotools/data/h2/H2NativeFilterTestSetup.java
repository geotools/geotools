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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCNativeFilterTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public final class H2NativeFilterTestSetup extends JDBCNativeFilterTestSetup {

    public H2NativeFilterTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createMeasurementsTable() throws Exception {
        // create the necessary able
        run(
                "CREATE TABLE \"geotools\".\"gt_jdbc_test_measurements\" ("
                        + "\"id\" INTEGER PRIMARY KEY, "
                        + "\"code\" TEXT NOT NULL, "
                        + "\"type\" TEXT NOT NULL, "
                        + "\"value\" REAL NOT NULL);");
        // add geometry columns
        run(
                "CALL AddGeometryColumn('geotools', 'gt_jdbc_test_measurements', 'location', 4326, 'POINT', 2);");
        // insert the needed records
        run(
                "INSERT INTO  \"geotools\".\"gt_jdbc_test_measurements\" VALUES "
                        + "(1, '#1', 'temperature', 15.0, ST_GeomFromText('POINT(1.0 2.0)', 4326));");
        run(
                "INSERT INTO  \"geotools\".\"gt_jdbc_test_measurements\" VALUES "
                        + "(2, '#2', 'temperature', 18.5, ST_GeomFromText('POINT (1.0 4.0)', 4326));");
        run(
                "INSERT INTO  \"geotools\".\"gt_jdbc_test_measurements\" VALUES "
                        + "(3, '#3', 'wind', 8.5, ST_GeomFromText('POINT (2.0 4.0)', 4326));");
        run(
                "INSERT INTO  \"geotools\".\"gt_jdbc_test_measurements\" VALUES "
                        + "(4, '#4', 'wind', 4.5, ST_GeomFromText('POINT (2.0 2.0)', 4326));");
        run(
                "INSERT INTO  \"geotools\".\"gt_jdbc_test_measurements\" VALUES "
                        + "(5, '#5', 'humidity', 0.7, ST_GeomFromText('POINT (1.0 4.0)', 4326));");
        run(
                "INSERT INTO  \"geotools\".\"gt_jdbc_test_measurements\" VALUES "
                        + "(6, '#6', 'humidity', 0.5, ST_GeomFromText('POINT (5.0 4.0)', 4326));");
        // create the spatial index
        run("CALL CreateSpatialIndex('geotools', 'gt_jdbc_test_measurements', 'location', 4326);");
    }

    @Override
    protected void dropMeasurementsTable() {
        // drop the table

        runSafe("DROP TABLE  \"geotools\".\"gt_jdbc_test_measurements\";");
        runSafe("DROP TABLE  \"geotools\".\"gt_jdbc_test_measurements\"_HATBOX;");
        runSafe(
                "DELETE FROM geometry_columns WHERE f_table_name = 'gt_jdbc_test_measurements'; COMMIT;");
    }
}
