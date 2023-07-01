/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.function;

import java.util.Collections;
import java.util.List;
import org.geotools.util.Converters;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;

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
