/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VPFLogger {
    /** The logger for the vpf module. */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger("org.geotools.data.vpf");

    protected static Level loggerLevel = Level.INFO;

    protected static boolean levelSet = false;

    public static Logger getLogger() {
        Logger log = VPFLogger.LOGGER;
        if (!VPFLogger.levelSet) {
            log.setLevel(VPFLogger.loggerLevel);
            VPFLogger.levelSet = true;
        }

        return log;
    }

    public static Level getLoggerLevel() {
        return VPFLogger.loggerLevel;
    }

    public static void setLoggerLevel(Level level) {
        VPFLogger.loggerLevel = level;
        VPFLogger.LOGGER.setLevel(level);
        VPFLogger.levelSet = true;
    }

    public static void log(String msg) {
        Logger log = VPFLogger.getLogger();
        Level level = VPFLogger.getLoggerLevel();
        log.log(level, msg);
    }

    public static boolean isLoggable(Level level) {
        return level.intValue() >= VPFLogger.loggerLevel.intValue();
    }
}
