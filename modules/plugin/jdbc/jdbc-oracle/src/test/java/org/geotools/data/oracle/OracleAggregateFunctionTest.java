package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCAggregateFunctionTest;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleAggregateFunctionTest extends JDBCAggregateFunctionTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

}
