package org.geotools.data.db2;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;

public class DB2ViewTest extends JDBCViewTest {

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
     * Whether the pk field in a view is nillable or not (it is for most databases, but not
     * for Oracle for example).
     * @return
     */
    protected boolean isPkNillable() {
        return false;
    }

}
