/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.sql.Driver;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;

/**
 * A set of utilities methods related to JDBC (<cite>Java Database Connectivity</cite>).
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @todo This class may be removed when we will be allowed to compile for J2SE 1.6.
 */
public final class JDBC {
    /** Lists of JDBC drivers already loaded. */
    private static final Set<String> DRIVERS = new HashSet<>();

    /** Do not allow instantiation of this class. */
    private JDBC() {}

    /**
     * Attempts to load the specified JDBC driver, if not already done. If this method has already been invoked for the
     * specified driver, then it does nothing and returns {@code null}. Otherwise, it attempts to load the specified
     * driver and returns a log record initialized with a message at the {@link Level#CONFIG CONFIG} level on success,
     * or at the {@link Level#WARNING WARNING} level on failure.
     *
     * @param driver The JDBC driver to load, as a fully qualified Java class name.
     * @return A log message with driver information, or {@code null} if the driver was already loaded.
     * @todo Remember to invoke {@link LogRecord#setLoggerName}.
     */
    public static LogRecord loadDriver(final String driver) {
        LogRecord log = null;
        if (driver != null) {
            synchronized (DRIVERS) {
                if (!DRIVERS.contains(driver)) {
                    try {
                        final Driver d = Class.forName(driver)
                                .asSubclass(Driver.class)
                                .getDeclaredConstructor()
                                .newInstance();
                        log = Loggings.format(
                                Level.CONFIG,
                                LoggingKeys.LOADED_JDBC_DRIVER_$3,
                                driver,
                                d.getMajorVersion(),
                                d.getMinorVersion());
                        DRIVERS.add(driver);
                    } catch (Exception exception) {
                        log = new LogRecord(Level.WARNING, exception.toString());
                    }
                }
            }
        }
        return log;
    }
}
