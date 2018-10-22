package org.geotools.data.db2;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;

public class DB2VirtualTableOnlineTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new DB2DataStoreAPITestSetup();
    }

    @Override
    protected void connect() throws Exception {
        dbSchemaName = DB2TestUtil.SCHEMA;
        super.connect();
    }
}
