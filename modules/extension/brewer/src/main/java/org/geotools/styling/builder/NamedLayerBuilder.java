package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.StyleFactory;

public class NamedLayerBuilder<P> implements Builder<NamedLayer> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private LinkedHashSet<FeatureTypeConstraintBuilder<NamedLayerBuilder<P>>> constraints = new LinkedHashSet<FeatureTypeConstraintBuilder<NamedLayerBuilder<P>>>();

    private String name;

    public NamedLayerBuilder() {
        this(null);
    }

    public NamedLayerBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    @SuppressWarnings("unchecked")
    public NamedLayer build() {
        if (unset) {
            return null;
        }
        NamedLayer layer = sf.createNamedLayer();
        layer.setName(name);
        List<FeatureTypeConstraint> list = new ArrayList<FeatureTypeConstraint>();
        for( FeatureTypeConstraintBuilder<NamedLayerBuilder<P>> constraint : constraints ){
            list.add( constraint.build() );
        }
        layer.layerFeatureConstraints().addAll(list);
        
        if( parent == null ) reset();
        return layer;
    }

    public P end() {
        return parent;
    }

    public NamedLayerBuilder<P> reset() {
        unset = false;
        constraints.clear();
        return this;
    }

    public NamedLayerBuilder<P> reset(NamedLayer layer) {
        if (layer == null) {
            return reset();
        }
        this.name = layer.getName();
        constraints.clear();
        if( layer.layerFeatureConstraints() != null ){
            for( FeatureTypeConstraint featureConstraint : layer.layerFeatureConstraints() ){
                constraints.add( new FeatureTypeConstraintBuilder(this).reset( featureConstraint));
            }
        }
        unset = false;
        return this;
    }

    public NamedLayerBuilder<P> unset() {
        unset = true;
        return this;
    }

}
