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

import java.util.Comparator;
import org.geotools.util.DateRange;
import org.geotools.util.Utilities;

/** A DateRange comparator */
public class DateRangeComparator implements Comparator<DateRange> {

    @Override
    public int compare(DateRange firstDateRange, DateRange secondDateRange) {
        Utilities.ensureNonNull("firstDateRange", firstDateRange);
        Utilities.ensureNonNull("secondDateRange", secondDateRange);
        final long beginFirst = firstDateRange.getMinValue().getTime();
        final long endFirst = firstDateRange.getMaxValue().getTime();
        final long beginSecond = secondDateRange.getMinValue().getTime();
        final long endSecond = secondDateRange.getMaxValue().getTime();
        return NumberRangeComparator.doubleCompare(beginFirst, endFirst, beginSecond, endSecond);
    }
}
