package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCEscapingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class PostgisEscapingTestSetup extends JDBCEscapingTestSetup {

    public PostgisEscapingTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void dropEscapingTable() throws Exception {
        run("DROP TABLE \"esca\"\"ping\"");
    }
}
