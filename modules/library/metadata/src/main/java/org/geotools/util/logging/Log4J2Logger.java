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
import org.apache.logging.log4j.spi.StandardLevel;

/**
 * An adapter that redirect all Java logging events to the Apache's <A
 * HREF="http://logging.apache.org/log4j">Log4J</A> framework.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Saul Farber (MassGIS)
 * @see Log4JLoggerFactory
 * @see Logging
 */
final class Log4J2Logger extends LoggerAdapter {
    /** The Log4J logger to use. */
    final org.apache.logging.log4j.Logger logger;

    /**
     * Define a Log4j Level mapping to java util logging {@link Level#CONFIG} (using StandardLevel
     * value 450).
     *
     * <p>Note: {@link StandardLevel#getStandardLevel(int)} will map to {@link StandardLevel#INFO}
     * if using a log4j adapter.
     */
    public static final org.apache.logging.log4j.Level CONFIG =
            org.apache.logging.log4j.Level.forName("CONFIG", StandardLevel.INFO.intLevel() + 50);

    /**
     * Define a Log4j Level mapping to java util logging {@link Level#FINEST} Level (using
     * StandardLevel value 700).
     *
     * <p>Note: {@link StandardLevel#getStandardLevel(int)} will map to {@link StandardLevel#DEBUG}
     * if using a log4j adapter.
     */
    public static final org.apache.logging.log4j.Level FINEST =
            org.apache.logging.log4j.Level.forName("FINEST", StandardLevel.DEBUG.intLevel() + 100);

    /**
     * Creates a new logger adapter mapping from Log4J to java util logging.
     *
     * @param name The logger name.
     * @param logger The result of {@code Logger.getLogger(name)}.
     */
    public Log4J2Logger(final String name, final org.apache.logging.log4j.Logger logger) {
        super(name);
        this.logger = logger;
    }

    /**
     * The Log4J level for the given java util logigng Level.
     *
     * @param level Java util logging level
     * @return Log4j Level for the provided java util logging Level.
     */
    @SuppressWarnings("fallthrough")
    private static org.apache.logging.log4j.Level toLog4JLevel(final Level level) {
        final int n = level.intValue();
        switch (n / 100) {
            case 10:
                return org.apache.logging.log4j.Level.ERROR; // SEVERE
            case 9:
                return org.apache.logging.log4j.Level.WARN; // WARNING
            case 8: // INFO
                return org.apache.logging.log4j.Level.INFO; // INFO
            case 7:
                return CONFIG; // CONFIG
            case 6: // (not allocated)
            case 5:
                return org.apache.logging.log4j.Level.DEBUG; // FINE
            case 4:
                return org.apache.logging.log4j.Level.TRACE; // FINER
            case 3: // FINEST
                return FINEST;
            case 2: // (not allocated)
            case 1: // (not allocated)
            case 0:
                return org.apache.logging.log4j.Level.ALL; // ALL
            default:
                {
                    // MAX_VALUE is a special value for Level.OFF. Otherwise and
                    // if positive, log to fatal since we are greater than SEVERE.
                    switch (n) {
                        case Integer.MIN_VALUE:
                            return org.apache.logging.log4j.Level.ALL;
                        case Integer.MAX_VALUE:
                            return org.apache.logging.log4j.Level.OFF;
                        default:
                            if (n >= 0)
                                return org.apache.logging.log4j.Level
                                        .FATAL; // fallthrough ALL otherwise.
                            else return org.apache.logging.log4j.Level.ALL;
                    }
                }
        }
    }

    /** Returns the Java level for the given Log4J level. */
    private static Level toJavaLevel(final org.apache.logging.log4j.Level level) {
        final int n = level.intLevel();
        if (n == StandardLevel.OFF.intLevel()) {
            return Level.OFF;
        } else if (n <= StandardLevel.FATAL.intLevel()) {
            return Level.SEVERE;
        } else if (n <= StandardLevel.ERROR.intLevel()) {
            return Level.SEVERE;
        } else if (n <= StandardLevel.WARN.intLevel()) {
            return Level.WARNING;
        } else if (n <= StandardLevel.INFO.intLevel()) {
            return Level.INFO;
        } else if (n < StandardLevel.DEBUG.intLevel()) {
            return Level.CONFIG;
        } else if (n == StandardLevel.DEBUG.intLevel()) {
            return Level.FINE;
        } else if (n <= StandardLevel.TRACE.intLevel()) {
            return Level.FINER;
        } else if (n <= 1000) {
            return Level.FINEST;
        } else {
            return Level.ALL;
        }
    }

    /** Set the level for this logger. */
    @Override
    public void setLevel(final Level level) {
        // not readily supported by log4j2 api as a design choice
        // (prefering to handle as configuration activity)
        org.apache.logging.log4j.core.config.Configurator.setLevel(logger, toLog4JLevel(level));
    }

    /** Returns the level for this logger. */
    @Override
    public Level getLevel() {
        return toJavaLevel(logger.getLevel());
    }

    /** Returns {@code true} if the specified level is loggable. */
    @Override
    public boolean isLoggable(final Level level) {
        return logger.isEnabled(toLog4JLevel(level));
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
        logger.info(message);
    }

    @Override
    public void fine(String message) {
        logger.debug(message);
    }

    @Override
    public void finer(String message) {
        logger.debug(message);
    }

    @Override
    public void finest(String message) {
        logger.trace(message);
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
