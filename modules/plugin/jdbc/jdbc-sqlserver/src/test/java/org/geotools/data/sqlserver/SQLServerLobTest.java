package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCLobTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class SQLServerLobTest extends JDBCLobTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new SQLServerLobTestSetup(new SQLServerTestSetup());
    }

}
