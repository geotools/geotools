/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Converters;
import org.geotools.util.DateRange;
import org.junit.Before;
import org.junit.Test;

public class TimeRangeVisitorTest {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    static final BBOX BBOX = FF.bbox("", 12, 50, 12.001, 50.001, "EPSG:4326");
    private static final String JULY_22_2022 = "2022-07-22T10:05:59.024Z";
    private static final String JULY_23_2022 = "2022-07-23T10:05:59.024Z";
    private static final String JULY_24_2022 = "2022-07-24T10:05:59.024Z";
    private static final String JULY_25_2022 = "2022-07-25T10:05:59.024Z";

    private static final Date JULY_22_2022_D = Converters.convert(JULY_22_2022, Date.class);

    private static final Date JULY_24_2022_D = Converters.convert(JULY_24_2022, Date.class);

    private static final Date JULY_25_2022_D = Converters.convert(JULY_25_2022, Date.class);

    private static final String DATETIME = "datetime";
    private static final PropertyName DATETIME_P = FF.property(DATETIME);
    static final Filter DATETIME_EQ = FF.equals(DATETIME_P, FF.literal(JULY_22_2022));

    static final Filter DATETIME_LESS = FF.less(DATETIME_P, FF.literal(JULY_22_2022));

    static final Filter DATETIME_LESS_FLIP = FF.less(FF.literal(JULY_22_2022), DATETIME_P);

    static final Filter DATETIME_LESS_EQ = FF.lessOrEqual(DATETIME_P, FF.literal(JULY_22_2022));

    static final Filter DATETIME_GREATER = FF.greater(DATETIME_P, FF.literal(JULY_22_2022));

    static final Filter DATETIME_GREATER_FLIP = FF.greater(FF.literal(JULY_22_2022), DATETIME_P);

    static final Filter DATETIME_GREATER_EQ =
            FF.greaterOrEqual(DATETIME_P, FF.literal(JULY_22_2022));

    static final Filter DATETIME_BEFORE = FF.before(DATETIME_P, FF.literal(JULY_22_2022));
    static final Filter DATETIME_AFTER = FF.after(DATETIME_P, FF.literal(JULY_22_2022));

    TimeRangeVisitor visitor = new TimeRangeVisitor();

    @Before
    public void reset() {
        visitor.reset();
    }

    @Test
    public void bbox() throws Exception {
        DateRange range = (DateRange) BBOX.accept(visitor, null);
        assertEquals(TimeRangeVisitor.INFINITY, range);
        assertFalse(visitor.isExact());
    }

    @Test
    public void equality() {
        DateRange range = (DateRange) DATETIME_EQ.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertEquals(JULY_22_2022_D, range.getMaxValue());
    }

    @Test
    public void less() {
        DateRange range = (DateRange) DATETIME_LESS.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(TimeRangeVisitor.DATE_NEGATIVE_INFINITE, range.getMinValue());
        assertEquals(JULY_22_2022_D, range.getMaxValue());
        assertFalse(range.isMaxIncluded());
    }

    @Test
    public void before() {
        DateRange range = (DateRange) DATETIME_BEFORE.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(TimeRangeVisitor.DATE_NEGATIVE_INFINITE, range.getMinValue());
        assertEquals(JULY_22_2022_D, range.getMaxValue());
        assertFalse(range.isMaxIncluded());
    }

    @Test
    public void lessFlip() {
        DateRange range = (DateRange) DATETIME_LESS_FLIP.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertEquals(TimeRangeVisitor.DATE_POSITIVE_INFINITE, range.getMaxValue());
        assertFalse(range.isMaxIncluded());
    }

    @Test
    public void lessEq() {
        DateRange range = (DateRange) DATETIME_LESS_EQ.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(TimeRangeVisitor.DATE_NEGATIVE_INFINITE, range.getMinValue());
        assertEquals(JULY_22_2022_D, range.getMaxValue());
        assertTrue(range.isMaxIncluded());
    }

    @Test
    public void greater() {
        DateRange range = (DateRange) DATETIME_GREATER.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertEquals(TimeRangeVisitor.DATE_POSITIVE_INFINITE, range.getMaxValue());
        assertFalse(range.isMaxIncluded());
    }

    @Test
    public void greaterFlip() {
        DateRange range = (DateRange) DATETIME_GREATER_FLIP.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(TimeRangeVisitor.DATE_NEGATIVE_INFINITE, range.getMinValue());
        assertEquals(JULY_22_2022_D, range.getMaxValue());
        assertFalse(range.isMinIncluded());
    }

    @Test
    public void greaterEq() {
        DateRange range = (DateRange) DATETIME_GREATER_EQ.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertEquals(TimeRangeVisitor.DATE_POSITIVE_INFINITE, range.getMaxValue());
        assertTrue(range.isMinIncluded());
    }

    @Test
    public void after() {
        DateRange range = (DateRange) DATETIME_AFTER.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertEquals(TimeRangeVisitor.DATE_POSITIVE_INFINITE, range.getMaxValue());
        assertFalse(range.isMaxIncluded());
    }

    @Test
    public void between() {
        Filter between = FF.between(DATETIME_P, FF.literal(JULY_22_2022), FF.literal(JULY_24_2022));
        DateRange range = (DateRange) between.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertTrue(range.isMinIncluded());
        assertEquals(JULY_24_2022_D, range.getMaxValue());
        assertTrue(range.isMinIncluded());
    }

    @Test
    public void andRange() {
        Filter and = FF.and(DATETIME_AFTER, FF.less(DATETIME_P, FF.literal(JULY_24_2022)));
        DateRange range = (DateRange) and.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertFalse(range.isMinIncluded());
        assertEquals(JULY_24_2022_D, range.getMaxValue());
        assertFalse(range.isMinIncluded());
    }

    @Test
    public void orRangeFull() {
        Filter or = FF.or(DATETIME_AFTER, FF.less(DATETIME_P, FF.literal(JULY_24_2022)));
        DateRange range = (DateRange) or.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(TimeRangeVisitor.INFINITY, range);
    }

    @Test
    public void orRangeOverlap() {
        Filter between1 =
                FF.between(DATETIME_P, FF.literal(JULY_22_2022), FF.literal(JULY_24_2022));
        Filter between2 =
                FF.between(DATETIME_P, FF.literal(JULY_23_2022), FF.literal(JULY_25_2022));
        Filter or = FF.or(between1, between2);
        DateRange range = (DateRange) or.accept(visitor, null);
        assertTrue(visitor.isExact());
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertTrue(range.isMinIncluded());
        assertEquals(JULY_25_2022_D, range.getMaxValue());
        assertTrue(range.isMinIncluded());
    }

    @Test
    public void orRangeDetached() {
        Filter between1 =
                FF.between(DATETIME_P, FF.literal(JULY_22_2022), FF.literal(JULY_23_2022));
        Filter between2 =
                FF.between(DATETIME_P, FF.literal(JULY_24_2022), FF.literal(JULY_25_2022));
        Filter or = FF.or(between1, between2);
        DateRange range = (DateRange) or.accept(visitor, null);
        assertFalse(visitor.isExact()); // the union range is not an exact match any longer
        assertEquals(JULY_22_2022_D, range.getMinValue());
        assertTrue(range.isMinIncluded());
        assertEquals(JULY_25_2022_D, range.getMaxValue());
        assertTrue(range.isMinIncluded());
    }
}
