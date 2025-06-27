/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.api.filter.expression.BinaryExpression;
import org.geotools.util.Converters;

/**
 * Holds a mathematical relationship between two expressions. Note that the sub expressions must be math expressions. In
 * other words, they must be a math literal, another math expression, or a feature attribute with a declared math type.
 * You may create math expressions of arbitrary complexity by nesting other math expressions as sub expressions in one
 * or more math expressions. This filter defines left and right values to clarify the sub expression precedence for
 * non-associative operations, such as subtraction and division. For example, the left value is the numerator and the
 * right is the denominator in an ExpressionMath division operation.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public abstract class MathExpressionImpl extends DefaultExpression implements BinaryExpression {

    /** Holds the 'left' value of this math expression. */
    private org.geotools.api.filter.expression.Expression leftValue = null;

    /** Holds the 'right' value of this math expression. */
    private org.geotools.api.filter.expression.Expression rightValue = null;

    /** No argument constructor. */
    protected MathExpressionImpl() {}

    protected MathExpressionImpl(
            org.geotools.api.filter.expression.Expression e1, org.geotools.api.filter.expression.Expression e2) {
        this.leftValue = e1;
        this.rightValue = e2;
    }

    /**
     * Gets the left or first expression.
     *
     * @return the expression on the first side of the comparison.
     */
    @Override
    public org.geotools.api.filter.expression.Expression getExpression1() {
        return leftValue;
    }

    /** Gets the left or first expression. */
    public void setExpression1(org.geotools.api.filter.expression.Expression expression) {
        if (isGeometryExpression(Filters.getExpressionType(expression))) {
            throw new IllegalFilterException("Attempted to add Geometry expression to math expression.");
        }
        this.leftValue = expression;
    }

    /**
     * Gets the second expression.
     *
     * @return the expression on the second side of the comparison.
     */
    @Override
    public org.geotools.api.filter.expression.Expression getExpression2() {
        return rightValue;
    }

    /** Gets the second expression. */
    public void setExpression2(org.geotools.api.filter.expression.Expression expression) {
        // Check to see if this is a valid math expression before adding.
        if (isGeometryExpression(Filters.getExpressionType(expression))) {
            throw new IllegalFilterException("Attempted to add Geometry expression to math expression.");
        }
        this.rightValue = expression;
    }

    /**
     * Convenience method which ensures that both expressions have been set. If any of operands not set an exception is
     * thrown.
     */
    protected void ensureOperandsSet() throws IllegalArgumentException {
        // Checks to make sure both sub expressions exist.
        if (leftValue == null || rightValue == null) {
            throw new IllegalArgumentException("Attempted read math expression with missing sub expressions.");
        }
    }

    protected Object number(double number) {
        // return Filters.puts( number );  // non strongly typed
        return Double.valueOf(number); // Getools 2.1 style
    }

    protected Object handleCollection(Object value1, Object value2) {
        List<Number> numericList;
        final Number scalar;
        if (value1 instanceof Collection && value2 instanceof Collection) {
            throw new RuntimeException("Both Maths expressions cannot be Collections");
        } else if (value1 instanceof Collection) {
            // first is collection, second is scalar
            @SuppressWarnings("unchecked")
            Collection<Object> c1 = (Collection<Object>) value1;
            numericList =
                    c1.stream().map(v -> Converters.convert(v, Number.class)).collect(Collectors.toList());
            scalar = Filters.number(value2);

        } else {
            // second is collection, first is scalar
            @SuppressWarnings("unchecked")
            Collection<Object> unc = (Collection<Object>) value2;
            numericList =
                    unc.stream().map(v -> Converters.convert(v, Number.class)).collect(Collectors.toList());
            scalar = Filters.number(value1);
        }

        return numericList.stream()
                .map(n -> doArithmeticOperation(n.doubleValue(), scalar.doubleValue()))
                .collect(Collectors.toList());
    }

    protected abstract Object doArithmeticOperation(Double operand1, Double operand2);
}
