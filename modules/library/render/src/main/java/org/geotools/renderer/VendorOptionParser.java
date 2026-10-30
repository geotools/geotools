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

import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.InternalFunction;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.TextSymbolizer.DisplacementMode;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.util.Converters;

/**
 * Helper class that provides utility methods to extract and parse elements from the vendor options map, some generic,
 * other geared to specific contents
 *
 * @author Andrea Aime - GeoSolutions
 */
public class VendorOptionParser {

    static final Pattern SPACES = Pattern.compile("\\s+");

    /** Largest number of distinct {@code ${cql}} option strings kept by one parser. */
    private static final int MAX_OPTION_EXPRESSIONS = 1000;

    /** Largest number of results remembered by one parser, for all its options together. */
    private static final int MAX_REMEMBERED_RESULTS = 10_000;

    /** Collects the properties an expression reads and tells whether its result depends on them only. */
    private static final class ReusabilityExtractor extends FilterAttributeExtractor {
        @Override
        public Object visit(Function function, Object data) {
            if (function instanceof InternalFunction) usingVolatileFunctions = true;
            return super.visit(function, data);
        }

        boolean isReusable() {
            return !usingVolatileFunctions && !usingDynamicProperties;
        }
    }

    /** A parsed {@code ${cql}} option and the properties its result depends on. */
    private static final class OptionExpression {
        final String value;
        final Expression expression;
        /**
         * The properties the expression reads, null when its result cannot be reused (volatile functions, dynamic
         * properties).
         */
        final PropertyName[] properties;

        OptionExpression(String value) {
            this.value = value;
            this.expression = ExpressionExtractor.extractCqlExpressions(value);
            ReusabilityExtractor extractor = new ReusabilityExtractor();
            expression.accept(extractor, null);
            this.properties =
                    extractor.isReusable() ? extractor.getPropertyNameSet().toArray(new PropertyName[0]) : null;
        }

        /**
         * The option string followed by the values of the properties it reads, null when the result cannot be reused.
         */
        List<Object> resultKey(Object feature) {
            if (properties == null) return null;
            List<Object> key = new ArrayList<>(properties.length + 1);
            key.add(value);
            for (PropertyName property : properties) {
                key.add(property.evaluate(feature));
            }
            return key;
        }
    }

    /**
     * {@code ${cql}} options by option string. A label option is evaluated for every feature, and most of them depend
     * on a few properties only (a rank, a kind) and on the zoom level, so the results are remembered by the values of
     * those properties. Call {@link #clearRememberedResults()} when what the expressions read besides the feature (such
     * as environment variables) may change.
     */
    private final ConcurrentMap<String, OptionExpression> optionExpressions = CacheBuilder.newBuilder()
            .maximumSize(MAX_OPTION_EXPRESSIONS)
            .<String, OptionExpression>build()
            .asMap();

    /** Results by option string followed by the values of the properties the option reads. */
    private final ConcurrentMap<List<Object>, Object> results = CacheBuilder.newBuilder()
            .maximumSize(MAX_REMEMBERED_RESULTS)
            .<List<Object>, Object>build()
            .asMap();

    /** Forgets the remembered option results, call it at the start of each rendering. */
    public void clearRememberedResults() {
        optionExpressions.clear();
        results.clear();
    }

    private Object evaluate(String value, Object feature) {
        OptionExpression option = optionExpressions.computeIfAbsent(value, OptionExpression::new);
        List<Object> key = option.resultKey(feature);
        if (key == null) return option.expression.evaluate(feature);
        return results.computeIfAbsent(key, k -> option.expression.evaluate(feature));
    }

    private Number evaluateNumber(String value, Object feature) {
        return Converters.convert(evaluate(value, feature), Number.class);
    }

    /** Extracts a enumeration from the vendor options map, returns it if found, returns the default value if not */
    public <T extends Enum<T>> Enum<T> getEnumOption(Symbolizer symbolizer, String optionName, Enum<T> defaultValue) {
        String value = resolve(symbolizer, optionName, null);
        if (value == null) return defaultValue;
        try {
            return Enum.valueOf(defaultValue.getDeclaringClass(), value.toUpperCase());
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
     * Reads the raw option and, when it holds a {@code ${cql}} expression, evaluates it against the feature and returns
     * the result as a string. A plain (literal) option is returned unchanged. Used by the getters that coerce a string;
     * the numeric getters evaluate the expression to their target type directly, so they do not go through here.
     */
    private String resolve(Symbolizer symbolizer, String optionName, Object feature) {
        String value = getOption(symbolizer, optionName);
        if (value == null || !value.contains("${")) {
            return value;
        }
        return Objects.toString(evaluate(value, feature), null);
    }

    /** Extracts a integer from the vendor options map, returns it if found, returns the default value if not */
    public int getIntOption(Symbolizer symbolizer, String optionName, int defaultValue) {
        return getIntOption(symbolizer, optionName, defaultValue, null);
    }

    /**
     * Extracts an integer from the vendor options map, evaluating a {@code ${cql}} value against the given feature
     * first. Rounds a fractional result. Returns the default value if the option is missing or does not evaluate to a
     * number.
     */
    public int getIntOption(Symbolizer symbolizer, String optionName, int defaultValue, Object feature) {
        String value = getOption(symbolizer, optionName);
        if (value == null) return defaultValue;
        if (value.contains("${")) {
            // int options are pixel sizes and counts, round to the nearest: the Integer converter truncates
            Number v = evaluateNumber(value, feature);
            return v != null ? (int) Math.round(v.doubleValue()) : defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /** Extracts a double from the vendor options map, returns it if found, returns the default value if not */
    public double getDoubleOption(Symbolizer symbolizer, String optionName, double defaultValue) {
        return getDoubleOption(symbolizer, optionName, defaultValue, null);
    }

    /**
     * Extracts a double from the vendor options map, evaluating a {@code ${cql}} value against the given feature first.
     * Returns the default value if the option is missing or does not evaluate to a number.
     */
    public double getDoubleOption(Symbolizer symbolizer, String optionName, double defaultValue, Object feature) {
        String value = getOption(symbolizer, optionName);
        if (value == null) return defaultValue;
        if (value.contains("${")) {
            Number v = evaluateNumber(value, feature);
            return v != null ? v.doubleValue() : defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /** Extracts a boolean from the vendor options map, returns it if found, returns the default value if not */
    public boolean getBooleanOption(Symbolizer symbolizer, String optionName, boolean defaultValue) {
        return getBooleanOption(symbolizer, optionName, defaultValue, null);
    }

    /**
     * Extracts a boolean from the vendor options map, evaluating a {@code ${cql}} value against the given feature
     * first. Returns the default value if the option is missing or does not evaluate.
     */
    public boolean getBooleanOption(Symbolizer symbolizer, String optionName, boolean defaultValue, Object feature) {
        String value = resolve(symbolizer, optionName, feature);
        if (value == null) {
            return defaultValue;
        }
        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1");
    }

    /**
     * Returns a CSS margin from the options map. The result always has 4 components, in top,right,bottom,left order.
     * The syntax can follow the CSS shorthand, http://www.w3schools.com/css/css_margin.asp
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
     * Returns an array of int in the range [0, 360) which corresponds to the possible displacement angles.
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
                    DisplacementMode mode = TextSymbolizer.DisplacementMode.valueOf(
                            values[i].trim().toUpperCase());
                    parsed[i] = mode.getAngle();
                } catch (Exception e) {
                    throw new IllegalArgumentException(values[i]
                            + " is not legal. The values of displacement mode must be one of the following: "
                            + Arrays.toString(TextSymbolizer.DisplacementMode.values()));
                }
            }
            return parsed;
        }
    }
}
