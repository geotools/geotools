package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCThreeValuedLogicOnlineTest;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

public class PostGISThreeValuedLogicOnlineTest extends JDBCThreeValuedLogicOnlineTest {

    @Override
    protected JDBCThreeValuedLogicTestSetup createTestSetup() {
        return new JDBCThreeValuedLogicTestSetup(new PostGISTestSetup());
    }
}
