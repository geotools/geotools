/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.geotools.jdbc.JDBCLobTestSetup;

public class GeoPkgLobTestSetup extends JDBCLobTestSetup {

    protected GeoPkgLobTestSetup() {
        super(new GeoPkgTestSetup());
    }

    @Override
    protected void createLobTable() throws Exception {

        // run processes newlines as separate statements, so keep it readable but strip newlines
        run(
                """
            CREATE TABLE "testlob" (
                "fid" INTEGER PRIMARY KEY AUTOINCREMENT,
                "blob_field" BLOB,
                "clob_field" TEXT,
                "raw_field" BLOB
            )
            """
                        .replace("\n", " ")
                        .trim());

        try (Connection cx = getDataSource().getConnection();
                PreparedStatement ps = cx.prepareStatement(
                        "INSERT INTO \"testlob\" (\"blob_field\"," + "\"clob_field\",\"raw_field\") VALUES (?,?,?)")) {

            ps.setBytes(1, new byte[] {1, 2, 3, 4, 5});
            ps.setString(2, "small clob");
            ps.setBytes(3, new byte[] {6, 7, 8, 9, 10});
            ps.execute();
        }

        // register the table in gpkg_contents
        run("INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) "
                + "VALUES ('testlob', 'features', 'testlob', 4326)");
    }

    @Override
    protected void dropLobTable() throws Exception {
        ((GeoPkgTestSetup) delegate).removeTable("testlob");
        ((GeoPkgTestSetup) delegate).removeTable("testLobCreate");
    }
}
