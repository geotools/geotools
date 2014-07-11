package org.geotools.ysld.parse;

import org.geotools.styling.Rule;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

import java.util.Deque;

public class SymbolizersHandler extends YsldParseHandler {

    Rule rule;

    public SymbolizersHandler(Rule rule, Factory factory) {
        super(factory);
        this.rule = rule;
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        String val = evt.getValue();
        if ("point".equals(val)) {
            handlers.push(new PointHandler(rule, factory));
        }
        else if ("line".equals(val)) {
            handlers.push(new LineHandler(rule, factory));
        }
        else if ("polygon".equals(val)) {
            handlers.push(new PolygonHandler(rule, factory));
        }
        else if ("text".equals(val)) {
            handlers.push(new TextHandler(rule, factory));
        }
        else if ("raster".equals(val)) {
            handlers.push(new RasterHandler(rule, factory));
        }
    }

    @Override
    public void endSequence(SequenceEndEvent evt, Deque<YamlParseHandler> handlers) {
        handlers.pop();
    }
}
