/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009 - 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.filter.expression;

import org.geotools.brewer.styling.builder.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Multiply;

public class MultiplyBuilder implements Builder<Multiply> {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    boolean unset = false;

    ChildExpressionBuilder<MultiplyBuilder> expr1;

    ChildExpressionBuilder<MultiplyBuilder> expr2;

    public MultiplyBuilder() {
        reset();
    }

    public MultiplyBuilder(Multiply expression) {
        reset(expression);
    }

    public MultiplyBuilder reset() {
        unset = false;
        expr1 = new ChildExpressionBuilder<MultiplyBuilder>(this);
        expr2 = new ChildExpressionBuilder<MultiplyBuilder>(this);
        return this;
    }

    public MultiplyBuilder reset(Multiply original) {
        unset = false;
        expr1 = new ChildExpressionBuilder<MultiplyBuilder>(this, original.getExpression1());
        expr2 = new ChildExpressionBuilder<MultiplyBuilder>(this, original.getExpression2());
        return this;
    }

    public MultiplyBuilder unset() {
        unset = true;
        expr1 = new ChildExpressionBuilder<MultiplyBuilder>(this).unset();
        expr2 = null;
        return this;
    }

    public Multiply build() {
        if (unset) {
            return null;
        }
        return ff.multiply(expr1.build(), expr2.build());
    }

    public ChildExpressionBuilder<MultiplyBuilder> expr1() {
        return expr1();
    }

    public MultiplyBuilder expr1(Object literal) {
        expr1.literal(literal);
        return this;
    }

    public ChildExpressionBuilder<MultiplyBuilder> expr2() {
        return expr2;
    }

    public MultiplyBuilder expr2(Object literal) {
        expr2.literal(literal);
        return this;
    }
}
