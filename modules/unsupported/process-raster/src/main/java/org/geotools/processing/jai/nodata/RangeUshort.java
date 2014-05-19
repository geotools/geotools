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
 * This class is a subclass of the {@link Range} class handling unsigned short data.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 */
public class RangeUshort extends Range {

    /** Minimum range bound */
    private final int minValue;

    /** Maximum range bound */
    private final int maxValue;

    /** Boolean indicating if the minimum bound is included */
    private final boolean minIncluded;

    /** Boolean indicating if the maximum bound is included */
    private final boolean maxIncluded;

    /** Boolean indicating if the maximum bound is included */
    private final boolean isPoint;

    RangeUshort(short minValue, boolean minIncluded, short maxValue, boolean maxIncluded) {
        int valueMin = minValue & 0xFFFF;
        int valueMax = maxValue & 0xFFFF;

        if (minValue < maxValue) {
            this.minValue = valueMin;
            this.maxValue = valueMax;
            this.isPoint = false;
            this.minIncluded = minIncluded;
            this.maxIncluded = maxIncluded;
        } else if (minValue > maxValue) {
            this.minValue = valueMax;
            this.maxValue = valueMin;
            this.isPoint = false;
            this.minIncluded = minIncluded;
            this.maxIncluded = maxIncluded;
        } else {
            this.minValue = valueMin;
            this.maxValue = valueMin;
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
    public boolean contains(short value) {

        final int valueUshort = value & 0xFFFF;

        if (isPoint) {
            return this.minValue == valueUshort;
        } else {
            final boolean lower;
            final boolean upper;

            if (minIncluded) {
                lower = valueUshort < minValue;
            } else {
                lower = valueUshort <= minValue;
            }

            if (maxIncluded) {
                upper = valueUshort > maxValue;
            } else {
                upper = valueUshort >= maxValue;
            }

            return !lower && !upper;
        }
    }

    @Override
    public DataType getDataType() {
        return DataType.USHORT;
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
