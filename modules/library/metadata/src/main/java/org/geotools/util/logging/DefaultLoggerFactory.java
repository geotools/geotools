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

import java.util.logging.Logger;

/**
 * Default LoggerFactory using java util logging framework directly.
 *
 * <p>This is a no-op factory placeholder delegating directly to the java util logging framework. It is used by
 * {@link Logging} as a default (rather than null {@link null}).
 *
 * @author Jody Garnett (GeoCat)
 */
public class DefaultLoggerFactory extends LoggerFactory<Logger> {
    /** The unique instance of this factory. */
    private static DefaultLoggerFactory factory;

    /** Constructs a default factory. */
    protected DefaultLoggerFactory() {
        super(Logger.class);
    }

    /** Returns the unique instance of this factory. */
    public static synchronized DefaultLoggerFactory getInstance() {
        if (factory == null) {
            factory = new DefaultLoggerFactory();
        }
        return factory;
    }

    /**
     * Returns the implementation to use for the logger of the specified name, or {@code null} if the logger would
     * delegate to Java logging anyway.
     */
    @Override
    protected Logger getImplementation(final String name) {
        return Logger.getLogger(name);
    }

    /** Wraps the specified {@linkplain #getImplementation implementation} in a Java logger. */
    @Override
    protected Logger wrap(String name, Logger implementation) {
        return implementation;
    }

    /**
     * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger, or {@code null} if
     * none.
     */
    @Override
    protected Logger unwrap(final Logger logger) {
        return logger;
    }

    @Override
    public String lookupConfiguration() {
        String configClass = System.getProperty("java.util.logging.config.class");
        String configFile = System.getProperty("java.util.logging.config.file");
        String javaHome = System.getProperty("java.home");
        if (configClass != null) {
            return configClass;
        } else if (configFile != null) {
            return configFile;
        } else if (javaHome != null) {
            return javaHome + "/lib/logging.properties";
        } else {
            return "java.util.logging";
        }
    }
}
