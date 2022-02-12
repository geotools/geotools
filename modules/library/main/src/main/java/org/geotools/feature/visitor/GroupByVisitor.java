/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;
import org.opengis.util.ProgressListener;

/** Group features by one or several attributes and applies an aggregator visitor to each group. */
public class GroupByVisitor implements FeatureCalc, FeatureAttributeVisitor {

    private final Aggregate aggregate;
    private final Expression expression;
    private final FeatureCalc visitorProtoType;
    private final List<Expression> groupByAttributes;

    private final InMemoryGroupBy inMemoryGroupBy = new InMemoryGroupBy();

    private CalcResult optimizationResult = CalcResult.NULL_RESULT;

    public GroupByVisitor(
            Aggregate aggregateVisitor,
            Expression expression,
            List<Expression> groupByAttributes,
            ProgressListener progressListener) {
        this.aggregate = aggregateVisitor;
        this.expression = expression;
        this.groupByAttributes = groupByAttributes;
        visitorProtoType = aggregateVisitor.create(expression);
    }

    public boolean wasOptimized() {
        return optimizationResult != null && optimizationResult != CalcResult.NULL_RESULT;
    }

    public boolean wasVisited() {
        return !inMemoryGroupBy.groupByIndexes.isEmpty();
    }

    /**
     * This method computes and returns the group by visitor result. If the computation was
     * optimized the optimization result is returned otherwise the result is computed in memory. If
     * for some reason an optimization result exists and there are visited features, an in memory
     * computation is performed and is merged with the existing optimization results.
     *
     * @return group by visitor result
     */
    @Override
    public CalcResult getResult() {
        // do a in memory computation for any visited feature
        Map<List<Object>, CalcResult> results = inMemoryGroupBy.visit();
        // create the result, if no feature was visited this will be an empty result that can be
        // safely merged
        GroupByResult result = new GroupByResult(results, aggregate, groupByAttributes);
        if (optimizationResult == CalcResult.NULL_RESULT) {
            // there is no optimization result so we just return the created one
            return result;
        }
        // an optimization result exists, we merge both
        return optimizationResult.merge(result);
    }

    @Override
    public void visit(Feature feature) {
        inMemoryGroupBy.index((SimpleFeature) feature);
    }

    public Expression getExpression() {
        return expression;
    }

    public FeatureVisitor getAggregateVisitor() {
        return visitorProtoType;
    }

    public List<Expression> getGroupByAttributes() {
        return groupByAttributes;
    }

    /**
     * Methods that allow optimizations to directly set the group by visitor result instead of
     * computing it visiting all the features. Aggregate visitor results are wrapped with the
     * appropriate feature calculation type.
     *
     * @param value the group by visitor result
     */
    public void setValue(List<GroupByRawResult> value) {
        Map<List<Object>, CalcResult> results = new HashMap<>();
        for (GroupByRawResult groupByRawResult : value) {
            // wrap the aggregate visitor result with the appropriate feature calculation type
            results.put(
                    groupByRawResult.groupByValues,
                    aggregate.wrap(expression, groupByRawResult.visitorValue));
        }
        // create a new group by result using the raw values returned by the optimization
        GroupByResult newResult = new GroupByResult(results, aggregate, groupByAttributes);
        if (optimizationResult == CalcResult.NULL_RESULT) {
            // if no current result we simply return the new one
            optimizationResult = newResult;
        } else {
            // if a result already exists we merge it with the new one
            optimizationResult = optimizationResult.merge(newResult);
        }
    }

    /** Helper class that should be used by optimizations to set the results. */
    public static class GroupByRawResult {

        final List<Object> groupByValues;
        final Object visitorValue;

        public GroupByRawResult(List<Object> groupByValues, Object visitorsValue) {
            this.groupByValues = groupByValues;
            this.visitorValue = visitorsValue;
        }
    }

    /** Helper class that do the computations for the group by visitor in memory. */
    private class InMemoryGroupBy {

        // feature collections grouped by the group by attributes
        private final Map<List<Object>, FeatureCalc> groupByIndexes = new HashMap<>();

        /**
         * Add a feature to the appropriate group by feature collection.
         *
         * @param feature the feature to be indexed
         */
        void index(SimpleFeature feature) {
            // list of group by attributes values
            List<Object> groupByValues =
                    groupByAttributes.stream()
                            .map(expression -> expression.evaluate(feature))
                            .collect(Collectors.toList());
            // check if a feature collection already for the group by values (using a list
            // feature collection to allow duplicates)
            FeatureCalc calc = groupByIndexes.get(groupByValues);
            if (calc == null) {
                calc = aggregate.create(expression);
                groupByIndexes.put(groupByValues, calc);
            }
            calc.visit(feature);
        }

        /**
         * We apply a copy of the aggregation visitor to each feature collection.
         *
         * @return the result of applying the aggregation visitor to eac feature collection
         */
        Map<List<Object>, CalcResult> visit() {
            Map<List<Object>, CalcResult> results = new HashMap<>();
            for (Map.Entry<List<Object>, FeatureCalc> entry : groupByIndexes.entrySet()) {
                // we add the aggregation visitor to the results
                results.put(entry.getKey(), entry.getValue().getResult());
            }
            return results;
        }
    }

    /** This class implements the feature calculation result of the group by visitor. */
    public static class GroupByResult implements CalcResult {

        private final Map<List<Object>, CalcResult> results;
        private final Aggregate aggregateVisitor;
        private final List<Expression> groupByAttributes;

        public GroupByResult(
                Map<List<Object>, CalcResult> results,
                Aggregate aggregateVisitor,
                List<Expression> groupByAttributes) {
            this.results = results;
            this.aggregateVisitor = aggregateVisitor;
            this.groupByAttributes = groupByAttributes;
        }

        public Map<List<Object>, CalcResult> getResults() {
            return results;
        }

        public Aggregate getAggregateVisitor() {
            return aggregateVisitor;
        }

        public List<Expression> getGroupByAttributes() {
            return groupByAttributes;
        }

        @Override
        public boolean isCompatible(CalcResult newResult) {
            if (newResult == CalcResult.NULL_RESULT) {
                // compatible with NULL result
                return true;
            }
            if (!(newResult instanceof GroupByResult)) {
                // not compatible with results that are not group by results
                return false;
            }
            GroupByResult groupByResult = (GroupByResult) newResult;
            // compatible only if the aggregation visitor is the same and the group by attributes
            // are the same
            return aggregateVisitor == groupByResult.getAggregateVisitor()
                    && groupByAttributes.equals(groupByResult.getGroupByAttributes());
        }

        @Override
        public CalcResult merge(CalcResult newResult) {
            if (!isCompatible(newResult)) {
                // not compatible results
                throw new IllegalArgumentException(
                        String.format(
                                "Feature calculation result '%s' is not compatible it this result '%s'.",
                                newResult.getClass().getSimpleName(),
                                GroupByResult.class.getSimpleName()));
            }
            if (newResult == CalcResult.NULL_RESULT) {
                // if the new result is a NULL result we simply return a copy of this result
                return new GroupByResult(results, aggregateVisitor, groupByAttributes);
            }
            // the merged results are initialized with the content of this result
            Map<List<Object>, CalcResult> mergedResults = new HashMap<>(results);
            for (Map.Entry<List<Object>, CalcResult> entry :
                    ((GroupByResult) newResult).getResults().entrySet()) {
                // check if this result contains the same aggregation result
                CalcResult existingResult = mergedResults.get(entry.getKey());
                if (existingResult != null) {
                    // the aggregation result exist in both results so we merge them
                    mergedResults.put(entry.getKey(), existingResult.merge(entry.getValue()));
                } else {
                    // the aggregation result only exists in the new result
                    mergedResults.put(entry.getKey(), entry.getValue());
                }
            }
            // we return a new group by result with the merged values
            return new GroupByResult(mergedResults, aggregateVisitor, groupByAttributes);
        }

        @Override
        public Object getValue() {
            return toArray();
        }

        @Override
        public int toInt() {
            return 0;
        }

        @Override
        public double toDouble() {
            return 0;
        }

        @Override
        public String toString() {
            // the calculation result as a string (or "" if not applicable)
            return results != null ? results.toString() : " ";
        }

        @Override
        public long toLong() {
            return 0;
        }

        @Override
        public float toFloat() {
            return 0;
        }

        @Override
        public Geometry toGeometry() {
            return null;
        }

        @Override
        public Envelope toEnvelope() {
            return null;
        }

        @Override
        public Point toPoint() {
            return null;
        }

        @Override
        public Set toSet() {
            return results.entrySet().stream().map(this::entryToArray).collect(Collectors.toSet());
        }

        @Override
        public List toList() {
            return results.entrySet().stream().map(this::entryToArray).collect(Collectors.toList());
        }

        @Override
        public Object[] toArray() {
            return results.entrySet().stream().map(this::entryToArray).toArray();
        }

        /**
         * The keys of the map will be List instead of arrays, since arrays don't give a decent hash
         * code.
         */
        @Override
        public Map toMap() {
            Map<List<Object>, Object> result = new HashMap<>();
            for (Map.Entry<List<Object>, CalcResult> item : results.entrySet()) {
                result.put(item.getKey(), item.getValue().getValue());
            }
            return result;
        }

        private Object[] entryToArray(Map.Entry<List<Object>, CalcResult> entry) {
            Object[] result = Arrays.copyOf(entry.getKey().toArray(), entry.getKey().size() + 1);
            result[entry.getKey().size()] = entry.getValue().getValue();
            return result;
        }
    }

    @Override
    public List<Expression> getExpressions() {
        List<Expression> result = new ArrayList<>(groupByAttributes);
        result.add(expression);
        return result;
    }

    @Override
    public Optional<List<Class>> getResultType(List<Class> inputTypes) {
        // the set of expressions includes the group by attributes and the actual attribute
        // being computed onto
        int expectedInputCount = groupByAttributes.size() + 1;
        if (inputTypes == null || inputTypes.size() != expectedInputCount)
            throw new IllegalArgumentException(
                    "Expecting " + expectedInputCount + " types, get: " + inputTypes);

        Class expressionType = inputTypes.get(expectedInputCount - 1);
        switch (aggregate) {
            case AVERAGE:
            case STD_DEV:
            case SUMAREA:
                return Optional.of(Arrays.asList(Double.class));
            default:
                return Optional.of(Arrays.asList(expressionType));
        }
    }
}
