package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCThreeValuedLogicTest;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

public class PostGISThreeValuedLogicTest extends JDBCThreeValuedLogicTest {

    @Override
    protected JDBCThreeValuedLogicTestSetup createTestSetup() {
        return new JDBCThreeValuedLogicTestSetup(new PostGISTestSetup());
    }

}
