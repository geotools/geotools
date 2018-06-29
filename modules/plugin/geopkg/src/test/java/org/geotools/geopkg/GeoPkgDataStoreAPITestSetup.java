/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.sql.SQLException;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;

/** @source $URL$ */
public class GeoPkgDataStoreAPITestSetup extends JDBCDataStoreAPITestSetup {

    boolean addSpatialIndex;

    protected GeoPkgDataStoreAPITestSetup(boolean addSpatialIndex) {
        super(new GeoPkgTestSetup());
        this.addSpatialIndex = addSpatialIndex;
    }

    @Override
    protected void setUpData() throws Exception {
        // kill all the data
        super.setUpData();
        try {
            dropRecreatedTable();
        } catch (SQLException e) {
        }
        try {
            dropDataTypesTable();
        } catch (SQLException e) {
        }
    }

    @Override
    protected int getInitialPrimaryKeyValue() {
        return 0;
    }

    @Override
    protected void createLakeTable() throws Exception {

        run("CREATE TABLE lake (fid INTEGER PRIMARY KEY, id INTEGER)");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('lake', 'geom', 'POLYGON', 4326, 0, 0)";
        run(sql);
        run("ALTER TABLE lake add geom BLOB");
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('lake', 'features', 'lake', 4326)";
        run(sql);
        run("ALTER TABLE lake add name VARCHAR");
        Polygon poly = gb.polygon(12, 6, 14, 8, 16, 6, 16, 4, 14, 4, 12, 6);

        sql =
                "INSERT INTO lake VALUES (0, 0,"
                        + "X'"
                        + ((GeoPkgTestSetup) delegate).toString(poly)
                        + "', 'muddy');";
        run(sql);

        if (addSpatialIndex) {
            ((GeoPkgTestSetup) delegate).createSpatialIndex("lake", "geom", "fid");
        }
    }

    @Override
    protected void dropLakeTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("lake");
        ((GeoPkgTestSetup) delegate).removeTable("rtree_lake_geom");
    }

    @Override
    protected void createRiverTable() throws Exception {
        run("CREATE TABLE river (fid INTEGER PRIMARY KEY, id INTEGER)");

        run("ALTER TABLE river add geom BLOB");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('river', 'geom', 'MULTILINESTRING', 4326, 0, 0)";
        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('river', 'features', 'river', 4326)";
        run(sql);

        run("ALTER TABLE river add river VARCHAR");
        run("ALTER TABLE river add flow FLOAT");
        MultiLineString line =
                gb.multiLineString(
                        gb.lineString(5, 5, 7, 4),
                        gb.lineString(7, 5, 9, 7, 13, 7),
                        gb.lineString(7, 5, 9, 3, 11, 3));

        sql =
                "INSERT INTO river VALUES (0, 0,"
                        + "X'"
                        + ((GeoPkgTestSetup) delegate).toString(line)
                        + "', 'rv1', 4.5);";
        run(sql);
        line = gb.multiLineString(gb.lineString(4, 6, 4, 8, 6, 10));

        sql =
                "INSERT INTO river VALUES (1, 1,"
                        + "X'"
                        + ((GeoPkgTestSetup) delegate).toString(line)
                        + "', 'rv2', 3.0);";
        run(sql);

        if (addSpatialIndex) {
            ((GeoPkgTestSetup) delegate).createSpatialIndex("river", "geom", "fid");
        }
    }

    @Override
    protected void dropRiverTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("river");
        ((GeoPkgTestSetup) delegate).removeTable("rtree_river_geom");
    }

    GeometryBuilder gb = new GeometryBuilder();

    @Override
    protected void createRoadTable() throws Exception {
        run("CREATE TABLE road (fid INTEGER PRIMARY KEY, id INTEGER)");

        run("ALTER TABLE road add geom BLOB");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('road', 'geom', 'LINESTRING', 4326, 0, 0)";
        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('road', 'features', 'road', 4326)";
        run(sql);
        run("ALTER TABLE road add name VARCHAR");

        LineString line = gb.lineString(1, 1, 2, 2, 4, 2, 5, 1);

        sql =
                "INSERT INTO road VALUES (0, 0,"
                        + "X'"
                        + ((GeoPkgTestSetup) delegate).toString(line)
                        + "', 'r1');";
        run(sql);

        line = gb.lineString(3, 0, 3, 2, 3, 3, 3, 4);
        sql =
                "INSERT INTO road VALUES (1, 1,"
                        + "X'"
                        + ((GeoPkgTestSetup) delegate).toString(line)
                        + "', 'r2');";
        run(sql);
        line = gb.lineString(3, 2, 4, 2, 5, 3);
        sql =
                "INSERT INTO road VALUES (2, 2,"
                        + "X'"
                        + ((GeoPkgTestSetup) delegate).toString(line)
                        + "', 'r3');";
        run(sql);

        if (addSpatialIndex) {
            ((GeoPkgTestSetup) delegate).createSpatialIndex("road", "geom", "fid");
        }
    }

    @Override
    protected void dropRoadTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("road");
        ((GeoPkgTestSetup) delegate).removeTable("rtree_road_geom");
    }

    @Override
    protected void dropBuildingTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("building");
    }

    protected void dropRecreatedTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("recreated");
    }

    protected void dropDataTypesTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("datatypes");
    }
}
