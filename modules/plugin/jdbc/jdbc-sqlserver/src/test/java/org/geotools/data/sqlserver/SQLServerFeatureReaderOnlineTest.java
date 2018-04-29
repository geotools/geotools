package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCFeatureReaderOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerFeatureReaderOnlineTest extends JDBCFeatureReaderOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }
}
