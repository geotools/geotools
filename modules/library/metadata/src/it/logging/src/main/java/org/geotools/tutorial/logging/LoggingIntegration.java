/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.logging;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Log4J2LoggerFactory;
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

        if (Logging.ALL.getLoggerFactory() != null) {
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
        LOGGER.info("Everything is okay.");
        LOGGER.warning("Everything is alarming!");
        LOGGER.severe("Everything is terrible!");
    }

    private static void checkProperty(String property) {
        if (System.getProperties().containsKey(property)) {
            LOGGER.config(property + "=" + System.getProperty(property));
        }
    }
}
