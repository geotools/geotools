package org.geotools.data.db2;

import org.geotools.jdbc.JDBCViewOnlineTest;
import org.geotools.jdbc.JDBCViewTestSetup;

public class DB2ViewOnlineTest extends JDBCViewOnlineTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new DB2ViewTestSetup();
    }

    @Override
    protected void connect() throws Exception {
        super.connect();
        dataStore.setDatabaseSchema("geotools");
    }

    /**
     * Whether the pk field in a view is nillable or not (it is for most databases, but not for
     * Oracle for example).
     */
    protected boolean isPkNillable() {
        return false;
    }
}
