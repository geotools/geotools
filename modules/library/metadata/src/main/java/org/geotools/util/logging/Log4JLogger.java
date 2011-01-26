/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.logging;

import java.util.logging.Level;


/**
 * An adapter that redirect all Java logging events to the Apache's
 * <A HREF="http://logging.apache.org/log4j">Log4J</A> framework.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Saul Farber (MassGIS)
 *
 * @see Log4JLoggerFactory
 * @see Logging
 */
final class Log4JLogger extends LoggerAdapter {
    /**
     * The Log4J logger to use.
     */
    final org.apache.log4j.Logger logger;

    /**
     * Creates a new logger.
     *
     * @param name   The logger name.
     * @param logger The result of {@code Logger.getLogger(name)}.
     */
    public Log4JLogger(final String name, final org.apache.log4j.Logger logger) {
        super(name);
        this.logger = logger;
    }

    /**
     * Returns the Log4J level for the given Java level.
     */
    @SuppressWarnings("fallthrough")
    private static org.apache.log4j.Level toLog4JLevel(final Level level) {
        final int n = level.intValue();
        switch (n / 100) {
            default: {
                // MAX_VALUE is a special value for Level.OFF. Otherwise and
                // if positive, log to fatal since we are greater than SEVERE.
                switch (n) {
                    default: if (n >= 0)    return org.apache.log4j.Level.FATAL; // fallthrough ALL otherwise.
                    case Integer.MIN_VALUE: return org.apache.log4j.Level.ALL;
                    case Integer.MAX_VALUE: return org.apache.log4j.Level.OFF;
                }
            }
            case 10: return org.apache.log4j.Level.ERROR;    // SEVERE
            case  9: return org.apache.log4j.Level.WARN;     // WARNING
            case  8:                                         // INFO
            case  7: return org.apache.log4j.Level.INFO;     // CONFIG
            case  6:                                         // (not allocated)
            case  5: return org.apache.log4j.Level.DEBUG;    // FINE
            case  4: return org.apache.log4j.Level.TRACE;    // FINER
            case  3:                                         // FINEST
            case  2:                                         // (not allocated)
            case  1:                                         // (not allocated)
            case  0: return org.apache.log4j.Level.ALL;      // ALL
        }
    }

    /**
     * Returns the Java level for the given Log4J level.
     */
    private static Level toJavaLevel(final org.apache.log4j.Level level) {
        final int n = level.toInt();
        if (n == org.apache.log4j.Level.OFF_INT)   return Level.OFF;
        if (n >= org.apache.log4j.Level.ERROR_INT) return Level.SEVERE;
        if (n >= org.apache.log4j.Level.WARN_INT)  return Level.WARNING;
        if (n >= org.apache.log4j.Level.INFO_INT)  return Level.CONFIG;
        if (n >= org.apache.log4j.Level.DEBUG_INT) return Level.FINE;
        if (n >= org.apache.log4j.Level.TRACE_INT) return Level.FINER;
        return Level.ALL;
    }

    /**
     * Set the level for this logger.
     */
    public void setLevel(final Level level) {
        logger.setLevel(toLog4JLevel(level));
    }

    /**
     * Returns the level for this logger.
     */
    public Level getLevel() {
        return toJavaLevel(logger.getEffectiveLevel());
    }

    /**
     * Returns {@code true} if the specified level is loggable.
     */
    public boolean isLoggable(final Level level) {
        return logger.isEnabledFor(toLog4JLevel(level));
    }

    /**
     * Logs a record at the specified level.
     */
    @Override
    public void log(final Level level, final String message) {
        logger.log(toLog4JLevel(level), message);
    }

    /**
     * Logs a record at the specified level.
     */
    @Override
    public void log(final Level level, final String message, final Throwable thrown) {
        logger.log(toLog4JLevel(level), message, thrown);
    }

    public void severe (String message) {logger.error(message);}
    public void warning(String message) {logger.warn (message);}
    public void info   (String message) {logger.info (message);}
    public void config (String message) {logger.info (message);}
    public void fine   (String message) {logger.debug(message);}
    public void finer  (String message) {logger.debug(message);}
    public void finest (String message) {logger.trace(message);}
}
