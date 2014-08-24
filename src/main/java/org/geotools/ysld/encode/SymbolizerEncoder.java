package org.geotools.ysld.encode;

import org.geotools.styling.Symbolizer;

import java.util.Map;

public abstract class SymbolizerEncoder<S extends Symbolizer> extends YsldEncodeHandler<S> {

    SymbolizerEncoder(S sym) {
        super(sym);
    }

    @Override
    protected void encode(S sym) {
        put("geometry", sym.getGeometry());
        put("uom", sym.getUnitOfMeasure());
        if (!sym.getOptions().isEmpty()) {
            push("options");
            for (Map.Entry<String,String> kv : sym.getOptions().entrySet()) {
                put(kv.getKey(), kv.getValue());
            }
        }
    }
}
