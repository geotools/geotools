/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1992-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.math;


/**
 * A fraction made of a numerator and a denominator. This is not the purpose of this class
 * to provides a full-fledged library for fractional number handling. This class exists mostly
 * for the limited needs of some operations on tiled images.
 * <p>
 * For performance reasons, the methods in this class never create new objects. They always
 * operate on an object specified in argument, and store the result in the object on which
 * the method was invoked.
 * <p>
 * This class is final for performance reason.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (MPO)
 */
public final class Fraction extends Number implements Comparable<Fraction>, Cloneable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -4501644254763471216L;

    /**
     * The numerator.
     */
    private int numerator;

    /**
     * The denominator.
     */
    private int denominator;

    /**
     * Creates a new fraction initialized to 0/0, which is an indetermined value.
     * Note that this is not the same than initializing a fraction to 0.
     */
    public Fraction() {
    }

    /**
     * Creates a new fraction initialized to the same value than the given fraction.
     *
     * @param other The fraction to copy in this fraction.
     */
    public Fraction(final Fraction other) {
        this.numerator   = other.numerator;
        this.denominator = other.denominator;
    }

    /**
     * Creates a new fraction initialized to the given numerator.
     *
     * @param numerator The numerator.
     */
    public Fraction(final int numerator) {
        this.numerator = numerator;
        denominator = 1;
    }

    /**
     * Creates a new fraction.
     *
     * @param numerator The numerator.
     * @param denominator The denominator.
     */
    public Fraction(final int numerator, final int denominator) {
        this.numerator   = numerator;
        this.denominator = denominator;
        simplify();
    }

    /**
     * Sets this fraction to the given value.
     *
     * @param numerator The numerator.
     * @param denominator The denominator.
     */
    public void set(final int numerator, final int denominator) {
        this.numerator   = numerator;
        this.denominator = denominator;
        simplify();
    }

    /**
     * Adds to this fraction the values given by the given fraction.
     * The results is stored in this fraction.
     *
     * @param other The fraction to add to this fraction.
     */
    public void add(final Fraction other) {
        numerator = numerator * other.denominator + other.numerator * denominator;
        denominator *= other.denominator;
        simplify();
    }

    /**
     * Subtracts to this fraction the values given by the given fraction.
     * The results is stored in this fraction.
     *
     * @param other The fraction to subtract to this fraction.
     */
    public void subtract(final Fraction other) {
        numerator = numerator * other.denominator - other.numerator * denominator;
        denominator *= other.denominator;
        simplify();
    }

    /**
     * Multiplies this fraction by the given fraction.
     * The results is stored in this fraction.
     *
     * @param other The fraction to multiply to this fraction.
     */
    public void multiply(final Fraction other) {
        numerator   *= other.numerator;
        denominator *= other.denominator;
        simplify();
    }

    /**
     * Divides this fraction by the given fraction.
     * The results is stored in this fraction.
     *
     * @param other The fraction to divide to this fraction.
     */
    public void divide(final Fraction other) {
        numerator   *= other.denominator;
        denominator *= other.numerator;
        simplify();
    }

    /**
     * Simplifies this fraction in-place.
     */
    private void simplify() {
        // Simplify  0/x  as  0/1
        if (numerator == 0) {
            denominator = XMath.sgn(denominator);
            return;
        }
        // Simplify  x/0  as  1/0
        if (denominator == 0) {
            numerator = XMath.sgn(numerator);
            return;
        }
        // Simplify  x/xy  as  1/y
        if (denominator % numerator == 0) {
            denominator /= numerator;
            if (denominator < 0) {
                denominator = -denominator;
                numerator = -1;
            } else {
                numerator = 1;
            }
            return;
        }
        int num = Math.abs(numerator);
        int den = Math.abs(denominator);
        num %= den;
        // Simplify  xy/x  as  y/1
        if (num == 0) {
            numerator /= denominator;
            denominator = 1;
            return;
        }
        // Search for greater common multiple.
        int pgcd = 1;
        int remainder = num;
        do {
            num = den;
            den = remainder;
            pgcd = remainder;
            remainder = num % den;
        } while (remainder != 0);
        numerator   /= pgcd;
        denominator /=pgcd;
        if (denominator < 0) {
            numerator   = -numerator;
            denominator = -denominator;
        }
    }

    /**
     * Returns the numerator.
     *
     * @return The numerator.
     */
    public int numerator() {
        return numerator;
    }

    /**
     * Returns the denominator.
     *
     * @return The denominator.
     */
    public int denominator() {
        return denominator;
    }

    /**
     * Returns the fraction as a floating point number.
     *
     * @return This fraction as a floating point number.
     */
    @Override
    public double doubleValue() {
        return (double) numerator / (double) denominator;
    }

    /**
     * Returns the fraction as a floating point number.
     *
     * @return This fraction as a floating point number.
     */
    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Returns this fraction rounded to nearest integer.
     *
     * @return This fraction rounded to nearest integer.
     */
    @Override
    public long longValue() {
        return intValue();
    }

    /**
     * Returns this fraction {@linkplain #round rounded} to nearest integer.
     *
     * @return This fraction rounded to nearest integer.
     */
    @Override
    public int intValue() {
        return round(numerator, denominator);
    }

    /**
     * Computes {@code numerator / denominator} and rounds the result toward nearest integer.
     * If the result is located at equal distance from the two nearest integers, then rounds
     * to the even one.
     *
     * @param numerator The numerator in the division.
     * @param denominator The denominator in the division.
     * @return {@code numerator / denominator} rounded toward nearest integer.
     */
    public static long round(final long numerator, final long denominator) {
        long n = numerator / denominator;
        long r = numerator % denominator;
        if (r != 0) {
            r = Math.abs(r << 1);
            final long d = Math.abs(denominator);
            if (r > d || (r == d && (n & 1) != 0)) {
                if ((numerator ^ denominator) >= 0) {
                    n++;
                } else {
                    n--;
                }
            }
        }
        return n;
    }

    /**
     * Computes {@code numerator / denominator} and rounds the result toward nearest integer.
     * If the result is located at equal distance from the two nearest integers, then rounds
     * to the even one.
     *
     * @param numerator The numerator in the division.
     * @param denominator The denominator in the division.
     * @return {@code numerator / denominator} rounded toward nearest integer.
     */
    public static int round(final int numerator, final int denominator) {
        int n = numerator / denominator;
        int r = numerator % denominator;
        if (r != 0) {
            r = Math.abs(r << 1);
            final int d = Math.abs(denominator);
            if (r > d || (r == d && (n & 1) != 0)) {
                if ((numerator ^ denominator) >= 0) {
                    n++;
                } else {
                    n--;
                }
            }
        }
        return n;
    }

    /**
     * Computes {@code numerator / denominator} and rounds the result toward negative infinity.
     * This is different from the default operation on primitive types, which rounds toward zero.
     * <p>
     * <b>Tip:</b> if the numerator and the denominator are both positive or both negative,
     * then the result is positive and identical to {@code numerator / denominator}.
     *
     * @param numerator The numerator in the division.
     * @param denominator The denominator in the division.
     * @return {@code numerator / denominator} rounded toward negative infinity.
     */
    public static int floor(final int numerator, final int denominator) {
        int n = numerator / denominator;
        if ((numerator ^ denominator) < 0 && (numerator % denominator) != 0) {
            n--;
        }
        return n;
    }

    /**
     * Computes {@code numerator / denominator} and rounds the result toward positive infinity.
     * This is different from the default operation on primitive types, which rounds toward zero.
     *
     * @param numerator The numerator in the division.
     * @param denominator The denominator in the division.
     * @return {@code numerator / denominator} rounded toward positive infinity.
     */
    public static int ceil(final int numerator, final int denominator) {
        int n = numerator / denominator;
        if ((numerator ^ denominator) >= 0 && (numerator % denominator) != 0) {
            n++;
        }
        return n;
    }

    /**
     * Compares this fraction with the given one for order.
     *
     * @param other The fraction to compare to this fraction for ordering.
     * @return A negative number if this fraction is smaller than the given fraction,
     *         a positive number if greater, or 0 if equals.
     */
    public int compareTo(final Fraction other) {
        return numerator * other.denominator - other.numerator * denominator;
    }

    /**
     * Compares this fraction with the given object for equality.
     *
     * @param other The object to compare with this fraction for equality.
     * @return {@code true} if the given object is an other fraction numerically
     *         equals to this fraction.
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Fraction) {
            final Fraction that = (Fraction) other;
            return numerator == that.numerator && denominator == that.denominator;
        }
        return false;
    }

    /**
     * Returns a clone of this fraction.
     *
     * @return A clone of this fraction.
     */
    @Override
    public Fraction clone() {
        try {
            return (Fraction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns a string representation of this fraction.
     *
     * @return A string representation of this fraction.
     */
    @Override
    public String toString() {
        return String.valueOf(numerator) + '/' + denominator;
    }
}
