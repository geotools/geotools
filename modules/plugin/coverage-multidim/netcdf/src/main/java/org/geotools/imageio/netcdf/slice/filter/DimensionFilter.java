/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.slice.filter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Filter on a single dimension of the coverage. */
public interface DimensionFilter {
    // like an Identity
    DimensionFilter ALL = new DimensionFilter() {
        @Override
        public DimensionFilter and(DimensionFilter other) {
            return other == null ? this : other;
        }
    };

    default DimensionFilter and(DimensionFilter other) {
        if (other == null || other == ALL) return this;
        if (this == ALL) return other;
        return new CompositeDimensionFilter(this, other);
    }

    /**
     * A filter that matches if the coordinate value on the given dimension is contained in the expected list. Used to
     * represent OR-ed equality conditions on dimensions (e.g. time = v1 OR time = v2 OR ...)
     */
    final class ListFilter implements DimensionFilter {
        private final Set<Object> values;

        public ListFilter(List<Object> values) {
            this.values = new HashSet<>();
            this.values.addAll(values);
        }

        public Set<Object> getValues() {
            return values;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    /** A filter that matches if the coordinate value on the given dimension is contained in the expected range. */
    final class RangeFilter implements DimensionFilter {
        private final Comparable lower;
        private final boolean lowerInclusive;
        private final Comparable upper;
        private final boolean upperInclusive;

        public RangeFilter(Object lower, boolean lowerInclusive, Object upper, boolean upperInclusive) {
            this.lower = (Comparable) normalizeComparable(lower);
            this.lowerInclusive = lowerInclusive;
            this.upper = (Comparable) normalizeComparable(upper);
            this.upperInclusive = upperInclusive;
        }

        private Object normalizeComparable(Object value) {
            if (value instanceof Date) {
                return ((Date) value).getTime();
            }
            return value;
        }

        public Comparable getLower() {
            return lower;
        }

        public Comparable getUpper() {
            return upper;
        }

        public boolean isLowerInclusive() {
            return lowerInclusive;
        }

        public boolean isUpperInclusive() {
            return upperInclusive;
        }
    }

    /** Associates a filter with a specific additional logical dimension. */
    record AdditionalDimensionFilter(int dimensionIndex, DimensionFilter filter) {}

    /**
     * A composite filter that combines two filters with logical AND. Used to merge multiple filters on the same
     * dimension (e.g. from multiple OR-ed equality conditions) into a single filter.
     */
    record CompositeDimensionFilter(DimensionFilter left, DimensionFilter right) implements DimensionFilter {}

    /** A filter that matches if the coordinate value on the given dimension is equal to the expected value. */
    record ExactDimensionFilter(Object expected) implements DimensionFilter {}
}
