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

public class LogbackLogger extends LoggerAdapter {
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
            Class LogbackLevelClass = Class.forName("ch.qos.logback.classic.Level");
            Class LogbackLoggerCLass = Class.forName("ch.qos.logback.classic.Logger");

            Field logbackLevelField = LogbackLevelClass.getField(levelName);
            Object levelObject = logbackLevelField.get(null);

            Method setLevelMethod = LogbackLoggerCLass.getMethod("setLevel", LogbackLevelClass);
            setLevelMethod.invoke(this.logger, levelObject);
        } catch (ClassNotFoundException logbackClassicUnavaialble) {
            // logback not available in this environment (so configuration is not available)
        } catch (NoSuchFieldException levelNameUnavailable) {
            System.err.println(
                    logger.getName()
                            + ": Logback-classic Looger.setLevel("
                            + levelName
                            + "): "
                            + levelNameUnavailable.getMessage());
        } catch (IllegalAccessException privateField) {
            System.err.println(
                    logger.getName()
                            + ": Logback-classic Looger.setLevel("
                            + levelName
                            + "): "
                            + privateField.getMessage());
        } catch (NoSuchMethodException setLevelUnavailable) {
            System.err.println(
                    logger.getName()
                            + ": Logback-classic Looger.setLevel("
                            + levelName
                            + "): "
                            + setLevelUnavailable.getMessage());
        } catch (InvocationTargetException setLevelFailed) {
            System.err.println(
                    logger.getName()
                            + ": Logback-classic Looger.setLevel("
                            + levelName
                            + "): "
                            + setLevelFailed.getMessage());
        } catch (IllegalArgumentException illegalLevelParameter) {
            System.err.println(
                    logger.getName()
                            + ": Logback-classic Looger.setLevel("
                            + levelName
                            + "): "
                            + illegalLevelParameter.getMessage());
        }
    }

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
                return "DEBUG"; // CONFIG
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
        if (logger.isTraceEnabled()) return Level.FINEST;
        if (logger.isDebugEnabled()) return Level.FINE;
        if (logger.isInfoEnabled()) return Level.INFO;
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
        logger.error("SEVERE", message);
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
        logger.debug("CONFIG", message);
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
        logger.trace("FINEST", message);
    }
}
