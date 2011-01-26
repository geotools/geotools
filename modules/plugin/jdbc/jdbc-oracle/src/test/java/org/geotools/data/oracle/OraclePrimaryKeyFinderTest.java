package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class OraclePrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new OraclePrimaryKeyFinderTestSetup();
    }

}
