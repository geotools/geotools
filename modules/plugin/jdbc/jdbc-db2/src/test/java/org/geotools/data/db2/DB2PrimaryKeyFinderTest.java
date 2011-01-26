package org.geotools.data.db2;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class DB2PrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

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
