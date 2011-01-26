package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;

public class IngresViewTest extends JDBCViewTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new IngresViewTestSetup();
    }

}
