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

package org.geotools.brewer.styling.filter.expression;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.brewer.styling.builder.Builder;
import org.geotools.factory.CommonFactoryFinder;

/** ExpressionBuilder acting as a simple wrapper around an Expression. */
public class ExpressionBuilder implements Builder<Expression> {
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    protected boolean unset = false;
    protected Builder<? extends Expression> delegate = new NilBuilder();

    public ExpressionBuilder() {
        reset();
    }

    public ExpressionBuilder(Expression expr) {
        reset(expr);
    }

    /**
     * Define expression as a literal.
     *
     * <p>Example:<code>b.literal().value( 1 );</code>
     */
    public LiteralBuilder literal() {
        delegate = new LiteralBuilder();
        unset = false;
        return (LiteralBuilder) delegate;
    }

    public Builder<?> literal(Object literal) {
        delegate = new LiteralBuilder().value(literal);
        unset = false;
        return this;
    }

    public AddBuilder add() {
        delegate = new AddBuilder();
        unset = false;
        return (AddBuilder) delegate;
    }

    public MultiplyBuilder multiply() {
        delegate = new MultiplyBuilder();
        unset = false;
        return (MultiplyBuilder) delegate;
    }

    public DivideBuilder divide() {
        delegate = new DivideBuilder();
        unset = false;
        return (DivideBuilder) delegate;
    }

    public SubtractBuilder subtract() {
        delegate = new SubtractBuilder();
        unset = false;
        return (SubtractBuilder) delegate;
    }

    public PropertyNameBuilder property() {
        delegate = new PropertyNameBuilder();
        unset = false;
        return (PropertyNameBuilder) delegate;
    }

    public Builder<?> property(String xpath) {
        delegate = new PropertyNameBuilder().property(xpath);
        unset = false;
        return this;
    }

    public FunctionBuilder function() {
        this.delegate = new FunctionBuilder();
        unset = false;
        return (FunctionBuilder) delegate;
    }

    public FunctionBuilder function(String name) {
        this.delegate = new FunctionBuilder().name(name);
        unset = false;
        return (FunctionBuilder) delegate;
    }

    /** Build the expression. */
    @Override
    public Expression build() {
        if (unset) {
            return null;
        }
        return delegate.build();
    }

    @Override
    public ExpressionBuilder reset() {
        this.delegate = new NilBuilder();
        this.unset = false;
        return this;
    }

    @Override
    public ExpressionBuilder reset(Expression original) {
        if (original == null) {
            return unset();
        }
        this.unset = false;
        if (original instanceof Literal literal) {
            delegate = new LiteralBuilder(literal);
        } else if (original instanceof PropertyName name) {
            delegate = new PropertyNameBuilder(name);
        } else if (original instanceof Function function) {
            delegate = new FunctionBuilder(function);
        } else if (original instanceof Add add) {
            delegate = new AddBuilder(add);
        } else if (original instanceof Divide divide) {
            delegate = new DivideBuilder(divide);
        } else if (original instanceof Multiply multiply) {
            delegate = new MultiplyBuilder(multiply);
        } else if (original instanceof Subtract subtract) {
            delegate = new SubtractBuilder(subtract);
        } else {
            this.delegate = new NilBuilder();
        }
        return this;
    }

    @Override
    public ExpressionBuilder unset() {
        this.unset = true;
        this.delegate = new NilBuilder();
        return this;
    }

    public boolean isUnset() {
        return unset;
    }
}
