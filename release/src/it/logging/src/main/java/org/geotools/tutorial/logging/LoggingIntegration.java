/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.logging;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.DefaultLoggerFactory;
import org.geotools.util.logging.Logging;
/**
 * Logging integration demonstration illustrating use of logging.properties to configure GeoTools.
 */
public class LoggingIntegration {
    static {
        GeoTools.init();
    }

    static final Logger LOGGER = Logging.getLogger(LoggingIntegration.class);

    public static void main(String args[]) {

        if (Logging.ALL.getLoggerFactory() != DefaultLoggerFactory.getInstance()) {
            System.err.println(
                    "Expected GeoTools.init() use native java util logging factory, was "
                            + Logging.ALL.getLoggerFactory());
        }

        LOGGER.info("Welcome to Logging Integration Example");
        checkProperty("java.util.logging.config.file");
        LOGGER.config("Configuration " + Logging.ALL.lookupConfiguration());

        LOGGER.finest("Everything is finest...");
        LOGGER.finer("Everything is finer...");
        LOGGER.fine("Everything is fine...");
        LOGGER.config("Everything is configured...");
        LOGGER.log(Logging.OPERATION, "Everything is operating...");
        LOGGER.info("Everything is okay.");
        LOGGER.warning("Everything is alarming!");
        LOGGER.severe("Everything is terrible!");
        LOGGER.log(Logging.FATAL, "Everything has died!");
    }

    private static void checkProperty(String property) {
        if (System.getProperties().containsKey(property)) {
            LOGGER.config(property + "=" + System.getProperty(property));
        }
    }
}
