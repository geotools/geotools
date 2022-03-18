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
 * An adapter that redirect all Java logging events to the Apache's <A
 * HREF="http://logging.apache.org/log4j">Log4J</A> framework.
 *
 * <ul>
 *   <li>{@link java.util.logging.Level#OFF}: {@link org.apache.log4j.Level#ALL}
 *   <li>{@link java.util.logging.Level#SEVERE}: {@link org.apache.log4j.Level#ERROR}
 *   <li>{@link java.util.logging.Level#WARNING} {@link org.apache.log4j.Level#WARN}
 *   <li>{@link java.util.logging.Level#INFO}: {@link org.apache.log4j.Level#INFO}
 *   <li>{@link java.util.logging.Level#CONFIG}: {@link #CONFIG}
 *   <li>{@link java.util.logging.Level#FINE}: {@link org.apache.log4j.Level#DEBUG}
 *   <li>{@link java.util.logging.Level#FINER}: {@link org.apache.log4j.Level#TRACE}
 *   <li>{@link java.util.logging.Level#FINEST}: {@link #FINEST}
 *   <li>{@link java.util.logging.Level#OFF}: {@link org.apache.log4j.Level#ALL}
 * </ul>
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Saul Farber (MassGIS)
 * @see Log4JLoggerFactory
 * @see Logging
 */
final class Log4JLogger extends LoggerAdapter {
    /** The Log4J logger to use. */
    final org.apache.log4j.Logger logger;

    /** Used to define additional Log4J levels. */
    static class Log4JLevel extends org.apache.log4j.Level {
        Log4JLevel(int level, String levelStr, int syslogEquivalent) {
            super(level, levelStr, syslogEquivalent);
        }
    }

    private static final int CONFIG_INT = 15000;
    /**
     * Maps to Java Utility Logging {@link Level#CONFIG}, showing up below INFO messages when used
     * in Log4J setup.
     */
    public static final org.apache.log4j.Level CONFIG = new Log4JLevel(CONFIG_INT, "CONFIG", 6);

    private static final int FINEST_INT = 4000;
    /**
     * Maps to Java Utility Logging {@link Level#FINEST}, showing up below TRACE messages when used
     * in Log4J setup
     */
    public static final org.apache.log4j.Level FINEST = new Log4JLevel(FINEST_INT, "FINEST", 7);

    /**
     * Creates a new logger.
     *
     * @param name The logger name.
     * @param logger The result of {@code Logger.getLogger(name)}.
     */
    public Log4JLogger(final String name, final org.apache.log4j.Logger logger) {
        super(name);
        this.logger = logger;
    }

    /** Returns the Log4J level for the given Java level. */
    @SuppressWarnings("fallthrough")
    private static org.apache.log4j.Level toLog4JLevel(final Level level) {
        final int n = level.intValue();
        switch (n / 100) {
            case 10: // SEVERE
                return org.apache.log4j.Level.ERROR;
            case 9: // WARNING
                return org.apache.log4j.Level.WARN;
            case 8: // INFO
                return org.apache.log4j.Level.INFO;
            case 7: // CONFIG
                return CONFIG;
            case 6: // (not allocated)
            case 5: // FINE
                return org.apache.log4j.Level.DEBUG;
            case 4: // FINER
                return org.apache.log4j.Level.TRACE;
            case 3: // FINEST
                return FINEST;
            case 2: // (not allocated)
            case 1: // (not allocated)
            case 0: // ALL
                return org.apache.log4j.Level.ALL;
            default:
                // MAX_VALUE is a special value for Level.OFF. Otherwise and
                // if positive, log to fatal since we are greater than SEVERE.
                switch (n) {
                    case Integer.MIN_VALUE:
                        return org.apache.log4j.Level.ALL;
                    case Integer.MAX_VALUE:
                        return org.apache.log4j.Level.OFF;
                    default:
                        if (n >= 0) return org.apache.log4j.Level.FATAL;
                        else return org.apache.log4j.Level.ALL;
                }
        }
    }

    /** Returns the Java level for the given Log4J level. */
    private static Level toJavaLevel(final org.apache.log4j.Level level) {
        final int n = level.toInt();
        if (n == org.apache.log4j.Level.OFF_INT) return Level.OFF;
        if (n >= org.apache.log4j.Level.ERROR_INT) return Level.SEVERE;
        if (n >= org.apache.log4j.Level.WARN_INT) return Level.WARNING;
        if (n >= org.apache.log4j.Level.INFO_INT) return Level.INFO;
        if (n >= CONFIG_INT) return Level.CONFIG;
        if (n >= org.apache.log4j.Level.DEBUG_INT) return Level.FINE;
        if (n >= org.apache.log4j.Level.TRACE_INT) return Level.FINER;
        if (n >= FINEST_INT) return Level.FINEST;
        return Level.ALL;
    }

    /** Set the level for this logger. */
    @Override
    public void setLevel(final Level level) {
        logger.setLevel(toLog4JLevel(level));
    }

    /** Returns the level for this logger. */
    @Override
    public Level getLevel() {
        return toJavaLevel(logger.getEffectiveLevel());
    }

    /** Returns {@code true} if the specified level is loggable. */
    @Override
    public boolean isLoggable(final Level level) {
        return logger.isEnabledFor(toLog4JLevel(level));
    }

    /** Logs a record at the specified level. */
    @Override
    public void log(final Level level, final String message) {
        logger.log(toLog4JLevel(level), message);
    }

    /** Logs a record at the specified level. */
    @Override
    public void log(final Level level, final String message, final Throwable thrown) {
        logger.log(toLog4JLevel(level), message, thrown);
    }

    @Override
    public void severe(String message) {
        logger.error(message);
    }

    @Override
    public void warning(String message) {
        logger.warn(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void config(String message) {
        logger.log(CONFIG, message);
    }

    @Override
    public void fine(String message) {
        logger.debug(message);
    }

    @Override
    public void finer(String message) {
        logger.trace(message);
    }

    @Override
    public void finest(String message) {
        logger.log(FINEST, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Log4jLogger: ");
        sb.append(getName() == null ? "anonymous" : getName());
        sb.append(" : ");
        sb.append(getLevel());
        sb.append(" (");
        sb.append(logger.getLevel());
        sb.append(")");

        return sb.toString();
    }
}
