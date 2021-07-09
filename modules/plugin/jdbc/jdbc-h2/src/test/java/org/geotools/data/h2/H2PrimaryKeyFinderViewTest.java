package org.geotools.data.h2;

import java.util.HashMap;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderViewOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderViewTestSetup;

public class H2PrimaryKeyFinderViewTest extends JDBCPrimaryKeyFinderViewOnlineTest {

    @Override
    protected JDBCPrimaryKeyFinderViewTestSetup createTestSetup() {
        return new H2PrimaryKeyFinderViewTestSetup();
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
        return params;
    }
}
