package org.geotools.mbstyle.parse;

import java.awt.Color;

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
 * and perform common JSON manipulation tasks.
 * </p>
 *
 * @author Torbien Barsballe (Boundless)
 */
public class MBObjectParser {
    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    public static JSONObject parseJSONObect(Object obj) throws MBFormatException {
        return parseJSONObect(obj,
                new MBFormatException("Not a JSON Object: " + toStringOrNull(obj)));
    }

    public static JSONObject parseJSONObect(Object obj, String message) throws MBFormatException {
        return parseJSONObect(obj, new MBFormatException(message));
    }

    public static JSONObject parseJSONObect(Object obj, MBFormatException exception)
            throws MBFormatException {
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw exception;
    }

    public static JSONArray parseJSONArray(Object obj) throws MBFormatException {
        return parseJSONArray(obj,
                new MBFormatException("Not a JSON Array: " + toStringOrNull(obj)));
    }

    public static JSONArray parseJSONArray(Object obj, String message) throws MBFormatException {
        return parseJSONArray(obj, new MBFormatException(message));
    }

    public static JSONArray parseJSONArray(Object obj, MBFormatException exception)
            throws MBFormatException {
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

    public static String parseJSONString(Object obj, MBFormatException exception)
            throws MBFormatException {
        if (obj instanceof String) {
            return (String) obj;
        }
        throw exception;
    }

    public static MBStyle parseRoot(Object obj) throws MBFormatException {
        return new MBStyle(
                parseJSONObect(obj, "Root must be a JSON Object: " + toStringOrNull(obj)));
    }

    public static MBLayer parseLayer(Object obj) throws MBFormatException {
        JSONObject layer = parseJSONObect(obj,
                "Layer must be a JSON Object: " + toStringOrNull(obj));
        String type = parseJSONString(layer.get("type"),
                "\"type\" must be a String: " + toStringOrNull(obj));
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
        throw new MBFormatException(
                ("\"type\" " + type + " is not a valid layer type. Must be one of: "
                        + "background, fill, line, symbol, raster, circle, fill-extrusion"));
    }

    public static String toStringOrNull(Object obj) {
        return obj == null ? "null" : obj.toString();
    }

    /**
     * Convert json to Expression number between 0 and 1, or a function.
     * 
     * @param json json representation
     * @return Expression based on provided json, or null if not provided
     * @throws MBFormatException
     */
    public static Expression percentage(JSONObject json, String tag) throws MBFormatException {
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
    public static Expression percentage(JSONObject json, String tag, Number fallback)
            throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(tag);

        if (obj instanceof String) {
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
    private static Expression number(String context, Object obj, Number fallback) {
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
    public static Expression number(JSONArray json, int index) throws MBFormatException {
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
    public static Expression number(JSONArray json, int index, Number fallback)
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
    public static Expression number(JSONObject json, String tag) throws MBFormatException {
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
    public static Expression number(JSONObject json, String tag, Number fallback)
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
    private static Expression string(String context, Object obj, String fallback) {
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
    public static Expression string(JSONObject json, String tag, String fallback)
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
    public static Expression color(JSONObject json, String tag, Color fallback)
            throws MBFormatException {
        if (json.get(tag) == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(tag);
        return color( "\""+tag+"\"", obj, fallback );
    }

    private static Expression color(String context, Object obj, Color fallback) {
        if (obj == null) {
            return fallback == null ? null : ff.literal(fallback);
        } else if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            throw new MBFormatException(context + " color from Number not supported");
        } else if (obj instanceof Boolean) {
            throw new MBFormatException(context + "  color from Boolean not supported");
        } else if (obj instanceof JSONObject) {
            throw new UnsupportedOperationException(
                    context + " color from Function not yet supported");
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException(context + " color from JSONArray not supported");
        } else {
            throw new IllegalArgumentException("json contents invalid, " + context
                    + " limited to String or JSONObject but was " + obj.getClass().getSimpleName());
        }
    }
    
    /**
     * Convert json to Expression boolean, or a function.
     * 
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     * @throws MBFormatException
     */
    public static Expression bool(JSONObject json, String tag, boolean fallback)
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
