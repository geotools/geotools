package org.geotools.data.h2;

import org.geotools.jdbc.JDBCThreeValuedLogicOnlineTest;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

public class H2ThreeValuedLogicTest extends JDBCThreeValuedLogicOnlineTest {

    @Override
    protected JDBCThreeValuedLogicTestSetup createTestSetup() {
        return new H2ThreeValuedLogicTestSetup();
    }
}
