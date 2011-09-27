package org.geotools.styling.builder;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.Builder;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.PolygonSymbolizer;
import org.opengis.style.Symbolizer;

/**
 * 
 *
 * @source $URL$
 */
public class RasterSymbolizerBuilder extends AbstractStyleBuilder<RasterSymbolizer> {
    private String name;

    private Expression geometry;

    private DescriptionBuilder description = new DescriptionBuilder().unset();

    private Unit<?> uom;

    private Expression opacity;

    private ChannelSelectionBuilder channelSelection = new ChannelSelectionBuilder(this).unset();

    private ColorMapBuilder colorMap = new ColorMapBuilder(this).unset();

    private OverlapBehavior overlapsBehaviour;

    private ContrastEnhancementBuilder contrast = new ContrastEnhancementBuilder(this).unset();

    private ShadedReliefBuilder shadedRelief = new ShadedReliefBuilder(this).unset();

    private Builder<? extends Symbolizer> outline;

    public RasterSymbolizerBuilder() {
        this(null);
    }

    public RasterSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public RasterSymbolizerBuilder name(String name) {
        this.name = name;
        unset = false;
        return this;
    }

    public DescriptionBuilder description() {
        unset = false;
        return description;
    }

    public RasterSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        this.unset = false;
        return this;
    }

    public RasterSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public RasterSymbolizerBuilder opacity(Expression opacity) {
        this.opacity = opacity;
        this.unset = false;
        return this;
    }

    public RasterSymbolizerBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public RasterSymbolizerBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public RasterSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public ChannelSelectionBuilder channelSelection() {
        unset = false;
        return channelSelection;
    }

    public ColorMapBuilder colorMap() {
        unset = false;
        return colorMap;
    }

    public ContrastEnhancementBuilder contrastEnhancement() {
        unset = false;
        return contrast;
    }

    public ShadedReliefBuilder shadedRelief() {
        unset = false;
        return shadedRelief;
    }

    public RasterSymbolizerBuilder overlapBehavior(OverlapBehavior behavior) {
        unset = false;
        this.overlapsBehaviour = behavior;
        return this;
    }

    public RasterSymbolizer build() {
        if (unset) {
            return null;
        }
        RasterSymbolizer symbolizer = sf.rasterSymbolizer(name, geometry, description.build(), uom,
                opacity, channelSelection.build(), overlapsBehaviour, colorMap.build(),
                contrast.build(), shadedRelief.build(), outline != null ? outline.build() : null);
        return symbolizer;
    }

    public RasterSymbolizerBuilder reset() {
        opacity = literal(1.0);
        channelSelection.unset();
        contrast.unset();
        unset = false;
        outline = null;
        return this;
    }

    public RasterSymbolizerBuilder reset(RasterSymbolizer symbolizer) {
        if (symbolizer == null) {
            return reset();
        }
        opacity = symbolizer.getOpacity();
        channelSelection.reset(symbolizer.getChannelSelection());
        colorMap.reset(symbolizer.getColorMap());
        contrast.reset(symbolizer.getContrastEnhancement());
        if (symbolizer.getImageOutline() instanceof LineSymbolizer) {
            this.outline = new LineSymbolizerBuilder().reset((LineSymbolizer) symbolizer
                    .getImageOutline());
        } else if (symbolizer.getImageOutline() instanceof PolygonSymbolizer) {
            this.outline = new PolygonSymbolizerBuilder().reset((PolygonSymbolizer) symbolizer
                    .getImageOutline());
        }
        unset = false;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().init(this);
    }

    @Override
    public RasterSymbolizerBuilder unset() {
        return (RasterSymbolizerBuilder) super.unset();
    }

}
