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

import java.util.List;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.imageio.netcdf.slice.DimensionBindingResolver;

/**
 * Splits a GeoTools filter into dimension-backed predicates and a residual post-filter. Recognized predicates on time,
 * elevation, and additional dimensions are converted into {@link DimensionFilter} instances. All remaining predicates
 * are preserved for in-memory evaluation during slice iteration.
 *
 * <p>The splitting process relies on a SplittingVisitor implementation that traverses the filter tree and identifies
 * supported predicates. The resulting pre- and post-filters are collected into a SplitFilterResult which is then
 * processed by a PreFilterDimensionExtractor to produce the final dimension filters and post-filter.
 */
public record DimensionFilterSplitter(DimensionBindingResolver bindings) {

    /** Splits the filter into recognized dimension filters and a residual post-filter. */
    public SplitFilterResult split(Filter filter) {
        if (filter == null || filter == Filter.INCLUDE || filter instanceof IncludeFilter) {
            return new SplitFilterResult(DimensionFilter.ALL, DimensionFilter.ALL, List.of(), Filter.INCLUDE);
        }

        if (filter == Filter.EXCLUDE || filter instanceof ExcludeFilter) {
            return new SplitFilterResult(DimensionFilter.ALL, DimensionFilter.ALL, List.of(), Filter.EXCLUDE);
        }

        FilterSplittingVisitor splitter = new FilterSplittingVisitor(bindings);
        filter.accept(splitter, null);

        Filter pre = safeFilter(splitter.getFilterPre());
        Filter post = safeFilter(splitter.getFilterPost());

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(bindings);
        return extractor.extract(pre, post);
    }

    /** Replaces a null filter with {@link Filter#INCLUDE}. */
    private static Filter safeFilter(Filter filter) {
        return filter == null ? Filter.INCLUDE : filter;
    }

    /** Holds a property/literal pair extracted from a binary comparison. */
    public record AttributeLiteralPair(String attributeName, Object literal) {

        /** Extracts a property/literal pair regardless of operand order. */
        static AttributeLiteralPair extract(Expression left, Expression right) {
            if (left instanceof PropertyName prop && right instanceof Literal lit) {
                return new AttributeLiteralPair(
                        DimensionBindingResolver.normalizeAttribute(prop.getPropertyName()), lit.getValue());
            }
            if (right instanceof PropertyName prop && left instanceof Literal lit) {
                return new AttributeLiteralPair(
                        DimensionBindingResolver.normalizeAttribute(prop.getPropertyName()), lit.getValue());
            }
            return null;
        }
    }

    /** Holds a comparison pair with the normalized comparison direction. */
    public record ComparisonPair(String attributeName, Object literal, ComparisonType type) {

        /** Extracts a comparison pair and inverts the operator when operands are swapped. */
        static ComparisonPair extract(Expression left, Expression right, ComparisonType type) {
            if (left instanceof PropertyName prop && right instanceof Literal lit) {
                return new ComparisonPair(
                        DimensionBindingResolver.normalizeAttribute(prop.getPropertyName()), lit.getValue(), type);
            }
            if (right instanceof PropertyName prop && left instanceof Literal lit) {
                return new ComparisonPair(
                        DimensionBindingResolver.normalizeAttribute(prop.getPropertyName()),
                        lit.getValue(),
                        type.invert());
            }
            return null;
        }
    }

    /** Comparison operators used while translating binary predicates into ranges. */
    public enum ComparisonType {
        LT,
        LTE,
        GT,
        GTE;

        /** Returns the equivalent operator for swapped operands. */
        ComparisonType invert() {
            return switch (this) {
                case LT -> GT;
                case LTE -> GTE;
                case GT -> LT;
                case GTE -> LTE;
            };
        }
    }
}
