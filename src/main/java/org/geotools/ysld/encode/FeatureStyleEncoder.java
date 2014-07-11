package org.geotools.ysld.encode;


import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;

public class FeatureStyleEncoder extends Encoder<FeatureTypeStyle> {

    public FeatureStyleEncoder(Style style) {
        super(style.featureTypeStyles().iterator());
    }

    @Override
    protected void encode(FeatureTypeStyle featureStyle) {
        put("name", featureStyle.getName());
        put("title", featureStyle.getTitle());
        put("abstract", featureStyle.getAbstract());
        put("rules", new RuleEncoder(featureStyle));
    }
}
