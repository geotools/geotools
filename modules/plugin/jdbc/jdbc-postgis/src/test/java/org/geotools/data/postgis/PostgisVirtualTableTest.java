package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableTest;

public class PostgisVirtualTableTest extends JDBCVirtualTableTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISTestSetup());
    }

}
