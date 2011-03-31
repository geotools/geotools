package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCLobTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class TeradataLobTest extends JDBCLobTest {


    protected JDBCLobTestSetup createTestSetup() {
        return new TeradataLobTestSetup(new TeradataTestSetup());
    }

}
