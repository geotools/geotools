package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCNoPrimaryKeyTest;
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class SQLServerNoPrimaryKeyTest extends JDBCNoPrimaryKeyTest {

    @Override
    protected JDBCNoPrimaryKeyTestSetup createTestSetup() {
        return new SQLServerNoPrimaryKeyTestSetup(new SQLServerTestSetup());
    }

}
