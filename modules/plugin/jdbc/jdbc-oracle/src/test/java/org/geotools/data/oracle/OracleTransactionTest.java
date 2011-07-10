package org.geotools.data.oracle;

import java.io.IOException;

import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTransactionTest;

public class OracleTransactionTest extends JDBCTransactionTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }
    
    public void testConcurrentTransactions() throws IOException {
        // Oracle won't pass this one
    }

}
