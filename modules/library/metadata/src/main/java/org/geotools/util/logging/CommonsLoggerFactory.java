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

import java.net.URL;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A factory for loggers that redirect all Java logging events to the Apache's <A
 * HREF="https://commons.apache.org/proper/commons-logging/">Commons-logging</A> framework.
 *
 * <p>Out of the box commons logging delegates to the java util logging framework (using the class
 * org.apache.commons.logging.impl.Jdk14Logger). If this factory detects Commons logging
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class CommonsLoggerFactory extends LoggerFactory<Log> {
    /** The unique instance of this factory. */
    private static CommonsLoggerFactory factory;

    /**
     * Constructs a default factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    protected CommonsLoggerFactory() throws NoClassDefFoundError {
        super(Log.class);
    }

    /**
     * Returns the unique instance of this factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    public static synchronized CommonsLoggerFactory getInstance() throws NoClassDefFoundError {
        if (factory == null) {
            factory = new CommonsLoggerFactory();
        }
        return factory;
    }

    /**
     * Returns the implementation to use for the logger of the specified name, or {@code null} if
     * the logger would delegate to Java logging anyway.
     */
    @Override
    protected Log getImplementation(final String name) {
        final Log log = LogFactory.getLog(name);
        if (log != null
                && log.getClass().getName().equals("org.apache.commons.logging.impl.Jdk14Logger")) {
            return null;
        }
        return log;
    }

    /** Wraps the specified {@linkplain #getImplementation implementation} in a Java logger. */
    @Override
    protected Logger wrap(String name, Log implementation) {
        return new CommonsLogger(name, implementation);
    }

    /**
     * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger,
     * or {@code null} if none.
     */
    @Override
    protected Log unwrap(final Logger logger) {
        if (logger instanceof CommonsLogger) {
            return ((CommonsLogger) logger).logger;
        }
        return null;
    }

    @Override
    public String lookupConfiguration() {
        URL url = getClass().getClassLoader().getResource("commons-logging.properties");
        if (url != null) {
            return url.getFile();
        } else {
            return "(undetermined)";
        }
    }
}
