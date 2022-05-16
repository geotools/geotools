package org.geotools.data.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.geotools.jdbc.JDBCLobTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerLobTestSetup extends JDBCLobTestSetup {

    protected SQLServerLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLobTable() throws Exception {

        try (Connection con = getDataSource().getConnection()) {
            try (PreparedStatement ps =
                    con.prepareStatement(
                            "create table \"testlob\" (\"fid\" int identity(0, 1) primary key, "
                                    + "\"blob_field\" VARBINARY(50), \"clob_field\" TEXT, \"raw_field\" VARBINARY(50))")) {
                ps.execute();
            }

            try (PreparedStatement ps =
                    con.prepareStatement(
                            "INSERT INTO \"testlob\" (\"blob_field\",\"clob_field\",\"raw_field\")  VALUES (?,?,?)")) {
                ps.setBytes(1, new byte[] {1, 2, 3, 4, 5});
                ps.setString(2, "small clob");
                ps.setBytes(3, new byte[] {6, 7, 8, 9, 10});
                ps.execute();
            }
        }
    }

    @Override
    protected void dropLobTable() throws Exception {
        runSafe("DROP TABLE \"testlob\"");
    }
}
