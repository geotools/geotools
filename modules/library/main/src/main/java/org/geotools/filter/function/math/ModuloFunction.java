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
package org.geotools.filter.function.math;

import java.util.Collections;
import java.util.List;

import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Implements the Knuth floored division modulo_operation
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Modulo_operation#Remainder_calculation_for_the_modulo_operation">Modulo_operation</a>
 */
public class ModuloFunction implements Function {

    static FunctionName NAME = new FunctionNameImpl(
        "modulo",
        Integer.class,
        FunctionNameImpl.parameter("dividend", Integer.class),
        FunctionNameImpl.parameter("divisor", Integer.class)
    );

    private final FunctionName functionName;

    private final List<Expression> parameters;

    private final Literal fallback;

    public ModuloFunction() {
        this.functionName = NAME;
        this.parameters = Collections.emptyList();
        this.fallback = null;
    }

    public ModuloFunction(List<Expression> parameters, Literal fallback) {
        if (parameters == null) {
            throw new NullPointerException("parameters must be provided");
        }

        if (parameters.size() != NAME.getArguments().size()) {
            throw new IllegalArgumentException(NAME.getArguments().size() + " function parameters are required");
        }

        this.functionName = NAME;
        this.parameters = parameters;
        this.fallback = fallback;
    }

    public Object evaluate(Object object) {
        return evaluate(object, functionName.getReturn().getType());
    }

    public <T> T evaluate(Object object, Class<T> context) {
        Expression dividendExpression = parameters.get(0);
        int dividend = dividendExpression.evaluate(object, Integer.class);

        Expression divisorExpression = parameters.get(1);
        int divisor = divisorExpression.evaluate(object, Integer.class);

        if (divisor == 0) {
            throw new IllegalArgumentException("divisor cannot be 0");
        }

        int modulo = dividend - divisor * (int) Math.floor((double) dividend / divisor);

        return Converters.convert(modulo, context);
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public String getName() {
        return functionName.getName();
    }

    public FunctionName getFunctionName() {
        return functionName;
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Literal getFallbackValue() {
        return fallback;
    }

}
