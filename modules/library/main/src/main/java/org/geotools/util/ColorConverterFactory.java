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
package org.geotools.util;

import java.awt.Color;

import org.geotools.factory.Hints;

/**
 * ConverterFactory for handling color conversions.
 * <p>
 * Supported conversions:
 * <ul>
 * 	<li>"#FF0000" (String) -> Color.RED
 * 	<li>"false" -> Boolean.FALSE
 * 	<li>0xFF0000FF (Integer) -> RED with Alpha
 * </ul>
 * </p>
 * <p>
 * This code was previously part of the SLD utility class, it is being made
 * available as part of the Converters framework to allow for broader use.
 * </p>
 * @author Jody Garnett (Refractions Research)
 * @since 2.5
 *
 *
 *
 * @source $URL$
 */
public class ColorConverterFactory implements ConverterFactory {
    
    /**
     * Uses {@link Color#decode(String)} to convert String to Color.
     */
    public static Converter CONVERT_STRING = new Converter() {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String rgba = (String) source;
            try {
                return target.cast( Color.decode(rgba) );
            } catch (NumberFormatException badRGB) {
                // unavailable
                return null;
            }
        }
    };
    
    /**
     * Converts provided integer to color, taking care to allow rgb and rgba support.
     */
    public static Converter CONVERT_NUMBER = new Converter() {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            Number number = (Number) source;
            // is it an integral number, and small enough to be an integer?
            if (((int) number.doubleValue()) == number.doubleValue() && number.doubleValue() < Integer.MAX_VALUE) {
                int rgba = number.intValue();
                int alpha = 0xff000000 & rgba;
                return target.cast(new Color(rgba, alpha != 0));
            } else {
                return null;
            }
        }
    };
    
    /**
     * Converts color to hex representation.
     */
    private Converter CONVERT_COLOR_TO_STRING = new Converter() {
        
        public <T> T convert(Object source, Class<T> target) throws Exception {
            Color color = (Color) source;
            
            String redCode = Integer.toHexString(color.getRed());
            String greenCode = Integer.toHexString(color.getGreen());
            String blueCode = Integer.toHexString(color.getBlue());

            if (redCode.length() == 1) {
                redCode = "0" + redCode;
            }

            if (greenCode.length() == 1) {
                greenCode = "0" + greenCode;
            }

            if (blueCode.length() == 1) {
                blueCode = "0" + blueCode;
            }
            String hex = ("#" + redCode + greenCode + blueCode).toUpperCase();
            return target.cast( hex );
        }
    };

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (target.equals(Color.class)) {
            // string to color
            if (source.equals(String.class)) {
                return CONVERT_STRING;
            }
            // can we convert the thing to a Integer with a safe conversion?
            if (Number.class.isAssignableFrom(source)) {
                return CONVERT_NUMBER;
            }
        } else if (target.equals(String.class) && source.equals(Color.class)) {
            return CONVERT_COLOR_TO_STRING;
        }
        return null;
    }

}