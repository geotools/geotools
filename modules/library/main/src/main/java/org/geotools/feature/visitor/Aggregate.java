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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpression;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Aggregate functions defined for use with the GeoTools library.
 *
 * <p>Aggregates are backed by a {@link FeatureCalc} visitor for direct use with a FeatureCollection
 * (or use with {@link GroupByVisitor} ). THe {@link #create(Expression)} method is used as a
 * factory method to configure an appropriate visitor.
 *
 * <p>During processing a {@link CalcResult} is used to store intermediate results. Implementations
 * that handle an aggregate function directly (such as JDBCDataStore) can use can use the wrap
 * method to generate the expect CalcResult from a calculated raw value.
 */
public enum Aggregate {
    AVERAGE {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new AverageVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            return new AverageVisitor.AverageResult(value);
        }
    },
    COUNT {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new CountVisitor();
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            int count = Converters.convert(value, Integer.class);
            return new CountVisitor.CountResult(count);
        }
    },
    MAX {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new MaxVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            return new MaxVisitor.MaxResult((Comparable<?>) value);
        }
    },
    MEDIAN {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new MedianVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            return new MedianVisitor.MedianResult(value);
        }
    },
    MIN {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new MinVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            return new MinVisitor.MinResult((Comparable<?>) value);
        }
    },
    STD_DEV {
        @Override
        public FeatureCalc create(Expression expr) {
            return new StandardDeviationVisitor(expr);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            if (value == null) {
                return AbstractCalcResult.NULL_RESULT;
            }
            Double deviation = Converters.convert(value, Double.class);
            return new StandardDeviationVisitor.Result(deviation);
        }
    },
    SUM {
        @Override
        public FeatureCalc create(Expression expr) {
            return new SumVisitor(expr);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            return new SumVisitor.SumResult(value);
        }
    },
    SUMAREA {
        @Override
        public FeatureCalc create(Expression expr) {
            // if expr is <property> wrap it with area2 function, if not, it's already wrapped
            if (expr instanceof FunctionExpression) {
                return new SumVisitor(expr);
            }
            FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
            return new SumVisitor(factory.function("area2", expr));
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            if (value == null) {
                return AbstractCalcResult.NULL_RESULT;
            }
            Double area = Converters.convert(value, Double.class);
            return new SumVisitor.SumResult(value);
        }
    };

    /**
     * Factory method creating a visitor using an aggregate attribute.
     *
     * @param expression Expression used to sample collection (often a PropertyName)
     * @return a new instance of the visitor
     */
    public abstract FeatureCalc create(Expression expression);

    /**
     * Wraps a raw value in the appropriate visitor calculation result. The typical usage of this is
     * to wrap values returned by stores able to handle the visitor (for example the JDBCDataStore).
     *
     * @param expression Expression used to sample collection (often a PropertyName)
     * @param value The raw value to be wrapped
     * @return value Wrapped in the appropriate visitor calculation result
     */
    public abstract CalcResult wrap(Expression expression, Object value);

    /**
     * Helper method that given a visitor name returns the appropriate enum constant.
     *
     * <p>The performed match by name is more permissive that the default one. The match will not be
     * case sensitive and slightly different names can be used (camel case versus snake case).
     *
     * @param visitorName the visitor name
     * @return this enum constant that machs the visitor name
     */
    public static Aggregate valueOfIgnoreCase(String visitorName) {
        if ("stddev".equalsIgnoreCase(visitorName)) {
            return Aggregate.STD_DEV;
        }
        return valueOf(visitorName.toUpperCase());
    }
}
