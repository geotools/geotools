package org.h2gis.geotools;

import org.geotools.jdbc.JDBCThreeValuedLogicOnlineTest;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

public class H2GISThreeValuedLogicTest extends JDBCThreeValuedLogicOnlineTest {

    @Override
    protected JDBCThreeValuedLogicTestSetup createTestSetup() {
        return new H2GISThreeValuedLogicTestSetup();
    }
}
