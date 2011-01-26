package org.geotools.data.ingres;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.geotools.jdbc.JDBCLobTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresLobTestSetup extends JDBCLobTestSetup {

    public IngresLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createLobTable() throws Exception {
        
        Connection con = getDataSource().getConnection();
        con.prepareStatement("create table \"testlob\" (\"fid\" int primary key, " +
        		"\"blob_field\" LONG BYTE, \"clob_field\" LONG VARCHAR)").execute();
        
        PreparedStatement ps =con.prepareStatement( "INSERT INTO \"testlob\" (\"fid\",\"blob_field\",\"clob_field\")  VALUES (?,?,?)");
        ps.setInt(1, 1);
        ps.setBytes(2, new byte[] {1,2,3,4,5});
        ps.setString(3, "small clob");
        ps.execute();
        ps.close();
        con.close();               
    }

    @Override
    protected void dropLobTable() throws Exception {
        runSafe("DROP TABLE \"testlob\"");
    }

}
