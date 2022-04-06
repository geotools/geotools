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

import java.util.logging.Logger;

import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Log4J2LoggerFactory;
import org.geotools.util.logging.Logging;
/** Example illustrating use of Log4J 2 API and startup environment. */
public class Log4JIntegration {
    static {
        GeoTools.init();
    }

    static final Logger LOGGER = Logging.getLogger(Log4JIntegration.class);

    public static void main(String args[]) {
        LOGGER.info("Welcome to Log4j Integration Example");
        if (!LOGGER.getClass().getName().equals("org.geotools.util.logging.Log4J2Logger")) {
            LOGGER.severe("Log4J2Logger expected, but was:" + LOGGER.getClass().getName());
        }

        // Log4J2 properties
        LOGGER.info("Welcome to Log4j Integration Example");
        checkProperty("log4j2.configurationFile");
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
