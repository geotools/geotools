package org.geotools.data.h2;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;

/** @source $URL$ */
public class H2VirtualTableTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new H2DataStoreAPITestSetup();
    }

    public void testGuessGeometry() throws Exception {
        // skip it, H2 does not have enough metadata on a query column to determine
        // it is a geometry
    }
}
