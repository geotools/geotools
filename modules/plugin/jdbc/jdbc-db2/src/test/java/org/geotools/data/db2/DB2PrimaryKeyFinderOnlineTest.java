package org.geotools.data.db2;

import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class DB2PrimaryKeyFinderOnlineTest extends JDBCPrimaryKeyFinderOnlineTest {

    @Override
    protected void connect() throws Exception {
        super.connect();
        dataStore.setDatabaseSchema(DB2TestUtil.SCHEMA);
    }

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new DB2PrimaryKeyFinderTestSetup();
    }
}
