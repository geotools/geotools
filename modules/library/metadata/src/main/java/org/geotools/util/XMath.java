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
package org.geotools.util;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * Simple mathematical functions.
 *
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class XMath {
    /** Do not allow instantiation of this class. */
    private XMath() {}

    /**
     * Finds the least float greater than d (if positive == true), or the greatest float less than d (if positive ==
     * false). If NaN, returns same value. This code is an adaptation of {@link java.text.ChoiceFormat#nextDouble}.
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    private static float next(final float f, final boolean positive) {
        final int SIGN = 0x80000000;
        final int POSITIVEINFINITY = 0x7F800000;

        // Filter out NaN's
        if (Float.isNaN(f)) {
            return f;
        }

        // Zero's are also a special case
        if (f == 0f) {
            final float smallestPositiveFloat = Float.intBitsToFloat(1);
            return positive ? smallestPositiveFloat : -smallestPositiveFloat;
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
     * Finds the least float greater than <var>f</var>. If {@code NaN}, returns same value.
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static float next(final float f) {
        return next(f, true);
    }

    /**
     * Finds the greatest float less than <var>f</var>. If {@code NaN}, returns same value.
     *
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static float previous(final float f) {
        return next(f, false);
    }

    /**
     * Finds the least double greater than <var>f</var>. If {@code NaN}, returns same value.
     *
     * @see java.text.ChoiceFormat#nextDouble
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static double next(final double f) {
        return ChoiceFormat.nextDouble(f);
    }

    /**
     * Finds the greatest double less than <var>f</var>. If {@code NaN}, returns same value.
     *
     * @see java.text.ChoiceFormat#previousDouble
     * @todo Remove this method when we will be allowed to use Java 6.
     */
    public static double previous(final double f) {
        return ChoiceFormat.previousDouble(f);
    }

    /**
     * Returns the next or previous representable number. If {@code amount} is equals to {@code 0}, then this method
     * returns the {@code value} unchanged. Otherwise, The operation performed depends on the specified {@code type}:
     *
     * <ul>
     *   <li>
     *       <p>If the {@code type} is {@link Double}, then this method is equivalent to invoking
     *       {@link #previous(double)} if {@code amount} is equals to {@code -1}, or invoking {@link #next(double)} if
     *       {@code amount} is equals to {@code +1}. If {@code amount} is smaller than {@code -1} or greater than
     *       {@code +1}, then this method invokes {@link #previous(double)} or {@link #next(double)} in a loop for
     *       {@code abs(amount)} times.
     *   <li>
     *       <p>If the {@code type} is {@link Float}, then this method is equivalent to invoking
     *       {@link #previous(float)} if {@code amount} is equals to {@code -1}, or invoking {@link #next(float)} if
     *       {@code amount} is equals to {@code +1}. If {@code amount} is smaller than {@code -1} or greater than
     *       {@code +1}, then this method invokes {@link #previous(float)} or {@link #next(float)} in a loop for
     *       {@code abs(amount)} times.
     *   <li>
     *       <p>If the {@code type} is an {@linkplain #isInteger integer}, then invoking this method is equivalent to
     *       computing {@code value + amount}.
     * </ul>
     *
     * @param type The type. Should be the class of {@link Double}, {@link Float}, {@link Long}, {@link Integer},
     *     {@link Short} or {@link Byte}.
     * @param value The number to rool.
     * @param amount -1 to return the previous representable number, +1 to return the next representable number, or 0 to
     *     return the number with no change.
     * @return One of previous or next representable number as a {@code double}.
     * @throws IllegalArgumentException if {@code type} is not one of supported types.
     */
    public static double rool(final Class type, double value, int amount) throws IllegalArgumentException {
        if (Double.class.equals(type)) {
            if (amount < 0) {
                do {
                    value = previous(value);
                } while (++amount != 0);
            } else if (amount != 0) {
                do {
                    value = next(value);
                } while (--amount != 0);
            }
            return value;
        }
        if (Float.class.equals(type)) {
            float vf = (float) value;
            if (amount < 0) {
                do {
                    vf = previous(vf);
                } while (++amount != 0);
            } else if (amount != 0) {
                do {
                    vf = next(vf);
                } while (--amount != 0);
            }
            return vf;
        }
        if (Classes.isInteger(type)) {
            return value + amount;
        }
        throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.UNSUPPORTED_DATA_TYPE_$1, type));
    }
}
