package org.geotools.ysld.encode;

import org.geotools.styling.Graphic;

public class GraphicEncoder extends YsldEncodeHandler<Graphic> {

    GraphicEncoder(Graphic g) {
        super(g);
    }

    @Override
    protected void encode(Graphic g) {
        inline(new AnchorPointEncoder(g.getAnchorPoint()));
        inline(new DisplacementEncoder(g.getDisplacement()));
        put("gap", nullIf(g.getGap(), 0d), nullIf(g.getInitialGap(), 0d));
        put("opacity", nullIf(g.getOpacity(), 1));
        put("size", g.getSize());
        put("rotation", nullIf(g.getRotation(), 0d));
        put("symbols", new SymbolsEncoder(g));
    }
}
