package org.geotools.data.postgis;

import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderViewOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderViewTestSetup;

public class PostgisPrimaryKeyFinderViewOnlineTest extends JDBCPrimaryKeyFinderViewOnlineTest {

    @Override
    protected JDBCPrimaryKeyFinderViewTestSetup createTestSetup() {
        return new PostgisPrimaryKeyFinderViewTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
        return params;
    }
}
