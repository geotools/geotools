package org.geotools.data.teradata.ps;

import org.geotools.data.teradata.TeradataDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableTest;

public class TeradataVirtualTableTest extends JDBCVirtualTableTest {


    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new TeradataDataStoreAPITestSetup(new TeradataPSTestSetup());
    }

}
