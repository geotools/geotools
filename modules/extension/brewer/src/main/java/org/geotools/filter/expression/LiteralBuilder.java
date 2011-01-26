package org.geotools.filter.expression;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

public class LiteralBuilder implements Builder<Literal> {
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);    
    Object literal = null; // will result in Expression.NIL
    boolean unset = false;
    
    public LiteralBuilder(){
         reset();        
    }
    public LiteralBuilder( Literal literal){
        reset( literal );        
    }
    public LiteralBuilder value( Object literal ){
        this.literal = literal;
        unset = false;
        return this;
    }
    public Literal build() {
        if( unset ){
            return null;
        }
        return ff.literal( literal );
    }

    public LiteralBuilder reset() {
        unset = false;
        literal = null;
        return this;
    }

    public LiteralBuilder reset(Literal original) {
        unset = false;
        literal = original.getValue();
        return this;
    }

    public LiteralBuilder unset() {
        unset = true;
        return this;
    }

}
