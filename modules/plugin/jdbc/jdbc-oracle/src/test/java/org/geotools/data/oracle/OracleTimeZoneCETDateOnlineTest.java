package org.geotools.data.oracle;

import java.util.TimeZone;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTimeZoneDateOnlineTest;

/**
 * 
 *
 * @source $URL$
 */
public class OracleTimeZoneCETDateOnlineTest extends JDBCTimeZoneDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        super.setTimeZone(TimeZone.getTimeZone("CET"));
        return new OracleDateTestSetup(new OracleTestSetup());
    }
    
}
