package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;

public class PostgisVirtualTableOnlineTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISTestSetup());
    }

}
