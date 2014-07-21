package org.geotools.ysld.parse;

import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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

            context.push(fs, "transform", new TransformHandler(featureStyle, factory));
            context.push(fs, "rules", new RuleHandler(featureStyle, factory));
        }
    }
}
