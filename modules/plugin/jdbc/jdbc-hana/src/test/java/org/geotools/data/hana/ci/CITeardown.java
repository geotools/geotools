package org.geotools.data.hana.ci;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

public final class CITeardown extends CIBase {

    private static Logger LOGGER = Logging.getLogger(CITeardown.class);

    public static void main(String[] args) throws Exception {
        CITeardown instance = new CITeardown();
        instance.run();
        LOGGER.info("Teardown done");
    }

    private CITeardown() {}

    private void run() throws Exception {
        Properties fixture = readFixture();
        try (Connection conn = connectUsingFixture(fixture)) {
            String schema = fixture.getProperty("schema");
            try {
                dropSchema(conn, schema);
            } catch (SQLException e) {
                // Ignore exceptions reporting non-existing schema
                if (e.getErrorCode() != 362) {
                    throw e;
                }
                LOGGER.info("Schema " + schema + " does not exist");
            }
        }
    }
}
