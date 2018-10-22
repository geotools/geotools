package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;

public class PostgisDateOnlineTest extends JDBCDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new PostgisDateTestSetup(new PostGISTestSetup());
    }
}
