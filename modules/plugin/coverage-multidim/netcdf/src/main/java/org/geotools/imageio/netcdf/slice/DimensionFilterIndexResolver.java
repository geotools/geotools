/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.CompositeDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ExactDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ListFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.RangeFilter;

/**
 * Resolves {@link DimensionFilter} instances into concrete axis index selections.
 *
 * <p>This class isolates the logic that turns exact, list, range, and composite dimension filters into arrays of
 * matching NetCDF axis indices.
 */
public final class DimensionFilterIndexResolver {

    /** Resolves a time filter into matching axis indices. */
    public int[] resolveTime(DimensionFilter filter, NetCDFDimensionIndexes.TimeAxisLookup axis) {
        if (axis == null || filter == null || filter == DimensionFilter.ALL) {
            return allIndices(axis);
        }
        return resolveTimeInternal(filter, axis);
    }

    /** Resolves a numeric filter into matching axis indices. */
    public int[] resolveNumeric(DimensionFilter filter, NetCDFDimensionIndexes.NumericAxisLookup axis) {
        if (axis == null || filter == null || filter == DimensionFilter.ALL) {
            return allIndices(axis);
        }
        return resolveNumericInternal(filter, axis);
    }

    /** Resolves an additional dimension filter using the provided axis lookup. */
    public int[] resolveAdditional(DimensionFilter filter, NetCDFDimensionIndexes.AxisLookup axis) {
        if (axis instanceof NetCDFDimensionIndexes.TimeAxisLookup timeAxis) {
            return resolveTime(filter, timeAxis);
        }
        if (axis instanceof NetCDFDimensionIndexes.NumericAxisLookup numericAxis) {
            return resolveNumeric(filter, numericAxis);
        }
        return allIndices(axis);
    }

    /** Internal resolution of time filters into index arrays. */
    private int[] resolveTimeInternal(DimensionFilter filter, NetCDFDimensionIndexes.TimeAxisLookup axis) {
        if (filter == DimensionFilter.ALL) {
            return allIndices(axis);
        }

        if (filter instanceof CompositeDimensionFilter composite) {
            Date exact = extractExactTime(composite);
            if (exact != null) {
                int idx = axis.exact(exact);
                return idx >= 0 ? new int[] {idx} : new int[0];
            }
            return intersect(resolveTimeInternal(composite.left(), axis), resolveTimeInternal(composite.right(), axis));
        }

        if (filter instanceof ExactDimensionFilter exact) {
            Date value = toDate(exact.expected());
            if (value == null) {
                return allIndices(axis);
            }
            int idx = axis.exact(value);
            return idx >= 0 ? new int[] {idx} : new int[0];
        }

        if (filter instanceof ListFilter list) {
            Set<Integer> indices = new HashSet<>();
            for (Object candidate : list.getValues()) {
                Date value = toDate(candidate);
                if (value == null) {
                    continue;
                }
                int idx = axis.exact(value);
                if (idx >= 0) {
                    indices.add(idx);
                }
            }
            return sorted(indices);
        }

        if (filter instanceof RangeFilter range) {
            Date lower = toDate(range.getLower());
            Date upper = toDate(range.getUpper());
            int start = lower == null ? 0 : axis.firstIndexAfter(lower, range.isLowerInclusive());
            int end = upper == null ? axis.size() : axis.lastIndexBefore(upper, range.isUpperInclusive());
            return NetCDFDimensionIndexes.contiguousRange(start, end);
        }

        return allIndices(axis);
    }

    /** Internal resolution of numeric filters into index arrays. */
    private int[] resolveNumericInternal(DimensionFilter filter, NetCDFDimensionIndexes.NumericAxisLookup axis) {
        if (filter == DimensionFilter.ALL) {
            return allIndices(axis);
        }

        if (filter instanceof CompositeDimensionFilter composite) {
            Double exact = extractExactNumeric(composite);
            if (exact != null) {
                int idx = axis.exact(exact);
                return idx >= 0 ? new int[] {idx} : new int[0];
            }
            return intersect(
                    resolveNumericInternal(composite.left(), axis), resolveNumericInternal(composite.right(), axis));
        }

        if (filter instanceof ExactDimensionFilter exact) {
            Double value = toDouble(exact.expected());
            if (value == null) {
                return allIndices(axis);
            }
            int idx = axis.exact(value);
            return idx >= 0 ? new int[] {idx} : new int[0];
        }

        if (filter instanceof ListFilter list) {
            Set<Integer> indices = new HashSet<>();
            for (Object candidate : list.getValues()) {
                Double value = toDouble(candidate);
                if (value == null) {
                    continue;
                }
                int idx = axis.exact(value);
                if (idx >= 0) {
                    indices.add(idx);
                }
            }
            return sorted(indices);
        }

        if (filter instanceof RangeFilter range) {
            Double lower = toDouble(range.getLower());
            Double upper = toDouble(range.getUpper());
            int start = lower == null ? 0 : axis.firstIndexAfter(lower, range.isLowerInclusive());
            int end = upper == null ? axis.size() : axis.lastIndexBefore(upper, range.isUpperInclusive());
            return NetCDFDimensionIndexes.contiguousRange(start, end);
        }

        return allIndices(axis);
    }

    /** Returns all valid indices for the provided axis. */
    private static int[] allIndices(NetCDFDimensionIndexes.AxisLookup axis) {
        if (axis == null) {
            return new int[0];
        }
        return NetCDFDimensionIndexes.contiguousRange(0, axis.size());
    }

    /** Detects if a composite filter represents an exact time value. */
    private static Date extractExactTime(CompositeDimensionFilter composite) {
        RangeFilter left = asRange(composite.left());
        RangeFilter right = asRange(composite.right());
        if (left == null || right == null) {
            return null;
        }

        Date exact = extractExactTime(left, right);
        if (exact != null) {
            return exact;
        }
        return extractExactTime(right, left);
    }

    /** Extracts an exact time if upper and lower bounds collapse to the same instant. */
    private static Date extractExactTime(RangeFilter upperBoundFilter, RangeFilter lowerBoundFilter) {
        if (upperBoundFilter.getLower() != null || lowerBoundFilter.getUpper() != null) {
            return null;
        }

        Date upper = toDate(upperBoundFilter.getUpper());
        Date lower = toDate(lowerBoundFilter.getLower());
        if (upper == null || lower == null) {
            return null;
        }

        if (upper.getTime() != lower.getTime()) {
            return null;
        }

        if (!upperBoundFilter.isUpperInclusive() || !lowerBoundFilter.isLowerInclusive()) {
            return null;
        }

        return upper;
    }

    /** Detects if a composite filter represents an exact numeric value. */
    private static Double extractExactNumeric(CompositeDimensionFilter composite) {
        RangeFilter left = asRange(composite.left());
        RangeFilter right = asRange(composite.right());
        if (left == null || right == null) {
            return null;
        }

        Double exact = extractExactNumeric(left, right);
        if (exact != null) {
            return exact;
        }
        return extractExactNumeric(right, left);
    }

    /** Extracts an exact numeric value if bounds collapse to the same value. */
    private static Double extractExactNumeric(RangeFilter upperBoundFilter, RangeFilter lowerBoundFilter) {
        if (upperBoundFilter.getLower() != null || lowerBoundFilter.getUpper() != null) {
            return null;
        }

        Double upper = toDouble(upperBoundFilter.getUpper());
        Double lower = toDouble(lowerBoundFilter.getLower());
        if (upper == null || lower == null) {
            return null;
        }

        if (Double.compare(upper, lower) != 0) {
            return null;
        }

        if (!upperBoundFilter.isUpperInclusive() || !lowerBoundFilter.isLowerInclusive()) {
            return null;
        }

        return upper;
    }

    /** Casts a dimension filter to a range filter if possible. */
    private static RangeFilter asRange(DimensionFilter filter) {
        return filter instanceof RangeFilter range ? range : null;
    }

    /** Computes the intersection of two sorted index arrays. */
    private static int[] intersect(int[] left, int[] right) {
        int i = 0, j = 0;
        int[] tmp = new int[Math.min(left.length, right.length)];
        int count = 0;
        while (i < left.length && j < right.length) {
            if (left[i] == right[j]) {
                tmp[count++] = left[i];
                i++;
                j++;
            } else if (left[i] < right[j]) {
                i++;
            } else {
                j++;
            }
        }
        int[] out = new int[count];
        System.arraycopy(tmp, 0, out, 0, count);
        return out;
    }

    /** Converts a set of indices into a sorted array. */
    private static int[] sorted(Set<Integer> values) {
        return values.stream().sorted().mapToInt(Integer::intValue).toArray();
    }

    /** Converts an object to a Date if possible. */
    private static Date toDate(Object value) {
        if (value instanceof Date d) {
            return d;
        }
        if (value instanceof Number n) {
            return new Date(n.longValue());
        }
        return null;
    }

    /** Converts an object to a Double if possible. */
    private static Double toDouble(Object value) {
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        return null;
    }
}
