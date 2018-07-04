/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.util.Range;
import org.geotools.util.Utilities;

/** A NumberRange comparator */
public class NumberRangeComparator implements Comparator<Range<? extends Number>> {

    @Override
    public int compare(Range<? extends Number> firstRange, Range<? extends Number> secondRange) {
        Utilities.ensureNonNull("firstRange", firstRange);
        Utilities.ensureNonNull("secondRange", secondRange);
        final Number firstRangeMin = firstRange.getMinValue();
        final Number firstRangeMax = firstRange.getMaxValue();
        final Number secondRangeMin = secondRange.getMinValue();
        final Number secondRangeMax = secondRange.getMaxValue();
        return doubleCompare(
                firstRangeMin.doubleValue(),
                firstRangeMax.doubleValue(),
                secondRangeMin.doubleValue(),
                secondRangeMax.doubleValue());
    }

    /**
     * Given a set of 4 double representing the extrema of 2 ranges, compare the 2 ranges.
     *
     * @param firstRangeMin the min value of the first range
     * @param firstRangeMax the max value of the first range
     * @param secondRangeMin the min value of the second range
     * @param secondRangeMax the max value of the second range
     * @return TODO: Improve that logic to deal with special cases on intervals management
     */
    public static int doubleCompare(
            final double firstRangeMin,
            final double firstRangeMax,
            final double secondRangeMin,
            final double secondRangeMax) {
        if (firstRangeMin == secondRangeMin && firstRangeMax == secondRangeMax) {
            return 0;
        }
        if (firstRangeMin == secondRangeMin) {
            if (firstRangeMax > secondRangeMax) {
                return +1;
            } else {
                return -1;
            }
        } else if (firstRangeMin < secondRangeMin) {
            if (firstRangeMax > secondRangeMax) {
                return -3;
            } else if (firstRangeMax < secondRangeMax) {
                return -4;
            } else {
                return -2;
            }
        } else {
            if (firstRangeMax < secondRangeMax) {
                return +3;
            } else if (firstRangeMax > secondRangeMax) {
                return +4;
            } else {
                return +2;
            }
        }
    }
}
