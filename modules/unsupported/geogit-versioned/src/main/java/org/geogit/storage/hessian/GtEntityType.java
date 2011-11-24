/* Copyright (c) 2011 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the LGPL 2.0 license, available at the root
 * application directory.
 */
package org.geogit.storage.hessian;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This enum describes the data type of each encoded feature attribute.
 * 
 * The integer value of each data type should never be changed, for backwards compatibility
 * purposes. If the method of encoding of an attribute type is changed, a new type should be
 * created, with a new value, and the writers updated to use it. The readers should continue to
 * support both the old and new versions.
 * 
 * @author mleslie
 */
enum GtEntityType implements Serializable {
    STRING(0, String.class), BOOLEAN(1, Boolean.class), BYTE(2, Byte.class), DOUBLE(3, Double.class), 
    BIGDECIMAL(4, BigDecimal.class), FLOAT(5, Float.class), INT(6, Integer.class), 
    BIGINT(7, BigInteger.class), LONG(8, Long.class), BOOLEAN_ARRAY(11, boolean[].class), 
    BYTE_ARRAY(12, byte[].class), CHAR_ARRAY(13, char[].class), DOUBLE_ARRAY(14, double[].class), 
    FLOAT_ARRAY(15, float[].class), INT_ARRAY(16, int[].class), LONG_ARRAY(17, long[].class), 
    GEOMETRY(9, Geometry.class), NULL(10, null), UNKNOWN_SERIALISABLE(18, Serializable.class), 
    UNKNOWN(19, null), UUID(20, java.util.UUID.class);
    
    public static GtEntityType determineType(Object value) {
        if (value == null)
            return NULL;
        if (value instanceof String)
            return STRING;
        if (value instanceof Boolean)
            return BOOLEAN;
        if (value instanceof Byte)
            return BYTE;
        if (value instanceof Double)
            return DOUBLE;
        if (value instanceof BigDecimal)
            return BIGDECIMAL;
        if (value instanceof Float)
            return FLOAT;
        if (value instanceof Integer)
            return INT;
        if (value instanceof BigInteger)
            return BIGINT;
        if (value instanceof Long)
            return LONG;
        if (value instanceof boolean[])
            return BOOLEAN_ARRAY;
        if (value instanceof byte[])
            return BYTE_ARRAY;
        if (value instanceof char[])
            return CHAR_ARRAY;
        if (value instanceof double[])
            return DOUBLE_ARRAY;
        if (value instanceof float[])
            return FLOAT_ARRAY;
        if (value instanceof int[])
            return INT_ARRAY;
        if (value instanceof long[])
            return LONG_ARRAY;
        if (value instanceof java.util.UUID)
            return UUID;
        if (value instanceof Geometry)
            return GEOMETRY;
        if (value instanceof Serializable)
            return UNKNOWN_SERIALISABLE;
        return UNKNOWN;
    }

    private int value;
    private Class binding;

    private GtEntityType(int value, Class binding) {
        this.value = value;
        this.binding = binding;
    }

    public int getValue() {
        return this.value;
    }
    
    public Class getBinding() {
        return this.binding;
    }
    
    public static GtEntityType fromBinding(Class cls) {
        if(cls == null)
            return NULL;
        /*
         * We're handling equality first, as some entity types are top-level
         * catch-alls, and we can't rely on processing order to ensure the 
         * more specific cases are handled first.
         */
        for (GtEntityType type : GtEntityType.values()) {
            if(type.binding != null && type.binding.equals(cls))
                return type;
        }
        for (GtEntityType type : GtEntityType.values()) {
            if(type.binding != null && type.binding.isAssignableFrom(cls))
                return type;
        }
        return UNKNOWN;
    }

    /**
     * Determines the EntityType given its integer value.
     * 
     * @param value The value of the desired EntityType, as read from the blob
     * @return The correct EntityType for the value, or null if none is found.
     */
    public static GtEntityType fromValue(int value) {
        for (GtEntityType type : GtEntityType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}