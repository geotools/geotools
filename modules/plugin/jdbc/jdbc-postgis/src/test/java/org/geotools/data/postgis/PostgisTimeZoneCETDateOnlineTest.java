package org.geotools.data.postgis;

import java.util.TimeZone;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTimeZoneDateOnlineTest;

public class PostgisTimeZoneCETDateOnlineTest extends JDBCTimeZoneDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        super.setTimeZone(TimeZone.getTimeZone("CET"));
        return new PostgisDateTestSetup(new PostGISTestSetup());
    }
}
