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

package org.geotools.data.h2;

import java.io.Reader;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.h2.jdbc.JdbcClob;

/**
 * Converts a H2 JdbcClob to a String
 *
 * @author Andrea Aime - GeoSolutions
 */
public class H2ClobConverterFactory implements ConverterFactory {

    H2ClobConverter converter = new H2ClobConverter();

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        // can only convert towards String
        if (!String.class.equals(target)) return null;

        // can only deal with JdbcClob
        if (!JdbcClob.class.isAssignableFrom(source)) return null;

        // converter is thread safe, so cache and return just one
        return converter;
    }

    static class H2ClobConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            JdbcClob clob = (JdbcClob) source;

            StringBuilder sb = new StringBuilder();
            char[] cbuf = new char[4096];
            int read;

            try (Reader r = clob.getCharacterStream()) {
                while ((read = r.read(cbuf)) > 0) {
                    sb.append(cbuf, 0, read);
                }

                return target.cast(sb.toString());
            }
        }
    }
}
