package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisLobTestSetup;
import org.geotools.jdbc.JDBCLobOnlineTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class PostgisLobOnlineTest extends JDBCLobOnlineTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new PostgisLobTestSetup(new PostGISPSTestSetup());
    }
}
