package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCNoPrimaryKeyTest;
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class MySQLNoPrimaryKeyTest extends JDBCNoPrimaryKeyTest {

    @Override
    protected JDBCNoPrimaryKeyTestSetup createTestSetup() {
        return new MySQLNoPrimaryKeyTestSetup();
    }

    
}
