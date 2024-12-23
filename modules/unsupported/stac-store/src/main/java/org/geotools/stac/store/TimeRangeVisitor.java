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

import java.util.Date;
import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNil;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.filter.visitor.NullFilterVisitor;
import org.geotools.util.DateRange;
import org.geotools.util.Range;

/**
 * Returns a {@link DateRange} if the filter is equivalent to a date range on the "datetime" property, <code>null</code>
 * otherwise, meaning there is more filtering included. <br>
 * The visitor is not thread safe, call <code>reset</code> in case you want to re-use and need to check the "exact"
 * property.
 */
public class TimeRangeVisitor extends NullFilterVisitor {

    public static final Date DATE_NEGATIVE_INFINITE = new Date(Long.MIN_VALUE);
    public static final Date DATE_POSITIVE_INFINITE = new Date(Long.MAX_VALUE);
    static final DateRange INFINITY = new DateRange(DATE_NEGATIVE_INFINITE, false, DATE_POSITIVE_INFINITE, false);

    private static final String DATETIME = "datetime";

    boolean exact = true;
    private String timeProperty = DATETIME;

    /** Returns the property name used to match time range expressions */
    public String getTimeProperty() {
        return timeProperty;
    }

    /** Sets the property name used to match time range expressions */
    public void setTimeProperty(String timeProperty) {
        this.timeProperty = timeProperty;
    }

    /**
     * Returns true if the date range exactly represents the filter, false if there are other components
     *
     * @return
     */
    public boolean isExact() {
        return exact;
    }

    public void reset() {
        this.exact = true;
    }

    private boolean isTimeLiteral(Expression ex) {
        return ex instanceof Literal && ex.evaluate(null, Date.class) != null;
    }

    /**
     * Checks if a property is a time property, users can override if they have more sophisticated logic than matching a
     * single property name
     *
     * @param expression
     * @return
     */
    protected boolean isTimeProperty(Expression expression) {
        return expression instanceof PropertyName && timeProperty.equals(((PropertyName) expression).getPropertyName());
    }

    private DateRange infinity() {
        exact = false;
        return INFINITY;
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        exact = false;
        return null;
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return INFINITY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visit(And filter, Object extraData) {
        DateRange mixed = INFINITY;
        for (Filter f : filter.getChildren()) {
            DateRange range = (DateRange) f.accept(this, extraData);
            Range<Date> in = (Range<Date>) mixed.intersect(range);
            mixed = toDateRange(in);
        }
        return mixed;
    }

    private DateRange toDateRange(Range<Date> in) {
        return new DateRange(in.getMinValue(), in.isMinIncluded(), in.getMaxValue(), in.isMaxIncluded());
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        // no matter what we have to return an infinite envelope
        // rationale
        // !(finite range) -> an unbounded range -> infinite
        // !(non time filter) -> infinite (no tim concern)
        // !(infinite) -> ... infinite, as the first infinite could be the result
        // of !(finite range)

        return INFINITY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visit(Or filter, Object extraData) {
        DateRange mixed = null;
        for (Filter f : filter.getChildren()) {
            DateRange range = (DateRange) f.accept(this, extraData);
            if (range != null) {
                if (mixed == null) {
                    mixed = range;
                } else {
                    // if there is space in between, the range won't be an exact match for the
                    // original filters anymore
                    exact &= mixed.intersects(range);
                    Range<Date> un = (Range<Date>) mixed.union(range);
                    mixed = toDateRange(un);
                }
            }
        }
        return mixed;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        if (!isTimeProperty(filter.getExpression())
                || !isTimeLiteral(filter.getLowerBoundary())
                || !isTimeLiteral(filter.getUpperBoundary())) {
            return infinity();
        }

        // both ends are included
        Date low = filter.getLowerBoundary().evaluate(null, Date.class);
        Date high = filter.getUpperBoundary().evaluate(null, Date.class);
        return new DateRange(low, high);
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        boolean p1 = isTimeProperty(filter.getExpression1());
        boolean p2 = isTimeProperty(filter.getExpression2());
        boolean l1 = isTimeLiteral(filter.getExpression1());
        boolean l2 = isTimeLiteral(filter.getExpression2());
        if (!(p1 && l2) && !(p2 && l1)) return infinity();

        Date date;
        if (p1 && l2) date = filter.getExpression2().evaluate(null, Date.class);
        else date = filter.getExpression1().evaluate(null, Date.class);
        return new DateRange(date, date);
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        // cannot build a range representing lack of a single point anyways
        return infinity();
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return greater(filter, false);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return greater(filter, true);
    }

    private DateRange greater(BinaryComparisonOperator filter, boolean included) {
        boolean p1 = isTimeProperty(filter.getExpression1());
        boolean p2 = isTimeProperty(filter.getExpression2());
        boolean l1 = isTimeLiteral(filter.getExpression1());
        boolean l2 = isTimeLiteral(filter.getExpression2());
        if (!(p1 && l2) && !(p2 && l1)) return infinity();

        if (p1 && l2) {
            Date date = filter.getExpression2().evaluate(null, Date.class);
            return new DateRange(date, included, DATE_POSITIVE_INFINITE, false);
        } else {
            Date date = filter.getExpression1().evaluate(null, Date.class);
            return new DateRange(DATE_NEGATIVE_INFINITE, false, date, included);
        }
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return lesser(filter, false);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return lesser(filter, true);
    }

    private DateRange lesser(BinaryComparisonOperator filter, boolean included) {
        boolean p1 = isTimeProperty(filter.getExpression1());
        boolean p2 = isTimeProperty(filter.getExpression2());
        boolean l1 = isTimeLiteral(filter.getExpression1());
        boolean l2 = isTimeLiteral(filter.getExpression2());
        if (!(p1 && l2) && !(p2 && l1)) return infinity();

        if (p1 && l2) {
            Date date = filter.getExpression2().evaluate(null, Date.class);
            return new DateRange(DATE_NEGATIVE_INFINITE, false, date, included);
        } else {
            Date date = filter.getExpression1().evaluate(null, Date.class);
            return new DateRange(date, included, DATE_POSITIVE_INFINITE, false);
        }
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return infinity();
    }

    @Override
    public Object visit(After filter, Object extraData) {
        boolean p1 = isTimeProperty(filter.getExpression1());
        boolean p2 = isTimeProperty(filter.getExpression2());
        boolean l1 = isTimeLiteral(filter.getExpression1());
        boolean l2 = isTimeLiteral(filter.getExpression2());
        if (!(p1 && l2) && !(p2 && l1)) return infinity();

        if (p1 && l2) {
            Date date = filter.getExpression2().evaluate(null, Date.class);
            return new DateRange(date, false, DATE_POSITIVE_INFINITE, false);
        } else {
            Date date = filter.getExpression1().evaluate(null, Date.class);
            return new DateRange(DATE_NEGATIVE_INFINITE, false, date, false);
        }
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(Before filter, Object extraData) {
        boolean p1 = isTimeProperty(filter.getExpression1());
        boolean p2 = isTimeProperty(filter.getExpression2());
        boolean l1 = isTimeLiteral(filter.getExpression1());
        boolean l2 = isTimeLiteral(filter.getExpression2());
        if (!(p1 && l2) && !(p2 && l1)) return infinity();

        if (p1 && l2) {
            Date date = filter.getExpression2().evaluate(null, Date.class);
            return new DateRange(DATE_NEGATIVE_INFINITE, false, date, false);
        } else {
            Date date = filter.getExpression1().evaluate(null, Date.class);
            return new DateRange(date, false, DATE_POSITIVE_INFINITE, false);
        }
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(During during, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }

    @Override
    public Object visit(TEquals filter, Object extraData) {
        boolean p1 = isTimeProperty(filter.getExpression1());
        boolean p2 = isTimeProperty(filter.getExpression2());
        boolean l1 = isTimeLiteral(filter.getExpression1());
        boolean l2 = isTimeLiteral(filter.getExpression2());
        if (!(p1 && l2) && !(p2 && l1)) return infinity();

        Date date;
        if (p1 && l2) date = filter.getExpression2().evaluate(null, Date.class);
        else date = filter.getExpression1().evaluate(null, Date.class);
        return new DateRange(date, date);
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        // no support for range oriented comparisons yet
        return infinity();
    }
}
