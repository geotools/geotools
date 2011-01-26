package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCSkipColumnTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class OracleSkipColumnsTest extends JDBCSkipColumnTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new OracleSkipColumnTestSetup();
    }

}
