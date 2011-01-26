package org.geotools.data.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.geotools.jdbc.JDBCLobTestSetup;

public class DB2LobTestSetup extends JDBCLobTestSetup {

    protected DB2LobTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createLobTable() throws Exception {
        
        Connection con = getDataSource().getConnection();
        con.prepareStatement("create table "+DB2TestUtil.SCHEMA_QUOTED+
                        ".\"testlob\" (\"fid\" int not null , \"blob_field\" BLOB(32) , \"clob_field\" CLOB(32), \"raw_field\" BLOB(32), PRIMARY KEY(\"fid\"))").execute();
        
        PreparedStatement ps =con.prepareStatement( "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"testlob\" (\"fid\",\"blob_field\",\"clob_field\",\"raw_field\")  VALUES (?,?,?,?)");
        ps.setInt(1,0);
        ps.setBytes(2, new byte[] {1,2,3,4,5});
        ps.setString(3, "small clob");
        ps.setBytes(4, new byte[] {6,7,8,9,10});
        ps.execute();
        ps.close();
        con.close();               
    }

    @Override
    protected void dropLobTable() throws Exception {
        Connection con = getDataSource().getConnection();
        try {
                DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "testlob", con);
        } catch (SQLException e) {              
        }
        
        con.close();
    }

}
