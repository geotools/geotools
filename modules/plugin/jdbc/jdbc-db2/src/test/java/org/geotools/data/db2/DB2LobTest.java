package org.geotools.data.db2;

import org.geotools.jdbc.JDBCLobTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class DB2LobTest extends JDBCLobTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new DB2LobTestSetup();
    }

}
