/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaSimplificationTestSetup extends JDBCDelegatingTestSetup {

    private static final String TABLE_ROUND_EARTH = "roundEarth";

    private static final String TABLE_PLANAR = "planar";

    public HanaSimplificationTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropRoundEarthTable();
        } catch (SQLException e) {
        }

        try {
            dropPlanarTable();
        } catch (SQLException e) {
        }

        createRoundEarthTable();
        createPlanarTable();
    }

    private void createRoundEarthTable() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(4326)"}
            };
            htu.createRegisteredTestTable(TABLE_ROUND_EARTH, cols);

            htu.insertIntoTestTable(
                    TABLE_ROUND_EARTH,
                    htu.nextTestSequenceValueForColumn(TABLE_ROUND_EARTH, "fid"),
                    0,
                    htu.geometry("LINESTRING(0 0, 1 0, 2 0, 3 0)", 4326));
        }
    }

    private void createPlanarTable() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(3857)"}
            };
            htu.createRegisteredTestTable(TABLE_PLANAR, cols);

            htu.insertIntoTestTable(
                    TABLE_PLANAR,
                    htu.nextTestSequenceValueForColumn(TABLE_PLANAR, "fid"),
                    0,
                    htu.geometry("LINESTRING(0 0, 1 0, 2 0, 3 0)", 3857));
        }
    }

    private void dropRoundEarthTable() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.dropTestTableCascade(TABLE_ROUND_EARTH);
        }
    }

    private void dropPlanarTable() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.dropTestTableCascade(TABLE_PLANAR);
        }
    }
}
