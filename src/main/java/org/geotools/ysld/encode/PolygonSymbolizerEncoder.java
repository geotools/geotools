package org.geotools.ysld.encode;

import org.geotools.styling.PolygonSymbolizer;

public class PolygonSymbolizerEncoder extends SymbolizerEncoder<PolygonSymbolizer> {
    public PolygonSymbolizerEncoder(PolygonSymbolizer sym) {
        super(sym);
    }

    @Override
    protected void encode(PolygonSymbolizer sym) {
        inline(new StrokeEncoder(sym.getStroke()));
        inline(new FillEncoder(sym.getFill()));
        put("offset", sym.getPerpendicularOffset());
        inline(new DisplacementEncoder(sym.getDisplacement()));

        super.encode(sym);
    }
}
