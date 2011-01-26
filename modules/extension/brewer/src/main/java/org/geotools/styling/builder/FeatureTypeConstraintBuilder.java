package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.SubFilterBuilder;
import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.StyleFactory;

public class FeatureTypeConstraintBuilder<P> implements Builder<FeatureTypeConstraint> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private Extent[] extents;

    private SubFilterBuilder<FeatureTypeConstraintBuilder<P>> filter = new SubFilterBuilder<FeatureTypeConstraintBuilder<P>>(
            this);

    private String featureTypeName;

    public FeatureTypeConstraintBuilder() {
        this(null);
    }

    public FeatureTypeConstraintBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public FeatureTypeConstraint build() {
        if (unset) {
            return null;
        }
        FeatureTypeConstraint constraint = sf.createFeatureTypeConstraint(featureTypeName, filter
                .build(), extents);
        return constraint;
    }

    public P end() {
        return parent;
    }

    public FeatureTypeConstraintBuilder<P> reset() {
        unset = false;
        return this;
    }

    public FeatureTypeConstraintBuilder<P> reset(FeatureTypeConstraint constraint) {
        if (constraint == null) {
            return reset();
        }
        unset = false;
        return this;
    }

    public FeatureTypeConstraintBuilder<P> unset() {
        unset = true;
        return this;
    }

}
