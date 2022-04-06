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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A factory for loggers that redirect all Java logging events to <A HREF="">logback framework</A>
 * (using the <AHREF="https://www.slf4j.org/">sl4j</A> API).
 *
 * <p>The sl4j api is used to all interactions excepting {@link LogbackLogger#setLevel(Level)}
 * (which is primarily used during test cases).
 *
 * @since 27
 * @version $Id$
 * @author Jody Garnett (GeoCat)
 */
public class LogbackLoggerFactory extends LoggerFactory<org.slf4j.Logger> {
    /** The unique instance of this factory. */
    private static LogbackLoggerFactory factory;

    /**
     * Constructs a default factory.
     *
     * @throws NoClassDefFoundError if sl4j's {@code org.slf4j.Logger} class was not found on the
     *     classpath.
     */
    protected LogbackLoggerFactory() throws NoClassDefFoundError {
        super(org.slf4j.Logger.class);
    }

    /**
     * Returns the unique instance of this factory.
     *
     * @throws NoClassDefFoundError if sl4j's {@code org.slf4j.Logger} class was not found on the
     *     classpath.
     */
    public static synchronized LogbackLoggerFactory getInstance() throws NoClassDefFoundError {
        if (factory == null) {
            factory = new LogbackLoggerFactory();
        }
        return factory;
    }

    /**
     * Returns the implementation to use for the logger of the specified name, or {@code null} if
     * the logger would delegate to Java logging anyway.
     */
    @Override
    protected org.slf4j.Logger getImplementation(final String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }

    /** Wraps the specified {@linkplain #getImplementation implementation} in a Java logger. */
    @Override
    protected LogbackLogger wrap(String name, org.slf4j.Logger implementation) {
        return new LogbackLogger(name, implementation);
    }

    /**
     * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger,
     * or {@code null} if none.
     */
    @Override
    protected org.slf4j.Logger unwrap(final Logger logger) {
        if (logger instanceof LogbackLogger) {
            return ((LogbackLogger) logger).logger;
        }
        return null;
    }

    @Override
    public String lookupConfiguration() {
        try {
            LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
            ConfigurationWatchList configurationWatchList =
                    ConfigurationWatchListUtil.getConfigurationWatchList(context);
            return configurationWatchList.getMainURL().toString();
        } catch (Exception unknown) {
            return "unknown";
        }
    }
}
