package org.geotools.styling.builder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.Graphic;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbol;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

public class GraphicBuilder<P> implements Builder<org.opengis.style.Graphic> {
    boolean unset = false;

    P parent;

    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    List<SymbolBuilder<GraphicBuilder<P>>> symbols = new ArrayList<SymbolBuilder<GraphicBuilder<P>>>();

    ChildExpressionBuilder<GraphicBuilder<P>> opacity = new ChildExpressionBuilder<GraphicBuilder<P>>(
            this);

    ChildExpressionBuilder<GraphicBuilder<P>> size = new ChildExpressionBuilder<GraphicBuilder<P>>(
            this);

    ChildExpressionBuilder<GraphicBuilder<P>> rotation = new ChildExpressionBuilder<GraphicBuilder<P>>(
            this);

    private AnchorPointBuilder<GraphicBuilder<P>> anchor = new AnchorPointBuilder<GraphicBuilder<P>>(
            this);

    private DisplacementBuilder<GraphicBuilder<P>> displacement = new DisplacementBuilder<GraphicBuilder<P>>(
            this);

    public GraphicBuilder() {
        this.parent = null;
        reset();
    }

    public GraphicBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public GraphicBuilder<P> opacity(Expression opacity) {
        this.opacity.reset(opacity);
        return this;
    }

    public GraphicBuilder<P> size(Expression size) {
        this.size.reset(size);
        return this;
    }

    public GraphicBuilder<P> rotation(Expression rotation) {
        this.rotation.reset(rotation);
        return this;
    }

    public ExternalGraphicBuilder<GraphicBuilder<P>> externalGraphic() {
        SymbolBuilder<GraphicBuilder<P>> symbolBuilder = new SymbolBuilder<GraphicBuilder<P>>(this);
        symbols.add( symbolBuilder );
        
        return symbolBuilder.external();
    }
    
    public ExternalGraphicBuilder<GraphicBuilder<P>> externalGraphic(URL onlineResource, String format) {
        SymbolBuilder<GraphicBuilder<P>> symbolBuilder = new SymbolBuilder<GraphicBuilder<P>>(this);
        symbols.add( symbolBuilder );
        ExternalGraphicBuilder<GraphicBuilder<P>> external = symbolBuilder.external().format(format);
        try {
            external.resource( new OnLineResourceImpl( onlineResource.toURI() ) );
        } catch (URISyntaxException e) {
        }        
        return external;
    }
    public GraphicBuilder<P> externalGraphic(String onlineResource, String format) {
        SymbolBuilder<GraphicBuilder<P>> symbolBuilder = new SymbolBuilder<GraphicBuilder<P>>(this);
        symbols.add( symbolBuilder );
        ExternalGraphicBuilder<GraphicBuilder<P>> external = symbolBuilder.external().format(format);
        try {
            external.resource( new OnLineResourceImpl( new URI( onlineResource) ));
        } catch (URISyntaxException e) {
        }
        return this;
    }

    public MarkBuilder<GraphicBuilder<P>> mark() {
        SymbolBuilder<GraphicBuilder<P>> symbolBuilder = new SymbolBuilder<GraphicBuilder<P>>(this);
        symbols.add( symbolBuilder );
        return symbolBuilder.mark();
    }
    public GraphicBuilder<P> mark(String wellKnownName ) {
        SymbolBuilder<GraphicBuilder<P>> symbolBuilder = new SymbolBuilder<GraphicBuilder<P>>(this);
        symbols.add( symbolBuilder );
        symbolBuilder.mark().wellKnownName().literal(wellKnownName);
        return this;
    }

    public Graphic build() {
        if (unset) {
            return null;
        }
        List<GraphicalSymbol> list = new ArrayList<GraphicalSymbol>();
        for( SymbolBuilder<GraphicBuilder<P>> symbol : symbols ){
            list.add( symbol.build() );
        }        
        Graphic g = sf.graphic(list, opacity.build(), size.build(), rotation.build(), anchor
                .build(), displacement.build());
        
        if( parent == null ) reset();
        return g;
    }

    public GraphicBuilder<P> unset() {
        unset = true;
        symbols.clear();
        opacity.unset();
        size.unset();
        rotation.unset();
        displacement.unset();
        anchor.unset();
        return this;
    }

    public GraphicBuilder<P> reset() {
        unset = false;
        symbols.clear();
        opacity.reset().literal(1.0);
        size.reset().literal(16);
        rotation.reset().literal(0);
        displacement.reset();
        anchor.reset();
        return this;
    }

    @SuppressWarnings("unchecked")
    public GraphicBuilder<P> reset(org.opengis.style.Graphic graphic) {
        if (graphic == null) {
            return unset();
        }
        unset = false;
        symbols.clear();
        for( GraphicalSymbol graphicalSymbol : graphic.graphicalSymbols()){
            if( graphicalSymbol instanceof Symbol){
                Symbol symbol = (Symbol) graphicalSymbol;
                symbols.add(new SymbolBuilder<GraphicBuilder<P>>(this).reset( symbol ));
            }            
        }
        opacity.reset( graphic.getOpacity() );
        size.reset( graphic.getSize() );
        rotation.reset( graphic.getRotation() );
        displacement.reset( graphic.getDisplacement() );
        anchor.reset( graphic.getAnchorPoint() );
        return this;
    }
}
