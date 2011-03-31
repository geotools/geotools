package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCSkipColumnTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class TeradataSkipColumnTest extends JDBCSkipColumnTest {


    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new TeradataSkipColumnTestSetup();
    }

}
