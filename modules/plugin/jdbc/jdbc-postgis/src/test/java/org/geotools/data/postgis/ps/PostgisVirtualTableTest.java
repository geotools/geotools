package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableTest;

public class PostgisVirtualTableTest extends JDBCVirtualTableTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISPSTestSetup());
    }

}
