package org.geotools.filter.expression;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Add;

public class AddBuilder implements Builder<Add> {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    boolean unset = false;

    ChildExpressionBuilder<AddBuilder> expr1;

    ChildExpressionBuilder<AddBuilder> expr2;

    public AddBuilder() {
        reset();
    }

    public AddBuilder(Add expression) {
        reset(expression);
    }

    public AddBuilder reset() {
        unset = false;
        expr1 = new ChildExpressionBuilder<AddBuilder>(this);
        expr2 = new ChildExpressionBuilder<AddBuilder>(this);
        return this;
    }

    public AddBuilder reset(Add original) {
        unset = false;
        expr1 = new ChildExpressionBuilder<AddBuilder>(this, original.getExpression1());
        expr2 = new ChildExpressionBuilder<AddBuilder>(this, original.getExpression2());
        return this;
    }

    public AddBuilder unset() {
        unset = true;
        expr1 = new ChildExpressionBuilder<AddBuilder>(this).unset();
        expr2 = null;
        return this;
    }

    public Add build() {
        if (unset) {
            return null;
        }
        return ff.add(expr1.build(), expr2.build());
    }

    public ChildExpressionBuilder<AddBuilder> expr1() {
        return expr1;
    }

    public AddBuilder expr1(Object literal) {
        expr1.literal(literal);
        return this;
    }

    public ChildExpressionBuilder<AddBuilder> expr2() {
        return expr2;
    }

    public AddBuilder expr2(Object literal) {
        expr2.literal(literal);
        return this;
    }
}
