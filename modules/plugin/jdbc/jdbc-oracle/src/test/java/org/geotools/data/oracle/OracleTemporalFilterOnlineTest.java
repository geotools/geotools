package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterOnlineTest;

public class OracleTemporalFilterOnlineTest extends JDBCTemporalFilterOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new OracleDateTestSetup(new OracleTestSetup());
    }

}
