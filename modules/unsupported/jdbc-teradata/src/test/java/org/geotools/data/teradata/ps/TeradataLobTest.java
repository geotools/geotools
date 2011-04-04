package org.geotools.data.teradata.ps;

import org.geotools.data.teradata.TeradataLobTestSetup;
import org.geotools.jdbc.JDBCLobTest;
import org.geotools.jdbc.JDBCLobTestSetup;

public class TeradataLobTest extends JDBCLobTest {


    protected JDBCLobTestSetup createTestSetup() {
        return new TeradataLobTestSetup(new TeradataPSTestSetup());
    }

    @Override
    // write lob without prepared statements not supported on Teradata 
    public void testWrite() throws Exception {
    }
}
