package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisDateTestSetup;
import org.geotools.jdbc.JDBCDateTest;
import org.geotools.jdbc.JDBCDateTestSetup;

public class PostgisDateTest extends JDBCDateTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new PostgisDateTestSetup(new PostGISPSTestSetup());
    }
    
}
