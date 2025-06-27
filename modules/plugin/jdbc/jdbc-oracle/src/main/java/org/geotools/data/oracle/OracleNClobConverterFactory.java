/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

public class OracleNClobConverterFactory implements ConverterFactory {

    OracleNClobConverter converter = new OracleNClobConverter();
    static final Class<?> ORA_NCLOB;
    static final Method ORA_GET_CHARS;
    static final Method ORA_LENGTH;

    static {
        Class<?> oracleClobClass = null;
        try {
            // note that in 23.x this class is actually deprecated,
            // see https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/NCLOB.html
            oracleClobClass = Class.forName("oracle.sql.NCLOB");
        } catch (ClassNotFoundException e) {
            // ojdbc*.jar not on the path
        }
        if (oracleClobClass == null) {
            ORA_NCLOB = null;
            ORA_GET_CHARS = null;
            ORA_LENGTH = null;
        } else {
            try {
                ORA_NCLOB = oracleClobClass;
                ORA_LENGTH = ORA_NCLOB.getMethod("getLength");
                ORA_GET_CHARS = ORA_NCLOB.getMethod("getChars", long.class, int.class, char[].class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException("Could not initialize the oracle NCLOB converter", e);
            }
        }
    }

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (ORA_NCLOB == null) return null;

        if (!String.class.equals(target)) return null;

        if (!ORA_NCLOB.isAssignableFrom(source)) return null;

        return converter;
    }

    static class OracleNClobConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            int length = ((Long) ORA_LENGTH.invoke(source)).intValue();
            char[] buffer = new char[length];
            ORA_GET_CHARS.invoke(source, 1L, length, buffer);
            return target.cast(new String(buffer));
        }
    }
}
