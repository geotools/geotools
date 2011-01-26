package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.StyleFactory;

public class ChannelSelectionBuilder<P> implements Builder<AnchorPoint> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    private ChildExpressionBuilder<ChannelSelectionBuilder<P>> x = new ChildExpressionBuilder<ChannelSelectionBuilder<P>>(this);
    private ChildExpressionBuilder<ChannelSelectionBuilder<P>> y = new ChildExpressionBuilder<ChannelSelectionBuilder<P>>(this);
    boolean unset = true; // current value is null
    public ChannelSelectionBuilder(){
        parent = null;
        reset();
    }
    public ChannelSelectionBuilder(AnchorPoint anchorPoint){
        parent = null;
        reset();
    }
    
    public AnchorPoint build() {
        if( unset ){
            return null;
        }
        AnchorPoint anchorPoint = sf.anchorPoint(x.build(), y.build());
        return anchorPoint;
    }
    
    public P end(){
        return parent;
    }

    public ChannelSelectionBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;        
        return this;
    }

    public ChannelSelectionBuilder<P> reset(AnchorPoint original) {
        if( original == null ){
            return reset();
        }
        x.reset().literal(original.getAnchorPointX());
        y.reset().literal(original.getAnchorPointY());
        unset = false; 
        return this;
    }

    public ChannelSelectionBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;        
        return this;
    }

}
