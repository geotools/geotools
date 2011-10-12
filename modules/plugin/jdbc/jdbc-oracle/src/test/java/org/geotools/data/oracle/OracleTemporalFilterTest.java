package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterTest;

public class OracleTemporalFilterTest extends JDBCTemporalFilterTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new OracleDateTestSetup(new OracleTestSetup());
    }

}
