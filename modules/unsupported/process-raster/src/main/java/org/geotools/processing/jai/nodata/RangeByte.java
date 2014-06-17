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
 * This class is a subclass of the {@link Range} class handling byte data.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 */
public class RangeByte extends Range {

    /** Minimum range bound */
    private final byte minValue;

    /** Maximum range bound */
    private final byte maxValue;

    /** Boolean indicating if the minimum bound is included */
    private final boolean minIncluded;

    /** Boolean indicating if the maximum bound is included */
    private final boolean maxIncluded;

    /** Boolean indicating if the maximum bound is included */
    private final boolean isPoint;

    RangeByte(byte minValue, boolean minIncluded, byte maxValue, boolean maxIncluded) {

        if (minValue < maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.isPoint = false;
            this.minIncluded = minIncluded;
            this.maxIncluded = maxIncluded;
        } else if (minValue > maxValue) {
            this.minValue = maxValue;
            this.maxValue = minValue;
            this.isPoint = false;
            this.minIncluded = minIncluded;
            this.maxIncluded = maxIncluded;
        } else {
            this.minValue = minValue;
            this.maxValue = minValue;
            this.isPoint = true;
            if (!minIncluded && !maxIncluded) {
                throw new IllegalArgumentException(
                        "Cannot create a single-point range without minimum and maximum "
                                + "bounds included");
            } else {
                this.minIncluded = true;
                this.maxIncluded = true;
            }
        }
    }

    @Override
    public boolean contains(byte value) {
        if (isPoint) {
            return this.minValue == value;
        } else {
            final boolean lower;
            final boolean upper;

            if (minIncluded) {
                lower = value < minValue;
            } else {
                lower = value <= minValue;
            }

            if (maxIncluded) {
                upper = value > maxValue;
            } else {
                upper = value >= maxValue;
            }

            return !lower && !upper;
        }
    }

    @Override
    public DataType getDataType() {
        return DataType.BYTE;
    }

    @Override
    public boolean isPoint() {
        return isPoint;
    }

    @Override
    public Number getMax() {
        return maxValue;
    }

    @Override
    public Number getMin() {
        return minValue;
    }
}
