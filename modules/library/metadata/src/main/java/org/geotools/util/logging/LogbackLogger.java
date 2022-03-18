/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Logger that redirect all Java logging events to <A HREF="">logback framework</A> * (using the
 * <AHREF="https://www.slf4j.org/">sl4j</A> API).
 *
 * <ul>
 *   <li>{@link Level#ALL}: {@link org.slf4j.Level#ALL}
 *   <li>{@link Level#SEVERE}: {@link org.slf4j.Level#ERROR}
 *   <li>{@link Level#WARNING}: {@link org.slf4j.Level#WARN}
 *   <li>{@link Level#INFO}: {@link org.slf4j.Level#INFO}}
 *   <li>{@link Level#CONFIG}: {@link org.slf4j.Level#INFO} with {@link #CONFIG} marker.
 *   <li>{@link Level#FINE}: {@link org.slf4j.Level#DEBUG}
 *   <li>{@link Level#FINER}: {@link org.slf4j.Level#TRACE}
 *   <li>{@link Level#FINEST}: {@link org.slf4j.Level#TRACE} with {@link #FINEST} marker.
 *   <li>{@link Level#OFF}: {@link org.slf4j.Level#OFF}
 * </ul>
 */
public class LogbackLogger extends LoggerAdapter {
    /**
     * Marker used to tag configuration {@link Level#CONFIG} messages, as checked with sl4j {@code
     * logger.isInfoEnabled(CONFIG)}.
     */
    private static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");

    /**
     * Marker used to tag configuration {@link Level#FINEST} messages, as checked with sl4j {@code
     * logger.isInfoEnabled(FINEST)}.
     */
    private static final Marker FINEST = MarkerFactory.getMarker("FINEST");

    public org.slf4j.Logger logger;

    public LogbackLogger(String name, org.slf4j.Logger logger) {
        super(name);
        this.logger = logger;
    }

    /**
     * Sets the level for this logger.
     *
     * <p>Care is taken to use reflection to access logback-classic Level and Logger classes (to
     * avoid hard runtime dependency when only the sl4j api is provided at runtime).
     *
     * <p>Of logback-classic is unavailable level cannot be changed programmatically.
     *
     * @param level Standard logging level used to configure logger
     */
    @Override
    public void setLevel(Level level) {
        String levelName = toLogbackLevelName(level);

        // uses reflection to avoid hard dependency on logback classic
        try {
            Class<?> LogbackLevelClass = Class.forName("ch.qos.logback.classic.Level");
            Class<?> LogbackLoggerCLass = Class.forName("ch.qos.logback.classic.Logger");

            Field logbackLevelField = LogbackLevelClass.getField(levelName);
            Object levelObject = logbackLevelField.get(null);

            Method setLevelMethod = LogbackLoggerCLass.getMethod("setLevel", LogbackLevelClass);
            setLevelMethod.invoke(this.logger, levelObject);
        } catch (ClassNotFoundException
                | NoSuchFieldException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalArgumentException ignore) {
            // Logback-classic Logger.setLevel( Level ) unavailable
        }
    }

    /**
     * Used by {@link #setLevel(Level)} to determine name used by logback classic.
     *
     * @param level Standard logging level
     * @return logback classic level name.
     */
    private static String toLogbackLevelName(final Level level) {
        final int n = level.intValue();
        switch (n / 100) {
            case 10:
                return "ERROR"; // SEVERE
            case 9:
                return "WARN"; // WARNING
            case 8: // INFO
                return "INFO"; // INFO
            case 7:
                return "INFO"; // CONFIG
            case 6: // (not allocated)
            case 5:
                return "DEBUG"; // FINE
            case 4:
                return "TRACE"; // FINER
            case 3:
                return "TRACE"; // FINEST
            case 2: // (not allocated)
            case 1: // (not allocated)
            case 0:
                return "ALL"; // ALL
            default:
                // MAX_VALUE is a special value for Level.OFF. Otherwise and
                // if positive, log to fatal since we are greater than SEVERE.
                switch (n) {
                    case Integer.MIN_VALUE:
                        return "ALL";
                    case Integer.MAX_VALUE:
                        return "OFF";
                    default:
                        if (n >= 0) return "FATAL"; // fallthrough ALL otherwise.
                        else return "ALL";
                }
        }
    }

    @Override
    public Level getLevel() {
        if (logger.isTraceEnabled())
            if (logger.isTraceEnabled(FINEST)) return Level.FINEST;
            else {
                return Level.FINER;
            }
        if (logger.isDebugEnabled()) return Level.FINE;
        if (logger.isInfoEnabled()) {
            if (logger.isInfoEnabled(CONFIG)) return Level.CONFIG;
            else return Level.INFO;
        }
        if (logger.isWarnEnabled()) return Level.WARNING;
        if (logger.isErrorEnabled()) return Level.SEVERE;
        return Level.OFF;
    }

    @Override
    public boolean isLoggable(Level level) {
        return getLevel().intValue() > level.intValue();
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
        logger.info(CONFIG, message);
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
        logger.trace(FINEST, message);
    }

    /**
     * Logs a record at the specified level, passing the provided throwable to slf4j api.</code>.
     */
    @Override
    public void log(final Level level, final String message, final Throwable thrown) {
        final int n = level.intValue();
        switch (n / 100) {
            default:
                {
                    if (n < 0 || n == Integer.MAX_VALUE) break;
                    // MAX_VALUE is a special value for Level.OFF. Otherwise and
                    // if positive, fallthrough since we are greater than SEVERE.
                }
            case 10:
                logger.error(message, thrown);
                break;
            case 9:
                logger.warn(message, thrown);
                break;
            case 8:
                logger.info(message, thrown);
                break;
            case 7:
                logger.info(CONFIG, message, thrown);
                break;
            case 6:
            case 5:
                logger.debug(message, thrown);
                break;
            case 4:
                logger.trace(message, thrown);
                break;
            case 3:
                logger.trace(FINEST, message, thrown);
                break;
            case 2: /* Logging OFF */
            case 1: /* Logging OFF */
            case 0: /* Logging OFF */
                break;
        }
    }
}
