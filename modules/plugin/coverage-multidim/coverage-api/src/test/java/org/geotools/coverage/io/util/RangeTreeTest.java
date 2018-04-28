/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.junit.Assert;
import org.junit.Test;

/** Testing Range Trees and related comparators */
public class RangeTreeTest extends Assert {

    @Test
    public void testDateRangeOrderCompare() {
        final long time1 = 1000000000l;
        final Date start1 = new Date(time1);
        final Date end1 = new Date(time1 + 1000000l);

        final long time2 = 1000100000l;
        final Date start2 = new Date(time2);
        final Date end2 = new Date(time2 + 1000000l);

        final DateRange dr1 = new DateRange(start1, end1);
        final DateRange dr2 = new DateRange(start2, end2);
        final List<DateRange> list = new ArrayList<DateRange>();
        list.add(dr2);
        list.add(dr1);

        final DateRangeTreeSet treeSet = new DateRangeTreeSet(list);
        assertSame(dr2, treeSet.last());
        assertSame(dr1, treeSet.first());

        final DateRangeTreeSet treeSet2 = new DateRangeTreeSet();
        treeSet2.add(dr2);
        treeSet2.add(dr1);
        assertSame(treeSet.last(), treeSet2.last());
        assertSame(treeSet.first(), treeSet2.first());
    }

    @Test
    public void testDoubleRangeOrderCompare() {
        final NumberRange<Double> dr1 = NumberRange.create(5d, 12d);
        final NumberRange<Double> dr2 = NumberRange.create(10d, 15d);
        final NumberRange<Double> dr3 = NumberRange.create(10d, 20d);
        final NumberRange<Double> dr4 = NumberRange.create(15d, 20d);
        final NumberRange<Double> dr5 = NumberRange.create(15d, 30d);
        final NumberRange<Double> dr6 = NumberRange.create(20d, 30d);
        final NumberRange<Double> dr7 = NumberRange.create(25d, 30d);

        final List<NumberRange<Double>> list = new ArrayList<NumberRange<Double>>();
        list.add(dr1);
        list.add(dr2);
        list.add(dr3);
        list.add(dr4);
        list.add(dr5);
        list.add(dr6);
        list.add(dr7);

        final DoubleRangeTreeSet treeSet = new DoubleRangeTreeSet(list);
        assertSame(dr7, treeSet.last());
        assertSame(dr1, treeSet.first());

        // Make sure that even with different insert order, the results come back in the same order
        final DoubleRangeTreeSet treeSet2 = new DoubleRangeTreeSet();
        treeSet2.add(dr2);
        treeSet2.add(dr1);
        treeSet2.add(dr3);
        treeSet2.add(dr7);
        treeSet2.add(dr5);
        treeSet2.add(dr4);
        treeSet2.add(dr6);

        assertSame(treeSet2.last(), treeSet.last());
        assertSame(treeSet2.first(), treeSet.first());
    }
}
