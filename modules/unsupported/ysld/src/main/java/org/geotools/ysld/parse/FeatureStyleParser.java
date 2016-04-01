package org.geotools.ysld.parse;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

public class FeatureStyleParser extends YsldParseHandler {

    Style style;

    FeatureStyleParser(Style style, Factory factory) {
        super(factory);
        this.style = style;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj.seq();
        for (YamlObject<?> o : seq) {
            YamlMap fs = o.map();

            FeatureTypeStyle featureStyle = factory.style.createFeatureTypeStyle();
            style.featureTypeStyles().add(featureStyle);

            featureStyle.setName(fs.str("name"));
            if (fs.has("title")) {
                featureStyle.getDescription().setTitle(fs.str("title"));
            }
            if (fs.has("abstract")) {
                featureStyle.getDescription().setAbstract(fs.str("abstract"));
            }
            
            featureStyle.getOptions().putAll(Util.vendorOptions(fs));

            context.push(fs, "transform", new TransformHandler(featureStyle, factory));
            context.push(fs, "rules", new RuleParser(featureStyle, factory));
        }
    }
}
