/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Converter factory for converting SQL Large Objects.
 *
 * <p>The factory provides conversions from java.sql.Blob to byte[] and from java.sql.Clob to
 * java.lang.String.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class LobConverterFactory implements ConverterFactory {

    private static final Logger LOGGER = Logging.getLogger(LobConverterFactory.class);

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (byte[].class.equals(target) && Blob.class.isAssignableFrom(source)) {
            return new BlobConverter();
        }
        if (String.class.equals(target) && Clob.class.isAssignableFrom(source)) {
            return new ClobConverter();
        }
        return null;
    }

    private static class BlobConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) {
                throw new IllegalArgumentException("source must not be null");
            }
            Blob blob = (Blob) source;
            byte[] data;
            try {
                // We try to fetch the full blob in one go, but that might not be supported by
                // the JDBC driver, e.g. if the size of the blob is not known yet. In such a
                // case we fallback to fetching the blob chunkwise.
                data = fullFetch(blob);
            } catch (SQLFeatureNotSupportedException e) {
                data = chunkwiseFetch(blob);
            }
            return target.cast(data);
        }

        private byte[] fullFetch(Blob blob) throws SQLException {
            long length = blob.length();
            if (length == 0) {
                return new byte[0];
            }
            if (length > Integer.MAX_VALUE) {
                LOGGER.fine("Blob has size " + length + ", which is too large for a byte array");
                return null;
            }
            int clength = (int) length;
            return blob.getBytes(1, clength);
        }

        private byte[] chunkwiseFetch(Blob blob) throws SQLException, IOException {
            try (InputStream is = blob.getBinaryStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                while (true) {
                    int bytesRead = is.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    baos.write(buffer, 0, bytesRead);
                }
                return baos.toByteArray();
            }
        }
    }

    private static class ClobConverter implements Converter {

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) {
                throw new IllegalArgumentException("source must not be null");
            }
            Clob clob = (Clob) source;
            String s;
            try {
                // We try to fetch the full clob in one go, but that might not be supported by
                // the JDBC driver, e.g. if the size of the clob is not known yet. In such a
                // case we fallback to fetching the clob chunkwise.
                s = fullFetch(clob);
            } catch (SQLFeatureNotSupportedException e) {
                s = chunkwiseFetch(clob);
            }
            return target.cast(s);
        }

        private String fullFetch(Clob clob) throws SQLException {
            long length = clob.length();
            if (length == 0) {
                return "";
            }
            if (length > Integer.MAX_VALUE) {
                LOGGER.fine("Clob has size " + length + ", which is too large for a string");
                return null;
            }
            int clength = (int) length;
            return clob.getSubString(1, clength);
        }

        private String chunkwiseFetch(Clob clob) throws SQLException, IOException {
            try (Reader reader = clob.getCharacterStream()) {
                StringWriter writer = new StringWriter();
                char[] buffer = new char[4096];
                while (true) {
                    int charsRead = reader.read(buffer);
                    if (charsRead == -1) {
                        break;
                    }
                    writer.write(buffer, 0, charsRead);
                }
                return writer.toString();
            }
        }
    }
}
