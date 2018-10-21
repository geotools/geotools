package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;

public class OracleVirtualTableOnlineTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new OracleDataStoreAPITestSetup(new OracleTestSetup());
    }
}
