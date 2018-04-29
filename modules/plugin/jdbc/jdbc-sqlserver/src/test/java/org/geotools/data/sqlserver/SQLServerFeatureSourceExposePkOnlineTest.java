package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCFeatureSourceExposePkOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerFeatureSourceExposePkOnlineTest extends JDBCFeatureSourceExposePkOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }
}
