/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import org.geotools.util.DateRange;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.opengis.feature.Feature;

/**
 * Generates a list of compact DateRanges from a collection
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 */
class DateRangeVisitor extends RangeVisitor {

    public DateRangeVisitor(String attributeTypeName1, String attributeTypeName2) {
        super(attributeTypeName1, attributeTypeName2, RangeType.DATE);
    }

    public void visit(Feature feature) {
        Object firstValue = expr1.evaluate(feature);
        Object secondValue = expr2.evaluate(feature);
        if (firstValue != null && secondValue != null) {
            long begin = ((Timestamp) firstValue).getTime();
            long end = ((Timestamp) secondValue).getTime();
            Date beginDate = new Date(begin);
            Date endDate = new Date(end);
            set.add(new DateRange(beginDate, endDate));
        }
    }

    /**
     * Setup the minimal set of dataRanges (intersecting ranges are merged together) as 
     * a Set of ISO8601 String intervals with period.
     * 
     * @return
     */
    @Override
    protected void populateRange() {
        Iterator<? extends Range> iterator = set.iterator();
        DateRange second = null;
        DateRange first;
        while (iterator.hasNext()) {
            first = second;
            second = (DateRange) iterator.next();
            if (first != null) {
                // Compact intervals with intersections
                if (second.intersects(first)) {
                    second = merge(first, second);
                    if (!iterator.hasNext()) {
                        minimalRanges.add(formatRange(second));
                    }
                } else {
                    minimalRanges.add(formatRange(second));
                }
            }
        }
    }

    /**
     * Format a DateRange into ISO8601 interval strings
     * 
     * @param range
     * @return
     */
    private String formatRange(DateRange range) {
        StringBuilder builder = new StringBuilder();
        String begin = ConvertersHack.convert(range.getMinValue(), String.class);
        String end = ConvertersHack.convert(range.getMaxValue(), String.class);
        builder.append(begin + "/" + end + "/PT1S");
        return builder.toString();
    }

    /**
     * Merge 2 ranges together. In order to speed up the computations, 
     * this method does the assumption that the 2 date ranges are already sorted
     * (first < second) and they intersect together.
     * 
     * @param firstDateRange
     * @param secondDateRange
     * @return
     */
    private static DateRange merge(DateRange firstDateRange, DateRange secondDateRange) {
        Utilities.ensureNonNull("firstDateRange", firstDateRange);
        Utilities.ensureNonNull("secondDateRange", secondDateRange);
        long beginFirst = firstDateRange.getMinValue().getTime();
        long endFirst = firstDateRange.getMaxValue().getTime();
        long beginSecond = secondDateRange.getMinValue().getTime();
        long endSecond = secondDateRange.getMaxValue().getTime();
        long max = Math.max(endFirst, endSecond);
        long min = Math.min(beginFirst, beginSecond);
        Date beginDate = new Date(min);
        Date endDate = new Date(max);
        return new DateRange(beginDate, endDate);
    }
}
