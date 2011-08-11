package org.geotools.styling.builder;

import java.util.List;

import org.geotools.styling.GraphicLegend;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

public class GraphicLegendBuilder extends AbstractStyleBuilder<GraphicLegend> {
    private List<GraphicalSymbol> symbols;

    private Expression opacity;

    private Expression size;

    private Expression rotation;

    private AnchorPointBuilder anchorPoint = new AnchorPointBuilder(this).unset();

    private DisplacementBuilder displacement = new DisplacementBuilder(this).unset();

    public GraphicLegendBuilder() {
        this(null);
    }

    public GraphicLegendBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public GraphicLegend build() {
        if (unset) {
            return null;
        }
        GraphicLegend graphic = sf.graphicLegend(symbols, opacity, size, rotation,
                anchorPoint.build(), displacement.build());
        return graphic;
    }

    public AnchorPointBuilder anchor() {
        unset = false;
        return anchorPoint;
    }

    public DisplacementBuilder displacement() {
        unset = false;
        return displacement;
    }

    public GraphicLegendBuilder opacity(Expression opacity) {
        this.opacity = opacity;
        return this;
    }

    public GraphicLegendBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public GraphicLegendBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public GraphicLegendBuilder size(Expression size) {
        this.size = size;
        return this;
    }

    public GraphicLegendBuilder size(double size) {
        return size(literal(size));
    }

    public GraphicLegendBuilder size(String cqlExpression) {
        return size(cqlExpression(cqlExpression));
    }

    public GraphicLegendBuilder rotation(Expression rotation) {
        this.rotation = rotation;
        return this;
    }

    public GraphicLegendBuilder rotation(double rotation) {
        return rotation(literal(rotation));
    }

    public GraphicLegendBuilder rotation(String cqlExpression) {
        return rotation(cqlExpression(cqlExpression));
    }

    public GraphicLegendBuilder reset() {
        opacity = literal(1);
        size = literal(16); // TODO: check what the actual default size is
        rotation = literal(0);
        anchorPoint.reset();
        displacement.reset();
        unset = false;
        return this;
    }

    public GraphicLegendBuilder reset(org.opengis.style.GraphicLegend graphic) {
        if (graphic == null) {
            return unset();
        }
        opacity = graphic.getOpacity();
        size = graphic.getSize();
        rotation = graphic.getRotation();
        anchorPoint.reset(graphic.getAnchorPoint());
        displacement.reset(graphic.getDisplacement());
        unset = false;
        return this;
    }

    public GraphicLegendBuilder unset() {
        return (GraphicLegendBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().legend().init(this);
    }

    @Override
    public GraphicLegendBuilder reset(GraphicLegend original) {
        reset((org.opengis.style.GraphicLegend) original);
        return this;
    }

}
