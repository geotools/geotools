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
package org.geotools.renderer.lite;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.annotation.Extension;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Optimizes filter trees by replacing expressions/filters with memoized equivalents, also replaces
 * the generic property access machinery with straight index access when the target feature type is
 * a {@link SimpleFeatureType}.
 */
class MemoryFilterOptimizer extends DuplicatingFilterVisitor {

    static final Object NULL_PLACEHOLDER = new Object();
    private final Set<Object> memoizeCandidates;

    Map<Expression, Expression> expressionReplacements = new HashMap<>();
    Map<Filter, Filter> filterReplacements = new HashMap<>();
    SimpleFeatureType simpleFeatureType;

    /**
     * Prepares to duplicate a filter for a given target schema. Only filters included in the
     * memoizeCandidates set will be wrapped within a caching proxy
     */
    public MemoryFilterOptimizer(FeatureType schema, Set<Object> memoizeCandidates) {
        if (schema instanceof SimpleFeatureType) {
            this.simpleFeatureType = (SimpleFeatureType) schema;
        }
        this.memoizeCandidates = memoizeCandidates;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return memoize(filter, extraData, super::visit);
    }

    public <T extends Filter> T memoize(
            T filter, Object extraData, BiFunction<T, Object, Object> duplicator) {
        // do we want to memoize this filter?
        if (!memoizeCandidates.contains(filter)) {
            // drill down, this clones and at the same time visits sub-filters
            // (the small parts inside a complex filter are the ones likely to repeat)
            return (T) duplicator.apply(filter, extraData);
        }
        // see if we already built a memoized replacement for it
        T replacement = (T) filterReplacements.get(filter);
        if (replacement == null) {
            T duplicated = (T) duplicator.apply(filter, extraData);
            replacement = FilterMemoizer.memoize(duplicated);
            filterReplacements.put(filter, replacement);
        }

        return replacement;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        Expression replacement = expressionReplacements.get(expression);
        if (replacement == null) {
            if (simpleFeatureType != null
                    && simpleFeatureType.indexOf(expression.getPropertyName()) >= 0) {
                // index access is significantly faster, does not need memoization
                replacement = new IndexPropertyName(simpleFeatureType, expression);
            } else if (memoizeCandidates.contains(expression)) {
                // other accesses can use caching instead
                replacement = new MemoizedPropertyName(expression);
            } else {
                replacement = expression;
            }

            expressionReplacements.put(expression, replacement);
        }

        return replacement;
    }

    /** Executes a straigth index access instead of a lookup by name when possible */
    static class IndexPropertyName implements PropertyName {

        private final SimpleFeatureType schema;
        PropertyName delegate;
        int index;

        public IndexPropertyName(SimpleFeatureType schema, PropertyName delegate) {
            this.delegate = delegate;
            this.schema = schema;
            this.index = schema.indexOf(delegate.getPropertyName());
        }

        @Override
        public String getPropertyName() {
            return delegate.getPropertyName();
        }

        @Override
        public NamespaceSupport getNamespaceContext() {
            return delegate.getNamespaceContext();
        }

        @Override
        @Extension
        public Object evaluate(Object object) {
            if (object instanceof SimpleFeature
                    && ((SimpleFeature) object).getFeatureType() == schema) {
                SimpleFeature sf = (SimpleFeature) object;
                try {
                    return sf.getAttribute(index);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException(
                            "Could not locate attribute at index "
                                    + index
                                    + " on feature "
                                    + object,
                            e);
                }
            }
            return delegate.evaluate(object);
        }

        @Override
        @Extension
        public <T> T evaluate(Object object, Class<T> context) {
            if (object instanceof SimpleFeature
                    && ((SimpleFeature) object).getFeatureType() == schema) {
                SimpleFeature sf = (SimpleFeature) object;
                try {
                    Object value = sf.getAttribute(index);
                    if (context == null || context.isInstance(value)) {
                        return (T) value;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException(
                            "Could not locate attribute at index "
                                    + index
                                    + " on feature "
                                    + object,
                            e);
                }
            }
            return delegate.evaluate(object, context);
        }

        @Override
        @Extension
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return delegate.accept(visitor, extraData);
        }
    }

    /**
     * Caches the result of the last property retrieved from the "feature", assuming the feature, as
     * well as the target type, are the same as in the last invocation
     */
    static class MemoizedPropertyName implements PropertyName {
        PropertyName delegate;
        Object lastFeature = NULL_PLACEHOLDER;
        Object lastResult;
        Class lastContext;

        public MemoizedPropertyName(PropertyName delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getPropertyName() {
            return delegate.getPropertyName();
        }

        @Override
        public NamespaceSupport getNamespaceContext() {
            return delegate.getNamespaceContext();
        }

        @Override
        @Extension
        public Object evaluate(Object object) {
            if (object != lastFeature || lastContext != null) {
                lastResult = delegate.evaluate(object);
                lastFeature = object;
                lastContext = null;
            }
            return lastResult;
        }

        @Override
        @Extension
        public <T> T evaluate(Object object, Class<T> context) {
            if (object != lastFeature || !Objects.equals(lastContext, context)) {
                lastResult = delegate.evaluate(object, context);
                lastContext = context;
            }
            return (T) lastResult;
        }

        @Override
        @Extension
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return delegate.accept(visitor, extraData);
        }
    }
}
