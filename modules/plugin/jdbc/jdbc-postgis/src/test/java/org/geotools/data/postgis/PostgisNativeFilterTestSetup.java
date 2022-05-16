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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCNativeFilterTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public final class PostgisNativeFilterTestSetup extends JDBCNativeFilterTestSetup {

    public PostgisNativeFilterTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createMeasurementsTable() throws Exception {
        // create the necessary able
        run(
                "CREATE TABLE gt_jdbc_test_measurements ("
                        + "id INTEGER PRIMARY KEY, "
                        + "code TEXT NOT NULL, "
                        + "type TEXT NOT NULL, "
                        + "value REAL NOT NULL, "
                        + "location GEOMETRY(Point, 4326) NOT NULL);");
        // create the spatial index
        run(
                "CREATE INDEX gt_jdbc_test_measurements_geom_index ON gt_jdbc_test_measurements USING GIST (\"location\");");
        // insert the needed records
        run(
                "INSERT INTO gt_jdbc_test_measurements VALUES "
                        + "(1, '#1', 'temperature', 15.0, ST_PointFromText('POINT (1.0 2.0)', 4326)), "
                        + "(2, '#2', 'temperature', 18.5, ST_PointFromText('POINT (1.0 4.0)', 4326)), "
                        + "(3, '#3', 'wind', 8.5, ST_PointFromText('POINT (2.0 4.0)', 4326)), "
                        + "(4, '#4', 'wind', 4.5, ST_PointFromText('POINT (2.0 2.0)', 4326)), "
                        + "(5, '#5', 'humidity', 0.7, ST_PointFromText('POINT (1.0 4.0)', 4326)), "
                        + "(6, '#6', 'humidity', 0.5, ST_PointFromText('POINT (5.0 4.0)', 4326));");
    }

    @Override
    protected void dropMeasurementsTable() {
        // drop the spatial index
        runSafe("DROP INDEX gt_jdbc_test_measurements_geom_index;");
        // drop the table
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'gt_jdbc_test_measurements';");
        runSafe("DROP TABLE gt_jdbc_test_measurements;");
    }
}
