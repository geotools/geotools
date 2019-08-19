package org.geotools.data.h2;

import java.util.HashMap;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCPrimaryKeyFinderOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class H2PrimaryKeyFinderTest extends JDBCPrimaryKeyFinderOnlineTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new H2PrimaryKeyFinderTestSetup();
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        params.put(JDBCDataStoreFactory.PK_METADATA_TABLE.key, "gt_pk_metadata");
        return params;
    }
}
