package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class IngresPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new IngresPrimaryKeyFinderTestSetup();
    }

}
