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

/**
 * This class is a factory class which creates a {@link Range} object for the specific data type. This Range can have 2 bounds or be a single-point
 * range. If the 2 bound values are equal and almost one of them is included, then a single-point range is created, else an exception is thrown. If
 * the minimum bound value is bigger than the maximum value, then the 2 numbers are inverted at the Range creation time.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 */
public class RangeFactory {

    // Private Constructor for avoiding a new factory instantiation
    private RangeFactory() {
    }

    // Byte data
    public static Range create(byte minValue, boolean minIncluded, byte maxValue,
            boolean maxIncluded) {
        return new RangeByte(minValue, minIncluded, maxValue, maxIncluded);
    }

    // Ushort data
    public static Range createU(short minValue, boolean minIncluded, short maxValue,
            boolean maxIncluded) {
        return new RangeUshort(minValue, minIncluded, maxValue, maxIncluded);
    }

    // Short data
    public static Range create(short minValue, boolean minIncluded, short maxValue,
            boolean maxIncluded) {
        return new RangeShort(minValue, minIncluded, maxValue, maxIncluded);
    }

    // Integer data
    public static Range create(int minValue, boolean minIncluded, int maxValue, boolean maxIncluded) {
        return new RangeInt(minValue, minIncluded, maxValue, maxIncluded);
    }

    // Float data
    public static Range create(float minValue, boolean minIncluded, float maxValue,
            boolean maxIncluded, boolean nanIncluded) {
        return new RangeFloat(minValue, minIncluded, maxValue, maxIncluded, nanIncluded);
    }

    // Double data
    public static Range create(double minValue, boolean minIncluded, double maxValue,
            boolean maxIncluded, boolean nanIncluded) {
        return new RangeDouble(minValue, minIncluded, maxValue, maxIncluded, nanIncluded);
    }

    // Long data
    public static Range create(long minValue, boolean minIncluded, long maxValue,
            boolean maxIncluded) {
        return new RangeLong(minValue, minIncluded, maxValue, maxIncluded);
    }
}
