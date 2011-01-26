package org.geotools.data.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.geotools.jdbc.JDBCLobTestSetup;

public class OracleLobTestSetup extends JDBCLobTestSetup {

    protected OracleLobTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    protected void createLobTable() throws Exception {
        run("CREATE TABLE testlob (fid int, blob_field BLOB, clob_field CLOB, raw_field RAW(50), PRIMARY KEY (fid) )");
        run("CREATE SEQUENCE testlob_fid_seq START WITH 0 MINVALUE 0");

        // insert data. We need to use prepared statements in order to insert blobs
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO testlob(fid, blob_field, clob_field, raw_field) VALUES(testlob_fid_seq.nextval, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setBytes(1, new byte[] {1,2,3,4,5});
            ps.setString(2, "small clob");
            ps.setBytes(3, new byte[] {6,7,8,9,10});
            ps.execute();
        } finally {
            if(ps != null) ps.close();
            conn.close();
        }
    }

    @Override
    protected void dropLobTable() throws Exception {
        runSafe("DROP SEQUENCE testlob_fid_seq");
        runSafe("DROP TABLE testlob");
    }

}
