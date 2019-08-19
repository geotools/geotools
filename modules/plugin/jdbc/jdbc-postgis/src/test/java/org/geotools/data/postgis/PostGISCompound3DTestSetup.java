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
import org.geotools.jdbc.JDBCCompound3DTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.util.Version;

public class PostGISCompound3DTestSetup extends JDBCCompound3DTestSetup {

    static final Version V_2_0_0 = new Version("2.0.0");

    private Version version;

    public PostGISCompound3DTestSetup(JDBCTestSetup setup) {
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
    protected void createLineCompound3DTable() throws Exception {
        Version version = getVersion();
        boolean atLeastV2 = version.compareTo(V_2_0_0) >= 0;
        String geometryType = atLeastV2 ? "geometry(LINESTRINGZ, 7415)" : "geometry";

        // setup table
        run(
                "CREATE TABLE \"lineCompound3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" "
                        + geometryType
                        + ", \"name\" varchar )");
        if (!atLeastV2) {
            run(
                    "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'lineCompound3d', 'geom', 3, '7415', 'LINESTRING')");
        }
        run("CREATE INDEX lineCompound3d_GEOM_IDX ON \"lineCompound3d\" USING GIST (\"geom\") ");

        // insert data
        run(
                "INSERT INTO \"lineCompound3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 7415),"
                        + "'l1')");
        run(
                "INSERT INTO \"lineCompound3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "ST_GeomFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 7415),"
                        + "'l2')");
    }

    @Override
    protected void createPointCompound3DTable() throws Exception {
        Version version = getVersion();
        boolean atLeastV2 = version.compareTo(V_2_0_0) >= 0;
        String geometryType = atLeastV2 ? "geometry(POINTZ, 7415)" : "geometry";

        // setup table
        run(
                "CREATE TABLE \"pointCompound3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" "
                        + geometryType
                        + ", \"name\" varchar )");
        if (atLeastV2) {
            run(
                    "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'pointCompound3d', 'geom', 3, '7415', 'POINT')");
        }
        run("CREATE INDEX POINTCompound3d_GEOM_IDX ON \"pointCompound3d\" USING GIST (\"geom\") ");

        // insert data
        run(
                "INSERT INTO \"pointCompound3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('POINT(1 1 1)', 7415),"
                        + "'p1')");
        run(
                "INSERT INTO \"pointCompound3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "ST_GeomFromText('POINT(3 0 1)', 7415),"
                        + "'p2')");
    }

    @Override
    protected void dropLineCompound3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lineCompound3d'");
        run("DROP TABLE \"lineCompound3d\"");
    }

    @Override
    protected void dropPolyCompound3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'polyCompound3d'");
        run("DROP TABLE \"polyCompound3d\"");
    }

    @Override
    protected void dropPointCompound3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'pointCompound3d'");
        run("DROP TABLE \"pointCompound3d\"");
    }
}
