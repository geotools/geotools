/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

public final class Loggers {

    private Loggers() {
        //
    }

    public static final Logger MODULE = Logging.getLogger("org.geotools.data.wfs");

    public static final Logger REQUESTS = Logging.getLogger("org.geotools.data.wfs.requests");

    public static final Logger RESPONSES = Logging.getLogger("org.geotools.data.wfs.responses");

    public static final Level MODULE_TRACE_LEVEL = Level.FINER;

    public static final Level MODULE_DEBUG_LEVEL = Level.FINE;

    public static final Level MODULE_INFO_LEVEL = Level.INFO;

    public static final Level REQUEST_TRACE_LEVEL = Level.INFO;// TODO: lower this

    public static final Level REQUEST_DEBUG_LEVEL = Level.INFO;// TODO: lower this

    public static final Level REQUEST_INFO_LEVEL = Level.INFO;

    public static final Level RESPONSES_TRACE_LEVEL = Level.INFO;// TODO: lower this

    public static final Level RESPONSES_DEBUG_LEVEL = Level.INFO;// TODO: lower this

    public static void trace(Object... message) {
        log(MODULE, MODULE_TRACE_LEVEL, message);
    }

    public static void debug(Object... message) {
        log(MODULE, MODULE_DEBUG_LEVEL, message);
    }

    public static void info(Object... message) {
        log(MODULE, MODULE_INFO_LEVEL, message);
    }

    public static void requestTrace(Object... message) {
        log(REQUESTS, REQUEST_TRACE_LEVEL, message);
    }

    public static void requestDebug(Object... message) {
        log(REQUESTS, REQUEST_DEBUG_LEVEL, message);
    }

    public static void requestInfo(Object... message) {
        log(REQUESTS, REQUEST_INFO_LEVEL, message);
    }

    public static void responseTrace(Object... message) {
        log(RESPONSES, RESPONSES_TRACE_LEVEL, message);
    }

    public static void responseDebug(Object... message) {
        log(RESPONSES, RESPONSES_DEBUG_LEVEL, message);
    }

    private static void log(Logger logger, Level level, Object... message) {
        if (logger.isLoggable(level)) {
            // miss guava Joiner....
            StringBuilder sb = new StringBuilder();
            for (Object part : message) {
                sb.append(part);
            }
            logger.log(level, sb.toString());
        }
    }

}
