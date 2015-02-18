package org.geotools.data.db2;

import org.geotools.jdbc.JDBCLobOnlineTest;
import org.geotools.jdbc.JDBCLobTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class DB2LobOnlineTest extends JDBCLobOnlineTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new DB2LobTestSetup();
    }

}
