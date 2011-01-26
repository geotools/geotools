package org.geotools.styling.builder;

import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.GraphicLegend;
import org.geotools.styling.StyleFactory;
import org.opengis.style.GraphicalSymbol;

public class GraphicLegendBuilder<P> implements Builder<GraphicLegend> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private List<GraphicalSymbol> symbols;

    private ChildExpressionBuilder<GraphicLegendBuilder<P>> opacity = new ChildExpressionBuilder<GraphicLegendBuilder<P>>(
            this);

    private ChildExpressionBuilder<GraphicLegendBuilder<P>> size = new ChildExpressionBuilder<GraphicLegendBuilder<P>>(
            this);

    private ChildExpressionBuilder<GraphicLegendBuilder<P>> rotation = new ChildExpressionBuilder<GraphicLegendBuilder<P>>(
            this);

    private AnchorPointBuilder<GraphicLegendBuilder<P>> anchorPoint = new AnchorPointBuilder<GraphicLegendBuilder<P>>(
            this);

    private DisplacementBuilder<GraphicLegendBuilder<P>> displacement = new DisplacementBuilder<GraphicLegendBuilder<P>>(
            this);

    public GraphicLegendBuilder() {
        this(null);
    }

    public GraphicLegendBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public GraphicLegend build() {
        if (unset) {
            return null;
        }
        GraphicLegend graphic = sf.graphicLegend(symbols, opacity.build(), size.build(), rotation
                .build(), anchorPoint.build(), displacement.build());
        return graphic;
    }

    public P end() {
        return parent;
    }

    public GraphicLegendBuilder<P> reset() {
        unset = false;
        return this;
    }

    public GraphicLegendBuilder<P> reset(GraphicLegend graphic) {
        if (graphic == null) {
            return reset();
        }
        unset = false;
        return this;
    }

    public GraphicLegendBuilder<P> unset() {
        unset = true;
        return this;
    }

}
