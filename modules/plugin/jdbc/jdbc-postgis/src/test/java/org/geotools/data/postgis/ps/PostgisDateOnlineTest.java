package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisDateTestSetup;
import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisDateOnlineTest extends JDBCDateOnlineTest {

    public PostgisDateOnlineTest() {
        testNegativeDates = true;
    }

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new PostgisDateTestSetup(new PostGISPSTestSetup());
    }
    
}
