package org.geotools.data.postgis;

import java.util.HashMap;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class PostgisPrimaryKeyFinderOnlineTest extends JDBCPrimaryKeyFinderOnlineTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new PostgisPrimaryKeyFinderTestSetup();
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
        return params;
    }
}
