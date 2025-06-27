/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Convert String to CRS classes.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 12.0
 * @version 11.0
 */
public class CRSConverterFactory implements ConverterFactory {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CRSConverterFactory.class);

    private static final CRSConverter STRING_TO_CRS = new CRSConverter();

    /**
     * Delegates to {@link ConvertUtils#lookup(java.lang.Class)} to create a converter instance.
     *
     * @see ConverterFactory#createConverter(Class, Class, Hints).
     */
    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (source == null || target == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("CRSConverterFactory can be applied from Strings to CRS only.");
            }
            return null; // only do strings
        }
        if (CoordinateReferenceSystem.class.isAssignableFrom(target) && String.class.isAssignableFrom(source)) {
            return STRING_TO_CRS;
        }

        if (CoordinateReferenceSystem.class.isAssignableFrom(source) && String.class.isAssignableFrom(target)) {
            return STRING_TO_CRS;
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("CRSConverterFactory can be applied from Strings to CRS  only.");
        }

        return null;
    }

    // some additional converters
    /** converts a string to an {@link CoordinateReferenceSystem} Object. */
    static class CRSConverter implements Converter {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            // checks
            if (source == null || target == null) {
                return null;
            }

            // STRING TO CRS
            if (source instanceof String && CoordinateReferenceSystem.class.isAssignableFrom(target)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("CRSConverter: Converting object of class "
                            + source.getClass().getCanonicalName()
                            + " to "
                            + target.getCanonicalName());
                }
                // convert
                String input = (String) source;
                input = input.trim();

                // try the decode first for EPSG:XXX
                try {
                    return (T) CRS.decode(input);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    }
                }

                // try the wkt afterwards
                try {
                    return (T) CRS.parseWKT(input);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    }
                }
            }

            // CRS TO STRING
            if (source instanceof CoordinateReferenceSystem && String.class.isAssignableFrom(target)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("CRSConverter: Converting object of class "
                            + source.getClass().getCanonicalName()
                            + " to "
                            + target.getCanonicalName());
                }
                try {
                    return (T) ((CoordinateReferenceSystem) source).toWKT();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    }
                }
            }

            // failed
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("CRSConverter: Unable to convert object of class "
                        + source.getClass().getCanonicalName()
                        + " to "
                        + target.getCanonicalName());
            }
            return null;
        }
    }
}
