package org.geotools.ysld.encode;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

public class SymbolizersEncoder extends YsldEncodeHandler<Symbolizer> {

    public SymbolizersEncoder(Rule rule) {
        super(rule.symbolizers().iterator());
    }

    @Override
    protected void encode(Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            push("point").inline(new PointSymblolizerEncoder((PointSymbolizer)sym));
        }
        else if (sym instanceof LineSymbolizer) {
            push("line").inline(new LineSymbolizerEncoder((LineSymbolizer) sym));
        }
        else if (sym instanceof PolygonSymbolizer) {
            push("polygon").inline(new PolygonSymbolizerEncoder((PolygonSymbolizer) sym));
        }
        else if (sym instanceof TextSymbolizer) {
            push("text").inline(new TextSymbolizerEncoder((TextSymbolizer)sym));
        }
        else if (sym instanceof RasterSymbolizer) {
            push("raster").inline(new RasterSymbolizerEncoder((RasterSymbolizer) sym));
        }
    }


    SymbolizersEncoder encode(LineSymbolizer sym) {
        return this;
    }

    SymbolizersEncoder encode(PolygonSymbolizer sym) {
        return this;
    }

    SymbolizersEncoder encode(RasterSymbolizer sym) {
        return this;
    }

}
