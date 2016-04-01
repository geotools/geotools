package org.geotools.ysld.encode;

import org.geotools.styling.PointSymbolizer;

public class PointSymblolizerEncoder extends SymbolizerEncoder<PointSymbolizer> {
    PointSymblolizerEncoder(PointSymbolizer sym) {
        super(sym);
    }

    @Override
    protected void encode(PointSymbolizer sym) {
        inline(new GraphicEncoder((((PointSymbolizer)sym).getGraphic())));
        super.encode(sym);
    }
}
