/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisGeometryTestSetup extends JDBCGeometryTestSetup {

    public PostgisGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();

        // create tables for dimension test
        run(
                "CREATE TABLE dim_point AS SELECT ST_GeomFromText('POINT(-120.0 40.0)',"
                        + "4326) as geom;");
        run(
                "CREATE TABLE dim_line AS SELECT ST_GeomFromText('LINESTRING(-120.0 40.0,"
                        + "-130.0 50.0)', 4326) as geom;");
        run(
                "CREATE TABLE dim_polygon AS SELECT ST_GeomFromText('POLYGON((-120.0 40.0,"
                        + "-130.0 40.0, -130.0 50.0, -130.0 40.0, -120.0 40.0))', 4326) as geom;");
        run(
                "CREATE TABLE dim_collection AS SELECT ST_GeomFromText('GEOMETRYCOLLECTION("
                        + "POINT(-120.0 40.0),LINESTRING(-120.0 40.0,-130.0 50.0))',4326) as geom");
    }

    @Override
    public void tearDown() throws Exception {
        dropSpatialTable("dim_point");
        dropSpatialTable("dim_line");
        dropSpatialTable("dim_polygon");
        dropSpatialTable("dim_collection");
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '" + tableName + "'");
        runSafe("DROP TABLE \"" + tableName + "\"");
    }
}
