package org.geotools.filter.expression;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Subtract;

public class SubtractBuilder implements Builder<Subtract> {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    boolean unset = false;

    ChildExpressionBuilder<SubtractBuilder> expr1;

    ChildExpressionBuilder<SubtractBuilder> expr2;

    public SubtractBuilder() {
        reset();
    }

    public SubtractBuilder(Subtract expression) {
        reset(expression);
    }

    public SubtractBuilder reset() {
        unset = false;
        expr1 = new ChildExpressionBuilder<SubtractBuilder>(this);
        expr2 = new ChildExpressionBuilder<SubtractBuilder>(this);
        return this;
    }

    public SubtractBuilder reset(Subtract original) {
        unset = false;
        expr1 = new ChildExpressionBuilder<SubtractBuilder>(this, original.getExpression1());
        expr2 = new ChildExpressionBuilder<SubtractBuilder>(this, original.getExpression2());
        return this;
    }

    public SubtractBuilder unset() {
        unset = true;
        expr1 = new ChildExpressionBuilder<SubtractBuilder>(this).unset();
        expr2 = null;
        return this;
    }

    public Subtract build() {
        if (unset) {
            return null;
        }
        return ff.subtract(expr1.build(), expr2.build());
    }

    public ChildExpressionBuilder<SubtractBuilder> expr1() {
        return expr1();
    }

    public SubtractBuilder expr1(Object literal) {
        expr1.literal(literal);
        return this;
    }

    public ChildExpressionBuilder<SubtractBuilder> expr2() {
        return expr2;
    }

    public SubtractBuilder expr2(Object literal) {
        expr2.literal(literal);
        return this;
    }
}