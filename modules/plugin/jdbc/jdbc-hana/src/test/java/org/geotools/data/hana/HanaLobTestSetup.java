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
import org.geotools.jdbc.JDBCLobTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaLobTestSetup extends JDBCLobTestSetup {

    private static final String TABLE = "testlob";

    public HanaLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLobTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"blob_field", "BLOB"},
                {"clob_field", "CLOB"},
                {"raw_field", "BLOB"}
            };
            htu.createRegisteredTestTable(TABLE, cols);

            htu.insertIntoTestTable(
                    TABLE,
                    1,
                    new byte[] {1, 2, 3, 4, 5},
                    "small clob",
                    new byte[] {6, 7, 8, 9, 10});
        }
    }

    @Override
    protected void dropLobTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
