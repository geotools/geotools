/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.reload;

import java.util.logging.Logger;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.geotools.util.logging.Log4JLoggerFactory;

/** Example illustrating use of Reload4J (providing Log4J 1 API) and startup environment. */
public class Reload4JIntegration {

    public static final Logger LOGGER = initLogger();

    public static void main(String args[]) {
        LOGGER.info("Welcome to Reload4J Integration Example");
        if (!LOGGER.getClass().getName().equals("org.geotools.util.logging.Log4JLogger")) {
            LOGGER.severe("Log4JLogger expected, but was:" + LOGGER.getClass().getName());
        }
        // Log4J Properties
        checkProperty("log4j.defaultInitOverride");
        checkProperty("log4j.configuratorClass");
        checkProperty("log4j.configuratorClass");

        if (System.getProperties().containsKey("log4j.configuration")) {
            LOGGER.config("log4j.configurationFile=" + System.getProperty("log4j.configuration"));
        }

        LOGGER.config("Configuration " + Log4JLoggerFactory.getInstance().lookupConfiguration());

        LOGGER.finest("Everything is finest...");
        LOGGER.finer("Everything is finer...");
        LOGGER.fine("Everything is fine...");
        LOGGER.config("Everything is configured...");
        LOGGER.info("Everything is okay.");
        LOGGER.warning("Everything is alarming!");
        LOGGER.severe("Everything is terrible!");
    }

    private static Logger initLogger() {
        GeoTools.init();
        return Logging.getLogger(Reload4JIntegration.class);
    }

    private static void checkProperty(String property) {
        if (System.getProperties().containsKey(property)) {
            LOGGER.config(property + "=" + System.getProperty(property));
        }
    }
}
