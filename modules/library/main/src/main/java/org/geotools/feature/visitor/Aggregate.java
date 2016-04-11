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

import org.geotools.util.Converters;
import org.geotools.util.UnsupportedImplementationException;
import org.opengis.filter.expression.Expression;

/**
 * This Enum contains all the visitors that can be used with the group by visitor.
 * Each visitor supported by the group by visitor has to provide a consistent way to
 * create a new instance and if the visitor result can be optimized a way to wrap
 * the raw result.
 */
public enum Aggregate {

    AVERAGE {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new AverageVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            throw new UnsupportedImplementationException("Wrapping raw result of average visitor not supported.");
        }
    },
    COUNT {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new CountVisitor();
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            CountVisitor visitor = new CountVisitor();
            visitor.setValue(Converters.convert(value, Integer.class));
            return visitor.getResult();
        }
    },
    MAX {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new MaxVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            MaxVisitor visitor = new MaxVisitor(aggregateAttribute);
            visitor.setValue(value);
            return visitor.getResult();
        }
    },
    MEDIAN {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new MedianVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            throw new UnsupportedImplementationException("Wrapping raw result of mean visitor not supported.");
        }
    },
    MIN {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new MinVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            MinVisitor visitor = new MinVisitor(aggregateAttribute);
            visitor.setValue(value);
            return visitor.getResult();
        }
    },
    STD_DEV {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new StandardDeviationVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            throw new UnsupportedImplementationException("Wrapping raw result of standard deviation visitor not supported.");
        }
    },
    SUM {
        @Override
        public FeatureCalc create(Expression aggregateAttribute) {
            return new SumVisitor(aggregateAttribute);
        }

        @Override
        public CalcResult wrap(Expression aggregateAttribute, Object value) {
            SumVisitor visitor = new SumVisitor(aggregateAttribute);
            visitor.setValue(value);
            return visitor.getResult();
        }
    };

    /**
     * Creates a new visitor using an aggregate attribute.
     *
     * @param aggregateAttribute the aggregate attribute to be used by the visitor
     * @return a new instance of the visitor
     */
    public abstract FeatureCalc create(Expression aggregateAttribute);

    /**
     * Wraps a raw value in the appropriate visitor calculation result. The typical usage of this is to wrap values
     * returned by stores able to handle the visitor (for example the JDBCDataStore).
     *
     * @param aggregateAttribute the aggregate attribute to be used by the visitor
     * @param value              the raw value to be wrapped
     * @return value wrapped in the appropriate visitor calculation result
     */
    public abstract CalcResult wrap(Expression aggregateAttribute, Object value);

    /**
     * Helper method that given a visitor name returns the appropriate enum constant.
     * The performed match by name is more permissive that the default one. The match will
     * not be case sensitive and slightly different names can be used (camel case versus snake case).
     *
     * @param visitorName the visitor name
     * @return this enum constant that machs the visitor name
     */
    public static Aggregate permissiveValueOf(String visitorName) {
        switch (visitorName.toLowerCase()) {
            case "average":
                return AVERAGE;
            case "count":
                return COUNT;
            case "max":
                return MAX;
            case "median":
                return MEDIAN;
            case "min":
                return MIN;
            case "std_dev":
                return STD_DEV;
            case "stddev":
                return STD_DEV;
            case "sum":
                return SUM;
            default:
                throw new IllegalArgumentException(
                        String.format("Visitor with name '%s' is not a valid group by visitor.", visitorName));
        }
    }
}