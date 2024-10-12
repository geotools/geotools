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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
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
import org.geotools.api.filter.expression.ExpressionVisitor;
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
import org.geotools.filter.function.InFunction;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.util.Converters;
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
        return memoize(filter, extraData, this::inFilterOptimizer);
    }

    /** Checks if an Or filter can be replaced by a single IN condition. */
    private Object inFilterOptimizer(Or filter, Object extraData) {
        InFunction inFilter = replaceWithInFilter(filter);
        if (inFilter == null) return super.visit(filter, extraData);
        return ff.equals(inFilter, ff.literal(true));
    }

    /**
     * Check if it's possible generate an IN function from the provided OR filter. If the OR filter
     * doesn't accomplish the conditions, returns null.
     */
    private InFunction replaceWithInFilter(Or filter) {
        List<Filter> children = filter.getChildren();
        Expression expression = null;
        List<Literal> literals = new ArrayList<>(children.size());
        for (Filter childFilter : children) {
            if (!(childFilter instanceof PropertyIsEqualTo)) return null;
            PropertyIsEqualTo eqto = (PropertyIsEqualTo) childFilter;
            Pair<Expression, Literal> equalsParameters = getEqualsParameters(eqto);
            if (equalsParameters == null) return null;
            if (expression == null) {
                expression = equalsParameters.getLeft();
            } else {
                if (!Objects.equals(expression, equalsParameters.getLeft())) return null;
            }
            literals.add(equalsParameters.getRight());
        }
        // create the IN function instance
        List<Expression> inParameters = new ArrayList<>(literals.size() + 1);
        inParameters.add(expression);
        inParameters.addAll(literals);
        InFunction inFunction = new InFunction();
        inFunction.setParameters(inParameters);
        return inFunction;
    }

    private Pair<Expression, Literal> getEqualsParameters(PropertyIsEqualTo equals) {
        Literal literal = null;
        Expression expression = null;
        if (equals.getExpression1() instanceof Literal) {
            literal = (Literal) equals.getExpression1();
            expression = equals.getExpression2();
        } else if (equals.getExpression2() instanceof Literal) {
            literal = (Literal) equals.getExpression2();
            expression = equals.getExpression1();
        }

        if (expression != null && literal != null) return Pair.of(expression, literal);
        return null;
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

    public <T extends Filter> T memoize(T filter, Object extraData, BiFunction<T, Object, Object> duplicator) {
        // do we want to memoize this filter?
        if (!memoizeCandidates.contains(filter)) {
            // drill down, this clones and at the same time visits sub-filters
            // (the small parts inside a complex filter are the ones likely to repeat)
            @SuppressWarnings("unchecked")
            T result = (T) duplicator.apply(filter, extraData);
            return result;
        }
        // see if we already built a memoized replacement for it
        @SuppressWarnings("unchecked")
        T replacement = (T) filterReplacements.get(filter);
        if (replacement == null) {
            @SuppressWarnings("unchecked")
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
            if (simpleFeatureType != null && simpleFeatureType.indexOf(expression.getPropertyName()) >= 0) {
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

        private final AttributeDescriptor descriptor;
        private SimpleFeatureType schema;
        PropertyName delegate;
        int index;

        public IndexPropertyName(SimpleFeatureType schema, PropertyName delegate) {
            this.delegate = delegate;
            this.schema = schema;
            this.index = schema.indexOf(delegate.getPropertyName());
            this.descriptor = schema.getDescriptor(index);
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
        public Object evaluate(Object object) {
            return this.evaluate(object, null);
        }

        @Override
        public <T> T evaluate(Object object, Class<T> context) {
            if (object instanceof SimpleFeature) {
                SimpleFeature sf = (SimpleFeature) object;
                SimpleFeatureType other = sf.getFeatureType();
                if (other == schema || other.getDescriptor(index).equals(descriptor)) {
                    // ContentFeatureSource/ContentFeatureCollection return a SimpleFeatureType
                    // instance that is equal, but not same, to the one associated to the features
                    // they return. For this optimization to work we need a fast check, so instance
                    // equality, so we'll switch the object reference if the two schemas are equal,
                    // but not same. In fact, within the limits of a PropertyName implementation,
                    // we can do this if the two return the same property at the same index,
                    // which makes for a even faster comparison when the reference equality fails.
                    if (other != schema) {
                        this.schema = other;
                    }

                    try {
                        Object value = sf.getAttribute(index);
                        if (context == null || context.isInstance(value)) {
                            @SuppressWarnings("unchecked")
                            T result = (T) value;
                            return result;
                        } else {
                            return Converters.convert(value, context);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new RuntimeException(
                                "Could not locate attribute at index " + index + " on feature " + object, e);
                    }
                }
            }

            return delegate.evaluate(object, context);
        }

        @Override
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return delegate.accept(visitor, extraData);
        }

        @Override
        public String toString() {
            return delegate.toString();
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
        public Object evaluate(Object object) {
            if (object != lastFeature || lastContext != null) {
                lastResult = delegate.evaluate(object);
                lastFeature = object;
                lastContext = null;
            }
            return lastResult;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T evaluate(Object object, Class<T> context) {
            if (object != lastFeature || !Objects.equals(lastContext, context)) {
                lastResult = delegate.evaluate(object, context);
                lastContext = context;
            }
            return (T) lastResult;
        }

        @Override
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return delegate.accept(visitor, extraData);
        }
    }
}
