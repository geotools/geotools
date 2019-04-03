/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.map;

import java.io.ByteArrayOutputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import org.geotools.util.logging.Logging;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class LoggerTest {
    private static final Logger LOGGER = Logging.getLogger(LoggerTest.class);
    private static Level oldLevel;
    protected Handler logHandler;
    protected ByteArrayOutputStream logStream;

    @BeforeClass
    public static void setupOnce() {
        oldLevel = LOGGER.getLevel();
        LOGGER.setLevel(Level.FINE);
    }

    @AfterClass
    public static void cleanupOnce() {
        LOGGER.setLevel(oldLevel);
    }

    protected void grabLogger() {
        grabLogger(Level.ALL);
    }

    protected void grabLogger(Level level) {
        logStream = new ByteArrayOutputStream();
        logHandler = new StreamHandler(logStream, new SimpleFormatter());
        logHandler.setLevel(level);
        LOGGER.addHandler(logHandler);
        LOGGER.setUseParentHandlers(false);
    }

    protected void releaseLogger() {
        if (logHandler != null) {
            LOGGER.removeHandler(logHandler);
            LOGGER.setUseParentHandlers(true);
        }
    }

    public String getLogOutput() {
        logHandler.flush();
        return logStream.toString();
    }
}
