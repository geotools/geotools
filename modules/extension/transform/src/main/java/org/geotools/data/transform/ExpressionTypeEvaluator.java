/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import org.geotools.filter.function.FilterFunction_Convert;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Utility class that tries to figure out the resulting type of an expression against a given
 * feature type by using static analysis.
 *
 * @author Andrea Aime - GeoSolutions
 */
class ExpressionTypeEvaluator implements ExpressionVisitor {

    private SimpleFeatureType schema;
    private CoordinateReferenceSystem crs;

    public ExpressionTypeEvaluator(SimpleFeatureType schema) {
        this.schema = schema;
    }

    /**
     * Returns the coordinate reference system of the last encontered geometry property. Unless a
     * filter function that reprojects geometries is used, that's also the crs of the eventual
     * output, in case it's a Geometry, that is.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return this.crs;
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        return null;
    }

    @Override
    public Object visit(Function f, Object extraData) {
        FunctionName fn = f.getFunctionName();
        if (fn != null && fn.getReturn() != null && fn.getReturn().getType() != Object.class) {
            return fn.getReturn().getType();
        } else if (f instanceof FilterFunction_Convert) {
            // special case for the convert function, which has the return type as
            // a parameter
            return f.getParameters().get(1).evaluate(null, Class.class);
        }

        return null;
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        if (expression.getValue() == null) {
            return null;
        } else {
            return expression.getValue().getClass();
        }
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        AttributeDescriptor result = expression.evaluate(schema, AttributeDescriptor.class);
        if (result == null) {
            throw new IllegalArgumentException(
                    "Original feature type does not have a property named "
                            + expression.getPropertyName());
        }

        if (result instanceof GeometryDescriptor) {
            this.crs = ((GeometryDescriptor) result).getCoordinateReferenceSystem();
        }
        return result.getType().getBinding();
    }

    private Object visitMathExpression(BinaryExpression expression) {
        Expression ex1 = expression.getExpression1();
        Expression ex2 = expression.getExpression2();

        Class c1 = getMathOperandType(ex1);
        Class c2 = getMathOperandType(ex2);

        if (c1 == Integer.class && c2 == Integer.class) {
            return Integer.class;
        } else if ((c1 == Integer.class || c1 == Long.class)
                && (c2 == Integer.class || c2 == Long.class)) {
            return Long.class;
        } else {
            return Double.class;
        }
    }

    private Class getMathOperandType(Expression expression) {
        Class result = (Class) expression.accept(this, null);

        // not a number, if a literal see if its contents can be cast to one
        if (!(Number.class.isAssignableFrom(result)) && expression instanceof Literal) {
            Double value = expression.evaluate(null, Double.class);
            if (value != null) {
                if (value.longValue() == value.doubleValue()) {
                    // integer type, keep it simple, int or long
                    if ((value < Integer.MAX_VALUE) && value > Integer.MIN_VALUE) {
                        result = Integer.class;
                    } else {
                        result = Long.class;
                    }
                }
            } else {
                result = Double.class;
            }
        }

        return result;
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        return visitMathExpression(expression);
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        return visitMathExpression(expression);
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        return visitMathExpression(expression);
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        return visitMathExpression(expression);
    }
}
