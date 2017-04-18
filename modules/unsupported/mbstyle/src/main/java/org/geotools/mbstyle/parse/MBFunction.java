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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.geotools.coverage.processing.operation.Interpolate;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.InterpolateFunction;
import org.geotools.filter.function.RecodeFunction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * MBFunction json wrapper, allowing conversion to a GeoTools Expression.
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
        IDENITY,
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

    public FunctionType getType() {
        String type = parse.get(json, "type", "exponential");
        switch (type) {
        case "identity":
            return FunctionType.IDENITY;
        case "exponential":
            return FunctionType.EXPONENTIAL;
        case "internval":
            return FunctionType.INTERVAL;
        case "categorical":
            return FunctionType.CATEGORICAL;
        default:
            throw new MBFormatException("Function type \"" + type
                    + "\" invalid - expected identity, expontential, interval, categorical");
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

    /** GeoTools {@link Function} from json definition that evaluates to a color.
     * <p>
     * This is the same as {@link #numeric()} except we can make some assumptions about
     * the values (converting hex to color, looking up color names).
     * 
     * @return {@link Function} (or identity {@link Expression} for the provided json 
     */
    public Expression color(){
        Expression value = value();
        // this is a plain property category so we can turn it into a function
        // Assume a EXPOTENTIAL function for now because it is the default for color
        FunctionType type = getType();
        if( type == null || type == FunctionType.CATEGORICAL){
            return colorCategorical(value); // CATEGORICAL is the default
        }
        else if( type == FunctionType.EXPONENTIAL){
            return colorExponential(value);
        }
        else if( type == FunctionType.INTERVAL){
            return colorInterval(value);
        }
        else if( type == FunctionType.IDENITY){
            return value;
        }
        throw new UnsupportedOperationException("Not yet implemented support for '"+type+"' function");
    }
    
    /**
     * Extracts value expression this function is performed against.
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
    private Expression value() {
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

    private Expression colorInterval(Expression expression) {
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
        return ff.function("Categorize", parameters.toArray(new Expression[parameters.size()]));
    }
    
    /**
     * Used to covert a zoom level function that has been reduced to two stops.
     * @param expression
     * @return
     */
    private Expression colorZoomExponential() {
        Expression zoomLevel = ff.function("zoomLevel",
                ff.function("env", ff.literal("wms_scale_denominator")),
                ff.literal("EPSG:3857")
        );
        List<Expression> parameters = new ArrayList<>();
        
        // See FilterFunction_pow: pow( base, exponent ): power
        Expression base = parse.number(json, "base", null);
        if( base == null ){
            base = ff.literal(1.0);
        }
//        JSONArray stops = getStops();
//        Object stop1 = entry.get(0);
//        Expression color1 = parse.color(entry.get(1));
//        Object stop2 = entry.get(2);
//        Expression color2 = parse.color(entry.get(3));
        
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
    /**
     * Used to 
     * @param expression
     * @return
     */
    private Expression colorExponential(Expression expression) {
        List<Expression> parameters = new ArrayList<>();
        
        // See FilterFunction_pow: pow( base, exponent ): power
        Expression base = parse.number(json, "base", null);
        if( base != null ){
            Function power = ff.function("pow", expression, ff.divide(ff.literal(1.0),base));
            parameters.add(power);
        }
        else {
            parameters.add(expression);
        }

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

    private Expression colorCategorical(Expression expression) {
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
        return ff.function("Recode", parameters.toArray(new Expression[parameters.size()]));
    }
    /**
     * Generate GeoTools {@link Function} from json definition.
     * <p>
     * This method is used for numeric properties such as line width or opacity.</p>
     * <p>
     * This only works for concrete functions, that have been resolved to the current zoom level.</p>.
     * 
     * @return GeoTools Function or the provided json
     */
    public Function numeric() {
        if( !category().contains(FunctionCategory.ZOOM)){
            // this is a plain property category so we can turn it into a function
            
            // assume a EXPOTENTIAL function for now because it is the default
            // TODO: make a special case for mapbox style enums which default to CATEGORICAL
            FunctionType type = FunctionType.CATEGORICAL;
            
            if( type == FunctionType.CATEGORICAL){
                return categorical();
            }
        }
        throw new UnsupportedOperationException("Not yet implemented support for this function");
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
     * @return
     */
    private Expression numericExponential(Expression value){
        
        // See FilterFunction_pow: pow( base, exponent ): power
//        Expression base = parse.number(json, "base", null);
//        if( base == null ){
//            base = ff.literal(1.0);
//        }
        double base = Double.valueOf( (String) json.get("base"));
        JSONArray stops = getStops();
        JSONArray entry1 = parse.jsonArray(stops.get(0));
        double stop1 = Double.valueOf((String)entry1.get(0));
        double value1 = Double.valueOf((String)entry1.get(1));
        
      
        JSONArray entry2 = parse.jsonArray(stops.get(1));
        double stop2 = Double.valueOf((String)entry2.get(0));
        double value2 = Double.valueOf((String)entry2.get(1));
        
        double scale = (value2-value1)/(Math.pow(stop2, base) - Math.pow(stop1, base));
        double offset = value1-scale*Math.pow(stop1, base);
        
        return ff.add(
                ff.literal(offset),
                ff.multiply(
                        ff.literal(scale),
                        ff.function("pow", value, ff.literal(base))
                    )
                );
    }
    /**
     * Create a cateogircal functions as {@link InterpolateFunction} for
     * {@link FunctionType#CATEGORICAL}.
     * 
     * @return Generated InterpolateFunction
     */
    public Function categorical() {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(ff.property(getProperty()));
        for (Object obj : getStops()) {
            JSONArray stop = parse.jsonArray(obj);
            parameters.add(ff.literal(stop.get(0)));
            parameters.add(ff.literal(stop.get(1)));
        }
        return ff.function("Interpolate", parameters.toArray(new Expression[parameters.size()]));
    }
}
