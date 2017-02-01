package org.geotools.mbstyle.parse;

import org.geotools.mbstyle.MBFillLayer;
import org.geotools.mbstyle.MBFormatException;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Utility methods for parsing JSONObject values
 *
 * @author tbarsballe
 */
public class MBObjectParser {
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
            return (JString) obj;
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
}
