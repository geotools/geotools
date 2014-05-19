/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
package org.geotools.processing.jai.nodata;

import java.awt.image.DataBuffer;

/**
 * Abstract class used for checking if a selected value is inside the selected Range. All the methods throw an {@link UnsupportedOperationException}
 * but for every subclass one of these methods is overridden with the correct functionality. These 6 methods are different only for the data type
 * used. In this way it is possible to reach a better performance by using primitive variables than generic. All the subclasses can contain a Range
 * composed by a minimum and a maximum or a single-point Range. For Double and Float data type the NaN data can be used only with a single-point
 * Range.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 * 
 */
public abstract class Range {

    public enum DataType {
        BYTE(Byte.class, DataBuffer.TYPE_BYTE), USHORT(Short.class, DataBuffer.TYPE_USHORT), SHORT(
                Short.class, DataBuffer.TYPE_SHORT), INTEGER(Integer.class, DataBuffer.TYPE_INT), FLOAT(
                Float.class, DataBuffer.TYPE_FLOAT), DOUBLE(Double.class, DataBuffer.TYPE_DOUBLE),
        // FIXME LONG VALUE CAN BE CHANGED IF LONG DATA TYPE TAKES ANOTHER VALUE
        LONG(Long.class, DataBuffer.TYPE_DOUBLE + 1);

        private Class<?> classType;

        private int dataType;

        private DataType(Class<?> classType, int dataType) {
            this.classType = classType;
            this.dataType = dataType;
        }

        public Class<?> getClassValue() {
            return classType;
        }

        public int getDataType() {
            return dataType;
        }

        public static int dataTypeFromClass(Class<?> classType) {
            if (classType == BYTE.getClass()) {
                return BYTE.getDataType();
            } else if (classType == SHORT.getClass()) {
                return SHORT.getDataType();
            } else if (classType == INTEGER.getClass()) {
                return INTEGER.getDataType();
            } else if (classType == FLOAT.getClass()) {
                return FLOAT.getDataType();
            } else if (classType == DOUBLE.getClass()) {
                return DOUBLE.getDataType();
            } else if (classType == LONG.getClass()) {
                return LONG.getDataType();
            } else {
                throw new IllegalArgumentException(
                        "This class does not belong to the already existing classes");
            }
        }

    }

    /** Method for checking if a byte value is contained inside the Range */
    public boolean contains(byte value) {
        throw new UnsupportedOperationException("Wrong data type");
    }

    /** Method for checking if a short/ushort value is contained inside the Range */
    public boolean contains(short value) {
        throw new UnsupportedOperationException("Wrong data type");
    }

    /** Method for checking if an integer value is contained inside the Range */
    public boolean contains(int value) {
        throw new UnsupportedOperationException("Wrong data type");
    }

    /** Method for checking if a float value is contained inside the Range */
    public boolean contains(float value) {
        throw new UnsupportedOperationException("Wrong data type");
    }

    /** Method for checking if a double value is contained inside the Range */
    public boolean contains(double value) {
        throw new UnsupportedOperationException("Wrong data type");
    }

    /** Method for checking if a long value is contained inside the Range */
    public boolean contains(long value) {
        throw new UnsupportedOperationException("Wrong data type");
    }

    /** Method for checking if a Generic value is contained inside the Range */
    public <T extends Number & Comparable<T>> boolean contains(T value) {
        int dataType = this.getDataType().getDataType();
        switch (dataType) {
        case DataBuffer.TYPE_BYTE:
            return contains(value.byteValue());
        case DataBuffer.TYPE_USHORT:
        case DataBuffer.TYPE_SHORT:
            return contains(value.shortValue());
        case DataBuffer.TYPE_INT:
            return contains(value.intValue());
        case DataBuffer.TYPE_FLOAT:
            return contains(value.floatValue());
        case DataBuffer.TYPE_DOUBLE:
            return contains(value.doubleValue());
        case DataBuffer.TYPE_DOUBLE + 1:
            return contains(value.longValue());
        default:
            throw new IllegalArgumentException("Wrong data type");
        }
    }

    /** Returns the Range data Type */
    public abstract DataType getDataType();

    /** Indicates if the Range is a degenerated single point Range or not */
    public abstract boolean isPoint();

    /** Returns the maximum bound of the Range */
    public abstract Number getMax();

    /** Returns the minimum bound of the Range */
    public abstract Number getMin();

}
