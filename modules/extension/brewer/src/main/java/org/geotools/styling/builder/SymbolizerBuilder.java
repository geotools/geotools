package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ExtensionSymbolizer;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

public class SymbolizerBuilder<P> implements Builder<Symbolizer> {    
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    P parent;
    
    Builder<? extends Symbolizer> delegate = null; // initially unset
    
    public SymbolizerBuilder() {
        this( null );
    }
    public SymbolizerBuilder(P parent ){
        this.parent = parent;
        reset();
    }

    public PointSymbolizerBuilder<P> point(){
        if( delegate == null || !(delegate instanceof PointSymbolizerBuilder)){
            delegate = new PointSymbolizerBuilder<P>(parent);
        }
        return (PointSymbolizerBuilder<P>) delegate;        
    }
    
    public LineSymbolizerBuilder<P> line(){
        if( delegate == null || !(delegate instanceof LineSymbolizerBuilder)){
            delegate = new LineSymbolizerBuilder<P>(parent);
        }
        return (LineSymbolizerBuilder<P>) delegate;        
    }
    
    public PolygonSymbolizerBuilder<P> polygon(){
        if( delegate == null || !(delegate instanceof PolygonSymbolizerBuilder)){
            delegate = new PolygonSymbolizerBuilder<P>(parent);
        }
        return (PolygonSymbolizerBuilder<P>) delegate;        
    }
    
    public TextSymbolizerBuilder<P> text(){
        if( delegate == null || !(delegate instanceof TextSymbolizerBuilder)){
            delegate = new TextSymbolizerBuilder<P>(parent);
        }
        return (TextSymbolizerBuilder<P>) delegate;        
    }
    
    public RasterSymbolizerBuilder<P> raster(){
        if( delegate == null || !(delegate instanceof RasterSymbolizerBuilder)){
            delegate = new RasterSymbolizerBuilder<P>(parent);
        }
        return (RasterSymbolizerBuilder<P>) delegate;        
    }
    
    public ExtensionSymbolizerBuilder<P> vendor(){
        if( delegate == null || !(delegate instanceof ExtensionSymbolizerBuilder)){
            delegate = new ExtensionSymbolizerBuilder<P>(parent);
        }
        return (ExtensionSymbolizerBuilder<P>) delegate;        
    }
    
    
    public SymbolizerBuilder<P> unset() {
        delegate = null;
        return this;
    }

    @SuppressWarnings("unchecked")
    public SymbolizerBuilder<P> reset() {
        delegate = new PointSymbolizerBuilder(this).reset();
        return this;
    }

    @SuppressWarnings("unchecked")
    public SymbolizerBuilder<P> reset(Symbolizer symbolizer) {
        if( symbolizer == null ){
            return unset();
        }
        if( symbolizer instanceof PointSymbolizer){
            delegate = new PointSymbolizerBuilder(this).reset((PointSymbolizer)symbolizer);       
        }
        else if( symbolizer instanceof LineSymbolizer){
            delegate = new LineSymbolizerBuilder(this).reset((LineSymbolizer)symbolizer);       
        }
        else if( symbolizer instanceof PolygonSymbolizer){
            delegate = new PolygonSymbolizerBuilder(this).reset((PolygonSymbolizer)symbolizer);       
        }
        else if( symbolizer instanceof TextSymbolizer){
            delegate = new TextSymbolizerBuilder(this).reset((TextSymbolizer)symbolizer);       
        }
        else if( symbolizer instanceof RasterSymbolizer){
            delegate = new RasterSymbolizerBuilder(this).reset((RasterSymbolizer)symbolizer);       
        }
        else if( symbolizer instanceof ExtensionSymbolizer){
            delegate = new ExtensionSymbolizerBuilder(this).reset((ExtensionSymbolizer)symbolizer);       
        }
        return this;
    }

    public Symbolizer build() {
        if(delegate == null){
            return null;
        }
        Symbolizer symbolizer = delegate.build();
        if( parent == null ) reset();
        return symbolizer;        
    }
}
