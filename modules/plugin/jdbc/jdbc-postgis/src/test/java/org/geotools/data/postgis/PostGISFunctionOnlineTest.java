package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCFunctionOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class PostGISFunctionOnlineTest extends JDBCFunctionOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostgisFunctionTestSetup();
    }

}
