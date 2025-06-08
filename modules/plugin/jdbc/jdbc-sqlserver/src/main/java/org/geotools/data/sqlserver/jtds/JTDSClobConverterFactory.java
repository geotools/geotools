/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.sqlserver.jtds;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

public class JTDSClobConverterFactory implements ConverterFactory {
    private static final Logger LOGGER = Logging.getLogger(JTDSClobConverterFactory.class);
    JTDSDateConverter converter = new JTDSDateConverter();
    static final Class<?> JTDS_CLOB;
    static final Method JTDS_GET_CHARS;
    static final Method JTDS_LENGTH;

    static {
        Class<?> jTDSClobClass = null;
        try {
            jTDSClobClass = Class.forName("net.sourceforge.jtds.jdbc.ClobImpl");
        } catch (ClassNotFoundException e) {
            // ojdbc*.jar not on the path
        }
        if (jTDSClobClass == null) {
            JTDS_CLOB = null;
            JTDS_GET_CHARS = null;
            JTDS_LENGTH = null;
        } else {
            try {
                JTDS_CLOB = jTDSClobClass;
                JTDS_LENGTH = JTDS_CLOB.getMethod("length");
                JTDS_GET_CHARS = JTDS_CLOB.getMethod("getSubString", long.class, int.class);
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize the jtds clob converter", e);
            }
        }
    }

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        // if the jdbc driver is not in the classpath don't bother trying to convert
        if (JTDS_CLOB == null) {
            LOGGER.fine("Failed to find JTDS jar");
            return null;
        }

        // can only convert towards String
        if (!String.class.equals(target)) {
            LOGGER.finest("Target not a string");
            return null;
        }

        // can only deal with oracle specific blob classes
        if (!JTDS_CLOB.isAssignableFrom(source)) {
            LOGGER.finest("Source not a clob");
            return null;
        }

        // converter is thread safe, so cache and return just one
        return converter;
    }

    class JTDSDateConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            int length = ((Long) JTDS_LENGTH.invoke(source)).intValue();
            String buffer = (String) JTDS_GET_CHARS.invoke(source, 1l, length);
            return target.cast(buffer);
        }
    }
}
