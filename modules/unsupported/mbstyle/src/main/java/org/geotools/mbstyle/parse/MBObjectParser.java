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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.util.ColorConverterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.expression.MBExpression;
import org.geotools.mbstyle.layer.LineMBLayer.LineJoin;
import org.geotools.styling.Displacement;
import org.geotools.styling.StyleFactory2;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Helper class used to perform JSON traversal of {@link JSONObject} and perform Expression and
 * Filter conversions. These utilities are used by the MBStyle to convert JSON to simple Java
 * objects, process functions and perform common JSON manipulation tasks.
 *
 * <h2>Access methods</h2>
 *
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
 * Generic "get" methods are also available for safely accessing required fields. These methods will
 * throw a validation error if the required tag was is not available.
 *
 * <pre><code> String id = parse.get("id");
 * String visibility = parse.getBoolean("visibility");
 * String source = parse.get("source");
 * </code></pre>
 *
 * Non generic "get" methods, like {@link #paint(JSONObject)}, are in position to provide an
 * appropriate default value.
 *
 * <pre><code> JSONObject paint = parse.paint( layer );
 * </code></pre>
 *
 * @author Torben Barsballe (Boundless)
 */
public class MBObjectParser {
    /** Wrapper class (used to provide better error messages). */
    Class<?> context;

    final FilterFactory2 ff;
    final StyleFactory2 sf;
    private static final Logger LOGGER = Logging.getLogger(MBObjectParser.class);

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
        sf =
                parse == null
                        ? (StyleFactory2) CommonFactoryFinder.getStyleFactory()
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

    /**
     * Safely look up paint in provided layer json.
     *
     * <p>Paint is optional, returning an empty JSONObject (to prevent the need for null checks).
     *
     * @return paint definition, optional so may be an empty JSONObject
     * @throws MBFormatException If paint is provided as an invalid type (such as boolean).
     */
    public JSONObject paint(JSONObject layer) {
        if (layer.containsKey("paint")) {
            Object paint = layer.get("paint");
            if (paint == null) {
                String type = get(layer, "type", "layer");
                throw new MBFormatException(type + " paint requires JSONObject");
            } else if (paint instanceof JSONObject) {
                return (JSONObject) paint;
            } else {
                String type = get(layer, "type", "layer");
                throw new MBFormatException(type + " paint requires JSONObject");
            }
        } else {
            // paint is optional, having a value here prevents need for null checks
            return new JSONObject();
        }
    }
    /**
     * Safely look up layout in provided layer json.
     *
     * <p>Layout is optional, returning an empty JSONObject (to prevent the need for null checks).
     *
     * @return layout definition, optional so may be an empty JSONObject
     * @throws MBFormatException If layout is provided as an invalid type (such as boolean).
     */
    public JSONObject layout(JSONObject layer) {
        if (layer.containsKey("layout")) {
            Object layout = layer.get("layout");
            if (layout == null) {
                String type = get(layer, "type", "layer");
                throw new MBFormatException(type + " layout requires JSONObject");
            } else if (layout instanceof JSONObject) {
                return (JSONObject) layout;
            } else {
                String type = get(layer, "type", "layer");
                throw new MBFormatException(type + " paint requires JSONObject");
            }
        } else {
            // paint is optional, having a value here prevents need for null checks
            return new JSONObject();
        }
    }

    /**
     * Access JSONObject for the indicated tag.
     *
     * <p>Confirms json contains the provided tag as a JSONObject, correctly throwing {@link
     * MBFormatException} if not available.
     *
     * @param json The JSONObject in which to lookup the provided tag and return a JSONObject
     * @param tag The tag to look up in the provided JSONObject
     * @return The JSONObject at the provided tag
     * @throws MBFormatException If JSONObject not available for the provided tag
     */
    public JSONObject getJSONObject(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof JSONObject) {
            return (JSONObject) json.get(tag);
        } else {
            throw new MBFormatException("\"" + tag + "\" requires JSONObject");
        }
    }

    /**
     * Access JSONObject for the indicated tag, with the provided fallback if the the json does not
     * contain a JSONObject for that tag.
     *
     * @param json The JSONObject in which to lookup the provided tag and return a JSONObject
     * @param tag The tag to look up in the provided JSONObject
     * @param fallback The JSONObject to return if the provided json does not contain a JSONObject
     *     for that tag.
     * @return The JSONObject at the provided tag, or the fallback object.
     */
    public JSONObject getJSONObject(JSONObject json, String tag, JSONObject fallback) {
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
     *
     * <p>Confirms json contains the provided tag as a JSONArray, correctly throwing {@link
     * MBFormatException} if not available.
     *
     * @return JSONObject
     * @throws MBFormatException If JSONObject not available for the provided tag
     */
    public JSONArray getJSONArray(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof JSONArray) {
            return (JSONArray) json.get(tag);
        } else {
            throw new MBFormatException("\"" + tag + "\" requires JSONArray");
        }
    }

    /**
     * Access a JSONArray at the provided tag in the provided JSONObject, with a fallback if no
     * JSONArray is found at that tag.
     *
     * @param json The JSONObject in which to lookup the provided tag and return a JSONArray
     * @param tag The tag to look up in the provided JSONObject
     * @param fallback The JSONArray to return if the provided json does not contain a JSONArray for
     *     that tag.
     * @return The JSONArray at the provided tag, or the fallback JSONArray.
     */
    public JSONArray getJSONArray(JSONObject json, String tag, JSONArray fallback) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        } else if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        } else if (!json.containsKey(tag) || json.get(tag) == null) {
            return fallback;
        } else if (json.containsKey(tag) && json.get(tag) instanceof JSONArray) {
            return (JSONArray) json.get(tag);
        } else {
            throw new MBFormatException("\"" + tag + "\" requires JSONArray");
        }
    }

    /**
     * Access a literal value (string, numeric, or boolean).
     *
     * @param json JSONArray object to parse.
     * @param index position in the provided array for which to retrieve a value.
     * @return required string, numeric or boolean
     * @throws MBFormatException if required index not available.
     */
    public Object value(JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index < json.size()) {
            Object value = json.get(index);
            if (value instanceof String || value instanceof Boolean || value instanceof Number) {
                return value;
            }
            if (value instanceof JSONArray) {
                return ff.literal(MBExpression.transformExpression(((JSONArray) value)));
            }
        }
        throw new MBFormatException(
                context.getSimpleName() + " requires [" + index + "] string, numeric or boolean");
    }
    /**
     * Access a literal value (string, numeric, or boolean).
     *
     * @return required string, numeric or boolean
     * @throws MBFormatException if required tag not available.
     */
    public Object value(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (json.containsKey(tag)) {
            Object value = json.get(tag);
            if (value == null
                    || value instanceof String
                    || value instanceof Boolean
                    || value instanceof Number) {
                return value;
            }
            throw new MBFormatException(
                    context.getSimpleName()
                            + " requires \""
                            + tag
                            + "\" literal required (was "
                            + value.getClass().getSimpleName()
                            + ")");
        }
        throw new MBFormatException(
                context.getSimpleName() + " requires \"" + tag + "\" required.");
    }

    /**
     * Quickly access required json index (as a String).
     *
     * @return required string
     * @throws MBFormatException if required index not available.
     */
    public String get(JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index < json.size() && json.get(index) instanceof String) {
            return (String) json.get(index);
        }
        if (index < json.size() && json.get(index) instanceof JSONArray) {
            return MBExpression.transformExpression((JSONArray) json.get(index)).toString();
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires [" + index + "] string");
        }
    }

    /**
     * Quickly access required json tag (as a String).
     *
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
     * @return required string, or fallback if unavailable
     */
    public String get(JSONObject json, String tag, String fallback) {
        if (tag == null || json == null) {
            return fallback;
        } else if (!json.containsKey(tag) || json.get(tag) == null) {
            return fallback;
        }
        if (json.containsKey(tag) && json.get(tag) instanceof String) {
            return (String) json.get(tag);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" string field");
        }
    }

    /**
     * Look up a double in the provided {@link JSONArray} at the provided index, or throw an {@link
     * MBFormatException}.
     *
     * @param json The array in which to look up the double
     * @param index The index at which to look up the double
     * @return The double from the array at index.
     */
    public double getNumeric(JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index < json.size() && json.get(index) instanceof Number) {
            return ((Number) json.get(index)).doubleValue();
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires [" + index + "] numeric value");
        }
    }

    /**
     * Look up a Double in the provided {@link JSONObject} at the provided 'tag', or thrown an
     * {@link MBFormatException}.
     *
     * @param json The object in which to look up the Double
     * @param tag The tag at which to look up the Double
     * @return The Double from the object at 'tag'
     */
    public Double getNumeric(JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && json.get(tag) instanceof Number) {
            return ((Number) json.get(tag)).doubleValue();
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" numeric field");
        }
    }

    /**
     * Look up a Boolean in the provided {@link JSONArray} at the provided index, or throw an {@link
     * MBFormatException}.
     *
     * @param json The array in which to look up the Boolean
     * @param index The index at which to look up the Boolean
     * @return The Boolean from the array at index.
     */
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

    /**
     * Look up a Boolean in the provided {@link JSONObject} at the provided 'tag', or thrown an
     * {@link MBFormatException}.
     *
     * @param json The object in which to look up the Boolean
     * @param tag The tag at which to look up the Boolean
     * @return The Boolean from the object at 'tag'
     */
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

    /**
     * Look up a Boolean in the provided {@link JSONObject} at the provided 'tag', or thrown an
     * {@link MBFormatException}, with a fallback if the json is null or contains no such 'tag'.
     *
     * @param json The object in which to look up the Boolean
     * @param tag The tag at which to look up the Boolean
     * @param fallback The value to return if the json is null or contains no such 'tag'.
     * @return The Boolean from the object at 'tag', or the fallback value
     */
    public Boolean getBoolean(JSONObject json, String tag, Boolean fallback) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        } else if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        } else if (!json.containsKey(tag) || json.get(tag) == null) {
            return fallback;
        } else if (json.get(tag) instanceof Boolean) {
            return (Boolean) json.get(tag);
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires \"" + tag + "\" boolean field");
        }
    }

    /**
     * Retrieve an object of the provided type in the JSONArray at this index, throwing an {@link
     * MBFormatException} if no object of that type is found at that index of the array.
     *
     * @param type The type of the object to retrieve.
     * @param json The JSONArray in which to retrieve the object.
     * @param index The index in the JSONArray at which to retrieve the object.
     * @return The object of the required type in the array at index.
     */
    public <T> T require(Class<T> type, JSONArray json, int index) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (index >= 0 && index <= json.size() && type.isInstance(json.get(index))) {
            return type.cast(json.get(index));
        } else {
            throw new MBFormatException(
                    context.getSimpleName() + " requires [" + index + "] " + type.getSimpleName());
        }
    }

    /**
     * Retrieve an object of the provided type in the JSONObject at this tag, throwing an {@link
     * MBFormatException} if no object of that type is found at that tag in the object.
     *
     * @param type The type of the object to retrieve.
     * @param json The JSONObject in which to retrieve the object.
     * @param tag The index in the JSONObject at which to retrieve the object.
     * @return The object of the required type in the JSONObject at the tag.
     */
    public <T> T require(Class<T> type, JSONObject json, String tag) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        }
        if (json.containsKey(tag) && type.isInstance(json.get(tag))) {
            return type.cast(json.get(tag));
        } else {
            throw new MBFormatException(
                    context.getSimpleName()
                            + " requires \""
                            + tag
                            + "\" "
                            + type.getSimpleName()
                            + " field");
        }
    }

    /**
     * Optional lookup, will return fallback if not available.
     *
     * @param type Type to lookup
     * @param json The JSONObject in which to lookup the value
     * @param tag The tag at which to lookupthe value in the JSONObject
     * @param fallback The fallback value to use if the JSONObject is null or does not contain the
     *     provided tag
     * @return value for the provided tag, or fallback if not available
     * @throws MBFormatException If value is found and is not the expected type
     */
    public <T> T optional(Class<T> type, JSONObject json, String tag, T fallback) {
        if (json == null) {
            throw new IllegalArgumentException("json required");
        } else if (tag == null) {
            throw new IllegalArgumentException("tag required for json access");
        } else if (!json.containsKey(tag)) {
            return fallback;
        } else {
            Object value = json.get(tag);
            if (!json.containsKey(tag) || json.get(tag) == null) {
                return fallback;
            } else if (Number.class.isAssignableFrom(type)) {
                T number = Converters.convert(value, type);
                if (number == null) {
                    throw new MBFormatException(
                            context.getSimpleName()
                                    + " optional \""
                                    + tag
                                    + "\" expects "
                                    + type.getSimpleName()
                                    + " value");
                } else {
                    return type.cast(number);
                }
            } else if (type.isInstance(value)) {
                return type.cast(value);
            } else {
                throw new MBFormatException(
                        context.getSimpleName()
                                + " optional \""
                                + tag
                                + "\" expects "
                                + type.getSimpleName()
                                + " value");
            }
        }
    }

    /**
     * Convert a String value to an enum value, ignoring case, with the provided fallback.
     *
     * @param json The json object to parse the value from
     * @param tag The key used to retrieve the value from the json.
     * @param enumeration The enum to convert the value to.
     * @return The enum value from the string, or the fallback value.
     * @throws MBFormatException if the value is not a String, or it is not a valid value for the
     *     enumeration.
     */
    public <T extends Enum<?>> T getEnum(
            JSONObject json, String tag, Class<T> enumeration, T fallback) {
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
            throw new MBFormatException(
                    "\""
                            + tag
                            + "\" contains invalid value for enumeration "
                            + enumeration.getSimpleName());
        } else {
            throw new MBFormatException(
                    "Conversion of \""
                            + tag
                            + "\" value from "
                            + value.getClass().getSimpleName()
                            + " to "
                            + enumeration.getSimpleName()
                            + " not supported.");
        }
    }

    /**
     * Parse a Mapbox enumeration property to a GeoTools Expression that evaluates to a GeoTools
     * constant (supports the property being specified as a mapbox function).
     *
     * <p>For example, converts {@link LineJoin#BEVEL} to an expression that evaluates to the String
     * value "bevel", or {@link LineJoin#MITER} to an expression that evaluates to "mitre".
     *
     * @param json The json object containing the property
     * @param tag The json key corresponding to the property
     * @param enumeration The Mapbox enumeration that the value should be an instance of
     * @param fallback The fallback enumeration value, if the value is missing or invalid for the
     *     provided enumeration.
     * @return A GeoTools expression corresponding to the Mapbox enumeration value, evaluating to a
     *     GeoTools constant.
     */
    public <T extends Enum<?>> Expression enumToExpression(
            JSONObject json, String tag, Class<T> enumeration, T fallback) {
        // Function name is inconsistent because "enum" is not a valid function name.
        Object value = json.get(tag);
        if (value == null) {
            return constant(fallback.toString(), enumeration);
        } else if (value instanceof String) {
            String stringVal = (String) value;
            if ("".equals(stringVal.trim())) {
                return constant(fallback.toString(), enumeration);
            }
            try {
                return constant(stringVal, enumeration);
            } catch (Exception e) {
                LOGGER.log(
                        Level.WARNING,
                        "\""
                                + stringVal
                                + "\" Exception parsing value for enumeration "
                                + enumeration.getSimpleName()
                                + ", falling back to default value.");
                return constant(fallback.toString(), enumeration);
            }
        } else if (value instanceof JSONObject) {
            MBFunction function = new MBFunction(this, (JSONObject) value);
            return function.enumeration(enumeration);
        } else {
            throw new MBFormatException(
                    "Conversion of \""
                            + tag
                            + "\" value from "
                            + value.getClass().getSimpleName()
                            + " to "
                            + enumeration.getSimpleName()
                            + " not supported.");
        }
    }

    /**
     * Utility method used to convert enumerations to an appropriate GeoTools literal string.
     *
     * <p>Any conversion between mapbox constants and geotools constants will be done here.
     *
     * @param value The value to be converted to the appropriate GeoTools literal
     * @param enumeration The type of the mapbox enumeration
     * @return Literal, or null if unavailable
     */
    public Literal constant(Object value, Class<? extends Enum<?>> enumeration) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
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
            if (enumValue == null) {
                throw new MBFormatException(
                        "\""
                                + stringVal
                                + "\" invalid value for enumeration "
                                + enumeration.getSimpleName());
            }

            // step 2 - convert to geotools constant
            // (Converting the string value to lowercase takes care of most cases).
            if (enumValue instanceof LineJoin && LineJoin.MITER.equals(enumValue)) {
                return ff.literal("mitre");
            }
            String literal = enumValue.toString().toLowerCase();
            return ff.literal(literal);
        }
        return null;
    }

    /**
     * Casts the provided obj to a JSONObject (safely reporting format exception
     *
     * @return JSONObject
     */
    public JSONObject jsonObject(Object obj) throws MBFormatException {
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        } else if (obj == null) {
            throw new MBFormatException("Not a JSONObject: null");
        } else {
            throw new MBFormatException("Not a JSONObject: " + obj.toString());
        }
    }

    /**
     * Casts the provided obj to a JSONObject (safely reporting format exception, with the provided
     * message).
     *
     * @param obj The object to cast
     * @param message The message for the exception of the object is not a JSONObject
     * @return The object, cast to JSONObject
     */
    public JSONObject jsonObect(Object obj, String message) throws MBFormatException {
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        } else {
            throw new MBFormatException(message);
        }
    }

    /**
     * Casts the provided obj to a JSONArray (otherwise throws an {@link MBFormatException}).
     *
     * @param obj The object to cast
     * @return The object, cast to JSONArray
     */
    public JSONArray jsonArray(Object obj) throws MBFormatException {
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        } else if (obj == null) {
            throw new MBFormatException("Not a JSONArray: null");
        } else {
            throw new MBFormatException("Not a JSONArray: " + obj.toString());
        }
    }

    /**
     * Casts the provided obj to a JSONArray (otherwise throws an {@link MBFormatException} with the
     * provided message).
     *
     * @param obj The object to cast
     * @param message The message for the exception of the object is not a JSONArray
     * @return The object, cast to JSONArray
     */
    public JSONArray jsonArray(Object obj, String message) throws MBFormatException {
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        } else {
            throw new MBFormatException(message);
        }
    }

    /**
     * Lookup an array of the provided type in the provided JSONObject at 'tag', with a fallback if
     * that tag is not found. Throws an exception if that tag is something other than an array, or
     * if its contents cannot be cast to type.
     *
     * @param type The type of the array
     * @param json The JSONObject in which to look up the array
     * @param tag The tag at which to look up the array
     * @param fallback The fallback array
     * @return An array of the provided type, or the fallback array.
     */
    @SuppressWarnings("unchecked")
    public <T> T[] array(Class<T> type, JSONObject json, String tag, T[] fallback) {
        if (json.containsKey(tag)) {
            Object obj = json.get(tag);
            if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray) obj;
                T[] returnArray = (T[]) Array.newInstance(type, array.size());
                for (int i = 0; i < array.size(); i++) {
                    Object value = array.get(i);
                    if (Number.class.isAssignableFrom(type) && value instanceof Number) {
                        if (type == Double.class) {
                            returnArray[i] = (T) Double.valueOf(((Number) value).doubleValue());
                            continue;
                        } else if (type == Integer.class) {
                            returnArray[i] = (T) Integer.valueOf(((Number) value).intValue());
                            continue;
                        }
                    }
                    returnArray[i] = type.cast(array.get(i));
                }
                return returnArray;
            } else {
                throw new MBFormatException(
                        "\""
                                + tag
                                + "\" required as JSONArray of "
                                + type.getSimpleName()
                                + ": Unexpected "
                                + obj.getClass().getSimpleName());
            }
        }
        return fallback;
    }

    /** Convert to int[] */
    public int[] array(JSONObject json, String tag, int[] fallback) {
        Integer[] array =
                array(
                        Integer.class,
                        json,
                        tag,
                        fallback == null
                                ? null
                                : Arrays.stream(fallback).mapToObj(i -> i).toArray(Integer[]::new));
        return array == null ? fallback : Arrays.stream(array).mapToInt(i -> i).toArray();
    }

    /** Convert to double[] */
    public double[] array(JSONObject json, String tag, double[] fallback) {
        Double[] array =
                array(
                        Double.class,
                        json,
                        tag,
                        fallback == null
                                ? null
                                : Arrays.stream(fallback).mapToObj(i -> i).toArray(Double[]::new));
        return array == null ? fallback : Arrays.stream(array).mapToDouble(i -> i).toArray();
    }

    /**
     * Convert json to Expression number between 0 and 1, or a function.
     *
     * @param json json representation
     * @return Expression based on provided json, or null if not provided
     */
    public Expression percentage(JSONObject json, String tag) throws MBFormatException {
        return percentage(json, tag, null);
    }

    /**
     * Convert json to Expression number between 0 and 1, or a function.
     *
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
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
            throw new MBFormatException(
                    "\""
                            + tag
                            + "\" percentage from boolean "
                            + obj
                            + " not supported, expected value between 0 and 1");
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction(this, (JSONObject) obj);
            return function.numeric();
        } else if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " number from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, \""
                            + tag
                            + "\" value limited to Number or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    //
    // Font
    //
    /**
     * Convert the provided object to a numeric Expression (or function), with a fallback value if
     * the object is null.
     *
     * @param json The json context of the object, used for error messages.
     * @param tag The object to convert
     * @param fallback The fallback value, used when the provided object is null.
     * @return A numeric expression for the provided object
     */
    private Expression font(JSONObject json, String tag, String fallback) throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(tag);

        if (obj == null) {
            return ff.literal(fallback);
        }
        if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction(this, (JSONObject) obj);
            return function.font();
        }
        if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " font from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, "
                            + tag
                            + " value limited to JSONArray, or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    public Expression font(JSONObject json, String tag) throws MBFormatException {
        return font(json, tag, null);
    }

    //
    // NUMBER
    //

    /**
     * Convert the provided object to a numeric Expression (or function), with a fallback value if
     * the object is null.
     *
     * @param context The json context of the object, used for error messages.
     * @param obj The object to convert
     * @param fallback The fallback value, used when the provided object is null.
     * @return A numeric expression for the provided object
     */
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
            MBFunction function = new MBFunction(this, (JSONObject) obj);
            return function.numeric();
        } else if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " number from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, "
                            + context
                            + " value limited to String, Number, Boolean or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    /**
     * Convert the value at 'index' in the provided JSONArray to a numeric Expression or a Function.
     *
     * @param json The JSONArray in which to look up a value
     * @param index The index in the JSONArray
     * @return Numeric Expression based on provided json, or null.
     */
    public Expression number(JSONArray json, int index) throws MBFormatException {
        return number(json, index, null);
    }

    /**
     * Convert the value in the provided JSONArray at index to a numeric Expression, or a function,
     * with a fallback if the json is null.
     *
     * @param json The JSONArray in which to look up the value
     * @param index The index in the JSONArray at which to look up the value
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     */
    public Expression number(JSONArray json, int index, Number fallback) throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(index);
        return number("index " + index, obj, fallback);
    }

    /**
     * Convert the value in the provided JSONObject at 'tag' to a numeric Expression, or a function.
     *
     * @param json The JSONObject in which to look up the value
     * @param tag The tag in the JSONObject at which to look up the value
     * @return Expression based on provided json, or null
     */
    public Expression number(JSONObject json, String tag) throws MBFormatException {
        return number(json, tag, null);
    }

    /**
     * Convert the value in the provided JSONObject at 'tag' to a numeric Expression, or a function,
     * with a fallback if the json is null.
     *
     * @param json The JSONObject in which to look up the value
     * @param tag The tag in the JSONObject at which to look up the value
     * @param fallback default value if the JSONObject is null
     * @return Expression based on provided json, or literal if json was null.
     */
    public Expression number(JSONObject json, String tag, Number fallback)
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

    /**
     * Convert the provided object to a string Expression (or function), with a fallback value if
     * the object is null.
     *
     * @param context The json context of the object, used for error messages.
     * @param obj The object to convert
     * @param fallback The fallback value, used when the provided object is null.
     * @return A string expression for the provided object
     */
    private Expression string(String context, Object obj, String fallback) {
        if (obj == null) {
            return fallback == null ? null : ff.literal(fallback);
        } else if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            Number number = (Number) obj;
            return ff.literal(number.toString());
        } else if (obj instanceof Boolean) {
            Boolean bool = (Boolean) obj;
            return ff.literal(bool.toString());
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction(this, (JSONObject) obj);
            return function.function(String.class);
        } else if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " string from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, "
                            + context
                            + " value limited to String, Number, Boolean or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    /**
     * Convert the value in the provided JSONArray at index to a string Expression, or a function.
     *
     * @param json The JSONArray in which to look up the value
     * @param index The index in the JSONArray at which to look up the value
     * @return Expression based on provided json, or literal if json was null.
     */
    public Expression string(JSONArray json, int index) {
        Object obj = json.get(index);
        if (obj == null) {
            return ff.literal(null);
        } else if (obj instanceof String) {
            String str = (String) obj;
            return ff.literal(str);
        } else if (obj instanceof Number) {
            Number number = (Number) obj;
            return ff.literal(number);
        } else if (obj instanceof Boolean) {
            Boolean bool = (Boolean) obj;
            return ff.literal(bool);
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction(this, (JSONObject) obj);
            return function.function(String.class);
        } else if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String
                    && MBExpression.canCreate(((JSONArray) obj).get(0).toString())) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " string from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, "
                            + context
                            + " value limited to String, Number, Boolean, JSONArray or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    /**
     * Convert json to Expression string, or a function.
     *
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     */
    public Expression string(JSONObject json, String tag, String fallback)
            throws MBFormatException {
        if (json == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        return string("\"" + tag + "\"", json.get(tag), fallback);
    }

    /**
     * Convert json to Expression color, or a function.
     *
     * @param json json representation
     * @param fallback default value (string representation of color) if json is null
     * @return Expression based on provided json, or literal if json was null.
     */
    public Expression color(JSONObject json, String tag, Color fallback) throws MBFormatException {
        if (json.get(tag) == null) {
            return fallback == null ? null : ff.literal(fallback);
        }
        Object obj = json.get(tag);
        return color("\"" + tag + "\"", obj, fallback);
    }

    /**
     * Handles literal color definitions supplied as a string, returning a {@link Literal}.
     *
     * <ul>
     *   <li>
     *       <pre>{"line-color": "yellow"</pre>
     *       named: a few have been put in pass test cases, prnding: plan to use {@link
     *       Hints#COLOR_DEFINITION} to allow for web colors.
     *   <li>
     *       <pre>{"line-color": "#ffff00"}</pre>
     *       hex: hex color conversion are supplied by {@link ColorConverterFactory}
     *   <li>
     *       <pre>{"line-color": "#ff0"}</pre>
     *       hex: we will need to special case this
     *   <li>
     *       <pre>{"line-color": "rgb(255, 255, 0)"}</pre>
     *       - we will need to special case this
     *   <li>
     *       <pre>{"line-color": "rgba(255, 255, 0, 1)"}</pre>
     *       - we will need to special case this
     *   <li>
     *       <pre>{"line-color": "hsl(100, 50%, 50%)"}</pre>
     *       - we will need to special case this
     *   <li>
     *       <pre>{"line-color": "hsla(100, 50%, 50%, 1)"}</pre>
     *       - we will need to special case this
     *   <li>
     * </ul>
     *
     * This method uses {@link Hints#COLOR_DEFINITION} "CSS" to support the use of web colors names.
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
     * @return color expression (literal or function)
     */
    private Expression color(String context, Object obj, Color fallback) {
        if (obj == null) {
            return fallback == null ? null : ff.literal(fallback);
        } else if (obj instanceof String) {
            String str = (String) obj;
            return color(str);
        } else if (obj instanceof Number) {
            throw new MBFormatException(context + " color from Number not supported");
        } else if (obj instanceof Boolean) {
            throw new MBFormatException(context + "  color from Boolean not supported");
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction((JSONObject) obj);
            return function.color();
        } else if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " color from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, "
                            + context
                            + " limited to String or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    /**
     * Converts color definitions supplied as a string to Color objects:
     *
     * <ul>
     *   <li>
     *       <pre>{"line-color": "yellow"</pre>
     *       named: a few have been put in pass test cases, prnding: plan to use {@link
     *       Hints#COLOR_DEFINITION} to allow for web colors.
     *   <li>
     *       <pre>{"line-color": "#ffff00"}</pre>
     *       hex: hex color conversion are supplied by {@link ColorConverterFactory}
     *   <li>
     *       <pre>{"line-color": "#ff0"}</pre>
     *       hex: we will need to special case this
     *   <li>
     *       <pre>{"line-color": "rgb(255, 255, 0)"}</pre>
     *       - we will need to special case this
     *   <li>
     *       <pre>{"line-color": "rgba(255, 255, 0, 1)"}</pre>
     *       - we will need to special case this
     *   <li>
     *       <pre>{"line-color": "hsl(100, 50%, 50%)"}</pre>
     *       - we will need to special case this
     *   <li>
     *       <pre>{"line-color": "hsla(100, 50%, 50%, 1)"}</pre>
     *       - we will need to special case this
     *   <li>
     * </ul>
     *
     * This method uses {@link Hints#COLOR_DEFINITION} "CSS" to support the use of web colors names.
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
        if (color.startsWith("#") && color.length() == 4) {
            String[] split = color.split("");
            StringBuilder builder = new StringBuilder();
            builder.append(split[0]);
            for (Integer j = 1; j < split.length; j++) {
                builder.append(split[j]).append(split[j]);
            }
            String cstring = builder.toString();
            return Color.decode(cstring);
        }
        Hints h = new Hints(Hints.COLOR_DEFINITION, "CSS");
        return Converters.convert(color, Color.class, h);
    }

    /**
     * Convert json to Expression boolean, or a function.
     *
     * @param json json representation
     * @param fallback default value if json is null
     * @return Expression based on provided json, or literal if json was null.
     */
    public Expression bool(JSONObject json, String tag, boolean fallback) throws MBFormatException {
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
            MBFunction function = new MBFunction(this, (JSONObject) obj);
            return function.function(Boolean.class);
        } else if (obj instanceof JSONArray) {
            if (((JSONArray) obj).get(0) instanceof String) {
                return MBExpression.transformExpression((JSONArray) obj);
            } else {
                throw new MBFormatException(context + " boolean from JSONArray not supported");
            }
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid, \""
                            + tag
                            + "\" value limited to String, Boolean or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    /**
     * Maps a json value at 'tag' in the provided JSONObject to a {@link Displacement}.
     *
     * @param json The JSONObject in which to look up a displacement value
     * @param tag The tag in the JSONObject
     * @param fallback The fallback displacement, if no value is found at that tag.
     * @return A displacement from the json
     */
    public Displacement displacement(JSONObject json, String tag, Displacement fallback) {
        Object defn = json.get(tag);
        if (defn == null) {
            return fallback;
        } else if (defn instanceof JSONArray) {
            JSONArray array = (JSONArray) defn;
            return sf.displacement(number(array, 0, 0), number(array, 1, 0));
        } else if (defn instanceof JSONObject) {
            // Function case
            MBFunction function = new MBFunction(this, (JSONObject) defn);
            if (!function.isArrayFunction()) {
                throw new MBFormatException(
                        "\""
                                + tag
                                + "\": Exception parsing displacement from Mapbox function: function values must all be arrays with length 2.");
            }

            List<MBFunction> functionForEachDimension;
            try {
                functionForEachDimension = function.splitArrayFunction();
            } catch (ParseException pe) {
                throw new MBFormatException(
                        "\""
                                + tag
                                + "\": Exception parsing displacement from Mapbox function: "
                                + pe.getMessage(),
                        pe);
            } catch (Exception e) {
                throw new MBFormatException(
                        "\""
                                + tag
                                + "\": Exception parsing displacement from Mapbox function: "
                                + e.getMessage(),
                        e);
            }

            if (functionForEachDimension.size() != 2) {
                throw new MBFormatException(
                        "\""
                                + tag
                                + "\": Exception parsing displacement from Mapbox function: function values must all be arrays with length 2.");
            }

            Expression xFn = functionForEachDimension.get(0).numeric();
            Expression yFn = functionForEachDimension.get(1).numeric();
            return sf.displacement(xFn, yFn);
        } else {
            throw new MBFormatException(
                    "\""
                            + tag
                            + "\": Expected array or function, but was "
                            + defn.getClass().getSimpleName());
        }
    }

    /** @return True if the layer has the provided property explicitly provided, False otherwise. */
    public boolean isPropertyDefined(JSONObject json, String propertyName)
            throws MBFormatException {
        return json.containsKey(propertyName) && json.get(propertyName) != null;
    }
}
