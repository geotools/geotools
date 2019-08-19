package org.geotools.data.postgis.ps;

import java.util.TimeZone;
import org.geotools.data.postgis.PostgisDateTestSetup;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTimeZoneDateOnlineTest;

public class PostgisTimeZoneGMTMinus14DateOnlineTest extends JDBCTimeZoneDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        super.setTimeZone(TimeZone.getTimeZone("Etc/GMT-14"));
        return new PostgisDateTestSetup(new PostGISPSTestSetup());
    }
}
