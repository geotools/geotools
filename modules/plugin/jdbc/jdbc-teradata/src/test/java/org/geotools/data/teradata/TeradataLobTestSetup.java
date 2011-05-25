/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCLobTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TeradataLobTestSetup extends JDBCLobTestSetup {

    public TeradataLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }


    protected void createLobTable() throws Exception {

        Connection con = getDataSource().getConnection();
        con.prepareStatement("create table \"testlob\" (\"fid\" PRIMARY KEY not null generated always as identity (start with 0)  integer, \"blob_field\" binary large object, \"clob_field\" character large object, \"raw_field\" binary large object)").execute();

        PreparedStatement ps = con.prepareStatement("INSERT INTO \"testlob\" (\"blob_field\",\"clob_field\",\"raw_field\")  VALUES (?,?,?)");
        ps.setBytes(1, new byte[]{1, 2, 3, 4, 5});
        ps.setString(2, "small clob");
        ps.setBytes(3, new byte[]{6, 7, 8, 9, 10});
        ps.execute();
        ps.close();
        con.close();
    }


    protected void dropLobTable() throws Exception {
        runSafe("DROP TABLE \"testlob\"");
    }

}
