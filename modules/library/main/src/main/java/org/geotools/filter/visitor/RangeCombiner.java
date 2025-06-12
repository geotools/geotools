/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.Range;

/**
 * Utility class used by {@link SimplifyingFilterVisitor} to combine range based filters. This class works correctly
 * only if all the range based filters have the same MatchAction, but does not check it internally, so it is suitable
 * for usage only in a simple feature context
 *
 * @author Andrea Aime - GeoSolutions
 */
abstract class RangeCombiner {

    /**
     * Combines ranges by unioning them when possible
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class Or extends RangeCombiner {

        public Or(FilterFactory ff, FeatureType featureType, List<Filter> filters) {
            super(ff, featureType, filters);
        }

        @Override
        protected <T extends Comparable<T>> MultiRange<T> combineRanges(MultiRange<T> r1, MultiRange<T> r2) {
            return r1.merge(r2);
        }

        @Override
        protected void addFiltersToResults(List<Filter> results, Filter filter) {
            if (filter instanceof org.geotools.api.filter.Or) {
                results.addAll(((org.geotools.api.filter.Or) filter).getChildren());
            } else {
                results.add(filter);
            }
        }
    }

    /**
     * Combines ranges by intersecting them
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class And extends RangeCombiner {

        public And(FilterFactory ff, FeatureType featureType, List<Filter> filters) {
            super(ff, featureType, filters);
        }

        @Override
        protected <T extends Comparable<T>> MultiRange<T> combineRanges(MultiRange<T> r1, MultiRange<T> r2) {
            return r1.intersect(r2);
        }

        @Override
        protected void addFiltersToResults(List<Filter> results, Filter filter) {
            if (filter instanceof org.geotools.api.filter.And) {
                results.addAll(((org.geotools.api.filter.And) filter).getChildren());
            } else {
                results.add(filter);
            }
        }
    }

    /**
     * A tuple made of a filter and its associated range
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class FilterRange {
        Filter filter;

        Range range;

        public FilterRange(Filter filter, Range range) {
            this.filter = filter;
            this.range = range;
        }
    }

    /**
     * A tuple made of an expression and its associated range
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class ExpressionRange {
        Expression expression;

        Range range;

        public ExpressionRange(Expression expression, Range range) {
            this.expression = expression;
            this.range = range;
        }
    }

    static class CombinationResult {
        Map<Expression, List<FilterRange>> rangeMap;

        boolean combinationHappened;

        public CombinationResult(Map<Expression, List<FilterRange>> rangeMap, boolean combinationHappened) {
            this.rangeMap = rangeMap;
            this.combinationHappened = combinationHappened;
        }
    }

    ExpressionTypeVisitor expressionTypeVisitor;

    Map<Expression, MultiRange> rangeMap = new HashMap<>();

    FeatureType featureType;

    List<Filter> otherFilters = new ArrayList<>();

    List<Filter> filters;

    FilterFactory ff;

    @SuppressWarnings("unchecked")
    // this class combines ranges of comparables, but without really knowing the type of comparable
    // hence it cannot avoid unchecked assignments
    public RangeCombiner(FilterFactory ff, FeatureType featureType, List<Filter> filters) {
        this.ff = ff;
        this.filters = filters;
        this.featureType = featureType;
        this.expressionTypeVisitor = new ExpressionTypeVisitor(featureType);
        // now organize by comparison filters to apply range based simplification
        for (Filter f : filters) {
            if (f instanceof PropertyIsBetween) {
                PropertyIsBetween pb = (PropertyIsBetween) f;
                Class binding = getTypeIfComparable(pb.getExpression());
                if (binding == null) {
                    otherFilters.add(pb);
                } else {
                    Object min = evaluate(pb.getLowerBoundary(), binding);
                    Object max = evaluate(pb.getUpperBoundary(), binding);
                    if (min == null || max == null) {
                        otherFilters.add(f);
                    } else {
                        Expression expression = pb.getExpression();
                        @SuppressWarnings("unchecked")
                        Range<?> range = new Range(binding, (Comparable) min, (Comparable) max);
                        addRange(rangeMap, expression, new MultiRange<>(range));
                    }
                }
            } else if (f instanceof PropertyIsNotEqualTo) {
                PropertyIsNotEqualTo ne = (PropertyIsNotEqualTo) f;
                Comparable exclusion = null;
                Expression expression = null;
                Class<Comparable<?>> binding = getTypeIfComparable(ne.getExpression1());
                if (binding != null) {
                    expression = ne.getExpression1();
                    if (binding != null) {
                        exclusion = evaluate(ne.getExpression2(), binding);
                    }
                } else {
                    expression = ne.getExpression2();
                    binding = getTypeIfComparable(ne.getExpression2());
                    if (binding != null) {
                        exclusion = evaluate(ne.getExpression1(), binding);
                    }
                }
                if (exclusion != null) {
                    addRange(rangeMap, expression, new MultiRange(binding, exclusion));
                } else {
                    otherFilters.add(f);
                }
            } else if (f instanceof BinaryComparisonOperator) {
                BinaryComparisonOperator op = (BinaryComparisonOperator) f;
                ExpressionRange er = getRange(op);

                // Right now, only PropertyIsEqualTo actually considers matchcase, all others
                // behave as if they were case sensitive regardgless of the setting.
                // TODO: change the logic to consider matchcase when
                if (er.range != null && (!(op instanceof PropertyIsEqualTo) || op.isMatchingCase())) {
                    addRange(rangeMap, er.expression, new MultiRange<>(er.range));
                } else {
                    otherFilters.add(f);
                }
            } else if (f instanceof org.geotools.api.filter.And || f instanceof org.geotools.api.filter.Or) {
                BinaryLogicOperator logic = (BinaryLogicOperator) f;
                List<Filter> children = logic.getChildren();
                RangeCombiner subCombiner;
                if (logic instanceof org.geotools.api.filter.And) {
                    subCombiner = new RangeCombiner.And(ff, featureType, children);
                } else {
                    subCombiner = new RangeCombiner.Or(ff, featureType, children);
                }

                // see if the sub-filter can be assimilated to a single range
                if (!subCombiner.otherFilters.isEmpty()
                        || (subCombiner.rangeMap.size() > 1
                                && !subCombiner.getClass().equals(this.getClass()))) {
                    otherFilters.add(f);
                } else {
                    Map<Expression, MultiRange> combined = subCombiner.rangeMap;
                    for (Map.Entry<Expression, MultiRange> entry : combined.entrySet()) {
                        Expression ex = entry.getKey();
                        MultiRange ranges = entry.getValue();
                        addRange(rangeMap, ex, ranges);
                    }
                }
            } else {
                // negation of ranges is not handled by choice, as the simplifying filter visitor
                // has already
                // switched negations out for us
                otherFilters.add(f);
            }
        }
    }

    // we don't know what type of range is built here, any comparable will do
    @SuppressWarnings("unchecked")
    private ExpressionRange getRange(BinaryComparisonOperator op) {
        Range range = null;
        Expression expression = null;
        if (!isStatic(op.getExpression1())) {
            expression = op.getExpression1();
            Class binding = getTypeIfComparable(expression);
            if (binding != null) {
                Object value = evaluate(op.getExpression2(), binding);
                if (value != null) {
                    if (op instanceof PropertyIsLessThan) {
                        range = new Range<>(binding, null, false, (Comparable) value, false);
                    } else if (op instanceof PropertyIsLessThanOrEqualTo) {
                        range = new Range<>(binding, null, false, (Comparable) value, true);
                    } else if (op instanceof PropertyIsEqualTo) {
                        range = new Range<>(binding, (Comparable) value, (Comparable) value);
                    } else if (op instanceof PropertyIsGreaterThanOrEqualTo) {
                        range = new Range<>(binding, (Comparable) value, true, null, false);
                    } else if (op instanceof PropertyIsGreaterThan) {
                        range = new Range<>(binding, (Comparable) value, false, null, false);
                    }
                }
            }
        } else if (!isStatic(op.getExpression2())) {
            expression = op.getExpression2();
            Class binding = getTypeIfComparable(expression);
            if (binding != null) {
                Object value = evaluate(op.getExpression1(), binding);
                if (value != null) {
                    if (op instanceof PropertyIsLessThan) {
                        range = new Range<>(binding, (Comparable) value, true, null, false);
                    } else if (op instanceof PropertyIsLessThanOrEqualTo) {
                        range = new Range<>(binding, (Comparable) value, false, null, false);
                    } else if (op instanceof PropertyIsEqualTo) {
                        range = new Range<>(binding, (Comparable) value, (Comparable) value);
                    } else if (op instanceof PropertyIsGreaterThanOrEqualTo) {
                        range = new Range<>(binding, null, false, (Comparable) value, false);
                    } else if (op instanceof PropertyIsGreaterThan) {
                        range = new Range<>(binding, null, false, (Comparable) value, true);
                    }
                }
            }
        }

        return new ExpressionRange(expression, range);
    }

    private boolean isStatic(Expression exp) {
        FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();
        exp.accept(attributeExtractor, null);
        return attributeExtractor.getAttributeNameSet().isEmpty();
    }

    private Class getTypeIfComparable(Expression ex) {
        Class type = (Class) ex.accept(expressionTypeVisitor, null);
        if (Comparable.class.isAssignableFrom(type)) {
            return type;
        } else {
            return null;
        }
    }

    String getPropertyName(Expression ex) {
        if (ex instanceof PropertyName) {
            PropertyName pn = (PropertyName) ex;
            return pn.getPropertyName();
        }

        return null;
    }

    public List<Filter> getReducedFilters() {
        if (rangeMap.isEmpty()) {
            return filters;
        }

        List<Filter> result = new ArrayList<>(otherFilters);
        for (Expression ex : new ArrayList<>(rangeMap.keySet())) {
            MultiRange multiRange = rangeMap.get(ex);
            addFiltersToResults(result, multiRange.toFilter(ff, ex));
        }

        return result;
    }

    protected abstract void addFiltersToResults(List<Filter> result, Filter filter);

    /** Combines two multiranges */
    protected abstract <T extends Comparable<T>> MultiRange<T> combineRanges(MultiRange<T> r1, MultiRange<T> r2);

    @SuppressWarnings("unchecked")
    private void addRange(Map<Expression, MultiRange> rangeMap, Expression expression, MultiRange other) {
        MultiRange ranges = rangeMap.get(expression);
        if (ranges == null) {
            rangeMap.put(expression, other);
        } else {
            MultiRange combined = combineRanges(ranges, other);
            rangeMap.put(expression, combined);
        }
    }

    private <T> T evaluate(Expression ex, Class<T> target) {
        return ex instanceof Literal ? ex.evaluate(null, target) : null;
    }
}
