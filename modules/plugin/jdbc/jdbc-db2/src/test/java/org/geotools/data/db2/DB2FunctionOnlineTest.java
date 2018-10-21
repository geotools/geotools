package org.geotools.data.db2;

import java.io.IOException;
import org.geotools.jdbc.JDBCFunctionOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

public class DB2FunctionOnlineTest extends JDBCFunctionOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DB2FunctionTestSetup();
    }

    @Override
    public void testStrEndsWithOtherProperty() throws IOException {
        // TODO
        // skip this test because the test uses numerical columns
        // instead of strings
    }

    @Override
    public void testStrStartsWithOtherProperty() throws IOException {
        // TODO
        // skip this test because the test uses numerical columns
        // instead of strings
    }
}
