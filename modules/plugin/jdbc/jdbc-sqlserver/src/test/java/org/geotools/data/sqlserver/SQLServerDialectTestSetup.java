/*
 * Copyright (C) 2024 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package org.geotools.data.sqlserver;

public class SQLServerDialectTestSetup extends SQLServerTestSetup {

    @Override
    protected void setUpData() throws Exception {
        runSafe("DROP TABLE multiGeom");

        String sql =
                "CREATE TABLE multiGeom (id int IDENTITY(0,1) PRIMARY KEY, geometry geometry, name varchar(255), polygon_geom geometry, geom_noindex geometry)";
        run(sql);

        // change column collation to support case-insensitive comparison
        sql = "ALTER TABLE multiGeom ALTER COLUMN name VARCHAR(255) COLLATE Latin1_General_CS_AS";
        run(sql);

        sql =
                "INSERT INTO multiGeom (geometry,name,polygon_geom) VALUES "
                        + "(geometry::STGeomFromText('POINT (132782 457575)',28992), 'Atoomweg 45', geometry::STGeomFromText('POLYGON ((132781 457574, 132783 457575, 132781 457578, 132779 457576, 132781 457574))',28992));";
        run(sql);

        sql =
                "INSERT INTO multiGeom (geometry,name,polygon_geom) VALUES "
                        + "(geometry::STGeomFromText('POINT (132975 457666)',28992), 'Atoomweg 48', geometry::STGeomFromText('POLYGON ((132973 457701, 132971 457700, 132972 457698, 132974 457699, 132973 457701))',28992));";
        run(sql);

        sql =
                "INSERT INTO multiGeom (geometry,name,polygon_geom) VALUES "
                        + "(geometry::STGeomFromText('POINT (132974 457620)',28992), 'Atoomweg 46', geometry::STGeomFromText('POLYGON ((132974 457622, 132972 457620, 132974 457618, 132976 457619, 132974 457622))',28992));";
        run(sql);

        // create the spatial index on the geometry and geom column; create a second spatial index
        // on the geom column
        run(
                "CREATE SPATIAL INDEX _multiGeom_geometry_index on multiGeom(geometry) WITH (BOUNDING_BOX = (132782, 457575, 132975, 457666))");
        run(
                "CREATE SPATIAL INDEX _multiGeom_geom_index on multiGeom(polygon_geom) WITH (BOUNDING_BOX = (132400, 457400, 132600, 457600))");
        run(
                "CREATE SPATIAL INDEX _multiGeom_geom_index2 on multiGeom(polygon_geom) WITH (BOUNDING_BOX = (132000, 457000, 133000, 458000))");
    }
}
