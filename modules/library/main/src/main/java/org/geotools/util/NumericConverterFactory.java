/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.HashMap;

import org.geotools.factory.Hints;

/**
 * ConverterFactory which converts between the "standard" numeric types.
 * <p>
 * Supported types:
 * <ul>
 * <li>{@link java.lang.Long}
 * <li>{@link java.lang.Integer}
 * <li>{@link java.lang.Short}
 * <li>{@link java.lang.Byte}
 * <li>{@link java.lang.BigInteger}
 * <li>{@link java.lang.Double}
 * <li>{@link java.lang.Float}
 * <li>{@link java.lang.BigDecimal}
 * </ul>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 *
 *
 * @source $URL$
 */
public class NumericConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        //convert to non-primitive class
        source = primitiveToWrapperClass(source);
        target = primitiveToWrapperClass(target);
        
        // check if source is a number or a string. We can't convert to a number
        // from anything else.
        if (!(Number.class.isAssignableFrom(source)) && !(String.class.isAssignableFrom(source)))
            return null;

        // check if target is one of supported
        if (Long.class.equals(target) || Integer.class.equals(target) || Short.class.equals(target)
                || Byte.class.equals(target) || BigInteger.class.equals(target)
                || BigDecimal.class.equals(target) || Double.class.equals(target)
                || Float.class.equals(target) || Number.class.equals(target)) {
           
            //check if teh safe conversion flag was set and if so only allow save conversions
            if (hints != null){
                Object safeConversion = hints.get(ConverterFactory.SAFE_CONVERSION);
                if (safeConversion instanceof Boolean && ((Boolean)safeConversion).booleanValue()) {
                    return new SafeNumericConverter();
                }
            }
            return new NumericConverter();
        }

        return null;
    }

    class SafeNumericConverter implements Converter {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            target = primitiveToWrapperClass(target);
            if (source instanceof Number){
                Number number = (Number) source;
                Class c = number.getClass();
                
                if (BigDecimal.class.equals(target)){
                   return (T) new BigDecimal(source.toString());
                }
                else if (Double.class.equals(target)){
                    if (c != BigDecimal.class && c != BigInteger.class){
                        if ( c == Float.class ) {
                            //this is done to avoid coordinate drift
                            return (T) new Double(number.toString());
                        }
                        
                        return (T) Double.valueOf(number.doubleValue());
                        //return (T) new Double(source.toString());
                    }
                }
                else if (Float.class.equals(target)){
                    if (c == Float.class || c == Integer.class || c == Short.class || c == Byte.class ) {
                        return (T) Float.valueOf( number.floatValue() );
                        //return (T) new Float(source.toString());
                    }
                }
                else if (BigInteger.class.equals(target)){
                    if ( BigInteger.class.isAssignableFrom(c) || c == Long.class || c == Integer.class 
                        || c == Short.class || c == Byte.class ) {
                        return (T) new BigInteger( number.toString() ); 
                        //return (T) new BigInteger(source.toString());
                    }
                }
                else if (Long.class.equals(target)){
                    if (c == Long.class || c == Integer.class || c == Short.class || c == Byte.class) {
                        return (T) Long.valueOf( number.longValue() );
                        //return (T) new Long(source.toString());
                    }
                }
                else if (Integer.class.equals(target)){
                    if (c == Integer.class || c == Short.class || c == Byte.class ) {
                        return (T) Integer.valueOf( number.intValue() );
                        //return (T) new Integer(source.toString());
                    }
                }
                else if (Short.class.equals(target)){
                    if (c == Short.class || c == Byte.class ) {
                        return (T) Short.valueOf(number.shortValue());
                        //return (T) new Short(source.toString());
                    }
                }
                else if (Byte.class.equals(target)){
                    if ( c == Byte.class ) {
                        return (T) Byte.valueOf(number.byteValue());
                        //return (T) new Byte(source.toString());
                    }
                }
            }
            else if (source instanceof String){
                String src = (String) source;
                try {
                    if (BigDecimal.class.isAssignableFrom( target ) ) {
                        return (T) new BigDecimal(src);
                        //if (x.toString().equals(src))
                        //    return (T) x;
                    }
                    else if (target == Double.class) {
                        Double x = new Double(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    } 
                    else if (target == Float.class) {
                        Float x = new Float(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    }
                    else if (BigInteger.class.isAssignableFrom(target)) {
                        BigInteger x = new BigInteger(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    }
                    else if (target == Long.class) {
                        Long x = new Long(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    }
                    else if (target == Integer.class) {
                        Integer x = new Integer(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    }
                    else if (target == Short.class) {
                        Short x = new Short(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    }
                    else if (target == Byte.class) {
                        Byte x = new Byte(src);
                        if (x.toString().equals(src))
                            return (T) x;
                    }
                } 
                catch (Exception ex) {
                    return null;
                }
            }

            return null;
        }
    }

    class NumericConverter implements Converter {

        public Object convert(Object source, Class target) throws Exception {
            target = primitiveToWrapperClass(target);
            if (source instanceof Number) {
                Number s = (Number) source;

                // integral
                if (Long.class.equals(target)) {
                    return new Long(s.longValue());
                }
                if (Integer.class.equals(target)) {
                    return new Integer(s.intValue());
                }
                if (Short.class.equals(target)) {
                    return new Short(s.shortValue());
                }
                if (Byte.class.equals(target)) {
                    return new Byte(s.byteValue());
                }
                if (BigInteger.class.equals(target)) {
                    return BigInteger.valueOf(s.longValue());
                }

                // floating point
                // JD: we use the string reprensentation to avoid coordinate
                // drift due to precision issues, there could be some
                // performance issues with this.
                if (Double.class.equals(target)) {
                    return new Double(s.toString());
                }
                if (Float.class.equals(target)) {
                    return new Float(s.toString());
                }
                if (BigDecimal.class.equals(target)) {
                    return new BigDecimal(s.toString());
                }

                if (Number.class.equals(target)) {
                    try {
                        return new Integer(s.toString());
                    } catch (Exception e) {
                    }

                    try {
                        return new BigInteger(s.toString());
                    } catch (Exception e) {
                    }

                    try {
                        return new Double(s.toString());
                    } catch (Exception e) {
                    }

                    try {
                        return new BigDecimal(s.toString());
                    } catch (Exception e) {
                    }
                }
            } else if (source instanceof String) {
                String s = (String) source;
                // ensure we trim any space off the string
                s = s.trim();

                String integral = toIntegral(s);

                // floating point
                if (Double.class.equals(target)) {
                    return new Double(s);
                }
                if (Float.class.equals(target)) {
                    return new Float(s);
                }
                if (BigDecimal.class.equals(target)) {
                    return new BigDecimal(s);
                }

                // textual
                if (Long.class.equals(target)) {
                    return new Long(integral);
                }
                if (Integer.class.equals(target)) {
                    return new Integer(integral);
                }
                if (Short.class.equals(target)) {
                    return new Short(integral);
                }
                if (Byte.class.equals(target)) {
                    return new Byte(integral);
                }
                if (BigInteger.class.equals(target)) {
                    return new BigInteger(integral);
                }

                // fallback. If you ask for Number, you get our 'best guess'
                if (Number.class.equals(target)) {
                    if (integral.equals(s)) {
                        // we think this is an integer of some sort
                        try {
                            return new Integer(integral);
                        } catch (Exception e) {
                        }

                        try {
                            return new BigInteger(integral);
                        } catch (Exception e) {
                        }
                    }
                    try {
                        return new Double(s);
                    } catch (Exception e) {
                    }

                    try {
                        return new BigDecimal(s);
                    } catch (Exception e) {
                    }
                }
            }
            // nothing matched. Return null.
            return null;
        }

    }

    /**
     * Extract the integral part out of a decimal format string.
     * 
     * @param s
     * @return integral component of decimal representation
     */
    static String toIntegral(String s) {
        // NumberFormat format = NumberFormat.getInstance();

        int radex = -1; // last non numeric character to account for "." vs "," seperators
        for (int i = s.length() - 1; i > 0; i--) {
            char ch = s.charAt(i);
            if (!Character.isDigit(ch) && '-' != ch) {
                radex = i;
                break;
            }
        }
        if (radex != -1) {
            // text is formatted in decimal but floating point format supplied
            return s.substring(0, radex);
        } else {
            return s;
        }
    }
    
    static HashMap<Class, Class> primitiveToWrapper = new HashMap();
    static {
        primitiveToWrapper.put(Byte.TYPE, Byte.class);
        primitiveToWrapper.put(Short.TYPE, Short.class);
        primitiveToWrapper.put(Integer.TYPE, Integer.class);
        primitiveToWrapper.put(Long.TYPE, Long.class);
        primitiveToWrapper.put(Float.TYPE, Float.class);
        primitiveToWrapper.put(Double.TYPE, Double.class);
        primitiveToWrapper.put(Boolean.TYPE, Boolean.class);
    }
    static Class primitiveToWrapperClass(Class primitive) {
        if (primitive.isPrimitive()) {
            Class wrapper = primitiveToWrapper.get(primitive); 
            primitive = wrapper != null ? wrapper : primitive;
        }
        return primitive;
    }
}
