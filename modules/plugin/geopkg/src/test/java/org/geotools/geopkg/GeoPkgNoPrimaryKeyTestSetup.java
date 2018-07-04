/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;
import org.locationtech.jts.geom.Polygon;

/** @source $URL$ */
public class GeoPkgNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected GeoPkgNoPrimaryKeyTestSetup() {
        super(new GeoPkgTestSetup());
    }

    @Override
    protected void createLakeTable() throws Exception {

        run(
                /*"CREATE TABLE lake (id INTEGER )");*/
                "CREATE TABLE lake (id INTEGER, geom BLOB)");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('lake', 'geom', 'POLYGON', 4326, 0, 0)";

        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('lake', 'features', 'lake', 4326)";
        run(sql);

        run("ALTER TABLE lake add name VARCHAR");
        GeometryBuilder gb = new GeometryBuilder();
        Polygon poly = gb.polygon(12, 6, 14, 8, 16, 6, 16, 4, 14, 4, 12, 6);
        // run( "INSERT INTO lake VALUES (0," +
        //    "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),'muddy')");
        sql =
                "INSERT INTO lake VALUES ("
                        + "1,X'"
                        + ((GeoPkgTestSetup) delegate).toString(poly)
                        + "', 'muddy');";
        run(sql);
    }

    @Override
    protected void dropLakeTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("lake");
    }
}
