package org.geotools.data.mysql.jndi;

import org.geotools.data.mysql.MySQLTestSetup;
import org.geotools.jdbc.JDBCDataStoreTest;
import org.geotools.jdbc.JDBCJNDITestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class MySQLDataStoreTest extends JDBCDataStoreTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new MySQLTestSetup());
    }

}
