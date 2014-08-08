package org.geotools.ysld.encode;

import org.geotools.styling.AnchorPoint;

public class AnchorPointEncoder extends Encoder<AnchorPoint> {

    AnchorPointEncoder(AnchorPoint anchor) {
        super(anchor);
    }

    @Override
    protected void encode(AnchorPoint anchor) {
        if (nullIf(anchor.getAnchorPointX(), 0) == null && nullIf(anchor.getAnchorPointY(),0.5) == null) {
            return;
        }
        put("anchor", anchor.getAnchorPointX(), anchor.getAnchorPointY());
    }
}
