package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.UserLayer;

public class StyledLayerBuilder<P> implements Builder<StyledLayer> {
    private P parent;
    protected Builder<? extends StyledLayer> delegate;
    public StyledLayerBuilder(){
        this( null );
    }
    public StyledLayerBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public StyledLayer build() {
        if( delegate == null ){
            return null;
        }
        StyledLayer styledLayer = delegate.build();
        if( parent == null ) reset();
        
        return styledLayer;
    }

    @SuppressWarnings("unchecked")
    UserLayerBuilder<P> user(){
        if( delegate == null || !(delegate instanceof UserLayerBuilder<?>)){
            delegate = new UserLayerBuilder<P>(parent);
        }
        return (UserLayerBuilder<P>) delegate;
    }
    @SuppressWarnings("unchecked")
    NamedLayerBuilder<P> named(){
        if( delegate == null || !(delegate instanceof NamedLayerBuilder<?>)){
            delegate = new NamedLayerBuilder<P>(parent);
        }
        return (NamedLayerBuilder<P>) delegate;
    }
    public P end(){
        return parent;
    }

    public StyledLayerBuilder<P> reset() {
        delegate = new NamedLayerBuilder<P>(parent);      
        return this;
    }

    public StyledLayerBuilder<P> unset() {
        delegate = null;
        return this;
    }
    public StyledLayerBuilder<P> reset(StyledLayer layer) {
        if( layer == null ){
            return reset();
        }
        if( layer instanceof NamedLayer ){
            delegate = new NamedLayerBuilder<P>().reset( (NamedLayer) layer );
        }
        else if (layer instanceof UserLayer){
            delegate = new UserLayerBuilder<P>(parent).reset( (UserLayer) layer );
        }
        return this;
    }

}
