package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCNoPrimaryKeyOnlineTest;
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class SQLServerNoPrimaryKeyOnlineTest extends JDBCNoPrimaryKeyOnlineTest {

    @Override
    protected JDBCNoPrimaryKeyTestSetup createTestSetup() {
        return new SQLServerNoPrimaryKeyTestSetup(new SQLServerTestSetup());
    }
}
