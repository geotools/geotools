package org.geotools.ysld.encode;

import org.geotools.styling.*;

import java.util.Collections;

public class RootEncoder extends Encoder<StyledLayerDescriptor> {

    RootEncoder(StyledLayerDescriptor sld) {
        super(Collections.singleton(sld).iterator());
    }

    @Override
    protected void encode(StyledLayerDescriptor sld) {
        Style style = SLD.defaultStyle(sld);
        if (style != null) {
            put("name", style.getName());
            put("title", style.getTitle());
            put("abstract", style.getAbstract());
            put("feature-styles", new FeatureStyleEncoder(style));
        }
    }
}
