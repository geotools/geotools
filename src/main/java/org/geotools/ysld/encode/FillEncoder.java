package org.geotools.ysld.encode;

import org.geotools.styling.Fill;

public class FillEncoder extends YsldEncodeHandler<Fill> {

    public FillEncoder(Fill fill) {
        super(fill);
    }

    @Override
    protected void encode(Fill fill) {
        put("fill-color", fill.getColor());
        put("fill-opacity", nullIf(fill.getOpacity(), 1d));
        if (fill.getGraphicFill() != null) {
            push("fill-graphic").inline(new GraphicEncoder(fill.getGraphicFill()));
        }
    }
}
