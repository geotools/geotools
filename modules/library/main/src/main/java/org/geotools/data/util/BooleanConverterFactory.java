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
package org.geotools.data.util;

import java.math.BigDecimal;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * ConverterFactory for handling boolean conversions.
 *
 * <p>Supported conversions:
 *
 * <ul>
 *   <li>"true" -> Boolean.TRUE
 *   <li>"false" -> Boolean.FALSE
 *   <li>"1" -> Boolean.TRUE
 *   <li>"0" -> Boolean.FALSE
 *   <li>1 -> Boolean.TRUE
 *   <li>0 -> Boolean.FALSE
 * </ul>
 *
 * <p>Supported numeric types:
 *
 * <ul>
 *   <li>Integer
 *   <li>BigDecimal
 * </ul>
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 */
public class BooleanConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {
        if (target.equals(Boolean.class)) {

            // string to boolean
            if (source.equals(String.class)) {
                return new Converter() {

                    @Override
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        if ("true".equals(source) || "1".equals(source)) {
                            return target.cast(Boolean.TRUE);
                        }
                        if ("false".equals(source) || "0".equals(source)) {
                            return target.cast(Boolean.FALSE);
                        }

                        return null;
                    }
                };
            }

            // integer to boolean
            if (source.equals(Integer.class)) {
                return new Converter() {

                    @Override
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        if (Integer.valueOf(1).equals(source)) {
                            return target.cast(Boolean.TRUE);
                        }
                        if (Integer.valueOf(0).equals(source)) {
                            return target.cast(Boolean.FALSE);
                        }

                        return null;
                    }
                };
            }

            // big decimal to boolean
            if (source.equals(BigDecimal.class)) {
                return new Converter() {

                    @Override
                    @SuppressWarnings("BigDecimalEquals")
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        if (BigDecimal.ONE.equals(source)) {
                            return target.cast(Boolean.TRUE);
                        }
                        if (BigDecimal.ZERO.equals(source)) {
                            return target.cast(Boolean.FALSE);
                        }

                        return null;
                    }
                };
            }
        }

        return null;
    }
}
