package org.geotools.ysld.parse;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public class FeatureStyleHandler extends YsldParseHandler {

    Style style;

    FeatureStyleHandler(Style style, Factory factory) {
        super(factory);
        this.style = style;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj.seq();
        for (YamlObject o : seq) {
            YamlMap fs = o.map();

            FeatureTypeStyle featureStyle = factory.style.createFeatureTypeStyle();
            style.featureTypeStyles().add(featureStyle);

            featureStyle.setName(fs.str("name"));
            if (fs.has("title")) {
                featureStyle.setTitle(fs.str("title"));
            }
            if (fs.has("abstract")) {
                featureStyle.setAbstract(fs.str("abstract"));
            }

            context.push(fs, "rules", new RuleHandler(featureStyle, factory));
        }
    }
}
