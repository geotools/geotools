package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;


/**
 * 
 *
 * @source $URL$
 */
public class IngresViewTest extends JDBCViewTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new IngresViewTestSetup();
    }
    
    //@Override
    //protected boolean supportsPkOnViews() {
    //    return true;
    //}

    @Override
    protected boolean isPkNillable() {
        return false;
    }

}
