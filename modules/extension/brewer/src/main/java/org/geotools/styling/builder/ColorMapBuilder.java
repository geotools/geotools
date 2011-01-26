package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.StyleFactory;

public class ColorMapBuilder<P> implements Builder<AnchorPoint> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    private ChildExpressionBuilder<ColorMapBuilder<P>> x = new ChildExpressionBuilder<ColorMapBuilder<P>>(this);
    private ChildExpressionBuilder<ColorMapBuilder<P>> y = new ChildExpressionBuilder<ColorMapBuilder<P>>(this);
    boolean unset = true; // current value is null
    public ColorMapBuilder(){
        parent = null;
        reset();
    }
    public ColorMapBuilder(AnchorPoint anchorPoint){
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

    public ColorMapBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;        
        return this;
    }

    public ColorMapBuilder<P> reset(AnchorPoint original) {
        if( original == null ){
            return reset();
        }
        x.reset().literal(original.getAnchorPointX());
        y.reset().literal(original.getAnchorPointY());
        unset = false; 
        return this;
    }

    public ColorMapBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;        
        return this;
    }

}
