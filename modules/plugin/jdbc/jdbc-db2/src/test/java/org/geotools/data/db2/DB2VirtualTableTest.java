package org.geotools.data.db2;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableTest;

public class DB2VirtualTableTest extends JDBCVirtualTableTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new DB2DataStoreAPITestSetup();
    }
   
    @Override
    protected void connect() throws Exception {
        dbSchemaName=DB2TestUtil.SCHEMA;
        super.connect();
    }
}