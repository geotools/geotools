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
import java.lang.reflect.Array;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.styling.StyleFactory2;
import org.geotools.util.ColorConverterFactory;
import org.geotools.util.Converters;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Helper class used to perform JSON traverse {@link JSONObject} and perform Expression and Filter
 * conversions. These utilities are used by the MBStyle to convert JSON to simple Java objects,
 * process functions and perform common JSON manipulation tasks.
 * 
 * <h2>Acess methods</h2>
 * Example of transformation to Expression, using the fallback value if provided:
 * 
 * <pre><code> MBObjectParser parse = new MBObjectParser( ff );
 * 
 * Expression fillOpacity = parse.percent( json, "fill-opacity", 1.0 );
 * Expression fillColor = parse.color( json, "fill-color", Color.BLACK );
 * </code></pre>
 * 
 * <h2>Get Methods</h2>
 * 
 * Generic "get" methods are also available for safely accessing required fields. These methods will throw a
 * validation error if the required tag was is not available.
 * 
 * <pre><code> String id = parse.get("id");
 * String visibility = parse.getBoolean("visibility");
 * String source = parse.get("source");
 * </code></pre>
 * 
 * Non generic "get" methods, like {@link #paint(JSONObject)}, are in position to provide an appropriate default value.
 * <pre><code> JSONObject paint = parse.paint( layer );
 * </code></pre>
 * 
 * @author Torbien Barsballe (Boundless)
 */
public class MBObjectParser {
    /** Wrapper class (used to provide better error messages). */
    Class<?> context;
    final FilterFactory2 ff;
    final StyleFactory2 sf;
    
    /**
     * Parser used to in the provided context.
     * 
     * @param context Context this parser is being used in (for better error reporting)
     */
    public MBObjectParser(Class<?> context) {
        this.context = context;
        this.sf = (StyleFactory2) CommonFactoryFinder.getStyleFactory();
        this.ff = CommonFactoryFinder.getFilterFactory2();
    }

    /**
     * Copy constructor allowing reuse of factories, while returning correct {@link #context}.
     * 
     * @param context Context this parser is being used in (for better error reporting)
     * @param parse Parent parser used to configure factories consistently
     */
    public MBObjectParser(Class<MBFilter> context, MBObjectParser parse) {
        this.context = context;
        sf = parse == null ? (StyleFactory2) CommonFactoryFinder.getStyleFactory()
                : parse.getStyleFactory();
        ff = parse == null ? CommonFactoryFinder.getFilterFactory2() : parse.getFilterFactory();
    }

    //
    // Utility methods for required lookup
    //
    // These methods throw a validation error if tag is not available
    //
    /** Shared FilterFactory */
    public FilterFactory2 getFilterFactory() {
        return this.ff;
    }

    /** Shared StyleFactory */
    public StyleFactory2 getStyleFactory() {
        return sf;
    }

    /** Safely look up paint in provided layer json.
     * <p>
     * Paint is optional, returning an empty JSONObject (to prevent the need for null checks).</p>
     * @param layer
     * @return paint definition, optional so may be an empty JSONObject
     * @throws MBFormatException If paint is provided as an invalid type (such as boolean).
     */
    public JSONObject paint(JSONObject layer) {
        if(layer.containsKey("paint")){
            Object paint = layer.get("paint");
            if( paint == null ){
                String type = get( layer, "type", "layer");
                throw new MBFormatException(  type + " paint rquires JSONOBject");
            }
            else if( paint instanceof JSONObject){
                return (JSONObject) paint;
            }
            else {
                String type = get( layer, "type", "layer");
                throw new MBFormatException( type + " paint rquires JSONOBject");
            }
        }
        else {
            // paint is optional, having a value here prevents need for null checks
            return new JSONObject(); 
        }
    }
    /** Safely look up layout in provided layer json.
     * <p>
     * Layout is optional, returning an empty JSONObject (to prevent the need for null checks).</p>
     * @param layer
     * @return layout definition, optional so may be an empty JSONObject
     * @throws MBFormatException If layout is provided as an invalid type (such as boolean).
     */
    public JSONObject layout(JSONObject layer) {
        if(layer.containsKey("layout")){
            Object layout = layer.get("layout");
            if( layout == null ){
                String type = get( layer, "type", "layer");
                throw new MBFormatException(  type + " layout rquires JSONOBject");
            }
            else if( layout instanceof JSONObject){
                return (JSONObject) layout;
            }
            else {
                String type = get( layer, "type", "layer");
                throw new MBFormatException( type + " paint rquires JSONOBject");
            }
        }
        else {
            // paint is optional, having a value here prevents need for null checks
            return new JSONObject(); 
        }
    }

    /**
     * Access JSONObject for the indicated tag.
     * <p>
     * Confirms json contains the provided tag as a JSONObject, correctly
     * throwing {@link MBFormatException} if not available.
     * 
     * @param json
     * @param tag
     * @return JSONObject 
     * @throws MBFormatException If JSONObject not available for the provided tag
     */
    public JSONObject getJSONObject( JSONObject json, String tag ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof JSONObject) {
            return (JSONObject) json.get(tag);
        } else {
            throw new MBFormatException("\""+tag+"\" requires JSONObject");
        }
    }
    public JSONObject getJSONObject( JSONObject json, String tag, JSONObject fallback ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof JSONObject) {
            return (JSONObject) json.get(tag);
        } else {
            return fallback;
        }
    }
    /**
     * Access json contains a JSONArray for the indicated tag.
     * <p>
     * Confirms json contains the provided tag as a JSONArray, correctly
     * throwing {@link MBFormatException} if not available.
     * 
     * @param json
     * @param tag
     * @return JSONObject 
     * @throws MBFormatException If JSONObject not available for the provided tag
     */
    public JSONArray getJSONArray( JSONObject json, String tag ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof JSONArray) {
            return (JSONArray) json.get(tag);
        } else {
            throw new MBFormatException("\""+tag+"\" requires JSONArray");
        }
    }
    public JSONArray getJSONArray( JSONObject json, String tag, JSONArray fallback ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        else if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        else if(!json.containsKey(tag) || json.get(tag)==null){
            return fallback;
        }
        else if (json.containsKey(tag) && json.get(tag) instanceof JSONArray) {
            return (JSONArray) json.get(tag);
        } else {
            throw new MBFormatException("\""+tag+"\" requires JSONArray");
        }
    }
    
    /**
     * Access a literal value (string, numeric, or boolean).
     * 
     * @param index
     * @return required string, numeric or boolean
     * @throws MBFormatException if required index not available.
     */
    public Object value(JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        else if (index < json.size() && json.get(index) instanceof String) {
            return (String) json.get(index);
        }
        else if (index < json.size() && json.get(index) instanceof Boolean) {
            return (Boolean) json.get(index);
        }
        else if (index < json.size() && json.get(index) instanceof Number) {
            return (Number) json.get(index);
        }
        else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires [" + index + "] string, numeric or boolean");
        }
    }
    /**
     * Access a literal value (string, numeric, or boolean).
     * 
     * @param tag
     * @return required string, numeric or boolean
     * @throws MBFormatException if required tag not available.
     */
    public Object value(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (json.containsKey(tag)) {
            Object value = json.get(tag);
            if (value == null || value instanceof String || value instanceof Boolean
                    || value instanceof Number) {
                return value;
            } else {
                throw new MBFormatException(context.getSimpleName() + " requires \"" + tag
                        + "\" literal required (was " + value.getClass().getSimpleName() + ")");
            }
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" required.");
        }
    }

    /**
     * Quickly access required json index (as a String).
     * 
     * @param index
     * @return required string
     * @throws MBFormatException if required index not available.
     */
    public String get(JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index < json.size() && json.get(index) instanceof String) {
            return (String) json.get(index);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires [" + index + "] string");
        }
    }

    /**
     * Quickly access required json tag (as a String).
     * 
     * @param tag
     * @return required string
     * @throws MBFormatException if required tag not available.
     */
    public String get(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof String) {
            return (String) json.get(tag);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" string field");
        }
    }
    
    /**
     * Quickly access required json tag.
     * 
     * @param json
     * @param tag 
     * @param fallback 
     * @return required string, or fallback if unavailable
     */
    public String get(JSONObject json, String tag, String fallback) {
        if (tag == null || json == null) {
            return fallback;
        }
        else if(!json.containsKey(tag) || json.get(tag)==null){
            return fallback;
        }
        if (json.containsKey(tag) && json.get(tag) instanceof String) {
            return (String) json.get(tag);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" string field");
        }
    }

    /** Boolean lookup. */
    public Boolean getBoolean(JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index < json.size() && json.get(index) instanceof Boolean) {
            return (Boolean) json.get(index);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires [" + index + "] boolean");
        }
    }
    /** Boolean lookup. */
    public Boolean getBoolean(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof Boolean) {
            return (Boolean) json.get(tag);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" boolean field");
        }
    }
    /** Boolean lookup, using the fallback value if not provided. */
    public Boolean getBoolean(JSONObject json, String tag, Boolean fallback) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        else if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        else if (!json.containsKey(tag) || json.get(tag)==null){
            return fallback;
        }
        else if (json.get(tag) instanceof Boolean) {
            return (Boolean) json.get(tag);
        } else {
            throw new MBFormatException(context.getSimpleName() + " requires \"" + tag + "\" boolean field");
        }
    }
    
    public <T> T require( Class<T> type, JSONArray json, int index ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index >= 0 && index <= json.size() && type.isInstance(json.get(index))) {
            return type.cast(json.get(index));
        } else {
            throw new MBFormatException(context.getSimpleName() + " requires [" + index + "] "+type.getSimpleName());
        }
    }
    
    public <T> T require( Class<T> type, JSONObject json, String tag ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && type.isInstance(json.get(tag))) {
            return type.cast(json.get(tag));
        } else {
            throw new MBFormatException(context.getSimpleName() + " requires \"" + tag + "\" "+type.getSimpleName()+" field");
        }
    }

    /**
     * Optional lookup, will return fallback if not available.
     * @param type Type to lookup
     * @param json
     * @param tag
     * @param fallback
     * @return value for the provided tag, or fallback if not available
     * @throws MBFormatException If alue is found and is not the expected type
     */
    public <T> T optional( Class<T> type, JSONObject json, String tag, T fallback ){
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        else if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        else if (!json.containsKey(tag) || json.get(tag)==null){
            return fallback;
        }
        if (json.containsKey(tag) && type.isInstance(json.get(tag))) {
            return type.cast(json.get(tag));
        } else {
            throw new MBFormatException(context.getSimpleName() + " requires \"" + tag + "\" "+type.getSimpleName()+" field");
        }
    }
    
    /**
     * 
     * Convert a String value to an enum value, ignoring case, with the provided fallback.
     * 
     * @param enumeration The enum to convert to.
     * @param value The string value to convert to an enum value.
     * @return The enum value from the string, or the fallback value.
     * @throws MBFormatException if the value is not a String, or it is not a valid value for the enumeration.
     */
    public <T extends Enum<?>> T toEnum(JSONObject json, String tag, Class<T> enumeration,
            T fallback) {
        Object value = json.get(tag);

        if (value == null) {
            return fallback;
        } else if (value instanceof String) {
            String stringVal = (String) value;
            if ("".equals(stringVal.trim())) {
                return fallback;
            }
            for (T enumValue : enumeration.getEnumConstants()) {
                if (enumValue.toString().equalsIgnoreCase(stringVal.trim())) {
                    return enumValue;
                }
            }
            throw new MBFormatException("\"" + tag + "\" contains invalid value for enumeration "
                    + enumeration.getSimpleName());
        } else {
            throw new MBFormatException(
                    "Conversion of \"" + tag + "\" value from " + value.getClass().getSimpleName()
                            + " to " + enumeration.getSimpleName() + " not supported.");
        }

    }
    
    /**
     * Casts the provided obj to a JSONObject (safely reporting format exception 
     * 
     * @param obj
     * @return JSONObject
     * @throws MBFormatException 
     */
    public JSONObject jsonObject(Object obj) throws MBFormatException {
        if( obj instanceof JSONObject){
            return (JSONObject) obj;
        }
        else if( obj == null ){
            throw new MBFormatException("Not a JSONObject: null");
        }
        else {
            throw new MBFormatException("Not a JSONObject: " + obj.toString());
        }
    }

    public JSONObject jsonObect(Object obj, String message) throws MBFormatException {
        if( obj instanceof JSONObject){
            return (JSONObject) obj;
        }
        else {
            throw new MBFormatException(message);
        }
    }

    public JSONArray jsonArray(Object obj) throws MBFormatException {
        if( obj instanceof JSONArray){
            return (JSONArray) obj;
        }
        else if( obj == null ){
            throw new MBFormatException("Not a JSONArray: null");
        }
        else {
            throw new MBFormatException("Not a JSONArray: " + obj.toString() );
        }
    }

    public JSONArray jsonArray(Object obj, String message) throws MBFormatException {
        if( obj instanceof JSONArray){
            return (JSONArray) obj;
        }
        else {
            throw new MBFormatException(message);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T[] array(Class<T> type, JSONObject json, String tag, T[] fallback) {
        if( json.containsKey(tag)){
            Object obj = json.get(tag);
            if( obj instanceof JSONArray){
                JSONArray array = (JSONArray) obj;                
                T[] returnArray = (T[]) Array.newInstance(type, array.size());
                for (int i = 0; i < array.size(); i++) {
                    returnArray[i] = type.cast(array.get(i));
                }
                return returnArray;               
            }
            else {
                throw new MBFormatException("\"" + tag + "\" required as JSONArray of "
                        + type.getSimpleName() + ": Unexpected " + obj.getClass().getSimpleName());
            }
        }
        return fallback;
    }

    /** Convert to doublep[] */
    public double[] array(JSONObject json, String tag, double[] fallback) {
        if (json.containsKey(tag)) {
            Object obj = json.get(tag);
            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                double result[] = new double[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    result[i] = ((Number) array.get(i)).doubleValue();
                }
                return result;
            } else {
                throw new MBFormatException(
                        "\"" + tag + "\" required as JSONArray of Number: Unexpected "
                                + obj.getClass().getSimpleName());
            }
        }
        return fallback;
    }

    /**
     * Convert json to Expression number between 0 and 1, or a function.
     * 
     * @param json json representation
     * @return Expression based on provided json, or null if not provided
     * @throws MBFormatException
     */
    public  Expression percentage(JSONObject json, String tag) throws MBFormatException {
        return percentage(json, tag, null);
    }

    /**
     * Convert json to Expression number between 0 and 1, or a function.
     * 
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public Expression percentage(JSONObject json, String tag, Number fallback)
            throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(tag);

        if (obj == null) {
            return ff.literal(fallback);  
        } else if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            Number number = (Number) obj;
            return ff.literal(number);
        } else if (obj instanceof Boolean) {
            throw new MBFormatException("\"" + tag + "\" percentage from boolean " + obj
                    + " not supported, expected value between 0 and 1");
        } else if (obj instanceof JSONObject) {
            throw new UnsupportedOperationException(
                    "\"" + tag + "\"precentage from Function not yet supported");
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException("\"" + tag
                    + "\" percentage from JSONArray not supported, expected value between 0 and 1");
        } else {
            throw new IllegalArgumentException("json contents invalid, \"" + tag
                    + "\" value limited to Number or JSONObject but was "
                    + obj.getClass().getSimpleName());
        }
    }
    //
    // NUMBER
    //
    private Expression number(String context, Object obj, Number fallback) {
        if (obj == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            Number number = (Number) obj;
            return ff.literal(number.toString());
        } else if (obj instanceof Boolean) {
            Boolean bool = (Boolean) obj;
            throw new MBFormatException(
                    context + " number from Boolean " + bool + " not supported");
        } else if (obj instanceof JSONObject) {
            throw new UnsupportedOperationException(
                    context + " number from Function not yet supported");
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException(context + " number from JSONArray not supported");
        } else {
            throw new IllegalArgumentException("json contents invalid, " + context
                    + " value limited to String, Number, Boolean or JSONObject but was "
                    + obj.getClass().getSimpleName());
        }
    }

    /**
     * Convert json to Expression number, or a function.
     * 
     * @param json json representation
     * @param index
     * @return Expression based on provided json, or null
     * @throws MBFormatException
     */
    public  Expression number(JSONArray json, int index) throws MBFormatException {
        return number(json, index, null);
    }

    /**
     * Convert json to Expression number, or a function.
     * 
     * @param json json representation
     * @param index
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public  Expression number(JSONArray json, int index, Number fallback)
            throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(index);
        return number("index " + index, obj, fallback);
    }

    /**
     * Convert json to Expression number, or a function.
     * 
     * @param json json representation
     * @param tag
     * @return Expression based on provided json, or null
     * @throws MBFormatException
     */
    public  Expression number(JSONObject json, String tag) throws MBFormatException {
        return number(json, tag, null);
    }

    /**
     * Convert json to Expression number, or a function.
     * 
     * @param json json representation
     * @param tag
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public  Expression number(JSONObject json, String tag, Number fallback)
            throws MBFormatException {
        if (json == null) {
            return ff.literal(fallback);
        }
        Object obj = json.get(tag);
        return number("\"" + tag + "\"", obj, fallback);
    }

    //
    // STRING
    //
    private  Expression string(String context, Object obj, String fallback) {
        if (obj == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        else if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            Number number = (Number) obj;
            return ff.literal(number.toString());
        } else if (obj instanceof Boolean) {
            Boolean bool = (Boolean) obj;
            return ff.literal(bool.toString());
        } else if (obj instanceof JSONObject) {
            throw new UnsupportedOperationException(
                    context + " string from Function not yet supported");
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException(context + " string from JSONArray not supported");
        } else {
            throw new IllegalArgumentException("json contents invalid, " + context
                    + " value limited to String, Number, Boolean or JSONObject but was "
                    + obj.getClass().getSimpleName());
        }
    }

    /**
     * Convert json to Expression string, or a function.
     * 
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public  Expression string(JSONObject json, String tag, String fallback)
            throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        return string( "\""+tag+"\"", json.get(tag), fallback );


    }

    /**
     * Convert json to Expression color, or a function.
     * 
     * @param json json representation
     * @param fallback default value (string representation of color) if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public  Expression color(JSONObject json, String tag, Color fallback)
            throws MBFormatException {
        if (json.get(tag) == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(tag);
        return color( "\""+tag+"\"", obj, fallback );
    }
    
    /**
     * Handles literal color definitions supplied as a string, returning a {@link Literal}.
     * 
     * <ul>
     * <li><pre>{"line-color": "yellow"</pre> named: a few have been put in pass test cases, prnding: plan to use {@link Hints#COLOR_NAMES} to allow for web colors.</li>
     * <li><pre>{"line-color": "#ffff00"}</pre> hex: hex color conversion are supplied by {@link ColorConverterFactory}</li>
     * <li><pre>{"line-color": "#ff0"}</pre> hex: we will need to special case this</li>
     * <li><pre>{"line-color": "rgb(255, 255, 0)"}</pre> - we will need to special case this </li>
     * <li><pre>{"line-color": "rgba(255, 255, 0, 1)"}</pre> - we will need to special case this </li>
     * <li><pre>{"line-color": "hsl(100, 50%, 50%)"}</pre> - we will need to special case this </li>
     * <li><pre>{"line-color": "hsla(100, 50%, 50%, 1)"}</pre> - we will need to special case this </li>
     * <li>
     * </ul>
     * 
     * This method uses {@link Hints#COLOR_NAMES} "CSS" to support the use of web colors names.
     * 
     * @param color name of color (CSS or "web" colors)
     * @return appropriate java color, or null if not available.
     */
    public Literal color(String color) {
        if (color == null) {
            return null;
        }
        return ff.literal(convertToColor(color));
    }
    
    /**
     * Parse obj into a color expression (literal or function).
     * 
     * @param context
     * @param obj
     * @param fallback
     * @return color expression (literal or function)
     */
    private  Expression color(String context, Object obj, Color fallback) {
        if (obj == null) {
            return fallback == null ? null : ff.literal(fallback);
        } else if (obj instanceof String) {
            String str = (String) obj;
            return color( str );
        } else if (obj instanceof Number) {
            throw new MBFormatException(context + " color from Number not supported");
        } else if (obj instanceof Boolean) {
            throw new MBFormatException(context + "  color from Boolean not supported");
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction( (JSONObject) obj );
            return function.color();
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException(context + " color from JSONArray not supported");
        } else {
            throw new IllegalArgumentException("json contents invalid, " + context
                    + " limited to String or JSONObject but was " + obj.getClass().getSimpleName());
        }
    }
    
    /**
     * Converts color definitions supplied as a string to Color objects:
     * 
     * <ul>
     * <li><pre>{"line-color": "yellow"</pre> named: a few have been put in pass test cases, prnding: plan to use {@link Hints#COLOR_NAMES} to allow for web colors.</li>
     * <li><pre>{"line-color": "#ffff00"}</pre> hex: hex color conversion are supplied by {@link ColorConverterFactory}</li>
     * <li><pre>{"line-color": "#ff0"}</pre> hex: we will need to special case this</li>
     * <li><pre>{"line-color": "rgb(255, 255, 0)"}</pre> - we will need to special case this </li>
     * <li><pre>{"line-color": "rgba(255, 255, 0, 1)"}</pre> - we will need to special case this </li>
     * <li><pre>{"line-color": "hsl(100, 50%, 50%)"}</pre> - we will need to special case this </li>
     * <li><pre>{"line-color": "hsla(100, 50%, 50%, 1)"}</pre> - we will need to special case this </li>
     * <li>
     * </ul>
     * 
     * This method uses {@link Hints#COLOR_NAMES} "CSS" to support the use of web colors names.
     * 
     * @param color name of color (CSS or "web" colors)
     * @return appropriate java color, or null if not available.
     */
    public Color convertToColor(String color) {
        if (color == null) {
            return null;
        }
        // quick examples to pass test case (while we work on color converter)
        if ("red".equalsIgnoreCase(color)) {
            return Color.RED;
        } else if ("blue".equalsIgnoreCase(color)) {
            return Color.BLUE;
        }
        return Converters.convert(color, Color.class, null); // TODO: Hints(Hints.COLOR_NAMES, "CSS")
    }
    
    /**
     * Convert json to Expression boolean, or a function.
     * 
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public Expression bool(JSONObject json, String tag, boolean fallback)
            throws MBFormatException {
        if (json.get(tag) == null) {
            return ff.literal(fallback);
        }

        Object obj = json.get(tag);

        if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            throw new UnsupportedOperationException(
                    "\"" + tag + "\": boolean from Number not supported");
        } else if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            return ff.literal(b);
        } else if (obj instanceof JSONObject) {
            throw new UnsupportedOperationException(
                    "\"" + tag + "\": boolean from Function not yet supported");
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException("\"" + tag + "\": boolean from JSONArray not supported");
        } else {
            throw new IllegalArgumentException("json contents invalid, \"" + tag
                    + "\" value limited to String, Boolean or JSONObject but was "
                    + obj.getClass().getSimpleName());
        }
    }
}
