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
 * A factory for loggers that redirect all Java logging events to the Apache's
 * <A HREF="http://logging.apache.org/log4j">Log4J</A> framework.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class Log4JLoggerFactory extends LoggerFactory<org.apache.log4j.Logger> {
    /**
     * The unique instance of this factory.
     */
    private static Log4JLoggerFactory factory;

    /**
     * Constructs a default factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    protected Log4JLoggerFactory() throws NoClassDefFoundError {
        super(org.apache.log4j.Logger.class);
    }

    /**
     * Returns the unique instance of this factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    public static synchronized Log4JLoggerFactory getInstance() throws NoClassDefFoundError {
        if (factory == null) {
            factory = new Log4JLoggerFactory();
        }
        return factory;
    }

    /**
     * Returns the implementation to use for the logger of the specified name,
     * or {@code null} if the logger would delegates to Java logging anyway.
     */
    protected org.apache.log4j.Logger getImplementation(final String name) {
        return org.apache.log4j.Logger.getLogger(name);
    }

    /**
     * Wraps the specified {@linkplain #getImplementation implementation} in a Java logger.
     */
    protected Logger wrap(String name, org.apache.log4j.Logger implementation) {
        return new Log4JLogger(name, implementation);
    }

    /**
     * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger,
     * or {@code null} if none.
     */
    protected org.apache.log4j.Logger unwrap(final Logger logger) {
        if (logger instanceof Log4JLogger) {
            return ((Log4JLogger) logger).logger;
        }
        return null;
    }
}
