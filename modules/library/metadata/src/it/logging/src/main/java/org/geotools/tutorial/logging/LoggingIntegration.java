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
import org.geotools.util.logging.Logging;

/**
 * Logging integration demonstration illustrating use of logging.properties to
 * configure GeoTools.
 */
public class LoggingIntegration {

    static final Logger LOGGER = Logging.getLogger(LoggingIntegration.class);
    
    public static void main(String args[]) {       
        LOGGER.info("Welcome to Logging Integration Example");
        if( System.getProperties().containsKey("java.util.logging.config.file") ){
            File config = new File(System.getProperty("java.util.logging.config.file"));
            LOGGER.config("Using java.util.logging.config.file='"+config+"' configuration.");
        }
        else {
            LOGGER.config("No java.util.logging.config.file configuration provided.");
        }
        LOGGER.fine("Everything is fine...");
        LOGGER.finer("Everything is finer...");
        LOGGER.finest("Everything is finest...");
    }
}