package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterOnlineTest;

public class PostgisTemporalFilterOnlineTest extends JDBCTemporalFilterOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new PostgisDateTestSetup(new PostGISTestSetup());
    }
}
