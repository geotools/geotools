package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;

public class OracleViewTest extends JDBCViewTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new OracleViewTestSetup();
    }
    
    @Override
    protected boolean isPkNillable() {
        return false;
    }

    @Override
    protected boolean supportsPkOnViews() {
        return true;
    }
}
