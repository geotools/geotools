/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.index.quadtree;

/**
 * DoubleBits manipulates Double numbers by using bit manipulation and bit-field extraction. For
 * some operations (such as determining the exponent) this is more accurate than using mathematical
 * operations (which suffer from round-off error).
 *
 * <p>The algorithms and constants in this class apply only to IEEE-754 double-precision floating
 * point format.
 *
 * @version 1.7.2
 */
public class DoubleBits {

    public static final int EXPONENT_BIAS = 1023;

    public static double powerOf2(int exp) {
        if (exp > 1023 || exp < -1022) throw new IllegalArgumentException("Exponent out of bounds");
        long expBias = exp + EXPONENT_BIAS;
        long bits = (long) expBias << 52;
        return Double.longBitsToDouble(bits);
    }

    public static int exponent(double d) {
        DoubleBits db = new DoubleBits(d);
        return db.getExponent();
    }

    public static double truncateToPowerOfTwo(double d) {
        DoubleBits db = new DoubleBits(d);
        db.zeroLowerBits(52);
        return db.getDouble();
    }

    public static String toBinaryString(double d) {
        DoubleBits db = new DoubleBits(d);
        return db.toString();
    }

    public static double maximumCommonMantissa(double d1, double d2) {
        if (d1 == 0.0 || d2 == 0.0) return 0.0;

        DoubleBits db1 = new DoubleBits(d1);
        DoubleBits db2 = new DoubleBits(d2);

        if (db1.getExponent() != db2.getExponent()) return 0.0;

        int maxCommon = db1.numCommonMantissaBits(db2);
        db1.zeroLowerBits(64 - (12 + maxCommon));
        return db1.getDouble();
    }

    private double x;

    private long xBits;

    public DoubleBits(double x) {
        this.x = x;
        xBits = Double.doubleToLongBits(x);
    }

    public double getDouble() {
        return Double.longBitsToDouble(xBits);
    }

    /** Determines the exponent for the number */
    public int biasedExponent() {
        int signExp = (int) (xBits >> 52);
        int exp = signExp & 0x07ff;
        return exp;
    }

    /** Determines the exponent for the number */
    public int getExponent() {
        return biasedExponent() - EXPONENT_BIAS;
    }

    public void zeroLowerBits(int nBits) {
        long invMask = (1L << nBits) - 1L;
        long mask = ~invMask;
        xBits &= mask;
    }

    public int getBit(int i) {
        long mask = (1L << i);
        return (xBits & mask) != 0 ? 1 : 0;
    }

    /**
     * This computes the number of common most-significant bits in the mantissa. It does not count
     * the hidden bit, which is always 1. It does not determine whether the numbers have the same
     * exponent - if they do not, the value computed by this function is meaningless.
     *
     * @return the number of common most-significant mantissa bits
     */
    public int numCommonMantissaBits(DoubleBits db) {
        for (int i = 0; i < 52; i++) {
            if (getBit(i) != db.getBit(i)) return i;
        }
        return 52;
    }

    /** A representation of the Double bits formatted for easy readability */
    public String toString() {
        String numStr = Long.toBinaryString(xBits);
        // 64 zeroes!
        String zero64 = "0000000000000000000000000000000000000000000000000000000000000000";
        String padStr = zero64 + numStr;
        String bitStr = padStr.substring(padStr.length() - 64);
        String str =
                bitStr.substring(0, 1)
                        + "  "
                        + bitStr.substring(1, 12)
                        + "("
                        + getExponent()
                        + ") "
                        + bitStr.substring(12)
                        + " [ "
                        + x
                        + " ]";
        return str;
    }
}
