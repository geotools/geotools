package org.geotools.util.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.media.jai.JAI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @author Marco Peters */
public class LoggingImagingListenerTest {

    private TestLogHandler logHandler;
    private Logger logger;

    @Before
    public void setupTest() {
        logger = Logging.getLogger("javax.media.jai");
        logger.setLevel(Level.ALL);
        logHandler = new TestLogHandler();
        logger.addHandler(logHandler);
    }

    @After
    public void teardownTest() {
        logger.setLevel(Level.INFO);
        logger.removeHandler(logHandler);
    }

    @SuppressWarnings({"TryFailThrowable", "AssertionFailureIgnored"})
    @Test
    public void errorOccurred_EnsureNoExceptionWhenNoNativeAccel() {
        LoggingImagingListener listener = new LoggingImagingListener();
        Throwable thrown = new Throwable("Continuing in pure Java mode");
        try {
            boolean recoverySuccess = listener.errorOccurred(thrown.getMessage(), thrown, JAI.class, false);
            assertFalse(recoverySuccess);
        } catch (Throwable ignore) {
            // expected
        }

        assertEquals(1, logHandler.logged.size());
        LogRecord logRecord = logHandler.logged.get(0);
        assertEquals("Continuing in pure Java mode", logRecord.getMessage());
        assertSame(thrown, logRecord.getThrown());
    }

    @Test
    public void errorOccurred_EnsureBehaviourOtherErrors() {
        LoggingImagingListener listener = new LoggingImagingListener();
        Throwable thrown = new IllegalStateException("Special test exception");
        try {
            listener.errorOccurred(thrown.getMessage(), thrown, JAI.class, false);
        } catch (IllegalStateException expected) {
        } catch (Throwable t) {
            fail(String.format("Exception '%s' not expected", t.getClass().getSimpleName()));
        }
        assertEquals(1, logHandler.logged.size());
        LogRecord logRecord = logHandler.logged.get(0);
        assertEquals("Special test exception", logRecord.getMessage());
        assertSame(thrown, logRecord.getThrown());
    }

    private static class TestLogHandler extends Handler {
        ArrayList<LogRecord> logged = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            logged.add(record);
        }

        @Override
        public void flush() {}

        @Override
        public void close() throws SecurityException {
            logged.clear();
        }
    }
}
