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
package org.geotools.metadata.math;

import static org.geotools.util.XMath.next;
import static org.geotools.util.XMath.previous;

import java.util.Arrays;
import org.geotools.util.XArray;

/**
 * Simple mathematical functions in addition to the ones provided in {@link Math}.
 *
 * @since 2.5
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class XMath {
    /** Table of some integer powers of 10. Used for fast computation of {@link #pow10(int)}. */
    private static final double[] POW10 = {
        1E+00, 1E+01, 1E+02, 1E+03, 1E+04, 1E+05, 1E+06, 1E+07, 1E+08, 1E+09,
        1E+10, 1E+11, 1E+12, 1E+13, 1E+14, 1E+15, 1E+16, 1E+17, 1E+18, 1E+19,
        1E+20, 1E+21, 1E+22
        // Do not add more elements, unless we verified that 1/x is accurate.
        // Last time we tried, it was not accurate anymore starting at 1E+23.
    };

    /**
     * The sequence of prime numbers computed so far. Will be expanded as needed. We limit ourself to 16 bits numbers
     * because they are suffisient for computing divisors of any 32 bits number.
     */
    private static short[] primes = {2, 3};

    /**
     * Maximum length allowed for the {@link #primes} array. This is the index of the first prime number that can not be
     * stored as 16 bits unsigned.
     */
    private static final int MAX_PRIMES_LENGTH = 6542;

    /** Do not allow instantiation of this class. */
    private XMath() {}

    /**
     * Computes 10 raised to the power of <var>x</var>. This method delegates to {@link #pow10(int)} if <var>x</var> is
     * an integer, or to {@link Math#pow} otherwise.
     *
     * @param x The exponent.
     * @return 10 raised to the given exponent.
     */
    public static double pow10(final double x) {
        final int ix = (int) x;
        if (ix == x) {
            return pow10(ix);
        } else {
            return Math.pow(10, x);
        }
    }

    /**
     * Computes 10 raised to the power of <var>x</var>. This method tries to be slightly more accurate than <code>
     * {@linkplain Math#pow Math.pow}(10,x)</code>, sometime at the cost of performance.
     *
     * <p>The {@code Math.pow(10,x)} method doesn't always return the closest IEEE floating point representation. More
     * accurate calculations are slower and usually not necessary, but the base 10 is a special case since it is used
     * for scaling axes or formatting human-readable output, in which case the precision may matter.
     *
     * @param x The exponent.
     * @return 10 raised to the given exponent.
     */
    public static strictfp double pow10(final int x) {
        if (x >= 0) {
            if (x < POW10.length) {
                return POW10[x];
            }
        } else if (x != Integer.MIN_VALUE) {
            final int nx = -x;
            if (nx < POW10.length) {
                return 1 / POW10[nx];
            }
        }
        try {
            /*
             * Double.parseDouble("1E"+x) give gives as good or better numbers than Math.pow(10,x)
             * for ALL integer powers, but is slower. We hope that the current workaround is only
             * temporary. See http://developer.java.sun.com/developer/bugParade/bugs/4358794.html
             */
            return Double.parseDouble("1E" + x);
        } catch (NumberFormatException exception) {
            return StrictMath.pow(10, x);
        }
    }

    /**
     * Returns the sign of <var>x</var>. This method returns -1 if <var>x</var> is negative, 0 if <var>x</var> is zero
     * or {@code NaN} and +1 if <var>x</var> is positive.
     *
     * @param x The number from which to get the sign.
     * @return {@code +1} if <var>x</var> is positive, {@code -1} if negative, or 0 otherwise.
     * @see Math#signum(double)
     */
    public static int sgn(final double x) {
        if (x > 0) return +1;
        if (x < 0) return -1;
        else return 0;
    }

    /**
     * Returns the sign of <var>x</var>. This method returns -1 if <var>x</var> is negative, 0 if <var>x</var> is zero
     * or {@code NaN} and +1 if <var>x</var> is positive.
     *
     * @param x The number from which to get the sign.
     * @return {@code +1} if <var>x</var> is positive, {@code -1} if negative, or 0 otherwise.
     * @see Math#signum(float)
     */
    public static int sgn(final float x) {
        if (x > 0) return +1;
        if (x < 0) return -1;
        else return 0;
    }

    /**
     * Returns the sign of <var>x</var>. This method returns -1 if <var>x</var> is negative, 0 if <var>x</var> is zero
     * and +1 if <var>x</var> is positive.
     *
     * @param x The number from which to get the sign.
     * @return {@code +1} if <var>x</var> is positive, {@code -1} if negative, or 0 otherwise.
     */
    public static int sgn(long x) {
        if (x > 0) return +1;
        if (x < 0) return -1;
        else return 0;
    }

    /**
     * Returns the sign of <var>x</var>. This method returns -1 if <var>x</var> is negative, 0 if <var>x</var> is zero
     * and +1 if <var>x</var> is positive.
     *
     * @param x The number from which to get the sign.
     * @return {@code +1} if <var>x</var> is positive, {@code -1} if negative, or 0 otherwise.
     */
    public static int sgn(int x) {
        if (x > 0) return +1;
        if (x < 0) return -1;
        else return 0;
    }

    /**
     * Returns the sign of <var>x</var>. This method returns -1 if <var>x</var> is negative, 0 if <var>x</var> is zero
     * and +1 if <var>x</var> is positive.
     *
     * @param x The number from which to get the sign.
     * @return {@code +1} if <var>x</var> is positive, {@code -1} if negative, or 0 otherwise.
     */
    public static short sgn(short x) {
        if (x > 0) return (short) +1;
        if (x < 0) return (short) -1;
        else return (short) 0;
    }

    /**
     * Returns the sign of <var>x</var>. This method returns -1 if <var>x</var> is negative, 0 if <var>x</var> is zero
     * and +1 if <var>x</var> is positive.
     *
     * @param x The number from which to get the sign.
     * @return {@code +1} if <var>x</var> is positive, {@code -1} if negative, or 0 otherwise.
     */
    public static byte sgn(byte x) {
        if (x > 0) return (byte) +1;
        if (x < 0) return (byte) -1;
        else return (byte) 0;
    }

    /**
     * Rounds the specified value, providing that the difference between the original value and the rounded value is not
     * greater than the specified amount of floating point units. This method can be used for hiding floating point
     * error likes 2.9999999996.
     *
     * @param value The value to round.
     * @param maxULP The maximal change allowed in ULPs (Unit in the Last Place).
     * @return The rounded value, of {@code value} if it was not close enough to an integer.
     */
    public static double roundIfAlmostInteger(final double value, int maxULP) {
        final double target = Math.rint(value);
        if (value != target) {
            final boolean pos = (value < target);
            double candidate = value;
            while (--maxULP >= 0) {
                candidate = pos ? next(candidate) : previous(candidate);
                if (candidate == target) {
                    return target;
                }
            }
        }
        return value;
    }

    /**
     * Tries to remove at least {@code n} fraction digits in the decimal representation of the specified value. This
     * method tries small changes to {@code value}, by adding or substracting up to {@code maxULP} (Unit in the Last
     * Place). If there is no small change that remove at least {@code n} fraction digits, then the value is returned
     * unchanged. This method is used for hiding rounding errors, like in conversions from radians to degrees.
     *
     * <p>Example: {@code XMath.trimLastDecimalDigits(-61.500000000000014, 12, 4)} returns {@code -61.5}.
     *
     * @param value The value to fix.
     * @param maxULP The maximal change allowed in ULPs (Unit in the Last Place). A typical value is 4.
     * @param n The minimum amount of fraction digits.
     * @return The trimmed value, or the unchanged {@code value} if there is no small change that remove at least
     *     {@code n} fraction digits.
     */
    public static double trimDecimalFractionDigits(final double value, final int maxULP, int n) {
        double lower = value;
        double upper = value;
        n = countDecimalFractionDigits(value) - n;
        if (n > 0) {
            for (int i = 0; i < maxULP; i++) {
                if (countDecimalFractionDigits(lower = previous(lower)) <= n) return lower;
                if (countDecimalFractionDigits(upper = next(upper)) <= n) return upper;
            }
        }
        return value;
    }

    /**
     * Counts the fraction digits in the string representation of the specified value. This method is equivalent to a
     * calling <code>{@linkplain Double#toString(double) Double.toString}(value)
     * </code> and counting the number of digits after the decimal separator.
     *
     * @param value The value for which to count the fraction digits.
     * @return The number of fraction digits.
     */
    public static int countDecimalFractionDigits(final double value) {
        final String asText = Double.toString(value);
        final int exp = asText.indexOf('E');
        int upper, power;
        if (exp >= 0) {
            upper = exp - 1;
            power = Integer.parseInt(asText.substring(exp + 1));
        } else {
            upper = asText.length() - 1;
            power = 0;
        }
        while (asText.charAt(upper) == '0') {
            upper--;
        }
        return Math.max(upper - asText.indexOf('.') - power, 0);
    }

    /**
     * Returns a {@link Float#NaN NaN} number for the specified index. Valid NaN numbers have bit fields ranging from
     * {@code 0x7f800001} through {@code 0x7fffffff} or {@code 0xff800001} through {@code 0xffffffff}. The standard
     * {@link Float#NaN} has bit fields {@code 0x7fc00000}. See {@link Float#intBitsToFloat} for more details on NaN bit
     * values.
     *
     * @param index The index, from -2097152 to 2097151 inclusive.
     * @return One of the legal {@link Float#NaN NaN} values as a float.
     * @throws IndexOutOfBoundsException if the specified index is out of bounds.
     * @see Float#intBitsToFloat
     */
    public static float toNaN(int index) throws IndexOutOfBoundsException {
        index += 0x200000;
        if (index >= 0 && index <= 0x3FFFFF) {
            final float value = Float.intBitsToFloat(0x7FC00000 + index);
            assert Float.isNaN(value) : value;
            return value;
        } else {
            throw new IndexOutOfBoundsException(Integer.toHexString(index));
        }
    }

    /**
     * Returns the <var>i</var><sup>th</sup> prime number. This method returns (2,3,5,7,11...) for index (0,1,2,3,4...).
     * This method is designed for relatively small prime numbers only; don't use it for large values.
     *
     * @param index The prime number index, starting at index 0 for prime number 2.
     * @return The prime number at the specified index.
     * @throws IndexOutOfBoundsException if the specified index is too large.
     * @see java.math.BigInteger#isProbablePrime
     */
    public static synchronized int primeNumber(final int index) throws IndexOutOfBoundsException {
        // 6541 is the largest index returning a 16 bits unsigned prime number.
        if (index < 0 || index >= MAX_PRIMES_LENGTH) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        short[] primes = XMath.primes;
        if (index >= primes.length) {
            int i = primes.length;
            int n = primes[i - 1] & 0xFFFF;
            primes = XArray.resize(primes, Math.min((index | 0xF) + 1, MAX_PRIMES_LENGTH));
            do {
                next:
                while (true) {
                    n += 2;
                    for (int j = 1; j < i; j++) {
                        if (n % (primes[j] & 0xFFFF) == 0) {
                            continue next;
                        }
                        // We could stop the search at the first value greater than sqrt(n), but
                        // given that the array is relatively short (because we limit ourself to
                        // 16 bits prime numbers), it probably doesn't worth.
                    }
                    assert n < 0xFFFF : i;
                    primes[i] = (short) n;
                    break;
                }
            } while (++i < primes.length);
            XMath.primes = primes;
        }
        return primes[index] & 0xFFFF;
    }

    /**
     * Returns the divisors of the specified number as positive integers. For any value other than {@code O} (which
     * returns an empty array), the first element in the returned array is always {@code 1} and the last element is
     * always the absolute value of {@code number}.
     *
     * @param number The number for which to compute the divisors.
     * @return The divisors in strictly increasing order.
     */
    public static int[] divisors(int number) {
        if (number == 0) {
            return new int[0];
        }
        number = Math.abs(number);
        int[] divisors = new int[16];
        divisors[0] = 1;
        int count = 1;
        /*
         * Searchs for the first divisors among the prime numbers. We stop the search at the
         * square root of 'n' because every values above that point can be inferred from the
         * values before that point, i.e. if n=p1*p2 and p2 is greater than 'sqrt', than p1
         * most be lower than 'sqrt'.
         */
        final int sqrt = (int) (Math.sqrt(number) + 1E-6); // Really wants rounding toward 0.
        for (int p, i = 0; (p = primeNumber(i)) <= sqrt; i++) {
            if (number % p == 0) {
                if (count == divisors.length) {
                    divisors = XArray.resize(divisors, count * 2);
                }
                divisors[count++] = p;
            }
        }
        /*
         * Completes the divisors past 'sqrt'. The numbers added here may or may not be prime
         * numbers. Side note: checking that they are prime numbers would be costly, but this
         * algorithm doesn't need that.
         */
        int source = count;
        if (count * 2 > divisors.length) {
            divisors = XArray.resize(divisors, count * 2);
        }
        int d1 = divisors[--source];
        int d2 = number / d1;
        if (d1 != d2) {
            divisors[count++] = d2;
        }
        while (--source >= 0) {
            divisors[count++] = number / divisors[source];
        }
        /*
         * Checks the products of divisors found so far. For example if 2 and 3 are divisors,
         * checks if 6 is a divisor as well. The products found will themself be used for
         * computing new products.
         */
        for (int i = 1; i < count; i++) {
            d1 = divisors[i];
            for (int j = i; j < count; j++) {
                d2 = d1 * divisors[j];
                if (number % d2 == 0) {
                    int p = Arrays.binarySearch(divisors, j, count, d2);
                    if (p < 0) {
                        p = ~p; // ~ operator, not minus
                        if (count == divisors.length) {
                            divisors = XArray.resize(divisors, count * 2);
                        }
                        System.arraycopy(divisors, p, divisors, p + 1, count - p);
                        divisors[p] = d2;
                        count++;
                    }
                }
            }
        }
        divisors = XArray.resize(divisors, count);
        assert XArray.isStrictlySorted(divisors);
        return divisors;
    }
}
