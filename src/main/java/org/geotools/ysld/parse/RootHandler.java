package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class RootHandler extends YsldParseHandler {

    StyledLayerDescriptor sld;
    Style style;

    public RootHandler() {
        super(new Factory());
    }

    @Override
    public void mapping(MappingStartEvent evt, YamlParseContext context) {
        sld = factory.style.createStyledLayerDescriptor();

        NamedLayer layer = factory.style.createNamedLayer();
        sld.layers().add(layer);

        layer.styles().add(style = factory.style.createStyle());
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("name".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    style.setName(value);
                }
            });
        }
        else if ("title".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    style.setTitle(value);
                }
            });
        }
        else if ("abstract".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    style.setAbstract(value);
                }
            });
        }
        else if ("feature-styles".equals(val)) {
            context.push(new FeatureStyleHandler(style, factory));
        }
        else if ("rules".equals(val)) {
            context.push(new RuleHandler(newFeatureTypeStyle(), factory));
        }
        else if ("symbolizers".equals(val)) {
            context.push(new SymbolizersHandler(newRule(), factory));
        }
        else if ("point".equals(val) || "line".equals(val) || "polygon".equals(val) ||
                "text".equals(val) || "raster".equals(val)) {
            context.push(new SymbolizersHandler(newRule(), factory)).pause();
        }
    }

    public FeatureTypeStyle newFeatureTypeStyle() {
        FeatureTypeStyle fts = factory.style.createFeatureTypeStyle();
        style.featureTypeStyles().add(fts);
        return fts;
    }

    public Rule newRule() {
        Rule rule = factory.style.createRule();
        newFeatureTypeStyle().rules().add(rule);
        return rule;
    }

    public StyledLayerDescriptor sld() {
        return sld;
    }
}
