package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCAggregateFunctionOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleAggregateFunctionTest extends JDBCAggregateFunctionOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

}
