package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCLobTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class OracleLobTest extends JDBCLobTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new OracleLobTestSetup();
    }

}
