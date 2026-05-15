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

import static org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.ComparisonType.GT;
import static org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.ComparisonType.GTE;
import static org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.ComparisonType.LT;
import static org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.ComparisonType.LTE;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.imageio.netcdf.slice.DimensionBindingResolver;
import org.geotools.imageio.netcdf.slice.NetCDFDimensionBindingResolver;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.AdditionalDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ExactDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ListFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.RangeFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.AttributeLiteralPair;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.ComparisonPair;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.ComparisonType;

/**
 * Converts the pre-filter produced by the split phase into dimension filter objects.
 *
 * <p>It expects only predicates already recognized by {@link DimensionFilterSplitter} and maps them to time, elevation,
 * or additional dimensions so they can later be resolved against NetCDF dimension indexes. Any residual predicate is
 * kept in the post-filter and is not interpreted here.
 *
 * <p>This extractor is therefore the step that turns filter syntax into {@link DimensionFilter} instances suitable for
 * efficient slice selection.
 */
public final class PreFilterDimensionExtractor {

    private DimensionFilter time = DimensionFilter.ALL;
    private DimensionFilter elevation = DimensionFilter.ALL;
    private final List<AdditionalDimensionFilter> additional = new ArrayList<>();
    private final DimensionBindingResolver bindings;

    /** Builds an extractor for the dimensions exposed by the provided variable. */
    public PreFilterDimensionExtractor(DimensionBindingResolver bindings) {
        this.bindings = bindings;
    }

    /** Extracts recognized dimension filters from the pre-filter and preserves the post-filter. */
    public SplitFilterResult extract(Filter preFilter, Filter postFilter) {
        consume(preFilter);
        return new SplitFilterResult(
                time, elevation, List.copyOf(additional), postFilter == null ? Filter.INCLUDE : postFilter);
    }

    /** Consumes a recognized pre-filter tree and merges its constraints by dimension. */
    private void consume(Filter filter) {
        if (filter == null || filter == Filter.INCLUDE || filter instanceof IncludeFilter) {
            return;
        }

        if (filter instanceof And and) {
            for (Filter child : and.getChildren()) {
                consume(child);
            }
            return;
        }

        if (filter instanceof PropertyIsEqualTo eq) {
            AttributeLiteralPair pair = AttributeLiteralPair.extract(eq.getExpression1(), eq.getExpression2());
            if (pair != null) {
                addRecognizedFilter(pair.attributeName(), new ExactDimensionFilter(pair.literal()));
                return;
            }
        }

        if (filter instanceof PropertyIsBetween between) {
            if (between.getExpression() instanceof PropertyName prop
                    && between.getLowerBoundary() instanceof Literal lower
                    && between.getUpperBoundary() instanceof Literal upper) {
                addRecognizedFilter(
                        bindings.normalize(prop.getPropertyName()),
                        new RangeFilter(lower.getValue(), true, upper.getValue(), true));
                return;
            }
        }

        if (filter instanceof PropertyIsLessThan lt) {
            consumeComparison(lt.getExpression1(), lt.getExpression2(), LT);
            return;
        }

        if (filter instanceof PropertyIsLessThanOrEqualTo lte) {
            consumeComparison(lte.getExpression1(), lte.getExpression2(), LTE);
            return;
        }

        if (filter instanceof PropertyIsGreaterThan gt) {
            consumeComparison(gt.getExpression1(), gt.getExpression2(), GT);
            return;
        }

        if (filter instanceof PropertyIsGreaterThanOrEqualTo gte) {
            consumeComparison(gte.getExpression1(), gte.getExpression2(), GTE);
            return;
        }

        if (filter instanceof Or or) {
            consumeOr(or);
            return;
        }
        throw new IllegalArgumentException("Unexpected pre-filter structure: " + filter);
    }

    /** Converts a recognized comparison predicate into a dimension range filter. */
    private void consumeComparison(Expression left, Expression right, ComparisonType type) {
        ComparisonPair pair = ComparisonPair.extract(left, right, type);
        if (pair == null) {
            throw new IllegalArgumentException("Unexpected comparison structure in pre-filter");
        }

        DimensionFilter filter =
                switch (pair.type()) {
                    case LT -> new RangeFilter(null, true, pair.literal(), false);
                    case LTE -> new RangeFilter(null, true, pair.literal(), true);
                    case GT -> new RangeFilter(pair.literal(), false, null, true);
                    case GTE -> new RangeFilter(pair.literal(), true, null, true);
                };

        addRecognizedFilter(pair.attributeName(), filter);
    }

    /** Converts an OR of equalities on the same attribute into a list filter. */
    private void consumeOr(Or or) {
        String commonAttribute = null;
        List<Object> values = new ArrayList<>();

        for (Filter child : or.getChildren()) {
            if (!(child instanceof PropertyIsEqualTo eq)) {
                throw new IllegalArgumentException("Unexpected OR child in pre-filter: " + child);
            }

            AttributeLiteralPair pair = AttributeLiteralPair.extract(eq.getExpression1(), eq.getExpression2());
            if (pair == null) {
                throw new IllegalArgumentException("Unexpected equality structure in OR pre-filter");
            }

            if (commonAttribute == null) {
                commonAttribute = pair.attributeName();
            } else if (!commonAttribute.equals(pair.attributeName())) {
                throw new IllegalArgumentException("Mixed attributes in OR pre-filter");
            }

            values.add(pair.literal());
        }

        addRecognizedFilter(commonAttribute, new ListFilter(values));
    }

    /** Adds a recognized filter to time, elevation, or an additional dimension. */
    private void addRecognizedFilter(String attributeName, DimensionFilter filter) {
        NetCDFDimensionBindingResolver.Binding binding = bindings.resolve(attributeName);
        if (binding == null) {
            throw new IllegalArgumentException("Unknown attribute found in pre-filter: " + attributeName);
        }

        if (binding.isTime()) {
            time = time.and(filter);
            return;
        }

        if (binding.isElevation()) {
            elevation = elevation.and(filter);
            return;
        }

        mergeAdditional(binding.logicalDimension(), filter);
    }

    /** Merges a filter into the existing constraint for an additional logical dimension. */
    private void mergeAdditional(int logicalDimension, DimensionFilter filter) {
        for (int i = 0; i < additional.size(); i++) {
            AdditionalDimensionFilter current = additional.get(i);
            if (current.dimensionIndex() == logicalDimension) {
                additional.set(
                        i,
                        new AdditionalDimensionFilter(
                                logicalDimension, current.filter().and(filter)));
                return;
            }
        }
        additional.add(new AdditionalDimensionFilter(logicalDimension, filter));
    }
}
