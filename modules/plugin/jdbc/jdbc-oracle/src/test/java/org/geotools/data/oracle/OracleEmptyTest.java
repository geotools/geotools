package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCEmptyTest;
import org.geotools.jdbc.JDBCEmptyTestSetup;

public class OracleEmptyTest extends JDBCEmptyTest {

    @Override
    protected JDBCEmptyTestSetup createTestSetup() {
        return new OracleEmptyTestSetup();
    }

}
