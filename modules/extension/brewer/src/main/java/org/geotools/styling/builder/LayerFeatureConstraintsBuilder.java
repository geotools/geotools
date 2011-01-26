package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.LayerFeatureConstraints;
import org.geotools.styling.StyleFactory;

public class LayerFeatureConstraintsBuilder<P> implements Builder<LayerFeatureConstraints> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>> x = new ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>>(
            this);

    private ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>> y = new ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>>(
            this);

    boolean unset = true; // current value is null

    public LayerFeatureConstraintsBuilder() {
        this(null);
    }

    public LayerFeatureConstraintsBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public LayerFeatureConstraints build() {
        if (unset) {
            return null;
        }
        FeatureTypeConstraint[] featureTypeConstraints = null;
        LayerFeatureConstraints constraints = sf
                .createLayerFeatureConstraints(featureTypeConstraints);
        return constraints;
    }

    public P end() {
        return parent;
    }

    public LayerFeatureConstraintsBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;
        return this;
    }

    public LayerFeatureConstraintsBuilder<P> reset(LayerFeatureConstraints constraints) {
        if (constraints == null) {
            return reset();
        }

        unset = false;
        return this;
    }

    public LayerFeatureConstraintsBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;
        return this;
    }

}
