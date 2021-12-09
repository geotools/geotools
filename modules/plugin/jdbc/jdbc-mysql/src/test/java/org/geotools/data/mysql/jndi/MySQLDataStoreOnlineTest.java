package org.geotools.data.mysql.jndi;

import org.geotools.data.mysql.MySQLTestSetup;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCJNDITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class MySQLDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new MySQLTestSetup());
    }

    @Override
    protected String getCLOBTypeName() {
        // CLOB is supported in MySQL 8 but not in 5
        return "TEXT";
    }
}
