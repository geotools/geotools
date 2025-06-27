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

import static org.geotools.filter.ExpressionType.ATTRIBUTE_DOUBLE;
import static org.geotools.filter.ExpressionType.ATTRIBUTE_GEOMETRY;
import static org.geotools.filter.ExpressionType.ATTRIBUTE_INTEGER;
import static org.geotools.filter.ExpressionType.ATTRIBUTE_STRING;
import static org.geotools.filter.ExpressionType.FUNCTION;
import static org.geotools.filter.ExpressionType.LITERAL_DOUBLE;
import static org.geotools.filter.ExpressionType.LITERAL_GEOMETRY;
import static org.geotools.filter.ExpressionType.LITERAL_INTEGER;
import static org.geotools.filter.ExpressionType.LITERAL_STRING;
import static org.geotools.filter.ExpressionType.MATH_ADD;
import static org.geotools.filter.ExpressionType.MATH_DIVIDE;
import static org.geotools.filter.ExpressionType.MATH_MULTIPLY;
import static org.geotools.filter.ExpressionType.MATH_SUBTRACT;

import org.geotools.filter.expression.ExpressionAbstract;

/**
 * Implements a default expression, with helpful variables and static methods.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public abstract class DefaultExpression extends ExpressionAbstract {

    /* ***********************************************************************
     * Following static methods check for certain aggregate types, based on
     * (above) declared types.  Note that these aggregate types do not
     * necessarily map directly to the sub-classes of FilterDefault.  In most,
     * but not all, cases, a single class implements an aggregate type.
     * However, there are aggregate types that are implemented by multiple
     * classes (ie. the Math type is implemented by two separate classes).
     ************************************************************************/

    /**
     * Checks to see if passed type is attribute.
     *
     * @param expressionType Type of expression for check.
     * @return Whether or not this is an attribute expression type.
     */
    protected static boolean isAttributeExpression(short expressionType) {
        return expressionType == ATTRIBUTE_DOUBLE
                || expressionType == ATTRIBUTE_INTEGER
                || expressionType == ATTRIBUTE_STRING;
    }

    /**
     * Checks to see if passed type is math.
     *
     * @param expressionType Type of expression for check.
     * @return Whether or not this is a math expression type.
     */
    protected static boolean isMathExpression(short expressionType) {
        return expressionType == MATH_ADD
                || expressionType == MATH_SUBTRACT
                || expressionType == MATH_MULTIPLY
                || expressionType == MATH_DIVIDE;
    }

    /**
     * Checks to see if passed type is geometry.
     *
     * @param expressionType Type of expression for check.
     * @return Whether or not this is a geometry expression type.
     */
    protected static boolean isLiteralExpression(short expressionType) {
        return expressionType == LITERAL_GEOMETRY
                || expressionType == LITERAL_DOUBLE
                || expressionType == LITERAL_INTEGER
                || expressionType == LITERAL_STRING;
    }

    /**
     * Checks to see if passed type is geometry.
     *
     * @param expressionType Type of expression for check.
     * @return Whether or not this is a geometry expression type.
     */
    protected static boolean isGeometryExpression(short expressionType) {
        return expressionType == ATTRIBUTE_GEOMETRY || expressionType == LITERAL_GEOMETRY;
    }

    /**
     * Checks to see if passed type is geometry.
     *
     * @param expressionType Type of expression for check.
     * @return Whether or not this is a geometry expression type.
     */
    public static boolean isExpression(short expressionType) {
        return isMathExpression(expressionType)
                || isAttributeExpression(expressionType)
                || isLiteralExpression(expressionType)
                || isFunctionExpression(expressionType);
    }

    protected static boolean isFunctionExpression(short expressionType) {
        return expressionType == FUNCTION;
    }
}
