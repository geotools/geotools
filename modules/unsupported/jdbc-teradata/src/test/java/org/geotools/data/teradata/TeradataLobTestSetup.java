package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCLobTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TeradataLobTestSetup extends JDBCLobTestSetup {

    public TeradataLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLobTable() throws Exception {

        Connection con = getDataSource().getConnection();
        con.prepareStatement("create table \"testlob\" (\"fid\" PRIMARY KEY not null generated always as identity (start with 0)  integer, \"blob_field\" BYTEA, \"clob_field\" TEXT, \"raw_field\" BYTEA)").execute();

        PreparedStatement ps = con.prepareStatement("INSERT INTO \"testlob\" (\"blob_field\",\"clob_field\",\"raw_field\")  VALUES (?,?,?)");
        ps.setBytes(1, new byte[]{1, 2, 3, 4, 5});
        ps.setString(2, "small clob");
        ps.setBytes(3, new byte[]{6, 7, 8, 9, 10});
        ps.execute();
        ps.close();
        con.close();
    }

    @Override
    protected void dropLobTable() throws Exception {
        runSafe("DROP TABLE \"testlob\"");
    }

}
