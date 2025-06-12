/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;

/**
 * Finds the nearest value to the provided one in the attribute domain.
 *
 * @author Andrea Aime - GeoSolutions
 * @author Ilkka Rinne / Spatineo Inc for the Finnish Meteorological Institute
 */
public class NearestVisitor implements FeatureCalc, FeatureAttributeVisitor {
    private Expression expr;

    private Class attributeClass;

    private NearestAccumulator accumulator;

    boolean visited = false;

    double shortestDistance = Double.MAX_VALUE;

    private Object valueToMatch;

    private Object nearest;

    /**
     * Creates a NearestVisitor instance for the given attribute and a value to match.
     *
     * @param valueToMatch The target value to match
     */
    public NearestVisitor(Expression expression, Object valueToMatch) {
        this.expr = expression;
        this.valueToMatch = valueToMatch;
    }

    /**
     * Visitor function, which looks at each feature and finds the value of the attribute given attribute nearest to the
     * given comparison value.
     *
     * @param feature the feature to be visited
     */
    @Override
    @SuppressWarnings("unchecked")
    public void visit(org.geotools.api.feature.Feature feature) {
        // bail out immediately if we have already found an exact match
        if (visited) {
            return;
        }

        if (attributeClass == null) {
            PropertyDescriptor desc = (PropertyDescriptor) expr.evaluate(feature.getType());
            attributeClass = desc.getType().getBinding();
            if (accumulator == null) {
                accumulator = getAccumulator(attributeClass);
            }
        }

        // extract the value
        Object attribValue = expr.evaluate(feature);
        if (attribValue != null) {
            visited |= accumulator.visit(attribValue);
        }
    }

    private NearestAccumulator<?> getAccumulator(Class<?> attributeClass) {
        if (Number.class.isAssignableFrom(attributeClass)) {
            Double convertedTarget = Converters.convert(valueToMatch, Double.class);
            return new NumberAccumulator(convertedTarget);
        } else if (Date.class.isAssignableFrom(attributeClass)) {
            Date convertedTarget = Converters.convert(valueToMatch, Date.class);
            return new DateAccumulator(convertedTarget);
        } else if (Geometry.class.isAssignableFrom(attributeClass)) {
            Geometry convertedTarget = Converters.convert(valueToMatch, Geometry.class);
            return new GeometryAccumulator(convertedTarget);
        } else if (Comparable.class.isAssignableFrom(attributeClass)) {
            Comparable convertedTarget = (Comparable) Converters.convert(valueToMatch, attributeClass);
            return new ComparableAccumulator(convertedTarget);
        }
        // TODO: we should probably create a custom one for strings, there are various
        // string distance algorithms described on the net

        throw new IllegalArgumentException("Don't know how to compute nearest for target class " + attributeClass);
    }

    public void reset() {
        visited = false;
        accumulator = null;
        attributeClass = null;
        nearest = null;
    }

    public void setValue(Comparable nearest) {
        this.nearest = nearest;
        this.visited = true;
    }

    public void setValue(Comparable maxBelow, Comparable minAbove) {
        if (maxBelow == null) {
            this.nearest = minAbove;
        } else if (minAbove == null) {
            this.nearest = maxBelow;
        } else {
            @SuppressWarnings("unchecked")
            NearestAccumulator<Object> accumulator = (NearestAccumulator<Object>) getAccumulator(maxBelow.getClass());
            accumulator.visit(maxBelow);
            accumulator.visit(minAbove);
            nearest = accumulator.getNearest();
        }
    }

    /** Returns the match after {@link #visit}. */
    public Object getNearestMatch() throws IllegalStateException {
        if (nearest == null) {
            if (accumulator != null) {
                this.nearest = accumulator.getNearest();
            }
        }

        return nearest;
    }

    @Override
    public CalcResult getResult() {
        return new AbstractCalcResult() {
            @Override
            public Object getValue() {
                return NearestVisitor.this.getNearestMatch();
            }
        };
    }

    /**
     * Expression used to access collection content.
     *
     * @return expr used to access collection
     */
    public Expression getExpression() {
        return expr;
    }

    /**
     * Provided value to match against.
     *
     * @return value to match against.
     */
    public Object getValueToMatch() {
        return valueToMatch;
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(expr);
    }

    @Override
    public Optional<List<Class>> getResultType(List<Class> inputTypes) {
        return CalcUtil.reflectInputTypes(1, inputTypes);
    }

    static interface NearestAccumulator<T> {
        public boolean visit(T value);

        public T getNearest();
    }

    @SuppressWarnings("unchecked") // we don't know what type of comparable till runtime
    static class ComparableAccumulator implements NearestAccumulator<Comparable> {

        private Comparable minAbove;

        private Comparable maxBelow;

        private Comparable targetValue;

        public ComparableAccumulator(Comparable targetValue) {
            this.targetValue = targetValue;
        }

        @Override
        public boolean visit(Comparable value) {
            // compare to find the two values that are right below, and right above, the target
            // number
            int aboveBelow = value.compareTo(targetValue);
            boolean exact = false;
            if (aboveBelow == 0) {
                // equality, bail out now
                minAbove = maxBelow = value;
                exact = true;
            } else if (aboveBelow > 0) {
                if (minAbove == null || minAbove.compareTo(value) > 0) {
                    minAbove = value;
                }
            } else if (aboveBelow < 0) {
                if (maxBelow == null || maxBelow.compareTo(value) < 0) {
                    maxBelow = value;
                }
            }

            return exact;
        }

        @Override
        public Comparable getNearest() {
            if (maxBelow == null) {
                return minAbove;
            } else if (minAbove == null) {
                return maxBelow;
            } else {
                // No real guarantee this will return the closest, but this is the best we can do
                // not knowing anything else about the target class
                int diffAbove = Math.abs(targetValue.compareTo(minAbove));
                int diffBelow = Math.abs(targetValue.compareTo(maxBelow));

                if (diffAbove < diffBelow) {
                    return minAbove;
                } else {
                    return maxBelow;
                }
            }
        }
    }

    static class NumberAccumulator implements NearestAccumulator<Number> {

        double targetValue;

        double difference = Double.MAX_VALUE;

        Number nearest;

        public NumberAccumulator(Number targetValue) {
            this.targetValue = targetValue.doubleValue();
        }

        @Override
        public boolean visit(Number value) {
            double v = value.doubleValue();
            double d = Math.abs(v - targetValue);
            if (d < difference) {
                difference = d;
                nearest = value;
            }

            return d == 0;
        }

        @Override
        public Number getNearest() {
            return nearest;
        }
    }

    static class DateAccumulator implements NearestAccumulator<Date> {

        long targetValue;

        long difference = Long.MAX_VALUE;

        Date nearest;

        public DateAccumulator(Date targetValue) {
            this.targetValue = targetValue.getTime();
        }

        @Override
        public boolean visit(Date value) {
            long v = value.getTime();
            long d = Math.abs(v - targetValue);
            if (d < difference) {
                difference = d;
                nearest = value;
            }

            return d == 0;
        }

        @Override
        public Date getNearest() {
            return nearest;
        }
    }

    static class GeometryAccumulator implements NearestAccumulator<Geometry> {

        Geometry targetValue;

        double distance = Double.MAX_VALUE;

        Geometry nearest;

        public GeometryAccumulator(Geometry targetValue) {
            this.targetValue = targetValue;
        }

        @Override
        public boolean visit(Geometry value) {
            double d = targetValue.distance(value);
            if (d < distance) {
                distance = d;
                nearest = value;
            }
            return d == 0;
        }

        @Override
        public Geometry getNearest() {
            return nearest;
        }
    }
}
