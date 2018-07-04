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
package org.geotools.styling.css.util;

import java.util.Date;
import java.util.List;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.parameter.Parameter;

/**
 * Applies duck typing to properties in filters, aggregating the types found in the {@link
 * TypeAggregator}
 *
 * @author Andrea Aime - GeoSolutions
 */
class FilterTypeVisitor extends DefaultFilterVisitor {

    TypeAggregator aggregator;

    public FilterTypeVisitor(TypeAggregator aggregator) {
        this.aggregator = aggregator;
    }

    String getPropertyName(Expression ex) {
        if (ex instanceof PropertyName) {
            PropertyName pn = (PropertyName) ex;
            return pn.getPropertyName();
        }

        return null;
    }

    Object getLiteralValue(Expression ex) {
        if (ex instanceof Literal) {
            return ex.evaluate(null);
        }

        return null;
    }

    @Override
    public Object visit(Add expression, Object data) {
        visitMathExpression(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Multiply expression, Object data) {
        visitMathExpression(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Divide expression, Object data) {
        visitMathExpression(expression);
        return super.visit(expression, data);
    }

    @Override
    public Object visit(Subtract expression, Object data) {
        visitMathExpression(expression);
        return super.visit(expression, data);
    }

    private void visitMathExpression(BinaryExpression expression) {
        String name = getPropertyName(expression.getExpression1());
        if (name != null) {
            aggregator.addType(name, Double.class);
        }
        name = getPropertyName(expression.getExpression2());
        if (name != null) {
            aggregator.addType(name, Double.class);
        }
    }

    @Override
    public Object visit(Function expression, Object data) {
        FunctionName name = expression.getFunctionName();
        if (name != null && name.getArgumentCount() > 0) {
            List<Parameter<?>> argumentTypes = name.getArguments();
            List<Expression> arguments = expression.getParameters();
            for (int i = 0; i < Math.min(arguments.size(), argumentTypes.size()); i++) {
                Expression ex = arguments.get(i);
                String propertyName = getPropertyName(ex);
                Parameter<?> argumentType = argumentTypes.get(i);
                if (propertyName != null && argumentType != null) {
                    aggregator.addType(propertyName, argumentType.getType());
                }
            }
        }

        return super.visit(expression, data);
    }

    public Object visit(After after, Object data) {
        visitTemporalExpression(after);
        return super.visit(after, data);
    }

    public Object visit(AnyInteracts anyInteracts, Object data) {
        visitTemporalExpression(anyInteracts);
        return super.visit(anyInteracts, data);
    }

    public Object visit(Before before, Object data) {
        visitTemporalExpression(before);
        return super.visit(before, data);
    }

    public Object visit(Begins begins, Object data) {
        visitTemporalExpression(begins);
        return super.visit(begins, data);
    }

    public Object visit(BegunBy begunBy, Object data) {
        visitTemporalExpression(begunBy);
        return super.visit(begunBy, data);
    }

    public Object visit(During during, Object data) {
        visitTemporalExpression(during);
        return super.visit(during, data);
    }

    public Object visit(EndedBy endedBy, Object data) {
        visitTemporalExpression(endedBy);
        return super.visit(endedBy, data);
    }

    public Object visit(Ends ends, Object data) {
        visitTemporalExpression(ends);
        return super.visit(ends, data);
    }

    public Object visit(Meets meets, Object data) {
        visitTemporalExpression(meets);
        return super.visit(meets, data);
    }

    public Object visit(MetBy metBy, Object data) {
        visitTemporalExpression(metBy);
        return super.visit(metBy, data);
    }

    public Object visit(OverlappedBy overlappedBy, Object data) {
        visitTemporalExpression(overlappedBy);
        return super.visit(overlappedBy, data);
    }

    public Object visit(TContains contains, Object data) {
        visitTemporalExpression(contains);
        return super.visit(contains, data);
    }

    public Object visit(TEquals equals, Object data) {
        visitTemporalExpression(equals);
        return super.visit(equals, data);
    }

    public Object visit(TOverlaps contains, Object data) {
        visitTemporalExpression(contains);
        return super.visit(contains, data);
    }

    private void visitTemporalExpression(BinaryTemporalOperator expression) {
        String name = getPropertyName(expression.getExpression1());
        if (name != null) {
            aggregator.addType(name, Date.class);
        }
        name = getPropertyName(expression.getExpression2());
        if (name != null) {
            aggregator.addType(name, Date.class);
        }
    }

    public Object visit(PropertyIsBetween filter, Object data) {
        String name = getPropertyName(filter.getExpression());
        if (name != null) {
            Object v1 = getLiteralValue(filter.getLowerBoundary());
            if (v1 != null) {
                aggregator.addType(name, v1.getClass());
            }
            Object v2 = getLiteralValue(filter.getUpperBoundary());
            if (v2 != null) {
                aggregator.addType(name, v2.getClass());
            }
        }
        return super.visit(filter, data);
    }

    public Object visit(PropertyIsEqualTo filter, Object data) {
        visitBinaryComparison(filter);
        return super.visit(filter, data);
    }

    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        visitBinaryComparison(filter);
        return super.visit(filter, data);
    }

    public Object visit(PropertyIsGreaterThan filter, Object data) {
        visitBinaryComparison(filter);
        return super.visit(filter, data);
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        visitBinaryComparison(filter);
        return super.visit(filter, data);
    }

    public Object visit(PropertyIsLessThan filter, Object data) {
        visitBinaryComparison(filter);
        return super.visit(filter, data);
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        visitBinaryComparison(filter);
        return super.visit(filter, data);
    }

    private void visitBinaryComparison(BinaryComparisonOperator filter) {
        String name = getPropertyName(filter.getExpression1());
        if (name != null) {
            Object value = getLiteralValue(filter.getExpression2());
            if (value != null) {
                aggregator.addType(name, value.getClass());
            }
        }
        name = getPropertyName(filter.getExpression2());
        if (name != null) {
            Object value = getLiteralValue(filter.getExpression1());
            if (value != null) {
                aggregator.addType(name, value.getClass());
            }
        }
    }

    public Object visit(PropertyIsLike filter, Object data) {
        String name = getPropertyName(filter.getExpression());
        if (name != null) {
            aggregator.addType(name, String.class);
        }
        return null;
    }

    public Object visit(Beyond filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Contains filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Crosses filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Disjoint filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(DWithin filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Equals filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Intersects filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Overlaps filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Touches filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    public Object visit(Within filter, Object data) {
        visitBinarySpatialOperator(filter);
        return super.visit(filter, data);
    }

    private void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        String name = getPropertyName(filter.getExpression1());
        if (name != null) {
            aggregator.addType(name, Geometry.class);
        }
        name = getPropertyName(filter.getExpression2());
        if (name != null) {
            aggregator.addType(name, Geometry.class);
        }
    }
}
