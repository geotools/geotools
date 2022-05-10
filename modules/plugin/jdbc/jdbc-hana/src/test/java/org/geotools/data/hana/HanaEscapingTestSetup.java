package org.geotools.data.hana;

import java.sql.Connection;
import org.geotools.jdbc.JDBCEscapingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class HanaEscapingTestSetup extends JDBCEscapingTestSetup {

    private static final String TABLE = "esca\"ping";

    public HanaEscapingTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void dropEscapingTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE);
        }
    }
}
