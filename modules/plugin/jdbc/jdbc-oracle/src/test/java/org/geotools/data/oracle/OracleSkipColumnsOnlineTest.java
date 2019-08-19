package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCSkipColumnOnlineTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class OracleSkipColumnsOnlineTest extends JDBCSkipColumnOnlineTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new OracleSkipColumnTestSetup();
    }
}
