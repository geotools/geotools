package org.geotools.ysld.encode;

import org.geotools.styling.LineSymbolizer;

public class LineSymbolizerEncoder extends SymbolizerEncoder<LineSymbolizer> {
    public LineSymbolizerEncoder(LineSymbolizer sym) {
        super(sym);
    }

    @Override
    protected void encode(LineSymbolizer sym) {
        inline(new StrokeEncoder(sym.getStroke()));
        put("offset", sym.getPerpendicularOffset());
        super.encode(sym);
    }
}
