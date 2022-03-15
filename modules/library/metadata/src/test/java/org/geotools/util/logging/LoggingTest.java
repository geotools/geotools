/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2022, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 * Tests the {@link Logging} class.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class LoggingTest {
    /** Checks {@link Logging#GEOTOOLS}. */
    @Test
    public void testGeotools() {
        assertEquals("", Logging.ALL.name);
        assertEquals("org.geotools", Logging.GEOTOOLS.name);
        assertEquals(0, Logging.GEOTOOLS.getChildren().length);
        Logging[] children = Logging.ALL.getChildren();
        assertEquals(1, children.length);
        assertEquals("org", children[0].name);
        assertSame(children[0], Logging.getLogging("org"));
        children = children[0].getChildren();
        assertEquals(1, children.length);
        assertSame(Logging.GEOTOOLS, children[0]);
        assertSame(Logging.ALL, Logging.getLogging(""));
        assertSame(Logging.GEOTOOLS, Logging.getLogging("org.geotools"));
    }

    /** Tests the redirection to Commons-logging. */
    @Test
    public void testCommonsLogging() throws ClassNotFoundException {
        try {
            Logging.GEOTOOLS.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");
            Logger logger = Logging.getLogger("org.geotools");
            /*
             * 'logger' would be an instanceof from the Java logging framework if Log4J wasn't in
             * the classpath.  But because it is (with "provided" scope), Commons-logging chooses
             * it instead of the Java logging one.
             */
            // assertTrue(logger instanceof CommonsLogger);
            /*
             * Tests level setting, ending with OFF in order to avoid
             * polluting the standard output stream with this test.
             */
            final org.apache.logging.log4j.Logger log4j =
                    org.apache.logging.log4j.LogManager.getLogger("org.geotools");
            final org.apache.logging.log4j.Level oldLevel = log4j.getLevel();

            org.apache.logging.log4j.core.config.Configurator.setLevel(
                    log4j, org.apache.logging.log4j.Level.WARN);

            // logger.setLevel(Level.WARNING);
            assertSame("java level mapped", Level.WARNING, logger.getLevel());
            assertTrue(logger.isLoggable(Level.WARNING));
            assertTrue(logger.isLoggable(Level.SEVERE));
            assertFalse(logger.isLoggable(Level.CONFIG));

            org.apache.logging.log4j.core.config.Configurator.setLevel(
                    log4j, org.apache.logging.log4j.Level.DEBUG);
            //          assertEquals(Level.FINER, logger.getLevel());
            // Commented-out because in older version of commons-logging, "trace" maps to "debug".
            assertTrue(logger.isLoggable(Level.FINE));
            assertTrue(logger.isLoggable(Level.SEVERE));

            org.apache.logging.log4j.core.config.Configurator.setLevel(
                    log4j, org.apache.logging.log4j.Level.OFF);
            assertSame("java level mapped", Level.OFF, logger.getLevel());

            logger.finest("Message to Commons-logging at FINEST level.");
            logger.finer("Message to Commons-logging at FINER level.");
            logger.fine("Message to Commons-logging at FINE level.");
            logger.config("Message to Commons-logging at CONFIG level.");
            logger.info("Message to Commons-logging at INFO level.");
            logger.warning("Message to Commons-logging at WARNING level.");
            logger.severe("Message to Commons-logging at SEVERE level.");
            org.apache.logging.log4j.core.config.Configurator.setLevel(log4j, oldLevel);
        } finally {
            Logging.GEOTOOLS.setLoggerFactory((String) null);
            assertEquals(Logger.class, Logging.getLogger(LoggingTest.class).getClass());
        }
    }

    /** Tests the redirection to Log4J classes (provided by reload4j) */
    @Test
    public void testLog4J() throws ClassNotFoundException {
        try {
            Logging.GEOTOOLS.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");
            Logger logger = Logging.getLogger("org.geotools");
            assertTrue(logger instanceof Log4JLogger);
            Log4JLogger logger4j = (Log4JLogger) logger;
            /*
             * Tests level setting, ending with OFF in order to avoid
             * polluting the standard output stream with this test.
             */
            final Level oldLevel = logger.getLevel();

            logger.setLevel(Level.WARNING);
            assertSame("java level mapped", Level.WARNING, logger.getLevel());
            assertSame(
                    "log4j level mapped", org.apache.log4j.Level.WARN, logger4j.logger.getLevel());
            assertTrue(logger.isLoggable(Level.WARNING));
            assertFalse(logger.isLoggable(Level.INFO));

            logger.setLevel(Level.CONFIG);
            assertSame("java level mapped", Level.CONFIG, logger.getLevel());
            assertSame("log4j level mapped", Log4JLogger.CONFIG, logger4j.logger.getLevel());
            assertTrue(logger.isLoggable(Level.CONFIG));
            assertFalse(logger.isLoggable(Level.FINE));

            logger.setLevel(Level.FINER);
            assertSame("java level mapped", Level.FINER, logger.getLevel());
            assertSame(
                    "log4j level mapped", org.apache.log4j.Level.TRACE, logger4j.logger.getLevel());
            assertTrue(logger.isLoggable(Level.FINER));
            assertTrue(logger.isLoggable(Level.SEVERE));

            logger.setLevel(Level.OFF);
            assertSame("java level mapped", Level.OFF, logger.getLevel());
            assertSame(
                    "log4j level mapped", org.apache.log4j.Level.OFF, logger4j.logger.getLevel());

            logger.finer("Message to Log4J at FINER level.");
            logger.fine("Message to Log4J at FINE level.");
            logger.config("Message to Log4J at CONFIG level.");
            logger.info("Message to Log4J at INFO level.");
            logger.warning("Message to Log4J at WARNING level.");
            logger.severe("Message to Log4J at SEVERE level.");
            logger.setLevel(oldLevel);
        } finally {
            Logging.GEOTOOLS.setLoggerFactory((String) null);
            assertEquals(Logger.class, Logging.getLogger("org.geotools").getClass());
        }
    }

    /** Tests the redirection to Log4J2. */
    @Test
    public void testLog4J2() throws ClassNotFoundException {
        try {
            Logging.GEOTOOLS.setLoggerFactory("org.geotools.util.logging.Log4J2LoggerFactory");
            Logger logger = Logging.getLogger("org.geotools");
            assertTrue(logger instanceof Log4J2Logger);
            Log4J2Logger logger4j = (Log4J2Logger) logger;
            /*
             * Tests level setting, ending with OFF in order to avoid
             * polluting the standard output stream with this test.
             */
            final Level oldLevel = logger.getLevel();

            logger.setLevel(Level.WARNING);
            assertSame("java level mapped", Level.WARNING, logger.getLevel());
            assertSame(
                    "log4j level mapped",
                    org.apache.logging.log4j.Level.WARN,
                    logger4j.logger.getLevel());
            assertTrue(logger.isLoggable(Level.WARNING));
            assertTrue(logger.isLoggable(Level.SEVERE));
            assertFalse(logger.isLoggable(Level.CONFIG));

            logger.setLevel(Level.CONFIG);
            assertSame("java level mapped", Level.CONFIG, logger.getLevel());
            assertSame("log4j level mapped", Log4J2Logger.CONFIG, logger4j.logger.getLevel());
            assertTrue(logger.isLoggable(Level.INFO));
            assertTrue(logger.isLoggable(Level.CONFIG));
            assertFalse(logger.isLoggable(Level.FINE));

            logger.setLevel(Level.FINER);
            assertSame(Level.FINER, logger.getLevel());
            assertSame(
                    "log4j level mapped",
                    org.apache.logging.log4j.Level.TRACE,
                    logger4j.logger.getLevel());
            assertTrue(logger.isLoggable(Level.FINER));
            assertTrue(logger.isLoggable(Level.SEVERE));

            logger.setLevel(Level.OFF);
            assertSame("java level mapped", Level.OFF, logger.getLevel());
            assertSame(
                    "log4j level mapped",
                    org.apache.logging.log4j.Level.OFF,
                    logger4j.logger.getLevel());

            logger.finer("Message to Log4J at FINER level.");
            logger.fine("Message to Log4J at FINE level.");
            logger.config("Message to Log4J at CONFIG level.");
            logger.info("Message to Log4J at INFO level.");
            logger.warning("Message to Log4J at WARNING level.");
            logger.severe("Message to Log4J at SEVERE level.");
            logger.setLevel(oldLevel);
        } finally {
            Logging.GEOTOOLS.setLoggerFactory((String) null);
            assertEquals(Logger.class, Logging.getLogger("org.geotools").getClass());
        }
    }
}
