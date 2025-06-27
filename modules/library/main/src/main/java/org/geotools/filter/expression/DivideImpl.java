/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import java.util.Collection;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.filter.Filters;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.util.Utilities;

/**
 * Implementation of divide expression.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class DivideImpl extends MathExpressionImpl implements Divide {

    public DivideImpl(Expression expr1, Expression expr2) {
        super(expr1, expr2);
    }

    @Override
    public Object evaluate(Object feature) throws IllegalArgumentException {
        ensureOperandsSet();

        Object eval1 = getExpression1().evaluate(feature);
        Object eval2 = getExpression2().evaluate(feature);
        if (eval1 instanceof Collection || eval2 instanceof Collection) {
            return handleCollection(eval1, eval2);
        } else {
            double leftDouble = Filters.number(getExpression1().evaluate(feature, Number.class));
            double rightDouble = Filters.number(getExpression2().evaluate(feature, Number.class));

            return doArithmeticOperation(leftDouble, rightDouble);
        }
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    /**
     * Compares this expression to the specified object. Returns true if the
     *
     * @param obj - the object to compare this expression against.
     * @return true if specified object is equal to this expression; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DivideImpl) {
            DivideImpl other = (DivideImpl) obj;

            return Utilities.equals(getExpression1(), other.getExpression1())
                    && Utilities.equals(getExpression2(), other.getExpression2());
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a hash code value for this divide expression.
     */
    @Override
    public int hashCode() {
        int result = 23;

        result = 37 * result + getExpression1().hashCode();
        result = 37 * result + getExpression2().hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "(" + getExpression1().toString() + "/" + getExpression2().toString() + ")";
    }

    @Override
    protected Object doArithmeticOperation(Double operand1, Double operand2) {
        return number(operand1 / operand2);
    }
}
