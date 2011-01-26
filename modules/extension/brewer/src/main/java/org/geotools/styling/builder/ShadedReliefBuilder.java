package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.StyleFactory;

public class ShadedReliefBuilder<P> implements Builder<ShadedRelief> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    private ChildExpressionBuilder<ShadedReliefBuilder<P>> reliefFactor = new ChildExpressionBuilder<ShadedReliefBuilder<P>>(this);
    private boolean brightnessOnly;

    boolean unset = true; // current value is null
    public ShadedReliefBuilder(){
        this( null );
    }
    public ShadedReliefBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public ShadedRelief build() {
        if( unset ){
            return null;
        }
        ShadedRelief relief = sf.shadedRelief(reliefFactor.build(), brightnessOnly);
        return relief;
    }
    
    public P end(){
        return parent;
    }

    public ShadedReliefBuilder<P> reset() {
        reliefFactor.reset().literal(0);
        brightnessOnly=false;
        unset = false;        
        return this;
    }

    public ShadedReliefBuilder<P> reset(ShadedRelief relief) {
        if( relief == null ){
            return reset();
        }
        unset = false; 
        return this;
    }

    public ShadedReliefBuilder<P> unset() {
        reliefFactor.unset();
        brightnessOnly=false;
        unset = true;        
        return this;
    }

}
