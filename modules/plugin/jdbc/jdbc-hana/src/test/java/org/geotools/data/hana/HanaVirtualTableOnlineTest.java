package org.geotools.data.hana;

import static org.junit.Assert.fail;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;
import org.geotools.util.logging.Logging;
import org.junit.Test;

/*
 * @author Johannes Quast, SAP SE
 */
public class HanaVirtualTableOnlineTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new HanaDataStoreAPITestSetup(new HanaTestSetupPSPooling());
    }

    @Override
    protected void connect() throws Exception {
        dbSchemaName = getFixture().getProperty("schema", "geotools");
        super.connect();
    }

    @Test
    public void testOptimizedBounds() throws Exception {
        Handler handler = getHandler();
        ((HanaDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        Logger logger = Logging.getLogger(HanaVirtualTableOnlineTest.class);
        Level oldLevel = logger.getLevel();
        try {
            logger.setLevel(Level.WARNING);
            logger.addHandler(handler);

            super.testBounds();
        } finally {
            ((HanaDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(false);
            logger.setLevel(oldLevel);
            logger.removeHandler(handler);
        }
    }

    private static Handler getHandler() {
        Handler handler =
                new Handler() {
                    @Override
                    public synchronized void publish(LogRecord record) {
                        fail(
                                "Fast extent estimation should not have been triggered on virtual "
                                        + "tables, but we received a log message anyway.");
                    }

                    @Override
                    public void flush() {
                        // nothing to do
                    }

                    @Override
                    public void close() throws SecurityException {
                        // nothing to do
                    }
                };
        handler.setLevel(Level.WARNING);
        return handler;
    }
}
