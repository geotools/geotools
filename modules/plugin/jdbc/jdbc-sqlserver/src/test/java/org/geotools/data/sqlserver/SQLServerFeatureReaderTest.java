package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCFeatureReaderTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerFeatureReaderTest extends JDBCFeatureReaderTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerTestSetup();
    }

}
