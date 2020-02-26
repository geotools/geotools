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
package org.geotools.temporal.object;

import javax.measure.Unit;
import org.geotools.util.Utilities;
import org.opengis.temporal.IntervalLength;

/**
 * A data type for intervals of time which supports the expression of duration in terms of a
 * specified multiple of a single unit of time.
 *
 * @author Mehdi Sidhoum (Geomatys)
 */
public class DefaultIntervalLength extends DefaultDuration implements IntervalLength {

    /** This is the name of the unit of measure used to express the length of the interval. */
    private Unit unit;
    /** This is the base of the multiplier of the unit. */
    private int radix;
    /** This is the exponent of the base. */
    private int factor;
    /**
     * This is the length of the time interval as an integer multiple of one radix(exp -factor) of
     * the specified unit.
     */
    private int value;

    /**
     * Creates a new instance of IntervalUnit example : Unit="second" radix=10 factor=3 value=7
     * specifies a time interval length of 7ms.
     */
    public DefaultIntervalLength(Unit unit, int radix, int factor, int value) {
        this.unit = unit;
        this.radix = radix;
        this.factor = factor;
        this.value = value;
    }

    /** The unit of measure used to express the length of the interval. */
    public Unit getUnit() {
        return unit;
    }

    /** A positive integer that is the base of the mulitplier of the unit. */
    public int getRadix() {
        return radix;
    }

    /** The exponent of the base. */
    public int getFactor() {
        return factor;
    }

    public int getValue() {
        return value;
    }

    @Override
    public long getTimeInMillis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultIntervalLength) {
            final DefaultIntervalLength that = (DefaultIntervalLength) object;

            return Utilities.equals(this.factor, that.factor)
                    && Utilities.equals(this.radix, that.radix)
                    && Utilities.equals(this.unit, that.unit)
                    && Utilities.equals(this.value, that.unit);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.unit != null ? this.unit.hashCode() : 0);
        hash = 37 * hash + this.factor;
        hash = 37 * hash + this.radix;
        hash = 37 * hash + this.value;
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("IntervalLength:").append('\n');
        if (unit != null) {
            s.append("unit:").append(unit).append('\n');
        }
        s.append("radix:").append(radix).append('\n');
        s.append("factor:").append(factor).append('\n');
        s.append("value:").append(value).append('\n');

        return s.toString();
    }
}
