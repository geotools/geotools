package org.geotools.data.h2;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterTest;

public class H2TemporalFilterTest extends JDBCTemporalFilterTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new H2DateTestSetup();
    }

}
