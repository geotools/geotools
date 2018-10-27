/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.geotools.brewer.styling.builder.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

public class FunctionBuilder implements Builder<Function> {
    public class ParamBuilder extends ChildExpressionBuilder<FunctionBuilder> {
        int index;

        ParamBuilder(int index) {
            super(FunctionBuilder.this, index < args.size() ? args.get(index) : null);
            this.index = index;
        }

        public Expression build() {
            return put(index, super.build());
        }

        public ParamBuilder param() {
            build();
            return new ParamBuilder(index + 1);
        }

        public FunctionBuilder function() {
            this.delegate = new FunctionBuilder(this);
            unset = false;
            return (FunctionBuilder) delegate;
        }

        public FunctionBuilder function(String name) {
            this.delegate = new FunctionBuilder(this).name(name);
            unset = false;
            return (FunctionBuilder) delegate;
        }
    }

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    LiteralBuilder literal = new LiteralBuilder();
    boolean unset = false;
    private String name = null; // ie unset!
    List<Expression> args = new ArrayList<Expression>();
    ParamBuilder parent;

    public FunctionBuilder() {
        reset();
    }

    public FunctionBuilder(ParamBuilder parent) {
        reset();
        this.parent = parent;
    }

    public FunctionBuilder(Function origional) {
        reset(origional);
    }

    Expression put(int index, Expression expr) {
        if (index < args.size()) {
            args.set(index, expr);
        } else if (index == args.size()) {
            args.add(expr);
        } else {
            // fine we will just add to the end?
            while (args.size() < index) {
                args.add(null); // placeholders so we can add at the correct index
            }
            args.add(expr); // add at the correct index
        }
        return expr;
    }

    public ParamBuilder param() {
        return param(args.size());
    }

    public FunctionBuilder literal(Object literal) {
        put(args.size(), ff.literal(literal));
        return this;
    }

    public FunctionBuilder property(String xpath) {
        put(args.size(), ff.property(xpath));
        return this;
    }

    public FunctionBuilder function() {
        return param(args.size()).function();
    }

    public FunctionBuilder function(String name) {
        return param(args.size()).function(name);
    }

    public ParamBuilder param(int index) {
        return new ParamBuilder(index);
    }

    public FunctionBuilder name(String function) {
        this.name = function;
        return this;
    }
    /** Inline fallback value to use if named function is not implemented */
    public FunctionBuilder fallback(Object obj) {
        literal.value(obj);
        return this;
    }
    /** Literal fallback value to use if named function is not implemented */
    public LiteralBuilder fallback() {
        return literal;
    }

    public Function build() {
        if (name == null) {
            return null; // unset!
        }
        Expression[] arguments = new Expression[args.size()];
        for (int index = 0; index < args.size(); index++) {
            arguments[index] = args.get(index);
        }
        return ff.function(name, arguments);
    }

    public FunctionBuilder reset() {
        name = null;
        args.clear();
        literal.unset();
        return this;
    }

    public FunctionBuilder reset(Function original) {
        name = original.getName();
        args.clear();
        args.addAll(original.getParameters());
        literal.reset(original.getFallbackValue());
        return this;
    }

    public FunctionBuilder unset() {
        name = null;
        args.clear();
        literal.unset();
        return this;
    }

    public FunctionBuilder end() {
        if (parent == null) {
            throw new IllegalArgumentException("No parent set");
        }
        return parent.end();
    }
}
