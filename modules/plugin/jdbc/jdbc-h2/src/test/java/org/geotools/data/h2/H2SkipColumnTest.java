package org.geotools.data.h2;

import org.geotools.jdbc.JDBCSkipColumnOnlineTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class H2SkipColumnTest extends JDBCSkipColumnOnlineTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new H2SkipColumnTestSetup();
    }

}
