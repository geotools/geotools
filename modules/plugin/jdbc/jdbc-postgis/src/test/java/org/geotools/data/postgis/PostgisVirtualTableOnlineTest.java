package org.geotools.data.postgis;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCVirtualTableOnlineTest;
import org.geotools.util.logging.Logging;

public class PostgisVirtualTableOnlineTest extends JDBCVirtualTableOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISTestSetup());
    }

    public void testOptimizedBounds() throws Exception {
        Handler handler =
                new Handler() {
                    @Override
                    public synchronized void publish(LogRecord record) {
                        fail("We should not have received any log statement");
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
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        Logger logger = Logging.getLogger(PostgisVirtualTableOnlineTest.class);
        Level oldLevel = logger.getLevel();
        try {
            logger.setLevel(java.util.logging.Level.WARNING);
            logger.addHandler(handler);

            super.testBounds();
        } finally {
            ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(false);
            logger.setLevel(oldLevel);
            logger.removeHandler(handler);
        }
    }
}
