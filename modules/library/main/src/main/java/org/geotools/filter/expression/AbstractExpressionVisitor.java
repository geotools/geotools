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

import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;

/**
 * Empty "abstract" implementation of ExpressionVisitor. Subclasses should override desired methods.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class AbstractExpressionVisitor implements ExpressionVisitor {

    @Override
    public Object visit(NilExpression expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(Add expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(Divide expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(Function expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(Literal expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(Multiply expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(PropertyName expr, Object extraData) {
        return expr;
    }

    @Override
    public Object visit(Subtract expr, Object extraData) {
        return expr;
    }
}
