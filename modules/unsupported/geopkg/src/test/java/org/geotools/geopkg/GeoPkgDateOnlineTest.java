package org.geotools.geopkg;

import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;

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
    
}
