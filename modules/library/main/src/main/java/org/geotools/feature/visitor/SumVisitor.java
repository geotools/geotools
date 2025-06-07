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
import java.util.Optional;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.AverageVisitor.AverageResult;
import org.geotools.feature.visitor.CountVisitor.CountResult;
import org.geotools.filter.IllegalFilterException;

/**
 * Calculates the Sum of an attribute (of a FeatureVisitor)
 *
 * @author Cory Horner, Refractions
 * @since 2.2.M2
 */
public class SumVisitor implements FeatureCalc, FeatureAttributeVisitor {
    private Expression expr;

    SumStrategy strategy;

    public SumVisitor(int attributeTypeIndex, SimpleFeatureType type) throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        AttributeDescriptor attributeType = type.getDescriptor(attributeTypeIndex);
        expr = factory.property(attributeType.getLocalName());
        createStrategy(attributeType.getType().getBinding());
    }

    public SumVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        AttributeDescriptor attributeType = type.getDescriptor(attrName);
        expr = factory.property(attributeType.getLocalName());
        createStrategy(attributeType.getType().getBinding());
    }

    public SumVisitor(Expression expr) throws IllegalFilterException {
        this.expr = expr;
    }

    protected void setStrategy(SumStrategy strategy) {
        if (this.strategy == null) {
            this.strategy = strategy;
        }
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
            return Optional.of(inputTypes);
        }
        throw new IllegalArgumentException("The input type for sum must be numeric, instead this was found: " + type);
    }

    /**
     * Factory method
     *
     * @param type The Class of the attributeType
     * @return The correct strategy class (which returns the correct data type)
     */
    private static SumStrategy createStrategy(Class type) {
        if (type == Integer.class) {
            return new IntegerSumStrategy();
        } else if (type == Long.class) {
            return new LongSumStrategy();
        } else if (type == Float.class) {
            return new FloatSumStrategy();
        } else if (Number.class.isAssignableFrom(type)) {
            return new DoubleSumStrategy();
        }

        return null;
    }

    public void visit(SimpleFeature feature) {
        visit((Feature) feature);
    }

    @Override
    public void visit(Feature feature) {
        Object value = expr.evaluate(feature);

        if (value != null) {
            if (strategy == null) {
                strategy = createStrategy(value.getClass());
            }

            strategy.add(value);
        }
    }

    public Expression getExpression() {
        return expr;
    }

    public Object getSum() {
        return strategy.getResult();
    }

    public void setValue(Object newSum) {
        strategy = createStrategy(newSum.getClass());
        strategy.add(newSum);
    }

    public void reset() {
        strategy = null;
    }

    @Override
    public CalcResult getResult() {
        if (strategy == null) {
            return CalcResult.NULL_RESULT;
        }
        return new SumResult(strategy);
    }

    protected interface SumStrategy {
        public void add(Object value);

        public Object getResult();
    }

    static class DoubleSumStrategy implements SumStrategy {
        double number = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).doubleValue();
        }

        @Override
        public Object getResult() {
            return Double.valueOf(number);
        }
    }

    static class FloatSumStrategy implements SumStrategy {
        float number = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).floatValue();
        }

        @Override
        public Object getResult() {
            return Float.valueOf(number);
        }
    }

    static class LongSumStrategy implements SumStrategy {
        long number = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).longValue();
        }

        @Override
        public Object getResult() {
            return Long.valueOf(number);
        }
    }

    static class IntegerSumStrategy implements SumStrategy {
        int number = 0;

        @Override
        public void add(Object value) {
            number += ((Number) value).intValue();
        }

        @Override
        public Object getResult() {
            return Integer.valueOf(number);
        }
    }

    public static class SumResult extends AbstractCalcResult {
        private SumStrategy sum;

        public SumResult(SumStrategy newSum) {
            sum = newSum;
        }

        public SumResult(Object value) {
            sum = createStrategy(value.getClass());
            sum.add(value);
        }

        @Override
        public Object getValue() {
            return sum.getResult();
        }

        @Override
        public boolean isCompatible(CalcResult targetResults) {
            if (targetResults == CalcResult.NULL_RESULT) return true;
            if (targetResults instanceof SumResult) return true;
            if (targetResults instanceof CountResult) return true;
            return false;
        }

        @Override
        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException("Parameter is not a compatible type");
            }

            if (resultsToAdd == CalcResult.NULL_RESULT) {
                return this;
            }

            if (resultsToAdd instanceof SumResult) {
                // create a new strategy object of the correct dataType
                Number[] sums = new Number[2];
                sums[0] = (Number) sum.getResult();
                sums[1] = (Number) resultsToAdd.getValue();
                SumStrategy newSum =
                        createStrategy(CalcUtil.getObject((Object[]) sums).getClass());
                // add the two sums
                newSum.add(sums[0]);
                newSum.add(sums[1]);
                return new SumResult(newSum);
            } else if (resultsToAdd instanceof CountResult) {
                // SumResult + CountResult = AverageResult
                int count = resultsToAdd.toInt();
                AverageResult newResult = new AverageResult(count, sum.getResult());

                return newResult;
            } else {
                throw new IllegalArgumentException(
                        "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
