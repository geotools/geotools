/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Simple support class that allows accumulating doubles in an array, transparently growing it as the data gets added.
 *
 * @author Andrea Aime - GeoSolutions
 */
class GrowableOrdinateArray {

    private double[] data;

    private int curr;

    /** Creates an array of the given initial size */
    public GrowableOrdinateArray(int size) {
        this.data = new double[size];
    }

    /** Builds an initialized array, which will be primed when {@link #ensureLength(int)} is called */
    GrowableOrdinateArray() {}

    /** Appends a single number to the array */
    public void add(double d) {
        ensureLength(curr + 1);
        data[curr++] = d;
    }

    /** Appends a two numbers to the array */
    public void add(double d1, double d2) {
        ensureLength(curr + 2);
        data[curr++] = d1;
        data[curr++] = d2;
    }

    /** Appends a list of doubles to the array */
    public void addAll(double... d) {
        ensureLength(curr + d.length);
        System.arraycopy(d, 0, data, curr, d.length);
        curr += d.length;
    }

    /** Appends a whole coordinate sequence to the array */
    public void addAll(CoordinateSequence cs) {
        int coordinatesCount = cs.size();
        ensureLength(curr + coordinatesCount * 2);
        for (int i = 0; i < coordinatesCount; i++) {
            data[curr + i * 2] = cs.getOrdinate(i, 0);
            data[curr + i * 2 + 1] = cs.getOrdinate(i, 1);
        }
        curr += coordinatesCount * 2;
    }

    /** Returns the accumulated numbers, in an array cut to the current size */
    public double[] getData() {
        if (data == null) {
            return new double[0];
        }
        if (data.length == curr) {
            return data;
        } else {
            double[] result = new double[curr];
            System.arraycopy(data, 0, result, 0, curr);
            return result;
        }
    }

    /** Returns the current data array, raw, uncut */
    public double[] getDataRaw() {
        return data;
    }

    /** Turns the array of ordinates into a coordinate sequence */
    public CoordinateSequence toCoordinateSequence(GeometryFactory gf) {
        double[] data = getData();
        CoordinateSequence cs = JTS.createCS(gf.getCoordinateSequenceFactory(), data.length / 2, 2);
        for (int i = 0; i < cs.size(); i++) {
            cs.setOrdinate(i, 0, data[i * 2]);
            cs.setOrdinate(i, 1, data[i * 2 + 1]);
        }
        return cs;
    }

    public int size() {
        return curr;
    }

    public void setSize(int newSize) {
        if (newSize < 0) {
            throw new IllegalArgumentException("The size must zero or positive, it was " + newSize + " instead");
        }
        ensureLength(newSize);
        curr = newSize;
    }

    /** Ensures the data array has the specified lenght, or grows it otherwise */
    void ensureLength(int length) {
        if (data == null) {
            data = new double[length];
        } else {
            int currLength = data.length;
            if (length > currLength) {
                int newLength = currLength * 3 / 2 + 1;
                if (newLength < length) {
                    newLength = length;
                }
                double[] newData = new double[newLength];
                System.arraycopy(data, 0, newData, 0, currLength);
                this.data = newData;
            }
        }
    }

    /** Reverses the values between start and end assuming it's a packed array of x/y ordinates */
    public void reverseOrdinates(int start, int end) {
        int limit = (start + end) / 2;
        for (int i = start; i < limit; i += 2) {
            // x
            double tmp = data[i];
            int simmetric = end - (i - start) - 1;
            data[i] = data[simmetric];
            data[simmetric] = tmp;
            // y
            tmp = data[i + 1];
            simmetric = end - (i - start);
            data[i + 1] = data[simmetric];
            data[simmetric] = tmp;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GrowableDataArray(" + curr + ")[");
        for (int i = 0; i < curr; i++) {
            sb.append(data[i]);
            if (i < curr - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /** Closes the sequence by adding the last point as first */
    public void close() {
        add(data[0], data[1]);
    }

    /** Copies a sub-array from another growable array */
    public void copy(GrowableOrdinateArray other, int from, int to) {
        ensureLength(to + 1);
        System.arraycopy(other.data, 0, data, 0, to + 1);
        this.curr = to + 1;
    }
}
