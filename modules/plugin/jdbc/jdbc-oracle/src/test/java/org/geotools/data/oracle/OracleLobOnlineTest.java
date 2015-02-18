package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCLobOnlineTest;
import org.geotools.jdbc.JDBCLobTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleLobOnlineTest extends JDBCLobOnlineTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new OracleLobTestSetup();
    }

}
