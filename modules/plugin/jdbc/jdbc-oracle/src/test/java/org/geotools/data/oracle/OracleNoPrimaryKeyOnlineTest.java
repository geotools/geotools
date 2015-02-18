package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCNoPrimaryKeyOnlineTest;
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleNoPrimaryKeyOnlineTest extends JDBCNoPrimaryKeyOnlineTest {

    @Override
    protected JDBCNoPrimaryKeyTestSetup createTestSetup() {
        return new OracleNoPrimaryKeyTestSetup();
    }

}
