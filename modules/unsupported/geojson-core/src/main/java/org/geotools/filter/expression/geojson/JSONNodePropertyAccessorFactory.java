/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression.geojson;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.util.factory.Hints;
import tools.jackson.core.JsonPointer;
import tools.jackson.databind.JsonNode;

/** Property Accessor Factory for {@link JsonNode} */
public class JSONNodePropertyAccessorFactory implements PropertyAccessorFactory {

    static PropertyAccessor JSONNODEPROPERTY = new JSONNodePropertyAccessor();
    static final String ROOT = "/";

    @Override
    public PropertyAccessor createPropertyAccessor(Class<?> type, String xpath, Class<?> target, Hints hints) {
        if (xpath == null) {
            return null;
        }
        if (!SimpleFeature.class.isAssignableFrom(type) && !SimpleFeatureType.class.isAssignableFrom(type))
            return null; // we only work with simple feature
        return JSONNODEPROPERTY;
    }

    /** Property Accessor for {@link JsonNode} */
    static class JSONNodePropertyAccessor implements PropertyAccessor {

        private static final String JSON_NODE_DATE_FORMAT_OFF = "JSON_NODE_DATE_FORMAT_OFF";

        @Override
        public boolean canHandle(Object object, String xpath, Class target) {
            try {
                if (xpath != null && object != null && object instanceof SimpleFeature feature) {
                    String[] parts = stripAndReturnHeadAndRest(xpath);
                    Object value = getValue(parts, feature);
                    if (value != null && value instanceof JsonNode node) {
                        if (parts.length < 2) {
                            // only one part means return whole JsonNode
                            return true;
                        } else {
                            JsonPointer pointer = JsonPointer.compile(parts[1]);
                            JsonNode jsonNode = node.at(pointer);
                            return !jsonNode.isMissingNode();
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                return false;
            }
            return false;
        }

        private static Object getValue(String[] parts, SimpleFeature feature) {
            Object value = feature.getAttribute(parts[0]);
            if (value == null) {
                // check to see if it is in the user data map which is sometimes populated
                // from geojson
                value = getValueFromUserData(parts, feature, value);
            }
            return value;
        }

        private static Object getValueFromUserData(String[] parts, SimpleFeature feature, Object value) {
            Map topLevelAttributes = (Map) feature.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);
            if (topLevelAttributes != null) {
                value = topLevelAttributes.get(parts[0]);
            }
            return value;
        }

        /**
         * Split xpath into head and rest
         *
         * @param xpath the xpath to strip
         * @return Split Xpath parts as array
         */
        private String[] stripAndReturnHeadAndRest(String xpath) {
            String[] parts = xpath.split("/", 2);
            if (parts.length == 2) {
                parts[1] = "/" + parts[1];
            }
            return parts;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {
            JsonNode jsonNode = null;
            if (xpath != null && object != null && object instanceof SimpleFeature feature) {
                String[] parts = stripAndReturnHeadAndRest(xpath);
                Object value = getValue(parts, feature);
                if (value != null && value instanceof JsonNode node) {
                    if (parts.length < 2) {
                        // only one part means return whole JsonNode
                        return (T) value;
                    } else {
                        JsonPointer pointer = JsonPointer.compile(parts[1]);
                        jsonNode = node.at(pointer);
                        if (jsonNode.isMissingNode()) {
                            throw new IllegalArgumentException("Cannot get property " + xpath + " from " + object);
                        }
                        return (T) getMostPrimitive(jsonNode);
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Property at " + xpath + " from " + object + " is not a JSON Node");
                }
            } else {
                throw new IllegalArgumentException("Xpath or object is null or not an Attribute");
            }
        }

        private Object getMostPrimitive(JsonNode jsonNode) {
            if (jsonNode.isNull()) {
                return null;
            } else if (jsonNode.isBoolean()) {
                return jsonNode.booleanValue();
            } else if (jsonNode.isNumber()) {
                if (jsonNode.isInt()) {
                    return jsonNode.intValue();
                } else if (jsonNode.isLong()) {
                    return jsonNode.longValue();
                } else if (jsonNode.isDouble()) {
                    return jsonNode.doubleValue();
                } else if (jsonNode.isBigDecimal()) {
                    return jsonNode.decimalValue();
                } else if (jsonNode.isFloatingPointNumber()) {
                    return jsonNode.doubleValue();
                } else if (jsonNode.isIntegralNumber()) {
                    return jsonNode.longValue();
                } else if (jsonNode.isBigInteger()) {
                    return jsonNode.bigIntegerValue();
                } else if (jsonNode.isShort()) {
                    return jsonNode.shortValue();
                } else if (jsonNode.isFloatingPointNumber()) {
                    return jsonNode.doubleValue();
                } else if (jsonNode.isIntegralNumber()) {
                    return jsonNode.longValue();
                } else if (jsonNode.isBigInteger()) {
                    return jsonNode.bigIntegerValue();
                } else if (jsonNode.isShort()) {
                    return jsonNode.shortValue();
                } else {
                    return jsonNode.numberValue();
                }
            } else if (jsonNode.isString()) {
                if (Boolean.getBoolean(JSON_NODE_DATE_FORMAT_OFF)) {
                    return jsonNode.asString();
                } else {
                    return toDateIfISO(jsonNode.asString());
                }
            } else {
                return jsonNode;
            }
        }

        private Object toDateIfISO(String textValue) {
            try {
                TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(textValue);
                Instant i = Instant.from(ta);
                return Date.from(i);
            } catch (Exception e) {
                try {
                    TemporalAccessor ta = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(textValue);
                    Instant i = Instant.from(ta);
                    return Date.from(i);
                } catch (Exception e2) {
                    return textValue;
                }
            }
        }

        @Override
        public <T> void set(Object object, String xpath, T value, Class<T> target) throws IllegalArgumentException {
            throw new UnsupportedOperationException("Does not support updating.");
        }
    }
}
