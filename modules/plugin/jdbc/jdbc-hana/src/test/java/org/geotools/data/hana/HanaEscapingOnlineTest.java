package org.geotools.data.hana;

import org.geotools.jdbc.JDBCEscapingOnlineTest;
import org.geotools.jdbc.JDBCEscapingTestSetup;

public class HanaEscapingOnlineTest extends JDBCEscapingOnlineTest {

    @Override
    protected JDBCEscapingTestSetup createTestSetup() {
        return new HanaEscapingTestSetup(new HanaTestSetup());
    }
}
