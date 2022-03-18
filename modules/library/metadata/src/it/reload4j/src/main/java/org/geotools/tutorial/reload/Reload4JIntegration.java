/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.reload;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.geotools.util.logging.Logging;

public class Reload4JIntegration {

    static final Logger LOGGER = Logging.getLogger(Reload4JIntegration.class);
    
    public static void main(String args[]) {       
        LOGGER.info("Welcome to Reload4J Integration Example");
        if( System.getProperties().containsKey("java.util.logging.config.file") ){
            File config = new File(System.getProperty("java.util.logging.config.file"));
            LOGGER.config("java.util.logging.config.file="+config);
        }
        LOGGER.fine("Everything is fine...");
        LOGGER.finer("Everything is finer...");
        LOGGER.finest("Everything is finest...");
    }
}