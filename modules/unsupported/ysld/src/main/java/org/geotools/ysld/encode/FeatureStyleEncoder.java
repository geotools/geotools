package org.geotools.ysld.encode;


import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.util.logging.Logging;

import java.util.logging.Logger;

public class FeatureStyleEncoder extends YsldEncodeHandler<FeatureTypeStyle> {

    static Logger LOG = Logging.getLogger(FeatureStyleEncoder.class);

    public FeatureStyleEncoder(Style style) {
        super(style.featureTypeStyles().iterator());
    }

    @Override
    protected void encode(FeatureTypeStyle featureStyle) {
        put("name", featureStyle.getName());
        put("title", featureStyle.getTitle());
        put("abstract", featureStyle.getAbstract());
        if (featureStyle.getTransformation() != null) {
            push("transform").inline(new TransformEncoder(                        featureStyle.getTransformation()));
            pop();
        }
        put("rules", new RuleEncoder(featureStyle));
        vendorOptions(featureStyle.getOptions());
    }
}
