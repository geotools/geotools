/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import java.lang.reflect.Array;
import java.util.AbstractList;

/**
 * Used to provide a List API of an ordinate array.
 *
 * <p>Insertions are not supported
 *
 * @see net.refractions.jspatial.jts
 * @author jgarnett, Refractions Reasearch Inc.
 * @version CVS Version
 */
public class AttributeList extends AbstractList {
    final Object ARRAY;
    final int OFFSET;
    final int LEN;
    final int SIZE;
    final int START;
    final int END;
    final int STEP;

    public AttributeList(Object array) {
        this(array, 0, 1);
    }

    public AttributeList(Object array, int offset, int len) {
        this(array, offset, len, 0, Array.getLength(array));
    }

    public AttributeList(Object array, int offset, int len, int start, int end) {
        START = start;
        END = end;
        ARRAY = array;
        OFFSET = offset;
        LEN = len;
        SIZE = Math.abs(START - END) / LEN;
        STEP = START < END ? LEN : -LEN;

        if (!ARRAY.getClass().isArray())
            throw new IllegalArgumentException("Provided argument was not an array");

        if (Array.getLength(ARRAY) % LEN != 0) {
            throw new IllegalArgumentException(
                    "You have requested Coordinates of "
                            + LEN
                            + " ordinates. "
                            + "This is inconsistent with an array of length "
                            + Array.getLength(ARRAY));
        }
    }
    /**
     * Used to grab value from array.
     *
     * <p>Description of get.
     *
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        rangeCheck(index);
        return Array.get(ARRAY, START + STEP * index + OFFSET);
    }
    /** Quick double access */
    public double getDouble(int index) {
        rangeCheck(index);
        return Array.getDouble(
                ARRAY, START + STEP * index + OFFSET); // ARRAY[ START+STEP*index + OFFSET ];
    }

    public String getString(int index) {
        rangeCheck(index);
        return Array.get(ARRAY, START + STEP * index + OFFSET).toString();
    }

    public double[] toDoubleArray() {
        double array[] = new double[size()];
        for (int i = 0; i < size(); i++) {
            array[i] = getDouble(i);
        }
        return array;
    }

    public Object[] toObjectArray() {
        Object array[] = new Object[size()];
        for (int i = 0; i < size(); i++) {
            array[i] = get(i);
        }
        return array;
    }
    /**
     * Check if the given index is in range. If not, throw an appropriate runtime exception. This
     * method does *not* check if the index is negative: It is always used immediately prior to an
     * array access, which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
        if (index >= SIZE)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + SIZE);
    }
    /**
     * Used to
     *
     * <p>Description of size.
     *
     * @see java.util.Collection#size()
     */
    public int size() {
        return SIZE;
    }
}
