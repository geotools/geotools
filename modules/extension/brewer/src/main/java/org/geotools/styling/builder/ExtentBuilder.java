package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Extent;
import org.geotools.styling.StyleFactory;

public class ExtentBuilder<P> implements Builder<Extent> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    boolean unset = true; // current value is null
    private String name;
    private String value;
    
    public ExtentBuilder(){
        this(null);
    }
    
    public ExtentBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public Extent build() {
        if( unset ){
            return null;
        }
        Extent extent = sf.createExtent(name, value);
        return extent;
    }
    
    public P end(){
        return parent;
    }

    public ExtentBuilder<P> reset() {
        this.name = null;
        this.value = null;
        unset = false;        
        return this;
    }

    public ExtentBuilder<P> reset(Extent extent) {
        if( extent == null ){
            return reset();
        }
        this.value = extent.getValue();
        this.name = extent.getName();
        unset = false; 
        return this;
    }

    public ExtentBuilder<P> unset() {
        this.name = null;
        this.value = null;
        unset = true;        
        return this;
    }

}
