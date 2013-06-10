package org.geotools.renderer;

import java.util.Map;

import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

/**
 * Helper class that provides utility methods to extract and parse elements from the vendor options
 * map, some generic, other geared to specific contents
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class VendorOptionParser {

    /**
     * Extracts a enumeration from the vendor options map, returns it if found, returns the default value if not 
     * 
     * @param symbolizer
     * @param optionName
     * @param defaultValue
     * @return
     */
    public <T extends Enum<T>> Enum<T> getEnumOption(Symbolizer symbolizer, String optionName, Enum<T> defaultValue) {
        String value = getOption(symbolizer, optionName);
        
        if (value == null)
            return defaultValue;
        try {
            Enum<T> enumValue = Enum.valueOf(defaultValue.getDeclaringClass(), value.toUpperCase());
            return enumValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Null safe options retrival
     * @param symbolizer
     * @param optionName
     * @return
     */
    private String getOption(Symbolizer symbolizer, String optionName) {
        Map<String, String> options = symbolizer.getOptions();
        if(options == null) {
            return null;
        } else {
            return options .get(optionName);
        }
    }

    /**
     * Extracts a integer from the vendor options map, returns it if found, returns the default value if not
     * 
     * @param symbolizer
     * @param optionName
     * @param defaultValue
     * @return
     */
    public int getIntOption(Symbolizer symbolizer, String optionName, int defaultValue) {
        String value = getOption(symbolizer, optionName);
        if (value == null)
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Extracts a double from the vendor options map, returns it if found, returns the default value if not
     * @param symbolizer
     * @param optionName
     * @param defaultValue
     * @return
     */
    public double getDoubleOption(Symbolizer symbolizer, String optionName, double defaultValue) {
        String value = getOption(symbolizer, optionName);
        if (value == null)
            return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Extracts a boolean from the vendor options map, returns it if found, returns the default value if not
     * 
     * @param symbolizer
     */
    public boolean getBooleanOption(TextSymbolizer symbolizer, String optionName,
            boolean defaultValue) {
        String value = getOption(symbolizer, optionName);
        if (value == null) {
            return defaultValue;
        }
        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("1");
    }
    
    /**
     * Returns a CSS margin from the options map. The result always has 4 components, in top,right,bottom,left
     * order.
     * The syntax can follow the CSS shorthand, http://www.w3schools.com/css/css_margin.asp
     * @param symbolizer
     * @return
     */
    public int[] getGraphicMargin(Symbolizer symbolizer, String optionName) {
        String value = getOption(symbolizer, optionName);
        if(value == null) {
            return null;
        } else {
            String[] values = value.trim().split("\\s+");
            if(values.length == 0) {
                return null;
            } else if(values.length > 4) {
                throw new IllegalArgumentException("The graphic margin is to be specified with 1, 2 or 4 values");
            }
            int[] parsed = new int[values.length];
            boolean allZeroMargin = false;
            for (int i = 0; i < parsed.length; i++) {
                int margin = Integer.parseInt(values[i]);
                allZeroMargin = allZeroMargin && margin == 0;
                parsed[i] = margin;
            } 
            // if not a single positive margin
            if(allZeroMargin) {
                return null;
            } else if(parsed.length == 4) {
                return parsed;
            } else if(parsed.length == 3) {
                return new int[] {parsed[0], parsed[1], parsed[2], parsed[1]};
            } else if(parsed.length == 2) {
                return new int[] {parsed[0], parsed[1], parsed[0], parsed[1]};
            } else {
                return new int[] {parsed[0], parsed[0], parsed[0], parsed[0]};
            }
        }
    }
}
