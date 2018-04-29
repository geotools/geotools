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
import org.geotools.util.DateRange;

/**
 * A treeset implementation with a built-in comparator for DateRange objects
 *
 * @author Andrea Aime - GeoSolutions
 */
public class DateRangeTreeSet extends TreeSet<DateRange> {

    private static final long serialVersionUID = -1613807310486642564L;

    static DateRangeComparator COMPARATOR = new DateRangeComparator();

    public DateRangeTreeSet() {
        super(COMPARATOR);
    }

    public DateRangeTreeSet(Collection<? extends DateRange> c) {
        super(COMPARATOR);
        addAll(c);
    }

    public DateRangeTreeSet(SortedSet<DateRange> s) {
        super(COMPARATOR);
        addAll(s);
    }
}
