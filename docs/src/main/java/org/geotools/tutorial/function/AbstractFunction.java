/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.tutorial.function;

import java.util.Collections;
import java.util.List;
import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public abstract class AbstractFunction implements Function {
    protected final FunctionName name;
    protected final List<Expression> params;
    protected final Literal fallback;

    protected AbstractFunction(FunctionName name, List<Expression> args, Literal fallback) {
        this.name = name;
        this.params = args;
        this.fallback = fallback;
    }

    public abstract Object evaluate(Object object);

    public <T> T evaluate(Object object, Class<T> context) {
        Object value = evaluate(object);
        return Converters.convert(value, context);
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public String getName() {
        return name.getName();
    }

    public FunctionName getFunctionName() {
        return name;
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(params);
    }

    public Literal getFallbackValue() {
        return fallback;
    }
    // helper methods
    <T> T eval(Object feature, int index, Class<T> type) {
        Expression expr = params.get(index);
        Object value = expr.evaluate(feature, type);
        return type.cast(value);
    }
}
