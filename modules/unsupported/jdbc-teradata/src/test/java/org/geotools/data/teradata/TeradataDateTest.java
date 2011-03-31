package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCDateTest;
import org.geotools.jdbc.JDBCDateTestSetup;

public class TeradataDateTest extends JDBCDateTest {


    protected JDBCDateTestSetup createTestSetup() {
        return new TeradataDateTestSetup(new TeradataTestSetup());
    }

}
