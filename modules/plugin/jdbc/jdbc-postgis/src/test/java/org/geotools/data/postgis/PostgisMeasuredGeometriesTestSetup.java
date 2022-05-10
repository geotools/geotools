/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCMeasuredGeometriesTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** Setups data in PostGIS for the tests with measurements coordinates. */
public final class PostgisMeasuredGeometriesTestSetup extends JDBCMeasuredGeometriesTestSetup {

    public PostgisMeasuredGeometriesTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createTablePointsM() throws Exception {
        // create the table
        run(
                "CREATE TABLE points_m ("
                        + "  id INTEGER PRIMARY KEY,"
                        + "  description VARCHAR(250) NOT NULL,"
                        + "  geometry GEOMETRY(PointM, 4326) NOT NULL"
                        + ");");
        // insert the data
        run(
                "INSERT INTO points_m (id, description, geometry)"
                        + "VALUES (1, 'point_m_a', ST_SetSRID(ST_GeomFromEWKT('POINTM(-2 1 0.5)'), 4326)),"
                        + "       (2, 'point_m_b', ST_SetSRID(ST_GeomFromEWKT('POINTM( 3 1 -1)'), 4326)),"
                        + "       (3, 'point_m_c', ST_SetSRID(ST_GeomFromEWKT('POINTM( 3 5 2)'), 4326)),"
                        + "       (4, 'point_m_d', ST_SetSRID(ST_GeomFromEWKT('POINTM(-2 5 -3.5)'), 4326));");
    }

    @Override
    protected void dropTablePointsM() {
        runSafe("DROP TABLE points_m;");
    }

    @Override
    protected void createTablePointsZM() throws Exception {
        // create the table
        run(
                "CREATE TABLE points_zm ("
                        + "  id INTEGER PRIMARY KEY,"
                        + "  description VARCHAR(250) NOT NULL,"
                        + "  geometry GEOMETRY(PointZM, 4326) NOT NULL"
                        + ");");
        // insert the data
        run(
                "INSERT INTO points_zm (id, description, geometry) VALUES"
                        + "      (1, 'point_zm_a', ST_SetSRID(ST_GeomFromEWKT('POINTZM(-2 1 10 0.5)'), 4326)),"
                        + "      (2, 'point_zm_b', ST_SetSRID(ST_GeomFromEWKT('POINTZM( 3 1 15 -1)'), 4326)),"
                        + "      (3, 'point_zm_c', ST_SetSRID(ST_GeomFromEWKT('POINTZM( 3 5 20 2)'), 4326)),"
                        + "      (4, 'point_zm_d', ST_SetSRID(ST_GeomFromEWKT('POINTZM(-2 5 25 -3.5)'), 4326));");
    }

    @Override
    protected void dropTablePointsZM() {
        runSafe("DROP TABLE points_zm;");
    }

    @Override
    protected void createTableLinesM() throws Exception {
        // create the table
        run(
                "CREATE TABLE lines_m ("
                        + "  id INTEGER PRIMARY KEY,"
                        + "  description VARCHAR(250) NOT NULL,"
                        + "  geometry GEOMETRY(LineStringM, 4326) NOT NULL"
                        + ");");
        // insert the data
        run(
                "INSERT INTO lines_m (id, description, geometry) VALUES "
                        + "(1, 'line_m_a', "
                        + "   ST_SetSRID(ST_GeomFromEWKT('LINESTRINGM(-2 1 0.5, 3 1 -1, 3 5 2, -2 5 3.5)'), 4326));");
    }

    @Override
    protected void dropTableLinesM() {
        runSafe("DROP TABLE lines_m;");
    }

    @Override
    protected void createTableLinesZM() throws Exception {
        // create the table
        run(
                "CREATE TABLE lines_zm ("
                        + "  id INTEGER PRIMARY KEY,"
                        + "  description VARCHAR(250) NOT NULL,"
                        + "  geometry GEOMETRY(LineStringZM, 4326) NOT NULL"
                        + ");");
        // insert the data
        run(
                "INSERT INTO lines_zm (id, description, geometry) VALUES "
                        + "(1, 'line_zm_a', "
                        + "   ST_SetSRID(ST_GeomFromEWKT("
                        + "   'LINESTRINGZM(-2 1 10 0.5, 3 1 15 -1, 3 5 20 2, -2 5 25 3.5)'), 4326));");
    }

    @Override
    protected void dropTableLinesZM() {
        runSafe("DROP TABLE lines_zm;");
    }
}
