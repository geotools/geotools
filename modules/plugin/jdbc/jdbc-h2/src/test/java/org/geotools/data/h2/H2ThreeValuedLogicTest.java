package org.geotools.data.h2;

import org.geotools.jdbc.JDBCThreeValuedLogicTest;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

public class H2ThreeValuedLogicTest extends JDBCThreeValuedLogicTest {

    @Override
    protected JDBCThreeValuedLogicTestSetup createTestSetup() {
        return new H2ThreeValuedLogicTestSetup();
    }

}
