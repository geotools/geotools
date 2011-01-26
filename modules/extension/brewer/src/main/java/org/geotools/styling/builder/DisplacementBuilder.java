package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.Displacement;
import org.geotools.styling.StyleFactory;

public class DisplacementBuilder<P> implements Builder<Displacement> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<DisplacementBuilder<P>> x = new ChildExpressionBuilder<DisplacementBuilder<P>>(
            this);

    private ChildExpressionBuilder<DisplacementBuilder<P>> y = new ChildExpressionBuilder<DisplacementBuilder<P>>(
            this);

    boolean unset = true; // current value is null

    public DisplacementBuilder() {
        parent = null;
        reset();
    }

    public DisplacementBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public Displacement build() {
        if (unset) {
            return null;
        }
        Displacement displacement = sf.displacement(x.build(), y.build());
        return displacement;
    }

    public P end() {
        return parent;
    }

    public DisplacementBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;
        return this;
    }

    public DisplacementBuilder<P> reset(Displacement displacement) {
        if (displacement == null) {
            return reset();
        }
        x.reset().literal(displacement.getDisplacementX());
        y.reset().literal(displacement.getDisplacementY());
        unset = false;
        return this;
    }

    public DisplacementBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;
        return this;
    }

    public DisplacementBuilder<P> reset(org.opengis.style.Displacement displacement) {
        if (displacement == null) {
            return reset();
        }
        x.reset().literal(displacement.getDisplacementX());
        y.reset().literal(displacement.getDisplacementY());
        unset = false;
        return this;
    }

}
