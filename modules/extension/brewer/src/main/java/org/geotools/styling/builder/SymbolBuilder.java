package org.geotools.styling.builder;

import java.awt.Color;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ExpressionBuilder;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbol;
import org.opengis.filter.expression.Expression;

public class SymbolBuilder<P> implements Builder<Symbol> {
    P parent;
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    Builder<Symbol> delegate; // Mark or ExternalGraphic
    private boolean unset;

    public SymbolBuilder() {
        this( null );
    }
    public SymbolBuilder(P parent) {
        this.parent = parent;
        reset();
    }
    public SymbolBuilder unset() {
        reset();
        unset = true;
        return this;
    }
    MarkBuilder<P> mark(){
        if( delegate != null && delegate instanceof MarkBuilder){
            return (MarkBuilder) delegate;
        }
        delegate = new MarkBuilder();
        unset = false;
        return (MarkBuilder) delegate;
    }
    ExternalGraphicBuilder<P> external(){
        if( delegate != null && delegate instanceof ExternalGraphicBuilder){
            return (ExternalGraphicBuilder) delegate;
        }
        delegate = new ExternalGraphicBuilder(parent);
        unset = false;
        return (ExternalGraphicBuilder) delegate;
    }
    /**
     * Reset stroke to default values.
     */
    public SymbolBuilder reset() {
        unset = false;
        return this;
    }

    /**
     * Reset builder to provided original stroke.
     * 
     * @param stroke
     */
    public SymbolBuilder reset(Symbol symbol) {
        unset = false;
        return this;
    }
    
    public Symbol build() {
        if (unset) {
            return null;
        }
        Symbol symbol = delegate.build();
        if( parent == null ) unset();
        return symbol;
    }
}
