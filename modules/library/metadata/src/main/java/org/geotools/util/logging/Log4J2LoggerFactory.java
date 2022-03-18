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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;

/**
 * A factory for loggers that redirect all Java logging events to the Apache's <A
 * HREF="http://logging.apache.org/log4j">Log4J</A> framework.
 *
 * <p>The {@code GeoTools.init()} method will select Log4J2LoggerFactory if log4j api is available
 * on the classpath, however if you have several logging libraries on the classpath call {@code
 * GeoTools.setLoggerFactory(Log4J2LoggerFactory#getInstance())}.
 *
 * @since 27
 * @version $Id$
 * @author Jody Garnett (GeoCat)
 */
public class Log4J2LoggerFactory extends LoggerFactory<org.apache.logging.log4j.Logger> {
    /** The unique instance of this factory. */
    private static Log4J2LoggerFactory factory;

    /**
     * Constructs a default factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    protected Log4J2LoggerFactory() throws NoClassDefFoundError {
        super(org.apache.logging.log4j.Logger.class);
    }

    /**
     * Returns the unique instance of this factory.
     *
     * @throws NoClassDefFoundError if Apache's {@code Log} class was not found on the classpath.
     */
    public static synchronized Log4J2LoggerFactory getInstance() throws NoClassDefFoundError {
        if (factory == null) {
            factory = new Log4J2LoggerFactory();
        }
        return factory;
    }

    /**
     * Returns the implementation to use for the logger of the specified name, or {@code null} if
     * the logger would delegates to Java logging anyway.
     */
    @Override
    protected org.apache.logging.log4j.Logger getImplementation(final String name) {
        return org.apache.logging.log4j.LogManager.getLogger(name);
    }

    /** Wraps the specified {@linkplain #getImplementation implementation} in a Java logger. */
    @Override
    protected Logger wrap(String name, org.apache.logging.log4j.Logger implementation) {
        return new Log4J2Logger(name, implementation);
    }

    /**
     * Returns the {@linkplain #getImplementation implementation} wrapped by the specified logger,
     * or {@code null} if none.
     */
    @Override
    protected org.apache.logging.log4j.Logger unwrap(final Logger logger) {
        if (logger instanceof Log4J2Logger) {
            return ((Log4J2Logger) logger).logger;
        }
        return null;
    }

    /**
     * Indication of Log4J configuration details, often a configuration filename or setting.
     *
     * @return Log4J configuration details, often a filename or setting.
     */
    @Override
    public String lookupConfiguration() {
        try (LoggerContext context = (LoggerContext) LogManager.getContext()) {
            Configuration configuration = context.getConfiguration();
            if (configuration instanceof XmlConfiguration) {
                return ((XmlConfiguration) configuration).getName();
            } else if (configuration instanceof YamlConfiguration) {
                return ((YamlConfiguration) configuration).getName();
            } else if (configuration instanceof JsonConfiguration) {
                return ((JsonConfiguration) configuration).getName();
            } else if (configuration instanceof PropertiesConfiguration) {
                return ((PropertiesConfiguration) configuration).getName();
            } else if (configuration instanceof DefaultConfiguration) {
                return "org.apache.logging.log4j.level="
                        + System.getProperty("org.apache.logging.log4j.level", "ERROR");
            } else if (configuration instanceof BuiltConfiguration) {
                return "built configuration";
            } else if (configuration instanceof NullConfiguration) {
                return "null configuration";
            }
            return null;
        } catch (Exception unknown) {
            return "unknown";
        }
    }
}
