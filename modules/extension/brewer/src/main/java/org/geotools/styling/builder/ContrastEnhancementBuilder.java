package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.StyleFactory;
import org.opengis.style.ContrastMethod;

public class ContrastEnhancementBuilder<P> implements Builder<ContrastEnhancement> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    boolean unset = true; // current value is null
    private ChildExpressionBuilder<ContrastEnhancementBuilder<P>> gamma = new ChildExpressionBuilder<ContrastEnhancementBuilder<P>>(this);
    private ContrastMethod method;
    public ContrastEnhancementBuilder(){
        this(null);
    }
    public ContrastEnhancementBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public ContrastEnhancement build() {
        if( unset ){
            return null;
        }
        ContrastEnhancement contrastEnhancement = sf.contrastEnhancement(gamma.build(), method);
        return contrastEnhancement;
    }
    
    public P end(){
        return parent;
    }

    public ContrastEnhancementBuilder<P> reset() {
        gamma.reset();
        unset = false;        
        return this;
    }

    public ContrastEnhancementBuilder<P> reset(ContrastEnhancement contrastEnhancement) {
        if( contrastEnhancement == null ){
            return reset();
        }
        gamma.reset( contrastEnhancement.getGammaValue() );
        method = contrastEnhancement.getMethod();
        unset = false; 
        return this;
    }

    public ContrastEnhancementBuilder<P> unset() {
        gamma.unset();
        method = ContrastMethod.NONE;
        unset = true;        
        return this;
    }

}
