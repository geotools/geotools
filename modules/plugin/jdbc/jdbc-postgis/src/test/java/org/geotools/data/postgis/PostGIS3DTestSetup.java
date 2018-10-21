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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.geotools.jdbc.JDBC3DTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.util.Version;

public class PostGIS3DTestSetup extends JDBC3DTestSetup {

    static final Version V_2_0_0 = new Version("2.0.0");

    private Version version;

    public PostGIS3DTestSetup(JDBCTestSetup setup) {
        super(setup);
    }

    public Version getVersion() throws SQLException, IOException {
        if (version == null) {
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = getDataSource().getConnection();
                st = conn.createStatement();
                rs = st.executeQuery("select PostGIS_Lib_Version()");
                if (rs.next()) {
                    version = new Version(rs.getString(1));
                }
            } finally {
                conn.close();
                st.close();
                rs.close();
            }
        }

        return version;
    }

    @Override
    protected void createLine3DTable() throws Exception {
        Version version = getVersion();
        boolean atLeastV2 = version.compareTo(V_2_0_0) >= 0;
        String geometryType = atLeastV2 ? "geometry(LINESTRINGZ, 4326)" : "geometry";

        // setup table
        run(
                "CREATE TABLE \"line3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" "
                        + geometryType
                        + ", \"name\" varchar )");
        if (!atLeastV2) {
            run(
                    "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'line3d', 'geom', 3, '4326', 'LINESTRING')");
        }
        run("CREATE INDEX line3d_GEOM_IDX ON \"line3d\" USING GIST (\"geom\") ");

        // insert data
        run(
                "INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 4326),"
                        + "'l1')");
        run(
                "INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "ST_GeomFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 4326),"
                        + "'l2')");
    }

    @Override
    protected void createPoint3DTable() throws Exception {
        Version version = getVersion();
        boolean atLeastV2 = version.compareTo(V_2_0_0) >= 0;
        String geometryType = atLeastV2 ? "geometry(POINTZ, 4326)" : "geometry";

        // setup table
        run(
                "CREATE TABLE \"point3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" "
                        + geometryType
                        + ", \"name\" varchar )");
        if (atLeastV2) {
            run(
                    "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'point3d', 'geom', 3, '4326', 'POINT')");
        }
        run("CREATE INDEX POINT3D_GEOM_IDX ON \"point3d\" USING GIST (\"geom\") ");

        // insert data
        run(
                "INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('POINT(1 1 1)', 4326),"
                        + "'p1')");
        run(
                "INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "ST_GeomFromText('POINT(3 0 1)', 4326),"
                        + "'p2')");
    }

    @Override
    protected void dropLine3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'line3d'");
        run("DROP TABLE \"line3d\"");
    }

    @Override
    protected void dropPoly3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'poly3d'");
        run("DROP TABLE \"poly3d\"");
    }

    @Override
    protected void dropPoint3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'point3d'");
        run("DROP TABLE \"point3d\"");
    }
}
