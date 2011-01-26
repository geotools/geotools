package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleFactory;

public class PolygonSymbolizerBuilder<P> implements Builder<PolygonSymbolizer> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    P parent;

    StrokeBuilder<PolygonSymbolizerBuilder<P>> stroke = new StrokeBuilder<PolygonSymbolizerBuilder<P>>();

    FillBuilder<PolygonSymbolizerBuilder<P>> fill = new FillBuilder<PolygonSymbolizerBuilder<P>>();

    String geometry = null;

    boolean unset = false;

    public PolygonSymbolizerBuilder() {
        this(null);
    }

    PolygonSymbolizerBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public P end() {
        return parent;
    }
    
    PolygonSymbolizerBuilder<P> geometry(String geometry) {
        this.geometry = geometry;
        unset = false;
        return this;
    }

    public StrokeBuilder stroke() {
        unset = false;
        return stroke;
    }

    public FillBuilder fill() {
        unset = false;
        return fill;
    }

    public PolygonSymbolizer build() {
        if (unset) {
            return null;
        }
        PolygonSymbolizer ps = sf.createPolygonSymbolizer(stroke.build(), fill.build(), geometry);
        reset();
        return ps;
    }

    public PolygonSymbolizerBuilder<P> reset() {
        stroke.reset(); // TODO: check what default stroke is for Polygon
        fill.reset(); // TODO: check what default fill is for Polygon
        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder<P> reset(PolygonSymbolizer symbolizer) {
        stroke.reset(symbolizer.getStroke());
        fill.reset(symbolizer.getFill());
        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder<P> unset() {
        stroke.unset();
        fill.unset();
        unset = true;
        return this;
    }
}
