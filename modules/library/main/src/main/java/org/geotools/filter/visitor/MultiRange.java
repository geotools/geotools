/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.geotools.util.Range;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;

/**
 * Represents the domain of a variable as a set of ranges
 *
 * @author Andrea Aime - GeoSolutions
 * @param <T>
 */
public class MultiRange<T extends Comparable<? super T>> {

    static final class RangeComparator<T extends Comparable<? super T>>
            implements Comparator<Range<T>> {

        @Override
        public int compare(Range<T> o1, Range<T> o2) {
            if (o1 == null) {
                return o2 != null ? -1 : 0;
            } else if (o2 == null) {
                return 1;
            } else if (o1.getMinValue() == null) {
                return o2.getMinValue() == null ? 0 : -1;
            } else if (o2.getMinValue() == null) {
                return 1;
            } else {
                return o1.getMinValue().compareTo(o2.getMinValue());
            }
        }
    }

    TreeSet<Range<T>> ranges = new TreeSet<>(new RangeComparator<>());

    public MultiRange(Range<T> range) {
        this.ranges.add(range);
    }

    public MultiRange(MultiRange<T> other) {
        this.ranges.addAll(other.ranges);
    }

    public MultiRange(List<Range<T>> ranges) {
        this.ranges.addAll(ranges);
    }

    public MultiRange(Class<T> binding, T exclusion) {
        this.ranges.add(new Range<>(binding, null, false, exclusion, false));
        this.ranges.add(new Range<>(binding, exclusion, false, null, false));
    }

    public MultiRange<T> merge(MultiRange<T> other) {
        MultiRange<T> result = new MultiRange<>(this);
        for (Range<T> r : other.ranges) {
            result.addRange(r);
        }
        return result;
    }

    public void addRange(Range<T> range) {
        if (range.isEmpty()) {
            return;
        }
        List<Range<T>> overlapping = getOverlappingRanges(range);
        if (overlapping != null && !overlapping.isEmpty()) {
            ranges.removeAll(overlapping);
            Range<T> combined = range;
            for (Range<T> r : overlapping) {
                @SuppressWarnings("unchecked")
                Range<T> union = (Range<T>) combined.union(r);
                combined = union;
            }
            ranges.add(combined);
        } else {
            ranges.add(range);
        }
    }

    public MultiRange<T> intersect(MultiRange<T> other) {
        List<Range<T>> intersections = new ArrayList<>();
        for (Range<T> r1 : ranges) {
            for (Range<T> r2 : other.ranges) {
                if (r1.intersects(r2)) {
                    @SuppressWarnings("unchecked")
                    Range<T> intersection = (Range<T>) r1.intersect(r2);
                    intersections.add(intersection);
                }
            }
        }

        return new MultiRange<>(intersections);
    }

    public void removeRange(Range<T> range) {
        List<Range<T>> overlapping = getOverlappingRanges(range);

        if (overlapping != null) {
            ranges.removeAll(overlapping);
            List<Range<T>> removed = new ArrayList<>();
            for (Range<T> r : overlapping) {
                @SuppressWarnings("unchecked")
                Range<T>[] difference = (Range<T>[]) r.subtract(range);
                for (Range<T> d : difference) {
                    if (!d.isEmpty()) {
                        removed.add(d);
                    }
                }
            }
            for (Range<T> r : removed) {
                ranges.add(r);
            }
        }
    }

    private List<Range<T>> getOverlappingRanges(Range<T> range) {
        List<Range<T>> overlapping = new ArrayList<>();
        for (Range<T> r : ranges) {
            if (r.intersects(range) || contiguous(r, range)) {
                overlapping.add(r);
            }
        }
        return overlapping;
    }

    private boolean contiguous(Range r1, Range<T> r2) {
        if (r1.getMinValue() != null
                && r2.getMaxValue() != null
                && (r1.isMinIncluded() || r2.isMaxIncluded())) {
            return r1.getMinValue().equals(r2.getMaxValue());
        } else if (r1.getMaxValue() != null
                && r2.getMinValue() != null
                && (r1.isMaxIncluded() || r2.isMinIncluded())) {
            return r1.getMaxValue().equals(r2.getMinValue());
        } else {
            return false;
        }
    }

    public Filter toFilter(FilterFactory ff, Expression variable) {
        if (ranges.isEmpty()) {
            return Filter.EXCLUDE;
        } else if (ranges.size() == 1
                && ranges.first().getMinValue() == null
                && ranges.first().getMaxValue() == null) {
            return Filter.INCLUDE;
        }

        List<Range<T>> rangeList = new ArrayList<>(ranges);
        List<Filter> filters = new ArrayList<>();
        int rangeCount = rangeList.size();
        for (int i = 0; i < rangeCount; ) {
            Range<T> range = rangeList.get(i);
            i++;
            List<T> exclusions = new ArrayList<>();
            Range<T> curr = range;
            while (i < rangeCount) {
                Range<T> next = rangeList.get(i);
                if (next.getMinValue().equals(curr.getMaxValue())) {
                    // do we have a hole?
                    if (!next.isMinIncluded() && !curr.isMaxIncluded()) {
                        exclusions.add(curr.getMaxValue());
                    }
                    i++;
                    curr = next;
                } else {
                    break;
                }
            }
            if (curr == range) {
                // no exclusions, this range is isolated
                filters.add(toFilter(ff, variable, range));
            } else {
                Range<T> union =
                        new Range<>(
                                range.getElementClass(),
                                range.getMinValue(),
                                range.isMinIncluded(),
                                curr.getMaxValue(),
                                curr.isMaxIncluded());
                Filter filter = toFilter(ff, variable, union);
                if (exclusions.isEmpty()) {
                    filters.add(filter);
                } else {
                    List<Filter> exclusionFilters = new ArrayList<>();
                    if (!filter.INCLUDE.equals(filter)) {
                        exclusionFilters.add(filter);
                    }
                    for (T exclusion : exclusions) {
                        PropertyIsNotEqualTo ne = ff.notEqual(variable, ff.literal(exclusion));
                        exclusionFilters.add(ne);
                    }
                    if (exclusionFilters.size() == 1) {
                        filter = exclusionFilters.get(0);
                    } else {
                        filter = ff.and(exclusionFilters);
                    }
                    filters.add(filter);
                }
            }
        }

        if (filters.isEmpty()) {
            return Filter.EXCLUDE;
        } else if (filters.size() == 1) {
            return filters.get(0);
        } else {
            return ff.or(filters);
        }
    }

    private Filter toFilter(FilterFactory ff, Expression variable, Range<T> range) {
        if (range.getMinValue() == null && range.getMaxValue() == null) {
            return Filter.INCLUDE;
        } else if (range.isMinIncluded() && range.isMaxIncluded()) {
            if (range.getMinValue().equals(range.getMaxValue())) {
                return ff.equals(variable, ff.literal(range.getMinValue()));
            }
            return ff.between(
                    variable, ff.literal(range.getMinValue()), ff.literal(range.getMaxValue()));
        } else if (range.getMinValue() == null) {
            return toLessFilter(ff, variable, range);
        } else if (range.getMaxValue() == null) {
            return toGreaterFilter(ff, variable, range);
        } else {
            Filter less = toLessFilter(ff, variable, range);
            Filter greater = toGreaterFilter(ff, variable, range);
            return ff.and(greater, less);
        }
    }

    private Filter toGreaterFilter(FilterFactory ff, Expression variable, Range<T> range) {
        if (range.isMinIncluded()) {
            return ff.greaterOrEqual(variable, ff.literal(range.getMinValue()));
        } else {
            return ff.greater(variable, ff.literal(range.getMinValue()));
        }
    }

    private Filter toLessFilter(FilterFactory ff, Expression variable, Range<T> range) {
        if (range.isMaxIncluded()) {
            return ff.lessOrEqual(variable, ff.literal(range.getMaxValue()));
        } else {
            return ff.less(variable, ff.literal(range.getMaxValue()));
        }
    }

    @Override
    public String toString() {
        return "MultiRange [ranges=" + ranges + "]";
    }
}
