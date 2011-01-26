package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.StyleFactory;
import org.opengis.style.Displacement;

public class PointPlacementBuilder<P> implements Builder<PointPlacement> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    private ChildExpressionBuilder<PointPlacementBuilder<P>> rotation = new ChildExpressionBuilder<PointPlacementBuilder<P>>(this);
    boolean unset = true; // current value is null
    private org.opengis.style.AnchorPoint anchor;
    private Displacement displacement;
    public PointPlacementBuilder(){
        parent = null;
        reset();
    }
    public PointPlacementBuilder(P parent){
        this.parent = null;
        reset();
    }
    
    public PointPlacement build() {
        if( unset ){
            return null;
        }
        PointPlacement placement = sf.pointPlacement(anchor, displacement, rotation.build());
        return placement;
    }
    
    public P end(){
        return parent;
    }

    public PointPlacementBuilder<P> reset() {
        rotation.reset();
        unset = false;        
        return this;
    }

    public PointPlacementBuilder<P> reset(PointPlacement placement) {
        if( placement == null ){
            return reset();
        }
        rotation.reset( placement.getRotation() );
        unset = false; 
        return this;
    }

    public PointPlacementBuilder<P> unset() {
        rotation.unset();
        unset = true;        
        return this;
    }

}
