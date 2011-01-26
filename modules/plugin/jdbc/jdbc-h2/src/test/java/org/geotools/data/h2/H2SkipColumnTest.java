package org.geotools.data.h2;

import org.geotools.jdbc.JDBCSkipColumnTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class H2SkipColumnTest extends JDBCSkipColumnTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new H2SkipColumnTestSetup();
    }

}
