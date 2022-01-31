/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class GeoPkgDataStoreFactoryTestSetup extends JDBCDelegatingTestSetup {

    public GeoPkgDataStoreFactoryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        dropTables();
        createTables();
    }

    private void createTables() throws Exception {
        run("CREATE TABLE b ( id INTEGER PRIMARY KEY, boolProperty BOOLEAN)");
        run("INSERT INTO b (boolProperty) VALUES (0)");
        run("INSERT INTO b (boolProperty) VALUES (1)");
        String sql =
                "INSERT INTO gpkg_geometry_columns VALUES ('b', 'geometry', 'POINT', 4326, 0, 0)";
        run(sql);

        run("CREATE TABLE a ( id INTEGER PRIMARY KEY, boolProperty BOOLEAN)");
        run("INSERT INTO a (boolProperty) VALUES (0)");
        run("INSERT INTO a (boolProperty) VALUES (1)");
        sql = "INSERT INTO gpkg_geometry_columns VALUES ('a', 'geometry', 'POINT', 4326, 0, 0)";
        run(sql);

        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('b', 'features', 'b', 4326)";
        run(sql);
    }

    private void dropTables() {
        runSafe("DROP TABLE b");
        runSafe("DROP TABLE a");
        runSafe("DELETE FROM gpkg_geometry_columns where table_name = 'b'");
        runSafe("DELETE FROM gpkg_geometry_columns where table_name = 'a'");
        runSafe("DELETE FROM gpkg_contents where table_name = 'b'");
    }
}
