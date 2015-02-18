package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCViewOnlineTest;
import org.geotools.jdbc.JDBCViewTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class OracleViewOnlineTest extends JDBCViewOnlineTest {

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
