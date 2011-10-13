package org.geotools.data.db2;

import java.io.IOException;

import org.geotools.jdbc.JDBCFunctionTest;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/jdbc/jdbc-db2/src/test/java/org/geotools/data/db2/DB2FunctionTest.java $
 */
public class DB2FunctionTest extends JDBCFunctionTest {


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
