package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCEscapingOnlineTest;
import org.geotools.jdbc.JDBCEscapingTestSetup;

public class PostgisEscapingOnlineTest extends JDBCEscapingOnlineTest {

    @Override
    protected JDBCEscapingTestSetup createTestSetup() {
        return new PostgisEscapingTestSetup(new PostGISTestSetup());
    }
}
