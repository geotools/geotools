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
package org.geotools.data.hana.converter;

import java.sql.Blob;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converter factory for converting all classes implementing java.sql.Blob to byte[].
 *
 * @author Stefan Uhrig, SAP SE
 */
public class BlobConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (byte[].class.equals(target) && Blob.class.isAssignableFrom(source)) {
            return new Converter() {
                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source == null) {
                        throw new IllegalArgumentException("source must not be null");
                    }
                    Blob blob = (Blob) source;
                    long length = blob.length();
                    if (length == 0) {
                        return target.cast(new byte[0]);
                    }
                    if (length > Integer.MAX_VALUE) {
                        return null;
                    }
                    int clength = (int) length;
                    byte[] data = blob.getBytes(1, clength);
                    return target.cast(data);
                }
            };
        }
        return null;
    }
}
