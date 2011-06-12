package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCFunctionTest;
import org.geotools.jdbc.JDBCTestSetup;

public class PostGISFunctionTest extends JDBCFunctionTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostgisFunctionTestSetup();
    }

}
