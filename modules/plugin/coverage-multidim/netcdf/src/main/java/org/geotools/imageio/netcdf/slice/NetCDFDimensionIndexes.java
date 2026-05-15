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
package org.geotools.imageio.netcdf.slice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.imageio.netcdf.VariableAdapter.UnidataAdditionalDomain;
import org.geotools.imageio.netcdf.VariableAdapter.UnidataVerticalDomain;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;

/**
 * Dimension indexes and lookup helpers used to resolve NetCDF slice dimensions from domain values.
 *
 * <p>Use regular-axis math when the coordinate variable is regular, and fall back to a compact array-backed lookup
 * otherwise. The provided lookup implementations support exact-match queries as well as lower/upper bound style
 * searches used by range filters.
 *
 * <p>This class is used by the slice-query machinery to convert requested time, elevation, and additional-domain values
 * into dimension indices that can be iterated efficiently.
 */
public final class NetCDFDimensionIndexes {

    /** Base contract for a dimension axis lookup. */
    public interface AxisLookup {
        /** Returns the number of values available on the axis. */
        int size();
    }

    NetCDFDimensionIndexes() {}

    /**
     * Lookup interface for temporal axes such as the TIME dimension.
     *
     * <p>Provides resolution from a temporal value to its corresponding axis index, supporting both exact matches and
     * range queries. Implementations may be backed by regular (start + increment) or irregular coordinate variables.
     *
     * <p>All lookup methods assume the underlying axis values are ordered.
     */
    public interface TimeAxisLookup extends AxisLookup {

        /** Returns the index of the axis element exactly matching the provided value. */
        int exact(Date value);

        /** Returns the first index whose value is after, or optionally equal to, the provided value. */
        int firstIndexAfter(Date value, boolean inclusive);

        /** Returns the exclusive upper bound index for values before, or optionally equal to, the provided value. */
        int lastIndexBefore(Date value, boolean inclusive);

        /** Returns the temporal value at the specified axis index. */
        Date valueAt(int index);
    }

    /**
     * Lookup interface for numeric axes such as elevation or additional numeric dimensions.
     *
     * <p>Provides resolution from a numeric value to its corresponding axis index, supporting both exact matches and
     * range queries. Implementations may be backed by regular (start + increment) or irregular coordinate variables.
     *
     * <p>All lookup methods assume the underlying axis values are ordered.
     */
    public interface NumericAxisLookup extends AxisLookup {

        /** Returns the index of the axis element exactly matching the provided value. */
        int exact(double value);

        /** Returns the first index whose value is after, or optionally equal to, the provided value. */
        int firstIndexAfter(double value, boolean inclusive);

        /** Returns the exclusive upper bound index for values before, or optionally equal to, the provided value. */
        int lastIndexBefore(double value, boolean inclusive);

        /** Returns the numeric value at the specified axis index. */
        double valueAt(int index);
    }

    /** Creates a temporal lookup from a time coordinate variable. */
    public static TimeAxisLookup forTime(CoordinateVariable<Date> cv) throws IOException {
        if (cv.isRegular()) {
            long start = (cv.getMinimum()).getTime();
            int size = (int) cv.getSize();
            long step = computeRegularLongStep(start, (cv.getMaximum()).getTime(), size);
            if (step != Long.MIN_VALUE) {
                return new RegularTimeAxisLookup(size, start, step);
            }
        }
        @SuppressWarnings("unchecked")
        List<Date> dates = cv.read();
        long[] values = new long[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            values[i] = dates.get(i).getTime();
        }
        return new ArrayTimeAxisLookup(values);
    }

    /** Creates a numeric lookup from a vertical coordinate variable. */
    public static NumericAxisLookup forVertical(UnidataVerticalDomain domain) throws IOException {
        CoordinateVariable<? extends Number> cv = domain.getAdaptee();
        if (cv.isRegular()) {
            return new RegularNumericAxisLookup((int) cv.getSize(), cv.getStart(), cv.getIncrement());
        }
        @SuppressWarnings("unchecked")
        List<Number> numbers = (List<Number>) cv.read();
        double[] values = new double[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            values[i] = numbers.get(i).doubleValue();
        }
        return new ArrayNumericAxisLookup(values);
    }

    /** Creates a numeric lookup from an additional numeric coordinate variable. */
    public static NumericAxisLookup forNumericAdditional(UnidataAdditionalDomain domain) throws IOException {
        CoordinateVariable<? extends Number> cv = getCoordinateVariable(domain);
        if (cv.isRegular()) {
            int size = (int) cv.getSize();
            double start = size == 1 ? cv.getMinimum().doubleValue() : cv.getStart();
            double increment = size == 1 ? 0.0 : cv.getIncrement();
            return new RegularNumericAxisLookup(size, start, increment);
        }
        @SuppressWarnings("unchecked")
        List<Number> numbers = (List<Number>) cv.read();
        double[] values = new double[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            values[i] = numbers.get(i).doubleValue();
        }
        return new ArrayNumericAxisLookup(values);
    }

    /** Returns the numeric coordinate variable for the given additional domain. */
    private static CoordinateVariable<? extends Number> getCoordinateVariable(UnidataAdditionalDomain domain) {
        if (domain.getType() != DomainType.NUMBER) {
            throw new IllegalArgumentException("Domain is not numeric: " + domain.getName());
        }
        CoordinateVariable<?> raw = domain.getAdaptee();
        if (!(raw.getType() != null && Number.class.isAssignableFrom(raw.getType()))) {
            throw new IllegalArgumentException("Coordinate variable is not numeric: " + domain.getName());
        }
        @SuppressWarnings("unchecked")
        CoordinateVariable<? extends Number> cv = (CoordinateVariable<? extends Number>) raw;
        return cv;
    }

    /** Creates a temporal lookup from an additional date coordinate variable. */
    public static TimeAxisLookup forDateAdditional(UnidataAdditionalDomain domain) throws IOException {
        if (domain.getType() != DomainType.DATE) {
            throw new IllegalArgumentException("Domain is not date: " + domain.getName());
        }
        CoordinateVariable<?> raw = domain.getAdaptee();
        if (!(raw.getType() != null && Date.class.isAssignableFrom(raw.getType()))) {
            throw new IllegalArgumentException("Coordinate variable is not date: " + domain.getName());
        }
        @SuppressWarnings("unchecked")
        CoordinateVariable<Date> cv = (CoordinateVariable<Date>) raw;
        return forTime(cv);
    }

    /** Computes the regular temporal step from axis endpoints and size. */
    private static long computeRegularLongStep(long start, long end, int size) {
        if (size <= 1) {
            return 0L;
        }
        long delta = end - start;
        int intervals = size - 1;
        if (delta % intervals == 0) {
            return delta / intervals;
        }
        return Long.MIN_VALUE;
    }

    /** Returns a contiguous array of indices in the range [startInclusive, endExclusive). */
    public static int[] contiguousRange(int startInclusive, int endExclusive) {
        if (endExclusive <= startInclusive) {
            return new int[0];
        }
        int[] out = new int[endExclusive - startInclusive];
        for (int i = 0; i < out.length; i++) {
            out[i] = startInclusive + i;
        }
        return out;
    }

    /** Regular time axis lookup backed by size, start, and step. */
    public record RegularTimeAxisLookup(int size, long start, long step) implements TimeAxisLookup {

        @Override
        public int exact(Date value) {
            return exactMillis(value.getTime());
        }

        @Override
        public int firstIndexAfter(Date value, boolean inclusive) {
            return firstIndexAfterMillis(value.getTime(), inclusive);
        }

        @Override
        public int lastIndexBefore(Date value, boolean inclusive) {
            return lastIndexBeforeMillis(value.getTime(), inclusive);
        }

        @Override
        public Date valueAt(int index) {
            return new Date(start + index * step);
        }

        /** Resolves an exact temporal value expressed in milliseconds. */
        private int exactMillis(long millis) {
            if (size == 0) return -1;
            if (step == 0L) {
                return millis == start ? 0 : -1;
            }
            long delta = millis - start;
            if (delta % step != 0) {
                return -1;
            }
            long idx = delta / step;
            return idx >= 0 && idx < size ? (int) idx : -1;
        }

        /** Resolves the first matching index for a lower temporal bound. */
        private int firstIndexAfterMillis(long millis, boolean inclusive) {
            if (size == 0) return 0;
            if (step == 0L) {
                if (inclusive ? millis <= start : millis < start) return 0;
                return size;
            }
            if (step > 0) {
                return findBoundaryIncreasing(millis, !inclusive);
            }
            return findBoundaryDecreasing(millis, !inclusive);
        }

        /** Resolves the exclusive upper bound index for an upper temporal bound. */
        private int lastIndexBeforeMillis(long millis, boolean inclusive) {
            if (size == 0) return 0;
            if (step == 0L) {
                if (inclusive ? millis < start : millis <= start) return 0;
                return size;
            }
            if (step > 0) {
                return findBoundaryIncreasing(millis, inclusive);
            }
            return findBoundaryDecreasing(millis, inclusive);
        }

        /** Computes a boundary index on an increasing regular temporal axis. */
        private int findBoundaryIncreasing(long millis, boolean movePastEqual) {
            double raw = (millis - start) / (double) step;
            int idx = movePastEqual ? (int) Math.floor(raw) + 1 : (int) Math.ceil(raw);
            return clamp(idx, size);
        }

        /** Computes a boundary index on a decreasing regular temporal axis. */
        private int findBoundaryDecreasing(long millis, boolean moveRightOnEqual) {
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                long v = start + mid * step;
                boolean takeRight = moveRightOnEqual ? v >= millis : v > millis;
                if (takeRight) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return lo;
        }
    }

    /** Array-backed time lookup for irregular temporal axes. */
    public record ArrayTimeAxisLookup(long[] values) implements TimeAxisLookup {
        /** Builds a temporal lookup backed by explicit axis values. */
        public ArrayTimeAxisLookup(long[] values) {
            this.values = values.clone();
        }

        @Override
        public int size() {
            return values.length;
        }

        @Override
        public int exact(Date value) {
            int idx = Arrays.binarySearch(values, value.getTime());
            return idx >= 0 ? idx : -1;
        }

        @Override
        public int firstIndexAfter(Date value, boolean inclusive) {
            return NetCDFDimensionIndexes.firstIndexAfter(values, value.getTime(), inclusive);
        }

        @Override
        public int lastIndexBefore(Date value, boolean inclusive) {
            return NetCDFDimensionIndexes.lastIndexBefore(values, value.getTime(), inclusive);
        }

        @Override
        public Date valueAt(int index) {
            return new Date(values[index]);
        }
    }

    /** Regular numeric axis lookup backed by size, start, and step. */
    public record RegularNumericAxisLookup(int size, double start, double step) implements NumericAxisLookup {
        private static final double EPS = 1E-9;

        @Override
        public int exact(double value) {
            if (size == 0) return -1;
            if (Math.abs(step) < EPS) {
                return Math.abs(value - start) <= EPS ? 0 : -1;
            }
            double raw = (value - start) / step;
            long rounded = Math.round(raw);
            if (Math.abs(raw - rounded) > EPS) {
                return -1;
            }
            return rounded >= 0 && rounded < size ? (int) rounded : -1;
        }

        @Override
        public int firstIndexAfter(double value, boolean inclusive) {
            if (size == 0) return 0;
            if (step == 0.0) {
                if (inclusive ? value <= start : value < start) return 0;
                return size;
            }
            if (step > 0) {
                return findBoundaryIncreasing(value, !inclusive);
            }
            return findBoundaryDecreasing(value, !inclusive);
        }

        @Override
        public int lastIndexBefore(double value, boolean inclusive) {
            if (size == 0) return 0;
            if (step == 0.0) {
                if (inclusive ? value < start : value <= start) return 0;
                return size;
            }
            if (step > 0) {
                return findBoundaryIncreasing(value, inclusive);
            }
            return findBoundaryDecreasing(value, inclusive);
        }

        @Override
        public double valueAt(int index) {
            return start + index * step;
        }

        /** Computes a boundary index on an increasing regular numeric axis. */
        private int findBoundaryIncreasing(double value, boolean movePastEqual) {
            double raw = (value - start) / step;
            int idx = movePastEqual ? (int) Math.floor(raw + EPS) + 1 : (int) Math.ceil(raw - EPS);
            return clamp(idx, size);
        }

        /** Computes a boundary index on a decreasing regular numeric axis. */
        private int findBoundaryDecreasing(double value, boolean moveRightOnEqual) {
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                double v = valueAt(mid);
                boolean takeRight = moveRightOnEqual ? v >= value : v > value;
                if (takeRight) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return lo;
        }
    }

    /** Array-backed numeric lookup for irregular numeric axes. */
    public record ArrayNumericAxisLookup(double[] values) implements NumericAxisLookup {
        private static final double EPS = 1E-9;

        /** Builds a numeric lookup backed by explicit axis values. */
        public ArrayNumericAxisLookup(double[] values) {
            this.values = values.clone();
        }

        @Override
        public int size() {
            return values.length;
        }

        @Override
        public int exact(double value) {
            int idx = Arrays.binarySearch(values, value);
            if (idx >= 0) return idx;
            int insertion = -idx - 1;
            if (insertion < values.length && Math.abs(values[insertion] - value) <= EPS) return insertion;
            if (insertion > 0 && Math.abs(values[insertion - 1] - value) <= EPS) return insertion - 1;
            return -1;
        }

        @Override
        public int firstIndexAfter(double value, boolean inclusive) {
            return NetCDFDimensionIndexes.firstIndexAfter(values, value, inclusive);
        }

        @Override
        public int lastIndexBefore(double value, boolean inclusive) {
            return NetCDFDimensionIndexes.lastIndexBefore(values, value, inclusive);
        }

        @Override
        public double valueAt(int index) {
            return values[index];
        }
    }

    /** Returns the first index after the target on an ordered long array. */
    public static int firstIndexAfter(long[] values, long target, boolean inclusive) {
        int lo = 0, hi = values.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (inclusive ? values[mid] < target : values[mid] <= target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /** Returns the upper bound index for values before the target on an ordered long array. */
    public static int lastIndexBefore(long[] values, long target, boolean inclusive) {
        int lo = 0, hi = values.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (inclusive ? values[mid] <= target : values[mid] < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /** Returns the first index after the target on an ordered double array. */
    public static int firstIndexAfter(double[] values, double target, boolean inclusive) {
        final double eps = 1E-9;
        int lo = 0, hi = values.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (inclusive ? values[mid] < target - eps : values[mid] <= target + eps) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /** Returns the exclusive upper bound index for values before the target on an ordered double array. */
    public static int lastIndexBefore(double[] values, double target, boolean inclusive) {
        final double eps = 1E-9;
        int lo = 0, hi = values.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (inclusive ? values[mid] <= target + eps : values[mid] < target - eps) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /** Clamps the provided index to the valid range [0, max]. */
    private static int clamp(int value, int max) {
        return Math.max(0, Math.min(max, value));
    }
}
