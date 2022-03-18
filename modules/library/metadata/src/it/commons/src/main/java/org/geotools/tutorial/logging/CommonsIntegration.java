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
import java.util.logging.Level;

import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.CommonsLoggerFactory;
import org.geotools.util.logging.Logging;

/**
 * Example illustrating use of SLF4J API and Logback startup environment.
 */
public class CommonsIntegration {

    public static final Logger LOGGER = initLogger();

    public static void main(String args[]) {
        LOGGER.info("Welcome to Commons Logging Integration Example");

        if(!LOGGER.getClass().getName().equals("org.geotools.util.logging.CommonsLogger")){
            LOGGER.severe("CommonsLogger expected, but was:" + LOGGER.getClass().getName() );
        }
        LOGGER.config("Configuration " + Logging.ALL.lookupConfiguration());

        LOGGER.finest("Everything is finest...");
        LOGGER.finer("Everything is finer...");
        LOGGER.fine("Everything is fine...");
        LOGGER.config("Everything is configured...");
        LOGGER.info("Everything is okay.");
        LOGGER.warning("Everything is alarming!");
        LOGGER.severe("Everything is terrible!");
    }
    
    private static Logger initLogger(){
        GeoTools.init();
        if( Logging.ALL.getLoggerFactory() != CommonsLoggerFactory.getInstance() ){
            System.err.println("Expected GeoTools.init() to configure CommonsLoggerFactory, was "+Logging.ALL.getLoggerFactory());
        }
        return Logging.getLogger(CommonsIntegration.class);
    }

}