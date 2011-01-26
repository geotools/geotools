/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.AbstractList;
import java.util.RandomAccess;
import java.io.Serializable;
import org.opengis.util.Cloneable;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A list of unsigned integer values. This class packs the values in the minimal amount of bits
 * required for storing unsigned integers of the given {@linkplain #maximalValue maximal value}.
 * <p>
 * This class is <strong>not</strong> thread-safe. Synchronizations (if wanted) are user's
 * reponsability.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public class IntegerList extends AbstractList<Integer> implements RandomAccess, Serializable, Cloneable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 1241962316404811189L;

    /**
     * The size of the primitive type used for the {@link #values} array.
     */
    private static final int VALUE_SIZE = 64;

    /**
     * The shift to apply on {@code index} in order to produce a result equivalent to
     * {@code index} / {@value #VALUE_SIZE}.
     */
    private static final int BASE_SHIFT = 6;

    /**
     * The mask to apply on {@code index} in order to produce a result equivalent to
     * {@code index} % {@value #VALUE_SIZE}.
     */
    private static final int OFFSET_MASK = 63;

    /**
     * The packed values. We use the {@code long} type instead of {@code int}
     * on the basis that 64 bits machines are becoming more and more common.
     */
    private long[] values;

    /**
     * The bit count for values.
     */
    private final int bitCount;

    /**
     * The mask computed as {@code (1 << bitCount) - 1}.
     */
    private final int mask;

    /**
     * The list size. Initially 0.
     */
    private int size;

    /**
     * Creates an initially empty list with the given initial capacity.
     *
     * @param initialCapacity The initial capacity.
     * @param maximalValue The maximal value to be allowed, inclusive.
     */
    public IntegerList(int initialCapacity, int maximalValue) {
        this(initialCapacity, maximalValue, false);
    }

    /**
     * Creates a new list with the given initial size.
     * The value of all elements are initialized to 0.
     *
     * @param initialCapacity The initial capacity.
     * @param maximalValue The maximal value to be allowed, inclusive.
     * @param fill If {@code true}, the initial {@linkplain #size} is set to the initial
     *        capacity with all values set to 0.
     */
    public IntegerList(final int initialCapacity, int maximalValue, final boolean fill) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.NOT_GREATER_THAN_ZERO_$1, initialCapacity));
        }
        if (maximalValue <= 0) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.NOT_GREATER_THAN_ZERO_$1, maximalValue));
        }
        int bitCount = 0;
        do {
            bitCount++;
            maximalValue >>>= 1;
        } while (maximalValue != 0);
        this.bitCount = bitCount;
        mask = (1 << bitCount) - 1;
        values = new long[length(initialCapacity)];
        if (fill) {
            size = initialCapacity;
        }
    }

    /**
     * Returns the array length required for holding a list of the given size.
     *
     * @param size The list size.
     * @return The array length for holding a list of the given size.
     */
    private int length(int size) {
        size *= bitCount;
        int length = size >>> BASE_SHIFT;
        if ((size & OFFSET_MASK) != 0) {
            length++;
        }
        return length;
    }

    /**
     * Returns the maximal value that can be stored in this list.
     * May be slighly higher than the value given to the constructor.
     *
     * @return The maximal value, inclusive.
     */
    public int maximalValue() {
        return mask;
    }

    /**
     * Returns the current number of values in this list.
     *
     * @return The number of values.
     */
    public int size() {
        return size;
    }

    /**
     * Sets the list size to the given value. If the new size is lower than previous size,
     * then the elements after the new size are discarted. If the new size is greater than
     * the previous one, then the extra elements are initialized to 0.
     *
     * @param size The new size.
     */
    public void resize(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (size > this.size) {
            int base = this.size * bitCount;
            final int offset = base & OFFSET_MASK;
            base >>>= BASE_SHIFT;
            if (offset != 0 && base < values.length) {
                values[base] &= (1L << offset) - 1;
                base++;
            }
            final int length = length(size);
            Arrays.fill(values, base, Math.min(length, values.length), 0L);
            if (length > values.length) {
                values = XArray.resize(values, length);
            }
        }
        this.size = size;
    }

    /**
     * Fills the list with the given value. Every existing values are overwritten from index
     * 0 inclusive up to {@link #size} exclusive.
     *
     * @param value The value to set.
     */
    @SuppressWarnings("fallthrough")
    public void fill(int value) {
        if (value < 0 || value > mask) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.VALUE_OUT_OF_BOUNDS_$3,
                    value, 0, mask));
        }
        final long p;
        if (value == 0) {
            p = 0;   // All bits set to 0.
        } else if (value == mask) {
            p = -1L; // All bits set to 1.
        } else switch (bitCount) {
            case  1: value |= (value << 1);  // Fall through
            case  2: value |= (value << 2);  // Fall through
            case  4: value |= (value << 4);  // Fall through
            case  8: value |= (value << 8);  // Fall through
            case 16: value |= (value << 16); // Fall through
            case 32: p = (value & 0xFFFFFFFFL) | ((long) value << 32); break;
            default: { // General case (unoptimized)
                for (int i=0; i<size; i++) {
                    setUnchecked(i, value);
                }
                return;
            }
        }
        Arrays.fill(values, 0, length(size), p);
    }

    /**
     * Discarts all elements in this list.
     */
    @Override
    public void clear() {
        size = 0;
    }

    /**
     * Adds the given element to this list.
     *
     * @param value The value to add.
     * @throws NullPointerException if the given value is null.
     * @throws IllegalArgumentException if the given value is out of bounds.
     */
    @Override
    public boolean add(final Integer value) throws IllegalArgumentException {
        addInteger(value);
        return true;
    }

    /**
     * Adds the given element as the {@code int} primitive type.
     *
     * @param value The value to add.
     * @throws IllegalArgumentException if the given value is out of bounds.
     */
    public void addInteger(final int value) throws IllegalArgumentException {
        if (value < 0 || value > mask) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.VALUE_OUT_OF_BOUNDS_$3,
                    value, 0, mask));
        }
        final int length = length(++size);
        if (length > values.length) {
            values = XArray.resize(values, 2*values.length);
        }
        setUnchecked(size - 1, value);
    }

    /**
     * Returns the element at the given index.
     *
     * @param index The element index.
     * @return The value at the given index.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     */
    public Integer get(final int index) throws IndexOutOfBoundsException {
        return getInteger(index);
    }

    /**
     * Returns the element at the given index as the {@code int} primitive type.
     *
     * @param index The element index.
     * @return The value at the given index.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     */
    public int getInteger(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, index));
        }
        index *= bitCount;
        int base   = index >>> BASE_SHIFT;
        int offset = index & OFFSET_MASK;
        int value  = (int) (values[base] >>> offset);
        offset = VALUE_SIZE - offset;
        if (offset < bitCount) {
            final int high = (int) values[++base];
            value |= (high << offset);
        }
        value &= mask;
        return value;
    }

    /**
     * Sets the element at the given index.
     *
     * @param index The element index.
     * @param value The value at the given index.
     * @return The previous value at the given index.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     * @throws IllegalArgumentException if the given value is out of bounds.
     * @throws NullPointerException if the given value is null.
     */
    @Override
    public Integer set(final int index, final Integer value) throws IndexOutOfBoundsException {
        final Integer old = get(index);
        setInteger(index, value);
        return old;
    }

    /**
     * Sets the element at the given index as the {@code int} primitive type.
     *
     * @param index The element index.
     * @param value The value at the given index.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     * @throws IllegalArgumentException if the given value is out of bounds.
     */
    public void setInteger(int index, int value) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, index));
        }
        if (value < 0 || value > mask) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.VALUE_OUT_OF_BOUNDS_$3,
                    value, 0, mask));
        }
        setUnchecked(index, value);
    }

    /**
     * Sets the element at the given index as the {@code int} primitive type.
     * This argument does not check argument validity, since it is assumed already done.
     *
     * @param index The element index.
     * @param value The value at the given index.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     * @throws IllegalArgumentException if the given value is out of bounds.
     */
    private void setUnchecked(int index, int value) {
        index *= bitCount;
        int base   = index >>> BASE_SHIFT;
        int offset = index & OFFSET_MASK;
        values[base] &= ~(((long) mask) << offset);
        values[base] |= ((long) value) << offset;
        offset = VALUE_SIZE - offset;
        if (offset < bitCount) {
            value >>>= offset;
            values[++base] &= ~(((long) mask) >>> offset);
            values[base] |= value;
        }
    }

    /**
     * Returns the occurence of the given value in this list.
     *
     * @param value The value to search for.
     * @return The number of time the given value occurs in this list.
     */
    public int occurence(final int value) {
        int count = 0;
        final int size = size();
        for (int i=0; i<size; i++) {
            if (getInteger(i) == value) {
                count++;
            }
        }
        return count;
    }

    /**
     * Trims the capacity of this list to be its current size.
     */
    public void trimToSize() {
        values = XArray.resize(values, length(size));
    }

    /**
     * Returns a clone of this list.
     *
     * @return A clone of this list.
     */
    @Override
    public IntegerList clone() {
        final IntegerList clone;
        try {
            clone = (IntegerList) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
        clone.values = clone.values.clone();
        return clone;
    }
}
