/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.geotools.coverage.io.range;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;

/**
 * Measurement recorded in provided unit.
 *
 * @author Jody Garnett
 * @param <V>
 * @param <Q>
 */
public abstract class Measure<V extends Number, Q extends Quantity<Q>>
        implements Comparable<Measure<V, Q>> {
    /** value of this measure */
    abstract V getValue();

    /** unit of this measure */
    abstract Unit<Q> getUnit();

    abstract Measure<V, Q> to(Unit<Q> unit);

    /**
     * Numeric representation of measure in the provided unit
     *
     * @return numeric representation.
     */
    public Number numericValue(Unit<Q> unit) {
        if (getUnit() == unit || getUnit().equals(unit)) {
            return getValue();
        } else {
            UnitConverter converter = getUnit().getConverterTo(unit);
            return converter.convert(getValue());
        }
    }
    /**
     * Double representation of {@link #getValue()} in the requested unit (values outside of the
     * range {@link Double#MIN_VALUE} - {@link Double#MAX_VALUE} are represented as infinite).
     *
     * @param unit to convert to double
     */
    public double doubleValue(Unit<Q> unit) {
        Number number = numericValue(unit);
        return number.doubleValue();
    }

    /**
     * Long representation of {@link #getValue()} in the requested unit (clipped to range {@link
     * Long#MIN_VALUE} - {@link Double#MAX_VALUE}).
     *
     * @param unit to convert to double
     */
    public long longValue(Unit<Q> unit) {
        Number number = numericValue(unit);
        return number.longValue();
    }

    @Override
    public int compareTo(Measure<V, Q> obj) {
        return Double.compare(doubleValue(getUnit()), obj.doubleValue(getUnit()));
    }
}
