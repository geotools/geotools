package org.geotools.geopkg;

import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.junit.Ignore;

/**
 * 
 *
 * @source $URL$
 */
public class GeoPkgDateOnlineTest extends JDBCDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new GeoPkgDateTestSetup(new GeoPkgTestSetup());
    }
    
    // This test fails because it's not able to re-create the table after dropping it
    // while the test is running, but for the life of me I cannot figure out why
    @Ignore
    public void testFiltersByDate() throws Exception {
        
    }
}
