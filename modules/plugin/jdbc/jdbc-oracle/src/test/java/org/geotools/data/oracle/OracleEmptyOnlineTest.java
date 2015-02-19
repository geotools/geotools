package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCEmptyOnlineTest;
import org.geotools.jdbc.JDBCEmptyTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleEmptyOnlineTest extends JDBCEmptyOnlineTest {

    @Override
    protected JDBCEmptyTestSetup createTestSetup() {
        return new OracleEmptyTestSetup();
    }

}
