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
import java.util.Date;
import java.util.logging.Logger;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.logging.Logging;

/**
 *
 *
 * @source $URL$
 */
public class JTDSDateConverterFactory implements ConverterFactory {
    private static final Logger LOGGER = Logging.getLogger(JTDSDateConverterFactory.class);
    JTDSDateConverter converter = new JTDSDateConverter();
    static final Class<?> JTDS_DATE;
    static final Class<?> JTDS_TIMESTAMP;
    static final Method JTDS_DATE_DVALUE;
    static final Method JTDS_DATE_TSVALUE;
    static final Method JTDS_TS_DVALUE;
    static final Method JTDS_TS_TSVALUE;

    static {
        Class<?> jTDSDateClass = null;
        try {
            jTDSDateClass = Class.forName("net.sourceforge.jtds.jdbc.DateTime");
        } catch (ClassNotFoundException e) {
            LOGGER.finest("Couldn't find JTDS jar on classpath");
            // ojdbc*.jar not on the path
        }
        if (jTDSDateClass == null) {
            JTDS_DATE = null;
            JTDS_DATE_DVALUE = null;
            JTDS_DATE_TSVALUE = null;
            JTDS_TIMESTAMP = null;
            JTDS_TS_DVALUE = null;
            JTDS_TS_TSVALUE = null;
        } else {
            try {
                JTDS_DATE = jTDSDateClass;
                JTDS_DATE_DVALUE = JTDS_DATE.getMethod("toDate");
                JTDS_DATE_TSVALUE = JTDS_DATE.getMethod("toTimestamp");
                JTDS_TIMESTAMP = Class.forName("net.sourceforge.jtds.jdbc.DateTime");
                JTDS_TS_DVALUE = JTDS_TIMESTAMP.getMethod("toDate");
                JTDS_TS_TSVALUE = JTDS_TIMESTAMP.getMethod("toTimestamp");
            } catch(Exception e) {
                throw new RuntimeException("Could not initialize the jtds date converter", e);
            }
        }
    }

    @Override
    public Converter createConverter(Class<?> source, Class<?> target,
            Hints hints) {
        // if the jdbc driver is not in the classpath don't bother trying to convert
        if(JTDS_DATE == null) {
            LOGGER.finest("No JTDS jar on classpath");
            return null;
        }

        // can only convert towards java.util.Date && subclasses
        if (!(Date.class.isAssignableFrom(target))) {
            LOGGER.finest("Target is not a Date");
            return null;
        }

        // can only deal with JTDScle specific date classes
        if (!(JTDS_TIMESTAMP.isAssignableFrom(source)) && !(JTDS_DATE.isAssignableFrom(source))) {
            LOGGER.finest("Source is not a date time object");
            return null;
        }

        // converter is thread safe, so cache and return just one
        return converter;
    }

    class JTDSDateConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (JTDS_TIMESTAMP.isInstance(source)) {
                if (java.sql.Date.class.isAssignableFrom(target)) {
                    LOGGER.finest("converting to Date from "+source.toString());
                    return (T) JTDS_TS_DVALUE.invoke(source);
                } else {
                    LOGGER.finest("converting to timestamp from "+source.toString());
                    return (T) JTDS_TS_TSVALUE.invoke(source);
                }
            } else {
                if (java.sql.Date.class.isAssignableFrom(target)) {
                    LOGGER.finest("converting to Date from "+source.toString());
                    return (T) JTDS_DATE_DVALUE.invoke(source);
                } else {
                    LOGGER.finest("converting to timestamp from "+source.toString());
                    return (T) JTDS_DATE_TSVALUE.invoke(source);
                }
            }
        }

    }


}
