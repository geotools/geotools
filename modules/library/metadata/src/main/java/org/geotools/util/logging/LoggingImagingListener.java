package org.geotools.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.util.ImagingListener;

final class LoggingImagingListener implements ImagingListener {
    @Override
    public boolean errorOccurred(
            String message, Throwable thrown, Object where, boolean isRetryable)
            throws RuntimeException {
        Logger log = Logging.getLogger(LoggingImagingListener.class);
        if (message.contains("Continuing in pure Java mode")) {
            log.log(Level.FINER, message, thrown);
        } else {
            log.log(Level.INFO, message, thrown);
        }
        return false; // we are not trying to recover
    }

    @Override
    public String toString() {
        return "LoggingImagingListener";
    }
}
