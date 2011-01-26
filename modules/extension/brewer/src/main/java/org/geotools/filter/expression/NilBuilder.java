package org.geotools.filter.expression;

import org.geotools.Builder;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.NilExpression;

public class NilBuilder implements Builder<NilExpression> {
    
    public NilExpression build() {
        return (NilExpression) Expression.NIL;
    }

    public Builder<NilExpression> reset() {
        return this;
    }

    public Builder<NilExpression> reset(NilExpression original) {
        return this;
    }

    public Builder<NilExpression> unset() {
        return this;
    }

}
