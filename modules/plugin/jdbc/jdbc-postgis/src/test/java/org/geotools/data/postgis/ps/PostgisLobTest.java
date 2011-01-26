package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisLobTestSetup;
import org.geotools.jdbc.JDBCLobTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class PostgisLobTest extends JDBCLobTest {

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new PostgisLobTestSetup(new PostGISPSTestSetup());
    }

}
