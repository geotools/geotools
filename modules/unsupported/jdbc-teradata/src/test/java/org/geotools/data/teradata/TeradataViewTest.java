package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;

public class TeradataViewTest extends JDBCViewTest {


    protected JDBCViewTestSetup createTestSetup() {
        return new TeradataViewTestSetup();
    }

}
