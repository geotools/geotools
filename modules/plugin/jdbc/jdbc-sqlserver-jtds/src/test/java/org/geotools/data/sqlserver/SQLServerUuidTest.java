package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCUuidTest;
import org.geotools.jdbc.JDBCUuidTestSetup;

public class SQLServerUuidTest extends JDBCUuidTest {

    @Override
    protected JDBCUuidTestSetup createTestSetup() {
        return new SQLServerUuidTestSetup();
    }

}
