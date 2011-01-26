package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCAggregateFunctionTest;
import org.geotools.jdbc.JDBCTestSetup;

public class OracleAggregateFunctionTest extends JDBCAggregateFunctionTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

}
