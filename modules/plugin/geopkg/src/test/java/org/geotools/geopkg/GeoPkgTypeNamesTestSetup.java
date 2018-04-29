/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCTypeNamesTestSetup;

public class GeoPkgTypeNamesTestSetup extends JDBCTypeNamesTestSetup {

    protected GeoPkgTypeNamesTestSetup() {
        super(new GeoPkgTestSetup());
    }

    @Override
    protected void createTypes() throws Exception {
        run("CREATE TABLE ftntable (" + "id INT, name VARCHAR, geom GEOMETRY)");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('ftntable', 'geom', 'POLYGON', 4326, 0, 0)";

        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('ftntable', 'features', 'ftntable', 4326)";
        run(sql);
        run("CREATE VIEW ftnview AS SELECT id, geom FROM ftntable");
        sql = "INSERT INTO gpkg_geometry_columns VALUES ('ftnview', 'geom', 'POLYGON', 4326, 0, 0)";

        run(sql);
        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('ftnview', 'features', 'ftnview', 4326)";
        run(sql);
    }

    @Override
    protected void dropTypes() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("ftnview");
        ((GeoPkgTestSetup) delegate).removeTable("ftntable");
    }
}
