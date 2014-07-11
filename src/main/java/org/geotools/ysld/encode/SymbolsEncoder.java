package org.geotools.ysld.encode;

import org.geotools.styling.*;
import org.opengis.style.GraphicalSymbol;

public class SymbolsEncoder extends Encoder<GraphicalSymbol>{

    public SymbolsEncoder(Graphic g) {
        super(g.graphicalSymbols().iterator());
    }

    @Override
    protected void encode(GraphicalSymbol symbol) {
        if (symbol instanceof Mark) {
            push("mark");
            encode((Mark) symbol);
        }
        else if (symbol instanceof ExternalGraphic) {

        }
    }

    SymbolsEncoder encode(Mark mark) {
        put("name", mark.getWellKnownName());
        encode("stroke", mark.getStroke());
        encode("fill", mark.getFill());
        //url:
        //inline:
        return this;
    }

    SymbolsEncoder encode(String key, Stroke stroke) {
        if (stroke != null) {
            push(key);
            put("color", stroke.getColor());
            put("width", stroke.getColor());
            put("opacity", nullIf(stroke.getColor(), 1d));
            pop();
//            join:
//            cap:
//            dash:
//            dash-offset:
//            graphic-fill: ?
//            graphic-stroke: ?
        }
        return this;
    }

    SymbolsEncoder encode(String key, Fill fill) {
        if (fill != null) {
            push(key);
            put("color", fill.getColor());
            put("opacity", nullIf(fill.getOpacity(), 1d));
            //graphic
            pop();
        }
        return this;
    }

}
