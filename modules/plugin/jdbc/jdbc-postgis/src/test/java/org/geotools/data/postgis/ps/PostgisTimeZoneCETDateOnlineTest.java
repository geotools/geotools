package org.geotools.data.postgis.ps;

import java.util.TimeZone;
import org.geotools.data.postgis.PostgisDateTestSetup;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTimeZoneDateOnlineTest;

public class PostgisTimeZoneCETDateOnlineTest extends JDBCTimeZoneDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        super.setTimeZone(TimeZone.getTimeZone("CET"));
        return new PostgisDateTestSetup(new PostGISPSTestSetup());
    }
}
