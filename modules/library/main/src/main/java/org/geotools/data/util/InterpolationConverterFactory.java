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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBicubic2;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Convert String to Interpolation classes.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 12.0
 * @version 11.0
 */
public class InterpolationConverterFactory implements ConverterFactory {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(InterpolationConverterFactory.class);

    private static final InterpolationConverter THE_INTERPOLATION_CONVERTER = new InterpolationConverter();

    /**
     * Delegates to {@link ConvertUtils#lookup(java.lang.Class)} to create a converter instance.
     *
     * @see ConverterFactory#createConverter(Class, Class, Hints).
     */
    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (source == null || target == null || !source.equals(String.class)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("InterpolationConverterFactory can be applied from Strings to Interpolation only.");
            }
            return null; // only do strings
        }
        if (Interpolation.class.isAssignableFrom(target)) {
            return THE_INTERPOLATION_CONVERTER;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("InterpolationConverterFactory can be applied from Strings to Interpolation  only.");
            }
        }
        return null;
    }

    // some additional converters
    /** converts a string to an Interpolation Object. */
    static class InterpolationConverter implements Converter {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            // checks
            if (source == null) {
                return null;
            }
            if (target == null || !Interpolation.class.isAssignableFrom(target)) {
                return null;
            }

            // convert
            String input = (String) source;
            input = input.trim();

            final int idx = input.indexOf('(');
            // get the interpolation key
            final String key =
                    idx == -1 ? input.toUpperCase() : input.substring(0, idx).toUpperCase();

            // get a parser
            InterpolationParser parser = InterpolationParser.valueOf(key);
            if (parser == null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("InterpolationConverterFactory can be applied to Strings like interpolation????(XXX). "
                            + source
                            + " is invalid!");
                }
            }

            // get the interpolation
            Interpolation output = parser.parse(input);
            if (output == null && LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Unable to parse " + input);
            }
            return (T) output;
        }
    }

    /** @author Simone Giannecchini, GeoSolutions */
    @SuppressWarnings("ImmutableEnumChecker")
    enum InterpolationParser {
        INTERPOLATIONNEAREST {

            /** INTERPOLATION_NEAREST */
            private final InterpolationNearest INTERPOLATION_NEAREST = new InterpolationNearest();

            @Override
            Interpolation parse(String interpolationString) {
                if (interpolationString.equals("InterpolationNearest")) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Parsed InterpolationNearest");
                    }
                    return INTERPOLATION_NEAREST;
                }
                // unable to parse
                return null;
            }
        },
        INTERPOLATIONBILINEAR {

            /** INTERPOLATION_BILINEAR */
            private final InterpolationBilinear INTERPOLATION_BILINEAR = new InterpolationBilinear();

            private final Pattern INTERPOLATION_BILINEAR_PATTERN_MATCH =
                    Pattern.compile("InterpolationBilinear\\(\\d+\\)");

            private final Pattern INTERPOLATION_BILINEAR_PATTERN_EXTRACT = Pattern.compile("\\d+");

            @Override
            Interpolation parse(String interpolationString) {

                // most common case, simple bilinear
                if (interpolationString.equals("InterpolationBilinear")) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Parsing InterpolationBilinear=" + interpolationString);
                    }
                    return INTERPOLATION_BILINEAR;
                }

                // more complex cases

                // match the string
                Matcher matcher = INTERPOLATION_BILINEAR_PATTERN_MATCH.matcher(interpolationString);
                if (matcher.matches()) {
                    // extract subsample bits
                    matcher = INTERPOLATION_BILINEAR_PATTERN_EXTRACT.matcher(interpolationString);
                    matcher.matches();
                    if (matcher.find()) {
                        // get the match and parse it
                        final String subsBitsString = matcher.group();
                        try {
                            return new InterpolationBilinear(Integer.parseInt(subsBitsString));
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                            }
                        }
                    }
                }
                // unable to parse
                return null;
            }
        },
        INTERPOLATIONBICUBIC {

            private final Pattern INTERPOLATION_BICUBIC_PATTERN_MATCH =
                    Pattern.compile("InterpolationBicubic\\(\\d+\\)");

            private final Pattern INTERPOLATION_BICUBIC_PATTERN_EXTRACT = Pattern.compile("\\d+");

            @Override
            Interpolation parse(String interpolationString) {

                // match the string
                Matcher matcher = INTERPOLATION_BICUBIC_PATTERN_MATCH.matcher(interpolationString);
                if (matcher.matches()) {
                    // extract subsample bits
                    matcher = INTERPOLATION_BICUBIC_PATTERN_EXTRACT.matcher(interpolationString);
                    matcher.matches();
                    if (matcher.find()) {
                        // get the match and parse it
                        final String subsBitsString = matcher.group();
                        try {
                            return new InterpolationBicubic(Integer.parseInt(subsBitsString));
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                            }
                        }
                    }
                }
                // unable to parse
                return null;
            }
        },
        INTERPOLATIONBICUBIC2 {

            private final Pattern INTERPOLATION_BICUBIC2_PATTERN_MATCH =
                    Pattern.compile("InterpolationBicubic2\\(\\d+\\)");

            private final Pattern INTERPOLATION_BICUBIC2_PATTERN_EXTRACT = Pattern.compile("\\d+");

            @Override
            Interpolation parse(String interpolationString) {

                // match the string
                Matcher matcher = INTERPOLATION_BICUBIC2_PATTERN_MATCH.matcher(interpolationString);
                if (matcher.matches()) {
                    // extract subsample bits
                    matcher = INTERPOLATION_BICUBIC2_PATTERN_EXTRACT.matcher(interpolationString);
                    matcher.matches();
                    if (matcher.find()) {
                        // get the match and parse it
                        final String subsBitsString = matcher.group();
                        try {
                            return new InterpolationBicubic2(Integer.parseInt(subsBitsString));
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                            }
                        }
                    }
                }
                // unable to parse
                return null;
            }
        };

        abstract Interpolation parse(String interpolationString);
    }
}
