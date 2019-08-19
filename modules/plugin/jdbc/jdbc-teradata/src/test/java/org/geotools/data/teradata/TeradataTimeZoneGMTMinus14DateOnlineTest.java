package org.geotools.data.teradata;

import java.util.TimeZone;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTimeZoneDateOnlineTest;

public class TeradataTimeZoneGMTMinus14DateOnlineTest extends JDBCTimeZoneDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        super.setTimeZone(TimeZone.getTimeZone("Etc/GMT-14"));
        return new TeradataDateTestSetup(new TeradataTestSetup());
    }
}
