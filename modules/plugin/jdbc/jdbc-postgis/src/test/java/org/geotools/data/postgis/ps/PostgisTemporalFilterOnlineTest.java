package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisDateTestSetup;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterOnlineTest;

public class PostgisTemporalFilterOnlineTest extends JDBCTemporalFilterOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new PostgisDateTestSetup(new PostGISPSTestSetup());
    }
}
