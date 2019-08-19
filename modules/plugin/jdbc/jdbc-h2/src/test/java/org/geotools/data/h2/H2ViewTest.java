package org.geotools.data.h2;

import org.geotools.jdbc.JDBCViewOnlineTest;
import org.geotools.jdbc.JDBCViewTestSetup;

public class H2ViewTest extends JDBCViewOnlineTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new H2ViewTestSetup();
    }
}
