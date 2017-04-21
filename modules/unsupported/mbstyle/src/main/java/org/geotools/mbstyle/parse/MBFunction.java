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
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;

import org.geotools.coverage.processing.operation.Interpolate;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.filter.function.math.FilterFunction_pow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * MBFunction json wrapper, allowing conversion to a GeoTools Expression.
 * <p>
 * Each function is evaluated according type: {@link FunctionType#IDENTITY},
 * {@link FunctionType#INTERVAL}, {@link FunctionType#CATEGORICAL},
 * {@link FunctionType#EXPONENTIAL}.
 * <p>
 * We have several methods that intelligently review {@link #getType()} and produce the correct
 * expression.</p>
 * <ul>
 * <li>{@link #color()}</li>
 * </ul>
 * 
 */
public class MBFunction {
    final protected MBObjectParser parse;

    final protected JSONObject json;

    private FilterFactory2 ff;

    public MBFunction(JSONObject json) {
        this(new MBObjectParser(MBFunction.class), json);
    }

    public MBFunction(MBObjectParser parse, JSONObject json) {
        this.parse = parse;
        this.ff = parse.getFilterFactory();

        this.json = json;

    }

    /** Optional type, one of identity, exponential, interval, categorical. */
    public static enum FunctionType {
        /** Functions return their input as their output. */
        IDENTITY,
        /**
         * Functions generate an output by interpolating between stops just less than and just
         * greater than the function input. The domain must be numeric. This is the default for
         * properties marked with "exponential" symbol.
         * <p>
         * This is the default function type for continuous value (such as color or line width).</p>
         * <p>
         * Maps to {@link Interpolate}, requiring use of {@link FilterFunction_pow} for base other that 1.</p>
         */
        EXPONENTIAL,
        /**
         * Functions return the output value of the stop just less than the function input. The
         * domain must be numeric. This is the default for properties marked with thie "interval"
         * symbol.
         * <p>
         * This is the default function type for enums values (such as line_cap).</p>
         * <p>
         * Maps to {@link CategorizeFunction}.</p>
         */
        INTERVAL,

        /**
         * Functions return the output value of the stop equal to the function input.
         * <p>
         * Maps to {@link RecodeFunction}.</p>
         */
        CATEGORICAL
    }

    /**
     * Access the function 'type', or null if not specified.
     * <p>
     * Depending on the domain you are working with ( {@link #enumeration(Class)}, {@link #color()},
     * {@link #enumeration(Class)}} ) the default value to use is different. These functions check
     * for null and use the appropriate setting.
     * </p>
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
            throw new MBFormatException("Function type \"" + type
                    + "\" invalid - expected identity, expontential, interval, categorical");
        }
    }
    
    /**
     * <p>
     * Return the function type, falling back to the default function type for the provided {@link Class} if no function type is explicitly declared.
     * The parameter is necessary because different output classes will have different default function types.
     * </p>
     *
     * <p>
     * Examples (For a function with no explicitly declared type):
     * </p>
     * 
     * <pre>
     * getTypeWithDefault(String.class); // -> "interval" function type
     * getTypeWithDefault(Number.class); // -> "exponential" function type
     * </pre>
     * 
     * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#types-function">The "type" header under Mapbox Spec: Functions</a>
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
     * If specified, the function will take the specified feature property as an input. See Zoom
     * Functions and Property Functions for more information.
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
     * <li>property: Each stop is an array with two elements, the first is a property input value
     * and the second is a function output value. Note that support for property functions is not
     * available across all properties and platforms at this time.
     * 
     * <pre>
     * {  "circle-color": {
     *      "property": "temperature",
     *      "stops": [
     *        [0, 'blue'],
     *        [100, 'red']
     *      ]
     *    }
     *  }
     * </pre>
     * 
     * </li>
     * <li>zoom: Each stop is an array with two elements: the first is a zoom level and the second
     * is a function output value.
     * 
     * <pre>
     * {  "circle-radius": {
     *      "stops": [
     *        [5, 1],
     *        [10, 2]
     *      ]
     *    }
     *  }
     * </pre>
     * 
     * </li>
     * <li>zoom and property: Each stop is an array with two elements, the first is a property input
     * value and the second is a function output value. Note that support for property functions is
     * not available across all properties and platforms at this time.
     * 
     * <pre>
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
     * 
     * </li>
     * </ul>
     * </ui>
     */
    public static enum FunctionCategory {
        /**
         * Property functions allow the appearance of a map feature to change with its properties.
         * Property functions can be used to visually differentate types of features within the same
         * layer or create data visualizations.
         */
        PROPERTY,
        /**
         * Zoom functions allow the appearance of a map feature to change with map’s zoom level.
         * Zoom functions can be used to create the illusion of depth and control data density.
         */
        ZOOM;
    }

    /**
     * Programmatically look at the structure of the function and determine if it is a Zoom
     * function, Property function or Zoom-and-property functions.
     * 
     * @return Classify function as {@link FunctionCategory#PROPERTY}, {@link FunctionCategory#ZOOM}
     *         or both.
     */
    public EnumSet<FunctionCategory> category() {
        String property = getProperty();
        JSONArray stops = getStops();
        if( property != null && stops == null ){
            return EnumSet.of(FunctionCategory.PROPERTY);
        }
        JSONArray first = parse.jsonArray(stops.get(0));

        if (property == null) {
            return EnumSet.of(FunctionCategory.ZOOM); // no property defined, zoom function
        } else if (property != null && first.get(0) instanceof JSONObject) {
            return EnumSet.of(FunctionCategory.ZOOM, FunctionCategory.PROPERTY);
        } else {
            return EnumSet.of(FunctionCategory.PROPERTY);
        }
    }

    /**
     * Functions are defined in terms of input and output values. A set of one input value and one
     * output value is known as a "stop."
     * 
     * @return stops definition, optional may be null.
     */
    public JSONArray getStops() {
        return parse.getJSONArray(json, "stops", null);
    }
    
    /**
     * (Optional) Number. Default is 1. The exponential base of the interpolation curve. It controls the rate at which the function output increases.
     * Higher values make the output increase more towards the high end of the range. With values close to 1 the output increases linearly.
     * 
     * @return The exponential base of the interpolation curve.
     */
    public Number getBase() {
        return parse.optional(Number.class, json, "base", 1);
    }

    /**
     * Extracts input value expression this function is performed against.
     * <p>
     * The value is determined by:
     * <ul>
     * <li>{@link FunctionCategory#ZOOM}: uses zoolLevel function with wms_scale_denominator evn variable</li>
     * <li>{@link FunctionCategory#PROPERTY}: uses the provided property to exract value from each feature</li>
     * </ul>
     * Zoom and Property functions are not supported and are expected to be reduced by the current zoom level prior to use.
     * </p>
     * @return expression function is evaualted against
     */
    private Expression input() {
        EnumSet<FunctionCategory> category = category();
        if (category.containsAll(EnumSet.of(FunctionCategory.ZOOM, FunctionCategory.PROPERTY))) {
            // double check if this can/should be supported now that we have a zoomLevel function
            throw new IllegalStateException("Reduce zoom and property function prior to use.");
        }
        else if( category.contains(FunctionCategory.ZOOM)){
            return ff.function("zoomLevel",
                    ff.function("env", ff.literal("wms_scale_denominator")),
                    ff.literal("EPSG:3857")
            );
        }
        else {
            return ff.property(getProperty());
        }
    }

    /**
     * Function as defined by json.
     * <p>
     * The value for any layout or paint property may be specified as a function. Functions allow
     * you to make the appearance of a map feature change with the current zoom level and/or the
     * feature's properties.
     * </p>
     * <p>
     * Function types:
     * </p>
     * <p>
     * <em>
     * 
     * @param json JSONOBject definition of Function
     * @return Function as defined by json
     */
    public static MBFunction create(JSONObject json) {
        return null;
    }
    
    //
    // Color
    //
    /**
     * GeoTools {@link Expression} from json definition that evaluates to a color, used for
     * properties such as 'color' and 'fill-color'.
     * <p>
     * This is the same as {@link #numeric()} except we can make some assumptions about the values
     * (converting hex to color, looking up color names).
     * </p>
     * <ul>
     * <li>{@link FunctionType#IDENTITY}: input is directly converted to a color, providing a way to process attribute data
     * into colors.</li>
     * <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input value</li>
     * <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input value</li>
     * <li>{@link FunctionType#EXPONENTIAL}: interpolates an output color between two stops</li>  
     * </ul>
     * If type is unspecified exponential is used as a default.</li>
     * 
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    public Expression color(){
        Expression value = input();
        FunctionType type = getTypeWithDefault(Color.class);
        
        if (type == FunctionType.EXPONENTIAL) {
            double base = parse.optional(Double.class, json, "base", 1.0 );
            if( base == 1.0){
                return colorGenerateInterpolation(value);
            }
            else {
                return colorGenerateExponential(value, base);
            }
        }
        if( type == null || type == FunctionType.CATEGORICAL){
            return colorGenerateRecode(value);
        }
        else if( type == FunctionType.INTERVAL){
            return colorGenerateCategorize(value);
        }
        else if( type == FunctionType.IDENTITY){
            return ff.function("color", value); // force conversion of CSS color names
        }
        throw new UnsupportedOperationException("Color unavailable for '"+type+"' function");
    }
    
    private Expression colorGenerateCategorize(Expression expression) {
        return generateCategorize(expression, (value, stop)->{
            Expression color = parse.color((String)value);
            if( color == null ){
                throw new MBFormatException("Could not convert stop "+stop+" color "+value+" into a color");
            }
            return color;
        });
    }
    /**
     * Use Recode function to implement {@link FunctionType#CATEGORICAL}.
     * <p>
     * Generated expression of the form:
     * <code>Recode( input, stop1, color1, stop2, color2, 'preceding')</code></p>
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
            Expression color = parse.color((String)value); // handles web colors
            if( color == null ){
                throw new MBFormatException("Could not convert stop "+stop+" color "+value+" into a color");
            }
            parameters.add(ff.literal(stop));
            parameters.add(color);
        }
        return ff.function("Recode", parameters.toArray(new Expression[parameters.size()]));
    }
    
    private Expression colorGenerateInterpolation(Expression expression) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(expression);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            Expression color = parse.color((String)value); // handles web colors
            if( color == null ){
                throw new MBFormatException("Could not convert stop "+stop+" color "+value+" into a color");
            }
            parameters.add(ff.literal(stop));
            parameters.add(color);
        }
        parameters.add(ff.literal("color"));
        return ff.function("Interpolate", parameters.toArray(new Expression[parameters.size()]));
    }
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
                throw new MBFormatException(
                        "Could not convert stop " + stop + " color " + value + " into a color");
            }
            parameters.add(ff.literal(stop));
            parameters.add(color);
        }
        return ff.function("Exponential", parameters.toArray(new Expression[parameters.size()]));
    }

    //
    // Numeric
    //
    
    /**
     * GeoTools {@link Expression} from json definition that evaluates to a numeric, used for
     * properties such as 'line-width' and 'opacity'.
     * <p>
     * This is the same as {@link #color()} except we can make some assumptions about the values
     * (converting "50%"  to 0.5).
     * </p>
     * <ul>
     * <li>{@link FunctionType#IDENTITY}: input is directly converted to a numeric output</li>
     * <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input, and returns stop value as a number</li>
     * <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value as a number</li>
     * <li>{@link FunctionType#EXPONENTIAL}: interpolates a numeric output between two stops</li>  
     * </ul>
     * If type is unspecified exponential is used as a default.</li>
     * 
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    public Expression numeric() {
        Expression input = input();
        FunctionType type = getTypeWithDefault(Number.class);
        
        if (type == FunctionType.EXPONENTIAL) {
            double base = parse.optional(Double.class, json, "base", 1.0 );
            if( base == 1.0){
                return numericGenerateInterpolation(input);
            }
            else {
                return numericGenerateExponential(input, base);
            }
        }
        if (type == FunctionType.CATEGORICAL) {
            return generateRecode(input);
        }
        else if( type == FunctionType.INTERVAL){
            return generateCategorize(input);
        }
        else if( type == FunctionType.IDENTITY){
            return input;
        }
        throw new UnsupportedOperationException("Numeric unavailable for '"+type+"' function");
    }

    /**
     * Used to calculate a numeric value.
     * <p>
     * Example adjusts circle size between 2 and 180 pixels when zooming between levels 12 and 22.
     * <pre><code>'circle-radius': {
     *   'stops': [[12, 2], [22, 180]]
     * }</pre></code>
     * 
     * @param value
     * @return Interpolate function
     */
    private Expression numericGenerateInterpolation(Expression input) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            if (value == null || !(value instanceof Number)) {
                throw new MBFormatException(
                        "Could not convert stop " + stop + " color " + value + " into a numeric");
            }
            parameters.add(ff.literal(stop));
            parameters.add(ff.literal(value));
        }
        parameters.add(ff.literal("numeric"));
        return ff.function("Interpolate", parameters.toArray(new Expression[parameters.size()]));
    }

    /**
     * Used to calculate a numeric value.
     * <p>
     * Example adjusts circle size between 2 and 180 pixels when zooming between levels 12 and 22.
     * <pre><code>'circle-radius': {
     *   'base': 1.75,
     *   'stops': [[12, 2], [22, 180]]
     * }</pre></code>
     * 
     * @param value
     * @return Exponential function
     */
    private Expression numericGenerateExponential(Expression input, double base){
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        parameters.add(ff.literal(base));
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            if (value == null || !(value instanceof Number)) {
                throw new MBFormatException(
                        "Could not convert stop " + stop + " color " + value + " into a numeric");
            }
            parameters.add(ff.literal(stop));
            parameters.add(ff.literal(value));
        }
        return ff.function("Exponential", parameters.toArray(new Expression[parameters.size()]));
    }
    
    //
    // General Purpose
    //
    /**
     * GeoTools {@link Expression} from json definition.
     * <p>
     * Delegates handling of Color, Number and Enum - for generic values (such as String) the following are available:
     * <ul>
     * <li>{@link FunctionType#IDENTITY}: input is directly converted to a literal</li>
     * <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input, and returns stop value as a literal</li>
     * <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value a literal</li>
     * </ul>
     * If type is unspecified interval is used as a default.
     * 
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    @SuppressWarnings("unchecked")
    public Expression function(Class<?> clazz){
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
        
        if( type == null || type == FunctionType.INTERVAL){
            return generateCategorize(input);
        }
        else if( type == FunctionType.CATEGORICAL){
            return generateRecode(input);
        }
        else if( type == FunctionType.IDENTITY){
            return input;
        }
        throw new UnsupportedOperationException("Function unavailable for '"+type+"' function with "+clazz.getSimpleName());
    }
    
    private Expression generateCategorize(Expression expression) {
        return generateCategorize(expression, (value, stop)->ff.literal(value));
    }
    
    private Expression generateCategorize(Expression expression, java.util.function.BiFunction<Object, Object, Expression> parseValue) {
        
        JSONArray stopsJson = getStops();
        List<Expression> parameters = new ArrayList<>(stopsJson.size()*2+3); // each stop is 2, plus property name, leading interval value, and "succeeding"
        parameters.add(expression);
        for (int i = 0; i < stopsJson.size(); i++) {
            JSONArray entry = parse.jsonArray(stopsJson.get(i));
            Object stop = entry.get(0);
            Expression value = parseValue.apply(entry.get(1), stop);
            
            if (i == 0) {
                // CategorizeFunction expects there to be a leading value for inputs < firstStopThreshold.
                // But the MapBox spec does not define the expected behavior in that case.
                // (spec: "functions return the output value of the stop just less than the function input.")
                // TODO Default value could go here.
                // Temporarily, return first interval value for inputs < firstStopThreshold.
                parameters.add(value);
            }
            
            parameters.add(ff.literal(stop));
            parameters.add(value);
        }
        parameters.add(ff.literal("succeeding"));
        return ff.function("Categorize", parameters.toArray(new Expression[parameters.size()]));
    }
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
        return ff.function("Recode", parameters.toArray(new Expression[parameters.size()]));
    }
    
    //
    // Enumerations
    //
    /**
     * GeoTools {@link Expression} from json definition that evaluates to the provided Enum, used for
     * properties such as 'line-cap' and 'text-transform'.
     * <ul>
     * <li>{@link FunctionType#IDENTITY}: input is directly converted to an appropriate literal</li>
     * <li>{@link FunctionType#CATEGORICAL}: selects stop equal to input, and returns stop value as a literal</li>
     * <li>{@link FunctionType#INTERVAL}: selects stop less than numeric input, and returns stop value a literal</li>
     * </ul>
     * If type is unspecified internval is used as a default.</li>
     * 
     * @return {@link Function} (or identity {@link Expression} for the provided json)
     */
    public Expression enumeration( Class<? extends Enum<?>> enumeration){
        Expression input = input();
        FunctionType type = getTypeWithDefault(Enumeration.class);
        if (type == FunctionType.INTERVAL) {
            return enumGenerateCategorize(input,enumeration);
        }
        else if( type == FunctionType.CATEGORICAL){
            return enumGenerateRecode(input,enumeration);
        }
        else if( type == FunctionType.IDENTITY){
            return enumGenerateIdentiy(input, enumeration);
        }
        throw new UnsupportedOperationException("Unable to support '"+type+"' function for "+enumeration.getSimpleName());
    }
    /**
     * Utilty method used to convert enumerations to an appropriate literal string.
     * <p>
     * Any coversion between mapbox constants and geotools constants will be done here.
     * @param value
     * @param enumeration
     * @return Literal, or null if unavailable
     */
    private Literal constant( Object value, Class<? extends Enum<?>> enumeration){
        if( value == null ){
            return null;
        }
        if( value instanceof String){
            // step 1 look up enumValue
            String stringVal = (String) value;
            if ("".equals(stringVal.trim())) {
                return null;
            }
            Object enumValue = null;
            for (Object constant : enumeration.getEnumConstants()) {
                if (constant.toString().equalsIgnoreCase(stringVal.trim())) {
                    enumValue = constant;
                    break;
                }
            }
            if( enumValue == null ){
                throw new MBFormatException("\"" + stringVal + "\" invalid value for enumeration "
                    + enumeration.getSimpleName());
            }
            // step 2 - convert to geotools constant
            // (for now just convert to lowe case)
            //
            String literal = enumValue.toString().toLowerCase();
            
            return ff.literal(literal);
        }
        return null;
    }
    
    private Expression enumGenerateRecode(Expression input, Class<? extends Enum<?>> enumeration) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Object obj : getStops()) {
            JSONArray entry = parse.jsonArray(obj);
            Object stop = entry.get(0);
            Object value = entry.get(1);
            parameters.add(ff.literal(stop));
            parameters.add(constant(value,enumeration));
        }
        return ff.function("Recode", parameters.toArray(new Expression[parameters.size()]));
    }
    
    private Expression enumGenerateCategorize(Expression input,
            Class<? extends Enum<?>> enumeration) {
        return generateCategorize(input,(value, stop)->constant(value,enumeration));
    }

    private Expression enumGenerateIdentiy(Expression input, Class<? extends Enum<?>> enumeration) {
        // this is an interesting challenge, we need to generate a recode mapping
        // mapbox constants defined by the enum, to appropriate geotools literals
        List<Expression> parameters = new ArrayList<>();
        parameters.add(input);
        for (Enum<?> constant : enumeration.getEnumConstants()) {
            Object value = constant.name().toLowerCase();
            parameters.add(ff.literal(value));
            parameters.add(constant(value,enumeration));
        }
        return ff.function("Recode", parameters.toArray(new Expression[parameters.size()]));
    }
}
