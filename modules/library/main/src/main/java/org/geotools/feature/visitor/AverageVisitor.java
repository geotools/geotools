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
import java.util.List;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the Average
 *
 * @author Cory Horner, Refractions
 * @since 2.2.M2
 */
public class AverageVisitor implements FeatureCalc, FeatureAttributeVisitor {
    private Expression expr;

    /**
     * This flag lets us know that an optimized result was stored and therefore we don't have enough
     * data to perform any merges.
     */
    private boolean isOptimized = false;

    AverageStrategy strategy;

    /**
     * Constructor class for the AverageVisitor using AttributeDescriptor ID
     *
     * @param attributeTypeIndex integer representing the AttributeDescriptor
     * @param type FeatureType
     */
    public AverageVisitor(int attributeTypeIndex, SimpleFeatureType type)
            throws IllegalFilterException {
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

    public void visit(org.opengis.feature.Feature feature) {
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
    public CalcResult getResult() {
        if (strategy == null) {
            return CalcResult.NULL_RESULT;
        }
        return new AverageResult(strategy, isOptimized);
    }

    public void setValue(Object newAverage) {
        reset();

        Class type = newAverage.getClass();
        strategy = createStrategy(type);
        strategy.add(newAverage);
        isOptimized = true;
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

        public void add(Object value) {
            number += ((Number) value).doubleValue();
            count++;
        }

        public Object getResult() {
            return Double.valueOf(number / count);
        }

        public Object getSum() {
            return Double.valueOf(number);
        }

        public int getCount() {
            return count;
        }

        public void set(int newCount, Object sum) {
            number = ((Number) sum).doubleValue();
            count = newCount;
        }
    }

    /** Implements the average calculation for values of the type float */
    static class FloatAverageStrategy implements AverageStrategy {
        float number = 0;
        int count = 0;

        public void add(Object value) {
            number += ((Number) value).floatValue();
            count++;
        }

        public Object getResult() {
            return Float.valueOf((float) number / count);
        }

        public Object getSum() {
            return Float.valueOf(number);
        }

        public int getCount() {
            return count;
        }

        public void set(int newCount, Object sum) {
            number = ((Number) sum).floatValue();
            count = newCount;
        }
    }

    /** Implements the average calculation for values of the type long */
    static class LongAverageStrategy implements AverageStrategy {
        long number = 0;
        int count = 0;

        public void add(Object value) {
            number += ((Number) value).longValue();
            count++;
        }

        public Object getResult() {
            return Double.valueOf((double) number / count);
        }

        public Object getSum() {
            return Long.valueOf(number);
        }

        public int getCount() {
            return count;
        }

        public void set(int newCount, Object sum) {
            number = ((Number) sum).longValue();
            count = newCount;
        }
    }

    /** Implements the average calculation for values of the type integer */
    static class IntegerAverageStrategy implements AverageStrategy {
        int number = 0;
        int count = 0;

        public void add(Object value) {
            number += ((Number) value).intValue();
            count++;
        }

        public Object getResult() {
            return Double.valueOf((double) number / count);
        }

        public Object getSum() {
            return Integer.valueOf(number);
        }

        public int getCount() {
            return count;
        }

        public void set(int newCount, Object sum) {
            number = ((Number) sum).intValue();
            count = newCount;
        }
    }

    /** */
    public static class AverageResult extends AbstractCalcResult {
        private AverageStrategy averageStrategy;
        private boolean isOptimized = false;

        public AverageResult(Object newAverageStrategy) {
            averageStrategy = (AverageStrategy) newAverageStrategy;
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
        public boolean isCompatible(CalcResult targetResults) {
            if (targetResults instanceof AverageResult || targetResults == CalcResult.NULL_RESULT) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Merges the contents of a CalcResult Object with another CalcResult Object. If the two
         * CalcResult objects are compatible, the merged result (a new object) is returned.
         *
         * @param resultsToAdd the CalcResult to merge the results with
         * @return a new merged CalcResult object
         * @throws IllegalArgumentException when the resultsToAdd are inappropriate
         */
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
                    throw new IllegalArgumentException(
                            "Optimized average results cannot be merged.");
                }

                Number[] sums =
                        new Number[] {
                            (Number) averageStrategy.getSum(),
                            (Number) moreResults.averageStrategy.getSum()
                        };
                Number newSum = CalcUtil.sum(sums);
                Number newCount =
                        (Number)
                                Integer.valueOf(
                                        averageStrategy.getCount()
                                                + moreResults.averageStrategy.getCount());
                Number[] params = new Number[] {newSum, newCount};
                Object newAverage = CalcUtil.getObject(params);
                AverageStrategy newAverageObj;
                newAverageObj = createStrategy(newAverage.getClass());
                newAverageObj.set(newCount.intValue(), newSum);

                return new AverageResult(newAverageObj);
            } else {
                throw new IllegalArgumentException(
                        "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
