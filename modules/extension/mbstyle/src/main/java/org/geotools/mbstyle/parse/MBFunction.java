/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.parse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.coverage.processing.operation.Interpolate;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.filter.function.math.FilterFunction_pow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * MBFunction json wrapper, allowing conversion of function to a GeoTools Expression.
 *
 * <p>As of v0.41.0, data expressions are the preferred method for styling features based on zoom level or the feature's
 * properties.
 *
 * <p>Each function is evaluated according type: {@link FunctionType#IDENTITY}, {@link FunctionType#INTERVAL},
 * {@link FunctionType#CATEGORICAL}, {@link FunctionType#EXPONENTIAL}.
 *
 * <p>We have several methods that intelligently review {@link #getType()} and produce the correct expression:
 *
 * <ul>
 *   <li>{@link #color()}
 *   <li>{@link #numeric()}
 *   <li>{@link #function(Class)}
 *   <li>{@link #enumeration(Class)}
 * </ul>
 */
public class MBFunction {
    protected final MBObjectParser parse;

    protected final JSONObject json;

    private final FilterFactory ff;

    final JSONParser parser = new JSONParser();

    public MBFunction(JSONObject json) {
        this(new MBObjectParser(MBFunction.class), json);
    }

    public MBFunction(MBObjectParser parse, JSONObject json) {
        this.parse = parse;
        this.ff = parse.getFilterFactory();

        this.json = json;
    }

    /** Optional type, one of identity, exponential, interval, categorical. */
    public enum FunctionType {
        /** Functions return their input as their output. */
        IDENTITY,
        /**
         * Functions generate an output by interpolating between stops just less than and just greater than the function
         * input. The domain must be numeric. This is the default for properties marked with "exponential" symbol.
         *
         * <p>This is the default function type for continuous value (such as color or line width).
         *
         * <p>Maps to {@link Interpolate}, requiring use of {@link FilterFunction_pow} for base other that 1.
         */
        EXPONENTIAL,
        /**
         * Functions return the output value of the stop just less than the function input. The domain must be numeric.
         * This is the default for properties marked with thie "interval" symbol.
         *
         * <p>This is the default function type for enums values (such as line_cap).
         *
         * <p>Maps to {@link CategorizeFunction}.
         */
        INTERVAL,

        /**
         * Functions return the output value of the stop equal to the function input.
         *
         * <p>Maps to {@link RecodeFunction}.
         */
        CATEGORICAL
    }

    /**
     * Access the function 'type', or null if not specified.
     *
     * <p>Depending on the domain you are working with ( {@link #enumeration(Class)}, {@link #color()},
     * {@link #enumeration(Class)}} ) the default value to use is different. These functions check for null and use the
     * appropriate setting.
     *
     * @return function type, or null if not defined.
     */
    public FunctionType getType() {
        String type = parse.get(json, "type", null);
        if (type == null) {
            return null;
        }
        switch (type) {
            case "identity":
                return FunctionType.IDENTITY;
            case "exponential":
                return FunctionType.EXPONENTIAL;
            case "interval":
                return FunctionType.INTERVAL;
            case "categorical":
                return FunctionType.CATEGORICAL;
            default:
                throw new MBFormatException("Function type \""
                        + type
                        + "\" invalid - expected identity, exponential, interval, categorical");
        }
    }

    /**
     * A value to serve as a fallback function result when a value isn't otherwise available. It is used in the
     * following circumstances:
     *
     * <ul>
     *   <li>In categorical functions, when the feature value does not match any of the stop domain values.
     *   <li>In property and zoom-and-property functions, when a feature does not contain a value for the specified
     *       property.
     *   <li>In identity functions, when the feature value is not valid for the style property (for example, if the
     *       function is being used for a circle-color property but the feature property value is not a string or not a
     *       valid color).
     *   <li>In interval or exponential property and zoom-and-property functions, when the feature value is not numeric.
     * </ul>
     *
     * <p>If no default is provided, the style property's default is used in these circumstances.
     *
     * @return The function's default value, or null if none was provided.
     */
    public Object getDefault() {
        if (json == null || !json.containsKey("default")) {
            return null;
        } else {
            return json.get("default");
        }
    }

    /**
     * Return the function type, falling back to the default function type for the provided {@link Class} if no function
     * type is explicitly declared. The parameter is necessary because different output classes will have different
     * default function types.
     *
     * <p>Examples (For a function with no explicitly declared type):
     *
     * <pre>
     * getTypeWithDefault(String.class); // "interval" function type
     * getTypeWithDefault(Number.class); // "exponential" function type
     * </pre>
     *
     * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#types-function">The "type" header under Mapbox
     *     Spec: Functions</a>
     * @param clazz The class for which to return the default function type
     * @return The function type, falling back to the default when the provided {@link Class} is the return type.
     */
    public FunctionType getTypeWithDefault(Class<?> clazz) {

        // If a function type is explicitly declared, return that.
        FunctionType declaredType = getType();

        if (declaredType != null) {
            return declaredType;
        }

        // Otherwise, return the correct default type for the provided class.
        if (Color.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz)) {
            return FunctionType.EXPONENTIAL;
        } else {
            return FunctionType.INTERVAL;
        }
    }

    /**
     * If specified, the function will take the specified feature property as an input. See Zoom Functions and Property
     * Functions for more information.
     *
     * @return property evaluated by function, optional (may be null for zoom functions).
     */
    public String getProperty() {
        return parse.optional(String.class, json, "property", null);
    }

    /**
     * Function category property, zoom, or property and zoom. Defined as:
     *
     * <ul>
     *   <li>property: Each stop is an array with two elements, the first is a property input value and the second is a
     *       function output value. Note that support for property functions is not available across all properties and
     *       platforms at this time.
     *       <pre>
     * {  "circle-color": {
     *      "property": "temperature",
     *      "stops": [
     *        [0, 'blue'],
     *        [100, 'red']
     *      ]
     *    }
     *  }
     * </pre>
     *   <li>zoom: Each stop is an array with two elements: the first is a zoom level and the second is a function
     *       output value.
     *       <pre>
     * {  "circle-radius": {
     *      "stops": [
     *        [5, 1],
     *        [10, 2]
     *      ]
     *    }
     *  }
     * </pre>
     *   <li>zoom and property: Each stop is an array with two elements, the first is a property input value and the
     *       second is a function output value. Note that support for property functions is not available across all
     *       properties and platforms at this time.
     *       <pre>
     * {  "circle-radius": {
     *      "property": "rating",
     *      "stops": [
     *        [{zoom: 0, value: 0}, 0],
     *        [{zoom: 0, value: 5}, 5],
     *        [{zoom: 20, value: 0}, 0],
     *        [{zoom: 20, value: 5}, 20]
     *      ]
     *    }
     *  }
     * </pre>
     * </ul>
     */
    public enum FunctionCategory {
        /**
         * Property functions allow the appearance of a map feature to change with its properties. Property functions
         * can be used to visually differentate types of features within the same layer or create data visualizations.
         */
        PROPERTY,
        /**
         * Zoom functions allow the appearance of a map feature to change with map’s zoom level. Zoom functions can be
         * used to create the illusion of depth and control data density.
         */
        ZOOM
    }

    /**
     * Programmatically look at the structure of the function and determine if it is a Zoom function, Property function
     * or Zoom-and-property functions.
     *
     * @return Classify function as {@link FunctionCategory#PROPERTY}, {@link FunctionCategory#ZOOM} or both.
     */
    public EnumSet<FunctionCategory> category() {
        String property = getProperty();
        JSONArray stops = getStops();
        if (property != null && stops == null) {
            return EnumSet.of(FunctionCategory.PROPERTY);
        }
        JSONArray first = parse.jsonArray(stops.get(0));

        if (property == null) {
            return EnumSet.of(FunctionCategory.ZOOM); // no property defined, zoom function
        } else if (first.get(0) instanceof JSONObject) {
            return EnumSet.of(FunctionCategory.ZOOM, FunctionCategory.PROPERTY);
        } else {
            return EnumSet.of(FunctionCategory.PROPERTY);
        }
    }

    /**
     * Functions are defined in terms of input and output values. A set of one input value and one output value is known
     * as a "stop."
     *
     * @return stops definition, optional may be null.
     */
    public JSONArray getStops() {
        return parse.getJSONArray(json, "stops", null);
    }

    /**
     * (Optional) Number. Default is 1. The exponential base of the interpolation curve. It controls the rate at which
     * the function output increases. Higher values make the output increase more towards the high end of the range.
     * With values close to 1 the output increases linearly.
     *
     * @return The exponential base of the interpolation curve.
     */
    public Number getBase() {
        return parse.optional(Number.class, json, "base", 1);
    }

    /**
     * Extracts input value expression this function is performed against.
     *
     * <p>The value is determined by:
     *
     * <ul>
     *   <li>{@link FunctionCategory#ZOOM}: uses zoomLevel function with wms_scale_denominator evn variable
     *   <li>{@link FunctionCategory#PROPERTY}: uses the provided property to extract value from each feature
     * </ul>
     *
     * Zoom and Property functions are not supported and are expected to be reduced by the current zoom level prior to
     * use.
     *
     * @return expression function is evaluated against
     */
    private Expression input() {
        EnumSet<FunctionCategory> category = category();
        if (category.containsAll(EnumSet.of(FunctionCategory.ZOOM, FunctionCategory.PROPERTY))) {
            // double check if this can/should be supported now that we have a zoomLevel function
            throw new IllegalStateException("Reduce zoom and property function prior to use.");
        } else if (category.contains(FunctionCategory.ZOOM)) {
            return ff.function(
                    "zoomLevel", ff.function("env", ff.literal("wms_scale_denominator")), ff.literal("EPSG:3857"));
        } else {
            return ff.property(getProperty());
        }
    }

    /**
     * Function as defined by json.
     *
     * <p>The value for any layout or paint property may be specified as a function. Functions allow you to make the
     * appearance of a map feature change with the current zoom level and/or the feature's properties.
     *
     * @param json Definition of Function
     * @return Function as defined by json
     */
    //    public static MBFunction create(JSONObject json) {
    //        return null;
    //    }

    //
    // Color
    //
    /**
     * GeoTools {@link Expression} from json definition that evaluates to a color, used for properties such as 'color'
     * and 'fill-color'.
     *
     * <p>This is the same as {@link #numeric()} except we can make some assumptions about the values (converting hex to
     * color, looking up color names).
     *
     * <ul>
     *   <li>{@link FunctionType#IDENTITY}: input is directly converted to a color, providing a way to process attribute
     *       data into colors.
     *   <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input value
     *   <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input value
     *   <li>{@link FunctionType#EXPONENTIAL}: interpolates an output color between two stops
     * </ul>
     *
     * If type is unspecified exponential is used as a default.
     *
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    public Expression color() {
        Expression value = input();
        FunctionType type = getTypeWithDefault(Color.class);

        if (type == FunctionType.EXPONENTIAL) {
            double base = parse.optional(Double.class, json, "base", 1.0);
            if (base == 1.0) {
                return colorGenerateInterpolation(value);
            } else {
                return colorGenerateExponential(value, base);
            }
        }
        if (type == null || type == FunctionType.CATEGORICAL) {
            return colorGenerateRecode(value);
        } else if (type == FunctionType.INTERVAL) {
            return colorGenerateCategorize(value);
        } else if (type == FunctionType.IDENTITY) {
            return withFallback(ff.function("css", value)); // force conversion of CSS color names
        }
        throw new UnsupportedOperationException("Color unavailable for '" + type + "' function");
    }

    /**
     * Generates a color expression for the output of this {@link MBFunction} (as a
     * {@link MBFunction.FunctionType#CATEGORICAL} function), based on the provided input Expression.
     *
     * @param expression The expression for the function input
     * @return The expression for the output of this function (as a {@link MBFunction.FunctionType#CATEGORICAL}
     *     function)
     */
    private Expression colorGenerateCategorize(Expression expression) {
        return generateCategorize(expression, (value, stop) -> {
            Expression color = parse.color((String) value);
            if (color == null) {
                throw new MBFormatException("Could not convert stop " + stop + " color " + value + " into a color");
            }
            return color;
        });
    }
    /**
     * Use Recode function to implement {@link FunctionType#CATEGORICAL}.
     *
     * <p>
     *
     * @param input input expression
     * @return recode function
     */
    private Expression colorGenerateRecode(Expression input) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            Expression color = parse.color((String) value); // handles web colors
            if (color == null) {
                throw new MBFormatException("Could not convert stop " + stop + " color " + value + " into a color");
            }
            parameters.add(ff.literal(stop));
            parameters.add(color);
        }
        return withFallback(ff.function("Recode", parameters.toArray(new Expression[parameters.size()])));
    }

    /**
     * Generates a color expression for the output of this {@link MBFunction} (as a interpolate function), based on the
     * provided input Expression.
     *
     * @param expression The expression for the function input
     * @return The expression for the output of this function (as an interpolate function)
     */
    private Expression colorGenerateInterpolation(Expression expression) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(expression);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            Expression color = parse.color((String) value); // handles web colors
            if (color == null) {
                throw new MBFormatException("Could not convert stop " + stop + " color " + value + " into a color");
            }
            parameters.add(ff.literal(stop));
            parameters.add(color);
        }
        parameters.add(ff.literal("color"));
        return withFallback(ff.function("Interpolate", parameters.toArray(new Expression[parameters.size()])));
    }

    /**
     * Generates a color expression for the output of this {@link MBFunction} (as an exponential function), based on the
     * provided input Expression.
     *
     * @param expression The expression for the function input
     * @return The expression for the output of this function (as an exponential function)
     */
    private Expression colorGenerateExponential(Expression expression, double base) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(expression);
        parameters.add(ff.literal(base));
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            Expression color = parse.color((String) value);
            if (color == null) {
                throw new MBFormatException("Could not convert stop " + stop + " color " + value + " into a color");
            }
            parameters.add(ff.literal(stop));
            parameters.add(color);
        }
        return withFallback(ff.function("Exponential", parameters.toArray(new Expression[parameters.size()])));
    }

    //
    // Font
    //
    /**
     * GeoTools {@link Expression} from json definition that evaluates to a font string, used for 'text-font'.
     *
     * <p>Fonts only use interval functions
     *
     * <ul>
     *   <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value as a number
     * </ul>
     *
     * @return Expression providing font string (used for `text-font`)
     */
    public Expression font() {
        Expression input = input();
        return fontGenerateCategorize(input);
    }

    private Expression fontGenerateCategorize(Expression expression) {
        return generateCategorize(expression, (value, stop) -> {
            Expression font = parse.ff.literal(((JSONArray) value).get(0));
            if (font == null) {
                throw new MBFormatException("Could not convert stop " + stop + " font " + value + " into a font");
            }
            return font;
        });
    }
    //
    // Numeric
    //

    /**
     * GeoTools {@link Expression} from json definition that evaluates to a numeric, used for properties such as
     * 'line-width' and 'opacity'.
     *
     * <p>This is the same as {@link #color()} except we can make some assumptions about the values (converting "50%" to
     * 0.5).
     *
     * <ul>
     *   <li>{@link FunctionType#IDENTITY}: input is directly converted to a numeric output
     *   <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input, and returns stop value as a number
     *   <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value as a number
     *   <li>{@link FunctionType#EXPONENTIAL}: interpolates a numeric output between two stops
     * </ul>
     *
     * If type is unspecified exponential is used as a default.
     *
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    public Expression numeric() {
        Expression input = input();
        FunctionType type = getTypeWithDefault(Number.class);

        if (type == FunctionType.EXPONENTIAL) {
            double base = parse.optional(Double.class, json, "base", 1.0);
            if (base == 1.0) {
                return numericGenerateInterpolation(input);
            } else {
                return numericGenerateExponential(input, base);
            }
        }
        if (type == FunctionType.CATEGORICAL) {
            return generateRecode(input);
        } else if (type == FunctionType.INTERVAL) {
            return generateCategorize(input);
        } else if (type == FunctionType.IDENTITY) {
            return withFallback(input);
        }
        throw new UnsupportedOperationException("Numeric unavailable for '" + type + "' function");
    }

    /**
     * Used to calculate a numeric value.
     *
     * <p>Example adjusts circle size between 2 and 180 pixels when zooming between levels 12 and 22.
     *
     * <pre><code>'circle-radius': {
     *   'stops': [[12, 2], [22, 180]]
     * }</pre></code>
     *
     * @param input The expression for the function input
     * @return The expression for the output of this function (as an interpolate function)
     */
    private Expression numericGenerateInterpolation(Expression input) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            if (!(value instanceof Number)) {
                throw new MBFormatException("Could not convert stop " + stop + " color " + value + " into a numeric");
            }
            parameters.add(ff.literal(stop));
            parameters.add(ff.literal(value));
        }
        parameters.add(ff.literal("numeric"));
        return withFallback(ff.function("Interpolate", parameters.toArray(new Expression[parameters.size()])));
    }

    /**
     * Used to calculate a numeric value.
     *
     * <p>Example adjusts circle size between 2 and 180 pixels when zooming between levels 12 and 22.
     *
     * <pre><code>'circle-radius': {
     *   'base': 1.75,
     *   'stops': [[12, 2], [22, 180]]
     * }</pre></code>
     *
     * @param input The expression for the function input
     * @param base The base of the exponential interpolation
     * @return The expression for the output of this function (as an exponential function)
     */
    private Expression numericGenerateExponential(Expression input, double base) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        parameters.add(ff.literal(base));
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            if (!(value instanceof Number)) {
                throw new MBFormatException("Could not convert stop " + stop + " color " + value + " into a numeric");
            }
            parameters.add(ff.literal(stop));
            parameters.add(ff.literal(value));
        }
        return withFallback(ff.function("Exponential", parameters.toArray(new Expression[parameters.size()])));
    }

    //
    // General Purpose
    //
    /**
     * GeoTools {@link Expression} from json definition.
     *
     * <p>Delegates handling of Color, Number and Enum - for generic values (such as String) the following are
     * available:
     *
     * <ul>
     *   <li>{@link FunctionType#IDENTITY}: input is directly converted to a literal
     *   <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input, and returns stop value as a literal
     *   <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value a literal
     * </ul>
     *
     * If type is unspecified interval is used as a default.
     *
     * @param clazz Type of data expected
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    @SuppressWarnings("unchecked")
    public Expression function(Class<?> clazz) {
        // check for special cases
        if (clazz.isAssignableFrom(Color.class)) {
            return color();
        } else if (clazz.isAssignableFrom(Number.class)) {
            return numeric();
        } else if (clazz.isAssignableFrom(Enum.class)) {
            return enumeration((Class<? extends Enum<?>>) clazz);
        }

        Expression input = input();
        FunctionType type = getTypeWithDefault(clazz);

        if (type == null || type == FunctionType.INTERVAL) {
            return generateCategorize(input);
        } else if (type == FunctionType.CATEGORICAL) {
            return generateRecode(input);
        } else if (type == FunctionType.IDENTITY) {
            return withFallback(input);
        }
        throw new UnsupportedOperationException(
                "Function unavailable for '" + type + "' function with " + clazz.getSimpleName());
    }

    private Expression generateCategorize(Expression expression) {
        return generateCategorize(expression, (value, stop) -> ff.literal(value));
    }

    /**
     * Takes an expression and wraps it with a function that falls back to this {@link MBFunction}'s default return
     * value.
     *
     * <p>If the input expression evaluates to null, the wrapper function will return the fallback value instead.
     *
     * @param expression The expression to wrap with a fallback to this {@link MBFunction}'s return value.
     */
    private Expression withFallback(Expression expression) {
        Object defaultValue = getDefault();
        if (defaultValue != null) {
            return ff.function("DefaultIfNull", expression, ff.literal(defaultValue));
        } else {
            return expression;
        }
    }

    /**
     * Generates an expression for the output of this {@link MBFunction} (as an {@link MBFunction.FunctionType#INTERVAL}
     * function), based on the provided input Expression.
     *
     * <p>Note: A mapbox "interval" function is implemented as a GeoTools "categorize" function, hence the name of this
     * method.
     *
     * @param expression The expression for the function input
     * @param parseValue A function of two arguments (stopValue, stop) that parses the stop value into an Expression.
     * @return The expression for the output of this function (as a {@link MBFunction.FunctionType#INTERVAL} function)
     */
    private Expression generateCategorize(
            Expression expression, java.util.function.BiFunction<Object, Object, Expression> parseValue) {

        JSONArray stopsJson = getStops();
        List<Expression> parameters = new ArrayList<>(
                stopsJson.size() * 2 + 3); // each stop is 2, plus property name, leading interval value,
        // and "succeeding"
        parameters.add(expression);
        for (int i = 0; i < stopsJson.size(); i++) {
            JSONArray entry = parse.jsonArray(stopsJson.get(i));
            Object stop = entry.get(0);
            Expression value = parseValue.apply(entry.get(1), stop);

            if (i == 0) {
                // CategorizeFunction expects there to be a leading value for inputs <
                // firstStopThreshold.
                // But the MapBox spec does not define the expected behavior in that case.
                // (spec: "functions return the output value of the stop just less than the function
                // input.")
                // Return the default value (if any), otherwise the first stop's value.
                Expression initialValue;
                Object defaultValue = getDefault();
                if (defaultValue != null) {
                    initialValue = ff.literal(defaultValue);
                } else {
                    initialValue = value; // The first stop's value
                }
                parameters.add(initialValue);
            }

            parameters.add(ff.literal(stop));
            parameters.add(value);
        }
        parameters.add(ff.literal("succeeding"));

        Function categorizeFunction = ff.function("Categorize", parameters.toArray(new Expression[parameters.size()]));
        return withFallback(categorizeFunction);
    }

    /**
     * Generates an expression for the output of this {@link MBFunction} (as a
     * {@link MBFunction.FunctionType#CATEGORICAL} function), based on the provided input Expression.
     *
     * <p>Note: A mapbox "categorical" function is implemented as a GeoTools "recode" function, hence the name of this
     * method.
     *
     * @param input The expression for the function input
     * @return The expression for the output of this function (as a {@link MBFunction.FunctionType#CATEGORICAL}
     *     function)
     */
    private Expression generateRecode(Expression input) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            parameters.add(ff.literal(stop));
            parameters.add(ff.literal(value));
        }
        Function recodeFn = ff.function("Recode", parameters.toArray(new Expression[parameters.size()]));
        return withFallback(recodeFn);
    }

    //
    // Enumerations
    //
    /**
     * GeoTools {@link Expression} from json definition that evaluates to the provided Enum, used for properties such as
     * 'line-cap' and 'text-transform'.
     *
     * <ul>
     *   <li>{@link FunctionType#IDENTITY}: input is directly converted to an appropriate literal
     *   <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input, and returns stop value as a literal
     *   <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value a literal
     * </ul>
     *
     * If type is unspecified internval is used as a default.
     *
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    public Expression enumeration(Class<? extends Enum<?>> enumeration) {
        Expression input = input();
        FunctionType type = getTypeWithDefault(Enumeration.class);
        if (type == FunctionType.INTERVAL) {
            return enumGenerateCategorize(input, enumeration);
        } else if (type == FunctionType.CATEGORICAL) {
            return enumGenerateRecode(input, enumeration);
        } else if (type == FunctionType.IDENTITY) {
            return withFallback(enumGenerateIdentity(input, enumeration));
        }
        throw new UnsupportedOperationException(
                "Unable to support '" + type + "' function for " + enumeration.getSimpleName());
    }
    /**
     * Generates an expression (based on a mapbox enumeration property) for the output of this {@link MBFunction} (as a
     * {@link MBFunction.FunctionType#CATEGORICAL} function), based on the provided input Expression.
     *
     * <p>Note: A mapbox "categorical" function is implemented as a GeoTools "recode" function, hence the name of this
     * method.
     *
     * @param input The expression for the function input
     * @param enumeration The type of the enumeration for the mapbox style property
     * @return The expression for the output of this function (as a {@link MBFunction.FunctionType#CATEGORICAL}
     *     function)
     */
    private Expression enumGenerateRecode(Expression input, Class<? extends Enum<?>> enumeration) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            parameters.add(ff.literal(stop));
            parameters.add(parse.constant(value, enumeration));
        }

        return withFallback(ff.function("Recode", parameters.toArray(new Expression[parameters.size()])));
    }

    /**
     * Generates an expression (based on a mapbox enumeration property) for the output of this {@link MBFunction} (as a
     * {@link MBFunction.FunctionType#INTERVAL} function), based on the provided input Expression.
     *
     * <p>Note: A mapbox "interval" function is implemented as a GeoTools "categorize" function, hence the name of this
     * method.
     *
     * @param input The expression for the function input
     * @param enumeration The type of the enumeration for the mapbox style property
     * @return The expression for the output of this function (as a {@link MBFunction.FunctionType#INTERVAL} function)
     */
    private Expression enumGenerateCategorize(Expression input, Class<? extends Enum<?>> enumeration) {
        return withFallback(generateCategorize(input, (value, stop) -> parse.constant(value, enumeration)));
    }

    /**
     * Generates an expression (based on a mapbox enumeration property) for the output of this {@link MBFunction} (as a
     * {@link MBFunction.FunctionType#IDENTITY} function), based on the provided input Expression.
     *
     * @param input The expression for the function input
     * @param enumeration The type of the enumeration for the mapbox style property
     * @return The expression for the output of this function (as a {@link MBFunction.FunctionType#IDENTITY} function)
     */
    private Expression enumGenerateIdentity(Expression input, Class<? extends Enum<?>> enumeration) {
        // this is an interesting challenge, we need to generate a recode mapping
        // mapbox constants defined by the enum, to appropriate geotools literals
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Enum<?> constant : enumeration.getEnumConstants()) {
            Object value = constant.name().toLowerCase();
            parameters.add(ff.literal(value));
            parameters.add(parse.constant(value, enumeration));
        }
        return withFallback(ff.function("Recode", parameters.toArray(new Expression[parameters.size()])));
    }

    /**
     * Returns true if this function's stop values are all arrays.
     *
     * <p>For example, the following is an array function:
     *
     * <pre>
     *
     * "{'property':'temperature',
     *   'type':'exponential',
     *   'base':1.5,
     *   'stops': [
     *          // [stopkey, stopValueArray]
     *             [0,       [0,10]],
     *             [100,     [2,15]]
     *    ]
     *   }"
     * </pre>
     *
     * @return true if this function's stop values are all arrays.
     */
    public boolean isArrayFunction() {

        if (getStops() == null) {
            return false;
        }

        // If any of the stops is not array-valued, return false.
        for (Object o : getStops()) {
            if (!(o instanceof JSONArray)) {
                return false;
            } else {
                JSONArray stop = (JSONArray) o;
                if (stop.size() != 2 || !(stop.get(1) instanceof JSONArray)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Splits an array function into multiple functions, one for each dimension in the function's stop value arrays.
     *
     * <p>For example, for the following array function:
     *
     * <pre>
     *
     * "{'property':'temperature',
     *   'type':'exponential',
     *   'base':1.5,
     *   'stops': [
     *          // [stopkey, stopValueArray]
     *             [0,       [0,10]],
     *             [100,     [2,15]]
     *    ]
     *   }"
     * </pre>
     *
     * <p>This method would split the above function into the following two functions:
     *
     * <p>"X" Function:
     *
     * <pre>
     *
     * "{'property':'temperature',
     *   'type':'exponential',
     *   'base':1.5,
     *   'stops': [
     *          [0,   0],
     *          [100, 2]
     *    ]
     *   }"
     * </pre>
     *
     * <p>And "Y" Function:
     *
     * <pre>
     *
     * "{'property':'temperature',
     *   'type':'exponential',
     *   'base':1.5,
     *   'stops': [
     *          [0,   10],
     *          [100, 15]
     *    ]
     *   }"
     * </pre>
     *
     * @return A list of {@link MBFunction}, one for each dimension in the stop value array.
     */
    @SuppressWarnings("unchecked")
    public List<MBFunction> splitArrayFunction() throws ParseException {
        JSONArray arr = getStops();

        // No need to split if there are no stops.
        if (arr.isEmpty()) {
            return Arrays.asList(this);
        }

        // Parse the stops
        List<MBArrayStop> parsedStops = new ArrayList<>();
        for (Object o : arr) {
            if (o instanceof JSONArray) {
                parsedStops.add(new MBArrayStop((JSONArray) o));
            } else {
                throw new MBFormatException("Exception handling array function: encountered non-array stop value.");
            }
        }

        // Make sure that all the stop value arrays have the same number of dimensions
        int dimensionCount = parsedStops.get(0).getStopValueCount();
        boolean allStopsSameDimension =
                parsedStops.stream().allMatch(stop -> stop.getStopValueCount() == dimensionCount);

        if (!allStopsSameDimension) {
            throw new MBFormatException(
                    "Exception handling array function: all stops arrays must have the same length.");
        }

        // Make sure that the default value also has the same number of dimensions
        JSONArray defaultStopValues = null;
        if (getDefault() != null) {
            Object def = getDefault();
            if (def instanceof JSONArray && ((JSONArray) def).size() == dimensionCount) {
                defaultStopValues = (JSONArray) def;
            } else {
                throw new MBFormatException(
                        "Exception handling array function: the default value must also be an array of length "
                                + dimensionCount);
            }
        }

        // Split the function into N functions, one for each dimension in the stop array values.
        List<MBFunction> functions = new ArrayList<>();
        for (int i = 0; i < dimensionCount; i++) {
            final Integer n = i;

            JSONArray newStops = new JSONArray();
            for (MBArrayStop stop : parsedStops) {
                JSONArray jsonArray = stop.reducedToIndex(n);
                newStops.add(jsonArray);
            }
            JSONObject newObj = (JSONObject) parser.parse(json.toJSONString());

            newObj.put("stops", newStops);
            if (defaultStopValues != null) {
                newObj.put("default", defaultStopValues.get(n));
            }

            MBFunction reduced = new MBFunction(newObj);
            functions.add(reduced);
        }

        return functions;
    }
}
