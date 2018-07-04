package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCAggregateFunctionOnlineTest;
import org.geotools.jdbc.JDBCAggregateTestSetup;

/** @source $URL$ */
public class OracleAggregateFunctionOnlineTest extends JDBCAggregateFunctionOnlineTest {

    @Override
    protected JDBCAggregateTestSetup createTestSetup() {
        return new OracleAggregateTestSetup();
    }
}
