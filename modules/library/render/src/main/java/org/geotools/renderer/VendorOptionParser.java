/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer.DisplacementMode;

/**
 * Helper class that provides utility methods to extract and parse elements from the vendor options
 * map, some generic, other geared to specific contents
 *
 * @author Andrea Aime - GeoSolutions
 */
public class VendorOptionParser {

    static final Pattern SPACES = Pattern.compile("\\s+");

    /**
     * Extracts a enumeration from the vendor options map, returns it if found, returns the default
     * value if not
     */
    public <T extends Enum<T>> Enum<T> getEnumOption(
            Symbolizer symbolizer, String optionName, Enum<T> defaultValue) {
        String value = getOption(symbolizer, optionName);

        if (value == null) return defaultValue;
        try {
            Enum<T> enumValue = Enum.valueOf(defaultValue.getDeclaringClass(), value.toUpperCase());
            return enumValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /** Null safe options retrival */
    private String getOption(Symbolizer symbolizer, String optionName) {
        if (symbolizer == null) {
            return null;
        }
        Map<String, String> options = symbolizer.getOptions();
        if (options == null) {
            return null;
        } else {
            return options.get(optionName);
        }
    }

    /**
     * Extracts a integer from the vendor options map, returns it if found, returns the default
     * value if not
     */
    public int getIntOption(Symbolizer symbolizer, String optionName, int defaultValue) {
        String value = getOption(symbolizer, optionName);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Extracts a double from the vendor options map, returns it if found, returns the default value
     * if not
     */
    public double getDoubleOption(Symbolizer symbolizer, String optionName, double defaultValue) {
        String value = getOption(symbolizer, optionName);
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Extracts a boolean from the vendor options map, returns it if found, returns the default
     * value if not
     */
    public boolean getBooleanOption(
            Symbolizer symbolizer, String optionName, boolean defaultValue) {
        String value = getOption(symbolizer, optionName);
        if (value == null) {
            return defaultValue;
        }
        return value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("1");
    }

    /**
     * Returns a CSS margin from the options map. The result always has 4 components, in
     * top,right,bottom,left order. The syntax can follow the CSS shorthand,
     * http://www.w3schools.com/css/css_margin.asp
     */
    public int[] getGraphicMargin(Symbolizer symbolizer, String optionName) {
        String value = getOption(symbolizer, optionName);
        if (value == null) {
            return null;
        } else {
            String[] values = SPACES.split(value.trim());
            if (values.length == 0) {
                return null;
            } else if (values.length > 4) {
                throw new IllegalArgumentException(
                        "The graphic margin is to be specified with 1, 2 or 4 values");
            }
            int[] parsed = new int[values.length];
            boolean allZeroMargin = false;
            for (int i = 0; i < parsed.length; i++) {
                int margin = Integer.parseInt(values[i]);
                allZeroMargin = allZeroMargin && margin == 0;
                parsed[i] = margin;
            }
            // if not a single positive margin
            if (allZeroMargin) {
                return null;
            } else if (parsed.length == 4) {
                return parsed;
            } else if (parsed.length == 3) {
                return new int[] {parsed[0], parsed[1], parsed[2], parsed[1]};
            } else if (parsed.length == 2) {
                return new int[] {parsed[0], parsed[1], parsed[0], parsed[1]};
            } else {
                return new int[] {parsed[0], parsed[0], parsed[0], parsed[0]};
            }
        }
    }

    /**
     * Returns an array of int in the range [0, 360) which corresponds to the possible displacement
     * angles.
     *
     * @param optionName expected a String with DisplacementMode enum values comma separated
     */
    public int[] getDisplacementAngles(Symbolizer symbolizer, String optionName) {
        String value = getOption(symbolizer, optionName);
        if (value == null) {
            return null;
        } else {
            String[] values = value.trim().split(",");
            if (values.length == 0) {
                return null;
            }
            int[] parsed = new int[values.length];
            for (int i = 0; i < parsed.length; i++) {
                try {
                    DisplacementMode mode =
                            DisplacementMode.valueOf(values[i].trim().toUpperCase());
                    parsed[i] = mode.getAngle();
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            values[i]
                                    + " is not legal. The values of displacement mode must be one of the following: "
                                    + Arrays.toString(DisplacementMode.values()));
                }
            }
            return parsed;
        }
    }
}
