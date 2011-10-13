package org.geotools.data.db2;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTemporalFilterTest;


public class DB2TemporalFilterTest extends JDBCTemporalFilterTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new DB2DateTestSetup();
    }

}
