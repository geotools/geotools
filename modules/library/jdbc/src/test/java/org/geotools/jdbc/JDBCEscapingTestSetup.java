package org.geotools.jdbc;

import java.sql.SQLException;

public abstract class JDBCEscapingTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCEscapingTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        try {
            dropEscapingTable();
        } catch (SQLException e) {
        }
    }

    /** Drops the "esca\"ping" table created in the test. */
    protected abstract void dropEscapingTable() throws Exception;
}
