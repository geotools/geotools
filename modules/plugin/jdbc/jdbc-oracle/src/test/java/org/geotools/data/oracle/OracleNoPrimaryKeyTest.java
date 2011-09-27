package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCNoPrimaryKeyTest;
import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleNoPrimaryKeyTest extends JDBCNoPrimaryKeyTest {

    @Override
    protected JDBCNoPrimaryKeyTestSetup createTestSetup() {
        return new OracleNoPrimaryKeyTestSetup();
    }

}
