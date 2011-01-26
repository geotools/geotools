package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.PointPlacement;

public class LabelPlacementBuilder<P> implements Builder<LabelPlacement> {
    private P parent;

    Builder<? extends LabelPlacement> delegate;

    public LabelPlacementBuilder() {
        this(null);
    }

    public LabelPlacementBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    @SuppressWarnings("unchecked")
    public PointPlacementBuilder<P> point(){
        if( delegate instanceof PointPlacementBuilder<?>){
            return (PointPlacementBuilder<P>) delegate;
        }
        delegate = new PointPlacementBuilder<P>(parent);
        return (PointPlacementBuilder<P>) delegate;
    }
    
    @SuppressWarnings("unchecked")
    public LinePlacementBuilder<P> line(){
        if( delegate instanceof LinePlacementBuilder<?>){
            return (LinePlacementBuilder<P>) delegate;
        }
        delegate = new LinePlacementBuilder<P>(parent);
        return (LinePlacementBuilder<P>) delegate;
    }
    
    public LabelPlacement build() {
        if (delegate == null) {
            return null;
        }
        return delegate.build();
    }

    public P end() {
        return parent;
    }

    public LabelPlacementBuilder<P> reset() {
        delegate = new PointPlacementBuilder<P>().reset();
        return this;
    }

    public LabelPlacementBuilder<P> reset(LabelPlacement placement) {
        if (placement == null) {
            delegate = null;
        }
        else if (placement instanceof PointPlacement ){
            PointPlacement pointPlacement = (PointPlacement) placement;
            delegate = new PointPlacementBuilder<P>(parent).reset( pointPlacement );
        }
        else if (placement instanceof LinePlacement ){
            LinePlacement linePlacement = (LinePlacement) placement;
            delegate = new LinePlacementBuilder<P>(parent).reset( linePlacement );
        }
        return this;
    }

    public LabelPlacementBuilder<P> unset() {
        delegate = null;
        return this;
    }

}
