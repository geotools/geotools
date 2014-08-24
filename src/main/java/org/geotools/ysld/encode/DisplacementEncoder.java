package org.geotools.ysld.encode;

import org.geotools.styling.Displacement;

public class DisplacementEncoder extends YsldEncodeHandler<Displacement> {

    DisplacementEncoder(Displacement displace) {
        super(displace);
    }

    @Override
    protected void encode(Displacement displace) {
        if (nullIf(displace.getDisplacementX(), 0) == null && nullIf(displace.getDisplacementY(),0) == null) {
            return;
        }
        put("displacement", displace.getDisplacementX(), displace.getDisplacementY());
    }
}
