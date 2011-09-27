package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class IngresPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new IngresPrimaryKeyFinderTestSetup();
    }

}
