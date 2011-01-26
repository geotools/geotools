package org.geotools.styling.builder;

import javax.measure.unit.Unit;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ChannelSelection;
import org.opengis.style.ColorMap;
import org.opengis.style.ContrastEnhancement;
import org.opengis.style.Description;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.ShadedRelief;
import org.opengis.style.Symbolizer;

public class RasterSymbolizerBuilder<P> implements Builder<RasterSymbolizer> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<RasterSymbolizerBuilder<P>> x = new ChildExpressionBuilder<RasterSymbolizerBuilder<P>>(
            this);

    private ChildExpressionBuilder<RasterSymbolizerBuilder<P>> y = new ChildExpressionBuilder<RasterSymbolizerBuilder<P>>(
            this);

    boolean unset = true; // current value is null

    private String name;

    private Expression geometry;

    private Description description;

    private Unit<?> unit;

    private Expression opacity;

    private ChannelSelection channelSelection;

    private OverlapBehavior overlapsBehaviour;

    private ColorMap colorMap;

    private ContrastEnhancement contrast;

    private ShadedRelief shaded;

    private Symbolizer outline;

    public RasterSymbolizerBuilder() {
        this( null );
    }

    public RasterSymbolizerBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public RasterSymbolizer build() {
        if (unset) {
            return null;
        }
        RasterSymbolizer symbolizer = sf.rasterSymbolizer(name, geometry, description, unit,
                opacity, channelSelection, overlapsBehaviour, colorMap, contrast, shaded, outline);
        return symbolizer;
    }

    public P end() {
        return parent;
    }

    public RasterSymbolizerBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;
        return this;
    }

    public RasterSymbolizerBuilder<P> reset(RasterSymbolizer symbolizer) {
        if (symbolizer == null) {
            return reset();
        }
        unset = false;
        return this;
    }

    public RasterSymbolizerBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;
        return this;
    }

}
