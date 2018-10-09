/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.sql.Connection;
import org.geotools.jdbc.JDBC3DTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class Hana3DTestSetup extends JDBC3DTestSetup {

    private static final String POINT3D_TABLE = "point3d";

    private static final String LINE3D_TABLE = "line3d";

    private static final String POLY3D_TABLE = "poly3d";

    public Hana3DTestSetup(JDBCTestSetup setup) {
        super(setup);
    }

    @Override
    protected void createPoint3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(1000004326)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(POINT3D_TABLE, cols);

            htu.insertIntoTestTable(
                    POINT3D_TABLE,
                    htu.nextTestSequenceValueForColumn(POINT3D_TABLE, "fid"),
                    0,
                    htu.geometry("POINT Z(1 1 1)", 1000004326),
                    "p1");
            htu.insertIntoTestTable(
                    POINT3D_TABLE,
                    htu.nextTestSequenceValueForColumn(POINT3D_TABLE, "fid"),
                    1,
                    htu.geometry("POINT Z(3 0 1)", 1000004326),
                    "p2");
        }
    }

    @Override
    protected void dropPoint3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(POINT3D_TABLE);
        }
    }

    @Override
    protected void createLine3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(1000004326)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(LINE3D_TABLE, cols);

            htu.insertIntoTestTable(
                    LINE3D_TABLE,
                    htu.nextTestSequenceValueForColumn(LINE3D_TABLE, "fid"),
                    0,
                    htu.geometry("LINESTRING Z(1 1 0, 2 2 0, 4 2 1, 5 1 1)", 1000004326),
                    "l1");
            htu.insertIntoTestTable(
                    LINE3D_TABLE,
                    htu.nextTestSequenceValueForColumn(LINE3D_TABLE, "fid"),
                    1,
                    htu.geometry("LINESTRING Z(3 0 1, 3 2 2, 3 3 3, 3 4 5)", 1000004326),
                    "l2");
        }
    }

    @Override
    protected void dropLine3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(LINE3D_TABLE);
        }
    }

    @Override
    protected void dropPoly3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(POLY3D_TABLE);
        }
    }
}
