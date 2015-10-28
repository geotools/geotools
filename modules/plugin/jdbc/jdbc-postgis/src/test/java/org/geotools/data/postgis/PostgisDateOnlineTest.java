package org.geotools.data.postgis;

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
        return new PostgisDateTestSetup(new PostGISTestSetup());
    }
    
}
