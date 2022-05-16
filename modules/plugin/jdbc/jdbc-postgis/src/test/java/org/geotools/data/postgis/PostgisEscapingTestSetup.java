package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCEscapingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostgisEscapingTestSetup extends JDBCEscapingTestSetup {

    public PostgisEscapingTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void dropEscapingTable() throws Exception {
        run("DROP TABLE \"esca\"\"ping\"");
    }
}
