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
import java.util.Properties;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaDisableSimplificationTestSetup extends JDBCDelegatingTestSetup {

    private static final String TABLE_NAME = "testtab";

    public HanaDisableSimplificationTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropTestTable();
        } catch (SQLException e) {
        }

        createTestTable();
    }

    private void createTestTable() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(3857)"}
            };
            htu.createRegisteredTestTable(TABLE_NAME, cols);

            htu.insertIntoTestTable(
                    TABLE_NAME,
                    htu.nextTestSequenceValueForColumn(TABLE_NAME, "fid"),
                    0,
                    htu.geometry("LINESTRING(0 0, 1 0, 2 0, 3 0)", 3857));
        }
    }

    private void dropTestTable() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn, fixture);
            htu.dropTestTableCascade(TABLE_NAME);
        }
    }

    @Override
    public void setFixture(Properties fixture) {
        fixture.setProperty("disable simplification", "true");
        super.setFixture(fixture);
    }
}
