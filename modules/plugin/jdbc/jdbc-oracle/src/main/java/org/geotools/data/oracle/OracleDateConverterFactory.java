/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.oracle;

import java.lang.reflect.Method;
import java.util.Date;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

public class OracleDateConverterFactory implements ConverterFactory {

    OracleDateConverter converter = new OracleDateConverter();
    static final Class<?> ORA_DATE;
    static final Class<?> ORA_TIMESTAMP;
    static final Method ORA_DATE_DVALUE;
    static final Method ORA_DATE_TSVALUE;
    static final Method ORA_TS_DVALUE;
    static final Method ORA_TS_TSVALUE;

    static {
        Class<?> oracleDateClass = null;
        try {
            oracleDateClass = Class.forName("oracle.sql.DATE");
        } catch (ClassNotFoundException e) {
            // ojdbc*.jar not on the path
        }
        if (oracleDateClass == null) {
            ORA_DATE = null;
            ORA_DATE_DVALUE = null;
            ORA_DATE_TSVALUE = null;
            ORA_TIMESTAMP = null;
            ORA_TS_DVALUE = null;
            ORA_TS_TSVALUE = null;
        } else {
            try {
                ORA_DATE = oracleDateClass;
                ORA_DATE_DVALUE = ORA_DATE.getMethod("dateValue");
                ORA_DATE_TSVALUE = ORA_DATE.getMethod("timestampValue");
                ORA_TIMESTAMP = Class.forName("oracle.sql.TIMESTAMP");
                ORA_TS_DVALUE = ORA_TIMESTAMP.getMethod("dateValue");
                ORA_TS_TSVALUE = ORA_TIMESTAMP.getMethod("timestampValue");
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize the oracle date converter", e);
            }
        }
    }

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        // if the jdbc driver is not in the classpath don't bother trying to convert
        if (ORA_DATE == null) return null;

        // can only convert towards java.util.Date && subclasses
        if (!Date.class.isAssignableFrom(target)) return null;

        // can only deal with oracle specific date classes
        if (!ORA_TIMESTAMP.isAssignableFrom(source) && !ORA_DATE.isAssignableFrom(source)) return null;

        // converter is thread safe, so cache and return just one
        return converter;
    }

    static class OracleDateConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (ORA_TIMESTAMP.isInstance(source)) {
                if (java.sql.Date.class.isAssignableFrom(target)) return target.cast(ORA_TS_DVALUE.invoke(source));
                else return target.cast(ORA_TS_TSVALUE.invoke(source));
            } else {
                if (java.sql.Date.class.isAssignableFrom(target)) return target.cast(ORA_DATE_DVALUE.invoke(source));
                else return target.cast(ORA_DATE_TSVALUE.invoke(source));
            }
        }
    }
}
