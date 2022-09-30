package org.h2gis.geotools;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;

public class H2GISVirtualTableTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new H2GISDataStoreAPITestSetup();
    }

    @Override
    public void testGuessGeometry() throws Exception {
        // skip it, H2 does not have enough metadata on a query column to determine
        // it is a geometry
    }
}
