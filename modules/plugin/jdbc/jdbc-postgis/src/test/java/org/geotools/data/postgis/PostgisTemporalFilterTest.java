package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterTest;

public class PostgisTemporalFilterTest extends JDBCTemporalFilterTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new PostgisDateTestSetup(new PostGISTestSetup());
    }

}
