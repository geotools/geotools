package org.geotools.jdbc;

import java.sql.SQLException;

public abstract class JDBCLobTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCLobTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropLobTable();
        } catch (SQLException e) {
        }

        //create all the data
        createLobTable();
    }

    /**
     * Creates a table with the following schema:
     * <p>
     * testlob( id:Integer; blob_field: blob; clob_field: clob)
     * </p>
     * <p>
     * The table should be populated with the following data
     *  0 | [0,1,2,3,4,5] | "small clob"
     * </p>
     * Where [0,1,2,3,4,5] is a byte[]
     */
    protected abstract void createLobTable() throws Exception;

    /**
     * Drops the "testlob" table
     */
    protected abstract void dropLobTable() throws Exception;

}
