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
public class OrdinateList extends AbstractList {
    final double ARRAY[];
    final int OFFSET;
    final int LEN; // number of things in stuff
    final int SIZE; // number of "elements"
    final int START;
    final int END;
    final int STEP;

    public OrdinateList(double array[]) {
        this(array, 0, 1);
    }

    public OrdinateList(double array[], int offset, int len) {
        this(array, offset, len, 0, array.length);
    }

    public OrdinateList(double array[], int offset, int len, int start, int end) {
        START = start;
        END = end;
        ARRAY = array;
        OFFSET = offset;
        LEN = len;
        SIZE = Math.abs(START - END) / LEN;
        STEP = START < END ? LEN : -LEN;

        if (ARRAY.length % LEN != 0) {
            throw new IllegalArgumentException(
                    "You have requested Coordinates of "
                            + LEN
                            + " ordinates. "
                            + "This is inconsistent with an array of length "
                            + ARRAY.length);
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
        return Double.valueOf(getDouble(index));
    }
    /** Quick double access */
    public double getDouble(int index) {
        assert index < SIZE : "Index: " + index + ", Size: " + SIZE;
        return ARRAY[START + STEP * index + OFFSET];
    }

    public double[] toDoubleArray() {
        double array[] = new double[size()];
        for (int i = 0; i < size(); i++) {
            array[i] = getDouble(i);
        }
        return array;
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
