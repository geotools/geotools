package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCLobOnlineTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class SQLServerLobOnlineTest extends JDBCLobOnlineTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new SQLServerLobTestSetup(new SQLServerTestSetup());
    }
}
