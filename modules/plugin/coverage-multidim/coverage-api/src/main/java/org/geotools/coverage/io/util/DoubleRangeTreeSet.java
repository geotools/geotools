/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.util;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import org.geotools.util.NumberRange;

/**
 * A treeset implementation with a built-in comparator for NumberRange<Double> objects
 *
 * @author Andrea Aime - GeoSolutions
 */
public class DoubleRangeTreeSet extends TreeSet<NumberRange<Double>> {

    private static final long serialVersionUID = -1613807310486642564L;

    static NumberRangeComparator COMPARATOR = new NumberRangeComparator();

    public DoubleRangeTreeSet() {
        super(COMPARATOR);
    }

    public DoubleRangeTreeSet(Collection<? extends NumberRange<Double>> c) {
        super(COMPARATOR);
        addAll(c);
    }

    public DoubleRangeTreeSet(SortedSet<NumberRange<Double>> s) {
        super(COMPARATOR);
        addAll(s);
    }
}
