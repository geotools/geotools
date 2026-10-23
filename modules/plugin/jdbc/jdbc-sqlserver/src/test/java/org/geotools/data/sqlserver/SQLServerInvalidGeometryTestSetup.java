/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

public class SQLServerInvalidGeometryTestSetup extends SQLServerGeometryTestSetup {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        runSafe("DROP TABLE invalid_geometry");

        String sql =
                "CREATE TABLE invalid_geometry (id int IDENTITY(0,1) PRIMARY KEY, label varchar(255), geometry geometry)";
        run(sql);

        sql =
                "INSERT INTO GEOMETRY_COLUMNS (F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, TYPE) "
                        + "VALUES ('dbo', 'invalid_geometry','geometry', 2, 4326, 'POLYGON')";
        run(sql);

        // insert a self-intersecting polygon
        sql =
                "INSERT INTO invalid_geometry (label, geometry) VALUES ('self-intersecting polygon', geometry::STGeomFromText('POLYGON ((0 0, 10 0, 10 10, 5 -5, 0 10, 0 0))', 4326))";
        run(sql);
    }
}
