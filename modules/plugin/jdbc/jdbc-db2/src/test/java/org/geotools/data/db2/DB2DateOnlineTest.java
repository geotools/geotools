package org.geotools.data.db2;

import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class DB2DateOnlineTest extends JDBCDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new DB2DateTestSetup();
    }

}
