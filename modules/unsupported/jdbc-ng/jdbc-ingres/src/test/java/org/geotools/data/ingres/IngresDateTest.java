package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCDateTest;
import org.geotools.jdbc.JDBCDateTestSetup;

public class IngresDateTest extends JDBCDateTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new IngresDateTestSetup(new IngresTestSetup());
    }
    
}
