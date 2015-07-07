package org.geotools.data.oracle;

import java.util.TimeZone;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTimeZoneDateOnlineTest;

/**
 * 
 *
 * @source $URL$
 */
public class OracleTimeZoneGMTMinus14DateOnlineTest extends JDBCTimeZoneDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        super.setTimeZone(TimeZone.getTimeZone("Etc/GMT-14"));
        return new OracleDateTestSetup(new OracleTestSetup());
    }
    
}
