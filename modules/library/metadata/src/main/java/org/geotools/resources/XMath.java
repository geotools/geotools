/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources;

import java.text.ChoiceFormat;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Simple mathematical functions.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class XMath {
    /**
     * Natural logarithm of 10.
     * Approximately equal to 2.302585.
     *
     * @deprecated Was for {@link #log10} internal usage only.
     */
    public static final double LN10 = 2.3025850929940456840179914546844;

    /**
     * Table of some integer powers of 10. Used
     * for fast computation of {@link #pow10(int)}.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    private static final double[] POW10 = {
        1E+00, 1E+01, 1E+02, 1E+03, 1E+04, 1E+05, 1E+06, 1E+07, 1E+08, 1E+09,
        1E+10, 1E+11, 1E+12, 1E+13, 1E+14, 1E+15, 1E+16, 1E+17, 1E+18, 1E+19,
        1E+20, 1E+21, 1E+22
    };

    /**
     * Do not allow instantiation of this class.
     */
    private XMath() {
    }

    /**
     * Computes the hypotenuse (<code>sqrt(x²+y²)</code>).
     *
     * @deprecated Replaced by {@link Math#hypot}.
     */
    @Deprecated
    public static double hypot(final double x, final double y) {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Computes the logarithm in base 10. See
     * http://developer.java.sun.com/developer/bugParade/bugs/4074599.html.
     *
     * @deprecated Replaced by {@link Math#log10}.
     */
    @Deprecated
    public static double log10(final double x) {
        return Math.log(x) / LN10;
    }

    /**
     * Computes 10 power <var>x</var>.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static double pow10(final double x) {
        return org.geotools.math.XMath.pow10(x);
    }

    /**
     * Computes <var>x</var> to the power of 10. This computation is very fast
     * for small power of 10 but has some rounding error issues (see
     * http://developer.java.sun.com/developer/bugParade/bugs/4358794.html).
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static strictfp double pow10(final int x) {
        return org.geotools.math.XMath.pow10(x);
    }

    /**
     * Returns the sign of <var>x</var>. This method returns
     *    -1 if <var>x</var> is negative,
     *     0 if <var>x</var> is null or {@code NaN} and
     *    +1 if <var>x</var> is positive.
     *
     * @see Math#signum(double)
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static int sgn(final double x) {
        return org.geotools.math.XMath.sgn(x);
    }

    /**
     * Returns the sign of <var>x</var>. This method returns
     *    -1 if <var>x</var> is negative,
     *     0 if <var>x</var> is null or {@code NaN} and
     *    +1 if <var>x</var> is positive.
     *
     * @see Math#signum(float)
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static int sgn(final float x) {
        return org.geotools.math.XMath.sgn(x);
    }

    /**
     * Returns the sign of <var>x</var>. This method returns
     *    -1 if <var>x</var> is negative,
     *     0 if <var>x</var> is null and
     *    +1 if <var>x</var> is positive.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static int sgn(long x) {
        return org.geotools.math.XMath.sgn(x);
    }

    /**
     * Returns the sign of <var>x</var>. This method returns
     *    -1 if <var>x</var> is negative,
     *     0 if <var>x</var> is null and
     *    +1 if <var>x</var> is positive.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static int sgn(int x) {
        return org.geotools.math.XMath.sgn(x);
    }

    /**
     * Returns the sign of <var>x</var>. This method returns
     *    -1 if <var>x</var> is negative,
     *     0 if <var>x</var> is null and
     *    +1 if <var>x</var> is positive.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static short sgn(short x) {
        return org.geotools.math.XMath.sgn(x);
    }

    /**
     * Returns the sign of <var>x</var>. This method returns
     *    -1 if <var>x</var> is negative,
     *     0 if <var>x</var> is null and
     *    +1 if <var>x</var> is positive.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static byte sgn(byte x) {
        return org.geotools.math.XMath.sgn(x);
    }

    /**
     * Rounds the specified value, providing that the difference between the original value and
     * the rounded value is not greater than the specified amount of floating point units. This
     * method can be used for hiding floating point error likes 2.9999999996.
     *
     * @param  value The value to round.
     * @param  flu The amount of floating point units.
     * @return The rounded value, of {@code value} if it was not close enough to an integer.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static double round(final double value, int flu) {
        return org.geotools.math.XMath.roundIfAlmostInteger(value, flu);
    }

    /**
     * Tries to remove at least {@code n} fraction digits in the string representation of
     * the specified value. This method try small changes to {@code value}, by adding or
     * substracting a maximum of 4 ulps. If there is no small change that remove at least
     * {@code n} fraction digits, then the value is returned unchanged. This method is
     * used for hiding rounding errors, like in conversions from radians to degrees.
     *
     * <P>Example: {@code XMath.fixRoundingError(-61.500000000000014, 12)} returns
     * {@code -61.5}.
     *
     * @param  value The value to fix.
     * @param  n The minimum amount of fraction digits.
     * @return The fixed value, or the unchanged {@code value} if there is no small change
     *         that remove at least {@code n} fraction digits.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static double fixRoundingError(final double value, int n) {
        return org.geotools.math.XMath.trimDecimalFractionDigits(value, 4, n);
    }

    /**
     * Counts the fraction digits in the string representation of
     * the specified value. This method is equivalent to a call to
     * <code>{@linkplain Double#toString(double) Double#toString}(value)</code>
     * and counting the number of digits after the decimal separator.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static int countFractionDigits(final double value) {
        return org.geotools.math.XMath.countDecimalFractionDigits(value);
    }

    /**
     * Finds the least float greater than d (if positive == true),
     * or the greatest float less than d (if positive == false).
     * If NaN, returns same value. This code is an adaptation of
     * {@link java.text.ChoiceFormat#nextDouble}.
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    private static float next(final float f, final boolean positive) {
        final int SIGN             = 0x80000000;
        final int POSITIVEINFINITY = 0x7F800000;

        // Filter out NaN's
        if (Float.isNaN(f)) {
            return f;
        }

        // Zero's are also a special case
        if (f == 0f) {
            final float smallestPositiveFloat = Float.intBitsToFloat(1);
            return (positive) ? smallestPositiveFloat : -smallestPositiveFloat;
        }

        // If entering here, d is a nonzero value.
        // Hold all bits in a int for later use.
        final int bits = Float.floatToIntBits(f);

        // Strip off the sign bit.
        int magnitude = bits & ~SIGN;

        // If next float away from zero, increase magnitude.
        // Else decrease magnitude
        if ((bits > 0) == positive) {
            if (magnitude != POSITIVEINFINITY) {
                magnitude++;
            }
        } else {
            magnitude--;
        }

        // Restore sign bit and return.
        final int signbit = bits & SIGN;
        return Float.intBitsToFloat(magnitude | signbit);
    }

    /**
     * Finds the least float greater than <var>f</var>.
     * If {@code NaN}, returns same value.
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static float next(final float f) {
        return next(f, true);
    }

    /**
     * Finds the greatest float less than <var>f</var>.
     * If {@code NaN}, returns same value.
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static float previous(final float f) {
        return next(f, false);
    }

    /**
     * Finds the least double greater than <var>f</var>.
     * If {@code NaN}, returns same value.
     *
     * @see java.text.ChoiceFormat#nextDouble
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static double next(final double f) {
        return ChoiceFormat.nextDouble(f);
    }

    /**
     * Finds the greatest double less than <var>f</var>.
     * If {@code NaN}, returns same value.
     *
     * @see java.text.ChoiceFormat#previousDouble
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static double previous(final double f) {
        return ChoiceFormat.previousDouble(f);
    }

    /**
     * Returns the next or previous representable number. If {@code amount} is equals to
     * {@code 0}, then this method returns the {@code value} unchanged. Otherwise,
     * The operation performed depends on the specified {@code type}:
     * <ul>
     *   <li><p>If the {@code type} is {@link Double}, then this method is
     *       equivalent to invoking   {@link #previous(double)} if {@code amount} is equals to
     *       {@code -1}, or invoking {@link #next(double)} if {@code amount} is equals to
     *       {@code +1}. If {@code amount} is smaller than {@code -1} or greater
     *       than {@code +1}, then this method invokes {@link #previous(double)} or
     *       {@link #next(double)} in a loop for {@code abs(amount)} times.</p></li>
     *
     *   <li><p>If the {@code type} is {@link Float}, then this method is
     *       equivalent to invoking   {@link #previous(float)} if {@code amount} is equals to
     *       {@code -1}, or invoking {@link #next(float)} if {@code amount} is equals to
     *       {@code +1}. If {@code amount} is smaller than {@code -1} or greater
     *       than {@code +1}, then this method invokes {@link #previous(float)} or
     *       {@link #next(float)} in a loop for {@code abs(amount)} times.</p></li>
     *
     *   <li><p>If the {@code type} is an {@linkplain #isInteger integer}, then invoking
     *       this method is equivalent to computing {@code value + amount}.</p></li>
     * </ul>
     *
     * @param type    The type. Should be the class of {@link Double}, {@link Float},
     *                {@link Long}, {@link Integer}, {@link Short} or {@link Byte}.
     * @param value   The number to rool.
     * @param amount  -1 to return the previous representable number,
     *                +1 to return the next representable number, or
     *                 0 to return the number with no change.
     * @return One of previous or next representable number as a {@code double}.
     * @throws IllegalArgumentException if {@code type} is not one of supported types.
     */
    public static double rool(final Class type, double value, int amount)
            throws IllegalArgumentException
    {
        if (Double.class.equals(type)) {
            if (amount<0) {
                do {
                    value = previous(value);
                } while (++amount != 0);
            } else if (amount!=0) {
                do {
                    value = next(value);
                } while (--amount != 0);
            }
            return value;
        }
        if (Float.class.equals(type)) {
            float vf = (float)value;
            if (amount<0) {
                do {
                    vf = previous(vf);
                } while (++amount != 0);
            } else if (amount!=0) {
                do {
                    vf = next(vf);
                } while (--amount != 0);
            }
            return vf;
        }
        if (isInteger(type)) {
            return value + amount;
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.UNSUPPORTED_DATA_TYPE_$1, type));
    }

    /**
     * Returns a {@link Float#NaN NaN} number for the specified index. Valid NaN numbers have
     * bit fields ranging from {@code 0x7f800001} through {@code 0x7fffffff} or {@code 0xff800001}
     * through {@code 0xffffffff}. The standard {@link Float#NaN} has bit fields {@code 0x7fc00000}.
     *
     * @param  index The index, from -2097152 to 2097151 inclusive.
     * @return One of the legal {@link Float#NaN NaN} values as a float.
     * @throws IndexOutOfBoundsException if the specified index is out of bounds.
     *
     * @deprecated Moved to {@link org.geotools.math.XMath}.
     */
    @Deprecated
    public static float toNaN(int index) throws IndexOutOfBoundsException {
        return org.geotools.math.XMath.toNaN(index);
    }

    /**
     * Returns {@code true} if the specified {@code type} is one of real
     * number types. Real number types includes {@link Float} and {@link Double}.
     *
     * @param  type The type to test (may be {@code null}).
     * @return {@code true} if {@code type} is the class {@link Float} or {@link Double}.
     *
     * @deprecated Moved to {@link Classes}.
     */
    @Deprecated
    public static boolean isReal(final Class<?> type) {
        return type != null &&
               Double.class.equals(type) ||
                Float.class.equals(type);
    }

    /**
     * Returns {@code true} if the specified {@code type} is one of integer types.
     * Integer types includes {@link Long}, {@link Integer}, {@link Short} and {@link Byte}.
     *
     * @param  type The type to test (may be {@code null}).
     * @return {@code true} if {@code type} is the class {@link Long}, {@link Integer},
     *         {@link Short} or {@link Byte}.
     *
     * @deprecated Moved to {@link Classes}.
     */
    @Deprecated
    public static boolean isInteger(final Class<?> type) {
        return type != null &&
               Long.class.equals(type) ||
            Integer.class.equals(type) ||
              Short.class.equals(type) ||
               Byte.class.equals(type);
    }

    /**
     * Returns the number of bits used by number of the specified type.
     *
     * @param  type The type (may be {@code null}).
     * @return The number of bits, or 0 if unknow.
     *
     * @deprecated Moved to {@link Classes}.
     */
    @Deprecated
    public static int getBitCount(final Class<?> type) {
        if (Double   .class.equals(type)) return Double   .SIZE;
        if (Float    .class.equals(type)) return Float    .SIZE;
        if (Long     .class.equals(type)) return Long     .SIZE;
        if (Integer  .class.equals(type)) return Integer  .SIZE;
        if (Short    .class.equals(type)) return Short    .SIZE;
        if (Byte     .class.equals(type)) return Byte     .SIZE;
        if (Character.class.equals(type)) return Character.SIZE;
        if (Boolean  .class.equals(type)) return 1;
        return 0;
    }

    /**
     * Change a primitive class to its wrapper (e.g. {@code double} to {@link Double}).
     * If the specified class is not a primitive type, then it is returned unchanged.
     *
     * @param  type The primitive type (may be {@code null}).
     * @return The type as a wrapper.
     *
     * @deprecated Moved to {@link Classes}.
     */
    @Deprecated
    public static Class<?> primitiveToWrapper(final Class<?> type) {
        if (Character.TYPE.equals(type)) return Character.class;
        if (Boolean  .TYPE.equals(type)) return Boolean  .class;
        if (Byte     .TYPE.equals(type)) return Byte     .class;
        if (Short    .TYPE.equals(type)) return Short    .class;
        if (Integer  .TYPE.equals(type)) return Integer  .class;
        if (Long     .TYPE.equals(type)) return Long     .class;
        if (Float    .TYPE.equals(type)) return Float    .class;
        if (Double   .TYPE.equals(type)) return Double   .class;
        return type;
    }

    /**
     * Converts the specified string into a value object. The value object will be an instance
     * of {@link Boolean}, {@link Integer}, {@link Double}, <cite>etc.</cite> according the
     * specified type.
     *
     * @param  type The requested type.
     * @param  value the value to parse.
     * @return The value object, or {@code null} if {@code value} was null.
     * @throws IllegalArgumentException if {@code type} is not a recognized type.
     * @throws NumberFormatException if the string value is not parseable as a number
     *         of the specified type.
     *
     * @since 2.4
     *
     * @deprecated Moved to {@link Classes}.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T> T valueOf(final Class<T> type, final String value)
            throws IllegalArgumentException, NumberFormatException
    {
        if (value == null) {
            return null;
        }
        if (Double .class.equals(type)) return (T) Double .valueOf(value);
        if (Float  .class.equals(type)) return (T) Float  .valueOf(value);
        if (Long   .class.equals(type)) return (T) Long   .valueOf(value);
        if (Integer.class.equals(type)) return (T) Integer.valueOf(value);
        if (Short  .class.equals(type)) return (T) Short  .valueOf(value);
        if (Byte   .class.equals(type)) return (T) Byte   .valueOf(value);
        if (Boolean.class.equals(type)) return (T) Boolean.valueOf(value);
        throw new IllegalArgumentException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
    }
}
