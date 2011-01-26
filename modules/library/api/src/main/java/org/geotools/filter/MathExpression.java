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

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.BinaryExpression;


/**
 * Holds a mathematical relationship between two expressions. Note that the sub
 * expressions must be math expressions.  In other words, they must be a math
 * literal, another math expression, or a feature attribute with a declared
 * math type.  You may create math expressions of arbitrary complexity by
 * nesting other math expressions as sub expressions in one or more math
 * expressions. This filter defines left and right values to clarify the sub
 * expression precedence for non-associative operations, such as subtraction
 * and division. For example, the left value is the numerator and the right is
 * the denominator in an ExpressionMath division operation.
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.expression.BinaryExpression}
 */
public interface MathExpression extends Expression, BinaryExpression {
    /**
     * Returns the value for this expression.
     *
     * @param feature Feature to use when return sub expression values.
     *
     * @return Value of this expression.
     *
     * @deprecated use {@link Expression#evaluate(Feature)}.
     *
     */
    Object getValue(SimpleFeature feature);

    /**
     * Adds the 'right' value to this expression.
     *
     * @param rightValue Expression to add to this expression.
     *
     * @throws IllegalFilterException Attempting to add non-math expression.
     *
     * @deprecated use {@link BinaryExpression#setExpression2(Expression)}
     */
    void addRightValue(Expression rightValue) throws IllegalFilterException;

    /**
     * Gets the type of this expression.
     *
     * @return Expression type.
     * @deprecated The expression type system has been replaced by an actual
     * class type system.
     */
    short getType();

    /**
     * Gets the left expression.
     *
     * @return the expression on the left of the comparison.
     * @deprecated use {@link BinaryExpression#getExpression1()}.
     */
    Expression getLeftValue();

    /**
     * Gets the right expression.
     *
     * @return the expression on the right of the comparison.
     * @deprecated use {@link BinaryExpression#getExpression2()}.
     */
    Expression getRightValue();

    /**
     * Adds the 'left' value to this expression.
     *
     * @param leftValue Expression to add to this expression.
     *
     * @throws IllegalFilterException Attempting to add non-math expression.
     *
     * @deprecated use {@link BinaryExpression#setExpression1(Expression)}
     */
    void addLeftValue(Expression leftValue) throws IllegalFilterException;
}
