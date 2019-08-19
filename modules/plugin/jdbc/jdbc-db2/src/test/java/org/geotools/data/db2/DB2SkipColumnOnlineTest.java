package org.geotools.data.db2;

import org.geotools.jdbc.JDBCSkipColumnOnlineTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class DB2SkipColumnOnlineTest extends JDBCSkipColumnOnlineTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new DB2SkipColumnTestSetup();
    }
}
