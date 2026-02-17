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

import org.geotools.api.filter.And;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.imageio.netcdf.slice.DimensionBindingResolver;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter.AttributeLiteralPair;

/**
 * Identifies which predicates can be evaluated as NetCDF dimension constraints. Supported predicates on known dimension
 * attributes are routed to the pre-filter, while all other predicates are left in the post-filter for later evaluation.
 */
final class FilterSplittingVisitor extends PostPreProcessFilterSplittingVisitor {

    private final DimensionBindingResolver bindings;

    private static final FilterCapabilities FILTER_CAPABILITIES;

    static {
        FILTER_CAPABILITIES = new FilterCapabilities();
        FILTER_CAPABILITIES.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);

        FILTER_CAPABILITIES.addType(IncludeFilter.class);
        FILTER_CAPABILITIES.addType(ExcludeFilter.class);

        FILTER_CAPABILITIES.addType(PropertyIsNull.class);
        FILTER_CAPABILITIES.addType(PropertyIsBetween.class);

        FILTER_CAPABILITIES.addType(And.class);
        FILTER_CAPABILITIES.addType(Or.class);
    }

    /** Creates a visitor bound to the known dimensions of a single variable. */
    FilterSplittingVisitor(DimensionBindingResolver bindings) {
        super(FILTER_CAPABILITIES, null, null);
        this.bindings = bindings;
    }

    // -------------------------------------------------------------------------------------
    // For all the following overrides, accept predicates only on known dimension attributes
    // -------------------------------------------------------------------------------------

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return isSupportedComparison(filter.getExpression1(), filter.getExpression2())
                ? super.visit(filter, extraData)
                : postProcess(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        boolean supported = filter.getExpression() instanceof PropertyName property
                && filter.getLowerBoundary() instanceof Literal
                && filter.getUpperBoundary() instanceof Literal
                && isKnownDimension(property);

        return supported ? super.visit(filter, extraData) : postProcess(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return isSupportedComparison(filter.getExpression1(), filter.getExpression2())
                ? super.visit(filter, extraData)
                : postProcess(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return isSupportedComparison(filter.getExpression1(), filter.getExpression2())
                ? super.visit(filter, extraData)
                : postProcess(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return isSupportedComparison(filter.getExpression1(), filter.getExpression2())
                ? super.visit(filter, extraData)
                : postProcess(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return isSupportedComparison(filter.getExpression1(), filter.getExpression2())
                ? super.visit(filter, extraData)
                : postProcess(filter, extraData);
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        // Only allow OR if it is a same-attribute equality disjunction,
        // because that is the only OR shape the extractor will optimize.
        return isSupportedOr(filter) ? this.preStack.push(filter) : postProcess(filter, extraData);
    }

    /** Returns true when the predicate is a property/literal comparison on a known dimension. */
    private boolean isSupportedComparison(Expression left, Expression right) {
        if (left instanceof PropertyName property && right instanceof Literal) {
            return isKnownDimension(property);
        }
        if (right instanceof PropertyName property && left instanceof Literal) {
            return isKnownDimension(property);
        }
        return false;
    }

    /** Returns true when the property matches time, elevation, or an additional dimension. */
    private boolean isKnownDimension(PropertyName property) {
        return property != null && bindings.isKnownAttribute(property.getPropertyName());
    }

    /** Returns true when every OR branch is an equality on the same known attribute. */
    private boolean isSupportedOr(Or or) {
        String commonAttribute = null;

        for (Filter child : or.getChildren()) {
            if (!(child instanceof PropertyIsEqualTo eq)) {
                return false;
            }

            AttributeLiteralPair pair = AttributeLiteralPair.extract(eq.getExpression1(), eq.getExpression2());
            if (pair == null) {
                return false;
            }

            if (bindings.resolve(pair.attributeName()) == null) {
                return false;
            }

            if (commonAttribute == null) {
                commonAttribute = pair.attributeName();
            } else if (!commonAttribute.equals(pair.attributeName())) {
                return false;
            }
        }

        return commonAttribute != null;
    }

    /** Marks a predicate as post-process only. */
    private Object postProcess(Filter filter, Object extraData) {
        if (original == null) {
            original = filter;
        }
        postStack.push(filter);
        return null;
    }
}
