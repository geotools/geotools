package org.geotools.styling.builder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Symbol;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

/**
 * 
 *
 * @source $URL$
 */
public class GraphicBuilder extends AbstractStyleBuilder<org.opengis.style.Graphic> {
    List<Builder<? extends Symbol>> symbols = new ArrayList<Builder<? extends Symbol>>();

    Expression opacity;

    Expression size;

    Expression rotation;

    private AnchorPointBuilder anchor = new AnchorPointBuilder(this);

    private DisplacementBuilder displacement = new DisplacementBuilder(this);

    public GraphicBuilder() {
        this(null);
    }

    GraphicBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public GraphicBuilder opacity(Expression size) {
        unset = false;
        this.size = size;
        return this;
    }

    public GraphicBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public GraphicBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public GraphicBuilder size(Expression size) {
        unset = false;
        this.size = size;
        return this;
    }

    public GraphicBuilder size(double size) {
        return size(literal(size));
    }

    public GraphicBuilder size(String cqlExpression) {
        return size(cqlExpression(cqlExpression));
    }

    public GraphicBuilder rotation(Expression rotation) {
        unset = false;
        this.rotation = rotation;
        return this;
    }

    public GraphicBuilder rotation(double rotation) {
        return rotation(literal(rotation));
    }

    public GraphicBuilder rotation(String cqlExpression) {
        return rotation(cqlExpression(cqlExpression));
    }

    public ExternalGraphicBuilder externalGraphic() {
        unset = false;
        ExternalGraphicBuilder builder = new ExternalGraphicBuilder(this);
        symbols.add(builder);
        return builder;
    }

    public GraphicBuilder externalGraphic(URL onlineResource, String format) {
        unset = false;
        ExternalGraphicBuilder builder = externalGraphic();
        try {
            builder.format(format).resource(new OnLineResourceImpl(onlineResource.toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to build URI from URL", e);
        }
        return this;
    }

    public GraphicBuilder externalGraphic(String onlineResource, String format) {
        unset = false;
        ExternalGraphicBuilder builder = externalGraphic();
        try {
            builder.format(format).resource(new OnLineResourceImpl(new URI(onlineResource)));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid online resource", e);
        }
        return this;
    }

    public MarkBuilder mark() {
        unset = false;
        MarkBuilder builder = new MarkBuilder(this);
        symbols.add(builder);
        return builder;
    }

    public Graphic build() {
        if (unset) {
            return null;
        }
        if(symbols.size() == 0) {
            // add the default mark
            mark();
        }
        List<GraphicalSymbol> list = new ArrayList<GraphicalSymbol>();
        for (Builder<? extends Symbol> symbol : symbols) {
            list.add(symbol.build());
        }
        Graphic g = sf.graphic(list, opacity, size, rotation, anchor.build(), displacement.build());
        
        if (parent == null) {
            reset();
        }
        return g;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().graphic().init(this);
    }

    public GraphicBuilder unset() {
        return (GraphicBuilder) super.unset();
    }

    public GraphicBuilder reset() {
        unset = false;
        symbols.clear();
        opacity = literal(1.0);
        size = literal(16);
        rotation = literal(0);
        displacement.reset();
        anchor.reset();
        return this;
    }

    public GraphicBuilder reset(org.opengis.style.Graphic graphic) {
        if (graphic == null || graphic.graphicalSymbols().size() == 0) {
            return unset();
        }
        unset = false;
        symbols.clear();
        for (GraphicalSymbol graphicalSymbol : graphic.graphicalSymbols()) {
            if (graphicalSymbol instanceof Symbol) {
                Symbol symbol = (Symbol) graphicalSymbol;
                Builder<? extends Symbol> builder;
                if (symbol instanceof Mark) {
                    builder = new MarkBuilder(this).reset((Mark) symbol);
                } else if (symbol instanceof ExternalGraphic) {
                    builder = new ExternalGraphicBuilder(this).reset((ExternalGraphic) symbol);
                } else {
                    throw new IllegalArgumentException("Unrecognized symbol type: "
                            + symbol.getClass());
                }
                if (builder != null) {
                    symbols.add(builder);
                } else {
                    throw new RuntimeException("Failed to clone a builder for symbol: " + symbol);
                }
            }
        }
        opacity = graphic.getOpacity();
        size = graphic.getSize();
        rotation = graphic.getRotation();
        displacement.reset(graphic.getDisplacement());
        anchor.reset(graphic.getAnchorPoint());
        return this;
    }

}
