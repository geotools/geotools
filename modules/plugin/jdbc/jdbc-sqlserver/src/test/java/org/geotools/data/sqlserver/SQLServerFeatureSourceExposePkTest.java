package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCFeatureSourceExposePkTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerFeatureSourceExposePkTest extends JDBCFeatureSourceExposePkTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }

}
