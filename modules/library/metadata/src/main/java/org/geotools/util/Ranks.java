/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;


/**
 * Sorts elements in an array while remembering their ranks. Each method in this class sort the
 * given array in the same way than {@link Arrays#sort(Object[])} does, and in addition return
 * the index that each element had in the original array prior the sorting.
 * <p>
 * Every methods are used in the same way:
 *
 * <blockquote><pre>
 * int[] index = ranks(source, target);
 * </pre></blockquote>
 *
 * where {@code source} and {@code target} are arrays of {@link Comparable} elements or of a
 * primitive type. The {@code source} argument is the array to sort and is left untouched
 * (unless {@code target} is a reference to the same array). The {@code target} argument,
 * if non-null, is the array where to store the sorted values.
 * <p>
 * The returned {@code index} array will have the same length than the {@code source} array. Each
 * element in the returned array is an index ranging from 0 inclusive to {@code source.length}
 * exclusive, such that {@code target[i]} = {@code source[index[i]]} for all <var>i</var>.
 * <p>
 * Invoking <code>{@linkplain #ranks(double[],double[]) ranks}(source, source)</code> is equivalent
 * to invoking <code>{@linkplain Arrays#sort(double[]) Arrays.sort}(source)</code> in a less
 * efficient way. The later should always be used (cloning the source array if needed) if the
 * returned ranks are not used.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class Ranks {
    /**
     * The index in the array before it has been sorted.
     */
    private final int index;

    /**
     * For inner class constructors only.
     */
    private Ranks(final int index) {
        this.index = index;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param <T> The type of elements in the array to be sorted.
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(Object[])
     */
    public static <T extends Comparable<T>> int[] ranks(final T[] source, final T[] target) {
        if (source == null) {
            return null;
        }
        final Any[] entries = new Any[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Any<T>(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                @SuppressWarnings("unchecked")
                final Any<T> entry = (Any<T>) entries[i];
                target[i] = entry.value;
            }
        }
        return ranks;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(double[])
     */
    public static int[] ranks(final double[] source, final double[] target) {
        if (source == null) {
            return null;
        }
        final Double[] entries = new Double[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Double(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                target[i] = entries[i].value;
            }
        }
        return ranks;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(float[])
     */
    public static int[] ranks(final float[] source, final float[] target) {
        if (source == null) {
            return null;
        }
        final Float[] entries = new Float[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Float(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                target[i] = entries[i].value;
            }
        }
        return ranks;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(long[])
     */
    public static int[] ranks(final long[] source, final long[] target) {
        if (source == null) {
            return null;
        }
        final Long[] entries = new Long[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Long(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                target[i] = entries[i].value;
            }
        }
        return ranks;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(int[])
     */
    public static int[] ranks(final int[] source, final int[] target) {
        if (source == null) {
            return null;
        }
        final Integer[] entries = new Integer[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Integer(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                target[i] = entries[i].value;
            }
        }
        return ranks;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(short[])
     */
    public static int[] ranks(final short[] source, final short[] target) {
        if (source == null) {
            return null;
        }
        final Short[] entries = new Short[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Short(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                target[i] = entries[i].value;
            }
        }
        return ranks;
    }

    /**
     * Sorts the elements in the given array and return their ranks.
     * See class description for details.
     *
     * @param  source The array of values to sort, or {@code null}.
     * @param  target The array where to store sorted values, or {@code null} if none. May be the
     *         same array than {@code source}, in which case the sorting will be performed in place.
     * @return The value ranks, or {@code null} if {@code source} was null.
     *
     * @see Arrays#sort(byte[])
     */
    public static int[] ranks(final byte[] source, final byte[] target) {
        if (source == null) {
            return null;
        }
        final Byte[] entries = new Byte[source.length];
        for (int i=0; i<entries.length; i++) {
            entries[i] = new Byte(i, source[i]);
        }
        final int[] ranks = ranks(entries);
        if (target != null) {
            for (int i=Math.min(entries.length, target.length); --i>=0;) {
                target[i] = entries[i].value;
            }
        }
        return ranks;
    }

    /**
     * Extracts the ranks from the specified entries.
     */
    private static int[] ranks(final Ranks[] entries) {
        Arrays.sort(entries);
        final int[] ranks = new int[entries.length];
        for (int i=0; i<ranks.length; i++) {
            ranks[i] = entries[i].index;
        }
        return ranks;
    }

    /** A rank element for the {@link Comparable} type. */
    private static final class Any<T extends Comparable<T>> extends Ranks implements Comparable<Any<T>> {
        /** The value to sort. */
        private final T value;

        /** Creates an element for the specified value. */
        Any(final int index, final T value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Any<T> other) {
            return value.compareTo(other.value);
        }
    }

    /** A rank element for the {@code double} type. */
    private static final class Double extends Ranks implements Comparable<Double> {
        /** The value to sort. */
        private final double value;

        /** Creates an element for the specified value. */
        Double(final int index, final double value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Double other) {
            return java.lang.Double.compare(value, other.value);
        }
    }

    /** A rank element for the {@code float} type. */
    private static final class Float extends Ranks implements Comparable<Float> {
        /** The value to sort. */
        private final float value;

        /** Creates an element for the specified value. */
        Float(final int index, final float value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Float other) {
            return java.lang.Float.compare(value, other.value);
        }
    }

    /** A rank element for the {@code long} type. */
    private static final class Long extends Ranks implements Comparable<Long> {
        /** The value to sort. */
        private final long value;

        /** Creates an element for the specified value. */
        Long(final int index, final long value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Long other) {
            return (value < other.value) ? -1 : ((value == other.value) ? 0 : 1);
        }
    }

    /** A rank element for the {@code int} type. */
    private static final class Integer extends Ranks implements Comparable<Integer> {
        /** The value to sort. */
        private final int value;

        /** Creates an element for the specified value. */
        Integer(final int index, final int value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Integer other) {
            return (value < other.value) ? -1 : ((value == other.value) ? 0 : 1);
        }
    }

    /** A rank element for the {@code short} type. */
    private static final class Short extends Ranks implements Comparable<Short> {
        /** The value to sort. */
        private final short value;

        /** Creates an element for the specified value. */
        Short(final int index, final short value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Short other) {
            return (int) value - (int) other.value;
        }
    }

    /** A rank element for the {@code byte} type. */
    private static final class Byte extends Ranks implements Comparable<Byte> {
        /** The value to sort. */
        private final byte value;

        /** Creates an element for the specified value. */
        Byte(final int index, final byte value) {
            super(index);
            this.value = value;
        }

        /** Compares this element with the specified one for order. */
        public int compareTo(final Byte other) {
            return (int) value - (int) other.value;
        }
    }
}
