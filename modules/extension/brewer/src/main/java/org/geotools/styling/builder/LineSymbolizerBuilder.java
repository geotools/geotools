package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;

public class LineSymbolizerBuilder<P> implements Builder<LineSymbolizer> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    P parent;
    
    StrokeBuilder<LineSymbolizerBuilder<P>> strokeBuilder = new StrokeBuilder<LineSymbolizerBuilder<P>>(this);
    String geometry = null;
    private boolean unset = false;

    public LineSymbolizerBuilder(){
        this( null );
    }
    public LineSymbolizerBuilder( P parent ){
        this.parent = parent;
        reset();
    }
    public P end() {
        return parent;
    }
    public LineSymbolizerBuilder<P> geometry(String geometry) {
        this.geometry = geometry;
        unset = false;
        return this;
    }

    public StrokeBuilder<LineSymbolizerBuilder<P>> stroke() {
        unset = false;
        return strokeBuilder;
    }

    public LineSymbolizer build() {
        if( unset ){
            return null; // builder was constructed but never used
        }
        Stroke stroke = strokeBuilder == null ? strokeBuilder.build() : Stroke.DEFAULT;
        LineSymbolizer ls = sf.createLineSymbolizer(stroke, geometry);
        reset();
        return ls;
    }

    public LineSymbolizerBuilder<P> reset() {
        strokeBuilder.reset();
        geometry = null;
        unset = false;
        return this;
    }
    
    public LineSymbolizerBuilder<P> reset( LineSymbolizer origional ){
        return this;   
    }
    
    public Builder<LineSymbolizer> unset() {
        strokeBuilder.reset();
        geometry = null;
        unset = true;
        return this;
    }
}
