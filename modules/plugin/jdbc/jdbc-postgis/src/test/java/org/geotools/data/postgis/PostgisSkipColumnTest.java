package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCSkipColumnTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class PostgisSkipColumnTest extends JDBCSkipColumnTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new PostgisSkipColumnTestSetup();
    }

}
