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
 *
 */
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisSimplifiedGeometryTestSetup extends JDBCGeometryTestSetup {

    public PostgisSimplifiedGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        // create tables for simplification tests
        tearDown();

        // points
        run("CREATE TABLE simplify_point(id serial, geom geometry(POINT, 4326))");
        run("INSERT INTO simplify_point(geom) VALUES(ST_GeomFromText('POINT(-120.0 40.0)', 4326))");

        // lines
        run("CREATE TABLE simplify_line(id serial, geom geometry(LINESTRING, 4326))");
        run(
                "INSERT INTO simplify_line(geom)  VALUES(ST_GeomFromText('LINESTRING(-120.0 40.0, -130.0 50.0, -140 60)', 4326))");

        // polygons
        run("CREATE TABLE simplify_polygon(id serial, geom geometry(POLYGON, 4326))");
        run(
                "INSERT INTO simplify_polygon(geom) values(ST_GeomFromText('POLYGON((-120.0 40.0,"
                        + "-130.0 40.0, -130.0 50.0, -130.0 40.0, -120.0 40.0))', 4326))");

        // generic collection, already identified as potentially non straight, won't use TWKB
        run("CREATE TABLE simplify_collection(id serial, geom geometry(GEOMETRYCOLLECTION, 4326))");
        run(
                "INSERT INTO simplify_collection(geom) VALUES(ST_GeomFromText('GEOMETRYCOLLECTION("
                        + "POINT(-120.0 40.0),LINESTRING(-120.0 40.0,-130.0 50.0,-140.0 50.0))',4326))");

        // an actual curve
        run("CREATE TABLE simplify_curve (id serial, geom geometry(CIRCULARSTRING, 4326))");
        run(
                "INSERT INTO simplify_curve(geom) VALUES (ST_geometryFromText('CIRCULARSTRING(10 15, 15 20, 20 15)', 4326))");
    }

    @Override
    public void tearDown() throws Exception {
        dropSpatialTable("simplify_point");
        dropSpatialTable("simplify_line");
        dropSpatialTable("simplify_polygon");
        dropSpatialTable("simplify_collection");
        dropSpatialTable("simplify_curve");
    }

    @Override
    protected void dropSpatialTable(String tableName) {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '" + tableName + "'");
        runSafe("DROP TABLE \"" + tableName + "\"");
    }
}
