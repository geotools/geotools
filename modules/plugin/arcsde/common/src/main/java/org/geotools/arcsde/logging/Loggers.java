/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.logging;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Creates a {@link Logger} using the geotools {@code org.geotools.util.logging.Logging} utility
 * class if available, or the standard {@link Logger#getLogger(String)} method otherwise.
 * <p>
 * This Jar may be used without geotools' gt-metadata being in the class path, so try to use the
 * org.geotools.util.logging.Logging.getLogger method reflectively and fall back to plain
 * java.util.logger if that's the case
 * </p>
 * 
 * @author Gabriel Roldan
 * @since 2.6.6
 *
 * @source $URL$
 */
public class Loggers {

    public static Logger getLogger(final String name) {

        Logger logger = null;
        try {
            Class<?> clazz = Class.forName("org.geotools.util.logging.Logging");
            Method method = clazz.getMethod("getLogger", String.class);
            logger = (Logger) method.invoke(null, "org.geotools.arcsde.session");
        } catch (Exception e) {
            logger = Logger.getLogger("org.geotools.arcsde.session");
            logger.info("org.geotools.util.logging.Logging seems not to be in the classpath, "
                    + "acquired Logger through java.util.Logger");
        }
        return logger;
    }
}
