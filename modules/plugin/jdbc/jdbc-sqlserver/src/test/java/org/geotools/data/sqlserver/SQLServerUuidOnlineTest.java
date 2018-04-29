package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCUuidOnlineTest;
import org.geotools.jdbc.JDBCUuidTestSetup;

public class SQLServerUuidOnlineTest extends JDBCUuidOnlineTest {

    @Override
    protected JDBCUuidTestSetup createTestSetup() {
        return new SQLServerUuidTestSetup();
    }
}
