/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.geotools.jdbc.JDBCLobTestSetup;

public class DuckDBLobTestSetup extends JDBCLobTestSetup {

    protected DuckDBLobTestSetup() {
        super(new DuckDBTestSetup());
    }

    @Override
    protected void createLobTable() throws Exception {
        run("CREATE SEQUENCE testlob_fid_seq");
        run("CREATE TABLE \"testlob\" ("
                + "\"fid\" BIGINT PRIMARY KEY DEFAULT nextval('testlob_fid_seq'), "
                + "\"blob_field\" BLOB, "
                + "\"clob_field\" VARCHAR, "
                + "\"raw_field\" BLOB)");

        try (Connection cx = getDataSource().getConnection();
                PreparedStatement ps = cx.prepareStatement(
                        "INSERT INTO \"testlob\" (\"blob_field\", \"clob_field\", \"raw_field\") VALUES (?, ?, ?)")) {
            ps.setBytes(1, new byte[] {1, 2, 3, 4, 5});
            ps.setString(2, "small clob");
            ps.setBytes(3, new byte[] {6, 7, 8, 9, 10});
            ps.executeUpdate();
        }
    }

    @Override
    protected void dropLobTable() throws Exception {
        runSafe("DROP TABLE IF EXISTS \"testlob\"");
        runSafe("DROP TABLE IF EXISTS \"testLobCreate\"");
        runSafe("DROP SEQUENCE IF EXISTS testlob_fid_seq");
    }
}
