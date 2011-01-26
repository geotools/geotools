package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class PostgisPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new PostgisPrimaryKeyFinderTestSetup();
    }

}
