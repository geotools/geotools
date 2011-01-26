package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.UserLayer;

public class UserLayerBuilder<P> implements Builder<UserLayer> {
    P parent;
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private boolean unset;

    public UserLayerBuilder() {
        this( null );
    }
    public UserLayerBuilder(P parent) {
        this.parent = parent;
        reset();
    }
    public UserLayerBuilder<P> unset() {
        reset();
        unset = true;
        return this;
    }

    /**
     * Reset stroke to default values.
     */
    public UserLayerBuilder<P> reset() {
        unset = false;
        return this;
    }

    /**
     * Reset builder to provided original stroke.
     * 
     * @param stroke
     */
    public UserLayerBuilder<P> reset(UserLayer stroke) {
        unset = false;
        return this;
    }

    public UserLayer build() {
        if (unset) {
            return null;
        }
        UserLayer layer = sf.createUserLayer();
        return layer;
    }
    
    public P end(){
        return parent;
    }
}
