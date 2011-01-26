package org.geotools.styling.builder;

import javax.swing.Icon;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ExternalMark;
import org.geotools.styling.StyleFactory;
import org.opengis.metadata.citation.OnLineResource;

public class ExternalMarkBuilder<P> implements Builder<ExternalMark> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    boolean unset = true; // current value is null
    private Icon inline;
    private String format;
    private int index;
    private OnLineResource resource;
    public ExternalMarkBuilder(){
        parent = null;
        reset();
    }
    public ExternalMarkBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public ExternalMark build() {
        if( unset ){
            return null;
        }
        if( inline != null ){
            return sf.externalMark(inline);
        }
        else {
            return sf.externalMark(resource, format, index );
        }
    }
    
    public P end(){
        return parent;
    }

    public ExternalMarkBuilder<P> reset() {
        unset = false;        
        return this;
    }

    public ExternalMarkBuilder<P> reset(org.opengis.style.ExternalMark mark) {
        if( mark == null ){
            return reset();
        }
        this.format = mark.getFormat();
        this.index = mark.getMarkIndex();
        this.inline = mark.getInlineContent();
        this.resource = mark.getOnlineResource();
        this.unset = false;
        
        return this;
    }
    public ExternalMarkBuilder<P> reset(ExternalMark mark) {
        if( mark == null ){
            return reset();
        }
        this.format = mark.getFormat();
        this.index = mark.getMarkIndex();
        this.inline = mark.getInlineContent();
        this.resource = mark.getOnlineResource();
        this.unset = false;
        
        return this;
    }

    public ExternalMarkBuilder<P> unset() {
        unset = true;        
        return this;
    }
    public boolean isUnset() {
        return unset;
    }

}
