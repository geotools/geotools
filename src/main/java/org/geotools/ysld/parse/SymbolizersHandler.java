package org.geotools.ysld.parse;

import org.geotools.styling.Rule;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public class SymbolizersHandler extends YsldParseHandler {

    Rule rule;

    public SymbolizersHandler(Rule rule, Factory factory) {
        super(factory);
        this.rule = rule;
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("point".equals(val)) {
            context.push(new PointHandler(rule, factory));
        }
        else if ("line".equals(val)) {
            context.push(new LineHandler(rule, factory));
        }
        else if ("polygon".equals(val)) {
            context.push(new PolygonHandler(rule, factory));
        }
        else if ("text".equals(val)) {
            context.push(new TextHandler(rule, factory));
        }
        else if ("raster".equals(val)) {
            context.push(new RasterHandler(rule, factory));
        }
    }

    @Override
    public void endSequence(SequenceEndEvent evt, YamlParseContext context) {
        context.pop();
    }
}
