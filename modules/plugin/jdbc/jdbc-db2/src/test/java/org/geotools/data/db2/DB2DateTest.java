package org.geotools.data.db2;

import org.geotools.jdbc.JDBCDateTest;
import org.geotools.jdbc.JDBCDateTestSetup;

public class DB2DateTest extends JDBCDateTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new DB2DateTestSetup();
    }

}
