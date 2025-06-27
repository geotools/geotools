/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;

/**
 * Calculates the Average
 *
 * @author Cory Horner, Refractions
 * @since 2.2.M2
 */
public class AverageVisitor implements FeatureCalc, FeatureAttributeVisitor {
    private Expression expr;

    /**
     * This flag lets us know that an optimized result was stored and therefore we don't have enough data to perform any
     * merges.
     */
    private boolean isOptimized = false;

    AverageStrategy strategy;

    /**
     * Constructor class for the AverageVisitor using AttributeDescriptor ID
     *
     * @param attributeTypeIndex integer representing the AttributeDescriptor
     * @param type FeatureType
     */
    public AverageVisitor(int attributeTypeIndex, SimpleFeatureType type) throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        AttributeDescriptor attributeType = type.getDescriptor(attributeTypeIndex);
        expr = factory.property(attributeType.getLocalName());
        createStrategy(attributeType.getType().getBinding());
    }

    /**
     * Constructor class for the AverageVisitor using AttributeDescriptor Name
     *
     * @param attrName string respresenting the AttributeDescriptor
     * @param type FeatureType
     */
    public AverageVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        AttributeDescriptor attributeType = type.getDescriptor(attrName);
        expr = factory.property(attributeType.getLocalName());
        createStrategy(attributeType.getType().getBinding());
    }

    /** Constructor class for the AverageVisitor using an expression */
    public AverageVisitor(Expression expr) throws IllegalFilterException {
        this.expr = expr;
    }

    public void init(SimpleFeatureCollection collection) {
        // do nothing
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(expr);
    }

    @Override
    public Optional<List<Class>> getResultType(List<Class> inputTypes) {
        if (inputTypes == null || inputTypes.size() != 1)
            throw new IllegalArgumentException("Expecting a single type in input, not " + inputTypes);

        Class type = inputTypes.get(0);
        if (Number.class.isAssignableFrom(type)) {
            return Optional.of(Collections.singletonList(Double.class));
        }
        throw new IllegalArgumentException("The input type for sum must be numeric, instead this was found: " + type);
    }

    /**
     * Factory method for creating the appropriate strategy object
     *
     * <p>
     *
     * @param type the class to use for the calculation
     * @return a strategy object of the correct data type to be used for calculating the average
     */
    private static AverageStrategy createStrategy(Class type) {
        if (type == Integer.class) {
            return new IntegerAverageStrategy();
        } else if (type == Long.class) {
            return new LongAverageStrategy();
        } else if (type == Float.class) {
            return new FloatAverageStrategy();
        } else if (Number.class.isAssignableFrom(type)) return new DoubleAverageStrategy();

        return null;
    }

    public void visit(SimpleFeature feature) {
        visit((Feature) feature);
    }

    @Override
    public void visit(org.geotools.api.feature.Feature feature) {
        Object value = expr.evaluate(feature);

        if (value != null) {
            if (strategy == null) {
                Class type = value.getClass();
                strategy = createStrategy(type);
            }

            strategy.add(value);
        }
    }

    public Expression getExpression() {
        return expr;
    }

    /**
     * Returns the average from the visitor's current
     *
     * @return the average
     */
    public Object getAverage() {
        return strategy.getResult();
    }

    /** Resets the "Average" strategy pattern */
    public void reset() {
        strategy = null;
        isOptimized = false;
    }

    /** Returns a CalcResult object (containing the Average) */
    @Override
    public CalcResult getResult() {
        if (strategy == null) {
            return CalcResult.NULL_RESULT;
        }
        return new AverageResult(strategy, isOptimized);
    }

    public void setValue(Object newAverage) {
        reset();

        if (newAverage instanceof AverageStrategy) {
            Class type = newAverage.getClass();
            strategy = createStrategy(type);
            strategy.add(newAverage);
            isOptimized = true;
        } else if (newAverage instanceof Number) {
            strategy = new FixedResultAverageStrategy((Number) newAverage);
            isOptimized = true;
        } else throw new IllegalArgumentException("Cannot set the value, should be a Number or an AverageStrategy");
    }

    public void setValue(int newCount, Object newSum) {
        reset();
        strategy = createStrategy(newSum.getClass());
        strategy.set(newCount, newSum);
        isOptimized = false; // this is an optimization, but we have the same

        // information we would otherwise have if we had
        // in fact visited each feature
    }

    /** Encapsulates the strategy pattern for the "Average" Visitor */
    interface AverageStrategy {
        public void add(Object value);

        public Object getResult();

        public Object getSum();

        public int getCount();

        public void set(int count, Object sum);
    }

    /** Implements the average calculation for values of the type double */
    static class DoubleAverageStrategy implements AverageStrategy {
        double number = 0;
        int count = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).doubleValue();
            count++;
        }

        @Override
        public Object getResult() {
            return Double.valueOf(number / count);
        }

        @Override
        public Object getSum() {
            return Double.valueOf(number);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public void set(int newCount, Object sum) {
            number = ((Number) sum).doubleValue();
            count = newCount;
        }
    }

    /** Implements the average calculation for values of the type float */
    static class FloatAverageStrategy implements AverageStrategy {
        float number = 0;
        int count = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).floatValue();
            count++;
        }

        @Override
        public Object getResult() {
            return Float.valueOf(number / count);
        }

        @Override
        public Object getSum() {
            return Float.valueOf(number);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public void set(int newCount, Object sum) {
            number = ((Number) sum).floatValue();
            count = newCount;
        }
    }

    /** Implements the average calculation for values of the type long */
    static class LongAverageStrategy implements AverageStrategy {
        long number = 0;
        int count = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).longValue();
            count++;
        }

        @Override
        public Object getResult() {
            return Double.valueOf((double) number / count);
        }

        @Override
        public Object getSum() {
            return Long.valueOf(number);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public void set(int newCount, Object sum) {
            number = ((Number) sum).longValue();
            count = newCount;
        }
    }

    /** Implements the average calculation for values of the type integer */
    static class IntegerAverageStrategy implements AverageStrategy {
        int number = 0;
        int count = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).intValue();
            count++;
        }

        @Override
        public Object getResult() {
            return Double.valueOf((double) number / count);
        }

        @Override
        public Object getSum() {
            return Integer.valueOf(number);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public void set(int newCount, Object sum) {
            number = ((Number) sum).intValue();
            count = newCount;
        }
    }

    /**
     * Implements the average calculation for fixed values (for optimizing out in memory counts. This however means the
     * average cannot be merged any with other results (normally not a problem when the final result has been calculated
     * already by storage)
     */
    static class FixedResultAverageStrategy implements AverageStrategy {
        private static final String NO_MERGE_ERROR = "This strategy does not support merge with other results";
        Number result;

        public FixedResultAverageStrategy(Number result) {
            this.result = result;
        }

        @Override
        public void add(Object value) {
            throw new UnsupportedOperationException(NO_MERGE_ERROR);
        }

        @Override
        public Object getResult() {
            return result;
        }

        @Override
        public Object getSum() {
            throw new UnsupportedOperationException(NO_MERGE_ERROR);
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException(NO_MERGE_ERROR);
        }

        @Override
        public void set(int newCount, Object sum) {
            throw new UnsupportedOperationException(NO_MERGE_ERROR);
        }
    }

    /** */
    public static class AverageResult extends AbstractCalcResult {
        private AverageStrategy averageStrategy;
        private boolean isOptimized = false;

        public AverageResult(Object value) {
            if (value instanceof AverageStrategy) {
                averageStrategy = (AverageStrategy) value;
            } else if (value instanceof Number) {
                averageStrategy = new FixedResultAverageStrategy((Number) value);
            } else {
                throw new IllegalArgumentException("Cannot build an AverageResult using "
                        + value
                        + ", must be a Number or an instance of AverageStrategy");
            }
        }

        public AverageResult(Object newAverageStrategy, boolean isOptimized) {
            averageStrategy = (AverageStrategy) newAverageStrategy;
            this.isOptimized = isOptimized;
        }

        public AverageResult(int newCount, Object newSum) {
            averageStrategy = createStrategy(newSum.getClass());
            averageStrategy.set(newCount, newSum);
        }

        public Object getResult() {
            return averageStrategy.getResult();
        }

        @Override
        public Object getValue() {
            return averageStrategy.getResult();
        }

        /**
         * The count used to calculate the average
         *
         * @return the count, or -1 if unknown
         */
        public int getCount() {
            if (isOptimized) {
                return -1; // there is no count, as an optimization was used.
            } else {
                return averageStrategy.getCount();
            }
        }

        /** The sum used to calculate the average */
        public Object getSum() {
            if (isOptimized) {
                return null;
            } else {
                return averageStrategy.getSum();
            }
        }

        /**
         * Determines if the target CalcResult object can be merged with this CalcResult object
         *
         * @param targetResults a second CalcResult object (target)
         * @return boolean
         */
        @Override
        public boolean isCompatible(CalcResult targetResults) {
            if (targetResults instanceof AverageResult || targetResults == CalcResult.NULL_RESULT) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Merges the contents of a CalcResult Object with another CalcResult Object. If the two CalcResult objects are
         * compatible, the merged result (a new object) is returned.
         *
         * @param resultsToAdd the CalcResult to merge the results with
         * @return a new merged CalcResult object
         * @throws IllegalArgumentException when the resultsToAdd are inappropriate
         */
        @Override
        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException("Parameter is not a compatible type");
            }

            if (resultsToAdd == CalcResult.NULL_RESULT) {
                return this;
            }

            if (resultsToAdd instanceof AverageResult) {
                AverageResult moreResults = (AverageResult) resultsToAdd;

                // ensure both results are NOT optimized
                if (isOptimized || moreResults.isOptimized) {
                    throw new IllegalArgumentException("Optimized average results cannot be merged.");
                }

                Number[] sums = {(Number) averageStrategy.getSum(), (Number) moreResults.averageStrategy.getSum()};
                Number newSum = CalcUtil.sum(sums);
                Number newCount = Integer.valueOf(averageStrategy.getCount() + moreResults.averageStrategy.getCount());
                Number[] params = {newSum, newCount};
                Object newAverage = CalcUtil.getObject((Object[]) params);
                AverageStrategy newAverageObj = createStrategy(newAverage.getClass());
                newAverageObj.set(newCount.intValue(), newSum);

                return new AverageResult(newAverageObj);
            } else {
                throw new IllegalArgumentException(
                        "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
