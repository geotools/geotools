package org.geotools.mbstyle.parse;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.MBFillLayer;
import org.geotools.mbstyle.MBFormatException;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * Utility methods for parsing JSONObject values.
 * <p>
 * These utilities are used by the MBStyle to convert JSON to simple Java objects, process functions
 * and perform common JSON manipulation tasks.</p>  
 *
 * @author Torbien Barsballe (Boundless)
 */
public class MBObjectParser {
    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    public static JSONObject parseJSONObect(Object obj) throws MBFormatException {
        return parseJSONObect(obj, new MBFormatException("Not a JSON Object: " + toStringOrNull(obj)));
    }
    public static JSONObject parseJSONObect(Object obj, String message) throws MBFormatException {
        return parseJSONObect(obj, new MBFormatException(message));
    }
    public static JSONObject parseJSONObect(Object obj, MBFormatException exception) throws MBFormatException {
    if (obj instanceof JSONObject) {
        return (JSONObject) obj;
    }
        throw exception;
    }
    public static JSONArray parseJSONArray(Object obj) throws MBFormatException {
        return parseJSONArray(obj, new MBFormatException("Not a JSON Array: " + toStringOrNull(obj)));
    }
    public static JSONArray parseJSONArray(Object obj, String message) throws MBFormatException {
        return parseJSONArray(obj, new MBFormatException(message));
    }
    public static JSONArray parseJSONArray(Object obj, MBFormatException exception) throws MBFormatException {
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw exception;
    }
    public static String parseJSONString(Object obj) throws MBFormatException {
        return parseJSONString(obj, new MBFormatException("Not a String: " + toStringOrNull(obj)));
    }
    public static String parseJSONString(Object obj, String message) throws MBFormatException {
        return parseJSONString(obj, new MBFormatException(message));
    }
    public static String parseJSONString(Object obj, MBFormatException exception) throws MBFormatException {
        if (obj instanceof String) {
            return (String) obj;
        }
        throw exception;
    }

    public static MBStyle parseRoot(Object obj) throws MBFormatException {
        return new MBStyle(parseJSONObect(obj, "Root must be a JSON Object: " + toStringOrNull(obj)));
    }
    public static MBLayer parseLayer(Object obj) throws MBFormatException {
        JSONObject layer = parseJSONObect(obj, "Layer must be a JSON Object: " + toStringOrNull(obj));
        String type = parseJSONString(layer.get("type"), "\"type\" must be a String: " + toStringOrNull(obj));
        if ("background".equals(type)) {
            throw new UnsupportedOperationException("\"type\" background is not supported");
        } else if ("fill".equals(type)) {
            return new MBFillLayer(layer);
        } else if ("line".equals(type)) {
            throw new UnsupportedOperationException("\"type\" background is not supported");
        } else if ("symbol".equals(type)) {
            throw new UnsupportedOperationException("\"type\" background is not supported");
        } else if ("raster".equals(type)) {
            throw new UnsupportedOperationException("\"type\" background is not supported");
        } else if ("circle".equals(type)) {
            throw new UnsupportedOperationException("\"type\" background is not supported");
        } else if ("fill-extrusion".equals(type)) {
            throw new UnsupportedOperationException("\"type\" background is not supported");
        }
        throw new MBFormatException(("\"type\" "+type+" is not a valid layer type. Must be one of: " +
                "background, fill, line, symbol, raster, circle, fill-extrusion"));
    }
    
    public static String toStringOrNull(Object obj) {
        return obj == null ? "null" : obj.toString();
    }
    /**
     * Convert json to Expression number between 0 and 1, or a function.
     *  
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException 
     */
    public static Expression percentage(JSONObject json, String tag, Number fallback) throws MBFormatException {
        if( json == null ){
            return ff.literal(fallback);
        }
        Object obj = json.get(tag);
        
        if( obj instanceof String){
            String str = (String) obj;
            return ff.literal(str);
        }
        else if (obj instanceof Number){
            Number number = (Number) obj;
            return ff.literal(number);
        }
        else if(obj instanceof Boolean){
            throw new MBFormatException("\""+tag+"\" percentage from boolean "+obj+" not supported, expected value between 0 and 1");
        }
        else if(obj instanceof JSONObject){
            throw new UnsupportedOperationException("\""+tag+"\"precentage from Function not yet supported");
        }
        else if(obj instanceof JSONArray){
            throw new MBFormatException("\""+tag+"\" percentage from JSONArray not supported, expected value between 0 and 1");
        }
        else {
            throw new IllegalArgumentException("json contents invalid, \""+tag+"\" value limited to Number or JSONObject but was "+obj.getClass().getSimpleName());
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
    public static Expression string(JSONObject json, String tag, String fallback) throws MBFormatException {
        if( json == null ){
            return ff.literal(fallback);
        }
        Object obj = json.get(tag);
        
        if( obj instanceof String){
            String str = (String) obj;
            return ff.literal(str);
        }
        else if (obj instanceof Number){
            Number number = (Number) obj;
            return ff.literal(number.toString());
        }
        else if(obj instanceof Boolean){
            Boolean bool = (Boolean) obj;
            return ff.literal(bool.toString());
        }
        else if(obj instanceof JSONObject){
            throw new UnsupportedOperationException("\""+tag+"\" string from Function not yet supported");
        }
        else if(obj instanceof JSONArray){
            throw new MBFormatException("\""+tag+"\" string from JSONArray not supported");
        }
        else {
            throw new IllegalArgumentException("json contents invalid, \""+tag+"\" value limited to String, Number, Boolean or JSONObject but was "+obj.getClass().getSimpleName());
        }
    }
}
