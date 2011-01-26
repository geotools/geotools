package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ImageOutline;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;

public class ImageOutlineBuilder<P> implements Builder<ImageOutline> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private Builder<Symbolizer> symbolizer;

    public ImageOutlineBuilder() {
        parent = null;
        reset();
    }

    public ImageOutlineBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public ImageOutline build() {
        if (symbolizer == null) {
            return null;
        }
        ImageOutline outline = sf.createImageOutline(symbolizer.build());
        return outline;
    }

    public P end() {
        return parent;
    }

    public ImageOutlineBuilder<P> reset() {
        symbolizer = new LineSymbolizerBuilder(this);

        return this;
    }

    @SuppressWarnings("unchecked")
    public ImageOutlineBuilder<P> reset(ImageOutline outline) {
        if (outline == null) {
            return reset();
        }
        symbolizer = null;
        if (outline.getSymbolizer() instanceof LineSymbolizer) {
            LineSymbolizer lineSymbolizer = (LineSymbolizer) outline.getSymbolizer();
            symbolizer = new LineSymbolizerBuilder(this).reset(lineSymbolizer);
        }
        if (outline.getSymbolizer() instanceof PolygonSymbolizer) {
            PolygonSymbolizer polySymbolizer = (PolygonSymbolizer) outline.getSymbolizer();
            symbolizer = new PolygonSymbolizerBuilder(this).reset(polySymbolizer);
        }
        return this;
    }

    public ImageOutlineBuilder<P> unset() {
        symbolizer = null;
        return this;
    }

}