/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.jdbc.JDBCViewTestSetup;
import org.locationtech.jts.geom.Polygon;

public class GeoPkgViewTestSetup extends JDBCViewTestSetup {

    public GeoPkgViewTestSetup() {
        super(new GeoPkgTestSetup());
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE lakes(fid int primary key, id int, geom BLOB)");

        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('lakes', 'geom', 'POLYGON', 4326, 0, 0)";

        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('lakes', 'features', 'lakes', 4326)";
        run(sql);

        run("ALTER TABLE lakes add name VARCHAR");
        GeometryBuilder gb = new GeometryBuilder();
        Polygon poly = gb.polygon(12, 6, 14, 8, 16, 6, 16, 4, 14, 4, 12, 6);
        // run( "INSERT INTO lake VALUES (0," +
        // "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),'muddy')");
        sql =
                "INSERT INTO lakes VALUES ("
                        + "0,0,X'"
                        + ((GeoPkgTestSetup) delegate).toString(poly)
                        + "', 'muddy');";
        run(sql);
    }

    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW IF NOT EXISTS lakesview as select * from lakes");
        // run("INSERT INTO views_geometry_columns VALUES ('lakesview', 'geom', 'id', 'lakes',
        // 'geom')");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('lakesview', 'geom', 'POLYGON', 4326, 0, 0)";

        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('lakesview', 'features', 'lakesview', 4326)";
        run(sql);
    }

    @Override
    protected void dropLakesTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("lakes");
    }

    @Override
    protected void dropLakesView() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("lakesview");
    }

    @Override
    protected void createLakesViewPk() throws Exception {}

    @Override
    protected void dropLakesViewPk() throws Exception {}
}
