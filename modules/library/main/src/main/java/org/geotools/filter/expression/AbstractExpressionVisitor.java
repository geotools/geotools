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
package org.geotools.filter.expression;

import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * Empty "abstract" implementation of ExpressionVisitor. Subclasses should
 * override desired methods.
 * 
 * @author Cory Horner, Refractions Research Inc.
 *
 *
 * @source $URL$
 */
public class AbstractExpressionVisitor implements ExpressionVisitor {

    public Object visit(NilExpression expr, Object extraData) {
        return expr;
    }

    public Object visit(Add expr, Object extraData) {
        return expr;
    }

    public Object visit(Divide expr, Object extraData) {
        return expr;
    }

    public Object visit(Function expr, Object extraData) {
        return expr;
    }

    public Object visit(Literal expr, Object extraData) {
        return expr;
    }

    public Object visit(Multiply expr, Object extraData) {
        return expr;
    }

    public Object visit(PropertyName expr, Object extraData) {
        return expr;
    }

    public Object visit(Subtract expr, Object extraData) {
        return expr;
    }

}
