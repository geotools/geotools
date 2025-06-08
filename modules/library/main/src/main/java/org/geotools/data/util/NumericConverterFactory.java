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
package org.geotools.data.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * ConverterFactory which converts between the "standard" numeric types.
 *
 * <p>Supported types:
 *
 * <ul>
 *   <li>{@link java.lang.Long}
 *   <li>{@link java.lang.Integer}
 *   <li>{@link java.lang.Short}
 *   <li>{@link java.lang.Byte}
 *   <li>{@link java.lang.BigInteger}
 *   <li>{@link java.lang.Double}
 *   <li>{@link java.lang.Float}
 *   <li>{@link java.lang.BigDecimal}
 * </ul>
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 */
public class NumericConverterFactory implements ConverterFactory {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(NumericConverterFactory.class);

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {
        // convert to non-primitive class
        source = primitiveToWrapperClass(source);
        target = primitiveToWrapperClass(target);

        // check if source is a number or a string. We can't convert to a number
        // from anything else.
        if (!Number.class.isAssignableFrom(source) && !String.class.isAssignableFrom(source)) return null;

        // check if target is one of supported
        if (Long.class.equals(target)
                || Integer.class.equals(target)
                || Short.class.equals(target)
                || Byte.class.equals(target)
                || BigInteger.class.equals(target)
                || BigDecimal.class.equals(target)
                || Double.class.equals(target)
                || Float.class.equals(target)
                || Number.class.equals(target)) {

            // check if the safe conversion flag was set and if so only allow save conversions
            if (hints != null) {
                Object safeConversion = hints.get(ConverterFactory.SAFE_CONVERSION);
                if (safeConversion instanceof Boolean && ((Boolean) safeConversion).booleanValue()) {
                    return new SafeNumericConverter();
                }
            }
            return new NumericConverter();
        }

        return null;
    }

    static class SafeNumericConverter implements Converter {
        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            return (T) convertInternal(source, target);
        }

        public Object convertInternal(Object source, Class<?> target) {
            target = primitiveToWrapperClass(target);
            if (source instanceof Number) {
                Number number = (Number) source;
                Class c = number.getClass();

                if (BigDecimal.class.equals(target)) {
                    return new BigDecimal(source.toString());
                } else if (Double.class.equals(target)) {
                    if (c != BigDecimal.class && c != BigInteger.class) {
                        if (c == Float.class) {
                            // this is done to avoid coordinate drift
                            return Double.valueOf(number.toString());
                        }

                        return Double.valueOf(number.doubleValue());
                        // return Double.valueOf(source.toString());
                    }
                } else if (Float.class.equals(target)) {
                    if (c == Float.class || c == Integer.class || c == Short.class || c == Byte.class) {
                        return Float.valueOf(number.floatValue());
                        // return Float.valueOf(source.toString());
                    }
                } else if (BigInteger.class.equals(target)) {
                    if (BigInteger.class.isAssignableFrom(c)
                            || c == Long.class
                            || c == Integer.class
                            || c == Short.class
                            || c == Byte.class) {
                        return new BigInteger(number.toString());
                        // return new BigInteger(source.toString());
                    }
                } else if (Long.class.equals(target)) {
                    if (c == Long.class || c == Integer.class || c == Short.class || c == Byte.class) {
                        return Long.valueOf(number.longValue());
                        // return Long.valueOf(source.toString());
                    }
                } else if (Integer.class.equals(target)) {
                    if (c == Integer.class || c == Short.class || c == Byte.class) {
                        return Integer.valueOf(number.intValue());
                        // return Integer.valueOf(source.toString());
                    }
                } else if (Short.class.equals(target)) {
                    if (c == Short.class || c == Byte.class) {
                        return Short.valueOf(number.shortValue());
                        // return Short.valueOf(source.toString());
                    }
                } else if (Byte.class.equals(target)) {
                    if (c == Byte.class) {
                        return Byte.valueOf(number.byteValue());
                        // return Byte.valueOf(source.toString());
                    }
                }
            } else if (source instanceof String) {
                String src = (String) source;
                try {
                    if (BigDecimal.class.isAssignableFrom(target)) {
                        return new BigDecimal(src);
                        // if (x.toString().equals(src))
                        //    return x;
                    } else if (target == Double.class) {
                        Double x = Double.valueOf(src);
                        if (x.toString().equals(src)) return x;
                    } else if (target == Float.class) {
                        Float x = Float.valueOf(src);
                        if (x.toString().equals(src)) return x;
                    } else if (BigInteger.class.isAssignableFrom(target)) {
                        BigInteger x = new BigInteger(src);
                        if (x.toString().equals(src)) return x;
                    } else if (target == Long.class) {
                        Long x = Long.valueOf(src);
                        if (x.toString().equals(src)) return x;
                    } else if (target == Integer.class) {
                        Integer x = Integer.valueOf(src);
                        if (x.toString().equals(src)) return x;
                    } else if (target == Short.class) {
                        Short x = Short.valueOf(src);
                        if (x.toString().equals(src)) return x;
                    } else if (target == Byte.class) {
                        Byte x = Byte.valueOf(src);
                        if (x.toString().equals(src)) return x;
                    }
                } catch (Exception ex) {
                    return null;
                }
            }

            return null;
        }
    }

    static class NumericConverter implements Converter {

        // target.cast won't work for both the object wrapper and the primitive class
        @Override
        @SuppressWarnings("unchecked")
        public <T> T convert(Object source, Class<T> target) throws Exception {
            return (T) convertInternal(source, target);
        }

        public Object convertInternal(Object source, Class<?> target) {
            target = primitiveToWrapperClass(target);
            source = cleanSource(source, target);

            if (source instanceof Number) {
                Number s = (Number) source;

                // integral
                if (Long.class.equals(target)) {
                    return Long.valueOf(s.longValue());
                }
                if (Integer.class.equals(target)) {
                    return Integer.valueOf(s.intValue());
                }
                if (Short.class.equals(target)) {
                    return Short.valueOf(s.shortValue());
                }
                if (Byte.class.equals(target)) {
                    return Byte.valueOf(s.byteValue());
                }
                if (BigInteger.class.equals(target)) {
                    return BigInteger.valueOf(s.longValue());
                }

                // floating point
                // JD: we use the string reprensentation to avoid coordinate
                // drift due to precision issues, there could be some
                // performance issues with this.
                if (Double.class.equals(target)) {
                    return Double.valueOf(s.toString());
                }
                if (Float.class.equals(target)) {
                    return Float.valueOf(s.toString());
                }
                if (BigDecimal.class.equals(target)) {
                    return new BigDecimal(s.toString());
                }

                if (Number.class.equals(target)) {
                    try {
                        return Integer.valueOf(s.toString());
                    } catch (Exception e) {
                        // ignore and continue
                    }

                    try {
                        return new BigInteger(s.toString());
                    } catch (Exception e) {
                        // ignore and continue
                    }

                    try {
                        return Double.valueOf(s.toString());
                    } catch (Exception e) {
                        // ignore and continue
                    }

                    try {
                        return new BigDecimal(s.toString());
                    } catch (Exception e) {
                        // ignore and continue
                    }
                }
            } else if (source instanceof String) {
                String s = (String) source;
                // ensure we trim any space off the string
                s = s.trim();

                String integral = toIntegral(s);

                // floating point
                if (Double.class.equals(target)) {
                    return Double.valueOf(s);
                }
                if (Float.class.equals(target)) {
                    return Float.valueOf(s);
                }
                if (BigDecimal.class.equals(target)) {
                    return new BigDecimal(s);
                }

                // textual
                if (Long.class.equals(target)) {
                    return Long.valueOf(integral);
                }
                if (Integer.class.equals(target)) {
                    return Integer.valueOf(integral);
                }
                if (Short.class.equals(target)) {
                    return Short.valueOf(integral);
                }
                if (Byte.class.equals(target)) {
                    return Byte.valueOf(integral);
                }
                if (BigInteger.class.equals(target)) {
                    return new BigInteger(integral);
                }

                // fallback. If you ask for Number, you get our 'best guess'
                if (Number.class.equals(target)) {
                    if (integral.equals(s)) {
                        // we think this is an integer of some sort
                        try {
                            return Integer.valueOf(integral);
                        } catch (Exception e) {
                            // ignore and continue
                        }

                        try {
                            return new BigInteger(integral);
                        } catch (Exception e) {
                            // ignore and continue
                        }
                    }
                    try {
                        return Double.valueOf(s);
                    } catch (Exception e) {
                        // ignore and continue
                    }

                    try {
                        return new BigDecimal(s);
                    } catch (Exception e) {
                        // ignore and continue
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
     * @return integral component of decimal representation
     */
    static String toIntegral(String s) {
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

    /**
     * Clean the source handling possible edge cases:
     *
     * <ol>
     *   <li>Scientific notation conversions
     * </ol>
     *
     * @return the cleaned source
     */
    static Object cleanSource(Object source, Class target) {
        BigDecimal bigDecimal = getBigDecimalFromScientificNotation(source, target);
        return bigDecimal != null ? bigDecimal : source;
    }

    /**
     * @return a BigDecimal in case the source is a String in scientific notation and the target is an integral number,
     *     null otherwise
     */
    static BigDecimal getBigDecimalFromScientificNotation(Object source, Class target) {
        if (source instanceof String
                && (Long.class.equals(target)
                        || Integer.class.equals(target)
                        || Short.class.equals(target)
                        || Byte.class.equals(target)
                        || BigInteger.class.equals(target))) {
            try {
                return ((String) source).toUpperCase().contains("E") ? new BigDecimal((String) source) : null;
            } catch (NumberFormatException ex) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("NumberFormatException for source=" + source);
                }
            }
        }
        return null;
    }

    static HashMap<Class, Class> primitiveToWrapper = new HashMap<>();

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
