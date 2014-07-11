package org.geotools.ysld.encode;

import org.geotools.styling.*;

public class SymbolizerEncoder extends Encoder<Symbolizer> {

    public SymbolizerEncoder(Rule rule) {
        super(rule.symbolizers().iterator());
    }

    @Override
    protected void encode(Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            encode((PointSymbolizer)sym);
        }
        else if (sym instanceof LineSymbolizer) {
            encode((LineSymbolizer)sym);
        }
        else if (sym instanceof PolygonSymbolizer) {
            encode((PolygonSymbolizer)sym);
        }
        else if (sym instanceof TextSymbolizer) {
            encode((TextSymbolizer)sym);
        }
        else if (sym instanceof RasterSymbolizer) {
            encode((RasterSymbolizer)sym);
        }
    }

    SymbolizerEncoder encode(PointSymbolizer sym) {
        push("point");
        encode(sym.getGraphic());
        return this;
    }

    SymbolizerEncoder encode(Graphic g) {
        encode("anchor", g.getAnchorPoint());
        encode("displace", g.getDisplacement());
        put("gap", nullIf(g.getGap(), 0d), nullIf(g.getInitialGap(), 0d));
        put("opacity", nullIf(g.getOpacity(), 1));
        put("size", g.getSize());
        put("rotate", nullIf(g.getRotation(), 0d));
        put("symbols", new SymbolsEncoder(g));
        return this;
    }

    SymbolizerEncoder encode(LineSymbolizer sym) {
        return this;
    }

    SymbolizerEncoder encode(PolygonSymbolizer sym) {
        return this;
    }

    SymbolizerEncoder encode(TextSymbolizer sym) {
        return this;
    }

    SymbolizerEncoder encode(RasterSymbolizer sym) {
        return this;
    }

    SymbolizerEncoder encode(String key, AnchorPoint anchor) {
        if (anchor != null) {
            put(key, nullIf(anchor.getAnchorPointX(), 0d), nullIf(anchor.getAnchorPointY(), 0d));
        }
        return this;
    }

    SymbolizerEncoder encode(String key, Displacement displace) {
        if (displace != null) {
            put(key, nullIf(displace.getDisplacementX(),0d), nullIf(displace.getDisplacementY(),0d));
        }
        return this;
    }

}
